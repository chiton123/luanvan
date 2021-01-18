package com.example.luanvan.ui.UpdateInfo;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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
    String id = ""; // id study trên csdl
    int position = 0; // trên mảng arraylist, thứ tự
    int update = 0;
    String url = "";
    ProgressDialog progressDialog;
    Handler handler = new Handler();
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
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        //    Toast.makeText(getApplicationContext(), "start: " + date_post_start + " end: " + date_post_end, Toast.LENGTH_SHORT).show();

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
                            x = 1;
                        }else {
                            editend.setText(dateFormat.format(calendar.getTime()));
                            x = 0;
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
                    if(check_start == 0 && editstart.getText().equals("")){
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


    private void eventUpdate() {
        btncapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editshool.getText().equals("") || editmota.getText().equals("") || editmajor.getText().equals("") || editstart.getText().equals("")
                || editend.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin" , Toast.LENGTH_SHORT).show();
                }else if(x == 1){
                    Toast.makeText(getApplicationContext(), "Ngày kết thúc phải sau ngày bắt đầu" , Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final String school = editshool.getText().toString();
                    final String major = editmajor.getText().toString();
                    final String mota = editmota.getText().toString();
                    if(update == 1){
                        url = MainActivity.urlupdate_old_study;
                    }else {
                        url = MainActivity.urlupdatestudy;
                    }
                    String key = MainActivity.mData.push().getKey();
                    Study study = new Study(key, MainActivity.uid, school, major, date_post_start, date_post_end, mota);
                    MainActivity.mData.child("study").push().setValue(study)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        setResult(1, intent);
                                        finish();
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


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
