# 解决 macOS 使用 Homebrew 安装国外镜像切换代理后的配置问题

> **文档版本**：2026-04-02
> **适用场景**：macOS 终端代理配置、Homebrew 代理、切换梯子后的配置迁移

---

## 一、问题背景

### 1.1 常见症状

| 症状 | 原因 |
|------|------|
| `curl: (7) Failed to connect to 127.0.0.1 port XXXXX` | 代理端口配置错误 |
| `brew install` 超时或失败 | Homebrew 未配置代理 |
| `git clone` 速度极慢 | Git 未配置代理 |
| `opencode: command not found` | PATH 未正确配置 |
| `Unable to connect. Is the computer able to access the url?` | CLI 工具未读取代理设置 |

### 1.2 根本原因

切换梯子（如 Clash → V2Ray → Xray）后，本地监听端口会变化，但 `.zshrc` 中的代理配置未同步更新。

**关键点**：
- 梯子客户端监听端口 ≠ 远程代理服务器端口
- 应该连接**本地监听端口**，不是远程端口

---

## 二、快速诊断

### 2.1 查看当前代理客户端监听端口

```bash
# 查看代理客户端（clash/xray/v2ray/surge等）监听的本地端口
lsof -i -P | grep -E "clash|xray|v2ray|surge|shadowsocks" | grep LISTEN
```

**输出示例**：
```
xray 26273 caolei 4u IPv4 ... TCP localhost:10808 (LISTEN)
```

→ **本地代理端口是 `10808`**

### 2.2 检查当前环境变量配置

```bash
# 检查代理环境变量
env | grep -i proxy

# 检查 shell 配置文件
grep -i proxy ~/.zshrc
```

### 2.3 对比是否一致

| 配置位置 | 端口 | 是否正确 |
|----------|------|----------|
| `.zshrc` 中的 `HTTP_PROXY` | 17569（旧） | ❌ |
| xray 实际监听端口 | 10808 | ✅ |

---

## 三、解决方案

### 3.1 标准配置（推荐）

编辑 `~/.zshrc`，添加以下配置：

```bash
# ============================================================================
# 代理配置（切换梯子后只需修改这个端口）
# ============================================================================
PROXY_PORT=10808  # ← 修改这里即可

export HTTP_PROXY="http://127.0.0.1:${PROXY_PORT}"
export HTTPS_PROXY="http://127.0.0.1:${PROXY_PORT}"
export ALL_PROXY="socks5://127.0.0.1:${PROXY_PORT}"

# 不走代理的地址（本地开发用）
export NO_PROXY="localhost,127.0.0.1,::1,*.local"

# ============================================================================
# Homebrew 代理配置
# ============================================================================
export ALL_PROXY_FOR_BREW="${ALL_PROXY}"

# ============================================================================
# Git 代理配置
# ============================================================================
# HTTP/HTTPS 协议
git config --global http.proxy "http://127.0.0.1:${PROXY_PORT}"
git config --global https.proxy "http://127.0.0.1:${PROXY_PORT}"

# SSH 协议（需要配置 ~/.ssh/config）
# 见下文 3.4 节
```

**生效配置**：
```bash
source ~/.zshrc
```

### 3.2 一键切换脚本

在 `~/.zshrc` 中添加函数：

```bash
# ============================================================================
# 代理切换函数
# ============================================================================
proxy_on() {
    local port=${1:-10808}  # 默认端口
    export HTTP_PROXY="http://127.0.0.1:${port}"
    export HTTPS_PROXY="http://127.0.0.1:${port}"
    export ALL_PROXY="socks5://127.0.0.1:${port}"
    echo "✅ 代理已开启: 127.0.0.1:${port}"
}

proxy_off() {
    unset HTTP_PROXY HTTPS_PROXY ALL_PROXY
    echo "❌ 代理已关闭"
}

# 查看当前代理状态
proxy_status() {
    if [ -n "$HTTP_PROXY" ]; then
        echo "当前代理: $HTTP_PROXY"
    else
        echo "代理未设置"
    fi
    
    # 测试连接
    echo -n "测试连接: "
    if curl -sI --connect-timeout 3 https://google.com > /dev/null 2>&1; then
        echo "✅ 成功"
    else
        echo "❌ 失败"
    fi
}
```

**使用方法**：
```bash
# 开启代理（默认端口）
proxy_on

# 开启代理（指定端口）
proxy_on 7890

# 关闭代理
proxy_off

# 查看状态
proxy_status
```

### 3.3 Homebrew 专用配置

Homebrew 有时需要单独配置：

```bash
# 方法一：环境变量（已包含在上面的标准配置中）
export ALL_PROXY_FOR_BREW="${ALL_PROXY}"

# 方法二：使用别名
alias brew='ALL_PROXY="${ALL_PROXY}" brew'
```

