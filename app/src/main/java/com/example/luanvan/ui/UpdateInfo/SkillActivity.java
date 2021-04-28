package com.example.luanvan.ui.UpdateInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.skill.SkillPickAdapter;
import com.example.luanvan.ui.Model.Skill;
import com.example.luanvan.ui.Model.SkillKey;
import com.example.luanvan.ui.User.NotificationsFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SkillActivity extends AppCompatActivity {
    Toolbar toolbar;
    public static EditText editname, editmota;
    RatingBar ratingBar;
    Button btnhuy, btncapnhat;
    int id = 0; // id skill trên csdl
    int position = 0; // trên mảng arraylist, thứ tự
    int update = 0;
    String url = "";
    ProgressDialog progressDialog;
    Handler handler, handler1;
    public static int idskill = 0;
    public static String skillName  = "";
    BottomSheetDialog bottomSheetDialog;
    RecyclerView recyclerView;
    SkillPickAdapter adapter;
    ArrayList<SkillKey> arrayList;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);
        anhxa();
        actionBar();
        eventUpdate();
        getInfo();
        eventSkill();



    }

    private void search() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void eventSkill() {
        editname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListSkill();
            }
        });
    }

    private void eventListSkill() {
        bottomSheetDialog = new BottomSheetDialog(SkillActivity.this,  R.style.BottomSheetTheme);
        View view = LayoutInflater.from(SkillActivity.this).inflate(R.layout.bottom_sheet_skill, (ViewGroup) findViewById(R.id.bottom_sheet));
        Button btnChoose = (Button) view.findViewById(R.id.buttonchon);
        Button btnCancel = (Button) view.findViewById(R.id.buttonhuy);
        searchView  = (SearchView)view. findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editname.setText(skillName);
                bottomSheetDialog.dismiss();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SkillActivity.this, LinearLayoutManager.VERTICAL, false));
        adapter = new SkillPickAdapter(SkillActivity.this, arrayList, SkillActivity.this);
        recyclerView.setAdapter(adapter);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

    }

    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    private void getInfo() {
        // 10: add, 3: update
        int check = getIntent().getIntExtra("confirm", 0);
        if(check == 3){
            update = 1;
            Skill skill = (Skill) getIntent().getSerializableExtra("com/example/luanvan/ui/Adapter/skill");
            position = getIntent().getIntExtra("position", 0);
            id = skill.getId();
            editname.setText(skill.getName());
            editmota.setText(skill.getDescription());
            ratingBar.setRating(skill.getStar());
        }
    }



    private void eventUpdate() {
        btncapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(getApplicationContext(), "idskill : " + idskill, Toast.LENGTH_SHORT).show();
                final String name = editname.getText().toString();
                final String mota = editmota.getText().toString();
                final float star = ratingBar.getRating();
              //  Toast.makeText(getApplicationContext(), "Cập nhật :" + star, Toast.LENGTH_SHORT).show();
                if(name.equals("") || mota.equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin" , Toast.LENGTH_SHORT).show();
                }else if(star == 0){
                    Toast.makeText(getApplicationContext(), "Đánh giá kỹ năng" , Toast.LENGTH_SHORT).show();
                }else {
                    loading();

                    if(update == 1){
                    //    Toast.makeText(getApplicationContext(), "id skill " + SkillActivity.idskill , Toast.LENGTH_SHORT).show();
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlUpdateSkill,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equals("success")){
                                            final Skill skill = new Skill(id, MainActivity.iduser,idskill, name, star, mota);
                                            Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                            MainActivity.skills.set(position, skill);
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> map = new HashMap<>();
                                map.put("id", String.valueOf(id));
                                map.put("idskill", String.valueOf(idskill));
                                map.put("star", String.valueOf(star));
                                map.put("description", mota);
                                return map;
                            }
                        };
                        requestQueue.add(stringRequest);

                        handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Intent intent = new Intent();
                                setResult(3, intent);
                                finish();
                            }
                        }, 2000);
                    }else {

                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlAddSkill,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(!response.equals("fail")){
                                            Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                            id = Integer.parseInt(response.toString());
                                            final Skill skill = new Skill(id, MainActivity.iduser,idskill, name, star, mota);
                                            MainActivity.skills.add(skill);
                                            NotificationsFragment.skillAdapter.notifyDataSetChanged();
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Kỹ năng đã tồn tại hoặc cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> map = new HashMap<>();
                                map.put("iduser", String.valueOf(MainActivity.iduser));
                                map.put("idskill", String.valueOf(idskill));
                                map.put("star", String.valueOf(star));
                                map.put("description", mota);
                                return map;
                            }
                        };
                        requestQueue.add(stringRequest);
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                NotificationsFragment.skillAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                             //   Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                setResult(3, intent);
                                finish();
                            }
                        },1000);
                    }


                }
            }
        });
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        idskill = 0;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editname = (EditText) findViewById(R.id.editskill);
        editmota = (EditText) findViewById(R.id.editmota);
        ratingBar = (RatingBar) findViewById(R.id.rating);
        btnhuy = (Button) findViewById(R.id.buttonhuy);
        btncapnhat = (Button) findViewById(R.id.buttoncapnhat);

        arrayList = new ArrayList<>();
        arrayList.add(new SkillKey(1,"Kỹ năng tuyển dụng"));
        arrayList.add(new SkillKey(2,"kỹ năng đào tạo"));
        arrayList.add(new SkillKey(3,"Kỹ năng bán hàng"));
        arrayList.add(new SkillKey(4,"Kỹ năng phân tích kinh doanh"));
        arrayList.add(new SkillKey(5,"Kỹ năng quản lý công việc"));
        arrayList.add(new SkillKey(6,"Kỹ năng thuyết trình"));
        arrayList.add(new SkillKey(7,"Kỹ năng thuyết phục"));
        arrayList.add(new SkillKey(8,"Kỹ năng đàm phán"));
        arrayList.add(new SkillKey(9,"Kỹ năng giao tiếp"));
        arrayList.add(new SkillKey(10,"Kỹ năng lãnh đạo"));
        arrayList.add(new SkillKey(11,"Kỹ năng sư phạm"));
        arrayList.add(new SkillKey(12,"Tin học văn phòng"));
        arrayList.add(new SkillKey(13,"Tiếng anh giao tiếp"));
        arrayList.add(new SkillKey(14,"Tiếng Hàn"));
        arrayList.add(new SkillKey(15,"Tiếng Trung"));
        arrayList.add(new SkillKey(16,"Tiếng Nhật"));
        arrayList.add(new SkillKey(17,"SQL server"));
        arrayList.add(new SkillKey(18,"Kotlin"));
        arrayList.add(new SkillKey(19,"Objective C"));
        arrayList.add(new SkillKey(20,"TCP/IP"));
        arrayList.add(new SkillKey(21,"Full stack"));
        arrayList.add(new SkillKey(22,"Unix"));
        arrayList.add(new SkillKey(23,"UX/UI"));
        arrayList.add(new SkillKey(24,"ReactJS"));
        arrayList.add(new SkillKey(25,"DNS"));
        arrayList.add(new SkillKey(26,"Photoshop"));
        arrayList.add(new SkillKey(27,"Android"));
        arrayList.add(new SkillKey(28,"Angular "));
        arrayList.add(new SkillKey(29,"Docker"));
        arrayList.add(new SkillKey(30,"Redux"));
        arrayList.add(new SkillKey(31,"TOEIC "));
        arrayList.add(new SkillKey(32,"Teamwork"));
        arrayList.add(new SkillKey(33,"Flutter"));
        arrayList.add(new SkillKey(34,"React native"));
        arrayList.add(new SkillKey(35,"Excel, Word, PowerPoint"));
        arrayList.add(new SkillKey(36,"Unity"));
        arrayList.add(new SkillKey(37,"Tester"));
        arrayList.add(new SkillKey(38,"PHP"));
        arrayList.add(new SkillKey(39,"MySQL"));
        arrayList.add(new SkillKey(40,"Database"));
        arrayList.add(new SkillKey(41,"IELTS"));
        arrayList.add(new SkillKey(42,"Windows Phone"));
        arrayList.add(new SkillKey(43,"WordPress"));
        arrayList.add(new SkillKey(44,"C#"));
        arrayList.add(new SkillKey(45,"C++"));
        arrayList.add(new SkillKey(46,"Laravel"));
        arrayList.add(new SkillKey(47,"Linux"));
        arrayList.add(new SkillKey(48,"ООР"));
        arrayList.add(new SkillKey(49,"Oracle"));
        arrayList.add(new SkillKey(50,"IELTS "));
        arrayList.add(new SkillKey(51,"JAVA"));
        arrayList.add(new SkillKey(52,"ASP.NET"));
        arrayList.add(new SkillKey(53,"Javascript"));
        arrayList.add(new SkillKey(54,"CSS"));
        arrayList.add(new SkillKey(55,"Designer"));
        arrayList.add(new SkillKey(56,"Django"));
        arrayList.add(new SkillKey(57,"REST API"));
        arrayList.add(new SkillKey(58,"Python"));
        arrayList.add(new SkillKey(59,"Ruby on Rails"));


    }
}
