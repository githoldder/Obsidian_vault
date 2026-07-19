# 工作区治理与工程防呆场景包 (05-governance-core)

> **对应标准 Skill**: `codex-skills/governance-core-pack` (用于 Agent 自动加载)

本场景包专注于工作区治理、熵减规则制定、工程操作防呆（Think-Before-Execute）以及 Skill 的持续演化管理。

---

## 1. 结构与文件导航

### 1.1 治理与防呆规范 (references/)
- [[references/think-before-execute-skill]] (工程操作防呆与检查点规范)
- [[references/darwin-skill-evolution]] (达尔文-Skill 演进思想与熵减法则)

### 1.2 系统治理模板 (templates/)
- [[templates/workspace-agent-governance-system]] (解耦型工作区 Agent 规范与治理系统 - Taste v4.0 Sense)

### 1.3 质量审计与检查清单 (audit/)
- [[audit/04-skills-entropy-audit]] (当前工作区的熵增/熵减诊断与审计报告)

---

## 2. 使用方法
- **AI 协作**：加载标准 Skill `codex-skills/governance-core-pack` 对代码库结构、不规范的文件命名以及没有更新的 `context.txt` 进行巡检。
- **熵减规则**：结合 `references/darwin-skill-evolution` 指引，对已经臃肿的技能库做进一步蒸馏和重组。
