# 场景化 Skillpack 建仓规划

## 目标

将 Obsidian 中的 `02-workflows` 与 `04-skills/skills_library` 重新整合为“按常用场景组织的 skillpack”，并用 Git 子仓库独立管理。`darwin-skill` 保留为达尔文机制仓，其中 Full 发行版承载完整机制、作者个人高频 skill、场景包索引和可分发总装版本；Pure 发行版只保留达尔文机制。

## 总体原则

1. 先按场景选包，不先按单个 skill 选文件。
2. `02-workflows` 负责路线图：输入、步骤、调用顺序、review 点、归档点。
3. `skills_library` 负责零件库：模板、检查清单、原子 skill、规范、参考资料。
4. 成熟 skillpack 独立建仓，低频或实验性内容留在 Obsidian 或归档区。
5. `darwin-skill` 的 Full 发行版不直接变成所有子仓库的替代品，而是作为完整分发层和机制总控层。
6. 目录统一采用 `两位编号-英文短名`，例如 `01-academic-writing`，保证 Obsidian、GitHub、终端和文件管理器里的排序一致。

## 三层架构

```text
Obsidian_root/
├── 02-workflows/                       # 场景路线，仍作为个人使用入口
├── 04-skills/
│   ├── skills_library/                 # 原子技能与资料池，后续瘦身为索引
│   ├── 01-academic-writing/            # 子仓库：课程作业、实验报告、论文、LaTeX
│   ├── 02-project-delivery/            # 子仓库：项目文档流与交付作品集
│   ├── 03-tech-sharing/                # 子仓库：技术分享与内部文档
│   ├── 04-presentation-demo/           # 子仓库：PPT、reveal.js、演讲稿
│   └── 05-governance-core/             # 子仓库：达尔文机制与治理底座

darwin-skill/
├── main                                # 机制主线与 Full 发行版源码根目录
├── skills/                             # 可加载的成熟 skills
├── library/                            # 长文 reference 与原始资产
├── registry/                           # 索引、评分、演化记录
├── dist/darwin-skill-v*.zip            # Full 发行包
├── dist/darwin-skill-pure-v*.zip       # Pure 发行包
└── packages/pure/                      # 只含达尔文机制的纯净版源码
```

## 场景包拆分

| 目录名 | 建议仓库名 | 对应需求 | 主要来源 | 建仓优先级 |
|---|---|---|---|---|
| `01-academic-writing` | `01-skillpack-academic-writing` | 课程作业、实验报告、毕业论文、LaTeX 模板包 | `02-workflows/03_论文科研写作与LaTeX`、`skills_library/04_国家标准.../03/06/07/templates/学术类` | P0 |
| `02-project-delivery` | `02-skillpack-project-delivery` | PRD/MRD/BRD、SRS、设计、测试、部署、用户手册、项目展示集 | `02-workflows/01_vibe-coding产品原型到上线`、`02_软件工程文档与交付`、`04_项目管理与问题解决`、`skills_library/03_需求文档类`、软件工程类模板 | P0 |
| `03-tech-sharing` | `03-skillpack-tech-sharing` | 群聊技术分享、技术文档、Obsidian 导出 PDF、内部分享材料 | `02-workflows/05_知识管理与效率工具`、`skills_library/01_调研分析类`、图表与建模规范 | P1 |
| `04-presentation-demo` | `04-skillpack-presentation-demo` | PPTX、reveal.js、演讲稿、checklist、用户指南 | 现有 checklist、未来新增 reveal.js 模板、演讲稿模板、用户指南模板 | P1 |
| `05-governance-core` | `05-skillpack-governance-core` | 达尔文机制、工程防呆、命名、版本控制、质量审查 | `skills_library/02_工程安全类`、文档命名与版本控制、文档质量审查清单、`darwin-skill` core skills | P0 |

## 每个子仓库的标准结构

