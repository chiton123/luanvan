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
import com.example.luanvan.ui.Model.JavaMailAPI;
import com.example.luanvan.ui.Model.Profile;
import com.example.luanvan.ui.Model.UserSearch;
import com.example.luanvan.ui.fragment.recruting.CVFilterFragment;
import com.example.luanvan.ui.fragment.recruting.GoToWorkFragment;
import com.example.luanvan.ui.fragment.recruting.InterviewFragment;
import com.example.luanvan.ui.recruiter.CVManagement.CVManageActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CVManagementActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CandidateDocumentFragment;
import com.example.luanvan.ui.recruiter.CVManagement.JobListFragment;
import com.example.luanvan.ui.recruiter.RecruiterActivity;
import com.example.luanvan.ui.schedule.ScheduleActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ViewCandidateAdapter extends RecyclerView.Adapter<ViewCandidateAdapter.ItemtHolder> {
    Context context;
    ArrayList<Profile> arrayList;
    Activity activity;
    String url_cv = ""; // url cv
    String name_cv = "";
    UserSearch userSearch;
    BottomSheetDialog bottomSheetContactInfo, bottomSheetSendEmail;

    public ViewCandidateAdapter(Context context, ArrayList<Profile> arrayList, Activity activity,
                                String url_cv, String name_cv,UserSearch userSearch) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.url_cv = url_cv;
        this.name_cv = name_cv;
        this.userSearch = userSearch;
    }

    @NonNull
    @Override
    public ItemtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_profile_candidate, parent, false);
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
                        try {
                            showContactInfo();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        showSendEmail();
                        break;
                    case 3:
                        //Toast.makeText(context, url_cv, Toast.LENGTH_SHORT).show();
                        downLoadCV_PDF(url_cv);
                        break;
                }
            }
        });
    }

    private void showSendEmail() {
        bottomSheetSendEmail = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_send_email,(ViewGroup) activity.findViewById(R.id.bottom_sheet));
        final EditText editContent = (EditText) view.findViewById(R.id.editcontent);
        Button btnSend = (Button) view.findViewById(R.id.buttongui);
        Button btnCancel = (Button) view.findViewById(R.id.buttonhuy);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetSendEmail.dismiss();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editContent.getText().toString();
                if(content.equals("")){
                    Toast.makeText(context, "Vui lòng điền nội dung email", Toast.LENGTH_SHORT).show();
                }else{
                    String title = "Nhà tuyển dụng đang tìm bạn";
                    sendMail(userSearch.getEmail(), title, content);
                    bottomSheetSendEmail.dismiss();
                }
            }
        });

        bottomSheetSendEmail.setCancelable(false);
        bottomSheetSendEmail.setContentView(view);
        bottomSheetSendEmail.show();

    }

    private void sendMail(String email, String title, String content) {
        JavaMailAPI mail = new JavaMailAPI(activity, email, title, content);
        mail.execute();
        Toast.makeText(context, "Đã gửi mail", Toast.LENGTH_SHORT).show();
    }
    private void showContactInfo() throws ParseException {
        bottomSheetContactInfo = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_contact_info,(ViewGroup) activity.findViewById(R.id.bottom_sheet));
        TextView txtName = (TextView) view.findViewById(R.id.txtname);
        TextView txtEmail = (TextView) view.findViewById(R.id.txtemail);
        TextView txtPhone = (TextView) view.findViewById(R.id.txtphone);
        TextView txtAddress = (TextView) view.findViewById(R.id.txtaddress);
        TextView txtBirthday = (TextView) view.findViewById(R.id.txtdate);
        Button btnClose = (Button) view.findViewById(R.id.buttondong);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetContactInfo.dismiss();
            }
        });
        txtName.setText(userSearch.getUsername());
        txtEmail.setText(userSearch.getEmail());
        txtPhone.setText("0"+ userSearch.getPhone()+"");
        txtAddress.setText(userSearch.getAddress());
        String ngay = userSearch.getBirthday();
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        date = simpleDateFormat.parse(ngay);
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd/mm/yyyy");
        txtBirthday.setText(fmtOut.format(date));
        bottomSheetContactInfo.setCancelable(false);
        bottomSheetContactInfo.setContentView(view);
        bottomSheetContactInfo.show();

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
                FancyToast.makeText(context, "Tải về thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                FancyToast.makeText(context,"Tải về thất bại", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            }
        });
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