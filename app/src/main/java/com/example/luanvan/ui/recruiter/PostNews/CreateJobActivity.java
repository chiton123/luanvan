package com.example.luanvan.ui.recruiter.PostNews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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
import com.example.luanvan.ui.Adapter.skill.SkillPickAdapter;
import com.example.luanvan.ui.Adapter.skill.SkillTagAdapter;
import com.example.luanvan.ui.Adapter.skill.TagAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.SpinnerNewAdapter;
import com.example.luanvan.ui.Model.GeneralObject;
import com.example.luanvan.ui.Model.JobList;
import com.example.luanvan.ui.Model.SkillCandidate;
import com.example.luanvan.ui.Model.SkillKey;

import com.example.luanvan.ui.recruiter.RecruiterActivity;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
    RecyclerView recyclerView, recyclerViewTag;
    TagAdapter tagAdapter;
    public static ArrayList<SkillKey>  arraylistChosenSkill;
    ArrayList<SkillCandidate> arraylistSkill;
    BottomSheetDialog bottomSheetSKill;
    SearchView searchView;
    SkillTagAdapter skillAdapter;
    ImageView imgAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job);
        anhxa();
        actionBar();
        eventSpinner();
        eventButton();
        eventDate();
        eventSkill();



    }

    private void eventSkill() {
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(getApplicationContext(), "Â", Toast.LENGTH_SHORT).show();
                bottomSheetSKill = new BottomSheetDialog(CreateJobActivity.this, R.style.BottomSheetTheme);
                View view = LayoutInflater.from(CreateJobActivity.this).inflate(R.layout.bottom_sheet_skill, (ViewGroup) findViewById(R.id.bottom_sheet));
                Button btnChoose = (Button) view.findViewById(R.id.buttonchon);
                Button btnCancel = (Button) view.findViewById(R.id.buttonhuy);
                bottomSheetSKill.setCancelable(false);
                searchView = (SearchView) view.findViewById(R.id.searchView);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        skillAdapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        skillAdapter.getFilter().filter(newText);
                        return false;
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetSKill.dismiss();
                        tagAdapter.notifyDataSetChanged();
                    }
                });
                btnChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tagAdapter.notifyDataSetChanged();
                        bottomSheetSKill.dismiss();
                    }
                });
                recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(CreateJobActivity.this, LinearLayoutManager.VERTICAL, false));
                skillAdapter = new SkillTagAdapter(CreateJobActivity.this, arraylistSkill, CreateJobActivity.this, arraylistChosenSkill);
                recyclerView.setAdapter(skillAdapter);
                bottomSheetSKill.setContentView(view);
                bottomSheetSKill.show();
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
                                        getJobRecentCreate();
                                        postNotificationForAdmin(0); // 0: cho admin

                                        postJobSkill();
                                        handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                Intent intent = new Intent();
                                                setResult(123);
                                                finish();
                                            }
                                        },6000);


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

    private void postJobSkill() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlAddJobSkill,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        if(response.equals("success")){
//                            Toast.makeText(getApplicationContext(), "Cập nhật kỹ năng cho tin tuyển dụng thành công", Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(getApplicationContext(), "Cập nhật kỹ năng cho tin tuyển dụng thất bại", Toast.LENGTH_SHORT).show();
//                        }
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
                JSONArray jsonArray = new JSONArray();
                for(int i=0; i < arraylistChosenSkill.size(); i++){
                    JSONObject object = new JSONObject();
                    try {
                        object.put("idjob", String.valueOf(idjobJust));
                        object.put("idskill", String.valueOf(arraylistChosenSkill.get(i).getId()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(object);
                }
                map.put("jsonarray", jsonArray.toString());


                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void getJobRecentCreate() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlGetJobRecentCreate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                         //   Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject object = jsonArray.getJSONObject(0);

                            RecruiterActivity.arrayListAuthenticationJobs.add(new JobList(
                                    idjobJust,
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
//                            Toast.makeText(getApplicationContext(), RecruiterActivity.arrayListAuthenticationJobs.size() + ""
//                                  , Toast.LENGTH_SHORT).show();

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
        imgAdd = (ImageView) findViewById(R.id.imgadd);
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
        recyclerViewTag = (RecyclerView) findViewById(R.id.recycleview);
        recyclerViewTag.setFocusable(false);
        recyclerViewTag.setClickable(false);
        recyclerViewTag.setHasFixedSize(true);
        arraylistSkill = new ArrayList<>();
        arraylistChosenSkill = new ArrayList<>();
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager();
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        recyclerViewTag.setLayoutManager(layoutManager);

        tagAdapter = new TagAdapter(CreateJobActivity.this, arraylistChosenSkill, CreateJobActivity.this, arraylistSkill );
        recyclerViewTag.setAdapter(tagAdapter);

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
        getDataSkill();

    }

    private void getDataSkill() {
        arraylistSkill.add(new SkillCandidate(1,"Kỹ năng tuyển dụng",0));;
        arraylistSkill.add(new SkillCandidate(2,"kỹ năng đào tạo",0));;
        arraylistSkill.add(new SkillCandidate(3,"Kỹ năng bán hàng",0));;
        arraylistSkill.add(new SkillCandidate(4,"Kỹ năng phân tích kinh doanh",0));;
        arraylistSkill.add(new SkillCandidate(5,"Kỹ năng quản lý công việc",0));;
        arraylistSkill.add(new SkillCandidate(6,"Kỹ năng thuyết trình",0));;
        arraylistSkill.add(new SkillCandidate(7,"Kỹ năng thuyết phục",0));;
        arraylistSkill.add(new SkillCandidate(8,"Kỹ năng đàm phán",0));;
        arraylistSkill.add(new SkillCandidate(9,"Kỹ năng giao tiếp",0));;
        arraylistSkill.add(new SkillCandidate(10,"Kỹ năng lãnh đạo",0));;
        arraylistSkill.add(new SkillCandidate(11,"Kỹ năng sư phạm",0));;
        arraylistSkill.add(new SkillCandidate(12,"Tin học văn phòng",0));;
        arraylistSkill.add(new SkillCandidate(13,"Tiếng anh giao tiếp",0));;
        arraylistSkill.add(new SkillCandidate(14,"Tiếng Hàn",0));;
        arraylistSkill.add(new SkillCandidate(15,"Tiếng Trung",0));;
        arraylistSkill.add(new SkillCandidate(16,"Tiếng Nhật",0));;
        arraylistSkill.add(new SkillCandidate(17,"SQL server",0));;
        arraylistSkill.add(new SkillCandidate(18,"Kotlin",0));;
        arraylistSkill.add(new SkillCandidate(19,"Objective C",0));;
        arraylistSkill.add(new SkillCandidate(20,"TCP/IP",0));;
        arraylistSkill.add(new SkillCandidate(21,"Full stack",0));;
        arraylistSkill.add(new SkillCandidate(22,"Unix",0));;
        arraylistSkill.add(new SkillCandidate(23,"UX/UI",0));;
        arraylistSkill.add(new SkillCandidate(24,"ReactJS",0));;
        arraylistSkill.add(new SkillCandidate(25,"DNS",0));;
        arraylistSkill.add(new SkillCandidate(26,"Photoshop",0));;
        arraylistSkill.add(new SkillCandidate(27,"Android",0));;
        arraylistSkill.add(new SkillCandidate(28,"Angular ",0));;
        arraylistSkill.add(new SkillCandidate(29,"Docker",0));;
        arraylistSkill.add(new SkillCandidate(30,"Redux",0));;
        arraylistSkill.add(new SkillCandidate(31,"TOEIC ",0));;
        arraylistSkill.add(new SkillCandidate(32,"Teamwork",0));;
        arraylistSkill.add(new SkillCandidate(33,"Flutter",0));;
        arraylistSkill.add(new SkillCandidate(34,"React native",0));;
        arraylistSkill.add(new SkillCandidate(35,"Excel, Word, PowerPoint",0));;
        arraylistSkill.add(new SkillCandidate(36,"Unity",0));;
        arraylistSkill.add(new SkillCandidate(37,"Tester",0));;
        arraylistSkill.add(new SkillCandidate(38,"PHP",0));;
        arraylistSkill.add(new SkillCandidate(39,"MySQL",0));;
        arraylistSkill.add(new SkillCandidate(40,"Database",0));;
        arraylistSkill.add(new SkillCandidate(41,"IELTS",0));;
        arraylistSkill.add(new SkillCandidate(42,"Windows Phone",0));;
        arraylistSkill.add(new SkillCandidate(43,"WordPress",0));;
        arraylistSkill.add(new SkillCandidate(44,"C#",0));;
        arraylistSkill.add(new SkillCandidate(45,"C++",0));;
        arraylistSkill.add(new SkillCandidate(46,"Laravel",0));;
        arraylistSkill.add(new SkillCandidate(47,"Linux",0));;
        arraylistSkill.add(new SkillCandidate(48,"ООР",0));;
        arraylistSkill.add(new SkillCandidate(49,"Oracle",0));;
        arraylistSkill.add(new SkillCandidate(50,"IELTS ",0));;
        arraylistSkill.add(new SkillCandidate(51,"JAVA",0));;
        arraylistSkill.add(new SkillCandidate(52,"ASP.NET",0));;
        arraylistSkill.add(new SkillCandidate(53,"Javascript",0));;
        arraylistSkill.add(new SkillCandidate(54,"CSS",0));;
        arraylistSkill.add(new SkillCandidate(55,"Designer",0));;
        arraylistSkill.add(new SkillCandidate(56,"Django",0));;
        arraylistSkill.add(new SkillCandidate(57,"REST API",0));;
        arraylistSkill.add(new SkillCandidate(58,"Python",0));;
        arraylistSkill.add(new SkillCandidate(59,"Ruby on Rails",0));;






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
