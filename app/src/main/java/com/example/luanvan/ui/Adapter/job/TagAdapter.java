package com.example.luanvan.ui.Adapter.job;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.SkillKey;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ItemHolder> {
    Context context;
    ArrayList<SkillKey> arrayList;
    Activity activity;

    public TagAdapter(Context context, ArrayList<SkillKey> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_tag, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if(arrayList.size() > 0){
            SkillKey skill = arrayList.get(position);
            holder.txtSkill.setText(skill.getName());

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        TextView txtSkill;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtSkill = (TextView) itemView.findViewById(R.id.txtskill);

        }
    }

}
