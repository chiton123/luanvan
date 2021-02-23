package com.example.luanvan.ui.recruiter.CVManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Applicant;
import com.example.luanvan.ui.Model.Pdf;
import com.example.luanvan.ui.modelCV.PdfCV;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class CandidateInfoActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtName, txtEmail, txtPhone, txtRound, txtStatus;
    WebView webView;
    Applicant applicant;
    int cv_id = 0;
    String user_id_f = "";
    String url1 = "https://docs.google.com/gview?embedded=true&url=";
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_info);
        anhxa();
        actionBar();
        getInfo();



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

    private void getInfo() {
        applicant = (Applicant) getIntent().getSerializableExtra("applicant");
        txtName.setText(applicant.getUsername());
        txtPhone.setText(applicant.getPhone());
        txtEmail.setText(applicant.getEmail());
        cv_id = applicant.getCv_id();
        user_id_f = applicant.getUser_id_f();
        int status = applicant.getStatus();
        switch (status){
            case 0:
                txtStatus.setText("Chưa đánh giá");
                txtRound.setText("Lọc CV");
                break;
            case 1:
                txtStatus.setText("Đạt yêu cầu");
                txtRound.setText("Lọc CV");
                break;
            case 2:
                txtStatus.setText("Không đạt yêu cầu");
                txtRound.setText("Lọc CV");
                break;

        }
        eventPDF();


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
        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        txtPhone = (TextView) findViewById(R.id.phone);
        txtRound = (TextView) findViewById(R.id.round);
        txtStatus = (TextView) findViewById(R.id.status);
        webView = (WebView) findViewById(R.id.webview);




    }
}
