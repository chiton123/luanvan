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

public class SpinnerNewAdapter extends ArrayAdapter<GeneralObject> {
    public SpinnerNewAdapter(Context context, ArrayList<GeneralObject> arrayList){
        super(context, 0 , arrayList);

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initview(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initview(position, convertView, parent);
    }
    private View initview(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dong_spinner, null, false);
        }
        TextView txtname = convertView.findViewById(R.id.name);
        GeneralObject object = getItem(position);
        if(object != null){
            txtname.setText(object.getName());
        }


        return  convertView;
    }


}
