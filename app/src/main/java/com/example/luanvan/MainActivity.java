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
import com.example.luanvan.ui.modelCV.PdfCV;
import com.example.luanvan.ui.modelCV.UserCV;
import com.example.luanvan.ui.modelCV.ExperienceCV;
import com.example.luanvan.ui.modelCV.SkillCV;
import com.example.luanvan.ui.modelCV.StudyCV;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

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
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
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
    public static ArrayList<PdfCV> arrayListCV = new ArrayList<>();
    // default CV info
    // - info connect : user
    // - Experience
    public static ExperienceCV experienceCV = new ExperienceCV("temp","TMA Solution","Developer", "2015", "2020", "Rất tốt");
    public static ArrayList<SkillCV> skillCVArray = new ArrayList<>();
    public static UserCV userCVDefault = new UserCV("Nguyễn Chí Tôn", "DBA", "batphuongtrinhvoti@gmail.com","0323232","Ký túc xá B, đại học Cần Thơ","Nam","20/10/1999");
    public static StudyCV studyCV = new StudyCV("temp", "Đại học Cần Thơ", "CÔNG NGHỆ THÔNG TIN", "10/2017", "10/2021", "Tốt nghiệp loại giỏi, điểm trung bình 8.0");
    public static String goalDefault = "Trở thành DBA làm việc trong 1 ngân hàng lớn lương 1000$/năm";



    // skillCV , infoCV, experienceCV, studyCV, goal để up lên

    public static ArrayList<SkillCV> skillCVS = new ArrayList<>();
    public static UserCV userCV = new UserCV();
    public static ArrayList<ExperienceCV> experienceCVS = new ArrayList<>();
    public static ArrayList<StudyCV> studyCVS = new ArrayList<>();
    public static String goal = "";
    // khi update cv, nếu hủy, k update nữa thì giữ nguyên url


    // check first , khi vào và điền thông tin rồi thì = 1
    public static int checkFirstInfo = 0;
    public static int checkFirstGoal = 0;
    public static int checkFirstStudy = 0;
    public static int checkFirstExperience = 0;
    public static int checkFirstSkill = 0;
    public static int checkFirstVolunteer = 0;

    // kind CV, only change color
    public static int color = 0;

    public static String host = "http://10.3.74.50:8888/luanvan/";
    public static String urljob1 = host + "job1.php";
    public static String urlcompany = host + "company.php";
    // dang nhap
    public static String urlRegister = host + "register.php";
    public static String urllogin = host + "login.php";
    // Đăng nhập nhà tuyển dụng
    public static String urlLoginRecruiter = host + "login_recruiter.php";
    public static String urluserinfo = host + "userinfo.php";
    public static String urlupdateuser = host + "updateuser.php";
    // filter
    public static String urlFilter = host + "filter.php";

    // Ứng tuyển
    public static String urlApply = host + "apply.php";
    // kiểm tra xem job đã ứng tuyển chưa
    public static String urlCheckApply = host + "check_apply.php";
    // Danh sách vị trí
    public static String urlJobList = host + "job_list.php";
    // filter CV
    public static String urlFilterCV = host + "filterCV.php";
    // update application, delete application
    public static String urlUpdateApplication = host + "update_application.php";
    public static String urlDeleteApplication = host + "delete_application.php";


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
        skillCVArray.add(new SkillCV("Kỹ năng tiếng anh", 5, "temp"));
        skillCVArray.add(new SkillCV("Kỹ năng tin học", 3, "temp"));

    }


}
