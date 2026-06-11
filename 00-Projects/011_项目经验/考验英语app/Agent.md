# 考验英语App - AI Agent 工作流配置

> **版本**: v1.0
> **更新日期**: 2026-03-05
> **适用模型**: Claude/Claude Code
> **项目类型**: AI辅助产品管理

---

## 一、项目概述

### 1.1 产品定位
**纯工具、无废话的真题备考引擎**。以95篇真题阅读为唯一素材，通过高频复习算法和全维度数据看板，100%聚焦于提升单词记忆效率与语法写作能力。

### 1.2 核心规则（锁死）
| 规则 | 说明 |
|------|------|
| 每日新词 | ≤ 50，必须来自真题阅读 |
| 单词展示 | 必须带拼写，单个词展示3~10次 |
| 语法来源 | 仅来自"语法3.0"Markdown库 |
| 阅读素材 | 仅限95篇真题 |
| 作文功能 | 只纠错，无模板 |
| AI问答 | 基于本地真题与语法RAG知识库 |

### 1.3 AI辅助目标
- **提升效率**: 将文档编写时间缩短50%
- **保证质量**: 确保输出符合产品管理规范
- **知识沉淀**: 建立可复用的项目模板
- **一致性**: 统一文档风格与术语使用

---

## 二、项目文件夹结构

```
011_项目经验/考验英语app/
├─ .agent/                          # AI配置中心
│   ├─ rules/                       # 项目规则
│   │   ├─ coding-standards.md      # 编码规范
│   │   ├─ doc-standards.md         # 文档规范
│   │   └─ review-checklist.md      # 审核清单
│   ├─ skills/                      # 技能包
│   │   ├─ prd-writer.md            # PRD写作技能
│   │   ├─ data-analyzer.md         # 数据分析技能
│   │   └─ diagram-generator.md     # 图表生成技能
│   └─ workflows/                   # 工作流
│       ├─ requirements-analysis.md # 需求分析流
│       ├─ prd-creation.md          # PRD创建流
│       └─ review-process.md        # 审核流程
├─ analysis/                        # 业务分析区
│   ├─ data-analysis/               # 数据分析
│   │   └─ user-behavior-analysis.md
│   ├─ process-simulation/          # 流程推演
│   │   └─ learning-path-simulation.md
│   └─ scope-analysis/              # 影响范围分析
│       └─ feature-impact-matrix.md
├─ context/                         # 项目上下文
│   ├─ project-brief.md             # 项目简介
│   ├─ user-personas.md             # 用户画像
│   └─ glossary.md                  # 术语表
├─ docs/                            # 参考文档库
│   ├─ 01-reference/                # 参考资料
│   │   ├─ 考研英语大纲.md
│   │   └─ 真题来源说明.md
│   └─ 02-other-docs/               # 其他文档
│       └─ 竞品分析报告.md
├─ drafts/                          # 草稿区
│   ├─ feature-ideas.md             # 功能想法
│   ├─ meeting-notes/               # 会议记录
│   └─ archive/                     # 归档
├─ prds/                            # 正式PRD输出区
│   ├─ 001-BRD.md                   # 商业需求文档
│   ├─ 002-MRD.md                   # 市场需求文档
│   ├─ 003-UCD.md                   # 用户洞察文档
│   ├─ 004-dev-specs.md             # 开发规范
│   ├─ 005-PRD.md                   # 产品需求文档
│   ├─ 006-database-design.md       # 数据库设计
│   ├─ 007-api-design.md            # 接口设计
│   ├─ 008-frontend-prototype.md    # 前端原型
│   ├─ 009-backend-dev.md           # 后端开发
│   ├─ 010-deployment.md            # 上线部署
│   └─ archive/                     # 归档
├─ prompts/                         # 提示词库
│   ├─ requirements-prompt.md
│   ├─ prd-prompt.md
│   ├─ analysis-prompt.md
│   └─ review-prompt.md
└─ templates/                       # 模板库
    ├─ brd-template.md
    ├─ mrd-template.md
    ├─ prd-template.md
    ├─ user-story-template.md
    └─ meeting-template.md
```

