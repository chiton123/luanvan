package com.example.luanvan.ui.Adapter.recruit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.JobList;

import java.util.ArrayList;

public class PositionScheduleAdapter extends RecyclerView.Adapter<PositionScheduleAdapter.ItemHolder> {
    Context context;
    ArrayList<JobList> arrayList;
    Activity activity;

    public PositionScheduleAdapter(Context context, ArrayList<JobList> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dong_vi_tri_schedule, null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        JobList job = arrayList.get(position);
        holder.radioButton.setText(job.getName());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public RadioButton radioButton;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = (RadioButton) itemView.findViewById(R.id.radioposition);

        }
    }
}
