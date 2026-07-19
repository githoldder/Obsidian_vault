# 智能体技能库与场景包索引 (04-skills)

> **核心原则**：统一命名、双层解耦（Agent可加载的标准技能与人类可读的知识场景包解耦），路径深度不超过3层。

---

## 1. 目录结构概览

```text
04-skills/
├── README.md                      # 人类总入口（本文件）
├── codex-skills/                  # 标准可加载 Skill 层（Agent 加载）
│   ├── academic-writing-pack/     # 学术与论文写作技能包
│   ├── project-delivery-pack/     # 项目开发与交付技能包
│   ├── tech-sharing-pack/         # 技术调研与分享技能包
│   ├── presentation-demo-pack/    # 演示与汇报技能包
│   └── governance-core-pack/      # 规范治理与工程防呆技能包
├── 01-academic-writing/           # 学术写作与论文科研场景包
├── 02-project-delivery/           # 项目开发与工程交付场景包
├── 03-tech-sharing/               # 技术分享与分析调研场景包
├── 04-presentation-demo/          # 汇报演示与PPTX模板场景包
├── 05-governance-core/            # 工作区治理与工程防呆场景包
└── _archive/                      # 归档目录
    ├── governance-evolution/      # 治理规范演进历史 (taste v0/v2/v3)
    ├── legacy/                    # 旧版规范与旧版 SVG 提示词
    └── plans/                     # 历史建仓与治理规划
```

---

## 2. 场景包与可加载技能映射

每一编号目录为一个人类可读的 **场景包**，包含参考文献 (`references/`)、通用模板 (`templates/`) 和检查清单 (`checklists/`)，并与 `codex-skills/` 中的标准技能形成映射：

| 编号场景包 | 对应标准 Skill 包 | 包含的核心内容 |
|---|---|---|
| [[01-academic-writing/README]] | `academic-writing-pack` | 学术文档规范、图表与建模、引用文献规范、论文/lab-report-template模板、论文化检查清单 |
| [[02-project-delivery/README]] | `project-delivery-pack` | 软件工程规范、SRS/测试/数据库设计模板、SRS/测试/设计检查清单 |
| [[03-tech-sharing/README]] | `tech-sharing-pack` | CLI工具调研、Agent调研任务库、Valyu-CLI 调研 Skill |
| [[04-presentation-demo/README]] | `presentation-demo-pack` | 汇报演示模板、reveal.js/pptx 相关配置 |
| [[05-governance-core/README]] | `governance-core-pack` | 工程防呆规范、达尔文 Skill 演化管理、解耦型工作区治理系统模板、审计报告 |

---

## 3. 维护规则

1. **Agent 自动加载**：AI 辅助编程或执行任务时，直接加载 `codex-skills/*/SKILL.md` 即可读取动作指令。
2. **人类编辑场景包**：在 `01` 至 `05` 场景包内维护具体的 markdown 笔记、规范细节和模板，与 Skill 配置文件解耦。
3. **保持扁平**：子文件夹最深不得超过2级（如 `01-academic-writing/references/xxx.md`），禁止多层深嵌套。
