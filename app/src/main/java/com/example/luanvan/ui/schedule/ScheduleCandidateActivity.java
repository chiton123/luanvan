package com.example.luanvan.ui.schedule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
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
import com.example.luanvan.ui.Adapter.schedule_a.ScheduleAdapter;
import com.example.luanvan.ui.Adapter.schedule_a.ScheduleCandidateAdapter;
import com.example.luanvan.ui.Model.Schedule;
import com.example.luanvan.ui.Model.ScheduleCandidate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScheduleCandidateActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    public static ScheduleCandidateAdapter adapter;
    public static ArrayList<ScheduleCandidate> arrayList;
    int REQUEST_CODE = 123;
    public static LinearLayout layout, layout_nothing;
    Handler handler;
    // status : 0 chưa xác nhận, 1 đồng ý , 2 từ chối , 3 dời lịch pv
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_candidate);
        anhxa();
        actionBar();
        getData();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkNothing();
            }
        },2000);


    }
    public void checkNothing(){
        if(arrayList.size() == 0){
            layout_nothing.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }else {
            layout_nothing.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlGetScheduleCandidate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response != null){
                            try {
                                // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    arrayList.add(new ScheduleCandidate(
                                            object.getInt("id"),
                                            object.getInt("id_recruiter"),
                                            object.getInt("id_job"),
                                            object.getString("job_name"),
                                            object.getString("company_name"),
                                            object.getInt("type"),
                                            object.getString("date"),
                                            object.getString("start_hour"),
                                            object.getString("end_hour"),
                                            object.getString("note"),
                                            object.getString("note_candidate"),
                                            object.getInt("status")
                                    ));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            adapter.notifyDataSetChanged();


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
                map.put("iduser", String.valueOf(MainActivity.iduser));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 123 && resultCode == 123){
            adapter.notifyDataSetChanged();
            checkNothing();
        }
        if(requestCode == 123 && resultCode == 345){
            adapter.notifyDataSetChanged();
            adapter.stopBottomSheet();
            checkNothing();
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
        arrayList = new ArrayList<>();
        adapter = new ScheduleCandidateAdapter(this, arrayList, this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        layout = (LinearLayout) findViewById(R.id.layout);
        layout_nothing = (LinearLayout) findViewById(R.id.layout_nothing);
    }



}