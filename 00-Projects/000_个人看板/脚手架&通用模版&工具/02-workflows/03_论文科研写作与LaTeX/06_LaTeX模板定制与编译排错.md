# LaTeX 模板适配与工具配置

> **定位**：从 LatexCenterTool 拉取模板到本地编译运行的完整指南。
> 覆盖：模板选择 → 目录结构 → 编译配置 → 常见修改场景 → 排错。
> 是"最强半自动化工作流"的**精密渲染层**，承接 Claude Code 生成的 `.tex` 到最终 PDF。

---

## 一、模板来源：LatexCenterTool 项目枢纽

所有模板统一通过 `LatexCenterTool` 管理，使用 git subtree 引入三个上游仓库：

```
LatexCenterTool/
├── packages/
│   ├── cit-scie-thesis-latex/        # 论文模板（常工院/学术论文）
│   ├── thuthesis-manual-writing-kit/  # 项目文档 / 手册模板
│   └── czu-beamer-template/           # 课设 PPT / Banner 模板
└── context/
```

### 1.1 选择模板

| 场景 | 路径 | 适用 |
|---|---|---|
| 学术论文 / 毕业设计 | `packages/cit-scie-thesis-latex` | 常工院论文、国标格式 |
| 项目文档 / 手册 | `packages/thuthesis-manual-writing-kit` | 技术文档、产品手册 |
| 课设 PPT / Banner | `packages/czu-beamer-template` | Beamer 幻灯片 |
| 无模板，从零开始 | Overleaf 官方模板 / TeX Live 基础模板 | 原型阶段 |

### 1.2 初始化项目

```bash
# 克隆总仓库
git clone https://github.com/your/LatexCenterTool.git
cd LatexCenterTool

# 同步所有子项目
./scripts/subtree-pull.sh

# 仅同步论文模板
./scripts/subtree-pull.sh thesis

# 查看当前 subtree HEAD（与上游同步状态）
cat context/repo-map.md
```

---

## 二、目录结构理解

### 2.1 标准论文模板结构

```
your-thesis/
├── main.tex              # 入口文件（低风险，仅填信息）
├── main.bbl              # 参考文献（由 biber 生成）
├── refs.bib              # 参考文献库（由 Zotero 导出）
├── chapters/             # 章节内容目录（低风险，专注于写内容）
│   ├── 01_abstract.tex
│   ├── 02_introduction.tex
│   ├── 03_related.tex
│   ├── 04_method.tex
│   ├── 05_experiment.tex
│   ├── 06_conclusion.tex
│   └── 99_appendix.tex
├── figures/              # 图表目录
│   ├── chapter_04/
│   └── chapter_05/
├── tables/               # 表格目录
├── utils/                # 工具宏包（可选）
│   ├── macros.tex
│   └── custom.sty
├── .vscode/              # VS Code LaTeX Workshop 配置（可选）
│   └── settings.json
└── Makefile              # 命令行编译脚本（可选）
```

### 2.2 文件风险等级

| 文件 | 风险 | 说明 |
|---|---|---|
| `*.cls` | **高风险** | 格式定义，修改可能导致整篇文档布局崩溃 |
| `main.tex` | 中风险 | 入口组装，修改前先备份 |
| `chapters/*.tex` | **低风险** | 内容文件，随便改，改完重新编译即可 |
| `refs.bib` | 低风险 | 文献库，由 Zotero 管理 |
| `figures/*` | 低风险 | 资源文件，不影响编译 |

> **核心原则**：改内容只在 `chapters/` 目录，改格式动 `*.cls`，动 `main.tex` 先备份。

---

## 三、编译配置

### 3.1 命令行完整编译链（必跑）

```bash
# 完整三步编译（xelatex → biber → xelatex*2）
xelatex main.tex
biber main
xelatex main.tex
xelatex main.tex

# 或者用 latexmk（自动检测依赖和循环次数）
latexmk -pdf main.tex

# 清理辅助文件（编译出 PDF 后执行）
latexmk -C main.tex
```

### 3.2 VS Code LaTeX Workshop 配置

在 `.vscode/settings.json`（项目级，非全局）：

