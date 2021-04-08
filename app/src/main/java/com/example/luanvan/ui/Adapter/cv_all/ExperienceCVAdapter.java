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
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Experience;
import com.example.luanvan.ui.UpdateInfo.ExperienceActivity;
import com.example.luanvan.ui.modelCV.ExperienceCV;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExperienceCVAdapter extends RecyclerView.Adapter<ExperienceCVAdapter.ItemHolder> {
    Context context;
    ArrayList<ExperienceCV> arrayList;
    Activity activity;
    int visable;
    public ExperienceCVAdapter(Context context, ArrayList<ExperienceCV> arrayList, Activity activity, int visable) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.visable = visable;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_experience, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        ExperienceCV experience = arrayList.get(position);
        holder.company.setText(experience.getCompany());
        holder.position.setText(experience.getPosition());
        holder.img.setImageResource(R.drawable.company1);
        String start = experience.getStart();
        String end = experience.getEnd();
        holder.date.setText(start + " - " + end);

        if(visable == 0){
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_cv_experience);
                    dialog.setCancelable(false);
                    final EditText editname = (EditText) dialog.findViewById(R.id.name);
                    editname.setText(arrayList.get(position).getCompany());
                    final EditText editposition = (EditText) dialog.findViewById(R.id.position);
                    editposition.setText(arrayList.get(position).getPosition());
                    final EditText editstart = (EditText) dialog.findViewById(R.id.start);
                    editstart.setText(arrayList.get(position).getStart());
                    final EditText editend = (EditText) dialog.findViewById(R.id.end);
                    editend.setText(arrayList.get(position).getEnd());
                    final EditText editdescription = (EditText) dialog.findViewById(R.id.description);
                    editdescription.setText(arrayList.get(position).getDescription());
                    Button btnLuu = (Button) dialog.findViewById(R.id.luu);
                    Button btnHuy = (Button) dialog.findViewById(R.id.huy);
                    btnLuu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            arrayList.get(position).setCompany(editname.getText().toString());
                            arrayList.get(position).setPosition(editposition.getText().toString());
                            arrayList.get(position).setStart(editstart.getText().toString());
                            arrayList.get(position).setEnd(editend.getText().toString());
                            arrayList.get(position).setDescription(editdescription.getText().toString());
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
        public TextView company, position, date;
        public ImageView img, delete, edit;
        public LinearLayout linearLayout;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            company = (TextView) itemView.findViewById(R.id.company);
            position = (TextView) itemView.findViewById(R.id.position);
            date = (TextView) itemView.findViewById(R.id.date);
            img = (ImageView) itemView.findViewById(R.id.hinh);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear);
            edit = (ImageView) itemView.findViewById(R.id.edit);
            delete = (ImageView) itemView.findViewById(R.id.delete);
        }
    }
}
