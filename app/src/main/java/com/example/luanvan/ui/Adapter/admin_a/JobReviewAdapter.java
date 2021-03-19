package com.example.luanvan.ui.Adapter.admin_a;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.bumptech.glide.Glide;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.DetailedJob.DetailJobActivity;
import com.example.luanvan.ui.DetailedJob.DetailJobAdminActivity;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.JobPost;
import com.example.luanvan.ui.admin.JobReviewActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JobReviewAdapter extends RecyclerView.Adapter<JobReviewAdapter.ItemHolder> {
    Context context;
    ArrayList<JobPost> arrayList;
    Activity activity;
    int REQUEST_CODE = 123;
    BottomSheetDialog bottomSheetDialog;
    String note_reject = "";
    public JobReviewAdapter(Context context, ArrayList<JobPost> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_tin_tuyen_dung_admin, null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        final JobPost job = arrayList.get(position);
        holder.txttencongviec.setText(job.getName());
        holder.txttencongty.setText(job.getCompany_name());
        String ngaybatdau = job.getStart_date();
        String ngayketthuc = job.getEnd_date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = fmt.parse(ngaybatdau);
            date2 = fmt.parse(ngayketthuc);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
        // chỉ để hạn chót nộp hồ sơ
        holder.txttime.setText(fmtOut.format(date2));
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtsalary.setText("Từ " + decimalFormat.format( + job.getSalary_min()) + "đ đến " + decimalFormat.format(job.getSalary_max()) + "đ" );
        holder.txtarea.setText(job.getAddress());
        Glide.with(context).load(job.getImg()).into(holder.imganh);
        if(job.getStatus_post() == 0){
            holder.txtstatus_post.setText("Đồng ý");
        }else if(job.getStatus_post() == 1){
            holder.txtstatus_post.setText("Đang chờ xác thực");
        }else {
            holder.txtstatus_post.setText("Từ chối");
        }
        holder.imganh.setFocusable(false);
        holder.imganh.setFocusable(false);
        holder.btnReject.setFocusable(false);
        holder.btnConfirm.setFocusable(false);
        holder.btnConfirm.setClickable(false);
        holder.btnReject.setClickable(false);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailJobAdminActivity.class);
                intent.putExtra("job", arrayList.get(position));
                intent.putExtra("position", position); // position trong mảng để load lại
                activity.startActivityForResult(intent, REQUEST_CODE);

            }
        });
        if(job.getStatus_post() != 1){
            holder.btnReject.setClickable(false);
            holder.btnConfirm.setClickable(false);
            holder.btnConfirm.setBackgroundResource(R.drawable.button_black);
            holder.btnReject.setBackgroundResource(R.drawable.button_black);
            holder.btnReject.setTextColor(Color.WHITE);
            holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Bạn đã xác nhận", Toast.LENGTH_SHORT).show();
                }
            });
            holder.btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Bạn đã xác nhận", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            holder.btnConfirm.setBackgroundResource(R.drawable.button_xacnhan);
            holder.btnReject.setBackgroundResource(R.drawable.background_button_view_cv);
            holder.btnReject.setTextColor(Color.BLACK);
            holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptOrRejectJob(0, position);
                }
            });
            holder.btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rejectDialog(position);
                }
            });
        }



    }

    public void rejectDialog(final int position){
        bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_job_reject, (ViewGroup) activity.findViewById(R.id.bottom_sheet));
        final EditText editNote = (EditText) view.findViewById(R.id.editnote);
        Button btnConfirm = (Button) view.findViewById(R.id.buttonxacnhan);
        Button btnCancel = (Button) view.findViewById(R.id.buttonhuy);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editNote.getText().toString().equals("")){
                    Toast.makeText(context, "Vui lòng nhập lý do từ chối", Toast.LENGTH_SHORT).show();
                }else {
//                    loading();
                    note_reject = editNote.getText().toString();
                    acceptOrRejectJob(2, position );
                    bottomSheetDialog.dismiss();
                }
            }
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

    }

    public void acceptOrRejectJob(final int status, final int position){
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlAcceptJob,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            arrayList.get(position).setStatus_post(status);
                            arrayList.get(position).setNote_reject(note_reject);
                            notifyDataSetChanged();

                        }else {
                            Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("job_id", String.valueOf(arrayList.get(position).getId()));
                map.put("status", String.valueOf(status));
                map.put("note_reject", note_reject);
                return map;
            }
        };
        requestQueue.add(stringRequest);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txttencongviec, txttencongty, txtarea, txttime, txtsalary, txtstatus_post;
        public ImageView imganh;
        public Button btnConfirm, btnReject;;
        public LinearLayout layout;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txttencongviec = (TextView) itemView.findViewById(R.id.tencongviec);
            txttencongty = (TextView) itemView.findViewById(R.id.tencongty);
            txtarea = (TextView) itemView.findViewById(R.id.area);
            txtsalary = (TextView) itemView.findViewById(R.id.salary);
            txttime = (TextView) itemView.findViewById(R.id.time);
            imganh = (ImageView) itemView.findViewById(R.id.anh);
            btnConfirm = (Button) itemView.findViewById(R.id.buttonxacnhan);
            btnReject = (Button) itemView.findViewById(R.id.buttontuchoi);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            txtstatus_post = (TextView) itemView.findViewById(R.id.statuspost);

        }
    }

}
