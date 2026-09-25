# Mac iCloud 同步机制与"关闭同步导致数据丢失"完全指南

> 本文档综合来源：
> - 本人 iCloud 数据丢失事件调查（2026-04-26）
> - 客服访谈记录（2026-04-26）
> - IndexedDB 恢复实验记录
> - Apple 官方文档 HT206985、HT202819
> - macOS iCloud 内部机制分析
>
> 生成时间：2026-04-27
> 关联文件：`~/Desktop/iCloud云盘迁移数据误删解决文档.md`

---

## 一、LevelDB / IndexedDB 文件机制详解

### 1.1 为什么我能恢复你的 Obsidian 数据？

Obsidian 是一个基于 **Electron** 的桌面应用，数据存储使用了 IndexedDB（浏览器同款数据库），而 IndexedDB 在 macOS 上底层对应的是 **LevelDB**（由 Google 开发的高性能 KV 存储引擎）。

#### 数据存储层级

```
Obsidian 应用层
    └── IndexedDB API（浏览器标准接口）
            └── LevelDB 引擎
                    └── .ldb 文件（SSTable 格式）
                            └── data blocks（压缩存储的 key-value 对）
```

Obsidian 的 IndexedDB 位于：
```
~/Library/Application Support/obsidian/IndexedDB/
    └── app_obsidian.md_0.indexeddb.leveldb/
            ├── 002553.ldb ~ 002562.ldb（共10个 SSTable 文件）
            ├── 002551.log（操作日志）
            └── LOCK（数据库锁文件）
```

#### LevelDB 的存储结构

每个 `.ldb` 文件是 **SSTable (Sorted String Table)**，内部结构：

```
┌─────────────────────────────────────────┐
│  Header (metaindex handle)              │
├─────────────────────────────────────────┤
│  Data Block 1 (压缩的 key-value 对)      │  ← snappy 压缩
├─────────────────────────────────────────┤
│  Data Block 2                           │
├─────────────────────────────────────────┤
│  ...                                    │
├─────────────────────────────────────────┤
│  Filter Block (布隆过滤器)               │
├─────────────────────────────────────────┤
│  Index Block                            │
├─────────────────────────────────────────┤
│  Footer (metaindex + index handle)      │
└─────────────────────────────────────────┘
```

每个 **key** 是文件的路径（如 `001_个人规划/001_日记/2026-04-25.md`），**value** 是 V8 序列化后的文件内容（Obsidian 用 V8 引擎）。

#### 恢复原理

```
1. 用 snappy 解压 .ldb 文件的 data blocks
2. 解析每个 key-value 对
3. 用 UTF-16LE 编码搜索目标关键词（如"2026-04-25"、"需求访谈"）
4. 从匹配的 value 中用 V8 反序列化提取原始 Markdown 文本
5. 清理 V8 元数据噪音，还原可读内容
```

#### 关键发现

- **Obsidian IndexedDB 缓存了 vault 中文件的多个版本/修订**
- 匹配 "2026-04-25" 的条目：8个（跨多个 ldb 文件）
- 匹配 "需求访谈" 的条目：14个
- 单个 ldb 文件最大扫描 49,435 个 key-value 条目
- **但 04-26 日记的 IndexedDB 条目只有元数据（headings/links/tags 索引），无完整正文**——这说明 Obsidian 在编辑时没有完整刷新 IndexedDB 缓存

---

## 二、macOS iCloud 同步机制详解

### 2.1 架构总览

```
用户 ~/Desktop/xxx.md
        │
        ▼
  APFS 文件系统（本地）
        │
        ▼
  FileProvider（内核扩展，文件协调）
        │
        ▼
  bird 守护进程（后台同步引擎）
        │
        ▼
  CloudKit（iCloud 服务端）
        │
        ▼
  用户其他设备
```

**核心组件：**
- `bird` (Background Internet Relay Daemon)：管理所有 iCloud 同步的后台进程
- `CloudDocs` (FileProvider)：将 iCloud 虚拟文件系统挂载到用户目录
- `mdworker` / `mds`：Spotlight 索引，与 FileProvider 联动

### 2.2 桌面与文稿文件夹的同步逻辑

当你开启"桌面与文稿文件夹"iCloud 同步：

```
~/Desktop  →  实际映射到  ~/Library/Mobile Documents/com~apple~CloudDocs/Desktop/
~/Documents →  实际映射到  ~/Library/Mobile Documents/com~apple~CloudDocs/Documents/
```

这不是简单的 rsync，macOS 会：

1. **系统级重定向**：FileProvider 接管目录访问，本地文件经过 APFS Clone + Copy on Write
2. **Optimize Mac Storage（优化存储）**：不常用的文件本地副本被驱逐，只保留 `.icloud` 占位符
3. **云端为权威**：所有修改先到云端，云端确认后才算"已保存"

### 2.3 iCloud 空间满了会怎样？

**iCloud 5GB 配额 = 云端存储总量（含桌面+文稿+iCloud Drive 所有内容）**