**测试 Homebrew 代理**：
```bash
# 不走代理
brew install --verbose wget 2>&1 | head -20

# 走代理
ALL_PROXY="http://127.0.0.1:10808" brew install --verbose wget 2>&1 | head -20
```

### 3.4 Git SSH 代理配置

对于 SSH 协议的 Git 仓库（如 `git@github.com:...`），需要配置 `~/.ssh/config`：

```bash
# ~/.ssh/config

# GitHub SSH 走代理
Host github.com
    HostName github.com
    User git
    ProxyCommand nc -X 5 -x 127.0.0.1:10808 %h %p
    # 或使用 connect-proxy
    # ProxyCommand connect -S 127.0.0.1:10808 %h %p

# GitLab SSH 走代理
Host gitlab.com
    HostName gitlab.com
    User git
    ProxyCommand nc -X 5 -x 127.0.0.1:10808 %h %p

# 所有 SSH 连接走代理（可选，影响所有 SSH）
# Host *
#     ProxyCommand nc -X 5 -x 127.0.0.1:10808 %h %p
```

**安装 netcat（如果没有）**：
```bash
brew install netcat
```

---

## 四、不同代理客户端的默认端口

| 代理客户端 | HTTP 端口 | SOCKS5 端口 | 配置文件位置 |
|------------|-----------|-------------|--------------|
| **Clash** | 7890 | 7891 | `~/.config/clash/config.yaml` |
| **ClashX Pro** | 7890 | 7891 | 菜单栏 → 配置 → 打开配置文件夹 |
| **V2RayU** | 1087 | 1080 | 菜单栏 → Configure |
| **V2RayX** | 1087 | 1080 | `~/Library/Application Support/V2RayX/` |
| **Xray** | 自定义 | 自定义 | 通常是 `10808` 或配置文件指定 |
| **Surge** | 6152 | 6153 | 菜单栏 → Open Config |
| **ShadowsocksX** | 1087 | 1086 | 菜单栏 → Preferences |
| **QClaw** | 19000 | - | 内置代理 |

**如何查找**：
```bash
# 方法一：查看监听端口
lsof -i -P | grep LISTEN | grep -E "7890|10808|1087|1080|6152"

# 方法二：查看应用配置文件
find ~/Library/Application\ Support -name "*.yaml" -o -name "*.json" 2>/dev/null | xargs grep -l "port" 2>/dev/null
```

---

## 五、切换梯子后的操作清单

### 5.1 标准流程

```bash
# 1️⃣ 查看新梯子的监听端口
lsof -i -P | grep -E "clash|xray|v2ray|surge" | grep LISTEN

# 2️⃣ 修改 ~/.zshrc 中的端口
nano ~/.zshrc
# 找到 PROXY_PORT=xxxx 并修改

# 3️⃣ 生效配置
source ~/.zshrc

# 4️⃣ 更新 Git 代理配置
git config --global http.proxy "http://127.0.0.1:新端口"
git config --global https.proxy "http://127.0.0.1:新端口"

# 5️⃣ 更新 SSH 配置（如果有使用 SSH Git）
nano ~/.ssh/config
# 修改 ProxyCommand 中的端口

# 6️⃣ 验证连接
curl -I https://google.com
git ls-remote https://github.com/some/repo.git
```

### 5.2 快速脚本

```bash
# 保存为 ~/bin/switch-proxy.sh
#!/bin/bash

NEW_PORT=$1

if [ -z "$NEW_PORT" ]; then
    echo "用法: switch-proxy.sh <端口号>"
    echo "示例: switch-proxy.sh 10808"
    exit 1
fi

# 更新 .zshrc
sed -i.bak "s/PROXY_PORT=.*/PROXY_PORT=${NEW_PORT}/" ~/.zshrc

# 更新环境变量
export HTTP_PROXY="http://127.0.0.1:${NEW_PORT}"
export HTTPS_PROXY="http://127.0.0.1:${NEW_PORT}"
export ALL_PROXY="socks5://127.0.0.1:${NEW_PORT}"

# 更新 Git 配置
git config --global http.proxy "http://127.0.0.1:${NEW_PORT}"
git config --global https.proxy "http://127.0.0.1:${NEW_PORT}"

echo "✅ 代理已切换到端口: ${NEW_PORT}"
echo "请重新打开终端窗口以完全生效"

# 测试
echo -n "测试连接: "
if curl -sI --connect-timeout 3 https://google.com > /dev/null 2>&1; then
    echo "✅ 成功"
else
    echo "❌ 失败，请检查端口是否正确"
fi
```

---

## 六、TUN 模式说明

