package com.example.luanvan.ui.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.login.LoginActivity;

public class NotificationsFragment extends Fragment {
    LinearLayout linearLayout1, linearLayout2;
    Button btndangnhap;
    int REQUEST_CODE = 123;

    private NotificationsViewModel notificationsViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        linearLayout1 = (LinearLayout) view.findViewById(R.id.linear);
        linearLayout2 = (LinearLayout) view.findViewById(R.id.linear2);
        btndangnhap = (Button) view.findViewById(R.id.buttondangnhap);
        if(MainActivity.login == 0){
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.GONE);
        }else {
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);
        }

        eventLogin();

        return view;
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
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
