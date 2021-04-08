package com.example.luanvan.ui.DetailedJob;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.ViewPageAdapter;
import com.example.luanvan.ui.Apply.ChooseCVActivity;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.Job_Apply;
import com.example.luanvan.ui.Model.Schedule;
import com.example.luanvan.ui.chat.MessageActivity;
import com.example.luanvan.ui.fragment.job_f.CompanyFragment;
import com.example.luanvan.ui.fragment.job_f.InfoFragment;
import com.example.luanvan.ui.fragment.job_f.RelevantJobFragment;
import com.example.luanvan.ui.home.HomeFragment;
import com.example.luanvan.ui.login.LoginActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailJobActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnApply, btnChange, btnUse, btnChat;
    ImageView anhcongty;
    TextView txttencongviec, txtcongty, txthannop;
    TabLayout tabLayout;
    ViewPageAdapter viewPageAdapter;
    ViewPager viewPager;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView txtCV, txtName, txtEmail, txtPhone;
    int checkCV = 0; // có cv thì 0, k có thì 1
    int REQUEST_CODE_CV = 123, REQUEST_CODE_LOGIN = 222;
    public static int job_id = 0;
    Dialog dialog;
    // dành cho từ notification chuyển qua
    Job job;
    int kind = 0; // 0: màn hình chính chuyển, 1: từ notification chuyển
  //  int checkApply = 0; // khi ứng tuyển, xem coi thành công hay thất bại rồi thông báo
    Handler handler;
    Handler handler2;
    ProgressDialog progressDialog;
    int id_application = 0; // sau khi ứng tuyển xong thì lấy về
    String type_notification = "Hồ sơ mới ứng tuyển";
    String content = "";
    public static int checkApplyAgain = 0;  // kiểm tra xem job đã ứng tuyển hay chưa
    BottomSheetDialog bottomSheetDialogAnswer;
    Button btnSchedule; // button lịch hẹn khi có thì xuất hiện
    Schedule schedule; // status : 0 chưa xác nhận, 1 đồng ý , 2 từ chối , 3 dời lịch pv
    int ap_id = 0; // id application của job đó với user hiện tại;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_job);
        anhxa();
        actionBar();
        getInfo();
        eventButton();
        if(MainActivity.login == 1){
            checkApplyOrNot();
            getSchedule();
        }






    }

    private void eventButton() {
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogAnswerSchedule();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.login == 0){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_LOGIN);
                }else {
                    showDialog();
                }
            }
        });
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.login == 0){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_LOGIN);
                }else {
                    Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                    intent.putExtra("kind", 1); // 1: từ detailjob qua, 2: từ chat qua
                    intent.putExtra("idrecruiter", job.getId_recruiter());
                    startActivity(intent);
                }
            }
        });
    }


    private void getSchedule() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlGetScheduleCandidate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length() > 0){
                                btnSchedule.setVisibility(View.VISIBLE);
                            }
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                schedule = new Schedule(
                                        object.getInt("id"),
                                        object.getInt("id_recruiter"),
                                        object.getInt("id_job"),
                                        object.getString("job_name"),
                                        object.getInt("id_user"),
                                        object.getString("username"),
                                        object.getInt("type"),
                                        object.getString("date"),
                                        object.getString("start_hour"),
                                        object.getString("end_hour"),
                                        object.getString("note"),
                                        object.getString("note_candidate"),
                                        object.getInt("status")
                                );

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
                map.put("iduser", String.valueOf(MainActivity.iduser));
                map.put("idjob", String.valueOf(job_id));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void dialogAnswerSchedule() throws ParseException {
        bottomSheetDialogAnswer = new BottomSheetDialog(DetailJobActivity.this, R.style.BottomSheetTheme);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_answer, (ViewGroup) findViewById(R.id.bottom_sheet));
        TextView txtSchedule = (TextView) view.findViewById(R.id.name);
        final EditText editNote = (EditText) view.findViewById(R.id.editnote);
        Button btnDongY = (Button) view.findViewById(R.id.buttondongy);
        Button btnTuChoi = (Button) view.findViewById(R.id.buttontuchoi);
        Button btnLuiLich = (Button) view.findViewById(R.id.buttonluilich);
        TextView txtDate = (TextView) view.findViewById(R.id.txtdate);
        if(schedule.getType() == 1){
            txtSchedule.setText("Hẹn phỏng vấn");
        }else {
            txtSchedule.setText("Hẹn làm việc");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        date = simpleDateFormat.parse(schedule.getDate());
        SimpleDateFormat fmtOut  = new SimpleDateFormat("dd/mm/yyyy");
        String start = schedule.getStart_hour();
        String end = schedule.getEnd_hour();
        SimpleDateFormat fmtTime = new SimpleDateFormat("HH:mm:ss");
        Date time1 = null;
        Date time2 = null;
        try {
            time1 =  fmtTime.parse(start);
            time2 = fmtTime.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");
        txtDate.setText("Ngày: "+ fmtOut.format(date) + ", từ " + formatHour.format(time1) + " đến " + formatHour.format(time2));
        // status : 0 chưa xác nhận, 1 đồng ý , 2 từ chối , 3 dời lịch pv
        btnDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSchedule(schedule.getId(), 1, editNote.getText().toString());
                postNotificationSchedule(1,1);
                bottomSheetDialogAnswer.dismiss();
                btnSchedule.setVisibility(View.GONE);
            }
        });
        btnTuChoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSchedule(schedule.getId(), 2, editNote.getText().toString());
                postNotificationSchedule(1,2);
                bottomSheetDialogAnswer.dismiss();
                btnSchedule.setVisibility(View.GONE);
            }
        });
        btnLuiLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSchedule(schedule.getId(), 3, editNote.getText().toString());
                postNotificationSchedule(1,3);
                bottomSheetDialogAnswer.dismiss();
                btnSchedule.setVisibility(View.GONE);
            }
        });


        bottomSheetDialogAnswer.setContentView(view);


        bottomSheetDialogAnswer.show();

    }
    public void postNotificationSchedule(final int type_user, int status){
        type_notification = "Trả lời lịch hẹn của ứng viên";
        if(status == 1){
            content = "Ứng viên " + MainActivity.username +" đồng ý phỏng vấn";
        }else if(status == 2) {
            content = "Ứng viên " + MainActivity.username +" từ chối phỏng vấn";
        }else {
            content = "Ứng viên " + MainActivity.username +" muốn dời lịch phỏng vấn";
        }
    //    Toast.makeText(getApplicationContext(), ap_id + "", Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlPostNotification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(getApplicationContext(), "Thông báo thành công", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "Thông báo thất bại", Toast.LENGTH_SHORT).show();
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
                map.put("id_application", String.valueOf(ap_id));
                map.put("type_notification", type_notification);
                map.put("content", content);
                map.put("iduser", String.valueOf(job.getId_recruiter()));
                map.put("type_user", String.valueOf(type_user));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }


    public void updateSchedule(final int id_sche, final int status, final String note_candidate){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlUpdateScheduleCandidate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       if(response.equals("success")){
                           Toast.makeText(getApplicationContext(), "Cập nhật schedule thành công", Toast.LENGTH_SHORT).show();
                       }else {
                           Toast.makeText(getApplicationContext(), "Cập nhật schedule thất bại", Toast.LENGTH_SHORT).show();
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
                map.put("id_sche", String.valueOf(id_sche));
                map.put("status", String.valueOf(status));
                map.put("note_candidate", note_candidate);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }


    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    private void checkApplyOrNot() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlCheckApply,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.length() > 4){
                            String test = response.substring(0,5);
                            ap_id = Integer.parseInt(response.substring(5, response.length()));
                         //   Toast.makeText(getApplicationContext(), test, Toast.LENGTH_SHORT).show();
                            if(test.equals("apply")){
                                btnApply.setClickable(false);
                                btnApply.setBackgroundColor(Color.BLACK);
                                btnApply.setTextColor(Color.WHITE);
                                btnApply.setText("Đã ứng tuyển");
                            }else if(test.equals("again")){
                                btnApply.setText("Ứng tuyển lại");
                                checkApplyAgain = 1;
                            }
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
                map.put("job_id", String.valueOf(job_id));
                map.put("user_id", String.valueOf(MainActivity.iduser));
                return map;
            }
        };
        requestQueue.add(stringRequest);

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
                int status = job.getStatus();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = fmt.parse(job.getEnd_date());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(status == 0 && date.after(Calendar.getInstance().getTime())){
                    if(checkApplyAgain == 0){
                        apply();
                    }else {
                        applyAgain();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Nhà tuyển dụng đã dừng tuyển", Toast.LENGTH_SHORT).show();
                }

            }
        });

        alert.show();
    }
    public void applyAgain(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlApplyAgain,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("fail")){
                            Toast.makeText(getApplicationContext(), "Ứng tuyển thành công", Toast.LENGTH_SHORT).show();
                            checkApplyOrNot();
                            int k = response.lastIndexOf('s');
                            id_application = Integer.parseInt(response.substring(k+1, response.length()));
                            content = "Ứng viên " + MainActivity.username + " - " + job.getName();
                            // Toast.makeText(getApplicationContext(), id_application + content , Toast.LENGTH_SHORT).show();
                            postNotification(1);
//                            if(checkApplyAgain == 1){
//                                // remove job trong list đã ứng tuyển
//                                for(int i=0; i < HomeFragment.arrayListDaUngTuyen.size(); i++){
//                                    if(HomeFragment.arrayListDaUngTuyen.get(i).getId() == job_id){
//                                        HomeFragment.arrayListDaUngTuyen.remove(i);
//                                    }
//                                }
//                            }
//
//                            HomeFragment.arrayListDaUngTuyen.add(new Job_Apply(
//                                    job.getId(),
//                                    job.getName(),
//                                    job.getIdcompany(),
//                                    job.getId_recruiter(),
//                                    MainActivity.arrayListCV.get(0).getKey(),
//                                    job.getImg(),
//                                    job.getAddress(),
//                                    job.getIdtype(),
//                                    job.getIdprofession(),
//                                    job.getStart_date(),
//                                    job.getEnd_date(),
//                                    job.getSalary_min(),
//                                    job.getSalary_max(),
//                                    job.getIdarea(),
//                                    job.getExperience(),
//                                    job.getNumber(),
//                                    job.getDescription(),
//                                    job.getRequirement(),
//                                    job.getBenefit(),
//                                    job.getStatus(),
//                                    job.getCompany_name(),
//                                    job.getType_job()
//                            ));
//                            HomeFragment.adapterDaUngTuyen.notifyDataSetChanged();
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
                map.put("cv_id", MainActivity.arrayListCV.get(0).getKey());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void apply(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlApply,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("fail")){
                            Toast.makeText(getApplicationContext(), "Ứng tuyển thành công", Toast.LENGTH_SHORT).show();
                            checkApplyOrNot();
                            int k = response.lastIndexOf('s');
                            id_application = Integer.parseInt(response.substring(k+1, response.length()));
                            content = "Ứng viên " + MainActivity.username + " - " + job.getName();
                            // Toast.makeText(getApplicationContext(), id_application + content , Toast.LENGTH_SHORT).show();
                            postNotification(1);

//                            HomeFragment.arrayListDaUngTuyen.add(new Job_Apply(
//                                    job.getId(),
//                                    job.getName(),
//                                    job.getIdcompany(),
//                                    job.getId_recruiter(),
//                                    MainActivity.arrayListCV.get(0).getKey(),
//                                    job.getImg(),
//                                    job.getAddress(),
//                                    job.getIdtype(),
//                                    job.getIdprofession(),
//                                    job.getStart_date(),
//                                    job.getEnd_date(),
//                                    job.getSalary_min(),
//                                    job.getSalary_max(),
//                                    job.getIdarea(),
//                                    job.getExperience(),
//                                    job.getNumber(),
//                                    job.getDescription(),
//                                    job.getRequirement(),
//                                    job.getBenefit(),
//                                    job.getStatus(),
//                                    job.getCompany_name(),
//                                    job.getType_job()
//                            ));
//                            HomeFragment.adapterDaUngTuyen.notifyDataSetChanged();
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
                map.put("cv_id", MainActivity.arrayListCV.get(0).getKey());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void postNotification(final int type_user) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlPostNotification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(getApplicationContext(), "Thông báo thành công", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "Thông báo thất bại", Toast.LENGTH_SHORT).show();
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
                map.put("type_user", String.valueOf(type_user));
                map.put("type_notification",  type_notification);
                map.put("iduser", String.valueOf(job.getId_recruiter()));
                map.put("content", content);
                map.put("id_application", String.valueOf(id_application));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void showDialog(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_apply);
//        dialog.setCancelable(false);

        txtName = (TextView) dialog.findViewById(R.id.txtname);
        txtPhone = (TextView) dialog.findViewById(R.id.txtphone);
        txtEmail = (TextView) dialog.findViewById(R.id.txtemail);
        txtCV = (TextView) dialog.findViewById(R.id.txtcv);
        btnChange = (Button) dialog.findViewById(R.id.buttonthaydoi);
        btnUse = (Button) dialog.findViewById(R.id.buttonsudung);
        txtName.setText(MainActivity.user.getName());
        txtEmail.setText(MainActivity.user.getEmail());
        txtPhone.setText(MainActivity.user.getPhone() + "");
        if(MainActivity.arrayListCV.size() > 0){
            txtCV.setText(MainActivity.arrayListCV.get(0).getName());
        }else {
            txtCV.setText("Bạn chưa có CV, vui lòng tạo");
            checkCV = 1;
        }
        btnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkCV == 1){
                    Toast.makeText(getApplicationContext(), "Vui lòng tạo CV", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else {
                    showAlert();
                    dialog.dismiss();
                }
            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkCV == 1){
                    Toast.makeText(getApplicationContext(), "Vui lòng tạo CV", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else {
                    Intent intent = new Intent(getApplicationContext(), ChooseCVActivity.class);
                    intent.putExtra("job", job);
                    startActivityForResult(intent, REQUEST_CODE_CV);

                }
            }
        });

        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_LOGIN && resultCode == 123){
            checkApplyOrNot();
            getDataApplied();
            getSchedule();
        }
        if(requestCode == REQUEST_CODE_CV && resultCode == 123){
            checkApplyOrNot();
            dialog.dismiss();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    // dành cho từ notification chuyển qua
    public void getJobInfo(final int job_id){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlJobFromNotification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    //    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject object = jsonArray.getJSONObject(0);
                                job = new Job(
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
                                );
                              //  Toast.makeText(getApplicationContext(), job.getName() + job.getStart_date(), Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


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
                map.put("job_id", String.valueOf(job_id));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void getInfo() {
        kind = getIntent().getIntExtra("kind",0);
        if(kind == 0){
            job = (Job) getIntent().getSerializableExtra("job");
            job_id = job.getId();
            Glide.with(getApplicationContext()).load(job.getImg()).into(anhcongty);
            txttencongviec.setText(job.getName());
            txtcongty.setText(job.getCompany_name());
            String ngaybatdau = job.getStart_date();
            String ngayketthuc = job.getEnd_date();

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = null;
            Date date2 = null;
            try {
                date1 = fmt.parse(ngaybatdau);
                date2 = fmt.parse(ngayketthuc);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
            txthannop.setText(fmtOut.format(date2));
        }else {
            loading();
            job_id = getIntent().getIntExtra("job_id",0);
            getJobInfo(job_id);
            final int ap_status = getIntent().getIntExtra("ap_status" ,0);
            final String ap_note = getIntent().getStringExtra("ap_note");
            handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getApplicationContext()).load(job.getImg()).into(anhcongty);
                    txttencongviec.setText(job.getName());
                    txtcongty.setText(job.getCompany_name());
                    String ngaybatdau = job.getStart_date();
                    String ngayketthuc = job.getEnd_date();
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = null;
                    Date date2 = null;
                    try {
                        date1 = fmt.parse(ngaybatdau);
                        date2 = fmt.parse(ngayketthuc);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
                    txthannop.setText(fmtOut.format(date2));
                    progressDialog.dismiss();
                    if(ap_status == 2 ){
                        Toast.makeText(getApplicationContext(), "Lý do bạn không đạt yêu cầu là : " + ap_note, Toast.LENGTH_SHORT).show();
                    }else if(ap_status == 11) {
                        Toast.makeText(getApplicationContext(), "Lý do bạn không đạt phỏng vấn là : " + ap_note, Toast.LENGTH_SHORT).show();
                    }



                }
            },4000);


        }



    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkApplyAgain = 0;
                finish();
            }
        });
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Chi tiết việc làm");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle("");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });


    }

    private void getDataApplied() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlJobApply,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
