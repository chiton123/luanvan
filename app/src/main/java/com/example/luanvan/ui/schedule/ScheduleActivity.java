package com.example.luanvan.ui.schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
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
import com.example.luanvan.ui.DetailedJob.DetailJobActivity;
import com.example.luanvan.ui.Model.Applicant;
import com.example.luanvan.ui.Model.JavaMailAPI;
import com.example.luanvan.ui.Model.Schedule;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ScheduleActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText  editKindSchedule, editDate, editStart, editEnd, editNote;
    Button btnSave, btnCancel;
    BottomSheetDialog bottomSheetDialog;
    int type_schedule = 0; // 1: phỏng vấn,  2: đi làm
    String date_post = "";
    Applicant applicant;
    String content = "";
    String type_notification = "";
    Handler handler;
    ProgressDialog progressDialog;
    Date time_start=null, time_end = null;
    String start_hour_refresh = "", end_hour_refresh = ""; // để reload lại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        anhxa();
        actionBar();
        eventEditText();
        eventButton();
        createNotificationChannel();

    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.NEWS_CHANNEL_ID), getString(R.string.CHANNEL_NEWS),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(getString(R.string.CHANNEL_DESCRIPTION));
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

        }

    }
    public void phoneNotification(){
        Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.NEWS_CHANNEL_ID))
                .setSmallIcon(R.drawable.topcv)
                .setContentTitle(type_notification)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(getResources().getInteger(R.integer.config_navAnimTime), builder.build());

    }
    private void eventButton() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Toast.makeText(getApplicationContext(), date_post, Toast.LENGTH_SHORT).show();
                if(editKindSchedule.getText().equals("") || editStart.getText().equals("") || editEnd.getText().equals("") || editDate.getText().equals("")
                ){
                    FancyToast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else if(time_end.before(time_start)){
                    FancyToast.makeText(getApplicationContext(), "Giờ phỏng vấn không hợp lệ", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }
                else {
                    final String date = editDate.getText().toString();
                    final String start_hour = editStart.getText().toString();
                    final String end_hour = editEnd.getText().toString();
                    final String note = editNote.getText().toString();

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlSchedule,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(!response.equals("fail")){
                                        FancyToast.makeText(getApplicationContext(), "Cập nhật thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                        if(ScheduleManagementActivity.checkLoad == 1){
                                            int last = response.lastIndexOf("s");
                                            int id_schedule = Integer.parseInt(response.substring(last+1, response.length()));
                                            Schedule schedule = new Schedule(id_schedule, MainActivity.iduser, applicant.getJob_id(),
                                                    applicant.getJob_name(), applicant.getUser_id(),
                                                    applicant.getUsername(), type_schedule, date_post, start_hour_refresh, end_hour_refresh, note,"",0);
                                            ScheduleManagementActivity.arrayList.add(schedule);
                                            ScheduleManagementActivity.adapter.notifyDataSetChanged();
                                        }

                                        loading();
                                        postNotification(0, date, start_hour, end_hour);
                                        sendMail();
                                        handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent();
                                                setResult(123);
                                                finish();
                                            }
                                        },3000);

                                    }else {
                                        FancyToast.makeText(getApplicationContext(),"Tạo thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
                            map.put("idrecruiter", String.valueOf(MainActivity.iduser));
                            map.put("type_schedule", String.valueOf(type_schedule));
                            map.put("idjob", String.valueOf(applicant.getJob_id()));
                            map.put("iduser", String.valueOf(applicant.getUser_id()));
                            map.put("date", date_post);
                            map.put("start", start_hour);
                            map.put("end", end_hour);
                            map.put("note", note);
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);

                }
            }
        });

    }
    private void sendMail() {
        JavaMailAPI mail = new JavaMailAPI(this, applicant.getEmail(), type_notification, content);
        mail.execute();
     //   Toast.makeText(getApplicationContext(), "Đã gửi mail", Toast.LENGTH_SHORT).show();
    }
    public void postNotification(final int type_user, String date, String start_hour, String end_hour){
        if(type_schedule == 1){
            type_notification = "Nhà tuyển dụng hẹn bạn phỏng vấn";
            content = "Lịch hẹn vào ngày " + date + " ,bắt đầu lúc "+ start_hour + " ,kết thúc lúc " + end_hour + ", chi tiết xin liên hệ nhà tuyển dụng";
        }else {
            type_notification = "Nhà tuyển dụng nhắc bạn đi làm";
            content = "Lịch đi làm vào ngày " + date + " ,bắt đầu lúc "+ start_hour + " ,kết thúc lúc " + end_hour + ", chi tiết xin liên hệ nhà tuyển dụng";
        }
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlPostNotification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                          //  Toast.makeText(getApplicationContext(), "Thông báo thành công", Toast.LENGTH_SHORT).show();
                            phoneNotification();
                        }else {


                        FancyToast.makeText(getApplicationContext(), "Thông báo thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
                map.put("id_application", String.valueOf(applicant.getId()));
                map.put("type_notification", type_notification);
                map.put("content", content);
                map.put("iduser", String.valueOf(applicant.getUser_id()));
                map.put("type_user", String.valueOf(type_user));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void showDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = dateFormat.format(Calendar.getInstance().getTime());
        final Date today1 = dateFormat.parse(today);
        final Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                if(calendar.getTime().before(today1) ){
                    FancyToast.makeText(getApplicationContext(), "Phải lớn hơn ngày hiện tại", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else {
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = fmt.parse(fmt.format(calendar.getTime()));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    date_post = fmt.format(date);
                    editDate.setText(dateFormat.format(date));
                //    Toast.makeText(getApplicationContext(),date_post , Toast.LENGTH_SHORT).show();
                }

            }
        }, nam, thang, ngay);
        datePickerDialog.show();

    }
    private void eventEditText() {
        editKindSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(ScheduleActivity.this, R.style.BottomSheetTheme);
                View view = LayoutInflater.from(ScheduleActivity.this).inflate(R.layout.bottom_sheet_schedule, (ViewGroup) findViewById(R.id.layout_schedule));
                final RadioButton radioInterView = (RadioButton) view.findViewById(R.id.radiohenpv);
                final RadioButton radioWork = (RadioButton) view.findViewById(R.id.radiodilam);
                Button btnChoose = (Button) view.findViewById(R.id.buttonchon);
                Button btnCancel = (Button) view.findViewById(R.id.buttonhuy);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                btnChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(radioInterView.isChecked()){
                            type_schedule = 1;
                            editKindSchedule.setText("Hẹn phỏng vấn");
                        }else if(radioWork.isChecked()){
                            type_schedule = 2;
                            editKindSchedule.setText("Nhắc lịch đi làm");
                        }
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();

            }
        });
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showDate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        editStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTime(1);
            }
        });
        editEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTime(2);
            }
        });


    }
    // kind: 1->start, 2->end
    public void ShowTime(final int kind){
        final Calendar calendar = Calendar.getInstance();
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                SimpleDateFormat reloadFormat =  new SimpleDateFormat("HH:mm:ss");
                calendar.set(0,0,0, hourOfDay, minute);
                if(kind == 1){
                    editStart.setText(simpleDateFormat.format(calendar.getTime()));
                    time_start = calendar.getTime();
                    start_hour_refresh = reloadFormat.format(calendar.getTime());
                }else {
                    editEnd.setText(simpleDateFormat.format(calendar.getTime()));
                    time_end = calendar.getTime();
                    end_hour_refresh = reloadFormat.format(calendar.getTime());
                }

            }
        }, gio, phut, true);
        timePickerDialog.show();

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
        editKindSchedule = (EditText) findViewById(R.id.editkindschedule);
        editDate = (EditText) findViewById(R.id.editdate);
        editStart = (EditText) findViewById(R.id.editstart);
        editEnd = (EditText) findViewById(R.id.editend);
        editNote = (EditText) findViewById(R.id.editnote);
        btnSave = (Button) findViewById(R.id.buttonluu);
        btnCancel = (Button) findViewById(R.id.buttonhuy);
        applicant = (Applicant) getIntent().getSerializableExtra("applicant");

    }
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
}
