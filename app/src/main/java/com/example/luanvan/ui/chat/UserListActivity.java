package com.example.luanvan.ui.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
    SearchView searchView;
    LinearLayout layout, layout_nothing;
    Handler handler;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
      //  loading();
        anhxa();
        actionBar();
        search();


    }
    private void search() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    void loading(){
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    void checkNothing(){
        if(arrayList.size() == 0){
            layout.setVisibility(View.GONE);
            layout_nothing.setVisibility(View.VISIBLE);
        }else {
            layout.setVisibility(View.VISIBLE);
            layout_nothing.setVisibility(View.GONE);
        }
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
        searchView = (SearchView) findViewById(R.id.searchView);
        userList = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserListActivity.this, LinearLayoutManager.VERTICAL, false));
        arrayList = new ArrayList<>();
        adapter = new UserAdapter(UserListActivity.this, arrayList, UserListActivity.this);
        recyclerView.setAdapter(adapter);
        layout = (LinearLayout) findViewById(R.id.layout);
        layout_nothing = (LinearLayout) findViewById(R.id.layout_nothing);
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
                            userList.add(chat.getReceiver());
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
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkNothing();
            }
        },2000);


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
                                 //   Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                arrayList.add(userF);
                            }
                        }
                    }
                }
                adapter = new UserAdapter(getApplicationContext(), arrayList, UserListActivity.this);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
