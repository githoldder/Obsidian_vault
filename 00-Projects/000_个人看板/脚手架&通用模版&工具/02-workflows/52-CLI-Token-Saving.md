# Vibe Coding CLI 工具链与 Token 节省范式

## 核心理念

用 CLI 替代 GUI，将所有操作变为 stdin/stdout。
AI 负责"逻辑大脑"（输出紧凑指令），CLI 负责"高性能执行手"。
目标：将 Token 消耗从"推理级"降到"函数调用级"。

---

## Phase 0：学术论文流水线（核心新增板块）

> **定位**：学术论文读写全自动化流水线，覆盖「文献检索 → 精读 → 综述 → 写作 → 编译」全链路。
> 与 skills 库中 13~17 深度联动：13=入口路由，14=参考文献，16=信息检索，17=扩写润色。

```
┌─────────────────────────────────────────────────────────────────────────┐
│              学术论文流水线（zot → pdf++ → Claude Code → LaTeX）          │
│                                                                         │
│  zot（文献基座）   →  PDF++（精读）  →  Claude Code（逻辑中枢）  →  LaTeX  │
│  文献检索/抓取      批注/导出摘录      综述/润色/代码执行        编译输出PDF │
│       │               │                  │                  │           │
│   .bib file      Markdown笔记         .tex文件             PDF输出    │
└─────────────────────────────────────────────────────────────────────────┘
```

| 阶段 | 工具 | 职责 | 对应 Skill |
|------|------|------|-----------|
| 文献基座 | **zot / Zotero** | 检索/抓取/导出 .bib | Phase 0（本节） |
| 深度精读 | **Obsidian + PDF++** | 批注/原子化摘录/block-link | 13_LaTeX入口 |
| 逻辑执行 | **Claude Code** | 综述/润色/跨文件关联/.tex 写作 | 17_扩写指南 |
| 精密渲染 | **Texifier / TeX Live** | 公式/图表/参考文献/PDF输出 | 13~15_LaTeX |

### 工具速查

| 工具 | 安装 | 核心命令 |
|------|------|---------|
| **zot / zotero-cli-cc** | `pip install zotero-cli-cc`（见 20_zot-调研报告） | `zot search` / `zot export --bibtex` |
| **Valyu CLI** | `npm install -g @valyu/cli` | `valyu search` / `valyu contents <url>` |
| **Perplexity CLI** | `npm install -g perplexity-cli` | `perplexity ask` |
| **Papis** | `brew install papis` | `papis add` / `papis export` |

### 流水线状态检查

```bash
# 检查工具就绪状态
zot --version 2>/dev/null && echo "✅ zot OK" || echo "❌ zot 未装"
papis --version 2>/dev/null && echo "✅ papis OK" || echo "❌ papis 未装"
valyu --version 2>/dev/null && echo "✅ valyu OK" || echo "❌ valyu 未装"
```

---

## Phase 1：信息获取

| 场景 | 工具 | 命令 | 用途 |
|------|------|------|------|
| AI 搜索 | **Perplexity CLI** | `perplexity ask "query"` | 带来源引用的终端搜索 |
| 多源聚合 | **Valyu CLI** | `valyu search` / `valyu answer` | 为 AI 优化的结构化 JSON 输出 |
| 网页内容提取 | Valyu | `valyu contents <url>` | 去除 HTML 噪声，直接吐出正文 |
| 学术文献检索 | **zot / Zotero** | `zot search "关键词"` | 文献检索 + 导出 BibTeX |
| 本地文献管理 | **Papis** | `papis add` / `papis export` | YAML 元数据，AI 友好 |

> 优先用 Valyu/Perplexity CLI 替代浏览器搜索，节省 4~32 倍 Token。
> 学术场景优先 zot + Zotero connector 抓取 metadata，再导出 .bib 供 Claude Code 使用。

---

## Phase 2：文档与需求管理

| 场景 | 工具 | 命令 | 用途 |
|------|------|------|------|
| 云端文档 | Google Workspace CLI (`gws`) | `gws docs create` | 终端读写 Google Docs/Sheets |
| 文档转换 | Pandoc | `pandoc input.md -o output.pdf` | Markdown → PDF/Word/LaTeX |
| 本地文献 | Papis (`papis`) | `papis add` / `papis export` | 文献管理（YAML 元数据，AI 友好） |

---

## Phase 3：开发与代码管理

| 场景           | 工具           | 命令                                 | 用途                     |
| ------------ | ------------ | ---------------------------------- | ---------------------- |
| 结对编程         | **Aider**    | `aider <file>`                     | 自然语言驱动本地多文件修改 + Git 提交 |
| GitHub 管理    | **gh**       | `gh issue create` / `gh pr create` | 终端管理 Issue/PR/Action   |
| 后端数据库        | Supabase CLI | `supabase db reset`                | 本地 Postgres + 鉴权服务     |
| 支付测试         | Stripe CLI   | `stripe listen`                    | 监听本地 Webhook 事件        |
| JSON/YAML 处理 | `jq` / `yq`  | `cat json \| jq '.field'`          | 数据管道字段提取               |

> **核心组合**：`gh` 读 Issue → `aider` 修复代码 → `gh pr create` 合并，全程无需开网页。

