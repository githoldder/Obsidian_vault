# Python包管理新秀：UV

> **记录时间：** 2026-04-17
> **相关笔记：** [[02-Python包管理工具家族：Anaconda、Conda、Miniconda、Miniforge发展史与层级关系]]

---

## 一、什么是 UV？

UV 是由 **Astral**（Ruff 代码检查工具的作者团队）用 **Rust** 编写的 Python 包和项目管理器，定位为 Python 工具链的终极合一方案。

> 官网：https://astral.sh/uv/

---

## 二、核心定位：一工具替代 7 种工具

| 替代工具 | 功能 | UV 对应命令 |
|---------|------|------------|
| pip | 安装包 | `uv pip install` |
| pip-tools | 依赖锁定 | `uv lock` |
| pipx | 全局工具安装 | `uv tool install` |
| poetry | 项目管理 | `uv init` / `uv add` |
| pyenv | Python 版本管理 | `uv python` |
| twine | 发布到 PyPI | `uv build` / `uv publish` |
| virtualenv | 虚拟环境 | （内置，无需单独使用） |

---

## 三、关键指标

- **速度：比 pip 快 10-100 倍**（Rust 实现，全局缓存，依赖去重）
- **单文件可执行脚本**：支持内联依赖声明（`# /// script` 语法）
- **通用 Lockfile**：统一锁文件格式，不绑定特定包管理器
- **Workspace 支持**：Cargo 风格的 monorepo 支持
- **跨平台**：macOS / Linux / Windows 全支持
- **零依赖安装**：不需要 Python 或 Rust 即可安装

---

## 四、安装方式

```bash
# 官方推荐（Shell，一行搞定）
curl -LsSf https://astral.sh/uv/install.sh | sh

# Homebrew
brew install uv

# pip 安装
pip install uv
```

---

## 五、常用命令速查

### 项目管理
```bash
uv init example          # 创建新项目（自动生成 pyproject.toml）
cd example
uv add requests          # 添加依赖
uv lock                  # 生成/更新锁文件
uv sync                  # 同步环境到 lockfile
uv run python script.py   # 在隔离环境中运行脚本
```

### 包安装
```bash
uv pip install numpy pandas matplotlib   # pip 兼容模式
uv pip install --requirements requirements.txt
```

### 全局工具
```bash
uv tool install ruff        # 安装全局工具（类似 pipx）
uv tool run ruff check .    # 运行全局工具
```

### Python 版本管理
```bash
uv python list             # 列出可用 Python 版本
uv python install 3.12      # 安装指定版本
uv python pin 3.11          # 锁定项目 Python 版本
```

---

## 六、与 Conda/Mamba 的关系

```
Conda/Mamba 生态          vs.          UV 生态
─────────────────────────────────────────────────────
设计理念：任意语言包                    专注 Python 包
二进制包预编译：✓                      部分包有预编译 wheel
conda-forge 生态                          PyPI 生态
C/C++ 科学计算包优势                      速度优势显著
```

**不互斥**：可以用 Miniforge 管理系统级科学计算环境（NumPy/SciPy），用 UV 管理 Python 纯包。

---

## 七、为什么 2024-2025 年 UV 爆发式增长？

1. **速度革命**：pip install 一次要几分钟，uv pip install 几秒
2. **Python 工具碎片化痛点**：poetry/pex/pipx/pyenv 各不相同，UV 一统江湖
3. **Astral 背书**：Ruff 已经证明他们能做出比原工具快一个数量级的替代品
4. **AI 编程友好**：Claude Code、Cursor 等 AI 工具链原生集成 UV

---

## 八、我的选择建议

| 场景 | 推荐 |
|------|------|
| 新 Python 项目（2025+） | **UV 优先**，极简、快速、现代 |
| 科学计算环境（NumPy/SciPy 为主） | Miniforge + conda-forge |
| 两者结合 | Conda 创建基础科学环境 + UV 管理项目依赖 |
| AI 辅助编程项目 | **UV 必选**，AI 工具链原生支持 |

---

*参考来源：astral.sh/uv 官方文档*
