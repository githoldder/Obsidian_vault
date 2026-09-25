`Scanner` 是 Java 中用于读取用户输入的工具类，位于 `java.util` 包中。它可以读取从键盘、文件或其他输入源输入的数据。以下是 `Scanner` 的详细用法和示例：

---

### **1. 基本用法**

#### **导入 Scanner 类**
```java
import java.util.Scanner;
```

#### **创建 Scanner 对象**
```java
Scanner scanner = new Scanner(System.in); // 从标准输入（键盘）读取数据
```

#### **读取不同类型的数据**
| **方法**            | **作用**                          | **示例**                              |
|---------------------|-----------------------------------|---------------------------------------|
| `next()`            | 读取一个字符串（以空格分隔）       | `String name = scanner.next();`       |
| `nextLine()`        | 读取一行字符串（包括空格）         | `String line = scanner.nextLine();`   |
| `nextInt()`         | 读取一个整数                      | `int age = scanner.nextInt();`        |
| `nextDouble()`      | 读取一个双精度浮点数              | `double price = scanner.nextDouble();`|
| `nextBoolean()`     | 读取一个布尔值                    | `boolean flag = scanner.nextBoolean();`|
| `nextLong()`        | 读取一个长整型数                  | `long number = scanner.nextLong();`   |
| `nextFloat()`       | 读取一个单精度浮点数              | `float value = scanner.nextFloat();`  |

#### **关闭 Scanner**
```java
scanner.close(); // 使用完后关闭 Scanner，释放资源
```

---

### **2. 示例代码**

#### **示例 1：读取用户输入并输出**
```java
import java.util.Scanner;

public class ScannerExample {
    public static void main(String[] args) {
        // 创建 Scanner 对象
        Scanner scanner = new Scanner(System.in);

        // 提示用户输入
        System.out.print("请输入您的名字: ");
        String name = scanner.nextLine(); // 读取一行字符串

        System.out.print("请输入您的年龄: ");
        int age = scanner.nextInt(); // 读取整数

        System.out.print("请输入您的身高（米）: ");
        double height = scanner.nextDouble(); // 读取双精度浮点数

        // 输出结果
        System.out.println("名字: " + name);
        System.out.println("年龄: " + age);
        System.out.println("身高: " + height + "米");

        // 关闭 Scanner
        scanner.close();
    }
}
```

#### **示例 2：处理多行输入**
```java
import java.util.Scanner;

public class MultiLineInputExample {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入多行文本（输入 'exit' 结束）:");
        while (true) {
            String line = scanner.nextLine(); // 读取一行
            if (line.equals("exit")) { // 判断是否退出
                break;
            }
            System.out.println("你输入的是: " + line);
        }

        scanner.close();
    }
}
```

---

### **3. 注意事项**

1. **`next()` 和 `nextLine()` 的区别**：
   - `next()` 读取以空格分隔的字符串，不会读取换行符。
   - `nextLine()` 读取整行内容，包括换行符。

2. **输入类型不匹配**：
   - 如果用户输入的数据类型与方法不匹配（如输入字符串但调用 `nextInt()`），会抛出 `InputMismatchException` 异常。
   - 可以使用 `hasNextInt()`、`hasNextDouble()` 等方法预先检查输入类型。

3. **关闭 Scanner**：
   - 使用完 `Scanner` 后，务必调用 `close()` 方法释放资源。

---

### **4. 高级用法**

#### **读取文件内容**
```java
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileScannerExample {
    public static void main(String[] args) {
        try {
            // 创建 Scanner 对象读取文件
            Scanner scanner = new Scanner(new File("example.txt"));

            // 逐行读取文件内容
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }

            // 关闭 Scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("文件未找到！");
        }
    }
}
```

#### **检查输入类型**
```java
import java.util.Scanner;

public class InputValidationExample {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入一个整数: ");
        while (!scanner.hasNextInt()) { // 检查输入是否为整数
            System.out.println("输入的不是整数，请重新输入！");
            scanner.next(); // 清除无效输入
        }
        int number = scanner.nextInt(); // 读取整数
        System.out.println("你输入的整数是: " + number);

        scanner.close();
    }
}
```

---

### **5. 总结**

- `Scanner` 是 Java 中用于读取用户输入的工具类，支持多种数据类型的读取。
- 使用时需要注意 `next()` 和 `nextLine()` 的区别，以及输入类型匹配问题。
- 读取文件或检查输入类型时，可以结合异常处理和循环逻辑。
