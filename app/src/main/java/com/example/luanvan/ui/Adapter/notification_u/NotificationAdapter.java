package com.example.luanvan.ui.Adapter.notification_u;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Notification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ItemHolder> {
    Context context;
    ArrayList<Notification> arrayList;
    Activity activity;

    public NotificationAdapter(Context context, ArrayList<Notification> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_thong_bao, null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Notification notification = arrayList.get(position);
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
        holder.txtDate.setText("Ngày xem: " + fmtOut.format(date1));


    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ImageView img;
        public TextView txtNotification, txtContent, txtDate;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            txtNotification = (TextView) itemView.findViewById(R.id.txtnotification);
            txtContent = (TextView) itemView.findViewById(R.id.txtcontent);
            txtDate = (TextView) itemView.findViewById(R.id.txtdate);

        }
    }

}
