# Obsidian 电脑 & 手机文件同步方案复盘

> 2026-06-17 | 完整搭建记录 + SOP 操作手册

---

## 一、项目背景

### 需求
- MacBook Air 上的 Obsidian 笔记库需要同步到安卓手机（vivo iQOO11）
- 手机端主要用于快速查阅、灵感记录，不做重度编辑
- 版本历史需要保留，Mac 端已有 Git 推送到 GitHub

### 方案选型

| 方案 | 优点 | 缺点 | 结论 |
|------|------|------|------|
| Obsidian Sync | 官方方案，最省心 | $5/月，贵 | ❌ |
| iCloud | Mac 原生 | 安卓不兼容 | ❌ |
| Git (Obsidian Git 插件) | 免费，有版本历史 | 手机 Git 操作复杂 | ❌ 作辅助 |
| **Syncthing** | 免费、P2P、端到端加密、跨平台 | 需手动配置 | ✅ 主方案 |

### 最终架构

```
┌─────────────────────────────────────────┐
│          Obsidian_root 笔记库            │
│                                          │
│  MacBook Air                  vivo 手机  │
│  ┌──────────┐              ┌──────────┐ │
│  │ Obsidian │  Syncthing   │ Obsidian │ │
│  │ Git 插件 │ ←──P2P 加密→ │ (纯阅读) │ │
│  └────┬─────┘              └──────────┘ │
│       │                                  │
│       ↓ Git push                         │
│  ┌──────────┐                            │
│  │  GitHub  │  版本历史 + 远程备份       │
│  └──────────┘                            │
└─────────────────────────────────────────┘
```

---

## 二、搭建 SOP（分步操作指南）

### 步骤 1：Mac 安装 Syncthing

```bash
brew install syncthing

# 启动（首次会自动打开 Web UI）
syncthing --no-browser &

# 或设为开机自启
brew services start syncthing
```

**端口**：Web UI `http://localhost:8384`，同步端口 `22000/TCP+QUIC`

### 步骤 2：Mac 端创建共享文件夹

Web UI → 添加文件夹：
- 文件夹标签：`obsidian-vault`（这个 ID 手机端必须一致）
- 文件夹路径：`/Users/caolei/Desktop/Obsidian_root`
- 文件夹类型：发送与接收（`sendreceive`）
- 版本控制：简单版本控制（保留 5 个版本）

### 步骤 3：Mac 端配置忽略规则（.stignore）

在文件夹选项 → 忽略模式中填入：

```
.git
.obsidian/workspace
.obsidian/workspace.json
.obsidian/workspace-mobile.json
.obsidian/cache
.obsidian/plugins/obsidian-git
.DS_Store
.Trashes
.Spotlight-V100
```

> ⚠️ 必须排除 `.obsidian/plugins/obsidian-git`，否则手机端会同步 Git 插件导致冲突。

### 步骤 4：手机安装 Syncthing-Fork

- Google Play / F-Droid 搜索 `Syncthing-Fork`
- 这是社区优化版，比官方版更适配安卓

### 步骤 5：设备配对

Mac Web UI → 操作 → 显示 ID → 复制二维码

手机 Syncthing-Fork → 设备 → 添加设备 → 扫描二维码

> ⚠️ 配对是**一次性操作**。确认 Device ID 前 6 位和后 6 位匹配。

### 步骤 6：手机接收共享文件夹

配对成功后，手机会收到 Mac 的共享通知：

```
"caoleideMacBook-Air 共享了文件夹 obsidian-vault"
```

→ 点击添加 → 设置手机端路径 → 确认

> ⚠️ 文件夹标签必须与 Mac 端一致：`obsidian-vault`

### 步骤 7：手机配置忽略规则

同步骤 3，在手机 Syncthing-Fork 中配置 .stignore。

### 步骤 8：等待同步完成

Web UI / CLI 监控：

```bash
syncthing cli show connections
curl -s -H "X-API-Key: $APIKEY" 'http://localhost:8384/rest/db/status?folder=obsidian-vault'
```

状态变为 `idle` + `needFiles: 0` = 同步完成。

### 步骤 9：手机打开 Obsidian

**关键顺序**：先等 Syncthing 同步完，再打开 Obsidian！

手机 Obsidian → 打开文件夹作为仓库 → 选择 Syncthing 同步目录

> ⚠️ 如果先打开 Obsidian 再同步，Obsidian 会自动生成 `.obsidian/` 配置，导致冲突文件。

### 步骤 10：处理首次配置冲突（如有）

检查冲突文件：

```bash
find Obsidian_root -name "*sync-conflict*"
```

处理原则：
- `core-plugins.json` → 保留 Mac 版，删除冲突文件
- `app.json` → 检查哪个版本配置更完整，合并后删除冲突文件
- `workspace.json` → 无关紧要，直接删除冲突版本

### 步骤 11：Mac 端 Git 配置（双轨架构）

```bash
# Obsidian Git 插件设置
autoPullInterval: 30   # 每 30 分钟 pull
autoPushInterval: 60   # 每 60 分钟 push
```

