# Context Manager - AI Agent 接力跑系统

> 智能上下文管理系统，实现多 Agent 无缝协作，消除思维断裂，确保项目可追溯。

---

## 🎯 核心解决的问题

### 1. 上下文不连贯
**问题**: 高强度使用 AI Agent 时，经常因为上下文断裂导致开发不连贯。
**解决**: 接力跑机制 + 自动上下文传递，每个 Agent 都能完美接手前一个的工作。

### 2. AI 思维断裂
**问题**: AI 为了应付用户而输出，未严格检验就结束任务。
**解决**: Evaluator-Optimizer 模式 + 质量关卡，每个阶段都有验收标准。

### 3. 项目熵增
**问题**: 多轮交互后文件混乱、结构不清、难以追溯。
**解决**: 标准目录结构 + 熵减守护者，自动维护项目整洁。

### 4. 无法快速接手
**问题**: 隔段时间再进入项目，或换 Agent 接手时，无法快速理解状态。
**解决**: Handoff Resolver，30秒内生成 Executive Summary，零输入接手。

---

## 🏗️ 系统架构

```
┌─────────────────────────────────────────────────────────────────┐
│                    用户交互层 (User Interface)                    │
│         Commands: init-project, status, resume, phase...         │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                   编排控制层 (Context Orchestrator)                │
│     任务分解 • Agent 调度 • 上下文传递 • 状态管理 • 熵减控制         │
└─────────────────────────────────────────────────────────────────┘
                              │
            ┌─────────────────┼─────────────────┐
            ▼                 ▼                 ▼
┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
│   Relay Agent   │ │ Handoff Resolver│ │Entropy Guardian │
│  (阶段执行者)    │ │  (快速接手专家)  │ │  (熵减守护者)   │
└─────────────────┘ └─────────────────┘ └─────────────────┘
            │                 │                 │
            └─────────────────┼─────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                   持久化层 (Context Persistence)                  │
│  project-manifest.json • context-history.json • decision-log     │
│  issue-tracker.json • phase-reports/ • handoff-documents/       │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🚀 快速开始

### 安装

```bash
# 方式1: 本地测试
claude --plugin-dir ./context-manager

# 方式2: 安装到插件目录
# 复制 context-manager 目录到 Claude Code 插件目录
```

### 初始化项目

```bash
/context-manager:init-project 电商平台 "B2C电商平台开发"
```

自动创建以下结构：

```
.电商平台/
├── 00-meta/
│   ├── project-manifest.json
│   ├── context-history.json
│   ├── decision-log.json
│   └── issue-tracker.json
├── 01-intake/
├── 02-design/
│   ├── architecture/
│   ├── data-model/
│   └── api-design/
├── 03-implementation/
├── 04-validation/
├── 05-delivery/
└── 99-archive/
```

### 日常使用

```bash
# 查看项目状态
/context-manager:status

# 继续工作（自动接手）
/context-manager:resume

# 查看阶段列表
/context-manager:phase list

# 完成当前阶段，进入下一阶段
/context-manager:phase next

# 记录一个决策
/context-manager:decision add

# 记录一个问题
/context-manager:issue add

# 查看交接文档
/context-manager:handoff view
```

---

## 📋 接力跑协议 (Relay Protocol)

### 阶段执行流程

```
┌──────────┐     ┌──────────┐     ┌──────────┐     ┌──────────┐
│  Receive │ ──→ │ Execute  │ ──→ │ Prepare  │ ──→ │ Handoff  │
│  接收任务 │     │ 执行任务  │     │ 准备交接  │     │ 交接确认  │
└──────────┘     └──────────┘     └──────────┘     └──────────┘
```

### 上下文传递机制

```
Phase A (Agent 1)           Phase B (Agent 2)
     │                            │
     ▼                            ▼
┌──────────┐                ┌──────────┐
│ 工作产出  │                │ 读取 Handoff │
│ 决策记录  │──handoff-out──→│ Document   │
│ 问题清单  │                │            │
└──────────┘                └──────────┘
                                   │
                                   ▼
                            ┌──────────┐
                            │ 完美接手   │
                            │ 继续工作   │
                            └──────────┘
