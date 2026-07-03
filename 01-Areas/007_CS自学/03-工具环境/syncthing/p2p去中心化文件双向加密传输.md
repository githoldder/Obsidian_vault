# P2P 去中心化文件双向加密传输

> 2026-06-17 | 基于 Syncthing 实现 Obsidian 跨设备同步的深度理解

---

## 一、核心概念：什么是 P2P 文件同步

### 1.1 传统云同步 vs P2P 同步

```
传统模型（iCloud / OneDrive / Google Drive）：
  设备A → 中心服务器 → 设备B
           ↑
      你的数据躺在这里
      （服务商看得到）

P2P 模型（Syncthing / Resilio）：
  设备A ←→ 设备B
      ↓     ↓
     中继（可选，仅转发，看不了解密内容）
```

### 1.2 Syncthing vs 区块链：同源异流

| 维度 | Syncthing | 区块链 |
|------|-----------|--------|
| 核心理念 | 去中心化文件同步 | 去中心化账本共识 |
| 连接拓扑 | 设备间直连 P2P | 节点间广播 P2P |
| 数据单元 | 文件块（block） | 交易（transaction） |
| 共识机制 | 无（以最新修改时间为准） | PoW/PoS/BFT 等 |
| 激励机制 | 无（开源公益） | 代币经济 |
| 安全性 | TLS + 设备证书 | 密码学 + 经济博弈 |
| 创建者 | Jakob Borg（2013） | 中本聪（2009） |
| 灵感来源 | BitTorrent Sync（Resilio） | B-Money / Hashcash |

**本质区别**：Syncthing 是工具，区块链是制度。Syncthing 不发行代币、不挖矿、不需要共识——两台设备之间只需要"谁的版本更新就用谁的"这一条规则。

### 1.3 为什么叫"P2P"

Syncthing 的去中心化体现在三层：

| 层 | 去中心化方式 |
|----|-------------|
| **发现层** | 全球发现服务器（只存设备 ID，不存数据） |
| **传输层** | 设备直连优先，中继仅做加密转发 |
| **数据层** | 文件只在你的设备上，不存在任何第三方服务器 |

---

## 二、加密体系：到底安全不安全

### 2.1 端到端加密全链路

```
发送端                                接收端
  │                                     │
  ├─ ① 文件分块（128KB blocks）         │
  ├─ ② 每块 SHA256 哈希                │
  ├─ ③ TLS 1.2+ 加密传输 ──────────→   │
  │                                     ├─ ④ TLS 解密
  │                                     ├─ ⑤ SHA256 校验
  │                                     └─ ⑥ 写入磁盘
```

### 2.2 密钥体系

```
设备配对时交换：
  Device ID（公钥指纹）
    ↓
  TLS 证书（自签名，不在 CA 体系内）
    ↓
  每台设备生成 Ed25519 密钥对
    ↓
  传输用 Perfect Forward Secrecy (PFS)
```

**关键结论**：
- 中继服务器只做加密转发，**看不到内容**
- 发现服务器只存设备 ID 和临时 IP，**不知道你有什么文件**
- 唯一信任根是你**手动确认的设备 ID 指纹**

### 2.3 攻击面分析

| 攻击方式 | 能否得逞 | 原因 |
|----------|---------|------|
| 中间人拦截 | ❌ | TLS 双向认证，需要设备私钥 |
| 中继服务器窥探 | ❌ | 端到端加密，中继只转发密文 |
| 伪造设备加入 | ❌ | 需手动确认 Device ID 指纹 |
| 局域网嗅探 | ❌ | TLS 加密，不降级明文 |
| 物理接触设备 | ⚠️ | 能直接读文件，与 Syncthing 无关 |

---

## 三、网络架构：任何网络都能通

### 3.1 连接优先级

```
1. 局域网直连（TCP/QUIC，端口 22000）
   ↓ 失败
2. NAT 穿透（UPnP / NAT-PMP / STUN）
   ↓ 失败
3. 中继转发（relay.syncthing.net，端口 22067）
```

### 3.2 实测场景

| 场景 | 连接方式 | 速度 | 延迟 |
|------|---------|------|------|
| Mac + 手机同 Wi-Fi | 局域网 TCP/QUIC | 带宽满速 | 1-5s |
| 电脑连手机热点 | 热点内局域网 | 带宽满速 | 1-5s |
| 手机 4G + 电脑校园网 | 中继转发 | ~1-5 MB/s | 10-30s |
| 两端不同校园网 | 中继转发 | ~1-5 MB/s | 10-30s |

> 实测中 `192.168.180.42` 是手机在同 Wi-Fi 下的 IP，直连成功；离开局域网后自动走中继。

### 3.3 全局发现机制

```
发现服务器（discovery.syncthing.net）
  ├─ announce：设备上报自己 IP:port
  └─ lookup：其他设备查询你的 IP
       ↓
  拿到 IP 后尝试 TCP/QUIC 直连
       ↓
  失败则通过 relay.syncthing.net 中转
```

---

## 四、本机部署详情

### 4.1 设备信息

| 设备 | Device ID（前 30 位） | 角色 |
|------|----------------------|------|
| MacBook Air | `7TLKO64-MX6MECS-KF24O6A-5CA2ZE...` | 主设备，Git 仓库 |
| vivo iQOO11 | `ZNCY7GM-HTGZYR3-3JSZVRU-Z7SPW5...` | 移动端，只读为主 |

### 4.2 文件夹配置

```
文件夹 ID: obsidian-vault
路径 (Mac): /Users/caolei/Desktop/Obsidian_root
类型: 发送与接收（sendreceive）
版本控制: 简单版本控制（保留 5 个历史版本）
忽略规则: .git, .obsidian/workspace*, .obsidian/plugins/obsidian-git, .DS_Store
```

### 4.3 双轨架构

```
文件同步层：  Mac ←──Syncthing──→ 手机
              │
版本控制层：  Mac ←──Git──→ GitHub
```

两轨互不干扰：Git 跟踪版本历史，Syncthing 同步最新状态。

### 4.4 管理命令

```bash
# Web UI
open http://localhost:8384

# CLI 查看状态
syncthing cli show connections   # 设备连接
syncthing cli show system        # 系统状态

# 强制重扫
syncthing cli operations rescan --folder=obsidian-vault

# REST API（可脚本化）
APIKEY="jH5R9wMxpwtwS6mNPCMRnf59qytsHrWT"
curl -s -H "X-API-Key: $APIKEY" http://localhost:8384/rest/db/status?folder=obsidian-vault
```

---

## 五、关键认知

1. **P2P 不等于区块链**：Syncthing 是 BitTorrent 血统的工具，不是加密经济的账本
2. **"去中心化"的边界**：发现和中继服务器仍然是中心化的基础设施，但你的数据从不在那
3. **安全模型是"信任你的设备"**：只要设备不丢、Device ID 不泄露，数据就是安全的
4. **中继 ≠ 不安全**：端到端加密保证了中继看不到内容，跟 Signal 的 relay 一个道理
5. **校园网不是问题**：客户端隔离走中继，不隔离走直连，总有一条路通