### 6.1 TUN 模式的优势

开启 TUN 模式后，**所有 TCP/UDP 流量**自动走代理，无需配置环境变量。

**检查 TUN 是否生效**：
```bash
# 查看 TUN 网卡
ifconfig | grep -A 5 "utun"

# 查看路由表
netstat -rn | grep utun
```

### 6.2 TUN 模式的局限

| 场景 | 是否自动代理 | 是否需要环境变量 |
|------|-------------|-----------------|
| 浏览器访问网页 | ✅ 自动 | 不需要 |
| `curl` 命令 | ✅ 自动 | 不需要 |
| `git clone` | ✅ 自动 | 不需要 |
| `brew install` | ✅ 自动 | 不需要 |
| 某些 CLI 工具（如 OpenCode） | ⚠️ 部分工具 | 可能需要 |
| Docker 容器内部 | ❌ 不代理 | 需要单独配置 |

### 6.3 为什么 OpenCode 仍需要环境变量

OpenCode CLI 内部可能使用了**自定义 HTTP 客户端**，绕过了系统网络栈，因此需要显式设置 `HTTP_PROXY` 环境变量。

---

## 七、常见问题排查

### 7.1 连接超时

```bash
# 检查代理端口是否监听
lsof -i :10808

# 检查代理进程是否运行
ps aux | grep -E "clash|xray|v2ray"

# 测试代理连接
curl -I --proxy http://127.0.0.1:10808 https://google.com
```

### 7.2 Homebrew 仍然很慢

```bash
# 检查是否配置了中科大镜像
brew config

# 如果使用镜像，可能不需要代理
# 临时关闭代理
unset HTTP_PROXY HTTPS_PROXY ALL_PROXY
brew install xxx
```

### 7.3 Git SSH 仓库无法连接

```bash
# 测试 SSH 连接
ssh -T git@github.com

# 检查 SSH 配置
cat ~/.ssh/config

# 手动测试代理
nc -X 5 -x 127.0.0.1:10808 github.com 22
```

### 7.4 某些命令不识别代理

```bash
# 方法一：使用 env 命令强制传递
env HTTP_PROXY=http://127.0.0.1:10808 some-command

# 方法二：在命令前直接设置
HTTP_PROXY=http://127.0.0.1:10808 some-command

# 方法三：使用 proxychains
brew install proxychains-ng
# 编辑 /usr/local/etc/proxychains.conf
# 在 [ProxyList] 下添加：
# socks5 127.0.0.1 10808
proxychains4 some-command
```

---

## 八、配置模板

### 8.1 完整的 ~/.zshrc 代理配置段

```bash
# ============================================================================
# 代理配置
# ============================================================================

# 主代理端口（切换梯子后修改这里）
PROXY_PORT=10808

# 设置代理环境变量
export HTTP_PROXY="http://127.0.0.1:${PROXY_PORT}"
export HTTPS_PROXY="http://127.0.0.1:${PROXY_PORT}"
export ALL_PROXY="socks5://127.0.0.1:${PROXY_PORT}"
export NO_PROXY="localhost,127.0.0.1,::1,*.local,*.lan"

# 小写版本（某些工具需要）
export http_proxy="${HTTP_PROXY}"
export https_proxy="${HTTPS_PROXY}"
export all_proxy="${ALL_PROXY}"
export no_proxy="${NO_PROXY}"

# Git 代理
git config --global http.proxy "${HTTP_PROXY}"
git config --global https.proxy "${HTTPS_PROXY}"

# ============================================================================
# 代理辅助函数
# ============================================================================

# 开启代理
proxy_on() {
    local port=${1:-$PROXY_PORT}
    export HTTP_PROXY="http://127.0.0.1:${port}"
    export HTTPS_PROXY="http://127.0.0.1:${port}"
    export ALL_PROXY="socks5://127.0.0.1:${port}"
    export http_proxy="${HTTP_PROXY}"
    export https_proxy="${HTTPS_PROXY}"
    export all_proxy="${ALL_PROXY}"
    echo "✅ 代理已开启: 127.0.0.1:${port}"
}

# 关闭代理
proxy_off() {
    unset HTTP_PROXY HTTPS_PROXY ALL_PROXY
    unset http_proxy https_proxy all_proxy
    echo "❌ 代理已关闭"
}

# 代理状态
proxy_status() {
    if [ -n "$HTTP_PROXY" ]; then
        echo "当前代理: $HTTP_PROXY"
        echo -n "测试连接: "
        if curl -sI --connect-timeout 3 https://google.com > /dev/null 2>&1; then
            echo "✅ 成功"
        else
            echo "❌ 失败"
        fi
    else
        echo "代理未设置"
    fi
}

# 快速切换代理端口
proxy_switch() {
    local new_port=$1
    if [ -z "$new_port" ]; then
        echo "用法: proxy_switch <端口号>"
        return 1
    fi
    
    # 更新配置文件
    if [ -f ~/.zshrc ]; then
        sed -i.bak "s/PROXY_PORT=.*/PROXY_PORT=${new_port}/" ~/.zshrc
        echo "已更新 ~/.zshrc"
    fi
    
    # 更新当前环境
    proxy_on $new_port
    
    # 更新 Git 配置
    git config --global http.proxy "http://127.0.0.1:${new_port}"
    git config --global https.proxy "http://127.0.0.1:${new_port}"
    
    echo "Git 代理已更新"
    echo "请运行 'source ~/.zshrc' 或重新打开终端以完全生效"
}

# ============================================================================
# 启动时显示代理状态（可选）
# ============================================================================
# proxy_status
```

