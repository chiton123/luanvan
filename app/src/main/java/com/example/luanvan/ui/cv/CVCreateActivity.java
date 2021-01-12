package com.example.luanvan.ui.cv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.CVKindAdapter;
import com.example.luanvan.ui.Model.CVKind;

import java.util.ArrayList;

public class CVCreateActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<CVKind> arrayList;
    CVKindAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_v_create);
        anhxa();
        actionBar();
        getKind();



    }

    private void getKind() {
        arrayList.add(new CVKind(1, "Loại 1", R.drawable.cv1));
        arrayList.add(new CVKind(2, "Loại 2", R.drawable.cv2));
        arrayList.add(new CVKind(3, "Loại 3", R.drawable.cv3));

        adapter.notifyDataSetChanged();
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
        adapter = new CVKindAdapter(getApplicationContext(), arrayList, this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

    }
}
