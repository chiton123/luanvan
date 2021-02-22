package com.example.luanvan.ui.fragment.job_f;

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
    TextView txtsalary, txtarea, txthinhthuc, txtnumber, txtexperience, txtdescription, txtbenefit, txtrequirement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        txtarea = (TextView) view.findViewById(R.id.area);
        txtsalary = (TextView) view.findViewById(R.id.salary);
        txthinhthuc = (TextView) view.findViewById(R.id.hinhthuc);
        txtnumber = (TextView) view.findViewById(R.id.number);
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
        txtexperience.setText(job.getExperience());

        String mota = xuongdong(job.getDescription());
        String yeucau = xuongdong(job.getRequirement());
        String quyenloi = xuongdong(job.getBenefit());
        txtdescription.setText(mota);
        txtrequirement.setText(yeucau);
        txtbenefit.setText(quyenloi);



    }
    public String xuongdong(String text){
        String ketqua = "";
        if(text.contains("-")){
            String[] split = text.split("-");
            for(String item : split){
                ketqua += "-" + item + "\n";
            }
            return ketqua;
        }else {
            return text;
        }
    }

}
