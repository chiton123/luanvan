package com.example.luanvan.ui.fragment.job_f;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.example.luanvan.ui.Model.Company;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.company.CompanyActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CompanyFragment extends Fragment {
    TextView txttencongty, txtdiachi, txtwebsite, txtchitiet, txtgioithieu;
    int idcompany = 0;
    ArrayList<Company> arrayList;
    Job job;
    int kind = 0, job_id = 0;
    Handler handler;
    Company company;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company, container, false);
        txttencongty = (TextView) view.findViewById(R.id.tencongty);
        txtdiachi = (TextView) view.findViewById(R.id.diachi);
        txtwebsite = (TextView) view.findViewById(R.id.website);
        txtchitiet = (TextView) view.findViewById(R.id.xemchitiet);
        txtgioithieu = (TextView) view.findViewById(R.id.gioithieu);
        arrayList = new ArrayList<>();
        getCompanyInfo();
        openWebsite();

        return view;
    }

    private void switchToCompany(final Company company) {
        txtchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CompanyActivity.class);
                intent.putExtra("company", company);
                startActivity(intent);
            }
        });

    }

    // dành cho từ notification chuyển qua
    public void getJobInfo(final int job_id){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlJobFromNotification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
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
    private void openWebsite() {
        txtwebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(txtwebsite.getText().toString()));
                startActivity(intentBrowser);
            }
        });

    }

    public String xuongdong(String text){
        String ketqua = "";
        if(text.contains("-")){
            String[] split = text.split("-");
            for(String item : split){
                ketqua += "-" + item + "\n";
            }
            return ketqua;
        }else {
            return text;
        }
    }
    public void getCompanyInfomation(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlcompany,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       //    Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        if(response != null){
                        //    Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject object = jsonArray.getJSONObject(0);
                                arrayList.add(new Company(
                                        object.getInt("id"),
                                        object.getString("name"),
                                        object.getString("introduction"),
                                        object.getString("address"),
                                        object.getInt("idarea"),
                                        object.getInt("idowner"),
                                        object.getString("image"),
                                        object.getString("image_background"),
                                        object.getString("website"),
                                        object.getInt("number_job"),
                                        object.getInt("status"),
                                        object.getDouble("vido"),
                                        object.getDouble("kinhdo")
                                ));

                                Company company = arrayList.get(0);
                                txtdiachi.setText(company.getAddress());
                                txttencongty.setText(company.getName());
                                txtwebsite.setText(company.getWebsite());
                                String gioithieu = xuongdong(company.getIntroduction());
                                txtgioithieu.setText(gioithieu);
                                switchToCompany(company);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("idcompany", String.valueOf(idcompany));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getCompanyInfo() {
        Intent intent = (Intent) getActivity().getIntent();
        kind = intent.getIntExtra("kind",0);
        if(kind == 0){
            Job job = (Job) intent.getSerializableExtra("job");
            idcompany = job.getIdcompany();
            getCompanyInfomation();

        }else {
            job_id = intent.getIntExtra("job_id",0);
            getJobInfo(job_id);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    idcompany = job.getIdcompany();
                    getCompanyInfomation();
                }
            },4000);
        }



    }


}
