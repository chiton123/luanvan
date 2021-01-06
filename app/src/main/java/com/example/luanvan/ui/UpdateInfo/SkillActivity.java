package com.example.luanvan.ui.UpdateInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);
        anhxa();
        actionBar();
        eventUpdate();
        getInfo();


    }
    private void getInfo() {
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
                                        object.getString("name"),
                                        object.getInt("star"),
                                        object.getString("description")
                                ));
                                editname.setText(object.getString("name"));
                                editmota.setText(object.getString("description"));
                                ratingBar.setRating(object.getInt("star"));
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
                final int star = ratingBar.getNumStars();
                if(editname.getText().equals("") || editmota.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin" , Toast.LENGTH_SHORT).show();
                }else if(star == 0){
                    Toast.makeText(getApplicationContext(), "Đánh giá kỹ năng" , Toast.LENGTH_SHORT).show();
                }else {
                    final String name = editname.getText().toString();
                    final String mota = editmota.getText().toString();
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlupdateskill,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("success")){
                                        Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        setResult(3);
                                        finish();
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
                            map.put("name", name);
                            map.put("mota", mota);
                            map.put("star", String.valueOf(star));
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
