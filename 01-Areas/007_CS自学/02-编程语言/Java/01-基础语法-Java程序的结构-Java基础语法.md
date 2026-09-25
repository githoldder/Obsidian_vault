---
tags:
  - 语法
  - 编程
  - java
  - 本质
  - 拆解
---
## 🧱 一、Java 基础语法（First Principles）

我们从最小构成单位来看，Java 程序的本质是：

> “**类（class）中定义方法（method），方法中写语句（statement）来执行逻辑**。”

### ✅ Java 程序的结构（原子级拆解）：

java

复制代码

`public class Main {        // 类定义     public static void main(String[] args) {  // 主方法：程序入口         System.out.println("Hello, Java!");   // 输出语句     } }`

|部分|作用|
|---|---|
|class|定义一个类|
|main 方法|Java 程序的主入口|
|System.out|输出语句，用于打印信息|
|语句结尾 `;`|每条语句以分号结束|