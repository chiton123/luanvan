package com.example.luanvan.ui.recruiter.search_r;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.recruit.ProfileCadidateAdapter;
import com.example.luanvan.ui.Adapter.recruit.ViewCandidateAdapter;
import com.example.luanvan.ui.Model.Applicant;
import com.example.luanvan.ui.Model.Profile;
import com.example.luanvan.ui.Model.UserSearch;
import com.example.luanvan.ui.recruiter.RecruiterActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CandidateViewActivity extends AppCompatActivity {
    Toolbar toolbar;
    WebView webView;
    UserSearch userSearch;
    int cv_id = 0;
    String user_id_f = "";
    int candidate_id = 0;
    String url1 = "https://docs.google.com/gview?embedded=true&url=";
    // name cv
    String name_cv = "";
    String url_cv = "";
    Handler handler;
    RecyclerView recyclerView;
    public static ViewCandidateAdapter viewAdapter;
    ArrayList<Profile> arrayList;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_view);
        anhxa();
        actionBar();


    }
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    private void eventPDF() {
        webView.requestFocus();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);

        MainActivity.mData.child("cv").child(user_id_f).child(String.valueOf(cv_id)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String url = snapshot.child("url").getValue(String.class);
                url1 += url;
                url_cv = snapshot.child("url").getValue(String.class);;
                name_cv = snapshot.child("name").getValue(String.class) + userSearch.getUsername();
              //  Toast.makeText(getApplicationContext(), url + "\n" + name_cv, Toast.LENGTH_SHORT).show();

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
        arrayList.add(new Profile(1, "Xem thông tin liên hệ", R.drawable.eye));
        arrayList.add(new Profile(2, "Email mời ứng tuyển", R.drawable.sendmail));
        arrayList.add(new Profile(3, "Tải CV PDF", R.drawable.download));
        loading();
        // get info
        userSearch = (UserSearch) getIntent().getSerializableExtra("user");
        cv_id = userSearch.getIdcv();
        user_id_f = userSearch.getUser_id_f();
        candidate_id = userSearch.getIduser();
//        Toast.makeText(getApplicationContext(), "id f: " + user_id_f, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), "cvid: " + cv_id, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), "candidate id: " + candidate_id, Toast.LENGTH_SHORT).show();
        eventPDF();


        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setRecyclerView();
                progressDialog.dismiss();
            }
        },3000);





    }
    public void setRecyclerView(){
        viewAdapter = new ViewCandidateAdapter(this, arrayList, this, url_cv, name_cv, userSearch);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(viewAdapter);
    }



}
