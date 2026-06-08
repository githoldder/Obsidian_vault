package com.example.studentapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
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
    private EditText inputStudentId, inputStudentName, inputStudentAge;
    private EditText inputStudentClass, inputStudentPhone, inputStudentAddress;
    private RadioGroup genderRadioGroup;
    private CheckBox chkBasketball, chkVolleyball, chkBadminton;
    private CheckBox chkTableTennis, chkSwimming, chkReading, chkWriting;
    private Button confirmButton, cancelButton, imagePickerButton;
    private ImageView avatarPreview;

    private final int[] portraitResources = {
        R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3
    };
    private String chosenAvatarRes = String.valueOf(R.drawable.avatar_1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        setupInputFields();
        setupCheckboxes();
        setupButtons();

        avatarPreview.setImageResource(R.drawable.avatar_1);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (performValidationAndStore()) {
                    finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imagePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvatarPicker();
            }
        });
    }

    private void setupInputFields() {
        inputStudentId = findViewById(R.id.etId);
        inputStudentName = findViewById(R.id.etName);
        inputStudentAge = findViewById(R.id.etAge);
        inputStudentClass = findViewById(R.id.etClazz);
        inputStudentPhone = findViewById(R.id.etPhone);
        inputStudentAddress = findViewById(R.id.etAddress);
        genderRadioGroup = findViewById(R.id.rgGender);
    }

    private void setupCheckboxes() {
        chkBasketball = findViewById(R.id.cbBasketball);
        chkVolleyball = findViewById(R.id.cbVolleyball);
        chkBadminton = findViewById(R.id.cbBadminton);
        chkTableTennis = findViewById(R.id.cbTableTennis);
        chkSwimming = findViewById(R.id.cbSwimming);
        chkReading = findViewById(R.id.cbReading);
        chkWriting = findViewById(R.id.cbWriting);
    }

    private void setupButtons() {
        confirmButton = findViewById(R.id.btnConfirm);
        cancelButton = findViewById(R.id.btnCancel);
        imagePickerButton = findViewById(R.id.btnSelectImage);
        avatarPreview = findViewById(R.id.ivAddAvatar);
    }

    private void showAvatarPicker() {
        final String[] options = {"头像1", "头像2", "头像3", "取消"};
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("图片选择");
        dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                if (index >= 0 && index <= 2) {
                    chosenAvatarRes = String.valueOf(portraitResources[index]);
                    avatarPreview.setImageResource(portraitResources[index]);
                } else {
                    dialogInterface.dismiss();
                }
            }
        });
        dialogBuilder.show();
    }

    private void displayError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean performValidationAndStore() {
        String stuId = inputStudentId.getText().toString().trim();
        String stuName = inputStudentName.getText().toString().trim();
        String stuAge = inputStudentAge.getText().toString().trim();
        String stuClass = inputStudentClass.getText().toString().trim();
        String stuPhone = inputStudentPhone.getText().toString().trim();
        String stuAddr = inputStudentAddress.getText().toString().trim();

        if (stuId.length() == 0 || stuName.length() == 0 || stuAge.length() == 0 ||
            stuClass.length() == 0 || stuPhone.length() == 0 || stuAddr.length() == 0) {
            displayError("所有项目不能为空");
            return false;
        }

        if (stuId.length() != 8) {
            displayError("学号必须是8位数字");
            return false;
        }
        for (int i = 0; i < stuId.length(); i++) {
            if (!Character.isDigit(stuId.charAt(i))) {
                displayError("学号必须是8位数字");
                return false;
            }
        }

        for (Map<String, String> existing : Student.getList()) {
            String existingId = existing.get("id");
            if (existingId != null && existingId.equals(stuId)) {
                displayError("学号不能重复");
                return false;
            }
        }

        if (stuPhone.length() != 11) {
            displayError("手机号必须是11位数字");
            return false;
        }
        for (int i = 0; i < stuPhone.length(); i++) {
            if (!Character.isDigit(stuPhone.charAt(i))) {
                displayError("手机号必须是11位数字");
                return false;
            }
        }

        int ageValue;
        try {
            ageValue = Integer.parseInt(stuAge);
        } catch (NumberFormatException ex) {
            displayError("年龄必须是整数");
            return false;
        }
        if (ageValue < 16 || ageValue > 30) {
            displayError("年龄必须在16~30之间");
            return false;
        }

        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        String genderText = "男";
        if (selectedGenderId != -1) {
            RadioButton selectedRadio = findViewById(selectedGenderId);
            if (selectedRadio != null) {
                genderText = selectedRadio.getText().toString();
            }
        }

        StringBuilder hobbyBuilder = new StringBuilder();
        appendHobby(hobbyBuilder, chkBasketball, "篮球");
        appendHobby(hobbyBuilder, chkVolleyball, "排球");
        appendHobby(hobbyBuilder, chkBadminton, "羽毛球");
        appendHobby(hobbyBuilder, chkTableTennis, "乒乓球");
        appendHobby(hobbyBuilder, chkSwimming, "游泳");
        appendHobby(hobbyBuilder, chkReading, "阅读");
        appendHobby(hobbyBuilder, chkWriting, "写作");

        HashMap<String, String> studentData = new HashMap<>();
        studentData.put("id", stuId);
        studentData.put("name", stuName);
        studentData.put("age", stuAge);
        studentData.put("clazz", stuClass);
        studentData.put("phone", stuPhone);
        studentData.put("gender", genderText);
        studentData.put("hobbies", hobbyBuilder.toString().trim());
        studentData.put("address", stuAddr);
        studentData.put("avatar", chosenAvatarRes);

        Student.add(studentData);
        return true;
    }

    private void appendHobby(StringBuilder sb, CheckBox box, String label) {
        if (box.isChecked()) {
            sb.append(label).append(" ");
        }
    }
}
