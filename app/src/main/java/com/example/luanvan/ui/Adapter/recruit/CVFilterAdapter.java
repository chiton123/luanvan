package com.example.luanvan.ui.Adapter.recruit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
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
import com.example.luanvan.ui.Adapter.update_personal_info.SpinnerNewAdapter;
import com.example.luanvan.ui.Model.Applicant;
import com.example.luanvan.ui.Model.GeneralObject;
import com.example.luanvan.ui.recruiter.CVManagement.CandidateInfoActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CVFilterAdapter extends RecyclerView.Adapter<CVFilterAdapter.ItemHolder> {
    Context context;
    ArrayList<Applicant> arrayList;
    Activity activity;
    int statusApplication = 0;


    public CVFilterAdapter(Context context, ArrayList<Applicant> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_loc_cv, null);
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
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
        Applicant applicant = arrayList.get(position);
        holder.txtName.setText(applicant.getUsername());
        holder.txtEmail.setText(applicant.getEmail());
        final int status = applicant.getStatus();
        switch (status){
            case 0:
                holder.txtStatus.setText("Chưa đánh giá");
                break;
            case 1:
                holder.txtStatus.setText("Đạt yêu cầu");
                break;
            case 2:
                holder.txtStatus.setText("Không đạt yêu cầu");
                break;

        }
        holder.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(activity, holder.btnChange);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_filter, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.show:
                                Intent intent = new Intent(context, CandidateInfoActivity.class);
                                intent.putExtra("applicant", arrayList.get(position));
                                activity.startActivity(intent);
                                break;
                            case R.id.access:
                                final Dialog dialog = new Dialog(activity);
                                dialog.setTitle("Đánh giá ứng viên");
                                dialog.setContentView(R.layout.dialog_access_candidate);
                                dialog.setCancelable(false);
                                TextView txtName = (TextView) dialog.findViewById(R.id.name);
                                EditText editNote = (EditText) dialog.findViewById(R.id.editnote);
                                Button btnUpdate = (Button) dialog.findViewById(R.id.buttoncapnhat);
                                Button btnCancel = (Button) dialog.findViewById(R.id.buttonhuy);
                                btnCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                btnUpdate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        updateStatus(position);
                                    }
                                });
                                Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
                                txtName.setText(arrayList.get(position).getUsername());
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
                                break;
                            case R.id.delete:
                                Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        return false;
                    }
                });

                popupMenu.show();

            }
        });

    }
    public void showDialog(int position){


    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txtName, txtEmail, txtStatus;
        public Button btnChange;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.cadidateName);
            txtEmail = (TextView) itemView.findViewById(R.id.email);
            txtStatus = (TextView) itemView.findViewById(R.id.status);
            btnChange = (Button) itemView.findViewById(R.id.buttonthaotac);

        }
    }
}
