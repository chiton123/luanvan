package com.example.luanvan.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.cv.CVIntroductionActivity;
import com.example.luanvan.ui.login.LoginActivity;

public class DashboardFragment extends Fragment {
    Button btnUse;
    int REQUEST_CODE = 123;
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        btnUse = (Button) view.findViewById(R.id.buttonuse);
        btnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.login == 0){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }else {
                    Intent intent = new Intent(getActivity(), CVIntroductionActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });

        return view;

    }





}
