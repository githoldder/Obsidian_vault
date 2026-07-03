# Claude Code 内部智能体编排架构深度剖析

> **事件背景**: 2026年3月31日，Anthropic 在发布 Claude Code v2.1.88 到 npm 时，因 `.npmignore` 配置遗漏，将一个 59.8MB 的 source map 文件（`.map`）随包发布。该文件指向托管在公开 Cloudflare R2 存储桶上的 `src.zip`，导致 512,000+ 行 TypeScript 源码（1,906 个文件）泄露。本文仅分析公开的架构设计信息，不涉及源码。

---

## 一、整体架构概览

Claude Code 并非简单的 LLM Chat Wrapper，而是一套 **插件化智能体操作系统**。其核心架构可分为五层：

```
┌─────────────────────────────────────────────────┐
│              用户交互层 (Terminal UI)              │
│         Ink-based TUI + 游戏级渲染优化器           │
├─────────────────────────────────────────────────┤
│              编排引擎层 (Query Engine)              │
│      ~46,000 行 | 多智能体协调 | 流式响应处理        │
├─────────────────────────────────────────────────┤
│              工具系统层 (Tool System)               │
│      ~40 个工具 | ~29,000 行 | 权限门控机制         │
├─────────────────────────────────────────────────┤
│              记忆架构层 (Memory Architecture)       │
│      三层记忆 | 自愈记忆 | 严格写入纪律             │
├─────────────────────────────────────────────────┤
│              安全与防护层 (Security Layer)          │
│    23 项 Bash 安全检查 | 客户端证明 | 反蒸馏机制     │
└─────────────────────────────────────────────────┘
```

---

## 二、工具系统（Tool System）

### 2.1 插件化工具架构

Claude Code 实现了约 40 个离散工具，每个工具拥有独立的：
- **权限模型**（Permission Model）：用户需授权才能执行
- **验证逻辑**（Validation Logic）：入参校验与安全检查
- **输出格式化**（Output Formatting）：结构化返回给 LLM

核心工具列表：

| 工具 | 功能 | 安全等级 |
|------|------|----------|
| `BashTool` | Shell 命令执行，含 23 项安全守卫 | 高 |
| `FileReadTool` | 文件读取 | 低 |
| `FileWriteTool` | 文件写入 | 中 |
| `FileEditTool` | 文件编辑（差量修改） | 中 |
| `WebFetchTool` | 实时网页抓取 | 中 |
| `LSPTool` | Language Server Protocol 集成 | 低 |
| `GlobTool` | 文件模式匹配搜索 | 低 |
| `GrepTool` | 代码内容正则搜索 | 低 |
| `NotebookReadTool` | Jupyter Notebook 读取 | 低 |
| `NotebookEditTool` | Jupyter Notebook 编辑 | 中 |
| `MultiEditTool` | 原子性多文件编辑 | 高 |
| `TodoReadTool` / `TodoWriteTool` | 任务追踪 | 低 |

### 2.2 Bash 安全检查（23 项门控）

`bashSecurity.ts` 实现了业界最严格的 Shell 威胁模型之一，包含：

1. **18 个被阻止的 Zsh 内置命令**
2. **Zsh 等号展开攻击防御**：`=curl` 绕过 `curl` 权限检查的防护
3. **Unicode 零宽字符注入防御**
4. **IFS 空字节注入防御**
5. **畸形 Token 绕过防御**（HackerOne 审计发现）

---

## 三、记忆架构（Memory Architecture）— 核心竞争力

这是竞争对手最应研究的部分。Anthropic 用 **三层记忆体系** 解决了"上下文熵增"问题——即长运行会话因上下文膨胀导致的幻觉与退化。

### 3.1 三层记忆模型

