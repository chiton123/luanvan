package com.example.luanvan.ui.Adapter.recruit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.luanvan.ui.Model.JobList;
import com.example.luanvan.ui.recruiter.AdjustJobActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CVManageActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CandidateDocumentFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewPostAdapter extends RecyclerView.Adapter<NewPostAdapter.ItemHolder> {
    Context context;
    ArrayList<JobList> arrayList;
    Activity activity;
    int kind;
    int REQUEST_CODE = 123;

    public NewPostAdapter(Context context, ArrayList<JobList> arrayList, Activity activity, int kind) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.kind = kind;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_tin_tuyen_dung, null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        JobList job = arrayList.get(position);
        holder.txtPosition.setText(job.getName());
        holder.txtIDJob.setText(job.getId() + "");
        Date date1 = null, date2 = null;
        String ngaybatdau = job.getStart_date();
        String ngayketthuc = job.getEnd_date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date1 = fmt.parse(ngaybatdau);
            date2 = fmt.parse(ngayketthuc);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
        holder.txtShowDate.setText(fmtOut.format(date1) + "-" + fmtOut.format(date2));
        holder.txtEndDate.setText(fmtOut.format(date2));
        holder.txtTotalCV.setText(job.getTotalDocument() + "");
        holder.txtNewCV.setText(job.getNew_document() + "");

        holder.btnAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(activity, AdjustJobActivity.class);
                intent1.putExtra("kind", 1); // kind: 0 JoblistFragment, 1: NewPostFragment
                intent1.putExtra("job", arrayList.get(position));
                intent1.putExtra("position", position);
                activity.startActivityForResult(intent1, REQUEST_CODE);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteJob(position);
            }
        });

    }

    public void deleteJob(final int position){
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("Xác nhận");
        alert.setMessage("Bạn có muốn xóa công việc này không ?");
        alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlDeleteJob,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("success")){
                                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
//                                    for(int i = 0; i < CVManageActivity.arrayListAll.size(); i++){
//                                        if(CVManageActivity.arrayListAll.get(i).getJob_id() == arrayList.get(position).getId()){
//                                            Toast.makeText(context, "i "+ i, Toast.LENGTH_SHORT).show();
//                                            CVManageActivity.arrayListAll.remove(i);
//                                            CandidateDocumentFragment.adapter.notifyDataSetChanged();
//                                            i--;
//                                        }
//                                    }
                                    arrayList.remove(position);
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
                        return map;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        alert.show();

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txtPosition, txtShowDate, txtEndDate, txtTotalCV, txtNewCV, txtIDJob;
        public Button btnAdjust;
        public ImageView imgDelete;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtPosition = (TextView) itemView.findViewById(R.id.txtposition);
            txtIDJob = (TextView) itemView.findViewById(R.id.txtjobid);
            txtShowDate = (TextView) itemView.findViewById(R.id.txtdate);
            txtEndDate = (TextView) itemView.findViewById(R.id.txtend);
            txtTotalCV = (TextView) itemView.findViewById(R.id.txttotalcv);
            txtNewCV = (TextView) itemView.findViewById(R.id.txtnewcv);
            btnAdjust = (Button) itemView.findViewById(R.id.buttonadjust);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgdelete);

        }
    }

}
