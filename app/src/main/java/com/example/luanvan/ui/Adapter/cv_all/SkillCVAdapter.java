package com.example.luanvan.ui.Adapter.cv_all;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.update_personal_info.SkillAdapter;
import com.example.luanvan.ui.Model.Skill;
import com.example.luanvan.ui.UpdateInfo.SkillActivity;
import com.example.luanvan.ui.modelCV.SkillCV;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SkillCVAdapter extends RecyclerView.Adapter<SkillCVAdapter.ItemHolder> {
    Context context;
    ArrayList<SkillCV> arrayList;
    Activity activity;
    int visable;
    String key = "";

    public SkillCVAdapter(Context context, ArrayList<SkillCV> arrayList, Activity activity, int visable) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.visable = visable;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_skill, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }
    public void getKey(final int position){
        MainActivity.mData.child("cvinfo").child("skill").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot x : snapshot.getChildren()){
                    if(snapshot.child("id").getValue().toString().equals(arrayList.get(position).getId())){
                        key = snapshot.getKey();
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
        final SkillCV skill = arrayList.get(position);
        holder.skill.setText(skill.getName());
        holder.ratingBar.setRating( skill.getStar());
        holder.img.setImageResource(R.drawable.skill1);
        if(visable == 0){
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // dialog
                    final Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_cv_skill);
                    dialog.setCancelable(false);
                    final EditText name = (EditText) dialog.findViewById(R.id.name);
                    name.setText(arrayList.get(position).getName());
                    final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.rating);
                    ratingBar.setRating(arrayList.get(position).getStar());
                    Button btnLuu = (Button) dialog.findViewById(R.id.luu);
                    Button btnHuy = (Button) dialog.findViewById(R.id.huy);
                    btnLuu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            arrayList.get(position).setName(name.getText().toString());
                            arrayList.get(position).setStar(ratingBar.getRating());
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });

                    btnHuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
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
                                    arrayList.remove(position);
                                    notifyDataSetChanged();
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
