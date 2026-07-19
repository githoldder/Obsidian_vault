本规范定义 Agent 在个人工作区、轻量任务和长期软件工程项目中的目录结构、执行边界、记忆管理、PRD 同步、交付校验与 Git 审计规则。核心原则是复杂度解耦：简单任务保持 IPO 清爽闭环，长期项目采用 Agent.md、context、PRD 双板、rules、skills、workflows 和自动化门禁组成的工程治理结构。

---

## 第一部分：轻任务 IPO 工作区规范

轻任务指实验报告、课程作业、临时数据处理、一次性脚本、单份交付文档等短周期任务。此类任务不需要完整 Agent 工程治理系统，目标是快速形成输入、处理、输出闭环，并确保最终交付物可以直接被人类复制、编辑和提交。

### 1. 轻任务目录结构

`task-root/ ├── 01-resource/ ├── 02-process/ │ ├── prompt/ │ ├── data/ │ ├── script/ │ ├── figure/ │ └── document/ └── 03-report/`

01-resource/ 存放原始输入，包括用户给出的要求、题目、数据、图片、代码、参考资料和教师模板。Agent 不应在此目录改写原始材料，除非用户明确要求清洗或转换。

02-process/ 存放中间过程。prompt/ 放任务提示词和分阶段 prompt 草稿，data/ 放清洗后的数据和中间表，script/ 放用于分析、转换、绘图、校验的脚本，figure/ 放中间图像或可再生成图表，document/ 放未最终定稿的章节草稿、摘录和改写过程。

03-report/ 存放最终交付物。默认交付 .txt 纯文本，必要时可同时放 .pdf、.docx、.pptx 或压缩包。这里的 .txt 文件必须执行 Anti-MD 校验。

### 2. 轻任务执行顺序

Agent 启动轻任务时，先读取 01-resource/ 中的原始要求和素材，再检查是否存在 requirement、rubric、template、example、评分标准等约束文件。若任务需要脚本处理，则脚本放入 02-process/script/，生成的数据或图表放入对应中间目录。最终只把可交付文件放入 03-report/。

轻任务不强制创建 .agent/、context/、prds/、Agent.md。如果任务中途演变为长期工程项目，再迁移到第二部分的软件工程项目结构。

### 3. 轻任务交付物格式契约

