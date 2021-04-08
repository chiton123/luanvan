package com.example.luanvan.ui.recruiter.PostNews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.luanvan.ui.Model.JobList;
import com.example.luanvan.ui.recruiter.CVManagement.CVManageActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CandidateDocumentFragment;
import com.example.luanvan.ui.recruiter.RecruiterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateJobActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editPosition, editStart, editEnd, editAddress, editNumber, editSalaryMin, editSalaryMax, editDescription, editRequirement, editBenefit;
    Spinner spinnerKhuvuc, spinnerNganhnghe, spinnerKinhnghiem, spinnerLoaiHinh;
    Button btnPost;
    ArrayList<GeneralObject> dataArea,dataProfession, dataSalary, dataExperience, dataKindJob;
    public static SpinnerNewAdapter khuVucAdapter, nganhNgheAdapter, kinhNghiemAdapter, loaiHinhAdapter;
    JobList job = null;
    int position_job = 0;
    int check_start = 0;
    String date_post_start = "", date_post_end = "";
    Date date_start = null, date_end = null;
    String position = "", address = "", number = "", description = "", requirement = "", benefit = "";
    int salary_min = 0, salary_max = 0;
    int x = 0; // check end date có trước start date hay k, nếu có thì 1
    GeneralObject area, profession, experience, kindJob;
    int idArea = 0, idProfession = 0, idExperience = 0, idKindJob = 0, job_id = 0;
    Handler handler;
    String type_notification = "", content = "";
    // id job vừa đăng
    int idjobJust = 0;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job);
        anhxa();
        actionBar();
        eventSpinner();
        eventButton();
        eventDate();



    }
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    public void showDate(final int kind) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = dateFormat.format(Calendar.getInstance().getTime());
        final Date today1 = dateFormat.parse(today);
        final Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                if(calendar.getTime().before(today1) && kind == 1){
                    Toast.makeText(getApplicationContext(), "Phải lớn hơn ngày hiện tại", Toast.LENGTH_SHORT).show();
                }else {
                    if(kind == 1){
                        editStart.setText(dateFormat.format(calendar.getTime()));
                        date_start = calendar.getTime();
                    }else {
                        date_end = calendar.getTime();
                        if(date_end.before(date_start)){
                            Toast.makeText(getApplicationContext(), "Ngày kết thúc phải sau ngày bắt đầu", Toast.LENGTH_SHORT).show();
                            x = 1;
                        }else {
                            editEnd.setText(dateFormat.format(calendar.getTime()));
                            x = 0;
                        }

                    }

                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = fmt.parse(fmt.format(calendar.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(kind == 1){
                        date_post_start = fmt.format(date);
                        check_start = 1;
                    }else {
                        date_post_end = fmt.format(date);
                    }

                }

            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }
    private void eventDate() {
        editStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showDate(1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        editEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(check_start == 0 || editStart.getText().equals("")){
                        Toast.makeText(getApplicationContext(), "Bạn chọn ngày bắt đầu trước", Toast.LENGTH_SHORT).show();
                    }else {
                        showDate(2);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void postNotificationForAdmin(final int type_user) {
        type_notification = "Duyệt tin tuyển dụng";
        content = "Công ty " + MainActivity.company_name + " đăng một tin tuyển dụng mới";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlPostNotificationForAdmin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(getApplicationContext(), "Thông báo thành công", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "Thông báo thất bại", Toast.LENGTH_SHORT).show();
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
                map.put("idjob", String.valueOf(idjobJust));
                map.put("type_user", String.valueOf(type_user));
                map.put("type_notification",  type_notification);
                map.put("iduser", String.valueOf(1)); // 1: admin
                map.put("content", content);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void eventButton() {
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editEnd.getText().equals("") || editStart.getText().equals("") || editAddress.getText().equals("") || editBenefit.getText().equals("")
                        || editDescription.getText().equals("") || editRequirement.getText().equals("") || editNumber.getText().equals("") || editSalaryMin.getText().equals("")
                        || editPosition.getText().equals("") || editSalaryMax.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
                }else if(idExperience == 0){
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn kinh nghiệm", Toast.LENGTH_SHORT).show();
                }else if(idProfession == 0){
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn ngành nghề", Toast.LENGTH_SHORT).show();
                }else if(idArea == 0){
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn khu vực", Toast.LENGTH_SHORT).show();
                }else if(idKindJob == 0){
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn loại hình công việc", Toast.LENGTH_SHORT).show();
                }else if(Integer.parseInt(editSalaryMin.getText().toString()) >= Integer.parseInt(editSalaryMax.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Mức lương không hợp lệ", Toast.LENGTH_SHORT).show();
                }else if(date_start.after(date_end)){
                    Toast.makeText(getApplicationContext(), "Ngày bắt đầu phải trước ngày kết thúc", Toast.LENGTH_SHORT).show();
                }
                else {
                    loading();
                    position = editPosition.getText().toString();
                    address = editAddress.getText().toString();
                    benefit = editBenefit.getText().toString();
                    description = editDescription.getText().toString();
                    requirement = editRequirement.getText().toString();
                    number = editNumber.getText().toString();
                    salary_min = Integer.parseInt(editSalaryMin.getText().toString());
                    salary_max = Integer.parseInt(editSalaryMax.getText().toString());
                    //status: đang hiển thị -> 0, chờ xác thực : 1, 2: từ chối
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlCreateJob,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(!response.equals("fail")){
                                        Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                        int k = response.lastIndexOf("s");

                                        idjobJust = Integer.parseInt(response.substring(k+1, response.length()));
                                     //   Toast.makeText(getApplicationContext(), idjobJust + " id", Toast.LENGTH_SHORT).show();
                                        postNotificationForAdmin(0); // 0: cho admin
                                        getJobRecentCreate();

                                        handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                Intent intent = new Intent();
                                                setResult(123);
                                                finish();
                                            }
                                        },3000);


                                    }else {
                                        Toast.makeText(getApplicationContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
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
                            Map<String, String> map = new HashMap<>();
                            map.put("position", position);
                            map.put("address", address);
                            map.put("benefit", benefit);
                            map.put("description", description);
                            map.put("requirement", requirement);
                            map.put("number", number);
                            map.put("salary_min", String.valueOf(salary_min));
                            map.put("salary_max", String.valueOf(salary_max));
                            map.put("start", date_post_start);
                            map.put("end", date_post_end);
                            map.put("idarea", String.valueOf(idArea));
                            map.put("idprofession", String.valueOf(idProfession));
                            map.put("idexperience", String.valueOf(idExperience));
                            map.put("idKindJob", String.valueOf(idKindJob));
                            map.put("idcompany", String.valueOf(MainActivity.idcompany));
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);


                }
            }
        });
    }

    private void getJobRecentCreate() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlGetJobRecentCreate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                RecruiterActivity.arrayListAuthenticationJobs.add(new JobList(
                                        object.getInt("id"),
                                        object.getString("name"),
                                        object.getInt("idcompany"),
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
                                        object.getString("type_job"),
                                        object.getString("note_reject"),
                                        object.getInt("document"),
                                        object.getInt("new_document"),
                                        object.getInt("interview"),
                                        object.getInt("work"),
                                        object.getInt("skip")
                                ));
                                AuthenticationFragment.adapter.notifyDataSetChanged();
                            }

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
                map.put("idjob", String.valueOf(idjobJust));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void eventSpinner() {
        khuVucAdapter = new SpinnerNewAdapter(this,R.layout.dong_spinner, dataArea);

        nganhNgheAdapter = new SpinnerNewAdapter(this,R.layout.dong_spinner, dataProfession);

        kinhNghiemAdapter = new SpinnerNewAdapter(this,R.layout.dong_spinner, dataExperience);

        loaiHinhAdapter = new SpinnerNewAdapter(this,R.layout.dong_spinner, dataKindJob);

        spinnerKhuvuc.setAdapter(khuVucAdapter);
        spinnerNganhnghe.setAdapter(nganhNgheAdapter);
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
        editPosition = (EditText) findViewById(R.id.editposition);
        editStart = (EditText) findViewById(R.id.editstart);
        editEnd = (EditText) findViewById(R.id.editend);
        editAddress = (EditText) findViewById(R.id.editaddress);
        editNumber = (EditText) findViewById(R.id.editnumber);
        editSalaryMin = (EditText) findViewById(R.id.editsalarymin);
        editSalaryMax = (EditText) findViewById(R.id.editsalarymax);
        editDescription = (EditText) findViewById(R.id.editdescription);
        editRequirement = (EditText) findViewById(R.id.editrequirement);
        editBenefit = (EditText) findViewById(R.id.editbenefit);
        spinnerKhuvuc = (Spinner) findViewById(R.id.spinnerkhuvuc);
        spinnerKinhnghiem = (Spinner) findViewById(R.id.spinnerkinhnghiem);
        spinnerLoaiHinh = (Spinner) findViewById(R.id.spinnerloaihinh);
        spinnerNganhnghe = (Spinner) findViewById(R.id.spinnernganhnghe);
        btnPost = (Button) findViewById(R.id.buttondangtin);

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
        dataKindJob.add(new GeneralObject(0,"Vui lòng chọn"));
        dataKindJob.add(new GeneralObject(1, "Toàn thời gian"));
        dataKindJob.add(new GeneralObject(2, "Bán thời gian"));
        dataKindJob.add(new GeneralObject(3, "Thực tập"));
        dataKindJob.add(new GeneralObject(4, "Remote - Làm việc từ xa"));
    }

    private void getDataExperience() {
        dataExperience.add(new GeneralObject(0,"Vui lòng chọn"));
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
        dataSalary.add(new GeneralObject(0,"Vui lòng chọn"));
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
        dataProfession.add(new GeneralObject(0,"Vui lòng chọn"));
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
