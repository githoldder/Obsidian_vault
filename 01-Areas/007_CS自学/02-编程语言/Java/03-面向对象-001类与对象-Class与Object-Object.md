# Object（对象）

## 📖 定义-解释-示例

### 什么是对象？

**对象（Object）** 是面向对象编程中**类的实例**，是程序运行时实际存在的实体。如果类（Class）是"狗"这个抽象概念，对象就是你家那只名叫旺财、3 岁、毛色金黄的具体金毛犬。

在 Java 中，一切（除 8 种基本类型外）都是对象。`String s = "hello"` 中，`s` 是引用变量，`"hello"` 是 `String` 类的对象。对象具有三个核心特征：**状态**（属性值）、**行为**（可调用的方法）、**标识**（唯一的身份，即使两个对象的状态完全相同，它们也是两个不同的对象）。

### 对象的创建——从 new 开始

**堆上分配**：

```java
Dog myDog = new Dog("旺财", "金毛", 3);
```

这一行代码发生了很多事：
1. `new` 关键字触发 JVM 在**堆（Heap）**上分配内存
2. 实例变量被赋予默认值（`name=null`, `breed=null`, `age=0`）
3. 构造方法 `Dog("旺财", "金毛", 3)` 被调用，将属性设置为实际值
4. 返回对象在堆上的**引用**（内存地址），赋值给栈上的变量 `myDog`

**牢记**：Java 中对象变量存储的是**引用**（指向堆上对象的指针），不是对象本身。`myDog` 是一个遥控器，不是电视。

### 对象的引用——指针的外衣

```java
Dog a = new Dog("旺财", "金毛", 3);
Dog b = a;  // b 和 a 指向同一个对象！
b.setName("来福");
System.out.println(a.getName());  // 输出 "来福" —— a 看到的也变了
```

`b = a` 复制的是**引用**，不是对象。两个引用指向堆上同一块内存。这和 C/C++ 的指针本质相同，只是 Java 隐藏了 `*` 和 `&` 的显式语法。

**实际后果**：
- 修改一个引用的对象状态 → 所有引用这个对象的变量都能看到变化
- `==` 比较的是**引用相等**（是否指向同一个对象），不是内容相等。判断内容相等用 `.equals()`
- Java 通过引用传递（"pass by sharing" or "pass by value of reference"）：方法的参数传递的是引用的副本，方法内可以修改对象的内容，但不能改变调用者的引用指向

### Java 中所有类的根——java.lang.Object

在 Java 中，每个类（不管是 `Dog`、`String`、`ArrayList` 还是你写的任何类）都**隐式继承自 `java.lang.Object`**。这定义了所有 Java 对象共享的契约：

**1. `toString()`**
- 默认实现：`类名@哈希码`，如 `Dog@1a2b3c`——几乎没用
- 最佳实践：**重写**，返回对象状态的清晰描述
- `System.out.println(obj)` 会自动调用 `obj.toString()`
- IDE（IntelliJ/Eclipse）和 Lombok 的 `@ToString` 可以自动生成

**2. `equals(Object obj)`**
- 默认实现：等同于 `==`，判断引用相等
- 必须重写来实现**逻辑相等**——比如两个 `Dog` 对象名字、品种、年龄都相同，它们应该是"相等的"
- **黄金法则**：重写 `equals` 必须同时重写 `hashCode()`。违反了这条规则，`HashMap`/`HashSet` 将产生诡异 bug（两个 `equals` 为真的对象被放到了不同的桶中，`contains()` 返回 `false`）

**3. `hashCode()`**
- 返回对象的**哈希码**（整数），用于 `HashMap`、`HashSet`、`Hashtable` 等哈希表
- 契约：如果 `a.equals(b)` 为真，则 `a.hashCode() == b.hashCode()` 必定为真。反之不必然（哈希冲突）
- 好的哈希函数应把对象尽量均匀分布到整数范围，减少冲突

