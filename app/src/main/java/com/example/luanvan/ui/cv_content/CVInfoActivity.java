package com.example.luanvan.ui.cv_content;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;

public class CVInfoActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editname, editposition, editphone, editemail, editaddress, editgender, editbirthday;
    Button btnluu, btnhuy;
    TextView txtname, txtposition, txtphone, txtemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_v_info);
        anhxa();
        actionBar();
        eventLuu();
        getInfo();


    }

    private void getInfo() {
        editname.setText(MainActivity.username);
        editposition.setText(MainActivity.position);
        editemail.setText(MainActivity.user.getEmail());
        editphone.setText(MainActivity.user.getPhone() + "");
        editaddress.setText(MainActivity.user.getAddress());
        editbirthday.setText(MainActivity.user.getBirthday());
        if(MainActivity.user.getGender() == 0){
            editgender.setText("Nam");
        }else {
            editgender.setText("Nữ");
        }

    }




    private void eventLuu() {
        btnluu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
        btnhuy = (Button) findViewById(R.id.buttonhuy);
        btnluu = (Button) findViewById(R.id.buttonluu);
        editname = (EditText) findViewById(R.id.name);
        editaddress = (EditText) findViewById(R.id.address);
        editbirthday = (EditText) findViewById(R.id.birthday);
        editemail = (EditText) findViewById(R.id.email);
        editgender = (EditText) findViewById(R.id.gender);
        editposition = (EditText) findViewById(R.id.position);
        editphone = (EditText) findViewById(R.id.phone);
        txtname = (TextView) findViewById(R.id.txtname);
        txtemail = (TextView) findViewById(R.id.txtemail);
        txtphone = (TextView) findViewById(R.id.txtphone);
        txtposition = (TextView) findViewById(R.id.txtposition);

    }
}
