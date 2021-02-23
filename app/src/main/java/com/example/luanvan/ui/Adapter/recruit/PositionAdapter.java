package com.example.luanvan.ui.Adapter.recruit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.recruiter.CVManagement.CVManagementActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
        Job job = arrayList.get(position);
        holder.txtName.setText(job.getName());
        String ngaybatdau = job.getStart_date();
        String ngayketthuc = job.getEnd_date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = fmt.parse(ngaybatdau);
            date2 = fmt.parse(ngayketthuc);
            if(date2.after(Calendar.getInstance().getTime())){
                if(job.getStatus() == 1){
                    holder.txtStatus.setText("Ngưng tuyển dụng");
                }else {
                    holder.txtStatus.setText("Đang tuyển dụng");
                }
            }else {
                holder.txtStatus.setText("Ngưng tuyển dụng");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
        holder.txtDate.setText(fmtOut.format(date1) + "-" + fmtOut.format(date2));
        // status:  dừng tuyển 1, đang tuyển 0
        holder.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(activity, holder.btnChange);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edit:
                                Toast.makeText(context, "edit", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.show:
                                Intent intent = new Intent(activity, CVManagementActivity.class);
                                intent.putExtra("job_id", arrayList.get(position).getId());
                                activity.startActivity(intent);
                                break;
                            case R.id.end:
                                Toast.makeText(context, "end", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.delete:
                                Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });



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
