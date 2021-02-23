package com.example.luanvan.ui.fragment.recruting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class CVFilterFragment extends Fragment {
    RecyclerView recyclerView;
    CVFilterAdapter adapter;
    ArrayList<Applicant> arrayList;
    int job_id = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c_v_filter, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        arrayList = new ArrayList<>();
        adapter = new CVFilterAdapter(getActivity(), arrayList, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        job_id = getActivity().getIntent().getIntExtra("job_id", 0);
        getData();

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
                                    arrayList.add(new Applicant(
                                            object.getInt("id"),
                                            object.getInt("job_id"),
                                            object.getInt("user_id"),
                                            object.getString("user_id_f"),
                                            object.getString("username"),
                                            object.getString("email"),
                                            object.getString("address"),
                                            object.getString("phone"),
                                            object.getInt("cv_id"),
                                            object.getInt("status")
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
