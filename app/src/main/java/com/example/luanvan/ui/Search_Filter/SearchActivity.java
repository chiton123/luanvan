package com.example.luanvan.ui.Search_Filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.example.luanvan.ui.Adapter.job.KindOfJobAdapter;
import com.example.luanvan.ui.Interface.ILoadMore;
import com.example.luanvan.ui.Model.Job;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    SearchView searchView;
    KindOfJobAdapter adapter;
    public static ArrayList<Job> arrayList;
    RecyclerView recyclerView;
    int REQUEST_CODE_FILTER = 123;
    LinearLayout layout, layout_nothing;
    Handler handler;
    int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        anhxa();
        actionBar();
        // all jobs
        getData(8, page);
        loadMore();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chechNothing();
            }
        },2000);


    }

    public void stopLoadMore(){
        adapter.setIsloaded(true);
    }
    private void loadMore() {
        adapter.setLoadmore(new ILoadMore() {
            @Override
            public void onLoadMore() {

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData(8,++page);
                        adapter.notifyDataSetChanged();
                        adapter.setIsloaded(false);

                    }
                },2000);

            }
        });
    }

    public void chechNothing(){
        if(arrayList.size() == 0){
            layout_nothing.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }else {
            layout.setVisibility(View.VISIBLE);
            layout_nothing.setVisibility(View.GONE);
        }
    }

    private void getData(final int kind, final int page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = MainActivity.urljob1 + String.valueOf(page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    //    Toast.makeText(getApplicationContext(), "page " + page, Toast.LENGTH_SHORT ).show();
                   //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                arrayList.add(new Job(
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
                                adapter.notifyDataSetChanged();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
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
                map.put("kind", String.valueOf(kind));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // lọc
        if(requestCode == REQUEST_CODE_FILTER && resultCode == 123){
            adapter.notifyDataSetChanged();
            chechNothing();
            page = 1;
            adapter.setIsloaded(true);

        }
        // hủy bộ lọc
        if(requestCode == REQUEST_CODE_FILTER && resultCode == 234){
            SearchActivity.arrayList.clear();
            page = 1;
            getData(8, page);
            adapter.setIsloaded(false);
            loadMore();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chechNothing();
                }
            },2000);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.searchhienthithongtin);
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);

        searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.filterhienthi:
                Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                searchView.setQuery("" , false);
                searchView.clearFocus();
                startActivityForResult(intent, REQUEST_CODE_FILTER);

        }

        return super.onOptionsItemSelected(item);
    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("FileName", 0);
                sharedPreferences.edit().clear().commit();
                finish();
            }
        });

    }

    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        arrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new KindOfJobAdapter(recyclerView,this, arrayList, this,0);
        recyclerView.setAdapter(adapter);
        layout = (LinearLayout) findViewById(R.id.layout);
        layout_nothing= (LinearLayout) findViewById(R.id.layout_nothing);


    }
}