**4. `clone()`**
- 创建对象的副本。默认是**浅拷贝**（只复制引用，不复制引用指向的对象）
- 需要实现 `Cloneable` 接口（标记接口，无方法）
- 深度拷贝需要手动实现
- 现代 Java 更推荐使用拷贝构造方法或静态工厂方法替代 `clone()`

**5. `getClass()`**
- 返回对象的 `Class` 对象（运行时类型信息）
- `obj.getClass().getName()` → 获取类名（全限定名）
- `obj.getClass() == Dog.class` → 判断是否是某个精确类型
- `obj instanceof Dog` → 判断是否是某个类型（包括子类）

**6. `finalize()`**
- 在对象被 GC 回收前由垃圾收集器调用。**已在 Java 9 标记为 @Deprecated，Java 18+正式移除**
- 不要依赖 `finalize()` 释放资源——时间不确定！用 `try-with-resources` 和 `Closeable` 接口

**7. `notify()` / `notifyAll()` / `wait()`**
- 用于**线程间通信**和同步
- `wait()` 使当前线程等待，直到其他线程调用同一对象的 `notify()` / `notifyAll()`
- 必须在 `synchronized` 块内调用，否则抛出 `IllegalMonitorStateException`

### 对象的生命周期

```
创建 (new) → 使用 (方法调用 & 状态修改) → 不可达 (无引用) → 垃圾收集 (GC 回收内存)
```

**不可达的判定**：GC 从 **GC Roots**（栈上的局部变量、静态变量、活跃线程等）出发，通过引用追踪所有可达对象。不可达的对象被标记为垃圾，等待回收。

**回收的实际发生时间**：不确定。GC 在 JVM 认为必要时才触发（内存压力大、空闲时间等）。`System.gc()` 只是"建议"JVM 做垃圾回收，JVM 可以无视。

### 对象 vs 基本类型

| 维度 | 基本类型（int, boolean...） | 对象类型（Integer, Boolean...） |
|------|---------------------------|-------------------------------|
| 存储位置 | 栈（局部变量）/ 堆（成员变量） | 堆 |
| 默认值 | 0, false 等（有值） | null（无引用） |
| 比较 | `==` 比较值 | `==` 比较引用，`.equals()` 比较内容 |
| 性能 | 快（无间接引用） | 慢（解引用 + 方法调用） |
| 装箱/拆箱 | 自动装箱为包装类 | 自动拆箱为基本类型 |

**NullPointerException（NPE）**：调用了一个 `null` 引用的方法或访问属性。Java 中最常见的运行时异常。防御策略：`Optional<T>`、`@NonNull` 注解、显式 null 检查、Objects.requireNonNull()。

### 对象的深拷贝 vs 浅拷贝

```java
// 浅拷贝——只复制引用
class Kennel implements Cloneable {
    Dog[] dogs;
    public Object clone() {
        Kennel k = (Kennel) super.clone();
        k.dogs = this.dogs;            // 引用复制！两个 Kennel 共享同一个 dogs 数组
        return k;
    }
}

// 深拷贝——递归复制所有引用对象
class Kennel implements Cloneable {
    Dog[] dogs;
    public Object clone() {
        Kennel k = (Kennel) super.clone();
        k.dogs = new Dog[this.dogs.length];
        for (int i = 0; i < dogs.length; i++) {
            k.dogs[i] = new Dog(this.dogs[i].getName(), ...);
        }
        return k;
    }
}
```

浅拷贝和深拷贝的区别：**浅拷贝只复制一层，深拷贝递归复制整棵树**。序列化/反序列化也是一种实现深拷贝的便捷方式（但较慢）。

## 🔗 相关笔记
[[03-面向对象-001类与对象-Class与Object-Class]]
[[继承与多态]]
[[封装与信息隐藏]]
[[equals与hashCode契约]]
[[垃圾收集GC]]
[[Java引用类型]]
[[深拷贝与浅拷贝]]
[[NullPointerException与Optional]]
[[不可变对象]]
[[Java内存模型-JVM堆栈]]

🏷️: [[Java]] [[OOP]] [[对象]] [[面向对象]] [[Object]] [[JVM]]