---

## 三、AI工作流规则

### 3.1 核心原则

```yaml
agent_principles:
  mvp_first: true              # MVP优先，第一版只做核心功能
  depth_over_breadth: true     # 深度优于广度，每个功能做到极致
  offline_first: true          # 离线优先，核心功能必须离线可用
  cost_control: true           # 极致省钱，严控AI调用成本
  data_safety: true            # 数据不丢失，任何崩溃不丢失进度
```

### 3.2 执行顺序（雷打不动）

```
需求分析 → 概要设计 → 详细设计 → 任务列表拆解 → 逐条执行 → 测试验收
```

**严禁**:
- ❌ 边写代码边想需求
- ❌ 未设计就开始开发
- ❌ 测试前上线

### 3.3 文档输出标准

| 类型 | 格式 | 要求 |
|------|------|------|
| 需求文档 | Markdown | 必须包含版本、日期、变更记录 |
| 流程图 | Mermaid | 统一使用Mermaid语法 |
| 数据表 | Markdown表格 | 表头使用中文，数据对齐 |
| 原型描述 | 结构化的文本 | 使用ASCII艺术或详细描述 |

### 3.4 文件命名规范

```
# 正式文档
XXX-文档类型.md                    # 如: 001-BRD.md, 005-PRD.md

# 草稿文档
[DRAFT]-描述.md                    # 如: [DRAFT]-新功能想法.md

# 归档文档
[日期]-原文件名.md                 # 如: [2026-02-20]-005-PRD-v1.md

# 分析文档
[类型]-主题.md                     # 如: [分析]-用户留存率.md
```

---

## 四、Agent工作流定义

### 4.1 需求分析流 (Requirements Analysis Flow)

```yaml
workflow_id: req-analysis-v1
trigger: 新项目启动或新功能需求
current_status: pending
steps:
  1_context_gathering:
    action: 收集项目背景
    inputs:
      - 项目one-page概述
      - 用户画像
      - 竞品分析
    outputs:
      - context/project-brief.md
    prompt_template: prompts/requirements-prompt.md#context_section

  2_stakeholder_interview:
    action: 利益相关者访谈模拟
    inputs:
      - 项目brief
      - 用户痛点列表
    outputs:
      - analysis/stakeholder-needs.md
    prompt_template: prompts/requirements-prompt.md#interview_section

  3_scope_definition:
    action: 定义功能范围
    inputs:
      - 所有需求输入
      - MVP原则
    outputs:
      - analysis/scope-analysis/feature-impact-matrix.md
      - drafts/feature-ideas.md
    prompt_template: prompts/requirements-prompt.md#scope_section

  4_requirements_doc:
    action: 生成需求文档
    inputs:
      - 功能范围定义
      - 业务流程
    outputs:
      - prds/001-BRD.md
      - prds/002-MRD.md
      - prds/003-UCD.md
    prompt_template: prompts/requirements-prompt.md#output_section

verification:
  - 所有需求是否可追溯
  - 是否符合MVP原则
  - 是否有明确的验收标准
```

### 4.2 PRD创建流 (PRD Creation Flow)

