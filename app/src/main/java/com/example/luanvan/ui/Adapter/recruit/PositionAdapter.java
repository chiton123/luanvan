package com.example.luanvan.ui.Adapter.recruit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Job;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.ItemHolder> {
    Context context;
    ArrayList<Job> arrayList;
    Activity activity;

    public PositionAdapter(Context context, ArrayList<Job> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dong_vi_tri, null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Job job = arrayList.get(position);
        holder.txtName.setText(job.getName());
        String ngay = job.getDate();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = fmt.parse(ngay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
        holder.txtDate.setText(fmtOut.format(date));
        // Đang tuyển dụng 0, ngưng tuyển là 1
        if(job.getStatus() == 0){
            holder.txtStatus.setText("Đang tuyển dụng");
        }else {
            holder.txtStatus.setText("Ngưng tuyển dụng");
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txtName, txtDate, txtStatus;
        public Button btnChange;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.name);
            txtDate = (TextView) itemView.findViewById(R.id.date);
            txtStatus = (TextView) itemView.findViewById(R.id.status);
            btnChange = (Button) itemView.findViewById(R.id.buttonthaotac);

        }
    }

}
