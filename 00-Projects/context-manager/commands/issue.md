---
description: 跟踪项目问题和阻塞项
---

# /context-manager:issue

跟踪项目问题：记录、更新、解决项目过程中遇到的问题。

## 用法

```
/context-manager:issue add                    # 添加新问题
/context-manager:issue list                   # 列出所有问题
/context-manager:issue open                   # 列出未解决问题
/context-manager:issue view [问题ID]          # 查看问题详情
/context-manager:issue resolve [问题ID]       # 标记问题已解决
/context-manager:issue close [问题ID]         # 关闭问题
```

## 问题记录字段

```json
{
  "id": "I-{阶段}-{序号}",
  "created-at": "创建时间",
  "resolved-at": "解决时间",
  "phase": "所属阶段",
  "severity": "严重级别 (critical/high/medium/low)",
  "category": "类别 (technical/requirement/resource/external)",
  "title": "问题标题",
  "description": "详细描述",
  "impact": "影响范围",
  "status": "状态 (open/in-progress/resolved/closed)",
  "assigned-to": "负责人",
  "resolution": "解决方案",
  "related-issues": ["相关问题ID"],
  "related-decisions": ["相关决策ID"]
}
```

## 严重级别定义

- **critical**: 阻塞进展，必须立即解决
- **high**: 严重影响，需要优先解决
- **medium**: 一般问题，按计划解决
- **low**: 轻微问题，可延后处理

## 示例

```
/context-manager:issue add
→ 询问问题标题
→ 询问严重级别
→ 询问详细描述
→ 询问影响范围
→ 保存到 issue-tracker.json
→ 如果是 critical，提示立即处理

/context-manager:issue open
→ 显示所有未解决问题
→ 按严重级别排序
→ 提示优先处理 critical/high

/context-manager:issue resolve I-02a-001
→ 询问解决方案
→ 记录解决时间
→ 更新状态为 resolved
→ 如有相关决策，提示记录
```

## 集成

- Relay Agent 在执行中遇到问题时自动记录
- Context Orchestrator 在阶段切换时检查未解决问题
- Handoff Resolver 在接手时显示 open issues 列表
