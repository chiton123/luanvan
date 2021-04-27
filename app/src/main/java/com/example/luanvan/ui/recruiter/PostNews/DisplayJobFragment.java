package com.example.luanvan.ui.recruiter.PostNews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.example.luanvan.ui.Adapter.recruit.NewPostAdapter;
import com.example.luanvan.ui.Model.JobList;
import com.example.luanvan.ui.recruiter.CVManagement.CVManageActivity;
import com.example.luanvan.ui.recruiter.RecruiterActivity;

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


public class DisplayJobFragment extends Fragment {
    RecyclerView recyclerView;
    public static NewPostAdapter adapter;
    // fragment 1: Đang hiển thị, 2 : Chờ xác thực, 3: Hết hạn, 4: Từ chối , khi chuyển qua bên adjustJob thì cập nhật tương ứng với fragment
    // kind: 0 là của joblistfragment chuyển qua, 1: là của tin tuyển dụng chuyển qua
    public static LinearLayout layout, layout_nothing;
    Handler handler;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_job, container, false);
        layout = (LinearLayout) view.findViewById(R.id.layout);
        layout_nothing = (LinearLayout) view.findViewById(R.id.layout_nothing);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new NewPostAdapter(getActivity(), RecruiterActivity.arrayListJobList, getActivity(), 1, 1);
        recyclerView.setAdapter(adapter);
        if(RecruiterActivity.arrayListJobList.size() == 0){
            getData(0);
        }
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkNothing();
            }
        },2500);

        return view;
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
            layout.setVisibility(View.GONE);
            layout_nothing.setVisibility(View.VISIBLE);
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
                            //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                Date date = null;
                                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                                date = fmt.parse(object.getString("end_date"));
                                if(date.after(Calendar.getInstance().getTime())){
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
                                }else {
                                    RecruiterActivity.arrayListOutdatedJobs.add(new JobList(
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

                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
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
            Toast.makeText(getActivity(), "display ", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
            checkNothing();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
