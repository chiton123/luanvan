package com.example.luanvan.ui.cv_content;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Pdf;
import com.example.luanvan.ui.cv.CVActivity;
import com.example.luanvan.ui.modelCV.UserCV;
import com.example.luanvan.ui.modelCV.Info;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import static com.example.luanvan.MainActivity.experienceCV;
import static com.example.luanvan.MainActivity.experienceCVS;
import static com.example.luanvan.ui.cv.CVActivity.*;
public class CVInfoActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editname, editposition, editphone, editemail, editaddress, editgender, editbirthday;
    Button btnluu, btnhuy;
    StorageReference storageReference;
    public static int pageWidth = 1500;
    Handler handler;
    ProgressDialog progressDialog;
    public static int a0 = 350, a1 = 600, a2 = 900, a3 = 1600;
    public static int x0 = 0, x1 = 0, x2 = 0, x3 = 0;
    // kiem tra xem x1, x2, x3 có nhảy lên bậc nào hay k khi tạo CV
    public static int checkX1 = 0, checkX2 = 0, checkX3 = 0; // chưa sử dụng

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
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
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

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1400,2500,1).create();
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
        if(a == 1 || kind == 2){
            paint1.setTextSize(45);
            canvas.drawText(MainActivity.userCV.getUsername(), 30, 80, paint1);
            paint1.setTextSize(30);
            canvas.drawText(MainActivity.userCV.getPosition(), 30, 130, paint1);
            canvas.drawText("Gender: " + MainActivity.userCV.getGender(), 30, 180, paint1);
            canvas.drawText("Phone: " + MainActivity.userCV.getPhone(), 500, 180, paint1);
            canvas.drawText("Birthday: " + MainActivity.userCV.getBirthday(), 900, 180, paint1);
            canvas.drawText( "Email: " +MainActivity.userCV.getEmail(), 30, 230, paint1);
            canvas.drawText("Address: " + MainActivity.userCV.getAddress(), 30, 280, paint1);

        }else {
            paint1.setTextSize(45);
            canvas.drawText(MainActivity.userCVDefault.getUsername(), 30, 80, paint1);
            paint1.setTextSize(30);
            canvas.drawText(MainActivity.userCVDefault.getPosition(), 30, 130, paint1);
            canvas.drawText("Gender: " + MainActivity.userCVDefault.getGender(), 30, 180, paint1);
            canvas.drawText("Phone: " + MainActivity.userCVDefault.getPhone(), 500, 180, paint1);
            canvas.drawText("Birthday: " + MainActivity.userCVDefault.getBirthday(), 900, 180, paint1);
            canvas.drawText( "Email: " +MainActivity.userCVDefault.getEmail(), 30, 230, paint1);
            canvas.drawText("Address: " + MainActivity.userCVDefault.getAddress(), 30, 280, paint1);



        }

        // muc tieu nghe nghiep
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(30);
        titlePaint.setColor(Color.BLACK);


        contentPaint.setColor(Color.BLACK);
        contentPaint.setTextSize(30);
        if(checkGoal == 0){
            x0 = a0;
            canvas.drawText("OBJECTIVES", 30, x0, titlePaint);
            if(b == 1 || kind == 2){
                if(MainActivity.goal.length() <= 90){
                    canvas.drawText(MainActivity.goal, 30, x0 + 20, contentPaint);
                }else {
                    TextPaint mTextPaint=new TextPaint();
                    mTextPaint.setTextSize(30);
                    mTextPaint.setColor(Color.BLACK);
                    StaticLayout mTextLayout = new StaticLayout(xuongdong(MainActivity.goal), mTextPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                    canvas.save();
                    // calculate x and y position where your text will be placed

                    int textX = 30;
                    int textY = x0 + 20;

                    canvas.translate(textX, textY);
                    mTextLayout.draw(canvas);
                    canvas.restore();
                }


            }else {
                canvas.drawText(MainActivity.goalDefault, 30, x0 + 20, contentPaint);
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

            canvas.drawText("STUDY", 30,  x1 - 50, titlePaint);
            titlePaint2.setTextSize(30);
            titlePaint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            if(c == 1 || kind == 2){
                for(int i=0; i < MainActivity.studyCVS.size(); i++){
                    if(i < 1){
                        canvas.drawText(MainActivity.studyCVS.get(i).getSchool(), 30, x1 + i*180, titlePaint2);
                        canvas.drawText(MainActivity.studyCVS.get(i).getStart() + " - " + MainActivity.studyCVS.get(i).getEnd(), 30, x1 + 50 + i*180, contentPaint);
                        canvas.drawText("CHUYÊN NGÀNH: " + MainActivity.studyCVS.get(i).getMajor(), 450, x1 + i*180, titlePaint2 );
                        //     canvas.drawText(MainActivity.studyCVS.get(i).getDescription(), 500, x1 + 50 + i*180, contentPaint);
                        TextPaint mTextPaint=new TextPaint();
                        mTextPaint.setTextSize(30);
                        mTextPaint.setColor(Color.BLACK);
                        StaticLayout mTextLayout = new StaticLayout(xuongdong(MainActivity.studyCVS.get(i).getDescription()), mTextPaint, canvas.getWidth() - 500, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                        canvas.save();
                        // calculate x and y position where your text will be placed

                        int textX = 450;
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
                canvas.drawText("CHUYÊN NGÀNH: " + MainActivity.studyCV.getMajor(), 450, x1 , titlePaint2 );
                canvas.drawText(MainActivity.studyCV.getDescription(), 450, x1 + 50, contentPaint);
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

            canvas.drawText("WORK EXPERIENCE", 30, x2, titlePaint);
            if(d == 1 || kind == 2){
                for(int i=0; i < experienceCVS.size(); i++){
                    if(i < 2){
                        canvas.drawText(experienceCVS.get(i).getStart()+"-"+experienceCVS.get(i).getEnd(), 30, x2 + 50 + i*250, contentPaint);
                        canvas.drawText(experienceCVS.get(i).getCompany(), 450, x2 + 50 + i*250, contentPaint);
                        canvas.drawText(experienceCVS.get(i).getPosition(), 450, x2 + 90 + i*250, contentPaint);
                        TextPaint mTextPaint=new TextPaint();
                        mTextPaint.setTextSize(30);
                        mTextPaint.setColor(Color.BLACK);
                        StaticLayout mTextLayout = new StaticLayout(xuongdong(experienceCVS.get(i).getDescription()), mTextPaint, canvas.getWidth() - 500, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                        canvas.save();
                        // calculate x and y position where your text will be placed

                        int textX = 450;
                        int textY = x2 + 100 + i*250;

                        canvas.translate(textX, textY);
                        mTextLayout.draw(canvas);
                        canvas.restore();
                    }else {
                        break;
                    }
                }
            }else {
                canvas.drawText(experienceCV.getStart()+"-"+experienceCV.getEnd(), 30, x2 + 50, contentPaint);
                canvas.drawText(experienceCV.getCompany(), 450, x2 + 50 , contentPaint);
                canvas.drawText(experienceCV.getPosition(), 450, x2 + 90 , contentPaint);
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


            canvas.drawText("SKILL", 30, x3, titlePaint);
            int width = 300, height = 50;
            if(e == 1 || kind == 2){
                for(int i=0; i < MainActivity.skillCVS.size(); i++){
                    if(i < 4){
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
                    //    Toast.makeText(getApplicationContext(), "upload success", Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        });
    }
    public String xuongdong(String text){
        String text1 = text.replaceAll("\\s\\s+", " ").trim();
        String ketqua = "";
        if(text.contains(".")){
            String[] split = text1.split(Pattern.quote("."));
            for(String item : split){
                ketqua +=  item ;
            }
            return ketqua;
        }else {
            return text;
        }
    }
    // add function
    public void getInfoAdd(){
        if(MainActivity.checkFirstInfo == 0){
            editname.setText(MainActivity.username);
            if(!MainActivity.user.getPosition().equals("")){
                editposition.setText(MainActivity.user.getPosition());
            }


            if(!MainActivity.user.getEmail().equals("")){
                editemail.setText(MainActivity.user.getEmail());
            }

            if(MainActivity.user.getPhone() != 0){
                editphone.setText(MainActivity.user.getPhone() + "");
            }

            if(!MainActivity.user.getAddress().equals("")){
                editaddress.setText(MainActivity.user.getAddress());
            }

            String ngay = MainActivity.user.getBirthday();
            if(!ngay.equals("")){
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = fmt.parse(ngay);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
                editbirthday.setText(fmtOut.format(date));
            }

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
    void getInfoCV(){
        if(MainActivity.checkFirstInfo == 0 && CVActivity.kind == 2){
            final ArrayList<String> list = new ArrayList<>();
            final int[] i = {0};
            MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(CVActivity.key).child("info").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String chuoi = snapshot.getValue(String.class);
                    list.add(chuoi);
                    if(i[0] == 6){
                        editname.setText(list.get(6));
                        editaddress.setText(list.get(0));
                        editposition.setText(list.get(5));
                        editemail.setText(list.get(2));
                        editphone.setText(list.get(4));
                        editgender.setText(list.get(3));
                        editbirthday.setText(list.get(1));
                    }
                    i[0] = i[0] + 1;
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
        }
    }



    private void getInfo() {
        //Toast.makeText(getApplicationContext(), "kind " + CVActivity.kind, Toast.LENGTH_SHORT).show();
        if(CVActivity.kind == 1){
            getInfoAdd();
        }else if(MainActivity.checkFirstInfo == 1){
            getInfoAdd();
        }
        getInfoCV();

    }

    private void eventLuu() {
        btnluu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(editname.getText().equals("") || editposition.getText().equals("") || editphone.getText().equals("") || editemail.getText().equals("")||
                        editaddress.getText().equals("") || editgender.getText().equals("") || editbirthday.getText().equals("")){
                    FancyToast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else {
                    loading();
                    String name = editname.getText().toString();
                    String position = editposition.getText().toString();
                    String phone = editphone.getText().toString();
                    String email = editemail.getText().toString();
                    String address = editaddress.getText().toString();
                    String gender = editgender.getText().toString();
                    String birthday = editbirthday.getText().toString();

                    MainActivity.userCV = new UserCV(name, position, email, phone, address, gender, birthday);
                    MainActivity.checkFirstInfo = 1;

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
                            progressDialog.dismiss();
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
