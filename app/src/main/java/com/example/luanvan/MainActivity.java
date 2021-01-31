package com.example.luanvan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.luanvan.ui.Adapter.update_personal_info.ExperienceAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.SkillAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.StudyAdapter;
import com.example.luanvan.ui.Model.Experience;
import com.example.luanvan.ui.Model.Skill;
import com.example.luanvan.ui.Model.Study;
import com.example.luanvan.ui.Model.User;
import com.example.luanvan.ui.modelCV.UserCV;
import com.example.luanvan.ui.modelCV.ExperienceCV;
import com.example.luanvan.ui.modelCV.SkillCV;
import com.example.luanvan.ui.modelCV.StudyCV;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
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

    // default CV info
    // - info connect : user
    // - Experience
    public static ExperienceCV experienceCV = new ExperienceCV("temp","TMA Solution","Developer", "2015", "2020", "Rất tốt");
    public static ArrayList<SkillCV> skillCVArray = new ArrayList<>();
    public static UserCV userCVDefault = new UserCV("Nguyễn Văn A", "DBA", "batphuongtrinhvoti@gmail.com","0323232","Vĩnh Long","Nam","20/10/1999");
    public static StudyCV studyCV = new StudyCV("temp", "Đại học Cần Thơ", "Công nghệ thông tin", "2017", "2021", "Giỏi, GPA: 3.5");
    public static String goalDefault = "Trở thành DBA lương 1000$";



    // skillCV , infoCV, experienceCV, studyCV, goal để up lên
    public static ArrayList<SkillCV> skillCVS = new ArrayList<>();
    public static UserCV userCV = new UserCV();
    public static ArrayList<ExperienceCV> experienceCVS = new ArrayList<>();
    public static ArrayList<StudyCV> studyCVS = new ArrayList<>();
    public static String goal = "";
    // check first , khi vào và điền thông tin rồi thì = 1
    public static int checkFirstInfo = 0;
    public static int checkFirstGoal = 0;
    public static int checkFirstStudy = 0;
    public static int checkFirstExperience = 0;
    public static int checkFirstSkill = 0;
    public static int checkFirstVolunteer = 0;


    public static String host = "http://10.3.74.116:8888/luanvan/";
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
        skillCVArray.add(new SkillCV("Kỹ năng tiếng anh", 4, "temp"));
        skillCVArray.add(new SkillCV("Kỹ năng tin học", 3, "temp"));

    }


}
