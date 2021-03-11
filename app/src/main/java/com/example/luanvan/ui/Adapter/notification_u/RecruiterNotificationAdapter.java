package com.example.luanvan.ui.Adapter.notification_u;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.luanvan.ui.home.HomeFragment;
import com.example.luanvan.ui.recruiter.CVManagement.CVManageActivity;
import com.example.luanvan.ui.recruiter.RecruiterActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecruiterNotificationAdapter extends RecyclerView.Adapter<RecruiterNotificationAdapter.ItemHolder> {
    Context context;
    ArrayList<Notification> arrayList;
    Activity activity;
    int kind;

    // 0: ứng viên xem, 1: nhà tuyển dụng xem

    public RecruiterNotificationAdapter(Context context, ArrayList<Notification> arrayList, Activity activity, int kind) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.kind = kind;
    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_thong_bao, null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        final Notification notification = arrayList.get(position);
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
        holder.txtDate.setText("Ngày ứng tuyển: " + fmtOut.format(date1));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatusNotification(position, 1);
                arrayList.get(position).setStatus(1);
                notifyDataSetChanged();
                MainActivity.k--;
                if(MainActivity.k == 0){
                    RecruiterActivity.txtNotification.setVisibility(View.GONE);
                }else {
                    RecruiterActivity.txtNotification.setText("" + MainActivity.k);
                    RecruiterActivity.txtNotification.setVisibility(View.VISIBLE);
                }
                Intent intent = new Intent(context, CVManageActivity.class);
                intent.putExtra("kind", 1); // Để nó trỏ tới hồ sơ ứng tuyển
                activity.startActivity(intent);
            }
        });
        if(notification.getStatus() == 0){
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
                            Toast.makeText(context, "Bạn đã xem tất cả thông báo", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Xem thông báo thất bại", Toast.LENGTH_SHORT).show();
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
                map.put("iduser", String.valueOf(MainActivity.iduser));
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }


    public void updateStatusNotification(final int position, final int status){
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlUpdateNotificationStatus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                               Toast.makeText(context, "Đã xem", Toast.LENGTH_SHORT).show();
                        }else {
                               Toast.makeText(context, "Thất bại", Toast.LENGTH_SHORT).show();
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
