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
    Button btndoimau, btntuychinh, btnnoidung;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100 && resultCode == 100){
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    webView.requestFocus();
                    WebSettings webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    webView.getSettings().setSupportZoom(true);
                 //   Toast.makeText(getApplicationContext(), MainActivity.urlCV, Toast.LENGTH_SHORT).show();
                    String url1 = "https://docs.google.com/gview?embedded=true&url=" + MainActivity.urlCV;
                    webView.loadUrl(url1);
                }
            },3000);
        }
        if(requestCode == 101 && resultCode == 101){
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    webView.requestFocus();
                    WebSettings webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    webView.getSettings().setSupportZoom(true);
                    //   Toast.makeText(getApplicationContext(), MainActivity.urlCV, Toast.LENGTH_SHORT).show();
                    String url1 = "https://docs.google.com/gview?embedded=true&url=" + MainActivity.urlCV;
                    webView.loadUrl(url1);
                }
            },3000);
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
    public void createCV() throws IOException {
        StorageReference reference =  storageReference.child("audit" + ".pdf");
        PdfDocument pdfDocument = new PdfDocument();
        // mo mau
        Paint myPaint = new Paint();
        // gioi thieu
        Paint paint1 = new Paint();
        // Cac tieu de
        Paint titlePaint = new Paint();
        Paint titlePaint2 = new Paint();
        // Noi dung
        Paint contentPaint = new Paint();
        // ky nang
        Paint kynang_paint = new Paint();
        kynang_paint.setStyle(Paint.Style.STROKE);
        kynang_paint.setStrokeWidth(10);
        Paint kynangphu = new Paint();
        kynangphu.setStyle(Paint.Style.STROKE);
        kynangphu.setStrokeWidth(10);
        kynangphu.setColor(Color.YELLOW);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200,2000,1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setColor(Color.rgb(100,100,100));
        canvas.drawRect(0, 0, pageWidth, 300, myPaint);

        paint1.setColor(Color.WHITE);
        paint1.setTextAlign(Paint.Align.LEFT);
        paint1.setTextSize(50);
        canvas.drawText("Nguyễn Chí Tôn", 30, 80, paint1);
        paint1.setTextSize(35);
        canvas.drawText("DBA", 30, 140, paint1);


        paint1.setTextSize(30);
        canvas.drawText("Ký túc xá B, đại học Cần Thơ", 30, 230, paint1);
        canvas.drawText("Batphuongtrinhvoti@gmail.com", 500, 230, paint1);
        canvas.drawText("032323232", pageWidth-230, 230, paint1);
        // muc tieu nghe nghiep
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(35);
        titlePaint.setColor(Color.BLACK);
        canvas.drawText("MỤC TIÊU NGHỀ NGHIỆP", 30, 380, titlePaint);

        contentPaint.setColor(Color.BLACK);
        contentPaint.setTextSize(25);
        canvas.drawText("Trở thành DBA làm việc trong một ngân hàng lớn, lương 1000$/năm", 30, 450, contentPaint);

        // hoc van
        canvas.drawText("HỌC VẤN", 30,  530, titlePaint);
        titlePaint2.setTextSize(30);
        titlePaint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("ĐẠI HỌC CẦN THƠ", 30, 610, titlePaint2);
        canvas.drawText("10/2017 - 10/2021", 30, 660, contentPaint);
        canvas.drawText("CHUYÊN NGÀNH: CÔNG NGHỆ THÔNG TIN", 500, 610, titlePaint2 );
        canvas.drawText("Tốt nghiệp loại giỏi, điểm trung bình 8.0", 500, 660, contentPaint);

        // hoat dong
        canvas.drawText("HOẠT ĐỘNG", 30, 730, titlePaint);
        canvas.drawText("THAM GIA CLUB TIẾNG ANH", 30, 800, titlePaint2);
        canvas.drawText("2020 - 2021", 30, 850, contentPaint);
        canvas.drawText("NGƯỜI THAM GIA", 500,800, titlePaint2);
        canvas.drawText("Nói tiếng anh với người bản xứ",  500, 850, contentPaint);

        // giai thuong
        canvas.drawText("GIẢI THƯỞNG", 30, 920, titlePaint);
        canvas.drawText("10/2017-12/2017", 30, 970, contentPaint);
        canvas.drawText("Học bổng du học Thái Lan", 500, 970, contentPaint);
        // ky nang
        canvas.drawText("KỸ NĂNG", 30, 1050, titlePaint);
        canvas.drawText("Kỹ năng tiếng anh", 30, 1100, contentPaint);
        int width = 300, height = 50;
        canvas.drawLine(30, 1150, 30 + width, 1150, kynang_paint );
        canvas.drawText("Kỹ năng tin học", 30, 1200, contentPaint);
        canvas.drawLine(30, 1250, width-100 + 30, 1250, kynang_paint);
        canvas.drawLine(30 + width - 100, 1250, width + 30, 1250, kynangphu);

        pdfDocument.finishPage(page);
        File file = new File(Environment.getExternalStorageDirectory(), "/a10.pdf");
        pdfDocument.writeTo(new FileOutputStream(file));
        pdfDocument.close();
        reference.putFile(Uri.fromFile(file))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();
                        Pdf pdf = new Pdf(MainActivity.uid,"audit.pdf", uri.toString());
                        MainActivity.mData.child("preview").child("audit").setValue(pdf);
                        Toast.makeText(getApplicationContext(), "upload success", Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.luu:
                String key = MainActivity.mData.child("cv").push().getKey();
                PdfCV pdfCV = new PdfCV(MainActivity.uid,"Ứng tuyển HD bank", MainActivity.urlCV, key);
                MainActivity.mData.child("cv").child(MainActivity.uid).setValue(pdfCV);
                Toast.makeText(getApplicationContext(), "Đã lưu", Toast.LENGTH_SHORT).show();
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
        btntuychinh = (Button) findViewById(R.id.buttontuychinh);
        btnnoidung = (Button) findViewById(R.id.buttonnoidung);



    }
}
