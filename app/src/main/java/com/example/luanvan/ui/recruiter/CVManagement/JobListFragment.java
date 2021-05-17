package com.example.luanvan.ui.recruiter.CVManagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.luanvan.ui.Adapter.recruit.PositionAdapter;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.JobList;
import com.example.luanvan.ui.recruiter.RecruiterActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JobListFragment extends Fragment {
    // status của applicant : status
    // lọc CV: 0 chưa đánh giá, 1: đạt yêu cầu, 2: không đạt yêu cầu
    // phỏng vấn : 3: Chưa liên hệ, 4: Không liên hệ được , 5: Đồng ý phỏng vấn, 6:Từ chối phỏng vấn, 7 Đến phỏng vấn, 8 Không đến phỏng vấn,
    // 9: Lùi lịch phỏng vấn, 10 Đạt phỏng vấn, 11: Không đạt phỏng vấn
    // nhận việc: 12: Đã thông báo kết quả, 13: Đã đến nhận việc, 14: Từ chối nhận việc
    public static PositionAdapter adapter;

    RecyclerView recyclerView;
    LinearLayout layout, layout_nothing;
    Handler handler;
    ProgressDialog progressDialog;
    SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_list, container, false);
        loading();
        adapter = new PositionAdapter(getActivity(), RecruiterActivity.arrayListJobList, getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        layout = (LinearLayout) view.findViewById(R.id.layout);
        layout_nothing = (LinearLayout) view.findViewById(R.id.layout_nothing);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        searchView = (SearchView) view.findViewById(R.id.searchView);
        if(RecruiterActivity.arrayListJobList.size() == 0){
            getData(0);
        }
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkNothing();
                progressDialog.dismiss();
            }
        },4000);
        search();
        return view;

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
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    public void checkNothing(){
        if(RecruiterActivity.arrayListJobList.size() == 0){
            layout_nothing.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }else {
            layout_nothing.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
    }

    private void getData(final int status_post) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlJobList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                           // Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                RecruiterActivity.arrayListJobList.add(new JobList(
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
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       FancyToast.makeText(getActivity(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("idrecuiter", String.valueOf(MainActivity.iduser));
                map.put("status_post", String.valueOf(status_post));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 123 && resultCode == 123){
          //  Toast.makeText(getActivity(), "haha", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
