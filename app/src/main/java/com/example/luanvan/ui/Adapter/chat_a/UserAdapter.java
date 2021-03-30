package com.example.luanvan.ui.Adapter.chat_a;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.UserF;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ItemHolder> {
    Context context;
    ArrayList<UserF> arrayList;

    public UserAdapter(Context context, ArrayList<UserF> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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
        UserF userF = arrayList.get(position);
        holder.txtUsername.setText(userF.getUsername());
        if(userF.getImageURL().equals("default")){
            holder.img.setImageResource(R.drawable.userchat);
        }else {
            Glide.with(context).load(userF.getImageURL()).into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txtUsername;
        public CircleImageView img;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = (TextView) itemView.findViewById(R.id.name);
            img = (CircleImageView) itemView.findViewById(R.id.img);
        }
    }


}
