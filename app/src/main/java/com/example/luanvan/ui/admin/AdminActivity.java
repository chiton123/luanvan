package com.example.luanvan.ui.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.admin_a.AdminAdapter;
import com.example.luanvan.ui.Adapter.admin_a.AdminAdapter_a;
import com.example.luanvan.ui.Model.Admin;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ArrayList<Admin> arrayList;
    GridView gridView;
    AdminAdapter adminAdapter;
    ListView listView;
    AdminAdapter_a adapter ;
    ArrayList<String> arrayListMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        anhxa();
        actionBar();
        eventGrid();
        eventListViewNavigation();


    }

    private void eventListViewNavigation() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        MainActivity.login_admin = 0;
                        Intent intent = new Intent();
                        setResult(RESULT_OK);
                        finish();
                        break;

                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }
    private void eventGrid() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 2:
                        Intent intent = new Intent(getApplicationContext(), JobReviewActivity.class);
                        startActivity(intent);
                        break;


                }
            }
        });

    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    public void addItem(){
        arrayList.add(new Admin(0, "Ứng viên", R.drawable.profilecandidate));
        arrayList.add(new Admin(1, "Nhà tuyển dụng", R.drawable.profilerecruiter));
        arrayList.add(new Admin(2, "Duyệt bài đăng", R.drawable.consider));
        arrayList.add(new Admin(3, "Thống kê", R.drawable.stastitics));

    }
    private void anhxa() {
        arrayListMenu = new ArrayList<>();
        arrayListMenu.add("Logout");
        adapter = new AdminAdapter_a(AdminActivity.this, arrayListMenu);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        gridView = (GridView) findViewById(R.id.gridview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.navi);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        arrayList = new ArrayList<>();
        addItem();
        adminAdapter = new AdminAdapter(AdminActivity.this, arrayList);
        try{
            gridView.setAdapter(adminAdapter);
        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(), "NullPointerException" ,Toast.LENGTH_SHORT).show();
        }



    }
}
