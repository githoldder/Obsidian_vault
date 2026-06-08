# LaTeX 参考文献全生命周期管理

> **定位**：参考文献从 Zotero 采集到 LaTeX 渲染的端到端闭环管理指南。
> 覆盖：Zotero 配置 → BibTeX 导出 → LaTeX 集成 → 自动化流水线。
> 是"最强半自动化工作流"的**数据基座层**，承接 Zotero 与 LaTeX 之间的所有数据转换。

---

## 一、核心架构：四阶段闭环

```
┌──────────────────────────────────────────────────────────────────┐
│                   参考文献全生命周期四阶段                         │
│                                                                  │
│  采集           管理           导出            渲染             │
│  (Zotero)      (Zotero)      (Better BibTeX)   (LaTeX)          │
│                                                                  │
│  ┌──────┐     ┌────────┐   ┌───────────┐   ┌──────────────┐     │
│  │抓取  │────→│元数据  │────→│.bib文件  │────→│\cite{}嵌入  │     │
│  │PDF   │     │标引    │     │自动导出  │     │\printbibliog │     │
│  │网页  │     │标签    │     │唯一Key   │     │rhy           │     │
│  └──────┘     └────────┘   └───────────┘   └──────────────┘     │
│                                                                  │
│  关键约束：.bib 文件是唯一事实来源，Claude Code 禁止凭空生成 Key   │
└──────────────────────────────────────────────────────────────────┘
```

---

## 二、阶段 1：采集（Zotero 作为文献基座）

### 2.1 安装与配置

