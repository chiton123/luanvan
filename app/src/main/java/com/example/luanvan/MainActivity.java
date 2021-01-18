package com.example.luanvan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.luanvan.ui.Adapter.ExperienceAdapter;
import com.example.luanvan.ui.Adapter.SkillAdapter;
import com.example.luanvan.ui.Adapter.StudyAdapter;
import com.example.luanvan.ui.Model.Experience;
import com.example.luanvan.ui.Model.Skill;
import com.example.luanvan.ui.Model.Study;
import com.example.luanvan.ui.Model.User;
import com.example.luanvan.ui.User.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Để adapter.notify trong runable handle sẽ k bị lỗi null
    // adapter của Notification fragment
    public static DatabaseReference mData = FirebaseDatabase.getInstance().getReference("one");
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance() ;
    public static FirebaseUser mUser;
    public static StudyAdapter studyAdapter;
    public static ExperienceAdapter experienceAdapter;
    public static SkillAdapter skillAdapter;
    public static int login = 0;
    public static int iduser = 0;
    public static String uid = "";
    public static String username = "";
    public static String position = "";
    public static User user = new User();
    public static ArrayList<Study> studies = new ArrayList<>();
    public static ArrayList<Experience> experiences = new ArrayList<>();
    public static ArrayList<Skill> skills = new ArrayList<>();
    // list CV
    public static ArrayList<String> arrayListCV = new ArrayList<>();

    public static String host = "http://10.10.33.143:8888/luanvan/";
    public static String urljob1 = host + "job1.php";
    public static String urlcompany = host + "company.php";
    // dang nhap
    public static String urlRegister = host + "register.php";
    public static String urllogin = host + "login.php";
    public static String urluserinfo = host + "userinfo.php";
    public static String urlupdateuser = host + "updateuser.php";


    public static String urlschool = host + "school.php";
    public static String urlexperience = host + "experience.php";

    public static String urlskill = host + "skill.php";
    // add new skill, experience, study
    public static String urlupdatestudy = host + "updatestudy.php";
    public static String urlupdateexperience = host + "updatexperience.php";
    public static String urlupdateskill = host + "updateskill.php";
    // cap nhat skill, experience, study
    public static String urlupdate_old_study = host + "update_old_study.php";
    public static String urlupdate_old_experience = host + "update_old_experience.php";
    public static String urlupdate_old_skill = host + "update_old_skill.php";
    // delete skill, experience, study
    public static String urldeleteitem = host + "deleteitem.php";




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
