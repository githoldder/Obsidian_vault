# HTTPS（超文本传输安全协议）

## 📖 定义-解释-示例

### 什么是 HTTPS？

**HTTPS**（HyperText Transfer Protocol Secure）是 HTTP 的安全版本，通过 **TLS/SSL** 协议在 HTTP 和 TCP 之间插入加密层，默认端口 **443**。HTTPS 提供三个核心安全保障：**加密**（防窃听）、**完整性校验**（防篡改）、**身份认证**（防冒充）。

### HTTPS 解决了 HTTP 的什么痛点？

HTTP 明文传输，存在三大安全风险：
1. **窃听风险**：第三方可截获通信内容（如密码、信用卡号）
2. **篡改风险**：中间人可修改内容（如插入广告、劫持链接）
3. **冒充风险**：攻击者可伪装成目标网站（钓鱼攻击）

HTTPS 通过 TLS 协议一举解决这三个问题。从用户视角看，浏览器地址栏的 🔒 锁图标 = HTTPS 连接已建立。

### HTTPS 的工作流程（TLS 1.2 握手）

HTTPS 连接建立的核心是 **TLS 握手**。完整流程如下：

1. **ClientHello** → 客户端发送支持的 TLS 版本、加密套件列表（如 `TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256`）、随机数 `client_random`
2. **ServerHello** ← 服务器选定加密套件、返回随机数 `server_random` 和**数字证书**（含公钥）
3. **证书验证**：客户端验证证书链（CA 签名 → 中间 CA → 根 CA），检查域名匹配、有效期、吊销状态
4. **密钥交换**：客户端生成 `premaster_secret`，用服务器公钥加密后发送。双方用 `client_random` + `server_random` + `premaster_secret` 计算 **会话密钥**（对称密钥）
5. **加密通信**：双方使用会话密钥进行**对称加密**通信，效率远高于非对称加密

**关键洞察**：非对称加密只在握手阶段使用（建立信任），实际数据传输用对称加密（保证性能）。这就是 HTTPS "快"的秘诀——不是全链路非对称加密。

### TLS 1.3 的改进

TLS 1.3（2018）做了重大精简：
- 握手从 **2-RTT** 降低到 **1-RTT**（甚至 0-RTT 会话恢复）
- 移除了不安全的加密套件（RC4、MD5、SHA-1、3DES）
- 仅支持前向安全（Forward Secrecy）的密钥交换算法（ECDHE）
- 0-RTT 模式允许客户在首次 Hello 消息中就携带数据，但面临重放攻击风险

### 数字证书与 PKI

HTTPS 可信赖的根基在于 **PKI（公钥基础设施）**：

- **CA（证书颁发机构）**：受信任的第三方如 Let's Encrypt（免费）、DigiCert、GlobalSign
- **证书链**：服务器证书 → 中间 CA 证书 → 根 CA 证书（预装于操作系统/浏览器）
- **证书内容**：域名、组织信息、公钥、有效期、签发者、签名算法、指纹
- **证书透明（Certificate Transparency）**：Google 推动的机制，所有证书必须公开记录到 CT 日志，防止 CA 错误签发或恶意签发

### HTTPS 的部署要素

- **服务器配置**：安装证书（PEM 格式 + 私钥），配置 Nginx/Apache 监听 443 端口，设置重定向（80 → 443）
- **HSTS（HTTP Strict Transport Security）**：`Strict-Transport-Security: max-age=31536000; includeSubDomains; preload` 强制浏览器仅通过 HTTPS 访问，防止 SSL 剥离攻击
- **SNI（Server Name Indication）**：一个 IP 托管多个 HTTPS 站点时，客户端在 ClientHello 中声明目标域名，服务器据此返回对应证书
- **OCSP Stapling**：服务器主动提供证书状态（有效/吊销），避免客户端实时查询 OCSP 服务器带来的隐私泄露和延迟

### HTTPS 对性能的影响

HTTPS 确实带来额外开销：TLS 握手增加延迟、加解密消耗 CPU。但现代优化手段已大幅缩小差距：
- **Session Resumption**：复用之前握手的会话密钥（Session ID / Session Ticket 机制），免去重复握手
- **False Start**：客户端不等握手完成即发送数据，减半延迟
- **HTTP/2**：通常只在 HTTPS 上生效，多路复用抵消了部分延迟
- **硬件加速**：现代 CPU 的 AES-NI 指令集将对称加密开销降至几乎可忽略

### 常见误区

**误区1**："HTTPS 很慢" → HTTP/2 只在 HTTPS 下生效，开启 HTTP/2 的 HTTPS 通常比 HTTP/1.1 的 HTTP 更快。
**误区2**："只有登录页面才需要 HTTPS" → 全站 HTTPS 是业界标准。即使用户只是浏览首页，不加密意味着攻击者可以注入恶意脚本。
**误区3**："自签名证书也能用" → 自签名证书无法通过浏览器验证，会触发安全警告。生产环境必须使用 CA 签发的证书。

## 🔗 相关笔记
[[应用层-万维网-HTTP]]
[[TLS与SSL协议]]
[[数字证书与PKI]]
[[加密算法-对称与非对称]]
[[HTTP-2协议]]
[[Nginx配置]]
[[中间人攻击]]
[[前向安全]]
[[Let's Encrypt]]
[[网络安全基础]]

🏷️: [[计算机网络]] [[HTTPS]] [[TLS]] [[SSL]] [[网络安全]] [[应用层]]
