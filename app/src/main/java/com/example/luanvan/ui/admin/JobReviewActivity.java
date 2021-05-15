package com.example.luanvan.ui.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.admin_a.JobReviewAdapter;
import com.example.luanvan.ui.Model.JobPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class JobReviewActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    LinearLayout layout, layout_nothing;
    JobReviewAdapter adapter;
    public static ArrayList<JobPost> jobPostArrayList;
    Handler handler;
    SearchView searchView;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_review);
        anhxa();
        actionBar();
        loading();
        getData();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sort();
                progressDialog.dismiss();
            }
        },3000);
        search();
    }

    private void search() {
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
    }
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    void sort(){
        if(jobPostArrayList.size() > 0){
            for(int j=0; j < jobPostArrayList.size(); j++){
                for(int k=j+1; k < jobPostArrayList.size(); k++){
                    if(jobPostArrayList.get(j).getStatus_post() != 1 && jobPostArrayList.get(k).getStatus_post() == 1){
                        JobPost jobPost = jobPostArrayList.get(j);
                        jobPostArrayList.set(j,jobPostArrayList.get(k));
                        jobPostArrayList.set(k, jobPost);
                    }

                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, MainActivity.urlJobPost, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response != null){
                            for(int i=0; i < response.length(); i++){
                                JSONObject object = null;
                                try {
                                    object = response.getJSONObject(i);
                                    jobPostArrayList.add(new JobPost(
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
                                            object.getInt("status_post"),
                                            object.getString("note_reject"),
                                            object.getString("company_name"),
                                            object.getString("type_job")
                                    ));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                adapter.notifyDataSetChanged();

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
                return super.getParams();
            }
        };
        requestQueue.add(jsonArrayRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 123 && resultCode == 123){
            //Toast.makeText(getApplicationContext(), "ee", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        searchView = (SearchView) findViewById(R.id.searchView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        layout = (LinearLayout) findViewById(R.id.layout);
        layout_nothing = (LinearLayout) findViewById(R.id.layout_nothing);
        jobPostArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new JobReviewAdapter(recyclerView,this, jobPostArrayList, this);
        recyclerView.setAdapter(adapter);

    }
}
