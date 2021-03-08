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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Study;
import com.example.luanvan.ui.UpdateInfo.StudyActivity;
import com.example.luanvan.ui.User.EditCombineActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

public class StudyAdapter extends RecyclerView.Adapter<StudyAdapter.ItemHolder> {
    Context context;
    ArrayList<Study> arrayList;
    Activity activity;
    int visable;
    Handler handler;
    int last = 0;

    public StudyAdapter(Context context, ArrayList<Study> arrayList, Activity activity, int visable) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.visable = visable;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_study, null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder,  int position) {
        Study study = arrayList.get(position);
        holder.school.setText(study.getSchool());
        holder.major.setText(study.getMajor());
        holder.img.setImageResource(R.drawable.school1);
        final String start = study.getDate_start();
        final String end = study.getDate_end();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null, date2 = null;
        try {
            date1 = fmt.parse(study.getDate_start());
            date2 = fmt.parse(study.getDate_end());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
        try {
            holder.date.setText(fmtOut.format(date1) + " - " + fmtOut.format(date2));
        }catch (NullPointerException e){
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
        final int positionX = position;
        // 0: hien thi
        if(visable == 0){
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StudyActivity.class);
                    // problem: khi muốn gửi có intent phải gửi 2 lần, xác nhận để không bị lỗi, 10: empty, 3: có đối tượng gửi
                    intent.putExtra("confirm", 3);
                    intent.putExtra("study", arrayList.get(positionX));
                    intent.putExtra("position", positionX);
                    activity.startActivityForResult(intent,1);
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
                                    Query query = MainActivity.mData.child("study").orderByChild("id").equalTo(arrayList.get(positionX).getId());
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
                                    Toast.makeText(context, arrayList.size() + "", Toast.LENGTH_SHORT).show();
                                    if(last == 1 || arrayList.size() == 1){
                                        ((EditCombineActivity) activity).refreshStudy();
                                        MainActivity.studyAdapter.notifyDataSetChanged();
                                    }else {
                                        if(arrayList.size() == 2){
                                            last = 1;
                                        }
                                        arrayList.remove(positionX);
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
    public void setItems(ArrayList<Study> study) {
        this.arrayList = study;
    }
    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView school, major, date;
        public ImageView img, delete, edit;
        public LinearLayout linearLayout;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            school = (TextView) itemView.findViewById(R.id.school);
            major = (TextView) itemView.findViewById(R.id.major);
            date = (TextView) itemView.findViewById(R.id.date);
            img = (ImageView) itemView.findViewById(R.id.hinh);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear);
            edit = (ImageView) itemView.findViewById(R.id.edit);
            delete = (ImageView) itemView.findViewById(R.id.delete);


        }
    }
}
