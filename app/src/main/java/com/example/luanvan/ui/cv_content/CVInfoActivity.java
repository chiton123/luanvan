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
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Pdf;
import com.example.luanvan.ui.modelCV.UserCV;
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

import static com.example.luanvan.MainActivity.experienceCV;
import static com.example.luanvan.MainActivity.experienceCVS;

public class CVInfoActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editname, editposition, editphone, editemail, editaddress, editgender, editbirthday;
    Button btnluu, btnhuy;
    StorageReference storageReference;
    int pageWidth = 1200;
    Handler handler;
    // check xem nếu là tạo cv mới thì lần đầu update thì check = 1, các edittext gán cho giá trị mới cho các lần sau

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
    // x: 1 thì up
    // a: info, 2: goal, 3: study, 4: experience, 5: skill, 1: already check, 0 : not, use default info
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createCV(int a, int b, int c, int d, int e) throws IOException {

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
        if(a == 1){
            paint1.setTextSize(50);
            canvas.drawText(MainActivity.userCV.getUsername(), 30, 80, paint1);
            paint1.setTextSize(35);
            canvas.drawText(MainActivity.userCV.getPosition(), 30, 140, paint1);

            paint1.setTextSize(30);
            canvas.drawText(MainActivity.userCV.getAddress(), 30, 230, paint1);
            canvas.drawText(MainActivity.userCV.getEmail(), 500, 230, paint1);
            canvas.drawText(MainActivity.userCV.getPhone(), pageWidth-230, 230, paint1);
        }else {
            paint1.setTextSize(50);
            canvas.drawText(MainActivity.userCVDefault.getUsername(), 30, 80, paint1);
            paint1.setTextSize(35);
            canvas.drawText(MainActivity.userCVDefault.getPosition(), 30, 140, paint1);

            paint1.setTextSize(30);
            canvas.drawText(MainActivity.userCVDefault.getAddress(), 30, 230, paint1);
            canvas.drawText(MainActivity.userCVDefault.getEmail(), 500, 230, paint1);
            canvas.drawText(MainActivity.userCVDefault.getPhone()+"", pageWidth-230, 230, paint1);
        }

        // muc tieu nghe nghiep
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(35);
        titlePaint.setColor(Color.BLACK);
        canvas.drawText("MỤC TIÊU NGHỀ NGHIỆP", 30, 380, titlePaint);

        contentPaint.setColor(Color.BLACK);
        contentPaint.setTextSize(25);
        if(b == 1){
            canvas.drawText(MainActivity.goal, 30, 450, contentPaint);
        }else {
            canvas.drawText("Trờ thành nhân viên xuất sắc của công ty, cống hiến, tận tụy trong công việc", 30, 450, contentPaint);
        }

        // hoc van
        int x1 = 610, x2 = 920, x3 = 1300;

        canvas.drawText("HỌC VẤN", 30,  530, titlePaint);
        titlePaint2.setTextSize(30);
        titlePaint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        if(c == 1){
            for(int i=0; i < MainActivity.studyCVS.size(); i++){
                if(i < 2){
                    canvas.drawText(MainActivity.studyCVS.get(i).getSchool(), 30, x1 + i*90, titlePaint2);
                    canvas.drawText(MainActivity.studyCVS.get(i).getStart() + " - " + MainActivity.studyCVS.get(i).getEnd(), 30, x1 + 50 + i*90, contentPaint);
                    canvas.drawText("CHUYÊN NGÀNH: " + MainActivity.studyCVS.get(i).getMajor(), 500, x1 + i*90, titlePaint2 );
                    canvas.drawText(MainActivity.studyCVS.get(i).getDescription(), 500, x1 + 50 + i*90, contentPaint);
                }else {
                    break;
                }

            }
        }else {
            canvas.drawText(MainActivity.studyCV.getSchool(), 30, x1, titlePaint2);
            canvas.drawText(MainActivity.studyCV.getStart() + " - " + MainActivity.studyCV.getEnd(), 30, x1 + 50, contentPaint);
            canvas.drawText("CHUYÊN NGÀNH: " + MainActivity.studyCV.getMajor(), 500, x1 , titlePaint2 );
            canvas.drawText(MainActivity.studyCV.getDescription(), 500, x1 + 50, contentPaint);
        }


        // kinh nghiem
        canvas.drawText("KINH NGHIỆM", 30, x2, titlePaint);
        if(d == 1){
            for(int i=0; i < experienceCVS.size(); i++){
                if(i < 2){
                    canvas.drawText(experienceCVS.get(i).getStart()+"-"+experienceCVS.get(i).getEnd(), 30, x2 + 50 + i*90, contentPaint);
                    canvas.drawText(experienceCVS.get(i).getCompany(), 500, x2 + 50 + i*90, contentPaint);
                    canvas.drawText(experienceCVS.get(i).getPosition(), 500, x2 + 90 + i*90, contentPaint);
                }else {
                    break;
                }
            }
        }else {
            canvas.drawText(experienceCV.getStart()+"-"+experienceCV.getEnd(), 30, x2 + 50, contentPaint);
            canvas.drawText(experienceCV.getCompany(), 500, x2 + 50 , contentPaint);
            canvas.drawText(experienceCV.getPosition(), 500, x2 + 90 , contentPaint);
        }


        // ky nang
        canvas.drawText("KỸ NĂNG", 30, x3, titlePaint);
        int width = 300, height = 50;
        if(e == 1){
            for(int i=0; i < MainActivity.skillCVS.size(); i++){
                if(i < 2){
                    canvas.drawText(MainActivity.skillCVS.get(i).getName(), 30, x3 + 50 + i*90, contentPaint);
                    float star1 = MainActivity.skillCVS.get(i).getStar()*60;
                    canvas.drawLine(30, x3+100 + i*90, star1+30, x3 + 100 + i*90, kynang_paint);
                    canvas.drawLine(star1 + 30, x3+100 + i*90, width + 30,x3 + 100 + i*90,  kynangphu );
                }else {
                    break;
                }
            }

        }else {
            canvas.drawText(MainActivity.skillCVArray.get(0).getName(), 30, x3 + 50, contentPaint);
            // 300 : 5 = 60
            float star1 = MainActivity.skillCVArray.get(0).getStar()*60;
            float star2 = MainActivity.skillCVArray.get(1).getStar()*60;
            canvas.drawLine(30, x3+100, star1+30, x3 + 100, kynang_paint);
            canvas.drawLine(star1 + 30, x3+100, width + 30,x3 + 100,  kynangphu );

            canvas.drawText(MainActivity.skillCVArray.get(1).getName(), 30, x3 + 150, contentPaint);
            canvas.drawLine(30, x3+200, star2+30, x3+200, kynang_paint);
            canvas.drawLine(star2+30, x3+200, width + 30, x3+200, kynangphu);

        }

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
        if(MainActivity.checkFirstInfo == 0){
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
        }else {
            editname.setText(MainActivity.userCV.getUsername());
            editaddress.setText(MainActivity.userCV.getAddress());
            editposition.setText(MainActivity.userCV.getPosition());
            editemail.setText(MainActivity.userCV.getEmail());
            editphone.setText(MainActivity.userCV.getPhone());
            editgender.setText(MainActivity.userCV.getGender());
            editbirthday.setText(MainActivity.userCV.getBirthday());
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

                    MainActivity.userCV = new UserCV(name, position, email, phone, address, gender, birthday);
                    MainActivity.checkFirstInfo = 1;
                    Info info = new Info(name, position, phone, email, address, gender, birthday);
                    MainActivity.mData.child("cvinfo").child(MainActivity.uid).child("info").setValue(info);
                    try {
                        createCV(MainActivity.checkFirstInfo, MainActivity.checkFirstGoal, MainActivity.checkFirstStudy, MainActivity.checkFirstExperience,
                                MainActivity.checkFirstSkill);
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

    }
}
