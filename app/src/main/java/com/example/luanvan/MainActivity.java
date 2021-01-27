package com.example.luanvan;

import android.os.Bundle;

import com.example.luanvan.ui.Adapter.update_personal_info.ExperienceAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.SkillAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.StudyAdapter;
import com.example.luanvan.ui.Model.Experience;
import com.example.luanvan.ui.Model.Skill;
import com.example.luanvan.ui.Model.Study;
import com.example.luanvan.ui.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Để adapter.notify trong runable handle sẽ k bị lỗi null
    // adapter của Notification fragment
    public static DatabaseReference mData = FirebaseDatabase.getInstance().getReference("one");
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance() ;
    public static FirebaseUser mUser;
    // fragment notification
    public static StudyAdapter studyAdapter;
    public static ExperienceAdapter experienceAdapter;
    public static SkillAdapter skillAdapter;
    public static int login = 0;
    public static int iduser = 0;
    public static int checkCV = 0;
    // 1: có cv, 2: chưa
    public static String uid = "";
    public static String username = "";
    public static String position = "";
    public static String urlCV = "";
    public static User user = new User();
    public static ArrayList<Study> studies = new ArrayList<>();
    public static ArrayList<Experience> experiences = new ArrayList<>();
    public static ArrayList<Skill> skills = new ArrayList<>();
    // list CV
    public static ArrayList<String> arrayListCV = new ArrayList<>();

    public static String host = "http://10.10.43.233:8888/luanvan/";
    public static String urljob1 = host + "job1.php";
    public static String urlcompany = host + "company.php";
    // dang nhap
    public static String urlRegister = host + "register.php";
    public static String urllogin = host + "login.php";
    public static String urluserinfo = host + "userinfo.php";
    public static String urlupdateuser = host + "updateuser.php";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_admin)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
//        try{
//            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//
//        }catch (NullPointerException e){
//            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
//        }


    }


}
