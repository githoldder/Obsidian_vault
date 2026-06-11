---
name: governance-core-pack
description: 达尔文机制、工程防呆、命名版本控制、质量审查、Git 留痕和 skill 演化治理场景包。适用于整理技能库、拆分仓库、迁移目录、沉淀 SOP、审查 skill 质量和维护 darwin-skill Full/Pure 发行结构。
---

# Governance Core Pack

## 触发场景

- 需要整理 skills_library、workflow、prompt、SOP 或模板库。
- 需要判断某个经验应保留为 skill、降级为 reference、合并、归档或脚本化。
- 需要迁移目录、建立仓库、设计命名规范或创建索引。
- 需要在大改前做工程防呆、Git checkpoint 和质量审查。

## 输入

- 待整理的目录、仓库或文件列表。
- 用户的高频场景、维护偏好和发布目标。
- 当前 Git 状态、已有 README、manifest 或 release 结构。

## 输出

- 目录重构方案、迁移清单和风险提示。
- skill 演化建议：保留、合并、归档、脚本化、常驻。
- 命名规则、版本策略、质量检查清单。
- registry 索引、evolution log 和 release 同步建议。

## 执行步骤

1. 先诊断目录结构、文件类型、重复内容和维护风险。
2. 按使用场景拆包，不按历史堆叠拆包。
3. 保留原文件，先建立索引和骨架，再复制迁移。
4. 每个包必须有 README、SKILL、manifest、CHANGELOG。
5. 对高频可复用能力进行 darwin 评分和常驻位判断。
6. 对重复、低频、过长内容提出合并或归档建议。
7. 涉及 Git 或发行版时先看工作区状态，再执行。

## 验收标准

- 目录命名稳定，采用 `两位编号-英文短名`。
- 每个包职责单一，输入输出明确。
- 原始 Obsidian 链接在迁移前不被破坏。
- `darwin-skill` Full/Pure 发行结构不混淆。

