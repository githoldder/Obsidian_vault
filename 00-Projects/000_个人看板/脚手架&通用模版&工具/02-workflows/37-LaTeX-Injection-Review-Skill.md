# LaTeX 结构化注入与双重审查 Skill

---

## 1. Skill 定位

本 Skill 用于解决 AI 在论文写作中习惯性输出 Markdown、自由改写 `.tex`、混入非法语法、破坏论文结构的问题。

核心原则：

> AI 不直接输出 Markdown 正文，也不直接自由改 `.tex`。AI 只生成结构化 LaTeX patch 或注入脚本；注入前必须经过 LaTeX 语法审查和论文审稿人审查。

适用场景：

1. 论文段落扩写。
2. 文献综述写入 `.tex`。
3. 章节、小节、图表说明、方法介绍、实验分析的内容注入。
4. 批量替换 LaTeX 正文。
5. 需要防止 Markdown 标题、列表、粗体、代码块污染 LaTeX 源码。

不适用场景：

1. 只是解释 LaTeX 概念。
2. 只是给人阅读的写作建议。
3. 只做纯 Markdown 笔记，不进入 `.tex`。

---

## 2. 强制工作流

```text
写作意图 / 段落目标
-> 证据材料 / 引用来源
-> 生成结构化 LaTeX patch
-> LaTeX 语法审查
-> 论文审稿人审查
-> 注入 .tex
-> 编译 smoke test
-> PDF 回归检查
-> commit / checkpoint
```

硬规则：

1. 禁止直接输出 Markdown 正文作为最终产物。
2. 禁止使用 Markdown 标题 `#`、Markdown 列表、`**加粗**`、代码围栏包裹论文正文。
3. 禁止直接把聊天输出粘贴进 `.tex`。
4. 所有正文变更必须以 `latex_patch`、`insert_after`、`replace_block`、`append_to_section` 等结构化操作描述。
5. 注入前必须通过双重审查。
6. 注入后必须编译，至少完成 smoke test。

如果输出包含以下内容，视为无效，必须重新生成：

```text
Markdown headings, Markdown bullets, fenced code blocks around prose, bold markers, fake cite keys, unescaped %, unescaped &, raw underscores outside math/code context.
```

---

## 3. 标准输入格式

```json
{
  "task_id": "latex-inject-001",
  "target_file": "chapters/03_related_work.tex",
  "target_anchor": "\\subsection{国内外研究现状}",
  "operation": "insert_after",
  "writing_goal": "扩写关于在线课堂系统研究现状的 2 个自然段",
  "evidence": [
    {
      "source": "zhang2024online",
      "claim": "在线课堂系统需要同时处理互动、出勤和媒体同步问题"
    }
  ],
  "style_constraints": [
    "正式学术书面语",
    "避免夸大绝对词",
    "每段必须有明确论点",
    "引用 key 必须存在于 refs.bib"
  ],
  "length": "300-500 Chinese characters"
}
```

---

## 4. 标准输出格式

AI 必须输出 JSON 或等价结构化 patch，不直接输出自由正文。

```json
{
  "target_file": "chapters/03_related_work.tex",
  "operation": "insert_after",
  "anchor": "\\subsection{国内外研究现状}",
  "latex": "在线课堂系统的研究通常围绕教学互动、学习过程记录与媒体传输稳定性展开。已有研究表明，单纯的视频会议能力难以完整覆盖课堂场景中的考勤、提问、反馈和课后追踪需求，因此相关系统往往需要在实时通信之外引入课堂状态管理与学习数据记录机制\\cite{zhang2024online}。\n\n从系统实现角度看，在线课堂平台的复杂性并不只来自音视频传输本身，还来自多角色协同、终端状态差异和异常恢复流程。教师端需要维持课堂节奏，学生端需要获得稳定的参与反馈，管理端则需要对课程、账号和教学数据进行持续维护。因此，需求规格说明应将媒体能力、课堂状态、权限管理和数据记录作为相互关联的功能集合，而不是彼此孤立的模块。",
  "assumptions": [
    "zhang2024online 已存在于 refs.bib"
  ],
  "review_required": [
    "latex_syntax_review",
    "academic_reviewer_review",
    "compile_smoke_test"
  ]
}
```

---

## 5. LaTeX 语法审查 Agent

### 5.1 角色

```text
You are a strict LaTeX syntax reviewer.
Your only job is to detect syntax, escaping, citation, environment, and Markdown contamination issues before content is injected into .tex files.
Do not judge academic quality.
```

### 5.2 审查清单

