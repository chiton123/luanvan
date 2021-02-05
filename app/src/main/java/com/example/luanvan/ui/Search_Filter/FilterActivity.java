package com.example.luanvan.ui.Search_Filter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Area;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {
    Spinner spinnerKhuvuc;
    Area area ;
    ArrayList<Area> dataArea = new ArrayList();
    SpinnerAdapter khuVucAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        anhxa();
        spinerEvent();

    }

    private void spinerEvent() {

        khuVucAdapter = new com.example.luanvan.ui.Adapter.SpinnerAdapter(this, dataArea);
       // khuVucAdapter.setDropDownViewResource(R.layout.dropdown_text_color);


        spinnerKhuvuc.setAdapter(khuVucAdapter);
//        spinnerKhuvuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                area = parent.getItemAtPosition(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }

    private void anhxa() {
        spinnerKhuvuc = (Spinner) findViewById(R.id.spinnerkhuvuc);
        dataArea = new ArrayList();
        getDataArea();

    }

    public void getDataArea() {
        dataArea.add(new Area(0,"Tất cả"));
        dataArea.add(new Area(1,"An Giang"));
        dataArea.add(new Area(2,"Bà Rịa - Vũng Tàu"));
        dataArea.add(new Area(3,"Bắc Giang"));
        dataArea.add(new Area(4,"Bắc Kạn"));
        dataArea.add(new Area(5,"Bạc Liêu"));
        dataArea.add(new Area(6,"Bắc Ninh"));
        dataArea.add(new Area(7,"Bến Tre"));
        dataArea.add(new Area(8,"Bình Định"));
        dataArea.add(new Area(9,"Bình Dương"));
        dataArea.add(new Area(10,"Bình Phước"));
        dataArea.add(new Area(10,"Bình Thuận"));
        dataArea.add(new Area(10,"Cà Mau"));
        dataArea.add(new Area(10,"Cao Bằng"));
        dataArea.add(new Area(10,"Đắk Lắk"));
        dataArea.add(new Area(10,"Đắk Nông"));
        dataArea.add(new Area(10,"Điện Biên"));
        dataArea.add(new Area(10,"Đồng Nai"));
        dataArea.add(new Area(10,"Đồng Tháp"));
        dataArea.add(new Area(10,"Gia Lai"));
        dataArea.add(new Area(10,"Hà Giang"));
        dataArea.add(new Area(10,"Hà Nam"));
        dataArea.add(new Area(10,"Hà Tĩnh"));
        dataArea.add(new Area(10,"Hải Dương"));
        dataArea.add(new Area(10,"Hậu Giang"));
        dataArea.add(new Area(10,"Hòa Bình"));
        dataArea.add(new Area(10,"Hưng Yên"));
        dataArea.add(new Area(10,"Khánh Hòa"));
        dataArea.add(new Area(10,"Kiên Giang"));
        dataArea.add(new Area(10,"Kon Tum"));
        dataArea.add(new Area(10,"Lai Châu"));
        dataArea.add(new Area(10,"Lâm Đồng"));
        dataArea.add(new Area(10,"Lạng Sơn"));
        dataArea.add(new Area(10,"Lào Cai"));
        dataArea.add(new Area(10,"Long An"));
        dataArea.add(new Area(10,"Nam Định"));
        dataArea.add(new Area(10,"Nghệ An"));
        dataArea.add(new Area(10,"Ninh Bình"));
        dataArea.add(new Area(10,"Ninh Thuận"));
        dataArea.add(new Area(10,"Phú Thọ"));
        dataArea.add(new Area(10,"Quảng Bình"));
        dataArea.add(new Area(10,"Quảng Nam"));
        dataArea.add(new Area(10,"Quảng Ngãi"));
        dataArea.add(new Area(10,"Quảng Ninh"));
        dataArea.add(new Area(10,"Quảng Trị"));
        dataArea.add(new Area(10,"Sóc Trăng"));
        dataArea.add(new Area(10,"Sơn La"));
        dataArea.add(new Area(10,"Tây Ninh"));
        dataArea.add(new Area(10,"Thái Bình"));
        dataArea.add(new Area(10,"Thái Nguyên"));
        dataArea.add(new Area(10,"Thanh Hóa"));
        dataArea.add(new Area(10,"Thừa Thiên Huế"));
        dataArea.add(new Area(10,"Tiền Giang"));
        dataArea.add(new Area(10,"Trà Vinh"));
        dataArea.add(new Area(10,"Tuyên Quang"));
        dataArea.add(new Area(10,"Vĩnh Long"));
        dataArea.add(new Area(10,"Vĩnh Phúc"));
        dataArea.add(new Area(10,"Yên Bái"));
        dataArea.add(new Area(10,"Phú Yên"));


    }
}