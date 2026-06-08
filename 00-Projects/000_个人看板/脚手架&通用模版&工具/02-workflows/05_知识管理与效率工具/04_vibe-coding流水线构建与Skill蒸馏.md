> 基于 Playwright CLI × AI 提炼总结 × Skill 复用 构建零边际成本开发流水线
> 核心目标：**让重复任务只花一次 AI 推理成本，此后零消耗运行**

---

## 一、核心思想：边际成本趋零

### 传统 Vibe Coding 的 Token 痛点

```
每次任务 → 每次 AI 调用 → 每次 Token 消耗
```

| 场景 | Token 消耗 | 问题 |
|------|-----------|------|
| 调试一个 bug | 8,000 tokens | 调试10次 = 80,000 tokens |
| 重复爬取数据 | 5,000 tokens/次 | 每周跑5次 = 每周25,000 tokens |
| 自动化测试跑100次 | 3,000 tokens/次 | ×100 = 300,000 tokens |

**现状：** 大部分 Vibe Coding 工作是**重复的**——调试、爬虫、测试、报表生成。但每次都在重复付费。

### 新范式：AI 蒸馏 + Skill 固化

```
[AI 复杂推理] → [生成确定性脚本] → [脚本零成本无限执行]
```

> **核心洞察：** AI 的价值在于"生成"，不在于"执行"。
> 生成一次，永久免费运行。

---

## 二、流水线架构图

```
┌─────────────────────────────────────────────────────────────────┐
│                        准备阶段                                  │
│   Node.js + playwright-cli Skill → 环境就绪                      │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      复杂任务（原始）                            │
│   例如：爬取竞品价格、自动生成测试用例、执行 UI 自动化操作        │
└─────────────────────────────────────────────────────────────────┘
                              │
                    ┌─────────┴─────────┐
                    ▼                   ▼
         【AI 提炼总结】          【AI 提炼总结】
                    │                   │
                    ▼                   ▼
            ┌───────────────┐  ┌───────────────┐
            │   Task Skill   │  │   Task Skill   │
            │   （固化模板）  │  │   （固化模板）  │
            └───────────────┘  └───────────────┘
                    │                   │
                    └─────────┬─────────┘
                              ▼
              【Skill 指导 → 迭代执行 → 再次提炼】
                              │
                              ▼
              【AI 汇总 → 生成可执行脚本】
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                       最终脚本                                    │
│           token 消耗：0（永久免费，无限次运行）                   │
└─────────────────────────────────────────────────────────────────┘
```

---

## 三、Token 节省效果

### Playwright 基准测试数据

| 方案 | Token 消耗 | 节省比例 |
|------|-----------|---------|
| MCP（Model Context Protocol） | ~114,000 tokens | 基准 |
| CLI（命令行） | ~27,000 tokens | **减少约 4 倍** |
| Skill 流水线（本方法） | ~11,400 tokens | **减少约 10 倍** |

> 数据来源：Playwright 团队基准测试，典型浏览器自动化任务场景。

### 为什么能节省 10 倍？

```
传统模式（每次任务）：
AI 调用 → 解释任务 → 生成代码 → 执行 → 验证 → 修改 → 再执行
         ↑
      每一步都要 Token

Skill 流水线：
AI（1次）→ 生成 Skill → 脚本化 → 无限次执行
           ↑
        一次性成本，后续为 0
```

---

## 四、实操：构建流水线步骤

### Step 1：环境准备（一次性）

```bash
# 安装 playwright-cli
npm install -g playwright-cli

# 初始化浏览器环境
playwright-cli install --skills

# 验证安装成功
playwright-cli --version
```

**Skill 准备完成标志：**
- `playwright-cli` 命令可用
- 浏览器已下载（Chromium 默认）
- 无头模式可正常运行

### Step 2：区分"复杂任务"和"可固化任务"

| 任务类型 | 示例 | 处理方式 |
|---------|------|---------|
| **一次性探索** | 新功能调研、未知领域代码生成 | 直接 AI，消耗 Token |
| **高频重复** | 每日数据爬取、每周测试回归 | 走 Skill 流水线 |
| **规则明确** | 表单自动化、截图对比、页面巡检 | 固化 Skill，永久免费 |

> **决策原则：** 如果一个任务要跑超过 **3 次**，就值得走 Skill 流水线。

### Step 3：AI 提炼 → 生成 Task Skill

**Prompt 模板（发给 AI）：**

```
我需要完成这个任务：[描述原始任务]

请帮我：
1. 提炼出可复用的操作步骤
2. 生成一个 Playwright CLI 命令脚本
3. 输出格式：纯 Bash 脚本，可直接运行
4. 考虑：cookie 持久化、错误重试、日志输出

[原始任务内容]
```

**示例：爬取竞品价格**