- [ ] 不含 Markdown 标题 `#`
- [ ] 不含 Markdown 粗体 `**`
- [ ] 不含 Markdown 代码围栏
- [ ] 不含 Markdown 列表污染论文正文
- [ ] `%` 已写作 `\%`
- [ ] `&` 已写作 `\&`，除非在表格对齐环境中
- [ ] `_` 未裸露在普通正文中
- [ ] `{}` 成对闭合
- [ ] `\cite{}` key 非空
- [ ] `\ref{}`、`\label{}` 命名规范
- [ ] 中文正文没有混入未解释的英文占位符
- [ ] 不新增未经确认的宏包或环境
- [ ] 不在正文里插入 inline TikZ 或复杂绘图代码，除非任务明确要求

### 5.3 输出格式

```md
# LaTeX Syntax Review

- Result: PASS / FAIL
- Blocking Issues:
  - ...
- Non-blocking Suggestions:
  - ...
- Fixed Patch:
  - 如可安全修复，给出修复后的 latex 字段
```

---

## 6. 论文审稿人审查 Agent

### 6.1 角色

```text
You are a strict academic reviewer.
Review the LaTeX content for logic, evidence, academic tone, terminology, exaggeration, repetition, and AI-like generic writing.
Do not focus on LaTeX syntax unless it affects meaning.
```

### 6.2 审查清单

- [ ] 每段有明确论点。
- [ ] 每个判断有材料或引用支撑。
- [ ] 没有“显著提升”“完全解决”“100%”“完美”等夸大绝对词。
- [ ] 没有空泛套话。
- [ ] 没有车轱辘话。
- [ ] 术语前后一致。
- [ ] 与章节标题和论文主线一致。
- [ ] 新增内容不偏离原段落目标。
- [ ] 引用不堆砌、不虚构。
- [ ] 读起来像论文正文，而不是 AI 总结。

### 6.3 输出格式

```md
# Academic Reviewer Review

- Result: PASS / FAIL
- Major Issues:
  - ...
- Minor Issues:
  - ...
- Required Rewrite:
  - 如 FAIL，给出符合学术标准的 latex 字段
```

---

## 7. 注入脚本策略

优先使用确定性脚本执行注入，避免人工复制污染格式。

### 7.1 推荐 patch 数据结构

```json
{
  "target_file": "chapters/03_related_work.tex",
  "operation": "insert_after",
  "anchor": "\\subsection{国内外研究现状}",
  "latex": "..."
}
```

### 7.2 支持操作

| operation | 说明 | 风险 |
|---|---|---|
| `insert_after` | 在锚点后插入正文 | 低 |
| `insert_before` | 在锚点前插入正文 | 低 |
| `replace_block` | 替换两个锚点之间内容 | 中 |
| `append_to_section` | 追加到某节末尾 | 中 |
| `delete_block` | 删除内容 | 高，必须 Human review |

### 7.3 注入前检查

1. `target_file` 存在。
2. `anchor` 在文件中唯一出现。
3. `latex` 非空。
4. 双重审查均 PASS。
5. 当前 git 工作区无不相关高风险变更。

---

## 8. 编译回归

注入后至少执行：

```bash
latexmk -pdf main.tex
```

或项目约定的构建脚本：

```bash
./build_manual.sh
make pdf
```

检查项：

- [ ] 编译无 fatal error
- [ ] 无 undefined citation
- [ ] 无 undefined reference
- [ ] PDF 页数变化符合预期
- [ ] 插入段落出现在正确章节
- [ ] 图表、目录、页眉页脚没有异常

---

## 9. 可复用总提示词

```text
You are writing content for a LaTeX academic paper.

Hard rules:
1. Do not output Markdown prose.
2. Do not use Markdown headings, Markdown bullets, bold markers, or fenced code blocks around the paper content.
3. Output only a structured LaTeX patch.
4. The patch must include target_file, operation, anchor, latex, assumptions, and review_required.
5. Escape LaTeX special characters such as %, &, and _ when needed.
6. Do not invent citation keys. Use only citation keys provided in the input.
7. Avoid exaggerated absolute claims and AI-like generic phrasing.
8. The patch must pass LaTeX syntax review and academic reviewer review before injection.

Task:
[写作目标]

Target:
[target_file + anchor + operation]

Evidence:
[材料与 cite keys]

Output:
Return a JSON object containing the LaTeX patch only.
```

---

## 10. 完成标准

一次 LaTeX 内容修改只有满足以下条件才算完成：

1. 结构化 patch 已生成。
2. LaTeX 语法审查 PASS。
3. 论文审稿人审查 PASS。
4. 内容已由脚本或明确 patch 注入。
5. 编译 smoke test 通过。
6. 修改记录写入 checkpoint 或 commit message。
