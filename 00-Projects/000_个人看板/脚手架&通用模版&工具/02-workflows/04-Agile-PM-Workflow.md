# 敏捷项目管理与问题解决工作流

---

## 1. 适用场景

用于个人或 1-3 人小团队的轻量敏捷管理：目标拆解、Sprint 规划、Ralph 机制、任务分发、阻塞上报、5W 根因分析、复盘和归档。

---

## 2. 总链路

```text
Object
-> Key Results
-> Tasks
-> Steps
-> Sprint Backlog
-> Execute
-> Blocker 5W
-> Review
-> Retro
-> Next Sprint
```

---

## 3. Skill 串联

| 阶段 | 调用文件 |
|---|---|
| 项目层级与规训 | [[41-Project-Three-Layers]] |
| 敏捷流水线 | [[42-BigTech-Agile-Spec]] |
| 个人 Scrum | [[43-Scrum-VibeCoding-Workflow]] |
| Agent 长任务 | [[55-Harness-Engineering]] |
| 问题解决 | [[45-Problem-Solving-Methodology]] |
| OKRTS + 5W | [[44-OKRTS-5W-Framework]] |

---

## 4. 简化版 Sprint 模板

```md
# Sprint XX

## Object

## Key Results

## Tasks
| Task | Owner | Output | Acceptance | Status |
|---|---|---|---|---|

## Blockers
| Blocker | 5W | Decision | Status |
|---|---|---|---|

## Review
- Done：
- Not Done：
- Risk：
- Next：
```

---

## 5. Ralph 机制规则

1. Human 只关注 Object、KR、Review。
2. Agent 关注 PRD JSON、Task、Step、Tests、Audit。
3. 微观执行只有遇到 blocker 才向 Human 求救。
4. 求救必须使用 5W。
5. 一个 task 完成后必须产生 review 和 commit。
