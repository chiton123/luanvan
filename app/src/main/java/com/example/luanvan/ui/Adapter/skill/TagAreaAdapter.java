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
import com.example.luanvan.ui.Model.Area;
import com.example.luanvan.ui.Model.AreaCandidate;
import com.example.luanvan.ui.Model.SkillCandidate;
import com.example.luanvan.ui.Model.SkillKey;

import java.util.ArrayList;

public class TagAreaAdapter extends RecyclerView.Adapter<TagAreaAdapter.ItemHolder> {
    Context context;
    ArrayList<Area> arrayList;
    ArrayList<AreaCandidate> candidateArrayList;
    Activity activity;

    public TagAreaAdapter(Context context, ArrayList<Area> arrayList, Activity activity, ArrayList<AreaCandidate> candidateArrayList) {
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
            Area skill = arrayList.get(position);
            holder.txtArea.setText(skill.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0; i < candidateArrayList.size(); i++){
                        if(candidateArrayList.get(i).getId() == arrayList.get(position).getId()){
                            candidateArrayList.remove(i);
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
        TextView txtArea;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtArea = (TextView) itemView.findViewById(R.id.txtskill);

        }
    }

}
