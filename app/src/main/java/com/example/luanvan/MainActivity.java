package com.example.luanvan;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.luanvan.ui.Adapter.update_personal_info.ExperienceAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.SkillAdapter;
import com.example.luanvan.ui.Adapter.update_personal_info.StudyAdapter;
import com.example.luanvan.ui.Model.Area;
import com.example.luanvan.ui.Model.Experience;
import com.example.luanvan.ui.Model.Notification;
import com.example.luanvan.ui.Model.NotificationAdmin;
import com.example.luanvan.ui.Model.Profession;
import com.example.luanvan.ui.Model.Recruiter;
import com.example.luanvan.ui.Model.Skill;
import com.example.luanvan.ui.Model.Study;
import com.example.luanvan.ui.Model.User;
import com.example.luanvan.ui.User.NotificationsFragment;
import com.example.luanvan.ui.admin.AdminFragment;
import com.example.luanvan.ui.dashboard.DashboardFragment;
import com.example.luanvan.ui.home.HomeFragment;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Để adapter.notify trong runable handle sẽ k bị lỗi null
    // adapter của Notification fragment
    public static DatabaseReference mData = FirebaseDatabase.getInstance().getReference("one");
    public static DatabaseReference mUserData = FirebaseDatabase.getInstance().getReference("Users");
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance() ;
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    // fragment notification

    public static int login = 0;
    public static int login_recruiter = 0;
    public static int login_admin = 0;

    public static int iduser = 0;
    public static int idcompany = 0;
    public static String company_name = "";
    public static int checkCV = 0;
    public static ArrayList<Notification> arrayListNotification = new ArrayList<>(); // Danh sách thông báo
    public static ArrayList<NotificationAdmin> arrayListAdminNotification = new ArrayList<>(); // Danh sách thông báo của admin
    public static int k = 0; // số lương thông báo mới
    public static int k_chat = 0; // Số lương tin nhắn mới
    public static int k_admin = 0; // số lương thông báo mới của admin
    // 1: có cv, 2: chưa
    public static String uid = ""; // của firebase
    public static String username = "";
    public static String password = "";
    public static String email_recruiter = "";
    public static String urlCV = "";
    public static User user = new User();
    public static Recruiter recruiter = new Recruiter();
    public static ArrayList<Study> studies = new ArrayList<>();
    public static ArrayList<Experience> experiences = new ArrayList<>();
    public static ArrayList<Skill> skills = new ArrayList<>();
    // list CV
    public static ArrayList<PdfCV> arrayListCV = new ArrayList<>();
    public static ArrayList<Area>  arraylistChosenArea = new ArrayList<>(); // Đã chọn
    public static ArrayList<Profession> arrayListChosenProfession = new ArrayList<>();
    // default CV info
    // - info connect : user
    // - Experience
    public static ExperienceCV experienceCV = new ExperienceCV("temp","TMA Solution","Web Developer", "2015", "2020", "Do many projects for Japanese customer");
    public static ArrayList<SkillCV> skillCVArray = new ArrayList<>();
    public static UserCV userCVDefault = new UserCV("Ton N.", "Full-stack Developer", "batphuongtrinhvoti@gmail.com","03239232","Can Tho city","Male","20/10/1999");
    public static StudyCV studyCV = new StudyCV("temp", "Can Tho University", "IT", "10/2017", "10/2021", "GPA: 3.2/4, taking part in many activities");
    public static String goalDefault = "Working in a professional environment to advance my technical skill";



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


    public static String host = "http://10.3.74.54:8888/luanvan/";
    public static String urljob1 = host + "job1.php?page=";
    public static String urlJobCompany = host + "job_company.php";
    public static String urlJobRelate = host + "job_relate.php";

    // job apply
    public static String urlJobApply = host + "job_apply.php";
    public static String urlJobApplyLoad = host + "job_apply_load.php?page=";
    // company
    public static String urlcompany = host + "company.php";
    public static String urlCompanyInfo = host + "get_company_info.php";
    public static String urlCompanySearch = host + "get_company.php";
    public static String urlCompanyTop = host + "company_top.php";
    public static String urlUpdateCompany = host + "update_company.php";
    // dang nhap
    public static String urlRegister = host + "register.php";
    public static String urlCheckRegister = host + "check_register.php";
    public static String urllogin = host + "login.php";
    // Đăng nhập nhà tuyển dụng
    public static String urlLoginRecruiter = host + "login_recruiter.php";
    public static String urlUpdateRecruiter = host + "update_recruiter.php";
    public static String urlGetInfoRecruiter = host + "recruiter.php";
    // Đăng nhập admin
    public static String urlLoginAdmin = host + "login_admin.php";
    // User
    public static String urluserinfo = host + "userinfo.php";
    public static String urlUserAdmin = host + "user_admin.php";
    public static String urlupdateuser = host + "updateuser.php";
    public static String urlUpdateUserStatus = host + "update_user_status.php";
    public static String urlPutMain = host + "user_update_maincv.php";
    // filter
    public static String urlFilter = host + "filter.php";

    // Ứng tuyển
    public static String urlApply = host + "apply.php";
    public static String urlApplyAgain = host + "apply_again.php";
    // kiểm tra xem job đã ứng tuyển chưa
    public static String urlCheckApply = host + "check_apply.php";
    // Danh sách vị trí
    public static String urlJobList = host + "job_list.php";
    // filter CV
    public static String urlFilterCV = host + "filterCV.php";
    // Candidate document
    public static String urlCandidateDocument = host + "candidate_document.php";
    // update application, delete application
    public static String urlUpdateApplication = host + "update_application.php";
    public static String urlDeleteApplication = host + "delete_application.php";
    // start end recruiting
    public static String urlStartEndRecruiting = host + "recruit_start_end.php";
    // update job
    public static String urlUpdateJob = host + "update_job.php";
    // create job
    public static String urlCreateJob = host + "create_job.php";
    // delete job
    public static String urlDeleteJob = host + "delete_job.php";

    // post notification
    public static String urlPostNotification = host + "post_notification.php";
    // post notification admin
    public static String urlPostNotificationForAdmin = host + "post_notification_admin.php";
    // check post or not
    public static String urlCheckPost = host + "check_post.php";
    // get notification
    public static String urlGetNotification =  host + "get_notification.php";
    // get notification recruiter
    public static String urlGetNotificationRecruiter =  host + "get_notification_recruiter.php";
    public static String urlUpdateRecruiterStatus = host + "update_recruiter_status.php";
    public static String urlRecruiterAdmin = host + "recruiter_admin.php";
    // get notification admin
    public static String getUrlGetNotificationAdmin = host + "get_notification_admin.php";
    // update notification status
    public static String urlUpdateNotificationStatus = host + "update_status_notification.php";
    // update notification status admin
    public static String urlUpdateNotificationAdminStatus = host + "update_notification_admin.php";
    // read all notification
    public static String urlUpdateReadAllNotification = host + "update_read_all_notification.php";
    // job from notification
    public static String urlJobFromNotification = host + "job_from_notification.php";

    // schedule
    public static String urlSchedule = host + "create_schedule.php";
    // get schedule
    public static String urlGetSchedule = host + "get_schedule.php";
    public static String urlGetScheduleCandidate = host + "get_schedule_candidate.php";
    // delete schedule
    public static String urlDeleteSchedule = host + "delete_schedule.php";
    // update schedule
    public static String urlUpdateSchedule = host + "update_schedule.php";
    // update schedule candidate
    public static String urlUpdateScheduleCandidate = host + "update_schedule_candidate.php";
    // user list
    public static String urlUserList = host + "user_list.php";
    // ap id
    public static String urlGetApplicationId = host + "get_ap_id.php";
    // admin
    // duyệt tin
    public static String urlJobPost = host + "job_post.php?page=";
    // accept, reject job
    public static String urlAcceptJob = host + "accept_job.php";
    // get recent created job
    public static String urlGetJobRecentCreate = host + "get_job_recent_create.php";
    // check cv in application
    public static String urlCheckCVInApplication = host + "check_cv_in_application.php";
    // Area
    public static String urlAddAreaCandidate = host + "add_area_candidate.php";
    public static String urlGetCandidateArea = host + "get_candidate_area.php";
    // Profession
    public static String urlAddProfessionCandidate = host + "add_profession_candidate.php";
    public static String urlGetCandidateProfession = host + "get_candidate_profession.php";

    // get iduser firebase
    public static String urlGetIdUserFirebase = host + "get_iduser_f.php";

    // switch mode
    public static String urlSwitchMode = host + "switch_mode.php";
    public static String urlCheckSwitch = host + "check_switch.php";


    // search user
    public static String urlSearchUser = host + "user_search.php";


    // evaluation
    public static String urlGetRemark = host + "get_remark.php";
    public static String urlRemark = host + "remark.php";
    public static String urlDeleteAssessment = host + "delete_assessment.php";
    public static String urlCheckAssessment = host + "check_assess.php";
    // put CV
    public static String urlPutCV = host + "putcv.php";
    public static String urlDeleteCV = host + "deletecv.php";
    public static String urlCheckMainCV = host + "check_maincv.php";

    ///------------------------------ JOB SKILL
    public static String urlAddJobSkill = host + "add_skill_job.php";
    public static String urlGetJobSkill = host + "get_job_skill.php";
    public static String urlUpdateJobSkill = host + "update_skill_job.php";

    // Candidate basic info
    ///------------------------------ SELECT

    public static String urlCandidateExperience = host + "candidate_experience.php";
    public static String urlCandidateStudy = host + "candidate_study.php";
    public static String urlCandidateSkill = host + "candidate_skill.php";

    ///------------------------------ INSERT

    public static String urlAddStudy = host + "add_study.php";
    public static String urlAddExperience = host + "add_experience.php";
    public static String urlAddSkill = host + "add_skill.php";

    ///------------------------------ UPDATE

    public static String urlUpdateStudy = host + "update_study.php";
    public static String urlUpdateExperience = host + "update_experience.php";
    public static String urlUpdateSkill = host + "update_skill.php";

    ///------------------------------ DELETE

    public static String urlDeleteStudy = host + "delete_study.php";
    public static String urlDeleteExperience = host + "delete_experience.php";
    public static String urlDeleteSkill = host + "delete_skill.php";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // Bị sửa
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_admin)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);


//
//        navView.setOnNavigationItemSelectedListener(th);





//        try{
//            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//
//        }catch (NullPointerException e){
//         //   Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
//        }
        skillCVArray.add(new SkillCV("English skill", 5, "temp"));
        skillCVArray.add(new SkillCV("Computer skill", 4, "temp"));

    }




}
