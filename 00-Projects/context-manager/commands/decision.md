---
description: 记录、查看或更新项目决策
---

# /context-manager:decision

记录项目决策到决策日志，确保所有重要决定都有据可查。

## 用法

```
/context-manager:decision add                 # 添加新决策
/context-manager:decision list                # 列出所有决策
/context-manager:decision view [决策ID]       # 查看决策详情
/context-manager:decision update [决策ID]     # 更新决策状态
/context-manager:decision pending             # 列出待确认决策
```

## 决策记录字段

```json
{
  "id": "D-{阶段}-{序号}",
  "timestamp": "记录时间",
  "phase": "所属阶段",
  "category": "类别 (scope/technical/design/process)",
  "title": "决策标题",
  "description": "详细描述",
  "context": "决策背景",
  "options-considered": ["考虑的选项"],
  "decision": "最终决策",
  "decision-makers": ["决策者"],
  "status": "状态 (proposed/accepted/rejected/superseded)",
  "consequences": {
    "positive": ["正面影响"],
    "negative": ["负面影响"]
  },
  "reversibility": "可逆性 (high/medium/low)",
  "related-decisions": ["相关决策ID"],
  "supersedes": "替代的旧决策ID"
}
```

## 示例

```
/context-manager:decision add
→ 询问决策标题
→ 询问决策描述
→ 询问考虑的选项
→ 询问最终决策
→ 询问影响
→ 保存到 decision-log.json

/context-manager:decision list
→ 显示所有决策列表
→ 可按阶段/类别筛选

/context-manager:decision pending
→ 显示状态为 "proposed" 的决策
→ 提示用户确认或修改
```

## 集成

- Relay Agent 在阶段完成时自动记录关键决策
- Context Orchestrator 在阶段切换时检查待确认决策
- Handoff Resolver 在接手时显示关键决策摘要
