package com.example.luanvan.ui.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.chat_a.UserAdapter;
import com.example.luanvan.ui.Model.Chat;
import com.example.luanvan.ui.Model.UserF;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class UserListActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    UserAdapter adapter;
    ArrayList<UserF> arrayList;
    DatabaseReference reference;
    ArrayList<String> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        anhxa();
        actionBar();


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
        userList = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        arrayList = new ArrayList<>();
        adapter = new UserAdapter(UserListActivity.this, arrayList);
        recyclerView.setAdapter(adapter);
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot x : snapshot.getChildren()){
                    Chat chat = x.getValue(Chat.class);
                    if(chat.getSender().equals(MainActivity.uid)){
                        if(userList.size() != 0){
                            int number = 0;
                            for(int i=0; i < userList.size(); i++){
                                if(userList.get(i).equals(chat.getReceiver())){
                                    number++;
                                }
                            }
                            if(number == 0){
                                userList.add(chat.getReceiver());
                            }
                        }else {
                            userList.add(chat.getSender());
                        }

                    }
                    if(chat.getReceiver().equals(MainActivity.uid)){
                        if(userList.size() != 0){
                            int number = 0;
                            for(int i=0; i < userList.size(); i++){
                                if(userList.get(i).equals(chat.getSender())){
                                    number++;
                                }
                            }
                            if(number == 0){
                                userList.add(chat.getSender());
                            }
                        }else {
                            userList.add(chat.getSender());
                        }

                    }

                }
//                for(int i=0; i < userList.size(); i++){
//                    Toast.makeText(getApplicationContext(), userList.get(i), Toast.LENGTH_SHORT).show();
//                }
                readChat();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void readChat(){
        arrayList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();

                for(DataSnapshot x : snapshot.getChildren()){
                    UserF userF = x.getValue(UserF.class);

                    for(String id: userList){
                        if(userF.getId().equals(id) && !userF.getId().equals(MainActivity.uid)){
                            if(arrayList.size() != 0){
                                try {
                                    for(UserF userF1 : arrayList){
                                        if(!userF.getId().equals(userF1.getId())){
                                            arrayList.add(userF);
                                        }
                                    }
                                }catch (ConcurrentModificationException e){
                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                arrayList.add(userF);
                            }
                        }
                    }
                }
                adapter = new UserAdapter(getApplicationContext(), arrayList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
