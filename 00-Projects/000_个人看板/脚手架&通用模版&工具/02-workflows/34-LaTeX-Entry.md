# LaTeX 工作流入口

> **定位**：学术写作生产线的场景路由与认知框架入口。
> 不再是安装向导，而是判断"我要做什么场景，该走哪条路"的决策中心。
> 基于 2026-05-10 日记中验证的**最强半自动化工作流**：Zotero(文献基座) → Obsidian/PDF++(知识解构) → Claude Code(逻辑中枢) → LaTeX(精密渲染)。

---

## 一、场景判定（必读）

回答两个问题，快速定位路径：

**问题 1：我是新手还是已有环境？**

| 选择 | 走 |
|---|---|
| 第一次接触 LaTeX，且免费本地使用 | → 路径 A |
| 第一次接触 LaTeX，且多人协作 / 不要本地环境 | → 路径 B |
| 已有 TeX 环境，需要模板和写作流程 | → 路径 C |
| 常工院毕业设计 | → 路径 D |
| 我有论文要写，需要完整流水线 | → 路径 E（全流程自动化） |

**问题 2：我是否有现成的 LaTeX 项目仓库？**

- 有 → 跳到 [二-2 已有环境](#二已有环境快速上手)
- 无，但需要论文/毕设 → 路径 C + 参考 `LatexCenterTool` 克隆模板
- 无，只想了解概念 → 继续看下面的流水线框架

---

## 二、完整流水线框架（核心认知）

### 2.1 四工具角色与数据流向

```
┌─────────────────────────────────────────────────────────────────┐
│                     学术写作全自动化流水线                         │
│                                                                 │
│  Zotero          Obsidian + PDF++     Claude Code         LaTeX  │
│  (文献基座)        (知识解构)           (逻辑中枢)          (精密渲染)  │
│                                                                 │
│  ┌──────┐       ┌──────────────┐    ┌──────────┐    ┌─────────┐ │
│  │抓取   │───→  │深度精读      │───→│逻辑执行   │───→│编译输出  │ │
│  │管理   │       │原子化摘录    │    │跨文件关联 │    │PDF渲染   │ │
│  │同步   │       │Canvas可视化  │    │自动修补  │    │实时预览  │ │
│  └──────┘       └──────────────┘    └──────────┘    └─────────┘ │
│      │                  │                  │               │    │
│      ↓                  ↓                  ↓               ↓    │
│   .bib file      Markdown笔记        .tex文件          PDF输出  │
│   metadata       block-link         结构化写作         最终成品  │
└─────────────────────────────────────────────────────────────────┘
```

| 工具 | 职责 | 在流水线中的位置 |
|---|---|---|
| **Zotero** | 文献元数据抓取、BibTeX 管理、多端同步 | 数据入口，文献基座 |
| **Obsidian + PDF++** | 深度精读、原子化摘录、block-link 回溯、Canvas 可视化 | 知识解构层 |
| **Claude Code** | 跨文件关联分析、自动 LaTeX 修补、数据清洗、文献综述 | 逻辑执行层（核心枢纽） |
| **LaTeX (Texifier / TeX Live)** | 公式渲染、图表引用、PDF 输出 | 精密渲染层 |

### 2.2 工具链配置推荐

| 场景 | 推荐工具链 | 说明 |
|---|---|---|
| 学术论文 / 毕业设计 | Zotero + Obsidian + Claude Code + **Texifier** | 实时预览，闭环最紧密 |
| 项目文档 / 手册 | Obsidian + Claude Code + TeX Live (命令行) | 无需 Texifier，本地批量编译 |
| 课设 PPT / Banner | Beamer + Texifier / Overleaf | 视觉化优先，协作方便 |

### 2.3 关键约束（必知）

1. **Zotero 的 `.bib` 文件是唯一事实来源**：Claude Code 只许引用其中的 Key，禁止凭空生成 `\cite{}`
2. **Obsidian 标注是 Markdown**：天然可以被 Claude Code 索引和理解
3. **上下文分段**：如果文献量极大（>50篇），先用 Obsidian 标签筛选核心文献再让 Claude 介入
4. **幻觉检查**：Claude 生成的参考文献必须回查 Zotero 验证
5. **正文注入禁止 Markdown**：进入 `.tex` 的论文正文必须通过 [[36-LaTeX-Injection-Review-Skill]]，禁止直接复制 AI 输出的 Markdown 自由文本

---

## 三、路径执行

### 路径 A：新手本地化

**适用**：第一次用，免费，不依赖任何云服务。

1. **安装 TeX Live**
   - macOS: `brew install --cask mactex-no-gui`
   - Windows: 官网下载 `install-tl-windows.exe`，完整安装
   - Ubuntu: `sudo apt install texlive-full`

2. **安装 VS Code + LaTeX Workshop**
   - VS Code 扩展市场搜索 `LaTeX Workshop`
   - 配置 recipes：`xelatex → bibtex → xelatex*2`
   - 关闭自动编译：`"latex-workshop.latex.autoBuild.run": "never"`
   - 快捷键：绑 `Cmd+S` 为 `latex-workshop.build`

3. **跑通最小示例**
   ```latex
   \documentclass{article}
   \usepackage[UTF8]{ctex}
   \begin{document}
   你好，LaTeX！
   \end{document}
   ```

### 路径 B：Overleaf 云端（无需安装）

**适用**：不想装本地环境，多人协作。

1. 注册 [Overleaf](https://www.overleaf.com)
2. 创建 `Blank Project` 或上传模板 zip
3. 编译：`Cmd+Enter`
4. 支持中文：导言区加 `\usepackage[UTF8]{ctex}`
5. 注意选择编译配方建议手动调整成`xelatex → bibtex → xelatex*2`

### 路径 C：本地模板实践（推荐做学术论文的人）

**适用**：已有 TeX 环境，需要模板开始写。

1. **克隆 LatexCenterTool**（项目枢纽）
   ```bash
   git clone <LatexCenterTool-url>
   cd LatexCenterTool
   ./scripts/subtree-pull.sh  # 同步所有子项目
   ```
2. **选择场景对应模板**

| 场景              | 路径                                                                            |
| --------------- | ----------------------------------------------------------------------------- |
| 学术论文 / 毕业设计     | [常工院毕业设计模版](https://github.com/githoldder/cit-scie-thesis-latex)              |
| 项目文档 / 手册       | [竞赛项目/文档智能助手协助手册](https://github.com/githoldder/thuthesis-manual-writing-kit) |
| 课设 PPT / Banner | [课程设计/毕业设计演示ppt](https://github.com/githoldder/czu-beamer-template)           |
3. **编译链**：`xelatex main.tex → bibtex main → xelatex*2`

### 路径 D：常工院毕设

1. 获取模板：`git clone` 常工院毕设模板仓库
2. `main.tex` 填基本信息：`\title{}`, `\author{}`, `\studentid{}`, `\major{}`, `\advisor{}`, `\date{}`
3. `chapters/` 写内容并用 `\input` 引入
4. 本地 `xelatex` 或 Overleaf 编译

### 路径 E：全流程自动化（面向学术论文写作者）

**这是最强半自动化工作流**，需要配置完整工具链：

1. **Zotero 配置**（文献基座）
   - 安装 [ZotLit](https://obsidian.md/plugins) 或 [Better BibTeX](https://ret.revolutionanalytics.com/anomalies/) 插件
   - 导出 `.bib` 文件到项目目录
   - 确保每篇文献有唯一 cite key（如 `zhang2024deep`）

2. **Obsidian + PDF++ 配置**（知识解构）
   - 用 PDF++ 打开 Zotero 抓取的 PDF 进行精读
   - 高亮关键段落，生成 block-link
   - 在 Obsidian 中创建 MOC（内容地图）串联相关文献

3. **Claude Code 配置**（逻辑中枢）
   - 在项目目录运行 `claude` 启动 Claude Code
   - 关键指令示例：
```

     分析 Resources/ 文件夹下关于『Transformer 架构』的所有摘录，
     生成一个结构化 LaTeX patch，目标文件为chapter_related_work.tex，
     operation 为 insert_after，anchor 为指定小节标题。
     patch 必须经过 LaTeX 语法审查和论文审稿人审查后再注入。

   - 写完自动读取 `.bib` 确保 `\cite{}` Key 有效
   - 禁止让 Claude Code 直接输出 Markdown 正文再人工粘贴到 `.tex`

```
4. **LaTeX 编译**（精密渲染）
   - 本地：Texifier/vscode 实时预览 或 命令行 `latexmk -pdf`
   - 多人：Overleaf 上传 `.bib` + `.tex`

---

## 四、质量检查清单

在提交最终 PDF 前，检查以下内容：

- [ ] `.bib` 文件中没有凭空生成的 cite key
- [ ] 所有 `\cite{}` 都可以在 Zotero 中找到对应条目
- [ ] 公式、图表编号交叉引用正确
- [ ] Claude Code 修改过的 `.tex` 文件经过本地编译验证
- [ ] 所有 AI 生成正文均以结构化 LaTeX patch 进入 `.tex`
- [ ] patch 注入前已通过 LaTeX 语法审查和论文审稿人审查
- [ ] 目录、页眉、页脚符合对应模板要求

---

## 五、关联技能

| 关联 | 说明 |
|---|---|
| `14_LaTeX参考文献全生命周期` | 参考文献从 Zotero 到 LaTeX 的完整链路 |
| `15_LaTeX模板适配与工具配置` | 具体模板的配置、编译和排错 |
| `16_论文信息获取与处理全流程` | 学术文献检索、筛选、截取、整合方法论 |
| `17_论文扩写与AI辅助指南` | AI 辅助扩写，避免 AI 味和质量稀释 |
| `07_LaTeX结构化注入与双重审查Skill` | 防止 Markdown 污染 LaTeX，使用 patch + 双重审查 + 编译回归 |

---

