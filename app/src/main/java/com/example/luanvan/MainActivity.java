package com.example.luanvan;

import android.os.Bundle;
import android.widget.Toast;

import com.example.luanvan.ui.Model.Experience;
import com.example.luanvan.ui.Model.Skill;
import com.example.luanvan.ui.Model.Study;
import com.example.luanvan.ui.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static int login = 0;
    public static int iduser = 0;
    public static String username = "";
    public static String position = "";
    public static User user = new User();
    public static ArrayList<Study> studies = new ArrayList<>();
    public static ArrayList<Experience> experiences = new ArrayList<>();
    public static ArrayList<Skill> skills = new ArrayList<>();


    public static String host = "http://10.3.74.116:8888/luanvan/";
    public static String urljob1 = host + "job1.php";
    public static String urlcompany = host + "company.php";
    // dang nhap
    public static String urlRegister = host + "register.php";
    public static String urllogin = host + "login.php";
    public static String urluserinfo = host + "userinfo.php";
    public static String urlupdateuser = host + "updateuser.php";
    public static String urlupdatestudy = host + "updatestudy.php";
    public static String urlupdateexperience = host + "updatexperience.php";
    public static String urlschool = host + "school.php";
    public static String urlexperience = host + "experience.php";
    public static String urlupdateskill = host + "updateskill.php";
    public static String urlskill = host + "skill.php";


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
