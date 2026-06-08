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
    private ListView studentListView;
    private Button addButton;
    private Button exitButton;
    private StudentListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        loadInitialData();
        bindEvents();
    }

    private void initViews() {
        studentListView = findViewById(R.id.listView);
        addButton = findViewById(R.id.btnAdd);
        exitButton = findViewById(R.id.btnExit);
        listAdapter = new StudentListAdapter(this, Student.getList());
        studentListView.setAdapter(listAdapter);
    }

    private void loadInitialData() {
        if (!Student.getList().isEmpty()) {
            return;
        }

        HashMap<String, String> firstStudent = new HashMap<>();
        firstStudent.put("id", "23030101");
        firstStudent.put("name", "王玲玲");
        firstStudent.put("gender", "女");
        firstStudent.put("age", "20");
        firstStudent.put("clazz", "23计一");
        firstStudent.put("phone", "18994230123");
        firstStudent.put("address", "江苏省兴化市");
        firstStudent.put("avatar", String.valueOf(R.drawable.avatar_1));
        Student.add(firstStudent);

        HashMap<String, String> secondStudent = new HashMap<>();
        secondStudent.put("id", "2023030202");
        secondStudent.put("name", "戴昊");
        secondStudent.put("gender", "男");
        secondStudent.put("age", "21");
        secondStudent.put("clazz", "23计二");
        secondStudent.put("phone", "13401358762");
        secondStudent.put("address", "江苏省南京市");
        secondStudent.put("avatar", String.valueOf(R.drawable.avatar_2));
        Student.add(secondStudent);

        HashMap<String, String> thirdStudent = new HashMap<>();
        thirdStudent.put("id", "2023030203");
        thirdStudent.put("name", "张伟");
        thirdStudent.put("gender", "男");
        thirdStudent.put("age", "19");
        thirdStudent.put("clazz", "23计一");
        thirdStudent.put("phone", "13800138000");
        thirdStudent.put("address", "浙江省杭州市");
        thirdStudent.put("avatar", String.valueOf(R.drawable.avatar_3));
        Student.add(thirdStudent);
    }

    private void bindEvents() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(MainActivity.this, AddStudentActivity.class);
                startActivity(addIntent);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }
}
