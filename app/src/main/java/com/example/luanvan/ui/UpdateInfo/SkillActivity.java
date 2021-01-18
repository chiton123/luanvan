package com.example.luanvan.ui.UpdateInfo;

import androidx.annotation.NonNull;
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
    Handler handler = new Handler();
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
       MainActivity.mData.child("skill").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.hasChild("skill")){
                   MainActivity.mData.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           for(DataSnapshot x : snapshot.getChildren()){
                               Skill skill = x.getValue(Skill.class);
                               MainActivity.skills.add(skill);
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
                        url = MainActivity.urlupdate_old_skill;
                    }else {
                        url = MainActivity.urlupdateskill;
                    }
                    String key = MainActivity.mData.push().getKey();
                    final Skill skill = new Skill(key, MainActivity.uid, name, star, mota);
                    MainActivity.mData.child("skill").push().setValue(skill)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        MainActivity.skills.add(skill);
                                        MainActivity.skillAdapter.notifyDataSetChanged();
                                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        setResult(3, intent);
                                        finish();
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

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
