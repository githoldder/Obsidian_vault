# 脚手架 & 通用模板 & 工具

> **个人知识库、场景化工作流、学习地图与智能体技能库的扁平化重构管理系统。**

---

## 1. 扁平化目录结构

本项目目录层级严格限制在3层以内（不含根目录），消除了深度嵌套和冗余文件：

```text
脚手架&通用模版&工具/
├── README.md                          # 统一总入口（本文件）
│
├── 01-mind/                          # 思维模式与习惯养成（6个文件）
│   ├── 01-专注力与脑补对抗.md
│   ├── 02-微习惯与长期主义.md
│   ├── 03-焦虑应对速查.md
│   ├── 04-思维模型图解.png
│   ├── 05-职场管理思维图谱.canvas
│   └── 06-每日学习微习惯模板.md       # 从工作流目录迁移，避免同名冲突
│
├── 02-workflows/                     # 场景化工作流与 SOP（32个文件+1个归档目录）
│   ├── 00-Workflows-Index.md          # 合并：根README与场景化工作流总入口
│   ├── 01-Product-Dev-Pipeline.md     # 场景入口：Vibe Coding 产品全生命周期
│   ├── 02-SE-Doc-Delivery.md          # 场景入口：软件工程文档与交付规范
│   ├── 03-Academic-Writing-Pipeline.md# 场景入口：论文科研写作与 LaTeX
│   ├── 04-Agile-PM-Workflow.md        # 场景入口：敏捷项目管理与问题解决
│   ├── 05-Knowledge-Mgmt-Workflow.md  # 场景入口：知识管理与效率工具
│   ├── 11-Req-Interview-SOP.md        # 合并：需求访谈SOP 与 To B开发SOP
│   ├── 12-UI-UX-Reference-List.md     # UI-UX 审美参照清单
│   ├── 13-UI-Taste-Constraint-Skill.md# UI Taste 约束
│   ├── 14-AI-Figma-SOP.md             # Figma 建模
│   ├── 15-UI-Audit-Anti-Template-Skill.md # UI 减法审查
│   ├── 21-Doc-Engineering-Standard.md # 文档工程规范
│   ├── 31-Paper-Info-Processing.md    # 论文信息处理
│   ├── 32-Industry-Research-SOP.md    # 行研信源
│   ├── 33-Paper-Expansion-Guide.md    # 论文扩写
│   ├── 34-LaTeX-Entry.md              # LaTeX 入口
│   ├── 35-BibTeX-Lifecycle.md         # 参考文献生命周期
│   ├── 36-LaTeX-Template-Troubleshoot.md # 模板与排错
│   ├── 37-LaTeX-Injection-Review-Skill.md # LaTeX 注入审查
│   ├── 41-Project-Three-Layers.md     # 三层规训框架
│   ├── 42-BigTech-Agile-Spec.md       # 大厂敏捷规范
│   ├── 43-Scrum-VibeCoding-Workflow.md# 合并：Vibe-B团队SOP 与 Scrum单人工作流
│   ├── 44-OKRTS-5W-Framework.md       # 目标拆解与根因分析
│   ├── 45-Problem-Solving-Methodology.md # 问题解决方法论
│   ├── 51-PARA-Management.md          # PARA 管理
│   ├── 52-CLI-Token-Saving.md         # CLI Token 节省
│   ├── 53-Git-Version-SOP.md          # Git 版本管理
│   ├── 54-Skill-Distillation.md       # Skill 蒸馏
│   ├── 55-Harness-Engineering.md      # Harness 工程
│   ├── 61-Problem-Solving-Mindmap.canvas # 思维流程图
│   ├── 62-Research-Toolkit.canvas     # 调研工具箱
│   └── 90-archive/                    # 工作流旧版归档（第3层）
│       ├── 91-LEGACY-Paper-Info-Old.md
│       ├── 92-LEGACY-Paper-Expansion-Old.md
│       └── 93-LEGACY-LaTeX-Basics-Old.md
│
├── 03-learningmaps/                  # 学习路线与知识地图（3个文件）
│   ├── 01-AI-PM-Dev-Flow.canvas       # AI产品经理开发流程
│   ├── 02-AI-History-Tech-Evolution.canvas # 人工智能发展史及技术迭代史
│   └── 03-苍穹外卖项目工作流.canvas   # 特定项目（苍穹外卖）知识图谱
│
├── 04-skills/                        # 场景化 Skillpack 智能体技能库（最多3层）
│   ├── README.md                      # 智能体技能总入口（映射规则与配置）
│   ├── codex-skills/                  # 标准可加载 Skill 层（Agent 加载）
│   │   ├── academic-writing-pack/
│   │   ├── project-delivery-pack/
│   │   ├── tech-sharing-pack/
│   │   ├── presentation-demo-pack/
│   │   └── governance-core-pack/
│   ├── 01-academic-writing/           # 学术写作与论文科研场景包
│   ├── 02-project-delivery/           # 项目开发与工程交付场景包
│   ├── 03-tech-sharing/               # 技术分享与分析调研场景包
│   ├── 04-presentation-demo/          # 汇报演示与PPTX模板场景包
│   ├── 05-governance-core/            # 工作区治理与工程防呆场景包
│   └── _archive/                      # 技能库历史归档（第3层）
│       ├── governance-evolution/      # 治理系统演化历史 (taste v0/v2/v3)
│       ├── legacy/                    # 旧版规范与旧版 SVG 提示词
│       └── plans/                     # 历史建仓与治理规划
│
├── 附件/                              # 概念与架构原理解析图（3个文件）
│   ├── 01-AI-Tool-Quadrant.png        # AI工具范式特性四象限
│   ├── 02-AI-Workflow-Construction.png# ai工作流水线构建
│   └── 03-Agent-Principle-Diagram.png # 智能体工作原理图
│
└── _archive/                          # 根级历史归档
    └── 调研报告/
        └── zot-cli调研-20260517.md    # 历史 zot-cli 工具调研报告
```

---

## 2. 核心入口指南

- **日常学习与习惯打卡**：查看 [[01-mind/README]] 下的思维模型与 [[06-每日学习微习惯模板]]。
- **执行业务工作流流程**：查看 [[02-workflows/00-Workflows-Index]] 开始选择你的具体场景（产品/文档/论文/管理/工具）。
- **查看学习知识大图**：查看 [[03-learningmaps/README]] 选择合适的技术路线（AI PM/AI 历史/苍穹外卖）。
- **让 Agent 加载技能执行任务**：参考 [[04-skills/README]] 中的标准 Skill 映射规则。
