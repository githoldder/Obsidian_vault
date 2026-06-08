package com.example.studentapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class StudentListAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> data;

    public StudentListAdapter(Context context, List<Map<String, String>> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
            holder = new ViewHolder();
            holder.tvId = convertView.findViewById(R.id.tvId);
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvClazz = convertView.findViewById(R.id.tvClazz);
            holder.tvPhone = convertView.findViewById(R.id.tvPhone);
            holder.btnDetail = convertView.findViewById(R.id.btnDetail);
            holder.ivAvatar = convertView.findViewById(R.id.ivAvatar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Map<String, String> map = data.get(position);
        holder.tvId.setText(map.get("id"));
        holder.tvName.setText(map.get("name"));
        // Make sure clazz exists
        holder.tvClazz.setText(map.containsKey("clazz") ? map.get("clazz") : "");
        holder.tvPhone.setText(map.get("phone"));

        // Avatar action
        if (map.containsKey("avatar") && map.get("avatar") != null && !map.get("avatar").isEmpty()) {
            try {
                holder.ivAvatar.setImageResource(Integer.parseInt(map.get("avatar")));
            } catch (Exception e) {
                holder.ivAvatar.setBackgroundColor(0xFFDDDDDD);
            }
        } else {
            holder.ivAvatar.setBackgroundColor(0xFFDDDDDD);
        }

        holder.btnDetail.setOnClickListener(v -> {
            Toast.makeText(context, "你点的是" + map.get("name") + "学生", Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }

    class ViewHolder {
        ImageView ivAvatar;
        TextView tvId, tvName, tvClazz, tvPhone;
        Button btnDetail;
    }
}
