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
import android.widget.AdapterView;
import android.widget.GridView;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.admin_a.AdminAdapter;
import com.example.luanvan.ui.Adapter.job.KindOfJobAdapter;
import com.example.luanvan.ui.Adapter.recruit.CompanyTopAdapter;
import com.example.luanvan.ui.Interface.ILoadMore;
import com.example.luanvan.ui.KindofJob.KindOfJobActivity;
import com.example.luanvan.ui.Model.Admin;
import com.example.luanvan.ui.Model.Chat;
import com.example.luanvan.ui.Model.Company;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.Notification;
import com.example.luanvan.ui.Search_Filter.SearchActivity;
import com.example.luanvan.ui.chat.UserListActivity;
import com.example.luanvan.ui.company.SearchCompanyActivity;
import com.example.luanvan.ui.login.LoginActivity;
import com.example.luanvan.ui.notification.CandidateNotificationActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

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
    public static KindOfJobAdapter adapterViecLamMoiNhat;
    public static ArrayList<Job> arrayListViecLamMoiNhat;
    public static TextView txtNotification, txtUnreadMessageNumber, txtUsername;
    CircleImageView img;
    public static LinearLayout layout_vieclammoinhat;
    GridView gridViewJob, gridViewCompany;
    ArrayList<Admin> arrayListJob;
    ArrayList<Company> arrayListCompany;
    AdminAdapter adapterJob;
    CompanyTopAdapter adapterCompany;
    int page = 1;
    Handler handler;