```
Layer 1: MEMORY.md（轻量索引层）
  ├── 永驻上下文，每条约 150 字符
  ├── 存储的是「指针」，不是数据本身
  └── 类似操作系统的页表（Page Table）

Layer 2: Topic Files（按需知识层）
  ├── 实际的项目知识
  ├── 按需加载，不会同时进入上下文
  └── 类似虚拟内存的按需分页（Demand Paging）

Layer 3: Raw Transcripts（原始日志层）
  ├── 完整的会话记录
  ├── 永远不会被完整回读
  └── 仅通过 grep 按特定标识符检索
```

### 3.2 严格写入纪律（Strict Write Discipline）

关键设计原则：**代理只能在文件写入成功确认后，才允许更新记忆索引。**

这意味着：
- 失败的尝试不会污染上下文
- 代理不会把自己的错误操作当作"事实"存储
- 记忆的一致性得到了保障

### 3.3 持疑式记忆（Skeptical Memory）

代理被指令将自身的记忆视为「提示」（hint），而非「事实」。在执行操作前，必须对照实际代码库验证记忆中的信息。这避免了代理基于过时或错误的记忆做出决策。

### 3.4 自愈记忆（Self-Healing Memory）

通过 KAIROS 的 `autoDream` 后台进程实现：
- **合并离散观察**：将碎片化的项目知识整合
- **消除逻辑矛盾**：清理记忆中的不一致
- **将模糊洞察转为绝对事实**：提升记忆置信度
- **使用 forked 子代理**执行维护，防止主代理的推理链被维护过程污染

---

## 四、编排引擎（Query Engine）

### 4.1 核心职责

约 46,000 行代码，被称为"操作的大脑"：

- **LLM API 调用管理**：所有模型交互的统一入口
- **流式响应处理**：Token 级别的实时流处理
- **Token 缓存与上下文管理**：Prompt Cache 经济学驱动的架构
- **多智能体编排**：协调多个并发工作代理
- **重试逻辑**：容错与自动恢复机制

### 4.2 Prompt Cache 经济学

`promptCacheBreakDetection.ts` 追踪 **14 种缓存失效向量**。架构中存在"粘性锁存器"（sticky latches）防止模式切换导致缓存失效。甚至有一个函数被标记为 `DANGEROUS_uncachedSystemPromptSection()`。

当每个 Token 都有成本时，缓存失效不仅是计算机科学问题，更是**会计问题**。

---

## 五、多智能体编排（Multi-Agent Orchestration）

### 5.1 Coordinator Mode

泄露代码中包含一个 **协调器模式**（Coordinator Mode），实现了一个 Claude 实例管理多个工作 Claude 实例：

```
         ┌─────────────┐
         │ Coordinator  │
         │  (Orchestrator)│
         └──────┬───────┘
                │
      ┌─────────┼─────────┐
      │         │         │
  ┌───▼───┐ ┌──▼────┐ ┌──▼────┐
  │Worker │ │Worker │ │Worker │
  │Agent 1│ │Agent 2│ │Agent 3│
  └───────┘ └───────┘ └───────┘
```

核心能力：
- **任务分配**（Task Distribution）
- **结果聚合**（Result Aggregation）
- **冲突仲裁**（Conflict Resolution）

### 5.2 编排算法 = Prompt，而非代码

最令人惊讶的发现：**多智能体的协调算法是通过 System Prompt 实现的，而非硬编码逻辑。**

关键 Prompt 指令示例：
- "Do not rubber-stamp weak work"（不要敷衍弱质量的工作）
- "You must understand findings before directing follow-up work. Never hand off understanding to another worker."（你必须先理解发现，再指派后续工作。绝不要把理解能力外包给其他工作代理。）

这体现了 **Prompt-as-Code** 的设计哲学——用自然语言定义协作协议。

---

## 六、KAIROS：自主守护模式

### 6.1 概念

KAIROS（源自古希腊语"恰当时机"）在代码中被提及 **150+ 次**，代表一个根本性的 UX 转变：从**被动响应式**到**主动自主式**。

### 6.2 核心组件

