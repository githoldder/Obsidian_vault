# Python科学计算生态：NumPy · Pandas · Matplotlib · SciPy

> **记录时间：** 2026-04-17
> **相关笔记：** [[01-Areas/007_CS自学/python/02-Python包管理工具家族：Anaconda、Conda、Miniconda、Miniforge发展史与层级关系]] | [[01-Areas/007_CS自学/python/03-Python包管理新秀：UV]]

---

## 一、生态全景图

```
                        PyData 生态（数据科学 / 科学计算）
    ┌─────────────────────────────────────────────────────┐
    │                    NumPy（底层基座）                   │
    │            C + Python，多维数组，向量化计算               │
    └─────────────┬───────────────────────────────────────┘
                  │ 构建于 NumPy 之上
    ┌─────────────▼───────────────────────────────────────┐
    │  Pandas（数据处理） │ SciPy（科学计算）                │
    │  表格数据，缺失值处理    优化，积分，统计，信号处理         │
    └─────────────┬───────────────────────────────────────┘
                  │ 可视化层
    ┌─────────────▼───────────────────────────────────────┐
    │         Matplotlib（可视化）                           │
    │       折线图/散点图/热力图/3D 图表                       │
    └─────────────────────────────────────────────────────┘
                  ↑
    ┌─────────────┴───────────────────────────────────────┐
    │  更高层框架：Scikit-learn / TensorFlow / PyTorch       │
    │  Jupyter Notebook / JupyterLab（交互式环境）            │
    └─────────────────────────────────────────────────────┘
```

---

## 二、NumPy

**发音：** /ˈnʌmpaɪ/（NUM-py）
**定位：** Python 科学计算的基础层

### 基本信息

| 属性 | 内容 |
|------|------|
| 官方地址 | numpy.org |
| 许可证 | BSD 3-Clause |
| 主要语言 | Python + C |
| 当前稳定版 | 2.x（2024） |

### 历史脉络

| 时间 | 事件 |
|------|------|
| **1995 年** | **Numeric** 发布，Jim Hugunin 创建（最早的多维数组库） |
| **2001-2005 年** | **Numarray** 发布，与 Numeric 竞争，性能优化更好 |
| **2005 年** | **Travis Oliphant**（也是 Anaconda/Continuum Analytics 创始人）将 Numarray 的特性合并到 Numeric，诞生 **NumPy 1.0** |
| **2017 年** | NumPy 转向社区驱动开发模式 |
| **2024 年** | **NumPy 2.0** 发布（重大版本更新） |

### 核心能力

- `ndarray`：N 维数组对象，比 Python 原生 list 快数十倍
- 向量化计算：无需循环，对数组直接做数学运算
- 广播机制（Broadcasting）：不同形状数组自动对齐计算
- 线性代数：`numpy.linalg`（矩阵分解、特征值）
- 傅里叶变换：`numpy.fft`
- 随机数生成：`numpy.random`

### 简单示例

```python
import numpy as np

a = np.array([1, 2, 3, 4, 5])
b = np.array([10, 20, 30, 40, 50])

# 向量化运算（无需循环）
c = a * b           # array([10, 40, 90, 160, 250])
d = np.sum(a)       # 15
matrix = np.arange(12).reshape(3, 4)  # 3x4 矩阵
```

---

## 三、Pandas

**定位：** 表格数据分析核心库
**官方地址：** pandas.pydata.org
**核心数据结构：** `Series`（一维）+ `DataFrame`（二维表格）

### 历史脉络

| 时间 | 事件 |
|------|------|
| **2008 年** | AQR Capital Management 工程师 Wes McKinney 启动 Pandas 项目 |
| **2009 年** | 开源发布 |
| **2012 年** | Pandas 成为 NumFOCUS 赞助项目 |
| **2020 年** | Pandas 2.0 开发启动 |
| **2023-2024 年** | **Pandas 2.0** 正式发布（基于 PyArrow 后端，速度大幅提升） |

### 核心能力

