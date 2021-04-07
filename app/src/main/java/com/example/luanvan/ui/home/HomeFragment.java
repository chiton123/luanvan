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
import com.example.luanvan.ui.Model.Chat;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.Job_Apply;
import com.example.luanvan.ui.Model.Notification;
import com.example.luanvan.ui.Search_Filter.SearchActivity;
import com.example.luanvan.ui.chat.UserListActivity;
import com.example.luanvan.ui.login.LoginActivity;
import com.example.luanvan.ui.notification.CandidateNotificationActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    RecyclerView recyclerViewViecLamMoiNhat;
    public static JobAdapter adapterViecLamMoiNhat;
    public static ArrayList<Job> arrayListViecLamMoiNhat;
    public static ArrayList<Job_Apply> arrayListDaUngTuyen;
    public static TextView txtNotification, txtUnreadMessageNumber;
    CircleImageView img;
    public static LinearLayout layout_vieclammoinhat;
    Handler handler;
//    public static int check_notification = 0; // kiểm tra đã load số thông báo hay chưa
    ProgressDialog progressDialog;
    DatabaseReference reference;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        recyclerViewViecLamMoiNhat = (RecyclerView) view.findViewById(R.id.recycleviewlammoinhat);
        recyclerViewViecLamMoiNhat.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewViecLamMoiNhat.setHasFixedSize(true);
        layout_vieclammoinhat = (LinearLayout) view.findViewById(R.id.layout_vieclammoinhat);
        img = (CircleImageView) view.findViewById(R.id.img);

        // toolbar menu option

        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        setHasOptionsMenu(true);

        arrayListViecLamMoiNhat = new ArrayList<>();
        arrayListDaUngTuyen = new ArrayList<>();
        adapterViecLamMoiNhat = new JobAdapter(getActivity(), arrayListViecLamMoiNhat, getActivity(),0);
        // getdata 0 : all, 1: luong cao,2: lam tu xa, 3: thuc tap, 4: moi nhat
        getData(4);

        recyclerViewViecLamMoiNhat.setAdapter(adapterViecLamMoiNhat);
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
        if(arrayListViecLamMoiNhat.size() == 0){
            layout_vieclammoinhat.setVisibility(View.GONE);
        }else {
            layout_vieclammoinhat.setVisibility(View.VISIBLE);
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
        setNotificationAndChat();
        toolbar.setTitle("");
        MainActivity.mUserData.child(MainActivity.uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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

        final MenuItem menuItem1 = menu.findItem(R.id.chat);
        View actionViewChat = menuItem1.getActionView();

        txtUnreadMessageNumber =  actionViewChat.findViewById(R.id.cart_badge_chat);
        txtUnreadMessageNumber.setVisibility(View.GONE);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        actionViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem1);
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setNotificationAndChat(){
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

        getDataChat();


    }

    private void getDataChat() {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MainActivity.k_chat = 0;
                for(DataSnapshot x : snapshot.getChildren()){
                    Chat chat = x.getValue(Chat.class);
                    if(chat.getReceiver().equals(MainActivity.uid) &&  !chat.isIsseen()){
                        MainActivity.k_chat++;
                    }
                }
                if(MainActivity.k_chat > 0){
                    txtUnreadMessageNumber.setVisibility(View.VISIBLE);
                    txtUnreadMessageNumber.setText(MainActivity.k_chat + "");
                }else {
                    txtUnreadMessageNumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        txtViecLamMoiNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KindOfJobActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("thuctap", 4);
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
