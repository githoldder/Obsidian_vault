# Class（类）

## 📖 定义-解释-示例

### 什么是类？

**类（Class）** 是面向对象编程中最重要的基础概念。它是**对象的蓝图或模板**——定义了对象将拥有什么**属性**（数据/状态）和什么**行为**（方法/操作）。如果对象是一栋具体的房子，类就是这栋房子的**设计图纸**。

类是**抽象**的、编译期存在的概念，对象是**具体**的、运行期存在的实例。`String` 是类，`"hello"` 是对象。`Dog` 是类，你家那只叫旺财的金毛是对象。

### 类的三个组成部分

一个完整的 Java 类包含三个核心部分：

**1. 属性（Fields / 成员变量）**
描述对象的状态。比如 `Dog` 类有 `name`（名字）、`breed`（品种）、`age`（年龄）属性。属性通常声明为 `private`（封装），通过 getter/setter 访问。属性的选择决定了这个类"记住什么"。

**2. 构造方法（Constructors）**
对象的"出生仪式"——当 `new ClassName()` 被调用时，构造方法负责初始化对象的状态。没有构造方法就没有对象。Java 中如果未显式声明构造方法，编译器自动生成无参默认构造函数。

**3. 方法（Methods）**
定义对象能做什么。`Dog` 能 `bark()`、`eat()`、`sleep()`。方法决定了这个类"能做什么"。方法体是实现细节，方法签名（名称+参数+返回值）是契约。

### Java 类的完整示例

```java
/**
 * 表示一只狗的类
 */
public class Dog {
    // === 属性（状态）===
    private String name;       // 私有的，外部不能直接访问
    private String breed;
    private int age;
    
    // 类变量（static）——属于类本身，不属于任何一个实例
    private static int dogCount = 0;
    
    // === 构造方法 ===
    public Dog(String name, String breed, int age) {
        this.name = name;
        this.breed = breed;
        this.age = age;
        dogCount++;  // 每创建一只狗，计数 +1
    }
    
    // 无参构造方法（默认值）
    public Dog() {
        this("未命名", "混血", 0);
    }
    
    // === Getter / Setter（访问器）===
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getBreed() { return breed; }
    
    public int getAge() { return age; }
    public void setAge(int age) {
        if (age >= 0) {  // 验证：年龄不能为负数
            this.age = age;
        }
    }
    
    // === 行为方法 ===
    public void bark() {
        if (age < 1) {
            System.out.println(name + "：呜~呜~（奶狗叫）");
        } else {
            System.out.println(name + "：汪汪汪！");
        }
    }
    
    public void eat(String food) {
        System.out.println(name + " 正在吃 " + food);
    }
    
    // === 静态方法 ===
    public static int getDogCount() {
        return dogCount;
    }
    
    // === toString：对象的字符串表示 ===
    @Override
    public String toString() {
        return "Dog{name='" + name + "', breed='" + breed + "', age=" + age + "}";
    }
    
    // === equals：判断两个对象是否 "相等" ===
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Dog other)) return false;
        return age == other.age 
            && Objects.equals(name, other.name) 
            && Objects.equals(breed, other.breed);
    }
    
    // === hashCode：equals 一致的哈希码 ===
    @Override
    public int hashCode() {
        return Objects.hash(name, breed, age);
    }
}
```

### 类中的关键概念

**1. 封装（Encapsulation）**
属性 `private`，方法 `public`。外部通过调用 `getName()` 获取名字，而不是直接读 `name`。为什么？因为如果未来名字的存储方式变了（从 `String` 变成 `Name` 对象），只需改 getter 内部逻辑——调用方不受影响。封装 = 实现细节对调用方隐藏 = 变更自由。

**2. 构造方法重载（Constructor Overloading）**
上述 `Dog` 类有两个构造方法——一个全参构造（`Dog(name, breed, age)`），一个无参构造带默认值。Java 根据参数数量/类型选择调用哪个。构造方法之间可以互相调用：`this(...)` 必须是构造方法的第一条语句。

**3. this 关键字**
`this.name` = 当前对象的 `name` 属性（区分同名的参数）。`this()` 调用同类其他构造方法。**`this` 就是一个"指向自己的引用"**。

**4. static 成员**
`static` 变量和方法**属于类本身，不属于任何特定实例**。`dogCount` 只有一个，所有 `Dog` 实例共享。`static` 方法内不能使用 `this`（因为没有当前对象的概念）。`Math.sqrt()` 就是 `static` 的——不需要创建 `Math` 对象就能调用。

**5. final 修饰符**
- `final` 类：不能被继承（`String` 是 final 的）
- `final` 方法：不能被子类重写
- `final` 变量：赋值后不可更改（常量）

### 类的生命周期

1. **加载（Loading）**：JVM 类加载器找到 `.class` 文件，读入方法区
2. **链接（Linking）**：验证字节码正确性、分配静态变量内存、解析符号引用
3. **初始化（Initialization）**：执行静态初始化块和静态变量赋值
4. **实例化（Instantiation）**：`new` 关键字触发——堆上分配内存 → 默认值初始化 → 显式初始化 → 构造方法执行
5. **使用（Using）**：对象的方法被调用，属性被读写
6. **回收（GC）**：对象不再被引用 → 垃圾收集器回收内存 → `finalize()`（已弃用）

### 内部类（Inner Class）概述

Java 允许在一个类中定义另一个类：
- **成员内部类**：像成员变量一样，可以访问外部类的所有成员（包括 private）
- **静态内部类**：用 `static` 修饰，不持有外部类引用，独立性强
- **局部内部类**：定义在方法内部，作用域仅限于该方法
- **匿名内部类**：没有名字的类，常用于简化事件处理器和回调

### 设计一个好类的原则

1. **单一职责**：一个类只负责一件事（SRP）。`Dog` 不负责把狗信息存到数据库
2. **高内聚**：类的属性和方法紧密相关。`Dog` 的 `bark()` 和 `name` 都围绕"狗"这个抽象
3. **对外简洁**：暴露最小必要的公共接口。狗的消化过程对外不需要暴露
4. **不可变优先**：如果对象状态不需要变，设计为不可变类（`final` 字段，无 setter）

## 🔗 相关笔记
[[03-面向对象-001类与对象-Class与Object-Object]]
[[继承与多态]]
[[封装与信息隐藏]]
[[Java构造方法]]
[[抽象类与接口]]
[[内部类与匿名类]]
[[类加载机制]]
[[垃圾收集GC]]
[[不可变对象]]
[[Java访问修饰符]]

🏷️: [[Java]] [[OOP]] [[类]] [[面向对象]] [[class]]
