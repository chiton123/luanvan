package com.example.luanvan.ui.User;

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
import com.example.luanvan.ui.Model.Assessment;
import com.example.luanvan.ui.Model.Company;
import com.example.luanvan.ui.company.CompanyActivity;
import com.example.luanvan.ui.fragment.company_f.AssessmentFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateAssessmentActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editRemark;
    RatingBar ratingBar;
    Button btnAssess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_assessment);
        anhxa();
        actionBar();
        eventAssess();
    }

    private void eventAssess() {
        btnAssess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String remark = editRemark.getText().toString();
                final float star = ratingBar.getRating();
                if(remark.equals("") || star == 0){
                    Toast.makeText(getApplicationContext(), "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlRemark,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(!response.equals("fail")){
                                        Toast.makeText(getApplicationContext(), "Đánh giá thành công", Toast.LENGTH_SHORT).show();
                                        int id = Integer.parseInt(response);
                                        AssessmentFragment.arrayList.add(new Assessment(id, CompanyActivity.company.getId(), MainActivity.iduser,
                                                MainActivity.user.getName(), remark, star));
                                        Intent intent = new Intent();
                                        setResult(111);
                                        finish();
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Đánh giá thất bại", Toast.LENGTH_SHORT).show();
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
                            map.put("idcompany", String.valueOf(CompanyActivity.company.getId()));
                            map.put("iduser", String.valueOf(MainActivity.iduser));
                            map.put("remark", remark);
                            map.put("star", String.valueOf(star));
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);

                }

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
        editRemark = (EditText) findViewById(R.id.editremark);
        ratingBar = (RatingBar) findViewById(R.id.rating);
        btnAssess = (Button) findViewById(R.id.buttondanhgia);
    }
}
