package com.example.luanvan.ui.Adapter.update_personal_info;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Skill;
import com.example.luanvan.ui.Model.Study;
import com.example.luanvan.ui.UpdateInfo.SkillActivity;
import com.example.luanvan.ui.User.EditCombineActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.ItemHolder> {
    Context context;
    ArrayList<Skill> arrayList;
    Activity activity;
    int visable;
    int last = 0;
    public SkillAdapter(Context context, ArrayList<Skill> arrayList, Activity activity, int visable) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.visable = visable;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_skill, null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
        Skill skill = arrayList.get(position);
        holder.skill.setText(skill.getName());
        holder.ratingBar.setRating( skill.getStar());
        holder.img.setImageResource(R.drawable.skill1);
        if(visable == 0){
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SkillActivity.class);
                    // problem: khi muốn gửi có intent phải gửi 2 lần, xác nhận để không bị lỗi, 10: empty, 3: có đối tượng gửi
                    intent.putExtra("confirm", 3);
                    intent.putExtra("skill", arrayList.get(position));
                    intent.putExtra("position", position);
                    activity.startActivityForResult(intent,3);
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setTitle("Xác nhận");
                    alert.setMessage("Bạn có muốn xóa không ?");
                    alert.setNegativeButton("Không",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    alert.setPositiveButton("Có",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Query query = MainActivity.mData.child("skill").orderByChild("id").equalTo(arrayList.get(position).getId());
                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for(DataSnapshot x : snapshot.getChildren()){
                                                x.getRef().removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    int pos = holder.getAdapterPosition();

                                    if(last == 1 || arrayList.size() == 0) {
                                        ((EditCombineActivity) activity).refreshSkill();
                                        MainActivity.skillAdapter.notifyDataSetChanged();

                                    }else {
                                        if(arrayList.size() == 2){
                                            last = 1;
                                        }
                                        arrayList.remove(position);
                                        notifyDataSetChanged();

                                    }
                                }
                            });

                    alert.show();
                }
            });
        }else {
            holder.linearLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public void setItems(ArrayList<Skill> skills) {
        this.arrayList = skills;
    }
    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView skill;
        public RatingBar ratingBar;
        public ImageView img, delete, edit;
        public LinearLayout linearLayout;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            skill = (TextView) itemView.findViewById(R.id.skill);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating);
            img = (ImageView) itemView.findViewById(R.id.hinh);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear);
            edit = (ImageView) itemView.findViewById(R.id.edit);
            delete = (ImageView) itemView.findViewById(R.id.delete);
        }
    }
}
