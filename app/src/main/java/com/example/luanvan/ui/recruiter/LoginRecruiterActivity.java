package com.example.luanvan.ui.recruiter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.HashMap;
import java.util.Map;

public class LoginRecruiterActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editEmail, editPass;
    Button btnDangnhap;
    ProgressDialog progressDialog;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_recruiter);
        anhxa();
        actionBar();
        eventButton();




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
                                                    //     Toast.makeText(getActivity(), MainActivity.uid, Toast.LENGTH_SHORT).show();
                                                    editEmail.setText("");
                                                    editPass.setText("");

                                                    MainActivity.login = 1;
                                                    MainActivity.iduser = Integer.parseInt(response);

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
        editEmail = (EditText) findViewById(R.id.editemail);
        editPass = (EditText) findViewById(R.id.editpass);
        btnDangnhap = (Button) findViewById(R.id.buttondangnhap);

    }
}
