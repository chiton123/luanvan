package com.example.luanvan.ui.recruiter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.luanvan.R;

public class AdjustJobActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editPosition, editStart, editEnd, editAddress, editNumber, editSalary, editDescription, editRequirement, editBenefit;
    Spinner spinnerKhuvuc, spinnerNganhnghe, spinnerKinhnghiem, spinnerLoaiHinh;
    Button btnUpdate, btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_job);
        anhxa();


    }

    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editPosition = (EditText) findViewById(R.id.editposition);
        editStart = (EditText) findViewById(R.id.editstart);
        editEnd = (EditText) findViewById(R.id.editend);
        editAddress = (EditText) findViewById(R.id.editaddress);
        editNumber = (EditText) findViewById(R.id.editnumber);
        editSalary = (EditText) findViewById(R.id.editsalary);
        editDescription = (EditText) findViewById(R.id.editdescription);
        editRequirement = (EditText) findViewById(R.id.editrequirement);
        editBenefit = (EditText) findViewById(R.id.editbenefit);
        spinnerKhuvuc = (Spinner) findViewById(R.id.spinnerkhuvuc);
        spinnerKinhnghiem = (Spinner) findViewById(R.id.spinnerkinhnghiem);
        spinnerLoaiHinh = (Spinner) findViewById(R.id.spinnerloaihinh);
        spinnerNganhnghe = (Spinner) findViewById(R.id.spinnernganhnghe);
        btnUpdate = (Button) findViewById(R.id.buttoncapnhat);
        btnCancel = (Button) findViewById(R.id.buttonhuy);




    }
}
