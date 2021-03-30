package com.example.luanvan.ui.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.luanvan.ui.Adapter.chat_a.MessageAdapter;
import com.example.luanvan.ui.Model.Chat;
import com.example.luanvan.ui.Model.UserF;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    CircleImageView img;
    TextView txtUsername;
    EditText editMessage;
    ImageButton btnSend;
    int id_recruiter = 0;
    String idrecruiterFirebase = "";
    DatabaseReference reference;
    List<Chat> mChat;
    MessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        anhxa();
        actionBar();


    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        id_recruiter = getIntent().getIntExtra("iduser", 0);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtUsername = (TextView) findViewById(R.id.txtusername);
        img = (CircleImageView) findViewById(R.id.img);
        editMessage = (EditText) findViewById(R.id.edittextmessage);
        btnSend = (ImageButton) findViewById(R.id.buttonsend);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        getIDFirebase();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void getIDFirebase() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlGetIdUserFirebase,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        if(!response.equals("fail")){
                            idrecruiterFirebase = response.toString();
                            getInfo();
                        }else {
                            Toast.makeText(getApplicationContext(), "Lấy Id không được", Toast.LENGTH_SHORT).show();
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
                map.put("idrecruiter", String.valueOf(id_recruiter));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void getInfo(){
        MainActivity.mUserData.child(idrecruiterFirebase).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserF userF = snapshot.getValue(UserF.class);
                txtUsername.setText(userF.getUsername());
                if(userF.getImageURL().equals("default")){
                    img.setImageResource(R.drawable.userchat);
                }else {
                    Glide.with(getApplicationContext()).load(userF.getImageURL()).into(img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void readMessage(){
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        })
    }
}
