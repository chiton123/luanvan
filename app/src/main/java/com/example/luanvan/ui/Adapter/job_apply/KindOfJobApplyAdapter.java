package com.example.luanvan.ui.Adapter.job_apply;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luanvan.R;
import com.example.luanvan.ui.DetailedJob.DetailJobActivity;
import com.example.luanvan.ui.Interface.ILoadMore;
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.Job_Apply;
import com.example.luanvan.ui.chat.MessageActivity;
import com.example.luanvan.ui.cv.CVShowActivity;
import com.example.luanvan.ui.home.HomeFragment;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class LoadingViewHolder extends RecyclerView.ViewHolder{
    public ProgressBar progressBar;
    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
    }
}

public class KindOfJobApplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    Context context;
    List<Job_Apply> filterArraylist;
    Activity activity;
    List<Job_Apply> nameList;
    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    ILoadMore loadmore;
    boolean isloading;
    int visableThreadHold = 4;
    int lastVisableItem,totalItemcount;
    int kind; // 0: normal, 1: việc đã ứng tuyển
    public KindOfJobApplyAdapter(RecyclerView recyclerView, Context context, List<Job_Apply> arrayList, Activity activity, int kind) {
        this.context = context;
        this.nameList = arrayList;
        this.activity = activity;
        this.filterArraylist = arrayList;
        this.kind = kind;

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
            View view = LayoutInflater.from(context).inflate(R.layout.dong_viec_lam_kind, parent, false);
            return new ItemHolder(view);
        }else if(viewType == VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }
    public void setLoadmore(ILoadMore loadmore) {
        this.loadmore = loadmore;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ItemHolder){
            ItemHolder itemHolder = (ItemHolder) holder;
            if(filterArraylist.size() > 0){
                final Job_Apply job = filterArraylist.get(position);
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
                itemHolder.txttime.setText(fmtOut.format(date2));
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                itemHolder.txtsalary.setText("Từ " + decimalFormat.format( + job.getSalary_min()) + "đ đến " + decimalFormat.format(job.getSalary_max()) + "đ" );
                itemHolder.txtarea.setText(job.getAddress());
                Glide.with(context).load(job.getImg()).into(itemHolder.imganh);
                itemHolder.imganh.setFocusable(false);
                itemHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DetailJobActivity.class);
                        // 0: từ màn hình chính, tìm kiếm, lọc chuyển qua, 1: từ notification chuyển qua
                        intent.putExtra("kind", 0);
                        for(int i = 0; i < HomeFragment.arrayList.size(); i++){
                            if(filterArraylist.get(position).getId() == HomeFragment.arrayList.get(i).getId()){
                                intent.putExtra("job",HomeFragment.arrayList.get(i));
                            }
                        }
                        activity.startActivity(intent);

                    }
                });
                if(kind == 1){
                    itemHolder.layout_chat.setVisibility(View.VISIBLE);
                    itemHolder.btnViewCV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, CVShowActivity.class);
                            intent.putExtra("kind", 2); // 1: show cv , 2: job apply
                            intent.putExtra("cv_id", job.getId_cv());
                            activity.startActivity(intent);
                        }
                    });
                    itemHolder.btnChat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, MessageActivity.class);
                            intent.putExtra("kind", 1);
                            intent.putExtra("idrecruiter", filterArraylist.get(position).getId_recruiter());
                            activity.startActivity(intent);
                        }
                    });
                }else {
                    itemHolder.layout_chat.setVisibility(View.GONE);
                }
            }


        }else if(holder instanceof LoadingViewHolder){
            LoadingViewHolder loadingViewHoler = (LoadingViewHolder) holder;
            loadingViewHoler.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return filterArraylist.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setIsloaded(boolean x) {
        this.isloading = x;
    }

    @Override
    public int getItemCount() {
        return filterArraylist.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public TextView txttencongviec, txttencongty, txtarea, txttime, txtsalary;
        public ImageView imganh;
        public LinearLayout layout, layout_chat;
        public Button btnChat, btnViewCV;;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txttencongviec = (TextView) itemView.findViewById(R.id.tencongviec);
            txttencongty = (TextView) itemView.findViewById(R.id.tencongty);
            txtarea = (TextView) itemView.findViewById(R.id.area);
            txtsalary = (TextView) itemView.findViewById(R.id.salary);
            txttime = (TextView) itemView.findViewById(R.id.time);
            imganh = (ImageView) itemView.findViewById(R.id.anh);
            layout = (LinearLayout) itemView.findViewById(R.id.linear);
            layout_chat = (LinearLayout) itemView.findViewById(R.id.layout_chat);
            btnChat = (Button) itemView.findViewById(R.id.buttonchat);
            btnViewCV = (Button) itemView.findViewById(R.id.buttonviewcv);

        }
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
                    List<Job_Apply> filteredList = new ArrayList<>();
                    for(Job_Apply job : nameList){
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
                filterArraylist = (ArrayList<Job_Apply>) results.values;
                notifyDataSetChanged();
            }
        };
    }


}
