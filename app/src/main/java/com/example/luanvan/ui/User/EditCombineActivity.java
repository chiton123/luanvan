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
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;

import com.example.luanvan.ui.Adapter.ExperienceAdapter;
import com.example.luanvan.ui.Adapter.SkillAdapter;
import com.example.luanvan.ui.Adapter.StudyAdapter;
import com.example.luanvan.ui.Model.Experience;
import com.example.luanvan.ui.Model.Skill;
import com.example.luanvan.ui.Model.Study;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class EditCombineActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    StudyAdapter studyAdapter;
    SkillAdapter skillAdapter;
    ExperienceAdapter experienceAdapter;
    ProgressDialog progressDialog;
    Handler handler;
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
    private void getInfoStudy() {
        MainActivity.mData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("study")){
                    MainActivity.mData.child("study").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot x : snapshot.getChildren()){
                                Study study = x.getValue(Study.class);
                                if(study.getUid().equals(MainActivity.uid)){
                                    MainActivity.studies.add(study);
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void getInfoExperience() {
        MainActivity.mData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("experience") && snapshot.exists()){
                    MainActivity.mData.child("experience").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot x : snapshot.getChildren()){
                                Experience experience = x.getValue(Experience.class);
                                if(experience.getUid().equals(MainActivity.uid)){
                                    MainActivity.experiences.add(experience);
                                }


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void getInfoSkill() {
        MainActivity.mData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("skill")){
                    MainActivity.mData.child("skill").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot x : snapshot.getChildren()){
                                Skill skill = x.getValue(Skill.class);
                                if(skill.getUid().equals(MainActivity.uid)){
                                    MainActivity.skills.add(skill);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
            MainActivity.studies.clear();
            getInfoStudy();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    studyAdapter.notifyDataSetChanged();
                    MainActivity.studyAdapter.notifyDataSetChanged();
                //    Toast.makeText(getApplicationContext(), MainActivity.studies.size() + "", Toast.LENGTH_SHORT).show();
                }
            },1000);

        }
        if(requestCode == 2 && resultCode == 2){
            MainActivity.studies.clear();
            getInfoExperience();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    experienceAdapter.notifyDataSetChanged();
                    MainActivity.experienceAdapter.notifyDataSetChanged();
                    //    Toast.makeText(getApplicationContext(), MainActivity.studies.size() + "", Toast.LENGTH_SHORT).show();
                }
            },1000);
        }
        if(requestCode == 3 && resultCode == 3){
            MainActivity.skills.clear();
            getInfoSkill();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    skillAdapter.notifyDataSetChanged();
                    MainActivity.skillAdapter.notifyDataSetChanged();
                    //    Toast.makeText(getApplicationContext(), MainActivity.studies.size() + "", Toast.LENGTH_SHORT).show();
                }
            },1000);
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
     //   recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));


    }






}
