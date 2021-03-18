package com.example.luanvan.ui.Adapter.admin_a;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Admin;


import java.util.ArrayList;

public class AdminAdapter extends BaseAdapter {
    Context context;
    ArrayList<Admin> admins;

    public AdminAdapter(Context context, ArrayList<Admin> admins) {
        this.context = context;
        this.admins = admins;
    }

    @Override
    public int getCount() {
        return admins.size();
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
        TextView txtname;
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.admin_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.txtname = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Admin admin = admins.get(position);
        viewHolder.txtname.setText(admin.getName());
        viewHolder.imageView.setImageResource(admin.getImage());

        return convertView;
    }
}
