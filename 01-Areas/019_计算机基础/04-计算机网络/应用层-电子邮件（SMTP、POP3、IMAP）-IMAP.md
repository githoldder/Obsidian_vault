# IMAP（互联网消息访问协议）

## 📖 定义-解释-示例

### 什么是 IMAP？

**IMAP**（Internet Message Access Protocol）是互联网电子邮件的**接收和管理**协议，运行在应用层，默认端口 **143**（明文）/ **993**（SSL/TLS）。IMAP 采用**在线模式**：邮件**始终存储在服务器**上，客户端只是"窗口"来查看和操作邮件。所有状态（已读/未读、标记、文件夹）在服务器端同步，多设备间自动一致。

### IMAP 与 POP3 的哲学差异

POP3 的设计哲学：**把你的邮件下载到本地，然后从服务器清理走**——适合单设备时代。
IMAP 的设计哲学：**邮件永远留在服务器上，客户端只是一个视图**——为云计算和多设备时代而生。

这个差异决定了几乎所有的行为区别：
- 换手机？IMAP 用户毫无感觉，所有邮件都在
- 搜索引擎查旧邮件？IMAP 可以在服务器端搜索
- 标记了一封邮件为"重要"？IMAP 在所有设备上自动同步
- 只想看邮件头判断是否下载？IMAP 支持只取头部

### IMAP 的会话过程

IMAP 使用**标签化命令**（tagged command），支持多个命令并发：

```
S: * OK IMAP4rev1 server ready
C: A001 LOGIN alice@example.com password123
S: A001 OK LOGIN completed
C: A002 LIST "" "*"                       ← 列出所有邮箱
S: * LIST (\HasNoChildren) "/" INBOX
S: * LIST (\HasNoChildren) "/" Sent
S: * LIST (\HasNoChildren) "/" Drafts
S: * LIST (\HasNoChildren) "/" Trash
S: A002 OK LIST completed
C: A003 SELECT INBOX                     ← 选择收件箱
S: * 42 EXISTS                           ← 42封邮件
S: * 5 RECENT                            ← 5封新邮件
S: * FLAGS (\Answered \Flagged \Draft \Deleted \Seen)
S: A003 OK SELECT completed
C: A004 FETCH 1:10 (FLAGS BODY.PEEK[HEADER.FIELDS (FROM SUBJECT DATE)])
S: * 1 FETCH (FLAGS (\Seen) ...)
S: * 2 FETCH (FLAGS () ...)
S: ...
S: A004 OK FETCH completed
C: A005 FETCH 3 (BODY[])                 ← 下载第3封全文
S: * 3 FETCH (BODY[] {2845}
...完整邮件内容...)
S: A005 OK FETCH completed
C: A006 STORE 3 +FLAGS (\Flagged)        ← 标记第3封为重要
S: * 3 FETCH (FLAGS (\Flagged \Seen))
S: A006 OK STORE completed
C: A007 LOGOUT
S: * BYE IMAP server logging out
S: A007 OK LOGOUT completed
```

### IMAP 的核心特性

**1. 服务器端存储**
邮件永久保存在服务器。客户端可以是"瘦客户端"——只加载当前在看的邮件，不占用本地空间。需要查看历史邮件时再按需下载。

**2. 多设备同步**
在手机上将邮件标记为已读 → 电脑上也自动显示已读。移动邮件到文件夹 → 所有设备同步。因为操作对象是**服务器上的邮件**，而非本地副本。

**3. 选择性下载**
`FETCH` 命令可以精确指定要获取的内容：
- `BODY.PEEK[HEADER]`：只看头部，判断是否值得下载
- `BODY[TEXT]`：只下载正文，不含头部
- `BODY[1]`：只下载 MIME 第一个部分（如纯文本版本）
- `BODY[]`：下载完整邮件
- 特别适合移动网络：先看头部，再决定是否花流量下载附件

**4. 服务器端搜索**
`SEARCH` 命令直接在服务器执行搜索，无需下载所有邮件到本地。支持搜索条件：发件人、主题、日期范围、已读/未读状态、关键字、附件大小等。`SEARCH FROM "boss" SINCE 1-Jan-2023`

**5. 文件夹管理**
IMAP 支持服务器端文件夹（邮箱）的创建、重命名、删除、移动。邮件可以在文件夹之间移动，服务端实现。与 Gmail 的标签体系不同（标签是 IMAP 的扩展实现）。

**6. 空闲通知（IDLE）**
`IDLE` 命令允许客户端维持开放连接，服务器有新邮件到达时**主动推送通知**，无需客户端轮询。这实现了近乎实时的邮件到达提示。

### IMAP 的局限性

- **依赖网络**：IMAP 是在线协议，离线时需要客户端本地缓存
- **服务器存储成本**：所有邮件存服务器，供应商需要大量存储
- **协议复杂**：比 POP3 复杂得多，实现成本更高
- **大附件下载慢**：每次打开大附件邮件都需下载
- **并发问题**：多客户端同时操作同一封邮件可能冲突

### IMAP 的实际使用建议

- **主力邮箱**：首选 IMAP，享受多设备同步
- **临时/归档邮箱**：POP3 也可，邮件只需一个地方存
- **大附件清理**：IMAP 用户应定期清理大附件邮件，避免邮箱超限
- **离线备份**：即使使用 IMAP，也应定期做本地备份（导出 PST/MBOX）

## 🔗 相关笔记
[[应用层-电子邮件-SMTP]]
[[应用层-电子邮件-POP3]]
[[POP3 vs IMAP 对比]]
[[Gmail标签体系]]
[[邮件客户端配置]]
[[TCP端口号]]
[[TLS与SSL协议]]
[[邮件服务器架构]]
[[MIME多用途互联网邮件扩展]]
[[IMAP IDLE推送机制]]

🏷️: [[计算机网络]] [[IMAP]] [[电子邮件]] [[应用层]] [[邮件协议]]
