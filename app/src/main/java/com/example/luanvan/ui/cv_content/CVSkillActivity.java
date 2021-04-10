package com.example.luanvan.ui.cv_content;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.cv_all.SkillCVAdapter;
import com.example.luanvan.ui.Model.Pdf;
import com.example.luanvan.ui.Model.Skill;
import com.example.luanvan.ui.cv.CVActivity;
import com.example.luanvan.ui.modelCV.SkillCV;
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
import java.util.regex.Pattern;

import static com.example.luanvan.MainActivity.experienceCV;
import static com.example.luanvan.MainActivity.experienceCVS;
import static com.example.luanvan.MainActivity.mAuth;
import static com.example.luanvan.ui.cv.CVActivity.checkExperience;
import static com.example.luanvan.ui.cv.CVActivity.checkSkill;
import static com.example.luanvan.ui.cv.CVActivity.checkStudy;
import static com.example.luanvan.ui.cv.CVActivity.*;

public class CVSkillActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnAdd, btnHuy, btnLuu;
    RecyclerView recyclerView;
    SkillCVAdapter adapter;
    int pageWidth = 1200;
    StorageReference storageReference;
    Handler handler;
    ProgressDialog progressDialog;
    public static int a0 = 400, a1 = 600, a2 = 1400, a3 = 2200;
    public static int x0 = 0, x1 = 0, x2 = 0, x3 = 0;
    // kiem tra xem x1, x2, x3 có nhảy lên bậc nào hay k khi tạo CV
    public static int checkX1 = 0, checkX2 = 0, checkX3 = 0; // chưa sử dụng
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_v_skill);
        anhxa();
        actionBar();
        eventButton();
        storageReference = FirebaseStorage.getInstance().getReference();
        getInfo();

    }
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    private void getData() {
        MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(CVActivity.key).child("skill").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot x : snapshot.getChildren()){
                    SkillCV a = x.getValue(SkillCV.class);
                    MainActivity.skillCVS.add(a);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getInfo() {
        if(MainActivity.checkFirstSkill == 0 && CVActivity.kind == 2){
            getData();
        }

    }
    public void showDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cv_skill);
        dialog.setCancelable(false);
        final EditText editName = (EditText) dialog.findViewById(R.id.name);
        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.rating);
        Button btnLuu = (Button) dialog.findViewById(R.id.luu);
        Button btnHuy = (Button) dialog.findViewById(R.id.huy);
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                float sosao = ratingBar.getRating();
                if(name.equals("") || sosao == 0){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {

                    SkillCV skill = new SkillCV(name, sosao, "temp");
                    MainActivity.skillCVS.add(skill);
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

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200,3000,1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        myPaint.setStyle(Paint.Style.FILL);
        if(MainActivity.color == 1){
            myPaint.setColor(Color.rgb(100,100,100));
        }else if(MainActivity.color == 2) {
            myPaint.setColor(Color.rgb(20,115,160));
        }else {
            myPaint.setColor(Color.rgb(190,55,10));
        }

        canvas.drawRect(0, 0, pageWidth, 300, myPaint);

        paint1.setColor(Color.WHITE);
        paint1.setTextAlign(Paint.Align.LEFT);
        if(a == 1){
            paint1.setTextSize(45);
            canvas.drawText(MainActivity.userCV.getUsername(), 30, 80, paint1);
            paint1.setTextSize(30);
            canvas.drawText(MainActivity.userCV.getPosition(), 30, 140, paint1);

            paint1.setTextSize(30);
            canvas.drawText(MainActivity.userCV.getAddress(), 30, 230, paint1);
            canvas.drawText(MainActivity.userCV.getEmail(), 500, 230, paint1);
            canvas.drawText(MainActivity.userCV.getPhone(), pageWidth-230, 230, paint1);
        }else {
            paint1.setTextSize(45);
            canvas.drawText(MainActivity.userCVDefault.getUsername(), 30, 80, paint1);
            paint1.setTextSize(30);
            canvas.drawText(MainActivity.userCVDefault.getPosition(), 30, 140, paint1);

            paint1.setTextSize(30);
            canvas.drawText(MainActivity.userCVDefault.getAddress(), 30, 230, paint1);
            canvas.drawText(MainActivity.userCVDefault.getEmail(), 500, 230, paint1);
            canvas.drawText(MainActivity.userCVDefault.getPhone()+"", pageWidth-230, 230, paint1);
        }

        // muc tieu nghe nghiep
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(30);
        titlePaint.setColor(Color.BLACK);


        contentPaint.setColor(Color.BLACK);
        contentPaint.setTextSize(30);
        if(checkGoal == 0){
            x0 = a0;
            canvas.drawText("MỤC TIÊU NGHỀ NGHIỆP", 30, x0, titlePaint);
            if(b == 1){
                canvas.drawText(MainActivity.goal, 30, x0 + 70, contentPaint);
            }else {
                canvas.drawText(MainActivity.goalDefault, 30, x0 + 70, contentPaint);
            }
        }

        // hoc van



        if(checkStudy == 0){
            if(x0 == 0){
                x1 = a0;
                checkX1 = 1; // nhảy 1 bậc
            }else {
                x1 = a1;
            }

            canvas.drawText("HỌC VẤN", 30,  x1 - 50, titlePaint);
            titlePaint2.setTextSize(30);
            titlePaint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            if(c == 1){
                for(int i=0; i < MainActivity.studyCVS.size(); i++){
                    if(i < 4){
                        canvas.drawText(MainActivity.studyCVS.get(i).getSchool(), 30, x1 + i*180, titlePaint2);
                        canvas.drawText(MainActivity.studyCVS.get(i).getStart() + " - " + MainActivity.studyCVS.get(i).getEnd(), 30, x1 + 50 + i*180, contentPaint);
                        canvas.drawText("CHUYÊN NGÀNH: " + MainActivity.studyCVS.get(i).getMajor(), 500, x1 + i*180, titlePaint2 );
                        //     canvas.drawText(MainActivity.studyCVS.get(i).getDescription(), 500, x1 + 50 + i*180, contentPaint);
                        TextPaint mTextPaint=new TextPaint();
                        mTextPaint.setTextSize(30);
                        mTextPaint.setColor(Color.BLACK);
                        StaticLayout mTextLayout = new StaticLayout(xuongdong(MainActivity.studyCVS.get(i).getDescription()), mTextPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                        canvas.save();
                        // calculate x and y position where your text will be placed

                        int textX = 500;
                        int textY = x1 + 20+ i*180;

                        canvas.translate(textX, textY);
                        mTextLayout.draw(canvas);
                        canvas.restore();

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
        }


        // kinh nghiem
        if(checkExperience == 0){
            if(x0 != 0){
                if(x1 != 0){
                    x2 = a2;
                }else {
                    x2 = a1;
                    checkX2 = 1;
                }

            }else {
                if(x1 == 0){
                    x2 = a0;
                    checkX2 = 2;
                }else {
                    x2 = a1;
                    checkX2 = 1;
                }
            }

            canvas.drawText("KINH NGHIỆM", 30, x2, titlePaint);
            if(d == 1){
                for(int i=0; i < experienceCVS.size(); i++){
                    if(i < 4){
                        canvas.drawText(experienceCVS.get(i).getStart()+"-"+experienceCVS.get(i).getEnd(), 30, x2 + 50 + i*180, contentPaint);
                        canvas.drawText(experienceCVS.get(i).getCompany(), 500, x2 + 50 + i*180, contentPaint);
                        canvas.drawText(experienceCVS.get(i).getPosition(), 500, x2 + 90 + i*180, contentPaint);
                        TextPaint mTextPaint=new TextPaint();
                        mTextPaint.setTextSize(30);
                        mTextPaint.setColor(Color.BLACK);
                        StaticLayout mTextLayout = new StaticLayout(xuongdong(experienceCVS.get(i).getDescription()), mTextPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                        canvas.save();
                        // calculate x and y position where your text will be placed

                        int textX = 500;
                        int textY = x2 + 100 + i*180;

                        canvas.translate(textX, textY);
                        mTextLayout.draw(canvas);
                        canvas.restore();
                    }else {
                        break;
                    }
                }
            }else {
                canvas.drawText(experienceCV.getStart()+"-"+experienceCV.getEnd(), 30, x2 + 50, contentPaint);
                canvas.drawText(experienceCV.getCompany(), 500, x2 + 50 , contentPaint);
                canvas.drawText(experienceCV.getPosition(), 500, x2 + 90 , contentPaint);
            }
        }


        // ky nang
        if(checkSkill == 0){
            if(x0 != 0){
                if(x1 != 0){
                    if(x2 != 0){
                        x3 = a3;
                    }else {
                        x3 = a2;
                        checkX3 = 1;
                    }
                }else {
                    if(x2 == 0){
                        x3 = a1;
                        checkX3 = 2;
                    }else {
                        x3 = a2;
                        checkX3 = 1;
                    }
                }

            }else {
                if(x1 != 0){
                    if(x2 != 0){
                        x3 = a2;
                        checkX3 = 1;
                    }else {
                        x3 = a1;
                        checkX3 = 2;
                    }

                }else {
                    if(x2 != 0){
                        x3 = a1;
                        checkX3 = 2;
                    }else {
                        x3 = a0;
                        checkX3 = 3;
                    }
                }

            }


            canvas.drawText("KỸ NĂNG", 30, x3, titlePaint);
            int width = 300, height = 50;
            if(e == 1){
                for(int i=0; i < MainActivity.skillCVS.size(); i++){
                    if(i < 4){
                        canvas.drawText(MainActivity.skillCVS.get(i).getName(), 30, x3 + 50 + i*180, contentPaint);
                        float star1 = MainActivity.skillCVS.get(i).getStar()*60;
                        canvas.drawLine(30, x3+100 + i*90, star1+30, x3 + 100 + i*180, kynang_paint);
                        canvas.drawLine(star1 + 30, x3+100 + i*90, width + 30,x3 + 100 + i*180,  kynangphu );
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
                        while (!uriTask.isComplete()) ;
                        Uri uri = uriTask.getResult();
                        MainActivity.urlCV = uri.toString();
                        //     Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                        Pdf pdf = new Pdf(MainActivity.uid, "audit1.pdf", uri.toString());
                        MainActivity.mData.child("preview").child("audit").setValue(pdf);
                        Toast.makeText(getApplicationContext(), "upload success", Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        });

    }
    public String xuongdong(String text){
        String ketqua = "";
        if(text.contains(".")){
            String[] split = text.split(Pattern.quote("."));
            for(String item : split){
                ketqua += "-  " +  item + "\n";
            }
            return ketqua;
        }else {
            return text;
        }
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
                if(MainActivity.skillCVS.size() == 0){
                    Toast.makeText(getApplicationContext(), "Bạn chưa thêm kỹ năng nào", Toast.LENGTH_SHORT).show();
                }else {
                    loading();
                    MainActivity.checkFirstSkill = 1;
                    try {
                        createCV(MainActivity.checkFirstInfo, MainActivity.checkFirstGoal, MainActivity.checkFirstStudy, MainActivity.checkFirstExperience,
                                MainActivity.checkFirstSkill);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    recyclerView.setVisibility(View.INVISIBLE);
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            setResult(104, intent);
                            finish();
                            //Toast.makeText(getApplicationContext(), "" + MainActivity.skillCVS.size(), Toast.LENGTH_SHORT).show();
                        }
                    },4000);
                    //    Toast.makeText(getApplicationContext(), "" + MainActivity.skillCVS.size(), Toast.LENGTH_SHORT).show();
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
        adapter = new SkillCVAdapter(getApplicationContext(), MainActivity.skillCVS, this, 0);
        recyclerView.setAdapter(adapter);



    }
}
