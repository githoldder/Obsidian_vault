

public class ValidatorTest {
    public static void main(String[] args) {
        System.out.println("Test 1 (Empty): " + Validator.validate("", "张三", "20", "13800138000")); // 预期: 所有项目不能为空
        System.out.println("Test 2 (ID err): " + Validator.validate("123", "张三", "20", "13800138000")); // 预期: 学号必须是8位数字
        System.out.println("Test 3 (Phone err): " + Validator.validate("12345678", "张三", "20", "123")); // 预期: 手机号必须是11位数字
        System.out.println("Test 4 (Age err): " + Validator.validate("12345678", "张三", "10", "13800138000")); // 预期: 年龄必须在16~30之间
        System.out.println("Test 5 (Pass): " + Validator.validate("12345678", "张三", "20", "13800138000")); // 预期: PASS
    }
}