```text
01-package-name/
├── README.md               # 人读入口：解决什么场景
├── SKILL.md                # Agent 入口：触发场景、输入、输出、步骤、验收
├── workflows/              # 场景路线，从 02-workflows 迁入或引用
├── templates/              # 可直接复制使用的模板
├── checklists/             # 质量验收清单
├── references/             # 标准、资料索引、长文 reference
├── examples/               # 示例产物
├── scripts/                # 可脚本化工具
├── registry/
│   ├── manifest.json       # 包内资产清单
│   └── evolution_log.md    # 演化记录
└── CHANGELOG.md
```

## `darwin-skill` Full 发行版的角色

GitHub 上 `githoldder/darwin-skill` 当前不是用 `full` 分支表示全量版，而是用 release asset 表示发行形态：

- Full：`darwin-skill-v1.0.0.zip`
- Pure：`darwin-skill-pure-v1.0.0.zip`

因此 Full 发行版建议继续由仓库根目录打包生成，并承担四件事：

1. 保留达尔文机制：扫描、评分、归档、蒸馏、脚本化、熵检测。
2. 收纳高频成熟 skill 的 `SKILL.md` 版本，作为可安装 Full 版。
3. 在 `registry/skillpacks_index.json` 中登记外部子仓库。
4. 将 Obsidian 子仓库中的成熟资产同步到 `library/` 或 `skills/`，但不替代原始场景包仓库。

建议新增索引文件：

```text
darwin-skill/
└── registry/
    └── skillpacks_index.json
```

示例字段：

```json
{
  "name": "02-project-delivery",
  "scene": "完整项目文档交付与作品集展示",
  "repo": "git@github.com:githoldder/02-skillpack-project-delivery.git",
  "local_path": "Obsidian_root/.../04-skills/02-project-delivery",
  "status": "active",
  "darwin_role": "author-provided-full",
  "priority": "P0"
}
```

## 迁移批次

### Phase 1：只建索引，不移动文件

- 在 Obsidian 中建立本规划文档。
- 给每个场景包列出候选文件清单。
- 在 `darwin-skill` 根目录维护 Full 发行版源码。
- 在 `darwin-skill/registry` 增加 `skillpacks_index.json` 草案，后续随 Full 版一起打包。

### Phase 2：建立 P0 子仓库骨架

- 创建 `01-academic-writing`。
- 创建 `02-project-delivery`。
- 创建 `05-governance-core`。
- 每个仓库先放 README、SKILL、manifest、CHANGELOG。
- 原文件先复制迁入，确认稳定后再考虑 Obsidian 内部链接替换。

### Phase 3：迁移内容并保留 Obsidian 入口

- 从 `02-workflows` 迁入场景路线到各包 `workflows/`。
- 从 `skills_library` 迁入模板、规范、检查清单到各包。
- `skills_library/README.md` 改为 Skillpack 导航，而不是厚重内容入口。
- `02-workflows/README.md` 保留个人日常路线入口，并链接到对应子仓库。

### Phase 4：同步到 `darwin-skill` Full 发行版

- 将成熟包中的 `SKILL.md` 同步到 `darwin-skill/skills/`。
- 将长文 SOP 和 reference 同步到 `darwin-skill/library/`。
- 更新 `package.manifest.json` 中的 Full 版描述与 authorProvidedSkillCount。
- 运行 `scripts/scan_skills.py` 和 `scripts/score_skills.py` 重建索引。

### Phase 5：GitHub 建仓与分发

- 每个 P0 包独立推送 GitHub。
- `darwin-skill` Full 发行版记录子仓库地址与版本。
- 后续可用 subtree/submodule 二选一管理：
  - `subtree`：适合需要把内容打包进 Full 版。
  - `submodule`：适合只引用外部仓库，不复制内容。

## 推荐的包与源文件映射

### `01-academic-writing`

