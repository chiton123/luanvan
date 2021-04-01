package com.example.luanvan.ui.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editEmail, editCurrentPass, editNewPass1, editNewPass2;
    Button btnCancel, btnUpdate;
    FirebaseUser firebaseUser;
    AuthCredential authCredential;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        anhxa();
        actionBar();
        eventUpdate();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    }

    private void eventUpdate() {
        if(MainActivity.login == 1){
            editEmail.setText(MainActivity.user.getEmail());
        }else {
            editEmail.setText(MainActivity.email_recruiter);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPass = editCurrentPass.getText().toString();
                final String newPass1 = editNewPass1.getText().toString();
                String newPass2 = editNewPass2.getText().toString();
                if(currentPass.equals("")){
                    Toast.makeText(getApplicationContext(), "Nhập mật khẩu hiện tại", Toast.LENGTH_SHORT).show();
                }else if(newPass1.equals("")){
                    Toast.makeText(getApplicationContext(), "Nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
                }else if(newPass2.equals("")){
                    Toast.makeText(getApplicationContext(), "Nhập lại mật khẩu mới", Toast.LENGTH_SHORT).show();
                }else if(!currentPass.equals(MainActivity.password)){
                    Toast.makeText(getApplicationContext(), "Nhập khẩu mật hiện tại không đúng", Toast.LENGTH_SHORT).show();
                }else if(newPass1.length() < 6){
                    Toast.makeText(getApplicationContext(), "Nhập mật mới phải dài ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                }else if(!newPass1.equals(newPass2)){
                    Toast.makeText(getApplicationContext(), "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                }else {
                    if(MainActivity.login == 1){
                        authCredential = EmailAuthProvider.getCredential(MainActivity.user.getEmail(), currentPass);
                    }else {
                        authCredential = EmailAuthProvider.getCredential(MainActivity.email_recruiter, currentPass);
                    }

                    firebaseUser.reauthenticate(authCredential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        firebaseUser.updatePassword(newPass1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }else {
                                                    Toast.makeText(getApplicationContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                    }else {
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
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
        editCurrentPass = (EditText) findViewById(R.id.editcurrentpassword);
        editNewPass1 = (EditText) findViewById(R.id.editnewpassword1);
        editNewPass2 = (EditText) findViewById(R.id.editnewpassword2);
        btnCancel = (Button) findViewById(R.id.buttonhuy);
        btnUpdate = (Button) findViewById(R.id.buttoncapnhat);

    }
}
