# Week1 — C++ 快速环境与练习

本目录为第一周（W1）练习的代码与构建脚本，包含：

- `template.cpp`：C++ 快速 IO 模板
- `Makefile`：一键编译所有练习并把可执行文件放到 `bin/`
- `solutions/1_two_sum.cpp`：LeetCode 1 Two Sum 示例实现与测试
- `solutions/217_contains_duplicate.cpp`：LeetCode 217 Contains Duplicate 示例实现与测试
- `solutions/242_valid_anagram.cpp`：LeetCode 242 Valid Anagram 示例实现与测试

使用方法（在 macOS zsh 下）：

1. 进入本目录：

```bash
cd /Users/caolei/Desktop/Obsidian_root/019_计算机基础/01-数据结构与算法/算法规划/week1
```

2. 编译（需要系统安装 g++ / clang++，推荐支持 C++17）：

```bash
make
```

3. 运行示例：

```bash
./bin/1_two_sum
./bin/217_contains_duplicate
./bin/242_valid_anagram
```

练习建议：
- 先阅读 `template.cpp`，熟悉快速 IO
- 把每个 `solutions` 文件作为可练习的单元，尝试在本地手动修改输入并运行
- 下一步：实现 Day1-2 其它题目，并开始记录错题本
