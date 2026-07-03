# SMTP（简单邮件传输协议）

## 📖 定义-解释-示例

### 什么是 SMTP？

**SMTP**（Simple Mail Transfer Protocol）是互联网电子邮件的**发送**协议，运行在应用层，默认端口 **25**（明文）/ **465**（SSL/TLS）/ **587**（STARTTLS）。SMTP 遵循"发送-转发"模型：邮件用户代理（MUA）→ 邮件传输代理（MTA）→ 目标 MTA → 邮件投递代理（MDA）→ 收件人邮箱。

SMTP 只负责**发送和转发**——它不处理接收和存储，这两个任务分别交给 **POP3** 和 **IMAP**。

### SMTP 的工作流程

一封邮件的完整发送过程：

1. 发件人用 MUA（Outlook/Thunderbird/Webmail）编写邮件，点击发送
2. MUA 通过 SMTP 连接发件人的邮件服务器（如 smtp.gmail.com:587）
3. 发件人邮件服务器（MTA）通过 DNS 查询收件人域的 **MX 记录**，找到目标邮件服务器
4. SMTP 逐跳转发（可能经过多个中继服务器），直到到达目标 MTA
5. 目标 MTA 将邮件投递给 MDA，存入收件人邮箱

### SMTP 的会话过程

SMTP 是**命令-响应**模式的文本协议。典型会话：

```
S: 220 mail.example.com ESMTP Ready
C: EHLO client.example.com         ← 扩展 Hello
S: 250-mail.example.com
S: 250-STARTTLS
S: 250 AUTH LOGIN PLAIN
C: STARTTLS                        ← 升级为加密连接
S: 220 Ready
C: AUTH LOGIN                      ← 身份认证
S: 334 VXNlcm5hbWU6
C: dXNlckBleGFtcGxlLmNvbQ==       ← Base64 编码的用户名
S: 334 UGFzc3dvcmQ6
C: cGFzc3dvcmQ=                    ← Base64 编码的密码
S: 235 Authentication successful
C: MAIL FROM:<alice@example.com>   ← 发件人信封地址
S: 250 OK
C: RCPT TO:<bob@other.com>         ← 收件人信封地址
S: 250 OK
C: DATA                            ← 开始发送邮件内容
S: 354 Start mail input
C: From: Alice <alice@example.com>
C: To: Bob <bob@other.com>
C: Subject: Meeting Tomorrow
C: Content-Type: text/plain
C:
C: Hi Bob, let's meet at 2pm.
C: .                               ← 单独一行 `.` 结束
S: 250 Message accepted
C: QUIT
S: 221 Bye
```

**关键要点**：
- `MAIL FROM` 和 `RCPT TO` 是**信封地址**（用于路由），邮件正文中的 `From:` 和 `To:` 是**展示地址**（用户可见），二者可以不同（这就是垃圾邮件常见手法）
- `EHLO` 是 SMTP 扩展版本，替代旧的 `HELO`，支持查询服务器能力（STARTTLS、AUTH、SIZE 等）
- `DATA` 命令后的内容以单独一行 `.` 结束

### SMTP 的关键特性

**存储-转发模型**：SMTP 是典型的存储-转发协议。如果目标服务器暂时不可达，中间 MTA 会将邮件排队重试，通常在 4 天内最多重试 3-5 次，之后退回发件人（bounce message）。

**MIME 扩展**：原始 SMTP 只支持 7-bit ASCII 文本。**MIME**（多用途互联网邮件扩展）通过 `Content-Type`、`Content-Transfer-Encoding` 头支持：多语言字符（UTF-8）、HTML 格式邮件、附件（Base64 编码）、内嵌图片（`multipart/related`）

**SMTP 的安全扩展**：
- **STARTTLS**（端口 587）：先建立明文连接，再用 TLS 升级为加密
- **SMTPS**（端口 465）：全程 TLS 加密
- **SPF（Sender Policy Framework）**：DNS TXT 记录声明哪些 IP 有权以该域名义发邮件
- **DKIM（DomainKeys Identified Mail）**：用公私钥签名邮件，验证来源和完整性
- **DMARC**：结合 SPF + DKIM，定义认证失败时的处理策略（拒绝/隔离/放行）

### SMTP 的局限性

- **仅发送，不接收**：需要配合 POP3/IMAP 完成完整邮件系统
- **文本协议**：明文通信存在安全风险（STARTTLS 部分解决）
- **垃圾邮件问题**：SMTP 本身缺乏发送者身份验证（SPF/DKIM/DMARC 弥补）
- **大附件问题**：Base64 编码使附件体积膨胀约 33%，且许多服务器限制单封邮件大小

## 🔗 相关笔记
[[应用层-电子邮件-POP3]]
[[应用层-电子邮件-IMAP]]
[[MIME多用途互联网邮件扩展]]
[[DNS-MX记录]]
[[SPF-DKIM-DMARC邮件认证]]
[[电子邮件的安全机制]]
[[TCP端口号]]
[[Base64编码]]
[[TLS与SSL协议]]
[[ESMTP扩展]]

🏷️: [[计算机网络]] [[SMTP]] [[电子邮件]] [[应用层]] [[邮件协议]]
