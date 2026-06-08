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
    private final Context mContext;
    private final List<Map<String, String>> mEntries;
    private final LayoutInflater mInflater;

    public StudentListAdapter(Context ctx, List<Map<String, String>> entries) {
        this.mContext = ctx;
        this.mEntries = entries;
        this.mInflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return mEntries.size();
    }

    @Override
    public Object getItem(int pos) {
        return mEntries.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View recycledView, ViewGroup container) {
        ItemViewHolder vh;
        if (recycledView == null) {
            recycledView = mInflater.inflate(R.layout.item_student, container, false);
            vh = new ItemViewHolder();
            vh.avatarImage = recycledView.findViewById(R.id.ivAvatar);
            vh.idLabel = recycledView.findViewById(R.id.tvId);
            vh.nameLabel = recycledView.findViewById(R.id.tvName);
            vh.classLabel = recycledView.findViewById(R.id.tvClazz);
            vh.phoneLabel = recycledView.findViewById(R.id.tvPhone);
            vh.detailBtn = recycledView.findViewById(R.id.btnDetail);
            recycledView.setTag(vh);
        } else {
            vh = (ItemViewHolder) recycledView.getTag();
        }

        final Map<String, String> record = mEntries.get(pos);

        vh.idLabel.setText(record.get("id"));
        vh.nameLabel.setText(record.get("name"));

        String classValue = record.get("clazz");
        vh.classLabel.setText(classValue != null ? classValue : "");

        vh.phoneLabel.setText(record.get("phone"));

        loadAvatar(vh.avatarImage, record.get("avatar"));

        vh.detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "你点的是" + record.get("name") + "学生", Toast.LENGTH_SHORT).show();
            }
        });

        return recycledView;
    }

    private void loadAvatar(ImageView target, String resIdStr) {
        if (resIdStr == null || resIdStr.isEmpty()) {
            target.setBackgroundColor(0xFFDDDDDD);
            return;
        }
        try {
            int resId = Integer.parseInt(resIdStr);
            target.setImageResource(resId);
        } catch (NumberFormatException e) {
            target.setBackgroundColor(0xFFDDDDDD);
        }
    }

    static class ItemViewHolder {
        ImageView avatarImage;
        TextView idLabel;
        TextView nameLabel;
        TextView classLabel;
        TextView phoneLabel;
        Button detailBtn;
    }
}
