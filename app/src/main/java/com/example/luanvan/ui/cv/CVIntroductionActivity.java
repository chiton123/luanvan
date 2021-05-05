package com.example.luanvan.ui.cv;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.cv_all.CVAdapter;
import com.example.luanvan.ui.dashboard.DashboardViewModel;
import com.example.luanvan.ui.modelCV.UserCV;

public class CVIntroductionActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnThem;
    int REQUEST_CODE = 234;
    private DashboardViewModel dashboardViewModel;
    RecyclerView recyclerView;
    CVAdapter adapter;
    // position to remove
    public static int position = 0;
    Handler handler;
    LinearLayout layout, layout_nothing;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_v_introduction);
        loading();
        anhxa();
        actionBar();
        // get cv sau khi đăng nhập rồi
        checkNothing();
        eventCreateCV();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        },1500);

    }
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    public void checkNothing(){
        if(MainActivity.arrayListCV.size() == 0){
            layout_nothing.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }else {
            layout.setVisibility(View.VISIBLE);
            layout_nothing.setVisibility(View.GONE);
        }
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



    public void getBackDefault(){
        CVActivity.checkSkill = 0;
        CVActivity.checkExperience = 0;
        CVActivity.checkStudy = 0;
        CVActivity.checkGoal = 0;
        MainActivity.checkFirstSkill = 0;
        MainActivity.checkFirstExperience = 0;
        MainActivity.checkFirstStudy = 0;
        MainActivity.checkFirstGoal = 0;
        MainActivity.checkFirstInfo = 0;
        MainActivity.skillCVS.clear();
        MainActivity.userCV = new UserCV();
        MainActivity.userCV.setUsername(MainActivity.userCVDefault.getUsername());
        MainActivity.userCV.setGender(MainActivity.userCVDefault.getGender());
        MainActivity.userCV.setBirthday(MainActivity.userCVDefault.getBirthday());
        MainActivity.userCV.setEmail(MainActivity.userCVDefault.getEmail());
        MainActivity.userCV.setPhone(MainActivity.userCVDefault.getPhone());
        MainActivity.userCV.setAddress(MainActivity.userCVDefault.getAddress());
        MainActivity.userCV.setPosition(MainActivity.userCVDefault.getPosition());
        MainActivity.experienceCVS.clear();
        MainActivity.studyCVS.clear();
        MainActivity.goal = "";
        MainActivity.color = 0;
        MainActivity.urlCV = "";
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // update nhe
        if(resultCode == 123 && requestCode == 123){
           // Toast.makeText(getApplicationContext(), "size " + MainActivity.arrayListCV.size(), Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getBackDefault();
                }
            },2000);
        }
        // toolbar finish
        if(requestCode == 123 && resultCode == 5){
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getBackDefault();
                }
            },1000);
        }
        // them
        if(resultCode == 123 && requestCode == REQUEST_CODE){
            // Để nó load lên firebase và trả về kịp mới notifychange
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    checkNothing();
                }
            },2500);

            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getBackDefault();
                }
            },5000);
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
        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new CVAdapter(getApplicationContext(), MainActivity.arrayListCV, this);
        recyclerView.setAdapter(adapter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        layout = (LinearLayout) findViewById(R.id.layout);
        layout_nothing = (LinearLayout) findViewById(R.id.layout_nothing);
        btnThem = (Button)findViewById(R.id.buttontaomoi);

        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);

    }
}
