package com.example.luanvan.ui.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.luanvan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.shashank.sony.fancytoastlib.FancyToast;

public class ResetPasswordActivity extends AppCompatActivity {
    Toolbar toolbar;
    MaterialEditText editEmail;
    Button btnReset;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        anhxa();
        actionBar();
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editEmail.getText().toString().equals("")){
                    FancyToast.makeText(getApplicationContext(), "Vui lòng điền email", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else {
                    String email = editEmail.getText().toString();
                    firebaseAuth = FirebaseAuth.getInstance();
                 //   Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FancyToast.makeText(getApplicationContext(), "Vui lòng kiểm tra email", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                finish();
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
        editEmail = (MaterialEditText) findViewById(R.id.send_email);
        btnReset = (Button) findViewById(R.id.buttonreset);

    }
}
