package com.example.luanvan.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.cv.CVCreateActivity;
import com.example.luanvan.ui.login.LoginActivity;

public class DashboardFragment extends Fragment {
    LinearLayout linearLayoutCV, linearLayoutEmpty;
    Button btnThem;
    int REQUEST_CODE = 123;
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        linearLayoutCV = (LinearLayout) view.findViewById(R.id.a);
        linearLayoutEmpty = (LinearLayout) view.findViewById(R.id.c);
        btnThem = (Button) view.findViewById(R.id.buttontaomoi);
        if(MainActivity.arrayListCV.size() > 0){
            linearLayoutCV.setVisibility(View.VISIBLE);
            linearLayoutEmpty.setVisibility(View.GONE);
        }else {
            linearLayoutCV.setVisibility(View.GONE);
            linearLayoutEmpty.setVisibility(View.VISIBLE);
        }
        eventCreateCV();

        return view;

    }
    private void eventCreateCV() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.login == 0){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), CVCreateActivity.class);
                    getActivity().startActivityForResult(intent, REQUEST_CODE);
                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 123 && resultCode == 123){
            Toast.makeText(getActivity(), "hehe", Toast.LENGTH_SHORT).show();

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
