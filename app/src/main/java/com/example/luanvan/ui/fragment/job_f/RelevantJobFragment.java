package com.example.luanvan.ui.fragment.job_f;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.luanvan.ui.Adapter.job.CompanyJobAdapter;
import com.example.luanvan.ui.DetailedJob.DetailJobActivity;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.company.CompanyActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RelevantJobFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Job> arrayList;
    CompanyJobAdapter adapter;
    Job job;
    LinearLayout layout, layout_nothing;
    ProgressDialog progressDialog;
    Handler handler;
    int kind;
    int job_id = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relevant_job, container, false);
        loading();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        arrayList = new ArrayList<>();
        adapter = new CompanyJobAdapter(getActivity(), arrayList, getActivity());
        recyclerView.setAdapter(adapter);
        layout = (LinearLayout) view.findViewById(R.id.layout);
        layout_nothing = (LinearLayout) view.findViewById(R.id.layout_nothing);
        kind = getActivity().getIntent().getIntExtra("kind",0);
        if(kind == 0){
            job = (Job) getActivity().getIntent().getSerializableExtra("job");
            getData();
            progressDialog.dismiss();
        }else {
            job_id = getActivity().getIntent().getIntExtra("job_id",0);
            getJobInfo(job_id);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   getData();

                }
            },3000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    checkNothing();
                }
            },4000);
        }



        return view;
    }
    // dành cho từ notification chuyển qua
    public void getJobInfo(final int job_id){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlJobFromNotification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject object = jsonArray.getJSONObject(0);
                                job = new Job(
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
                                );


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


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
                map.put("job_id", String.valueOf(job_id));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
    public void checkNothing(){
        if(arrayList.size() == 0){
            layout_nothing.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }else {
            layout_nothing.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }

    }
    void loading(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlJobRelate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       //  Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                map.put("idprofession", String.valueOf(job.getIdprofession()));
                map.put("jobid", String.valueOf(job.getId()));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
}
