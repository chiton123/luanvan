package com.example.luanvan.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.admin_a.AdminAdapter;
import com.example.luanvan.ui.Adapter.admin_a.AdminAdapter_a;
import com.example.luanvan.ui.Model.Admin;
import com.example.luanvan.ui.admin.AdminActivity;
import com.example.luanvan.ui.company.SearchCompanyActivity;
import com.example.luanvan.ui.cv.CVIntroductionActivity;
import com.example.luanvan.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    ArrayList<Admin> arrayList;
    GridView gridView;
    AdminAdapter adminAdapter;
    int REQUEST_CODE = 123;
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        arrayList = new ArrayList<>();
        addItem();
        adminAdapter = new AdminAdapter(getActivity(), arrayList);
        try{
            gridView.setAdapter(adminAdapter);
        }catch (NullPointerException e){
            Toast.makeText(getActivity(), "NullPointerException" ,Toast.LENGTH_SHORT).show();
        }
        evetGridView();

        return view;

    }

    private void evetGridView() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        if(MainActivity.login == 0){
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivityForResult(intent, REQUEST_CODE);
                        }else {
                            Intent intent = new Intent(getActivity(), CVIntroductionActivity.class);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                        break;
                    case 1:
                        Intent intent = new Intent(getActivity(), SearchCompanyActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        });
    }

    public void addItem(){
        arrayList.add(new Admin(0, "Tạo CV", R.drawable.cv_dashbard));
        arrayList.add(new Admin(1, "Tìm kiếm công ty", R.drawable.company));
        arrayList.add(new Admin(2, "Thương hiệu uy tín", R.drawable.good_company));
        arrayList.add(new Admin(3, "Lịch hẹn", R.drawable.schedule));

    }




}