Git 和 Syncthing 互不依赖：
- Git → GitHub：版本历史备份
- Syncthing → 手机：实时文件同步

---

## 三、日常使用 SOP

### Mac 端（主战场）

```
打开 Obsidian → 正常编辑 → 自动同步到手机
├── Syncthing 后台自动同步
└── Git 插件自动 commit + push
```

### 手机端（轻量场景）

```
拿起手机 → 等 3-5 秒让 Syncthing 连接
→ 打开 Obsidian → 阅读/快速记录
→ 自动同步回 Mac
```

### 手动强制同步

```bash
# Mac 端强制重扫（改了大量文件后）
syncthing cli operations rescan --folder=obsidian-vault

# 或 Web UI 点击文件夹 → Rescan
```

---

## 四、警戒线与踩坑记录

### 🔴 绝对不要做

| 禁止操作 | 后果 | 原因 |
|----------|------|------|
| 手机安装 Obsidian Git 插件 | 仓库冲突，手机卡死 | 1.3GB git 仓库不适合手机 |
| 两端同时编辑同一文件 | 冲突文件无法自动合并 | 一边改第 3 行一边改第 5 行也不行 |
| 手机新增/删除插件 | 插件状态冲突 | 两端插件环境不可控 |
| 用 iCloud 同时同步这个 vault | 文件损坏或丢失 | 两个同步工具抢文件 |
| 手机未同步完就打开 Obsidian | 产生 `.sync-conflict` 文件 | Obsidian 自动生成配置 |
| 手机批量重命名/移动文件 | 同步风暴 + 冲突 | 大量文件同时变更 |

### 🟡 容易踩的坑

| 坑 | 表现 | 解决 |
|----|------|------|
| 文件夹标签不一致 | 永不连接 | 两端都必须是 `obsidian-vault` |
| Syncthing 未开机自启 | Mac 重启后同步中断 | `brew services start syncthing` |
| 手机息屏后断连 | 编辑后无法同步 | 点亮屏幕等 5-15 秒 |
| 校园网客户端隔离 | 直连失败 | 自动走中继，稍慢但不影响 |
| 手机流量跑同步 | 后台消耗移动数据 | 手机 Syncthing 可设仅 Wi-Fi |

### 🟢 安全操作

| 操作 | 注意事项 |
|------|---------|
| Mac 端随时编辑任何文件 | 无限制 |
| 手机端快速记录灵感 | 避免和 Mac 同时改同一个文件 |
| 手机端阅读、搜索 | 完全安全 |
| Mac 端修改插件配置 | 改完会自动同步到手机 |
| 不跨设备同时操作 | 最安全的使用模式 |

---

## 五、故障排查速查表

| 症状 | 检查项 | 命令/操作 |
|------|--------|-----------|
| 手机不连接 | 手机 Syncthing 是否在运行 | 打开 App 检查 |
| 文件不同步 | 文件夹是否共享给设备 | Web UI 检查文件夹 → 共享 |
| 同步很慢 | 是否在走中继 | `syncthing cli show connections` |
| 有冲突文件 | `find . -name "*sync-conflict*"` | 按步骤 10 处理 |
| Web UI 打不开 | Syncthing 是否在运行 | `brew services list | grep syncthing` |
| 手机看不到新文件 | 是否被 .stignore 过滤 | 检查忽略规则 |

---

## 六、设备信息速查

| 项目 | 值 |
|------|-----|
| Mac Device ID | `7TLKO64-MX6MECS-KF24O6A-5CA2ZEA-RS34C3H-GL5T6SC-PQ4HINH-63EGIAH` |
| 手机 Device ID | `ZNCY7GM-HTGZYR3-3JSZVRU-Z7SPW5S-BJUU76R-EVQRAAU-ODAFHN5-GOYO2QG` |
| 文件夹标签 | `obsidian-vault` |
| Mac 路径 | `/Users/caolei/Desktop/Obsidian_root` |
| Web UI | `http://localhost:8384` |
| API Key | `jH5R9wMxpwtwS6mNPCMRnf59qytsHrWT` |
| Git 仓库 | `github.com/githoldder/Obsidian_vault.git` |
| Syncthing 版本 | 2.1.1 (arm64) |

---

## 七、方案评估与反思

### 优点
- 零成本，全部开源免费
- 数据完全私有，不经过第三方存储
- 校园网、移动网络、热点都能通
- Git 双轨保证版本历史不丢失

### 代价
- 需要手动配置（约 30 分钟一次性成本）
- 手机端需要后台运行 Syncthing（耗电略增）
- 偶尔需要处理冲突（遵守 SOP 可避免）

### 是否推荐
✅ **推荐给：** 有 Mac + 安卓手机、重视数据隐私、愿意花 30 分钟配置的 Obsidian 用户

❌ **不推荐给：** 只有苹果全家桶（用 iCloud 更省心）、或愿意付费买 Obsidian Sync 的用户
