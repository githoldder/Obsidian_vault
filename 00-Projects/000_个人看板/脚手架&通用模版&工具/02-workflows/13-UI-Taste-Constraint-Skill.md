# AI 前端 UI Taste 约束生成 Skill

---

## 1. Skill 定位

本 Skill 用于在 AI 写前端代码、Figma 页面、产品原型或 UI 改版前，先把“审美判断”转化成可执行的设计约束。

核心原则：

> Taste 不是审美玄学，而是稳定判断什么该删、什么该弱化、什么该突出、什么不该用模板解决。

本 Skill 不负责收集参考图，也不负责最终审查。相关 Skill：

- 参考来源：[[12-UI-UX-Reference-List]]
- 生成后审查：[[15-UI-Audit-Anti-Template-Skill]]

---

## 2. 触发场景

当出现以下任务时使用：

1. 让 AI 生成前端页面、后台系统、工作台、仪表盘、Web App、移动端界面。
2. 用户说“不要 AI 味”“不要模板感”“要高级一点”“有 taste 一点”。
3. 项目容易被 AI 生成成卡片网格、渐变 hero、大图标功能区。
4. 需要为项目建立 `design.md`、视觉规则、UI prompt、Figma 设计约束。
5. 需要把产品气质、组件禁用清单、布局偏好写进 `.agent` 或 PRD。

---

## 3. 输入

至少收集以下信息：

| 输入项 | 说明 |
|---|---|
| 产品类型 | 后台管理、开发者工具、学习系统、研究工作台、消费 App、文化科技产品等 |
| 用户场景 | 用户在什么压力、频率和任务中使用 |
| 核心任务 | 用户需要理解、比较、决定或操作什么 |
| 内容密度 | 信息型、工作台型、营销型、阅读型、消费型 |
| 禁忌风格 | 不想像什么，例如 SaaS 模板、Dribbble 假内容、启动页、玻璃拟态 |
| 可参考产品 | Linear、GitHub、Vercel、Stripe、Raycast、Notion、Apple Settings 等 |

---

## 4. 输出

每次执行本 Skill，先输出一份 UI Taste Brief，再允许进入实现。

```md
# UI Taste Brief

## 1. 产品气质
- 应该感觉像：
- 不应该感觉像：

## 2. 用户任务
- 用户主要要理解：
- 用户主要要比较：
- 用户主要要决定：
- 用户主要要操作：

## 3. 视觉取舍
- 需要突出：
- 需要弱化：
- 需要隐藏：
- 需要删除：

## 4. 布局模型
- 首选布局：
- 禁用布局：
- 信息默认可见：
- 信息折叠或进入详情：

## 5. 组件规则
- 允许组件：
- 禁用组件：
- 图标使用规则：
- 色彩使用规则：

## 6. 参考与反例
- 参考原则：
- 反例警戒：

## 7. 实现前硬约束
- Rule 1：
- Rule 2：
- Rule 3：
```

---

## 5. Taste 决策流程

### Step 1：从“好看”改成“产品气质”

禁止使用：

```text
Make it modern and beautiful.
```

改成：

```text
Design the interface with a restrained, product-grade visual language.
It should feel like a serious tool used daily by professionals, not a marketing landing page.

The visual personality should be:
- quiet
- precise
- structured
- information-first
- low-decoration
- high-trust

Avoid visual excitement unless it supports user understanding.
```

### Step 2：先做风格取舍，再写代码

在任何 UI 实现前，先要求 AI 回答：

```text
Before writing code, define the visual taste of this interface.

Give me:
1. What this UI should feel like.
2. What it should deliberately avoid.
3. What visual elements are allowed.
4. What visual elements are forbidden.
5. How typography, spacing, color, and density should behave.
6. Which existing products are useful references, and which references should be avoided.

Only after that, implement the UI.
```

### Step 3：用反例约束 AI

```text
Do not make the interface look like:
- a generic AI-generated SaaS dashboard
- a Tailwind UI template
- a startup landing page
- a Dribbble shot with fake content
- a feature-card grid with big colorful icons
- a glassmorphism demo
- a component library showcase

Make it feel closer to:
- Linear
- GitHub
- Vercel Dashboard
- Stripe Dashboard
- Raycast
- Apple System Settings
- Notion database views

Use these references as design principles, not as visual copies.
```

### Step 4：从装饰型 UI 转成信息型 UI

```text
This UI must be content-first and information-dense.

Do not use decorative blocks to fill space.
Every section must contain realistic, useful content:
- actual records
- statuses
- timestamps
- metadata
- actions
- progress
- constraints
- warnings
- comparisons
- relationships

Visual hierarchy should come from information structure, not from large icons or colorful cards.
```

### Step 5：定义布局，不让 AI 默认九宫格

```text
Avoid card-grid layout as the default.

Explore a more mature layout:
- left navigation
- central working area
- right-side inspector
- top command bar
- bottom activity log
- split-pane preview
- table + detail drawer
- timeline + metadata panel

The layout should support workflow, not just display features.
```