```json
{
  "latex-workshop.latex.recipes": [
    {
      "name": "xelatex -> biber -> xelatex*2",
      "tools": ["xelatex", "biber", "xelatex", "xelatex"]
    }
  ],
  "latex-workshop.latex.tools": [
    {
      "name": "xelatex",
      "command": "xelatex",
      "args": ["-interaction=nonstopmode", "-synctex=1", "%DOC%"]
    },
    {
      "name": "biber",
      "command": "biber",
      "args": ["%DOC%" ]
    }
  ],
  "latex-workshop.latex.autoBuild.run": "never",
  "latex-workshop.view.pdf.internal.synctex.enabled": true
}
```

### 3.3 Overleaf 配置（无需本地）

1. 将 `main.tex` 和 `chapters/` 目录压缩为 zip
2. Overleaf → New Project → Upload zip
3. Overleaf 自动识别 `main.tex` 为入口
4. 多人协作：Settings → Compiler 选 `XeLaTeX`

---

## 四、常见修改场景

### 4.1 仅改内容（在 chapters/*.tex 中）

不碰任何格式定义，专注于写：

```latex
% main.tex 填信息（仅这些位置，不改其他）
\title{你的论文标题}
\author{你的姓名}
\studentid{你的学号}
\major{你的专业}
\advisor{你的导师}
\date{2026年5月}

% chapters/01_introduction.tex 写内容
\section{研究背景}
在这里写你的研究背景内容...

\input{chapters/02_related_work}
```

### 4.2 改格式（在 *.cls 中）

**修改前必做**：备份原文件（`thesis.cls` → `thesis.cls.bak`）

| 目标 | 搜索关键词 | 说明 |
|---|---|---|
| 标题样式 | `\chapter`、`\@makeschapterhead` | 控制章标题字号/加粗 |
| 行距 | `\linespread`、`\baselineskip` | 控制全文行距 |
| 页眉页脚 | `fancyhdr`、`\pagestyle` | 控制每页顶部和底部内容 |
| 正文字号 | `\zihao{-4}` | `-4`=小四，`5`=五号 |
| 段间距 | `\parindent`、`\parskip` | 控制段落缩进和间距 |
| 图表标题 | `\caption`、`\@captype` | 控制图表标题样式 |

### 4.3 增删章节

**增**：
1. 在 `chapters/` 创建新文件（如 `07_future.tex`）
2. 在 `main.tex` 的 `\mainmatter` 后加入：
   ```latex
   \input{chapters/07_future}
   ```

**删**：
注释掉对应 `\input{}` 行：
```latex
% \input{chapters/03_related}  % 已删除这章
```

### 4.4 替换参考文献样式

在 `main.tex` 导言区：

```latex
% GB/T 7714-2015（中文默认）
\usepackage[backend=biber, style=gb7714-2015]{biblatex}

% APA（外文期刊）
\usepackage[backend=biber, style=apa]{biblatex}

% IEEE（工科会议/期刊）
\usepackage[backend=biber, style=ieee]{biblatex}
```

### 4.5 添加常用宏包

在 `main.tex` 导言区（`\documentclass{...}` 之后，`\begin{document}` 之前）：

```latex
% 代码块
\usepackage{listings}
\lstset{basicstyle=\ttfamily, numbers=left}

% 交叉引用（推荐，比 \ref 更智能）
\usepackage{cleveref}
\crefname{figure}{图}{图}
\crefname{table}{表}{表}

% 流程图
\usepackage{tikz}
\usetikzlibrary{shapes.geometric, arrows}

% 数学定理环境
\usepackage{amsthm}
\newtheorem{definition}{定义}[chapter]
```

---

## 五、与 Claude Code / Codex 集成（逻辑中枢层）

Claude Code / Codex 是流水线中的逻辑执行者，但不应自由输出 Markdown 正文后人工粘贴到 `.tex` 文件。论文正文变更必须走 [[07_LaTeX结构化注入与双重审查Skill]]。

### 5.1 典型工作流