```yaml
workflow_id: prd-creation-v1
trigger: 需求分析完成后
current_status: pending
steps:
  1_structure_design:
    action: 设计PRD结构
    inputs:
      - 需求文档
      - 产品类型（工具类/内容类/社交类）
    outputs:
      - drafts/prd-outline.md
    prompt_template: prompts/prd-prompt.md#structure_section

  2_module_breakdown:
    action: 模块拆解
    inputs:
      - PRD大纲
      - 功能列表
    outputs:
      - drafts/module-specs.md
    prompt_template: prompts/prd-prompt.md#module_section

  3_interaction_design:
    action: 交互设计描述
    inputs:
      - 模块规格
      - 用户流程
    outputs:
      - drafts/interaction-flow.md
    prompt_template: prompts/prd-prompt.md#interaction_section

  4_data_modeling:
    action: 数据建模
    inputs:
      - 功能模块
      - 业务实体关系
    outputs:
      - prds/006-database-design.md
    prompt_template: prompts/prd-prompt.md#data_section

  5_api_design:
    action: API设计
    inputs:
      - 数据模型
      - 功能模块
    outputs:
      - prds/007-api-design.md
    prompt_template: prompts/prd-prompt.md#api_section

  6_prd_assembly:
    action: 组装完整PRD
    inputs:
      - 所有子文档
    outputs:
      - prds/005-PRD.md
    prompt_template: prompts/prd-prompt.md#assembly_section

verification:
  - 是否包含所有必需章节
  - 验收标准是否可量化
  - 技术可行性是否经过评估
```

### 4.3 数据分析流 (Data Analysis Flow)

```yaml
workflow_id: data-analysis-v1
trigger: 需要数据支持决策时
current_status: pending
steps:
  1_data_collection:
    action: 数据收集
    inputs:
      - 数据来源说明
      - 采集指标列表
    outputs:
      - analysis/data-analysis/raw-data.md
    prompt_template: prompts/analysis-prompt.md#collection_section

  2_data_cleaning:
    action: 数据清洗
    inputs:
      - 原始数据
    outputs:
      - analysis/data-analysis/cleaned-data.md
    prompt_template: prompts/analysis-prompt.md#cleaning_section

  3_insight_generation:
    action: 洞察生成
    inputs:
      - 清洗后数据
      - 分析目标
    outputs:
      - analysis/data-analysis/insights.md
    prompt_template: prompts/analysis-prompt.md#insight_section

  4_visualization:
    action: 可视化描述
    inputs:
      - 洞察结果
    outputs:
      - analysis/data-analysis/visualization.md
    prompt_template: prompts/analysis-prompt.md#viz_section

verification:
  - 数据来源是否可靠
  - 分析方法是否合理
  - 结论是否有数据支撑
```

### 4.4 审核流程 (Review Process)

```yaml
workflow_id: review-process-v1
trigger: 文档完成或迭代完成时
current_status: pending
steps:
  1_self_check:
    action: 自检
    inputs:
      - 待审核文档
      - .agent/rules/review-checklist.md
    outputs:
      - drafts/self-check-result.md
    prompt_template: prompts/review-prompt.md#selfcheck_section

  2_peer_review:
    action: 同行评审（AI模拟）
    inputs:
      - 待审核文档
      - 自检结果
    outputs:
      - analysis/review-comments.md
    prompt_template: prompts/review-prompt.md#peer_section

  3_revision:
    action: 修订
    inputs:
      - 评审意见
    outputs:
      - 修订后的文档
    prompt_template: prompts/review-prompt.md#revision_section

  4_final_approval:
    action: 最终确认
    inputs:
      - 修订后文档
    outputs:
      - 正式发布到prds/目录
    prompt_template: prompts/review-prompt.md#approval_section

verification:
  - 所有检查项是否通过
  - 评审意见是否全部处理
  - 版本号是否正确更新
```

---

## 五、提示词模板索引

### 5.1 需求分析提示词

```markdown
# 需求分析提示词结构

## 1. 背景理解
"请基于以下项目背景，提取关键业务目标：
- 项目定位: {project_positioning}
- 目标用户: {target_users}
- 核心问题: {core_problems}"

## 2. 需求挖掘
"基于用户画像和业务目标，列出：
1. 用户痛点（按优先级排序）
2. 功能需求（Must/Should/Could/Won't）
3. 非功能需求（性能/安全/体验）"

## 3. 范围定义
"根据MVP原则，从上述需求中筛选出第一版必须实现的功能：
- 必须满足的核心用户场景
- 可量化的成功指标
- 明确不包含的功能"

## 4. 输出要求
"生成符合以下模板的需求文档：
- BRD模板: templates/brd-template.md
- MRD模板: templates/mrd-template.md
- UCD模板: templates/user-story-template.md"
```