### 8.2 ~/.ssh/config 模板

```bash
# GitHub SSH 走代理
Host github.com
    HostName github.com
    User git
    ProxyCommand nc -X 5 -x 127.0.0.1:10808 %h %p
    IdentityFile ~/.ssh/id_ed25519

# GitLab SSH 走代理
Host gitlab.com
    HostName gitlab.com
    User git
    ProxyCommand nc -X 5 -x 127.0.0.1:10808 %h %p
    IdentityFile ~/.ssh/id_ed25519

# Gitee SSH（国内，通常不需要代理）
Host gitee.com
    HostName gitee.com
    User git
    IdentityFile ~/.ssh/id_ed25519

# 内网服务器（不走代理）
Host 192.168.*.*
    ProxyCommand none

Host *.local
    ProxyCommand none
```

---

## 九、参考资源

### 9.1 常用代理客户端下载

| 客户端 | 下载地址 | 特点 |
|--------|---------|------|
| **ClashX Pro** | https://github.com/yichengchen/clashX/releases | 功能全面，支持规则 |
| **V2RayU** | App Store 搜索 | 简单易用 |
| **Surge** | https://nssurge.com/ | 专业版付费，功能强大 |
| **ShadowsocksX** | https://github.com/shadowsocks/ShadowsocksX-NG/releases | 经典客户端 |

### 9.2 Homebrew 镜像源

如果代理不稳定，可以使用国内镜像：

```bash
# 中科大镜像
export HOMEBREW_API_DOMAIN="https://mirrors.ustc.edu.cn/homebrew-bottles/api"
export HOMEBREW_BOTTLE_DOMAIN="https://mirrors.ustc.edu.cn/homebrew-bottles"
export HOMEBREW_BREW_GIT_REMOTE="https://mirrors.ustc.edu.cn/brew.git"
export HOMEBREW_CORE_GIT_REMOTE="https://mirrors.ustc.edu.cn/homebrew-core.git"
export HOMEBREW_PIP_INDEX_URL="https://mirrors.ustc.edu.cn/pypi/web/simple"

# 清华镜像
export HOMEBREW_API_DOMAIN="https://mirrors.tuna.tsinghua.edu.cn/homebrew-bottles/api"
export HOMEBREW_BOTTLE_DOMAIN="https://mirrors.tuna.tsinghua.edu.cn/homebrew-bottles"
export HOMEBREW_BREW_GIT_REMOTE="https://mirrors.tuna.tsinghua.edu.cn/git/homebrew/brew.git"
export HOMEBREW_CORE_GIT_REMOTE="https://mirrors.tuna.tsinghua.edu.cn/git/homebrew/homebrew-core.git"
```

---

## 十、快速参考卡片

```
┌─────────────────────────────────────────────────────────────┐
│                    macOS 终端代理快速参考                      │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  📌 查看代理端口：                                            │
│     lsof -i -P | grep -E "clash|xray|v2ray" | grep LISTEN   │
│                                                             │
│  📌 设置环境变量：                                            │
│     export HTTP_PROXY=http://127.0.0.1:10808                │
│     export HTTPS_PROXY=http://127.0.0.1:10808               │
│     export ALL_PROXY=socks5://127.0.0.1:10808              │
│                                                             │
│  📌 Git 代理：                                               │
│     git config --global http.proxy http://127.0.0.1:10808  │
│     git config --global https.proxy http://127.0.0.1:10808 │
│                                                             │
│  📌 测试连接：                                               │
│     curl -I https://google.com                             │
│     curl -I --proxy http://127.0.0.1:10808 https://google.com│
│                                                             │
│  📌 关闭代理：                                               │
│     unset HTTP_PROXY HTTPS_PROXY ALL_PROXY                 │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

*文档最后更新：2026-04-02*
*作者：总管*
