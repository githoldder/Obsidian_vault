# 04-skills

本目录用于管理个人技能库、场景化 skillpack、旧资料池和 Codex 可加载技能。

## 入口分工

| 区域 | 用途 | 面向对象 |
|---|---|---|
| `codex-skills/` | 标准 Codex/Anthropic skill 根目录，可同步到 `~/.codex/skills` 或 `~/.agents/skills` | Agent |
| `01-academic-writing/` 等编号目录 | Obsidian 场景包工作区，保留原始链接、README 和迁移记录 | Human |
| `skills_library/` | 旧原子技能、模板、规范和参考资料池 | Human + Agent reference |
| `SCENARIO_SKILLPACK_REPO_PLAN.md` | 建仓、分发、darwin-skill Full/Pure 规划 | Human |
| `audit/` | 熵增/熵减诊断报告 | Human |

## 推荐使用方式

日常调用 Agent 时，优先使用 `codex-skills/` 中的标准技能：

```text
codex-skills/
├── academic-writing-pack/
├── project-delivery-pack/
├── tech-sharing-pack/
├── presentation-demo-pack/
└── governance-core-pack/
```

需要追溯资料来源、模板和旧笔记时，再进入 `skills_library/` 或编号场景包。

## 当前标准技能

| Skill | 场景 | 状态 |
|---|---|---|
| `academic-writing-pack` | 课程作业、实验报告、论文科研写作、LaTeX | P0 |
| `project-delivery-pack` | AI-native 项目交付、PRD、SRS、测试、部署、作品集 | P0 |
| `tech-sharing-pack` | 技术分享、调研资料整理、Obsidian 导出 PDF | P1 |
| `presentation-demo-pack` | PPTX、reveal.js、演讲稿、demo checklist | P1 |
| `governance-core-pack` | skill 治理、熵减、工程防呆、版本与质量审查 | P0 |

## 维护规则

1. 新的可执行技能进入 `codex-skills/<skill-name>/SKILL.md`。
2. `SKILL.md` 保持轻量，只写触发、流程、引用路由和验收。
3. 长文方法论、旧笔记映射、模板说明放入 `references/`。
4. 旧 `skills_library/` 不直接扩张；新增内容优先进入标准 skill。
5. 目录分类靠 skill 名称和 description，不靠多层嵌套。
