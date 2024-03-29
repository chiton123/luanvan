package com.example.luanvan.ui.Adapter.notification_u;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.Glide;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.DetailedJob.DetailJobActivity;
import com.example.luanvan.ui.Model.Notification;
import com.example.luanvan.ui.Model.NotificationAdmin;
import com.example.luanvan.ui.admin.AdminActivity;
import com.example.luanvan.ui.admin.JobReviewActivity;
import com.example.luanvan.ui.home.HomeFragment;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdminNotificationAdapter extends RecyclerView.Adapter<AdminNotificationAdapter.ItemHolder> {
    Context context;
    ArrayList<NotificationAdmin> arrayList;
    Activity activity;

    public AdminNotificationAdapter(Context context, ArrayList<NotificationAdmin> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_thong_bao, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        final NotificationAdmin notification = arrayList.get(position);
        Glide.with(context).load(notification.getImg());
        holder.txtContent.setText(notification.getContent());
        holder.txtNotification.setText(notification.getType_notification());
        String ngaydoc = notification.getDate_read();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        try {
            date1 = fmt.parse(ngaydoc);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
        holder.txtDate.setText("" + fmtOut.format(date1));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notification.getStatus() == 0){
                    updateStatusNotification(position, 1);
                    arrayList.get(position).setStatus(1);
                    notifyDataSetChanged();
                    MainActivity.k_admin--;
                    if(MainActivity.k_admin == 0){
                        AdminActivity.txtNotification.setVisibility(View.GONE);
                    }else {
                        AdminActivity.txtNotification.setText("" + MainActivity.k_admin);
                        AdminActivity.txtNotification.setVisibility(View.VISIBLE);
                    }
                }
                Intent intent = new Intent(activity, JobReviewActivity.class);
                activity.startActivity(intent);
            }
        });

        if(arrayList.get(position).getStatus() == 0){
            holder.layout.setBackgroundResource(R.drawable.backgroud_not_notification);
        }else {
            holder.layout.setBackgroundResource(R.drawable.backgroud_already_notification);
        }
    }

    public void readAll(){
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlUpdateReadAllNotification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            FancyToast.makeText(context, "Bạn đã xem tất cả thông báo", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        }else {
                            FancyToast.makeText(context,"Xem thông báo thất bại", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        FancyToast.makeText(context, error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("kind", String.valueOf(2)); // 1: candidate, recruiter, 2: admin
                map.put("iduser", String.valueOf(0));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void updateStatusNotification(final int position, final int status){
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlUpdateNotificationAdminStatus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                           // Toast.makeText(context, "Đã xem", Toast.LENGTH_SHORT).show();
                        }else {
                          //  Toast.makeText(context, "Thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        FancyToast.makeText(context, error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("n_id", String.valueOf(arrayList.get(position).getId()));
                map.put("status", String.valueOf(status));
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
        public ImageView img;
        public TextView txtNotification, txtContent, txtDate;
        public RelativeLayout layout;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            txtNotification = (TextView) itemView.findViewById(R.id.txtnotification);
            txtContent = (TextView) itemView.findViewById(R.id.txtcontent);
            txtDate = (TextView) itemView.findViewById(R.id.txtdate);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout);
        }
    }

}
