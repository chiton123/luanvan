package com.example.luanvan.ui.DetailedJob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    int REQUEST_CODE_CV = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_job);
        anhxa();
        actionBar();
        getInfo();
        eventApply();


    }

    public void showDialog(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_apply);
        dialog.setCancelable(false);

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
                }else {


                }
            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkCV == 1){
                    Toast.makeText(getApplicationContext(), "Vui lòng tạo CV", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(), ChooseCVActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_CV);

                }
            }
        });

        dialog.show();

    }
    private void eventApply() {
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.login == 0){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    showDialog();
                }

            }
        });

    }

    private void getInfo() {
        Job job = (Job) getIntent().getSerializableExtra("job");
        Glide.with(getApplicationContext()).load(job.getImg()).into(anhcongty);
        txttencongviec.setText(job.getName());
        txtcongty.setText(job.getCompany_name());
        String ngay = job.getDate();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = fmt.parse(ngay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
        txthannop.setText(fmtOut.format(date));


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
