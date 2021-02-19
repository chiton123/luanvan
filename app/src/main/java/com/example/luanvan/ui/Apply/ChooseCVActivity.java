package com.example.luanvan.ui.Apply;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import com.example.luanvan.ui.Adapter.CVChooseAdapter;
import com.example.luanvan.ui.DetailedJob.DetailJobActivity;

import java.util.HashMap;
import java.util.Map;

public class ChooseCVActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    CVChooseAdapter adapter;
    Button btnBack, btnApply;
    int check = 0; // chọn chưa, chọn rồi thì 1
    int positionCV = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_c_v);
        anhxa();
        actionBar();
        eventChoose();
        eventButton();


    }
    public void showAlert(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Bạn có chắc chắn muốn ứng tuyển công việc này không ?");
        alert.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.setPositiveButton("Ứng tuyển", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlApply,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("success")){
                                    Toast.makeText(getApplicationContext(), "Ứng tuyển thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    setResult(123);
                                    finish();
                                }else {
                                    Toast.makeText(getApplicationContext(), "Ứng tuyển thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<>();
                        map.put("job_id", String.valueOf(DetailJobActivity.job_id));
                        map.put("user_id", String.valueOf(MainActivity.user.getId()));
                        map.put("user_id_f", MainActivity.uid);
                        map.put("cv_id", MainActivity.arrayListCV.get(positionCV).getKey());
                        return map;
                    }
                };
                requestQueue.add(stringRequest);

            }
        });

        alert.show();
    }

    private void eventButton() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check == 0){
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn CV", Toast.LENGTH_SHORT).show();
                }else {
                    showAlert();
                }
            }
        });

    }

    private void eventChoose() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                check = 1;
                positionCV = position;
            }
        });

    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new CVChooseAdapter(getApplicationContext(), MainActivity.arrayListCV, this);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        btnApply = (Button) findViewById(R.id.buttonungtuyen);
        btnBack = (Button) findViewById(R.id.buttonquaylai);

    }
}
