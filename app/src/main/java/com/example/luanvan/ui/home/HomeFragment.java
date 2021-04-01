package com.example.luanvan.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.job.JobAdapter;
import com.example.luanvan.ui.Adapter.job_apply.JobApplyAdapter;
import com.example.luanvan.ui.KindofJob.KindOfJobActivity;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.Job_Apply;
import com.example.luanvan.ui.Model.Notification;
import com.example.luanvan.ui.Search_Filter.SearchActivity;
import com.example.luanvan.ui.chat.UserListActivity;
import com.example.luanvan.ui.login.LoginActivity;
import com.example.luanvan.ui.notification.CandidateNotificationActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    // getdata 0 : all, 1: luong cao,2: lam tu xa, 3: thuc tap, 4: moi nhat
    Toolbar toolbar;
    private HomeViewModel homeViewModel;
    RecyclerView recyclerView, recyclerViewthuctap, recyclerViewLuongCao, recyclerViewViecLamTuXa,recyclerViewViecLamMoiNhat, recyclerViewDaUngTuyen;
    public static JobAdapter jobAdapter, adapterThuctap, adapterLuongCao, adapterViecLamTuXa, adapterViecLamMoiNhat;
    public static JobApplyAdapter adapterDaUngTuyen;
    public static ArrayList<Job> arrayList, arrayListThuctap, arrayListLuongCao, arrayListViecLamTuXa, arrayListViecLamMoiNhat;
    public static ArrayList<Job_Apply> arrayListDaUngTuyen;
    TextView txtthuctap, txtviectotnhat, txtLuongCao, txtViecLamTuXa, txtViecLamMoiNhat, txtDaUngTuyen, txtUserName;
    public static TextView txtNotification;
    CircleImageView img;
    public static LinearLayout layout_daungtuyen, layout_vieclamtotnhat, layout_viecthuctap, layout_viecluongcao, layout_vieclamtuxa, layout_vieclammoinhat,
    layout_user;
    Handler handler;
