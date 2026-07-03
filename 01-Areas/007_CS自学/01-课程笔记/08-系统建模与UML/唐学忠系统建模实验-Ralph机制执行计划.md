# 唐学忠系统建模实验：Ralph 机制执行计划

更新时间：2026-05-27 00:37
适用范围：系统建模与 UML 实验、EA `.eap` 文件、同一份 Word 实验报告、真实版本记录与阶段复盘

---

## 0. 合规边界

本计划只用于把实验真实做完、真实记录、真实提交。禁止伪造系统时间、伪造 SVN/Git 历史、伪造实验过程、替换他人作品或隐藏非本人完成痕迹。

可借鉴的经验只有三点：

1. 二进制文件不适合靠肉眼 diff 审查，所以必须用人工验收清单和截图/导出物补足证据。
2. Word 必须是同一个报告文件持续演进，所以要从第一版开始维护同一份主文档。
3. Agent 容易在目标、任务、步骤之间跳层，所以必须把 Human 区和 Agent 区分开，并让 Agent 只从结构化任务里取活。

---

## 1. Ralph 机制定义

Ralph 机制 = `Object/KR 由 Human 把控` + `Task/Step 由 Agent 执行` + `阻塞时用 5W 求救` + `每个任务有独立证据和版本记录`。

### 1.1 Human 只关注三件事

Human 不盯微观步骤，只负责：

1. Object：这次实验最终要交什么，标准是什么。
2. Key Result：四次实验各自达到什么阶段性成果。
3. Review：Agent 交付后，按验收清单批阅，发现问题时追问 5W。

Human 不直接管理：

1. 每一步 EA 怎么点。
2. 每个图的局部元素怎么命名。
3. 每次提交前的文件整理。
4. Agent 的执行顺序。

### 1.2 Agent 只关注四件事

Agent 只允许从项目规则和结构化任务中执行：

1. `.agent/`：顶层规则、技能、工作流、阻塞上报模板。
2. `prds/md/`：Human 可读的目标、里程碑、任务说明。
3. `prds/json/`：Agent 可执行的任务、步骤、输入输出和验收标准。
4. `review/`：每个任务完成后的自检、对抗审查和 Human 批阅记录。

### 1.3 阻塞求救规则

Agent 只有遇到以下情况才向 Human 求救：

1. 需求冲突：老师要求、实验指导书、已有文件之间互相矛盾。
2. 信息缺失：缺少题目、评分标准、样例图、报告模板或 EA 操作要求。
3. 工具阻塞：EA 无法打开、`.eap` 损坏、SVN/Git 提交失败、Word 格式异常。
4. 判断型问题：图的建模边界不确定，可能影响评分。
5. 安全/合规风险：任何涉及伪造、代交、时间篡改、隐藏来源的要求。

求救时必须使用 5W，不允许只说“我不会”：

```md
## Blocker
- What：具体卡在哪里？
- Where：发生在哪个文件/图/步骤？
- When：哪个任务执行到哪一步时出现？
- Why1：直接原因是什么？
- Why2：前置条件是什么？
- Why3：流程哪里没有拦住？
- Why4：Agent 缺少什么认知？
- Why5：根问题是什么？
- Need Human：需要 Human 提供什么决定或材料？
```

---

## 2. 实验 Object 与 Key Results

### Object

在系统建模课程中，完成四次真实、可审查、可复盘的实验交付：四次 `.eap` 阶段文件 + 同一份持续演进的 Word 实验报告。最终交付物能解释每次实验做了什么、为什么这样建模、每个模型如何对应题目需求。

### Key Results

| KR | 阶段目标 | 可验收结果 |
|---|---|---|
| KR1 | 建立实验项目骨架 | 本地项目目录、`.agent`、`prds/md`、`prds/json`、`review`、`deliverables` 建立完成 |
| KR2 | 完成实验一建模 | `experiment01.eap`、报告第 1 部分、图导出图片、review 记录齐全 |
| KR3 | 完成实验二建模 | `experiment02.eap`、报告第 2 部分、图导出图片、review 记录齐全 |
| KR4 | 完成实验三建模 | `experiment03.eap`、报告第 3 部分、图导出图片、review 记录齐全 |
| KR5 | 完成实验四与最终整合 | `experiment04.eap`、同一份 Word 最终版、总目录、提交清单、最终 review |

