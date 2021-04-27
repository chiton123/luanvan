package com.example.luanvan.ui.Adapter.chat_a;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Chat;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.UserF;
import com.example.luanvan.ui.chat.MessageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ItemHolder> implements Filterable {
    Context context;
    ArrayList<UserF> filterArraylist;
    ArrayList<UserF> nameList;
    Activity activity;
    String theLastMessage;

    public UserAdapter(Context context, ArrayList<UserF> arrayList, Activity activity) {
        this.context = context;
        this.filterArraylist = arrayList;
        this.nameList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if(filterArraylist.size() > 0){
            final UserF userF = filterArraylist.get(position);
            holder.txtUsername.setText(userF.getUsername());
            if(userF.getImageURL().equals("default")){
                holder.img.setImageResource(R.drawable.userchat);
            }else {
                Glide.with(context).load(userF.getImageURL()).into(holder.img);
            }
            lastMessage(userF.getId(), holder.txtLastMessage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("kind",2); // 1: từ detailjob qua, 2: từ chat qua
                    intent.putExtra("idrecruiter", userF.getId());
                    activity.startActivity(intent);
                }
            });
            if(position == filterArraylist.size() - 1){
                holder.view.setVisibility(View.GONE);
            }
        }



    }

    @Override
    public int getItemCount() {
        return filterArraylist.size();
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
                    ArrayList<UserF> filteredList = new ArrayList<>();
                    for(UserF userF : nameList){
                        String name1 = stripAccents(userF.getUsername()).trim();
                        if(name1.toLowerCase().contains(charSequenceString.toLowerCase())){
                            filteredList.add(userF);
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
                filterArraylist = (ArrayList<UserF>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txtUsername, txtLastMessage;
        public CircleImageView img;
        public View view;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = (TextView) itemView.findViewById(R.id.name);
            img = (CircleImageView) itemView.findViewById(R.id.img);
            view = (View) itemView.findViewById(R.id.view);
            txtLastMessage = (TextView) itemView.findViewById(R.id.txt_lastmessage);
        }
    }

    public void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot x : snapshot.getChildren()){
                    Chat chat = x.getValue(Chat.class);
                    if(chat.getReceiver().equals(MainActivity.uid) && chat.getSender().equals(userid)
                    || chat.getReceiver().equals(userid) && chat.getSender().equals(MainActivity.uid)){
                        theLastMessage = chat.getMessage();
                    }
                }
                switch (theLastMessage){
                    case "default":
                        last_msg.setText("No message");
                        last_msg.setVisibility(View.GONE);
                        break;
                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
