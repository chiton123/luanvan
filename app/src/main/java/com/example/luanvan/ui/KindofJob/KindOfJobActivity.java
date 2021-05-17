package com.example.luanvan.ui.KindofJob;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import com.example.luanvan.ui.Adapter.job.KindOfJobAdapter;
import com.example.luanvan.ui.Interface.ILoadMore;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.Job_Apply;
import com.example.luanvan.ui.Adapter.job_apply.*;
import com.example.luanvan.ui.recruiter.search_r.SearchCandidateActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.SearchView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KindOfJobActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<Job> arrayList;
    KindOfJobAdapter adapter;
    ArrayList<Job_Apply> job_applyArrayList;
    KindOfJobApplyAdapter jobApplyAdapter;
    Handler handler;
    int page = 1;
    int kind = 0; // // kind : 0 : thuc tap, 1: tu xa, 2: ban thoi gian, 3 toan thoi gian, 4 da ung tuyen, 5 quan tam, 6 moi nhat, 7 phu hop
    SearchView searchView;
    LinearLayout layout, layout_nothing;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kind_of_job);
        loading();
        anhxa();
        actionBar();
        if(kind == 4){
            getDataApply(page);
        }else {
            getData(page);
        }
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                checkNothing();
            }
        },3000);
        loadMore();
        search();

    }

    private void search() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(kind == 4){
                    jobApplyAdapter.getFilter().filter(query);
                }else {
                    adapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(kind == 4){
                    jobApplyAdapter.getFilter().filter(newText);
                }else {
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });

    }

    public void checkNothing(){
        if(kind == 4){
            if(job_applyArrayList.size() == 0){
                layout.setVisibility(View.INVISIBLE);
                layout_nothing.setVisibility(View.VISIBLE);
            }else {
                layout.setVisibility(View.VISIBLE);
                layout_nothing.setVisibility(View.INVISIBLE);
            }
        }else {
            if(arrayList.size() == 0){
                layout.setVisibility(View.INVISIBLE);
                layout_nothing.setVisibility(View.VISIBLE);
            }else {
                layout.setVisibility(View.VISIBLE);
                layout_nothing.setVisibility(View.INVISIBLE);
            }
        }

    }

    private void loadMore() {
        adapter.setLoadmore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(kind == 4){
                            getDataApply(++page);
                        }else {
                            getData(++page);
                        }
                        adapter.notifyDataSetChanged();
                        adapter.setIsloaded(false);

                    }
                },2000);

            }
        });
    }

    private void getData(int page) {
        int checkprofession = 0;
        int checkarea = 0;
        int checkskill = 0;
        if(MainActivity.arrayListChosenProfession.size() > 0){
            checkprofession = 1;
        }
        if(MainActivity.arraylistChosenArea.size() > 0){
            checkarea = 1;
        }
        if(MainActivity.skills.size() > 0){
            checkskill = 1;
        }
        String area = "(";
        if(MainActivity.arraylistChosenArea.size() > 0){
            for(int i=0; i < MainActivity.arraylistChosenArea.size(); i++){
                if(i == MainActivity.arraylistChosenArea.size() - 1){
                    area += MainActivity.arraylistChosenArea.get(i).getId() + ")";
                }else {
                    area += MainActivity.arraylistChosenArea.get(i).getId() + ",";
                }
            }
        }

        String skill = "(";
        for(int i=0; i < MainActivity.skills.size(); i++){
            if(i == MainActivity.skills.size() - 1){
                skill += MainActivity.skills.get(i).getIdskill() + ")";
            }else {
                skill += MainActivity.skills.get(i).getIdskill() + ",";
            }
          //  Toast.makeText(getApplicationContext(),  MainActivity.skills.get(i).getIdskill() + ",", Toast.LENGTH_SHORT).show();
        }

        String profession = "(";
        for(int i=0; i < MainActivity.arrayListChosenProfession.size(); i++){
            if(i == MainActivity.arrayListChosenProfession.size() - 1){
                profession += MainActivity.arrayListChosenProfession.get(i).getId() + ")";
            }else {
                profession += MainActivity.arrayListChosenProfession.get(i).getId() + ",";
            }
        }
    //    Toast.makeText(getApplicationContext(), "p size: " + MainActivity.arrayListChosenProfession.size(), Toast.LENGTH_SHORT).show();
    //    Toast.makeText(getApplicationContext(), "area: " + area + " skill : " + skill + " profession : " + profession, Toast.LENGTH_SHORT).show();
        String url = MainActivity.urljob1 + String.valueOf(page);
        final String finalArea = area;
        final int finalCheckarea = checkarea;
        final String finalSkill = skill;
        final int finalCheckskill = checkskill;
        final int finalCheckprofession = checkprofession;
        final String finalProfession = profession;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    //    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                int status = object.getInt("status");
                                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = fmt.parse(object.getString("end_date"));
                                int check_trunglap = 0;
                                if(arrayList.size() > 0){
                                    for(int j=0; j < arrayList.size(); j++){
                                        if(object.getInt("id") == arrayList.get(j).getId()){
                                            check_trunglap = 1;
                                        }
                                    }
                                }
                                if(check_trunglap == 0){
                                    arrayList.add(new Job(
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

                                adapter.notifyDataSetChanged();


                            }


                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
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
                map.put("kind", String.valueOf(kind));
                if(kind == 7){

                    map.put("checkarea", String.valueOf(finalCheckarea));
                    map.put("area", finalArea);

                    map.put("checkskill", String.valueOf(finalCheckskill));
                    map.put("skill", finalSkill);

                    map.put("checkprofession", String.valueOf(finalCheckprofession));
                    map.put("profession", finalProfession);


                }
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }


    private void getDataApply(int page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = MainActivity.urlJobApplyLoad + String.valueOf(page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                job_applyArrayList.add(new Job_Apply(
                                        object.getInt("id"),
                                        object.getString("name"),
                                        object.getInt("idcompany"),
                                        object.getInt("id_recruiter"),
                                        object.getString("id_cv"),
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



                                    jobApplyAdapter.notifyDataSetChanged();
                                }





                        } catch (JSONException e) {
                            e.printStackTrace();
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
                map.put("iduser", String.valueOf(MainActivity.iduser));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 0 : all - việc làm tốt nhất, 1: luong cao,2: lam tu xa, 3: thuc tap, 4: moi nhat, 5: job_apply
        switch (kind){
            case 0:
                getSupportActionBar().setTitle("Việc thực tập");
                break;
            case 1:
                getSupportActionBar().setTitle("Việc làm từ xa");
                break;
            case 2:
                getSupportActionBar().setTitle("Việc làm thêm");
                break;
            case 3:
                getSupportActionBar().setTitle("Việc làm toàn thời gian");
                break;
            case 4:
                getSupportActionBar().setTitle("Việc đã ứng tuyển");
                break;
            case 5:
                getSupportActionBar().setTitle("Việc quan tâm");
                break;
            case 6:
                getSupportActionBar().setTitle("Việc mới nhất");
                break;
            case 7:
                getSupportActionBar().setTitle("Việc phù hợp");
                break;

        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        kind = getIntent().getIntExtra("kind",0);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (SearchView) findViewById(R.id.searchView);
        arrayList = new ArrayList<>();
        job_applyArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new KindOfJobAdapter(recyclerView, this, arrayList, this,0);
        jobApplyAdapter = new KindOfJobApplyAdapter(recyclerView, this, job_applyArrayList, this, 1);
        if(kind != 4){
            recyclerView.setAdapter(adapter);
        }else {
            recyclerView.setAdapter(jobApplyAdapter); // 5: da ung tuyen
        }
        layout = (LinearLayout) findViewById(R.id.layout);
        layout_nothing = (LinearLayout) findViewById(R.id.layout_nothing);

    }
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
}
