# OpenClaw 云服务器部署日志

> **部署时间**：2026-04-07
> **服务器**：腾讯云 Ubuntu (华东地区上海)
> **公网IP**：101.34.72.227
> **内网IP**：10.0.0.17
> **部署方式**：源码编译（非 Docker）

---

## 1. 部署背景

### 1.1 方案选择

因腾讯云服务器网络限制（无法访问 github.com、ghcr.io、registry-1.docker.io），采用**源码编译部署**方案。

### 1.2 架构设计

```
┌─────────────────────────────────────────────────────────────┐
│                      腾讯云服务器                             │
│  ┌─────────────────┐    ┌─────────────────────────────┐   │
│  │   OpenClaw       │    │     PM2 进程守护             │   │
│  │   Gateway        │    │     - 自动重启              │   │
│  │   Port: 18789    │    │     - 开机自启              │   │
│  └────────┬─────────┘    └─────────────────────────────┘   │
│           │                                                │
│           │ Token 认证                                     │
│           ▼                                                │
│  ┌─────────────────┐    ┌─────────────────────────────┐   │
│  │   Telegram Bot   │    │     Gemini API              │   │
│  │   (待配置)       │    │     (已配置)                │   │
│  └─────────────────┘    └─────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
           │
           │ Token 认证
           ▼
┌─────────────────────────────────────────────────────────────┐
│                      本地 Mac                                │
│  ┌─────────────────┐    ┌─────────────────────────────┐   │
│  │   QClaw         │    │     日常开发使用             │   │
│  │   (本地)        │    │     平台模型路由            │   │
│  └─────────────────┘    └─────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

---

## 2. 安全配置

### 2.1 风险分析

| 端口 | 状态 | 风险说明 |
|------|------|----------|
| 18789 | 监听 0.0.0.0 | ⚠️ 腾讯云告警：公网暴露 |
| 22 | SSH | 仅密钥认证 |

### 2.2 当前安全措施

| 措施 | 状态 | 说明 |
|------|------|------|
| **Gateway Token** | ✅ 已配置 | `OPENCLAW_Gateway_Token=11c1...` |
| **SSH 密钥认证** | ✅ 已配置 | 本地私钥远程登录 |
| **端口放行** | ✅ 仅 18789 | 腾讯云防火墙仅放行所需端口 |

### 2.3 风险评估

#### 腾讯云告警分析

```
检测到 OpenClaw 服务暴露在公网 18789 端口
└── 黑客可利用该服务组件漏洞进行勒索攻击
```

#### 实际风险等级：🟡 中低风险

**原因**：
1. **Token 认证**：Gateway 强制要求 Token，任何访问都需要有效 Token
2. **非默认端口**：非 22/3389 等常见高危端口
3. **无已知公开漏洞**：OpenClaw 是相对小众的服务
4. **腾讯云基础防护**：云厂商有基础 DDoS/扫描防护

#### 建议增强措施

| 措施 | 优先级 | 说明 |
|------|--------|------|
| **IP 白名单** | 🟡 建议 | 仅允许本地 IP 访问 |
| **Cloudflare Tunnel** | 🟢 可选 | 零暴露公网方案 |
| **定期更新** | 🟢 建议 | 关注 OpenClaw 安全更新 |
| **日志监控** | 🟢 建议 | 开启异常访问告警 |

### 2.4 IP 白名单配置（可选）

如果需要限制访问来源：

```bash
# 在服务器配置 iptables
iptables -A INPUT -p tcp --dport 18789 -s YOUR_LOCAL_IP -j ACCEPT
iptables -A INPUT -p tcp --dport 18789 -j DROP
```

---

## 3. 部署流程

### 3.1 环境准备

```bash
# 1. 安装 Node.js v22
curl -fsSL https://deb.nodesource.com/setup_22.x | sudo -E bash -
sudo apt-get install -y nodejs

# 2. 安装 pnpm
npm install -g pnpm

# 3. 验证版本
node -v  # v22.22.2
pnpm -v  # 10.33.0
```

### 3.2 安装 OpenClaw

```bash
# 1. 创建目录
mkdir -p ~/openclaw && cd ~/openclaw

# 2. 安装依赖（使用阿里云镜像）
npm config set registry https://registry.npmmirror.com
pnpm config set registry https://registry.npmmirror.com

