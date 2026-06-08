# zot CLI 调研报告

**日期**：2026-05-17 | **调研人**：代可行

---
![[Applications of Artificial Intelligence, Machine Learning, and Deep Learning in Nutrition A Systema.pdf#page=4&rect=65,359,572,766&color=yellow|Applications of Artificial Intelligence, Machine Learning, and Deep Learning in Nutrition A Systema, p.4]]
## 一、zot 是什么

用户日记中描述：`zot是一个结合zotero和agent助手的文献检索管理cli`。

根据 GitHub 搜索，zot 类工具有多个实现，定位为 **Zotero 的命令行客户端**，让 AI Agent 可以通过 CLI 操作 Zotero 文献库（检索/抓取/导出 BibTeX）。

---

## 二、主流 zot/Zotero CLI 工具横向对比

| 工具 | GitHub | ⭐ | 语言 | 核心定位 |
|------|--------|---|------|----------|
| **jbaiter/zotero-cli** | github.com/jbaiter/zotero-cli | 330 | Python | 通用 Zotero CLI |
| **Agents365-ai/zotero-cli-cc** | github.com/Agents365-ai/zotero-cli-cc | 126 | Python | **Claude Code 专用**（SQLite读+API写） |
| **PiaoyangGuohai1/cli-anything-zotero** | github.com/PiaoyangGuohai1/cli-anything-zotero | 50 | Python | MCP server，70+命令，支持Claude/Cursor/ChatGPT |
| **tnajdek/zotero-api-client** | github.com/tnajdek/zotero-api-client | 118 | JS | Zotero API 轻量客户端 |

### 核心推荐

**首选：Agents365-ai/zotero-cli-cc**（126 ⭐，专为 Claude Code 设计）
- SQLite 本地读 + Web API 写
- 与 Claude Code 深度集成
- 文档友好，Claude Code 场景定制

**备选：PiaoyangGuohai1/cli-anything-zotero**（50 ⭐，功能最全）
- MCP 协议支持
- 70+ 命令，覆盖 search/import/PDF/BibTeX/notes
- 适合复杂流水线（同时支持 Claude/Cursor/ChatGPT）

---

## 三、核心功能（以 zotero-cli-cc 为例）

```bash
# 文献检索
zot search "deep learning medical imaging"
zot search "author:zhang year:2024"

# 导出 BibTeX
zot export --bibtex --output refs.bib
zot export --keys zhang2024deep,li2023transformer --bibtex

# 获取文献 metadata
zot info <item_key>

# 全文 PDF 管理
zot pdf <item_key>        # 获取 PDF 路径
zot pdf-add <url>         # 添加 PDF
```

---

## 四、与现有 pipeline 的关系

```
zot（文献检索/管理）
    ↓ .bib 导出
Obsidian + PDF++（深度精读 + 标注）
    ↓ 原子化摘录
Claude Code（文献综述 + .tex 写作）
    ↓
LaTeX（编译输出 PDF）
```

**现有 skills 覆盖情况**：

| Skill | 覆盖内容 | 与 zot 的关系 |
|-------|---------|--------------|
| 13_LaTeX工作流入口 | 四工具流水线框架 | ✅ 已定义 zot 位置 |
| 14_LaTeX参考文献全生命周期 | Zotero → .bib → LaTeX | ✅ 已定义 |
| 16_论文信息获取与处理全流程 | 文献检索/筛选/截取 | ⚠️ 有人工检索，缺 zot CLI 自动化 |
| 17_论文扩写与AI辅助指南 | AI 辅助写作 | ✅ 无需改造 |

---

## 五、zot CLI 调研结论

### 结论

1. **zot 不是pip包**，而是一个 Zotero API 封装工具（Python 写的 CLI 脚本），GitHub 上有多个实现
2. 用户日记中的"zot调研"**核心诉求是**：Zotero CLI → Claude Code → Obsidian 打通
3. 已有 skills 中**缺口在 16_论文信息获取与处理全流程**缺少 zot CLI 自动化部分
4. **zotero-cli-cc** 是最适合用户场景的工具（Claude Code 专用 + SQLite 读取）

### 待补充信息（需用户确认）

- [ ] Zotero 是否已安装？（`brew install --cask zotero`）
- [ ] Zotero 库大小（影响 zot 读取性能）
- [ ] 是否有 Zotero API Key？（BBT 插件可生成，用于 Web API 写操作）
- [ ] 目标论文方向？（影响 zot search 的关键词策略）

---

## 六、下一步建议

**立即可做**：在 16_论文信息获取与处理全流程 中增加"zot CLI 自动化"模块（Phase 2），补充 zotero-cli-cc 安装配置 + Claude Code 集成方法。

**待用户确认后执行**：
1. 安装 zotero-cli-cc
2. 配置 Zotero API Key + BBT cite key 格式
3. 测试 `zot export --bibtex` → Obsidian 路径
4. 与 PDF++ 联动验证