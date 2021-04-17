package com.example.luanvan.ui.Adapter.position_a;

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
import com.example.luanvan.ui.Model.Position;
import com.example.luanvan.ui.UpdateInfo.PersonalInfoActivity;
import com.example.luanvan.ui.recruiter.search_r.FilterCandidateActivity;

import java.text.Normalizer;
import java.util.ArrayList;

public class PositionPickAdapter extends RecyclerView.Adapter<PositionPickAdapter.ItemHolder> implements Filterable {
    Context context;
    ArrayList<Position> filterArraylist;
    ArrayList<Position> nameList;
    Activity activity;
    int kind; // kind 1: Từ ứng viên cho, kind 2: Từ nhà tuyển dụng chọn
    int selectItem = 0;
    public PositionPickAdapter(Context context, ArrayList<Position> arrayList, Activity activity, int kind) {
        this.context = context;
        this.filterArraylist = arrayList;
        this.nameList = arrayList;
        this.activity = activity;
        this.kind = kind;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_position_pick, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if(filterArraylist.size() > 0){
            Position position_current = filterArraylist.get(position);
            holder.radioButton.setText(position_current.getName());
            holder.radioButton.setChecked(position == selectItem);
            if(holder.radioButton.isChecked()){
                if(kind == 1){
                    PersonalInfoActivity.idposition = filterArraylist.get(position).getId();
                    PersonalInfoActivity.position = filterArraylist.get(position).getName();
                }else {
                    FilterCandidateActivity.idposition = filterArraylist.get(position).getId();
                    FilterCandidateActivity.position = filterArraylist.get(position).getName();
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
                    ArrayList<Position> filteredList = new ArrayList<>();
                    for(Position position : nameList){
                        String name1 = stripAccents(position.getName()).trim();
                        if(name1.toLowerCase().contains(charSequenceString.toLowerCase())){
                            filteredList.add(position);
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
                filterArraylist = (ArrayList<Position>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
