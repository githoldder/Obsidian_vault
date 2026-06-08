package com.example.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Button btnAdd, btnExit;
    private StudentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        btnAdd = findViewById(R.id.btnAdd);
        btnExit = findViewById(R.id.btnExit);

        // 初始化模拟数据 (满足至少三行演示数据，第一行自己)
        if (Student.getList().isEmpty()) {
            Map<String, String> self = new HashMap<>();
            self.put("id", "23030101");
            self.put("name", "王玲玲");
            self.put("gender", "女");
            self.put("age", "20");
            self.put("clazz", "23计一");
            self.put("phone", "18994230123");
            self.put("address", "江苏省兴化市");
            self.put("avatar", String.valueOf(R.drawable.avatar_1));
            Student.add(self);

            Map<String, String> peer1 = new HashMap<>();
            peer1.put("id", "2023030202");
            peer1.put("name", "戴昊");
            peer1.put("gender", "男");
            peer1.put("age", "21");
            peer1.put("clazz", "23计二");
            peer1.put("phone", "13401358762");
            peer1.put("address", "江苏省南京市");
            peer1.put("avatar", String.valueOf(R.drawable.avatar_2));
            Student.add(peer1);

            Map<String, String> peer2 = new HashMap<>();
            peer2.put("id", "2023030203");
            peer2.put("name", "张伟");
            peer2.put("gender", "男");
            peer2.put("age", "19");
            peer2.put("clazz", "23计一");
            peer2.put("phone", "13800138000");
            peer2.put("address", "浙江省杭州市");
            peer2.put("avatar", String.valueOf(R.drawable.avatar_3));
            Student.add(peer2);
        }

        adapter = new StudentListAdapter(this, Student.getList());
        listView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
            startActivity(intent);
        });

        btnExit.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
