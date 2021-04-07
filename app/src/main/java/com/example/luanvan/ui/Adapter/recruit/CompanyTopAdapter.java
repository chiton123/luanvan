package com.example.luanvan.ui.Adapter.recruit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Company;
import com.example.luanvan.ui.company.CompanyActivity;

import java.util.ArrayList;

public class CompanyTopAdapter extends BaseAdapter {
    Context context;
    ArrayList<Company> arrayList;
    Activity activity;

    public CompanyTopAdapter(Context context, ArrayList<Company> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class ViewHolder{
        TextView txtCompany, txtNumberJob;
        ImageView img, imgBackground;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_cong_ty_2, null);
            viewHolder.imgBackground = (ImageView) convertView.findViewById(R.id.img_background);
            viewHolder.txtCompany = (TextView) convertView.findViewById(R.id.txtcompany);
            viewHolder.txtNumberJob = (TextView) convertView.findViewById(R.id.txtnumberjob);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(arrayList.size() > 0){
            final Company company = arrayList.get(position);
            Glide.with(context).load(company.getImage_backgroud()).into(viewHolder.imgBackground);
            viewHolder.txtCompany.setText(company.getName());
            Glide.with(context).load(company.getImage()).into(viewHolder.img);
            viewHolder.txtNumberJob.setText(company.getNumber_job() + " tin tuyển dụng");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CompanyActivity.class);
                    intent.putExtra("company", company);
                    activity.startActivity(intent);
                }
            });
        }




        return convertView;
    }
}
