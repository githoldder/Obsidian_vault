# 常工生鲜 - Scrum + Vibe Coding 个人团队开发工作流

> 基于 Scrum 规范 × Vibe Coding 工程化规范 × 本项目实际约束制定
> 适用规模：1~3人极简团队，全栈独立开发

---

## 一、核心原则

1. **一个人就是一支队伍** —— 不设专职角色轮换，每个 Sprint 内开发者同时担任 PO + SM + Dev
2. **Vibe Coding 优先** —— AI 辅助编程是主要生产力，开发者做决策者而非打字员
3. **Sprint 咬合业务节奏** —— 生鲜业务以"周"为最小运营周期，Sprint 长度固定为 **1周（周五~下周四）**
4. **可交付 == 可上线** —— 每个 Sprint 结束时必须有可对用户可见的功能，不接受"完成但未集成"

---

## 二、团队角色（单人模式）

| 角色                | 职责                | 执行者                        |
| ----------------- | ----------------- | -------------------------- |
| Product Owner（PO） | 拍板做什么、不做什么，定义验收标准 | 开发者本人                      |
| Scrum Master（SM）  | 保护开发过程不被打断，推进回顾   | 开发者本人                      |
| Developer         | 编码、测试、部署          | 开发者本人                      |
| AI Copilot        | 承担初级开发、文档生成、代码审查  | GPT-4o / Claude / DeepSeek |

> **重要：** 每日站会改为**每日日志（daily log）**——用 Obsidian 写 3 行：
> - 昨天干了什么
> - 今天要干什么
> - 遇到了什么卡点

---

## 三、Sprint 周期结构（1周 Sprint）

```
周五（14:00）─────────────────────────────── 下周四（18:00）
     │                                              │
  Sprint Planning                              Sprint Review
  (2h)                                         (Demo + Retrospective)
     │                                              │
  周六 ~ 周三：开发迭代                               │
  周四：收尾 + 集成测试                              │
```

### Sprint 0（准备 Sprint，1次性）
- 搭建项目骨架（代码仓库、CI/CD、数据库schema）
- 明确 Definition of Done（DoD）
- AI Agent 本地部署配置（Ollama + One-API）
- 微信小程序账号申请、备案

### Sprint N（第N个迭代）
**周五 14:00 — Sprint Planning（2h）**
1. 回顾上一 Sprint 的 Review 反馈
2. 从 Product Backlog 选取 P0~P1 故事卡（不超过 3 张）
3. 估算工时（用 T-Shirt Size：S/M/L/XL）
4. 拆解任务，分配给开发者 + AI Copilot

**周六 ~ 周三 — 日常开发**
- 晨间：更新 Sprint Board（Trello / 飞书任务板）
- 开发时：Vibe Coding 模式（见第五章）
- 遇到阻塞 → 记录到 Blockers List，不超过 24h 必须解决

**周四 — 收尾日**
- 功能集成测试
- 若功能达到 DoD → 提测 / 发布
- 若未达到 → 拆分遗留项进入下一 Sprint

**周五 14:00 — Sprint Review + Retrospective**
- Review：演示已完成的 User Story，给 PO 验收
- Retrospective：3个好做法 + 2个改进行动（用 Mad/Sad/Glad 框架）
- 产出：更新后的 Product Backlog + 下一 Sprint 计划

---

## 四、Product Backlog 优先级框架

> 用 ICE 评分：Impact（影响）× Confidence（信心）× Ease（难易）

### 当前 MVP Backlog（按 ICE 排序）

| ID | User Story | 角色 | ICE | Sprint |
|----|-----------|------|-----|--------|
| P0-01 | 作为用户，我能在首页浏览水果列表并下单 | 用户 | 9×9×8=648 | Sprint 1 |
| P0-02 | 作为骑手，我能抢单并更新配送状态 | 骑手 | 9×9×7=567 | Sprint 1 |
| P0-03 | 作为采购，我能拍照录入水果信息 | 采购 | 8×8×7=448 | Sprint 1 |
| P1-01 | 作为管理员，我能在后台看到实时订单状态 | Admin | 7×7×6=294 | Sprint 2 |
| P1-02 | AI Agent 自动生成财务日报并推送 | 系统 | 7×7×6=294 | Sprint 2 |
| P1-03 | 作为用户，我能在下单时使用优惠券 | 用户 | 6×6×5=180 | Sprint 3 |
| P2-01 | 墨菲（市场调研Agent）自动生成采购建议 | 系统 | 5×6×7=210 | Sprint 4+ |
| P2-02 | 骑手端地图导航集成 | 骑手 | 5×5×4=100 | Sprint 4+ |

---

## 五、Vibe Coding 开发流程（核心）

> **核心理念：** 开发者描述"要什么"，AI 生成"怎么做"，开发者决策"对不对"

### 标准流程（Single Person）

```
[1. 需求确认] → [2. AI生成代码] → [3. 开发者Review] → [4. 提交 & 测试] → [5. 部署]
     10min              30min              15min              20min           5min
```

### 详细步骤

**Step 1 — 需求确认（10min）**
- 在 Obsidian 里写 `Task.md`：描述 User Story + 验收标准
- 明确技术方案（用白纸画流程图，拍照存 Obsidian）
- AI Agent 角色分配：谁负责哪部分代码生成

