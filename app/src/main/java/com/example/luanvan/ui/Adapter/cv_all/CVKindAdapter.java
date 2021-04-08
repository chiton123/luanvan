package com.example.luanvan.ui.Adapter.cv_all;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.CVKind;
import com.example.luanvan.ui.cv.CVActivity;

import java.util.ArrayList;

public class CVKindAdapter extends RecyclerView.Adapter<CVKindAdapter.ItemHolder> {
    Context context;
    ArrayList<CVKind> arrayList;
    Activity activity;
    int REQUEST_CODE = 123;

    public CVKindAdapter(Context context, ArrayList<CVKind> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_cv_kind, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        CVKind cvKind = arrayList.get(position);
        holder.name.setText(cvKind.getName());
        holder.img.setImageResource(cvKind.getImage());
        holder.btnuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CVActivity.class);
                intent.putExtra("kind",1);
                // 1: defaut, 2: blue, 3: green
                intent.putExtra("color", arrayList.get(position).getId());
                activity.startActivityForResult(intent, REQUEST_CODE);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView img;
        Button btnuse;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            img = (ImageView) itemView.findViewById(R.id.image);
            btnuse = (Button) itemView.findViewById(R.id.buttonuse);

        }
    }

}
