package com.example.luanvan.ui.Adapter.recruit;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CVFilterAdapter extends RecyclerView.Adapter<CVFilterAdapter.ItemHolder> {
    Context context;


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txtName, txtEmail, txtStatus;
        public Button btnView;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);


        }
    }
}
