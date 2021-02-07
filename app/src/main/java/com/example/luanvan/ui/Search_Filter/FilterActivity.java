package com.example.luanvan.ui.Search_Filter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.example.luanvan.ui.Model.Job;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FilterActivity extends AppCompatActivity {
    Spinner spinnerKhuvuc, spinnerNganhnghe, spinnerLuong, spinnerKinhnghiem, spinnerLoaiHinh;
    GeneralObject area, profession, salary, experience, kindJob;
    int idArea = 0, idProfession = 0, idSalary = 0, idExperience = 0, idKindJob = 0;
    ArrayList<GeneralObject> dataArea,dataProfession, dataSalary, dataExperience, dataKindJob;
    SpinnerNewAdapter khuVucAdapter, nganhNgheAdapter, luongAdapter, kinhNghiemAdapter, loaiHinhAdapter;
    Button btnHuy, btnTimKiem;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        anhxa();
        spinerEvent();
        eventButton();

    }

    private void eventButton() {
        btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "idarea:  "+ idArea + " pro " + idProfession , Toast.LENGTH_SHORT).show();
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlFilter,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                SearchActivity.arrayList.clear();

                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for(int i=0 ; i < jsonArray.length(); i++){
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        SearchActivity.arrayList.add(new Job(
                                                object.getInt("id"),
                                                object.getString("name"),
                                                object.getInt("idcompany"),
                                                object.getString("img"),
                                                object.getString("area"),
                                                object.getInt("idtype"),
                                                object.getInt("idprofession"),
                                                object.getString("date"),
                                                object.getInt("salary"),
                                                object.getInt("idarea"),
                                                object.getInt("gender"),
                                                object.getString("experience"),
                                                object.getInt("number"),
                                                object.getString("position"),
                                                object.getString("description"),
                                                object.getString("requirement"),
                                                object.getString("benefit"),
                                                object.getInt("status"),
                                                object.getString("company_name"),
                                                object.getString("type_job")
                                        ));

                                    }
                                    handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent  = new Intent();
                                            setResult(123);
                                            finish();
                                        }
                                    },3000);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<>();
                        map.put("idarea", String.valueOf(idArea));
                        map.put("idprofession", String.valueOf(idProfession));
                        map.put("idkindjob", String.valueOf(idKindJob));
                        map.put("idsalary", String.valueOf(idSalary));
                        map.put("idexperience", String.valueOf(idExperience));
                        return map;
                    }
                };
                requestQueue.add(stringRequest);

            }
        });

    }

    private void spinerEvent() {

        khuVucAdapter = new SpinnerNewAdapter(this,R.layout.dong_spinner, dataArea);

        nganhNgheAdapter = new SpinnerNewAdapter(this,R.layout.dong_spinner, dataProfession);

        luongAdapter = new SpinnerNewAdapter(this,R.layout.dong_spinner, dataSalary);

        kinhNghiemAdapter = new SpinnerNewAdapter(this,R.layout.dong_spinner, dataExperience);

        loaiHinhAdapter = new SpinnerNewAdapter(this,R.layout.dong_spinner, dataKindJob);

        spinnerKhuvuc.setAdapter(khuVucAdapter);
        spinnerNganhnghe.setAdapter(nganhNgheAdapter);
        spinnerLuong.setAdapter(luongAdapter);
        spinnerKinhnghiem.setAdapter(kinhNghiemAdapter);
        spinnerLoaiHinh.setAdapter(loaiHinhAdapter);
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
        spinnerNganhnghe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profession = nganhNgheAdapter.getObject(position);
                idProfession = profession.getId();
             //   Toast.makeText(getApplicationContext(), " p " + idProfession, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerLuong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                salary = luongAdapter.getObject(position);
                idSalary = salary.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerKinhnghiem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                experience = kinhNghiemAdapter.getObject(position);
                idExperience = experience.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerLoaiHinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kindJob = loaiHinhAdapter.getObject(position);
                idKindJob = kindJob.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void anhxa() {
        spinnerKhuvuc = (Spinner) findViewById(R.id.spinnerkhuvuc);
        spinnerNganhnghe = (Spinner) findViewById(R.id.spinnernganhnghe);
        spinnerLuong = (Spinner) findViewById(R.id.spinnerluong);
        spinnerKinhnghiem = (Spinner) findViewById(R.id.spinnerkinhnghiem);
        spinnerLoaiHinh = (Spinner) findViewById(R.id.spinnerloaihinh);
        btnHuy = (Button) findViewById(R.id.buttonhuy);
        btnTimKiem = (Button) findViewById(R.id.buttontimkiem);
        dataArea = new ArrayList();
        dataProfession = new ArrayList<>();
        dataSalary = new ArrayList<>();
        dataExperience = new ArrayList<>();
        dataKindJob = new ArrayList<>();
        getDataArea();
        getDataProfession();
        getDataSalary();
        getDataExperience();
        getDataKindJob();

    }

    private void getDataKindJob() {
        dataKindJob.add(new GeneralObject(0, "Tất cả"));
        dataKindJob.add(new GeneralObject(1, "Toàn thời gian"));
        dataKindJob.add(new GeneralObject(2, "Bán thời gian"));
        dataKindJob.add(new GeneralObject(3, "Thực tập"));
        dataKindJob.add(new GeneralObject(4, "Remote - Làm việc từ xa"));
    }

    private void getDataExperience() {
        dataExperience.add(new GeneralObject(0, "Tất cả"));
        dataExperience.add(new GeneralObject(1, "Chưa có kinh nghiệm"));
        dataExperience.add(new GeneralObject(2, "Dưới 1 năm"));
        dataExperience.add(new GeneralObject(3, "1 năm"));
        dataExperience.add(new GeneralObject(4, "2 năm"));
        dataExperience.add(new GeneralObject(5, "3 năm"));
        dataExperience.add(new GeneralObject(6, "4 năm"));
        dataExperience.add(new GeneralObject(7, "5 năm"));
        dataExperience.add(new GeneralObject(8, "Trên 5 năm"));

    }

    private void getDataSalary() {
        dataSalary.add(new GeneralObject(0, "Tất cả"));
        dataSalary.add(new GeneralObject(1, "Dưới 3 triệu"));
        dataSalary.add(new GeneralObject(2, "3 - 5 triệu"));
        dataSalary.add(new GeneralObject(3, "5 - 7 triệu"));
        dataSalary.add(new GeneralObject(4, "7 - 10 triệu"));
        dataSalary.add(new GeneralObject(5, "10 - 12 triệu"));
        dataSalary.add(new GeneralObject(6, "12 - 15 triệu"));
        dataSalary.add(new GeneralObject(7, "15 - 20 triệu"));
        dataSalary.add(new GeneralObject(8, "20 - 25 triệu"));
        dataSalary.add(new GeneralObject(9, "25 - 30 triệu"));
        dataSalary.add(new GeneralObject(10, "Trên 30 triệu"));
        dataSalary.add(new GeneralObject(11, "Thỏa thuận"));


    }

    private void getDataProfession() {
        dataProfession.add(new GeneralObject(0, "Tất cả"));
        dataProfession.add(new GeneralObject(1,"Kinh doanh, bán hàng"));
        dataProfession.add(new GeneralObject(2, "Biên phiên dịch"));
        dataProfession.add(new GeneralObject(3, "Báo chí, truyền hình"));
        dataProfession.add(new GeneralObject(4, "Bất động sản"));
        dataProfession.add(new GeneralObject(5, "Du lịch"));
        dataProfession.add(new GeneralObject(6, "Luật, pháp lý"));
        dataProfession.add(new GeneralObject(7, "Điện tử viễn thông"));
        dataProfession.add(new GeneralObject(8, "Giáo dục / Đào tạo"));
        dataProfession.add(new GeneralObject(9, "IT Phần cứng / Mạng"));
        dataProfession.add(new GeneralObject(10, "IT Phần mềm"));
        dataProfession.add(new GeneralObject(11, "Kế toán / Kiểm toán"));
        dataProfession.add(new GeneralObject(12, "Marketing / truyền thông / Quảng cáo"));
        dataProfession.add(new GeneralObject(13, "Thực phẩm / Đồ uống"));
        dataProfession.add(new GeneralObject(14, "Thiết kế đồ họa"));

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
        dataArea.add(new GeneralObject(59," Cần Thơ"));
        dataArea.add(new GeneralObject(59,"Đà Nẵng"));
        dataArea.add(new GeneralObject(59,"Hải Phòng"));
        dataArea.add(new GeneralObject(59,"Hà Nội"));
        dataArea.add(new GeneralObject(59,"TP HCM"));


    }
}