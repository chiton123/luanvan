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
    // getdata 0 : all, 1: luong cao,2: lam tu xa, 3: thuc tap, 4: moi nhat
    Toolbar toolbar;
    private HomeViewModel homeViewModel;
    RecyclerView recyclerView, recyclerViewthuctap, recyclerViewLuongCao, recyclerViewViecLamTuXa,recyclerViewViecLamMoiNhat;
    JobAdapter jobAdapter, adapterThuctap, adapterLuongCao, adapterViecLamTuXa, adapterViecLamMoiNhat;
    ArrayList<Job> arrayList, arrayListThuctap, arrayListLuongCao, arrayListViecLamTuXa, arrayListViecLamMoiNhat;
    TextView txtthuctap, txtviectotnhat, txtLuongCao, txtViecLamTuXa, txtViecLamMoiNhat;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerViewthuctap = (RecyclerView) view.findViewById(R.id.recycleviewthuctap);
        recyclerViewLuongCao = (RecyclerView) view.findViewById(R.id.recycleviewluongcao);
        recyclerViewViecLamTuXa = (RecyclerView) view.findViewById(R.id.recycleviewlamtuxa);
        recyclerViewViecLamMoiNhat = (RecyclerView) view.findViewById(R.id.recycleviewlammoinhat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewthuctap.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewLuongCao.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewViecLamTuXa.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewViecLamMoiNhat.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerViewthuctap.setHasFixedSize(true);
        recyclerViewLuongCao.setHasFixedSize(true);
        recyclerViewViecLamTuXa.setHasFixedSize(true);
        recyclerViewViecLamMoiNhat.setHasFixedSize(true);
        // toolbar menu option

        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        setHasOptionsMenu(true);

        txtthuctap = (TextView) view.findViewById(R.id.txtviecthuctap);
        txtviectotnhat = (TextView) view.findViewById(R.id.txtvieclamtotnhat);
        txtLuongCao = (TextView) view.findViewById(R.id.txtviecluongcao);
        txtViecLamTuXa = (TextView) view.findViewById(R.id.txtvieclamtuxa);
        txtViecLamMoiNhat = (TextView) view.findViewById(R.id.txtvieclammoinhat);
        arrayList = new ArrayList<>();
        arrayListThuctap = new ArrayList<>();
        arrayListLuongCao = new ArrayList<>();
        arrayListViecLamTuXa = new ArrayList<>();
        arrayListViecLamMoiNhat = new ArrayList<>();
        jobAdapter = new JobAdapter(getActivity(), arrayList, getActivity());
        adapterThuctap = new JobAdapter(getActivity(), arrayListThuctap, getActivity());
        adapterLuongCao = new JobAdapter(getActivity(), arrayListLuongCao, getActivity());
        adapterViecLamTuXa = new JobAdapter(getActivity(), arrayListViecLamTuXa, getActivity());
        adapterViecLamMoiNhat = new JobAdapter(getActivity(), arrayListViecLamMoiNhat, getActivity());
        // tất cả job
        getData(0);
        // job thực tập
        getData(3);
        getData(1);
        getData(2);
        getData(4);
        recyclerView.setAdapter(jobAdapter);
        recyclerViewthuctap.setAdapter(adapterThuctap);
        recyclerViewLuongCao.setAdapter(adapterLuongCao);
        recyclerViewViecLamTuXa.setAdapter(adapterViecLamTuXa);
        recyclerViewViecLamMoiNhat.setAdapter(adapterViecLamMoiNhat);

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
        // 0 : all, 1: luong cao,2: lam tu xa, 3: thuc tap, 4: moi nhat
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
        txtLuongCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KindOfJobActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("thuctap", 1);
                startActivity(intent);
            }
        });
        txtViecLamMoiNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KindOfJobActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("thuctap", 4);
                startActivity(intent);
            }
        });
        txtViecLamTuXa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KindOfJobActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("thuctap", 2);
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
                                            object.getString("start_date"),
                                            object.getString("end_date"),
                                            object.getInt("salary"),
                                            object.getInt("idarea"),
                                            object.getString("experience"),
                                            object.getInt("number"),
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
                                            object.getString("start_date"),
                                            object.getString("end_date"),
                                            object.getInt("salary"),
                                            object.getInt("idarea"),
                                            object.getString("experience"),
                                            object.getInt("number"),
                                            object.getString("description"),
                                            object.getString("requirement"),
                                            object.getString("benefit"),
                                            object.getInt("status"),
                                            object.getString("company_name"),
                                            object.getString("type_job")
                                    ));
                                    adapterThuctap.notifyDataSetChanged();
                                }else if(kind == 1){
                                    arrayListLuongCao.add(new Job(
                                            object.getInt("id"),
                                            object.getString("name"),
                                            object.getInt("idcompany"),
                                            object.getString("img"),
                                            object.getString("area"),
                                            object.getInt("idtype"),
                                            object.getInt("idprofession"),
                                            object.getString("start_date"),
                                            object.getString("end_date"),
                                            object.getInt("salary"),
                                            object.getInt("idarea"),
                                            object.getString("experience"),
                                            object.getInt("number"),
                                            object.getString("description"),
                                            object.getString("requirement"),
                                            object.getString("benefit"),
                                            object.getInt("status"),
                                            object.getString("company_name"),
                                            object.getString("type_job")
                                    ));
                                    adapterLuongCao.notifyDataSetChanged();
                                } else if(kind == 2){
                                    arrayListViecLamTuXa.add(new Job(
                                            object.getInt("id"),
                                            object.getString("name"),
                                            object.getInt("idcompany"),
                                            object.getString("img"),
                                            object.getString("area"),
                                            object.getInt("idtype"),
                                            object.getInt("idprofession"),
                                            object.getString("start_date"),
                                            object.getString("end_date"),
                                            object.getInt("salary"),
                                            object.getInt("idarea"),
                                            object.getString("experience"),
                                            object.getInt("number"),
                                            object.getString("description"),
                                            object.getString("requirement"),
                                            object.getString("benefit"),
                                            object.getInt("status"),
                                            object.getString("company_name"),
                                            object.getString("type_job")
                                    ));
                                    adapterViecLamTuXa.notifyDataSetChanged();
                                }else if(kind == 4){
                                    arrayListViecLamMoiNhat.add(new Job(
                                            object.getInt("id"),
                                            object.getString("name"),
                                            object.getInt("idcompany"),
                                            object.getString("img"),
                                            object.getString("area"),
                                            object.getInt("idtype"),
                                            object.getInt("idprofession"),
                                            object.getString("start_date"),
                                            object.getString("end_date"),
                                            object.getInt("salary"),
                                            object.getInt("idarea"),
                                            object.getString("experience"),
                                            object.getInt("number"),
                                            object.getString("description"),
                                            object.getString("requirement"),
                                            object.getString("benefit"),
                                            object.getInt("status"),
                                            object.getString("company_name"),
                                            object.getString("type_job")
                                    ));
                                    adapterViecLamMoiNhat.notifyDataSetChanged();
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