```
你：分析 chapter_03/ 中的所有 PDF++ 摘录，
    为『国内外研究现状』章节生成 500 字左右的结构化 LaTeX patch，
    引用 Zotero 中的 5 篇核心文献。
    patch 注入前必须执行 LaTeX 语法审查和论文审稿人审查。

Claude Code：
1. 读取 refs.bib，验证 cite key 存在
2. 读取 chapter_03/ 下的 Obsidian 笔记
3. 生成结构化 LaTeX patch
4. 执行 LaTeX 语法审查
5. 执行论文审稿人审查
6. 审查通过后注入 chapter_03/related_work.tex
7. 自动补全 \cite{} 引用，但不得编造 cite key
8. 编译验证
```

### 5.2 Claude Code 安全约束

```
约束 1：所有 \cite{} 必须来自 refs.bib（禁止凭空生成 key）
约束 2：禁止输出 Markdown 正文后人工粘贴到 .tex
约束 3：正文变更必须先生成结构化 LaTeX patch
约束 4：patch 注入前必须通过 LaTeX 语法审查与论文审稿人审查
约束 5：写入 .tex 后必须触发一次编译验证
约束 6：不修改 *.cls 文件（高风险）
约束 7：涉及 *.cls 修改需先备份，并告知风险
```

### 5.3 在 Claude Code 中调用编译

```bash
# 在项目目录启动 Claude Code
claude

# 写完内容后，Claude Code 执行：
!latexmk -pdf main.tex

# 若编译失败，Claude Code 读取错误信息，定位并修复
```

---

## 六、排错速查

| 错误信息 | 原因 | 解决 |
|---|---|---|
| `Undefined control sequence` | 拼写错误 / 未引入宏包 | 检查 `\usepackage{}` 是否包含该命令 |
| `File not found` | 文件路径错误 | 检查正斜杠 `/`，Windows 用反斜杠 `\` |
| `Missing $ inserted` | 数学公式未加 `$` 或 `$$` | 检查公式是否在 `$...$` 或 `equation` 环境中 |
| `Float specifier changed` | 图表位置参数错误 | 移除 `[h!]` 中的 `!` 或改为 `[htbp]` |
| `Undefined citation` | cite key 不存在 | 检查 `refs.bib` 中是否存在该 key |
| `Biber parallel error` | 多进程 biber 冲突 | 单次编译只用 `biber main`（不要并行） |
| `PDF file not found` | 辅助文件缺失 | 完整跑一次 `xelatex → biber → xelatex*2` |
| `Chinese font not found` | 系统缺少中文字体 | macOS 安装 `mactex-no-gui`；Linux 检查 `fc-list :lang=zh` |
| `Misplaced alignment tab character &` | 正文中的 `&` 未转义 | 普通正文写作 `\&`，表格环境中才保留 `&` |
| `Runaway argument` | `{}`、环境或命令参数未闭合 | 检查最近注入的 patch，确认括号与环境成对 |
| Markdown 标题出现在 PDF | AI 输出了 `#` 标题 | 回退该 patch，按 [[07_LaTeX结构化注入与双重审查Skill]] 重新生成 |

---

## 七、质量检查清单

- [ ] `main.tex` 中 `\title`、`\author`、`\date` 等基础信息已填
- [ ] 编译链完整（xelatex → biber → xelatex*2）
- [ ] `refs.bib` 与 Zotero 同步，cite key 可读
- [ ] `chapters/` 目录下的文件都已被 `\input{}` 引入
- [ ] VS Code LaTeX Workshop 配了 recipe 并测试通过
- [ ] PDF 输出正常，无乱码
- [ ] 目录、图表索引、参考文献页码正确
- [ ] AI 生成正文没有 Markdown 标题、列表、`**加粗**` 或代码围栏污染
- [ ] AI 生成正文以结构化 LaTeX patch 注入
- [ ] patch 注入前已通过 LaTeX 语法审查与论文审稿人审查

---

## 八、关联技能

| 关联 | 说明 |
|---|---|
| `13_LaTeX工作流入口` | 场景路由，判断是否需要走模板 |
| `14_LaTeX参考文献全生命周期` | .bib 管理与 LaTeX 集成 |
| `16_论文信息获取与处理全流程` | 学术文献检索 |
| `17_论文扩写与AI辅助指南` | AI 辅助写作 |
| `07_LaTeX结构化注入与双重审查Skill` | 结构化 patch、双重审查、注入与编译回归 |

---

*本文档对应的项目枢纽：`/Users/caolei/Desktop/LatexCenterTool`*
