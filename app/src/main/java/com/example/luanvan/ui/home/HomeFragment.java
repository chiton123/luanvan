package com.example.luanvan.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.JobAdapter;
import com.example.luanvan.ui.KindofJob.KindOfJobActivity;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Search_Filter.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    Toolbar toolbar;
    private HomeViewModel homeViewModel;
    RecyclerView recyclerView, recyclerViewthuctap;
    JobAdapter jobAdapter, adapterThuctap;
    ArrayList<Job> arrayList, arrayListThuctap;
    TextView txtthuctap, txtviectotnhat;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerViewthuctap = (RecyclerView) view.findViewById(R.id.recycleviewthuctap);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerViewthuctap.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewthuctap.setHasFixedSize(true);
        // toolbar menu option

        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        setHasOptionsMenu(true);

        txtthuctap = (TextView) view.findViewById(R.id.txtviecthuctap);
        txtviectotnhat = (TextView) view.findViewById(R.id.txtvieclamtotnhat);
        arrayList = new ArrayList<>();
        arrayListThuctap = new ArrayList<>();
        jobAdapter = new JobAdapter(getActivity(), arrayList, getActivity());
        adapterThuctap = new JobAdapter(getActivity(), arrayListThuctap, getActivity());
        // tất cả job
        getData(0);
        // job thực tập
        getData(3);
        recyclerView.setAdapter(jobAdapter);
        recyclerViewthuctap.setAdapter(adapterThuctap);


        eventXemtatca();


        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.chat:
                Toast.makeText(getActivity(), "chat", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 3: thuc tap
    private void eventXemtatca() {
        txtthuctap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KindOfJobActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("thuctap", 3);
                startActivity(intent);
            }
        });
        txtviectotnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KindOfJobActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("thuctap", 0);
                startActivity(intent);
            }
        });

    }




    private void getData(final int kind) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urljob1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                if(kind == 0){
                                    arrayList.add(new Job(
                                            object.getInt("id"),
                                            object.getString("name"),
                                            object.getInt("idcompany"),
                                            object.getString("img"),
                                            object.getString("area"),
                                            object.getInt("idtype"),
                                            object.getInt("idprofession"),
                                            object.getString("date"),
                                            object.getInt("salary"),
                                            object.getInt("idarea"),
                                            object.getInt("gender"),
                                            object.getString("experience"),
                                            object.getInt("number"),
                                            object.getString("position"),
                                            object.getString("description"),
                                            object.getString("requirement"),
                                            object.getString("benefit"),
                                            object.getInt("status"),
                                            object.getString("company_name"),
                                            object.getString("type_job")
                                    ));
                                    jobAdapter.notifyDataSetChanged();
                                }else if(kind == 3){
                                    arrayListThuctap.add(new Job(
                                            object.getInt("id"),
                                            object.getString("name"),
                                            object.getInt("idcompany"),
                                            object.getString("img"),
                                            object.getString("area"),
                                            object.getInt("idtype"),
                                            object.getInt("idprofession"),
                                            object.getString("date"),
                                            object.getInt("salary"),
                                            object.getInt("idarea"),
                                            object.getInt("gender"),
                                            object.getString("experience"),
                                            object.getInt("number"),
                                            object.getString("position"),
                                            object.getString("description"),
                                            object.getString("requirement"),
                                            object.getString("benefit"),
                                            object.getInt("status"),
                                            object.getString("company_name"),
                                            object.getString("type_job")
                                    ));
                                    adapterThuctap.notifyDataSetChanged();
                                }

                            }


                        } catch (JSONException e) {
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
                map.put("kind", String.valueOf(kind));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
}