//                                HomeFragment.arrayListDaUngTuyen.add(new Job_Apply(
//                                        object.getInt("id"),
//                                        object.getString("name"),
//                                        object.getInt("idcompany"),
//                                        object.getInt("id_recruiter"),
//                                        object.getString("id_cv"),
//                                        object.getString("img"),
//                                        object.getString("area"),
//                                        object.getInt("idtype"),
//                                        object.getInt("idprofession"),
//                                        object.getString("start_date"),
//                                        object.getString("end_date"),
//                                        object.getInt("salary_min"),
//                                        object.getInt("salary_max"),
//                                        object.getInt("idarea"),
//                                        object.getString("experience"),
//                                        object.getInt("number"),
//                                        object.getString("description"),
//                                        object.getString("requirement"),
//                                        object.getString("benefit"),
//                                        object.getInt("status"),
//                                        object.getString("company_name"),
//                                        object.getString("type_job")
//                                ));
//
//                            }
//                            HomeFragment.adapterDaUngTuyen.notifyDataSetChanged();
//                            HomeFragment.layout_daungtuyen.setVisibility(View.VISIBLE);

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
                map.put("iduser", String.valueOf(MainActivity.iduser));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void anhxa() {
        checkApplyAgain = 0;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnSchedule = (Button) findViewById(R.id.buttonschedule);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbarcollapse);
        anhcongty = (ImageView) findViewById(R.id.hinhanh);
        txtcongty = (TextView) findViewById(R.id.tencongty);
        txttencongviec = (TextView) findViewById(R.id.tencongviec);
        txthannop = (TextView) findViewById(R.id.hannop);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        setUpFragment();
        tabLayout.setupWithViewPager(viewPager);
        btnApply = (Button) findViewById(R.id.buttonungtuyen);
        btnChat = (Button) findViewById(R.id.buttonnhantin);



    }

    private void setUpFragment() {
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPageAdapter.addFragment(new InfoFragment(), "Thông tin");
        viewPageAdapter.addFragment(new CompanyFragment(), "Công ty");
        viewPageAdapter.addFragment(new RelevantJobFragment(), "Việc liên quan");
        viewPager.setAdapter(viewPageAdapter);
    }
}
