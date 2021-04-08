package com.example.luanvan.ui.Adapter.recruit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.JobList;
import com.example.luanvan.ui.schedule.CreateScheduleActivity;

import java.util.ArrayList;

public class PositionScheduleAdapter extends RecyclerView.Adapter<PositionScheduleAdapter.ItemHolder> {
    Context context;
    ArrayList<JobList> arrayList;
    Activity activity;
    int selectItem = 0;
    int kind;
    public PositionScheduleAdapter(Context context, ArrayList<JobList> arrayList, Activity activity, int kind) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.kind = kind;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dong_vi_tri_schedule, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        JobList job = arrayList.get(position);
        holder.radioButton.setText(job.getName());
        holder.radioButton.setChecked(position == selectItem);
        if(holder.radioButton.isChecked()){
            CreateScheduleActivity.job_id = arrayList.get(position).getId();
            CreateScheduleActivity.job_name = arrayList.get(position).getName();
            if(kind == 2){
                if(CreateScheduleActivity.job_id != CreateScheduleActivity.job_id_update){
                    CreateScheduleActivity.user_id = 0;
                    CreateScheduleActivity.username = "";
                    CreateScheduleActivity.editCandidate.setText("");
                }
            }

        }

    }
    //https://stackoverflow.com/questions/41251403/using-radio-button-with-recyclerview-in-android
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public RadioButton radioButton;
        public LinearLayout layout;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = (RadioButton) itemView.findViewById(R.id.radioposition);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);

            View.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectItem = getAdapterPosition();
                    notifyItemRangeChanged(0, arrayList.size());
                }
            };

            itemView.setOnClickListener(l);
            radioButton.setOnClickListener(l);
            layout.setOnClickListener(l);

        }
    }
}
