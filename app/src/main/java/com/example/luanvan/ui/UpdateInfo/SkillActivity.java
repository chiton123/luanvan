package com.example.luanvan.ui.UpdateInfo;

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
    int id = 0; // id study trên csdl
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlskill,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                MainActivity.skills.add(new Skill(
                                        object.getInt("id"),
                                        object.getString("name"),
                                        (float) object.getDouble("star"),
                                        object.getString("description")
                                ));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("iduser", String.valueOf(MainActivity.iduser));
                return map;
            }
        };
        requestQueue.add(stringRequest);

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
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("success")){
                                        Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                        if(update == 1){
                                            MainActivity.skills.get(position).setDescription(mota);
                                            MainActivity.skills.get(position).setName(name);
                                            MainActivity.skills.get(position).setStar(star);
                                        }else {
                                            MainActivity.skills.clear();
                                            getInfoSkill();

                                        }

                                        loading();
                                        handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(update == 0){
                                                    MainActivity.skillAdapter.notifyDataSetChanged();
                                                }
                                                MainActivity.skillAdapter.notifyDataSetChanged();
                                                progressDialog.dismiss();
                                                Intent intent = new Intent();
                                                setResult(3);
                                                finish();
                                            }
                                        }, 3000);

                                    }else {
                                        Toast.makeText(getApplicationContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<>();
                            map.put("id", String.valueOf(id));
                            map.put("name", name);
                            map.put("mota", mota);
                            map.put("star", String.valueOf(star1));
                            map.put("iduser", String.valueOf(MainActivity.iduser));
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);


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
