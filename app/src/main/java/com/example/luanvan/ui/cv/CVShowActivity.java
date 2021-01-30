package com.example.luanvan.ui.cv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.luanvan.R;

public class CVShowActivity extends AppCompatActivity {
    Toolbar toolbar;
    WebView webView;
    String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_v_show);
        anhxa();
        actionBar();
        eventPDF();




    }
    private void eventPDF() {
        url = getIntent().getStringExtra("url");
        webView.requestFocus();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        String url1 = "https://docs.google.com/gview?embedded=true&url=" + url;
        webView.loadUrl(url1);

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



    }
}
