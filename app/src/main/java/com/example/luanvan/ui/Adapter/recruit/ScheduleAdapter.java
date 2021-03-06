package com.example.luanvan.ui.Adapter.recruit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Schedule;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ItemHolder> {
    Context context;
    ArrayList<Schedule> arrayList;
    Activity activity;

    public ScheduleAdapter(Context context, ArrayList<Schedule> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_schedule, null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Schedule schedule = arrayList.get(position);
        if(schedule.getType() == 1){
            holder.txtTypeSchedule.setText("Hẹn phỏng vấn");
            holder.img.setImageResource(R.drawable.call);
        }else {
            holder.txtTypeSchedule.setText("Nhắc lịch đi làm ");
        }
        holder.txtPosition.setText(schedule.getJob_name());
        holder.txtCandidate.setText(schedule.getUsername());
        String start = schedule.getStart_hour();
        String end = schedule.getEnd_hour();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date time1 = null;
        Date time2 = null;
        try {
            time1 =  simpleDateFormat.parse(start);
            time2 = simpleDateFormat.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");
        holder.txtTime.setText(formatHour.format(time1) + " - " + formatHour.format(time2));



        String ngay = schedule.getDate();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatDate.parse(ngay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmOutDay = new SimpleDateFormat("dd");
        SimpleDateFormat fmOutMonth = new SimpleDateFormat("MM");
        holder.txtDay.setText(fmOutDay.format(date)+"");
        holder.txtMonth.setText("T"+ fmOutMonth.format(date));


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txtPosition, txtCandidate, txtTypeSchedule, txtTime, txtDay, txtMonth;
        public ImageView img;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtPosition = (TextView) itemView.findViewById(R.id.txtposition);
            txtTypeSchedule = (TextView) itemView.findViewById(R.id.txttype);
            txtCandidate = (TextView) itemView.findViewById(R.id.txtname);
            txtTime = (TextView) itemView.findViewById(R.id.txttime);
            txtDay = (TextView) itemView.findViewById(R.id.txtday);
            txtMonth = (TextView) itemView.findViewById(R.id.txtmonth);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
