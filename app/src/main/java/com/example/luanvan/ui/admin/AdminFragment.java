package com.example.luanvan.ui.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import com.example.luanvan.ui.dashboard.DashboardViewModel;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class AdminFragment extends Fragment {
    private AdminViewModel adminViewModel;
    EditText editusername, editpass;
    Button btnlogin;
    int REQUEST_CODE = 123;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        adminViewModel =
                ViewModelProviders.of(this).get(AdminViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin, container, false);
        editusername = (EditText) root.findViewById(R.id.username);
        editpass = (EditText) root.findViewById(R.id.password);
        btnlogin = (Button) root.findViewById(R.id.login);
        eventLogin();

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            editpass.setText("");
            editusername.setText("");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void eventLogin() {
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = editusername.getText().toString();
                final String password = editpass.getText().toString();
                if(username.equals("") || password.equals("")){
                    FancyToast.makeText(getActivity(), "Vui lòng nhập đủ thông tin", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else {
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlLoginAdmin,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("success")){
                                        FancyToast.makeText(getActivity(), "Đăng nhập thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                        MainActivity.login_admin = 1;
                                        Intent intent = new Intent(getActivity(), AdminActivity.class);
                                        startActivityForResult(intent, REQUEST_CODE);
                                    }else {
                                        FancyToast.makeText(getActivity(),"Sai tên hoặc mật khẩu", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
                            map.put("username", username);
                            map.put("password", password);
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);
                }


            }
        });

    }
}
