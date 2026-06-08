

import java.util.regex.Pattern;

public class Validator {
    public static String validate(String id, String name, String ageStr, String phone) {
        if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty() ||
            ageStr == null || ageStr.trim().isEmpty() || phone == null || phone.trim().isEmpty()) {
            return "所有项目不能为空";
        }
        if (id.length() != 8 || !Pattern.matches("\\d+", id)) {
            return "学号必须是8位数字";
        }
        if (phone.length() != 11 || !Pattern.matches("\\d+", phone)) {
            return "手机号必须是11位数字";
        }
        try {
            int age = Integer.parseInt(ageStr);
            if (age < 16 || age > 30) {
                return "年龄必须在16~30之间";
            }
        } catch (NumberFormatException e) {
            return "年龄必须是整数";
        }
        return "PASS";
    }
}
