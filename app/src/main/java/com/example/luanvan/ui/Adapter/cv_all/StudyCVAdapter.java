package com.example.luanvan.ui.Adapter.cv_all;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.R;

import com.example.luanvan.ui.cv_content.CVStudyActivity;
import com.example.luanvan.ui.modelCV.StudyCV;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;

public class StudyCVAdapter extends RecyclerView.Adapter<StudyCVAdapter.ItemHolder> {
    Context context;
    ArrayList<StudyCV> arrayList;
    Activity activity;
    int visable;

    public StudyCVAdapter(Context context, ArrayList<StudyCV> arrayList, Activity activity, int visable) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.visable = visable;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_study,null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        StudyCV studyCV = arrayList.get(position);
        holder.school.setText(studyCV.getSchool());
        holder.major.setText(studyCV.getMajor());
        holder.img.setImageResource(R.drawable.company1);
        String start = studyCV.getStart();
        String end = studyCV.getEnd();
        holder.date.setText(start + " - " + end);

        if(visable == 0){
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_cv_study);
                    dialog.setCancelable(false);
                    final EditText editschool = (EditText) dialog.findViewById(R.id.school);
                    editschool.setText(arrayList.get(position).getSchool());
                    final EditText editmajor = (EditText) dialog.findViewById(R.id.major);
                    editmajor.setText(arrayList.get(position).getMajor());
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
                            String school = editschool.getText().toString();
                            String major = editmajor.getText().toString();
                            String start = editstart.getText().toString();
                            String end = editend.getText().toString();
                            String description = editdescription.getText().toString();
                            if(school.equals("") || major.equals("") || start.equals("") || end.equals("") || description.equals("")){
                                FancyToast.makeText(context, "Vui lòng nhập đủ thông tin", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            }else{
                                arrayList.get(position).setSchool(school);
                                arrayList.get(position).setMajor(major);
                                arrayList.get(position).setStart(start);
                                arrayList.get(position).setEnd(end);
                                arrayList.get(position).setDescription(description);
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }

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
                                    ((CVStudyActivity) activity).checkNothing();
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

    public class ItemHolder extends RecyclerView.ViewHolder {
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