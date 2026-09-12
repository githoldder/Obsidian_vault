# Karpathy 编码约束规范 — Token 经济学最高层级技巧

> **来源**: [multica-ai/andrej-karpathy-skills](https://github.com/multica-ai/andrej-karpathy-skills)  
> **提炼者**: Andrej Karpathy (OpenAI 创始成员、前 Tesla Autopilot 负责人)  
> **定位**: Token 经济学中通过大量实战和重复实验总结出的节省 Token 消耗、提高工作效率的最高层级规则  
> **适用**: Claude Code / Cursor / OpenClaw 等 AI 编程 Agent  

---

## 一、核心洞察：为什么这是 Token 经济学的最高层级？

### 传统 Vibe Coding 的隐性 Token 浪费

| 问题 | Token 浪费场景 | 后果 |
|------|---------------|------|
| **盲目假设** | AI 脑补需求，写 1000 行后发现方向错误 | 1000 行代码 + 调试对话全部作废 |
| **过度设计** | 为简单需求堆砌抽象层、工厂类、配置注入 | 50 行能解决的事写了 200 行，后续维护持续消耗 Token |
| **顺手重构** | 改 A 功能时"优化"了无关的 B 功能 | 引入副作用，产生新的 Bug，进入调试循环 |
| **无验收标准** | "让它能跑就行" | AI 反复猜测、反复修改、反复验证 |

> **Karpathy 原话**: "模型会替你做错误假设然后一路跑下去，不管理困惑、不寻求澄清、不呈现不一致、不展示权衡、不适当反驳。"

### 四大原则的 Token 节省机制

```
┌─────────────────────────────────────────────────────────────┐
│                    Token 经济学金字塔                        │
├─────────────────────────────────────────────────────────────┤
│  Layer 4 (最高层级) │  Karpathy 编码约束规范                 │
│                     │  → 从根因上减少错误假设和过度设计       │
│                     │  → 节省比例: 10x ~ 100x               │
├─────────────────────────────────────────────────────────────┤
│  Layer 3            │  Skill 蒸馏 + 脚本固化                  │
│                     │  → 重复任务零边际成本                   │
│                     │  → 节省比例: 10x                       │
├─────────────────────────────────────────────────────────────┤
│  Layer 2            │  CLI 替代 GUI + 工具链优化              │
│                     │  → 减少上下文窗口占用                   │
│                     │  → 节省比例: 4x                        │
├─────────────────────────────────────────────────────────────┤
│  Layer 1 (基础层级) │  模型选择 + Prompt 优化                 │
│                     │  → 单次调用效率最大化                   │
│                     │  → 节省比例: 2x                        │
└─────────────────────────────────────────────────────────────┘
```

**关键洞察**: 前三个层级（模型选择、CLI 优化、Skill 固化）都是在**单次任务执行层面**优化。而 Karpathy 约束规范是在**任务源头**消除错误——让 AI 在第一次就做对，避免后续的修正循环。这是 Token 经济学中 ROI 最高的投资。

---

## 二、四大核心原则

### 原则 1: Think Before Coding（编码前先思考）

> **针对问题**: 模型静默猜测方向、假装理解需求、隐藏困惑  
> **Token 节省机制**: 1 轮澄清对话 vs 10 轮返工对话

#### 规则清单

- [ ] **不假设** — 存在歧义时必须提问，而不是自行脑补
- [ ] **不隐藏困惑** — 遇到不确定的地方，停下来明确说出哪里不懂
- [ ] **呈现权衡** — 存在多种实现方案时，主动列出选项并说明取舍
- [ ] **适当反驳** — 发现需求不合理或存在更简单路径时，主动提出
- [ ] **停止并澄清** — 当困惑累积时，命名不清楚的点并请求澄清

#### 执行模板

```markdown
## 需求澄清检查清单

在编码前，先回答以下问题：

1. **需求理解**: 我理解的正确吗？（复述需求，等待确认）
2. **歧义识别**: 这个需求中有哪些可能的歧义？
3. **方案对比**: 有哪些实现方案？各自的优缺点是什么？
4. **范围确认**: 哪些在范围内？哪些明确不在范围内？
5. **验收标准**: 怎样算"完成"？有可验证的标准吗？
```

#### 反例 → 正例

| 反例（浪费 Token） | 正例（节省 Token） |
|-------------------|-------------------|
| "好的，我这就开始实现" | "我理解你要做的是 X，这样对吗？如果不对，请指出偏差" |
| 直接开始写代码 | 先列出 2-3 个实现方案，让用户选择 |
| 默默处理"可能的"边界情况 | 明确问："是否需要处理 Y 场景？" |

---

### 原则 2: Simplicity First（极简优先）

> **针对问题**: 过度工程化、抽象膨胀、代码臃肿  
> **Token 节省机制**: 50 行代码 vs 200 行代码，后续维护 Token 持续节省

#### 规则清单

- [ ] **无需求不新增** — 不添加没有被要求的功能
- [ ] **无复用不抽象** — 不为只用一次的代码创建抽象层
- [ ] **无请求不配置** — 不添加没人要求的"灵活性"或"可配置性"
- [ ] **不处理不可能** — 不处理不可能发生的异常情况
- [ ] **主动精简** — 如果 200 行能压到 50 行，重写

#### 判断标准

> **黄金标准**: 如果资深工程师看了会说"这是不是太复杂了"，就简化。

#### 反例 → 正例

| 反例（浪费 Token） | 正例（节省 Token） |
|-------------------|-------------------|
| 为单个函数创建接口 + 工厂类 + 配置注入 | 直接写函数，用到第二次时再抽象 |
| "预留扩展性"，添加未使用的配置项 | 只实现当前需求，扩展性在需求明确时再加 |
| 处理"理论上可能"的边界情况 | 只处理需求中明确提到的场景 |

---

### 原则 3: Surgical Changes（外科手术式精准修改）

> **针对问题**: 顺手"优化"无关代码、改动范围不可控、引入副作用  
> **Token 节省机制**: 避免副作用引入的新 Bug，减少调试循环

#### 规则清单

- [ ] **只动该动的** — 仅修改需求直接关联的代码
- [ ] **不动相邻代码** — 不改动相邻函数、注释、格式
- [ ] **不删遗留代码** — 原有死代码不擅自删除，仅做标注提醒
- [ ] **只清自己的 mess** — 仅清理本次改动产生的无用导入、变量
- [ ] **可追溯** — 每一行修改都能追溯到用户原始需求

#### 判断标准

> **黄金标准**: 每个变更的行都应该能直接追溯到用户的请求。

#### 反例 → 正例

| 反例（浪费 Token） | 正例（节省 Token） |
|-------------------|-------------------|
| 改 A 功能时"顺手"重构了 B 模块 | 只改 A，B 的问题单独提 Issue |
| 删除"看起来没用"的注释 | 保留注释，如果确定无用再单独处理 |
| 统一代码风格（与本次需求无关） | 保持原有风格，风格统一另开 PR |

---

### 原则 4: Goal-Driven Execution（目标驱动执行）

> **针对问题**: 写完无法判断是否满足需求、缺少测试闭环  
> **Token 节省机制**: 明确验收标准 → AI 自主迭代 → 减少人工干预

#### 规则清单

- [ ] **定义成功标准** — 将模糊指令转化为可验证的目标
- [ ] **测试优先** — 先写测试，再写实现
- [ ] **验证循环** — 每步完成后验证，不堆积到最后一刻
- [ ] **独立迭代** — 给 AI 清晰目标，让它自主循环直到达标

#### 指令转换模板

| 模糊指令（浪费 Token） | 目标驱动指令（节省 Token） |
|---------------------|-------------------------|
| "Add validation" | "Write tests for invalid inputs, then make them pass" |
| "Fix the bug" | "Write a test that reproduces it, then make it pass" |
| "Refactor X" | "Ensure tests pass before and after" |
| "Make it work" | "Define: [输入] → [期望输出] → [验证方法]" |

#### 多步骤任务模板

```markdown
## 执行计划

1. [Step 1 描述] → verify: [验证标准]
2. [Step 2 描述] → verify: [验证标准]
3. [Step 3 描述] → verify: [验证标准]
```

> **Karpathy 原话**: "LLMs are exceptionally good at looping until they meet specific goals... Don't tell it what to do, give it success criteria and watch it go."

---

## 三、与现有工作流的双链关联

### 3.1 与 Vibe Coding 工作流的整合

| 现有工作流 | 整合方式 | 双链链接 |
|-----------|---------|---------|
| **Scrum + Vibe Coding** | Sprint Planning 时应用原则 1（澄清需求），Daily 时应用原则 4（验证进度） | [[43-Scrum-VibeCoding-Workflow]] |
| **CLI Token 节省** | Karpathy 规范是 Layer 4，CLI 优化是 Layer 2，两者叠加 | [[52-CLI-Token-Saving]] |
| **Skill 蒸馏** | 高频重复任务 → Skill 固化前，先用原则 2 确保 Skill 本身不过度设计 | [[54-Skill-Distillation]] |
| **Harness 工程** | Ralph 循环的每轮迭代都应用四大原则，特别是原则 3（精准修改）和原则 4（目标驱动） | [[55-Harness-Engineering]] |

### 3.2 与文档工程规范的整合

| 规范文件 | 整合点 |
|---------|--------|
| **需求访谈 SOP** | 原则 1 的"澄清检查清单"直接嵌入 [[11-Req-Interview-SOP]] |
| **文档工程标准** | 原则 2 的"极简优先"适用于文档写作，避免过度模板化 | [[21-Doc-Engineering-Standard]] |
| **问题解决方法论** | 原则 1 的"停止并澄清"与 [[45-Problem-Solving-Methodology]] 的"定义问题"阶段联动 |

### 3.3 在 Token 经济学中的位置

```
Token 节省策略栈
├── Layer 4: Karpathy 编码约束规范（本文件）← 最高层级，根因治理
│   └── 核心: 四大原则 → 减少错误假设和过度设计
├── Layer 3: Skill 蒸馏 + 脚本固化
│   └── [[54-Skill-Distillation]]
├── Layer 2: CLI 替代 GUI + 工具链优化
│   └── [[52-CLI-Token-Saving]]
└── Layer 1: 模型选择 + Prompt 优化
    └── 单次调用效率最大化
```

---

## 四、安装与使用

### 方式 A: 作为 OpenClaw Skill（推荐）

将本文件作为 Skill 安装到 OpenClaw，所有项目自动生效。

### 方式 B: 项目级 CLAUDE.md

在项目根目录创建 `CLAUDE.md`，将四大原则追加进去：

```bash
# 新项目
curl -o CLAUDE.md https://raw.githubusercontent.com/multica-ai/andrej-karpathy-skills/main/CLAUDE.md

# 已有项目（追加）
echo "" >> CLAUDE.md
curl https://raw.githubusercontent.com/multica-ai/andrej-karpathy-skills/main/CLAUDE.md >> CLAUDE.md
```

### 方式 C: Cursor Rules

将内容保存为 `.cursor/rules/karpathy-guidelines.mdc`：

```markdown
---
description: Karpathy coding guidelines
globs: "*"
alwaysApply: true
---

[粘贴四大原则内容]
```

---

## 五、效果验证

### 验证指标

| 指标 | 优化前 | 优化后 | 验证方法 |
|------|--------|--------|---------|
| 需求返工率 | 高（AI 脑补错误） | 低（先澄清再编码） | 统计 Sprint 内需求变更次数 |
| 代码行数 | 200 行（过度设计） | 50 行（极简实现） | Code Review 时统计 |
| 副作用引入 | 频繁（顺手重构） | 极少（精准修改） | Bug 追踪系统中"无关修改引入"标签 |
| 调试轮数 | 5-10 轮 | 1-2 轮 | 统计 AI 对话轮数 |

### 何时生效

这些规范正在生效的标志：
- ✅ Diff 中只出现请求过的变更
- ✅ 代码第一次就简单，不需要后续简化
- ✅ 澄清问题出现在实现之前，而不是错误之后
- ✅ PR 干净、最小化，没有"路过式重构"

---

## 六、权衡说明

> **这些规范偏向谨慎而非速度。**

- 对于**琐碎任务**（简单拼写修复、明显的一行修改），使用判断 — 不需要完整的严谨流程。
- 目标是**减少非琐碎工作中的昂贵错误**，而不是拖慢简单任务。

---

## 七、参考文献与来源

| 来源 | 链接 | 说明 |
|------|------|------|
| 原始仓库 | https://github.com/multica-ai/andrej-karpathy-skills | multica-ai 官方仓库 |
| Karpathy 推文 | https://x.com/karpathy/status/2015883857489522876 | 原始观察来源 |
| 中文 README | https://github.com/multica-ai/andrej-karpathy-skills/blob/main/README.zh.md | 官方中文文档 |
| Claude Skills 市场 | https://claudeskills.info/skills/multica-ai/andrej-karpathy-skills/ | 安装统计 |

---

## 八、社区扩展版：十规则 / 十二规则（非官方，高价值衍生）

> **⚠️ 声明**：以下扩展规则来自社区衍生版本（whitesmell/karpathy-skills、Mnimiy/@mnilax 等），**非 multica-ai 上游官方内容**。但在 30 个代码库 / 50 个任务的实测中，错误率从原 4 条的 ~11% 进一步降至 ~3%。建议作为「进阶约束」按需选用。

### 8.1 十规则版（whitesmell/karpathy-skills, 2026-06）

在原 4 条基础上新增 6 条，聚焦「代码写完之后」的自检阶段：

| # | 规则 | 针对问题 | 核心要求 |
|---|------|---------|---------|
| 5 | **Read Before You Write** | 盲改代码、风格漂移、发明 API | 修改前完整读取目标文件；复制已有模式；检查 imports；看测试文件 |
| 6 | **Verification** | 未测试的修复、跳过回归检查、弱断言 | 修 bug 前先写可靠复现的测试；改代码→跑测试→通过才算解决 |
| 7 | **Debugging** | 自信的错误诊断、未复现就改 | 完整读错误与 stack trace → 先复现 → 每次只改一个变量 |
| 8 | **Dependencies** | 不必要的包、重复库、维护负担 | 先问标准库能否解决；必须加时显式记录理由 |
| 9 | **Communication** | 未陈述的担忧、模糊不确定、无用总结 | 区分「有用的不确定」（可行动）与「空洞安慰」；禁止不确定时装自信 |
| 10 | **Common Failure Modes** | Kitchen Sink、Wrong Abstraction、Runaway Refactor | 显式列出四类陷阱供执行时对照 |

**Common Failure Modes 四条陷阱**：
- **Kitchen Sink**：改动远超预期 → 立即停止，回到最小范围
- **Wrong Abstraction**：重复 3+ 次 → 提取共享函数；否则保持内联
- **Optimistic Path**：无错误处理 → 列出失败场景逐个处理
- **Runaway Refactor**：级联蔓延 → 只改原始需求涉及部分

### 8.2 十二规则版（Mnimiy/@mnilax, 2026-05）

在 4 条基础上追加 8 条，针对 Agent 时代的新失败模式（Agent 冲突、hook 级联、Skill 加载冲突、跨 Session 工作流断裂）：

| # | 规则 | 针对问题 | 核心要求 |
|---|------|---------|---------|
| 5 | **Use the model only for judgment calls** | 用模型做非语言工作（路由/重试/状态码处理） | 分类/起草/总结/提取用模型；路由/重试/确定性转换用代码 |
| 6 | **Hard token budgets** | Agent 循环无限制消耗 Token | 简单任务 ≤8K；标准任务 ≤20K；复杂任务 ≤40K；超预算停下询问 |
| 7 | **Surface conflicts, don't average them** | 两处代码矛盾时取平均 | 标记冲突并问该遵循哪个；不混合成「两边都满足」的烂代码 |
| 8 | **Read before you write** | 未读邻接文件就添加代码 | 读文件 exports、直接调用方、共享工具；不懂就问 |
| 9 | **Tests verify intent, not just behavior** | 通过测试即万事大吉 | 测试必须编码「为什么重要」；业务逻辑变时测试必须能失败 |
| 10 | **Checkpoint after every significant step** | 长操作无检查点 | 每步后总结已完成/已验证/待完成；无法描述状态就停下 |
| 11 | **Convention beats novelty** | 引入新风格与既有模式冲突 | 成熟代码库中匹配既有模式，即使有「更好」的；分歧另开对话 |
| 12 | **Fail visibly, not silently** | 把绕过的当成功 | 暴露每条跳过记录/回滚事务/约束违反；绝不静默失败 |

**关键实测数据**（Mnimiy, 30 codebases × 50 tasks）：
- 无 CLAUDE.md：错误率 ~41%
- 原 4 条：错误率 ~11%，合规率 ~78%
- 全 12 条：错误率 ~3%，合规率 ~76%
- **>14 条：合规率骤降至 ~52%**（200 行 / 14 条为注意力天花板）

### 8.3 如何选用

```markdown
## 选用指南

- **单会话代码编写** → 原 4 条足够
- **自主 Agent 循环** → 加 6 条（十规则版的 Read/Verification/Debugging/Dependencies/Communication/Failure Modes）
- **多 Agent / 多步骤 / 跨 Session** → 用全 12 条
- **文件长度红线** → 总文件 < 200 行，否则合规率暴跌
```

---

## 九、与现有工作流的双链关联（扩展版）

### 9.1 与 Vibe Coding 工作流的整合

| 现有工作流 | 整合方式 | 双链链接 |
|-----------|---------|---------|
| **Scrum + Vibe Coding** | Sprint Planning 时应用原则 1（澄清需求），Daily 时应用原则 4（验证进度） | [[43-Scrum-VibeCoding-Workflow]] |
| **CLI Token 节省** | Karpathy 规范是 Layer 4，CLI 优化是 Layer 2，两者叠加 | [[52-CLI-Token-Saving]] |
| **Skill 蒸馏** | 高频重复任务 → Skill 固化前，先用原则 2 确保 Skill 本身不过度设计 | [[54-Skill-Distillation]] |
| **Harness 工程** | Ralph 循环的每轮迭代都应用四大原则，特别是原则 3（精准修改）和原则 4（目标驱动） | [[55-Harness-Engineering]] |

### 9.2 扩展规则与工作流的对应

| 扩展规则 | 对应工作流 | 整合点 |
|---------|-----------|--------|
| **Read Before You Write** | [[43-Scrum-VibeCoding-Workflow]] | Sprint 开始前读取现有代码库规范 |
| **Verification / Debugging** | [[55-Harness-Engineering]] | Ralph 循环的验证阶段嵌入测试优先 |
| **Hard Token Budgets** | [[52-CLI-Token-Saving]] | Layer 4 约束与 Layer 2 工具叠加 |
| **Checkpoint** | [[05-Knowledge-Mgmt-Workflow]] | 多步骤任务的知识沉淀与状态检查 |
| **Convention beats novelty** | [[21-Doc-Engineering-Standard]] | 文档写作匹配既有模板，不发明新格式 |
| **Fail visibly** | [[45-Problem-Solving-Methodology]] | 问题定义阶段暴露假设，不静默猜测 |

---

## 十、更新日志

| 日期 | 版本 | 变更 |
|------|------|------|
| 2026-09-12 | v1.0 | 初始版本，整合四大原则，建立与现有工作流的双链关联 |
| 2026-09-13 | v1.1 | 新增社区扩展版：十规则（whitesmell）+ 十二规则（Mnimiy），附实测数据与选用指南；扩展双链关联表 |

---

tags: [karpathy, coding-guidelines, token-economics, ai-agent, claude-code, cursor, vibe-coding]
type: skill
status: 已掌握
source: https://github.com/multica-ai/andrej-karpathy-skills
created: 2026-09-12
updated: 2026-09-13
