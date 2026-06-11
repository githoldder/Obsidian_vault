package com.example.studentapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Student {
    private static List<Map<String, String>> studentList = new ArrayList<>();

    public static List<Map<String, String>> getList() {
        return studentList;
    }

    public static void setList(List<Map<String, String>> list) {
        studentList = list;
    }

    public static void add(Map<String, String> student) {
        studentList.add(student);
    }
}
