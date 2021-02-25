package com.example.luanvan.ui.Adapter.recruit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.recruiter.AdjustJobActivity;
import com.example.luanvan.ui.recruiter.CVManagement.CVManagementActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.ItemHolder> {
    Context context;
    ArrayList<Job> arrayList;
    Activity activity;
    int REQUEST_CODE = 123;
    // kiểm tra xem có quá ngày hay không, nếu quá ngày thì k dc tiếp tục tuyển

    public PositionAdapter(Context context, ArrayList<Job> arrayList, Activity activity) {
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
        Job job = arrayList.get(position);
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

        holder.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(activity, holder.btnChange);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                MenuItem item = popupMenu.getMenu().getItem(2);
                int statusJob = arrayList.get(position).getStatus();
                if(statusJob == 0){
                    if(finalDate.after(Calendar.getInstance().getTime())){
                        item.setTitle("Dừng tuyển");
                    }else {
                        item.setTitle("Tiếp tục tuyển");
                    }

                }else{
                    item.setTitle("Tiếp tục tuyển");
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edit:
                                Intent intent1 = new Intent(activity, AdjustJobActivity.class);
                                intent1.putExtra("job", arrayList.get(position));
                                intent1.putExtra("position", position);
                                activity.startActivityForResult(intent1, REQUEST_CODE);
                                break;
                            case R.id.show:
                                Intent intent = new Intent(activity, CVManagementActivity.class);
                                intent.putExtra("job_id", arrayList.get(position).getId());
                                activity.startActivity(intent);
                                break;
                            case R.id.end:
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

                                break;
                            case R.id.delete:
                                deleteJob(position);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
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

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txtName, txtDate, txtStatus;
        public Button btnChange;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.name);
            txtDate = (TextView) itemView.findViewById(R.id.date);
            txtStatus = (TextView) itemView.findViewById(R.id.status);
            btnChange = (Button) itemView.findViewById(R.id.buttonthaotac);

        }
    }

}
