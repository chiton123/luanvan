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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.luanvan.ui.Adapter.job.KindOfJobAdapter;
import com.example.luanvan.ui.DetailedJob.DetailJobActivity;
import com.example.luanvan.ui.DetailedJob.DetailJobAdminActivity;
import com.example.luanvan.ui.Interface.ILoadMore;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.JobPost;
import com.example.luanvan.ui.admin.JobReviewActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LoadingViewHolder extends RecyclerView.ViewHolder{
    public ProgressBar progressBar;
    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
    }
}

public class JobReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    Context context;
    ArrayList<JobPost> filterArraylist;
    ArrayList<JobPost> nameList;
    Activity activity;
    int REQUEST_CODE = 123;
    BottomSheetDialog bottomSheetDialog;
    String note_reject = "";
    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    ILoadMore loadmore;
    boolean isloading;
    int visableThreadHold = 3;
    int lastVisableItem,totalItemcount;
    public JobReviewAdapter(RecyclerView recyclerView, Context context, ArrayList<JobPost> arrayList, Activity activity) {
        this.context = context;
        this.nameList = arrayList;
        this.filterArraylist = arrayList;
        this.activity = activity;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemcount = linearLayoutManager.getItemCount();
                lastVisableItem = linearLayoutManager.findLastVisibleItemPosition();
                if(!isloading && totalItemcount < (lastVisableItem + visableThreadHold)){
                    if(loadmore != null){
                        loadmore.onLoadMore();
                    }
                    isloading = true;
                }

            }
        });

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(context).inflate(R.layout.dong_tin_tuyen_dung_admin, parent, false);
            return new ItemHolder(view);
        }else if(viewType == VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ItemHolder){
            ItemHolder itemHolder = (ItemHolder) holder;
            JobPost job = filterArraylist.get(position);
            itemHolder.txttencongviec.setText(job.getName());
            itemHolder.txttencongty.setText(job.getCompany_name());
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
            itemHolder.txttime.setText(fmtOut.format(date2));
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            itemHolder.txtsalary.setText("Từ " + decimalFormat.format( + job.getSalary_min()) + "đ đến " + decimalFormat.format(job.getSalary_max()) + "đ" );
            itemHolder.txtarea.setText(job.getAddress());
            Glide.with(context).load(job.getImg()).into(itemHolder.imganh);
            if(job.getStatus_post() == 0){
                itemHolder.txtstatus_post.setText("Đồng ý");
            }else if(job.getStatus_post() == 1){
                itemHolder.txtstatus_post.setText("Đang chờ xác thực");
            }else {
                itemHolder.txtstatus_post.setText("Từ chối");
            }
            itemHolder.imganh.setFocusable(false);
            itemHolder.imganh.setFocusable(false);
            itemHolder.btnReject.setFocusable(false);
            itemHolder.btnConfirm.setFocusable(false);
            itemHolder.btnConfirm.setClickable(false);
            itemHolder.btnReject.setClickable(false);
            itemHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailJobAdminActivity.class);
                    intent.putExtra("job", filterArraylist.get(position));
                    intent.putExtra("position", position); // position trong mảng để load lại
                    activity.startActivityForResult(intent, REQUEST_CODE);

                }
            });
            if(job.getStatus_post() != 1){
                itemHolder.btnReject.setClickable(false);
                itemHolder.btnConfirm.setClickable(false);
                itemHolder.btnConfirm.setBackgroundResource(R.drawable.button_black);
                itemHolder.btnReject.setBackgroundResource(R.drawable.button_black);
                itemHolder.btnReject.setTextColor(Color.WHITE);
                itemHolder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Bạn đã xác nhận", Toast.LENGTH_SHORT).show();
                    }
                });
                itemHolder.btnReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Bạn đã xác nhận", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                itemHolder.btnConfirm.setBackgroundResource(R.drawable.button_xacnhan);
                itemHolder.btnReject.setBackgroundResource(R.drawable.background_button_view_cv);
                itemHolder.btnReject.setTextColor(Color.BLACK);
                itemHolder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        acceptOrRejectJob(0, position);
                    }
                });
                itemHolder.btnReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rejectDialog(position);
                    }
                });
            }


        }else if(holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHoler = (LoadingViewHolder) holder;
            loadingViewHoler.progressBar.setIndeterminate(true);
        }

    }
    public void setLoadmore(ILoadMore loadmore) {
        this.loadmore = loadmore;
    }
    @Override
    public int getItemViewType(int position) {
        return filterArraylist.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setIsloaded(boolean x) {
        this.isloading = x;
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
                            filterArraylist.get(position).setStatus_post(status);
                            filterArraylist.get(position).setNote_reject(note_reject);
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
                map.put("job_id", String.valueOf(filterArraylist.get(position).getId()));
                map.put("status", String.valueOf(status));
                map.put("note_reject", note_reject);
                return map;
            }
        };
        requestQueue.add(stringRequest);


    }

    @Override
    public int getItemCount() {
        return filterArraylist.size();
    }
    public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
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
                    ArrayList<JobPost> filteredList = new ArrayList<>();
                    for(JobPost job : nameList){
                        String name1 = stripAccents(job.getName()).trim();
                        if(name1.toLowerCase().contains(charSequenceString.toLowerCase())){
                            filteredList.add(job);
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
                filterArraylist = (ArrayList<JobPost>) results.values;
                notifyDataSetChanged();
            }
        };
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
