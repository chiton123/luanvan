package com.example.luanvan.ui.Adapter.recruit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.luanvan.ui.Model.Profile;
import com.example.luanvan.ui.fragment.recruting.CVFilterFragment;
import com.example.luanvan.ui.fragment.recruting.GoToWorkFragment;
import com.example.luanvan.ui.fragment.recruting.InterviewFragment;
import com.example.luanvan.ui.recruiter.CVManagement.CVManageActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CVManagementActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CandidateDocumentFragment;
import com.example.luanvan.ui.recruiter.CVManagement.JobListFragment;
import com.example.luanvan.ui.schedule.ScheduleActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileCadidateAdapter extends RecyclerView.Adapter<ProfileCadidateAdapter.ItemtHolder> {
    Context context;
    ArrayList<Profile> arrayList;
    Activity activity;
    Applicant applicant;
    int statusApplication = 0;
    int positionX; // vị trí arraylist của cvmanagmentactivity 3 cái cvfilter, interview, work
    String note = "";
    int kind;
    int first_status = 0; // chưa làm gì
    int job_id = 0; // để cập nhật dữ liệu bên joblistFragment
    int positionJobList = 0; // cập nhật tại vị trí trùng job_id
    String url_cv = ""; // url cv
    String name_cv = "";
    public ProfileCadidateAdapter(Context context, ArrayList<Profile> arrayList, Activity activity, Applicant applicant, int kind, int positionX,
                                  String url_cv, String name_cv) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.applicant = applicant;
        this.kind = kind;
        this.positionX = positionX;
        this.first_status = applicant.getStatus();
        this.url_cv = url_cv;
        this.name_cv = name_cv;
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
                        Intent intent = new Intent(activity, ScheduleActivity.class);
                        intent.putExtra("applicant", applicant);
                        activity.startActivity(intent);
                        break;
                    case 3:
                        //Toast.makeText(context, url_cv, Toast.LENGTH_SHORT).show();
                        downLoadCV_PDF(url_cv);
                        break;
                }
            }
        });
    }
    public void downLoadCV_PDF(String url){
        StorageReference reference = MainActivity.storage.getReferenceFromUrl(url);
        File rootPath = new File(Environment.getExternalStorageDirectory(), "CVdownload");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }
        final File localFile = new File(rootPath,name_cv + "_TOPCV" + ".pdf");
        reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Tải về thành công", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "Tải về thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDialogAccessCandidate(final int position){
        job_id = applicant.getJob_id();
        for(int i=0; i < CVManageActivity.arrayListJobList.size(); i++){
            if(CVManageActivity.arrayListJobList.get(i).getId() == job_id){
                positionJobList = i;
            }
        }
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
        // lọc CV: 0 chưa đánh giá, 1: đạt yêu cầu, 2: không đạt yêu cầu
        // phỏng vấn : 3: Chưa liên hệ, 4: Không liên hệ được , 5: Đồng ý phỏng vấn, 6:Từ chối phỏng vấn, 7 Đến phỏng vấn, 8 Không đến phỏng vấn,
        // 9: Lùi lịch phỏng vấn, 10 Đạt phỏng vấn, 11: Không đạt phỏng vấn
        // nhận việc: 12: Đã thông báo kết quả, 13: Đã đến nhận việc, 14: Từ chối nhận việc
        final String[] mang = new String[] {
                "Chưa đánh giá",
                "Đạt yêu cầu",
                "Không đạt yêu cầu",
                "Chưa liên hệ",
                "Không liên hệ được",
                "Đồng ý phỏng vấn",
                "Từ chối phỏng vấn",
                "Đến phỏng vấn",
                "Không đến phỏng vấn",
                "Lùi lịch phỏng vấn",
                "Đạt phỏng vấn"
                ,"Không đạt phỏng vấn",
                "Đã thông báo kết quả",
                "Đã đến nhận việc",
                "Từ chối nhận việc"};

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

    public void kindOne(final int position){
        if(first_status == 2){
            // skip
            if(statusApplication >= 3 && statusApplication <= 11){
                CVManageActivity.arrayListInterView.add(applicant);
                CVManageActivity.arrayListCVFilter.remove(position);

                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setSkip(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getSkip() -1);
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setInterview(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getInterview() + 1);
            }else if(statusApplication >= 12){
                CVManageActivity.arrayListGoToWork.add(applicant);
                CVManageActivity.arrayListCVFilter.remove(position);
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setWork(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getWork() + 1);
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setSkip(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getSkip() -1);

            }else if(statusApplication == 1 || statusApplication == 0){
                CVManageActivity.arrayListCVFilter.set(positionX, applicant);
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setNew_document(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getNew_document() + 1);
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setSkip(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getSkip() -1);
            }else {
                CVManageActivity.arrayListCVFilter.set(positionX, applicant);
            }

        }else {
            // new document
            if(statusApplication >= 3 && statusApplication <= 11){
                CVManageActivity.arrayListInterView.add(applicant);
                CVManageActivity.arrayListCVFilter.remove(position);

                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setNew_document(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getNew_document() -1);
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setInterview(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getInterview() + 1);
            }else if(statusApplication >= 12){
                CVManageActivity.arrayListGoToWork.add(applicant);
                CVManageActivity.arrayListCVFilter.remove(position);
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setWork(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getWork() + 1);
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setNew_document(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getNew_document() -1);

            }else if(statusApplication == 2) {
                CVManageActivity.arrayListCVFilter.set(positionX, applicant);
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setSkip(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getSkip()  + 1);
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setNew_document(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getNew_document() -1);
            }
            else {
                CVManageActivity.arrayListCVFilter.set(positionX, applicant);
            }
        }

    }
    public void kindTwo(final int position){
        if(statusApplication <= 2){
            CVManageActivity.arrayListCVFilter.add(applicant);
            CVManageActivity.arrayListInterView.remove(position);
            if(statusApplication == 2){
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setSkip(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getSkip()  + 1);
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setInterview(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getInterview() - 1);
            }else {
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setNew_document(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getNew_document()  + 1);
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setInterview(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getInterview() - 1);
            }


        }else if(statusApplication >= 12){
            CVManageActivity.arrayListGoToWork.add(applicant);
            CVManageActivity.arrayListInterView.remove(position);
            CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setInterview(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getInterview() - 1);
            CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setWork(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getWork() + 1);
        }else {
            CVManageActivity.arrayListInterView.set(positionX, applicant);
        }

    }
    public void kindThree(final int position){
        if(statusApplication >= 3 && statusApplication <= 11){
            CVManageActivity.arrayListInterView.add(applicant);
            CVManageActivity.arrayListGoToWork.remove(position);
            CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setInterview(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getInterview()  + 1);
            CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setWork(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getWork() - 1);


        }else if(statusApplication <= 2){
            CVManageActivity.arrayListCVFilter.add(applicant);
            CVManageActivity.arrayListGoToWork.remove(position);
            if(statusApplication == 2){
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setSkip(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getSkip()  + 1);
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setWork(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getWork() - 1);
            }else {
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setNew_document(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getNew_document()  + 1);
                CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).setWork(CVManageActivity.arrayListJobList.get(CVManagementActivity.position_job_list).getWork() - 1);
            }


        }else {
            CVManageActivity.arrayListGoToWork.set(positionX, applicant);
        }

    }
    // cho phần hồ sơ tuyển dụng
    public void kindOne2(final int position){
      //  Toast.makeText(context, "Cập nhật thành công 1", Toast.LENGTH_SHORT).show();
        if(first_status == 2){
            // skip
            if(statusApplication >= 3 && statusApplication <= 11){
                CVManageActivity.arrayListJobList.get(positionJobList).setSkip(CVManageActivity.arrayListJobList.get(positionJobList).getSkip() -1);
                CVManageActivity.arrayListJobList.get(positionJobList).setInterview(CVManageActivity.arrayListJobList.get(positionJobList).getInterview() + 1);
            }else if(statusApplication >=  12){
                CVManageActivity.arrayListJobList.get(positionJobList).setWork(CVManageActivity.arrayListJobList.get(positionJobList).getWork() + 1);
                CVManageActivity.arrayListJobList.get(positionJobList).setSkip(CVManageActivity.arrayListJobList.get(positionJobList).getSkip() -1);

            }else if(statusApplication == 1 || statusApplication == 0){
                CVManageActivity.arrayListJobList.get(positionJobList).setNew_document(CVManageActivity.arrayListJobList.get(positionJobList).getNew_document() + 1);
                CVManageActivity.arrayListJobList.get(positionJobList).setSkip(CVManageActivity.arrayListJobList.get(positionJobList).getSkip() -1);
            }else {

            }

        }else {
            // new document
            if(statusApplication >= 3 && statusApplication <= 11){
                CVManageActivity.arrayListJobList.get(positionJobList).setNew_document(CVManageActivity.arrayListJobList.get(positionJobList).getNew_document() -1);
                CVManageActivity.arrayListJobList.get(positionJobList).setInterview(CVManageActivity.arrayListJobList.get(positionJobList).getInterview() + 1);
            }else if(statusApplication >= 12){
                CVManageActivity.arrayListJobList.get(positionJobList).setWork(CVManageActivity.arrayListJobList.get(positionJobList).getWork() + 1);
                CVManageActivity.arrayListJobList.get(positionJobList).setNew_document(CVManageActivity.arrayListJobList.get(positionJobList).getNew_document() -1);

            }else if(statusApplication == 2) {
                CVManageActivity.arrayListJobList.get(positionJobList).setSkip(CVManageActivity.arrayListJobList.get(positionJobList).getSkip()  + 1);
                CVManageActivity.arrayListJobList.get(positionJobList).setNew_document(CVManageActivity.arrayListJobList.get(positionJobList).getNew_document() -1);
            }
            else {

            }
        }

    }
    public void kindTwo2(final int position){
   //     Toast.makeText(context, "Cập nhật thành công 2", Toast.LENGTH_SHORT).show();
        if(statusApplication <= 2){
            if(statusApplication == 2){
                CVManageActivity.arrayListJobList.get(positionJobList).setSkip(CVManageActivity.arrayListJobList.get(positionJobList).getSkip()  + 1);
                CVManageActivity.arrayListJobList.get(positionJobList).setInterview(CVManageActivity.arrayListJobList.get(positionJobList).getInterview() - 1);
            }else {
                CVManageActivity.arrayListJobList.get(positionJobList).setNew_document(CVManageActivity.arrayListJobList.get(positionJobList).getNew_document()  + 1);
                CVManageActivity.arrayListJobList.get(positionJobList).setInterview(CVManageActivity.arrayListJobList.get(positionJobList).getInterview() - 1);
            }


        }else if(statusApplication >= 12){
            CVManageActivity.arrayListJobList.get(positionJobList).setInterview(CVManageActivity.arrayListJobList.get(positionJobList).getInterview() - 1);
            CVManageActivity.arrayListJobList.get(positionJobList).setWork(CVManageActivity.arrayListJobList.get(positionJobList).getWork() + 1);
        }else {

        }

    }
    public void kindThree2(final int position){
     //   Toast.makeText(context, "Cập nhật thành công 3", Toast.LENGTH_SHORT).show();
        if(statusApplication >= 3 && statusApplication <= 11){
            CVManageActivity.arrayListJobList.get(positionJobList).setInterview(CVManageActivity.arrayListJobList.get(positionJobList).getInterview()  + 1);
            CVManageActivity.arrayListJobList.get(positionJobList).setWork(CVManageActivity.arrayListJobList.get(positionJobList).getWork() - 1);


        }else if(statusApplication <= 2){
            if(statusApplication == 2){
                CVManageActivity.arrayListJobList.get(positionJobList).setSkip(CVManageActivity.arrayListJobList.get(positionJobList).getSkip()  + 1);
                CVManageActivity.arrayListJobList.get(positionJobList).setWork(CVManageActivity.arrayListJobList.get(positionJobList).getWork() - 1);
            }else {
                CVManageActivity.arrayListJobList.get(positionJobList).setNew_document(CVManageActivity.arrayListJobList.get(positionJobList).getNew_document()  + 1);
                CVManageActivity.arrayListJobList.get(positionJobList).setWork(CVManageActivity.arrayListJobList.get(positionJobList).getWork() - 1);
            }


        }else {

        }

    }


    public void updateCandidateDocument(){
        for(int i=0; i < CVManageActivity.arrayListAll.size(); i++){
            if(applicant.getId() == CVManageActivity.arrayListAll.get(i).getId()){
                CVManageActivity.arrayListAll.set(i, applicant);
                CandidateDocumentFragment.adapter.notifyDataSetChanged();
            }
        }
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
                            updateCandidateDocument();
                            if(kind == 0){
                             //   Toast.makeText(context, "Cập nhật thành công " + kind, Toast.LENGTH_SHORT).show();
                                if(first_status == 0 || first_status == 1 || first_status == 2){
                                    kindOne2(position);
                                }else if(first_status >= 3 && first_status <= 11) {
                                    kindTwo2(position);
                                }else {
                                    kindThree2(position);
                                }
                                JobListFragment.adapter.notifyDataSetChanged();
                            }


                            if(kind == 1){
                                kindOne(position);
                            }

                            if(kind == 2){
                                kindTwo(position);
                            }
                            if(kind == 3){
                                kindThree(position);
                            }

                            notifyDataSetChanged();
                            JobListFragment.adapter.notifyDataSetChanged();

                            infoNothing(); // khi k có ứng viên thì báo k có

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
        if(CVManageActivity.arrayListCVFilter.size() == 0){
            CVFilterFragment.layout_nothing.setVisibility(View.VISIBLE);
            CVFilterFragment.layout.setVisibility(View.GONE);
        }else {
            CVFilterFragment.layout_nothing.setVisibility(View.GONE);
            CVFilterFragment.layout.setVisibility(View.VISIBLE);
        }
        if(CVManageActivity.arrayListInterView.size() == 0){
            InterviewFragment.layout_nothing.setVisibility(View.VISIBLE);
            InterviewFragment.layout.setVisibility(View.GONE);
        }else {
            InterviewFragment.layout_nothing.setVisibility(View.GONE);
            InterviewFragment.layout.setVisibility(View.VISIBLE);
        }

        if(CVManageActivity.arrayListGoToWork.size() == 0){
            GoToWorkFragment.layout_nothing.setVisibility(View.VISIBLE);
            GoToWorkFragment.layout.setVisibility(View.GONE);
        }else {
            GoToWorkFragment.layout_nothing.setVisibility(View.GONE);
            GoToWorkFragment.layout.setVisibility(View.VISIBLE);
        }
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