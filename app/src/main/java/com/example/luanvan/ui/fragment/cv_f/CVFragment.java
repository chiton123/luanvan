package com.example.luanvan.ui.fragment.cv_f;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.cv.CVCreateActivity;
import com.example.luanvan.ui.login.LoginActivity;

public class CVFragment extends Fragment {
    LinearLayout linearLayoutCV, linearLayoutEmpty;
    Button btnThem;
    int REQUEST_CODE = 123;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c_v, container, false);
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
}