---

## 3. 推荐目录结构

在真实实验项目目录中建立以下结构。建议位置：

`/Users/caolei/Desktop/system-modeling-tangxuezhong/`

```text
system-modeling-tangxuezhong/
├── .agent/
│   ├── AGENTS.md
│   ├── skills/
│   │   ├── uml-modeling.md
│   │   ├── ea-file-check.md
│   │   └── word-report-check.md
│   └── workflows/
│       ├── task-execute.md
│       ├── task-review.md
│       └── blocker-5w.md
├── human/
│   ├── prompts-draft/
│   └── review-inbox/
├── prds/
│   ├── README.md
│   ├── md/
│   │   └── sprint01-prd-260527-v0.1.md
│   └── json/
│       └── sprint01-prd-260527-v0.1.json
├── workbench/
│   ├── ea/
│   ├── word/
│   ├── exports/
│   └── references/
├── review/
│   ├── agent-self-check/
│   ├── adversarial-review/
│   └── human-review/
└── deliverables/
    ├── experiment01/
    ├── experiment02/
    ├── experiment03/
    ├── experiment04/
    └── final-submit/
```

目录规则：

1. `human/` 只放 Human 草稿、临时想法、需要阅读批阅的文件。
2. `.agent/` 只放 Agent 执行规则，不放实验成品。
3. `prds/md/` 给 Human 看，写目标、里程碑、任务。
4. `prds/json/` 给 Agent 执行，写步骤、文件、命令、验收标准。
5. `workbench/` 是工作区，允许反复修改。
6. `deliverables/` 是交付区，只放已通过 review 的版本。

---

## 4. PRD 命名与同步规则

### 4.1 命名规范

格式：

```text
sprint{两位序号}-prd-{YYMMDD}-v{主版本}.{小版本}.md
sprint{两位序号}-prd-{YYMMDD}-v{主版本}.{小版本}.json
```

示例：

```text
sprint01-prd-260527-v0.1.md
sprint01-prd-260527-v0.1.json
```

规则：

1. 每次 Human 产生可量化需求，就更新当前 sprint 的 PRD。
2. 如果只是措辞微调，更新小版本：`v0.1 -> v0.2`。
3. 如果新增实验阶段或改变交付范围，更新主版本：`v0.9 -> v1.0`。
4. 每次更新必须写 `updated_at`，精确到分钟。
5. `.md` 与 `.json` 必须同名同版本。
6. `.md` 更新后，Agent 必须同步 `.json`。
7. `.json` 更新后，Agent 必须回写 `.md` 的任务摘要。

### 4.2 `prds/README.md` 必须声明

```md
# PRD 管理说明

- Human 阅读：`prds/md/`
- Agent 执行：`prds/json/`
- 同步责任：Agent
- 命名格式：`sprint01-prd-260527-v0.1`
- 更新要求：每次需求变更必须记录 `updated_at`
- 执行入口：Agent 只能读取最新版本 json 中 status=pending 的 task
```

---

## 5. Sprint 01 计划

### Sprint 01 Object

建立实验项目骨架，并完成实验一从题目理解到 EA 建模、Word 报告、导出图片、版本提交的闭环。

### Sprint 01 Key Results

| KR | 目标 | 完成定义 |
|---|---|---|
| KR1 | 项目骨架可运行 | 目录、规则、PRD、review 模板全部建立 |
| KR2 | 实验一需求澄清 | 题目、评分点、图类型、交付物清单写入 PRD |
| KR3 | 实验一 EA 建模完成 | `.eap` 能打开，模型元素命名清楚，图能导出 |
| KR4 | Word 报告第 1 部分完成 | 同一份主报告包含实验一：目的、需求、模型说明、图、总结 |
| KR5 | 完成审查和版本记录 | 自检、对抗审查、Human review、真实提交记录齐全 |

---

## 6. Agent 任务拆解

下面是第一轮任务颗粒度。Agent 一次只能领取一个 `task`，完成所有 `steps` 后必须 review，再提交真实版本记录。

