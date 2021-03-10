package com.example.luanvan.ui.Adapter.job_apply;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luanvan.R;
import com.example.luanvan.ui.DetailedJob.DetailJobActivity;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.Job_Apply;
import com.example.luanvan.ui.cv.CVShowActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JobApplyAdapter extends RecyclerView.Adapter<JobApplyAdapter.ItemHolder>  {
    Context context;
    ArrayList<Job_Apply> arrayList;
    Activity activity;
    int kind; // 0: normal, 1: việc đã ứng tuyển

    public JobApplyAdapter(Context context, ArrayList<Job_Apply> arrayList, Activity activity, int kind) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.kind = kind;
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
        final Job_Apply job = arrayList.get(position);
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
        holder.txtarea.setText(job.getAddress());
        Glide.with(context).load(job.getImg()).into(holder.imganh);
        holder.imganh.setFocusable(false);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailJobActivity.class);
                // 0: từ màn hình chính, tìm kiếm, lọc chuyển qua, 1: từ notification chuyển qua
                intent.putExtra("kind", 0);
                intent.putExtra("job", arrayList.get(position));
                activity.startActivity(intent);

            }
        });
        if(kind == 1){
            holder.layout_chat.setVisibility(View.VISIBLE);
            holder.btnViewCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CVShowActivity.class);
                    intent.putExtra("kind", 2); // 1: show cv , 2: job apply
                    intent.putExtra("cv_id", job.getId_cv() );
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txttencongviec, txttencongty, txtarea, txttime, txtsalary;
        public ImageView imganh;
        public LinearLayout layout, layout_chat;
        public Button btnChat, btnViewCV;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txttencongviec = (TextView) itemView.findViewById(R.id.tencongviec);
            txttencongty = (TextView) itemView.findViewById(R.id.tencongty);
            txtarea = (TextView) itemView.findViewById(R.id.area);
            txtsalary = (TextView) itemView.findViewById(R.id.salary);
            txttime = (TextView) itemView.findViewById(R.id.time);
            imganh = (ImageView) itemView.findViewById(R.id.anh);
            layout = (LinearLayout) itemView.findViewById(R.id.linear);
            layout_chat = (LinearLayout) itemView.findViewById(R.id.layout_chat);
            btnChat = (Button) itemView.findViewById(R.id.buttonchat);
            btnViewCV = (Button) itemView.findViewById(R.id.buttonviewcv);


        }
    }

}