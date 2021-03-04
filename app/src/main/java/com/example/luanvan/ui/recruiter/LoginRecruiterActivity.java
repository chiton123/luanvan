package com.example.luanvan.ui.recruiter;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.HashMap;
import java.util.Map;

public class LoginRecruiterActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editEmail, editPass;
    Button btnDangnhap, btnLogout;
    ProgressDialog progressDialog;
    Handler handler = new Handler();
    LinearLayout layoutLogin, layoutLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_recruiter);
        anhxa();
        actionBar();
        eventButton();
        eventLogout();
        checkLogin();


    }

    public void logOut(){
        MainActivity.login_recruiter = 0;
        MainActivity.mAuth.signOut();
        MainActivity.uid = "";
        MainActivity.iduser = 0;
        MainActivity.arrayListNotification.clear();
        MainActivity.k = 0;
    }
    private void eventLogout() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
                checkLogin();

            }
        });

    }

    public void checkLogin(){
        if(MainActivity.login_recruiter == 0){
            layoutLogout.setVisibility(View.INVISIBLE);
            layoutLogin.setVisibility(View.VISIBLE);
        }else {
            layoutLogout.setVisibility(View.VISIBLE);
            layoutLogin.setVisibility(View.INVISIBLE);
        }
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
                                                    loading();
                                                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                                    MainActivity.mUser = MainActivity.mAuth.getCurrentUser();

                                                    MainActivity.uid = MainActivity.mUser.getUid();

                                                    editEmail.setText("");
                                                    editPass.setText("");

                                                    MainActivity.login_recruiter = 1;
                                                    MainActivity.iduser = Integer.parseInt(response);
                                                    checkLogin();
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressDialog.dismiss();
                                                            Intent intent = new Intent(getApplicationContext(), RecruiterActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    },2000);
                                                }else {
                                                    Toast.makeText(getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else {
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
                            map.put("pass", pass);
                            map.put("email", email);
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);

                }
            }
        });

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
        layoutLogout = (LinearLayout) findViewById(R.id.lineardangxuat);
        btnLogout = (Button) findViewById(R.id.buttonlogout);


    }
}
