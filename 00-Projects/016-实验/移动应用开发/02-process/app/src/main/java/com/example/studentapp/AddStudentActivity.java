package com.example.studentapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class AddStudentActivity extends AppCompatActivity {
    private EditText etId, etName, etAge, etClazz, etPhone, etAddress;
    private RadioGroup rgGender;
    private CheckBox cbBasketball, cbVolleyball, cbBadminton, cbTableTennis, cbSwimming, cbReading, cbWriting;
    private Button btnConfirm, btnCancel, btnSelectImage;
    private ImageView ivAddAvatar;
    private int[] avatarResIds = {R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3};
    private String selectedAvatarId = String.valueOf(R.drawable.avatar_1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        etId = findViewById(R.id.etId);
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etClazz = findViewById(R.id.etClazz);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);

        rgGender = findViewById(R.id.rgGender);

        cbBasketball = findViewById(R.id.cbBasketball);
        cbVolleyball = findViewById(R.id.cbVolleyball);
        cbBadminton = findViewById(R.id.cbBadminton);
        cbTableTennis = findViewById(R.id.cbTableTennis);
        cbSwimming = findViewById(R.id.cbSwimming);
        cbReading = findViewById(R.id.cbReading);
        cbWriting = findViewById(R.id.cbWriting);

        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        ivAddAvatar = findViewById(R.id.ivAddAvatar);

        ivAddAvatar.setImageResource(R.drawable.avatar_1);

        btnConfirm.setOnClickListener(v -> {
            if (validateAndSave()) {
                finish();
            }
        });

        btnCancel.setOnClickListener(v -> finish());

        btnSelectImage.setOnClickListener(v -> {
            String[] items = {"头像1", "头像2", "头像3", "取消"};
            new AlertDialog.Builder(this)
                .setTitle("图片选择")
                .setItems(items, (dialog, which) -> {
                    if (which < 3) {
                        selectedAvatarId = String.valueOf(avatarResIds[which]);
                        ivAddAvatar.setImageResource(avatarResIds[which]);
                    } else {
                        dialog.dismiss();
                    }
                })
                .show();
        });
    }

    private boolean validateAndSave() {
        String idStr = etId.getText().toString().trim();
        String nameStr = etName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String clazzStr = etClazz.getText().toString().trim();
        String phoneStr = etPhone.getText().toString().trim();
        String addressStr = etAddress.getText().toString().trim();

        if (TextUtils.isEmpty(idStr) || TextUtils.isEmpty(nameStr) || TextUtils.isEmpty(ageStr) ||
            TextUtils.isEmpty(clazzStr) || TextUtils.isEmpty(phoneStr) || TextUtils.isEmpty(addressStr)) {
            Toast.makeText(this, "所有项目不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (idStr.length() != 8 || !idStr.matches("\\d+")) {
            Toast.makeText(this, "学号必须是8位数字", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (Map<String, String> student : Student.getList()) {
            if (idStr.equals(student.get("id"))) {
                Toast.makeText(this, "学号不能重复", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (phoneStr.length() != 11 || !phoneStr.matches("\\d+")) {
            Toast.makeText(this, "手机号必须是11位数字", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            int age = Integer.parseInt(ageStr);
            if (age < 16 || age > 30) {
                Toast.makeText(this, "年龄必须在16~30之间", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "年龄必须是整数", Toast.LENGTH_SHORT).show();
            return false;
        }

        int checkedId = rgGender.getCheckedRadioButtonId();
        RadioButton rb = findViewById(checkedId);
        String genderStr = rb != null ? rb.getText().toString() : "男";

        StringBuilder hobbies = new StringBuilder();
        if (cbBasketball.isChecked()) hobbies.append("篮球 ");
        if (cbVolleyball.isChecked()) hobbies.append("排球 ");
        if (cbBadminton.isChecked()) hobbies.append("羽毛球 ");
        if (cbTableTennis.isChecked()) hobbies.append("乒乓球 ");
        if (cbSwimming.isChecked()) hobbies.append("游泳 ");
        if (cbReading.isChecked()) hobbies.append("阅读 ");
        if (cbWriting.isChecked()) hobbies.append("写作 ");

        Map<String, String> map = new HashMap<>();
        map.put("id", idStr);
        map.put("name", nameStr);
        map.put("age", ageStr);
        map.put("clazz", clazzStr);
        map.put("phone", phoneStr);
        map.put("gender", genderStr);
        map.put("hobbies", hobbies.toString().trim());
        map.put("address", addressStr);
        map.put("avatar", selectedAvatarId);

        Student.add(map);
        return true;
    }
}
