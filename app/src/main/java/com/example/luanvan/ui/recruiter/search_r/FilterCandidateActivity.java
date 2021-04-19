package com.example.luanvan.ui.recruiter.search_r;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
import com.example.luanvan.ui.Adapter.position_a.PositionPickAdapter;
import com.example.luanvan.ui.Adapter.recruit.CandidateScheduleAdapter;
import com.example.luanvan.ui.Adapter.recruit.PositionScheduleAdapter;
import com.example.luanvan.ui.Adapter.skill.AreaBottomSheetTagAdapter;
import com.example.luanvan.ui.Adapter.skill.TagAreaAdapter;
import com.example.luanvan.ui.Model.Area;
import com.example.luanvan.ui.Model.AreaCandidate;
import com.example.luanvan.ui.Model.JobList;
import com.example.luanvan.ui.Model.Position;
import com.example.luanvan.ui.Model.SkillKey;
import com.example.luanvan.ui.Model.UserApplicant;
import com.example.luanvan.ui.Model.UserSearch;
import com.example.luanvan.ui.UpdateInfo.PersonalInfoActivity;
import com.example.luanvan.ui.schedule.CreateScheduleActivity;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FilterCandidateActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editPosition, editJob;
    public static int idposition = 0;
    BottomSheetDialog bottomSheetPosition;
    public static String position  = "";
    RecyclerView recyclerView;
    PositionPickAdapter adapter;
    ArrayList<Position> arrayList; // posiotion
    SearchView searchView;
    RecyclerView recyclerViewArea, recyclerViewTagArea;
    TagAreaAdapter tagAdapter; // Những tag trong recycleview
    public static ArrayList<Area>  arraylistChosenArea; // Đã chọn
    ArrayList<AreaCandidate> arraylistArea; // Trên bottemsheet có check hay k luôn
    LinearLayout layout_area;
    BottomSheetDialog bottomSheetArea;
    SearchView searchViewArea;
    AreaBottomSheetTagAdapter areaAdapter; // adapter trong bottomsheet

    public static ArrayList<JobList> jobArrayList;
    PositionScheduleAdapter positionScheduleAdapter;
    RecyclerView recyclerViewJob; // Tin tuyen dung
    BottomSheetDialog bottomSheetJob; // Tin tuyen dung
    public static int job_id = 0;
    public static String job_name = "";

    Button btnDongY, btnHuy;
    ProgressDialog progressDialog;
    Handler handler;
    String jobSkill = "";
    ArrayList<SkillKey> arrayListJobSkill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_candidate);
        anhxa();
      //  Toast.makeText(getApplicationContext(), job_id +"", Toast.LENGTH_SHORT).show();
        actionBar();
        eventEdit();
        eventButton();
        eventArea();
        if(SearchCandidateActivity.search_or_not == 1){
            getInfo();
        }



    }

    private void getInfo() {
        for(int i=0; i < arrayList.size(); i++){
            if(arrayList.get(i).getId() == idposition){
                editPosition.setText(arrayList.get(i).getName());
            }
        }
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(job_id != 0){
                    getJobSkill();
                }
                for(int i=0; i < jobArrayList.size(); i++){
                    if(jobArrayList.get(i).getId() == job_id){
                        job_name = jobArrayList.get(i).getName();
                        editJob.setText(job_name);
                    }
                }
            }
        },500);





    }

    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    private void eventArea() {
        layout_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), "Â", Toast.LENGTH_SHORT).show();
                bottomSheetArea = new BottomSheetDialog(FilterCandidateActivity.this, R.style.BottomSheetTheme);
                View view = LayoutInflater.from(FilterCandidateActivity.this).inflate(R.layout.bottom_sheet_area, (ViewGroup) findViewById(R.id.bottom_sheet));
                Button btnChoose = (Button) view.findViewById(R.id.buttonchon);
                Button btnCancel = (Button) view.findViewById(R.id.buttonhuy);
                bottomSheetArea.setCancelable(false);
                searchView = (SearchView) view.findViewById(R.id.searchView);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        areaAdapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        areaAdapter.getFilter().filter(newText);
                        return false;
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arraylistChosenArea.clear();
                        for(int i=0; i < arraylistArea.size(); i++){
                            arraylistArea.get(i).setCheck(0);
                        }
                        bottomSheetArea.dismiss();
                        tagAdapter.notifyDataSetChanged();
                    }
                });
                btnChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tagAdapter.notifyDataSetChanged();
                        bottomSheetArea.dismiss();
                    }
                });
                recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(FilterCandidateActivity.this, LinearLayoutManager.VERTICAL, false));
                areaAdapter = new AreaBottomSheetTagAdapter(FilterCandidateActivity.this, arraylistArea, FilterCandidateActivity.this, arraylistChosenArea);
                recyclerView.setAdapter(areaAdapter);
                bottomSheetArea.setContentView(view);
                bottomSheetArea.show();
            }

        });

    }

    private void eventButton() {
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayListJobSkill.clear();
                arraylistChosenArea.clear();
                idposition = 0;
                job_id = 0;
                SearchCandidateActivity.search_or_not = 0;
                Intent intent = new Intent();
                setResult(2);
                finish();
            }
        });
        btnDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchCandidateActivity.search_or_not = 1;
                String area = "(";
                if(arraylistChosenArea.size() > 0){
                    for(int i=0; i < arraylistChosenArea.size(); i++){
                        if(i == arraylistChosenArea.size() - 1){
                            area += arraylistChosenArea.get(i).getId() + ")";
                        }else {
                            area += arraylistChosenArea.get(i).getId() + ",";
                        }
                    }
                }

                jobSkill = "(";
                for(int i=0; i < arrayListJobSkill.size(); i++){
                    if(i == arrayListJobSkill.size() - 1){
                        jobSkill += arrayListJobSkill.get(i).getId() + ")";
                    }else {
                        jobSkill += arrayListJobSkill.get(i).getId() + ",";
                    }
                }
               // Toast.makeText(getApplicationContext(), jobSkill, Toast.LENGTH_SHORT).show();
              //  Toast.makeText(getApplicationContext(), arraylistChosenArea.size() + "", Toast.LENGTH_SHORT).show();
                loading();
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                final String finalArea = area;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlSearchUser,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                           //     Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                SearchCandidateActivity.arrayList.clear();
                                if(response != null){
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        for(int i=0; i < jsonArray.length(); i++){
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            int check_trunglap = 0;
                                            if(SearchCandidateActivity.arrayList.size() > 0){
                                                for(int j=0; j < SearchCandidateActivity.arrayList.size(); j++){
                                                    if(object.getInt("iduser") == SearchCandidateActivity.arrayList.get(j).getIduser()){
                                                        check_trunglap = 1;
                                                    }
                                                }
                                            }

                                            if(check_trunglap == 0){
                                                SearchCandidateActivity.arrayList.add(new UserSearch(
                                                        object.getInt("iduser"),
                                                        object.getInt("idposition"),
                                                        object.getString("position"),
                                                        object.getInt("idcv"),
                                                        object.getString("user_id_f"),
                                                        object.getString("username"),
                                                        object.getString("birthday"),
                                                        object.getInt("gender"),
                                                        object.getString("address"),
                                                        object.getString("email"),
                                                        object.getString("introduction"),
                                                        object.getInt("phone"),
                                                        object.getInt("mode"),
                                                        object.getString("experience"),
                                                        object.getString("study"),
                                                        object.getInt("idarea"),
                                                        object.getString("area")
                                                ));
                                            }


                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent();
                                            setResult(1);
                                            finish();
                                        }
                                    },2000);


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
                        map.put("idposition", String.valueOf(idposition));
                        int checkarea = 0;
                        if(arraylistChosenArea.size() > 0){
                            checkarea = 1;
                        }
                        map.put("checkarea", String.valueOf(checkarea));
                        map.put("area", finalArea);
                        int checkjobskill = 0;
                        if(arrayListJobSkill.size() > 0){
                            checkjobskill = 1;
                        }
                        map.put("checkjobskill", String.valueOf(checkjobskill));
                        map.put("jobskill", jobSkill);
                        return map;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });

    }

    private void eventEdit() {
        editPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListPosition();
            }
        });
        editJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventSheetJob();
            }
        });

    }
    public void eventSheetJob(){
        bottomSheetJob = new BottomSheetDialog(FilterCandidateActivity.this, R.style.BottomSheetTheme);
        final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_position, (ViewGroup) findViewById(R.id.bottom_sheet));
        Button btnChoose = (Button) view.findViewById(R.id.buttonchon);
        Button btnCancel = (Button) view.findViewById(R.id.buttonhuy);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editJob.setText("");
                job_id = 0;
                arrayListJobSkill.clear();
                bottomSheetJob.dismiss();
            }
        });
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editJob.setText(job_name);
                getJobSkill();
                bottomSheetJob.dismiss();
            }
        });


        recyclerViewJob = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerViewJob.setHasFixedSize(false);
        recyclerViewJob.setLayoutManager(new LinearLayoutManager(FilterCandidateActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerViewJob.setAdapter(positionScheduleAdapter);
        bottomSheetJob.setContentView(view);
        bottomSheetJob.show();

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
                                    arrayListJobSkill.add(new SkillKey(
                                            object.getInt("id"),
                                            object.getString("name")
                                    ));
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
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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
    private void eventListPosition() {
        //    Toast.makeText(getApplicationContext(), "sasa", Toast.LENGTH_SHORT).show();
        bottomSheetPosition = new BottomSheetDialog(FilterCandidateActivity.this,  R.style.BottomSheetTheme);
        View view = LayoutInflater.from(FilterCandidateActivity.this).inflate(R.layout.bottom_sheet_position_user, (ViewGroup) findViewById(R.id.bottom_sheet));
        Button btnChoose = (Button) view.findViewById(R.id.buttonchon);
        Button btnCancel = (Button) view.findViewById(R.id.buttonhuy);
        searchView  = (SearchView)view. findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetPosition.dismiss();
                idposition = 0;
                editPosition.setText("");
            }
        });
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPosition.setText(position);
                bottomSheetPosition.dismiss();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(FilterCandidateActivity.this, LinearLayoutManager.VERTICAL, false));
        adapter = new PositionPickAdapter(FilterCandidateActivity.this, arrayList, FilterCandidateActivity.this,2);// kind 1: Từ ứng viên cho, kind 2: Từ nhà tuyển dụng chọn
        recyclerView.setAdapter(adapter);
        bottomSheetPosition.setContentView(view);
        bottomSheetPosition.show();

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
        editJob = (EditText) findViewById(R.id.editjob);
        btnDongY = (Button) findViewById(R.id.buttondongy);
        btnHuy = (Button) findViewById(R.id.buttonhuy);
        arrayList = new ArrayList<>();
        arrayListJobSkill = new ArrayList<>();
        getDataPosition();
        arraylistArea = new ArrayList<>();
        if(SearchCandidateActivity.search_or_not == 0){
            arraylistChosenArea = new ArrayList<>();
        }


        getDataArea();
        layout_area = (LinearLayout) findViewById(R.id.layout_area);

        recyclerViewTagArea = (RecyclerView) findViewById(R.id.recycleview);
        recyclerViewTagArea.setFocusable(false);
        recyclerViewTagArea.setClickable(false);
        recyclerViewTagArea.setHasFixedSize(true);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager();
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        recyclerViewTagArea.setLayoutManager(layoutManager);

        tagAdapter = new TagAreaAdapter(FilterCandidateActivity.this, arraylistChosenArea, FilterCandidateActivity.this, arraylistArea );
        recyclerViewTagArea.setAdapter(tagAdapter);
        if(SearchCandidateActivity.search_or_not == 0){
            jobArrayList = new ArrayList<>();
            getDataJob();
        }

        // kind: 1: create, 2: adjust, 3: search
        positionScheduleAdapter = new PositionScheduleAdapter(FilterCandidateActivity.this, jobArrayList, FilterCandidateActivity.this, 3);


    }
    private void getDataJob() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlJobList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                jobArrayList.add(new JobList(
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
                            }
                            positionScheduleAdapter.notifyDataSetChanged();

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
                map.put("idrecuiter", String.valueOf(MainActivity.iduser));
                map.put("status_post", String.valueOf(0));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void getDataPosition() {
        arrayList.add(new Position(1,"Chuyên viên Nhân sự"));
        arrayList.add(new Position(2,"Chuyên viên Tuyển dụng"));
        arrayList.add(new Position(3,"Nhân viên Hành chính - Văn phòng"));
        arrayList.add(new Position(4,"Nhân viên Hành chính - Nhân sự"));
        arrayList.add(new Position(5,"Nhân viên Hành chính - Kế toán."));
        arrayList.add(new Position(6,"Nhân viên kinh doanh"));
        arrayList.add(new Position(7,"Nhân viên bán hàng"));
        arrayList.add(new Position(8,"Sales survey - Tiếp thị"));
        arrayList.add(new Position(9,"Tư vấn bán hàng"));
        arrayList.add(new Position(10,"Trưởng nhóm kinh doanh"));
        arrayList.add(new Position(11,"Giám đốc kinh doanh"));
        arrayList.add(new Position(12,"Giám sát bán hàng"));
        arrayList.add(new Position(13,"Tư vấn tài chính"));
        arrayList.add(new Position(14,"Nghiên cứu thị trường"));
        arrayList.add(new Position(15,"Telesales"));
        arrayList.add(new Position(16,"Sales bảo hiểm"));
        arrayList.add(new Position(17,"Sales Bất động sản"));
        arrayList.add(new Position(18,"Sales Thị trường"));
        arrayList.add(new Position(19,"Nhân viên phát triển thị trường"));
        arrayList.add(new Position(20,"Business Manager"));
        arrayList.add(new Position(21,"Trình Dược Viên"));
        arrayList.add(new Position(22,"Chăm sóc khách hàng"));
        arrayList.add(new Position(23,"Tư vấn đào tạo"));
        arrayList.add(new Position(24,"Thu ngân"));
        arrayList.add(new Position(25,"Lễ tân"));
        arrayList.add(new Position(26,"Giao dịch viên"));
        arrayList.add(new Position(27,"Kế toán"));
        arrayList.add(new Position(28,"Kế toán tổng hợp"));
        arrayList.add(new Position(29,"Nhân viên Marketing"));
        arrayList.add(new Position(30,"Trưởng nhóm Marketing"));
        arrayList.add(new Position(31,"Giám đốc Marketing"));
        arrayList.add(new Position(32,"Digital Marketing"));
        arrayList.add(new Position(33,"Marketing Ads"));
        arrayList.add(new Position(34,"Copywriter"));
        arrayList.add(new Position(35,"Content Writer"));
        arrayList.add(new Position(36,"Admin Fanpage"));
        arrayList.add(new Position(37,"PR Truyền thông"));
        arrayList.add(new Position(38,"SEO"));
        arrayList.add(new Position(39,"Tổ chức sự kiện"));
        arrayList.add(new Position(40,"IT Support / Helpdesk"));
        arrayList.add(new Position(41,"Lập trình viên"));
        arrayList.add(new Position(42,"Thiết kế web"));
        arrayList.add(new Position(43,"Lập trình web"));
        arrayList.add(new Position(44,"Frontend Developer"));
        arrayList.add(new Position(45,"Backend Developer"));
        arrayList.add(new Position(46,"Quản trị mạng"));
        arrayList.add(new Position(47,"Quản trị hệ thống - System admin"));
        arrayList.add(new Position(48,"PHP Developer"));
        arrayList.add(new Position(49,"Java Developer"));
        arrayList.add(new Position(50,".Net Developer"));
        arrayList.add(new Position(51,"Android Developer"));
        arrayList.add(new Position(52,"IOS Developer"));
        arrayList.add(new Position(53,"Game Developer"));
        arrayList.add(new Position(54,"Unity Developer"));
        arrayList.add(new Position(55,"C/C# Developer"));
        arrayList.add(new Position(56,"Cocos Developer"));
        arrayList.add(new Position(57,"Ruby Developer"));
        arrayList.add(new Position(58,"Python Developer"));
        arrayList.add(new Position(59,"Tester"));
        arrayList.add(new Position(60,"QA/QC"));
        arrayList.add(new Position(61,"Giáo viên"));
        arrayList.add(new Position(62,"Trợ giảng"));
        arrayList.add(new Position(63,"Giáo viên tiếng Anh"));
        arrayList.add(new Position(64,"Giáo viên tiếng Trung"));
        arrayList.add(new Position(65,"Phiên dịch viên"));
        arrayList.add(new Position(66,"Phiên dịch viên tiếng Anh"));
        arrayList.add(new Position(67,"Phiên dịch viên tiếng Trung"));
        arrayList.add(new Position(68,"Phiên dịch viên tiếng Nhật"));
        arrayList.add(new Position(69,"Xuất nhập khẩu"));
        arrayList.add(new Position(70,"Nhân viên phục vụ"));
        arrayList.add(new Position(71,"Nhân viên pha chế"));
        arrayList.add(new Position(72,"Luật - Pháp chế"));
        arrayList.add(new Position(73,"Hướng dẫn viên du lịch"));
        arrayList.add(new Position(74,"Nhân viên bán tour"));
        arrayList.add(new Position(75,"Điều hành tour"));
        arrayList.add(new Position(76,"Nghiên cứu phát triển - R&D"));
        arrayList.add(new Position(77,"Designer"));
        arrayList.add(new Position(78,"Thiết kế đồ họa"));
        arrayList.add(new Position(79,"Diễn họa nội thất"));
        arrayList.add(new Position(80,"Thiết kế nội thất"));
        arrayList.add(new Position(81,"Điều dưỡng viên"));
        arrayList.add(new Position(82,"Nhân viên bếp"));
        arrayList.add(new Position(83,"Trợ lý giám đốc"));
        arrayList.add(new Position(84,"Thư ký"));
        arrayList.add(new Position(85,"Search Engine Marketing"));
        arrayList.add(new Position(86,"Nhân viên kho"));
        arrayList.add(new Position(87,"Nhân viên buồng phòng"));
        arrayList.add(new Position(88,"Lập trình di động"));
        arrayList.add(new Position(89,"Kế toán trưởng"));
        arrayList.add(new Position(90,"Thiết kế UX/UI"));
        arrayList.add(new Position(91,"Kỹ sư xây dựng"));
        arrayList.add(new Position(92,"Kỹ sư Cơ khí"));
        arrayList.add(new Position(93,"Kỹ sư điện - Cơ điện"));

    }
    private void getDataArea() {
        arraylistArea.add(new AreaCandidate(1,"An Giang",0));
        arraylistArea.add(new AreaCandidate(2,"Bà Rịa - Vũng Tàu",0));
        arraylistArea.add(new AreaCandidate(3,"Bắc Giang",0));
        arraylistArea.add(new AreaCandidate(4,"Bắc Kạn",0));
        arraylistArea.add(new AreaCandidate(5,"Bạc Liêu",0));
        arraylistArea.add(new AreaCandidate(6,"Bắc Ninh",0));
        arraylistArea.add(new AreaCandidate(7,"Bến Tre",0));
        arraylistArea.add(new AreaCandidate(8,"Bình Định",0));
        arraylistArea.add(new AreaCandidate(9,"Bình Dương",0));
        arraylistArea.add(new AreaCandidate(10,"Bình Phước",0));
        arraylistArea.add(new AreaCandidate(11,"Bình Thuận",0));
        arraylistArea.add(new AreaCandidate(12,"Cà Mau",0));
        arraylistArea.add(new AreaCandidate(13,"Cao Bằng",0));
        arraylistArea.add(new AreaCandidate(14,"Đắk Lắk",0));
        arraylistArea.add(new AreaCandidate(15,"Đắk Nông",0));
        arraylistArea.add(new AreaCandidate(16,"Điện Biên",0));
        arraylistArea.add(new AreaCandidate(17,"Đồng Nai",0));
        arraylistArea.add(new AreaCandidate(18,"Đồng Tháp",0));
        arraylistArea.add(new AreaCandidate(19,"Gia Lai",0));
        arraylistArea.add(new AreaCandidate(20,"Hà Giang",0));
        arraylistArea.add(new AreaCandidate(21,"Hà Nam",0));
        arraylistArea.add(new AreaCandidate(22,"Hà Tĩnh",0));
        arraylistArea.add(new AreaCandidate(23,"Hải Dương",0));
        arraylistArea.add(new AreaCandidate(24,"Hậu Giang",0));
        arraylistArea.add(new AreaCandidate(25,"Hòa Bình",0));
        arraylistArea.add(new AreaCandidate(26,"Hưng Yên",0));
        arraylistArea.add(new AreaCandidate(27,"Khánh Hòa",0));
        arraylistArea.add(new AreaCandidate(28,"Kiên Giang",0));
        arraylistArea.add(new AreaCandidate(29,"Kon Tum",0));
        arraylistArea.add(new AreaCandidate(30,"Lai Châu",0));
        arraylistArea.add(new AreaCandidate(31,"Lâm Đồng",0));
        arraylistArea.add(new AreaCandidate(32,"Lạng Sơn",0));
        arraylistArea.add(new AreaCandidate(33,"Lào Cai",0));
        arraylistArea.add(new AreaCandidate(34,"Long An",0));
        arraylistArea.add(new AreaCandidate(35,"Nam Định",0));
        arraylistArea.add(new AreaCandidate(36,"Nghệ An",0));
        arraylistArea.add(new AreaCandidate(37,"Ninh Bình",0));
        arraylistArea.add(new AreaCandidate(38,"Ninh Thuận",0));
        arraylistArea.add(new AreaCandidate(39,"Phú Thọ",0));
        arraylistArea.add(new AreaCandidate(40,"Quảng Bình",0));
        arraylistArea.add(new AreaCandidate(41,"Quảng Nam",0));
        arraylistArea.add(new AreaCandidate(42,"Quảng Ngãi",0));
        arraylistArea.add(new AreaCandidate(43,"Quảng Ninh",0));
        arraylistArea.add(new AreaCandidate(44,"Quảng Trị",0));
        arraylistArea.add(new AreaCandidate(45,"Sóc Trăng",0));
        arraylistArea.add(new AreaCandidate(46,"Sơn La",0));
        arraylistArea.add(new AreaCandidate(47,"Tây Ninh",0));
        arraylistArea.add(new AreaCandidate(48,"Thái Bình",0));
        arraylistArea.add(new AreaCandidate(49,"Thái Nguyên",0));
        arraylistArea.add(new AreaCandidate(50,"Thanh Hóa",0));
        arraylistArea.add(new AreaCandidate(51,"Thừa Thiên Huế",0));
        arraylistArea.add(new AreaCandidate(52,"Tiền Giang",0));
        arraylistArea.add(new AreaCandidate(53,"Trà Vinh",0));
        arraylistArea.add(new AreaCandidate(54,"Tuyên Quang",0));
        arraylistArea.add(new AreaCandidate(55,"Vĩnh Long",0));
        arraylistArea.add(new AreaCandidate(56,"Vĩnh Phúc",0));
        arraylistArea.add(new AreaCandidate(57,"Yên Bái",0));
        arraylistArea.add(new AreaCandidate(58,"Phú Yên",0));
        arraylistArea.add(new AreaCandidate(59,"Cần Thơ",0));
        arraylistArea.add(new AreaCandidate(60,"Đà Nẵng",0));
        arraylistArea.add(new AreaCandidate(61,"Hải Phòng",0));
        arraylistArea.add(new AreaCandidate(62,"Hà Nội",0));
        arraylistArea.add(new AreaCandidate(63,"TP HCM",0));

    }
}