当配额满了：

| 操作 | 结果 |
|------|------|
| 创建新文件 | 文件存在本地，但**上传队列阻塞**，持续报 CKErrorDomain:25 "Quota exceeded" |
| 修改现有文件 | 旧版本在云端，新修改卡在本地待上传队列 |
| 开启优化存储 | `.icloud` 占位符替换本地文件，**本地内容被驱逐** |
| 关闭同步开关 | macOS **不保证**将未上传的本地修改保留 |

**关键问题：**

> ⚠️ **macOS 在关闭 iCloud Drive 同步时，不会在本地创建未上传内容的备份。**
> 它假设所有文件都已经同步到云端。如果没有（因为配额满），这些修改**永久丢失**。

### 2.4 关闭同步开关时 macOS 实际做了什么

根据 Apple 官方文档 HT206985 的描述：

> "When you turn off iCloud Drive or sign out of iCloud, a new Desktop and Documents folder is created in your home folder. You also have the option to keep a local copy of your files that are in iCloud Drive."

**实际操作流程（关闭"桌面与文稿文件夹"）：**

1. `bird` 停止运行，不再上传
2. FileProvider 卸载虚拟文件系统
3. macOS 检查 `~/Library/Mobile Documents/` 中**已下载到本地的文件**
4. 如果有**云端不存在本地副本**的文件（即 `needs-upload` 状态），macOS **不会特殊处理**
5. 新建空的 `~/Desktop` 替换原目录
6. 云端文件等待重新同步（如果重新开启）

**你的具体场景：**

```
iCloud 配额满 → 04-25 日记修改后 "needs-upload" 卡在队列
     ↓
关闭同步 → macOS 卸载 FileProvider
     ↓
原 ~/Desktop 被清空（不是移到别处，是直接清空）
     ↓
04-25.md 在云端只有旧版本（04-21 的版本），本地新内容随文件一起消失
     ↓
重新开启同步 → 云端旧版本下载回本地
     ↓
文件回来了但内容是 04-21 的，不是 04-25 的
```

---

## 三、根因分析总结

### 3.1 故障链

```
① iCloud 5GB 配额被占满（主犯：LaTeX 论文项目 800MB + 其他大文件）
     ↓
② 04-25 修改日记 → 上传失败，卡在 "pending-quota" 状态
     ↓
③ "优化 Mac 存储" 已开启 → .icloud 占位符可能已替换本地文件
     ↓
④ 用户清空 iCloud 废纸篓（误操作）→ 790MB 释放但仍不够
     ↓
⑤ 关闭 iCloud 桌面同步 → macOS 没有保留未上传的本地修改
     ↓
⑥ 重新开启同步 → 云端旧版本下载，内容丢失
```

### 3.2 核心设计缺陷

**不是 macOS 的 bug，但是一个严重的设计缺陷：**

1. **配额满时无警告**：iCloud 配额满，macOS 只在系统设置显示小红点，不会在用户关闭同步前弹出"有未上传修改"警告
2. **"最近删除"保护失效**：因为文件根本没有上传成功，所以云端"最近删除"里也没有
3. **FileProvider 驱逐时机不可预测**：用户不知道何时本地文件被换成了 .icloud 占位符
4. **索引缓存≠备份**：Obsidian IndexedDB 有内容不代表能完整恢复（04-26 日记就是例子）

---

## 四、解决方案

### 4.1 立即行动（你现在应该做的）

```
□ 检查 iCloud 配额：系统设置 → Apple ID → iCloud → 管理账户存储空间
□ 清理 iCloud 废纸篓（如果还没清）
□ 关闭"优化 Mac 存储"（防止本地文件被意外驱逐）
□ 检查 brctl 状态：终端输入 `brctl list` 确认无 pending-uploads
□ 将所有重要文件同步到非 iCloud 位置（外接硬盘 / 百度网盘）
```

### 4.2 短期解决方案

**方案 A：升级 iCloud 套餐（推荐）**
- 50GB = ¥6/月，200GB = ¥21/月
- 根本上避免配额满的问题

**方案 B：迁移到本地优先工作流**
- Obsidian vault 完全移到 ~/Documents/Obsidian_root（不同步到 iCloud）
- 用 GitHub private repo 做笔记备份
- 外接硬盘做 Time Machine 备份

**方案 C：混合云策略**
```
~/Desktop/Obsidian_root  →  iCloud 同步（方便多设备）
~/Documents/备份/        →  外接硬盘 + Time Machine
~/Projects/              →  GitHub / 百度网盘
```

### 4.3 长期最佳实践

```
1. 永远不要相信"云端有备份"就等于"安全"
2. 重要文件修改前，先检查 iCloud 配额是否充足
3. 关闭同步前，务必确认 brctl 显示所有文件 "upload complete"
4. Obsidian 重要笔记，用 Git 做版本控制
5. Time Machine 开启，外接硬盘定期备份
6. iCloud 配额只用于真正需要多设备同步的文件
```