### Task 01：建立项目骨架

目标：创建 Ralph 机制所需的项目目录和规则文件。

输入：

1. 本计划。
2. 课程实验要求。
3. 当前项目根目录。

输出：

1. 目录结构完整。
2. `.agent/AGENTS.md`。
3. `prds/README.md`。
4. `prds/md/sprint01-prd-260527-v0.1.md`。
5. `prds/json/sprint01-prd-260527-v0.1.json`。

Steps：

1. 确认项目根目录是否存在。
2. 如果不存在，创建项目根目录。
3. 创建 `.agent/skills`。
4. 创建 `.agent/workflows`。
5. 创建 `human/prompts-draft`。
6. 创建 `human/review-inbox`。
7. 创建 `prds/md`。
8. 创建 `prds/json`。
9. 创建 `workbench/ea`。
10. 创建 `workbench/word`。
11. 创建 `workbench/exports`。
12. 创建 `workbench/references`。
13. 创建 `review/agent-self-check`。
14. 创建 `review/adversarial-review`。
15. 创建 `review/human-review`。
16. 创建 `deliverables/experiment01` 到 `experiment04`。
17. 创建 `deliverables/final-submit`。
18. 写入 `.agent/AGENTS.md` 的执行边界。
19. 写入 `prds/README.md` 的同步规则。
20. 写入 Sprint 01 的 `.md` 和 `.json` 初稿。

验收标准：

1. 所有目录存在。
2. `prds/md` 和 `prds/json` 文件同名同版本。
3. `.agent/AGENTS.md` 明确禁止伪造时间、伪造记录和跳过 review。
4. JSON 能被解析，没有注释和尾逗号。

### Task 02：收集实验材料并建立题目档案

目标：把实验一题目、评分标准、交付要求整理成 Agent 可执行输入。

输入：

1. 老师发布的实验题目。
2. 实验指导书。
3. 课堂练习或样例图。

输出：

1. `workbench/references/experiment01-requirements.md`。
2. PRD 中实验一任务更新。

Steps：

1. 读取实验题目原文。
2. 摘出系统边界。
3. 摘出参与者/角色。
4. 摘出主要业务流程。
5. 摘出异常流程。
6. 摘出必须绘制的 UML 图类型。
7. 摘出 Word 报告必须包含的栏目。
8. 标记所有不确定项。
9. 对不确定项进行 5W 分析。
10. 如果不确定项影响建模边界，向 Human 求救。
11. 如果不影响，写出默认假设。
12. 更新 PRD JSON 的 `assumptions` 字段。

验收标准：

1. 题目原文不丢失。
2. 需求拆解能直接指导建模。
3. 所有假设都可被 Human review。

### Task 03：实验一 EA 建模

目标：在 EA 中完成实验一模型，并导出图片。

输入：

1. `experiment01-requirements.md`。
2. `prds/json` 中的实验一任务。
3. UML 建模技能规则。

输出：

1. `workbench/ea/experiment01.eap`。
2. `workbench/exports/experiment01-usecase.png` 等导出图片。
3. `review/agent-self-check/task03-experiment01-ea.md`。

Steps：

1. 打开 EA。
2. 新建或打开实验项目 `.eap`。
3. 建立 package：`Experiment01`。
4. 根据题目列出 actors。
5. 建立 actor 命名表。
6. 根据业务目标列出 use cases。
7. 建立 use case 命名表。
8. 画第一版用例图。
9. 检查 actor 是否都与至少一个 use case 相连。
10. 检查 use case 是否都能从题目找到依据。
11. 补充 include/extend/generalization，禁止为了“看起来高级”乱加关系。
12. 如果题目要求类图，列实体名词。
13. 为每个类写职责。
14. 添加属性时只写题目直接支持的信息。
15. 添加方法时只写业务行为，不写 UI 操作。
16. 如果题目要求顺序图，选一个主流程。
17. 画 lifeline。
18. 画消息。
19. 检查消息顺序是否符合业务流程。
20. 保存 `.eap`。
21. 关闭后重新打开 `.eap`，确认文件没有损坏。
22. 导出每张图为 PNG。
23. 写自检记录。

验收标准：

