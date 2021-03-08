package com.example.luanvan.ui.Adapter.recruit;

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
import com.example.luanvan.ui.Model.UserApplicant;
import com.example.luanvan.ui.schedule.CreateScheduleActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ItemHolder> {
    Context context;
    ArrayList<Schedule> arrayList;
    Activity activity;
    BottomSheetDialog bottomSheetDialog;
    int REQUEST_CODE = 123;
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
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        final Schedule schedule = arrayList.get(position);
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
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                        alert.setTitle("Xác nhận");
                        alert.setMessage("Bạn có muốn xóa lịch này không ?");
                        alert.setCancelable(false);
                        alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteSchedule(position);
                                bottomSheetDialog.dismiss();
                            }
                        });
                        alert.show();
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


    }
    public void stopBottomSheet(){
        bottomSheetDialog.dismiss();
    }

    public void deleteSchedule(final int position){
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlDeleteSchedule,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            arrayList.remove(position);
                            notifyDataSetChanged();
                        }else {
                            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("sche_id", String.valueOf(arrayList.get(position).getId()));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txtPosition, txtCandidate, txtTypeSchedule, txtTime, txtDay, txtMonth, txtNote;
        public ImageView img;
        public LinearLayout layout;
        public View view_straight;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtPosition = (TextView) itemView.findViewById(R.id.txtposition);
            txtTypeSchedule = (TextView) itemView.findViewById(R.id.txttype);
            txtCandidate = (TextView) itemView.findViewById(R.id.txtname);
            txtTime = (TextView) itemView.findViewById(R.id.txttime);
            txtDay = (TextView) itemView.findViewById(R.id.txtday);
            txtMonth = (TextView) itemView.findViewById(R.id.txtmonth);
            img = (ImageView) itemView.findViewById(R.id.img);
            txtNote = (TextView) itemView.findViewById(R.id.txtnote);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            view_straight = (View) itemView.findViewById(R.id.view);
        }
    }
}
