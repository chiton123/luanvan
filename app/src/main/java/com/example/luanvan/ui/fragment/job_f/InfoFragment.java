package com.example.luanvan.ui.fragment.job_f;

import android.content.Intent;
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
import com.example.luanvan.ui.Model.Job;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class InfoFragment extends Fragment {
    TextView txtsalary, txtarea, txthinhthuc, txtnumber, txtexperience, txtdescription, txtbenefit, txtrequirement;
    int kind = 0;
    Job job;
    int job_id = 0;
    Handler handler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        txtarea = (TextView) view.findViewById(R.id.area);
        txtsalary = (TextView) view.findViewById(R.id.salary);
        txthinhthuc = (TextView) view.findViewById(R.id.hinhthuc);
        txtnumber = (TextView) view.findViewById(R.id.number);
        txtexperience = (TextView) view.findViewById(R.id.experience);
        txtdescription = (TextView) view.findViewById(R.id.motacongviec);
        txtbenefit = (TextView) view.findViewById(R.id.quyenloi);
        txtrequirement = (TextView) view.findViewById(R.id.yeucaucongviec);
        setContent();

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
    private void setContent() {
        kind = getActivity().getIntent().getIntExtra("kind",0);
        if(kind == 0){
            Intent intent = (Intent) getActivity().getIntent();
            Job job = (Job) intent.getSerializableExtra("job");
            txtarea.setText(job.getAddress());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            txtsalary.setText("Từ " + decimalFormat.format( + job.getSalary_min()) + "đ đến " + decimalFormat.format(job.getSalary_max()) + "đ" );
            txthinhthuc.setText(job.getType_job());
            txtnumber.setText(job.getNumber()+"");
            txtexperience.setText(job.getExperience());

            String mota = xuongdong(job.getDescription());
            String yeucau = xuongdong(job.getRequirement());
            String quyenloi = xuongdong(job.getBenefit());
            txtdescription.setText(mota);
            txtrequirement.setText(yeucau);
            txtbenefit.setText(quyenloi);
        }else {
            job_id = getActivity().getIntent().getIntExtra("job_id",0);
            getJobInfo(job_id);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    txtarea.setText(job.getAddress());
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    txtsalary.setText("Từ " + decimalFormat.format( + job.getSalary_min()) + "đ đến " + decimalFormat.format(job.getSalary_max()) + "đ" );
                    txthinhthuc.setText(job.getType_job());
                    txtnumber.setText(job.getNumber()+"");
                    txtexperience.setText(job.getExperience());

                    String mota = xuongdong(job.getDescription());
                    String yeucau = xuongdong(job.getRequirement());
                    String quyenloi = xuongdong(job.getBenefit());
                    txtdescription.setText(mota);
                    txtrequirement.setText(yeucau);
                    txtbenefit.setText(quyenloi);

                }
            },4000);

        }




    }
    public String xuongdong(String text){
        String ketqua = "";
        if(text.contains(".")){
            String[] split = text.split(Pattern.quote("."));
            for(String item : split){
                ketqua += "-" +  item + "\n";
            }
            return ketqua;
        }else {
            return text;
        }
    }

}
