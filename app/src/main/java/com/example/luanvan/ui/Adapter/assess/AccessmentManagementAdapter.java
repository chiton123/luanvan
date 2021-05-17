package com.example.luanvan.ui.Adapter.assess;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
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
import com.example.luanvan.ui.Model.Assessment;
import com.example.luanvan.ui.recruiter.AssessmentManagementActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

public class AccessmentManagementAdapter extends RecyclerView.Adapter<AccessmentManagementAdapter.ItemHolder> {
    Context context;
    ArrayList<Assessment> arrayList;
    Activity activity;
    DecimalFormat decimalFormat = new DecimalFormat("#.#");

    public AccessmentManagementAdapter(Context context, ArrayList<Assessment> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dong_quanlydanhgia, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        if(arrayList.size() > 0){
            Assessment assessment = arrayList.get(position);
            holder.txtCandidate.setText(assessment.getUsername());
            holder.txtRemark.setText(assessment.getRemark());
            holder.ratingBar.setRating(assessment.getStar());
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });

        }


    }
    public void deleteItem(final int position){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.urlDeleteAssessment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            FancyToast.makeText(context, "Xóa thành công", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            AssessmentManagementActivity.total -= arrayList.get(position).getStar();
                            arrayList.remove(position);
                            notifyDataSetChanged();
                            if(arrayList.size() > 0){
                                AssessmentManagementActivity.average = Float.valueOf(decimalFormat.format(AssessmentManagementActivity.total/arrayList.size()));
                                AssessmentManagementActivity.txtRating.setText(AssessmentManagementActivity.average +"");
                                AssessmentManagementActivity.overallRating.setRating(AssessmentManagementActivity.average);
                            }
                        }else {
                            FancyToast.makeText(context, "Xóa thất bại", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
                HashMap<String,String> map = new HashMap<>();
                map.put("id", String.valueOf(arrayList.get(position).getId()));
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
        TextView txtCandidate, txtRemark;
        RatingBar ratingBar;
        Button btnDelete;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtCandidate = (TextView) itemView.findViewById(R.id.txtcandidate);
            txtRemark = (TextView) itemView.findViewById(R.id.txtremark);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating);
            btnDelete = (Button) itemView.findViewById(R.id.buttondelete);


        }
    }

}
