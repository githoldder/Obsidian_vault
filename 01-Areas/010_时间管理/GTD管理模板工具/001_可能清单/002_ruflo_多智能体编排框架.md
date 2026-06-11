---
type: possible
tags: [可能清单, AI, Agent, 多智能体, ruflo, Claude Code]
状态: 研究中
---

**下一步**:: 评估项目编排能力
**项目阶段**:: 技术调研

# 002_ruflo - 多智能体编排框架

## 基本信息

| 项目 | 内容 |
|------|------|
| **项目名称** | Ruflo |
| **GitHub** | https://github.com/ruvnet/ruflo (重定向至 ruvnet/claude-flow) |
| **定位** | 面向 Claude Code 的多智能体编排框架 |
| **星标数** | ~21.6k ⭐ |
| **核心能力** | 多 Agent 任务分配 + 自主学习 + 成本优化 |

## 项目说明

Ruflo 是一个面向 Claude Code 的多智能体编排框架，让单打独斗的大模型变成分工协作的智能体团队。

### 核心能力

| 能力 | 说明 |
|------|------|
| **多智能体编排** | 将单一 LLM 转变为分工协作的 Agent 团队 |
| **自主学习** | 从每一次任务执行中自主学习，留存成功的执行模式 |
| **灾难性遗忘防护** | 避免传统 Agent 长期运行后的能力退化 |
| **智能任务分配** | 将任务分配至各领域相应 Agent 处理 |
| **成本优化** | API 调用成本可降低高达 75% |
| **能力扩展** | Claude 能力上限提升 2.5 倍 |

### 技术特点

- **任务拆分**：自动将复杂任务拆分为子任务，分配给最适合的 Agent
- **执行模式记忆**：保存成功的执行流程，下次遇到类似任务可复用
- **动态路由**：根据任务类型自动选择最优 Agent 处理

## 适用场景

- 复杂软件项目的多模块开发
- 需要不同专业领域 Agent 协作的任务
- 长期运行的大型 Agent 项目（需要防止能力退化）
- 成本敏感的 AI 应用开发

## 与其他项目的关系

- **vs VCPToolBox**：ruflo 侧重任务层面的多 Agent 编排，VCP 侧重基础设施和记忆层
- **vs superpowers**：两者都服务于 Claude Code，但 superpowers 是技能扩展，ruflo 是多 Agent 框架
- **vs Forgetful**：ruflo 不专注于记忆，而是任务编排；可结合 Forgetful 做记忆层

## 当前任务

- [ ] 找到官方 GitHub 仓库深入研究
- [ ] 理解多 Agent 编排机制
- [ ] 评估与现有工作流的整合

## 进展记录

- 2026-03-28: 发现项目，21.6k Stars，关注其多 Agent 编排能力

## 参考资料

- [GitHub ruvnet/ruflo](https://github.com/ruvnet/ruflo)
- [相关报道：Ruflo 开源](https://so.html5.qq.com/page/real/search_news?docid=70000021_00169bb7a7773052)
