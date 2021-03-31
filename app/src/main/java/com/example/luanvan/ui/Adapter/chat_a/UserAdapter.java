package com.example.luanvan.ui.Adapter.chat_a;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.UserF;
import com.example.luanvan.ui.chat.MessageActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ItemHolder> {
    Context context;
    ArrayList<UserF> arrayList;
    Activity activity;

    public UserAdapter(Context context, ArrayList<UserF> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        final UserF userF = arrayList.get(position);
        holder.txtUsername.setText(userF.getUsername());
        if(userF.getImageURL().equals("default")){
            holder.img.setImageResource(R.drawable.userchat);
        }else {
            Glide.with(context).load(userF.getImageURL()).into(holder.img);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("kind",2); // 1: từ detailjob qua, 2: từ chat qua
                intent.putExtra("idrecruiter", userF.getId());
                activity.startActivity(intent);
            }
        });
        if(position == arrayList.size() - 1){
            holder.view.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txtUsername;
        public CircleImageView img;
        public View view;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = (TextView) itemView.findViewById(R.id.name);
            img = (CircleImageView) itemView.findViewById(R.id.img);
            view = (View) itemView.findViewById(R.id.view);
        }
    }


}
