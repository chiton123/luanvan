package com.example.luanvan.ui.recruiter.CVManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.recruit.ProfileCadidateAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.ProfileAdapter;
import com.example.luanvan.ui.Model.Applicant;
import com.example.luanvan.ui.Model.Pdf;
import com.example.luanvan.ui.Model.Profile;
import com.example.luanvan.ui.modelCV.PdfCV;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class CandidateInfoActivity extends AppCompatActivity {
    Toolbar toolbar;
    WebView webView;
    Applicant applicant = new Applicant();
    int cv_id = 0;
    String user_id_f = "";
    String url1 = "https://docs.google.com/gview?embedded=true&url=";
    Handler handler;
    RecyclerView recyclerView;
    public static ProfileCadidateAdapter adapter;
    ArrayList<Profile> arrayList;
    int kind = 0;
    int position = 0; // vị trí của mảng ở cvmanagement


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_info);
        anhxa();
        actionBar();
        eventPDF();



    }


    private void eventPDF() {
        webView.requestFocus();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);

        MainActivity.mData.child("cv").child(user_id_f).child(String.valueOf(cv_id)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String url = snapshot.child("url").getValue(String.class);
                url1 += url;
                //  Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(url1);
            }
        },3000);

    }



    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        webView = (WebView) findViewById(R.id.webview);
        arrayList = new ArrayList<>();
        arrayList.add(new Profile(1, "Đánh giá ứng viên", R.drawable.star));
        arrayList.add(new Profile(2, "Đặt lịch hẹn ứng viên", R.drawable.calendar));
        // get info
        applicant = (Applicant) getIntent().getSerializableExtra("applicant");
        kind = getIntent().getIntExtra("kind", 0);
        position = getIntent().getIntExtra("position", 0);
        cv_id = applicant.getCv_id();
        user_id_f = applicant.getUser_id_f();
        adapter = new ProfileCadidateAdapter(this, arrayList, this, applicant, kind, position);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);



    }
}
