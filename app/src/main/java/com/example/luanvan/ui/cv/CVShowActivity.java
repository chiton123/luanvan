package com.example.luanvan.ui.cv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class CVShowActivity extends AppCompatActivity {
    Toolbar toolbar;
    WebView webView;
    String url = "";
    String url1 = "https://docs.google.com/gview?embedded=true&url=";
    int kind = 0; // 1: show cv , 2: job apply
    String cv_id = "";
    Handler handler;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_v_show);
        anhxa();
        actionBar();
        eventPDF();




    }

    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    private void eventPDF() {
        loading();
        webView.requestFocus();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        if(kind == 1){
            url = getIntent().getStringExtra("url");
            url1 +=  url;
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(url1);
                    progressDialog.dismiss();
                }
            },1500);

        }else {
            cv_id = getIntent().getStringExtra("cv_id");
            MainActivity.mData.child("cv").child(MainActivity.uid).child(cv_id).addChildEventListener(new ChildEventListener() {
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
                    progressDialog.dismiss();
                }
            },2000);


        }


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
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        webView = (WebView) findViewById(R.id.webview);
        kind = getIntent().getIntExtra("kind", 0);



    }
}
