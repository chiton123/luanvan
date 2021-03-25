package com.example.luanvan.ui.recruiter.PostNews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.ViewPageAdapter;
import com.example.luanvan.ui.Model.JobList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class RecruitmentNewsActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPageAdapter viewPageAdapter;
    ViewPager viewPager;
    FloatingActionButton btnAdd;
    Handler handler;
  //  public static ArrayList<JobList> arrayListDisplay = new ArrayList<>();
    int REQUEST_CODE_ADD_JOB = 5;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruitment_news);
        loading();
        anhxa();
        actionBar();
        eventButton();
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
    private void eventButton() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateJobActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_JOB);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 123 && resultCode == 123){
          //  Toast.makeText(getApplicationContext(), data.getStringExtra("abc"), Toast.LENGTH_SHORT).show();
         //   Toast.makeText(getApplicationContext(), "outdated ", Toast.LENGTH_SHORT).show();
            OutdatedJobFragment outdatedJobFragment = (OutdatedJobFragment) viewPageAdapter.getItem(2);
            outdatedJobFragment.onActivityResult(requestCode, resultCode, data);
        //    Toast.makeText(getApplicationContext(), "display ", Toast.LENGTH_SHORT).show();
            DisplayJobFragment displayJobFragment = (DisplayJobFragment) viewPageAdapter.getItem(0);
            displayJobFragment.onActivityResult(requestCode, resultCode, data);

        }
        if(requestCode == 123 && resultCode == 234 ){
           // Toast.makeText(getApplicationContext(), "auth ", Toast.LENGTH_SHORT).show();
            AuthenticationFragment authenticationFragment = (AuthenticationFragment) viewPageAdapter.getItem(1);
            authenticationFragment.onActivityResult(requestCode, resultCode, data);
        }
        if(requestCode == 123 && resultCode == 345){
            Toast.makeText(getApplicationContext(), "reject ", Toast.LENGTH_SHORT).show();
            RejectJobFragment rejectJobFragment = (RejectJobFragment) viewPageAdapter.getItem(3);
            rejectJobFragment.onActivityResult(requestCode, resultCode, data);
        }
        if(requestCode == 123 && resultCode == 333){
       //     Toast.makeText(getApplicationContext(), "display ", Toast.LENGTH_SHORT).show();
            DisplayJobFragment displayJobFragment = (DisplayJobFragment) viewPageAdapter.getItem(0);
            displayJobFragment.onActivityResult(requestCode, resultCode, data);
         //   Toast.makeText(getApplicationContext(), "outdated ", Toast.LENGTH_SHORT).show();
            OutdatedJobFragment outdatedJobFragment = (OutdatedJobFragment) viewPageAdapter.getItem(2);
            outdatedJobFragment.onActivityResult(requestCode, resultCode, data);
        }
        if(requestCode == REQUEST_CODE_ADD_JOB && resultCode == 123){
            Toast.makeText(getApplicationContext(), "authe ", Toast.LENGTH_SHORT).show();
            AuthenticationFragment authenticationFragment = (AuthenticationFragment) viewPageAdapter.getItem(1);
            authenticationFragment.onActivityResult(requestCode, resultCode, data);
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
        btnAdd = (FloatingActionButton) findViewById(R.id.buttonaction);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPageAdapter.addFragment(new DisplayJobFragment(), "ĐANG HIỂN THỊ");
        viewPageAdapter.addFragment(new AuthenticationFragment(), "CHỜ XÁC THỰC");
        viewPageAdapter.addFragment(new OutdatedJobFragment(), "HÊT HẠN");
        viewPageAdapter.addFragment(new RejectJobFragment(), "TỪ CHỐI");
        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
