package com.example.luanvan.ui.fragment.recruting;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.luanvan.ui.Adapter.recruit.CVFilterAdapter;
import com.example.luanvan.ui.Model.Applicant;
import com.example.luanvan.ui.recruiter.CVManagement.CVManageActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CVManagementActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CVFilterFragment extends Fragment {
    RecyclerView recyclerView;
    public static CVFilterAdapter adapter;
    int job_id = 0;
    Handler handler;
    public static LinearLayout layout, layout_nothing;
    // kind: 1: lọc CV, 2: phỏng vấn, 3: nhận việc để cập nhật sau khi đánh giá, 0: candidateDocument
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c_v_filter, container, false);
        layout = (LinearLayout) view.findViewById(R.id.layout);
        layout_nothing = (LinearLayout) view.findViewById(R.id.layout_nothing);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        adapter = new CVFilterAdapter(getActivity(), CVManageActivity.arrayListCVFilter, getActivity(), 1, CVManageActivity.arrayListJobList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        job_id = getActivity().getIntent().getIntExtra("job_id", 0);
        CVManageActivity.arrayListCVFilter.clear();
        // 1: lọc CV, 2: phỏng vấn, 3: nhận việc
        if (CVManageActivity.arrayListCVFilter.size() == 0) {
            getData();
        }
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(CVManageActivity.arrayListCVFilter.size() == 0){
                    layout_nothing.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.GONE);
                }else {
                    layout_nothing.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                }
            }
        },1000);

        return view;
    }

    private void getData() {
      //  Toast.makeText(getActivity(), "job id " + job_id, Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlFilterCV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                         //   Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int status = object.getInt("status");
                                    if (status == 0 || status == 1 || status == 2) {
                                        CVManageActivity.arrayListCVFilter.add(new Applicant(
                                                object.getInt("id"),
                                                object.getInt("job_id"),
                                                object.getString("job_name"),
                                                object.getInt("user_id"),
                                                object.getString("user_id_f"),
                                                object.getString("username"),
                                                object.getString("email"),
                                                object.getString("address"),
                                                object.getString("phone"),
                                                object.getInt("cv_id"),
                                                object.getInt("status"),
                                                object.getString("note"),
                                                object.getString("date")
                                        ));
                                    }

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
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("job_id", String.valueOf(job_id));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
}