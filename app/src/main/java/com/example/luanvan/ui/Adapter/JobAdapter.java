package com.example.luanvan.ui.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luanvan.R;
import com.example.luanvan.ui.DetailedJob.DetailJobActivity;
import com.example.luanvan.ui.Model.Job;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ItemHolder>  {
    Context context;
    ArrayList<Job> arrayList;
    Activity activity;


    public JobAdapter(Context context, ArrayList<Job> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;

    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dong_viec_lam, null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        Job job = arrayList.get(position);
        holder.txttencongviec.setText(job.getName());
        holder.txttencongty.setText(job.getCompany_name());
        String ngaybatdau = job.getStart_date();
        String ngayketthuc = job.getEnd_date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = fmt.parse(ngaybatdau);
            date2 = fmt.parse(ngayketthuc);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
        // chỉ để hạn chót nộp hồ sơ
        holder.txttime.setText(fmtOut.format(date2));
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtsalary.setText(decimalFormat.format(job.getSalary()) + "đ");
        holder.txtarea.setText(job.getArea());
        Glide.with(context).load(job.getImg()).into(holder.imganh);
        holder.imganh.setFocusable(false);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailJobActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("job", arrayList.get(position));
                activity.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txttencongviec, txttencongty, txtarea, txttime, txtsalary;
        public ImageView imganh;
        public LinearLayout layout;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txttencongviec = (TextView) itemView.findViewById(R.id.tencongviec);
            txttencongty = (TextView) itemView.findViewById(R.id.tencongty);
            txtarea = (TextView) itemView.findViewById(R.id.area);
            txtsalary = (TextView) itemView.findViewById(R.id.salary);
            txttime = (TextView) itemView.findViewById(R.id.time);
            imganh = (ImageView) itemView.findViewById(R.id.anh);
            layout = (LinearLayout) itemView.findViewById(R.id.linear);


        }
    }

}