package com.example.luanvan.ui.cv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.add_remove.AddAdapter;
import com.example.luanvan.ui.Adapter.add_remove.TitleAdapter;
import com.example.luanvan.ui.Model.Pdf;
import com.example.luanvan.ui.Model.Title;
import com.example.luanvan.ui.modelCV.ExperienceCV;
import com.example.luanvan.ui.modelCV.Info;
import com.example.luanvan.ui.modelCV.PdfCV;
import com.example.luanvan.ui.modelCV.UserCV;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.luanvan.MainActivity.experienceCV;
import static com.example.luanvan.MainActivity.experienceCVS;

public class CVActivity extends AppCompatActivity {
    Toolbar toolbar;
    WebView webView;
    Button  btnnoidung;
    EditText cvName;
    Dialog dialog;
    ListView listViewThongtinLienHe, listViewRemove, listViewAdd;
    public static TitleAdapter titleAdapterTTLH, titleAdapterRemove;
    public static ArrayList<Title> arrayListTTLH, arrayListRemove, arrayListAdd, arrayListTongHop;
    public static AddAdapter addAdapter;
    StorageReference storageReference;
    ImageView imgCancel;


    // default add
    // url cv theo từng loại
    String urlKind1 = "https://firebasestorage.googleapis.com/v0/b/project-7807e.appspot.com/o/loai1.pdf?alt=media&token=0851ef5c-dc88-483e-a223-11dcceeeef93";
    String urlKind2 = "https://firebasestorage.googleapis.com/v0/b/project-7807e.appspot.com/o/loai2.pdf?alt=media&token=8025c631-ae6f-4d6c-85ac-327adab71386";
    String urlKind3 = "https://firebasestorage.googleapis.com/v0/b/project-7807e.appspot.com/o/loai3.pdf?alt=media&token=988ea675-41df-4298-bfb3-218f7dcedab6";
    // update cv
    String urlX = "";
    String nameUpdate = "";
    // key cua CV
    public static String key = "";
    int pageWidth = 1200;
    int pageHeight = 2000;
    Handler handler;
    public static long idCV = 0;
    // kind: 1 add, kind: 2 update
    public static int kind = 0;
    // kiểm tra thông tin nào có trong CV, k cần phải ghi hết nhé : bắc buộc: info
    public static int checkGoal = 0, checkSkill = 0, checkStudy = 0, checkExperience = 0;
    // layout cho CV
    public static int a0 = 400, a1 = 650, a2 = 950, a3 = 1200;
    public static int x0 = 0, x1 = 0, x2 = 0, x3 = 0;
    // kiem tra xem x1, x2, x3 có nhảy lên bậc nào hay k khi tạo CV
    public static int checkX1 = 0, checkX2 = 0, checkX3 = 0; // chưa sử dụng
    String CVNameToPost = "";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_v);
        anhxa();
        actionBar();
        if(kind == 1){
            getIDCV();
        }

        eventPDF();
        eventButton();
        storageReference = FirebaseStorage.getInstance().getReference();


    }
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    private void getIDCV() {
        MainActivity.mData.child("cv").child(MainActivity.uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idCV = (snapshot.getChildrenCount() + 1)*10 + 1;
                cvName.setText("Ứng tuyển "+ String.valueOf(idCV + 1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        }, 3000);
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
        String url1 = "https://docs.google.com/gview?embedded=true&url=";
        if(kind == 1){
            if(MainActivity.color == 1){
                url1 += urlKind1;
            }else if(MainActivity.color == 2){
                url1 += urlKind2;
            }else {
                url1 += urlKind3;
            }

        }else {
            url1 += urlX;
        }
        webView.loadUrl(url1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cv, menu);
        return super.onCreateOptionsMenu(menu);
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


        contentPaint.setColor(Color.BLACK);
        contentPaint.setTextSize(25);
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
        }


        pdfDocument.finishPage(page);
        File file = new File(Environment.getExternalStorageDirectory(), "/a10.pdf");
        pdfDocument.writeTo(new FileOutputStream(file));
        pdfDocument.close();
        CVNameToPost = cvName.getText().toString();
        // lưu thật sự file pdf, còn CV content như goal, study ... thì chỉ lưu nháp trong file abc.pdf thôi, k dc giống nhe
        storageReference.child(CVNameToPost+".pdf").putFile(Uri.fromFile(file))
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

    // push add
    // push info
    public void pushInfoAdd(){
        if(MainActivity.checkFirstInfo == 1){
            MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(String.valueOf(CVActivity.idCV+1)).child("info").setValue(MainActivity.userCV);
        }else {
            MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(String.valueOf(CVActivity.idCV+1)).child("info").setValue(MainActivity.userCVDefault);
        }

    }


    public void updateInfoCVAll(){
        MainActivity.mData.child("cv").child(MainActivity.uid).child(key).removeValue();
        PdfCV pdfCV = new PdfCV(MainActivity.uid, cvName.getText().toString(), MainActivity.urlCV, key);
        MainActivity.mData.child("cv").child(MainActivity.uid).child(key).push().setValue(pdfCV);
        // khi checkfirst = 1, nghĩa là đã cập nhật, nên cập nhật lại CSDL
        // info
        if(MainActivity.checkFirstInfo == 1){
            MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(key).child("info").setValue(MainActivity.userCV);
        }

        // goal
        if(checkGoal == 0){
            if(MainActivity.checkFirstGoal == 1){
                MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(key).child("goal").setValue(MainActivity.goal);
            }else {
                MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(String.valueOf(CVActivity.idCV+1)).child("goal").setValue(MainActivity.goalDefault);
            }

        }else if(checkGoal == 1) {
            MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(key).child("goal").removeValue();
        }
        // study
        if( checkStudy == 0){
            if(MainActivity.checkFirstStudy == 1){
                MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(key).child("study").removeValue();

                for(int i=0; i < MainActivity.studyCVS.size(); i++){
                    String keyx = MainActivity.mData.push().getKey();
                    MainActivity.studyCVS.get(i).setId(keyx);
                    MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(key).child("study").push().setValue(MainActivity.studyCVS.get(i));
                }
            }else {
                String keyx = MainActivity.mData.push().getKey();
                MainActivity.studyCV.setId(keyx);
                MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(String.valueOf(CVActivity.idCV+1)).child("study").push().setValue(MainActivity.studyCV);
            }

        }else if(checkStudy == 1) {
            MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(key).child("study").removeValue();
        }

        // experience
        if( checkExperience == 0){
            if(MainActivity.checkFirstExperience == 1){
                MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(key).child("experience").removeValue();
                for(int i=0; i < experienceCVS.size(); i++){
                    String keyx = MainActivity.mData.push().getKey();
                    experienceCVS.get(i).setId(keyx);
                    MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(key).child("experience").push().setValue(experienceCVS.get(i));
                }
            }else {
                String keyx = MainActivity.mData.push().getKey();
                MainActivity.experienceCV.setId(keyx);
                MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(String.valueOf(CVActivity.idCV+1)).child("experience").push().setValue(MainActivity.experienceCV);
            }

        }else if(checkExperience == 1){
            MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(key).child("experience").removeValue();
        }

        // skill
        if( checkSkill == 0){
            if(MainActivity.checkFirstSkill == 1){
                MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(key).child("skill").removeValue();
                for(int i=0; i < MainActivity.skillCVS.size(); i++){
                    String keyx = MainActivity.mData.push().getKey();
                    MainActivity.skillCVS.get(i).setId(keyx);
                    MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(key).child("skill").push().setValue(MainActivity.skillCVS.get(i));
                }
            }else {
                for(int i=0; i < MainActivity.skillCVArray.size(); i++){
                    String keyx = MainActivity.mData.push().getKey();
                    MainActivity.skillCVArray.get(i).setId(keyx);
                    MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(String.valueOf(CVActivity.idCV+1)).child("skill").push().setValue(MainActivity.skillCVArray.get(i));
                }
            }

        }else if(checkSkill == 1){
            MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(key).child("skill").removeValue();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void updateCVAll() throws IOException {

        // tạo cv mới rồi up lên
        createCV(MainActivity.checkFirstInfo, MainActivity.checkFirstGoal, MainActivity.checkFirstStudy, MainActivity.checkFirstExperience,
                MainActivity.checkFirstSkill);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateInfoCVAll();
            }
        },4000);

    }
    // push goal
    public void pushGoalAdd(){
        if(MainActivity.checkFirstGoal == 1){
            MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(String.valueOf(CVActivity.idCV+1)).child("goal").setValue(MainActivity.goal);
        }else {
            MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(String.valueOf(CVActivity.idCV+1)).child("goal").setValue(MainActivity.goalDefault);
        }
    }
    // push study
    public void pushStudyAdd(){
        if(MainActivity.checkFirstStudy == 1){
            for(int i=0; i < MainActivity.studyCVS.size(); i++){
                String keyx = MainActivity.mData.push().getKey();
                MainActivity.studyCVS.get(i).setId(keyx);
                MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(String.valueOf(CVActivity.idCV+1)).child("study").push().setValue(MainActivity.studyCVS.get(i));
            }
        }else {
            String keyx = MainActivity.mData.push().getKey();
            MainActivity.studyCV.setId(keyx);
            MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(String.valueOf(CVActivity.idCV+1)).child("study").push().setValue(MainActivity.studyCV);
        }
    }
    // push experience
    public void pushExperienceAdd(){
        if(MainActivity.checkFirstExperience == 1){
            for(int i=0; i < experienceCVS.size(); i++){
                String keyx = MainActivity.mData.push().getKey();
                experienceCVS.get(i).setId(keyx);
                MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(String.valueOf(CVActivity.idCV+1)).child("experience").push().setValue(experienceCVS.get(i));
            }
        }else {
            String keyx = MainActivity.mData.push().getKey();
            MainActivity.experienceCV.setId(keyx);
            MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(String.valueOf(CVActivity.idCV+1)).child("experience").push().setValue(MainActivity.experienceCV);
        }
    }
    // push skill
    public void pushSkillAdd(){
        if(MainActivity.checkFirstSkill == 1){
            for(int i=0; i < MainActivity.skillCVS.size(); i++){
                String keyx = MainActivity.mData.push().getKey();
                MainActivity.skillCVS.get(i).setId(keyx);
                MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(String.valueOf(CVActivity.idCV+1)).child("skill").push().setValue(MainActivity.skillCVS.get(i));
            }
        }else {
            for(int i=0; i < MainActivity.skillCVArray.size(); i++){
                String keyx = MainActivity.mData.push().getKey();
                MainActivity.skillCVArray.get(i).setId(keyx);
                MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(String.valueOf(CVActivity.idCV+1)).child("skill").push().setValue(MainActivity.skillCVArray.get(i));
            }
        }
    }
    // push CV Add
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void pushCVAdd(){
        final String key = String.valueOf(idCV + 1);
        try {
            createCV(MainActivity.checkFirstInfo, MainActivity.checkFirstGoal, MainActivity.checkFirstStudy, MainActivity.checkFirstExperience,
                    MainActivity.checkFirstSkill);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    PdfCV pdfCV = new PdfCV(MainActivity.uid, cvName.getText().toString(), MainActivity.urlCV, key);
                    MainActivity.mData.child("cv").child(MainActivity.uid).child(String.valueOf(idCV + 1)).push().setValue(pdfCV);
                }
            },3000);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // push ADD
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void pushAddAll(){
        // CV
        pushCVAdd();

        // info
        pushInfoAdd();
        // những biến nào = 0 thì ghi vào CV info
        // study
        if(checkStudy == 0){
            pushStudyAdd();
        }

        // experience
        if(checkExperience == 0){
            pushExperienceAdd();
        }

        // skill
        if(checkSkill == 0){
            pushSkillAdd();
        }

        // goal
        if(checkGoal == 0){
            pushGoalAdd();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.luu:
                if(cvName.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng điền tên CV", Toast.LENGTH_SHORT).show();
                }else {
                    loading();
                    if(kind == 1){
                        pushAddAll();
                    }else {
                        try {
                            updateCVAll();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Đã lưu", Toast.LENGTH_SHORT).show();
                    // lưu id CV bằng số tăng dần, có ví dụ rồi, mỗi user thì có nhiều CV
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Intent intent = new Intent();
                            setResult(123);
                            finish();
                        }
                    },3000);

                }
                break;
            case R.id.huy:
                if(MainActivity.checkFirstSkill != 0 || MainActivity.checkFirstExperience != 0 || MainActivity.checkFirstStudy != 0
                || MainActivity.checkFirstGoal != 0 || MainActivity.checkFirstInfo != 0){
                    Toast.makeText(getApplicationContext(), "Bạn đã thay đổi nội dung của CV, hãy chọn lưu", Toast.LENGTH_SHORT).show();
                }else {
                    finish();
                    break;
                }
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
    public void checkChild(String key){
        MainActivity.mData.child("cvinfo").child(MainActivity.uid).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("goal")){
                    checkGoal = 0;
                }else {
                    checkGoal = 1;
                }
                if(snapshot.hasChild("study")){
                    checkStudy = 0;
                }else {
                    checkStudy = 1;
                }
                if(snapshot.hasChild("experience")){
                    checkExperience = 0;
                }else {
                    checkExperience = 1;
                }
                if(snapshot.hasChild("skill")){
                    checkSkill = 0;
                }else {
                    checkSkill = 1;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        webView = (WebView) findViewById(R.id.webview);
        btnnoidung = (Button) findViewById(R.id.buttonnoidung);
        cvName = (EditText) findViewById(R.id.editname);
        kind = getIntent().getIntExtra("kind",0);
        if(kind == 2){
            urlX = getIntent().getStringExtra("url");
            nameUpdate = getIntent().getStringExtra("cvname");
            cvName.setText(nameUpdate);
            key = getIntent().getStringExtra("key");
            MainActivity.urlCV = urlX;
        }
        arrayListRemove = new ArrayList<>();
        arrayListTTLH = new ArrayList<>();
        arrayListAdd = new ArrayList<>();
        arrayListTongHop = new ArrayList<>();
        arrayListTongHop.add(new Title(0, "Thông tin liên hệ"));
        arrayListTongHop.add(new Title(1, "Mục tiêu nghề nghiệp"));
        arrayListTongHop.add(new Title(2, "Học vấn"));
        arrayListTongHop.add(new Title(3, "Kinh nghiệm làm việc"));
        arrayListTongHop.add(new Title(4, "Kỹ năng"));

        arrayListRemove.add(arrayListTongHop.get(0));
        if(kind == 1){
            arrayListRemove.add(arrayListTongHop.get(1));
            arrayListRemove.add(arrayListTongHop.get(2));
            arrayListRemove.add(arrayListTongHop.get(3));
            arrayListRemove.add(arrayListTongHop.get(4));
            MainActivity.color = getIntent().getIntExtra("color",0);

        }else {
            checkChild(key);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(checkGoal == 0){
                        arrayListRemove.add(arrayListTongHop.get(1));
                    }else {
                        arrayListAdd.add(arrayListTongHop.get(1));
                    }
                    if(checkStudy == 0){
                        arrayListRemove.add(arrayListTongHop.get(2));
                    }else {
                        arrayListAdd.add(arrayListTongHop.get(2));
                    }
                    if(checkExperience == 0){
                        arrayListRemove.add(arrayListTongHop.get(3));
                    }else {
                        arrayListAdd.add(arrayListTongHop.get(3));
                    }
                    if(checkSkill == 0){
                        arrayListRemove.add(arrayListTongHop.get(4));
                    }else {
                        arrayListAdd.add(arrayListTongHop.get(4));
                    }
                }
            }, 2000);


        }

        //        Toast.makeText(getApplicationContext(), "goal : " + checkGoal + " ex: " + checkExperience + " study " + checkStudy + " skill " + checkSkill,
        //                Toast.LENGTH_SHORT).show();

    }
}