- 表格读取：`read_csv()`、`read_excel()`、`read_sql()`
- 数据清洗：缺失值处理、重复值删除、类型转换
- 数据选择：按行/列/条件筛选
- 分组聚合：`groupby()`（类似 SQL GROUP BY）
- 透视表：`pivot_table()`
- 时间序列：`DatetimeIndex`、重采样、移动窗口

### 简单示例

```python
import pandas as pd

# 读取 CSV
df = pd.read_csv("data.csv")

# 按条件筛选
filtered = df[df["score"] > 80]

# 分组聚合
grouped = df.groupby("class")["score"].mean()

# 透视表
pivot = df.pivot_table(values="score", index="class", columns="subject", aggfunc="sum")
```

---

## 四、Matplotlib

**定位：** Python 可视化基础库（几乎所有 Python 可视化的底层依赖）
**官方地址：** matplotlib.org
**核心概念：** Figure（画布）→ Axes（坐标系）→ Plot（绘图）

### 历史脉络

| 时间 | 事件 |
|------|------|
| **2002 年** | John Hunter 在神经科学项目中创建（用于可视化癫痫患者脑电数据） |
| **2003 年** | 首个公开发布版本 |
| 至今 | 成为 Python 可视化事实标准，Seaborn/Altair/HoloViews 均基于 Matplotlib |

### 简单示例

```python
import matplotlib.pyplot as plt
import numpy as np

x = np.linspace(0, 2 * np.pi, 100)
y = np.sin(x)

plt.figure(figsize=(10, 6))
plt.plot(x, y, label="sin(x)", color="steelblue")
plt.scatter([np.pi/2], [1], color="red", s=100, zorder=5, label="峰值")
plt.title("Matplotlib 示例：sin 函数")
plt.xlabel("x")
plt.ylabel("sin(x)")
plt.legend()
plt.grid(True)
plt.show()
```

---

## 五、SciPy

**定位：** 科学计算算法库（构建于 NumPy 之上）
**官方地址：** scipy.org
**许可证：** BSD

### 核心子模块

| 子模块 | 功能 |
|--------|------|
| `scipy.optimize` | 数值优化（最小二乘法、非线性方程求解） |
| `scipy.integrate` | 数值积分、常微分方程求解 |
| `scipy.stats` | 统计分析（假设检验、分布函数） |
| `scipy.signal` | 信号处理（滤波器、频谱分析） |
| `scipy.linalg` | 高级线性代数 |
| `scipy.sparse` | 稀疏矩阵（大型稀疏系统） |

### 简单示例

```python
from scipy import optimize, stats
import numpy as np

# 优化：找最小值
result = optimize.minimize_scalar(lambda x: x**2 + 2*x + 1)
print(result.x)   # -1.0（函数最小值点）

# 统计：正态分布
X = stats.norm(loc=0, scale=1)  # 标准正态分布
print(X.cdf(0))    # 0.5（累积分布函数）
```

---

## 六、四个库的关系总结

```
NumPy    → 提供底层多维数组（ndarray），是一切的基础
Pandas   → 把 NumPy 的数组封装成表格（DataFrame），专注数据清洗和分析
Matplotlib → 把数据画出来，基于 NumPy 的数组直接绑定
SciPy    → 把 NumPy 数组用于科学算法（优化/积分/统计/信号）

共同特点：均构建于 PyData 生态，NumFOCUS 赞助，BSD 协议
```

---

## 七、学习路径建议

| 阶段 | 内容 | 优先级 |
|------|------|--------|
| **入门** | NumPy 数组基础 + 广播 + 索引 | ⭐⭐⭐ 最高 |
| **进阶** | Pandas DataFrame + 数据清洗 + groupby | ⭐⭐⭐ 最高 |
| **可视化** | Matplotlib 基本图表 + Pandas 内置 plot | ⭐⭐ 中 |
| **深化** | SciPy 子模块按需学习（stats/optimize） | ⭐ 按需 |

---

*参考来源：numpy.org / pandas.pydata.org / matplotlib.org / scipy.org 官方文档；Wikipedia (NumPy)*
