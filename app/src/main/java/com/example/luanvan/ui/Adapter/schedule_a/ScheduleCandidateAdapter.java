package com.example.luanvan.ui.Adapter.schedule_a;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Schedule;
import com.example.luanvan.ui.Model.ScheduleCandidate;
import com.example.luanvan.ui.schedule.CreateScheduleActivity;
import com.example.luanvan.ui.schedule.ScheduleManagementActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ScheduleCandidateAdapter extends RecyclerView.Adapter<ScheduleCandidateAdapter.ItemHolder> {
    Context context;
    ArrayList<ScheduleCandidate> arrayList;
    Activity activity;
    BottomSheetDialog bottomSheetDialog;
    int REQUEST_CODE = 123;
    public ScheduleCandidateAdapter(Context context, ArrayList<ScheduleCandidate> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_schedule_candidate, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        final ScheduleCandidate schedule = arrayList.get(position);
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
        if(!schedule.getNote().equals("")){
            holder.txtNote.setText(schedule.getNote());
            holder.txtNote.setVisibility(View.VISIBLE);
            holder.view_straight.setVisibility(View.VISIBLE);
        }else {
            holder.txtNote.setVisibility(View.GONE);
            holder.view_straight.setVisibility(View.GONE);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
                View view = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet_chinhsua_xoa, (ViewGroup)activity.findViewById(R.id.bottom_sheet));
                Button btnAdjust = (Button) view.findViewById(R.id.buttonadjust);
                Button btnDelete = (Button) view.findViewById(R.id.buttondelete);
                Button btnStop = (Button) view.findViewById(R.id.buttonstop);
                btnStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                btnAdjust.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CreateScheduleActivity.class);
                        intent.putExtra("kind", 2); // kind: 1: create, 2: adjust
                        intent.putExtra("schedule", schedule);
                        intent.putExtra("position", position); // position trên list
                        activity.startActivityForResult(intent, REQUEST_CODE);
                    }
                });

                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();
            }
        });
        if(schedule.getStatus() == 0){
            holder.txtStatusCandidate.setText("Chưa trả lời");
            holder.txtNoteCandidate.setVisibility(View.GONE);
        }else if(schedule.getStatus() == 1){
            holder.txtStatusCandidate.setText("Đã đồng ý");
        }else if(schedule.getStatus() == 2) {
            holder.txtStatusCandidate.setText("Đã từ chối");
        }else {
            holder.txtStatusCandidate.setText("Yêu cầu dời lịch hẹn");
        }
        if(schedule.getNote_candidate().length() > 0){
            holder.txtNoteCandidate.setVisibility(View.VISIBLE);
            holder.txtNoteCandidate.setText("Ghi chú: " + schedule.getNote_candidate());
        }else {
            holder.txtNoteCandidate.setVisibility(View.GONE);
        }

    }
    public void stopBottomSheet(){
        bottomSheetDialog.dismiss();
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txtPosition, txtCompany, txtTypeSchedule, txtTime, txtDay, txtMonth, txtNote, txtStatusCandidate, txtNoteCandidate;
        public ImageView img;
        public LinearLayout layout, layout_candidate;
        public View view_straight;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtPosition = (TextView) itemView.findViewById(R.id.txtposition);
            txtTypeSchedule = (TextView) itemView.findViewById(R.id.txttype);
            txtCompany = (TextView) itemView.findViewById(R.id.txtcompany);
            txtTime = (TextView) itemView.findViewById(R.id.txttime);
            txtDay = (TextView) itemView.findViewById(R.id.txtday);
            txtMonth = (TextView) itemView.findViewById(R.id.txtmonth);
            img = (ImageView) itemView.findViewById(R.id.img);
            txtNote = (TextView) itemView.findViewById(R.id.txtnote);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            view_straight = (View) itemView.findViewById(R.id.view);
            layout_candidate = (LinearLayout) itemView.findViewById(R.id.layout_candidate);
            txtStatusCandidate = (TextView) itemView.findViewById(R.id.txtstatus);
            txtNoteCandidate = (TextView) itemView.findViewById(R.id.txtnotecandidate);

        }
    }
}
