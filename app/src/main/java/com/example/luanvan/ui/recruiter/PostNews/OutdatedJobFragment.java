package com.example.luanvan.ui.recruiter.PostNews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.recruit.NewPostAdapter;
import com.example.luanvan.ui.recruiter.RecruiterActivity;

public class OutdatedJobFragment extends Fragment {
    RecyclerView recyclerView;
    public static NewPostAdapter adapter;
    public static LinearLayout layout, layout_nothing;
    Handler handler;
    ProgressDialog progressDialog;
    // fragment 1: Đang hiển thị, 2 : Chờ xác thực, 3: Hết hạn, 4: Từ chối , khi chuyển qua bên adjustJob thì cập nhật tương ứng với fragment
    // kind: 0 là của joblistfragment chuyển qua, 1: là của tin tuyển dụng chuyển qua
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outdated_job, container, false);
        layout = (LinearLayout) view.findViewById(R.id.layout);
        layout_nothing = (LinearLayout) view.findViewById(R.id.layout_nothing);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new NewPostAdapter(getActivity(), RecruiterActivity.arrayListOutdatedJobs, getActivity(), 1, 3);
        recyclerView.setAdapter(adapter);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkNothing();
            }
        },2500);

        return view;
    }
    void loading(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    public void checkNothing(){
        if(RecruiterActivity.arrayListOutdatedJobs.size() == 0){
            layout.setVisibility(View.GONE);
            layout_nothing.setVisibility(View.VISIBLE);
        }else {
            layout_nothing.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 123 && resultCode == 333){
            Toast.makeText(getActivity(), "outdated ", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
            checkNothing();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