---

## 6. 长期组件边界

### 禁用组件

- 大号图标功能卡片
- 彩色浅底图标块
- 居中式 feature cards
- 渐变 hero
- 玻璃拟态面板
- 悬浮抽象光斑
- trophy / flame / rocket / sparkle 等通用兴奋图标
- 过度阴影
- 过度圆角
- 空洞营销留白
- 重复三列功能网格
- 没有上下文的假指标
- 只为了填空间存在的装饰区块

### 允许组件

- 紧凑表格
- Split panes
- Sidebar
- Command palette
- Breadcrumbs
- Status badges
- Metadata rows
- Timeline logs
- File trees
- Property panels
- Inline actions
- Diff views
- Code/data preview
- Detail drawer
- Small utility icons

---

## 7. 色彩规则

```text
Create a restrained visual system.

Color rules:
- Use one primary color only for primary actions and active states.
- Use neutral grays for structure.
- Use semantic colors only for status: success, warning, error.
- Do not use rainbow gradients.
- Do not assign a different pastel color to every card.
- Color should communicate meaning, not decoration.
```

---

## 8. 常用 Taste Profiles

### 8.1 Developer Tool Taste

适合：文件转换系统、AI Agent 工作台、云盘、代码平台。

```text
Design with a developer-tool taste:
compact, precise, keyboard-friendly, log-aware, status-driven, and low-decoration.
Use tables, file trees, command bars, split panes, diff views, logs, and inspectors.
Avoid marketing-style cards and decorative icons.
```

### 8.2 Research Workspace Taste

适合：论文阅读、Zotero + Obsidian、知识库、文献管理。

```text
Design with a research-workspace taste:
calm, text-first, citation-aware, note-oriented, and optimized for deep reading.
Use document panes, annotations, references, backlinks, metadata, reading progress, and excerpt cards.
Avoid colorful dashboards, gamified icons, and startup-style visuals.
```

### 8.3 Enterprise System Taste

适合：后台管理、学生系统、审批系统、运营系统。

```text
Design with an enterprise-system taste:
stable, trustworthy, structured, and operational.
Use dense tables, filters, status tags, audit logs, permission panels, and clear form layouts.
Avoid playful illustrations, oversized cards, and decorative gradients.
```

### 8.4 Premium Consumer App Taste

适合：学习类、语言类、普通用户工具。

```text
Design with a premium consumer-app taste:
warm, focused, polished, and emotionally restrained.
Use elegant typography, soft but disciplined spacing, meaningful microcopy, and subtle motion.
Avoid childish gamification, random badges, and exaggerated achievement icons.
```

### 8.5 Cultural-Tech Taste

适合：LingoBridge、中哈俄桥梁、文化语言项目。

```text
Design with a cultural-tech taste:
modern but not cold, international but not generic, culturally grounded but not decorative.
Use restrained symbolic elements, subtle geographic or linguistic references, editorial layouts, and serious service-oriented structure.
Avoid tourist-poster aesthetics, flag-color overload, and generic translation-app visuals.
```

---

## 9. 可复用总提示词

```text
You are designing a serious, product-grade interface, not a generic AI-generated SaaS template.

Before implementation, define the design taste:
- What should this product feel like?
- What visual patterns must be avoided?
- What layout model best supports the user's workflow?
- What information should be visible by default?
- What should be hidden, compressed, or removed?

Design principles:
1. Content first, decoration last.
2. Typography, spacing, alignment, and information hierarchy should carry the interface.
3. Icons are allowed only as small utility indicators, never as large decorative blocks.
4. Color must communicate state, priority, or action. Do not use random pastel colors.
5. Avoid large rounded icon tiles, gradient blobs, glassmorphism, generic feature-card grids, trophy/flame/rocket/sparkle icons, and excessive shadows.
6. The UI should feel closer to Linear, GitHub, Vercel Dashboard, Stripe Dashboard, Raycast, Apple Settings, or Notion database views.
7. Use realistic data, real states, metadata, timestamps, logs, statuses, and contextual actions.
8. Every visual element must help the user understand, compare, decide, or act. Otherwise remove it.

Preferred layout:
Use a workflow-oriented structure such as sidebar + main workspace + inspector panel, table + detail drawer, split preview, timeline log, or command palette. Avoid simple card grids unless the content truly requires them.

After generating the first version, perform a taste review:
- remove decorative elements
- reduce visual noise
- improve density
- replace fake content with realistic content
- make the hierarchy clearer
- ensure the UI feels like a real product used every day
```

---

## 10. 执行硬规则

1. 不允许只说“现代化、美观、高级”，必须翻译成产品气质、禁用组件、布局模型和删减标准。
2. 先输出 UI Taste Brief，再实现 UI。
3. 如果页面是工具型产品，默认优先 `Sidebar + Workbench + Inspector`，不是卡片网格。
4. 如果视觉元素不能帮助用户理解、比较、决定或行动，默认删除。
5. 参考产品只能作为原则，不允许照抄视觉外观。
