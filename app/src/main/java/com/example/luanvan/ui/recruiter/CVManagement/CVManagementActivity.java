package com.example.luanvan.ui.recruiter.CVManagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.ViewPageAdapter;
import com.example.luanvan.ui.Adapter.recruit.CVFilterAdapter;
import com.example.luanvan.ui.Model.Applicant;
import com.example.luanvan.ui.fragment.recruting.CVFilterFragment;
import com.example.luanvan.ui.fragment.recruting.GoToWorkFragment;
import com.example.luanvan.ui.fragment.recruting.InterviewFragment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CVManagementActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPageAdapter viewPageAdapter;

    // Nhận kết quả trả về rồi reload
    int kind = 0;
    int statusApplication = 0;
    public static int position_job_list = 0; // chỉ dùng được cho danh sách vị trí
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_v_management);
        anhxa();
        actionBar();


    }
    // 1: lọc cv, 2: phỏng vấn , 3: nhận việc
    public void reloadFilterCV(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CVFilterFragment cvFilterFragment = null;
        cvFilterFragment = (CVFilterFragment) viewPageAdapter.getItem(0);
        transaction.detach(cvFilterFragment);
        transaction.attach(cvFilterFragment);
        transaction.commitNowAllowingStateLoss();
    }
    public void reloadInterview(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        InterviewFragment interviewFragment = null;
        interviewFragment = (InterviewFragment) viewPageAdapter.getItem(1);
        transaction.detach(interviewFragment);
        transaction.attach(interviewFragment);
        transaction.commitNowAllowingStateLoss();
    }
    public void reloadGoToWork(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        GoToWorkFragment goToWorkFragment = null;
        goToWorkFragment = (GoToWorkFragment) viewPageAdapter.getItem(2);
        transaction.detach(goToWorkFragment);
        transaction.attach(goToWorkFragment);
        transaction.commitNowAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 123 && resultCode == 123 ){
            kind = data.getIntExtra("kind", 0);
            statusApplication = data.getIntExtra("status", 0);
            if(kind == 1){
                if(statusApplication >= 3 && statusApplication <= 8){
                    reloadInterview();
                    reloadFilterCV();
                }else if(statusApplication >= 9){
                    reloadGoToWork();
                    reloadFilterCV();
                }else {
                    CVFilterFragment.adapter.notifyDataSetChanged();
                }
            }

            if(kind == 2){
                if(statusApplication <= 2){
                    reloadInterview();
                    reloadFilterCV();
                }else if(statusApplication >= 9){
                    reloadGoToWork();
                    reloadInterview();
                }else {
                    InterviewFragment.adapter.notifyDataSetChanged();
                }

            }
            if(kind == 3){
                if(statusApplication >= 3 && statusApplication <= 8){
                    reloadInterview();
                    reloadGoToWork();
                }else if(statusApplication <= 2){
                    reloadGoToWork();
                    reloadFilterCV();
                }else {
                    GoToWorkFragment.adapter.notifyDataSetChanged();
                }

            }

        }

        super.onActivityResult(requestCode, resultCode, data);
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
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPageAdapter.addFragment(new CVFilterFragment(), "Lọc CV");
        viewPageAdapter.addFragment(new InterviewFragment(), "Phỏng vấn");
        viewPageAdapter.addFragment(new GoToWorkFragment(), "Nhận việc");
        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        position_job_list = getIntent().getIntExtra("position", 0);


    }
}