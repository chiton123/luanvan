package com.example.luanvan.ui.cv_content;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Pdf;
import com.example.luanvan.ui.modelCV.Info;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CVInfoActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editname, editposition, editphone, editemail, editaddress, editgender, editbirthday;
    Button btnluu, btnhuy;
    TextView txtname, txtposition, txtphone, txtemail;

    StorageReference storageReference;
    int pageWidth = 1200;
    String cv_name = "", cv_phone = "", cv_address = "", cv_email = "", cv_position = "", cv_birthday = "", cv_gender = "";
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_v_info);
        anhxa();
        actionBar();
        eventLuu();
        getInfo();
        // root path
        storageReference = FirebaseStorage.getInstance().getReference();

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createCV() throws IOException {

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
        canvas.drawText(cv_name, 30, 80, paint1);
        paint1.setTextSize(35);
        canvas.drawText(cv_position, 30, 140, paint1);


        paint1.setTextSize(30);
        canvas.drawText(cv_address, 30, 230, paint1);
        canvas.drawText(cv_email, 500, 230, paint1);
        canvas.drawText(cv_phone, pageWidth-230, 230, paint1);
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
        storageReference.child("abc.pdf").putFile(Uri.fromFile(file))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();
                        MainActivity.urlCV = uri.toString();
                   //     Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                        Pdf pdf = new Pdf(MainActivity.uid,"audit1.pdf", uri.toString());
                        MainActivity.mData.child("preview").child("audit").setValue(pdf);
                        Toast.makeText(getApplicationContext(), "upload success", Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        });
    }
    private void getInfo() {
        editname.setText(MainActivity.username);
        editposition.setText(MainActivity.position);
        editemail.setText(MainActivity.user.getEmail());
        editphone.setText(MainActivity.user.getPhone() + "");
        editaddress.setText(MainActivity.user.getAddress());
        String ngay = MainActivity.user.getBirthday();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = fmt.parse(ngay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
        editbirthday.setText(fmtOut.format(date));
        if(MainActivity.user.getGender() == 0){
            editgender.setText("Nam");
        }else {
            editgender.setText("Nữ");
        }

    }

    private void eventLuu() {
        btnluu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(editname.getText().equals("") || editposition.getText().equals("") || editphone.getText().equals("") || editemail.getText().equals("")||
                editaddress.getText().equals("") || editgender.getText().equals("") || editbirthday.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    String name = editname.getText().toString();
                    String position = editposition.getText().toString();
                    String phone = editphone.getText().toString();
                    String email = editemail.getText().toString();
                    String address = editaddress.getText().toString();
                    String gender = editgender.getText().toString();
                    String birthday = editbirthday.getText().toString();
                    cv_name = name;
                    cv_address = address;
                    cv_birthday = birthday;
                    cv_email = email;
                    cv_gender = gender;
                    cv_phone = phone;
                    cv_position = position;
                    Info info = new Info(name, position, phone, email, address, gender, birthday);
                    MainActivity.mData.child("cvinfo").child(MainActivity.uid).child("info").setValue(info);
                    try {
                        createCV();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            setResult(100);
                            finish();
                        }
                    },4000);

                }

            }
        });
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        btnhuy = (Button) findViewById(R.id.buttonhuy);
        btnluu = (Button) findViewById(R.id.buttonluu);
        editname = (EditText) findViewById(R.id.name);
        editaddress = (EditText) findViewById(R.id.address);
        editbirthday = (EditText) findViewById(R.id.birthday);
        editemail = (EditText) findViewById(R.id.email);
        editgender = (EditText) findViewById(R.id.gender);
        editposition = (EditText) findViewById(R.id.position);
        editphone = (EditText) findViewById(R.id.phone);
        txtname = (TextView) findViewById(R.id.txtname);
        txtemail = (TextView) findViewById(R.id.txtemail);
        txtphone = (TextView) findViewById(R.id.txtphone);
        txtposition = (TextView) findViewById(R.id.txtposition);

    }
}