| 组件 | 功能 |
|------|------|
| `autoDream` | 后台记忆整合进程——"夜间记忆蒸馏" |
| `/dream` skill | 手动触发记忆整理 |
| 后台 Daemon Worker | 常驻后台工作进程 |
| Cron 调度 | 每 5 分钟自动刷新 |
| GitHub Webhook 订阅 | 监听仓库事件 |
| Daily Append-only Logs | 每日追加式日志 |

### 6.3 运行模式

```
用户活跃时 → 前台交互，响应用户指令
用户空闲时 → autoDream 启动，执行记忆维护
              ├── 合并离散观察
              ├── 消除逻辑矛盾
              ├── 转换模糊洞察为确定事实
              └── 更新 MEMORY.md 索引
用户返回时 → 上下文已清洁且高度相关
```

---

## 七、ULTRAPLAN：远程深度规划

ULTRAPLAN 允许将复杂规划任务卸载到**远程云容器运行时（CCR）**，运行 Opus 模型进行长达 30 分钟的深度推理：

```
本地终端 → 发送规划请求 → 远程 CCR (Opus, 30min)
                                    │
                              深度推理中...
                                    │
用户手机/浏览器 ← 审批结果 ← 规划完成
        │
    批准 → __ULTRAPLAN_TELEPORT_LOCAL__ → 结果回传本地
```

---

## 八、Undercover Mode：隐藏 AI 痕迹

### 8.1 设计目的

当 Claude Code 在非 Anthropic 内部仓库中使用时，自动剥离所有 Anthropic 内部痕迹：
- 不提及内部代号（如 "Capybara"、"Tengu"）
- 不提及内部 Slack 频道、仓库名称
- 不提及"Claude Code"本身

### 8.2 单向门设计

```typescript
// 关键设计：可强制开启，不可强制关闭
// 源码注释：
// "There is NO force-OFF. This guards against model codename leaks."
```

- `CLAUDE_CODE_UNDERCOVER=1` 可强制开启
- **没有** `force-OFF` 开关
- 外部构建中，整个函数被死代码消除为平凡返回

---

## 九、反蒸馏机制（Anti-Distillation）

### 9.1 假工具注入

在 `claude.ts` 中，`ANTI_DISTILLATION_CC` 标志开启后：
1. API 请求携带 `anti_distillation: ['fake_tools']`
2. 服务端在 System Prompt 中注入**伪装的工具定义**
3. 如果竞品在录制 API 流量来训练自己的模型，假工具会污染其训练数据

### 9.2 连接器文本摘要

服务端将助手在工具调用之间的推理文本缓冲、摘要、并**加密签名**。录制流量的竞品只能获得摘要，而非完整的推理链。

### 9.3 绕过难度

实际绕过并不复杂——MITM 代理可剥离 `anti_distillation` 字段，环境变量 `CLAUDE_CODE_DISABLE_EXPERIMENTAL_BETAS` 可禁用整个机制。真正的保护可能是法律手段，而非技术手段。

---

## 十、客户端证明（Native Client Attestation）

### 10.1 实现原理

API 请求中包含 `cch=00000` 占位符。在请求离开进程之前，Bun 的原生 HTTP 栈（Zig 编写）用计算出的哈希值覆盖这五个零。

```
JS 层:  x-anthropic-billing-header: cch=00000
                    ↓
Zig 原生层:  cch=00000 → cch=a3f2b (哈希计算)
                    ↓
网络层:  服务端验证哈希，确认来自真实 Claude Code 二进制
```

### 10.2 设计巧思

- 使用相同长度的占位符，避免改变 Content-Length 或需要缓冲区重分配
- 计算发生在 JS 运行时之下，对 JS 层完全不可见
- 本质是 **HTTP 传输层的 DRM**

---

## 十一、其他重要发现

### 11.1 BUDDY 宠物系统

`buddy/companion.ts` 实现了一个终端内的电子宠物：
- **18 个物种**（鸭子、龙、蝾螈、水豚、蘑菇、幽灵等）
- **稀有度分级**：Common → Uncommon → Rare → Epic → Legendary
- **1% 闪光概率**
- **RPG 属性**：DEBUGGING / PATIENCE / CHAOS / WISDOM / SNARK
- 使用 Mulberry32 PRNG，基于用户 ID 哈希 + 盐 `'friend-2026-401'` 生成，同一用户永远获得同一物种
- 物种名称使用 `String.fromCharCode()` 编码，以逃避构建系统的 grep 检测

