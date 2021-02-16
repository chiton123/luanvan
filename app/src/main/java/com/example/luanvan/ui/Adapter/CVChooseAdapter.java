package com.example.luanvan.ui.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.luanvan.R;
import com.example.luanvan.ui.modelCV.PdfCV;

import java.util.ArrayList;

public class CVChooseAdapter extends BaseAdapter {
    Context context;
    ArrayList<PdfCV> arrayList;
    Activity activity;

    public CVChooseAdapter(Context context, ArrayList<PdfCV> arrayList, Activity activity) {
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
        public TextView txtName;
        public RadioButton radioButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_cv_ungtuyen, null);
            viewHolder.radioButton = (RadioButton) convertView.findViewById(R.id.radiobutton);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.namecv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PdfCV pdfCV = arrayList.get(position);
        viewHolder.txtName.setText(pdfCV.getName());
        if(viewHolder.radioButton.isChecked()){
            notifyDataSetChanged();
        }



        return convertView;
    }
}