### 5.2 PRD写作提示词

```markdown
# PRD写作提示词结构

## 1. 模块设计
"针对功能模块'{module_name}'，请设计：
- 功能描述（一句话说明）
- 用户故事（作为...我想要...以便...）
- 业务流程（用Mermaid流程图表示）
- 页面流转（用状态图表示）"

## 2. 交互细节
"详细描述以下交互：
| 操作 | 触发条件 | 系统响应 | 异常处理 |
|------|---------|---------|---------|
| ... | ... | ... | ... |"

## 3. 数据结构
"基于功能需求，定义数据实体：
```typescript
interface EntityName {
  // 字段说明
}
```"

## 4. 验收标准
"为每个功能点定义可测试的验收标准：
- [ ] 标准1：量化指标
- [ ] 标准2：边界条件
- [ ] 标准3：错误处理"

## 5. 格式规范
"所有输出必须：
- 使用Markdown格式
- 图表使用Mermaid语法
- 表格数据对齐
- 包含版本号和更新日期"
```

### 5.3 数据分析提示词

```markdown
# 数据分析提示词结构

## 1. 数据理解
"分析以下数据集的特征：
- 数据类型和分布
- 缺失值情况
- 异常值识别"

## 2. 分析方法
"选择适当的分析方法：
- 描述性统计
- 相关性分析
- 趋势分析
- 用户分群"

## 3. 洞察提炼
"从数据中提炼3-5个关键洞察：
1. 洞察1：现象描述 + 数据支撑 + 业务含义
2. 洞察2：...
3. 建议行动项"

## 4. 可视化
"描述应使用的可视化方式：
- 趋势数据：折线图
- 对比数据：柱状图
- 占比数据：饼图/环形图
- 关系数据：散点图/热力图"
```

### 5.4 审核提示词

```markdown
# 审核提示词结构

## 1. 完整性检查
"检查文档是否包含以下必需章节：
- [ ] 文档信息（版本、日期、作者）
- [ ] 变更记录
- [ ] 术语表
- [ ] 功能模块
- [ ] 验收标准"

## 2. 一致性检查
"检查以下内容的一致性：
- 术语使用是否统一
- 数据前后是否矛盾
- 链接引用是否正确"

## 3. 可行性检查
"评估以下内容的技术可行性：
- 功能实现难度
- 数据获取可能性
- 时间资源需求"

## 4. 改进建议
"针对发现的问题，提供具体改进建议：
1. 问题描述
2. 建议修改
3. 优先级（高/中/低）"
```

---

## 六、项目上下文

### 6.1 关键文档链接

| 文档 | 路径 | 说明 |
|------|------|------|
| 项目概述 | [[000-项目one-page]] | 一页纸项目概述 |
| 商业需求 | [[001-商业需求文档 (BRD)]] | 商业模式与市场规模 |
| 市场研究 | [[002-市场与竞争研究 (MRD)]] | 竞品分析与差异化 |
| 用户洞察 | [[003-微观用户洞察 (UCD)]] | 用户画像与行为模型 |
| 开发规范 | [[004-产品开发任务执行规范]] | 技术选型与开发路径 |
| 产品需求 | [[005-功能需求分析文档（PRD）]] | 详细功能设计 |
| 数据库设计 | [[006-数据库设计]] | 表结构详细设计 |
| 接口设计 | [[007-接口文档设计]] | API规范 |
| 前端原型 | [[008-前端原型设计]] | UI/UX设计 |
| 后端开发 | [[009-后端服务开发]] | 服务端实现 |
| 部署维护 | [[010-上线部署&测试&维护]] | 运维规范 |

