package com.example.luanvan.ui.DetailedJob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.bumptech.glide.Glide;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.ViewPageAdapter;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.JobPost;
import com.example.luanvan.ui.Model.Schedule;
import com.example.luanvan.ui.admin.JobReviewActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class DetailJobAdminActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnConfirm, btnReject;
    ImageView anhcongty;
    TextView txttencongviec, txtcongty, txthannop;
    TextView txtsalary, txtarea, txthinhthuc, txtnumber, txtexperience, txtdescription, txtbenefit, txtrequirement;
    JobPost jobPost;
    Handler handler;
    ProgressDialog progressDialog;
    int job_id = 0, position = 0; // position để cập nhật lại bên kia
    int status_post = 0;
    String note_reject = "";
    BottomSheetDialog bottomSheetDialog;
    String type_notification = "";
    String content = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_job_admin);
        anhxa();
        actionBar();
        getInfo();
        eventButton();

    }

    private void eventButton() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status_post == 1){
                    loading();
                    acceptOrRejectJob(0);
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Intent intent = new Intent();
                            setResult(123);
                            finish();
                        }
                    },2000);
                }else {
                    FancyToast.makeText(getApplicationContext(), "Bạn đã xác nhận rồi", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }

            }
        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status_post == 1){
                    rejectDialog();
                }else {
                    FancyToast.makeText(getApplicationContext(), "Bạn đã xác nhận rồi", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }


            }
        });

    }

    private void postNotification(final int type_user) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlPostNotificationForAdmin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                         //   Toast.makeText(getApplicationContext(), "Thông báo thành công", Toast.LENGTH_SHORT).show();
                        }else {

                    FancyToast.makeText(getApplicationContext(), "Thông báo thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
                map.put("type_user", String.valueOf(type_user));
                map.put("type_notification",  type_notification);
                map.put("iduser", String.valueOf(jobPost.getId_recruiter()));
                map.put("content", content);
                map.put("idjob", String.valueOf(job_id));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }




    public void rejectDialog(){
        bottomSheetDialog = new BottomSheetDialog(DetailJobAdminActivity.this, R.style.BottomSheetTheme);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_job_reject, (ViewGroup)findViewById(R.id.bottom_sheet));
        final EditText editNote = (EditText) view.findViewById(R.id.editnote);
        Button btnConfirm = (Button) view.findViewById(R.id.buttonxacnhan);
        Button btnCancel = (Button) view.findViewById(R.id.buttonhuy);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editNote.getText().toString().equals("")){
                    FancyToast.makeText(getApplicationContext(), "Vui lòng nhập lý do từ chối", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else {
                    loading();
                    note_reject = editNote.getText().toString();
                    acceptOrRejectJob(2);
                    bottomSheetDialog.dismiss();
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Intent intent = new Intent();
                            setResult(123);
                            finish();
                        }
                    },2000);

                }
            }
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

    }

    public void acceptOrRejectJob(final int status){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlAcceptJob,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            FancyToast.makeText(getApplicationContext(), "Cập nhật thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            JobReviewActivity.jobPostArrayList.get(position).setStatus_post(status);
                            JobReviewActivity.jobPostArrayList.get(position).setNote_reject(note_reject);
                            if(status == 0){
                                type_notification = "Tin tuyển dụng được duyệt";
                                content = "Vị trí " + jobPost.getName();
                            }else if(status == 2) {
                                type_notification = "Tin tuyển dụng bị từ chối";
                                content = "Vị trí " + jobPost.getName();
                            }
                            postNotification(1);

                        }else {
                            FancyToast.makeText(getApplicationContext(),"Cập nhật thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
                map.put("job_id", String.valueOf(job_id));
                map.put("status", String.valueOf(status));
                map.put("note_reject", note_reject);
                return map;
            }
        };
        requestQueue.add(stringRequest);


    }

    private void getInfo() {
        jobPost = (JobPost) getIntent().getSerializableExtra("job");
        position = getIntent().getIntExtra("position", 0);
        job_id = jobPost.getId();
        status_post = jobPost.getStatus_post();
        if(status_post != 1){
            btnReject.setBackgroundResource(R.drawable.button_black);
            btnConfirm.setBackgroundResource(R.drawable.button_black);
            btnConfirm.setClickable(false);
            btnReject.setClickable(false);
            btnReject.setTextColor(Color.WHITE);
        }
       // Toast.makeText(getApplicationContext(), jobPost.getAddress(), Toast.LENGTH_SHORT);
        Glide.with(getApplicationContext()).load(jobPost.getImg()).into(anhcongty);
        txttencongviec.setText(jobPost.getName());
        txtcongty.setText(jobPost.getCompany_name());
        String ngaybatdau = jobPost.getStart_date();
        String ngayketthuc = jobPost.getEnd_date();

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = fmt.parse(ngaybatdau);
            date2 = fmt.parse(ngayketthuc);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
        txthannop.setText(fmtOut.format(date2));
        txtarea.setText(jobPost.getAddress());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtsalary.setText("Từ " + decimalFormat.format( + jobPost.getSalary_min()) + "đ đến " + decimalFormat.format(jobPost.getSalary_max()) + "đ" );
        txthinhthuc.setText(jobPost.getType_job());
        txtnumber.setText(jobPost.getNumber()+"");
        txtexperience.setText(jobPost.getExperience());

        String mota = xuongdong(jobPost.getDescription());
        String yeucau = xuongdong(jobPost.getRequirement());
        String quyenloi = xuongdong(jobPost.getBenefit());
        txtdescription.setText(mota);
        txtrequirement.setText(yeucau);
        txtbenefit.setText(quyenloi);

    }
    public String xuongdong(String text){
        String text1 = text.replaceAll("\\s\\s+", " ").trim();
        String ketqua = "";
        if(text.contains(".")){
            String[] split = text1.split(Pattern.quote("."));
            for(String item : split){
                ketqua += "- " +  item + "\n";
            }
            return ketqua;
        }else {
            return text;
        }
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
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        anhcongty = (ImageView) findViewById(R.id.hinhanh);
        txtcongty = (TextView) findViewById(R.id.tencongty);
        txttencongviec = (TextView) findViewById(R.id.tencongviec);
        txthannop = (TextView) findViewById(R.id.hannop);
        txtarea = (TextView) findViewById(R.id.area);
        txtsalary = (TextView)findViewById(R.id.salary);
        txthinhthuc = (TextView)findViewById(R.id.hinhthuc);
        txtnumber = (TextView)findViewById(R.id.number);
        txtexperience = (TextView)findViewById(R.id.experience);
        txtdescription = (TextView) findViewById(R.id.motacongviec);
        txtbenefit = (TextView) findViewById(R.id.quyenloi);
        txtrequirement = (TextView) findViewById(R.id.yeucaucongviec);
        btnConfirm = (Button) findViewById(R.id.buttonxacnhan);
        btnReject = (Button) findViewById(R.id.buttontuchoi);

    }

}
