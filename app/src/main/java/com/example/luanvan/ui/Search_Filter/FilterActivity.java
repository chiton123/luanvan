package com.example.luanvan.ui.Search_Filter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FilterActivity extends AppCompatActivity {
    Toolbar toolbar;
    Spinner spinnerKhuvuc, spinnerNganhnghe, spinnerLuong, spinnerKinhnghiem, spinnerLoaiHinh;
    GeneralObject area, profession, salary, experience, kindJob;
    int idArea = 0, idProfession = 0, idSalary = 0, idExperience = 0, idKindJob = 0;
    int min_salary = 0, max_salary = 0;
    ArrayList<GeneralObject> dataArea = new ArrayList<>(),dataProfession = new ArrayList<>(), dataSalary = new ArrayList<>(),
            dataExperience = new ArrayList<>(), dataKindJob = new ArrayList<>();
    public static SpinnerNewAdapter khuVucAdapter, nganhNgheAdapter, luongAdapter, kinhNghiemAdapter, loaiHinhAdapter;
    Button btnHuy, btnTimKiem;
    Handler handler;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        anhxa();
        actionBar();
        spinerEvent();
        eventButton();

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
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    private void eventButton() {
        btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading();
               // Toast.makeText(getApplicationContext(), "idarea:  "+ idArea + " pro " + idProfession , Toast.LENGTH_SHORT).show();
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlFilter,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                              //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                SearchActivity.arrayList.clear();
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for(int i=0 ; i < jsonArray.length(); i++){
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        SearchActivity.arrayList.add(new Job(
                                                object.getInt("id"),
                                                object.getString("name"),
                                                object.getInt("idcompany"),
                                                object.getInt("id_recruiter"),
                                                object.getString("img"),
                                                object.getString("address"),
                                                object.getInt("idtype"),
                                                object.getInt("idprofession"),
                                                object.getString("start_date"),
                                                object.getString("end_date"),
                                                object.getInt("salary_min"),
                                                object.getInt("salary_max"),
                                                object.getInt("idarea"),
                                                object.getString("area"),
                                                object.getString("experience"),
                                                object.getInt("number"),
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
                                            progressDialog.dismiss();
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
                                FancyToast.makeText(getApplicationContext(),error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                progressDialog.dismiss();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<>();
                        map.put("idarea", String.valueOf(idArea));
                        map.put("idprofession", String.valueOf(idProfession));
                        map.put("idkindjob", String.valueOf(idKindJob));
                        map.put("min_salary", String.valueOf(min_salary));
                        map.put("max_salary", String.valueOf(max_salary));
                        map.put("idsalary", String.valueOf(idSalary));
                        map.put("idexperience", String.valueOf(idExperience));
                        return map;
                    }
                };
                requestQueue.add(stringRequest);

            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("FileName", 0);
                sharedPreferences.edit().clear().commit();
                Intent intent = new Intent();
                setResult(234);
                finish();
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
        SharedPreferences sharedPref = getSharedPreferences("FileName",MODE_PRIVATE);
        int spinnerValueLoaiHinh = sharedPref.getInt("loaihinh",-1);
        if(spinnerValueLoaiHinh != -1) {
            // set the selected value of the spinner
            spinnerLoaiHinh.setSelection(spinnerValueLoaiHinh);
        }

        int spinnerValueKinhNghiem = sharedPref.getInt("kinhnghiem", -1);
        if(spinnerValueKinhNghiem != -1){
            spinnerKinhnghiem.setSelection(spinnerValueKinhNghiem);
        }

        int spinnerValueLuong = sharedPref.getInt("luong", -1);
        if(spinnerValueLuong != -1){
            spinnerLuong.setSelection(spinnerValueLuong);
        }

        int spinnerValueKhuVuc = sharedPref.getInt("khuvuc", -1);
        if(spinnerValueKhuVuc != -1){
            spinnerKhuvuc.setSelection(spinnerValueKhuVuc);
        }

        int spinnerValueNganhNghe = sharedPref.getInt("nganhnghe", -1);
        if(spinnerValueNganhNghe != -1){
            spinnerNganhnghe.setSelection(spinnerValueNganhNghe);
        }

        spinnerKhuvuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                area = khuVucAdapter.getObject(position);
                idArea = area.getId();
                int userChoice = spinnerKhuvuc.getSelectedItemPosition();
                SharedPreferences sharedPref = getSharedPreferences("FileName",0);
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putInt("khuvuc",userChoice);
                prefEditor.commit();
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
                int userChoice = spinnerNganhnghe.getSelectedItemPosition();
                SharedPreferences sharedPref = getSharedPreferences("FileName",0);
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putInt("nganhnghe",userChoice);
                prefEditor.commit();
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
                switch (idSalary){
                    case 0:
                        min_salary = 0;
                        max_salary = 1000000000;
                        break;
                    case 1:
                        min_salary = 0;
                        max_salary = 3000000;
                        break;
                    case 2:
                        min_salary = 3000000;
                        max_salary = 5000000;
                        break;
                    case 3:
                        min_salary = 5000000;
                        max_salary = 7000000;
                        break;
                    case 4:
                        min_salary = 7000000;
                        max_salary = 10000000;
                        break;
                    case 5:
                        min_salary = 10000000;
                        max_salary = 12000000;
                        break;
                    case 6:
                        min_salary = 12000000;
                        max_salary = 15000000;
                        break;
                    case 7:
                        min_salary = 15000000;
                        max_salary = 20000000;
                        break;
                    case 8:
                        min_salary = 20000000;
                        max_salary = 25000000;
                        break;
                    case 9:
                        min_salary = 25000000;
                        max_salary = 30000000;
                        break;
                    case 10:
                        min_salary = 30000000;
                        max_salary = 1000000000;
                        break;

                }

                int userChoice = spinnerLuong.getSelectedItemPosition();
                SharedPreferences sharedPref = getSharedPreferences("FileName",0);
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putInt("luong",userChoice);
                prefEditor.commit();
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
                int userChoice = spinnerKinhnghiem.getSelectedItemPosition();
                SharedPreferences sharedPref = getSharedPreferences("FileName",0);
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putInt("kinhnghiem",userChoice);
                prefEditor.commit();

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
                int userChoice = spinnerLoaiHinh.getSelectedItemPosition();
                SharedPreferences sharedPref = getSharedPreferences("FileName",0);
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putInt("loaihinh",userChoice);
                prefEditor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

    }

    private void getDataProfession() {
        dataProfession.add(new GeneralObject(0, "Tất cả"));
        dataProfession.add(new GeneralObject(1,"Kinh doanh / Bán hàng"));
        dataProfession.add(new GeneralObject(2,"Biên / Phiên dịch"));
        dataProfession.add(new GeneralObject(3,"Báo chí / Truyền hình"));
        dataProfession.add(new GeneralObject(4,"Bưu chính - Viễn thông"));
        dataProfession.add(new GeneralObject(5,"Bảo hiểm"));
        dataProfession.add(new GeneralObject(6,"Bất động sản"));
        dataProfession.add(new GeneralObject(7,"Chứng khoán / Vàng / Ngoại tệ"));
        dataProfession.add(new GeneralObject(8,"Công nghệ cao"));
        dataProfession.add(new GeneralObject(9,"Cơ khí - Chế tạo / Tự động hóa"));
        dataProfession.add(new GeneralObject(10,"Du lịch"));
        dataProfession.add(new GeneralObject(11,"Dầu khí/Hóa chất"));
        dataProfession.add(new GeneralObject(12,"Dệt may / Da giày"));
        dataProfession.add(new GeneralObject(13,"Dịch vụ khách hàng"));
        dataProfession.add(new GeneralObject(14,"Điện tử viễn thông"));
        dataProfession.add(new GeneralObject(15,"Điện / Điện tử / Điện lạnh"));
        dataProfession.add(new GeneralObject(16,"Giáo dục / Đào tạo"));
        dataProfession.add(new GeneralObject(17,"Hoá học 7 Sinh học"));
        dataProfession.add(new GeneralObject(18,"Hoạch định/Dự án"));
        dataProfession.add(new GeneralObject(19,"Hàng gia dụng"));
        dataProfession.add(new GeneralObject(20,"Hàng hải"));
        dataProfession.add(new GeneralObject(21,"Hàng không"));
        dataProfession.add(new GeneralObject(22,"Hành chính / Văn phòng"));
        dataProfession.add(new GeneralObject(23,"In ấn / Xuất bản"));
        dataProfession.add(new GeneralObject(24,"IT Phần cứng / Mạng"));
        dataProfession.add(new GeneralObject(25,"IT phần mềm"));
        dataProfession.add(new GeneralObject(26,"Khách sạn / Nhà hàng"));
        dataProfession.add(new GeneralObject(27,"Kế toán / Kiểm toán"));
        dataProfession.add(new GeneralObject(28,"Marketing / Truyền thông - Quảng cáo"));
        dataProfession.add(new GeneralObject(29,"Môi trường / Xử lý chất thải"));
        dataProfession.add(new GeneralObject(30,"Mỹ phẩm / Trang sức"));
        dataProfession.add(new GeneralObject(31,"Mỹ thuật / Nghệ thuật / Điện ảnh"));
        dataProfession.add(new GeneralObject(32,"Ngân hàng / Tài chính"));
        dataProfession.add(new GeneralObject(33,"Nhân sự"));
        dataProfession.add(new GeneralObject(34,"Nông - Lâm / Ngư nghiệp"));
        dataProfession.add(new GeneralObject(35,"Luật/Pháp lý"));
        dataProfession.add(new GeneralObject(36,"Quản lý chất lượng (QA/QC)"));
        dataProfession.add(new GeneralObject(37,"Quản lý điều hành"));
        dataProfession.add(new GeneralObject(38,"Thiết kế đồ họa"));
        dataProfession.add(new GeneralObject(39,"Thời trang"));
        dataProfession.add(new GeneralObject(40,"Thực phẩm / Đồ uống"));
        dataProfession.add(new GeneralObject(41,"Tư vấn"));
        dataProfession.add(new GeneralObject(42,"Tổ chức sự kiện / Quà tặng"));
        dataProfession.add(new GeneralObject(43,"Vận tải / Kho vận"));
        dataProfession.add(new GeneralObject(44,"Logistics"));
        dataProfession.add(new GeneralObject(45,"Xuất nhập khẩu"));
        dataProfession.add(new GeneralObject(46,"Xây dựng"));
        dataProfession.add(new GeneralObject(47,"Y tế / Dược"));
        dataProfession.add(new GeneralObject(48,"Công nghệ Ô tô"));
        dataProfession.add(new GeneralObject(49,"An toàn lao động"));
        dataProfession.add(new GeneralObject(50,"Bán hàng kỹ thuật"));
        dataProfession.add(new GeneralObject(51,"Bán lẻ / bán sỉ"));
        dataProfession.add(new GeneralObject(52,"Bảo trì / Sửa chữa"));
        dataProfession.add(new GeneralObject(53,"Dược phẩm | Công nghệ sinh học"));
        dataProfession.add(new GeneralObject(54,"Địa chất / Khoáng sản"));
        dataProfession.add(new GeneralObject(55,"Hàng cao cấp"));
        dataProfession.add(new GeneralObject(56,"Hàng tiêu dùng"));
        dataProfession.add(new GeneralObject(57,"Kiến trúc"));
        dataProfession.add(new GeneralObject(58,"Phi chính phủ / Phi lợi nhuận"));
        dataProfession.add(new GeneralObject(59,"Sản phẩm công nghiệp"));
        dataProfession.add(new GeneralObject(60,"Sản xuất"));
        dataProfession.add(new GeneralObject(61,"Tài chính / Đầu tư"));
        dataProfession.add(new GeneralObject(62,"Thiết kế nội thất"));
        dataProfession.add(new GeneralObject(63,"Thư ký / Trợ lý"));
        dataProfession.add(new GeneralObject(64,"Spa / Làm đẹp"));
        dataProfession.add(new GeneralObject(65,"Công nghệ thông tin"));
        dataProfession.add(new GeneralObject(66,"NG0 / Phi chính phủ / Phi lợi nhuận"));
        dataProfession.add(new GeneralObject(67,"Ngành nghề khác"));



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