package com.example.luanvan.ui.recruiter.PostNews;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.recruit.NewPostAdapter;
import com.example.luanvan.ui.Model.JobList;

import java.util.ArrayList;


public class DisplayJobFragment extends Fragment {
    RecyclerView recyclerView;
    NewPostAdapter adapter;
    ArrayList<JobList> arrayList;
    // kind: 0 đang hiển thị, 1: chờ xác thực, 2: từ chối, 3: hến hạn
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_job, container, false);
        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new NewPostAdapter(getActivity(), arrayList, getActivity(), 0);
        recyclerView.setAdapter(adapter);


        return view;
    }
}
