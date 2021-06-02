package com.example.luanvan.ui.schedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.LinearLayout;
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
import com.example.luanvan.ui.Adapter.recruit.CandidateScheduleAdapter;
import com.example.luanvan.ui.Adapter.recruit.PositionScheduleAdapter;
import com.example.luanvan.ui.DetailedJob.DetailJobActivity;
import com.example.luanvan.ui.Model.JavaMailAPI;
import com.example.luanvan.ui.Model.JobList;
import com.example.luanvan.ui.Model.Schedule;
import com.example.luanvan.ui.Model.User;
import com.example.luanvan.ui.Model.UserApplicant;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateScheduleActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editKindSchedule, editDate, editStart, editEnd, editNote, editPosition;
    public static EditText editCandidate;
    Button btnSave, btnCancel;
    BottomSheetDialog bottomSheetDialog;
    int type_schedule = 0; // 1: phỏng vấn,  2: đi làm
    String date_post = "";
    String content = "";
    String type_notification = "";
    String start_hour_refresh = "", end_hour_refresh = ""; // để reload lại
    Handler handler;
    ProgressDialog progressDialog;
    Date time_start=null, time_end = null;
    ArrayList<JobList> jobArrayList;
    ArrayList<UserApplicant> candidateArrayList;
    PositionScheduleAdapter positionScheduleAdapter;
    CandidateScheduleAdapter candidateScheduleAdapter;
    RecyclerView recyclerViewJob, recyclerViewCandidate;
    BottomSheetDialog bottomSheetDialogPosition, bottomSheetDialogCandidate;
    public static int job_id = 0,  user_id = 0, ap_id = 0; // để put lên
    public static String job_name = "", username = "", email = "";
    int check_candidate = 0; // nếu có user list rồi thì k tải nữa
    int check_position = 0; // nếu thay đổi thì userlist load lại
    int kind = 0; // kind: 1: create, 2: adjust
    int position_update = 0; // từ bên schedule đưa qua để cập nhật
    Schedule scheduleInfo;
    public static int job_id_update = 0;
    String urlCreate = MainActivity.urlSchedule;
    String urlUpdate = MainActivity.urlUpdateSchedule;
    // schedule ban đầu với jobid, userid, khi 1 trong 2 thay đổi thì k thêm moreAnnounceMent
    int first_jobid = 0, first_userid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);
        anhxa();
        actionBar();
        getInfo();
        eventEditText();
        eventButton();
        createNotificationChannel();

    }

    void setDefault(){
        job_id = 0;
        user_id = 0;
        ap_id = 0;
        job_name = "";
        username = "";
        email = "";
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
        Intent intent = new Intent(CreateScheduleActivity.this, MainActivity.class);
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

    private void getInfo() {
        if(kind == 2){
            scheduleInfo = (Schedule) getIntent().getSerializableExtra("schedule");
            position_update = getIntent().getIntExtra("position",0);
            job_id_update = scheduleInfo.getId_job();
            job_id = scheduleInfo.getId_job();
            first_jobid = scheduleInfo.getId_job();
            first_userid = scheduleInfo.getId_user();
            user_id = scheduleInfo.getId_user();
            job_name = scheduleInfo.getJob_name();
            username = scheduleInfo.getUsername();
            type_schedule = scheduleInfo.getType();
            getAp_ID();
            getCandidateList(job_id_update);
            editPosition.setText(scheduleInfo.getJob_name());
            editCandidate.setText(scheduleInfo.getUsername());
            String ngay = scheduleInfo.getDate();
            date_post = scheduleInfo.getDate();
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = formatDate.parse(ngay);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
            editDate.setText(fmtOut.format(date));

            String start = scheduleInfo.getStart_hour();
            String end = scheduleInfo.getEnd_hour();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            Date time1 = null;
            Date time2 = null;
            try {
                time1 =  simpleDateFormat.parse(start);
                time2 = simpleDateFormat.parse(end);
                time_start = time1;
                time_end = time2;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");
            editStart.setText(formatHour.format(time1));
            editEnd.setText(formatHour.format(time2));

            SimpleDateFormat reloadFormat =  new SimpleDateFormat("HH:mm:ss");
            start_hour_refresh = reloadFormat.format(time1);
            end_hour_refresh = reloadFormat.format(time2);


            if(scheduleInfo.getType() == 1){
                editKindSchedule.setText("Hẹn phỏng vấn");
            }else {
                editKindSchedule.setText("Nhắc lịch đi làm");
            }
            editNote.setText(scheduleInfo.getNote());
        }else{
            setDefault();
        }

    }

    private void getAp_ID() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlGetApplicationId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       if(!response.equals("fail")){
                           ap_id = Integer.parseInt(response.toString());
                       }else {
                           FancyToast.makeText(getApplicationContext(), "Lấy application id fail", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
                map.put("idjob", String.valueOf(job_id));
                map.put("iduser", String.valueOf(user_id));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getDataPosition() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlJobList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                       //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                jobArrayList.add(new JobList(
                                        object.getInt("id"),
                                        object.getString("name"),
                                        object.getInt("idcompany"),
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
                                        object.getString("type_job"),
                                        object.getString("note_reject"),
                                        object.getInt("document"),
                                        object.getInt("new_document"),
                                        object.getInt("interview"),
                                        object.getInt("work"),
                                        object.getInt("skip")
                                ));
                            }
                            positionScheduleAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
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
                map.put("idrecuiter", String.valueOf(MainActivity.iduser));
                map.put("status_post", String.valueOf(0));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void eventButton() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefault();
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(getApplicationContext(), "candidate " + editCandidate.getText().toString(), Toast.LENGTH_SHORT).show();
                if(editKindSchedule.getText().equals("") || editStart.getText().equals("") || editEnd.getText().equals("") || editDate.getText().equals("")
                || editCandidate.getText().toString().equals("") || editPosition.getText().equals("")){
                    FancyToast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else if(time_end.before(time_start)){
                    FancyToast.makeText(getApplicationContext(), "Giờ phỏng vấn không hợp lệ", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }
                else {
                    final String date = editDate.getText().toString();
                    final String start_hour = editStart.getText().toString();
                    final String end_hour = editEnd.getText().toString();
                    final String note = editNote.getText().toString();
                    if(kind == 1){
                        createSchedule(date, start_hour, end_hour, note);
                    }else {
                        updateSchedule(date, start_hour, end_hour, note);
                    }

                }
            }
        });

    }
    public void updateSchedule(final String date, final String start_hour, final String end_hour, final String note){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            FancyToast.makeText(getApplicationContext(), "Cập nhật thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

                            Schedule schedule = new Schedule( scheduleInfo.getId(), MainActivity.iduser, job_id, job_name, user_id,
                                    username, type_schedule, date_post, start_hour_refresh, end_hour_refresh, note, "",0);
                            ScheduleManagementActivity.arrayList.set(position_update, schedule);
                            loading();
                            postNotification(0, date, start_hour, end_hour);
                            handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent();
                                    setResult(345);
                                    setDefault();
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
                map.put("id_sche", String.valueOf(scheduleInfo.getId()));
                map.put("type_schedule", String.valueOf(type_schedule));
                map.put("idjob", String.valueOf(job_id));
                map.put("iduser", String.valueOf(user_id));
                map.put("date", date_post);
                map.put("start", start_hour);
                map.put("end", end_hour);
                map.put("note", note);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }


    public void createSchedule(final String date, final String start_hour, final String end_hour, final String note){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlCreate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("fail")){
                            FancyToast.makeText(getApplicationContext(), "Cập nhật thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            int last = response.lastIndexOf('s');
                            int id_schedule = Integer.parseInt(response.substring(last+1, response.length()));
                            Schedule schedule = new Schedule(id_schedule, MainActivity.iduser, job_id, job_name, user_id,
                                    username, type_schedule, date_post, start_hour_refresh, end_hour_refresh, note,"",0);
                            ScheduleManagementActivity.arrayList.add(schedule);
                            loading();
                            postNotification(0, date, start_hour, end_hour);
                            handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent();
                                    setResult(123);
                                    setDefault();
                                    finish();
                                }
                            },3000);

                        }else {
                            FancyToast.makeText(getApplicationContext(), "Tạo thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
                map.put("idjob", String.valueOf(job_id));
                map.put("iduser", String.valueOf(user_id));
                map.put("date", date_post);
                map.put("start", start_hour);
                map.put("end", end_hour);
                map.put("note", note);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

//    private void sendMail() {
//        JavaMailAPI mail = new JavaMailAPI(this, email, type_notification, content);
//        mail.execute();
//      //  Toast.makeText(getApplicationContext(), "Đã gửi mail", Toast.LENGTH_SHORT).show();
//    }
    public void postNotification(final int type_user, String date, String start_hour, String end_hour){
        if(type_schedule == 1){
            type_notification = "Nhà tuyển dụng hẹn bạn phỏng vấn";
            content = "Lịch hẹn vào ngày " + date + " ,bắt đầu lúc "+ start_hour + " ,kết thúc lúc " + end_hour + ", chi tiết xin liên hệ nhà tuyển dụng";

        }else {
            type_notification = "Nhà tuyển dụng nhắc bạn đi làm";
            content = "Lịch đi làm vào ngày " + date + " ,bắt đầu lúc "+ start_hour + " ,kết thúc lúc " + end_hour + ", chi tiết xin liên hệ nhà tuyển dụng";
        }
    //    Toast.makeText(getApplicationContext(), "userid : " + user_id + " jobid : " + job_id, Toast.LENGTH_SHORT).show();
   //     Toast.makeText(getApplicationContext(), "ap_id  : " + ap_id + " content : " + content, Toast.LENGTH_SHORT).show();
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
                map.put("id_application", String.valueOf(ap_id));
                map.put("type_notification", type_notification);
                map.put("content", content);
                map.put("iduser", String.valueOf(user_id));
                map.put("type_user", String.valueOf(type_user));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void getCandidateList(final int job_id){
        check_candidate = 1;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlUserList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                           // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                candidateArrayList.add(new UserApplicant(
                                        object.getInt("id"),
                                        object.getInt("ap_id"),
                                        object.getString("name"),
                                        object.getString("birthday"),
                                        object.getInt("gender"),
                                        object.getString("address"),
                                        object.getString("email"),
                                        object.getString("introduction"),
                                        object.getString("position"),
                                        object.getInt("phone"),
                                        object.getInt("status"),
                                        object.getInt("mode")
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
                        FancyToast.makeText(getApplicationContext(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("job_id", String.valueOf(job_id));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void eventSheetPosition(){
        bottomSheetDialogPosition = new BottomSheetDialog(CreateScheduleActivity.this, R.style.BottomSheetTheme);
        final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_position, (ViewGroup) findViewById(R.id.bottom_sheet));
        Button btnChoose = (Button) view.findViewById(R.id.buttonchon);
        Button btnCancel = (Button) view.findViewById(R.id.buttonhuy);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogPosition.dismiss();
            }
        });
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPosition.setText(job_name);
                bottomSheetDialogPosition.dismiss();
            }
        });


        recyclerViewJob = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerViewJob.setHasFixedSize(false);
        recyclerViewJob.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewJob.setAdapter(positionScheduleAdapter);
        bottomSheetDialogPosition.setContentView(view);
        bottomSheetDialogPosition.show();

    }

    public void eventSheetCandidate(){
        bottomSheetDialogCandidate = new BottomSheetDialog(CreateScheduleActivity.this, R.style.BottomSheetTheme);
        final View view = LayoutInflater.from(CreateScheduleActivity.this).inflate(R.layout.bottom_sheet_candidate, (ViewGroup) findViewById(R.id.bottom_sheet));
        Button btnChoose = (Button) view.findViewById(R.id.buttonchon);
        Button btnCancel = (Button) view.findViewById(R.id.buttonhuy);
        LinearLayout layout_nothing = (LinearLayout) view.findViewById(R.id.layout_nothing);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout);
        if(candidateArrayList.size() == 0){
            layout.setVisibility(View.GONE);
            layout_nothing.setVisibility(View.VISIBLE);
        }else {
            layout.setVisibility(View.VISIBLE);
            layout_nothing.setVisibility(View.GONE);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogCandidate.dismiss();
            }
        });
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_id != 0){
                    editCandidate.setText(username);
                }
                bottomSheetDialogCandidate.dismiss();

            }
        });


        recyclerViewCandidate = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerViewCandidate.setHasFixedSize(true);
        recyclerViewCandidate.setLayoutManager(new LinearLayoutManager(CreateScheduleActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerViewCandidate.setAdapter(candidateScheduleAdapter);
        bottomSheetDialogCandidate.setContentView(view);
        bottomSheetDialogCandidate.show();

    }

    public void eventSheetSchedule(){
        bottomSheetDialog = new BottomSheetDialog(CreateScheduleActivity.this, R.style.BottomSheetTheme);
        View view = LayoutInflater.from(CreateScheduleActivity.this).inflate(R.layout.bottom_sheet_schedule, (ViewGroup) findViewById(R.id.layout_schedule));
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

        editPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_position = 1;
                eventSheetPosition();

            }
        });

        editCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "positoon " + check_position + " can " + check_candidate, Toast.LENGTH_SHORT).show();
                if(check_candidate == 0 || check_position == 1){
                    candidateArrayList.clear();
                    getCandidateList(job_id);
                }
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        eventSheetCandidate();
                    }
                },1000);

            }
        });

        editKindSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventSheetSchedule();
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
                setDefault();
                finish();
            }
        });
    }

    private void anhxa() {
        kind = getIntent().getIntExtra("kind", 0);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editKindSchedule = (EditText) findViewById(R.id.editkindschedule);
        editDate = (EditText) findViewById(R.id.editdate);
        editStart = (EditText) findViewById(R.id.editstart);
        editEnd = (EditText) findViewById(R.id.editend);
        editNote = (EditText) findViewById(R.id.editnote);
        btnSave = (Button) findViewById(R.id.buttonluu);
        btnCancel = (Button) findViewById(R.id.buttonhuy);
        editPosition = (EditText) findViewById(R.id.editposition);
        editCandidate = (EditText) findViewById(R.id.editcandidate);
        jobArrayList = new ArrayList<>();
        candidateArrayList = new ArrayList<>();
        positionScheduleAdapter = new PositionScheduleAdapter(CreateScheduleActivity.this, jobArrayList, CreateScheduleActivity.this, kind);
        getDataPosition();
        candidateScheduleAdapter = new CandidateScheduleAdapter(CreateScheduleActivity.this, candidateArrayList, CreateScheduleActivity.this);

    }


    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

}
