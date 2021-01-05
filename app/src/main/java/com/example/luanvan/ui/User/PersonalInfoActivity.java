package com.example.luanvan.ui.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonalInfoActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editname, editposition, editbirthday, editaddress, editemail, editphone, editmota;
    RadioButton btnNam, btnNu;
    Button btnHuy, btnCapNhat;
    String date_post = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        anhxa();
        actionBar();
        eventUpdate();
        eventPickDate();
    }
    public void showDate() throws ParseException {
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


                if(calendar.getTime().after(today1)){
                    Toast.makeText(getApplicationContext(), "Phải lớn nhỏ ngày hiện tại", Toast.LENGTH_SHORT).show();
                }else {
                    editbirthday.setText(dateFormat.format(calendar.getTime()));
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = fmt.parse(fmt.format(calendar.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    date_post = fmt.format(date);
                }

            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }
    private void eventPickDate() {
        editbirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showDate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getInfo() {
        if(MainActivity.login == 1){
            editname.setText(MainActivity.user.getName());
            editemail.setText(MainActivity.user.getEmail());
            String ngay = MainActivity.user.getBirthday();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = fmt.parse(ngay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
            editbirthday.setText(fmtOut.format(date));
            date_post = fmt.format(date);
            editposition.setText(MainActivity.user.getPosition());
            editphone.setText(MainActivity.user.getPhone() + "");
            editmota.setText(MainActivity.user.getIntroduction());
            editaddress.setText(MainActivity.user.getAddress());
            if(MainActivity.user.getGender() == 0 ){
                btnNam.setChecked(true);
            }else {
                btnNu.setChecked(true);
            }

        }

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
    private void eventUpdate() {
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editname.getText().equals("") || editposition.getText().equals("") || editbirthday.getText().equals("") || editaddress.getText().equals("")
                || editemail.getText().equals("") || editphone.getText().equals("") || editmota.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else if(!isEmailValid(editemail.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Sai định dạng email", Toast.LENGTH_SHORT).show();
                }else if(editphone.getText().length() > 10){
                    Toast.makeText(getApplicationContext(), "Không đúng định dạng số điện thoại", Toast.LENGTH_SHORT).show();
                } else
                {
                    final String name = editname.getText().toString();
                    final String position = editposition.getText().toString();
                //    final String birthday = editbirthday.getText().toString();
                    final String address = editaddress.getText().toString();
                    final String email = editemail.getText().toString();
                    final String phone = editphone.getText().toString();
                    final String mota = editmota.getText().toString();
                    String gender = "";
                    if(btnNam.isChecked()){
                        gender += 0;
                    }else {
                        gender += 1;
                    }
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    final String finalGender = gender;
                    final String finalGender1 = gender;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlupdateuser,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("success")){
                                        Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                        MainActivity.user.setAddress(address);
                                        MainActivity.user.setBirthday(date_post);
                                        MainActivity.user.setEmail(email);
                                        MainActivity.user.setGender(Integer.parseInt(finalGender1));
                                        MainActivity.user.setIntroduction(mota);
                                        MainActivity.user.setName(name);
                                        MainActivity.user.setPosition(position);
                                        MainActivity.user.setPhone(Integer.parseInt(phone));
                                        Intent intent = new Intent();
                                        setResult(234);
                                        finish();
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
                            map.put("iduser", String.valueOf(MainActivity.user.getId()));
                            map.put("name", name);
                            map.put("email", email);
                            map.put("gender", finalGender);
                            map.put("address", address);
                            map.put("phone", phone);
                            map.put("position", position);
                            map.put("birthday", date_post);
                            map.put("introduction", mota);
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
        editaddress = (EditText) findViewById(R.id.editaddress);
        editname = (EditText) findViewById(R.id.editname);
        editmota = (EditText) findViewById(R.id.editmota);
        editphone = (EditText) findViewById(R.id.editphone);
        editposition = (EditText) findViewById(R.id.editposition);
        editemail = (EditText) findViewById(R.id.editemail);
        editbirthday = (EditText) findViewById(R.id.editbirthday);
        btnNam = (RadioButton) findViewById(R.id.radionam);
        btnNu = (RadioButton) findViewById(R.id.radionu);
        btnHuy = (Button) findViewById(R.id.buttonhuy);
        btnCapNhat = (Button) findViewById(R.id.buttoncapnhat);
        getInfo();



    }
}
