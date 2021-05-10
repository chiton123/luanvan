package com.example.luanvan.ui.recruiter.updateInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateRecruiterActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnUpdate, btnCancel;
    EditText editName, editPhone, editEmail, editAddress, editIntroduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recruiter);
        anhxa();
        actionBar();
        getInfo();
        eventUpdate();


    }

    private void getInfo() {
        editName.setText(MainActivity.recruiter.getName());
        editEmail.setText(MainActivity.recruiter.getEmail());
        editPhone.setText("0"+MainActivity.recruiter.getPhone() + "");
        editIntroduction.setText(MainActivity.recruiter.getIntroduction());
        editAddress.setText(MainActivity.recruiter.getAddress());

    }

    private void eventUpdate() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editName.getText().toString();
                final String email = editEmail.getText().toString();
                final String phone = editPhone.getText().toString();
                final String address = editAddress.getText().toString();
                final String intro = editIntroduction.getText().toString();
                if(!isEmailValid(email)){
                    Toast.makeText(getApplicationContext(), "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
                }else if(name.equals("")){
                    Toast.makeText(getApplicationContext(), "Chưa điền họ tên", Toast.LENGTH_SHORT).show();
                }else if(email.equals("")){
                    Toast.makeText(getApplicationContext(), "Chưa điền email", Toast.LENGTH_SHORT).show();
                }else if(phone.equals("")){
                    Toast.makeText(getApplicationContext(), "Chưa điền số điện thoại", Toast.LENGTH_SHORT).show();
                }else if(address.equals("")){
                    Toast.makeText(getApplicationContext(), "Chưa điền địa chỉ", Toast.LENGTH_SHORT).show();
                }else if(intro.equals("")){
                    Toast.makeText(getApplicationContext(), "Chưa điền giới thiệu", Toast.LENGTH_SHORT).show();
                }else if(phone.length() > 10){
                    Toast.makeText(getApplicationContext(), "Số điện thoại phải ít hơn 11 số", Toast.LENGTH_SHORT).show();
                }

                else {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlUpdateRecruiter,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("success")){
                                        Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                        MainActivity.recruiter.setEmail(email);
                                        MainActivity.recruiter.setAddress(address);
                                        MainActivity.recruiter.setPhone(Integer.parseInt(phone));
                                        MainActivity.recruiter.setName(name);
                                        MainActivity.recruiter.setIntroduction(intro);
                                        finish();
                                    }else{
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
                            map.put("idrecruiter", String.valueOf(MainActivity.iduser));
                            map.put("name", name);
                            map.put("address", address);
                            map.put("email", email);
                            map.put("phone", phone);
                            map.put("introduction", intro);
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);

                }

            }
        });
    }

    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
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
        btnCancel = (Button) findViewById(R.id.buttonhuy);
        btnUpdate = (Button) findViewById(R.id.buttoncapnhat);
        editName = (EditText) findViewById(R.id.editname);
        editAddress = (EditText) findViewById(R.id.editaddress);
        editEmail = (EditText) findViewById(R.id.editemail);
        editIntroduction = (EditText) findViewById(R.id.editintroduction);
        editPhone = (EditText) findViewById(R.id.editphone);
    }
}