03-report/*.txt 是面向 Word、PDF、课程平台或人工二次编辑的最终文本，默认使用自然段，不使用 Markdown。除非用户明确要求分点展示，否则正文应以段落组织，通过换行、缩进和自然承接词表达层次。

禁止出现在最终 .txt 交付物中的结构级 Markdown 标记包括：

`^#+\s ^[*-]\s ^\d+\.\s \*\*.*?\*\*`

代码围栏反引号也禁止出现在最终 .txt 交付物中。脚本、日志和代码片段应放在 02-process/，不要混入最终段落型报告。

### 4. 轻任务 Git 规则

轻任务不强制 Git commit。若任务位于已有 Git 仓库内，Agent 在提交前必须检查 git status，只提交本次任务相关文件，不使用 git add .。

---

## 第二部分：正规 Agent 软件工程项目结构规范

正规软件工程项目指需要长期维护、跨 Agent 协作、持续迭代、测试部署、PRD 管理、审计交付的软件项目。此类项目采用 LingoBridge 型工程治理结构：Agent.md 是入口，context 是记忆，prds 是需求和执行控制中心，.agent 是规则、技能与工作流操作系统。

### 1. 标准根目录结构

`project-root/ ├── Agent.md ├── README.md ├── .agent/ │ ├── rules/ │ ├── workflows/ │ └── skills/ ├── context/ │ ├── project-brief.md │ ├── directory-map.md │ ├── context.txt │ └── memory.md ├── prds/ │ ├── README.md │ ├── current/ │ ├── md/ │ ├── json/ │ ├── machine/ │ ├── sprints/ │ └── archive/ ├── docs/ ├── analysis/ ├── drafts/ ├── prompts/ ├── templates/ ├── output/ ├── scripts/ ├── tests/ └── source-code-folders/`

Agent.md 是所有 Agent 的第一入口，必须写清项目当前状态、读取顺序、当前 Sprint、禁止修改区域、常用命令、验证门禁和 Git 规则。Agent 进入项目后应先读 Agent.md，再读 context/project-brief.md 和 context/directory-map.md。

README.md 面向人类，说明项目简介、快速开始、目录结构、主要文档入口、测试和部署方式。

.agent/ 是 Agent 操作系统。rules/ 存放长期有效的约束，workflows/ 存放可复用流程，skills/ 存放项目专属技能说明、检查清单、脚本使用说明和领域规范。

context/ 是项目记忆区。project-brief.md 写产品定位、核心闭环、当前目标、非目标；directory-map.md 写目录角色；context.txt 写短期工作现场；memory.md 写长期决策和历史归档。

prds/ 是需求和执行控制中心。current/ 放当前产品边界、最新决策、守卫规则和开放问题；md/ 放人类评审版 Sprint PRD；json/ 放 Agent 执行版任务矩阵；machine/ 放兼容旧 Agent 或工具的机器摘要；sprints/ 放历史或过渡期 Sprint 计划；archive/ 放过期版本。

docs/ 放长期技术文档、规格书、架构说明、部署说明、会议记录和正式项目文档。analysis/ 放非最终推理、调研、数据分析和过程模拟。drafts/ 放未批准的人类想法、需求草稿和交付草稿。prompts/ 放可直接发给子 Agent 的任务提示词。templates/ 放可复用的任务、报告、验收和 prompt 结构模板。output/ 放编译产物、截图、预览和阶段性生成物，默认不是事实源。

### 2. Agent.md 最小内容规范

Agent.md 至少包含以下模块：

`# Agent Guide ## Operating Principle 说明 Agent 进入项目后应先读哪些文件，当前批准的需求入口在哪里。 ## Current State 说明项目当前代码结构、技术栈、主要已完成状态和未完成状态。 ## Work Rules 列出禁止事项、范围边界、文件放置规则、不得扩大的功能范围。 ## Folder Rules 用表格说明关键目录的用途。 ## Audit Gate 说明交付前必须运行的测试、构建、格式扫描、文档渲染或人工检查。`

Agent.md 不应成为超长知识库。细节规则应链接到 .agent/rules/、.agent/workflows/、context/ 或 prds/。

### 3. PRD 双板规范

PRD 使用人类 Markdown 板和 Agent JSON 板分离。Markdown 板表达目标、关键结果和高层任务，JSON 板表达可执行任务、步骤、文件范围、工具、状态和验收方法。

#### 3.1 prds/md 命名规则

命名格式：

`sprintNN-prd-YYMMDD-vX.Y.md`

示例：

`sprint10-prd-260601-v0.1.md`

NN 是 Sprint 编号，YYMMDD 是创建或批准日期，vX.Y 是版本。每个文件必须包含 Last Updated: YYYY-MM-DD HH:MM。

#### 3.2 prds/md 文件内容

prds/md/ 面向人类评审，推荐结构如下：

`# Sprint NN — Title Last Updated: YYYY-MM-DD HH:MM ## Object 一句话说明本 Sprint 的总目标。 ## Key-Results 列出可验证的关键结果，每条 KR 应能通过测试、文档、功能或交付物证明。 ## Tasks 按 Task ID 列出任务，例如 S10-T01、S10-T02。每个任务写清目标、范围、主要交付物和验收口径。 ## Acceptance 写明 Sprint 完成时需要满足的人工验收或自动化验收条件。 ## Walkthrough Sprint 结束后记录完成内容、验证结果、残余风险和是否允许 push。`

Markdown 板不承载细粒度执行步骤，避免人类文档过载。它负责让人快速判断方向是否正确。

#### 3.3 prds/json 命名规则

命名格式与 Markdown 板保持一一对应：

`sprintNN-prd-YYMMDD-vX.Y.json`

同一个 Sprint 的 .md 与 .json 文件名只允许扩展名不同。

#### 3.4 prds/json 文件内容

prds/json/ 是 Agent 执行状态的单一事实源。推荐字段如下：

`{ "sprint": "10", "version": "0.1", "status": "Ready", "lastUpdated": "2026-06-01 19:15", "object": "完成某项可验证目标", "key_results": [ "KR-1: 可验证结果" ], "tasks": [ { "id": "S10-T01", "status": "todo", "topic": "任务主题", "target_dir": "目标目录", "owned_files": [ "允许修改的文件" ], "out_of_scope": [ "禁止顺手修改的范围" ], "inputs": [ "需要读取的资料或文件" ], "tools": [ "需要使用的脚本、命令、技能或 workflow" ], "steps": [ { "step_id": "S10-T01-STEP01", "status": "todo", "action": "具体动作", "description": "步骤说明", "expected_output": "步骤产物" } ], "acceptance_criteria": [ "验收标准" ], "verification": [ "构建、测试、扫描或人工检查命令" ], "handoff_notes": "" } ] }`

Agent 执行任务时只更新 JSON 中的任务状态、步骤状态、验证结果和 handoff_notes。状态值限定为 todo、in_progress、blocked、done。如果人类修改了 prds/md/ 的需求语义，Agent 应重新生成或修正对应 JSON，而不是在执行中让 MD 和 JSON 双向漂移。

### 4. .agent/rules 规范

.agent/rules/ 存放跨 Sprint 长期生效的硬约束。规则文件应按主题拆分，避免一个巨型规则文件吞掉所有上下文。

推荐命名：

`agent-ops-governance.md project-map.md security.md mvp-scope.md deployment-boundaries.md latex-writing-discipline.md adversarial-audit.md coding-standards.md doc-standards.md review-checklist.md`

agent-ops-governance.md 定义人类层与 Agent 层的职责边界、PRD 双板协议、Git 本地 commit 与禁止自动 push 规则。

project-map.md 定义产品形态、核心流程、关键源码路径、部署面和高风险文件。

security.md 定义密钥、.env、上传文件、私有路径、日志输出和远程部署凭据的红线。

mvp-scope.md 或业务边界规则定义哪些功能属于当前范围，哪些功能禁止 Agent 顺手扩展。

latex-writing-discipline.md 定义正式 LaTeX 文档的格式红线，包括禁止 Markdown 泄漏、禁止伪造引用、三线表、图片外置、交叉引用闭环和 PDF 视觉门禁。

adversarial-audit.md 定义对抗式审查机制，用于让执行 Agent 和审核 Agent 分工：执行者产出，审核者从事实、格式、专业性、引用、可追溯性、排版和风险维度挑错。

### 5. .agent/skills 规范

.agent/skills/ 存放项目专属能力说明。每个技能目录必须有 SKILL.md，必要时附带检查清单、脚本说明、参考材料和示例。

推荐命名：

`.agent/skills/ ├── 01_latex_infrastructure/ │ ├── SKILL.md │ └── structured-injection-review.md ├── 02_research_and_sourcing/ │ └── SKILL.md ├── 03_content_and_graphics/ │ └── SKILL.md ├── 04_vibe_coding_engineering/ │ └── SKILL.md └── 05_project_docs_standard/ ├── SKILL.md ├── document-engineering-writing.md ├── document-quality-checklist.md ├── chart-and-modeling.md └── software-engineering-docs.md`

技能不是普通笔记，而是 Agent 执行某类任务前必须读取的操作说明。技能文件应写清适用场景、输入、输出、禁止事项、检查清单和验证方式。

对于文档工程项目，建议至少配置两类技能：一类处理 LaTeX 编译、模板、引用、图表和注入安全；另一类处理内容质量、学术表达、工程文档规范和事实审查。

### 6. .agent/workflows 规范

.agent/workflows/ 存放可重复执行的流程。workflow 不是具体任务，而是某一类任务的标准做法。

推荐命名：

`mvp-implementation.md prototype-vibe-coding.md technical-research.md document-latex-injection-and-delivery.md devops-smoke-test.md release.md`

每个 workflow 至少包含适用场景、需要读取的 skills、执行步骤、硬规则和交付前门禁。

例如正式文档 workflow 应包含：读取 PRD 和参考资料，生成结构化 patch，运行 LaTeX 语法检查，运行内容专业性审核，注入修改，编译 PDF，渲染页面检查留白，检查引用闭环，记录 handoff。

### 7. 文档工程双层门禁

参考 LingoBridge 的实践，正式软件工程文档、论文式报告、LaTeX 生命周期文档应采用双层门禁，而不是只看能否编译。

#### 7.1 第一层：语法与构建门禁

第一层检查文档能否被工具正确处理，重点是机械正确性。

检查项包括：LaTeX 是否编译通过，是否存在 fatal error，是否存在 undefined citation 或 undefined reference，是否存在重复 label，是否有超过阈值的 overfull hbox，表格是否越界，图片路径是否有效，是否有 Markdown 标记泄漏，是否有本地绝对路径泄漏。

如果是 .txt 交付物，则运行 scripts/validate_report.py 检查 Anti-MD。若是 .tex 或 PDF 交付物，则运行对应编译脚本并检查日志。

#### 7.2 第二层：内容专业性与事实门禁

第二层检查文档是否真的可信，重点是内容质量。

检查项包括：论点是否有证据链，引用是否真实可检索，数据是否来自参考资料或代码事实，需求是否可追踪到 PRD，接口和架构描述是否与源码一致，测试计划是否覆盖核心风险，部署说明是否可复现，用户手册是否按真实用户流程组织，是否存在 AI 腔套话、空洞扩写、夸大结论或把假设写成事实。

对于高风险正式文档，建议执行对抗式审核：Executor 负责产出，Auditor 按事实、结构、格式、引用、专业性、可追溯性、交付风险七个维度审查；审核未通过时返回修复清单，超过三轮仍失败则上报人类。

### 8. context 记忆管理规范

context/context.txt 是短期记忆，不是命令日志。它只记录关键技术决策、用户偏好变化、Issue 解决节点、当前阻塞点、重要验证结果和下一步交接信息。

不记录无信息量的成功命令、普通文件读取、重复探索和临时输出。记录格式建议为：

`[Issue] | [Observation] | [Status]`

context/memory.md 是长期记忆，应按日期、Sprint 或主题归档。压缩触发条件包括：context.txt 接近 1000 行、Sprint 结束、关键目标完成、上下文出现重复。压缩后不应完全清空现场，需在 context.txt 保留最近 20 到 50 行活跃上下文。

### 9. 交付物格式契约

不同交付物使用不同格式契约。

.txt 最终报告禁止 Markdown，默认自然段，可直接复制进 Word 或 PDF。

.md 用于 README、PRD、规则、workflow、草稿和审计报告，允许 Markdown。

.json 必须可解析，字段稳定，作为任务执行状态源。

.tex 禁止 Markdown 泄漏，必须使用 LaTeX 原生命令，正式交付前需要编译、日志审查和 PDF 视觉检查。

.pdf、.docx、.pptx 属于最终交付物，必须打开、渲染或截图检查，不能只依赖文件生成成功。

### 10. Git 安全工作流

在任何 commit 前，Agent 必须执行 git status。如果工作区存在用户或其他 Agent 的无关修改，必须隔离当前任务变更，不得混入 commit。

禁止使用：

`git add .`

允许使用：

`git add path/to/file1 path/to/file2 git commit -m "S10-T01: complete target description"`

本地 commit 用于审计留痕。Sprint 未通过 walkthrough 审计前禁止自动 push。push 只能在人类明确授权后执行。

### 11. walkthrough 审计规范

每个 Sprint 或阶段性交付结束时，应生成或者更新 walkthrough.md。该文件用于人类审计，不是夸功总结。

推荐内容包括：本阶段目标、完成的 Task ID、修改文件清单、验证命令和结果、未验证项、残余风险、是否产生 commit、是否允许 push、下一步建议。

如果当前目录不是 Git 仓库，walkthrough 必须明确说明未执行 commit 的原因。