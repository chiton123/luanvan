package com.example.luanvan.ui.fragment.recruting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class GoToWorkFragment extends Fragment {
    RecyclerView recyclerView;
    public static CVFilterAdapter adapter;
    int job_id = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_go_to_work, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        CVManageActivity.arrayListGoToWork = new ArrayList<>();
        adapter = new CVFilterAdapter(getActivity(), CVManageActivity.arrayListGoToWork, getActivity(),3, CVManageActivity.arrayListJobList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        job_id = getActivity().getIntent().getIntExtra("job_id", 0);
        // 1: lọc CV, 2: phỏng vấn, 3: nhận việc
        if(CVManageActivity.arrayListGoToWork.size() == 0){
            getData();
        }


        return view;
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlFilterCV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int status = object.getInt("status");
                                    if(status >= 12){
                                        CVManageActivity.arrayListGoToWork.add(new Applicant(
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
}