```bash
# AI 提炼后生成的脚本
#!/bin/bash
# crawl-competitor-prices.sh
# Token 消耗：0（已固化）

playwright-cli open https://competitor-site.com/products \
  --headed false \
  --persistent ~/projects/.browser-data/competitor

playwright-cli screenshot \
  --output ./data/competitor-prices-$(date +%Y%m%d).png

playwright-cli eval "
  const prices = document.querySelectorAll('.price');
  return Array.from(prices).map(p => p.innerText);
" > ./data/prices-$(date +%Y%m%d).json
```

### Step 4：Skill 指导 → 迭代优化

**Skill 文件结构（Obsidian 笔记）：**

```markdown
# Skill：竞品价格爬取

## 使用场景
每周一运行，获取竞品价格变化

## 执行命令
```bash
./scripts/crawl-competitor-prices.sh
```

## 注意事项
- 需要先登录（cookie 已持久化）
- 失败时自动重试 3 次
- 结果保存在 `data/` 目录

## 更新日志
- 2026-04-19：初始化版本
```

### Step 5：CI/CD 集成（可选）

```yaml
# .github/workflows/weekly-crawl.yml
name: Weekly Price Crawl
on:
  schedule:
    - cron: '0 8 * * 1'  # 每周一早上8点

jobs:
  crawl:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Run Crawl Script
        run: ./scripts/crawl-competitor-prices.sh
      - name: Upload Results
        uses: actions/upload-artifact@v4
        with:
          name: competitor-prices
          path: ./data/*.json
```

---

## 五、浏览器模式对比

| 模式 | 参数 | 后台运行 | 界面 | 适用场景 |
|------|------|---------|------|---------|
| **有头浏览器** | `--headed` | 否 | 有 | 调试、首次开发、手动验证 |
| **无头浏览器** | 默认（不加参数） | 是 | 无 | 生产运行、CI/CD、多任务并行 |

### 常用命令速查

```bash
# 打开网页（有界面，方便调试）
playwright-cli open https://example.com --headed

# 打开网页 + 持久化状态（cookie、登录状态）
playwright-cli open https://example.com --headed --persistent

# 无头模式运行（生产环境）
playwright-cli open https://example.com

# 截图
playwright-cli screenshot --output ./output.png

# 批量执行 JS
playwright-cli eval "document.querySelectorAll('.item').length"
```

---

## 六、与现有 Vibe-Coding 工作流的整合

### 整合点

| 现有流程 | 整合方式 |
|---------|---------|
| Scrum Sprint | 把"高频重复任务"从 Sprint Backlog 提取为 Skill |
| AI Copilot 分配 | 首次遇到高频任务 → AI 生成脚本 → 后续由 CLI 执行 |
| 每日日志 | 记录 Skill 创建情况，追踪 Token 节省量 |

### 建议的 Skill 库目录

```
04-skills/
├── 高效构建vibe-coding流水线（最大化节省token消耗）.md  ← 你在这里
├── 自动化测试skill库/
│   ├── 回归测试skill.md
│   └── UI巡检skill.md
├── 数据采集skill库/
│   ├── 价格爬取skill.md
│   └── 库存监控skill.md
└── 文档生成skill库/
    └── 周报自动生成skill.md
```

---

## 七、常见问题

### Q1：什么时候该用 Skill 流水线？什么时候直接 AI 调用？

```
评估公式：任务重复次数 × 每次 Token 消耗 > AI 生成成本

示例：
- 每天跑1次，每次5000 tokens → 5次 > 生成成本 → 用 Skill
- 只跑1次，每次3000 tokens → 直接 AI

保守标准：重复 ≥ 3 次
```

### Q2：Skill 脚本需要维护吗？

```
需要的。但维护成本远低于重复 AI 调用。

维护场景：
- 网站结构变了 → 更新 selector
- 登录态过期了 → 重新持久化 cookie

维护频率：通常几周一次
```

### Q3：Skill 流水线能覆盖所有场景吗？

```
不能。Skill 流水线适用于：

✅ 规则明确、步骤可枚举
✅ 高频重复、变化缓慢
❌ 探索性任务（需要 AI 每次判断）
❌ 复杂决策（每次上下文不同）
```

---

## 八、行动计划

| 时间 | 动作 | 目标 |
|------|------|------|
| 今天 | 搭建环境 + 安装 playwright-cli | 环境就绪 |
| 本周 | 识别 Sprint 1 中 ≥3 次的任务 | 列出至少 2 个可固化 Skill |
| 本周 | AI 生成首个 Skill 脚本 | 完成第一个零边际成本任务 |
| 每月 | 统计 Token 节省量 | 验证节省是否达到 10x |

---

tags: [vibe-coding, playwright, token优化, 自动化, skill开发]
date: 2026-04-19
version: 1.0