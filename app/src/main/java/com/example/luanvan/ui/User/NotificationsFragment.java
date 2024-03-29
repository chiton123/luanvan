package com.example.luanvan.ui.User;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
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
import com.example.luanvan.ui.Adapter.admin_a.AdminAdapter;
import com.example.luanvan.ui.Adapter.admin_a.InfoAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.ExperienceAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.ProfileAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.SkillAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.StudyAdapter;
import com.example.luanvan.ui.Model.Admin;
import com.example.luanvan.ui.Model.Profile;
import com.example.luanvan.ui.Model.User;
import com.example.luanvan.ui.UpdateInfo.EditCombineActivity;
import com.example.luanvan.ui.UpdateInfo.PersonalInfoActivity;
import com.example.luanvan.ui.login.LoginActivity;
import com.example.luanvan.ui.modelCV.UserCV;
import com.example.luanvan.ui.recruiter.LoginRecruiterActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class NotificationsFragment extends Fragment {
    Toolbar toolbar;
    public static StudyAdapter studyAdapter;
    public static ExperienceAdapter experienceAdapter;
    public static SkillAdapter skillAdapter;
    LinearLayout linearLayout1, linearLayout2;
    Button btndangnhap, btnLoginRecruiter;
    int REQUEST_CODE = 123;
    int REQUEST_CODE2 = 234;
    int REQUEST_CODE_RECRUITER = 333;
    // edit hoc van, kinh nghiem, skill
    int REQUEST_HOCVAN = 111, REQUEST_KINHNGHIEM = 2, REQUEST_KYNANG = 3;

    TextView name, positon, company_name;
    RecyclerView recyclerView, recyclerViewstudy, recyclerViewexperience, recyclerViewskill;
    ProfileAdapter profileAdapter;
    DatabaseReference reference;
    StorageReference storageReference;
    int IMAGE_REQUEST = 1;
    Uri imageUri;
    StorageTask uploadTask;

    ArrayList<Profile> arrayList;
    ScrollView scrollView;
    ImageView edithocvan, editkinhnghiem, editkynang, imgProfile;
    MainActivity activity;
    Handler handler;
    SwitchCompat switchCompat;
    LinearLayout layout_editProfile, layout_passwordReset;
    TextView txtLogout;
    private NotificationsViewModel notificationsViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        layout_editProfile = (LinearLayout) view.findViewById(R.id.layout_editprofile);
        layout_passwordReset = (LinearLayout) view.findViewById(R.id.layout_passwordreset);
        linearLayout1 = (LinearLayout) view.findViewById(R.id.linear);
        linearLayout2 = (LinearLayout) view.findViewById(R.id.linear2);
        edithocvan = (ImageView) view.findViewById(R.id.edithocvan);
        editkinhnghiem = (ImageView) view.findViewById(R.id.editkinhnghiem);
        editkynang = (ImageView) view.findViewById(R.id.editkynang);
        btndangnhap = (Button) view.findViewById(R.id.buttondangnhap);
        name = (TextView) view.findViewById(R.id.name);
        positon = (TextView) view.findViewById(R.id.position);
        company_name = (TextView) view.findViewById(R.id.company);
        btnLoginRecruiter = (Button) view.findViewById(R.id.buttondangnhaptuyendung);
        scrollView = (ScrollView)  view.findViewById(R.id.scrollview);
        switchCompat = (SwitchCompat) view.findViewById(R.id.switchcompat);
        arrayList = new ArrayList<>();
        getProfile();
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
        studyAdapter = new StudyAdapter(getActivity(), MainActivity.studies, getActivity(), 1);
   //     Toast.makeText(getActivity(), "hehe", Toast.LENGTH_SHORT).show();
        skillAdapter = new SkillAdapter(getActivity(), MainActivity.skills, getActivity(), 1);
        experienceAdapter = new ExperienceAdapter(getActivity(), MainActivity.experiences, getActivity(),1);
        recyclerView.setAdapter(profileAdapter);
        recyclerViewstudy.setAdapter(studyAdapter);
        recyclerViewexperience.setAdapter(experienceAdapter);
        recyclerViewskill.setAdapter(skillAdapter);
        imgProfile = (ImageView) view.findViewById(R.id.imgprofile);

        if(MainActivity.login == 0){
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);
        }else {
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.VISIBLE);
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getInfoFromFirebase();
                }
            },1000);

        }
        eventLogin();
        getInfo();
        eventEdit();
        storageReference = FirebaseStorage.getInstance().getReference("photo");
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
        if(MainActivity.login == 1){
            checkSwitch();
        }
        eventSwitch();
        eventEditProfileAndPasswordReset();
