package com.example.luanvan.ui.fragment.company_f;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.luanvan.ui.Adapter.assess.AccessmentAdapter;
import com.example.luanvan.ui.Model.Assessment;
import com.example.luanvan.ui.User.CreateAssessmentActivity;
import com.example.luanvan.ui.company.CompanyActivity;
import com.example.luanvan.ui.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AssessmentFragment extends Fragment {
    RatingBar overallRating;
    TextView txtRating;
    public static ArrayList<Assessment> arrayList;
    AccessmentAdapter adapter;
    RecyclerView recyclerView;
    float average = 0, total = 0;
    Button btnCreate;
    int REQUEST_REMARK = 111;
    DecimalFormat decimalFormat = new DecimalFormat("#.#");
    LinearLayout layout, layout_nothing;
    Handler handler;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assessment, container, false);
        overallRating = (RatingBar) view.findViewById(R.id.rating);
        txtRating = (TextView) view.findViewById(R.id.txtrating);
        arrayList = new ArrayList<>();
        btnCreate = (Button) view.findViewById(R.id.buttoncreate);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new AccessmentAdapter(getActivity(), arrayList, getActivity());
        recyclerView.setAdapter(adapter);
        layout = (LinearLayout) view.findViewById(R.id.layout);
        layout_nothing = (LinearLayout) view.findViewById(R.id.layout_nothing);
        loading();
        getData();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkNothing();
                progressDialog.dismiss();
            }
        },2000);
        eventButton();

        return view;
    }

    void loading(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
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
    private void eventButton() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.login == 0){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), CreateAssessmentActivity.class);
                    startActivityForResult(intent, REQUEST_REMARK);
                }

            }
        });

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlGetRemark,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("idcompany", String.valueOf(CompanyActivity.company.getId()));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_REMARK && resultCode == 111){
            adapter.notifyDataSetChanged();
            total += arrayList.get(arrayList.size() - 1).getStar();
            average = Float.valueOf(decimalFormat.format(total/arrayList.size()));
            txtRating.setText(average +"");
            overallRating.setRating(average);
            checkNothing();
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}
