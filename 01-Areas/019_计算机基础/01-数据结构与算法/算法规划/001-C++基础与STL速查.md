# 001-C++基础与STL速查

> 西蒙学习法·输入阶段 | 目标：30分钟内掌握竞赛编程必备C++基础

---

## 一、快速IO（必背模板）

```cpp
#include <bits/stdc++.h>
using namespace std;

int main() {
    ios::sync_with_stdio(false);  // 关闭C与C++的同步
    cin.tie(nullptr);             // 解除cin与cout的绑定
    
    // 你的代码
    
    return 0;
}
```

**一句话记忆**：`sync_with_stdio(false)` 让cin变快，`tie(nullptr)` 让cin不等待cout。

---

## 二、STL容器速查表

### 2.1 vector（动态数组）

| 操作 | 代码 | 时间复杂度 |
|------|------|-----------|
| 尾部添加 | `v.push_back(x)` | O(1)均摊 |
| 尾部删除 | `v.pop_back()` | O(1) |
| 随机访问 | `v[i]` | O(1) |
| 获取大小 | `v.size()` | O(1) |
| 清空 | `v.clear()` | O(n) |
| 排序 | `sort(v.begin(), v.end())` | O(nlogn) |
| 二分查找 | `binary_search(v.begin(), v.end(), x)` | O(logn) |

**初始化技巧**：
```cpp
vector<int> v(n);           // n个0
vector<int> v(n, val);      // n个val
vector<int> v = {1,2,3};    // 列表初始化
```

---

### 2.2 map/unordered_map（映射）

| 特性 | map | unordered_map |
|------|-----|---------------|
| 底层 | 红黑树 | 哈希表 |
| 有序性 | 按键排序 | 无序 |
| 查找 | O(logn) | O(1)平均 |
| 适用 | 需要有序 | 纯查找 |

**常用操作**：
```cpp
map<string, int> mp;
mp["key"] = value;          // 插入/修改
mp.count("key");            // 是否存在（0或1）
mp.find("key") != mp.end(); // 是否存在
mp.erase("key");            // 删除
```

---

### 2.3 set/unordered_set（集合）

```cpp
set<int> s;
s.insert(x);    // 插入
s.erase(x);     // 删除
s.count(x);     // 是否存在（0或1）
s.find(x);      // 返回迭代器

// 遍历（有序）
for (int x : s) cout << x << " ";
```

---

### 2.4 stack（栈）

```cpp
stack<int> st;
st.push(x);     // 入栈
st.pop();       // 出栈
st.top();       // 栈顶
st.empty();     // 是否为空
st.size();      // 大小
```

---

### 2.5 queue（队列）

```cpp
queue<int> q;
q.push(x);      // 入队
q.pop();        // 出队
q.front();      // 队首
q.back();       // 队尾
q.empty();
q.size();
```

---

### 2.6 priority_queue（优先队列/堆）

```cpp
// 大根堆（默认）
priority_queue<int> pq;

// 小根堆
priority_queue<int, vector<int>, greater<int>> pq;

pq.push(x);     // 入堆
pq.pop();       // 弹出堆顶
pq.top();       // 堆顶
```

---

## 三、常用算法函数

```cpp
// 排序
sort(a.begin(), a.end());                    // 升序
sort(a.begin(), a.end(), greater<int>());    // 降序

// 二分查找（需先排序）
binary_search(a.begin(), a.end(), x);        // 是否存在
lower_bound(a.begin(), a.end(), x);          // 第一个>=x的位置
upper_bound(a.begin(), a.end(), x);          // 第一个>x的位置

// 最值
*max_element(a.begin(), a.end());
*min_element(a.begin(), a.end());

// 求和
accumulate(a.begin(), a.end(), 0);

// 去重（需先排序）
sort(a.begin(), a.end());
a.erase(unique(a.begin(), a.end()), a.end());
```

---

## 四、字符串处理

```cpp
string s = "hello";
s.length();     // 长度
s.substr(pos, len);  // 子串
s.find("sub");  // 查找，返回位置或string::npos
s.replace(pos, len, "new");  // 替换

// 数字与字符串互转
int x = stoi("123");
string s = to_string(123);
```

---

## 五、输入输出技巧

```cpp
// 不定量输入
int x;
while (cin >> x) {
    // 处理
}

// 多组数据
int T;
cin >> T;
while (T--) {
    // 处理每组
}

// 输出精度
cout << fixed << setprecision(2) << x << endl;  // 保留2位小数
```

---

## 六、费曼自测

1. 为什么需要 `ios::sync_with_stdio(false)`？
2. map和unordered_map的核心区别是什么？
3. 如何创建一个小根堆？
4. lower_bound和upper_bound的区别？

---

## 七、本周刷题清单

| 题号 | 题目 | 知识点 |
|------|------|--------|
| LeetCode 1 | Two Sum | map应用 |
| LeetCode 217 | Contains Duplicate | set应用 |
| LeetCode 242 | Valid Anagram | map计数 |
| LeetCode 20 | Valid Parentheses | stack基础 |
| LeetCode 155 | Min Stack | stack设计 |
| LeetCode 232 | Implement Queue using Stacks | stack+queue |
| LeetCode 26 | Remove Duplicates from Sorted Array | 双指针 |
| LeetCode 27 | Remove Element | 双指针 |
| LeetCode 344 | Reverse String | 双指针 |

---

tags: [C++, STL, 基础, 西蒙学习法]
date: 2026-04-10
