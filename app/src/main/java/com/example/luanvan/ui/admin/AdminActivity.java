package com.example.luanvan.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
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
import com.example.luanvan.ui.Adapter.admin_a.AdminAdapter;
import com.example.luanvan.ui.Adapter.admin_a.AdminAdapter_a;
import com.example.luanvan.ui.Adapter.admin_a.InfoAdapter;
import com.example.luanvan.ui.Model.Admin;
import com.example.luanvan.ui.Model.NotificationAdmin;
import com.example.luanvan.ui.Model.NotificationRecruiter;
import com.example.luanvan.ui.notification.AdminNotificationActivity;
import com.example.luanvan.ui.notification.RecruiterNotificationActivity;
import com.example.luanvan.ui.recruiter.RecruiterActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ArrayList<Admin> arrayList;
    GridView gridView;
    AdminAdapter adminAdapter;
    ListView listView;
    InfoAdapter infoAdapter ;
    ArrayList<Admin> arrayListMenu;
    public static TextView txtNotification;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        anhxa();
        actionBar();
        eventGrid();
        eventListViewNavigation();
        getDataNotification();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setNotification();
            }
        },1500);

    }

    public void setNotification(){
        for(int i=0; i < MainActivity.arrayListAdminNotification.size(); i++){
            if(MainActivity.arrayListAdminNotification.get(i).getStatus() == 0){
                //  Toast.makeText(getApplicationContext(), arrayListNotificationRecruiter.get(i).getStatus() + "", Toast.LENGTH_SHORT).show();
                MainActivity.k_admin++;
            }
        }
        //  Toast.makeText(getApplicationContext(), MainActivity.k + " k", Toast.LENGTH_SHORT).show();
        if(MainActivity.k_admin == 0){
            txtNotification.setVisibility(View.GONE);
        }else {
            txtNotification.setText("" + MainActivity.k_admin);
            txtNotification.setVisibility(View.VISIBLE);
        }

    }
    private void getDataNotification() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.getUrlGetNotificationAdmin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    MainActivity.arrayListAdminNotification.add(new NotificationAdmin(
                                            object.getInt("id"),
                                            object.getInt("job_id"),
                                            object.getString("type_notification"),
                                            object.getInt("type_user"),
                                            object.getInt("id_user"),
                                            object.getString("content"),
                                            object.getInt("status"),
                                            object.getString("img"),
                                            object.getString("date_read")
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
        getMenuInflater().inflate(R.menu.menu_admin, menu);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.notification:
                Intent intent = new Intent(getApplicationContext(), AdminNotificationActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void eventListViewNavigation() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        MainActivity.login_admin = 0;
                        MainActivity.arrayListAdminNotification.clear();
                        MainActivity.k_admin = 0;
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
                    case 0:
                        Intent intent1 = new Intent(getApplicationContext(), UserManagementActivity.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2 = new Intent(getApplicationContext(), RecruiterManagementActivity.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent = new Intent(getApplicationContext(), JobReviewActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getApplicationContext(), CompanyListActivity.class);
                        startActivity(intent3);
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
        arrayList.add(new Admin(3, "Công ty", R.drawable.company_a));

    }
    private void anhxa() {
        arrayListMenu = new ArrayList<>();
        arrayListMenu.add(new Admin(1, "Đăng xuất", R.drawable.draw_signout));
        infoAdapter = new InfoAdapter(AdminActivity.this, arrayListMenu);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(infoAdapter);
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
