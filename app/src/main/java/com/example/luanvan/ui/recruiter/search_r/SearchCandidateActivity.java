package com.example.luanvan.ui.recruiter.search_r;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.luanvan.ui.Adapter.recruit.CandidateSearchAdapter;
import com.example.luanvan.ui.Model.UserSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchCandidateActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    public static ArrayList<UserSearch> arrayList;
    CandidateSearchAdapter adapter;
    int REQUEST_FILTER = 1;
    public static ArrayList<UserSearch> arrayListAll;
    public static int search_or_not = 0;
    LinearLayout layout, layout_nothing;
    Handler handler;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_candidate);
        anhxa();
        actionBar();
        getData();



    }
    void checkNothing(){
        if(arrayList.size() == 0){
            layout_nothing.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }else {
            layout_nothing.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
    }
    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlSearchUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int check_trunglap = 0;
                                    if(arrayList.size() > 0){
                                        for(int j=0; j < arrayList.size(); j++){
                                            if(object.getInt("iduser") == arrayList.get(j).getIduser()){
                                                check_trunglap = 1;
                                            }
                                        }
                                    }

                                    if(check_trunglap == 0){
                                        arrayList.add(new UserSearch(
                                                object.getInt("iduser"),
                                                object.getInt("idposition"),
                                                object.getString("position"),
                                                object.getInt("idcv"),
                                                object.getString("user_id_f"),
                                                object.getString("username"),
                                                object.getString("birthday"),
                                                object.getInt("gender"),
                                                object.getString("address"),
                                                object.getString("email"),
                                                object.getString("introduction"),
                                                object.getInt("phone"),
                                                object.getInt("mode"),
                                                object.getString("experience"),
                                                object.getString("study"),
                                                object.getInt("idarea"),
                                                object.getString("area")
                                        ));
                                        arrayListAll.add(new UserSearch(
                                                object.getInt("iduser"),
                                                object.getInt("idposition"),
                                                object.getString("position"),
                                                object.getInt("idcv"),
                                                object.getString("user_id_f"),
                                                object.getString("username"),
                                                object.getString("birthday"),
                                                object.getInt("gender"),
                                                object.getString("address"),
                                                object.getString("email"),
                                                object.getString("introduction"),
                                                object.getInt("phone"),
                                                object.getInt("mode"),
                                                object.getString("experience"),
                                                object.getString("study"),
                                                object.getInt("idarea"),
                                                object.getString("area")
                                        ));
                                    }

                                    adapter.notifyDataSetChanged();
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
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("idposition", String.valueOf(0));
                map.put("checkarea", String.valueOf(0));
                map.put("area", "a");
                map.put("checkjobskill", String.valueOf(0));
                map.put("jobskill", "a");
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // filter
        if(requestCode == REQUEST_FILTER && resultCode == 1){
            loading();
            adapter.notifyDataSetChanged();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkNothing();
                    progressDialog.dismiss();
                }
            },2000);
        }
        if(requestCode == REQUEST_FILTER && resultCode == 2){
            loading();
            arrayList.clear();
            arrayList.addAll(arrayListAll);
            adapter.notifyDataSetChanged();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkNothing();
                    progressDialog.dismiss();
                }
            },2000);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.filterhienthi:
                Intent intent = new Intent(getApplicationContext(), FilterCandidateActivity.class);
                startActivityForResult(intent, REQUEST_FILTER);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_or_not = 0;
                finish();
            }
        });
    }

    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchCandidateActivity.this, LinearLayoutManager.VERTICAL, false));
        arrayList = new ArrayList<>();
        arrayListAll = new ArrayList<>();
        adapter = new CandidateSearchAdapter(SearchCandidateActivity.this, arrayList, SearchCandidateActivity.this);
        recyclerView.setAdapter(adapter);
        layout = (LinearLayout) findViewById(R.id.layout);
        layout_nothing = (LinearLayout) findViewById(R.id.layout_nothing);


    }
}
