package com.example.luanvan.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    EditText editEmail, editPass;
    Button btnDangnhap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        editEmail = (EditText) view.findViewById(R.id.editemail);
        editPass = (EditText) view.findViewById(R.id.editpass);
        btnDangnhap = (Button) view.findViewById(R.id.buttondangnhap);

        eventLogin();


        return view;
    }

    private void eventLogin() {
        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editPass.getText().equals("") || editEmail.getText().equals("")){
                    Toast.makeText(getActivity(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    final String email = editEmail.getText().toString();
                    final String pass = editPass.getText().toString();
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urllogin,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("success")){
                                        Toast.makeText(getActivity(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        editEmail.setText("");
                                        editPass.setText("");
                                        MainActivity.login = 1;
                                        Intent intent = new Intent();
                                        getActivity().setResult(123);
                                        getActivity().finish();
                                    }else {
                                        Toast.makeText(getActivity(), "Sai tên hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<>();
                            map.put("pass", pass);
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
