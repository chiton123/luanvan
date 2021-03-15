package com.example.luanvan.ui.recruiter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.example.luanvan.ui.Model.JobList;
import com.example.luanvan.ui.Model.Notification;
import com.example.luanvan.ui.Model.NotificationRecruiter;
import com.example.luanvan.ui.notification.RecruiterNotificationActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CVManageActivity;
import com.example.luanvan.ui.recruiter.PostNews.RecruitmentNewsActivity;
import com.example.luanvan.ui.schedule.ScheduleManagementActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecruiterActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imgCVManagement, imgPostJob, imgSchedule;
    public static TextView txtNotification;
    Handler handler;
    public static ArrayList<NotificationRecruiter> arrayListNotificationRecruiter = new ArrayList<>();
    public static ArrayList<JobList> arrayListJobList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter);
        anhxa();
        actionBar();
        eventCVManagement();
        getDataNotification();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setNotification();
            }
        },3000);




    }


    private void getDataNotification() {
        MainActivity.k = 0;
        arrayListNotificationRecruiter.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlGetNotificationRecruiter,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    arrayListNotificationRecruiter.add(new NotificationRecruiter(
                                            object.getInt("id"),
                                            object.getInt("ap_id"),
                                            object.getInt("job_id"),
                                            object.getString("type_notification"),
                                            object.getInt("type_user"),
                                            object.getInt("id_user"),
                                            object.getString("content"),
                                            object.getInt("status"),
                                            object.getString("img"),
                                            object.getString("date_read"),
                                            object.getInt("ap_status"),
                                            object.getString("ap_note")
                                    ));

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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
                map.put("id_recruiter", String.valueOf(MainActivity.iduser));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recruiter, menu);
        final MenuItem menuItem = menu.findItem(R.id.notification);
        View actionView = menuItem.getActionView();
        txtNotification = actionView.findViewById(R.id.cart_badge);
        txtNotification.setVisibility(View.GONE);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    public void setNotification(){
        for(int i=0; i < arrayListNotificationRecruiter.size(); i++){
            if(arrayListNotificationRecruiter.get(i).getStatus() == 0){
              //  Toast.makeText(getApplicationContext(), arrayListNotificationRecruiter.get(i).getStatus() + "", Toast.LENGTH_SHORT).show();
                MainActivity.k++;
            }
        }
        if(MainActivity.k == 0){
            txtNotification.setVisibility(View.GONE);
        }else {
            txtNotification.setText("" + MainActivity.k);
            txtNotification.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.chat:
                Toast.makeText(getApplicationContext(), "chat", Toast.LENGTH_SHORT).show();
                break;
            case R.id.notification:
                Intent intent = new Intent(getApplicationContext(), RecruiterNotificationActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void eventCVManagement() {
        imgCVManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CVManageActivity.class);
                startActivity(intent);
            }
        });
        imgSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScheduleManagementActivity.class);
                startActivity(intent);
            }
        });
        imgPostJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecruitmentNewsActivity.class);
                startActivity(intent);
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
        imgCVManagement = (ImageView) findViewById(R.id.imgcv);
        imgPostJob = (ImageView) findViewById(R.id.imgpost);
        imgSchedule = (ImageView) findViewById(R.id.imgschedule);
    }
}
