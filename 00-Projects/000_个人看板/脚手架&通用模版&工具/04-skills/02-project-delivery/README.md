# 项目开发与工程交付场景包 (02-project-delivery)

> **对应标准 Skill**: `codex-skills/project-delivery-pack` (用于 Agent 自动加载)

本场景包专注于软件工程文档（如需求规格说明书 SRS、设计文档、测试报告）的规范化设计与交付，确保交付物的高质量与系统化。

---

## 1. 结构与文件导航

### 1.1 工程与文档规范 (references/)
- [[references/software-engineering-doc-standards]] (软件工程文档国标体系与写作规范)
- [[references/national-standards-index]] (国家标准索引说明)
- [[references/national-standards-table.csv]] (国家标准编号与名称表数据)
- [[references/doc-naming-version-control]] (项目文档命名与版本控制规范)
- [[references/doc-classification-lifecycle]] (文档分类体系与生命周期管理)
- [[references/needs-doc-pipeline-skill.md]] (需求文档自动化更新流水线 Skill)

### 1.2 通用交付模板 (templates/)
- [[templates/srs-template]] (软件需求规格说明书 SRS 规范模板)
- [[templates/database-design-template]] (database-design-template)
- [[templates/high-level-design-template]] (high-level-design-template)
- [[templates/detailed-design-template]] (detailed-design-template)
- [[templates/testing-report-template]] (软件testing-report-template)
- [[templates/project-retrospective-template]] (项目开发复盘模板)
- [[templates/meeting-minutes-template]] (项目meeting-minutes-template)

### 1.3 质量审计与检查清单 (checklists/)
- [[checklists/srs-checklist]] (软件需求规格说明书审核清单)
- [[checklists/design-doc-checklist]] (概要设计/详细设计文档审核清单)
- [[checklists/testing-doc-checklist]] (测试方案与测试用例审核清单)
- [[checklists/doc-quality-audit-checklist]] (文档工程整体质量审查清单)

---

## 2. 使用方法
- **AI 协作**：加载标准 Skill `codex-skills/project-delivery-pack` 用于自动审查 SRS 文档或根据模板自动生成概要/详细设计文档。
- **模板与清单**：在 `templates/` 下复制相应的文档结构，使用 `checklists/` 下的审查表在交付前进行交叉确认。
