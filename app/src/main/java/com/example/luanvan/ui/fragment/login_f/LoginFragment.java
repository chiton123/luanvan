package com.example.luanvan.ui.fragment.login_f;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.luanvan.ui.Model.Experience;
import com.example.luanvan.ui.Model.Skill;
import com.example.luanvan.ui.Model.Study;
import com.example.luanvan.ui.Model.User;
import com.example.luanvan.ui.User.ResetPasswordActivity;
import com.example.luanvan.ui.login.LoginActivity;
import com.example.luanvan.ui.modelCV.PdfCV;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    EditText editEmail, editPass;
    Button btnDangnhap;
    ProgressDialog progressDialog;
    TextView txt_forgotPassword;
    Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        editEmail = (EditText) view.findViewById(R.id.editemail);
        editPass = (EditText) view.findViewById(R.id.editpass);
        btnDangnhap = (Button) view.findViewById(R.id.buttondangnhap);
        txt_forgotPassword = (TextView) view.findViewById(R.id.txt_forgotpassword);
        eventLogin();
        eventForgotPassword();

        return view;
    }

    private void eventForgotPassword() {
        txt_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    void loading(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    private void getDataCV() {
        final ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //  Toast.makeText(getApplicationContext(), snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                for(DataSnapshot x : snapshot.getChildren()){
                    PdfCV pdfCV = x.getValue(PdfCV.class);
                    if(pdfCV.key != null){
                        MainActivity.arrayListCV.add(pdfCV);
                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        MainActivity.mData.child("cv").child(MainActivity.uid).addChildEventListener(childEventListener);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               // MainActivity.mData.removeEventListener(childEventListener);
            }
        },3000);



    }
    public void getInfo(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urluserinfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject object = jsonArray.getJSONObject(0);
                                MainActivity.user.setAddress(object.getString("address"));
                                MainActivity.user.setBirthday(object.getString("birthday"));
                                MainActivity.user.setEmail(object.getString("email"));
                                MainActivity.user.setGender(object.getInt("gender"));
                                MainActivity.user.setStatus(object.getInt("status"));
                                MainActivity.user.setIntroduction(object.getString("introduction"));
                                MainActivity.user.setId(object.getInt("id"));
                                MainActivity.user.setName(object.getString("name"));
                                MainActivity.user.setPosition(object.getString("position"));
                                MainActivity.user.setPhone(object.getInt("phone"));
                                MainActivity.username = object.getString("name");
                                MainActivity.user.setMode(object.getInt("mode"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
    private void getInfoStudy() {
        MainActivity.mData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("study")){
                    MainActivity.mData.child("study").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot x : snapshot.getChildren()){
                                Study study = x.getValue(Study.class);
                                if(study.getUid().equals(MainActivity.uid)){
                                    MainActivity.studies.add(study);
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void getInfoExperience() {
        MainActivity.mData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("experience") && snapshot.exists()){
                    MainActivity.mData.child("experience").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot x : snapshot.getChildren()){
                                Experience experience = x.getValue(Experience.class);
                                if(experience.getUid().equals(MainActivity.uid)){
                                    MainActivity.experiences.add(experience);
                                }


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void getInfoSkill() {
        MainActivity.mData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("skill")){
                    MainActivity.mData.child("skill").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot x : snapshot.getChildren()){
                                Skill skill = x.getValue(Skill.class);
//                                Toast.makeText(getActivity(), skill.getUid(), Toast.LENGTH_SHORT).show();
                                if(skill.getUid().equals(MainActivity.uid)){
                                    MainActivity.skills.add(skill);
                                }
                                //  MainActivity.skills.add(skill);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void eventLogin() {
        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editPass.getText().equals("") || editEmail.getText().equals("")){
                    Toast.makeText(getActivity(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    loading();
                    final String email = editEmail.getText().toString();
                    final String pass = editPass.getText().toString();
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urllogin,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(final String response) {
                                    if(!response.equals("fail")){
                                        MainActivity.mAuth.signInWithEmailAndPassword(email, pass)
                                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(getActivity(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                                            MainActivity.password = pass;
                                                            MainActivity.mUser = MainActivity.mAuth.getCurrentUser();

                                                            MainActivity.uid = MainActivity.mUser.getUid();
                                                       //     Toast.makeText(getActivity(), MainActivity.uid, Toast.LENGTH_SHORT).show();
                                                            editEmail.setText("");
                                                            editPass.setText("");

                                                            MainActivity.login = 1;
                                                            MainActivity.iduser = Integer.parseInt(response);
                                                            getInfo();

                                                            getInfoStudy();
                                                            getInfoExperience();
                                                            getInfoSkill();
                                                            getDataCV();
                                                            Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    progressDialog.dismiss();
                                                                    Intent intent = new Intent();
                                                                    getActivity().setResult(123);
                                                                    getActivity().finish();
                                                                }
                                                            },4000);
                                                        }else {
                                                            Toast.makeText(getActivity(), "Sai tên hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                                                            progressDialog.dismiss();
                                                        }
                                                    }
                                                });


                                    }else {
                                        Toast.makeText(getActivity(), "Sai tên hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<>();
                            map.put("email", email);
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);

                }
            }
        });

    }
}
