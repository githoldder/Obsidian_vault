package com.example.studentapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Student {
    private static final List<Map<String, String>> records = new ArrayList<>();

    public static List<Map<String, String>> getList() {
        return records;
    }

    public static void setList(List<Map<String, String>> newRecords) {
        records.clear();
        records.addAll(newRecords);
    }

    public static void add(Map<String, String> entry) {
        records.add(entry);
    }
}
