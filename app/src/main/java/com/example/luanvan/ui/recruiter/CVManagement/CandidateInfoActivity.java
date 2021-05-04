package com.example.luanvan.ui.recruiter.CVManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
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
import com.example.luanvan.ui.Adapter.recruit.ProfileCadidateAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.ProfileAdapter;
import com.example.luanvan.ui.Model.Applicant;
import com.example.luanvan.ui.Model.Pdf;
import com.example.luanvan.ui.Model.Profile;
import com.example.luanvan.ui.modelCV.PdfCV;
import com.example.luanvan.ui.recruiter.RecruiterActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CandidateInfoActivity extends AppCompatActivity {
    Toolbar toolbar;
    WebView webView;
    Applicant applicant = new Applicant();
    int cv_id = 0;
    // để gửi thông báo cho ứng viên
    String user_id_f = "";
    int candidate_id = 0;
    String content = "";
    int id_application = 0;
    String type_notification = "Nhà tuyển dụng vừa xem hồ sơ";
    String url1 = "https://docs.google.com/gview?embedded=true&url=";
    // name cv
    String name_cv = "";
    String url_cv = "";
    Handler handler;
    RecyclerView recyclerView;
    public static ProfileCadidateAdapter adapter;
    ArrayList<Profile> arrayList;
    int kind = 0;
    int position = 0; // vị trí của 1 trong 3 mảng ở cvmanagementactivity
    // để load lại 3 fragment phía trước -> kind, statusapplication
    public static int status_application = 0;



    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_info);
        anhxa();
        actionBar();




    }
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    private void eventPDF() {
        webView.requestFocus();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);

        MainActivity.mData.child("cv").child(user_id_f).child(String.valueOf(cv_id)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String url = snapshot.child("url").getValue(String.class);
                url1 += url;
                url_cv = snapshot.child("url").getValue(String.class);;
                name_cv = snapshot.child("name").getValue(String.class) + applicant.getUsername();

                //  Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(url1);
            }
        },3000);

    }



    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("kind", kind);
                intent.putExtra("status", status_application);
                setResult(123, intent);
                finish();
            }
        });
    }


    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        webView = (WebView) findViewById(R.id.webview);

        arrayList = new ArrayList<>();
        arrayList.add(new Profile(1, "Đánh giá ứng viên", R.drawable.star));
        arrayList.add(new Profile(2, "Đặt lịch hẹn ứng viên", R.drawable.calendar));
        arrayList.add(new Profile(3, "Tải CV PDF", R.drawable.download));
        loading();
        // get info
        applicant = (Applicant) getIntent().getSerializableExtra("applicant");
        kind = getIntent().getIntExtra("kind", 0);
        position = getIntent().getIntExtra("position", 0);
        cv_id = applicant.getCv_id();
        user_id_f = applicant.getUser_id_f();
        candidate_id = applicant.getUser_id();
        id_application = applicant.getId();
        eventPDF();
        content = RecruiterActivity.arrayListJobList.get(0).getCompany_name() + " đã xem CV ứng tuyển của bạn";
        if(applicant.getStatus() == 0){
            checkPostOrNot();
        }

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setRecyclerView();
                progressDialog.dismiss();
            }
        },3000);





    }
    public void setRecyclerView(){
        adapter = new ProfileCadidateAdapter(this, arrayList, this, applicant, kind, position, url_cv, name_cv);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void checkPostOrNot() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlCheckPost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                           // Toast.makeText(getApplicationContext(), "Đã thông báo", Toast.LENGTH_SHORT).show();
                        }else {
                           // Toast.makeText(getApplicationContext(), "Thông báo thất bại", Toast.LENGTH_SHORT).show();
                            postNotification(0, content); // 0: candidate, 1: recruiter
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
                map.put("iduser", String.valueOf(candidate_id));
                map.put("id_application", String.valueOf(id_application));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void postNotification(final int type_user, final String content) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlPostNotification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                         //   Toast.makeText(getApplicationContext(), "Thông báo thành công", Toast.LENGTH_SHORT).show();
                        }else {
                        //    Toast.makeText(getApplicationContext(), "Thông báo thất bại", Toast.LENGTH_SHORT).show();
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
                map.put("type_user", String.valueOf(type_user));
                map.put("type_notification",  type_notification);
                map.put("iduser", String.valueOf(candidate_id));
                map.put("content", content);
                map.put("id_application", String.valueOf(id_application));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
}
