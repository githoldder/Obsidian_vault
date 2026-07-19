# Scrum + Vibe Coding 个人与团队开发工作流 (43-Scrum-VibeCoding-Workflow)

> **适用范围**：1～3人极简团队/全栈独立开发，将模糊想法落地为可交付系统。
> **核心思想**：文档驱动 + 双层 AI 协作 + 原型先行 + 敏捷周期咬合。

---

## 一、核心原则

1. **多重角色合一**：不设专职角色轮换。单人模式下，开发者同时担任 Product Owner (PO) + Scrum Master (SM) + Developer。
2. **Vibe Coding 优先**：AI 辅助编程是主要生产力，开发者做决策者而非打字员。
3. **Sprint 咬合业务**：以 "周" 为最小运营周期，Sprint 长度固定为 **1 周（周五至下周四）**。
4. **可交付 == 可上线**：每个 Sprint 结束时必须有对用户可见的功能，不接受“完成但未集成”。

---

## 二、双层 AI 协作模型与文档机制

### 2.1 协作模型
```
Layer 1（需求对接层）  ←→  人（发散需求 → 收敛为 PRD）
  ↓ 产出：PRD.md + PRD.json
        │
Layer 2（全栈开发层）  ←→  人（AI Plan 方案A/B/C ↔ AI Review 建议 ↔ 人工确认）
  ↓ 产出：代码 + Git 提交
        │
Layer 3（测试维护层）  ←→  人（AI 设计测试，开发人员集成，Review 合并）
```

### 2.2 上下文累积机制
- 任务执行中修改 `PRD.json` 的 `value`。
- 将踩坑记录写入 `context/context.txt`。
- 当 `context.txt` 达到一定长度（建议100行或100条），进行高低优先级提取：
  - 高优先级经验 -> 全局 `agent.md`
  - 低优先级经验/历史日志 -> `memory.md`
  - 随后清理重置 `context.txt`。

---

## 三、Sprint 周期结构 (1周迭代)

```
周五（14:00）─────────────────────────────── 下周四（18:00）
     │                                              │
  Sprint Planning                              Sprint Review
  (2h 确定Backlog与DoD)                       (演示 + Mad/Sad/Glad回顾)
     │                                              │
  周六 ~ 周三：日常 Vibe Coding 开发               │
  周四：收尾 + 集成测试                             │
```

- **Sprint 0（准备阶段，仅一次）**：搭建项目骨架、CI/CD、数据库 schema、DoD 确定、AI 本地部署与接口配置。
- **Sprint Planning**：从 Backlog 选取 P0~P1 需求，用 T-Shirt Size (S/M/L/XL) 估算工时并进行任务拆解。
- **每日日志 (Daily Log)**：每日站会简化为 Obsidian 日志，记录三行：昨天做了什么、今天要做什么、有何卡点。
- **Sprint Review & Retrospective**：向 PO（或自我验收）演示可交付功能，使用 Mad/Sad/Glad 框架进行回顾，更新 Backlog。

---

## 四、Product Backlog 优先级框架

使用 **ICE 评分** 确定优先级：`Impact (影响) × Confidence (信心) × Ease (易用度)`
- **P0 核心需求**：必须在当前 Sprint 交付的核心用户闭环（如商品浏览与下单）。
- **P1 支撑功能**：管理后台、自动化报表等（如第 2-3 迭代上线）。
- **P2 增值功能**：智能推荐、高级图表等。

---

## 五、Vibe Coding 标准开发步骤

```
[1. 需求确认] → [2. AI生成代码] → [3. 开发者Review] → [4. 提交 & 测试] → [5. 部署]
     10min              30min              15min              20min           5min
```

### Step 1 — 需求确认 (10min)
- 在 `Task.md` 中写明 User Story 和验收标准。
- 梳理技术方案，手绘业务流程并分配 AI Agent 角色。

### Step 2 — AI 生成代码 (30min)
- 使用 Cursor/Claude Code/Copilot。
- 按模块拆分 Prompt，每次只生成一个功能单元。
- 生成代码归入 `/src/features/{feature_name}/` 目录，**安全和支付等核心逻辑必须手写/严加审核**。

### Step 3 — 开发者 Review (15min)
- 逐个文件检查逻辑正确性、安全红线（防止 SQL 注入、密钥泄漏等）。
- 可用另一大模型做交叉 Code Review。

### Step 4 — 提交与测试 (20min)
- **Git 分支策略**：使用 `main` (稳定分支) -> `feature/{name}` (功能分支) / `bugfix/{name}` (Bug修复) 模式。
- **Commit 规范**：`feat/fix/refactor({module}): 描述`。
- **测试**：运行单元测试与集成测试，确保核心业务路径完全跑通。

### Step 5 — 部署 (5min)
- 使用 Docker 容器化并托管到云服务器。
- 通过 GitHub Actions 实现 CI/CD 自动化构建，并保留一键回滚能力。

---

## 六、Definition of Done (DoD) 清单

每个 User Story 必须通过以下 DoD 判定才算“完成”：
- [ ] 代码已通过 Review 并合并至 `main` 分支。
- [ ] 测试环境/体验版能够无异常扫码或访问。
- [ ] 核心业务逻辑的单元测试覆盖率 ≥ 60%。
- [ ] 控制台无未处理的 Error 或 Promise Rejection 报错。
- [ ] 开发者本人/PO 确认功能符合设计并签字/打勾。

---

## 七、AI 本地/云端混合部署参考方案

- **本地推理（低负载任务）**：
  - 推理引擎：Ollama (运行 DeepSeek-7B / Qwen-2.5-7B)。
  - 模型路由：One-API 统一接口协议，无缝切换本地模型与云端 API。
- **云端推理（复杂架构与计划制定）**：
  - 路由到云端高参数模型 (如 DeepSeek-V3/R1, Claude 3.5 Sonnet)。
- **智能体编排**：
  - 使用轻量编排层进行多 Agent 任务分发与状态流转。