# 3. 克隆或安装 OpenClaw
# 由于无法访问 github，需要手动上传源码或使用其他方式
```

### 3.3 环境变量配置

```bash
# ~/.openclaw/.env
OPENCLAW_GATEWAY_TOKEN=11c1082fe49621edea8b179b4f38ee241232867190a968a905c8080e4f33c3d4
GEMINI_API_KEY=AIzaSyCVhNp1t1OSw8Q1jwYSxv0HG8qK9otcB9Q
OPENCLAW_STATE_DIR=~/.openclaw
OPENCLAW_CONFIG_PATH=~/.openclaw/openclaw.json
```

### 3.4 PM2 进程守护

```bash
# 安装 PM2
npm install -g pm2

# 启动 OpenClaw
pm2 start npm --name "openclaw" -- start

# 设置开机自启
pm2 startup
pm2 save

# 常用命令
pm2 status          # 查看状态
pm2 logs openclaw   # 查看日志
pm2 restart openclaw  # 重启
pm2 stop openclaw   # 停止
```

### 3.5 腾讯云防火墙配置

| 端口 | 协议 | 来源 | 说明 |
|------|------|------|------|
| 18789 | TCP | 0.0.0.0/0 | OpenClaw Gateway |
| 22 | TCP | 仅密钥 | SSH（已有规则） |

---

## 4. 连接配置

### 4.1 SSH 配置

```bash
# ~/.ssh/config
Host qclaw
    HostName 101.34.72.227
    User ubuntu
    IdentityFile ~/.ssh/qclaw_key.pem
    Port 22
```

### 4.2 连接命令

```bash
# 连接服务器
ssh qclaw

# 查看 OpenClaw 状态
ssh qclaw "pm2 status"

# 查看日志
ssh qclaw "pm2 logs openclaw --lines 50"
```

---

## 5. 运维指南

### 5.1 日常维护

| 操作 | 命令 |
|------|------|
| 查看状态 | `ssh qclaw "pm2 status"` |
| 查看日志 | `ssh qclaw "pm2 logs openclaw --lines 100"` |
| 重启服务 | `ssh qclaw "pm2 restart openclaw"` |
| 检查端口 | `ssh qclaw "ss -tlnp \| grep 18789"` |

### 5.2 故障排查

```bash
# 1. 检查进程是否运行
ssh qclaw "pm2 status"

# 2. 检查端口监听
ssh qclaw "ss -tlnp | grep 18789"

# 3. 检查日志
ssh qclaw "pm2 logs openclaw --err --lines 50"

# 4. 检查 Token 配置
ssh qclaw "cat ~/.openclaw/.env"
```

### 5.3 数据备份

```bash
# 备份配置
ssh qclaw "tar -czf ~/openclaw_backup.tar.gz ~/.openclaw/"
scp qclaw:~/openclaw_backup.tar.gz ~/Desktop/
```

---

## 6. 配置参数汇总

| 参数 | 值 |
|------|-----|
| 服务器公网 IP | 101.34.72.227 |
| 服务器内网 IP | 10.0.0.17 |
| Gateway 端口 | 18789 |
| Gateway Token | `11c1082fe49621edea8b179b4f38ee241232867190a968a905c8080e4f33c3d4` |
| 模型 | gemini/gemini-2.5-pro |
| API Key | `AIzaSyCVhNp1t1OSw8Q1jwYSxv0HG8qK9otcB9Q` |
| SSH 别名 | qclaw |
| SSH 私钥 | ~/.ssh/qclaw_key.pem |
| PM2 进程名 | openclaw |

---

## 7. 腾讯云告警说明

### 7.1 告警内容

```
【腾讯云】检测到 OpenClaw 服务暴露在公网 18789 端口
黑客可利用该服务组件漏洞进行勒索攻击
```

### 7.2 风险说明

| 因素 | 评估 |
|------|------|
| 端口暴露 | 确实暴露在公网 |
| Token 认证 | ✅ 有效，无 Token 无法访问 |
| 已知漏洞 | ❌ 未发现公开漏洞 |
| 攻击价值 | 🟡 中低，黑客更倾向高价值目标 |

### 7.3 建议处理

| 选项 | 操作 | 适用场景 |
|------|------|----------|
| **忽略** | 已有 Token 保护，可忽略 | 临时测试 |
| **IP 白名单** | 限制仅本地 IP 访问 | 推荐生产使用 |
| **Cloudflare Tunnel** | 零暴露公网 | 高安全要求 |

---

**文档结束**
