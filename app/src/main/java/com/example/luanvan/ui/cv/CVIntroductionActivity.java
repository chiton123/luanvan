package com.example.luanvan.ui.cv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.CVAdapter;
import com.example.luanvan.ui.dashboard.DashboardViewModel;
import com.example.luanvan.ui.login.LoginActivity;
import com.example.luanvan.ui.modelCV.PdfCV;
import com.example.luanvan.ui.modelCV.UserCV;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.luanvan.MainActivity.experienceCVS;

public class CVIntroductionActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnThem;
    int REQUEST_CODE = 234;
    private DashboardViewModel dashboardViewModel;
    RecyclerView recyclerView;
    public static ArrayList<PdfCV> arrayListCV;
    CVAdapter adapter;
    // position to remove
    public static int position = 0;
    Handler handler;


    // kind: 1 tạo mới, 2: đổi mãu
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_v_introduction);
        anhxa();
        actionBar();
        getData();

        eventCreateCV();

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


    private void getData() {
        MainActivity.mData.child("cv").child(MainActivity.uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
              //  Toast.makeText(getApplicationContext(), snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                for(DataSnapshot x : snapshot.getChildren()){
                    PdfCV pdfCV = x.getValue(PdfCV.class);
                    if(pdfCV.key != null){
                        arrayListCV.add(pdfCV);
                    }

                }
                adapter.notifyDataSetChanged();
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
    public void getBackDefault(){
        MainActivity.checkFirstSkill = 0;
        MainActivity.checkFirstExperience = 0;
        MainActivity.checkFirstStudy = 0;
        MainActivity.checkFirstGoal = 0;
        MainActivity.checkFirstInfo = 0;
        MainActivity.skillCVS.clear();
        MainActivity.userCV = new UserCV();
        MainActivity.experienceCVS.clear();
        MainActivity.studyCVS.clear();
        MainActivity.goal = "";
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // update nhe
        if(resultCode == 123 && requestCode == 123){
            Toast.makeText(getApplicationContext(), "haha", Toast.LENGTH_SHORT).show();
            CVActivity.checkSkill = 0;
            CVActivity.checkExperience = 0;
            CVActivity.checkStudy = 0;
            CVActivity.checkGoal = 0;
            arrayListCV.remove(position);
            adapter.notifyDataSetChanged();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getBackDefault();
                }
            },5000);

        }
        // them
        if(resultCode == 123 && requestCode == REQUEST_CODE){


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void eventCreateCV() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CVCreateActivity.class);
                intent.putExtra("kind", 1); // tạo mới
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

    }
    private void anhxa() {
        arrayListCV = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new CVAdapter(getApplicationContext(), arrayListCV, this);
        recyclerView.setAdapter(adapter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        btnThem = (Button)findViewById(R.id.buttontaomoi);


    }
}