1. `.eap` 能关闭后重新打开。
2. 每张图都有标题。
3. 每个模型元素命名清晰，不出现 `Class1`、`UseCase1`、`Actor1`。
4. 图中每个关键元素能追溯到题目原文或明确假设。
5. 导出图清晰可读。

### Task 04：Word 报告第 1 部分

目标：在同一份 Word 主报告中完成实验一章节。

输入：

1. EA 导出图。
2. 实验一需求档案。
3. Agent 自检记录。

输出：

1. `workbench/word/系统建模实验报告.docx`。
2. `review/agent-self-check/task04-experiment01-word.md`。

Steps：

1. 如果主报告不存在，创建主报告。
2. 如果主报告已存在，直接在原文件追加实验一章节。
3. 写实验目的。
4. 写题目需求摘要。
5. 写建模假设。
6. 插入 UML 图。
7. 每张图下方写图注。
8. 每张图后写模型说明。
9. 写遇到的问题。
10. 写解决方法。
11. 写实验总结。
12. 检查标题层级。
13. 检查图片是否清晰。
14. 检查页码、目录或占位目录。
15. 保存 Word。
16. 关闭后重新打开 Word，确认图片不丢失。
17. 导出 PDF 预览，检查版式。

验收标准：

1. 使用同一份主报告持续演进。
2. 实验一章节完整。
3. 图片不变形、不模糊。
4. 报告文字能解释图，而不是只贴图。

### Task 05：对抗审查

目标：让审查 Agent 扮演严格老师，找出可能扣分点。

输入：

1. `.eap`。
2. Word 报告。
3. 导出图。
4. 实验题目。

输出：

1. `review/adversarial-review/task05-experiment01-review.md`。
2. 修复任务列表。

Steps：

1. 检查交付物是否齐全。
2. 检查题目需求是否被覆盖。
3. 检查每个 UML 关系是否合理。
4. 检查是否存在过度建模。
5. 检查是否存在命名不规范。
6. 检查 Word 是否像实验报告，而不是聊天记录。
7. 检查图片是否能独立阅读。
8. 检查是否有未说明的假设。
9. 将问题按 P1/P2/P3/P4 分类。
10. P1/P2 必须修复。
11. P3 视时间修复。
12. P4 快速处理或记录。

验收标准：

1. 至少完成一次自检和一次对抗审查。
2. 所有 P1/P2 问题有处理结果。
3. Human 能直接根据 review 文件批阅。

### Task 06：真实版本记录

目标：一个 task 完成后，留下可回滚、可审查的真实版本记录。

输入：

1. 本 task 修改过的文件。
2. 自检与 review 文件。

输出：

1. 本地 Git commit 或 SVN revision。
2. `review/human-review/task06-version-record.md`。

Steps：

1. 查看变更文件列表。
2. 确认没有临时文件进入提交。
3. 确认没有伪造时间、伪造记录、无关文件。
4. 将本 task 相关文件加入版本管理。
5. 使用真实当前时间提交。
6. commit message 写明 task 编号和交付内容。
7. 在 review 文件中记录提交哈希或 revision。

验收标准：

1. 每个 task 单独提交。
2. 提交信息能看出完成了什么。
3. 一个 sprint 结束后再由 Human 决定是否 push。

---

## 7. PRD JSON 模板

Agent 只能从这个结构中取任务。字段缺失时必须停止执行并修复 PRD，不允许自由发挥。

