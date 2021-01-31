package com.example.luanvan.ui.cv_content;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.cv_all.ExperienceCVAdapter;
import com.example.luanvan.ui.Adapter.cv_all.StudyCVAdapter;
import com.example.luanvan.ui.Model.Pdf;
import com.example.luanvan.ui.cv.CVActivity;
import com.example.luanvan.ui.modelCV.ExperienceCV;
import com.example.luanvan.ui.modelCV.StudyCV;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.luanvan.MainActivity.experienceCV;
import static com.example.luanvan.MainActivity.experienceCVS;
import static com.example.luanvan.MainActivity.skillCVS;


public class CVStudyActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnAdd, btnHuy, btnLuu;
    RecyclerView recyclerView;
    StudyCVAdapter adapter;

    int pageWidth = 1200;
    StorageReference storageReference;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_v_study);
        anhxa();
        actionBar();
        eventButton();
        storageReference = FirebaseStorage.getInstance().getReference();
        getInfo();

    }
    private void getData() {
        MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(CVActivity.key).child("study").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot x : snapshot.getChildren()){
                    StudyCV a = x.getValue(StudyCV.class);
                    MainActivity.studyCVS.add(a);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getInfo(){
        if(MainActivity.checkFirstStudy == 0 && CVActivity.kind == 2) {
            getData();
        }

    }
    public void showDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cv_study);
        dialog.setCancelable(false);
        final EditText editschool = (EditText) dialog.findViewById(R.id.school);
        final EditText editmajor = (EditText) dialog.findViewById(R.id.major);
        final EditText editstart = (EditText) dialog.findViewById(R.id.start);
        final EditText editend = (EditText) dialog.findViewById(R.id.end);
        final EditText editdescription = (EditText) dialog.findViewById(R.id.description);
        Button btnLuu = (Button) dialog.findViewById(R.id.luu);
        Button btnHuy = (Button) dialog.findViewById(R.id.huy);
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editschool.getText().equals("") || editmajor.getText().equals("") || editstart.getText().equals("")
                        || editend.getText().equals("") || editdescription.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    String school = editschool.getText().toString();
                    String major = editmajor.getText().toString();
                    String start = editstart.getText().toString();
                    String end = editend.getText().toString();
                    String description = editdescription.getText().toString();
                    StudyCV studyCV = new StudyCV("temp", school, major, start, end, description);
                    MainActivity.studyCVS.add(studyCV);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();

                }


            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
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
            canvas.drawText(MainActivity.goalDefault, 30, 450, contentPaint);
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
    private void eventButton() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(MainActivity.studyCVS.size() == 0){
                    Toast.makeText(getApplicationContext(), "Bạn chưa thêm kinh nghiệm nào", Toast.LENGTH_SHORT).show();
                }else {
                    MainActivity.checkFirstStudy = 1;
                    recyclerView.setVisibility(View.INVISIBLE);
//                    MainActivity.mData.child("cvinfo").child(MainActivity.uid).child("study").removeValue();
//                    for(int i=0; i < MainActivity.studyCVS.size(); i++){
//                        String key = MainActivity.mData.push().getKey();
//                        MainActivity.studyCVS.get(i).setId(key);
//                        MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(String.valueOf(CVActivity.idCV+1)).child("study").push().setValue(MainActivity.studyCVS.get(i));
//
//                    }
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
                            Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            setResult(102, intent);
                            finish();
                        }
                    },4000);
                }


            }
        });


        btnHuy.setOnClickListener(new View.OnClickListener() {
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
        btnAdd = (Button) findViewById(R.id.buttonadd);
        btnHuy = (Button) findViewById(R.id.buttonhuy);
        btnLuu = (Button) findViewById(R.id.buttonluu);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new StudyCVAdapter(getApplicationContext(), MainActivity.studyCVS, this, 0);
        recyclerView.setAdapter(adapter);



    }


}
