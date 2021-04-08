package com.example.luanvan.ui.Adapter.update_personal_info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Profile;
import com.example.luanvan.ui.UpdateInfo.ExperienceActivity;
import com.example.luanvan.ui.UpdateInfo.SkillActivity;
import com.example.luanvan.ui.UpdateInfo.StudyActivity;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ItemtHolder> {
    Context context;
    ArrayList<Profile> arrayList;
    Activity activity;
    int REQUEST_CODE1 = 1, REQUEST_CODE2 = 2, REQUEST_CODE3 = 3;

    public ProfileAdapter(Context context, ArrayList<Profile> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_profile, parent, false);
        ItemtHolder itemtHolder = new ItemtHolder(view);
        return itemtHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemtHolder holder, final int position) {
        Profile profile = arrayList.get(position);
        holder.txtname.setText(profile.getName());
        holder.img.setImageResource(profile.getImg());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(context, StudyActivity.class);
                        intent.putExtra("confirm", 10);
                        activity.startActivityForResult(intent, REQUEST_CODE1);
                        break;
                    case 1:
                        Intent intent1 = new Intent(context, ExperienceActivity.class);
                        intent1.putExtra("confirm", 10);
                        activity.startActivityForResult(intent1, REQUEST_CODE2);
                        break;
                    case 2:
                        Intent intent2 = new Intent(context, SkillActivity.class);
                        intent2.putExtra("confirm", 10);
                        activity.startActivityForResult(intent2, REQUEST_CODE3);
                        break;

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemtHolder extends RecyclerView.ViewHolder{
        public TextView txtname;
        public ImageView img;
        public LinearLayout linearLayout;
        public ItemtHolder(@NonNull View itemView) {
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.name);
            img = (ImageView) itemView.findViewById(R.id.hinh);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear);


        }
    }

}