### 6.2 术语表

| 术语 | 英文 | 定义 |
|------|------|------|
| 间隔重复 | Spaced Repetition | 依据记忆曲线，在即将遗忘时复习，以强化长期记忆 |
| RAG | Retrieval-Augmented Generation | 检索增强生成 |
| MVP | Minimum Viable Product | 最小可行产品 |
| 掌握率 | Mastery Rate | 用户对该单词形成长期记忆的比例 |
| 完成率 | Completion Rate | 当日/周应完成复习任务的实际完成比例 |
| 真题 | Past Exam Papers | 历年考研英语真题 |
| 词单 | Word List | 用户自定义的生词列表 |

### 6.3 技术栈

```yaml
frontend:
  framework: Flutter
  state_management: Riverpod
  local_db: SQLite (sqflite)
  notifications: flutter_local_notifications
  charts: fl_chart
  markdown: flutter_markdown

backend:
  framework: FastAPI (Python)
  rag_framework: LlamaIndex / LangChain
  vector_db: ChromaDB
  llm: Ollama (local) / OpenAI API (fallback)
  deployment: Docker

data_formats:
  articles: JSON
  grammar: Markdown → JSON
  words: CSV → SQLite
```

---

## 七、使用指南

### 7.1 启动新项目

```bash
# 1. 复制项目模板
cp -r templates/project-template ./new-project

# 2. 更新Agent.md中的项目信息
# - 修改项目名称
# - 更新产品定位
# - 调整核心规则

# 3. 初始化上下文
echo "# 项目简介" > context/project-brief.md

# 4. 开始需求分析
# 触发: 需求分析流
```

### 7.2 日常开发流程

```
需求变更 → 更新drafts/ → 分析影响范围 → 更新PRD → 审核 → 归档旧版本
```

### 7.3 文档迭代流程

```
发现需要更新 → 复制到drafts/ → 修改 → 审核 → 移动到prds/ → 归档旧版本
```

---

## 八、审核清单

### 8.1 文档质量检查表

- [ ] **元信息完整**: 版本号、日期、作者、更新记录
- [ ] **结构清晰**: 层级分明，目录可导航
- [ ] **术语一致**: 全文术语使用统一
- [ ] **数据准确**: 引用的数据有来源
- [ ] **图表规范**: Mermaid图表可渲染
- [ ] **链接有效**: 内部链接指向正确
- [ ] **验收可测**: 验收标准可量化、可验证
- [ ] **MVP对齐**: 符合MVP优先原则

### 8.2 代码规范检查表

- [ ] **模块化**: 单文件不超过300行
- [ ] **注释完整**: 函数有输入/输出/副作用说明
- [ ] **类型安全**: 使用TypeScript/Dart类型注解
- [ ] **错误处理**: 所有异步操作有try-catch
- [ ] **数据库迁移**: 变更使用Migration脚本

---

## 九、变更记录

| 版本 | 日期 | 变更内容 | 作者 |
|------|------|----------|------|
| v1.0 | 2026-03-05 | 初始版本，建立AI Agent工作流框架 | AI Assistant |

---

## 十、附录

### 10.1 常用命令

```bash
# 查找文档
grep -r "关键词" prds/ --include="*.md"

# 生成目录树
tree -L 3 -I 'archive'

# 统计文档字数
wc -m prds/*.md
```

### 10.2 快速链接

- [Mermaid语法参考](https://mermaid.js.org/)
- [Markdown规范](https://www.markdownguide.org/)
- [Flutter官方文档](https://docs.flutter.dev/)
- [FastAPI官方文档](https://fastapi.tiangolo.com/)

---

> **使用提示**: 本文档是AI Agent在项目中的操作指南。当开始新任务时，请先阅读相关章节，按照工作流步骤执行，确保输出质量和一致性。
