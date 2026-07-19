
---

## 核心理念

基于 **PARA 方法** + **就近原则** + **看板驱动** 构建的个人知识管理系统。

- **PARA**: Projects（项目）+ Areas（领域）+ Resources（资源）+ Archives（归档）
- **就近原则**: 附件随文档走，消除全局附件文件夹
- **看板驱动**: 通过 Dashboard 管理活跃项目，而非翻阅文件夹

---

## 目录结构

```
Obsidian_root/
├── 00_Inbox/                    # 收件箱 - 临时/碎片待整理
├── 10_Projects/                 # 项目 - 有明确截止日期的活跃工作
├── 20_Areas/                    # 领域 - 长期维护的职责和学习
├── 30_Resources/                # 资源 - 参考资料和兴趣点
├── 40_Archives/                 # 归档 - 已完成或停滞的内容
└── 50_System/                   # 系统 - 模板、脚本、配置
```

---

## 各目录详解

### 00_Inbox - 收件箱

**用途：** 临时存放未分类的内容，定期清理（建议每日/每周）。

**内容示例：**
- 快速剪藏的网页
- 临时笔记
- 待处理的文件
- 截图和草稿

**维护规则：**
- 禁止长期堆积，超过 7 天必须分类或删除
- 定期清空，保持为空状态

---

### 10_Projects - 项目

**定义：** 有明确目标、截止日期的活跃工作。

**项目文件夹结构：**
```
10_Projects/
└── 项目名称/
    ├── _Dashboard.md           # 看板型主页（必须）
    ├── Current_Work.md         # 当前工作追踪（必须）
    ├── 01_Data/                # 调研数据、参考资料
    ├── 02_Notes/               # 过程笔记、会议纪要
    ├── 03_Assets/              # 图片、附件（就近原则）
    ├── 04_Archive/             # 旧版本、废弃方案
    └── 05_Output/              # 最终产出物
```

**必需文件模板：**

**_Dashboard.md:**
```markdown
# 项目名称 Dashboard

## 项目信息
- 目标：
- 截止日期：
- 负责人：

## 进度看板
### 待办
- [ ] 任务1

### 进行中
- [ ] 任务2

### 已完成
- [x] 任务3

## 关键链接
- [需求文档]()
- [设计稿]()
- [代码仓库]()

## 风险与阻碍
1. 
```

**Current_Work.md:**
```markdown
# 当前工作

## 今日/本周聚焦
1. 

## 上下文记录
- 上次做到哪：
- 下一步：
- 阻塞点：

## 决策日志
| 日期 | 决策 | 原因 |
|------|------|------|
|      |      |      |
```

---

### 20_Areas - 领域

**定义：** 需要长期维护、没有明确截止日期的职责或学习方向。

**子目录示例：**
```
20_Areas/
├── CS_Learning/                # 计算机科学学习
│   ├── Algorithms/
│   ├── System_Design/
│   └── Languages/
├── Language_Center/            # 语言学习
│   ├── English/
│   └── Japanese/
├── Self_Management/            # 自我管理
│   ├── GTD/
│   ├── Habits/
│   └── Reviews/
└── Career_Development/         # 职业发展
```

**与项目的区别：**

| 维度 | Projects | Areas |
|------|----------|-------|
| 目标 | 明确、可完成 | 持续、无终点 |
| 时间 | 有截止日期 | 长期维护 |
| 结果 | 产出交付物 | 能力提升 |
| 示例 | 开发小程序、写论文 | 学英语、健身 |

---

### 30_Resources - 资源

**定义：** 兴趣点和参考资料，非主动创作内容。

**子目录示例：**
```
30_Resources/
├── Reading_Notes/              # 读书笔记
│   ├── Books/
│   └── Articles/
├── Media_Notes/                # 视频/播客笔记
│   ├── Courses/
│   └── Talks/
├── Thinking_Models/            # 思维模型、方法论
│   ├── Mental_Models/
│   └── Frameworks/
└── Tools_Library/              # 工具库
    ├── Software/
    └── Skills/
```

**管理原则：**
- 按主题聚合，而非来源
- 使用标签补充分类
- 定期回顾，删除过时内容

---

### 40_Archives - 归档

**定义：** 已完成项目、过期资料、不再活跃的内容。

**归档策略：**
- 项目完成后 30 天移入归档
- 保留原始结构和附件
- 添加归档日期前缀：`2024-Q1-项目名称/`

**子目录示例：**
```
40_Archives/
├── 2024_Projects/              # 按年份归档
├── 2023_Projects/
├── Past_Employment/            # 过往工作经历
├── Old_Courses/                # 已完成的课程
└── Deprecated_Tools/           # 废弃的工具/方法
```

---

### 50_System - 系统

**定义：** 个人知识管理系统的"操作系统"。

