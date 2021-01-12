package com.example.luanvan.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.ViewPageAdapter;
import com.example.luanvan.ui.fragment.cv_f.CVFragment;
import com.example.luanvan.ui.fragment.cv_f.CoverLetterFragment;
import com.google.android.material.tabs.TabLayout;

public class DashboardFragment extends Fragment {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPageAdapter adapter;
    ViewPager viewPager;
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout)view.findViewById(R.id.tablayout);
        adapter = new ViewPageAdapter(getParentFragmentManager());
        adapter.addFragment(new CVFragment(), "CV");
        adapter.addFragment(new CoverLetterFragment(),"COVER LETTER");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);




        return view;
    }


}
