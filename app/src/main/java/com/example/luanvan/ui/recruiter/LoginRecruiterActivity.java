package com.example.luanvan.ui.recruiter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.luanvan.ui.User.ResetPasswordActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CVManageActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CVManagementActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginRecruiterActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editEmail, editPass;
    Button btnDangnhap;
    ProgressDialog progressDialog;
    Handler handler = new Handler();
    LinearLayout layoutLogin;
    int REQUEST_CODE = 123;
    TextView txt_forgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_recruiter);
        anhxa();
        actionBar();
        eventButton();
        eventForgotPassword();

    }
    private void eventForgotPassword() {
        txt_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    public void logOut(){
        MainActivity.login_recruiter = 0;
        MainActivity.mAuth.signOut();
        MainActivity.uid = "";
        MainActivity.iduser = 0;
        MainActivity.arrayListNotification.clear();
        MainActivity.k = 0;
        MainActivity.idcompany = 0;
        MainActivity.company_name = "";
        RecruiterActivity.arrayListJobList.clear();
        RecruiterActivity.arrayListNotificationRecruiter.clear();
        CVManageActivity.arrayListInterView.clear();
        CVManageActivity.arrayListGoToWork.clear();
        CVManageActivity.arrayListCVFilter.clear();
        CVManageActivity.arrayListAll.clear();
        CVManagementActivity.position_job_list = 0;
        MainActivity.email_recruiter = "";
        MainActivity.password = "";
        MainActivity.k_chat = 0;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            logOut();
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    private void eventButton() {
        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editPass.getText().equals("") || editEmail.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    loading();
                    final String email = editEmail.getText().toString();
                    final String pass = editPass.getText().toString();
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlLoginRecruiter,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(final String response) {
                                    if(!response.equals("fail")){
                                        MainActivity.mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful()){

                                                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                                    MainActivity.email_recruiter = email;
                                                    MainActivity.password = pass;
                                                    MainActivity.mUser = MainActivity.mAuth.getCurrentUser();

                                                    MainActivity.uid = MainActivity.mUser.getUid();

                                                    editEmail.setText("");
                                                    editPass.setText("");

                                                    MainActivity.login_recruiter = 1;
                                                    MainActivity.iduser = Integer.parseInt(response);
                                                    getCompanyInfo();
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressDialog.dismiss();
                                                            Intent intent = new Intent(getApplicationContext(), RecruiterActivity.class);
                                                            startActivityForResult(intent, REQUEST_CODE);
                                                        }
                                                    },2000);
                                                }else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Sai tên hoặc mật khẩu", Toast.LENGTH_SHORT).show();
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
                            map.put("email", email);
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);

                }
            }
        });

    }
    public void getCompanyInfo(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlCompanyInfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject object = jsonArray.getJSONObject(0);
                                MainActivity.idcompany = object.getInt("id");
                                MainActivity.company_name = object.getString("name");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
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
                map.put("id_recruiter", String.valueOf(MainActivity.iduser));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void showAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Xác nhận");
        alert.setMessage("Bạn có muốn đăng xuất khỏi nhà tuyển dụng không ?");
        alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logOut();
                finish();
            }
        });
        alert.show();
    }
    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.login_recruiter == 1){
                    showAlert();
                }else {
                    finish();
                }
            }
        });
    }

    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editEmail = (EditText) findViewById(R.id.editemail);
        editPass = (EditText) findViewById(R.id.editpass);
        btnDangnhap = (Button) findViewById(R.id.buttondangnhap);
        layoutLogin = (LinearLayout) findViewById(R.id.lineardangnhap);
        txt_forgotPassword = (TextView) findViewById(R.id.txt_forgotpassword);


    }
}
