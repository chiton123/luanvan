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
import com.example.luanvan.ui.fragment.recruting.CVFilterFragment;
import com.example.luanvan.ui.fragment.recruting.GoToWorkFragment;
import com.example.luanvan.ui.fragment.recruting.InterviewFragment;
import com.example.luanvan.ui.recruiter.AdjustJobActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CVManageActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CVManagementActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CandidateDocumentFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.ItemHolder> {
    Context context;
    ArrayList<JobList> arrayList;
    Activity activity;
    int REQUEST_CODE = 123;
    // kiểm tra xem có quá ngày hay không, nếu quá ngày thì k dc tiếp tục tuyển

    public PositionAdapter(Context context, ArrayList<JobList> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dong_vi_tri, null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
        JobList job = arrayList.get(position);
        Date date1 = null;
        Date date2 = null;
        if(job != null){
            holder.txtName.setText(job.getName());
            String ngaybatdau = job.getStart_date();
            String ngayketthuc = job.getEnd_date();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

            try {
                date1 = fmt.parse(ngaybatdau);
                date2 = fmt.parse(ngayketthuc);
                if(date2.after(Calendar.getInstance().getTime())){
                    if(job.getStatus() == 1){
                        holder.txtStatus.setText("Ngưng tuyển dụng");
                    }else {
                        holder.txtStatus.setText("Đang tuyển dụng");
                    }
                }else {
                    holder.txtStatus.setText("Ngưng tuyển dụng");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
            holder.txtDate.setText(fmtOut.format(date1) + "-" + fmtOut.format(date2));

        }
        final Date finalDate = date2;
        // status:  dừng tuyển 1, đang tuyển 0
        holder.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CVManagementActivity.class);
                intent.putExtra("job_id", arrayList.get(position).getId());
                intent.putExtra("position", position); // position của danh sách công việc
                activity.startActivity(intent);
            }
        });
        holder.txtDocument.setText(job.getTotalDocument() + "");
        holder.txtNewDocument.setText(job.getNew_document() + "");
        holder.txtInterview.setText(job.getInterview() + "");
        holder.txtSkip.setText(job.getSkip() + "");
        holder.txtGotoWork.setText(job.getWork() + "");
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteJob(position);
            }
        });
        holder.btnAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(activity, AdjustJobActivity.class);
                intent1.putExtra("job", arrayList.get(position));
                intent1.putExtra("position", position);
                activity.startActivityForResult(intent1, REQUEST_CODE);
            }
        });
        int statusJob = arrayList.get(position).getStatus();
        if(statusJob == 0){
            if(finalDate.after(Calendar.getInstance().getTime())){
                holder.btnEnd.setText("Dừng tuyển");
            }else {
                holder.btnEnd.setText("Tiếp tục tuyển");
            }

        }else{
            holder.btnEnd.setText("Tiếp tục tuyển");
        }
        holder.btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle("Xác nhận");
                alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                if(arrayList.get(position).getStatus() == 1){
                    if(finalDate.after(Calendar.getInstance().getTime())){
                        alert.setMessage("Bạn có muốn tiếp tục tuyển cho vị trí này không ?");
                        alert.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                endRecruiting(position, 0);
                                notifyDataSetChanged();
                            }
                        });
                        alert.show();
                    }else {
                        Toast.makeText(context, "Ngày hết hạn đã trước ngày hiện tại, không thể tuyển được nữa", Toast.LENGTH_SHORT).show();
                    }

                }else {

                    if(finalDate.after(Calendar.getInstance().getTime())){
                        alert.setMessage("Bạn có muốn ngưng tuyển cho vị trí này không ?");
                        alert.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                endRecruiting(position, 1);
                                notifyDataSetChanged();
                            }
                        });
                        alert.show();
                    }else {
                        Toast.makeText(context, "Ngày hết hạn đã trước ngày hiện tại, không thể tuyển được nữa", Toast.LENGTH_SHORT).show();
                    }

                }
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
                                    for(int i=0; i < CVManageActivity.arrayListAll.size(); i++){
                                        if(CVManageActivity.arrayListAll.get(i).getJob_id() == arrayList.get(position).getId()){
                                            Toast.makeText(context, "i "+ i, Toast.LENGTH_SHORT).show();
                                            CVManageActivity.arrayListAll.remove(i);
                                            CandidateDocumentFragment.adapter.notifyDataSetChanged();
                                            i--;
                                        }
                                    }
                                    arrayList.remove(position);
                                    notifyDataSetChanged();
                                    infoNothing();

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

    public void endRecruiting(final int position, final int status){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlStartEndRecruiting,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            arrayList.get(position).setStatus(status);
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
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void infoNothing(){
        // khi xóa thì xét coi có = 0 -> thông báo là k có ứng viên
        // candidateDocument
        if(CVManageActivity.arrayListAll.size() == 0){
            CandidateDocumentFragment.layout_nothing.setVisibility(View.VISIBLE);
            CandidateDocumentFragment.layout.setVisibility(View.GONE);
        }else {
            CandidateDocumentFragment.layout_nothing.setVisibility(View.GONE);
            CandidateDocumentFragment.layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txtName, txtDate, txtStatus, txtDocument, txtNewDocument, txtInterview, txtGotoWork, txtSkip;
        public Button btnAdjust, btnShow, btnEnd;
        public ImageView imgDelete;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.name);
            txtDate = (TextView) itemView.findViewById(R.id.date);
            txtStatus = (TextView) itemView.findViewById(R.id.status);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgdelete);
            btnAdjust = (Button) itemView.findViewById(R.id.buttonchinhsua);
            btnEnd = (Button) itemView.findViewById(R.id.buttondungtuyen);
            btnShow = (Button) itemView.findViewById(R.id.buttonxem);
            txtDocument = (TextView) itemView.findViewById(R.id.txthoso);
            txtNewDocument = (TextView) itemView.findViewById(R.id.txthosomoi);
            txtInterview = (TextView) itemView.findViewById(R.id.txtphongvan);
            txtGotoWork = (TextView) itemView.findViewById(R.id.txtdilam);
            txtSkip = (TextView) itemView.findViewById(R.id.txtloai);

        }
    }

}
