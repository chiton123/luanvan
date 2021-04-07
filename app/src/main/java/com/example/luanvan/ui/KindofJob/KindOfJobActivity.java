package com.example.luanvan.ui.KindofJob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kind_of_job);
        anhxa();
        actionBar();
        if(kind == 4){
            getDataApply(page);
        }else {
            getData(page);
        }
        loadMore();

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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = MainActivity.urljob1 + String.valueOf(page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                int status = object.getInt("status");
                                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = fmt.parse(object.getString("end_date"));
                                arrayList.add(new Job(
                                        object.getInt("id"),
                                        object.getString("name"),
                                        object.getInt("idcompany"),
                                        object.getInt("id_recruiter"),
                                        object.getString("img"),
                                        object.getString("area"),
                                        object.getInt("idtype"),
                                        object.getInt("idprofession"),
                                        object.getString("start_date"),
                                        object.getString("end_date"),
                                        object.getInt("salary_min"),
                                        object.getInt("salary_max"),
                                        object.getInt("idarea"),
                                        object.getString("experience"),
                                        object.getInt("number"),
                                        object.getString("description"),
                                        object.getString("requirement"),
                                        object.getString("benefit"),
                                        object.getInt("status"),
                                        object.getString("company_name"),
                                        object.getString("type_job")
                                ));
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
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("kind", String.valueOf(kind));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_kindjob, menu);
        MenuItem searchItem = menu.findItem(R.id.searchhienthithongtin);
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);

        searchView = null;
        if (searchItem != null) {
            searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        }

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
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

        return super.onCreateOptionsMenu(menu);
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
                                        object.getString("area"),
                                        object.getInt("idtype"),
                                        object.getInt("idprofession"),
                                        object.getString("start_date"),
                                        object.getString("end_date"),
                                        object.getInt("salary_min"),
                                        object.getInt("salary_max"),
                                        object.getInt("idarea"),
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
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
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


    }
}