//        visabletxtLogout();
        return view;
    }

    private void eventEditProfileAndPasswordReset() {
        layout_passwordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent2);
            }
        });
        layout_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                startActivityForResult(intent, REQUEST_CODE2);
            }
        });
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        setHasOptionsMenu(true);

    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_signout, menu);
        final MenuItem menuItem = menu.findItem(R.id.signout);
        if(MainActivity.login == 0){
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            menuItem.setTitle("Đăng nhập");
        }else {
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuItem.setTitle("Đăng xuất");
        }


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.signout:
                if(MainActivity.login == 1){
                    setDefault();
                    getActivity().recreate();
                    break;
                }else{
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }


        }

        return super.onOptionsItemSelected(item);
    }

    private void checkSwitch() {
        if(getActivity() != null){
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlCheckSwitch,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response != null){
                                int mode = Integer.parseInt(response.toString());
                                if(mode == 1){
                                    switchCompat.setText("Đang bật tìm việc");
                                    switchCompat.setTextColor(Color.GREEN);
                                    switchCompat.setChecked(true);
                                }else {
                                    switchCompat.setText("Đang tắt tìm việc");
                                    switchCompat.setTextColor(Color.RED);
                                    switchCompat.setChecked(false);
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            FancyToast.makeText(getActivity(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<>();
                    map.put("iduser", String.valueOf(MainActivity.iduser));
                    return map;
                }
            };
            requestQueue.add(stringRequest);
        }


    }

    private void eventSwitch() {
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switchCompat.isChecked()){
                    switchMode(1);
                    switchCompat.setText("Đang bật tìm việc");
                    switchCompat.setTextColor(Color.GREEN);
                }else {
                    switchMode(0);
                    switchCompat.setText("Đang tắt tìm việc");
                    switchCompat.setTextColor(Color.RED);
                }
            }
        });

    }
    public void switchMode(final int status){
        if(getActivity() != null){
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlSwitchMode,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("success")){
                                //  Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            }else {
                                //   Toast.makeText(getActivity(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            FancyToast.makeText(getActivity(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<>();
                    map.put("iduser", String.valueOf(MainActivity.iduser));
                    map.put("status", String.valueOf(status));
                    return map;
                }
            };
            requestQueue.add(stringRequest);
        }

    }




    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);

    }
    private String getFileExtention(Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Uploading");
        pd.show();

        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtention(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(MainActivity.uid);
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("imageURL", mUri);
                        reference.updateChildren(hashMap);
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                            }
                        },1500);


                    }else {
                        FancyToast.makeText(getActivity(), "Fail", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FancyToast.makeText(getActivity(), e.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    pd.dismiss();
                }
            });
        }else {
            FancyToast.makeText(getActivity(),"No image seleted", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        }

    }



    public void getInfoFromFirebase(){
        if(MainActivity.login == 1){
            reference = FirebaseDatabase.getInstance().getReference("Users").child(MainActivity.uid);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    final String img = snapshot.child("imageURL").getValue(String.class);
                    if(img.equals("default")){
                        imgProfile.setImageResource(R.drawable.imgprofile);
                    }else {
                        if(img != null ){
                            handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(getActivity() != null){
                                        Glide.with(getActivity()).load(img).into(imgProfile);
                                    }

                                }
                            },2000);

                            try {

                            }catch (NullPointerException e){
                                FancyToast.makeText(getActivity(),"Lỗi", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    public void setDefault(){
        MainActivity.login = 0;
        MainActivity.iduser = 0;
        MainActivity.checkCV = 0;
        MainActivity.uid = "";
        MainActivity.username = "";
        MainActivity.password = "";
        MainActivity.urlCV = "";
        MainActivity.user = new User();
        MainActivity.studies.clear();
        MainActivity.experiences.clear();
        MainActivity.skills.clear();
        MainActivity.arrayListCV.clear();
        MainActivity.arraylistChosenArea.clear();
        MainActivity.arrayListChosenProfession.clear();
        MainActivity.userCV = new UserCV();
        MainActivity.userCV = null;
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
        MainActivity.arrayListNotification.clear();
        MainActivity.k = 0;
        MainActivity.k_chat = 0;


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
        arrayList.add(new Profile(0, "Thêm thông tin học vấn", R.drawable.study ));
        arrayList.add(new Profile(1, "Thêm thông tin kinh nghiệm", R.drawable.experience));
        arrayList.add(new Profile(2, "Thêm kỹ năng", R.drawable.skill));

    }

    private void getInfo() {
        if(MainActivity.login == 1){
           // Toast.makeText(getActivity(), MainActivity.user.getName() + "username", Toast.LENGTH_SHORT).show();
            try {
                if(!MainActivity.user.getName().equals("")){
                    name.setText(MainActivity.user.getName());
                }

                if(!MainActivity.user.getPosition().equals("")){
                    positon.setText(MainActivity.user.getPosition());
                }
            }catch (NullPointerException e){
                FancyToast.makeText(getActivity(),e.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }

        }


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
        // Dang nhap
        if(requestCode == REQUEST_CODE && resultCode == 123){
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.VISIBLE);
            getInfo();
            getInfoFromFirebase();
            checkSwitch();
            getActivity().invalidateOptionsMenu();


        }
        if(requestCode == REQUEST_CODE2 && resultCode == 234){
            getInfo();
            getInfoFromFirebase();
        }

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            if(uploadTask != null && uploadTask.isInProgress()){
                FancyToast.makeText(getActivity(), "Upload is in progress", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            }else {
                uploadImage();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}
