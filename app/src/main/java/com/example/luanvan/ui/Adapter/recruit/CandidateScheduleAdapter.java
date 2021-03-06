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
import com.example.luanvan.ui.Model.JobList;
import com.example.luanvan.ui.Model.User;
import com.example.luanvan.ui.Model.UserApplicant;
import com.example.luanvan.ui.schedule.CreateScheduleActivity;

import java.util.ArrayList;

public class CandidateScheduleAdapter extends RecyclerView.Adapter<CandidateScheduleAdapter.ItemHolder> {
    Context context;
    ArrayList<UserApplicant> arrayList;
    Activity activity;
    int selectItem = 0;
    public CandidateScheduleAdapter(Context context, ArrayList<UserApplicant> arrayList, Activity activity) {
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
        UserApplicant user = arrayList.get(position);
        holder.radioButton.setText(user.getName());
        holder.radioButton.setChecked(position == selectItem);
        if(holder.radioButton.isChecked()){
            CreateScheduleActivity.user_id = arrayList.get(position).getId();
            CreateScheduleActivity.username = arrayList.get(position).getName();
            CreateScheduleActivity.email = arrayList.get(position).getEmail();
            CreateScheduleActivity.ap_id = arrayList.get(position).getAp_id();
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
