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
import com.example.luanvan.ui.Model.Applicant;
import com.example.luanvan.ui.recruiter.CVManagement.CandidateInfoActivity;

import java.util.ArrayList;

public class CVFilterAdapter extends RecyclerView.Adapter<CVFilterAdapter.ItemHolder> {
    Context context;
    ArrayList<Applicant> arrayList;
    Activity activity;

    public CVFilterAdapter(Context context, ArrayList<Applicant> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_loc_cv, null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
        Applicant applicant = arrayList.get(position);
        holder.txtName.setText(applicant.getUsername());
        holder.txtEmail.setText(applicant.getEmail());
        int status = applicant.getStatus();
        switch (status){
            case 0:
                holder.txtStatus.setText("Chưa đánh giá");
                break;
            case 1:
                holder.txtStatus.setText("Đạt yêu cầu");
                break;
            case 2:
                holder.txtStatus.setText("Không đạt yêu cầu");
                break;

        }
        holder.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(activity, holder.btnChange);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_filter, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.show:
                                Intent intent = new Intent(context, CandidateInfoActivity.class);
                                intent.putExtra("applicant", arrayList.get(position));
                                activity.startActivity(intent);
                                break;
                            case R.id.access:
                                Toast.makeText(context, "access", Toast.LENGTH_SHORT).show();
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
        public TextView txtName, txtEmail, txtStatus;
        public Button btnChange;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.cadidateName);
            txtEmail = (TextView) itemView.findViewById(R.id.email);
            txtStatus = (TextView) itemView.findViewById(R.id.status);
            btnChange = (Button) itemView.findViewById(R.id.buttonthaotac);

        }
    }
}
