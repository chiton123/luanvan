package com.example.luanvan.ui.Adapter.add_remove;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Title;
import com.example.luanvan.ui.cv.CVActivity;

import java.util.ArrayList;

public class TitleAdapter extends BaseAdapter {
    Context context;
    ArrayList<Title> arrayList;
    int kind;

    public TitleAdapter(Context context, ArrayList<Title> arrayList, int kind) {
        this.context = context;
        this.arrayList = arrayList;
        this.kind = kind;
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
        public TextView txtname;
        public TextView txtRemove;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_remove, null);
            viewHolder.txtname = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtRemove = (TextView) convertView.findViewById(R.id.remove);
            if(kind == 1){
                viewHolder.txtRemove.setVisibility(View.GONE);
            }
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Title title = arrayList.get(position);
        viewHolder.txtname.setText(title.getName());
        viewHolder.txtRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CVActivity.arrayListAdd.add(arrayList.get(position));
                CVActivity.addAdapter.notifyDataSetChanged();
                CVActivity.arrayListRemove.remove(position);
                notifyDataSetChanged();

            }
        });
        return convertView;
    }
}
