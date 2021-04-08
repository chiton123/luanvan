package com.example.luanvan.ui.Adapter.job;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.R;
import com.example.luanvan.ui.DetailedJob.DetailJobActivity;
import com.example.luanvan.ui.Model.Job;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CompanyJobAdapter extends RecyclerView.Adapter<CompanyJobAdapter.ItemHolder> {
    Context context;
    ArrayList<Job> arrayList;
    Activity activity;

    public CompanyJobAdapter(Context context, ArrayList<Job> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dong_viec_lam_cung_cong_ty, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if(arrayList.size() > 0){
            final Job job = arrayList.get(position);
            holder.txtJob.setText(job.getName());
            holder.txtArea.setText(job.getArea());
            String ngayketthuc = job.getEnd_date();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = null;
            try {
                date1 = fmt.parse(ngayketthuc);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
            holder.txtExpireDate.setText(fmtOut.format(date1));
            DecimalFormat decimalFormat = new DecimalFormat("#");
            holder.txtSalary.setText("" + decimalFormat.format(job.getSalary_min()/1000000) +
                    " đến " + decimalFormat.format(job.getSalary_max()/1000000) + " triệu");
            switch (job.getIdtype()){
                case 1:
                    holder.txtType.setText("Toàn thời gian");
                    break;
                case 2:
                    holder.txtType.setText("Bán thời gian");
                    break;
                case 3:
                    holder.txtType.setText("Thực tập");
                    break;
                case 4:
                    holder.txtType.setText("Làm việc từ xa");
                    break;

            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailJobActivity.class);
                    // 0: từ màn hình chính, tìm kiếm, lọc chuyển qua, công ty, 1: từ notification chuyển qua
                    intent.putExtra("kind", 0);
                    intent.putExtra("job", job);
                    activity.startActivity(intent);
                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txtJob, txtArea, txtExpireDate, txtSalary, txtType;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtJob = (TextView) itemView.findViewById(R.id.txtjob);
            txtArea = (TextView) itemView.findViewById(R.id.txtarea);
            txtExpireDate = (TextView) itemView.findViewById(R.id.txtdate);
            txtSalary = (TextView) itemView.findViewById(R.id.txtsalary);
            txtType = (TextView) itemView.findViewById(R.id.txttypejob);

        }
    }

}
