package com.example.luanvan.ui.Adapter.skill;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.Skill;
import com.example.luanvan.ui.Model.SkillKey;
import com.example.luanvan.ui.UpdateInfo.SkillActivity;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class SkillPickAdapter extends RecyclerView.Adapter<SkillPickAdapter.ItemHolder> implements Filterable {
    Context context;
    ArrayList<SkillKey> filterArraylist;
    ArrayList<SkillKey> nameList;
    Activity activity;
    int selectItem = 0;
    public SkillPickAdapter(Context context, ArrayList<SkillKey> arrayList, Activity activity) {
        this.context = context;
        this.filterArraylist = arrayList;
        this.nameList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_skill_pick, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if(filterArraylist.size() > 0){
            SkillKey skill = filterArraylist.get(position);
            holder.radioButton.setText(skill.getName());
            holder.radioButton.setChecked(position == selectItem);
            if(holder.radioButton.isChecked()){
                SkillActivity.idskill = filterArraylist.get(position).getId();
             //   Toast.makeText(context, "id skill " + SkillActivity.idskill , Toast.LENGTH_SHORT).show();
                SkillActivity.skillName = filterArraylist.get(position).getName();
            }

        }
    }

    @Override
    public int getItemCount() {
        return filterArraylist.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder{
        public RadioButton radioButton;
        public LinearLayout layout;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = (RadioButton) itemView.findViewById(R.id.radioposition);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);

            View.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectItem = getAdapterPosition();
                    notifyItemRangeChanged(0, filterArraylist.size());
                }
            };

            itemView.setOnClickListener(l);
            radioButton.setOnClickListener(l);
            layout.setOnClickListener(l);

        }
    }
    public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = stripAccents(constraint.toString()).trim();
                if(charSequenceString.isEmpty()){
                    filterArraylist = nameList;
                }else {
                    ArrayList<SkillKey> filteredList = new ArrayList<>();
                    for(SkillKey skillKey : nameList){
                        String name1 = stripAccents(skillKey.getName()).trim();
                        if(name1.toLowerCase().contains(charSequenceString.toLowerCase())){
                            filteredList.add(skillKey);
                        }
                        filterArraylist = filteredList;
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filterArraylist;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterArraylist = (ArrayList<SkillKey>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
