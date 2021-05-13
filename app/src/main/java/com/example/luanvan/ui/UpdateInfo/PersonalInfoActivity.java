package com.example.luanvan.ui.UpdateInfo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Adapter.position_a.PositionPickAdapter;
import com.example.luanvan.ui.Adapter.skill.AreaBottomSheetTagAdapter;
import com.example.luanvan.ui.Adapter.skill.ProfessionBottomSheetTagAdapter;
import com.example.luanvan.ui.Adapter.skill.SkillTagAdapter;
import com.example.luanvan.ui.Adapter.skill.TagAdapter;
import com.example.luanvan.ui.Adapter.skill.TagAreaAdapter;
import com.example.luanvan.ui.Adapter.skill.TagProfessionAdapter;
import com.example.luanvan.ui.Model.Area;
import com.example.luanvan.ui.Model.AreaCandidate;
import com.example.luanvan.ui.Model.Position;
import com.example.luanvan.ui.Model.Profession;
import com.example.luanvan.ui.Model.ProfessionCandidate;
import com.example.luanvan.ui.Model.SkillCandidate;
import com.example.luanvan.ui.Model.SkillKey;
import com.example.luanvan.ui.recruiter.PostNews.CreateJobActivity;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonalInfoActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editname, editposition, editbirthday, editaddress, editemail, editphone, editmota;
    RadioButton btnNam, btnNu;
    Button btnHuy, btnCapNhat;
    String date_post = "";
    public static int idposition = 0;
    BottomSheetDialog bottomSheetPosition;
    public static String position  = "";
    RecyclerView recyclerView;
    PositionPickAdapter adapter;
    ArrayList<Position> arrayList;
    SearchView searchView;
    RecyclerView recyclerViewArea, recyclerViewTagArea, recyclerViewProfession, recyclerViewTagProfession;
    TagAreaAdapter tagAdapter; // Những tag trong recycleview
    TagProfessionAdapter tagProfessionAdapter;
    ArrayList<AreaCandidate> arraylistArea; // Trên bottemsheet có check hay k luôn
    ArrayList<ProfessionCandidate> arrayListProfession;
    BottomSheetDialog bottomSheetArea, bottomSheetProfession;
    SearchView searchViewArea, searchViewProfession;
    AreaBottomSheetTagAdapter areaAdapter; // adapter trong bottomsheet
    ProfessionBottomSheetTagAdapter professionAdapter;
    ProgressDialog progressDialog;
    Handler handler ;
    ImageView imgAddArea, imgAddProfession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        anhxa();
        actionBar();
        idposition = MainActivity.user.getIdposition();
        eventUpdate();
        eventPickDate();
        eventPosition();
        eventArea();
        eventProfession();

    }

    private void eventProfession() {
        imgAddProfession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), "Â", Toast.LENGTH_SHORT).show();
                bottomSheetProfession = new BottomSheetDialog(PersonalInfoActivity.this, R.style.BottomSheetTheme);
                View view = LayoutInflater.from(PersonalInfoActivity.this).inflate(R.layout.bottom_sheet_profession, (ViewGroup) findViewById(R.id.bottom_sheet));
                Button btnChoose = (Button) view.findViewById(R.id.buttonchon);
                Button btnCancel = (Button) view.findViewById(R.id.buttonhuy);
                bottomSheetProfession.setCancelable(false);
                searchView = (SearchView) view.findViewById(R.id.searchView);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        professionAdapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        professionAdapter.getFilter().filter(newText);
                        return false;
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tagProfessionAdapter.notifyDataSetChanged();
                        bottomSheetProfession.dismiss();
                    }
                });
                btnChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tagProfessionAdapter.notifyDataSetChanged();
                     //   Toast.makeText(getApplicationContext(), "s " + arrayListProfession.size(), Toast.LENGTH_SHORT).show();
                        bottomSheetProfession.dismiss();
                    }
                });
                recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(PersonalInfoActivity.this, LinearLayoutManager.VERTICAL, false));
                professionAdapter = new ProfessionBottomSheetTagAdapter(PersonalInfoActivity.this, arrayListProfession, PersonalInfoActivity.this, MainActivity.arrayListChosenProfession);
                recyclerView.setAdapter(professionAdapter);
                bottomSheetProfession.setCancelable(false);
                bottomSheetProfession.setContentView(view);
                bottomSheetProfession.show();
            }

        });

    }


    private void eventArea() {
        imgAddArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), "Â", Toast.LENGTH_SHORT).show();
                bottomSheetArea = new BottomSheetDialog(PersonalInfoActivity.this, R.style.BottomSheetTheme);
                View view = LayoutInflater.from(PersonalInfoActivity.this).inflate(R.layout.bottom_sheet_area, (ViewGroup) findViewById(R.id.bottom_sheet));
                Button btnChoose = (Button) view.findViewById(R.id.buttonchon);
                Button btnCancel = (Button) view.findViewById(R.id.buttonhuy);
                bottomSheetArea.setCancelable(false);
                searchView = (SearchView) view.findViewById(R.id.searchView);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        areaAdapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        areaAdapter.getFilter().filter(newText);
                        return false;
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetArea.dismiss();
                        tagAdapter.notifyDataSetChanged();
                    }
                });
                btnChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tagAdapter.notifyDataSetChanged();
                        bottomSheetArea.dismiss();
                    }
                });
                recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(PersonalInfoActivity.this, LinearLayoutManager.VERTICAL, false));
                areaAdapter = new AreaBottomSheetTagAdapter(PersonalInfoActivity.this, arraylistArea, PersonalInfoActivity.this, MainActivity.arraylistChosenArea);
                recyclerView.setAdapter(areaAdapter);
                bottomSheetArea.setContentView(view);
                bottomSheetArea.show();
            }

        });

    }


    private void postProfessionCandidate() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlAddProfessionCandidate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        if(response.equals("success")){
//                            Toast.makeText(getApplicationContext(), "Cập nhật kỹ năng cho tin tuyển dụng thành công", Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(getApplicationContext(), "Cập nhật kỹ năng cho tin tuyển dụng thất bại", Toast.LENGTH_SHORT).show();
//                        }
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
                JSONArray jsonArray = new JSONArray();
                for(int i=0; i < MainActivity.arrayListChosenProfession.size(); i++){
                    JSONObject object = new JSONObject();
                    try {
                        object.put("iduser", String.valueOf(MainActivity.iduser));
                        object.put("idprofession", String.valueOf(MainActivity.arrayListChosenProfession.get(i).getId()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(object);
                }
                map.put("iduser", String.valueOf(MainActivity.iduser));
                map.put("jsonarray", jsonArray.toString());


                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void postAreaCandidate() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlAddAreaCandidate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        if(response.equals("success")){
//                            Toast.makeText(getApplicationContext(), "Cập nhật kỹ năng cho tin tuyển dụng thành công", Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(getApplicationContext(), "Cập nhật kỹ năng cho tin tuyển dụng thất bại", Toast.LENGTH_SHORT).show();
//                        }
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
                JSONArray jsonArray = new JSONArray();
                for(int i=0; i < MainActivity.arraylistChosenArea.size(); i++){
                    JSONObject object = new JSONObject();
                    try {
                        object.put("iduser", String.valueOf(MainActivity.iduser));
                        object.put("idarea", String.valueOf(MainActivity.arraylistChosenArea.get(i).getId()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(object);
                }
                map.put("iduser", String.valueOf(MainActivity.iduser));
                map.put("jsonarray", jsonArray.toString());


                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
    private void eventPosition() {
        editposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListPosition();
            }
        });

    }
    private void eventListPosition() {
    //    Toast.makeText(getApplicationContext(), "sasa", Toast.LENGTH_SHORT).show();
        bottomSheetPosition = new BottomSheetDialog(PersonalInfoActivity.this,  R.style.BottomSheetTheme);
        View view = LayoutInflater.from(PersonalInfoActivity.this).inflate(R.layout.bottom_sheet_position_user, (ViewGroup) findViewById(R.id.bottom_sheet));
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
                bottomSheetPosition.dismiss();
                idposition = MainActivity.user.getIdposition();
            }
        });
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editposition.setText(position);
                bottomSheetPosition.dismiss();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PersonalInfoActivity.this, LinearLayoutManager.VERTICAL, false));
        adapter = new PositionPickAdapter(PersonalInfoActivity.this, arrayList, PersonalInfoActivity.this, 1); // kind 1: Từ ứng viên cho, kind 2: Từ nhà tuyển dụng chọn
        recyclerView.setAdapter(adapter);
        bottomSheetPosition.setContentView(view);
        bottomSheetPosition.show();

    }




    private void eventUpdate() {
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editname.getText().toString();
                final String position = editposition.getText().toString();
                //    final String birthday = editbirthday.getText().toString();
                final String address = editaddress.getText().toString();
                final String email = editemail.getText().toString();
                final String phone = editphone.getText().toString();
                final String mota = editmota.getText().toString();
                if(name.equals("") || position.equals("") || editbirthday.getText().toString().equals("") || address.equals("")
                        || email.equals("") || phone.equals("") || mota.equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else if(!isEmailValid(editemail.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Sai định dạng email", Toast.LENGTH_SHORT).show();
                }else if(editphone.getText().length() > 10){
                    Toast.makeText(getApplicationContext(), "Không đúng định dạng số điện thoại", Toast.LENGTH_SHORT).show();
                } else
                {
                    loading();
                    String gender = "";
                    if(btnNam.isChecked()){
                        gender += 0;
                    }else {
                        gender += 1;
                    }
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    final String finalGender = gender;
                    final String finalGender1 = gender;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlupdateuser,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("success")){
                                        Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                        postAreaCandidate();
                                        postProfessionCandidate();
                                        MainActivity.user.setAddress(address);
                                        MainActivity.user.setBirthday(date_post);
                                        MainActivity.user.setEmail(email);
                                        MainActivity.user.setGender(Integer.parseInt(finalGender1));
                                        MainActivity.user.setIntroduction(mota);
                                        MainActivity.user.setName(name);
                                        MainActivity.user.setPosition(position);
                                        MainActivity.user.setPhone(Integer.parseInt(phone));
                                        MainActivity.user.setIdposition(idposition);
                                        MainActivity.user.setPosition(position);
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                Intent intent = new Intent();
                                                setResult(234);
                                                finish();
                                            }
                                        },2000);

                                    }else {
                                        Toast.makeText(getApplicationContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<>();
                            map.put("iduser", String.valueOf(MainActivity.user.getId()));
                            map.put("name", name);
                            map.put("email", email);
                            map.put("gender", finalGender);
                            map.put("address", address);
                            map.put("phone", phone);
                            map.put("idposition", String.valueOf(idposition));
                            map.put("birthday", date_post);
                            map.put("introduction", mota);
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);

                }
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void showDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String today = dateFormat.format(Calendar.getInstance().getTime());
        final Date today1 = dateFormat.parse(today);
        final Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


                if(calendar.getTime().after(today1)){
                    Toast.makeText(getApplicationContext(), "Phải lớn nhỏ ngày hiện tại", Toast.LENGTH_SHORT).show();
                }else {
                    editbirthday.setText(dateFormat.format(calendar.getTime()));
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = fmt.parse(fmt.format(calendar.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    date_post = fmt.format(date);
                }

            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }
    private void eventPickDate() {
        editbirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showDate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getInfo() {
        if(MainActivity.login == 1){
            editname.setText(MainActivity.user.getName());
            editemail.setText(MainActivity.user.getEmail());
            String ngay = MainActivity.user.getBirthday();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = fmt.parse(ngay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
            editbirthday.setText(fmtOut.format(date));
            date_post = fmt.format(date);
            editposition.setText(MainActivity.user.getPosition());
            editphone.setText("0"+ MainActivity.user.getPhone() + "");
            editmota.setText(MainActivity.user.getIntroduction());
            editaddress.setText(MainActivity.user.getAddress());
            if(MainActivity.user.getGender() == 0 ){
                btnNam.setChecked(true);
            }else {
                btnNu.setChecked(true);
            }

        }

    }

    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editaddress = (EditText) findViewById(R.id.editaddress);
        editname = (EditText) findViewById(R.id.editname);
        editmota = (EditText) findViewById(R.id.editmota);
        editphone = (EditText) findViewById(R.id.editphone);
        editposition = (EditText) findViewById(R.id.editposition);
        editemail = (EditText) findViewById(R.id.editemail);
        editbirthday = (EditText) findViewById(R.id.editbirthday);
        btnNam = (RadioButton) findViewById(R.id.radionam);
        btnNu = (RadioButton) findViewById(R.id.radionu);
        btnHuy = (Button) findViewById(R.id.buttonhuy);
        btnCapNhat = (Button) findViewById(R.id.buttoncapnhat);
        getInfo();
        arrayList = new ArrayList<>();
        getDataPosition();
        arraylistArea = new ArrayList<>();
        arrayListProfession = new ArrayList<>();
        getDataArea();
        getDataProfession();
        imgAddProfession = (ImageView) findViewById(R.id.imgaddnganhnghe);
        imgAddArea = (ImageView) findViewById(R.id.imgaddkhuvuc);
        recyclerViewTagArea = (RecyclerView) findViewById(R.id.recycleview);
        recyclerViewTagArea.setFocusable(false);
        recyclerViewTagArea.setClickable(false);
        recyclerViewTagArea.setHasFixedSize(true);
        recyclerViewTagProfession = (RecyclerView) findViewById(R.id.recycleviewprofession);
        recyclerViewTagProfession.setFocusable(false);
        recyclerViewTagProfession.setClickable(false);
        recyclerViewTagProfession.setHasFixedSize(true);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager();
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        recyclerViewTagArea.setLayoutManager(layoutManager);
        FlexboxLayoutManager layoutManager2 = new FlexboxLayoutManager();
        layoutManager2.setFlexWrap(FlexWrap.WRAP);
        layoutManager2.setFlexDirection(FlexDirection.ROW);
        layoutManager2.setAlignItems(AlignItems.STRETCH);
        recyclerViewTagProfession.setLayoutManager(layoutManager2);

        tagAdapter = new TagAreaAdapter(PersonalInfoActivity.this, MainActivity.arraylistChosenArea, PersonalInfoActivity.this, arraylistArea );
        recyclerViewTagArea.setAdapter(tagAdapter);
        tagProfessionAdapter = new TagProfessionAdapter(PersonalInfoActivity.this, MainActivity.arrayListChosenProfession, PersonalInfoActivity.this, arrayListProfession);
        recyclerViewTagProfession.setAdapter(tagProfessionAdapter);


    }

    private void getDataProfession() {
        arrayListProfession.add(new ProfessionCandidate(1,"Kinh doanh / Bán hàng",0));
        arrayListProfession.add(new ProfessionCandidate(2,"Biên / Phiên dịch",0));
        arrayListProfession.add(new ProfessionCandidate(3,"Báo chí / Truyền hình",0));
        arrayListProfession.add(new ProfessionCandidate(4,"Bưu chính - Viễn thông",0));
        arrayListProfession.add(new ProfessionCandidate(5,"Bảo hiểm",0));
        arrayListProfession.add(new ProfessionCandidate(6,"Bất động sản",0));
        arrayListProfession.add(new ProfessionCandidate(7,"Chứng khoán / Vàng / Ngoại tệ",0));
        arrayListProfession.add(new ProfessionCandidate(8,"Công nghệ cao",0));
        arrayListProfession.add(new ProfessionCandidate(9,"Cơ khí - Chế tạo / Tự động hóa",0));
        arrayListProfession.add(new ProfessionCandidate(10,"Du lịch",0));
        arrayListProfession.add(new ProfessionCandidate(11,"Dầu khí/Hóa chất",0));
        arrayListProfession.add(new ProfessionCandidate(12,"Dệt may / Da giày",0));
        arrayListProfession.add(new ProfessionCandidate(13,"Dịch vụ khách hàng",0));
        arrayListProfession.add(new ProfessionCandidate(14,"Điện tử viễn thông",0));
        arrayListProfession.add(new ProfessionCandidate(15,"Điện / Điện tử / Điện lạnh",0));
        arrayListProfession.add(new ProfessionCandidate(16,"Giáo dục / Đào tạo",0));
        arrayListProfession.add(new ProfessionCandidate(17,"Hoá học 7 Sinh học",0));
        arrayListProfession.add(new ProfessionCandidate(18,"Hoạch định/Dự án",0));
        arrayListProfession.add(new ProfessionCandidate(19,"Hàng gia dụng",0));
        arrayListProfession.add(new ProfessionCandidate(20,"Hàng hải",0));
        arrayListProfession.add(new ProfessionCandidate(21,"Hàng không",0));
        arrayListProfession.add(new ProfessionCandidate(22,"Hành chính / Văn phòng",0));
        arrayListProfession.add(new ProfessionCandidate(23,"In ấn / Xuất bản",0));
        arrayListProfession.add(new ProfessionCandidate(24,"IT Phần cứng / Mạng",0));
        arrayListProfession.add(new ProfessionCandidate(25,"IT phần mềm",0));
        arrayListProfession.add(new ProfessionCandidate(26,"Khách sạn / Nhà hàng",0));
        arrayListProfession.add(new ProfessionCandidate(27,"Kế toán / Kiểm toán",0));
        arrayListProfession.add(new ProfessionCandidate(28,"Marketing / Truyền thông - Quảng cáo",0));
        arrayListProfession.add(new ProfessionCandidate(29,"Môi trường / Xử lý chất thải",0));
        arrayListProfession.add(new ProfessionCandidate(30,"Mỹ phẩm / Trang sức",0));
        arrayListProfession.add(new ProfessionCandidate(31,"Mỹ thuật / Nghệ thuật / Điện ảnh",0));
        arrayListProfession.add(new ProfessionCandidate(32,"Ngân hàng / Tài chính",0));
        arrayListProfession.add(new ProfessionCandidate(33,"Nhân sự",0));
        arrayListProfession.add(new ProfessionCandidate(34,"Nông - Lâm / Ngư nghiệp",0));
        arrayListProfession.add(new ProfessionCandidate(35,"Luật/Pháp lý",0));
        arrayListProfession.add(new ProfessionCandidate(36,"Quản lý chất lượng (QA/QC)",0));
        arrayListProfession.add(new ProfessionCandidate(37,"Quản lý điều hành",0));
        arrayListProfession.add(new ProfessionCandidate(38,"Thiết kế đồ họa",0));
        arrayListProfession.add(new ProfessionCandidate(39,"Thời trang",0));
        arrayListProfession.add(new ProfessionCandidate(40,"Thực phẩm / Đồ uống",0));
        arrayListProfession.add(new ProfessionCandidate(41,"Tư vấn",0));
        arrayListProfession.add(new ProfessionCandidate(42,"Tổ chức sự kiện / Quà tặng",0));
        arrayListProfession.add(new ProfessionCandidate(43,"Vận tải / Kho vận",0));
        arrayListProfession.add(new ProfessionCandidate(44,"Logistics",0));
        arrayListProfession.add(new ProfessionCandidate(45,"Xuất nhập khẩu",0));
        arrayListProfession.add(new ProfessionCandidate(46,"Xây dựng",0));
        arrayListProfession.add(new ProfessionCandidate(47,"Y tế / Dược",0));
        arrayListProfession.add(new ProfessionCandidate(48,"Công nghệ Ô tô",0));
        arrayListProfession.add(new ProfessionCandidate(49,"An toàn lao động",0));
        arrayListProfession.add(new ProfessionCandidate(50,"Bán hàng kỹ thuật",0));
        arrayListProfession.add(new ProfessionCandidate(51,"Bán lẻ / bán sỉ",0));
        arrayListProfession.add(new ProfessionCandidate(52,"Bảo trì / Sửa chữa",0));
        arrayListProfession.add(new ProfessionCandidate(53,"Dược phẩm | Công nghệ sinh học",0));
        arrayListProfession.add(new ProfessionCandidate(54,"Địa chất / Khoáng sản",0));
        arrayListProfession.add(new ProfessionCandidate(55,"Hàng cao cấp",0));
        arrayListProfession.add(new ProfessionCandidate(56,"Hàng tiêu dùng",0));
        arrayListProfession.add(new ProfessionCandidate(57,"Kiến trúc",0));
        arrayListProfession.add(new ProfessionCandidate(58,"Phi chính phủ / Phi lợi nhuận",0));
        arrayListProfession.add(new ProfessionCandidate(59,"Sản phẩm công nghiệp",0));
        arrayListProfession.add(new ProfessionCandidate(60,"Sản xuất",0));
        arrayListProfession.add(new ProfessionCandidate(61,"Tài chính / Đầu tư",0));
        arrayListProfession.add(new ProfessionCandidate(62,"Thiết kế nội thất",0));
        arrayListProfession.add(new ProfessionCandidate(63,"Thư ký / Trợ lý",0));
        arrayListProfession.add(new ProfessionCandidate(64,"Spa / Làm đẹp",0));
        arrayListProfession.add(new ProfessionCandidate(65,"Công nghệ thông tin",0));
        arrayListProfession.add(new ProfessionCandidate(66,"NG0 / Phi chính phủ / Phi lợi nhuận",0));
        arrayListProfession.add(new ProfessionCandidate(67,"Ngành nghề khác",0));



    }
    void loading(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    private void getDataArea() {
        arraylistArea.add(new AreaCandidate(1,"An Giang",0));
        arraylistArea.add(new AreaCandidate(2,"Bà Rịa - Vũng Tàu",0));
        arraylistArea.add(new AreaCandidate(3,"Bắc Giang",0));
        arraylistArea.add(new AreaCandidate(4,"Bắc Kạn",0));
        arraylistArea.add(new AreaCandidate(5,"Bạc Liêu",0));
        arraylistArea.add(new AreaCandidate(6,"Bắc Ninh",0));
        arraylistArea.add(new AreaCandidate(7,"Bến Tre",0));
        arraylistArea.add(new AreaCandidate(8,"Bình Định",0));
        arraylistArea.add(new AreaCandidate(9,"Bình Dương",0));
        arraylistArea.add(new AreaCandidate(10,"Bình Phước",0));
        arraylistArea.add(new AreaCandidate(11,"Bình Thuận",0));
        arraylistArea.add(new AreaCandidate(12,"Cà Mau",0));
        arraylistArea.add(new AreaCandidate(13,"Cao Bằng",0));
        arraylistArea.add(new AreaCandidate(14,"Đắk Lắk",0));
        arraylistArea.add(new AreaCandidate(15,"Đắk Nông",0));
        arraylistArea.add(new AreaCandidate(16,"Điện Biên",0));
        arraylistArea.add(new AreaCandidate(17,"Đồng Nai",0));
        arraylistArea.add(new AreaCandidate(18,"Đồng Tháp",0));
        arraylistArea.add(new AreaCandidate(19,"Gia Lai",0));
        arraylistArea.add(new AreaCandidate(20,"Hà Giang",0));
        arraylistArea.add(new AreaCandidate(21,"Hà Nam",0));
        arraylistArea.add(new AreaCandidate(22,"Hà Tĩnh",0));
        arraylistArea.add(new AreaCandidate(23,"Hải Dương",0));
        arraylistArea.add(new AreaCandidate(24,"Hậu Giang",0));
        arraylistArea.add(new AreaCandidate(25,"Hòa Bình",0));
        arraylistArea.add(new AreaCandidate(26,"Hưng Yên",0));
        arraylistArea.add(new AreaCandidate(27,"Khánh Hòa",0));
        arraylistArea.add(new AreaCandidate(28,"Kiên Giang",0));
        arraylistArea.add(new AreaCandidate(29,"Kon Tum",0));
        arraylistArea.add(new AreaCandidate(30,"Lai Châu",0));
        arraylistArea.add(new AreaCandidate(31,"Lâm Đồng",0));
        arraylistArea.add(new AreaCandidate(32,"Lạng Sơn",0));
        arraylistArea.add(new AreaCandidate(33,"Lào Cai",0));
        arraylistArea.add(new AreaCandidate(34,"Long An",0));
        arraylistArea.add(new AreaCandidate(35,"Nam Định",0));
        arraylistArea.add(new AreaCandidate(36,"Nghệ An",0));
        arraylistArea.add(new AreaCandidate(37,"Ninh Bình",0));
        arraylistArea.add(new AreaCandidate(38,"Ninh Thuận",0));
        arraylistArea.add(new AreaCandidate(39,"Phú Thọ",0));
        arraylistArea.add(new AreaCandidate(40,"Quảng Bình",0));
        arraylistArea.add(new AreaCandidate(41,"Quảng Nam",0));
        arraylistArea.add(new AreaCandidate(42,"Quảng Ngãi",0));
        arraylistArea.add(new AreaCandidate(43,"Quảng Ninh",0));
        arraylistArea.add(new AreaCandidate(44,"Quảng Trị",0));
        arraylistArea.add(new AreaCandidate(45,"Sóc Trăng",0));
        arraylistArea.add(new AreaCandidate(46,"Sơn La",0));
        arraylistArea.add(new AreaCandidate(47,"Tây Ninh",0));
        arraylistArea.add(new AreaCandidate(48,"Thái Bình",0));
        arraylistArea.add(new AreaCandidate(49,"Thái Nguyên",0));
        arraylistArea.add(new AreaCandidate(50,"Thanh Hóa",0));
        arraylistArea.add(new AreaCandidate(51,"Thừa Thiên Huế",0));
        arraylistArea.add(new AreaCandidate(52,"Tiền Giang",0));
        arraylistArea.add(new AreaCandidate(53,"Trà Vinh",0));
        arraylistArea.add(new AreaCandidate(54,"Tuyên Quang",0));
        arraylistArea.add(new AreaCandidate(55,"Vĩnh Long",0));
        arraylistArea.add(new AreaCandidate(56,"Vĩnh Phúc",0));
        arraylistArea.add(new AreaCandidate(57,"Yên Bái",0));
        arraylistArea.add(new AreaCandidate(58,"Phú Yên",0));
        arraylistArea.add(new AreaCandidate(59,"Cần Thơ",0));
        arraylistArea.add(new AreaCandidate(60,"Đà Nẵng",0));
        arraylistArea.add(new AreaCandidate(61,"Hải Phòng",0));
        arraylistArea.add(new AreaCandidate(62,"Hà Nội",0));
        arraylistArea.add(new AreaCandidate(63,"TP HCM",0));

    }

    private void getDataPosition() {
        arrayList.add(new Position(1,"Chuyên viên Nhân sự"));
        arrayList.add(new Position(2,"Chuyên viên Tuyển dụng"));
        arrayList.add(new Position(3,"Nhân viên Hành chính - Văn phòng"));
        arrayList.add(new Position(4,"Nhân viên Hành chính - Nhân sự"));
        arrayList.add(new Position(5,"Nhân viên Hành chính - Kế toán."));
        arrayList.add(new Position(6,"Nhân viên kinh doanh"));
        arrayList.add(new Position(7,"Nhân viên bán hàng"));
        arrayList.add(new Position(8,"Sales survey - Tiếp thị"));
        arrayList.add(new Position(9,"Tư vấn bán hàng"));
        arrayList.add(new Position(10,"Trưởng nhóm kinh doanh"));
        arrayList.add(new Position(11,"Giám đốc kinh doanh"));
        arrayList.add(new Position(12,"Giám sát bán hàng"));
        arrayList.add(new Position(13,"Tư vấn tài chính"));
        arrayList.add(new Position(14,"Nghiên cứu thị trường"));
        arrayList.add(new Position(15,"Telesales"));
        arrayList.add(new Position(16,"Sales bảo hiểm"));
        arrayList.add(new Position(17,"Sales Bất động sản"));
        arrayList.add(new Position(18,"Sales Thị trường"));
        arrayList.add(new Position(19,"Nhân viên phát triển thị trường"));
        arrayList.add(new Position(20,"Business Manager"));
        arrayList.add(new Position(21,"Trình Dược Viên"));
        arrayList.add(new Position(22,"Chăm sóc khách hàng"));
        arrayList.add(new Position(23,"Tư vấn đào tạo"));
        arrayList.add(new Position(24,"Thu ngân"));
        arrayList.add(new Position(25,"Lễ tân"));
        arrayList.add(new Position(26,"Giao dịch viên"));
        arrayList.add(new Position(27,"Kế toán"));
        arrayList.add(new Position(28,"Kế toán tổng hợp"));
        arrayList.add(new Position(29,"Nhân viên Marketing"));
        arrayList.add(new Position(30,"Trưởng nhóm Marketing"));
        arrayList.add(new Position(31,"Giám đốc Marketing"));
        arrayList.add(new Position(32,"Digital Marketing"));
        arrayList.add(new Position(33,"Marketing Ads"));
        arrayList.add(new Position(34,"Copywriter"));
        arrayList.add(new Position(35,"Content Writer"));
        arrayList.add(new Position(36,"Admin Fanpage"));
        arrayList.add(new Position(37,"PR Truyền thông"));
        arrayList.add(new Position(38,"SEO"));
        arrayList.add(new Position(39,"Tổ chức sự kiện"));
        arrayList.add(new Position(40,"IT Support / Helpdesk"));
        arrayList.add(new Position(41,"Lập trình viên"));
        arrayList.add(new Position(42,"Thiết kế web"));
        arrayList.add(new Position(43,"Lập trình web"));
        arrayList.add(new Position(44,"Frontend Developer"));
        arrayList.add(new Position(45,"Backend Developer"));
        arrayList.add(new Position(46,"Quản trị mạng"));
        arrayList.add(new Position(47,"Quản trị hệ thống - System admin"));
        arrayList.add(new Position(48,"PHP Developer"));
        arrayList.add(new Position(49,"Java Developer"));
        arrayList.add(new Position(50,".Net Developer"));
        arrayList.add(new Position(51,"Android Developer"));
        arrayList.add(new Position(52,"IOS Developer"));
        arrayList.add(new Position(53,"Game Developer"));
        arrayList.add(new Position(54,"Unity Developer"));
        arrayList.add(new Position(55,"C/C# Developer"));
        arrayList.add(new Position(56,"Cocos Developer"));
        arrayList.add(new Position(57,"Ruby Developer"));
        arrayList.add(new Position(58,"Python Developer"));
        arrayList.add(new Position(59,"Tester"));
        arrayList.add(new Position(60,"QA/QC"));
        arrayList.add(new Position(61,"Giáo viên"));
        arrayList.add(new Position(62,"Trợ giảng"));
        arrayList.add(new Position(63,"Giáo viên tiếng Anh"));
        arrayList.add(new Position(64,"Giáo viên tiếng Trung"));
        arrayList.add(new Position(65,"Phiên dịch viên"));
        arrayList.add(new Position(66,"Phiên dịch viên tiếng Anh"));
        arrayList.add(new Position(67,"Phiên dịch viên tiếng Trung"));
        arrayList.add(new Position(68,"Phiên dịch viên tiếng Nhật"));
        arrayList.add(new Position(69,"Xuất nhập khẩu"));
        arrayList.add(new Position(70,"Nhân viên phục vụ"));
        arrayList.add(new Position(71,"Nhân viên pha chế"));
        arrayList.add(new Position(72,"Luật - Pháp chế"));
        arrayList.add(new Position(73,"Hướng dẫn viên du lịch"));
        arrayList.add(new Position(74,"Nhân viên bán tour"));
        arrayList.add(new Position(75,"Điều hành tour"));
        arrayList.add(new Position(76,"Nghiên cứu phát triển - R&D"));
        arrayList.add(new Position(77,"Designer"));
        arrayList.add(new Position(78,"Thiết kế đồ họa"));
        arrayList.add(new Position(79,"Diễn họa nội thất"));
        arrayList.add(new Position(80,"Thiết kế nội thất"));
        arrayList.add(new Position(81,"Điều dưỡng viên"));
        arrayList.add(new Position(82,"Nhân viên bếp"));
        arrayList.add(new Position(83,"Trợ lý giám đốc"));
        arrayList.add(new Position(84,"Thư ký"));
        arrayList.add(new Position(85,"Search Engine Marketing"));
        arrayList.add(new Position(86,"Nhân viên kho"));
        arrayList.add(new Position(87,"Nhân viên buồng phòng"));
        arrayList.add(new Position(88,"Lập trình di động"));
        arrayList.add(new Position(89,"Kế toán trưởng"));
        arrayList.add(new Position(90,"Thiết kế UX/UI"));
        arrayList.add(new Position(91,"Kỹ sư xây dựng"));
        arrayList.add(new Position(92,"Kỹ sư Cơ khí"));
        arrayList.add(new Position(93,"Kỹ sư điện - Cơ điện"));



    }

}