```json
{
  "sprint_id": "sprint01",
  "version": "v0.1",
  "updated_at": "2026-05-27 00:37",
  "object": "完成系统建模实验一闭环，并建立四次实验可复用的 Ralph 执行机制",
  "human_focus": [
    "确认实验目标",
    "确认里程碑",
    "批阅 review 文件",
    "处理 blocker"
  ],
  "agent_focus": [
    ".agent 规则",
    "prds/json 任务",
    "workbench 执行",
    "review 自检与对抗审查"
  ],
  "key_results": [
    {
      "id": "KR1",
      "name": "项目骨架建立",
      "acceptance": "目录、规则、PRD、review 模板齐全"
    },
    {
      "id": "KR2",
      "name": "实验一完成",
      "acceptance": ".eap、Word 章节、导出图、review、版本记录齐全"
    }
  ],
  "tasks": [
    {
      "id": "T01",
      "name": "建立项目骨架",
      "status": "pending",
      "owner": "agent",
      "inputs": [
        "本执行计划",
        "课程实验要求"
      ],
      "outputs": [
        ".agent/AGENTS.md",
        "prds/README.md",
        "prds/md/sprint01-prd-260527-v0.1.md",
        "prds/json/sprint01-prd-260527-v0.1.json"
      ],
      "steps": [
        "确认项目根目录",
        "创建目录结构",
        "写入 agent 规则",
        "写入 PRD README",
        "写入 md/json 初稿",
        "解析 JSON 验证格式"
      ],
      "acceptance": [
        "目录存在",
        "md/json 同名同版本",
        "JSON 可解析",
        "规则明确禁止伪造记录"
      ],
      "commit_required": true,
      "blocker_policy": "遇到需求冲突、工具失败或合规风险时按 5W 上报"
    }
  ]
}
```

---

## 8. `.agent/AGENTS.md` 规则草案

```md
# AGENTS.md

## 执行边界

1. Agent 只能执行 `prds/json` 中 `status=pending` 的任务。
2. Agent 每次只执行一个 task。
3. Agent 不得伪造时间、伪造提交记录、伪造实验过程。
4. Agent 不得跳过自检、对抗审查和版本记录。
5. Agent 不得把 Human 草稿当作最终事实，必须同步进 PRD 后再执行。

## 文件规则

1. `.eap` 文件必须关闭后重新打开验证。
2. Word 必须维护同一份主报告。
3. 每张 UML 图必须导出 PNG 并写入报告。
4. 每个模型元素必须能追溯到题目或假设。
5. 交付区只放 review 通过的文件。

## 提交规则

1. 一个 task 对应一个本地 commit/revision。
2. sprint 结束前只做本地记录。
3. Human review 通过后，才允许 push 或最终提交。

## 阻塞规则

遇到 blocker，必须用 5W 模板上报，不允许自行编造需求。
```

---

## 9. UML 建模防呆清单

### 9.1 用例图

1. Actor 是系统外部角色，不是数据库、按钮、页面。
2. Use Case 是用户目标，不是 UI 操作。
3. 每个 Actor 至少连接一个 Use Case。
4. 每个 Use Case 必须有业务价值。
5. `include` 表示必然复用。
6. `extend` 表示条件扩展。
7. 泛化只用于同类角色或同类用例。
8. 不为“看起来复杂”添加关系。

### 9.2 类图

1. 类来自业务名词，不来自页面控件。
2. 属性来自题目中明确要求记录的数据。
3. 方法来自业务行为，不写 `clickButton()`。
4. 关联关系必须能解释。
5. 多重性必须保守，不确定就写假设。
6. 聚合/组合谨慎使用，必须说明生命周期关系。

### 9.3 顺序图

1. 只选一个明确业务流程。
2. Lifeline 不要太多，优先 3-6 个。
3. 消息顺序必须符合题目业务。
4. 返回消息可省略，但关键计算结果必须体现。
5. 不把所有用例强行塞进一张顺序图。

### 9.4 状态图

1. 只给有状态变化的核心对象画。
2. 状态是对象稳定阶段，不是动作。
3. 转移条件要写清楚。
4. 初态和终态必须存在。
5. 不确定状态必须写入假设。

---

## 10. Word 报告防呆结构

每次实验章节固定使用以下结构：

```md
## 实验 X：标题

### 1. 实验目的
说明本次实验训练什么能力。

### 2. 题目与需求摘要
用自己的话概括系统边界、角色、业务流程。

### 3. 建模假设
列出题目没有明说但建模必须决定的假设。

### 4. UML 模型
插入图 + 图注 + 模型说明。

### 5. 关键设计说明
说明为什么这样划分角色、类、流程或状态。

### 6. 问题与解决
记录真实遇到的问题和处理方式。

### 7. 实验总结
总结学到的建模方法和本次不足。
```

禁止：

1. 只贴图不解释。
2. 把聊天记录粘进报告。
3. 每次实验新建一份完全无关的 Word。
4. 图片模糊、变形、无标题。

