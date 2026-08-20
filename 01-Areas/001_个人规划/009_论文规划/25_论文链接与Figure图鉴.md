---
title: 论文链接清单与 Figure 图鉴
别名: 必读论文 + 图表规范
类型: 参考
状态: 2026-08-20
---

# 论文链接清单与 Figure 图鉴

> 用于「模仿对齐」的标准顶会顶刊论文链接，以及顶会论文 Figure 的类别学与呈现规范。

---

## 一、必读论文链接（已核实 arXiv 编号）

### A. 知识追踪核心链（KT 主线，必看）

| 论文 | 会议 | arXiv | 链接 |
|---|---|---|---|
| DKT: Deep Knowledge Tracing | NeurIPS 2015 | 1506.05908 | https://arxiv.org/abs/1506.05908 |
| DKVMN: Dynamic Key-Value Memory Networks for KT | WWW 2017 | 1611.08108 | https://arxiv.org/abs/1611.08108 |
| AKT: Context-Aware Attentive Knowledge Tracing | KDD 2020 | 2007.12324 | https://arxiv.org/abs/2007.12324 |
| SAKT: A Self-Attentive Model for KT | EDM 2019 | 1907.06837 | https://arxiv.org/abs/1907.06837 |
| simpleKT: A Simple But Tough-to-Beat Baseline | ICLR 2023 | 2302.06881 | https://arxiv.org/abs/2302.06881 |
| SparseKT: Robust KT via k-Sparse Attention | AAAI 2024 | 2407.17097 | https://arxiv.org/abs/2407.17097 |
| pyKT: A Python Library to Benchmark DLKT | NeurIPS 2022 D&B | 2206.11460 | https://arxiv.org/abs/2206.11460 |

### B. 发音评估 / 序数回归（本论文交叉点，核心对照）

| 论文 | 会议 | arXiv | 链接 |
|---|---|---|---|
| SpeechOcean762（数据基准） | Interspeech 2021 | 2104.01378 | https://arxiv.org/abs/2104.01378 |
| Preserving Phonemic Distinctions for Ordinal Regression | Interspeech 2023 | 2310.01839 | https://arxiv.org/abs/2310.01839 |
| Improving pronunciation assessment via ordinal regression | ICASSP 2021 | 2010.13339 | https://arxiv.org/abs/2010.13339 |

### C. 权威综述（认知地图主干）

| 综述 | 出处 | 链接 |
|---|---|---|
| Knowledge Tracing: A Survey | ACM Computing Surveys 2023 (IF≈14) | https://doi.org/10.1145/3569576 |
| Computer-assisted Pronunciation Training — Speech synthesis is almost all you need | arXiv 2022 | https://arxiv.org/abs/2207.00774 |

> 建议阅读顺序：先 C（综述建骨架）→ A（simpleKT/SparseKT 看基线 + 架构图）→ B（序数回归看交叉点 + 发音评估的 figure 规范）。

---

## 二、顶会论文 Figure 的类别学（9 类）

标准顶会论文（NeurIPS/ICLR/AAAI/ACL/EDM/AIED）的 Figure 按功能分九类，几乎每篇都会命中其中 5-6 类：

| # | 类别 | 英文名 | 典型位置 | 本论文对应 |
|---|---|---|---|---|
| 1 | 架构/模型图 | Architecture / Model Overview | Figure 1 | 序数回归 KT 架构 |
| 2 | 框架/流程示意图 | Pipeline / Framework | Figure 1-2 | 数据→特征→模型流程 |
| 3 | 主结果对比 | Main Results | Figure 2-3（柱状/表格） | 三基线 + 序数回归 |
| 4 | 消融实验 | Ablation Study | 中间（柱状/条形） | 标签粒度、edit 消融 |
| 5 | 统计显著性 | Significance | 误差棒图 | edit 配对 t-test |
| 6 | 定性/案例可视化 | Qualitative / Case Study | 靠后（热力图/t-SNE） | 母语负迁移音素 |
| 7 | 超参/敏感度 | Hyperparameter Sensitivity | 靠后（折线/热力图） | 阈值、seq_len |
| 8 | 学习曲线 | Training/Learning Curve | 附录 | loss/AUC 收敛 |
| 9 | 数据统计图 | Dataset Statistics | Figure 2 或附录 | 标签分布、音素难度 |

### 各类的「标准长相」

