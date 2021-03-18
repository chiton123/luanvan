package com.example.luanvan.ui.Adapter.admin_a;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luanvan.R;
import com.example.luanvan.ui.DetailedJob.DetailJobActivity;
import com.example.luanvan.ui.DetailedJob.DetailJobAdminActivity;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.JobPost;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JobReviewAdapter extends RecyclerView.Adapter<JobReviewAdapter.ItemHolder> {
    Context context;
    ArrayList<JobPost> arrayList;
    Activity activity;

    public JobReviewAdapter(Context context, ArrayList<JobPost> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_tin_tuyen_dung_admin, null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        final JobPost job = arrayList.get(position);
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
        holder.txtsalary.setText("Từ " + decimalFormat.format( + job.getSalary_min()) + "đ đến " + decimalFormat.format(job.getSalary_max()) + "đ" );
        holder.txtarea.setText(job.getAddress());
        Glide.with(context).load(job.getImg()).into(holder.imganh);
        holder.imganh.setFocusable(false);
        holder.imganh.setFocusable(false);
        holder.btnReject.setFocusable(false);
        holder.btnConfirm.setFocusable(false);
        holder.btnConfirm.setClickable(false);
        holder.btnReject.setClickable(false);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailJobAdminActivity.class);
                intent.putExtra("job", arrayList.get(position));
                activity.startActivity(intent);

            }
        });
        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "aaa", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "bbb", Toast.LENGTH_SHORT).show();
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
        public Button btnConfirm, btnReject;;
        public LinearLayout layout;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txttencongviec = (TextView) itemView.findViewById(R.id.tencongviec);
            txttencongty = (TextView) itemView.findViewById(R.id.tencongty);
            txtarea = (TextView) itemView.findViewById(R.id.area);
            txtsalary = (TextView) itemView.findViewById(R.id.salary);
            txttime = (TextView) itemView.findViewById(R.id.time);
            imganh = (ImageView) itemView.findViewById(R.id.anh);
            btnConfirm = (Button) itemView.findViewById(R.id.buttonxacnhan);
            btnReject = (Button) itemView.findViewById(R.id.buttontuchoi);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);

        }
    }

}
