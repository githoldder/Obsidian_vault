### 一、SSH 公钥和私钥是怎么回事？

SSH（Secure Shell）是一种用于通过网络进行安全通信的协议。当你使用 Git 进行操作（如 `git push` 或 `git pull`）时，Git 会使用 SSH 或 HTTPS 协议来与远程仓库进行交互。如果你使用 SSH，Git 会使用一对 **公钥** 和 **私钥** 来进行身份验证。

#### 1. **公钥与私钥的关系**

- **私钥**：这是你的个人密钥，应该保存在你本地的计算机上，并且保持秘密。私钥用于加密和解密信息。
    
- **公钥**：这是公开的密钥，你可以将它分享给其他人或服务器。公钥用于加密数据，只有持有对应私钥的人才能解密。
    

在使用 SSH 进行身份验证时，Git 会将你的公钥存储在远程服务器上（例如 Gitea），而当你执行 `git push` 等操作时，Git 会用你的私钥生成一个加密签名，服务器会用公钥进行验证。如果公钥能够成功解密私钥生成的签名，就说明你是合法的用户。

#### 2. **公钥私钥的工作原理**

- **生成密钥对**：首先你需要生成一对公钥和私钥。
    
- **上传公钥到 Gitea**：将生成的公钥上传到 Gitea，这样 Gitea 就能够识别你的身份。
    
- **使用私钥进行验证**：每当你执行 `git push` 等操作时，本地的 Git 会使用私钥生成一个签名，远程的 Gitea 会使用你的公钥验证这个签名。

### 二、如何在 Gitea 中配置 SSH 密钥？

#### 1. **生成 SSH 密钥对**

如果你还没有生成过 SSH 密钥对，可以按以下步骤操作：

1. 打开 Git Bash 或 PowerShell（你在 `E:` 盘的 PowerShell 中）。
    
2. 运行以下命令生成 SSH 密钥对：
    
    `ssh-keygen -t rsa -b 4096 -C "your_email@example.com"`
    
    其中，`-t rsa` 是指定使用 RSA 算法生成密钥对，`-b 4096` 是指定密钥的位数，`-C` 是给密钥加上一个注释（通常使用邮箱）。
    
3. 系统会询问你存储密钥的路径，默认是 `C:\Users\你的用户名\.ssh\id_rsa`，按 `Enter` 键即可使用默认路径。
    
4. 系统会要求你输入一个 **passphrase**，这是一个保护私钥的密码（可以为空）。
    

生成完成后，你会得到两文件：

- **私钥**：`id_rsa`，保存在 `C:\Users\你的用户名\.ssh` 目录下。
    
- **公钥**：`id_rsa.pub`，保存在 `C:\Users\你的用户名\.ssh` 目录下。
    

#### 2. **将公钥添加到 Gitea**

1. 打开公钥文件 `id_rsa.pub`，复制其中的内容。
    
2. 登录到 Gitea，进入 **个人设置** 页面。
    
3. 找到 **SSH 密钥**（SSH Keys）部分。
    
4. 点击 **添加密钥**，将公钥粘贴进去并保存。
    

#### 3. **配置 Git 使用 SSH**

你已经设置了正确的 SSH URL（`git@czu.candysanjo.top:yeh/ds_assignments.git`），所以现在需要确保你的本地 Git 使用的是 SSH 密钥来进行验证。

1. 打开 Git Bash 或 PowerShell，检查一下 SSH 连接是否配置成功：
    
    `ssh -T git@czu.candysanjo.top`
    
    如果成功，你应该看到类似以下的提示：
    
    `Hi your_username! You've successfully authenticated, but Gitea does not provide shell access.`
    
1. 如果测试成功，接下来执行 `git push` 操作时，就不会再要求输入密码了，而是通过 SSH 密钥进行身份验证。
### 三、如果 SSH 不行，使用 Token 进行身份验证

如果你在配置 SSH 密钥时遇到问题，或者不想折腾 SSH，可以选择使用 Gitea 的 **Personal Access Token（个人访问令牌）** 来替代密码。

#### 1. **生成 Token**

1. 登录到 Gitea。
    
2. 进入 **个人设置** 页面，找到 **应用程序**（Applications）选项。
    
3. 点击 **生成新的访问令牌**，并给它一个描述（比如 `Git操作`）。
    
4. 选择合适的权限（通常选择 `repo` 权限）。
    
5. 保存生成的 Token，记下这个值。
    

#### 2. **使用 Token**

当你在 `git push` 时遇到提示输入密码时，直接使用 **Token** 代替密码即可。

例如：

`git push https://your_username@czu.candysanjo.top/yeh/ds_assignments.git`

在输入密码时，直接使用你生成的 Token。

### 四、如果 SSH 和 Token 都无法使用，退回到 HTTP

如果前面的两种方法都不行，你可以退回到原始的 HTTP 方式进行操作。只需要将仓库 URL 改为 HTTPS 形式即可：

`git remote set-url origin https://czu.candysanjo.top/yeh/ds_assignments.git`

然后，执行 `git push` 时就会提示输入账号密码，你可以选择记住密码或者每次输入。