package com.example.luanvan.ui.UpdateInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.luanvan.ui.User.NotificationsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

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
    int id = 0; // mysql
    int position = 0; // trên mảng arraylist, thứ tự
    int update = 0;
    String url = "";
    ProgressDialog progressDialog;
    Handler handler;
    Handler handler1;
    int x = 0; // check end date có trước start date hay k, nếu có thì 1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        anhxa();
        actionBar();
        getInfo();
        eventUpdate();
        eventDate();


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
           // Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
            editshool.setText(study.getSchool());
            editmajor.setText(study.getMajor());
            editmota.setText(study.getDescription());
            String ngaybatdau = study.getDate_start();
            String ngayketthuc = study.getDate_end();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = null, date2 = null;
            try {
                date1 = fmt.parse(ngaybatdau);
                date2 = fmt.parse(ngayketthuc);

                date_start = date1;
                date_end = date2;
                date_post_start = fmt.format(date1);
                date_post_end = fmt.format(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                editstart.setText(fmtOut.format(date1));
                editend.setText(fmtOut.format(date2));
            }catch (NullPointerException e){
                FancyToast.makeText(getApplicationContext(), e.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
        //    Toast.makeText(getApplicationContext(), "start: " + date_post_start + " end: " + date_post_end, Toast.LENGTH_SHORT).show();

        }
    }

    public void showDate(final int kind) throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                Date date_temp = null;
                try {
                    date_temp = fmt.parse(fmt.format(calendar.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(kind == 1){
                    if(!editend.getText().toString().equals("")){
                        if(date_temp.after(date_end) || date_temp.equals(date_end)){
                            FancyToast.makeText(getApplicationContext(), "Ngày bắt đầu phải trước ngày kết thúc", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            x = 1;
                        }else {
                            date_start = calendar.getTime();
                            editstart.setText(dateFormat.format(calendar.getTime()));
                            x = 0;
                        }
                    }else {
                        date_start = calendar.getTime();
                        editstart.setText(dateFormat.format(calendar.getTime()));
                        x = 0;
                    }

                }else {
                    if(!editstart.getText().toString().equals("")){
                        if(date_temp.before(date_start) || date_temp.equals(date_start)){
                            FancyToast.makeText(getApplicationContext(), "Ngày kết thúc phải sau ngày bắt đầu", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            x = 1;
                           // FancyToast.makeText(getApplicationContext(), "0", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                        }else{
                            date_end = calendar.getTime();
                            editend.setText(dateFormat.format(calendar.getTime()));
                            x = 0;
                          //  FancyToast.makeText(getApplicationContext(), "1", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                        }
                    }else {
                        date_end = calendar.getTime();
                        editend.setText(dateFormat.format(calendar.getTime()));
                        x = 0;
                       // FancyToast.makeText(getApplicationContext(), "2", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                    }

                }


                Date date = null;
                try {
                    date = fmt.parse(fmt.format(calendar.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(kind == 1 && x == 0){
                    date_post_start = fmt.format(date);
                  //  check_start = 1;
                }else if(kind == 2 && x == 0){
                    date_post_end = fmt.format(date);
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
                    showDate(2);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    private void eventUpdate() {

            btncapnhat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String school = editshool.getText().toString();
                    final String major = editmajor.getText().toString();
                    final String mota = editmota.getText().toString();
                    String start = editstart.getText().toString();
                    String end = editend.getText().toString();
                    if (school.equals("") || major.equals("") || mota.equals("") || start.equals("") || end.equals("")) {
                        FancyToast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                    } else if (date_end.before(date_start)) {
                        FancyToast.makeText(getApplicationContext(), "Ngày kết thúc phải sau ngày bắt đầu", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                    } else if (date_start.after(date_end)) {
                        FancyToast.makeText(getApplicationContext(), "Ngày bắt đầu phải trước ngày kết thúc", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                    } else {
                        loading();
                        if (update == 1) {
                            final Study study = new Study(id, MainActivity.iduser, school, major, date_post_start, date_post_end, mota);
                            MainActivity.studies.set(position, study);
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlUpdateStudy,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if(response.equals("success")){
                                                FancyToast.makeText(getApplicationContext(), "Cập nhật thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                            }else {
                                                FancyToast.makeText(getApplicationContext(),"Cập nhật thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            FancyToast.makeText(getApplicationContext(),error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                        }
                                    }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> map = new HashMap<>();
                                    map.put("id", String.valueOf(id));
                                    map.put("school", school);
                                    map.put("major", major);
                                    map.put("start", date_post_start);
                                    map.put("end", date_post_end);
                                    map.put("description", mota);
                                    return map;
                                }
                            };
                            requestQueue.add(stringRequest);

                            handler1 = new Handler();
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent();
                                    setResult(1, intent);
                                    finish();
                                }
                            }, 2000);


                        } else {
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlAddStudy,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if(!response.equals("fail")){
                                                FancyToast.makeText(getApplicationContext(), "Cập nhật thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                                id = Integer.parseInt(response.toString());
                                                final Study study = new Study(id, MainActivity.iduser, school, major, date_post_start, date_post_end, mota);
                                                MainActivity.studies.add(study);
                                                NotificationsFragment.studyAdapter.notifyDataSetChanged();
                                            }else {
                                                FancyToast.makeText(getApplicationContext(),"Cập nhật thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            FancyToast.makeText(getApplicationContext(),error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                        }
                                    }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> map = new HashMap<>();
                                    map.put("school", school);
                                    map.put("major", major);
                                    map.put("start", date_post_start);
                                    map.put("end", date_post_end);
                                    map.put("description", mota);
                                    map.put("iduser", String.valueOf(MainActivity.iduser));
                                    return map;
                                }
                            };
                            requestQueue.add(stringRequest);

                            handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    NotificationsFragment.studyAdapter.notifyDataSetChanged();
                                    progressDialog.dismiss();
                                    Intent intent = new Intent();
                                    setResult(1, intent);
                                    finish();
                                }
                            }, 1000);
                        }
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
