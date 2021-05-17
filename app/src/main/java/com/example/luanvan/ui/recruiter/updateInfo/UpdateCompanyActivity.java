package com.example.luanvan.ui.recruiter.updateInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.update_personal_info.SpinnerNewAdapter;
import com.example.luanvan.ui.Model.GeneralObject;
import com.example.luanvan.ui.recruiter.LoginRecruiterActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateCompanyActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnUpdate, btnCancel;
    EditText editName, editSize, editWebsite, editAddress, editIntroduction;
    Spinner spinnerKhuvuc;
    ArrayList<GeneralObject> dataArea;
    public static SpinnerNewAdapter khuVucAdapter;
    GeneralObject area;
    int idArea = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_company);
        anhxa();
        actionBar();
        eventSpinner();
        getInfo();
        eventUpdate();


    }

    private void eventUpdate() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editName.getText().toString();
                final String size = editSize.getText().toString();
                final String website = editWebsite.getText().toString();
                final String address = editAddress.getText().toString();
                final String intro = editIntroduction.getText().toString();
                if(name.equals("")){
                    FancyToast.makeText(getApplicationContext(), "Chưa điền họ tên", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else if(size.equals("")){
                    FancyToast.makeText(getApplicationContext(), "Chưa điền quy mô công ty", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else if(website.equals("")){
                    FancyToast.makeText(getApplicationContext(), "Chưa điền website công ty", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else if(address.equals("")){
                    FancyToast.makeText(getApplicationContext(), "Chưa điền địa chỉ", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else if(intro.equals("")){
                    FancyToast.makeText(getApplicationContext(), "Chưa điền giới thiệu", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else if(idArea == 0){
                    FancyToast.makeText(getApplicationContext(), "Chưa chọn khu vực", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }
                else {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlUpdateCompany,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("success")){
                                        FancyToast.makeText(getApplicationContext(), "Cập nhật thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                        LoginRecruiterActivity.company.setAddress(address);
                                        LoginRecruiterActivity.company.setWebsite(website);
                                        LoginRecruiterActivity.company.setSize(size);
                                        LoginRecruiterActivity.company.setName(name);
                                        LoginRecruiterActivity.company.setIntroduction(intro);
                                        LoginRecruiterActivity.company.setIdarea(idArea);
                                        finish();

                                    }else{
                                        FancyToast.makeText(getApplicationContext(),"Cập nhật thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    FancyToast.makeText(getApplicationContext(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<>();
                            map.put("idcompany", String.valueOf(MainActivity.idcompany));
                            map.put("name", name);
                            map.put("address", address);
                            map.put("size", size);
                            map.put("website", website);
                            map.put("introduction", intro);
                            map.put("idarea", String.valueOf(idArea));
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);

                }

            }
        });
    }
    private void eventSpinner() {
        khuVucAdapter = new SpinnerNewAdapter(this,R.layout.dong_spinner, dataArea);
        spinnerKhuvuc.setAdapter(khuVucAdapter);
        spinnerKhuvuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                area = khuVucAdapter.getObject(position);
                idArea = area.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getInfo() {
        editName.setText(LoginRecruiterActivity.company.getName());
        editSize.setText(LoginRecruiterActivity.company.getSize());
        editWebsite.setText(LoginRecruiterActivity.company.getWebsite());
        editIntroduction.setText(LoginRecruiterActivity.company.getIntroduction());
        editAddress.setText(LoginRecruiterActivity.company.getAddress());
        idArea = LoginRecruiterActivity.company.getIdarea();
        spinnerKhuvuc.setSelection(idArea);
       // Toast.makeText(getApplicationContext(), idArea + "", Toast.LENGTH_SHORT).show();

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
        btnCancel = (Button) findViewById(R.id.buttonhuy);
        btnUpdate = (Button) findViewById(R.id.buttoncapnhat);
        editName = (EditText) findViewById(R.id.editname);
        editAddress = (EditText) findViewById(R.id.editaddress);
        editSize = (EditText) findViewById(R.id.editsize);
        editIntroduction = (EditText) findViewById(R.id.editintroduction);
        editWebsite = (EditText) findViewById(R.id.editwebsite);
        spinnerKhuvuc = (Spinner) findViewById(R.id.spinnerkhuvuc);
        dataArea = new ArrayList();
        getDataArea();

    }
    public void getDataArea() {
        dataArea.add(new GeneralObject(0,"Vui lòng chọn"));
        dataArea.add(new GeneralObject(1,"An Giang"));
        dataArea.add(new GeneralObject(2,"Bà Rịa - Vũng Tàu"));
        dataArea.add(new GeneralObject(3,"Bắc Giang"));
        dataArea.add(new GeneralObject(4,"Bắc Kạn"));
        dataArea.add(new GeneralObject(5,"Bạc Liêu"));
        dataArea.add(new GeneralObject(6,"Bắc Ninh"));
        dataArea.add(new GeneralObject(7,"Bến Tre"));
        dataArea.add(new GeneralObject(8,"Bình Định"));
        dataArea.add(new GeneralObject(9,"Bình Dương"));
        dataArea.add(new GeneralObject(10,"Bình Phước"));
        dataArea.add(new GeneralObject(11,"Bình Thuận"));
        dataArea.add(new GeneralObject(12,"Cà Mau"));
        dataArea.add(new GeneralObject(13,"Cao Bằng"));
        dataArea.add(new GeneralObject(14,"Đắk Lắk"));
        dataArea.add(new GeneralObject(15,"Đắk Nông"));
        dataArea.add(new GeneralObject(16,"Điện Biên"));
        dataArea.add(new GeneralObject(17,"Đồng Nai"));
        dataArea.add(new GeneralObject(18,"Đồng Tháp"));
        dataArea.add(new GeneralObject(19,"Gia Lai"));
        dataArea.add(new GeneralObject(20,"Hà Giang"));
        dataArea.add(new GeneralObject(21,"Hà Nam"));
        dataArea.add(new GeneralObject(22,"Hà Tĩnh"));
        dataArea.add(new GeneralObject(23,"Hải Dương"));
        dataArea.add(new GeneralObject(24,"Hậu Giang"));
        dataArea.add(new GeneralObject(25,"Hòa Bình"));
        dataArea.add(new GeneralObject(26,"Hưng Yên"));
        dataArea.add(new GeneralObject(27,"Khánh Hòa"));
        dataArea.add(new GeneralObject(28,"Kiên Giang"));
        dataArea.add(new GeneralObject(29,"Kon Tum"));
        dataArea.add(new GeneralObject(30,"Lai Châu"));
        dataArea.add(new GeneralObject(31,"Lâm Đồng"));
        dataArea.add(new GeneralObject(32,"Lạng Sơn"));
        dataArea.add(new GeneralObject(33,"Lào Cai"));
        dataArea.add(new GeneralObject(34,"Long An"));
        dataArea.add(new GeneralObject(35,"Nam Định"));
        dataArea.add(new GeneralObject(36,"Nghệ An"));
        dataArea.add(new GeneralObject(37,"Ninh Bình"));
        dataArea.add(new GeneralObject(38,"Ninh Thuận"));
        dataArea.add(new GeneralObject(39,"Phú Thọ"));
        dataArea.add(new GeneralObject(40,"Quảng Bình"));
        dataArea.add(new GeneralObject(41,"Quảng Nam"));
        dataArea.add(new GeneralObject(42,"Quảng Ngãi"));
        dataArea.add(new GeneralObject(43,"Quảng Ninh"));
        dataArea.add(new GeneralObject(44,"Quảng Trị"));
        dataArea.add(new GeneralObject(45,"Sóc Trăng"));
        dataArea.add(new GeneralObject(46,"Sơn La"));
        dataArea.add(new GeneralObject(47,"Tây Ninh"));
        dataArea.add(new GeneralObject(48,"Thái Bình"));
        dataArea.add(new GeneralObject(49,"Thái Nguyên"));
        dataArea.add(new GeneralObject(50,"Thanh Hóa"));
        dataArea.add(new GeneralObject(51,"Thừa Thiên Huế"));
        dataArea.add(new GeneralObject(52,"Tiền Giang"));
        dataArea.add(new GeneralObject(53,"Trà Vinh"));
        dataArea.add(new GeneralObject(54,"Tuyên Quang"));
        dataArea.add(new GeneralObject(55,"Vĩnh Long"));
        dataArea.add(new GeneralObject(56,"Vĩnh Phúc"));
        dataArea.add(new GeneralObject(57,"Yên Bái"));
        dataArea.add(new GeneralObject(58,"Phú Yên"));
        dataArea.add(new GeneralObject(59,"Cần Thơ"));
        dataArea.add(new GeneralObject(60,"Đà Nẵng"));
        dataArea.add(new GeneralObject(61,"Hải Phòng"));
        dataArea.add(new GeneralObject(62,"Hà Nội"));
        dataArea.add(new GeneralObject(63,"TP HCM"));


    }
}
