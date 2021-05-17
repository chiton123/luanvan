package com.example.luanvan.ui.recruiter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
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
import com.example.luanvan.ui.Adapter.assess.AccessmentManagementAdapter;
import com.example.luanvan.ui.Model.Assessment;
import com.example.luanvan.ui.company.CompanyActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AssessmentManagementActivity extends AppCompatActivity {
    Toolbar toolbar;
    public static RatingBar overallRating;
    public static TextView txtRating;
    AccessmentManagementAdapter adapter;
    ArrayList<Assessment> arrayList;
    RecyclerView recyclerView;
    DecimalFormat decimalFormat = new DecimalFormat("#.#");
    public static float average = 0, total = 0;
    LinearLayout layout, layout_nothing;
    ProgressDialog progressDialog;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_management);
        loading();
        anhxa();
        actionBar();
        getData();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkNothing();
                progressDialog.dismiss();
            }
        },2000);


    }
    public void checkNothing(){
        if(arrayList.size() == 0){
            layout_nothing.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }else {
            layout_nothing.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
    }
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    private void getData() {
   //     Toast.makeText(getApplicationContext(), "" + MainActivity.idcompany, Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlGetRemark,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                  //      Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        if(response != null){
                            arrayList.clear();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                //  Toast.makeText(getActivity(), jsonArray.length() + "", Toast.LENGTH_SHORT).show();
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    arrayList.add(new Assessment(
                                            object.getInt("id"),
                                            object.getInt("idcompany"),
                                            object.getInt("iduser"),
                                            object.getString("username"),
                                            object.getString("remark"),
                                            (float) object.getDouble("star")
                                    ));
                                    total += (float) object.getDouble("star");
                                    adapter.notifyDataSetChanged();
                                }
                                if(arrayList.size() > 0){
                                    average = Float.valueOf(decimalFormat.format(total/arrayList.size()));
                                    txtRating.setText(average +"");
                                    overallRating.setRating(average);
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
                        FancyToast.makeText(getApplicationContext(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("idcompany", String.valueOf(MainActivity.idcompany));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                average = 0;
                total = 0;
                finish();
            }
        });
    }

    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new AccessmentManagementAdapter(getApplicationContext(), arrayList, AssessmentManagementActivity.this);
        recyclerView.setAdapter(adapter);
        layout = (LinearLayout) findViewById(R.id.layout);
        layout_nothing = (LinearLayout) findViewById(R.id.layout_nothing);
        overallRating = (RatingBar) findViewById(R.id.rating);
        txtRating = (TextView) findViewById(R.id.txtrating);
    }
}
