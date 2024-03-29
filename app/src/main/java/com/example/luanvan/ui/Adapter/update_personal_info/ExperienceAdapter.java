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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Experience;
import com.example.luanvan.ui.UpdateInfo.ExperienceActivity;
import com.example.luanvan.ui.UpdateInfo.EditCombineActivity;
import com.example.luanvan.ui.User.NotificationsFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.ItemHolder> {
    Context context;
    ArrayList<Experience> arrayList;
    Activity activity;
    int visable;
    int last = 0;
    public ExperienceAdapter(Context context, ArrayList<Experience> arrayList, Activity activity, int visable) {
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
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
        final Experience experience = arrayList.get(position);
        holder.company.setText(experience.getCompany());
        holder.position.setText(experience.getPosition());
        holder.img.setImageResource(R.drawable.company1);
        String start = experience.getDate_start();
        String end = experience.getDate_end();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null, date2 = null;
        try {
            date1 = fmt.parse(start);
            date2 = fmt.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
        try {
            holder.date.setText(fmtOut.format(date1) + " - " + fmtOut.format(date2));
        }catch (NullPointerException e){

            FancyToast.makeText(context, e.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        }

        if(visable == 0){
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ExperienceActivity.class);
                    // problem: khi muốn gửi có intent phải gửi 2 lần, xác nhận để không bị lỗi, 10: empty, 3: có đối tượng gửi
                    intent.putExtra("confirm", 3);
                    intent.putExtra("experience", arrayList.get(position));
                    intent.putExtra("position", position);
                    activity.startActivityForResult(intent,2);
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
                                    RequestQueue requestQueue = Volley.newRequestQueue(activity);
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlDeleteExperience,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    if(response.equals("success")){

                                                        FancyToast.makeText(context, "Xóa thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                                        int pos = holder.getAdapterPosition();

                                                        if(last == 1 || arrayList.size() == 1){
                                                            ((EditCombineActivity) activity).refreshExperience();
                                                            NotificationsFragment.experienceAdapter.notifyDataSetChanged();

                                                        }else {
                                                            if(arrayList.size() == 2){
                                                                last = 1;
                                                            }
                                                            arrayList.remove(position);
                                                            notifyDataSetChanged();

                                                        }
                                                        NotificationsFragment.experienceAdapter.notifyDataSetChanged();
                                                    }else {

                                                        FancyToast.makeText(context, "Xóa thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    FancyToast.makeText(context, error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                                }
                                            }){
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String,String> map = new HashMap<>();
                                            map.put("id", String.valueOf(experience.getId()));
                                            return map;
                                        }
                                    };
                                    requestQueue.add(stringRequest);

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
    public void setItems(ArrayList<Experience> experiences) {
        this.arrayList = experiences;
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
