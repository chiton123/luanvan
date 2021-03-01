package com.example.luanvan.ui.recruiter.CVManagement;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CandidateDocumentFragment extends Fragment {
    RecyclerView recyclerView;
    public static CVFilterAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_candidate_document, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);

        // 0 : all, 1: cvfilterfragment, 2: interviewfragment, 3: gotoworkfragment
        adapter = new CVFilterAdapter(getActivity(), CVManageActivity.arrayListAll, getActivity(), 0, CVManageActivity.arrayListJobList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        getData();


        return view;
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlCandidateDocument,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    CVManageActivity.arrayListAll.add(new Applicant(
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
                map.put("id_recruiter", String.valueOf(MainActivity.iduser));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
}
