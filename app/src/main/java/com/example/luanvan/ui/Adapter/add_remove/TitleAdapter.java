package com.example.luanvan.ui.Adapter.add_remove;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.luanvan.R;
import com.example.luanvan.ui.Model.Title;
import com.example.luanvan.ui.cv.CVActivity;
import com.example.luanvan.ui.cv_content.CVExperienceActivity;
import com.example.luanvan.ui.cv_content.CVGoalActivity;
import com.example.luanvan.ui.cv_content.CVInfoActivity;
import com.example.luanvan.ui.cv_content.CVSkillActivity;
import com.example.luanvan.ui.cv_content.CVStudyActivity;

import java.util.ArrayList;

public class TitleAdapter extends BaseAdapter {
    Context context;
    ArrayList<Title> arrayList;
    Activity activity;
    int kind;
    int REQUEST_CODE0 = 100, REQUEST_CODE1 = 101, REQUEST_CODE2 = 102,REQUEST_CODE3 = 103,REQUEST_CODE4 = 104,REQUEST_CODE5 = 105;

    public TitleAdapter(Context context, ArrayList<Title> arrayList, Activity activity, int kind) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
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
        public LinearLayout layout;

    }
    // hàm kiểm tra khi remove ra thì các biến check sẽ chuyển thành 1
    public void check(int id){
        switch (id){
            case 1:
                CVActivity.checkGoal = 1;
                CVActivity.x0 = 0;
                break;
            case 2:
                CVActivity.checkStudy = 1;
                CVActivity.x1 = 0;
                break;
            case 3:
                CVActivity.checkExperience = 1;
                CVActivity.x2 = 0;
                break;
            case 4:
                CVActivity.checkSkill = 1;
                CVActivity.x3 = 0;
                break;

        }
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
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.linear);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Title title = arrayList.get(position);
        viewHolder.txtname.setText(title.getName());
        if(kind == 1){
            viewHolder.txtRemove.setVisibility(View.GONE);
        }else {
            viewHolder.txtRemove.setVisibility(View.VISIBLE);
        }
        if(arrayList.get(position).getId() == 0){
            viewHolder.txtRemove.setVisibility(View.INVISIBLE);
        }

        viewHolder.txtRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CVActivity.arrayListAdd.add(arrayList.get(position));
                check(arrayList.get(position).getId());
                CVActivity.addAdapter.notifyDataSetChanged();
                arrayList.remove(position);
                notifyDataSetChanged();

            }
        });
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (arrayList.get(position).getId()){
                    case 0:
                        Intent intent = new Intent(context, CVInfoActivity.class);
                        activity.startActivityForResult(intent, REQUEST_CODE0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(context, CVGoalActivity.class);
                        activity.startActivityForResult(intent1, REQUEST_CODE1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(context, CVStudyActivity.class);
                        activity.startActivityForResult(intent2, REQUEST_CODE2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(context, CVExperienceActivity.class);
                        activity.startActivityForResult(intent3, REQUEST_CODE3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(context, CVSkillActivity.class);
                        activity.startActivityForResult(intent4, REQUEST_CODE4);
                        break;

                }
            }
        });
        return convertView;
    }

}