- `02-workflows/03_论文科研写作与LaTeX/`
- `02-workflows/00_场景化工作流入口/03_论文科研写作与LaTeX工作流.md`
- `skills_library/04_国家标准文档工程化写作规范/03_学术文档规范.md`
- `skills_library/04_国家标准文档工程化写作规范/06_图表与建模规范.md`
- `skills_library/04_国家标准文档工程化写作规范/07_引用与参考文献规范.md`
- `skills_library/04_国家标准文档工程化写作规范/templates/学术类/`
- `skills_library/04_国家标准文档工程化写作规范/checklists/毕业论文检查清单.md`
- `skills_library/04_国家标准文档工程化写作规范/checklists/引用格式检查清单.md`

### `02-project-delivery`

- `02-workflows/01_vibe-coding产品原型到上线/`
- `02-workflows/02_软件工程文档与交付/`
- `02-workflows/04_项目管理与问题解决/`
- `02-workflows/00_场景化工作流入口/01_vibe-coding产品原型到上线工作流.md`
- `02-workflows/00_场景化工作流入口/02_软件工程文档交付工作流.md`
- `02-workflows/00_场景化工作流入口/04_敏捷项目管理与问题解决工作流.md`
- `skills_library/03_需求文档类/`
- `skills_library/04_国家标准文档工程化写作规范/04_软件工程文档规范.md`
- `skills_library/04_国家标准文档工程化写作规范/05_产品与项目管理文档规范.md`
- `skills_library/04_国家标准文档工程化写作规范/templates/软件工程类/`
- `skills_library/04_国家标准文档工程化写作规范/templates/项目管理类/`
- `skills_library/04_国家标准文档工程化写作规范/checklists/SRS检查清单.md`
- `skills_library/04_国家标准文档工程化写作规范/checklists/测试文档检查清单.md`
- `skills_library/04_国家标准文档工程化写作规范/checklists/软件设计文档检查清单.md`

### `03-tech-sharing`

- `02-workflows/05_知识管理与效率工具/`
- `02-workflows/00_场景化工作流入口/05_知识管理与效率工具工作流.md`
- `skills_library/01_调研分析类/`
- `skills_library/04_国家标准文档工程化写作规范/06_图表与建模规范.md`
- `skills_library/04_国家标准文档工程化写作规范/10_Agent调研任务库.md`

### `04-presentation-demo`

- 待新增：`reveal.js` 模板
- 待新增：演讲稿模板
- 待新增：演示 checklist
- 待新增：用户指南模板
- 可复用：项目交付包中的用户手册模板

### `05-governance-core`

- `skills_library/02_工程安全类/`
- `skills_library/04_国家标准文档工程化写作规范/08_文档命名与版本控制.md`
- `skills_library/04_国家标准文档工程化写作规范/09_文档质量审查清单.md`
- `02-workflows/05_知识管理与效率工具/03_vibe-coding-Git版本管理SOP.md`
- `02-workflows/05_知识管理与效率工具/04_vibe-coding流水线构建与Skill蒸馏.md`
- `darwin-skill/skills/darwin-skill-manager/`
- `darwin-skill/skills/darwin-skill-distiller/`
- `darwin-skill/skills/darwin-skill-auditor/`
- `darwin-skill/skills/darwin-skill-archivist/`

## 下一步执行清单

1. 在 `darwin-skill` 根目录维护 Full 发行版内容，避免把 Full 误建成分支。
2. 在 Obsidian `04-skills/` 下创建 5 个 skillpack 目录骨架。
3. 为 P0 三个包生成 README、SKILL、manifest、CHANGELOG。
4. 先复制迁移，不删除原文件。
5. 更新 `skills_library/README.md`，把它改成场景包导航。
6. 更新 `02-workflows/README.md`，补充每条 workflow 对应的 skillpack 仓库。
7. 在 `darwin-skill` 建立 `registry/skillpacks_index.json`，并随 Full 发行版打包。
8. 再决定每个 skillpack 是独立 Git 仓、subtree，还是 submodule。
