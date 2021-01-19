package com.example.luanvan.ui.UpdateInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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
import com.example.luanvan.ui.Model.Experience;
import com.example.luanvan.ui.Model.Skill;
import com.example.luanvan.ui.Model.Study;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SkillActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editname, editmota;
    RatingBar ratingBar;
    Button btnhuy, btncapnhat;
    String id = ""; // id study trên csdl
    int position = 0; // trên mảng arraylist, thứ tự
    int update = 0;
    String url = "";
    ProgressDialog progressDialog;
    Handler handler, handler1;
    String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);
        anhxa();
        actionBar();
        eventUpdate();
        getInfo();


    }
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    private void getInfo() {
        // 10: add, 3: update
        int check = getIntent().getIntExtra("confirm", 0);
        if(check == 3){
            update = 1;
            Skill skill = (Skill) getIntent().getSerializableExtra("skill");
            position = getIntent().getIntExtra("position", 0);
            id = skill.getId();
            editname.setText(skill.getName());
            editmota.setText(skill.getDescription());
            ratingBar.setRating(skill.getStar());
        }
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
    public void getKey(){
        MainActivity.mData.child("skill").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot x : snapshot.getChildren()){
                    if(snapshot.child("id").getValue().toString().equals(id)){
                        key = snapshot.getKey();
                    }
                }

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
    private void eventUpdate() {
        btncapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final float star = ratingBar.getRating();
              //  Toast.makeText(getApplicationContext(), "Cập nhật :" + star, Toast.LENGTH_SHORT).show();
                if(editname.getText().equals("") || editmota.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin" , Toast.LENGTH_SHORT).show();
                }else if(star == 0){
                    Toast.makeText(getApplicationContext(), "Đánh giá kỹ năng" , Toast.LENGTH_SHORT).show();
                }else {
                    final String name = editname.getText().toString();
                    final String mota = editmota.getText().toString();
                    final float star1 = ratingBar.getRating();
                    if(update == 1){
                        loading();
                        getKey();
                        final Skill skill = new Skill(id, MainActivity.uid, name, star1, mota);
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.mData.child("skill").child(key).setValue(skill);
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            }
                        },2000);
                        handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Intent intent = new Intent();
                                setResult(3, intent);
                                finish();
                            }
                        }, 2000);
                    }else {
                        String key = MainActivity.mData.push().getKey();
                        final Skill skill = new Skill(key, MainActivity.uid, name, star, mota);
                        MainActivity.mData.child("skill").push().setValue(skill)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(), "Đã thêm", Toast.LENGTH_SHORT).show();
                                            loading();
                                            MainActivity.skills.clear();
                                            getInfoSkill();
                                            MainActivity.skillAdapter.notifyDataSetChanged();
                                            handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent();
                                                    setResult(3, intent);
                                                    finish();
                                                }
                                            },1000);


                                        }else {
                                            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }


                }
            }
        });
        btnhuy.setOnClickListener(new View.OnClickListener() {
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
        editname = (EditText) findViewById(R.id.editskill);
        editmota = (EditText) findViewById(R.id.editmota);
        ratingBar = (RatingBar) findViewById(R.id.rating);
        btnhuy = (Button) findViewById(R.id.buttonhuy);
        btncapnhat = (Button) findViewById(R.id.buttoncapnhat);
    }
}
