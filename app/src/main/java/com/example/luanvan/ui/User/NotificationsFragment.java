package com.example.luanvan.ui.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.update_personal_info.ExperienceAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.ProfileAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.SkillAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.StudyAdapter;
import com.example.luanvan.ui.Model.Profile;
import com.example.luanvan.ui.Model.User;
import com.example.luanvan.ui.login.LoginActivity;
import com.example.luanvan.ui.modelCV.UserCV;
import com.example.luanvan.ui.recruiter.LoginRecruiterActivity;

import java.util.ArrayList;

import static com.example.luanvan.MainActivity.experienceAdapter;
import static com.example.luanvan.MainActivity.skillAdapter;
import static com.example.luanvan.MainActivity.studyAdapter;

public class NotificationsFragment extends Fragment {
    LinearLayout linearLayout1, linearLayout2;
    Button btndangnhap, btnLoginRecruiter;
    int REQUEST_CODE = 123;
    int REQUEST_CODE2 = 234;
    int REQUEST_CODE_RECRUITER = 333;
    // edit hoc van, kinh nghiem, skill
    int REQUEST_HOCVAN = 111, REQUEST_KINHNGHIEM = 2, REQUEST_KYNANG = 3;
    ImageView edit;
    TextView name, positon, company_name, txtLogOut;
    RecyclerView recyclerView, recyclerViewstudy, recyclerViewexperience, recyclerViewskill;
    ProfileAdapter profileAdapter;



    ArrayList<Profile> arrayList;
    ScrollView scrollView;
    ImageView edithocvan, editkinhnghiem, editkynang;
    MainActivity activity;

    private NotificationsViewModel notificationsViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        linearLayout1 = (LinearLayout) view.findViewById(R.id.linear);
        linearLayout2 = (LinearLayout) view.findViewById(R.id.linear2);
        edithocvan = (ImageView) view.findViewById(R.id.edithocvan);
        editkinhnghiem = (ImageView) view.findViewById(R.id.editkinhnghiem);
        editkynang = (ImageView) view.findViewById(R.id.editkynang);
        btndangnhap = (Button) view.findViewById(R.id.buttondangnhap);
        edit = (ImageView) view.findViewById(R.id.edit);
        name = (TextView) view.findViewById(R.id.name);
        positon = (TextView) view.findViewById(R.id.position);
        company_name = (TextView) view.findViewById(R.id.company);
        btnLoginRecruiter = (Button) view.findViewById(R.id.buttondangnhaptuyendung);
        scrollView = (ScrollView)  view.findViewById(R.id.scrollview);
        arrayList = new ArrayList<>();
        getProfile();
        txtLogOut = (TextView) view.findViewById(R.id.logout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerViewexperience = (RecyclerView) view.findViewById(R.id.recycleviewexperience);
        recyclerViewstudy = (RecyclerView) view.findViewById(R.id.recycleviewstudy);
        recyclerViewskill = (RecyclerView) view.findViewById(R.id.recycleviewskill);
        recyclerView.setHasFixedSize(true);
        recyclerViewskill.setHasFixedSize(true);
        recyclerViewstudy.setHasFixedSize(true);
        recyclerViewexperience.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewstudy.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewexperience.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewskill.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        profileAdapter = new ProfileAdapter(getActivity(), arrayList, getActivity());
        MainActivity.studyAdapter = new StudyAdapter(getActivity(), MainActivity.studies, getActivity(), 1);
   //     Toast.makeText(getActivity(), "hehe", Toast.LENGTH_SHORT).show();
        MainActivity.skillAdapter = new SkillAdapter(getActivity(), MainActivity.skills, getActivity(), 1);
        MainActivity.experienceAdapter = new ExperienceAdapter(getActivity(), MainActivity.experiences, getActivity(),1);
        recyclerView.setAdapter(profileAdapter);
        recyclerViewstudy.setAdapter(studyAdapter);
        recyclerViewexperience.setAdapter(experienceAdapter);
        recyclerViewskill.setAdapter(skillAdapter);


        if(MainActivity.login == 0){
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);
        }else {
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.VISIBLE);
        }

        eventLogin();
        eventUpdateInfo();
        getInfo();
        eventEdit();
        eventLogout();

        return view;
    }
    public void setDefault(){
        MainActivity.login = 0;
        MainActivity.iduser = 0;
        MainActivity.checkCV = 0;
        MainActivity.uid = "";
        MainActivity.username = "";
        MainActivity.position = "";
        MainActivity.urlCV = "";
        MainActivity.user = new User();
        MainActivity.studies.clear();
        MainActivity.experiences.clear();
        MainActivity.skills.clear();
        MainActivity.arrayListCV.clear();
        MainActivity.userCV = new UserCV();
        MainActivity.experienceCVS.clear();
        MainActivity.studyCVS.clear();
        MainActivity.skillCVS.clear();
        MainActivity.goal = "";
        MainActivity.checkFirstExperience = 0;
        MainActivity.checkFirstGoal = 0;
        MainActivity.checkFirstInfo = 0;
        MainActivity.checkFirstSkill = 0;
        MainActivity.checkFirstStudy = 0;
        MainActivity.checkFirstVolunteer = 0;
        MainActivity.mAuth.signOut();

    }

    private void eventLogout() {
        txtLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefault();
                getActivity().recreate();
            }
        });
    }

    private void eventEdit() {
        edithocvan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditCombineActivity.class);
                intent.putExtra("number", 1);
                getActivity().startActivityForResult(intent, REQUEST_HOCVAN);
            }
        });
        editkinhnghiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditCombineActivity.class);
                intent.putExtra("number", 2);
                getActivity().startActivityForResult(intent, REQUEST_HOCVAN);
            }
        });
        editkynang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditCombineActivity.class);
                intent.putExtra("number", 3);
                getActivity().startActivityForResult(intent, REQUEST_HOCVAN);
            }
        });

    }


    private void getProfile() {
        arrayList.add(new Profile(0, "Cập nhật thông tin học vấn", R.drawable.study ));
        arrayList.add(new Profile(1, "Cập nhật thông tin kinh nghiệm", R.drawable.experience));
        arrayList.add(new Profile(2, "Cập nhật thông tin kỹ năng", R.drawable.skill));

    }

    private void getInfo() {
        if(MainActivity.login == 1){
           // Toast.makeText(getActivity(), MainActivity.user.getName() + "username", Toast.LENGTH_SHORT).show();
            try {
                name.setText(MainActivity.user.getName());
                if(MainActivity.position.equals("")){

                }else {
                    positon.setText(MainActivity.user.getPosition());
                }
            }catch (NullPointerException e){
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }

        }


    }

    private void eventUpdateInfo() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                startActivityForResult(intent, REQUEST_CODE2);
            }
        });

    }

    private void eventLogin() {
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        btnLoginRecruiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginRecruiterActivity.class);
                startActivityForResult(intent, REQUEST_CODE_RECRUITER);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
     //   Toast.makeText(getActivity(), "Result code: " + resultCode + " request: " + requestCode, Toast.LENGTH_SHORT).show();
        if(requestCode == REQUEST_CODE && resultCode == 123){
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.VISIBLE);
            getInfo();

        }
        if(requestCode == REQUEST_CODE2 && resultCode == 234){
            getInfo();
        }



        super.onActivityResult(requestCode, resultCode, data);

    }
}