```

---

## 🛠️ 命令列表

| 命令 | 用途 | 示例 |
|------|------|------|
| `init-project` | 初始化新项目 | `/context-manager:init-project 电商平台` |
| `status` | 查看项目状态 | `/context-manager:status` |
| `resume` | 恢复工作 | `/context-manager:resume` |
| `phase` | 阶段管理 | `/context-manager:phase list` |
| `handoff` | 交接文档 | `/context-manager:handoff view` |
| `decision` | 决策记录 | `/context-manager:decision add` |
| `issue` | 问题跟踪 | `/context-manager:issue add` |

---

## 🧩 Skills 说明

### context-orchestrator
核心编排技能，负责任务分解、Agent 调度、上下文管理。

### relay-agent
接力执行技能，专注单一阶段的深度执行和高质量输出。

### context-persistence
上下文持久化技能，自动保存和管理所有关键输出。

### handoff-resolver
快速接手技能，无需用户提供 context 即可自动分析项目状态。

---

## 👤 Agents 说明

### relay-runner
专注于单一阶段的执行 Agent，由 Orchestrator 委派。

### handoff-specialist
专门负责生成交接文档，确保阶段间信息传递完整。

### entropy-guardian
自动维护项目结构整洁，定期清理和优化。

---

## 📁 项目结构规范

```
.{project-name}/
├── 00-meta/                    # 元数据与配置
│   ├── project-manifest.json   # 项目总览
│   ├── context-history.json    # 上下文历史链
│   ├── decision-log.json       # 决策日志
│   ├── issue-tracker.json      # 问题跟踪
│   └── phase-reports/          # 阶段报告
├── 01-intake/                  # 需求输入
│   ├── raw-requirements.md     # 原始需求
│   └── clarified-needs.md      # 澄清后的需求
├── 02-design/                  # 设计阶段
│   ├── architecture/           # 架构设计
│   ├── data-model/             # 数据模型
│   └── api-design/             # 接口设计
├── 03-implementation/          # 实现阶段
│   ├── sprint-XXX/             # 迭代目录
│   └── milestones/             # 里程碑
├── 04-validation/              # 验证阶段
│   ├── test-reports/           # 测试报告
│   └── review-notes/           # 审查记录
├── 05-delivery/                # 交付阶段
│   ├── documentation/          # 文档
│   └── deployment/             # 部署
└── 99-archive/                 # 归档
    └── completed/              # 已完成阶段归档
```

---

## 🔧 配置选项

在 `project-manifest.json` 中配置：

```json
{
  "configuration": {
    "auto-cleanup": true,           # 自动清理临时文件
    "backup-interval": "1h",        # 备份间隔
    "health-check-interval": "24h"  # 健康检查间隔
  }
}
```

---

## 🎨 最佳实践

### 1. 每个阶段必须有交接文档
不要跳过交接，即使当前和下一个阶段由同一个 Agent 执行。

### 2. 及时记录决策
重要决策立即记录到 decision-log.json，避免后期遗忘。

### 3. 遇到阻塞立即记录
发现 blocking issue 时立即用 `/context-manager:issue add` 记录。

### 4. 定期查看状态
使用 `/context-manager:status` 定期查看项目整体状态。

### 5. 利用自动接手
进入项目目录时，让 Handoff Resolver 自动生成摘要，不要手动回忆。

---

## 📝 示例工作流程

### 场景：开发电商平台

```
# 1. 初始化项目
/context-manager:init-project 电商平台 "B2C电商平台"

# 2. 记录原始需求
[Agent 自动记录到 01-intake/raw-requirements.md]

# 3. 完成需求澄清阶段
[Agent 工作...]
[生成交接文档]

# 4. 进入架构设计
/context-manager:phase next
[显示上一阶段交接文档]
[Agent 开始架构设计]

# 5. 记录一个决策
/context-manager:decision add
→ 选择微服务架构
→ 记录决策理由

# 6. 遇到问题
/context-manager:issue add
→ 支付网关API有限流
→ 记录为 medium 优先级

# 7. 完成架构设计
[生成 phase-report.json]
[生成 handoff-out.json]

# 8. 隔天后继续
$ claude
[Handoff Resolver 自动触发]
"检测到电商平台项目，当前在数据模型设计阶段..."
"您想继续设计订单模型吗？"

# 9. 查看状态
/context-manager:status
[显示完整项目状态]
```

---

## 🤝 与现有工具的集成

### 与 Claude Code 原生集成
- 使用内置的 `Task` 工具启动 sub-agent
- 利用 `TodoWrite` 跟踪阶段内任务
- 使用 `Read/Write/Edit` 操作文件

### 可扩展性
- 自定义阶段模板
- 添加自定义 Agent
- 扩展命令集合

---

## 📚 相关资源

- [Anthropic Cookbook - Building Effective Agents](https://github.com/anthropics/anthropic-cookbook/tree/main/patterns/agents)
- [Claude Code Plugins Documentation](https://code.claude.com/docs/en/plugins)
- [Feature Dev Plugin](https://github.com/anthropics/claude-code/tree/main/plugins/feature-dev) - 参考实现

---

## 🐛 故障排除

### 问题：项目目录无法检测
**解决**: 确保在项目根目录运行命令，或显式指定项目名称。

### 问题：context-history.json 损坏
**解决**: 使用备份恢复，或重新初始化（会丢失历史）。

### 问题：阶段交接失败
**解决**: 检查 handoff-out.json 是否存在且格式正确。

---

## 📄 License

MIT License

---

## 🙏 致谢

- 基于 Anthropic 的 [Building Effective Agents](https://www.anthropic.com/research/building-effective-agents) 研究
- 参考 Claude Code 的 [Feature Dev Plugin](https://github.com/anthropics/claude-code/tree/main/plugins/feature-dev) 实现
