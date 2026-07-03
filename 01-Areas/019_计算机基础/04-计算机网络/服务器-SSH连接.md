---
aliases:
  - 连接服务器
tags:
  - 计算机基础
  - SSH
  - 服务器
  - 计算机网络
  - 程序员必学
---
在 Windows 11 上连接服务器的具体步骤会根据服务器类型（如 Linux 或 Windows）和连接协议的不同有所区别。以下是针对 Windows 11 的常用方法及详细操作：

---

### **一、连接 Linux 服务器（使用 SSH）**
Windows 11 内置了 OpenSSH 客户端，无需安装第三方工具即可通过 SSH 连接 Linux 服务器。

#### **步骤：**
1. **打开 PowerShell 或终端**：
   - 按下 `Win + S`，搜索“PowerShell”或“终端”，选择以管理员或普通用户身份打开。

2. **输入 SSH 命令**：
   ```bash
   ssh username@server_ip
   ```
   - `username`：服务器用户名（如 `root` 或普通用户）。
   - `server_ip`：服务器的 IP 地址或域名（如 `192.168.1.100` 或 `example.com`）。

3. **首次连接验证**：
   - 首次连接时会提示是否信任服务器指纹，输入 `yes` 确认。

4. **输入密码**：
   - 输入服务器用户的密码（输入时不会显示字符），回车后即可登录。

#### **示例：**
```bash
ssh user@203.0.113.45
```

#### **进阶功能**：
- **使用密钥登录**（推荐更安全）：
  1. 生成密钥对：`ssh-keygen -t rsa`（默认保存在 `C:\Users\你的用户名\.ssh\`）。
  2. 将公钥（`id_rsa.pub`）内容复制到服务器的 `~/.ssh/authorized_keys` 文件中。
  3. 登录时无需密码：`ssh -i C:\Users\用户名\.ssh\id_rsa user@server_ip`。

---
	·
### **二、连接 Windows 服务器（使用 RDP）**
Windows 11 内置了远程桌面工具（Remote Desktop Protocol, RDP），可直接连接 Windows 服务器。

#### **步骤：**
1. **启用远程桌面**（在服务器端）：
   - 服务器需开启远程桌面权限：`设置 > 系统 > 远程桌面 > 启用`。

2. **打开远程桌面客户端**：
   - 按下 `Win + S`，搜索“远程桌面连接”并打开。

3. **输入服务器信息**：
   - 输入服务器的 IP 地址或计算机名。

4. **输入凭据**：
   - 输入服务器的用户名和密码（需有远程登录权限的账户）。

5. **连接**：
   - 点击“连接”，首次连接可能需要确认证书警告。

#### **示例：**
- 服务器 IP：`192.168.1.100`
- 用户名：`Administrator`
- 密码：`YourPassword`

---

### **三、文件传输（使用 SFTP 或 SCP）**
Windows 11 支持通过命令行或第三方工具（如 WinSCP、FileZilla）进行文件传输。

#### **方法 1：通过 PowerShell 使用 SCP 命令**
1. 打开 PowerShell。
2. 上传文件到服务器：
   ```bash
   scp C:\本地文件路径 username@server_ip:/目标路径
   ```
3. 下载文件到本地：
   ```bash
   scp username@server_ip:/服务器文件路径 C:\本地目标路径
   ```

#### **方法 2：使用 WinSCP（图形化工具）**
1. 下载安装 [WinSCP](https://winscp.net/)。
2. 新建会话：
   - 协议：`SFTP` 或 `SCP`。
   - 主机名：服务器 IP。
   - 用户名/密码：服务器凭据。
3. 拖拽文件进行传输。

---

### **四、使用第三方工具（可选）**
- **PuTTY**：适用于 SSH 连接（下载地址：[PuTTY](https://www.chiark.greenend.org.uk/~sgtatham/putty/)）。
- **MobaXterm**：集成 SSH、SFTP、远程桌面等功能（适合多协议需求）。

---

### **五、常见问题排查**
1. **连接超时/拒绝**：
   - 检查服务器 IP 和端口是否正确。
   - 确认服务器防火墙开放了相应端口（SSH 默认 22，RDP 默认 3389）。
   - 确保服务器已启动并联网。

2. **认证失败**：
   - 检查用户名和密码是否输入正确。
   - 如果是密钥登录，确认公钥已正确添加到服务器。

3. **权限问题**：
   - Linux 服务器用户需有 SSH 登录权限（检查 `/etc/ssh/sshd_config`）。
   - Windows 服务器用户需加入“Remote Desktop Users”组。

---

### **总结**
- **Linux 服务器**：使用内置的 SSH 客户端（PowerShell/终端）。
- **Windows 服务器**：使用远程桌面连接（RDP）。
- **文件传输**：通过 SCP 命令或 WinSCP 工具。
- **安全建议**：优先使用 SSH 密钥登录，避免密码泄露。

如果需要更具体的场景指导（如云服务器连接、端口配置），可以进一步补充说明！