# Vibe Coding 产品原型到上线工作流

---

## 1. 适用场景

从一个模糊产品想法出发，完成需求访谈、PRD、架构选型、UI 设计、AI 原型、GitHub 同步、Vercel 展示、全栈 sprint 开发、测试、review、audit、commit、push、CI/CD 和最终部署。

---

## 2. 总链路

```text
需求访谈/需求分析 draft
-> PRD
-> 架构选型
-> UI 设计 taste brief
-> 竞品截图/参考收集
-> Google AI Stitch 设计
-> Google AI Studio 设计转代码
-> GitHub 同步
-> Vercel 原型展示
-> 软件工程文档输出
-> PRD JSON
-> Sprint
-> Execute
-> Tests
-> Review
-> Audit
-> Commit
-> Push
-> CI/CD
-> Vercel CLI / tccli 部署
```

---

## 3. 阶段与 Skill 串联

| 阶段 | 目标 | 调用文件 |
|---|---|---|
| 01 需求访谈 | 从聊天、想法、甲方表达中抽出真实需求 | [[11-Req-Interview-SOP]] |
| 02 PRD 基线 | 把 draft 转成可执行的 PRD 与需求边界 | [[21-Doc-Engineering-Standard]]、`skills_library/需求文档更新skill.md` |
| 03 项目规训 | 确定项目目录、层级、Agent/Human 边界 | [[41-Project-Three-Layers]]、[[55-Harness-Engineering]] |
| 04 UI Taste | 先定义产品气质、布局、禁用组件 | [[13-UI-Taste-Constraint-Skill]] |
| 05 UI 参考 | 找竞品截图和审美参考 | [[12-UI-UX-Reference-List]] |
| 06 Stitch 设计 | 在 Google AI Stitch 中生成可视化方案 | [[13-UI-Taste-Constraint-Skill]]、[[15-UI-Audit-Anti-Template-Skill]] |
| 07 设计转代码 | 在 Google AI Studio 中把设计转为前端代码 | [[15-UI-Audit-Anti-Template-Skill]] |
| 08 Git 管理 | 同步 GitHub，按任务提交 | [[53-Git-Version-SOP]] |
| 09 原型托管 | Vercel 展示原型，收集反馈 | [[42-BigTech-Agile-Spec]] |
| 10 Sprint 开发 | PRD JSON -> sprint -> execute -> tests -> review | [[43-Scrum-VibeCoding-Workflow]]、[[55-Harness-Engineering]] |
| 11 效率优化 | CLI、脚本、Skill 蒸馏降低 token 和重复劳动 | [[52-CLI-Token-Saving]]、[[54-Skill-Distillation]] |
| 12 部署上线 | Vercel CLI、tccli、CI/CD、DevOps | [[42-BigTech-Agile-Spec]] |

---

## 4. 关键产物

| 阶段 | 产物 |
|---|---|
| 需求 | `draft.md`、访谈纪要、需求澄清问题 |
| PRD | `prd.md`、`prd.json`、需求变更记录 |
| UI | `design.md`、taste brief、竞品截图、Stitch 设计链接 |
| 原型 | GitHub repo、Vercel preview URL |
| Sprint | `sprintXX-prd-YYMMDD-vX.Y.md/json`、task list |
| 开发 | tests、review、audit、commit、push |
| 部署 | CI/CD 记录、Vercel/云函数/腾讯云部署记录 |

---

## 5. 执行硬规则

1. PRD 没有成型前，不进入代码实现。
2. UI 没有 Taste Brief 前，不让 AI 直接生成页面。
3. 原型阶段可以快，但必须有 GitHub 和 Vercel 展示链接。
4. 开发阶段必须遵循 `prd.json -> sprint -> execute -> tests -> review -> audit -> commit -> push`。
5. 一个 task 对应一个提交，Sprint 结束后再合并或推送主线。
6. 每个 Sprint 结束都写一条 checkpoint，格式参考：

```md
[Issue] | None | [Status] Completed Sprint XX successfully

## 1. 完成成果
## 2. 验证结果
## 3. 版本归档
## 4. 下一步
```
