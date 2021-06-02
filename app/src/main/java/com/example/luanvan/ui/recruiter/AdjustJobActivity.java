package com.example.luanvan.ui.recruiter;

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
import com.example.luanvan.ui.Adapter.skill.SkillTagAdapter;
import com.example.luanvan.ui.Adapter.skill.TagAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.SpinnerNewAdapter;
import com.example.luanvan.ui.Model.GeneralObject;
import com.example.luanvan.ui.Model.JobList;
import com.example.luanvan.ui.Model.SkillCandidate;
import com.example.luanvan.ui.Model.SkillKey;
import com.example.luanvan.ui.recruiter.CVManagement.CVManageActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CandidateDocumentFragment;
import com.example.luanvan.ui.recruiter.CVManagement.JobListFragment;
import com.example.luanvan.ui.recruiter.PostNews.CreateJobActivity;
import com.example.luanvan.ui.recruiter.PostNews.DisplayJobFragment;
import com.example.luanvan.ui.recruiter.PostNews.RecruitmentNewsActivity;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shashank.sony.fancytoastlib.FancyToast;

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

public class AdjustJobActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editPosition, editStart, editEnd, editAddress, editNumber, editSalaryMin, editSalaryMax, editDescription, editRequirement, editBenefit;
    Spinner spinnerKhuvuc, spinnerNganhnghe, spinnerKinhnghiem, spinnerLoaiHinh;
    Button btnUpdate, btnCancel;
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
    int kind = 0; // kind: 0 JoblistFragment, 1: NewPostFragment chuyển qua
    int fragment; // 1: Đang hiển thị, 2 : Chờ xác thực, 3: Hết hạn, 4: Từ chối ,
    // khi NewPostAdapter chuyển qua bên adjustJob thì cập nhật tương ứng với fragment
    int check_change_fragment = 0; // khi cập nhật end date sẽ thay đổi giữa fragment hết hạn và đang hiển thị
    String abc = "same"; // để trả dữ liệu về
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
        setContentView(R.layout.activity_adjust_job);
        anhxa();
        actionBar();
        eventSpinner();
        eventButton();
        getInfo();
        eventDate();
        eventSkill();

    }

    private void eventSkill() {
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), "Â", Toast.LENGTH_SHORT).show();
                bottomSheetSKill = new BottomSheetDialog(AdjustJobActivity.this, R.style.BottomSheetTheme);
                View view = LayoutInflater.from(AdjustJobActivity.this).inflate(R.layout.bottom_sheet_skill, (ViewGroup) findViewById(R.id.bottom_sheet));
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
                recyclerView.setLayoutManager(new LinearLayoutManager(AdjustJobActivity.this, LinearLayoutManager.VERTICAL, false));
                skillAdapter = new SkillTagAdapter(AdjustJobActivity.this, arraylistSkill, AdjustJobActivity.this, arraylistChosenSkill);
                recyclerView.setAdapter(skillAdapter);
                bottomSheetSKill.setContentView(view);
                bottomSheetSKill.show();
            }

        });

    }

    void loading(){
        progressDialog = new ProgressDialog(AdjustJobActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    private void getInfo() {
        kind = getIntent().getIntExtra("kind", 0);
        job = (JobList) getIntent().getSerializableExtra("job");
        position_job = getIntent().getIntExtra("position", 0);
        if(kind == 1){
            fragment = getIntent().getIntExtra("fragment", 0);
        }
       // Toast.makeText(getApplicationContext(), "kind: " + kind + "fragment " + fragment, Toast.LENGTH_SHORT).show();

        job_id = job.getId();
        getJobSkill();
        idArea = job.getIdarea();
        idKindJob = job.getIdtype();
        idProfession = job.getIdprofession();
        for(int i=0; i < dataExperience.size(); i++){
            if(job.getExperience().equals(dataExperience.get(i).getName())){
                idExperience = dataExperience.get(i).getId();
            }
        }
        spinnerNganhnghe.setSelection(idProfession);
        spinnerLoaiHinh.setSelection(idKindJob);
        spinnerKinhnghiem.setSelection(idExperience);
        spinnerKhuvuc.setSelection(idArea);
        editPosition.setText(job.getName());
        String ngaybatdau = job.getStart_date();
        String ngayketthuc = job.getEnd_date();

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = fmt.parse(ngaybatdau);
            date2 = fmt.parse(ngayketthuc);
            date_start = date1;
            date_end = date2;
            date_post_start = fmt.format(date1);
            date_post_end = fmt.format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
        editStart.setText(fmtOut.format(date1));
        editEnd.setText(fmtOut.format(date2));
        editAddress.setText(job.getAddress());
        editRequirement.setText(job.getRequirement());
        editDescription.setText(job.getDescription());
        editBenefit.setText(job.getBenefit());
        editSalaryMin.setText(job.getSalary_min() + "");
        editSalaryMax.setText(job.getSalary_max() + "");
        editNumber.setText(job.getNumber()+"");




    }

    private void getJobSkill() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlGetJobSkill,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    arraylistChosenSkill.add(new SkillKey(
                                       object.getInt("id"),
                                       object.getString("name")
                                    ));
                                    tagAdapter.notifyDataSetChanged();
                                }
                                for(int i=0; i < arraylistSkill.size(); i++){
                                    for(int j=0; j < arraylistChosenSkill.size(); j++){
                                        if(arraylistSkill.get(i).getId() == arraylistChosenSkill.get(j).getId()){
                                            arraylistSkill.get(i).setCheck(1);
                                        }
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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
                map.put("idjob", String.valueOf(job_id));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void postJobSkill() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlUpdateJobSkill,
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
                        FancyToast.makeText(getApplicationContext(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                JSONArray jsonArray = new JSONArray();
                for(int i=0; i < arraylistChosenSkill.size(); i++){
                    JSONObject object = new JSONObject();
                    try {
                        object.put("idjob", String.valueOf(job_id));
                        object.put("idskill", String.valueOf(arraylistChosenSkill.get(i).getId()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(object);
                }
                map.put("jsonarray", jsonArray.toString());
                map.put("idjob", String.valueOf(job_id));

                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
    public void showDate(final int kind) throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                Date date_temp = null;
                try {
                    date_temp = fmt.parse(fmt.format(calendar.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(kind == 1){
                    if(!editEnd.getText().toString().equals("")){
                        if(date_temp.after(date_end) || date_temp.equals(date_end)){
                            FancyToast.makeText(getApplicationContext(), "Ngày bắt đầu phải trước ngày kết thúc", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            x = 1;
                        }else {
                            date_start = calendar.getTime();
                            editStart.setText(dateFormat.format(calendar.getTime()));
                            x = 0;
                        }
                    }else {
                        date_start = calendar.getTime();
                        editStart.setText(dateFormat.format(calendar.getTime()));
                        x = 0;
                    }

                }else {
                    if(!editStart.getText().toString().equals("")){
                        if(date_temp.before(date_start) || date_temp.equals(date_start)){
                            FancyToast.makeText(getApplicationContext(), "Ngày kết thúc phải sau ngày bắt đầu", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            x = 1;
                            // FancyToast.makeText(getApplicationContext(), "0", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                        }else{
                            date_end = calendar.getTime();
                            editEnd.setText(dateFormat.format(calendar.getTime()));
                            x = 0;
                            //  FancyToast.makeText(getApplicationContext(), "1", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                        }
                    }else {
                        date_end = calendar.getTime();
                        editEnd.setText(dateFormat.format(calendar.getTime()));
                        x = 0;
                        // FancyToast.makeText(getApplicationContext(), "2", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                    }

                }


                Date date = null;
                try {
                    date = fmt.parse(fmt.format(calendar.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(kind == 1 && x == 0){
                    date_post_start = fmt.format(date);
                    //  check_start = 1;
                }else if(kind == 2 && x == 0){
                    date_post_end = fmt.format(date);
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
                    showDate(2);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void eventButton() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = editPosition.getText().toString();
                address = editAddress.getText().toString();
                benefit = editBenefit.getText().toString();
                description = editDescription.getText().toString();
                requirement = editRequirement.getText().toString();
                number = editNumber.getText().toString();
                salary_min = Integer.parseInt(editSalaryMin.getText().toString());
                salary_max = Integer.parseInt(editSalaryMax.getText().toString());
                String end = editEnd.getText().toString();
                String start = editStart.getText().toString();
                String min = editSalaryMin.getText().toString();
                String max = editSalaryMax.getText().toString();
           //     Toast.makeText(getApplicationContext(),"start: " + date_post_start + " start: " + date_post_end, Toast.LENGTH_SHORT).show();
                if(end.equals("") || start.equals("") || address.equals("") || benefit.equals("")
                    || description.equals("") || requirement.equals("") || number.equals("") || min.equals("")
                    || position.equals("") || max.equals("")){
                   FancyToast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else if(idExperience == 0){
                    FancyToast.makeText(getApplicationContext(), "Vui lòng chọn kinh nghiệm", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else if(idProfession == 0){
                    FancyToast.makeText(getApplicationContext(), "Vui lòng chọn ngành nghề", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else if(idArea == 0){
                    FancyToast.makeText(getApplicationContext(), "Vui lòng chọn khu vực", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else if(idKindJob == 0){
                    FancyToast.makeText(getApplicationContext(), "Vui lòng chọn loại hình công việc", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else if(Integer.parseInt(editSalaryMin.getText().toString()) >= Integer.parseInt(editSalaryMax.getText().toString())){
                    FancyToast.makeText(getApplicationContext(), "Mức lương không hợp lệ", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else if(date_start.after(date_end)){
                    FancyToast.makeText(getApplicationContext(), "Ngày bắt đầu phải trước ngày kết thúc", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }
                else if (date_end.before(date_start)) {
                    FancyToast.makeText(getApplicationContext(), "Ngày kết thúc phải sau ngày bắt đầu", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }

                else {

                    loading();


                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlUpdateJob,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("success")){
                                        FancyToast.makeText(getApplicationContext(), "Cập nhật thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                        postJobSkill();
                                        if(kind == 0){
                                            updatePreviousJoblist();
                                        }else {

                                            switch (fragment){
                                                case 1:
                                                    // Nếu end date mà update thành hết hạn -> chuyển bên kia
                                                    SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd");
                                                    Date date1 = null;
                                                    try {
                                                        date1 = fmt1.parse(date_post_end);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    Date current = new Date();
                                                    String now = fmt1.format(current);
                                                    try {

                                                        current = fmt1.parse(now);

                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    if(date1.compareTo(current) == 0 || date1.after(current)){
                                                        updatePreviousJobDisplay();
                                                    }else {
                                                        updatePreviousJobDisplay();
                                                        RecruiterActivity.arrayListOutdatedJobs.add(RecruiterActivity.arrayListJobList.get(position_job));
                                                        RecruiterActivity.arrayListJobList.remove(position_job);
                                                    }
                                                    break;
                                                case 2:
                                                    updatePreviousAuthenticationJob();
                                                    break;
                                                case 3:
                                                    // Nếu end date mà update thành còn thời hạn -> chuyển qua đang hiển thị và xóa bên hết hạn
                                                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                                                    Date date = null;
                                                    try {
                                                        date = fmt.parse(date_post_end);
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }

                                                    Date current1 = new Date();
                                                    String now1 = fmt.format(current1);
                                                    try {
                                                        current1 = fmt.parse(now1);

                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }

                                                    if(date.compareTo(current1) == 0 || date.after(current1)){
                                                        updatePreviousOutdatedJob();
                                                        RecruiterActivity.arrayListJobList.add(RecruiterActivity.arrayListOutdatedJobs.get(position_job));
                                                        RecruiterActivity.arrayListOutdatedJobs.remove(position_job);
                                                    }else {
                                                        updatePreviousOutdatedJob();
                                                    }

                                                    break;
                                                case 4:
                                                    updatePreviousRejectJob();
                                                    handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            RecruiterActivity.arrayListAuthenticationJobs.add(RecruiterActivity.arrayListRejectJobs.get(position_job));
                                                            RecruiterActivity.arrayListRejectJobs.remove(position_job);
                                                        }
                                                    },2000);

                                                    break;
                                            }

                                        }
                                        handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                Intent intent = new Intent();
                                                if(fragment == 1){
                                                    setResult(123);
                                                }else if(fragment == 2){
                                                    setResult(234);
                                                }else if(fragment == 4){
                                                    setResult(345);
                                                }else if(fragment == 3){
                                                    setResult(333);
                                                }
                                                finish();
                                            }
                                        },3000);


                                    }else {
                                        FancyToast.makeText(getApplicationContext(),"Cập nhật thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    FancyToast.makeText(getApplicationContext(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                    progressDialog.dismiss();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("job_id", String.valueOf(job_id));
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
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);


                }
            }
        });
    }

    public void updatePreviousJoblist(){
        RecruiterActivity.arrayListJobList.get(position_job).setName(position);
        RecruiterActivity.arrayListJobList.get(position_job).setAddress(address);
        RecruiterActivity.arrayListJobList.get(position_job).setBenefit(benefit);
        RecruiterActivity.arrayListJobList.get(position_job).setDescription(description);
        RecruiterActivity.arrayListJobList.get(position_job).setRequirement(requirement);
        RecruiterActivity.arrayListJobList.get(position_job).setNumber(Integer.parseInt(number));
        RecruiterActivity.arrayListJobList.get(position_job).setSalary_min(salary_min);
        RecruiterActivity.arrayListJobList.get(position_job).setSalary_max(salary_max);
        RecruiterActivity.arrayListJobList.get(position_job).setIdarea(idArea);
        RecruiterActivity.arrayListJobList.get(position_job).setIdprofession(idProfession);
        RecruiterActivity.arrayListJobList.get(position_job).setIdtype(idKindJob);
        RecruiterActivity.arrayListJobList.get(position_job).setStart_date(date_post_start);
        RecruiterActivity.arrayListJobList.get(position_job).setEnd_date(date_post_end);
        JobListFragment.adapter.notifyDataSetChanged();
        // cập nhật cho hồ sơ ứng tuyển
        for(int i=0; i < CVManageActivity.arrayListAll.size(); i++){
            if(CVManageActivity.arrayListAll.get(i).getJob_id() == job_id){
                CVManageActivity.arrayListAll.get(i).setJob_name(position);
                CandidateDocumentFragment.adapter.notifyDataSetChanged();
            }

        }
        switch (idExperience){
            case 1:
                RecruiterActivity.arrayListJobList.get(position_job).setExperience("Chưa có kinh nghiệm");
                break;
            case 2:
                RecruiterActivity.arrayListJobList.get(position_job).setExperience("Dưới 1 năm");
                break;
            case 3:
                RecruiterActivity.arrayListJobList.get(position_job).setExperience("1 năm");
                break;
            case 4:
                RecruiterActivity.arrayListJobList.get(position_job).setExperience("2 năm");
                break;
            case 5:
                RecruiterActivity.arrayListJobList.get(position_job).setExperience("3 năm");
                break;
            case 6:
                RecruiterActivity.arrayListJobList.get(position_job).setExperience("4 năm");
                break;
            case 7:
                RecruiterActivity.arrayListJobList.get(position_job).setExperience("5 năm");
                break;
            case 8:
                RecruiterActivity.arrayListJobList.get(position_job).setExperience("Trên 5 năm");
                break;
        }
    }
    public void updatePreviousJobDisplay(){
        RecruiterActivity.arrayListJobList.get(position_job).setName(position);
        RecruiterActivity.arrayListJobList.get(position_job).setAddress(address);
        RecruiterActivity.arrayListJobList.get(position_job).setBenefit(benefit);
        RecruiterActivity.arrayListJobList.get(position_job).setDescription(description);
        RecruiterActivity.arrayListJobList.get(position_job).setRequirement(requirement);
        RecruiterActivity.arrayListJobList.get(position_job).setNumber(Integer.parseInt(number));
        RecruiterActivity.arrayListJobList.get(position_job).setSalary_min(salary_min);
        RecruiterActivity.arrayListJobList.get(position_job).setSalary_max(salary_max);
        RecruiterActivity.arrayListJobList.get(position_job).setIdarea(idArea);
        RecruiterActivity.arrayListJobList.get(position_job).setIdprofession(idProfession);
        RecruiterActivity.arrayListJobList.get(position_job).setIdtype(idKindJob);
        RecruiterActivity.arrayListJobList.get(position_job).setStart_date(date_post_start);
        RecruiterActivity.arrayListJobList.get(position_job).setEnd_date(date_post_end);
    //    DisplayJobFragment.adapter.notifyDataSetChanged();
        switch (idExperience){
            case 1:
                RecruiterActivity.arrayListJobList.get(position_job).setExperience("Chưa có kinh nghiệm");
                break;
            case 2:
                RecruiterActivity.arrayListJobList.get(position_job).setExperience("Dưới 1 năm");
                break;
            case 3:
                RecruiterActivity.arrayListJobList.get(position_job).setExperience("1 năm");
                break;
            case 4:
                RecruiterActivity.arrayListJobList.get(position_job).setExperience("2 năm");
                break;
            case 5:
                RecruiterActivity.arrayListJobList.get(position_job).setExperience("3 năm");
                break;
            case 6:
                RecruiterActivity.arrayListJobList.get(position_job).setExperience("4 năm");
                break;
            case 7:
                RecruiterActivity.arrayListJobList.get(position_job).setExperience("5 năm");
                break;
            case 8:
                RecruiterActivity.arrayListJobList.get(position_job).setExperience("Trên 5 năm");
                break;
        }
    }
    public void updatePreviousOutdatedJob(){
        RecruiterActivity.arrayListOutdatedJobs.get(position_job).setName(position);
        RecruiterActivity.arrayListOutdatedJobs.get(position_job).setAddress(address);
        RecruiterActivity.arrayListOutdatedJobs.get(position_job).setBenefit(benefit);
        RecruiterActivity.arrayListOutdatedJobs.get(position_job).setDescription(description);
        RecruiterActivity.arrayListOutdatedJobs.get(position_job).setRequirement(requirement);
        RecruiterActivity.arrayListOutdatedJobs.get(position_job).setNumber(Integer.parseInt(number));
        RecruiterActivity.arrayListOutdatedJobs.get(position_job).setSalary_min(salary_min);
        RecruiterActivity.arrayListOutdatedJobs.get(position_job).setSalary_max(salary_max);
        RecruiterActivity.arrayListOutdatedJobs.get(position_job).setIdarea(idArea);
        RecruiterActivity.arrayListOutdatedJobs.get(position_job).setIdprofession(idProfession);
        RecruiterActivity.arrayListOutdatedJobs.get(position_job).setIdtype(idKindJob);
        RecruiterActivity.arrayListOutdatedJobs.get(position_job).setStart_date(date_post_start);
        RecruiterActivity.arrayListOutdatedJobs.get(position_job).setEnd_date(date_post_end);
        //    DisplayJobFragment.adapter.notifyDataSetChanged();
        switch (idExperience){
            case 1:
                RecruiterActivity.arrayListOutdatedJobs.get(position_job).setExperience("Chưa có kinh nghiệm");
                break;
            case 2:
                RecruiterActivity.arrayListOutdatedJobs.get(position_job).setExperience("Dưới 1 năm");
                break;
            case 3:
                RecruiterActivity.arrayListOutdatedJobs.get(position_job).setExperience("1 năm");
                break;
            case 4:
                RecruiterActivity.arrayListOutdatedJobs.get(position_job).setExperience("2 năm");
                break;
            case 5:
                RecruiterActivity.arrayListOutdatedJobs.get(position_job).setExperience("3 năm");
                break;
            case 6:
                RecruiterActivity.arrayListOutdatedJobs.get(position_job).setExperience("4 năm");
                break;
            case 7:
                RecruiterActivity.arrayListOutdatedJobs.get(position_job).setExperience("5 năm");
                break;
            case 8:
                RecruiterActivity.arrayListOutdatedJobs.get(position_job).setExperience("Trên 5 năm");
                break;
        }
    }

    public void updatePreviousRejectJob(){
        RecruiterActivity.arrayListRejectJobs.get(position_job).setName(position);
        RecruiterActivity.arrayListRejectJobs.get(position_job).setAddress(address);
        RecruiterActivity.arrayListRejectJobs.get(position_job).setBenefit(benefit);
        RecruiterActivity.arrayListRejectJobs.get(position_job).setDescription(description);
        RecruiterActivity.arrayListRejectJobs.get(position_job).setRequirement(requirement);
        RecruiterActivity.arrayListRejectJobs.get(position_job).setNumber(Integer.parseInt(number));
        RecruiterActivity.arrayListRejectJobs.get(position_job).setSalary_min(salary_min);
        RecruiterActivity.arrayListRejectJobs.get(position_job).setSalary_max(salary_max);
        RecruiterActivity.arrayListRejectJobs.get(position_job).setIdarea(idArea);
        RecruiterActivity.arrayListRejectJobs.get(position_job).setIdprofession(idProfession);
        RecruiterActivity.arrayListRejectJobs.get(position_job).setIdtype(idKindJob);
        RecruiterActivity.arrayListRejectJobs.get(position_job).setStart_date(date_post_start);
        RecruiterActivity.arrayListRejectJobs.get(position_job).setEnd_date(date_post_end);
        RecruiterActivity.arrayListRejectJobs.get(position_job).setStatus(1);
        RecruiterActivity.arrayListRejectJobs.get(position_job).setNote_reject("");
        //    DisplayJobFragment.adapter.notifyDataSetChanged();
        switch (idExperience){
            case 1:
                RecruiterActivity.arrayListRejectJobs.get(position_job).setExperience("Chưa có kinh nghiệm");
                break;
            case 2:
                RecruiterActivity.arrayListRejectJobs.get(position_job).setExperience("Dưới 1 năm");
                break;
            case 3:
                RecruiterActivity.arrayListRejectJobs.get(position_job).setExperience("1 năm");
                break;
            case 4:
                RecruiterActivity.arrayListRejectJobs.get(position_job).setExperience("2 năm");
                break;
            case 5:
                RecruiterActivity.arrayListRejectJobs.get(position_job).setExperience("3 năm");
                break;
            case 6:
                RecruiterActivity.arrayListRejectJobs.get(position_job).setExperience("4 năm");
                break;
            case 7:
                RecruiterActivity.arrayListRejectJobs.get(position_job).setExperience("5 năm");
                break;
            case 8:
                RecruiterActivity.arrayListRejectJobs.get(position_job).setExperience("Trên 5 năm");
                break;
        }
        moveToAutheJob(1, position_job);

    }
    public void moveToAutheJob(final int status, final int position){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlAcceptJob,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                           // FancyToast.makeText(getApplicationContext(), "Cập nhật thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

                        }else {
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
                map.put("job_id", String.valueOf(RecruiterActivity.arrayListRejectJobs.get(position).getId()));
                map.put("status", String.valueOf(status));
                map.put("note_reject", "");
                return map;
            }
        };
        requestQueue.add(stringRequest);


    }

    public void updatePreviousAuthenticationJob(){
        RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setName(position);
        RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setAddress(address);
        RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setBenefit(benefit);
        RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setDescription(description);
        RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setRequirement(requirement);
        RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setNumber(Integer.parseInt(number));
        RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setSalary_min(salary_min);
        RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setSalary_max(salary_max);
        RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setIdarea(idArea);
        RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setIdprofession(idProfession);
        RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setIdtype(idKindJob);
        RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setStart_date(date_post_start);
        RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setEnd_date(date_post_end);
        //    DisplayJobFragment.adapter.notifyDataSetChanged();
        switch (idExperience){
            case 1:
                RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setExperience("Chưa có kinh nghiệm");
                break;
            case 2:
                RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setExperience("Dưới 1 năm");
                break;
            case 3:
                RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setExperience("1 năm");
                break;
            case 4:
                RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setExperience("2 năm");
                break;
            case 5:
                RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setExperience("3 năm");
                break;
            case 6:
                RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setExperience("4 năm");
                break;
            case 7:
                RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setExperience("5 năm");
                break;
            case 8:
                RecruiterActivity.arrayListAuthenticationJobs.get(position_job).setExperience("Trên 5 năm");
                break;
        }
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
        btnUpdate = (Button) findViewById(R.id.buttoncapnhat);
        btnCancel = (Button) findViewById(R.id.buttonhuy);

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

        tagAdapter = new TagAdapter(AdjustJobActivity.this, arraylistChosenSkill, AdjustJobActivity.this, arraylistSkill );
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
