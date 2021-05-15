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
import android.widget.Filter;
import android.widget.Filterable;
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
import com.example.luanvan.ui.Model.JobList;
import com.example.luanvan.ui.recruiter.AdjustJobActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CVManageActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CandidateDocumentFragment;
import com.example.luanvan.ui.recruiter.PostNews.AuthenticationFragment;
import com.example.luanvan.ui.recruiter.PostNews.DisplayJobFragment;
import com.example.luanvan.ui.recruiter.PostNews.OutdatedJobFragment;
import com.example.luanvan.ui.recruiter.PostNews.RejectJobFragment;
import com.example.luanvan.ui.recruiter.RecruiterActivity;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewPostAdapter extends RecyclerView.Adapter<NewPostAdapter.ItemHolder> implements Filterable {
    Context context;
    ArrayList<JobList> filterArraylist;
    ArrayList<JobList> nameList;
    Activity activity;
    int kind;
    int REQUEST_CODE = 123;
    int fragment; // 1: Đang hiển thị, 2 : Chờ xác thực, 3: Hết hạn, 4: Từ chối , khi chuyển qua bên adjustJob thì cập nhật tương ứng với fragment

    public NewPostAdapter(Context context, ArrayList<JobList> arrayList, Activity activity, int kind, int fragment) {
        this.context = context;
        this.filterArraylist = arrayList;
        this.nameList = arrayList;
        this.activity = activity;
        this.kind = kind;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_tin_tuyen_dung, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        JobList job = filterArraylist.get(position);
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
        if(fragment == 4){
            holder.layout_reject.setVisibility(View.VISIBLE);
            holder.txtReason.setText(job.getNote_reject());
        }
        holder.btnAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(activity, AdjustJobActivity.class);
                intent1.putExtra("kind", kind); // kind: 0 JoblistFragment, 1: NewPostFragment
                intent1.putExtra("job", filterArraylist.get(position));
                intent1.putExtra("position", position);
                intent1.putExtra("fragment", fragment);
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
                                    filterArraylist.remove(position);
                                    notifyDataSetChanged();
                                    switch (fragment){
                                        case 1:
                                            checkNothingDisplayJob();
                                            break;
                                        case 2:
                                            checkNothingAuthencation();
                                            break;
                                        case 3:
                                            checkNothingOutdatedJob();
                                            break;
                                        case 4:
                                            checkNothingRejectJob();
                                            break;

                                    }

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
                        map.put("job_id", String.valueOf(filterArraylist.get(position).getId()));
                        return map;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        alert.show();

    }
    public void checkNothingAuthencation(){
        if(RecruiterActivity.arrayListAuthenticationJobs.size() == 0){
            AuthenticationFragment.layout.setVisibility(View.GONE);
            AuthenticationFragment.layout_nothing.setVisibility(View.VISIBLE);
        }else {
            AuthenticationFragment.layout_nothing.setVisibility(View.GONE);
            AuthenticationFragment.layout.setVisibility(View.VISIBLE);
        }
    }
    public void checkNothingOutdatedJob(){
        if(RecruiterActivity.arrayListOutdatedJobs.size() == 0){
            OutdatedJobFragment.layout.setVisibility(View.GONE);
            OutdatedJobFragment.layout_nothing.setVisibility(View.VISIBLE);
        }else {
            OutdatedJobFragment.layout_nothing.setVisibility(View.GONE);
            OutdatedJobFragment.layout.setVisibility(View.VISIBLE);
        }
    }

    public void checkNothingDisplayJob(){
        if(RecruiterActivity.arrayListJobList.size() == 0){
            DisplayJobFragment.layout.setVisibility(View.GONE);
            DisplayJobFragment.layout_nothing.setVisibility(View.VISIBLE);
        }else {
            DisplayJobFragment.layout_nothing.setVisibility(View.GONE);
            DisplayJobFragment.layout.setVisibility(View.VISIBLE);
        }
    }

    public void checkNothingRejectJob(){
        if(RecruiterActivity.arrayListRejectJobs.size() == 0){
            RejectJobFragment.layout.setVisibility(View.GONE);
            RejectJobFragment.layout_nothing.setVisibility(View.VISIBLE);
        }else {
            RejectJobFragment.layout_nothing.setVisibility(View.GONE);
            RejectJobFragment.layout.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return filterArraylist.size();
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
                    ArrayList<JobList> filteredList = new ArrayList<>();
                    for(JobList jobList : nameList){
                        String name1 = stripAccents(jobList.getName()).trim();
                        if(name1.toLowerCase().contains(charSequenceString.toLowerCase())){
                            filteredList.add(jobList);
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
                filterArraylist = (ArrayList<JobList>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txtPosition, txtShowDate, txtEndDate, txtTotalCV, txtNewCV, txtIDJob, txtReason;
        public Button btnAdjust;
        public ImageView imgDelete;
        public LinearLayout layout_reject;
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
            layout_reject = (LinearLayout) itemView.findViewById(R.id.layout_reject);
            txtReason = (TextView) itemView.findViewById(R.id.txtreason);

        }
    }
    public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

}
