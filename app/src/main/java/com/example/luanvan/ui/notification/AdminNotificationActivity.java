package com.example.luanvan.ui.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.notification_u.AdminNotificationAdapter;
import com.example.luanvan.ui.Adapter.notification_u.NotificationAdapter;
import com.example.luanvan.ui.admin.AdminActivity;
import com.example.luanvan.ui.home.HomeFragment;

public class AdminNotificationActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    AdminNotificationAdapter adapter;
    LinearLayout layout, layout_nothing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification);
        anhxa();
        actionBar();
        if(MainActivity.arrayListAdminNotification.size() == 0){
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
                if(MainActivity.arrayListAdminNotification.size() > 0){
                    adapter.readAll();
                    for(int i=0; i < MainActivity.arrayListAdminNotification.size(); i++){
                        MainActivity.arrayListAdminNotification.get(i).setStatus(1);
                    }
                    adapter.notifyDataSetChanged();
                    MainActivity.k_admin = 0;
                    AdminActivity.txtNotification.setVisibility(View.GONE);
                    break;
                }

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
        adapter = new AdminNotificationAdapter(this, MainActivity.arrayListAdminNotification, this);
        recyclerView.setAdapter(adapter);
        layout = (LinearLayout) findViewById(R.id.layout);
        layout_nothing = (LinearLayout) findViewById(R.id.layout_nothing);

    }

}
