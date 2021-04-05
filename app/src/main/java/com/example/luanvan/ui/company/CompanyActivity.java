package com.example.luanvan.ui.company;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.ViewPageAdapter;
import com.example.luanvan.ui.Model.Company;
import com.example.luanvan.ui.fragment.company_f.AssessmentFragment;
import com.example.luanvan.ui.fragment.company_f.CompanyRecruitmentFragment;
import com.example.luanvan.ui.fragment.company_f.IntroductionCompanyFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

public class CompanyActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPageAdapter adapter;
    ImageView img;
    TextView txtCompanyName;
    Company company;
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        anhxa();
        actionBar();
        getData();


    }

    private void getData() {
        company = (Company) getIntent().getSerializableExtra("company");
        if(company != null){
            txtCompanyName.setText(company.getName());
            Glide.with(getApplicationContext()).load(company.getImage()).into(img);
        }

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
                    collapsingToolbarLayout.setTitle("Công ty");
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
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new IntroductionCompanyFragment(), "GIỚI THIỆU");
        adapter.addFragment(new CompanyRecruitmentFragment(), "TUYỂN DỤNG");
        adapter.addFragment(new AssessmentFragment(), "ĐÁNH GIÁ");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        txtCompanyName = (TextView) findViewById(R.id.tencongty);
        img = (ImageView) findViewById(R.id.img);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbarcollapse);

    }
}
