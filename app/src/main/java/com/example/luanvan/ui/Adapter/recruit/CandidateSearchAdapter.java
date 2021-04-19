package com.example.luanvan.ui.Adapter.recruit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.UserSearch;
import com.example.luanvan.ui.recruiter.search_r.CandidateViewActivity;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class CandidateSearchAdapter extends RecyclerView.Adapter<CandidateSearchAdapter.ItemHolder> {
    Context context;
    ArrayList<UserSearch> arrayList;
    Activity activity;

    public CandidateSearchAdapter(Context context, ArrayList<UserSearch> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_candidate, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if(arrayList.size() > 0){
            final UserSearch user = arrayList.get(position);
            holder.txtName.setText(user.getUsername());
            holder.txtPosition.setText(user.getPosition());
            if(user.getMode() == 0){
                holder.txtMode.setVisibility(View.INVISIBLE);

            }else {
                holder.txtMode.setText("Đang tìm việc");
                holder.txtMode.setVisibility(View.VISIBLE);
            }
            holder.txtArea.setText(user.getArea());
            if(user.getExperience().equals("")){
                holder.layout_experience.setVisibility(View.GONE);
            }else {
                holder.txtExperience.setText(xuongdong(user.getExperience()));
                holder.layout_experience.setVisibility(View.VISIBLE);
            }
            if(user.getStudy().equals("")){
                holder.layout_study.setVisibility(View.GONE);
            }else {
                holder.layout_study.setVisibility(View.VISIBLE);
                holder.txtStudy.setText(xuongdong(user.getStudy()));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CandidateViewActivity.class);
                    intent.putExtra("user", user);
                    activity.startActivity(intent);
                }
            });

        }

    }
    public String xuongdong(String text){
        String text1 = text.replaceAll("\\s\\s+", " ").trim();
        String ketqua = "";
        if(text.contains(".")){
            String[] split = text1.split(Pattern.quote("."));
            for(String item : split){
                ketqua += "+" +  item + "\n";
            }
            return ketqua;
        }else {
            return text;
        }
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        TextView txtName, txtMode, txtPosition, txtExperience, txtStudy, txtArea;
        LinearLayout layout_study, layout_experience;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtcandidate);
            txtMode = (TextView) itemView.findViewById(R.id.txtmode);
            txtPosition = (TextView) itemView.findViewById(R.id.txtposition);
            txtExperience = (TextView) itemView.findViewById(R.id.txtexperience1);
            txtStudy = (TextView) itemView.findViewById(R.id.txtstudy);
            txtArea = (TextView) itemView.findViewById(R.id.txtarea);
            layout_study = (LinearLayout) itemView.findViewById(R.id.layout_study);
            layout_experience  = (LinearLayout) itemView.findViewById(R.id.layout_experience);



        }
    }


}
