package com.example.luanvan.ui.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;

import com.example.luanvan.ui.Adapter.update_personal_info.ExperienceAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.SkillAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.StudyAdapter;
import com.example.luanvan.ui.Model.Experience;
import com.example.luanvan.ui.Model.Skill;
import com.example.luanvan.ui.Model.Study;
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
    LinearLayout layout, layout_nothing;
    int x = 0;
    int number = 0; // 1: adapter study, 2: experience, 3: skill
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
    public void reloadStudy(){
        x = 1;
        MainActivity.studies.clear();
        getInfoStudy();
    }
    public void reloadExperience(){
        x = 2;
        MainActivity.experiences.clear();
        getInfoExperience();
    }
    public void reloadSkill(){
        x = 3;
        MainActivity.skills.clear();
        getInfoSkill();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == 1){
            reloadStudy();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    studyAdapter.notifyDataSetChanged();
                    MainActivity.studyAdapter.notifyDataSetChanged();
                    checkStudy();
                }
            },1000);


        }
        if(requestCode == 2 && resultCode == 2){
            reloadExperience();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    experienceAdapter.notifyDataSetChanged();
                    MainActivity.experienceAdapter.notifyDataSetChanged();
                    checkExperience();
                }
            },1000);

        }
        if(requestCode == 3 && resultCode == 3){
            reloadSkill();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    skillAdapter.notifyDataSetChanged();
                    MainActivity.skillAdapter.notifyDataSetChanged();
                    checkSkill();
                }
            },1000);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void checkStudy(){
        if(MainActivity.studies.size() == 0){
            layout_nothing.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }else {
            layout_nothing.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
    }

    public void refreshStudy(){
        layout_nothing.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        MainActivity.studies.clear();
        studyAdapter.notifyDataSetChanged();
        MainActivity.studyAdapter.notifyDataSetChanged();
    }
    public void refreshExperience(){
        layout_nothing.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        MainActivity.experiences.clear();
        experienceAdapter.notifyDataSetChanged();
        MainActivity.experienceAdapter.notifyDataSetChanged();
    }
    public void refreshSkill(){
        layout_nothing.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        MainActivity.skills.clear();
        skillAdapter.notifyDataSetChanged();
        MainActivity.skillAdapter.notifyDataSetChanged();
    }
    public void checkExperience(){
        if(MainActivity.experiences.size() == 0){
            layout_nothing.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }else {
            layout_nothing.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
    }

    public void checkSkill(){
        if(MainActivity.skills.size() == 0){
            layout_nothing.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }else {
            layout_nothing.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
    }
    private void PickAdapter() {
        Intent intent = getIntent();
        number = intent.getIntExtra("number",0);
        switch (number){
            case 1:
                studyAdapter = new StudyAdapter(getApplicationContext(), MainActivity.studies, this, 0);
                recyclerView.setAdapter(studyAdapter);
                getSupportActionBar().setTitle("Học vấn");
                checkStudy();
                break;
            case 2:
                experienceAdapter = new ExperienceAdapter(getApplicationContext(), MainActivity.experiences, this, 0);
                recyclerView.setAdapter(experienceAdapter);
                getSupportActionBar().setTitle("Kinh nghiệm");
                checkExperience();
                break;
            case 3:
                skillAdapter = new SkillAdapter(getApplicationContext(), MainActivity.skills, this, 0);
                recyclerView.setAdapter(skillAdapter);
                getSupportActionBar().setTitle("Kỹ năng");
                checkSkill();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        layout = (LinearLayout) findViewById(R.id.layout);
        layout_nothing = (LinearLayout) findViewById(R.id.layout_nothing);

    }






}
