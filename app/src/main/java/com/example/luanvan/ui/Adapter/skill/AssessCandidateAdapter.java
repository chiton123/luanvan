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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.recruit.ProfileCadidateAdapter;
import com.example.luanvan.ui.Model.SkillCandidate;
import com.example.luanvan.ui.Model.SkillKey;
import com.example.luanvan.ui.UpdateInfo.SkillActivity;

import java.text.Normalizer;
import java.util.ArrayList;

public class AssessCandidateAdapter extends RecyclerView.Adapter<AssessCandidateAdapter.ItemHolder>  {
    Context context;
    ArrayList<SkillCandidate> arrayList;
    Activity activity;
    // int selectItem = 0;
    TextView txtStatus;
    public AssessCandidateAdapter(Context context, ArrayList<SkillCandidate> arrayList, Activity activity,  TextView txtStatus) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.txtStatus = txtStatus;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_assessment_candidate, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        if(arrayList.size() > 0){
            SkillCandidate skill = arrayList.get(position);
            holder.radioButton.setText(skill.getName());
          //  holder.radioButton.setChecked(position == selectItem);
            if(skill.getCheck() == 1){
                holder.radioButton.setChecked(true);
            }else {
                holder.radioButton.setChecked(false);
            }
            switch (arrayList.get(position).getId()){
                case 0:
                    holder.txtRound.setText("Vòng lọc CV");
                    holder.txtRound.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    holder.txtRound.setText("Vòng phỏng vấn");
                    holder.txtRound.setVisibility(View.VISIBLE);
                    break;
                case 12:
                    holder.txtRound.setText("Vòng nhận việc");
                    holder.txtRound.setVisibility(View.VISIBLE);
                    break;
                default:
                    holder.txtRound.setVisibility(View.GONE);
                    break;
            }

            holder.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(arrayList.get(position).getCheck() == 1){
                        ProfileCadidateAdapter.statusApplication = 0;
                        arrayList.get(position).setCheck(0);
                        txtStatus.setText("Bạn chưa chọn");
                    }else {
                        ProfileCadidateAdapter.statusApplication = arrayList.get(position).getId();
                        arrayList.get(position).setCheck(1);
                        txtStatus.setText(arrayList.get(position).getName());
                        for(int i=0; i < arrayList.size(); i++){
                            if(arrayList.get(i).getId() != arrayList.get(position).getId()){
                                arrayList.get(i).setCheck(0);
                            }
                        }
//                        status = 0;
//                        arrayList.get(0).setCheck(1);
                    }

                    notifyDataSetChanged();
                //    Toast.makeText(context, ProfileCadidateAdapter.statusApplication + "", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder{
        public RadioButton radioButton;
        public TextView txtRound;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = (RadioButton) itemView.findViewById(R.id.radioposition);
            txtRound = (TextView) itemView.findViewById(R.id.txtround);



        }
    }


}