//    public static int check_notification = 0; // kiểm tra đã load số thông báo hay chưa
    ProgressDialog progressDialog;
    DatabaseReference reference;
    LinearLayout layout_user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        recyclerViewViecLamMoiNhat = (RecyclerView) view.findViewById(R.id.recycleviewlammoinhat);
        recyclerViewViecLamMoiNhat.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewViecLamMoiNhat.setHasFixedSize(true);
        layout_vieclammoinhat = (LinearLayout) view.findViewById(R.id.layout_vieclammoinhat);
        img = (CircleImageView) view.findViewById(R.id.img);
        txtUsername = (TextView) view.findViewById(R.id.txtusername);
        gridViewJob = (GridView) view.findViewById(R.id.gridviewvieclam);
        gridViewCompany = (GridView) view.findViewById(R.id.gridviewnhatuyendunghangdau);
        layout_user = (LinearLayout) view.findViewById(R.id.layout_user);
        arrayListJob = new ArrayList<>();
        arrayListCompany = new ArrayList<>();
        adapterJob = new AdminAdapter(getActivity(), arrayListJob);
        adapterCompany = new CompanyTopAdapter(getActivity(), arrayListCompany, getActivity());  // 1:company search , 2: home
      //  adapterRecruiter = new AdminAdapter(getActivity(), arrayListRecruiter);
        gridViewJob.setAdapter(adapterJob);
        gridViewCompany.setAdapter(adapterCompany);
      //  gridViewRecruiter.setAdapter(adapterRecruiter);
        getBasicInfo();
        // toolbar menu option

        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        setHasOptionsMenu(true);

        arrayListViecLamMoiNhat = new ArrayList<>();
        adapterViecLamMoiNhat = new KindOfJobAdapter(recyclerViewViecLamMoiNhat, getActivity(), arrayListViecLamMoiNhat, getActivity(),0);
        recyclerViewViecLamMoiNhat.setAdapter(adapterViecLamMoiNhat);
        // getdata 0 : all, 1: luong cao,2: lam tu xa, 3: thuc tap, 4: moi nhat

        getDataTopCompany();
        loadMore();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData(4, page);
            }
        },3000);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkNothing();
            }
        },7000);

        if(MainActivity.login == 1){
            activateAfterLogin();
        }

        eventJob();


        return view;
    }

    private void eventJob() {
        gridViewJob.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(getActivity(), KindOfJobActivity.class);
                        intent.putExtra("kind", 0);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getActivity(), KindOfJobActivity.class);
                        intent1.putExtra("kind", 1);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getActivity(), KindOfJobActivity.class);
                        intent2.putExtra("kind", 2);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getActivity(), KindOfJobActivity.class);
                        intent3.putExtra("kind", 3);
                        startActivity(intent3);
                        break;
                    case 4:
                        if(MainActivity.login == 1){
                            Intent intent4 = new Intent(getActivity(), KindOfJobActivity.class);
                            intent4.putExtra("kind", 4);
                            startActivity(intent4);
                        }else {
                            Intent intent4 = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent4);
                        }

                        break;
                    case 5:
                        Intent intent5 = new Intent(getActivity(), SearchCompanyActivity.class);
                        startActivity(intent5);
                        break;
                    case 6:
                        Intent intent6 = new Intent(getActivity(), KindOfJobActivity.class);
                        intent6.putExtra("kind", 6);
                        startActivity(intent6);
                        break;
                    case 7:
                        if(MainActivity.login == 1){
                            Intent intent7 = new Intent(getActivity(), KindOfJobActivity.class);
                            intent7.putExtra("kind", 7);
                            startActivity(intent7);
                        }else {
                            Intent intent4 = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent4);
                        }

                        break;

                }
            }
        });
    }

    private void getDataTopCompany() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, MainActivity.urlCompanyTop, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response != null){
                            for(int i=0; i < response.length(); i++){
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    arrayListCompany.add(new Company(
                                            object.getInt("id"),
                                            object.getString("name"),
                                            object.getString("introduction"),
                                            object.getString("address"),
                                            object.getInt("idarea"),
                                            object.getInt("idowner"),
                                            object.getString("image"),
                                            object.getString("image_background"),
                                            object.getString("website"),
                                            object.getString("size"),
                                            object.getInt("number_job"),
                                            object.getDouble("vido"),
                                            object.getDouble("kinhdo")
                                    ));


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapterCompany.notifyDataSetChanged();
                        }else {
                            FancyToast.makeText(getActivity(), "Không có công ty nào", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       FancyToast.makeText(getActivity(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void getBasicInfo() {
        arrayListJob.add(new Admin(0, "Việc thực tập", R.drawable.m_intern));
        arrayListJob.add(new Admin(1,"Việc làm từ xa", R.drawable.m_remotejob));
        arrayListJob.add(new Admin(2, "Việc làm thêm", R.drawable.m_parttimejob));
        arrayListJob.add(new Admin(3, "Việc toàn thời gian", R.drawable.m_fulltimejob));
        arrayListJob.add(new Admin(4, "Việc đã ứng tuyển", R.drawable.m_appliedjob));
        arrayListJob.add(new Admin(5, "Tìm kiếm công ty", R.drawable.company));
        arrayListJob.add(new Admin(6, "Việc mới nhất", R.drawable.m_newjob));
        arrayListJob.add(new Admin(7, "Việc phù hợp", R.drawable.m_suitablejob));

        adapterJob.notifyDataSetChanged();




    }

    private void loadMore() {
        adapterViecLamMoiNhat.setLoadmore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData(4,++page);
                        adapterViecLamMoiNhat.setIsloaded(false);
                    }
                },2000);

            }
        });
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
        layout_user.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(MainActivity.uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String avatar = snapshot.child("imageURL").getValue(String.class);
                if(avatar.equals("default")){
                    img.setImageResource(R.drawable.imgprofile);
                }else {
                    if(getActivity() != null){
                        try {
                            Glide.with(getActivity()).load(avatar).into(img);
                        }catch (NullPointerException e){
                            FancyToast.makeText(getActivity(), "Lỗi", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        txtUsername.setText(MainActivity.user.getName());
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
                       FancyToast.makeText(getActivity(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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


    private void getData(final int kind, int page) {
        if(getActivity() != null){
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String url = MainActivity.urljob1 + String.valueOf(page);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //  Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                            if(response != null){
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
                                        ));
                                        adapterViecLamMoiNhat.notifyDataSetChanged();
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
                           FancyToast.makeText(getActivity(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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

}