### 4.4 如果已发生数据丢失

**按优先级尝试：**

| 优先级 | 方法 | 适用场景 |
|--------|------|----------|
| 1 | `brctl list` 查看是否还有 pending 文件在队列 | 关闭同步后马上操作 |
| 2 | iCloud.com 网页版 → 最近删除 | 云端有旧版本 |
| 3 | Time Machine 备份恢复 | 有开启 Time Machine |
| 4 | Obsidian IndexedDB 解析（本文方法） | Electron 应用文件恢复 |
| 5 | 磁盘数据恢复软件（如 Disk Drill） | 文件被删除但未覆写 |
| 6 | Apple 官方支持 | 极端情况，付费可能恢复 |

---

## 五、04-21 到 04-25 期间 IndexedDB 索引的文件清单

以下是根据 IndexedDB 恢复实验扫描到的所有在 04-21 ~ 04-25 期间有记录的文件：

### 5.1 已恢复文件

| 文件路径 | 恢复状态 | 内容摘要 |
|----------|----------|----------|
| `001_个人规划/001_日记/2026-04-25.md` | ✅ 完整恢复（13,663字） | PM2进程管理详解 + Docker混合工作流分析 |
| `011_项目经验/反诈小程序/09_需求最终对齐.md` | ✅ 完整恢复（3,979字） | 第三次需求访谈最终对齐清单（2026-04-26） |
| `011_项目经验/反诈小程序/08_第三次需求访谈记录.md` | ✅ 磁盘原文件完好 | 第三次需求访谈分析，4,343字节 |

### 5.2 IndexedDB 中无记录的文件

| 日期 | 状态说明 |
|------|----------|
| 2026-04-21 | 磁盘原文件完好（6,961字节），IndexedDB 有结构化元数据 |
| 2026-04-22 | IndexedDB 无匹配记录 |
| 2026-04-23 | IndexedDB 无匹配记录 |
| 2026-04-24 | IndexedDB 无匹配记录 |
| 2026-04-26 | 只有元数据索引（headings/links/tags），无完整正文 |

### 5.3 其他在 IndexedDB 中有记录的文件

| 文件路径 | 匹配日期 | 说明 |
|----------|----------|------|
| `000_个人看板/脚手架&通用模版&工具/04-skills/06_vibe-coding-CLI-token节省范式.md` | 04-22 | 有 IndexedDB 记录，内容需单独提取 |

---

## 六、Obsidian 备份策略建议

### 6.1 为什么不能完全依赖 iCloud

- iCloud 5GB 配额极其有限（Obsidian vault 通常 500MB+）
- 配额满时文件上传卡住，但 Obsidian 仍会保存到 IndexedDB
- IndexedDB 不等于完整备份（04-26 日记就是证据）
- 多设备同步时，一个设备损坏可能传染所有设备

### 6.2 推荐备份方案

```
日常备份：
├── Obsidian Git 插件 → 每次关闭时自动 commit + push
├── iCloud 同步 → 多设备访问（控制 vault 大小在 200MB 以内）
└── 每周手动 rsync 到外接硬盘

vault 大小控制：
├── 附件用相对路径，本地存储
├── 定期清理 .obsidian/plugins 中的缓存
├── 大文件（>10MB）不放 vault，用符号链接引用外部路径
└── 用 Dataview 建索引，不重复存储相同数据
```

### 6.3 Git 备份命令（参考）

```bash
# 在 Obsidian vault 目录执行
git init
git remote add origin https://github.com/your-username/obsidian-backup.git
git add .
git commit -m "vault backup $(date '+%Y-%m-%d %H:%M')"
git push -u origin main

# 或用 obsidian-git 插件自动执行
```

---

## 七、技术术语表

| 术语 | 解释 |
|------|------|
| **LevelDB** | Google 开发的高性能 KV 存储数据库，IndexedDB 的底层引擎 |
| **SSTable** | LevelDB 的存储格式，key-value 对按 key 排序后分块存储 |
| **snappy** | Google 压缩算法，LevelDB 用它压缩 data blocks |
| **V8** | Chrome/Node.js 的 JavaScript 引擎，Obsidian 用它序列化 IndexedDB 数据 |
| **FileProvider** | macOS 内核扩展，将云端文件虚拟化为本地文件系统 |
| **bird** | macOS iCloud 同步的后台守护进程 |
| **CloudKit** | Apple 的云端数据库，iCloud Drive 基于此构建 |
| **CKErrorDomain:25** | CloudKit 错误码，含义为 "Quota exceeded"（配额超限） |
| **.icloud 占位符** | macOS 本地文件被优化存储后替换成的特殊文件，访问时触发云端下载 |
| **APFS Clone** | macOS 文件系统的写时复制机制，修改文件不复制全部数据 |
| **brctl** | Apple 的 CloudKit 调试工具，可查看文件上传队列状态 |

---

*文档版本：v1.0 | 最后更新：2026-04-27*