---

## Phase 4：测试

| 场景 | 工具 | 命令 | 用途 |
|------|------|------|------|
| E2E 测试 | Playwright CLI | `playwright test` / `codegen` | 无头浏览器录制 + 回放 |
| 数据分析 | DuckDB CLI | `duckdb -c "SQL"` | 终端 SQL 分析 CSV/Parquet |
| 表格探索 | VisiData (`vd`) | `vd file.csv` | 终端 TUI 表格处理 |

---

## Phase 5：部署上线（Layer 3）

### 场景A：国内开发 → 国内上线

```
境内服务器（腾讯云/阿里云）+ nginx 网关（IP 端口映射）
```

| 用途         | 工具                       | 关键命令                                   |
| ---------- | ------------------------ | -------------------------------------- |
| 云资源管理      | **TCCLI**（腾讯云 CLI）       | `tccli cvm DescribeInstances`          |
| 静态网站/云函数部署 | **CloudBase CLI**（`tcb`） | `tcb hosting deploy dist/ -e <env-id>` |
| 一键部署前端框架   | `tcb`                    | `tcb app deploy --framework vite`      |
| 数据任务调度     | **WeData CLI**           | `wedata bundle deploy`                 |
| 容器管理       | Docker Compose           | `docker-compose up -d`                 |
| 进程守护       | PM2                      | `pm2 start ecosystem.config.js`        |
| 域名/备案      | 腾讯云控制台                   | 人工确认资质备案                               |
| CI/CD      | **CODING CI**            | 流水线触发构建部署                              |

> **原型开发最小集**：服务器 + nginx（仅 HTTP，简化备案流程）= 最高效

---

### 场景B：国内开发 → 国外上线

```
代码在境内编写，部署到境外云（Vercel/Cloudflare/Fly.io）
```

| 用途       | 工具                            | 关键命令                           |
| -------- | ----------------------------- | ------------------------------ |
| 前端部署     | **Vercel CLI**                | `vercel --prod`                |
| Edge 部署  | **Cloudflare CLI (Wrangler)** | `wrangler deploy`              |
| 容器化      | Docker                        | `docker build` + `docker push` |
| 境外 CI/CD | GitHub Actions                | 触发远程构建                         |
| 静态站点     | Firebase CLI                  | `firebase deploy`              |

> **场景B 特点**：无需备案，但需注意境内网络访问境外服务的延迟。

---

### 场景对比

| 维度 | 国内上线 | 国外上线 |
|------|---------|---------|
| 备案 | 需 ICP 备案（周期长） | 无需备案 |
| 域名 | 需境内资质 | 自由选择 |
| 工具链 | TCCLI / CloudBase / CODING | Vercel / Wrangler / GitHub Actions |
| 原型速度 | nginx 直连最快 | Vercel 一键最快 |
| 成本 | 云服务器费用 | 免费额度充足 |

---

## 闭环路径：实践 → SOP → 脚本

```
第一步：AI 终端试错
  → 开启 Aider watch 模式，AI 自动捕获错误并修复

第二步：提取神经补丁
  → "回顾刚才成功的 gh + tcb 联调流程，生成 Markdown SOP"

第三步：SOP 脚本化
  → 将 SOP 编写为 Bash/Python 脚本
  → 下次同类任务：直接调用脚本，Token 从"推理级"降到"函数调用级"
```

---

## 图表即代码（Diagrams as Code）

| 工具 | 命令 | 用途 |
|------|------|------|
| Mermaid CLI (`mmdc`) | `mmdc -i input.mmd -o output.png` | 流程图/时序图/类图 |
| D2 | `d2 input.d2 output.svg` | 现代声明式图表 |
| Graphviz (`dot`) | `dot -Tpng input.dot -o output.png` | 架构拓扑图 |

> AI 输出 Mermaid/D2 语法 → CLI 渲染为图片，全程无需打开任何画图工具。

---

## 工具优先级速查

```
必装：      gh, aider, pandoc, jq
论文流水线：zot, papis, valyu, perplexity-cli（Phase 0）
国内部署：  tcb, tccli, docker, pm2
国外部署：  vercel, wrangler, firebase
测试：      playwright, duckdb
图表：      mmdc, d2
```

---

## 关联 Skills（13~17 流水线联动）

| Skill | 定位 | 与本文关系 |
|-------|------|-----------|
| `13_LaTeX工作流入口` | 四工具流水线 + 场景路由 | ✅ 定义 zot 位置 |
| `14_LaTeX参考文献全生命周期` | Zotero → .bib → LaTeX | ✅ 补充 zot 导出链路 |
| `15_LaTeX模板定制与修改` | Texifier/TeX Live 配置 | ✅ 编译层 |
| `16_论文信息获取与处理全流程` | 文献检索/筛选/截取 | ✅ 增补 zot CLI 自动化 |
| `17_论文扩写与AI辅助指南` | AI 辅助扩写/润色 | ✅ Claude Code 写作层 |
| `20_zot-调研报告` | zot/Zotero CLI 工具调研 | ✅ 详细工具对比 |

---

*Last updated: 2026-05-17 | 关联流水线：zot → PDF++ → Claude Code → LaTeX*