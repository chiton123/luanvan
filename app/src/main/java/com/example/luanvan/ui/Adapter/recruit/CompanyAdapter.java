package com.example.luanvan.ui.Adapter.recruit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Company;
import com.example.luanvan.ui.Model.Job;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ItemHolder> implements Filterable {
    Context context;
    ArrayList<Company> filterArraylist;
    ArrayList<Company> nameList;
    Activity activity;

    public CompanyAdapter(Context context, ArrayList<Company> arrayList, Activity activity) {
        this.context = context;
        this.filterArraylist = arrayList;
        this.nameList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_cong_ty, null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if(filterArraylist.size() > 0){
            Company company = filterArraylist.get(position);
            holder.txtCompany.setText(company.getName());
            holder.txtIntroduction.setText(company.getIntroduction());
            Glide.with(context).load(company.getImage()).into(holder.img);
            holder.txtNumberJob.setText("Xem " + company.getNumber_job() + " tin tuyển dụng");
        }


    }

    @Override
    public int getItemCount() {
        return filterArraylist.size();
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
                    ArrayList<Company> filteredList = new ArrayList<>();
                    for(Company company : nameList){
                        String name1 = stripAccents(company.getName()).trim();
                        if(name1.toLowerCase().contains(charSequenceString.toLowerCase())){
                            filteredList.add(company);
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
                filterArraylist = (ArrayList<Company>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        TextView txtCompany, txtIntroduction, txtNumberJob;
        ImageView img;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtCompany = (TextView) itemView.findViewById(R.id.txtcompany);
            txtIntroduction = (TextView) itemView.findViewById(R.id.txtintroduce);
            txtNumberJob = (TextView) itemView.findViewById(R.id.txtnumberjob);
            img = (ImageView) itemView.findViewById(R.id.img);


        }
    }
    public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }


}