1. **架构图**：横向或纵向 block 流程图，模块用圆角矩形，数据流用箭头，颜色 2-3 种主色区分「输入/编码器/输出头」。**不堆文字**，靠图例。用 draw.io / TikZ / Figma 画，不是 matplotlib。
2. **主结果**：论文核心是**表格为主、图为辅**。顶会论文几乎都有一张「大表」列所有模型 × 所有数据集 × 所有指标（加粗最优、下划线次优）。图只做最关键的 1-2 个对比。
3. **消融**：横向条形图，每个消融维度一组，删除项用灰色/浅色，完整模型用主题色，标注增量百分比。
4. **显著性**：误差棒（mean ± std，5+ seed），星号标 p 值（\* p<0.05, \*\* p<0.01），或用括号连线标 t-test。
5. **案例可视化**：注意力热力图（viridis 色）、t-SNE/UMAP 散点（不同类不同色）、轨迹曲线。
6. **敏感度**：x 轴超参、y 轴指标，多根折线（每根一个配置），或热力图（x/y 两维超参）。

---

## 三、配色与排版规范（对应用户「配色难看」的痛点）

### 色盲友好调色板（学术标配）

**Okabe-Ito 8 色**（Nature 推荐，色盲安全）：

| 名称 | HEX |
|---|---|
| 橙 Orange | `#E69F00` |
| 天蓝 Sky Blue | `#56B4E9` |
| 蓝绿 Bluish Green | `#009E73` |
| 黄 Yellow | `#F0E442` |
| 蓝 Blue | `#0072B2` |
| 朱红 Vermillion | `#D55E00` |
| 粉紫 Reddish Purple | `#CC79A7` |
| 黑 Black | `#000000` |

**连续变量**（热力图/密度）：`viridis`、`cividis`（色盲安全 + 灰度打印友好）> `plasma` > `magma`。

**本论文推荐主色**：蓝色 `#0072B2`（主）、橙色 `#E69F00`（对照）、灰色 `#999999`（删除/基线）。避免红绿对比（色盲不可辨）。

### matplotlib 出版级 rcParams（可直接套用）

```python
import matplotlib as mpl
mpl.rcParams.update({
    'font.family': 'sans-serif',
    'font.sans-serif': ['Helvetica', 'Arial', 'PingFang SC'],  # 中文走 PingFang SC
    'font.size': 9,
    'axes.titlesize': 10,
    'axes.labelsize': 9,
    'axes.edgecolor': '#444444',
    'axes.linewidth': 0.8,
    'xtick.labelsize': 8,
    'ytick.labelsize': 8,
    'axes.spines.top': False,      # 去上边框
    'axes.spines.right': False,    # 去右边框
    'legend.frameon': False,       # 图例去框
    'legend.fontsize': 8,
    'figure.dpi': 300,             # 出版级
    'savefig.dpi': 300,
    'savefig.bbox': 'tight',
    'savefig.pad_inches': 0.02,
})
```

### 排版六原则

1. **去 chartjunk**：去掉背景网格、上/右边框、装饰性阴影，只留必要坐标轴。
2. **一个图一个信息**：不要在一张图里塞 3 个以上结论；结论多了就拆图。
3. **字号与论文正文一致**（8-10pt），图里文字不小于 7pt。
4. **误差棒必带**（多 seed），标注均值 ± std 或 ± sem，明确 seed 数。
5. **表格优先**：主结果用三线表（booktabs 风格），加粗最优、下划线次优，图只辅助。
6. **同一指标同一色**：全篇 AUC 统一蓝色、MAE 统一橙色，跨图一致。

### 工具分工

| 内容 | 工具 |
|---|---|
| 架构图 / 框架图 | draw.io / Figma / TikZ（**不用 matplotlib**） |
| 数据统计 / 结果图 | matplotlib（套上面 rcParams） |
| 三线表 | LaTeX booktabs（正文） |
| 中文渲染 | PingFang SC（本机已验证） |

---

## 四、本论文现有 6 张图的「升级对照」

| 现有图 | 问题 | 升级方向 |
|---|---|---|
| fig1 基线对比 | 柱状图配色可能单调 | 用 Okabe-Ito，音素级/词级分组对比 |
| fig2 序数建模 | — | 表格为主，图简化为关键指标对比 |
| fig3 标签粒度 | 双轴曾重叠 | 已修；改为「U 型」双曲线图更直观 |
| fig4 edit 显著性 | — | 加 p 值星号 + 三指标并排误差棒 |
| fig5 冷启动 | 描述性对比 | 已补配对 t-test，图上加显著性星号 |
| fig6 音素难度 | — | 改横向条形图，按错误率排序，负迁移组高亮 |

---

## 相关笔记

- [[20_认知地图_AI语言学习建模]]
- [[22_技术树_知识追踪KT]]
- [[23_方向判断_A方案差异化卖点]]
- [[30_论文初稿_音素级知识追踪]]

🏷️ #论文写作 #Figure规范 #配色 #知识追踪 #发音评估
