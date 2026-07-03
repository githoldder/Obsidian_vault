# Personal Knowledge Asset OS 产品 PRD

**初心来源**：`/Users/caolei/Desktop/Obsidian_root/00-Projects/011_项目经验/personal-blog/idea.md`  
**版本日期**：2026-07-03  
**状态**：Living PRD  
**Product Owner**：曹磊  

## 1. Executive Summary

### 1.1 问题陈述

个人能力资产分散在简历 PDF、GitHub 项目、reveal.js/Slidev 演示、Obsidian 笔记、PDF、截图、录屏、开发日志和本地经验库中。传统个人博客只能承载文章，无法系统性证明技术深度，也无法安全地把私有知识库投影到公网。

### 1.2 解决方案

构建一个静态优先的 Personal Knowledge Asset OS：以内容真源为核心，通过构建管线输出简历、项目页、演示页、笔记页、资料库、搜索索引和语义图谱；再通过本地 Admin/API 层实现源同步、预览、审核、发布；最终演进到语义智能实验室。

### 1.3 用户价值

- 对招聘方：快速看到可信的简历、项目、演示和技术证明。
- 对技术同行：可以深入浏览项目细节、学习笔记和知识图谱。
- 对自己：形成持续积累、持续发布、持续进化的个人知识资产系统。

## 2. Problem Definition

| 维度 | 描述 |
|---|---|
| Who | Owner、招聘方、技术同行、未来 Admin 使用者。 |
| What | 需要将简历、项目、笔记、演示和多媒体资产统一成可访问、可验证、可维护的系统。 |
| When | 求职、面试、项目复盘、知识输出、个人资产整理、技术展示。 |
| Where | 公共网站、本地 Admin cockpit、GitHub、Obsidian vault。 |
| Why | 资产丰富但分散，缺少最终展示路线和安全发布边界。 |
| Impact | 如果不解决，许多真实能力沉淀会停留在本地，难以被外部理解和验证。 |

## 3. Goals And Metrics

| 目标 | 指标 | Target |
|---|---|---|
| 公共证明面可用 | 核心路由可访问 | Resume、Projects、Notes、Decks、Graph 路由返回 200。 |
| 简历可信 | PDF 构建与链接 | 网站可访问最新生成的 PDF。 |
| 项目可验证 | 项目详情完整度 | 主要项目包含背景、方案、技术栈、截图/演示和 GitHub 链接。 |
| 知识同步安全 | 隐私泄漏检查 | 无本地绝对路径、token、`obsidian://`、私有源内容外露。 |
| 图谱可读 | 图谱可用性 | 支持 scope、PARA/folder 折叠、节点裁剪与性能控制。 |
| 治理可持续 | 文档同步 | 重大交付后 context/PRD 均被更新。 |

## 4. Product Scope

### 4.1 In Scope

- Resume Studio：结构化简历、PDF 生成、网页展示、未来热更新预览。
- Project Roadshow：GitHub 项目、截图、录屏、文档、演示稿和 PDF 证明链路。
- Obsidian Publishing：Markdown、wiki-link、附件、PDF、Canvas、Excalidraw 静态化。
- Immersive Reader：网页端长文阅读体验和插件噪声清理。
- Semantic Graph：多 scope 图谱、PARA 折叠、未来 embedding/cluster。
- Admin/API：本地 source sync、publish manifest、preview contract、安全边界。
- Semantic Lab：LLM Agent、3D Graph、MediaPipe、WebGPU 实验。

### 4.2 Out Of Scope

- 未经审批的公网写入。
- 浏览器端访问私有 token。
- 远程 LLM/embedding 默认上传私有知识资产。
- 仅依赖 3D/手势的主导航。
- 未经审核的 Obsidian 全量公开。

## 5. Epics And User Stories

### EPIC-01：Resume Studio

**目标**：让简历 PDF 成为第一可信公开资产。

