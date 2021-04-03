package com.example.luanvan.ui.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
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
import com.example.luanvan.ui.home.HomeFragment;
import com.example.luanvan.ui.recruiter.RecruiterActivity;
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
    ArrayList<Chat> mChat;
    MessageAdapter messageAdapter;
    int kind = 0;  // 1: từ detailjob qua, 2: từ chat qua
    ValueEventListener seenListener;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        anhxa();
        actionBar();
        eventSend();

    }


    private void eventSend() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editMessage.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Chưa có nội dung", Toast.LENGTH_SHORT).show();
                }else {
                    String message = editMessage.getText().toString();
                    sendMessage(MainActivity.uid, idrecruiterFirebase, message);
                }
                editMessage.setText("");
            }
        });
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
        kind = getIntent().getIntExtra("kind",0); // 1: từ detailjob, applied job qua, 2: từ chat qua
        if(kind == 1){
            id_recruiter = getIntent().getIntExtra("idrecruiter", 0);
            getIDFirebase();
        }else {
            idrecruiterFirebase = getIntent().getStringExtra("idrecruiter");
            getInfo();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtUsername = (TextView) findViewById(R.id.txtusername);
        img = (CircleImageView) findViewById(R.id.img);
        editMessage = (EditText) findViewById(R.id.edittextmessage);
        btnSend = (ImageButton) findViewById(R.id.buttonsend);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mChat = new ArrayList<>();
    }

    private void getIDFirebase() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlGetIdUserFirebase,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                readMessage(MainActivity.uid, idrecruiterFirebase, userF.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                seenMessage(idrecruiterFirebase);
            }
        },1000);


    }

    public void seenMessage(final String userid){

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot x : snapshot.getChildren()){
                    Chat chat = x.getValue(Chat.class);
                    if(chat.getReceiver().equals(MainActivity.uid) && chat.getSender().equals(userid) && !chat.isIsseen()){
                        if(MainActivity.k_chat > 0){
                            MainActivity.k_chat--;
                        //    Toast.makeText(getApplicationContext(), MainActivity.k_chat + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(chat.getReceiver().equals(MainActivity.uid) && chat.getSender().equals(userid) && !chat.isIsseen()){
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        x.getRef().updateChildren(hashMap);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(MainActivity.k_chat > 0){
                    if(MainActivity.login == 1){
                        HomeFragment.txtUnreadMessageNumber.setText(MainActivity.k_chat + "");
                    }else {
                        RecruiterActivity.txtUnreadMessageNumber.setText(MainActivity.k_chat + "");
                    }

                }else {
                    if(MainActivity.login == 1){
                        HomeFragment.txtUnreadMessageNumber.setVisibility(View.GONE);
                    }else {
                        RecruiterActivity.txtUnreadMessageNumber.setVisibility(View.GONE);
                    }

                }
            }
        },200);

    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
    }

    public void sendMessage(String sender, String receiver, String message){

        reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        reference.child("Chats").push().setValue(hashMap);
    }

    public void readMessage(String myid, String userid, final String imageURL){

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                //  Toast.makeText(getApplicationContext(), snapshot.toString(), Toast.LENGTH_SHORT).show();
                for(DataSnapshot x : snapshot.getChildren()){
                    Chat chat = x.getValue(Chat.class);
                    if(chat.getReceiver().equals(MainActivity.uid) && chat.getSender().equals(idrecruiterFirebase) ||
                            chat.getReceiver().equals(idrecruiterFirebase) && chat.getSender().equals(MainActivity.uid)){
                        mChat.add(chat);
                    }

                }
                messageAdapter = new MessageAdapter(getApplicationContext(), mChat, imageURL);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