### 11.2 情绪检测（Regex 实现）

```regex
/\b(wtf|wth|ffs|omfg|shit(ty|tiest)?|dumbass|horrible|awful|
piss(ed|ing)? off|piece of (shit|crap|junk)|what the (fuck|hell)|
fucking? (broken|useless|terrible|awful|horrible)|fuck you|
screw (this|you)|so frustrating|this sucks|damn it)\b/
```

一家 LLM 公司用正则表达式做情感分析——讽刺但务实。Regex 比 LLM 推理更快、更便宜、更可预测。

### 11.3 每日 250,000 次浪费的 API 调用

`autoCompact.ts` 中的注释：
> "BQ 2026-03-10: 1,279 sessions had 50+ consecutive failures (up to 3,272) in a single session, wasting ~250K API calls/day globally."

修复方案：`MAX_CONSECUTIVE_AUTOCOMPACT_FAILURES = 3`。连续 3 次压缩失败后，会话禁用压缩。**三行代码，每天节省 25 万次 API 调用。**

### 11.4 终端渲染优化

借鉴游戏引擎技术：
- `Int32Array` 回收池的 ASCII 字符缓冲区
- 位掩码编码的样式元数据
- 补丁优化器（合并光标移动、取消隐藏/显示对）
- 自驱逐行宽缓存（声称在 token 流式传输期间减少约 50 倍的 `stringWidth` 调用）

---

## 十二、未发布模型代号泄露

| 内部代号 | 对应模型 | 状态 |
|----------|----------|------|
| **Capybara** | Claude 4.6 变体 | 迭代中（v8），假声明率 29-30%（v4 为 16.7%） |
| **Fennec** | Opus 4.6 | 开发中 |
| **Numbat** | 未发布 | 测试中 |
| **Mythos** | Capybara 别名 | 早期泄露确认 |

代码中还包含"assertiveness counterweight"（自信度配重），防止模型在重构时过于激进。

---

## 十三、架构启示与行业影响

### 13.1 对 AI Agent 构建者的启示

1. **三层记忆是银弹**：指针索引 + 按需加载 + grep 检索，比 RAG 更适合代码场景
2. **严格写入纪律**：防止代理用失败尝试污染自身上下文
3. **Prompt-as-Code**：多智能体协作协议用自然语言定义，而非硬编码
4. **缓存经济学**：在 Token 计费模型下，缓存策略是核心架构决策
5. **安全门控应在工具层**：每个工具独立的权限模型，而非全局权限检查

### 13.2 竞争格局影响

泄露使 AI 编码代理领域的竞争**事实性地拉平**：
- Google Gemini CLI 和 OpenAI Codex 已开源其 Agent SDK
- 但 Claude Code 泄露的是**完整产品内部结构**，而非 SDK
- 特别是 KAIROS、ULTRAPLAN 等未发布功能的蓝图暴露，让竞争对手能提前布局

### 13.3 安全影响

泄露源码让攻击者能够：
- 设计针对 Claude Code Hooks 和 MCP 服务器的恶意仓库
- 精确设计绕过权限提示的攻击
- 理解安全检查的具体实现细节

---

## 参考来源

| 来源               | 链接                                   |
| ---------------- | ------------------------------------ |
| VentureBeat 深度报道 | venturebeat.com                      |
| Alex Kim 技术深潜    | alex000kim.com                       |
| DEV.to 完整拆解      | dev.to (Varshith V Hegde)            |
| Hacker News 讨论   | news.ycombinator.com                 |
| GitHub 镜像分析      | github.com/Kuberwastaken/claude-code |

---

*最后更新: 2026-04-02 | 本文基于公开泄露信息整理，仅供学习研究用途*
