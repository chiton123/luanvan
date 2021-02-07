package com.example.luanvan.ui.Adapter.update_personal_info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.GeneralObject;

import java.util.ArrayList;
import java.util.List;

public class SpinnerNewAdapter extends ArrayAdapter<GeneralObject> {
    ArrayList<GeneralObject> arrayList;
    LayoutInflater layoutInflater;
    public SpinnerNewAdapter(@NonNull Context context, int resource, @NonNull List<GeneralObject> objects) {
        super(context, resource, objects);
        layoutInflater = LayoutInflater.from(context);
        this.arrayList = (ArrayList<GeneralObject>) objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = null;
        TextView txtname = null;
        if(convertView == null){
            rowView = layoutInflater.inflate(R.layout.dong_spinner, null, true);
            txtname = rowView.findViewById(R.id.name);
        }

        GeneralObject object = getItem(position);
        
        if(object != null){
            txtname.setText(object.getName());
        }



        return rowView;
    }
    public GeneralObject getObject(int position){
        return arrayList.get(position);
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.dong_spinner, parent, false);
        }
        GeneralObject object = getItem(position);
        TextView txtname = convertView.findViewById(R.id.name);
        txtname.setText(object.getName());
        return convertView;
    }


}
