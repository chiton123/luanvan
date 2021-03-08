package com.example.luanvan.ui.schedule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.luanvan.ui.Adapter.recruit.ScheduleAdapter;
import com.example.luanvan.ui.Model.Schedule;
import com.example.luanvan.ui.Model.UserApplicant;
import com.example.luanvan.ui.schedule.CreateScheduleActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScheduleManagementActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    FloatingActionButton buttonAction;
    public static ScheduleAdapter adapter;
    public static ArrayList<Schedule> arrayList;
    int REQUEST_CODE = 123;
    public static int checkLoad = 0; // check xem coi đã vào activity này hay chưa
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_management);
        anhxa();
        actionBar();
        eventButton();
        getData();
    }

    private void getData() {
        checkLoad = 1;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlGetSchedule,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                arrayList.add(new Schedule(
                                        object.getInt("id"),
                                        object.getInt("id_recruiter"),
                                        object.getInt("id_job"),
                                        object.getString("job_name"),
                                        object.getInt("id_user"),
                                        object.getString("username"),
                                        object.getInt("type"),
                                        object.getString("date"),
                                        object.getString("start_hour"),
                                        object.getString("end_hour"),
                                        object.getString("note")

                                ));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();

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
                map.put("id_recruiter", String.valueOf(MainActivity.iduser));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 123 && resultCode == 123){
            adapter.notifyDataSetChanged();
        }
        if(requestCode == 123 && resultCode == 345){
            adapter.notifyDataSetChanged();
            adapter.stopBottomSheet();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void eventButton() {
        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateScheduleActivity.class);
                intent.putExtra("kind", 1); // kind: 1: create, 2: adjust
                startActivityForResult(intent, REQUEST_CODE);
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
        buttonAction = (FloatingActionButton) findViewById(R.id.buttonaction);
        arrayList = new ArrayList<>();
        adapter = new ScheduleAdapter(this, arrayList, this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}
