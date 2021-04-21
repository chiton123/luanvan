package com.example.luanvan.ui.Adapter.skill;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.SkillCandidate;
import com.example.luanvan.ui.Model.SkillKey;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ItemHolder> {
    Context context;
    ArrayList<SkillKey> arrayList;
    ArrayList<SkillCandidate> candidateArrayList;
    Activity activity;

    public TagAdapter(Context context, ArrayList<SkillKey> arrayList, Activity activity, ArrayList<SkillCandidate> candidateArrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.candidateArrayList = candidateArrayList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_tag, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        if(arrayList.size() > 0){
            SkillKey skill = arrayList.get(position);
            holder.txtSkill.setText(skill.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0; i < candidateArrayList.size(); i++){
                        if(candidateArrayList.get(i).getId() == arrayList.get(position).getId()){
                            candidateArrayList.get(i).setCheck(0);
                        }
                    }

                    arrayList.remove(position);
                    notifyDataSetChanged();
                }
            });

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
