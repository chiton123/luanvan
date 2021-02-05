package com.example.luanvan.ui.Search_Filter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.update_personal_info.SpinnerNewAdapter;
import com.example.luanvan.ui.Model.GeneralObject;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {
    Spinner spinnerKhuvuc, spinnerNganhnghe;
    GeneralObject area, profession;
    int idArea, idProfession;
    ArrayList<GeneralObject> dataArea,dataProfession;
    SpinnerNewAdapter khuVucAdapter, nganhNgheAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        anhxa();
        spinerEvent();

    }

    private void spinerEvent() {

        khuVucAdapter = new SpinnerNewAdapter(this, dataArea);
       // khuVucAdapter.setDropDownViewResource(R.layout.dropdown_text_color);
        nganhNgheAdapter = new SpinnerNewAdapter(this, dataProfession);

        spinnerKhuvuc.setAdapter(khuVucAdapter);
        spinnerNganhnghe.setAdapter(nganhNgheAdapter);
        spinnerKhuvuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                area = (GeneralObject) spinnerKhuvuc.getSelectedItem();
                idArea = area.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerNganhnghe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profession = (GeneralObject) spinnerKhuvuc.getSelectedItem();
                idProfession = profession.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void anhxa() {
        spinnerKhuvuc = (Spinner) findViewById(R.id.spinnerkhuvuc);
        spinnerNganhnghe = (Spinner) findViewById(R.id.spinnernganhnghe);
        dataArea = new ArrayList();
        dataProfession = new ArrayList<>();
        getDataArea();
        getDataProfession();

    }

    private void getDataProfession() {
        dataProfession.add(new GeneralObject(0, "Tất cả"));
        dataProfession.add(new GeneralObject(1,"Kinh doanh, bán hàng"));
        dataProfession.add(new GeneralObject(2, "Biên phiên dịch"));
        dataProfession.add(new GeneralObject(3, "Báo chí, truyền hình"));
        dataProfession.add(new GeneralObject(4, "Bất động sản"));
        dataProfession.add(new GeneralObject(5, "Du lịch"));
        dataProfession.add(new GeneralObject(6, "IT phần mềm"));
        dataProfession.add(new GeneralObject(7, "Khách sạn, nhà hàng"));
        dataProfession.add(new GeneralObject(8, "Kế toán, kiểm toán"));
        dataProfession.add(new GeneralObject(9, "Marketing"));
        dataProfession.add(new GeneralObject(10, "Luật, pháp lý"));
    }

    public void getDataArea() {
        dataArea.add(new GeneralObject(0,"Tất cả"));
        dataArea.add(new GeneralObject(1,"An Giang"));
        dataArea.add(new GeneralObject(2,"Bà Rịa - Vũng Tàu"));
        dataArea.add(new GeneralObject(3,"Bắc Giang"));
        dataArea.add(new GeneralObject(4,"Bắc Kạn"));
        dataArea.add(new GeneralObject(5,"Bạc Liêu"));
        dataArea.add(new GeneralObject(6,"Bắc Ninh"));
        dataArea.add(new GeneralObject(7,"Bến Tre"));
        dataArea.add(new GeneralObject(8,"Bình Định"));
        dataArea.add(new GeneralObject(9,"Bình Dương"));
        dataArea.add(new GeneralObject(11,"Bình Phước"));
        dataArea.add(new GeneralObject(12,"Bình Thuận"));
        dataArea.add(new GeneralObject(13,"Cà Mau"));
        dataArea.add(new GeneralObject(14,"Cao Bằng"));
        dataArea.add(new GeneralObject(15,"Đắk Lắk"));
        dataArea.add(new GeneralObject(16,"Đắk Nông"));
        dataArea.add(new GeneralObject(17,"Điện Biên"));
        dataArea.add(new GeneralObject(18,"Đồng Nai"));
        dataArea.add(new GeneralObject(19,"Đồng Tháp"));
        dataArea.add(new GeneralObject(20,"Gia Lai"));
        dataArea.add(new GeneralObject(21,"Hà Giang"));
        dataArea.add(new GeneralObject(22,"Hà Nam"));
        dataArea.add(new GeneralObject(23,"Hà Tĩnh"));
        dataArea.add(new GeneralObject(24,"Hải Dương"));
        dataArea.add(new GeneralObject(25,"Hậu Giang"));
        dataArea.add(new GeneralObject(26,"Hòa Bình"));
        dataArea.add(new GeneralObject(27,"Hưng Yên"));
        dataArea.add(new GeneralObject(28,"Khánh Hòa"));
        dataArea.add(new GeneralObject(29,"Kiên Giang"));
        dataArea.add(new GeneralObject(30,"Kon Tum"));
        dataArea.add(new GeneralObject(31,"Lai Châu"));
        dataArea.add(new GeneralObject(32,"Lâm Đồng"));
        dataArea.add(new GeneralObject(33,"Lạng Sơn"));
        dataArea.add(new GeneralObject(34,"Lào Cai"));
        dataArea.add(new GeneralObject(35,"Long An"));
        dataArea.add(new GeneralObject(36,"Nam Định"));
        dataArea.add(new GeneralObject(37,"Nghệ An"));
        dataArea.add(new GeneralObject(38,"Ninh Bình"));
        dataArea.add(new GeneralObject(39,"Ninh Thuận"));
        dataArea.add(new GeneralObject(40,"Phú Thọ"));
        dataArea.add(new GeneralObject(41,"Quảng Bình"));
        dataArea.add(new GeneralObject(42,"Quảng Nam"));
        dataArea.add(new GeneralObject(43,"Quảng Ngãi"));
        dataArea.add(new GeneralObject(44,"Quảng Ninh"));
        dataArea.add(new GeneralObject(45,"Quảng Trị"));
        dataArea.add(new GeneralObject(46,"Sóc Trăng"));
        dataArea.add(new GeneralObject(47,"Sơn La"));
        dataArea.add(new GeneralObject(48,"Tây Ninh"));
        dataArea.add(new GeneralObject(49,"Thái Bình"));
        dataArea.add(new GeneralObject(50,"Thái Nguyên"));
        dataArea.add(new GeneralObject(51,"Thanh Hóa"));
        dataArea.add(new GeneralObject(52,"Thừa Thiên Huế"));
        dataArea.add(new GeneralObject(53,"Tiền Giang"));
        dataArea.add(new GeneralObject(54,"Trà Vinh"));
        dataArea.add(new GeneralObject(55,"Tuyên Quang"));
        dataArea.add(new GeneralObject(56,"Vĩnh Long"));
        dataArea.add(new GeneralObject(57,"Vĩnh Phúc"));
        dataArea.add(new GeneralObject(58,"Yên Bái"));
        dataArea.add(new GeneralObject(59,"Phú Yên"));


    }
}