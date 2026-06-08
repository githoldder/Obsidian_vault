# 场景化工作流入口

这个目录不再按“单个 skill”记忆，而是按真实工作场景编排。每条工作流是一串可连续调用的 skill：先做什么、后做什么、何时 review、何时 commit、何时部署。

---

## 1. 核心工作流总览

| 场景 | 入口 | 适用任务 |
|---|---|---|
| Vibe Coding 产品从 0 到上线 | [[01_vibe-coding产品原型到上线工作流]] | 需求访谈、PRD、UI、原型、GitHub、Vercel、全栈 sprint、CI/CD |
| 软件工程文档交付 | [[02_软件工程文档交付工作流]] | SRS、概要设计、详细设计、测试文档、用户手册、Sprint 文档归档 |
| 论文科研与 LaTeX | [[03_论文科研写作与LaTeX工作流]] | idea、检索、爬取、精读、筛选、扩写、图表、LaTeX 编译 |
| 敏捷项目管理简化版 | [[04_敏捷项目管理与问题解决工作流]] | OKRTS、Scrum、Ralph、5W、问题四象限、团队协作 |
| 知识管理与效率工具 | [[05_知识管理与效率工具工作流]] | PARA、CLI、Git、Skill 蒸馏、Harness、Agent 长任务管理 |

---

## 2. 使用原则

1. 先选场景，不要先选 skill。
2. 一个场景只打开该场景链路里的文件。
3. 原子 skill 是工具，workflow 是路线。
4. 每条 workflow 都要有：输入、产出、调用顺序、review 点、归档点。
5. 如果一个 skill 被多条 workflow 共用，只在 workflow 中引用，不复制内容。

---

## 3. 目录职责

```text
02-workflows/
├── 00_场景化工作流入口/         # 总入口和路线图
├── 01_vibe-coding产品原型到上线/ # 产品、UI、原型、开发、部署
├── 02_软件工程文档与交付/       # 国标/软件工程文档体系
├── 03_论文科研写作与LaTeX/      # 论文生产线
├── 04_项目管理与问题解决/       # 管理、Scrum、OKRTS、5W
└── 05_知识管理与效率工具/       # CLI、Git、Harness、Skill 蒸馏
```

---

## 4. 迁移说明

原 `04-skills` 中的大多数成熟 skill 已按工作流移动到 `02-workflows`。`04-skills/skills_library` 保留为原子技能库和素材库，后续新增内容优先判断：

- 如果是单一能力模板，放 `04-skills/skills_library`。
- 如果是连续工作路线，放 `02-workflows`。
- 如果是某条路线的步骤说明，放对应 workflow 子目录。
