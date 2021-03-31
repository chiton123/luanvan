package com.example.luanvan.ui.Adapter.chat_a;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Chat;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ItemHoler> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private ArrayList<Chat> mchat;
    private String imageURL;
    public MessageAdapter(Context context, ArrayList<Chat> mchat, String imageURL) {
        this.context = context;
        this.mchat = mchat;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public ItemHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_right, null);
            ItemHoler itemHoler = new ItemHoler(v);
            return itemHoler;
        }else {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_left, null);
            ItemHoler itemHoler = new ItemHoler(v);
            return itemHoler;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHoler holder, int position) {
        Chat chat = mchat.get(position);
        holder.txt_send.setText(chat.getMessage());

        if(imageURL.equals("default")){
            holder.profile_image.setImageResource(R.drawable.userchat);
        }else {
            Glide.with(context).load(imageURL).into(holder.profile_image);
        }
        if(position == mchat.size() - 1){
            if(chat.isIsseen()){
                holder.txt_seen.setText("Đã xem");
            }else {
                holder.txt_seen.setText("Đã gửi");
            }
        }else {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }


    public class ItemHoler extends RecyclerView.ViewHolder{
        public TextView txt_send, txt_seen;
        public CircleImageView profile_image;
        public ItemHoler(@NonNull View itemView) {
            super(itemView);
            txt_send = (TextView) itemView.findViewById(R.id.show_message);
            profile_image = (CircleImageView) itemView.findViewById(R.id.profile_image);
            txt_seen = (TextView) itemView.findViewById(R.id.txt_seen);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mchat.get(position).getSender().equals(MainActivity.uid)){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
