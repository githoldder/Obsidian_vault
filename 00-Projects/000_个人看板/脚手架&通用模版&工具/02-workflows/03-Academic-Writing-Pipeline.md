# 论文科研写作与 LaTeX 工作流

---

## 1. 适用场景

从论文 idea 到检索、爬取、精读、筛选、写作、扩写、图表、BibTeX、LaTeX 模板、编译、PDF 交付的全过程。

---

## 2. 总链路

```text
Idea
-> 研究问题
-> 关键词组
-> 数据库检索
-> 文献爬取/导入 Zotero
-> 粗筛
-> 精读
-> 证据表
-> 论文结构
-> 段落扩写
-> 图表生成
-> BibTeX 同步
-> LaTeX 写作
-> 结构化 LaTeX patch / 注入脚本
-> LaTeX 语法审查
-> 论文审稿人审查
-> 编译排错
-> PDF 审查
```

---

## 3. Skill 串联

| 阶段 | 调用文件 |
|---|---|
| 研究主题调研 | [[32-Industry-Research-SOP]]、[[31-Paper-Info-Processing]] |
| 文献筛选与证据整理 | [[31-Paper-Info-Processing]] |
| 内容扩写 | [[33-Paper-Expansion-Guide]] |
| LaTeX 工作流判断 | [[34-LaTeX-Entry]] |
| BibTeX 与参考文献 | [[35-BibTeX-Lifecycle]] |
| 模板与编译 | [[36-LaTeX-Template-Troubleshoot]] |
| 结构化注入与双重审查 | [[36-LaTeX-Injection-Review-Skill]] |
| 图表与建模规范 | `skills_library/04_国家标准文档工程化写作规范/chart-modeling-standards.md` |
| 引用规范 | `skills_library/04_国家标准文档工程化写作规范/citation-reference-standards.md` |

---

## 4. 关键规则

1. 先证据表，后扩写。
2. 先结构，后润色。
3. 扩写阶段追求质量，降重只是可选后处理。
4. BibTeX 由 Zotero/BBT 作为数据基座，LaTeX 只消费同步结果。
5. 论文正文不允许以 Markdown 自由文本直接注入 `.tex`。
6. 所有正文修改必须先生成结构化 LaTeX patch 或注入脚本。
7. patch 注入前必须通过 LaTeX 语法审查和论文审稿人审查。
8. 每次编译失败都记录错误、原因、修复动作。
9. 图表必须有数据来源、图题、单位、解释文字。
