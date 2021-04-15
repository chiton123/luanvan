package com.example.luanvan.ui.UpdateInfo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.luanvan.ui.Model.Position;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        anhxa();
        actionBar();
        eventUpdate();
        eventPickDate();
        eventPosition();

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
        adapter = new PositionPickAdapter(PersonalInfoActivity.this, arrayList, PersonalInfoActivity.this);
        recyclerView.setAdapter(adapter);
        bottomSheetPosition.setContentView(view);
        bottomSheetPosition.show();

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
            editphone.setText(MainActivity.user.getPhone() + "");
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
                                        Intent intent = new Intent();
                                        setResult(234);
                                        finish();
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
