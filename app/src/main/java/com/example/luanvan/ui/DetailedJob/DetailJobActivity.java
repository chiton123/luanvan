package com.example.luanvan.ui.DetailedJob;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.example.luanvan.ui.Apply.ChooseCVActivity;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.fragment.job_f.CompanyFragment;
import com.example.luanvan.ui.fragment.job_f.InfoFragment;
import com.example.luanvan.ui.fragment.job_f.RelevantJobFragment;
import com.example.luanvan.ui.login.LoginActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailJobActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnApply, btnChange, btnUse;
    ImageView anhcongty;
    TextView txttencongviec, txtcongty, txthannop;
    TabLayout tabLayout;
    ViewPageAdapter viewPageAdapter;
    ViewPager viewPager;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView txtCV, txtName, txtEmail, txtPhone;
    int checkCV = 0; // có cv thì 0, k có thì 1
    int REQUEST_CODE_CV = 123, REQUEST_CODE_LOGIN = 222;
    public static int job_id = 0;
    Dialog dialog;
  //  int checkApply = 0; // khi ứng tuyển, xem coi thành công hay thất bại rồi thông báo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_job);
        anhxa();
        actionBar();
        getInfo();
        eventApply();
        if(MainActivity.login == 1){
            checkApplyOrNot();
        }



    }

    private void checkApplyOrNot() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlCheckApply,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            btnApply.setClickable(false);
                            btnApply.setBackgroundColor(Color.BLACK);
                            btnApply.setTextColor(Color.WHITE);
                            btnApply.setText("Đã ứng tuyển");
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
                map.put("job_id", String.valueOf(job_id));
                map.put("user_id", String.valueOf(MainActivity.iduser));
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
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlApply,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("success")){
                                    Toast.makeText(getApplicationContext(), "Ứng tuyển thành công", Toast.LENGTH_SHORT).show();
                                    checkApplyOrNot();
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
        });

        alert.show();
    }
    public void showDialog(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_apply);
//        dialog.setCancelable(false);

        txtName = (TextView) dialog.findViewById(R.id.txtname);
        txtPhone = (TextView) dialog.findViewById(R.id.txtphone);
        txtEmail = (TextView) dialog.findViewById(R.id.txtemail);
        txtCV = (TextView) dialog.findViewById(R.id.txtcv);
        btnChange = (Button) dialog.findViewById(R.id.buttonthaydoi);
        btnUse = (Button) dialog.findViewById(R.id.buttonsudung);
        txtName.setText(MainActivity.user.getName());
        txtEmail.setText(MainActivity.user.getEmail());
        txtPhone.setText(MainActivity.user.getPhone() + "");
        if(MainActivity.arrayListCV.size() > 0){
            txtCV.setText(MainActivity.arrayListCV.get(0).getName());
        }else {
            txtCV.setText("Bạn chưa có CV, vui lòng tạo");
            checkCV = 1;
        }
        btnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkCV == 1){
                    Toast.makeText(getApplicationContext(), "Vui lòng tạo CV", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else {
                    showAlert();
                    dialog.dismiss();
                }
            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkCV == 1){
                    Toast.makeText(getApplicationContext(), "Vui lòng tạo CV", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else {
                    Intent intent = new Intent(getApplicationContext(), ChooseCVActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_CV);

                }
            }
        });

        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_LOGIN && resultCode == 123){
            checkApplyOrNot();
        }
        if(requestCode == REQUEST_CODE_CV && resultCode == 123){
            checkApplyOrNot();
            dialog.dismiss();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void eventApply() {
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.login == 0){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_LOGIN);
                }else {
                    showDialog();
                }
            }
        });

    }

    private void getInfo() {
        Job job = (Job) getIntent().getSerializableExtra("job");
        job_id = job.getId();
        Glide.with(getApplicationContext()).load(job.getImg()).into(anhcongty);
        txttencongviec.setText(job.getName());
        txtcongty.setText(job.getCompany_name());
        String ngaybatdau = job.getStart_date();
        String ngayketthuc = job.getEnd_date();
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


    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Chi tiết việc làm");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle("");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });


    }


    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbarcollapse);
        anhcongty = (ImageView) findViewById(R.id.hinhanh);
        txtcongty = (TextView) findViewById(R.id.tencongty);
        txttencongviec = (TextView) findViewById(R.id.tencongviec);
        txthannop = (TextView) findViewById(R.id.hannop);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        setUpFragment();
        tabLayout.setupWithViewPager(viewPager);
        btnApply = (Button) findViewById(R.id.buttonungtuyen);


    }

    private void setUpFragment() {
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPageAdapter.addFragment(new InfoFragment(), "Thông tin");
        viewPageAdapter.addFragment(new CompanyFragment(), "Công ty");
        viewPageAdapter.addFragment(new RelevantJobFragment(), "Việc liên quan");
        viewPager.setAdapter(viewPageAdapter);
    }
}