| Story ID | 用户故事 | Priority | 验收标准 |
|---|---|---|---|
| US-01-01 | 作为招聘方，我希望能直接打开简历 PDF，以便快速检查标准职业材料。 | P0 | Resume 页面链接到 `public/assets/resume.pdf`。 |
| US-01-02 | 作为 Owner，我希望简历来自结构化数据，以便后续可维护、可重建。 | P0 | YAML/Typst 管线可以构建 PDF。 |
| US-01-03 | 作为 Owner，我希望未来能实时编辑简历文本并预览 PDF。 | P1 | S10 定义热更新/预览合同。 |

### EPIC-02：Project Roadshow

**目标**：把 GitHub 项目转化成有说服力的展示路线。

| Story ID | 用户故事 | Priority | 验收标准 |
|---|---|---|---|
| US-02-01 | 作为技术评审，我希望看到项目详情页，以便理解背景、方案、技术栈和成果。 | P0 | 每个重点项目都有 `/projects/[slug]/`。 |
| US-02-02 | 作为技术评审，我希望在线预览项目演示文稿，以便不用本地部署也能看展示。 | P1 | Deck index 提供预览/PDF 状态按钮。 |
| US-02-03 | 作为 Owner，我希望截图、录屏、文档可以挂接到项目页，以便形成证据链。 | P1 | 静态附件通过 `/assets/attachments/` 访问。 |

### EPIC-03：Obsidian Knowledge Publishing

**目标**：安全地把本地知识库投影为公开网站。

| Story ID | 用户故事 | Priority | 验收标准 |
|---|---|---|---|
| US-03-01 | 作为读者，我希望 Obsidian 笔记能作为普通网页打开。 | P0 | 笔记路由为 `/notes/[slug]/`，无本地 Obsidian 依赖。 |
| US-03-02 | 作为 Owner，我希望 wiki-link 自动改写为静态路由。 | P0 | `[[Target]]` 和别名能在无歧义时解析。 |
| US-03-03 | 作为 Owner，我希望图片/PDF 附件自动重写并复制到公开资源。 | P0 | 附件路径转为 `/assets/attachments/` 或 `/assets/pdfs/`。 |
| US-03-04 | 作为 Owner，我希望 publish manifest 管理公开边界。 | P0 | S08 定义 manifest schema。 |

### EPIC-04：Immersive Reader And Visual Quality

**目标**：让公开笔记像高质量长文，而不是原始同步文件。

| Story ID | 用户故事 | Priority | 验收标准 |
|---|---|---|---|
| US-04-01 | 作为读者，我希望笔记页有安静、舒展的阅读体验。 | P1 | 采用纸感背景、长文排版和 thought rhythm。 |
| US-04-02 | 作为读者，我不希望看到 Excalidraw 插件原始提示和压缩 JSON。 | P0 | 清理插件警告、`compressed-json`、block id。 |
| US-04-03 | 作为移动端用户，我希望内容和控件不重叠。 | P0 | 核心路由通过响应式检查。 |

### EPIC-05：Semantic Graph And Knowledge Map

**目标**：保留 Obsidian 图谱感，同时提升可读性和性能。

| Story ID | 用户故事 | Priority | 验收标准 |
|---|---|---|---|
| US-05-01 | 作为知识探索者，我希望看到笔记、项目、书籍和演示之间的关系。 | P1 | `/lab/graph` 可读取 `semantic_graph.json`。 |
| US-05-02 | 作为读者，我希望大图可以按 scope/PARA 折叠，避免混乱。 | P1 | 输出 scoped graph 和 `para.json`。 |
| US-05-03 | 作为 Owner，我希望未来支持 embedding 和聚类。 | P2 | S14 包含 chunk、vector、cluster 任务。 |

### EPIC-06：Admin / Source Sync / Publish Ops

**目标**：建立本地 cockpit，支持源文件读取、预览、审核和发布。

| Story ID | 用户故事 | Priority | 验收标准 |
|---|---|---|---|
| US-06-01 | 作为 Owner，我希望有本地 API 合同读取 source tree。 | P0 | S08 API contract 区分 read/write 权限。 |
| US-06-02 | 作为 Owner，我希望 GitHub/Obsidian source adapter 替代硬编码本地路径。 | P0 | source sync config 文档化。 |
| US-06-03 | 作为 Owner，我希望预览 Markdown/PDF/图片/代码/Typst/LaTeX。 | P1 | S10 preview contract 禁止泄漏本地路径。 |

