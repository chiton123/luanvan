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

import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.ViewPageAdapter;
import com.example.luanvan.ui.Model.Applicant;
import com.example.luanvan.ui.fragment.recruting.CVFilterFragment;
import com.example.luanvan.ui.fragment.recruting.GoToWorkFragment;
import com.example.luanvan.ui.fragment.recruting.InterviewFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class CVManagementActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPageAdapter viewPageAdapter;
    public static ArrayList<Applicant> arrayListCVFilter = new ArrayList<>();
    public static ArrayList<Applicant> arrayListInterView = new ArrayList<>();
    public static ArrayList<Applicant> arrayListGoToWork = new ArrayList<>();
    // Nhận kết quả trả về rồi reload
    int kind = 0;
    int statusApplication = 0;

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
                if(statusApplication == 3 || statusApplication == 4 || statusApplication == 5){
                    reloadInterview();
                    reloadFilterCV();
                }else if(statusApplication > 5){
                    reloadGoToWork();
                    reloadFilterCV();
                }else {
                    CVFilterFragment.adapter.notifyDataSetChanged();
                }
            }

            if(kind == 2){
                if(statusApplication < 3){
                    reloadInterview();
                    reloadFilterCV();
                }else if(statusApplication > 5){
                    reloadGoToWork();
                    reloadInterview();
                }else {
                    InterviewFragment.adapter.notifyDataSetChanged();
                }

            }
            if(kind == 3){
                if(statusApplication < 6 && statusApplication > 2){
                    reloadInterview();
                    reloadGoToWork();
                }else if(statusApplication < 3){
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


    }
}
