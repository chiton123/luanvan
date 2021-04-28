package com.example.luanvan.ui.Adapter.schedule_a;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
import com.example.luanvan.ui.DetailedJob.DetailJobActivity;
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
    BottomSheetDialog bottomSheetDialogAnswer;
    int REQUEST_CODE = 123;
    String type_notification = "";
    String content = "";
    int ap_id = 0;
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
        holder.txtCompany.setText(schedule.getCompany_name());
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogAnswerSchedule(position);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
        bottomSheetDialogAnswer.dismiss();
    }
    public void dialogAnswerSchedule(final int position) throws ParseException {
        bottomSheetDialogAnswer = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        final View view = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet_answer, (ViewGroup)activity.findViewById(R.id.bottom_sheet));
        TextView txtSchedule = (TextView) view.findViewById(R.id.name);
        final EditText editNote = (EditText) view.findViewById(R.id.editnote);
        Button btnDongY = (Button) view.findViewById(R.id.buttondongy);
        Button btnTuChoi = (Button) view.findViewById(R.id.buttontuchoi);
        Button btnLuiLich = (Button) view.findViewById(R.id.buttonluilich);
        TextView txtDate = (TextView) view.findViewById(R.id.txtdate);
        if(arrayList.get(position).getType() == 1){
            txtSchedule.setText("Hẹn phỏng vấn");
        }else {
            txtSchedule.setText("Hẹn làm việc");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        date = simpleDateFormat.parse(arrayList.get(position).getDate());
        SimpleDateFormat fmtOut  = new SimpleDateFormat("dd/mm/yyyy");
        String start = arrayList.get(position).getStart_hour();
        String end = arrayList.get(position).getEnd_hour();
        SimpleDateFormat fmtTime = new SimpleDateFormat("HH:mm:ss");
        Date time1 = null;
        Date time2 = null;
        try {
            time1 =  fmtTime.parse(start);
            time2 = fmtTime.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");
        txtDate.setText("Ngày: "+ fmtOut.format(date) + ", từ " + formatHour.format(time1) + " đến " + formatHour.format(time2));
        // status : 0 chưa xác nhận, 1 đồng ý , 2 từ chối , 3 dời lịch pv
        btnDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSchedule(arrayList.get(position).getId(), 1, editNote.getText().toString());
                getApId(position, 1,1);
                arrayList.get(position).setStatus(1);
                arrayList.get(position).setNote_candidate(editNote.getText().toString());
                bottomSheetDialogAnswer.dismiss();
                notifyDataSetChanged();
            }
        });
        btnTuChoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSchedule(arrayList.get(position).getId(), 2, editNote.getText().toString());
                getApId(position, 1,2);
                arrayList.get(position).setStatus(2);
                arrayList.get(position).setNote_candidate(editNote.getText().toString());
                bottomSheetDialogAnswer.dismiss();
                notifyDataSetChanged();
            }
        });
        btnLuiLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSchedule(arrayList.get(position).getId(), 3, editNote.getText().toString());
                getApId(position, 1, 3);
                arrayList.get(position).setStatus(3);
                arrayList.get(position).setNote_candidate(editNote.getText().toString());
                bottomSheetDialogAnswer.dismiss();
                notifyDataSetChanged();
            }
        });


        bottomSheetDialogAnswer.setContentView(view);


        bottomSheetDialogAnswer.show();

    }


    // Để post notification lên
    private void getApId(final int position, final int type_user, final int status) {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlCheckApply,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.length() > 4){
                            String test = response.substring(0,5);
                            ap_id = Integer.parseInt(response.substring(5, response.length()));
                            postNotificationSchedule(type_user, status, position);

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
                map.put("job_id", String.valueOf(arrayList.get(position).getId_job()));
                map.put("user_id", String.valueOf(MainActivity.iduser));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(activity.getString(R.string.NEWS_CHANNEL_ID), activity.getString(R.string.CHANNEL_NEWS),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(activity.getString(R.string.CHANNEL_DESCRIPTION));
            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

        }

    }
    public void phoneNotification(){
        Intent intent = new Intent(activity, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity,0, intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, activity.getString(R.string.NEWS_CHANNEL_ID))
                .setSmallIcon(R.drawable.topcv)
                .setContentTitle(type_notification)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(activity);
        notificationManagerCompat.notify(activity.getResources().getInteger(R.integer.config_navAnimTime), builder.build());

    }
    public void postNotificationSchedule(final int type_user, int status, final int position){
        createNotificationChannel();
        type_notification = "Trả lời lịch hẹn của ứng viên";
        if(status == 1){
            content = "Ứng viên " + MainActivity.username +" đồng ý phỏng vấn";
        }else if(status == 2) {
            content = "Ứng viên " + MainActivity.username +" từ chối phỏng vấn";
        }else {
            content = "Ứng viên " + MainActivity.username +" muốn dời lịch phỏng vấn";
        }
        //    Toast.makeText(getApplicationContext(), ap_id + "", Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlPostNotification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(context, "Thông báo thành công", Toast.LENGTH_SHORT).show();
                            phoneNotification();
                        }else {
                            Toast.makeText(context, "Thông báo thất bại", Toast.LENGTH_SHORT).show();
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
                map.put("id_application", String.valueOf(ap_id));
                map.put("type_notification", type_notification);
                map.put("content", content);
                map.put("iduser", String.valueOf(arrayList.get(position).getId_recruiter()));
                map.put("type_user", String.valueOf(type_user));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
    public void updateSchedule(final int id_sche, final int status, final String note_candidate){
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlUpdateScheduleCandidate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(context, "Cập nhật schedule thành công", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Cập nhật schedule thất bại", Toast.LENGTH_SHORT).show();
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
                map.put("id_sche", String.valueOf(id_sche));
                map.put("status", String.valueOf(status));
                map.put("note_candidate", note_candidate);
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
