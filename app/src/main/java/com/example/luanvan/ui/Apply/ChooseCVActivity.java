package com.example.luanvan.ui.Apply;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import com.example.luanvan.ui.Adapter.cv_all.CVChooseAdapter;
import com.example.luanvan.ui.DetailedJob.DetailJobActivity;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.Job_Apply;
import com.example.luanvan.ui.home.HomeFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChooseCVActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    CVChooseAdapter adapter;
    Button btnBack, btnApply;
    int check = 0; // chọn chưa, chọn rồi thì 1
    int positionCV = 0;
    int id_application = 0; // sau khi ứng tuyển xong thì lấy về
    String type_notification = "Hồ sơ mới ứng tuyển";
    String content = "";
    Handler handler;
    ProgressDialog progressDialog;
    Job job;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_c_v);
        anhxa();
        actionBar();
        eventChoose();
        eventButton();


    }
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    private void postNotification(final int type_user) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlPostNotification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(getApplicationContext(), "Thông báo thành công", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getApplicationContext(), "Thông báo thất bại", Toast.LENGTH_SHORT).show();
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
                map.put("iduser", String.valueOf(job.getId_recruiter()));
                map.put("content", content);
                map.put("id_application", String.valueOf(id_application));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
    public void showAlert(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Bạn có chắc chắn muốn ứng tuyển công việc này không ?");
        alert.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.setPositiveButton("Ứng tuyển", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int status = job.getStatus();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = fmt.parse(job.getEnd_date());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(status == 0 && date.after(Calendar.getInstance().getTime())){
                    if(DetailJobActivity.checkApplyAgain == 0){
                        apply();
                    }else {
                        applyAgain();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Nhà tuyển dụng đã dừng tuyển", Toast.LENGTH_SHORT).show();
                }

            }
        });

        alert.show();
    }
    public void applyAgain(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlApplyAgain,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        if(!response.equals("fail")){
                            loading();
                            Toast.makeText(getApplicationContext(), "Ứng tuyển thành công", Toast.LENGTH_SHORT).show();
                            int k = response.lastIndexOf('s');
                            id_application = Integer.parseInt(response.substring(k+1, response.length()));
                            content = "Ứng viên " + MainActivity.username + " - " + job.getName();
                            // Toast.makeText(getApplicationContext(), id_application + content , Toast.LENGTH_SHORT).show();
                            postNotification(1);
                            if(DetailJobActivity.checkApplyAgain == 1){
                                // remove job trong list đã ứng tuyển
                                for(int i=0; i < HomeFragment.arrayListDaUngTuyen.size(); i++){
                                    if(HomeFragment.arrayListDaUngTuyen.get(i).getId() == job.getId()){
                                        HomeFragment.arrayListDaUngTuyen.remove(i);
                                    }
                                }
                            }

                            HomeFragment.arrayListDaUngTuyen.add(new Job_Apply(
                                    job.getId(),
                                    job.getName(),
                                    job.getIdcompany(),
                                    job.getId_recruiter(),
                                    MainActivity.arrayListCV.get(0).getKey(),
                                    job.getImg(),
                                    job.getAddress(),
                                    job.getIdtype(),
                                    job.getIdprofession(),
                                    job.getStart_date(),
                                    job.getEnd_date(),
                                    job.getSalary_min(),
                                    job.getSalary_max(),
                                    job.getIdarea(),
                                    job.getExperience(),
                                    job.getNumber(),
                                    job.getDescription(),
                                    job.getRequirement(),
                                    job.getBenefit(),
                                    job.getStatus(),
                                    job.getCompany_name(),
                                    job.getType_job()
                            ));
                            HomeFragment.adapterDaUngTuyen.notifyDataSetChanged();
                            handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent();
                                    setResult(123);
                                    finish();
                                }
                            },1500);
                        }else {
                            Toast.makeText(getApplicationContext(), "Ứng tuyển thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("job_id", String.valueOf(DetailJobActivity.job_id));
                map.put("user_id", String.valueOf(MainActivity.user.getId()));
                map.put("user_id_f", MainActivity.uid);
                map.put("cv_id", MainActivity.arrayListCV.get(0).getKey());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void apply(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlApply,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("fail")){
                            loading();
                            Toast.makeText(getApplicationContext(), "Ứng tuyển thành công", Toast.LENGTH_SHORT).show();
                            int k = response.lastIndexOf('s');
                            id_application = Integer.parseInt(response.substring(k+1, response.length()));
                            content = "Ứng viên " + MainActivity.username + " - " + job.getName();
                            // Toast.makeText(getApplicationContext(), id_application + content , Toast.LENGTH_SHORT).show();
                            postNotification(1);

                            HomeFragment.arrayListDaUngTuyen.add(new Job_Apply(
                                    job.getId(),
                                    job.getName(),
                                    job.getIdcompany(),
                                    job.getId_recruiter(),
                                    MainActivity.arrayListCV.get(0).getKey(),
                                    job.getImg(),
                                    job.getAddress(),
                                    job.getIdtype(),
                                    job.getIdprofession(),
                                    job.getStart_date(),
                                    job.getEnd_date(),
                                    job.getSalary_min(),
                                    job.getSalary_max(),
                                    job.getIdarea(),
                                    job.getExperience(),
                                    job.getNumber(),
                                    job.getDescription(),
                                    job.getRequirement(),
                                    job.getBenefit(),
                                    job.getStatus(),
                                    job.getCompany_name(),
                                    job.getType_job()
                            ));
                            HomeFragment.adapterDaUngTuyen.notifyDataSetChanged();
                            handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent();
                                    setResult(123);
                                    finish();
                                }
                            },1500);
                        }else {
                            Toast.makeText(getApplicationContext(), "Ứng tuyển thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("job_id", String.valueOf(DetailJobActivity.job_id));
                map.put("user_id", String.valueOf(MainActivity.user.getId()));
                map.put("user_id_f", MainActivity.uid);
                map.put("cv_id", MainActivity.arrayListCV.get(0).getKey());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void eventButton() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check == 0){
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn CV", Toast.LENGTH_SHORT).show();
                }else {
                    showAlert();
                }
            }
        });

    }

    private void eventChoose() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                check = 1;
                positionCV = position;
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
        job = (Job) getIntent().getSerializableExtra("job");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new CVChooseAdapter(getApplicationContext(), MainActivity.arrayListCV, this);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        btnApply = (Button) findViewById(R.id.buttonungtuyen);
        btnBack = (Button) findViewById(R.id.buttonquaylai);

    }
}
