package com.example.luanvan.ui.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.ProfileAdapter;
import com.example.luanvan.ui.Model.Profile;
import com.example.luanvan.ui.login.LoginActivity;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {
    LinearLayout linearLayout1, linearLayout2;
    Button btndangnhap;
    int REQUEST_CODE = 123;
    int REQUEST_CODE2 = 234;
    ImageView edit;
    TextView name, positon, company_name;
    RecyclerView recyclerView;
    ProfileAdapter profileAdapter;
    ArrayList<Profile> arrayList;

    private NotificationsViewModel notificationsViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        linearLayout1 = (LinearLayout) view.findViewById(R.id.linear);
        linearLayout2 = (LinearLayout) view.findViewById(R.id.linear2);
        btndangnhap = (Button) view.findViewById(R.id.buttondangnhap);
        edit = (ImageView) view.findViewById(R.id.edit);
        name = (TextView) view.findViewById(R.id.name);
        positon = (TextView) view.findViewById(R.id.position);
        company_name = (TextView) view.findViewById(R.id.company);
        arrayList = new ArrayList<>();
        getProfile();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        profileAdapter = new ProfileAdapter(getActivity(), arrayList, getActivity());
        recyclerView.setAdapter(profileAdapter);


        if(MainActivity.login == 0){
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.GONE);
        }else {
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);
        }

        eventLogin();
        eventUpdateInfo();
        getInfo();

        return view;
    }



    private void getProfile() {
        arrayList.add(new Profile(0, "Cập nhật thông tin học vấn", R.drawable.study ));
        arrayList.add(new Profile(1, "Cập nhật thông tin kinh nghiệm", R.drawable.experience));
        arrayList.add(new Profile(2, "Cập nhật thông tin kỹ năng", R.drawable.skill));

    }

    private void getInfo() {
        if(MainActivity.login == 1){
           // Toast.makeText(getActivity(), MainActivity.user.getName() + "username", Toast.LENGTH_SHORT).show();
            try {
                name.setText(MainActivity.user.getName());
                if(MainActivity.position.equals("")){

                }else {
                    positon.setText(MainActivity.user.getPosition());
                }
            }catch (NullPointerException e){
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }

        }


    }

    private void eventUpdateInfo() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                startActivityForResult(intent, REQUEST_CODE2);
            }
        });

    }

    private void eventLogin() {
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == 123){
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);
            getInfo();
        }
        if(requestCode == REQUEST_CODE2 && resultCode == 234){
            getInfo();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
