package com.example.luanvan.ui.Adapter.recruit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.example.luanvan.ui.Model.Applicant;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.Profile;
import com.example.luanvan.ui.UpdateInfo.ExperienceActivity;
import com.example.luanvan.ui.UpdateInfo.SkillActivity;
import com.example.luanvan.ui.UpdateInfo.StudyActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CVManageActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CVManagementActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CandidateInfoActivity;
import com.example.luanvan.ui.recruiter.CVManagement.JobListFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileCadidateAdapter extends RecyclerView.Adapter<ProfileCadidateAdapter.ItemtHolder> {
    Context context;
    ArrayList<Profile> arrayList;
    Activity activity;
    Applicant applicant;
    int statusApplication = 0;
    int positionX; // vị trí arraylist của cvmanagmentactivity
    String note = "";
    int kind;
    int first_status = 0; // chưa làm gì

    public ProfileCadidateAdapter(Context context, ArrayList<Profile> arrayList, Activity activity, Applicant applicant, int kind, int positionX) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.applicant = applicant;
        this.kind = kind;
        this.positionX = positionX;
        this.first_status = applicant.getStatus();
    }

    @NonNull
    @Override
    public ItemtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_profile_candidate, null);
        ItemtHolder itemtHolder = new ItemtHolder(view);
        return itemtHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemtHolder holder, final int position) {
        final Profile profile = arrayList.get(position);
        holder.txtname.setText(profile.getName());
        holder.img.setImageResource(profile.getImg());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (profile.getId()){
                    case 1:
                        showDialogAccessCandidate(positionX);
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                }
            }
        });
    }
    public void showDialogAccessCandidate(final int position){
        final Dialog dialog = new Dialog(activity);
        dialog.setTitle("Đánh giá ứng viên");
        dialog.setContentView(R.layout.dialog_access_candidate);
        dialog.setCancelable(false);
        TextView txtName = (TextView) dialog.findViewById(R.id.name);
        final EditText editNote = (EditText) dialog.findViewById(R.id.editnote);
        Button btnUpdate = (Button) dialog.findViewById(R.id.buttoncapnhat);
        Button btnCancel = (Button) dialog.findViewById(R.id.buttonhuy);
        editNote.setText(applicant.getNote());
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note = editNote.getText().toString();
                updateStatus(position);
                dialog.dismiss();
            }
        });
        Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
        txtName.setText(applicant.getUsername());
        final String[] mang = new String[] {"Chưa đánh giá","Đạt yêu cầu","Không đạt yêu cầu","Chưa liên hệ","Đạt phỏng vấn"
                ,"Không đạt phỏng vấn","Đã thông báo kết quả","Đã đến nhận việc","Từ chối nhận việc"};
        // lọc CV: 0 chưa đánh giá, 1: đạt yêu cầu, 2: không đạt yêu cầu
        // phỏng vấn: 3: chưa liên hệ, 4: đạt phỏng vấn , 5: không đạt phỏng vấn
        // nhận việc: 6: đã thông báo kết quả, 7: đã đến nhận việc, 8: từ chối nhận việc
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, mang);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                //Toast.makeText(context, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                statusApplication = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dialog.show();
    }
    public void updateStatus(final int position){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlUpdateApplication,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Toast.makeText(context, "Cập nhật thành công ", Toast.LENGTH_SHORT).show();
                            applicant.setStatus(statusApplication);
                            applicant.setNote(note);

                            if(kind == 1){
                                if(first_status == 2){
                                    // skip
                                    if(statusApplication == 3 || statusApplication == 4 || statusApplication == 5){
                                        CVManagementActivity.arrayListInterView.add(applicant);
                                        CVManagementActivity.arrayListCVFilter.remove(position);

                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setSkip(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getSkip() -1);
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setInterview(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getInterview() + 1);
                                    }else if(statusApplication > 5){
                                        CVManagementActivity.arrayListGoToWork.add(applicant);
                                        CVManagementActivity.arrayListCVFilter.remove(position);
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setWork(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getWork() + 1);
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setSkip(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getSkip() -1);

                                    }else if(statusApplication == 1 || statusApplication == 0){
                                        CVManagementActivity.arrayListCVFilter.set(positionX, applicant);
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setNew_document(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getNew_document() + 1);
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setSkip(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getSkip() -1);
                                    }else {
                                        CVManagementActivity.arrayListCVFilter.set(positionX, applicant);
                                    }

                                }else {
                                    // new document
                                    if(statusApplication == 3 || statusApplication == 4 || statusApplication == 5){
                                        CVManagementActivity.arrayListInterView.add(applicant);
                                        CVManagementActivity.arrayListCVFilter.remove(position);

                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setNew_document(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getNew_document() -1);
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setInterview(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getInterview() + 1);
                                    }else if(statusApplication > 5){
                                        CVManagementActivity.arrayListGoToWork.add(applicant);
                                        CVManagementActivity.arrayListCVFilter.remove(position);
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setWork(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getWork() + 1);
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setNew_document(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getNew_document() -1);

                                    }else if(statusApplication == 2) {
                                        CVManagementActivity.arrayListCVFilter.set(positionX, applicant);
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setSkip(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getSkip()  + 1);
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setNew_document(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getNew_document() -1);
                                    }
                                    else {
                                        CVManagementActivity.arrayListCVFilter.set(positionX, applicant);
                                    }
                                }

                            }

                            if(kind == 2){
                                if(statusApplication < 3){
                                    CVManagementActivity.arrayListCVFilter.add(applicant);
                                    CVManagementActivity.arrayListInterView.remove(position);
                                    if(statusApplication == 2){
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setSkip(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getSkip()  + 1);
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setInterview(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getInterview() - 1);
                                    }else {
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setNew_document(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getNew_document()  + 1);
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setInterview(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getInterview() - 1);
                                    }


                                }else if(statusApplication > 5){
                                    CVManagementActivity.arrayListGoToWork.add(applicant);
                                    CVManagementActivity.arrayListInterView.remove(position);
                                    CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setInterview(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getInterview() - 1);
                                    CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setWork(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getWork() + 1);
                                }else {
                                    CVManagementActivity.arrayListInterView.set(positionX, applicant);
                                }

                            }
                            if(kind == 3){
                                if(statusApplication < 6 && statusApplication > 2){
                                    CVManagementActivity.arrayListInterView.add(applicant);
                                    CVManagementActivity.arrayListGoToWork.remove(position);
                                    CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setInterview(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getInterview()  + 1);
                                    CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setWork(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getWork() - 1);


                                }else if(statusApplication < 3){
                                    CVManagementActivity.arrayListCVFilter.add(applicant);
                                    CVManagementActivity.arrayListGoToWork.remove(position);
                                    if(statusApplication == 2){
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setSkip(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getSkip()  + 1);
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setWork(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getWork() - 1);
                                    }else {
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setNew_document(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getNew_document()  + 1);
                                        CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setWork(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getWork() - 1);
                                    }


                                }else {
                                    CVManagementActivity.arrayListGoToWork.set(positionX, applicant);
                                }

                            }

                            notifyDataSetChanged();
                            JobListFragment.adapter.notifyDataSetChanged();

                            Intent intent = new Intent();
                            intent.putExtra("kind", kind);
                            intent.putExtra("status", statusApplication);
                            activity.setResult(123, intent);
                            activity.finish();

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
                map.put("ap_id", String.valueOf(applicant.getId()));
                map.put("status", String.valueOf(statusApplication));
                map.put("note",  note);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemtHolder extends RecyclerView.ViewHolder{
        public TextView txtname;
        public ImageView img;
        public ItemtHolder(@NonNull View itemView) {
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.name);
            img = (ImageView) itemView.findViewById(R.id.hinh);


        }
    }

}
