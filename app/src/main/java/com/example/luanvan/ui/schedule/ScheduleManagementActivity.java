package com.example.luanvan.ui.schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.luanvan.R;
import com.example.luanvan.ui.schedule.CreateScheduleActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ScheduleManagementActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    FloatingActionButton buttonAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_management);
        anhxa();
        actionBar();
        eventButton();


    }

    private void eventButton() {
        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateScheduleActivity.class);
                startActivity(intent);
            }
        });

    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        buttonAction = (FloatingActionButton) findViewById(R.id.buttonaction);
    }
}
