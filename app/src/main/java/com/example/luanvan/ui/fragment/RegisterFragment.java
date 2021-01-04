package com.example.luanvan.ui.fragment;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {
    EditText editEmail, editPass, editPass2, editName;
    Button btnDangky;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        editEmail = (EditText) view.findViewById(R.id.editemail);
        editPass = (EditText) view.findViewById(R.id.editpass);
        btnDangky = (Button) view.findViewById(R.id.buttondangky);
        editName = (EditText)view.findViewById(R.id.editname);
        editPass2 = (EditText) view.findViewById(R.id.editpass2);
        eventRegister();

        return view;
    }
    private void eventRegister() {
        btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editEmail.getText().equals("") || editName.getText().equals("") || editPass.getText().equals("") || editPass2.getText().equals("")){
                    Toast.makeText(getActivity(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else if(editPass.getText().length() < 8 || editPass2.getText().length() < 8){
                    Toast.makeText(getActivity(), "Mật khẩu ít nhất 8 ký tự", Toast.LENGTH_SHORT).show();
                }else if(!isEmailValid(editEmail.getText().toString())){
                    Toast.makeText(getActivity(), "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
                }else if (!editPass2.getText().toString().equals(editPass.getText().toString())){
                    Toast.makeText(getActivity(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                }else {
                    final String name = editName.getText().toString();
                    final String email = editEmail.getText().toString();
                    final String pass = editPass.getText().toString();
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlRegister,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equals("success")){
                                        Toast.makeText(getActivity(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                        editEmail.setText("");
                                        editName.setText("");
                                        editPass.setText("");
                                        editPass2.setText("");
                                        ((LoginActivity) getActivity()).Switch1();
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
                            map.put("name", name);
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
