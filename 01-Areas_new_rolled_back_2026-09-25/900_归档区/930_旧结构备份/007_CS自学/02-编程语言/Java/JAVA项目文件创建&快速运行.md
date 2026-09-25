### **创建 Java 项目并运行 Java 代码**

#### **1. 创建 Java 项目**

1. 打开 Eclipse，点击 **File（文件）** > **New（新建）** > **Java Project（Java 项目）**。
2. 在 **Project name（项目名称）** 输入你的项目名称，例如 `HelloWorld`。
3. 选择 JDK 版本（一般默认即可）。
4. 点击 **Finish（完成）**，Eclipse 会创建一个 Java 项目。

#### **2. 创建 Java 类**

1. 右键 **src 文件夹**，选择 **New（新建）** > **Class（类）**。
2. 在 **Name（名称）** 栏输入类名，例如 `HelloWorld`。
3. 勾选 **public static void main(String[] args)**，然后点击 **Finish**。
4. Eclipse 会自动生成一个 Java 文件，如下：
    
    java
    
    复制编辑
    
    `public class HelloWorld {     public static void main(String[] args) {         System.out.println("Hello, World!");     } }`
    

#### **3. 运行 Java 程序**

1. 选中 `HelloWorld.java` 文件，点击 Eclipse 上方的 **运行按钮（绿色的播放键 ▶）**。
2. 或者，右键 `HelloWorld.java` 文件，选择 **Run As（运行方式）** > **Java Application（Java 应用程序）**。
3. 运行后，Eclipse 的 **Console（控制台）** 会输出：
    
    复制编辑
    
    `Hello, World!`
    

---

### **三、常见问题**

1. **Eclipse 提示 Java 版本错误？**
    
    - 确保你的 Eclipse 绑定了正确的 JDK。
    - 打开 **Window（窗口）** > **Preferences（首选项）** > **Java** > **Installed JREs**，检查 JDK 是否正确。
2. **Eclipse 运行时报错 “Editor does not contain a main type”**
    
    - 确保 Java 类包含 `main` 方法，即：
        
        java
        
        复制编辑
        
        `public static void main(String[] args) {`
        
    - 确保你的 Java 类文件已保存在 `src` 目录下。
3. **Eclipse 运行时报错 “Could not find or load main class”**
    
    - 右键项目，选择 **Properties（属性）** > **Java Build Path**，检查是否设置了 `src` 目录。
    - 清理项目：点击 **Project** > **Clean**，然后重新运行。

---

### **四、使用快捷键提高效率**

|快捷键|功能|
|---|---|
|`Ctrl + N`|新建 Java 类、项目等|
|`Ctrl + S`|保存当前文件|
|`Ctrl + Shift + O`|自动导入缺失的包|
|`Ctrl + F11`|运行程序|
|`Alt + Shift + S`|显示 Source 代码操作（如生成构造方法）|

如果你是 Eclipse 的新手，可以先熟悉基本操作，之后再学习如何使用 Debug 调试代码、配置 Maven 等高级功能。🚀