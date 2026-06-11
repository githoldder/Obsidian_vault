---
type: possible
tags: [可能清单, AI, Agent, 记忆系统, Zettelkasten, Obsidian, MCP]
状态: 已测试
---

**下一步**:: 深入集成到开发工作流
**项目阶段**:: 测试中

# 004_forgetful - 基于 Zettelkasten 的 AI 记忆系统

## 基本信息

| 项目 | 内容 |
|------|------|
| **项目名称** | Forgetful |
| **GitHub** | https://github.com/ScottRBK/forgetful |
| **定位** | 开源 AI Agents 记忆服务，MCP 服务器 |
| **星标数** | 持续增长中 (2026年3月活跃) |
| **编程语言** | Python (FastMCP 框架) |
| **协议** | Model Context Protocol (MCP) |

## 项目说明

Forgetful 是一个为 AI Agents 设计的存储和检索工具。基于 FastMCP 框架构建的 MCP 服务器。一旦连接此服务，MCP 客户端（如 Coding Agents、Chat Bots 或自定义 Agents）可以存储和检索同一知识库中的信息。

### 核心理念：Zettelkasten 原则

Forgetful 与其他记忆型 MCP 服务的不同之处在于其对 Zettelkasten（卢曼卡片盒）原则的坚持：
- **原子性**：每个记忆必须是一个概念（每个笔记一个概念）
- **上下文关联**：记录创建记忆时的上下文、关键词和标签
- **语义嵌入**：自动生成语义向量存储，支持后续检索
- **自动链接**：自动将与现有记忆有相似度的记忆链接，构建知识图谱

> "Forgetful 有点像 AI Agents 的 Obsidian，自动链接推动它们构建知识图谱。"

### 核心功能

| 功能 | 说明 |
|------|------|
| **记忆工具** | 创建、查询、更新、链接、标记过时 |
| **项目工具** | 按上下文/范围组织知识 |
| **实体工具** | 追踪人物、组织、设备；构建知识图谱 |
| **代码片段工具** | 存储可重用的代码片段 |
| **文档工具** | 存储长篇内容（>400词） |
| **技能工具** | 存储程序性知识，支持语义搜索和 SKILL.md 导入/导出 |
| **用户工具** | 个人资料和认证 |

### 技术架构

- **传输机制**：STDIO 或 HTTP（可同时支持）
- **认证**：多种认证方式支持
- **元工具发现**：仅暴露 3 个工具给客户端（42 个工具通过 execute_forgetful_tool 访问）
- **存储**：SQLite（默认，零配置）或 PostgreSQL（生产规模）
- **向量搜索**：FastEmbed 本地运行，无需云调用
- **跨编码器重排**：提升记忆检索精度

### 与 A-Mem 的关联

借鉴了 A-Mem（NeurIPS 2025 论文）的研究成果：
> "正如 others (A-MEM: Agentic Memory for LLM Agents) 所发现的那样，这有助于确保 Agent 以后需要从记忆系统获取相关信息时，返回正确的信息。"

## 适用场景

- 跨多个 AI Agent 共享知识库
- 长期项目的上下文保持
- 个人数字化资产积累
- AI Agent 的持续学习

## 安装和使用

```bash
# 直接运行（无需安装）
uvx forgetful-ai

# 或全局安装
uv tool install forgetful-ai
forgetful

# Docker 部署
cd docker
cp .env.example .env
docker compose -f docker-compose.sqlite.yml up -d
```

### MCP 客户端配置

```json
{
  "mcpServers": {
    "forgetful": {
      "type": "stdio",
      "command": "uvx",
      "args": ["forgetful-ai"]
    }
  }
}
```

## 与其他项目的关系

- **vs VCPToolBox**：Forgetful 是专注记忆的轻量级方案，VCP 是完整生态
- **vs ruflo**：Forgetful 提供记忆层，ruflo 提供任务编排层，可互补
- **vs superpowers**：superpowers 可调用 Forgetful 做记忆增强

## 当前任务

- [x] 项目调研和技术可行性评估
- [x] 本地环境搭建测试
- [ ] 与 OpenCode/Claude Code 集成
- [ ] 构建个人知识图谱

## 进展记录

- 2026-03-27: 完成调研，理解 Zettelkasten 记忆架构
- 2026-03-28: 完成本地测试，理解 MCP 集成方式

## 参考资料

- [GitHub ScottRBK/forgetful](https://github.com/ScottRBK/forgetful)
- [Zettelkasten 维基百科](https://en.wikipedia.org/wiki/Zettelkasten)
- [BMAD Method](https://github.com/bmad-code-org/BMAD-METHOD)
- [A-Mem 论文](https://arxiv.org/abs/2502.12110)
- [Agent Skills 标准](https://agentskills.io)