//    public static int check_notification = 0; // kiểm tra đã load số thông báo hay chưa
    ProgressDialog progressDialog;
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
        recyclerViewDaUngTuyen = (RecyclerView) view.findViewById(R.id.recycleviewdaungtuyen);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewthuctap.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewLuongCao.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewViecLamTuXa.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewViecLamMoiNhat.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewDaUngTuyen.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerViewthuctap.setHasFixedSize(true);
        recyclerViewLuongCao.setHasFixedSize(true);
        recyclerViewViecLamTuXa.setHasFixedSize(true);
        recyclerViewViecLamMoiNhat.setHasFixedSize(true);
        recyclerViewDaUngTuyen.setHasFixedSize(true);
        layout_daungtuyen = (LinearLayout) view.findViewById(R.id.layout_daungtuyen);
        layout_vieclammoinhat = (LinearLayout) view.findViewById(R.id.layout_vieclammoinhat);
        layout_vieclamtotnhat = (LinearLayout) view.findViewById(R.id.layout_vieclamtotnhat);
        layout_vieclamtuxa = (LinearLayout) view.findViewById(R.id.layout_vieclamtuxa);
        layout_viecluongcao = (LinearLayout) view.findViewById(R.id.layout_viecluongcao);
        layout_viecthuctap = (LinearLayout) view.findViewById(R.id.layout_viecthuctap);
        layout_user = (LinearLayout) view.findViewById(R.id.layout_user);
        img = (CircleImageView) view.findViewById(R.id.img);

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
        txtDaUngTuyen = (TextView) view.findViewById(R.id.txtviecdaungtuyen);
        txtUserName = (TextView) view.findViewById(R.id.txtusername);
        arrayList = new ArrayList<>();
        arrayListThuctap = new ArrayList<>();
        arrayListLuongCao = new ArrayList<>();
        arrayListViecLamTuXa = new ArrayList<>();
        arrayListViecLamMoiNhat = new ArrayList<>();
        arrayListDaUngTuyen = new ArrayList<>();
        jobAdapter = new JobAdapter(getActivity(), arrayList, getActivity(),0);
        adapterThuctap = new JobAdapter(getActivity(), arrayListThuctap, getActivity(),0);
        adapterLuongCao = new JobAdapter(getActivity(), arrayListLuongCao, getActivity(),0);
        adapterViecLamTuXa = new JobAdapter(getActivity(), arrayListViecLamTuXa, getActivity(),0);
        adapterViecLamMoiNhat = new JobAdapter(getActivity(), arrayListViecLamMoiNhat, getActivity(),0);
        adapterDaUngTuyen = new JobApplyAdapter(getActivity(), arrayListDaUngTuyen, getActivity(), 1);
        // tất cả job
        getData(0);
        // getdata 0 : all, 1: luong cao,2: lam tu xa, 3: thuc tap, 4: moi nhat
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
        recyclerViewDaUngTuyen.setAdapter(adapterDaUngTuyen);
        eventXemtatca();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkNothing();
            }
        },6000);

        if(MainActivity.login == 1){
            activateAfterLogin();
        }


        return view;
    }
    public void checkNothing(){
        if(arrayList.size() == 0){
            layout_vieclamtotnhat.setVisibility(View.GONE);
        }else {
            layout_vieclamtotnhat.setVisibility(View.VISIBLE);
        }
        if(arrayListViecLamMoiNhat.size() == 0){
            layout_vieclammoinhat.setVisibility(View.GONE);
        }else {
            layout_vieclammoinhat.setVisibility(View.VISIBLE);
        }
        if(arrayListViecLamTuXa.size() == 0){
            layout_vieclamtuxa.setVisibility(View.GONE);
        }else {
            layout_vieclamtuxa.setVisibility(View.VISIBLE);
        }
        if(arrayListLuongCao.size() == 0){
            layout_viecluongcao.setVisibility(View.GONE);
        }else {
            layout_viecluongcao.setVisibility(View.VISIBLE);
        }
        if(arrayListThuctap.size() == 0){
            layout_viecthuctap.setVisibility(View.GONE);
        }else {
            layout_viecthuctap.setVisibility(View.VISIBLE);
        }

    }
    void loading(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    public void activateAfterLogin(){
        setNotification();
        getDataApplied();
        layout_daungtuyen.setVisibility(View.VISIBLE);
        toolbar.setTitle("");
        layout_user.setVisibility(View.VISIBLE);
        txtUserName.setText(MainActivity.user.getName());
        MainActivity.mUserData.child(MainActivity.uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             //   Toast.makeText(getActivity(), snapshot.toString(), Toast.LENGTH_SHORT).show();
//                UserF userF = snapshot.getValue(UserF.class);
                final String imgURL = snapshot.child("imageURL").getValue(String.class);
                if(imgURL.equals("default")){
                    img.setImageResource(R.drawable.user1);
                }else {
                    if(imgURL != null){
                        try {

                            Glide.with(getActivity()).load(imgURL).into(img);

                        }catch (NullPointerException e){
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getDataNotification() {
        MainActivity.arrayListNotification.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlGetNotification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    MainActivity.arrayListNotification.add(new Notification(
                                            object.getInt("id"),
                                            object.getInt("ap_id"),
                                            object.getInt("job_id"),
                                            object.getString("type_notification"),
                                            object.getInt("type_user"),
                                            object.getInt("id_user"),
                                            object.getString("content"),
                                            object.getInt("status"),
                                            object.getString("img"),
                                            object.getString("date_read"),
                                            object.getInt("ap_status"),
                                            object.getString("ap_note")
                                    ));

                                }

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
                map.put("iduser", String.valueOf(MainActivity.iduser));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        final MenuItem menuItem = menu.findItem(R.id.notification);
        View actionView = menuItem.getActionView();
        txtNotification = actionView.findViewById(R.id.cart_badge);
        txtNotification.setVisibility(View.GONE);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setNotification(){
        MainActivity.k = 0;
        getDataNotification();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(MainActivity.login == 1){
                    if(MainActivity.arrayListNotification.size() > 0){
                        for(int i=0; i < MainActivity.arrayListNotification.size(); i++){
                            if(MainActivity.arrayListNotification.get(i).getStatus() == 0){
                                // Toast.makeText(getActivity(), MainActivity.arrayListNotification.get(i).getStatus() + "", Toast.LENGTH_SHORT).show();
                                MainActivity.k++;
                            }
                        }
                    }
                    if(MainActivity.k == 0){
                        txtNotification.setVisibility(View.GONE);
                    }else {
                        txtNotification.setText("" + MainActivity.k);
                        txtNotification.setVisibility(View.VISIBLE);
                    }
                }else {
                    txtNotification.setVisibility(View.GONE);
                }
            }
        },3000);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.chat:
                if(MainActivity.login == 0){
                    Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent2, 123);
                }else {
                    Intent intent1 = new Intent(getActivity(), UserListActivity.class);
                    startActivity(intent1);
                    break;
                }
                break;
            case R.id.notification:
                if(MainActivity.login == 0){
                    Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent2, 123);
                }else {
                    Intent intent1 = new Intent(getActivity(), CandidateNotificationActivity.class);
                    startActivity(intent1);
                    break;
                }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 123 && resultCode == 123){
            activateAfterLogin();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 3: thuc tap
    private void eventXemtatca() {
        // 0 : all, 1: luong cao,2: lam tu xa, 3: thuc tap, 4: moi nhat, 5: job_apply
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
        txtDaUngTuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KindOfJobActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("thuctap", 5);
                startActivity(intent);
            }
        });

    }



    private void getData(final int kind) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlJobHome,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                // kiểm tra xem có hết hạn nộp hay không
                                int status = object.getInt("status");
                                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = null;

                                try {
                                    date = fmt.parse(object.getString("end_date"));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if(kind == 0){
                                    arrayList.add(new Job(
                                            object.getInt("id"),
                                            object.getString("name"),
                                            object.getInt("idcompany"),
                                            object.getInt("id_recruiter"),
                                            object.getString("img"),
                                            object.getString("area"),
                                            object.getInt("idtype"),
                                            object.getInt("idprofession"),
                                            object.getString("start_date"),
                                            object.getString("end_date"),
                                            object.getInt("salary_min"),
                                            object.getInt("salary_max"),
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
                                            object.getInt("id_recruiter"),
                                            object.getString("img"),
                                            object.getString("area"),
                                            object.getInt("idtype"),
                                            object.getInt("idprofession"),
                                            object.getString("start_date"),
                                            object.getString("end_date"),
                                            object.getInt("salary_min"),
                                            object.getInt("salary_max"),
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
                                            object.getInt("id_recruiter"),
                                            object.getString("img"),
                                            object.getString("area"),
                                            object.getInt("idtype"),
                                            object.getInt("idprofession"),
                                            object.getString("start_date"),
                                            object.getString("end_date"),
                                            object.getInt("salary_min"),
                                            object.getInt("salary_max"),
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
                                            object.getInt("id_recruiter"),
                                            object.getString("img"),
                                            object.getString("area"),
                                            object.getInt("idtype"),
                                            object.getInt("idprofession"),
                                            object.getString("start_date"),
                                            object.getString("end_date"),
                                            object.getInt("salary_min"),
                                            object.getInt("salary_max"),
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
                                            object.getInt("id_recruiter"),
                                            object.getString("img"),
                                            object.getString("area"),
                                            object.getInt("idtype"),
                                            object.getInt("idprofession"),
                                            object.getString("start_date"),
                                            object.getString("end_date"),
                                            object.getInt("salary_min"),
                                            object.getInt("salary_max"),
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
    private void getDataApplied() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlJobApply,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                arrayListDaUngTuyen.add(new Job_Apply(
                                        object.getInt("id"),
                                        object.getString("name"),
                                        object.getInt("idcompany"),
                                        object.getInt("id_recruiter"),
                                        object.getString("id_cv"),
                                        object.getString("img"),
                                        object.getString("area"),
                                        object.getInt("idtype"),
                                        object.getInt("idprofession"),
                                        object.getString("start_date"),
                                        object.getString("end_date"),
                                        object.getInt("salary_min"),
                                        object.getInt("salary_max"),
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

                            }
                            adapterDaUngTuyen.notifyDataSetChanged();


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
                map.put("iduser", String.valueOf(MainActivity.iduser));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
}