**Step 2 — AI 生成代码（30min）**
- 使用 AI Coding 工具（Cursor / Claude Code / Github Copilot）
- 按模块拆分 Prompt，每次只生成一个功能单元
- 生成的代码存入 `/src/features/{feature_name}/`
- 关键文件（数据库操作、支付逻辑）**必须手写**，AI 只能辅助注释

**Step 3 — 开发者 Review（15min）**
- 逐文件检查：逻辑正确性、安全性、是否符合现有架构
- 用 AI 做 Code Review（另一模型交叉验证）
- 安全红线检查：支付、用户数据、密码存储

**Step 4 — 提交 & 测试（20min）**
- 提交规范：`feat/{feature}-{issue_id}: 简短描述`
- 单元测试：核心业务逻辑必须有测试覆盖
- 集成测试：手动走通完整用户路径

**Step 5 — 部署（5min）**
- 腾讯云轻量服务器：GitHub Actions 自动部署
- Docker 容器化，一键回滚

### Vibe Coding 禁忌
- ❌ 不审查直接复制 AI 生成的代码
- ❌ 不让 AI 写支付、安全相关的核心逻辑
- ❌ 不用 AI 生成的内容作为最终产品文案（水果文案需人工润色）

---

## 六、AI Agent 本地部署方案（与工作流集成）

> 来自想法.md 的技术决策，纳入工程化体系

### 架构选择

```
M4 Mac 本地推理（低负载任务）
        ↓
  Ollama（推理引擎）
  ├── deepseek-7b（蜂王子智能体：格式化数据、简单文案）
  └── qwen-2.5-7b（备用）
        ↓
  One-API（模型路由 & 协议转换）
  ├── 路由到本地 Ollama（省钱）
  └── 路由到阿里百炼/DeepSeek云端（复杂推理）
        ↓
  Ruflo（Swarm 编排层）
  ├── 蜂王（Queen）：任务分发 & 状态管理
  └── 8个子智能体：各自执行特定业务域
```

### 与 Scrum 的集成方式

| Sprint | AI 任务范围 | 部署状态 |
|--------|------------|---------|
| Sprint 1-2 | 仅用云端 API（快速验证业务） | 本地 Ollama 搭建完成 |
| Sprint 3 | 子智能体接入 One-API | 本地推理灰度测试 |
| Sprint 4+ | 蜂王 + 全部子智能体上线 | 混合部署（本地为主） |

### 本地部署 Checklist（每个 Sprint 结束时检查）
- [ ] Ollama 服务状态正常（`systemctl status ollama`）
- [ ] One-API 模型配置完整（至少1个本地 + 1个云端）
- [ ] 子智能体 Prompt 版本已更新（存 Obsidian）
- [ ] 日Token消耗记录（控制在预算内）

---

## 七、Definition of Done（DoD）

每个 User Story "完成"的判定标准：

- [ ] 代码已合并到 `main` 分支
- [ ] 微信小程序体验版可扫码访问
- [ ] 后台对应功能可正常操作
- [ ] 单元测试覆盖率 ≥ 60%（核心路径）
- [ ] 无 `console.error` 或未处理 Promise Rejection
- [ ] PO（开发者自己）已验收通过

---

## 八、工具链清单

| 阶段       | 工具                          | 用途               |
| -------- | --------------------------- | ---------------- |
| 需求管理     | Obsidian + Trello           | Backlog、每日日志、流程图 |
| 代码托管     | GitHub Private Repo         | 版本控制、CI/CD       |
| 前端       | 微信小程序（Uniapp）               | 统一前端             |
| 后端       | Node.js + Express / Fastify | API 服务           |
| 数据库      | MySQL（腾讯云）                  | 关系数据             |
| 缓存       | Redis                       | 会话、实时状态          |
| AI 本地    | Ollama + M4 MLX             | 子智能体推理           |
| AI 路由    | One-API                     | 多模型统一入口          |
| Swarm 编排 | Ruflo                       | Agent 任务编排       |
| AI 云端    | 阿里百炼 / DeepSeek             | 蜂王复杂推理           |
| 部署       | Docker + 腾讯云                | 容器化部署            |
| 监控       | 腾讯云监控 + 日志服务                | 错误追踪             |
| 推送       | 企业微信机器人                     | 财务日报、预警通知        |

---

## 九、风险与对策

| 风险 | 概率 | 影响 | 对策 |
|------|------|------|------|
| 微信小程序账号审核被拒 | 中 | 高 | Sprint 0 提前申请，预留2周审核缓冲 |
| AI Token 费用超支 | 高 | 中 | Sprint 1 结束设定月度上限，本地模型优先 |
| 单人精力耗尽 | 中 | 高 | Sprint 4 之后评估是否引入兼职助手 |
| 水果保鲜期导致运营节奏被打乱 | 低 | 高 | 先做预售模式规避库存压力 |
| Ollama M4 推理速度慢 | 低 | 低 | 7B 模型足够简单任务，复杂推理走云端 |

---

*文档版本：v1.0 | 最后更新：2026-04-14 | 制定者：代可行*
