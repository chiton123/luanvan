package com.example.luanvan.ui.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;

import com.example.luanvan.ui.Adapter.ExperienceAdapter;
import com.example.luanvan.ui.Adapter.SkillAdapter;
import com.example.luanvan.ui.Adapter.StudyAdapter;
import com.google.android.material.appbar.AppBarLayout;

public class EditCombineActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    StudyAdapter studyAdapter;
    SkillAdapter skillAdapter;
    ExperienceAdapter experienceAdapter;
    ProgressDialog progressDialog;
    Handler handler = new Handler();
    // request_code 1: study, 2: experience, 3: skill
    // tip: khi update xong, thì edit arraylist tại vị trí position, sau đó, quay về, onActivityResult => adapter.notify
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_combine);
        anhxa();
        actionBar();
        PickAdapter();


    }
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == 1){
            studyAdapter.notifyDataSetChanged();
            MainActivity.studyAdapter.notifyDataSetChanged();
        }
        if(requestCode == 2 && resultCode == 2){
            experienceAdapter.notifyDataSetChanged();
            MainActivity.experienceAdapter.notifyDataSetChanged();
        }
        if(requestCode == 3 && resultCode == 3){
            skillAdapter.notifyDataSetChanged();
            MainActivity.skillAdapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void PickAdapter() {
        Intent intent = getIntent();
        int number = intent.getIntExtra("number",0);
        switch (number){
            case 1:
                studyAdapter = new StudyAdapter(getApplicationContext(), MainActivity.studies, this, 0);
                recyclerView.setAdapter(studyAdapter);
                getSupportActionBar().setTitle("Học vấn");
                break;
            case 2:
                experienceAdapter = new ExperienceAdapter(getApplicationContext(), MainActivity.experiences, this, 0);
                recyclerView.setAdapter(experienceAdapter);
                getSupportActionBar().setTitle("Kinh nghiệm");
                break;
            case 3:
                skillAdapter = new SkillAdapter(getApplicationContext(), MainActivity.skills, this, 0);
                recyclerView.setAdapter(skillAdapter);
                getSupportActionBar().setTitle("Kỹ năng");
                break;
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
    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));


    }






}
