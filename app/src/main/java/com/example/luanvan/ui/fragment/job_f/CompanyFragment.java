package com.example.luanvan.ui.fragment.job_f;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.luanvan.ui.Model.Company;
import com.example.luanvan.ui.Model.Job;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CompanyFragment extends Fragment {
    TextView txttencongty, txtdiachi, txtwebsite, txtchitiet, txtgioithieu;
    int idcompany = 0;
    ArrayList<Company> arrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company, container, false);
        txttencongty = (TextView) view.findViewById(R.id.tencongty);
        txtdiachi = (TextView) view.findViewById(R.id.diachi);
        txtwebsite = (TextView) view.findViewById(R.id.website);
        txtchitiet = (TextView) view.findViewById(R.id.xemchitiet);
        txtgioithieu = (TextView) view.findViewById(R.id.gioithieu);
        arrayList = new ArrayList<>();
        getCompanyInfo();
        openWebsite();

        return view;
    }

    private void openWebsite() {
        txtwebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(txtwebsite.getText().toString()));
                startActivity(intentBrowser);
            }
        });

    }

    public String xuongdong(String text){
        String ketqua = "";
        if(text.contains("-")){
            String[] split = text.split("-");
            for(String item : split){
                ketqua += "-" + item + "\n";
            }
            return ketqua;
        }else {
            return text;
        }
    }
    private void getCompanyInfo() {
        Intent intent = (Intent) getActivity().getIntent();
        Job job = (Job) intent.getSerializableExtra("job");
        idcompany = job.getIdcompany();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlcompany,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response != null){

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                    JSONObject object = jsonArray.getJSONObject(0);
                                    arrayList.add(new Company(
                                            object.getInt("id"),
                                            object.getString("name"),
                                            object.getString("introduction"),
                                            object.getString("address"),
                                            object.getInt("idarea"),
                                            object.getInt("idowner"),
                                            object.getString("image"),
                                            object.getString("website"),
                                            object.getInt("status"),
                                            object.getDouble("vido"),
                                            object.getDouble("kinhdo")
                                            ));

                                    Company company = arrayList.get(0);
                                    txtdiachi.setText(company.getAddress());
                                    txttencongty.setText(company.getName());
                                    txtwebsite.setText(company.getWebsite());
                                    String gioithieu = xuongdong(company.getIntroduction());
                                    txtgioithieu.setText(gioithieu);

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
                map.put("idcompany", String.valueOf(idcompany));
                return map;
            }
        };
        requestQueue.add(stringRequest);


    }


}
