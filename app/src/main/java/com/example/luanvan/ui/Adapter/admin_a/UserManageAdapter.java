package com.example.luanvan.ui.Adapter.admin_a;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.luanvan.ui.Model.Job;
import com.example.luanvan.ui.Model.User;
import com.example.luanvan.ui.Search_Filter.SearchActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserManageAdapter extends RecyclerView.Adapter<UserManageAdapter.ItemHolder> {
    Context context;
    ArrayList<User> arrayList;
    Activity activity;

    public UserManageAdapter(Context context, ArrayList<User> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_quanly_candidate, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        if(arrayList.size() > 0){
            User user = arrayList.get(position);
            holder.txtPhone.setText(user.getPhone() + "");
            holder.txtName.setText(user.getName());
            holder.txtEmail.setText(user.getEmail());
            holder.txtAddress.setText(user.getAddress());
            holder.txtintroduce.setText(user.getIntroduction());
            if(arrayList.get(position).getStatus() == 1){
                holder.btnBlock.setText("Mở khóa");
            }else {
                holder.btnBlock.setText("Khóa");
            }
            holder.btnBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(arrayList.get(position).getStatus() == 0){
                        updateStatus(position, 1);
                    }else {
                        updateStatus(position, 0);
                    }

                }
            });
        }

    }
    public void updateStatus(final int position, final int status){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlUpdateUserStatus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            FancyToast.makeText(context, "Cập nhật thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            arrayList.get(position).setStatus(status);
                            notifyDataSetChanged();
                        }else {
                            FancyToast.makeText(context,"Cập nhật thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        FancyToast.makeText(context, error.toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("iduser", String.valueOf(arrayList.get(position).getId()));
                map.put("status", String.valueOf(status));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        TextView txtName, txtEmail, txtPhone, txtAddress, txtintroduce;
        Button btnBlock;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtEmail = (TextView) itemView.findViewById(R.id.txtemail);
            txtName = (TextView) itemView.findViewById(R.id.txtname);
            txtPhone = (TextView) itemView.findViewById(R.id.txtphone);
            btnBlock = (Button) itemView.findViewById(R.id.buttonkhoa);
            txtAddress = (TextView) itemView.findViewById(R.id.txtaddress);
            txtintroduce = (TextView) itemView.findViewById(R.id.txtintroduce);



        }
    }


}
