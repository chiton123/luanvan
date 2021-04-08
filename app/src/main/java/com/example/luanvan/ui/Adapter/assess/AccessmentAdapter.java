package com.example.luanvan.ui.Adapter.assess;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Assessment;

import java.util.ArrayList;

public class AccessmentAdapter extends RecyclerView.Adapter<AccessmentAdapter.ItemHolder> {
    Context context;
    ArrayList<Assessment> arrayList;
    Activity activity;

    public AccessmentAdapter(Context context, ArrayList<Assessment> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_danh_gia, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if(arrayList.size() > 0){
            Assessment assessment = arrayList.get(position);
            holder.txtCandidate.setText(assessment.getUsername());
            holder.txtRemark.setText(assessment.getRemark());
            holder.ratingBar.setRating(assessment.getStar());

        }
        if(position == arrayList.size() - 1){
            holder.view.setVisibility(View.GONE);
        }else {
            holder.view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        TextView txtCandidate, txtRemark;
        RatingBar ratingBar;
        View view;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtCandidate = (TextView) itemView.findViewById(R.id.txtcandidate);
            txtRemark = (TextView) itemView.findViewById(R.id.txtremark);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating);
            view = (View) itemView.findViewById(R.id.view);

        }
    }

}
