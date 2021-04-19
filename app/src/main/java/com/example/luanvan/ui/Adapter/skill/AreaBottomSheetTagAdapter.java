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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Area;
import com.example.luanvan.ui.Model.AreaCandidate;
import com.example.luanvan.ui.Model.SkillCandidate;
import com.example.luanvan.ui.Model.SkillKey;
import com.example.luanvan.ui.recruiter.search_r.SearchCandidateActivity;

import java.text.Normalizer;
import java.util.ArrayList;

public class AreaBottomSheetTagAdapter extends RecyclerView.Adapter<AreaBottomSheetTagAdapter.ItemHolder> implements Filterable {
    Context context;
    ArrayList<AreaCandidate> filterArraylist;
    ArrayList<AreaCandidate> nameList;
    ArrayList<Area> arrayListChosen;
    Activity activity;
    public AreaBottomSheetTagAdapter(Context context, ArrayList<AreaCandidate> arrayList, Activity activity, ArrayList<Area> arrayListChosen) {
        this.context = context;
        this.filterArraylist = arrayList;
        this.nameList = arrayList;
        this.activity = activity;
        this.arrayListChosen = arrayListChosen;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_skill_pick, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
        if(filterArraylist.size() > 0){
            final AreaCandidate areaCandidate = filterArraylist.get(position);
            holder.radioButton.setText(areaCandidate.getName());
            if(areaCandidate.getCheck() == 1){
                holder.radioButton.setChecked(true);
            }else {
                holder.radioButton.setChecked(false);
            }
            holder.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(context, "id: " + skill.getId(), Toast.LENGTH_SHORT).show();
                    if(filterArraylist.get(position).getCheck() == 1){
                       // holder.radioButton.setChecked(false);
                        filterArraylist.get(position).setCheck(0);
                        for(int i=0; i < arrayListChosen.size(); i++){
                            if(arrayListChosen.get(i).getId() == filterArraylist.get(position).getId()){
                                arrayListChosen.remove(i);
                             //   Toast.makeText(context, "id: " + filterArraylist.get(position).getId(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                     //   holder.radioButton.setChecked(true);
                        filterArraylist.get(position).setCheck(1);
                        arrayListChosen.add(new Area(filterArraylist.get(position).getId(), filterArraylist.get(position).getName()));

                    }
                    notifyDataSetChanged();

                }
            });
            for(int i=0; i < arrayListChosen.size(); i++){
                if(arrayListChosen.get(i).getId() == filterArraylist.get(position).getId()){
                    filterArraylist.get(position).setCheck(1);
                    holder.radioButton.setChecked(true);
                }
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

//            View.OnClickListener l = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    selectItem = getAdapterPosition();
//                    notifyItemRangeChanged(0, filterArraylist.size());
//                }
//            };
//
//            itemView.setOnClickListener(l);
//            radioButton.setOnClickListener(l);
//            layout.setOnClickListener(l);

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
                    ArrayList<AreaCandidate> filteredList = new ArrayList<>();
                    for(AreaCandidate areaCandidate : nameList){
                        String name1 = stripAccents(areaCandidate.getName()).trim();
                        if(name1.toLowerCase().contains(charSequenceString.toLowerCase())){
                            filteredList.add(areaCandidate);
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
                filterArraylist = (ArrayList<AreaCandidate>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
