package com.example.luanvan.ui.fragment.login_f;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.luanvan.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    EditText editEmail, editPass, editPass2, editName;
    Button btnDangky;
    ProgressDialog progressDialog;
    Handler handler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        FirebaseApp.initializeApp(getActivity());

        editEmail = (EditText) view.findViewById(R.id.editemail);
        editPass = (EditText) view.findViewById(R.id.editpass);
        btnDangky = (Button) view.findViewById(R.id.buttondangky);
        editName = (EditText)view.findViewById(R.id.editname);
        editPass2 = (EditText) view.findViewById(R.id.editpass2);
        eventRegister();

        return view;
    }
    void loading(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }
    public void SignUp(final String email, String password, final String name){
        // firebase
        MainActivity.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            String username = editName.getText().toString();
                            FirebaseUser firebaseUser = MainActivity.mAuth.getCurrentUser();
                            registerMySQL(name, email, firebaseUser.getUid());
                            String userid = "";
                            if(firebaseUser != null){
                                userid = firebaseUser.getUid();
                                MainActivity.mUserData.child(userid);
                                HashMap<String,Object> map = new HashMap<>();
                                map.put("id", userid);
                                map.put("username", username);
                                map.put("imageURL", "default");
                                MainActivity.mUserData.child(userid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                         //   Toast.makeText(getActivity(), "Lưu thông tin firebase thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                FancyToast.makeText(getActivity(), "Không có thông tin user trên firebase", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            }


                            editEmail.setText("");
                            editName.setText("");
                            editPass.setText("");
                            editPass2.setText("");

                            handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    ((LoginActivity) getActivity()).Switch1();
                                }
                            },1500);

                        }else {
                            FancyToast.makeText(getActivity(), "Đăng ký thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            progressDialog.dismiss();
                        }
                    }
                });

    }
    private void eventRegister() {
        btnDangky.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                final String name = editName.getText().toString();
                final String email = editEmail.getText().toString();
                final String pass = editPass.getText().toString();
                final String pass2 = editPass2.getText().toString();
                if(email.equals("") || name.equals("") || pass.equals("") || pass2.equals("")){
                    FancyToast.makeText(getActivity(), "Vui lòng nhập đủ thông tin", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else if(pass.length() < 6 || pass2.length() < 6){
                    FancyToast.makeText(getActivity(), "Mật khẩu ít nhất 6 ký tự", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else if(!isEmailValid(email)){
                    FancyToast.makeText(getActivity(), "Email không đúng định dạng", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else if (!pass2.equals(pass)){
                    FancyToast.makeText(getActivity(), "Mật khẩu không khớp", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else {
                    loading();

                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlCheckRegister,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("fail")){
                                        SignUp(email, pass, name);
                                    }else {
                                        FancyToast.makeText(getActivity(), "Email này đã được sử dụng", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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

    public void registerMySQL(final String name, final String email, final String uid_f){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlRegister,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            FancyToast.makeText(getActivity(), "Đăng ký thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        }else {
                            FancyToast.makeText(getActivity(), "Đăng ký thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                         //   progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        FancyToast.makeText(getActivity(), error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                   //     progressDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("name", name);
                map.put("email", email);
                map.put("uid", uid_f);
                return map;
            }
        };
        requestQueue.add(stringRequest);
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


}
