package com.example.luanvan.ui.Adapter.cv_all;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.luanvan.R;
import com.example.luanvan.ui.modelCV.PdfCV;

import java.util.ArrayList;

public class CVChooseAdapter extends BaseAdapter implements Checkable {
    Context context;
    ArrayList<PdfCV> arrayList;
    Activity activity;
    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;


    public CVChooseAdapter(Context context, ArrayList<PdfCV> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }
    // if called with the same position multiple lines it works as toggle
    public void setSelection(int position) {
        if (selectedPos == position) {
            selectedPos = NOT_SELECTED;
        } else {
            selectedPos = position;
        }
        notifyDataSetChanged();
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

    @Override
    public void setChecked(boolean checked) {

    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public void toggle() {

    }

    public class ViewHolder{
        public TextView txtName;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_cv_ungtuyen, null);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.namecv);


            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PdfCV pdfCV = arrayList.get(position);
        viewHolder.txtName.setText(pdfCV.getName());

        if (position == selectedPos) {
            // your color for selected item
            convertView.setBackgroundColor(Color.parseColor("#87A7FF"));
        } else {
            // your color for non-selected item
            convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }


        return convertView;
    }

}
