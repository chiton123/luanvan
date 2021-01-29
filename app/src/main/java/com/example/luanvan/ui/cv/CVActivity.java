package com.example.luanvan.ui.cv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.add_remove.AddAdapter;
import com.example.luanvan.ui.Adapter.add_remove.TitleAdapter;
import com.example.luanvan.ui.Model.Pdf;
import com.example.luanvan.ui.Model.Title;
import com.example.luanvan.ui.modelCV.Info;
import com.example.luanvan.ui.modelCV.PdfCV;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CVActivity extends AppCompatActivity {
    Toolbar toolbar;
    WebView webView;
    Button btndoimau, btnnoidung;
    Dialog dialog;
    ListView listViewThongtinLienHe, listViewRemove, listViewAdd;
    public static TitleAdapter titleAdapterTTLH, titleAdapterRemove;
    public static ArrayList<Title> arrayListTTLH, arrayListRemove, arrayListAdd, arrayListTongHop;
    public static AddAdapter addAdapter;
    StorageReference storageReference;
    ImageView imgCancel;
    String url = "https://firebasestorage.googleapis.com/v0/b/project-7807e.appspot.com/o/default.pdf?alt=media&token=e22cfec0-f4fc-47a8-b65d-84e3d17a9b7a";
    int pageWidth = 1200;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_v);
        anhxa();
        actionBar();
        eventPDF();
        eventButton();
        storageReference = FirebaseStorage.getInstance().getReference();



    }

    private void eventButton() {
        btnnoidung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDialog();
            }
        });

    }
    public void reloadWebview(){
        final String url1 = "https://docs.google.com/gview?embedded=true&url=" + MainActivity.urlCV;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(url1);
            }
        }, 2000);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 100 && resultCode == 100) {
            reloadWebview();
        }
        if (requestCode == 101 && resultCode == 101) {
            reloadWebview();
        }
        if (requestCode == 104 && resultCode == 104) {
            reloadWebview();
        }
        if (requestCode == 103 && resultCode == 103) {
            reloadWebview();
        }
        if (requestCode == 102 && resultCode == 102) {
            reloadWebview();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void eventDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cv);
        dialog.setCancelable(false);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        listViewThongtinLienHe = (ListView) dialog.findViewById(R.id.listviewthongtinlienhe);
        listViewRemove = (ListView) dialog.findViewById(R.id.listviewRemove);
        listViewAdd = (ListView) dialog.findViewById(R.id.listviewAdd);
        imgCancel = (ImageView) dialog.findViewById(R.id.cancel);
        arrayListRemove = new ArrayList<>();
        arrayListTTLH = new ArrayList<>();
        arrayListAdd = new ArrayList<>();
        arrayListTongHop = new ArrayList<>();
        arrayListTongHop.add(new Title(0, "Thông tin liên hệ"));
        arrayListTongHop.add(new Title(1, "Mục tiêu nghề nghiệp"));
        arrayListTongHop.add(new Title(2, "Học vấn"));
        arrayListTongHop.add(new Title(3, "Kinh nghiệm làm việc"));
        arrayListTongHop.add(new Title(4, "Kỹ năng"));
        arrayListTongHop.add(new Title(5, "Hoạt động"));


        arrayListRemove.add(arrayListTongHop.get(0));
        arrayListRemove.add(arrayListTongHop.get(1));
        arrayListRemove.add(arrayListTongHop.get(2));
        arrayListRemove.add(arrayListTongHop.get(3));
        arrayListAdd.add(arrayListTongHop.get(4));
        arrayListAdd.add(arrayListTongHop.get(5));
        titleAdapterTTLH = new TitleAdapter(getApplicationContext(), arrayListTTLH, this, 1);
        titleAdapterRemove = new TitleAdapter(getApplicationContext(), arrayListRemove, this, 0);
        addAdapter = new AddAdapter(getApplicationContext(), arrayListAdd);
        listViewAdd.setAdapter(addAdapter);
        listViewThongtinLienHe.setAdapter(titleAdapterTTLH);
        listViewRemove.setAdapter(titleAdapterRemove);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    private void eventPDF() {
        webView.requestFocus();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        String url1 = "https://docs.google.com/gview?embedded=true&url=" + url;
        webView.loadUrl(url1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cv, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.luu:
                String key = MainActivity.mData.child("cv").child(MainActivity.uid).push().getKey();
                PdfCV pdfCV = new PdfCV(MainActivity.uid,"Ứng tuyển HD bank", MainActivity.urlCV, key);
                MainActivity.mData.child("cv").child(MainActivity.uid).child(MainActivity.uid).setValue(pdfCV);
                Toast.makeText(getApplicationContext(), "Đã lưu", Toast.LENGTH_SHORT).show();
                // lưu id CV bằng số tăng dần, có ví dụ rồi, mỗi user thì có nhiều CV
                break;
            case R.id.huy:

                break;
        }

        return super.onOptionsItemSelected(item);
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
        btndoimau = (Button) findViewById(R.id.buttondoimau);
        btnnoidung = (Button) findViewById(R.id.buttonnoidung);



    }
}
