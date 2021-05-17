package com.example.luanvan.ui.recruiter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.admin_a.AdminAdapter;
import com.example.luanvan.ui.Adapter.admin_a.AdminAdapter_a;
import com.example.luanvan.ui.Adapter.admin_a.InfoAdapter;
import com.example.luanvan.ui.Model.Admin;
import com.example.luanvan.ui.Model.Chat;
import com.example.luanvan.ui.Model.JobList;
import com.example.luanvan.ui.Model.NotificationRecruiter;
import com.example.luanvan.ui.User.ChangePasswordActivity;
import com.example.luanvan.ui.chat.UserListActivity;
import com.example.luanvan.ui.notification.RecruiterNotificationActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CVManageActivity;
import com.example.luanvan.ui.recruiter.PostNews.RecruitmentNewsActivity;
import com.example.luanvan.ui.recruiter.search_r.SearchCandidateActivity;
import com.example.luanvan.ui.recruiter.updateInfo.UpdateCompanyActivity;
import com.example.luanvan.ui.recruiter.updateInfo.UpdateRecruiterActivity;
import com.example.luanvan.ui.schedule.ScheduleManagementActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecruiterActivity extends AppCompatActivity {
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
    public static ArrayList<NotificationRecruiter> arrayListNotificationRecruiter = new ArrayList<>();
    public static ArrayList<JobList> arrayListJobList = new ArrayList<>();
    public static ArrayList<JobList> arrayListAuthenticationJobs = new ArrayList<>();
    public static ArrayList<JobList> arrayListRejectJobs = new ArrayList<>();
    public static ArrayList<JobList> arrayListOutdatedJobs = new ArrayList<>();
    public static TextView txtUnreadMessageNumber;
    DatabaseReference reference;
    CircleImageView img;
    TextView txtUsername;
    LinearLayout layout_user;
    int REQUEST_CODE_UPDATRINFO = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter);
        anhxa();
        actionBar();
        eventGridView();
        getDataNotification();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setNotification();
            }
        },2000);
        eventListViewNavigation();
        activateAfterLogin();


    }
    public void activateAfterLogin(){
        toolbar.setTitle("");
        layout_user.setVisibility(View.VISIBLE);
        img.setImageResource(R.drawable.userprofile);
        txtUsername.setText(MainActivity.recruiter.getName());

    }
    private void eventListViewNavigation() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        logOut();
                        Intent intent = new Intent();
                        setResult(RESULT_OK);
                        finish();
                        break;
                    case 1:
                        Intent intent1 = new Intent(getApplicationContext(), UpdateRecruiterActivity.class);
                        startActivityForResult(intent1, REQUEST_CODE_UPDATRINFO);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                        startActivity(intent2);

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_UPDATRINFO && resultCode == 123){
            activateAfterLogin();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void logOut(){
        MainActivity.login_recruiter = 0;
        arrayListAuthenticationJobs.clear();
        arrayListRejectJobs.clear();
        arrayListJobList.clear();
        arrayListNotificationRecruiter.clear();
        arrayListOutdatedJobs.clear();
        MainActivity.email_recruiter = "";
        arrayListMenu.clear();
        arrayList.clear();
    }
    private void getDataNotification() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlGetNotificationRecruiter,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
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
                                        object.getInt("kind"),
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        FancyToast.makeText(getApplicationContext(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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

        final MenuItem menuItem1 = menu.findItem(R.id.chat);
        View actionViewChat = menuItem1.getActionView();

        txtUnreadMessageNumber =  actionViewChat.findViewById(R.id.cart_badge_chat);
        txtUnreadMessageNumber.setVisibility(View.GONE);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        actionViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem1);
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
      //  Toast.makeText(getApplicationContext(), MainActivity.k + " k", Toast.LENGTH_SHORT).show();
        if(MainActivity.k == 0){
            txtNotification.setVisibility(View.GONE);
        }else {
            txtNotification.setText("" + MainActivity.k);
            txtNotification.setVisibility(View.VISIBLE);
        }

        getDataChat();

    }

    private void getDataChat() {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        final ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MainActivity.k_chat = 0;
                for(DataSnapshot x : snapshot.getChildren()){
                    Chat chat = x.getValue(Chat.class);
                    if(chat.getReceiver().equals(MainActivity.uid) &&  !chat.isIsseen()){
                        MainActivity.k_chat++;
                    }
                }
                if(MainActivity.k_chat > 0){
                    txtUnreadMessageNumber.setVisibility(View.VISIBLE);
                    txtUnreadMessageNumber.setText(MainActivity.k_chat + "");
                }else {
                    txtUnreadMessageNumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        reference.addValueEventListener(valueEventListener);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reference.removeEventListener(valueEventListener);
            }
        },1300);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.chat:
                Intent intent1 = new Intent(getApplicationContext(), UserListActivity.class);
                startActivity(intent1);
                break;
            case R.id.notification:
                Intent intent = new Intent(getApplicationContext(), RecruiterNotificationActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void eventGridView() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(getApplicationContext(), CVManageActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getApplicationContext(), RecruitmentNewsActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getApplicationContext(), ScheduleManagementActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getApplicationContext(), AssessmentManagementActivity.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(getApplicationContext(), UpdateCompanyActivity.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(getApplicationContext(), SearchCandidateActivity.class);
                        startActivity(intent5);
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
        arrayList.add(new Admin(0, "Hồ sơ ứng tuyển", R.drawable.cv_recruiter));
        arrayList.add(new Admin(1, "Tin tuyển dụng", R.drawable.news));
        arrayList.add(new Admin(2, "Lịch hẹn", R.drawable.schedule));
        arrayList.add(new Admin(3, "Quản lý đánh giá", R.drawable.comment));
        arrayList.add(new Admin(4, "Cập nhật công ty", R.drawable.company));
        arrayList.add(new Admin(5, "Tìm kiếm ứng viên", R.drawable.searchclient));

    }
    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        arrayListMenu = new ArrayList<>();
        arrayListMenu.add(new Admin(1, "Đăng xuất", R.drawable.draw_signout));
        arrayListMenu.add(new Admin(2, "Hồ sơ", R.drawable.draw_profile));
        arrayListMenu.add(new Admin(3, "Đổi mật khẩu", R.drawable.draw_password_reset));
        infoAdapter = new InfoAdapter(RecruiterActivity.this, arrayListMenu);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(infoAdapter);
        gridView = (GridView) findViewById(R.id.gridview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.navi);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        arrayList = new ArrayList<>();
        addItem();
        adminAdapter = new AdminAdapter(RecruiterActivity.this, arrayList);
        try{
            gridView.setAdapter(adminAdapter);
        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(), "NullPointerException" ,Toast.LENGTH_SHORT).show();
        }
        img = (CircleImageView) findViewById(R.id.img);
        txtUsername = (TextView) findViewById(R.id.txtusername);
        layout_user = (LinearLayout) findViewById(R.id.layout_user);
    }
}