**子目录结构：**
```
50_System/
├── Templates/                  # 文档模板
│   ├── Project_Dashboard_Template.md
│   ├── Meeting_Notes_Template.md
│   └── Weekly_Review_Template.md
├── Scripts/                    # 自动化脚本
├── Navigation/                 # 导航看板
│   ├── Home.canvas             # 主页导航
│   └── Projects_Overview.md
├── SOPs/                       # 标准操作流程
│   ├── File_Organization_SOP.md
│   └── Weekly_Review_SOP.md
└── Configs/                    # 配置文件
    ├── Obsidian_Settings.md
    └── Plugin_Configs/
```

**导航看板 (Home.canvas):**
使用 Obsidian Canvas 创建可视化导航，链接到：
- 活跃项目 Dashboard
- 常用领域入口
- 系统工具

---

## 命名规范

### 文件夹命名

```
XX_描述性名称/                 # 数字前缀 + 下划线分隔
```

**前缀规则：**

| 前缀 | 用途 |
|------|------|
| `00_` | 系统级、入口 |
| `10_` - `40_` | PARA 四大类 |
| `50_` | 元系统 |
| `01_` - `09_` | 项目内部子目录 |

### 文件命名

```
YYYYMMDD_描述_版本.扩展名       # 日期前缀（可选）
描述性名称_版本.扩展名           # 常规命名
```

**示例：**
- `20240412_会议纪要_项目启动.md`
- `需求分析文档_v2.md`
- `架构设计图_final.png`

---

## 就近原则详解

### 问题
传统做法将所有附件放在全局 `012_图片` 或 `附件/` 文件夹，导致：
- 文件与上下文分离
- 项目迁移困难
- 附件归属不清

### 解决方案

**Obsidian 设置：**
1. 打开 **Settings → Files and Links**
2. 设置 **Default location for new attachments**: `In the folder specified below`
3. 设置 **Attachment folder path**: `./03_Assets`

**效果：**
- 拖入图片自动保存到当前文件所在目录的 `03_Assets/` 下
- 项目整体移动时附件跟随
- 删除项目时附件一并清理

---

## 工作流示例

### 新项目启动流程

1. **在 `10_Projects/` 创建项目文件夹**
   ```
   10_Projects/新项目名称/
   ```

2. **复制模板文件**
   - `_Dashboard.md`
   - `Current_Work.md`

3. **创建子目录**
   ```
   01_Data/
   02_Notes/
   03_Assets/      # Obsidian 自动创建
   04_Archive/
   05_Output/
   ```

4. **在 `_Dashboard.md` 填写项目信息**

5. **在 `50_System/Navigation/Home.canvas` 添加项目入口**

### 日常维护流程

**每日：**
- 清空 `00_Inbox`
- 更新 `Current_Work.md`

**每周：**
- 回顾 `_Dashboard.md` 进度
- 归档已完成任务

**每月：**
- 检查 `10_Projects` 是否有可归档项目
- 整理 `30_Resources` 新增内容

**每季度：**
- 大规模归档到 `40_Archives`
- 更新系统模板和 SOP

---

## 与 PARA 的映射

| PARA 概念 | 本系统位置 | 管理频率 |
|-----------|-----------|----------|
| Projects | `10_Projects/` | 每日 |
| Areas | `20_Areas/` | 每周 |
| Resources | `30_Resources/` | 每月 |
| Archives | `40_Archives/` | 每季度 |

---

## 工具推荐

### Obsidian 必备插件

| 插件 | 用途 |
|------|------|
| **Templater** | 模板自动化 |
| **Dataview** | 动态查询和列表 |
| **Canvas** | 可视化导航 |
| **QuickAdd** | 快速捕获 |
| **Periodic Notes** | 周期性笔记 |
| **Tag Wrangler** | 标签管理 |

### 辅助工具

- **Hazel** (macOS) / **File Juggler** (Windows): 自动文件整理
- **Alfred** / **Raycast**: 快速搜索和打开

---

## 常见问题

### Q: 项目 vs 领域如何区分？
A: 问自己：这件事有明确的完成标准和截止日期吗？
- 有 → Projects
- 没有 → Areas

### Q: 文件应该放在 Projects 还是 Resources？
A: 问自己：这是我主动创作的，还是收集参考的？
- 主动创作 → Projects
- 收集参考 → Resources

### Q: 如何处理跨项目的参考资料？
A: 原始文件放 `30_Resources/`，在项目中用Obsidian的`[[双链]]`语法引用。

### Q: 附件太多影响性能怎么办？
A: 
- 定期归档已完成项目
- 大文件（视频、大型 PDF）放云盘，笔记中存链接
- 使用 Obsidian 的 "Exclude files" 设置排除大文件夹索引

---

## 关联技能

- [[35-BibTeX-Lifecycle]] - 学术文档编写规范
- [[21-Doc-Engineering-Standard]] - 文档编写标准与图表绘制
- [[41-Project-Three-Layers]] - 项目管理方法论

---

## 参考资源

- [Building a Second Brain](https://www.buildingasecondbrain.com/) - Tiago Forte
- [PARA Method](https://fortelabs.com/blog/para/) - 官方介绍
- [Obsidian 官方文档](https://help.obsidian.md/)
