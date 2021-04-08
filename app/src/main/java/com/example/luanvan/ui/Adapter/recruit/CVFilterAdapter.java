package com.example.luanvan.ui.Adapter.recruit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import com.example.luanvan.ui.Model.Applicant;
import com.example.luanvan.ui.Model.JobList;
import com.example.luanvan.ui.fragment.recruting.CVFilterFragment;
import com.example.luanvan.ui.fragment.recruting.GoToWorkFragment;
import com.example.luanvan.ui.fragment.recruting.InterviewFragment;
import com.example.luanvan.ui.recruiter.CVManagement.CVManageActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CVManagementActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CandidateDocumentFragment;
import com.example.luanvan.ui.recruiter.CVManagement.CandidateInfoActivity;
import com.example.luanvan.ui.recruiter.CVManagement.JobListFragment;
import com.example.luanvan.ui.recruiter.RecruiterActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CVFilterAdapter extends RecyclerView.Adapter<CVFilterAdapter.ItemHolder> {
    Context context;
    ArrayList<Applicant> arrayList;
    Activity activity;
    // để biết vị trí position của danh sách vị trí, để load dữ liệu số hồ sơ, loại,...
    ArrayList<JobList> arrayListJobList;
    int kind;
    int REQUEST_CODE = 123;
    int statusApplication = 0;
    String note = "";
    int positionJobList = 0; // để xóa


    public CVFilterAdapter(Context context, ArrayList<Applicant> arrayList, Activity activity, int kind, ArrayList<JobList> arrayListJobList) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.kind = kind;
        this.arrayListJobList = arrayListJobList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_loc_cv, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }
    public void updateStatus(final int position){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlUpdateApplication,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            arrayList.get(position).setStatus(statusApplication);
                            if(kind == 1){
                                if(statusApplication == 3 || statusApplication == 4 || statusApplication == 5){
                                    ((CVManagementActivity) activity).reloadInterview();
                                    ((CVManagementActivity) activity).reloadFilterCV();
                                }
                                if(statusApplication > 5){
                                    ((CVManagementActivity) activity).reloadGoToWork();
                                    ((CVManagementActivity) activity).reloadFilterCV();
                                }
                            }

                            if(kind == 2){
                                if(statusApplication < 3){
                                    ((CVManagementActivity) activity).reloadInterview();
                                    ((CVManagementActivity) activity).reloadFilterCV();
                                }
                                if(statusApplication > 5){
                                    ((CVManagementActivity) activity).reloadGoToWork();
                                    ((CVManagementActivity) activity).reloadInterview();
                                }

                            }
                            if(kind == 3){
                                if(statusApplication < 6 && statusApplication > 2){
                                    ((CVManagementActivity) activity).reloadInterview();
                                    ((CVManagementActivity) activity).reloadGoToWork();
                                }
                                if(statusApplication < 3){
                                    ((CVManagementActivity) activity).reloadGoToWork();
                                    ((CVManagementActivity) activity).reloadFilterCV();
                                }

                            }


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
                map.put("ap_id", String.valueOf(arrayList.get(position).getId()));
                map.put("status", String.valueOf(statusApplication));
                map.put("note",  note);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
        Applicant applicant = arrayList.get(position);
        holder.txtName.setText(applicant.getUsername());
        holder.txtEmail.setText("Email : " + applicant.getEmail());
        holder.txtPhone.setText("Số điện thoại: " + applicant.getPhone());
        holder.txtPosition.setText("Vị trí tuyển dụng: " + applicant.getJob_name());
        String ngay = applicant.getDate();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = fmt.parse(ngay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        holder.txtDate.setText("Thời gian: " + fmtOut.format(date));
        final int status = applicant.getStatus();
        switch (status){
            case 0:
                holder.txtStatus.setText("Chưa đánh giá");
                holder.txtRound.setText("Lọc CV");
                break;
            case 1:
                holder.txtStatus.setText("Đạt yêu cầu");
                holder.txtRound.setText("Lọc CV");
                break;
            case 2:
                holder.txtStatus.setText("Không đạt yêu cầu");
                holder.txtRound.setText("Lọc CV");
                break;
            case 3:
                holder.txtStatus.setText("Chưa liên hệ");
                holder.txtRound.setText("Phỏng vấn");
                break;
            // lọc CV: 0 chưa đánh giá, 1: đạt yêu cầu, 2: không đạt yêu cầu
            // phỏng vấn : 3: Chưa liên hệ, 4: Không liên hệ được , 5: Đồng ý phỏng vấn, 6:Từ chối phỏng vấn, 7 Đến phỏng vấn, 8 Không đến phỏng vấn,
            // 9: Lùi lịch phỏng vấn, 10 Đạt phỏng vấn, 11: Không đạt phỏng vấn
            // nhận việc: 12: Đã thông báo kết quả, 13: Đã đến nhận việc, 14: Từ chối nhận việc
            case 4:
                holder.txtStatus.setText("Không liên hệ được");
                holder.txtRound.setText("Phỏng vấn");
                break;
            case 5:
                holder.txtStatus.setText("Đồng ý phỏng vấn");
                holder.txtRound.setText("Phỏng vấn");
                break;
            case 6:
                holder.txtStatus.setText("Từ chối phỏng vấn");
                holder.txtRound.setText("Phỏng vấn");
                break;
            case 7:
                holder.txtStatus.setText("Đến phỏng vấn");
                holder.txtRound.setText("Phỏng vấn");
                break;
            case 8:
                holder.txtStatus.setText("Không đến phỏng vấn");
                holder.txtRound.setText("Phỏng vấn");
                break;
            case 9:
                holder.txtStatus.setText("Lùi lịch phỏng vấn");
                holder.txtRound.setText("Phỏng vấn");
                break;
            case 10:
                holder.txtStatus.setText("Đạt phỏng vấn");
                holder.txtRound.setText("Phỏng vấn");
                break;
            case 11:
                holder.txtStatus.setText("Không đạt phỏng vấn");
                holder.txtRound.setText("Phỏng vấn");
                break;
            case 12:
                holder.txtStatus.setText("Đã thông báo kết quả");
                holder.txtRound.setText("Nhận việc");
                break;
            case 13:
                holder.txtStatus.setText("Đã đến nhận việc");
                holder.txtRound.setText("Nhận việc");
                break;
            case 14:
                holder.txtStatus.setText("Từ chối nhận việc");
                holder.txtRound.setText("Nhận việc");
                break;

        }
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle("Xác nhận");
                alert.setMessage("Bạn có chắc chắn xóa CV này không ?" );
                alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteApplicant(position);
                    }
                });
                alert.show();

            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CandidateInfoActivity.class);
                intent.putExtra("applicant", arrayList.get(position));
                intent.putExtra("kind", kind);
                intent.putExtra("position", position);
                activity.startActivityForResult(intent, REQUEST_CODE);
            }
        });


    }
    public void deleteApplicant(final int position){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlDeleteApplication,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            int status = arrayList.get(position).getStatus();


                            if(kind != 0){
                                RecruiterActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setTotalDocument(RecruiterActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getTotalDocument() - 1);
                                if(status == 0 || status == 1){
                                    RecruiterActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setNew_document(RecruiterActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getNew_document() - 1);
                                }else if(status == 2){
                                    RecruiterActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setSkip(RecruiterActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getSkip() -1);
                                }else if(status >= 3 && status <= 11){
                                    RecruiterActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setInterview(RecruiterActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getInterview() -1);
                                }else {
                                    RecruiterActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setWork(RecruiterActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getWork() -1);
                                }
                                // xóa bên kia luôn
                                int x = 0;
                                for(int i=0; i < CVManageActivity.arrayListAll.size(); i++){
                                    if(CVManageActivity.arrayListAll.get(i).getId() == arrayList.get(position).getId()){
                                        x = i;
                                    }
                                }
                                CVManageActivity.arrayListAll.remove(x); // bên hồ sơ ứng tuyển
                                CandidateDocumentFragment.adapter.notifyDataSetChanged();


                            }else {
                                for(int i=0; i < RecruiterActivity.arrayListJobList.size(); i++){
                                    if(RecruiterActivity.arrayListJobList.get(i).getId() == arrayList.get(position).getJob_id()){
                                        positionJobList = i;
                                    }
                                }
                                RecruiterActivity.arrayListJobList.get(positionJobList).setTotalDocument(RecruiterActivity.arrayListJobList.get(positionJobList).getTotalDocument() - 1);
                                if(status == 0 || status == 1){
                                    RecruiterActivity.arrayListJobList.get(positionJobList).setNew_document(RecruiterActivity.arrayListJobList.get(positionJobList).getNew_document() - 1);
                                }else if(status == 2){
                                    RecruiterActivity.arrayListJobList.get(positionJobList).setSkip(RecruiterActivity.arrayListJobList.get(positionJobList).getSkip() -1);
                                }else if(status >= 3 && status <= 11){
                                    RecruiterActivity.arrayListJobList.get(positionJobList).setInterview(RecruiterActivity.arrayListJobList.get(positionJobList).getInterview() -1);
                                }else {
                                    RecruiterActivity.arrayListJobList.get(positionJobList).setWork(RecruiterActivity.arrayListJobList.get(positionJobList).getWork() -1);
                                }


                            }

                            JobListFragment.adapter.notifyDataSetChanged();
                            CandidateDocumentFragment.adapter.notifyDataSetChanged();
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
                map.put("ap_id", String.valueOf(arrayList.get(position).getId()));
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
        if(kind == 1){
            if(CVManageActivity.arrayListCVFilter.size() == 0){
                CVFilterFragment.layout_nothing.setVisibility(View.VISIBLE);
                CVFilterFragment.layout.setVisibility(View.GONE);
            }else {
                CVFilterFragment.layout_nothing.setVisibility(View.GONE);
                CVFilterFragment.layout.setVisibility(View.VISIBLE);
            }
        }

        if(kind == 2){
            if(CVManageActivity.arrayListInterView.size() == 0){
                InterviewFragment.layout_nothing.setVisibility(View.VISIBLE);
                InterviewFragment.layout.setVisibility(View.GONE);
            }else {
                InterviewFragment.layout_nothing.setVisibility(View.GONE);
                InterviewFragment.layout.setVisibility(View.VISIBLE);
            }
        }

        if(kind == 3){
            if(CVManageActivity.arrayListGoToWork.size() == 0){
                GoToWorkFragment.layout_nothing.setVisibility(View.VISIBLE);
                GoToWorkFragment.layout.setVisibility(View.GONE);
            }else {
                GoToWorkFragment.layout_nothing.setVisibility(View.GONE);
                GoToWorkFragment.layout.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txtName, txtEmail, txtStatus, txtRound, txtPosition, txtPhone, txtDate;
        public CardView cardView;
        public ImageView imgDelete;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.cadidateName);
            txtEmail = (TextView) itemView.findViewById(R.id.email);
            txtStatus = (TextView) itemView.findViewById(R.id.status);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            txtRound = (TextView) itemView.findViewById(R.id.round);
            txtPhone = (TextView) itemView.findViewById(R.id.phone);
            txtPosition = (TextView) itemView.findViewById(R.id.textviewposition);
            txtDate = (TextView) itemView.findViewById(R.id.textviewtime);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgdelete);

        }
    }
}
