package com.example.luanvan.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Job;

import java.text.DecimalFormat;


public class InfoFragment extends Fragment {
    TextView txtsalary, txtarea, txthinhthuc, txtnumber, txtgender, txtposition, txtexperience, txtdescription, txtbenefit, txtrequirement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        txtarea = (TextView) view.findViewById(R.id.area);
        txtsalary = (TextView) view.findViewById(R.id.salary);
        txthinhthuc = (TextView) view.findViewById(R.id.hinhthuc);
        txtnumber = (TextView) view.findViewById(R.id.number);
        txtposition = (TextView) view.findViewById(R.id.position);
        txtgender = (TextView) view.findViewById(R.id.gender);
        txtexperience = (TextView) view.findViewById(R.id.experience);
        txtdescription = (TextView) view.findViewById(R.id.motacongviec);
        txtbenefit = (TextView) view.findViewById(R.id.quyenloi);
        txtrequirement = (TextView) view.findViewById(R.id.yeucaucongviec);
        setContent();

        return view;
    }

    private void setContent() {
        Intent intent = (Intent) getActivity().getIntent();
        Job job = (Job) intent.getSerializableExtra("job");
        txtarea.setText(job.getArea());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtsalary.setText(decimalFormat.format(job.getSalary())+"đ");
        txthinhthuc.setText(job.getType_job());
        txtnumber.setText(job.getNumber()+"");
        txtposition.setText(job.getPosition());
        if(job.getGender() == 0){
            txtgender.setText("Không yêu cầu");
        }else if(job.getGender() == 1){
            txtgender.setText("Nam");
        }else {
            txtgender.setText("Nữ");
        }
        txtexperience.setText(job.getExperience());
        txtdescription.setText(job.getDescription());
        txtrequirement.setText(job.getRequirement());
        txtbenefit.setText(job.getBenefit());



    }
}
