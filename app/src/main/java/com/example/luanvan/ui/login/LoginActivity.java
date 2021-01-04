package com.example.luanvan.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;

import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.ViewPageAdapter;
import com.example.luanvan.ui.fragment.LoginFragment;
import com.example.luanvan.ui.fragment.RegisterFragment;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhxa();
        actionBar();



    }
    public void Switch1(){
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.select();
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
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoginFragment(), "Đăng nhập");
        adapter.addFragment(new RegisterFragment(), "Đăng ký");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
