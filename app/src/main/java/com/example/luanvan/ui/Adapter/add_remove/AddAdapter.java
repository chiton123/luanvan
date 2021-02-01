package com.example.luanvan.ui.Adapter.add_remove;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.luanvan.MainActivity;
import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Title;
import com.example.luanvan.ui.cv.CVActivity;

import java.util.ArrayList;

public class AddAdapter extends BaseAdapter {
    Context context;
    ArrayList<Title> arrayList;

    public AddAdapter(Context context, ArrayList<Title> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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
        public TextView txtAdd;

    }
    // hàm kiểm tra khi remove ra thì các biến check sẽ chuyển thành 0
    public void check(int id){
        switch (id){
            case 1:
                CVActivity.checkGoal = 0;
                break;
            case 2:
                CVActivity.checkStudy = 0;
                break;
            case 3:
                CVActivity.checkExperience = 0;
                break;
            case 4:
                CVActivity.checkSkill = 0;
                break;

        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_add, null);
            viewHolder.txtname = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtAdd = (TextView) convertView.findViewById(R.id.add);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Title title = arrayList.get(position);
        viewHolder.txtname.setText(title.getName());
        viewHolder.txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CVActivity.arrayListRemove.add(arrayList.get(position));
                check(arrayList.get(position).getId());
                CVActivity.titleAdapterRemove.notifyDataSetChanged();
                arrayList.remove(arrayList.get(position));
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

}