### EPIC-07：Semantic Intelligence And Geek Lab

**目标**：在不破坏稳定网站的前提下，实现高野心实验室。

| Story ID | 用户故事 | Priority | 验收标准 |
|---|---|---|---|
| US-07-01 | 作为 Owner，我希望构建 semantic chunks 和 vector index。 | P2 | 默认不上传私有数据。 |
| US-07-02 | 作为 Owner，我希望探索 3D 知识图谱。 | P2 | Three.js 图谱 opt-in，并保留 2D fallback。 |
| US-07-03 | 作为 Owner，我希望实验 WebGPU 和手势控制。 | P3 | 遵守 lab-input-policy，显式授权。 |
| US-07-04 | 作为 Owner，我希望 LLM Agent 能建议摘要、分类、链接和隐私风险。 | P2 | Agent 输出仅为建议，发布需人工批准。 |

## 6. Functional Requirements

| ID | Requirement | Priority |
|---|---|---|
| FR-01 | 从结构化源数据生成并展示 resume PDF。 | P0 |
| FR-02 | 渲染项目索引和项目详情页。 | P0 |
| FR-03 | 构建 notes/projects/decks/resume 搜索索引。 | P0 |
| FR-04 | 同步选定 Obsidian 笔记为公开 Markdown 页面。 | P0 |
| FR-05 | 改写 wiki-link、图片、PDF 和附件路径。 | P0 |
| FR-06 | 支持 Excalidraw 和 Canvas 的网页预览或安全降级。 | P1 |
| FR-07 | 生成 semantic graph、scoped graph 和 PARA folded graph。 | P1 |
| FR-08 | 提供本地 Admin/API/source sync 合同。 | P1 |
| FR-09 | 支持演示稿预览和 PDF/export 按钮。 | P1 |
| FR-10 | 为 embedding、聚类、3D graph、WebGPU、MediaPipe、LLM Agent 预留实现路径。 | P2 |

## 7. Non-Functional Requirements

| 类型 | 要求 |
|---|---|
| Performance | 静态页面不依赖服务端；图谱需要 scope/folding 降低负载。 |
| Security | 不暴露 token、私有源文件、本地绝对路径。 |
| Privacy | 公共投影必须经过 whitelist/manifest/review gate。 |
| Accessibility | 阅读、项目、简历路径不能依赖 canvas、3D、摄像头或手势。 |
| Reliability | 归档前需要 build/preview/route smoke check。 |
| Maintainability | 重大交付必须更新 context/PRD。 |

## 8. Design Principles

- 首页应该是可用工作台，不是泛泛营销页。
- 笔记阅读应像高质量数字花园长文。
- Admin/操作面板应克制、密集、可扫描。
- 图谱优先可读性，再追求特效。
- 实验性交互必须可关闭、可降级、可回退。

## 9. Risks And Mitigations

| Risk | Impact | Mitigation |
|---|---|---|
| 私有 Obsidian 内容泄漏 | High | publish manifest、denylist、local path stripping、内容审核清单。 |
| 图谱过于混乱 | Medium | scope rail、PARA folding、节点裁剪、聚类、减少动画。 |
| 资产体积过大 | Medium | selective publishing、资产审计、未来 Git LFS/R2。 |
| 实验功能破坏核心站点 | High | 2D/静态路径默认可用，实验室 opt-in。 |
| 本地路径阻塞公开访问 | High | S08 source config、DOM 清理、构建期路径重写。 |

## 10. Release Strategy

1. PM2 `4321` 运行 dev 热更新。
2. PM2 `4322` 运行静态 preview。
3. 执行 build/verify 和核心路由 smoke check。
4. 归档 Git commit。
5. 用户明确要求时推送到 `githoldder/personal-blog`。

## 11. Open Questions

- Obsidian 云端真源最终采用私有 GitHub、公开子集仓库，还是混合 adapter？
- PDF/图片等大资产长期放 Git、Git LFS，还是对象存储？
- S14 首个 embedding 方案使用本地模型、fixture，还是显式授权的远程 provider？
- 哪些项目最优先做成完整 roadshow？