| 步骤 | 操作 |
|---|---|
| 1 | 下载 [Zotero 7+](https://www.zotero.org/download) |
| 2 | 安装浏览器连接器（Zotero Connector） |
| 3 | 安装 [Better BibTeX (BBT)](https://ret.revolutionanalytics.com/anomalies/) 插件 |
| 4 | 注册 Zotero 账号，开启 Zotero FS 自动同步 |

### 2.2 采集来源

| 来源 | 方法 |
|---|---|
| **学术数据库**（Google Scholar、CNKI、Web of Science） | 浏览器 Connector 一键抓取，自动抓取 metadata |
| **PDF** | 直接拖入 Zotero，或用 Connector 打开 PDF 页面抓取 |
| **网页** | Connector 抓取，带自动摘要 |
| **命令行** | `curl` 调用 Zotero API（需要 API Key）|

### 2.3 必做配置：生成唯一 cite key

BBT 配置（Zotero → 偏好设置 → Better BibTeX）：

```
导出格式：\cite{author}{year}{shortTitle}
例如：zhang2024deep、li2023transformer
```

**禁止使用默认生成的 key（如 `QXV0aG9yMjAyNA`）**，因为无法在 LaTeX 中手写引用。

### 2.4 组织策略

| 方法 | 适用场景 | 说明 |
|---|---|---|
| **标签（Tags）** | 按研究主题分类 | 推荐，Obsidian 可读取 |
| **集合（Collections）** | 按项目/论文分类 | Zotero 原生，适合一棵树上分类 |
| **两者结合** | 复杂研究 | Tags 管主题，Collections 管项目 |

---

## 三、阶段 2：管理（Zotero 内部组织）

### 3.1 元数据补全 SOP

每条文献入库后必须检查：

```
1. 作者名：是否正确分词（First Last vs Last, First）
2. 年份：是否正确
3. 期刊/会议：全称还是缩写（根据目标期刊要求）
4. DOI / URL：是否完整
5. 标签：是否标注研究主题
```

### 3.2 自动化抓取 DOI

安装 [Zotero DOI Manager](https://github.com/bwiernik/zotero-doi-tools) 插件，可批量补全 DOI，提升 .bib 导出质量。

### 3.3 与 Obsidian 联动

通过 [ZotLit 插件](https://obsidian.md/plugins)（或其他 Zotero-Obsidian 桥接插件）：

- 在 Obsidian 中嵌入 Zotero 文献卡片
- 点击跳转到 Zotero 原文件
- PDF++ 读取时自动关联 Zotero metadata

---

## 四、阶段 3：导出（Better BibTeX 自动导出）

### 4.1 导出配置

Zotero → 文件 → 导出库 → 选择 **Better BibTeX**

| 设置项 | 推荐值 | 说明 |
|---|---|---|
| 格式 | BibTeX | LaTeX 原生支持 |
| 编码 | UTF-8 | 默认，无需改动 |
| 字段 | 仅保留 LaTeX 需要的字段 | 减少冗余 |
| `{{citation}}` | `\cite{author}{year}{Title}` | 自动生成 cite key |

### 4.2 持续同步方案

| 方案 | 适用场景 | 说明 |
|---|---|---|
| **手动导出** | 单篇论文，偶尔更新 | Zotero → 导出 → 覆盖本地 `.bib` |
| **BBT 自动导出** | 高频更新项目 | BBT 实时监控，文件变更时自动写 `.bib` |
| **Git 管理** | 版本控制需求 | `.bib` 加入 Git，BBT 导出到项目目录 |
| **Symbolic Link** | 多项目复用同一文献库 | `ln -s ~/Zotero/bibtex/my_library.bib ./refs.bib` |

### 4.3 多语言处理

| 语言 | 处理方式 |
|---|---|
| 中文文献 | BBT 配置 `use JabRef formatting`；或手动处理 `author = {张伟 and 李娜}` |
| 日语文献 | 同上，BBT 会处理 |
| 西文文献 | 默认 UTF-8，无需特殊处理 |

---

## 五、阶段 4：渲染（LaTeX 集成）

### 5.1 基础引用方式

```latex
% 导言区
\usepackage[backend=biber, style=gb7714-2015]{biblatex}
\addbibresource{refs.bib}  % 相对路径，从 main.tex 同级目录

% 正文
这篇论文~\cite{zhang2024deep} 提出了...

% 参考文献输出
\printbibliography[heading=bibintoc]  % 带目录入口
```

### 5.2 编译链（必须按顺序执行）

```bash
xelatex main.tex       # 第一次编译，生成 .bbl
biber main             # 处理参考文献
xelatex main.tex       # 第二次编译，引用正确
xelatex main.tex       # 第三次编译，目录/交叉引用稳定
```

> **警告**：只跑 `xelatex` 不跑 `biber`，参考文献会是空的。

### 5.3 常见引用格式切换

| 格式 | 导言区设置 |
|---|---|
| GB/T 7714-2015（中文期刊/毕设） | `[backend=biber, style=gb7714-2015]` |
| APA（外文期刊） | `[backend=biber, style=apa]` |
| IEEE（工科） | `[backend=biber, style=ieee]` |
| Author-Year | `[backend=biber, style=authoryear]` |

### 5.4 文本编辑器集成

**VS Code (LaTeX Workshop)**:
```json
"latex-workshop.latex.recipes": [
  {
    "name": "xelatex -> biber -> xelatex*2",
    "tools": ["xelatex", "biber", "xelatex", "xelatex"]
  }
]
```

**Texifier**: 自动监听文件变更，`.bib` 更新后自动重绘 PDF。

---

## 六、命令行工具（高级用户）

### 6.1 Zotero API 批量导出

```bash
# 需要 API Key，存到环境变量
export ZOTERO_API_KEY="your_key_here"
export ZOTERO_USER_ID="your_user_id"

# 导出整个库
curl -u "$ZOTERO_API_KEY" \
  "https://api.zotero.org/users/$ZOTERO_USER_ID/items?format=bibtex" \
  > refs.bib
```

### 6.2 自动同步脚本（git pre-commit hook）

在项目目录 `.git/hooks/pre-commit`：

```bash
#!/bin/sh
# 自动在编译前检查 .bib 文件是否有未提交的 Zotero 更新
# （需要 BBT 实时导出配置）
git add refs.bib
```

### 6.3 文献清理脚本（Node.js）

```javascript
// scripts/clean-bib.js
// 移除 .bib 中 LaTeX 不需要的字段，压缩文件体积
const fs = require('fs');
const input = fs.readFileSync('refs.bib', 'utf8');
const cleaned = input.replace(/^\s*(abstract|keywords|annote)\s*=/gim, '');
fs.writeFileSync('refs.bib', cleaned);
console.log('Cleaned .bib file');
```

---

## 七、排错速查

| 错误 | 原因 | 解决 |
|---|---|---|
| `Citation 'xxx' undefined` | `.bib` 文件未加载 | 检查 `\addbibresource{refs.bib}` 路径 |
| `[Biber] Warning: Duplicate entry` | 同一 key 在 .bib 中出现多次 | 用 BBT 重新生成，确保 key 唯一 |
| 参考文献顺序是字母序而非插入序 | 用了 `bibliography` 而非 `biblatex` | 切换到 `biblatex` 方案 |
| 编译后参考文献是空的 | 未跑 `biber` | 确保编译链包含 `biber main` |
| `Undefined control sequence` | cite key 拼写错误 | 检查 `.bib` 中对应 key 的大小写 |
| 中文文献作者显示异常 | 作者字段格式不对 | BBT 导出设置选 `JabRef formatting` |

---

## 八、质量检查清单

- [ ] 每篇文献有唯一可读的 cite key（如 `zhang2024deep`，不是 `QXV0aG9y`)
- [ ] `.bib` 文件已加入 Git 版本控制
- [ ] 导言区 `\addbibresource{refs.bib}` 路径正确
- [ ] 编译链包含 `biber` 步骤
- [ ] 所有 `\cite{}` 都可以在 Zotero 中找到对应条目
- [ ] 中文文献作者格式符合 GB/T 7714-2015（`张伟 and 李娜`）

---

## 九、关联技能

| 关联 | 说明 |
|---|---|
| `13_LaTeX工作流入口` | 场景路由，入口判定 |
| `15_LaTeX模板适配与工具配置` | 具体模板配置 |
| `16_论文信息获取与处理全流程` | 学术文献检索、筛选 |
| `17_论文扩写与AI辅助指南` | AI 辅助写作，引用验证 |

---

*本文档对应的项目枢纽：`/Users/caolei/Desktop/LatexCenterTool`*