本项目旨在为 Agent 建立一套“复杂度解耦、轻量化同步、自动化校验”的工作区规范与治理系统。本规范整合了成熟项目 LingoBridge 的优秀实践（如 `Agent.md` 入口与双板 PRD 机制），并优化了执行边界与自动化成本。

---

## 一、 系统三层架构设计

为了兼顾“低复杂度临时任务”的高效清爽与“长期工程项目”的强治理和可追踪性，系统划分为三层：

### 1. 第一层：轻任务 IPO (低复杂度)

针对单次、临时、简单脚本或单次报告编写任务，不强制在根目录创建复杂的系统文件，只需按需创建局部 IPO 结构：

- `01-resource/`：原始输入数据、文档、参考资料。
- `02-process/`：中间处理过程（子目录包括 `prompt/`, `data/`, `script/`, `figure/`, `document/`）。
- `03-report/`：最终可交付无 Markdown 语法的纯文本 `.txt`（或渲染后的 `.pdf`/`.docx`）。
- **Git 规则**：不强制进行 Commit，完成交付即可。

### 2. 第二层：长期项目 Agent 配置 (高复杂度)

针对需要长期维护、跨 Agent 协作且具有工程化特征的项目。在根目录下配置标准的 Agent 操作系统级文件：

text

project-root/

├── Agent.md                    # ⚡ Agent 第一入口（状态指示牌、禁止碰区、提效路径）

├── README.md                   # 面向人类的项目全局介绍与目录树

├── .agent/

│   ├── rules/                  # 项目持久化规则（如命名规范、特定架构规则）

│   ├── workflows/              # 复用工作流（如编译部署步骤、自动化迁移脚本）

│   └── skills/                 # 专属工具包与脚本

├── context/

│   ├── project-brief.md        # 项目简介（产品定位、核心闭环、非目标）

│   ├── directory-map.md        # 目录地图（详细声明各目录角色，防止文件乱放）

│   ├── context.txt             # 短期记忆 (ST-Memory，仅记录决策、状态变更与关键命令结果)

│   └── memory.md               # 长期记忆 (LT-Memory，关键决策归档，保留最新活跃上下文)

├── prds/

│   ├── README.md               # PRD 同步协议规范

│   ├── md/                     # 人类评审视图 (Objectives, KR Milestones, Tasks)

│   └── json/                   # ⚡ 机器执行单一事实源 (Task Steps, inputs, tools, status)

├── docs/                       # 存放工程规格书、技术规范等

├── drafts/                     # 用户的 Prompt 草稿与临时想法

├── prompts/                    # 发送给子 Agent 的指令模板

├── templates/                  # 复用任务或报告格式的模板

├── output/                     # 编译生成物与阶段产出

└── scripts/ & tests/           # 脚本与测试包

### 3. 第三层：自动化守卫 (校验机制)

引入自动化脚本与契约，不再依赖 Agent 的“口头承诺”：

- **交付物格式校验器**：校验 `03-report/`（或 `output/`）中最终交付的 `.txt` 是否含有 Markdown 标记。
- **PRD 双板同步守卫**：在 JSON 修改后，通过自动化脚本或严格的同步规则生成 MD 摘要。
- **Git 脏工作区保护**：提交前自动比对 `git status`，杜绝将人类或其他 Agent 留下的脏改动误提交。

---

## 二、 核心机制细节

### 1. 交付物格式契约 (Anti-MD 精细匹配)

- **非强制范围**：`README.md`、`prds/md/`、`context/` 等文档和代码注释**允许且推荐**使用 Markdown 以获得良好的可读性。
- **精细匹配规则**：在 `03-report/*.txt` 中，Agent 的校验逻辑将仅对**行首或结构级**的 MD 标记进行打回重写：
    - 行首 `##` 等标题符号
    - 行首 `-` 或 `*` 无序列表符号
    - 行首数字加点加空格 `1.` 有序列表符号
    - 成对的加粗符号 `**文本**`
    - 反引号代码块 ` ``` `
- **文本布局**：默认以纯自然段（使用换行与首行缩进）排版，保证可以直接复制进 Word。

### 2. 智能化短/长期记忆管理

为了降低 Token 消耗并保持现场：

- `context/context.txt` **不记录**：无信息量的成功命令（如 `ls`、`pwd`）、常规代码读取操作。
- `context/context.txt` **只记录**：关键技术决策、遇到并解决的 Issue 节点、用户偏好调整、阻塞点。
- **阶段触发压缩**：
    - 当 `context.txt` 接近 1000 行，或者一个 Sprint 阶段结束、关键目标达成时进行压缩。
    - 压缩机制：把过期或冗余信息提炼为“历史档案”存入 `context/memory.md`，并在清空 `context.txt` 后**保留最近 20 到 50 行**当前活跃的工作上下文，确保工作现场不丢失。

### 3. PRD 双板单事实源 (Single Source of Truth)

- **以 JSON 为主，MD 为辅**：`prds/json/` 是 Agent 执行的**唯一事实源 (Source of Truth)**。包含任务状态 (`todo`, `in_progress`, `done`)。
- **同步流程**：
    1. 人类修改 `prds/md/` 的需求时，Agent 将其解析并转化/同步至 `prds/json/`。
    2. Agent 在执行过程中**只修改 JSON 中的状态**，执行完毕后再用脚本或规则，将 JSON 的状态摘要回写、同步到 `prds/md/`（如更新 KR 完成情况）。

### 4. Git 工作流与脏工作区防护

1. **提交范围隔离**：在准备 commit 之前，必须执行 `git status`，输出变更归属说明。禁止使用 `git add .` 一键 stage，必须精确 `git add <file>`，避免污染工作区中用户已修改的文件。
2. **Commit 消息规范**：消息中必须带有 Task ID（如 `docs: complete S01-T02 requirement template`）。
3. **禁止自动 Push**：本地 commits 仅做留痕审计，待 Sprint 结束、由人类 Walkthrough 审计通过后，手动执行 Push。

---

## 三、 执行与落地规划

### Step 1: 创建基础模板与校验脚本

在 `scripts/validate_report.py` 中实现 Anti-MD 的检测机制，用于持续集成或交付前的门禁检测。

### Step 2: 建立初始化工作流 (Workflow)

在 `.agent/workflows/` 下编写初始化脚本，使得面对新项目时，只需一条命令即可自动映射轻量 IPO 结构或生成高复杂度的 `Agent.md` 与 `context/` 框架。