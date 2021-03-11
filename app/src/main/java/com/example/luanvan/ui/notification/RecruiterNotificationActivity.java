package com.example.luanvan.ui.notification;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.notification_u.NotificationAdapter;
import com.example.luanvan.ui.Adapter.notification_u.RecruiterNotificationAdapter;
import com.example.luanvan.ui.Model.Notification;
import com.example.luanvan.ui.recruiter.RecruiterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecruiterNotificationActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    RecruiterNotificationAdapter adapter;
    LinearLayout layout, layout_nothing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_notification);
        anhxa();
        actionBar();
        if(RecruiterActivity.arrayListNotificationRecruiter.size() == 0){
            layout_nothing.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }else {
            layout_nothing.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification_read, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.read:
                if(RecruiterActivity.arrayListNotificationRecruiter.size() > 0){
                    adapter.readAll();
                    for(int i=0; i < RecruiterActivity.arrayListNotificationRecruiter.size(); i++){
                        RecruiterActivity.arrayListNotificationRecruiter.get(i).setStatus(1);
                    }
                    adapter.notifyDataSetChanged();
                    MainActivity.k = 0;
                    RecruiterActivity.txtNotification.setVisibility(View.GONE);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
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
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new RecruiterNotificationAdapter(this, RecruiterActivity.arrayListNotificationRecruiter, this,1);
        recyclerView.setAdapter(adapter);
        layout = (LinearLayout) findViewById(R.id.layout);
        layout_nothing = (LinearLayout) findViewById(R.id.layout_nothing);

    }
}
