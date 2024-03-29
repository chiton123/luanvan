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
import com.example.luanvan.ui.Model.Area;
import com.example.luanvan.ui.Model.Experience;
import com.example.luanvan.ui.Model.Profession;
import com.example.luanvan.ui.Model.Skill;
import com.example.luanvan.ui.Model.Study;
import com.example.luanvan.ui.Model.User;
import com.example.luanvan.ui.User.ResetPasswordActivity;
import com.example.luanvan.ui.login.LoginActivity;
import com.example.luanvan.ui.modelCV.PdfCV;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.shashank.sony.fancytoastlib.FancyToast;

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
    private void getCandidateProfession() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlGetCandidateProfession,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    MainActivity.arrayListChosenProfession.add(new Profession(
                                            object.getInt("id"),
                                            object.getString("name")
                                    ));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        FancyToast.makeText(getActivity(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
    private void getCandidateArea() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlGetCandidateArea,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    MainActivity.arraylistChosenArea.add(new Area(
                                            object.getInt("id"),
                                            object.getString("name")
                                    ));

                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        FancyToast.makeText(getActivity(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
                                MainActivity.user = new User(
                                        object.getInt("id"),
                                        object.getInt("idposition"),
                                        object.getString("position"),
                                        object.getString("name"),
                                        object.getString("birthday"),
                                        object.getInt("gender"),
                                        object.getString("address"),
                                        object.getString("email"),
                                        object.getString("introduction"),
                                        object.getInt("phone"),
                                        object.getInt("status"),
                                        object.getInt("mode")

                                );
                                MainActivity.username = object.getString("name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        FancyToast.makeText(getActivity(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlCandidateStudy,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                   //     Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    MainActivity.studies.add(new Study(
                                            object.getInt("id"),
                                            object.getInt("iduser"),
                                            object.getString("school"),
                                            object.getString("major"),
                                            object.getString("start"),
                                            object.getString("end"),
                                            object.getString("description")
                                    ));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        FancyToast.makeText(getActivity(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
    private void getInfoExperience() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlCandidateExperience,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                 //       Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    MainActivity.experiences.add(new Experience(
                                       object.getInt("id"),
                                       object.getInt("iduser"),
                                       object.getString("company"),
                                       object.getString("position"),
                                       object.getString("start"),
                                       object.getString("end"),
                                       object.getString("description")
                                    ));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        FancyToast.makeText(getActivity(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
    private void getInfoSkill() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlCandidateSkill,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        if(response != null){
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                  //  Toast.makeText(getActivity(), "idskill " + object.getInt("idskill"), Toast.LENGTH_SHORT).show();
                                    MainActivity.skills.add(new Skill(
                                            object.getInt("id"),
                                            object.getInt("iduser"),
                                            object.getInt("idskill"),
                                            object.getString("name"),
                                            (float)object.getDouble("star"),
                                            object.getString("description")
                                    ));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        FancyToast.makeText(getActivity(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
    private void eventLogin() {
        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = editEmail.getText().toString();
                final String pass = editPass.getText().toString();
                if(email.equals("") || pass.equals("")){
                    FancyToast.makeText(getActivity(), "Vui lòng nhập đủ thông tin", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else {
                    loading();
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urllogin,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(final String response) {
                                    if(!response.equals("fail") && !response.equals("fail2")){
                                        MainActivity.mAuth.signInWithEmailAndPassword(email, pass)
                                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if(task.isSuccessful()){
                                                            FancyToast.makeText(getActivity(), "Đăng nhập thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                                            MainActivity.password = pass;
                                                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                                            MainActivity.mAuth = FirebaseAuth.getInstance();
                                                            MainActivity.uid = firebaseUser.getUid();
                                                       //     Toast.makeText(getActivity(), MainActivity.uid, Toast.LENGTH_SHORT).show();
                                                            editEmail.setText("");
                                                            editPass.setText("");

                                                            MainActivity.login = 1;
                                                            MainActivity.iduser = Integer.parseInt(response);
                                                            getInfo();
                                                            getCandidateArea();
                                                            getCandidateProfession();
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
                                                            },5000);
                                                        }else {
                                                            FancyToast.makeText(getActivity(), "Sai tên hoặc mật khẩu", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                                                            progressDialog.dismiss();
                                                        }
                                                    }
                                                });


                                    }else if(response.equals("fail2")){
                                        FancyToast.makeText(getActivity(), "Tài khoản đã bị khóa", FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show();
                                        progressDialog.dismiss();
                                    }else {
                                        FancyToast.makeText(getActivity(), "Sai tên hoặc mật khẩu", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    FancyToast.makeText(getActivity(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