---

## 11. Review 机制

### 11.1 Agent 自检

每个 task 完成后写：

```md
# Agent Self Check

- Task ID：
- 完成文件：
- 是否满足 outputs：
- 是否满足 acceptance：
- 已知风险：
- 需要 Human 注意：
- 下一步建议：
```

### 11.2 对抗审查

审查 Agent 角色设定：

> 你是严格、挑剔、按评分标准扣分的系统建模老师。你的任务不是鼓励，而是找出所有可能导致扣分、无法解释、边界不清、模型关系错误、报告不专业的问题。

输出格式：

```md
# Adversarial Review

## P1 必须修复
- 问题：
- 证据：
- 为什么扣分：
- 修复建议：

## P2 建议修复
- 问题：
- 证据：
- 修复建议：

## P3 可优化
- 问题：
- 建议：

## 通过项
- 已满足：
```

### 11.3 Human Review

Human 只需要回答：

```md
# Human Review

- 是否通过：
- 必须修改：
- 可选修改：
- 新增需求：
- 是否允许进入下一 task：
```

---

## 12. 版本管理策略

### 12.1 本地提交节奏

1. 完成一个 task 后提交一次。
2. 不把多个 task 混在一个提交里。
3. 不把临时文件、缓存文件、Office 锁文件提交进去。
4. sprint 完成后，Human 统一 review。
5. Human 确认合格后，再进行远端 push 或最终归档。

### 12.2 Commit Message 模板

```text
task(T03): complete experiment01 EA modeling

- add use case diagram
- export PNG diagrams
- add agent self-check
```

### 12.3 二进制文件额外证据

因为 `.eap` 和 `.docx` 都不适合直接 diff，必须额外保留：

1. UML 图导出 PNG。
2. Word 导出 PDF。
3. 每次 task 的 self-check。
4. 每次实验的 review 文件。
5. 提交哈希或 revision。

---

## 13. 四次实验推进节奏

| Sprint | 目标 | 交付物 | Review 重点 |
|---|---|---|---|
| Sprint 01 | 项目骨架 + 实验一闭环 | `experiment01.eap`、报告第 1 部分、导出图、review | 机制是否跑通 |
| Sprint 02 | 实验二 | `experiment02.eap`、报告第 2 部分、导出图、review | 图与题目对应是否清楚 |
| Sprint 03 | 实验三 | `experiment03.eap`、报告第 3 部分、导出图、review | 复杂关系是否合理 |
| Sprint 04 | 实验四 + 最终整合 | `experiment04.eap`、完整 Word、最终提交包 | 交付物完整性和一致性 |

每个 Sprint 固定流程：

```text
Human 提需求
-> Agent 更新 prds/md
-> Agent 同步 prds/json
-> Agent 执行一个 task
-> Agent 自检
-> 对抗审查
-> 修复 P1/P2
-> 本地提交
-> Human review
-> 进入下一个 task
```

---

## 14. 最终提交清单

最终提交前检查：

1. 四个 `.eap` 文件都能打开。
2. 同一份 Word 主报告包含四次实验。
3. Word 中每张图都清晰可读。
4. 每次实验都有导出图。
5. 每次实验都有 review 文件。
6. 每个 task 都有真实本地版本记录。
7. `deliverables/final-submit/` 中只有最终文件，没有草稿、缓存和临时文件。
8. 文件命名统一。
9. 报告中的时间、标题、实验序号一致。
10. 没有任何伪造记录或不可解释内容。

建议最终结构：

```text
final-submit/
├── experiment01.eap
├── experiment02.eap
├── experiment03.eap
├── experiment04.eap
├── 系统建模实验报告.docx
├── 系统建模实验报告.pdf
└── 提交说明.md
```

---

## 15. 第一条可执行指令

当 Human 决定启动时，只需要给 Agent 这条指令：

```md
启动唐学忠系统建模实验 Sprint 01。请先建立 Ralph 项目骨架，只执行 `T01 建立项目骨架`，完成后输出 self-check，不要开始 T02。
```

这条指令的目的：强制 Agent 只做一件事，先把机制跑通，再做具体实验，避免一上来就乱画图、乱写报告、乱提交。
