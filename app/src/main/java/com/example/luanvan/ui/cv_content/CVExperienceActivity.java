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
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.cv_all.ExperienceCVAdapter;
import com.example.luanvan.ui.Model.Pdf;
import com.example.luanvan.ui.modelCV.ExperienceCV;
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

import static com.example.luanvan.MainActivity.experiences;
import static com.example.luanvan.MainActivity.skillCVS;
import static com.example.luanvan.ui.cv_content.CVInfoActivity.cv_address;
import static com.example.luanvan.ui.cv_content.CVInfoActivity.cv_email;
import static com.example.luanvan.ui.cv_content.CVInfoActivity.cv_name;
import static com.example.luanvan.ui.cv_content.CVInfoActivity.cv_phone;
import static com.example.luanvan.ui.cv_content.CVInfoActivity.cv_position;

public class CVExperienceActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnAdd, btnHuy, btnLuu;
    RecyclerView recyclerView;
    ExperienceCVAdapter adapter;
    public static ArrayList<ExperienceCV> arrayList;
    int pageWidth = 1200;
    StorageReference storageReference;
    Handler handler;
    public static int firstcheck = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_v_experience);
        anhxa();
        actionBar();
        eventButton();
        storageReference = FirebaseStorage.getInstance().getReference();
        getData();

    }
    private void getData() {
        if(firstcheck == 1){
            MainActivity.mData.child("cvinfo").child(MainActivity.uid).child("experience").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot x : snapshot.getChildren()){
                        ExperienceCV a = x.getValue(ExperienceCV.class);
                        arrayList.add(a);
                        adapter.notifyDataSetChanged();
                    }

                    //   Toast.makeText(getApplicationContext(), "" + skillCVS.size(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
    public void showDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cv_experience);
        dialog.setCancelable(false);
        final EditText editname = (EditText) dialog.findViewById(R.id.name);
        final EditText editposition = (EditText) dialog.findViewById(R.id.position);
        final EditText editstart = (EditText) dialog.findViewById(R.id.start);
        final EditText editend = (EditText) dialog.findViewById(R.id.end);
        final EditText editdescription = (EditText) dialog.findViewById(R.id.description);
        Button btnLuu = (Button) dialog.findViewById(R.id.luu);
        Button btnHuy = (Button) dialog.findViewById(R.id.huy);
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editname.getText().equals("") || editposition.getText().equals("") || editstart.getText().equals("")
                || editend.getText().equals("") || editdescription.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    String name = editname.getText().toString();
                    String position = editposition.getText().toString();
                    String start = editstart.getText().toString();
                    String end = editend.getText().toString();
                    String description = editdescription.getText().toString();
                    ExperienceCV experienceCV = new ExperienceCV("temp", name, position, start, end, description);
                    arrayList.add(experienceCV);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();

                }


            }
        });

        dialog.show();
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
        canvas.drawText(CVGoalActivity.cv_goal, 30, 450, contentPaint);

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

        // KINH NGHIEM
        canvas.drawText("KINH NGHIỆM", 30, 920, titlePaint);
        for(int i=0; i < arrayList.size(); i++){
            canvas.drawText(arrayList.get(i).getStart()+"-"+arrayList.get(i).getEnd(), 30, 970 + i*90, contentPaint);
            canvas.drawText(arrayList.get(i).getCompany(), 500, 970 + i*90, contentPaint);
            canvas.drawText(arrayList.get(i).getPosition(), 500, 1010 + i*90, contentPaint);
        }

//        canvas.drawText("10/2017-12/2017", 30, 970, contentPaint);
//        canvas.drawText("Học bổng du học Thái Lan", 500, 970, contentPaint);
        // ky nang
        canvas.drawText("KỸ NĂNG", 30, 1250, titlePaint);
        canvas.drawText(skillCVS.get(0).getName(), 30, 1300, contentPaint);
        int width = 300, height = 50;
        // 300 : 5 = 60
        float star1 = skillCVS.get(0).getStar()*60;
        float star2 = skillCVS.get(1).getStar()*60;
        canvas.drawLine(30, 1350, star1+30, 1350, kynang_paint);
        canvas.drawLine(star1 + 30, 1350, width + 30,1350,  kynangphu );

        canvas.drawText(skillCVS.get(1).getName(), 30, 1400, contentPaint);
        canvas.drawLine(30, 1450, star2+30, 1450, kynang_paint);
        canvas.drawLine(star2+30, 1450, width + 30, 1450, kynangphu);

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
                if(arrayList.size() == 0){
                    Toast.makeText(getApplicationContext(), "Bạn chưa thêm kinh nghiệm nào", Toast.LENGTH_SHORT).show();
                }else {
                    recyclerView.setVisibility(View.INVISIBLE);
                    MainActivity.mData.child("cvinfo").child(MainActivity.uid).child("experience").removeValue();
                    for(int i=0; i < arrayList.size(); i++){
                        String key = MainActivity.mData.push().getKey();
                        arrayList.get(i).setId(key);
                        MainActivity.mData.child("cvinfo").child(MainActivity.uid).child("experience").push().setValue(arrayList.get(i));
                        firstcheck = 1;
                    }
                    try {
                        createCV();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            setResult(103);
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
        arrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new ExperienceCVAdapter(getApplicationContext(), arrayList, this, 0);
        recyclerView.setAdapter(adapter);



    }
}
