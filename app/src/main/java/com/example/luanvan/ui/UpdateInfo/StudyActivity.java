package com.example.luanvan.ui.UpdateInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.luanvan.ui.Model.Study;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StudyActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editshool, editmajor, editstart, editend, editmota;
    Button btnhuy, btncapnhat;
    String date_post_start = "", date_post_end = "";
    Date date_start = null, date_end = null;
    int check_start = 0;
    int id = 0; // id study trên csdl
    int position = 0; // trên mảng arraylist, thứ tự
    int update = 0;
    String url = "";
    ProgressDialog progressDialog;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        anhxa();
        actionBar();
        eventUpdate();
        eventDate();
        getInfo();

    }
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    private void getInfo() {
        // 10: add, 3: update
        int check = getIntent().getIntExtra("confirm", 0);
        if(check == 3){
            update = 1;
            Study study = (Study) getIntent().getSerializableExtra("study");
            position = getIntent().getIntExtra("position", 0);
            id = study.getId();
            editshool.setText(study.getSchool());
            editmajor.setText(study.getMajor());
            editmota.setText(study.getDescription());
            String ngaybatdau = study.getDate_start();
            String ngayketthuc = study.getDate_end();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = null, date2 = null;
            try {
                date1 = fmt.parse(ngaybatdau);
                date2 = fmt.parse(ngayketthuc);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
            try {
                editstart.setText(fmtOut.format(date1));
                editend.setText(fmtOut.format(date2));
            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void showDate(final int kind) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = dateFormat.format(Calendar.getInstance().getTime());
        final Date today1 = dateFormat.parse(today);
        final Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                if(calendar.getTime().after(today1) && kind == 1){
                    Toast.makeText(getApplicationContext(), "Phải lớn nhỏ ngày hiện tại", Toast.LENGTH_SHORT).show();
                }else {
                    if(kind == 1){
                        editstart.setText(dateFormat.format(calendar.getTime()));
                        date_start = calendar.getTime();
                    }else {
                        date_end = calendar.getTime();
                        if(date_end.before(date_start)){
                            Toast.makeText(getApplicationContext(), "Ngày kết thúc phải sau ngày bắt đầu", Toast.LENGTH_SHORT).show();
                        }else {
                            editend.setText(dateFormat.format(calendar.getTime()));
                        }

                    }

                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = fmt.parse(fmt.format(calendar.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(kind == 1){
                        date_post_start = fmt.format(date);
                        check_start = 1;
                    }else {
                        date_post_end = fmt.format(date);
                    }

                }

            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }
    private void eventDate() {
        editstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showDate(1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        editend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(check_start == 0){
                        Toast.makeText(getApplicationContext(), "Bạn chọn ngày bắt đầu trước", Toast.LENGTH_SHORT).show();
                    }else {
                        showDate(2);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getInfoStudy() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlschool,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                MainActivity.studies.add(new Study(
                                        object.getInt("id"),
                                        object.getString("school"),
                                        object.getString("major"),
                                        object.getString("start"),
                                        object.getString("end"),
                                        object.getString("description")
                                ));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
    private void eventUpdate() {
        btncapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editshool.getText().equals("") || editmota.getText().equals("") || editmajor.getText().equals("") || editstart.getText().equals("")
                || editend.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin" , Toast.LENGTH_SHORT).show();
                }else {
                    final String school = editshool.getText().toString();
                    final String major = editmajor.getText().toString();
                    final String mota = editmota.getText().toString();
                    if(update == 1){
                        url = MainActivity.urlupdate_old_study;
                    }else {
                        url = MainActivity.urlupdatestudy;
                    }
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("success")){
                                        Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                                        if(update == 1){
                                            MainActivity.studies.get(position).setSchool(school);
                                            MainActivity.studies.get(position).setMajor(major);
                                            MainActivity.studies.get(position).setDescription(mota);
                                            MainActivity.studies.get(position).setDate_start(date_post_start);
                                            MainActivity.studies.get(position).setDate_end(date_post_end);
                                        }else {
                                            MainActivity.studies.clear();
                                            getInfoStudy();
                                        }
                                        loading();
                                        handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(update == 0){

                                                    MainActivity.studyAdapter.notifyDataSetChanged();
                                                }

                                                progressDialog.dismiss();
                                                Intent intent = new Intent();
                                                setResult(1, intent);
                                                finish();
                                            }
                                        },3000);

                                    }else {
                                        Toast.makeText(getApplicationContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
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
                            map.put("id", String.valueOf(id));
                            map.put("iduser", String.valueOf(MainActivity.iduser));
                            map.put("school", school);
                            map.put("mota", mota);
                            map.put("major", major);
                            map.put("start", date_post_start);
                            map.put("end", date_post_end);
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            }
        });


        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        editshool = (EditText) findViewById(R.id.editschool);
        editmajor = (EditText) findViewById(R.id.editmajor);
        editstart = (EditText) findViewById(R.id.editstart);
        editend = (EditText) findViewById(R.id.editend);
        editmota = (EditText) findViewById(R.id.editmota);
        btnhuy = (Button) findViewById(R.id.buttonhuy);
        btncapnhat = (Button) findViewById(R.id.buttoncapnhat);
    }

}
