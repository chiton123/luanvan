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
import com.example.luanvan.ui.Model.Profession;
import com.example.luanvan.ui.Model.ProfessionCandidate;

import java.util.ArrayList;

public class TagProfessionAdapter extends RecyclerView.Adapter<TagProfessionAdapter.ItemHolder> {
    Context context;
    ArrayList<Profession> arrayList;
    ArrayList<ProfessionCandidate> candidateArrayList;
    Activity activity;

    public TagProfessionAdapter(Context context, ArrayList<Profession> arrayList, Activity activity, ArrayList<ProfessionCandidate> candidateArrayList) {
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
            Profession profession = arrayList.get(position);
            holder.txtProfession.setText(profession.getName());
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
        TextView txtProfession;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtProfession = (TextView) itemView.findViewById(R.id.txtskill);

        }
    }

}
