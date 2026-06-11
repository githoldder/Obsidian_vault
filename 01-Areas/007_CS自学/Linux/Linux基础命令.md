
### 1.1 文件和目录操作

```bash
# 目录操作
pwd                                  # 显示当前路径
ls                                   # 列出文件
ls -la                               # 列出所有文件（包括隐藏）+ 详细信息
ls -lh                               # 人类可读的文件大小
ls -ltr                              # 按时间倒序排列

cd /path/to/dir                      # 切换目录
cd ~                                 # 回到家目录
cd -                                 # 回到上一个目录
cd ..                                # 回到上级目录

mkdir mydir                          # 创建目录
mkdir -p a/b/c                       # 递归创建目录

rmdir mydir                          # 删除空目录
rm -rf mydir                         # 强制删除目录及其内容（慎用！）

# 文件操作
touch file.txt                       # 创建空文件
rm file.txt                          # 删除文件
rm -i file.txt                       # 删除前询问
cp file.txt file2.txt                # 复制文件
cp -r dir1 dir2                      # 复制目录
mv file.txt newname.txt              # 重命名
mv file.txt /path/to/dir/            # 移动文件

# 查看文件内容
cat file.txt                         # 显示整个文件
head -20 file.txt                    # 显示前 20 行
tail -20 file.txt                    # 显示后 20 行
tail -f file.txt                     # 实时跟踪文件末尾（查看日志神器）
less file.txt                        # 分页查看（支持搜索）
  # less 中：/pattern 搜索，n 下一个，N 上一个，q 退出
more file.txt                        # 分页查看（简单版）

# 查找文件
find / -name "*.log"                 # 从根目录查找所有 .log 文件
find . -name "*.txt" -type f         # 在当前目录及子目录查找 .txt 文件
find . -size +100M                   # 查找大于 100M 的文件
find . -mtime -7                     # 查找 7 天内修改过的文件

# 快速查找（基于数据库，更快但需要更新）
locate filename                      # 快速定位文件
updatedb                             # 更新 locate 数据库

# 文件权限
ls -l                                # 查看文件权限
chmod 755 file.sh                    # 设置权限：rwxr-xr-x
chmod +x file.sh                     # 添加可执行权限
chmod -R 755 mydir                   # 递归修改目录权限
chown user:group file.txt            # 修改所有者和组

# 权限数字含义：
# 4 = r (读), 2 = w (写), 1 = x (执行)
# 7 = rwx, 6 = rw-, 5 = r-x, 4 = r--
# 755 = rwxr-xr-x（所有者全权限，其他人只读+执行）
```

### 1.2 文本处理

```bash
# grep - 文本搜索
grep "error" log.txt                 # 在文件中搜索 error
grep -i "error" log.txt              # 不区分大小写
grep -r "error" /var/log/            # 递归搜索目录
grep -n "error" log.txt              # 显示行号
grep -v "error" log.txt              # 反向匹配（排除 error）
grep -E "error|warning" log.txt      # 正则表达式匹配
grep "error" log.txt | grep -v "ignore"  # 管道组合

# awk - 文本处理工具
awk '{print $1}' file.txt            # 打印第一列
awk '{print $1, $3}' file.txt        # 打印第1和第3列
awk -F',' '{print $2}' file.csv      # 指定逗号分隔符
awk '{sum+=$1} END {print sum}' file.txt  # 求和
awk '$3 > 100 {print $0}' file.txt   # 条件筛选

# sed - 流编辑器
sed 's/old/new/' file.txt            # 替换每行第一个 old 为 new
sed 's/old/new/g' file.txt           # 替换所有 old 为 new
sed -i 's/old/new/g' file.txt        # 直接修改文件
sed '2d' file.txt                    # 删除第 2 行
sed '/pattern/d' file.txt            # 删除匹配行
sed -n '5,10p' file.txt              # 打印 5-10 行

# sort 和 uniq
sort file.txt                        # 排序
sort -r file.txt                     # 倒序排序
sort -n file.txt                     # 按数字排序
sort -k 2 file.txt                   # 按第 2 列排序
sort file.txt | uniq                 # 去重
sort file.txt | uniq -c              # 统计重复次数

# cut - 剪切列
cut -d',' -f1,3 file.csv             # 按逗号分隔，取第1和3列
cut -c1-10 file.txt                  # 取每行前10个字符

# wc - 统计
wc -l file.txt                       # 统计行数
wc -w file.txt                       # 统计单词数
wc -c file.txt                       # 统计字节数
```

### 1.3 系统管理

```bash
# 进程管理
ps aux                               # 查看所有进程
ps aux | grep nginx                  # 查找特定进程
top                                  # 动态查看进程（按 q 退出）
htop                                 # 增强版 top（需安装）
kill PID                             # 终止进程
kill -9 PID                          # 强制终止进程
killall nginx                        # 终止所有 nginx 进程
pkill nginx                          # 根据名字终止进程

# 后台运行任务
command &                            # 后台运行
nohup command &                      # 后台运行，断开 SSH 也继续
Ctrl + Z                             # 暂停当前任务
bg                                   # 将暂停的任务放到后台
fg                                   # 将后台任务放到前台
jobs                                 # 查看后台任务

# 系统信息
uname -a                             # 系统内核信息
cat /etc/os-release                  # 查看操作系统版本
uptime                               # 系统运行时间
whoami                               # 当前用户
who                                  # 当前登录用户
id                                   # 当前用户 ID 和组

# 磁盘管理
df -h                                # 查看磁盘使用情况（人类可读）
du -sh /var                          # 查看目录总大小
du -h --max-depth=1                  # 查看当前目录下各子目录大小
lsblk                                # 查看块设备
fdisk -l                             # 查看分区表（需 root）

# 内存和 CPU
free -h                              # 查看内存使用情况
cat /proc/meminfo                    # 详细内存信息
cat /proc/cpuinfo                    # CPU 详细信息
nproc                                # 查看 CPU 核心数

# 网络
ifconfig                             # 查看网络接口（旧版）
ip addr                              # 查看 IP 地址（新版推荐）
ip route                             # 查看路由表
ping google.com                      # 测试连通性
netstat -tlnp                        # 查看监听端口
ss -tlnp                             # netstat 替代（更快）
curl -I http://example.com           # 查看 HTTP 头
wget http://example.com/file.zip     # 下载文件

# 查看端口占用
lsof -i :8080                        # 查看占用 8080 端口的进程
netstat -tlnp | grep 8080
```

### 1.4 用户和权限

```bash
# 用户管理
useradd username                     # 创建用户
useradd -m -s /bin/bash username     # 创建用户并指定 shell 和家目录
passwd username                      # 设置密码
usermod -aG docker username          # 将用户加入 docker 组
userdel username                     # 删除用户
userdel -r username                  # 删除用户及其家目录

# 组管理
groupadd groupname                   # 创建组
groupdel groupname                   # 删除组

# 切换用户
su - username                        # 切换到用户（加载环境变量）
su username                          # 切换到用户（不加载环境变量）
sudo command                         # 以 root 权限执行命令
sudo -i                              # 切换到 root
sudo -u username command             # 以指定用户执行命令

# 免密 sudo（编辑 /etc/sudoers）
visudo                               # 安全编辑 sudoers 文件
# 添加行：username ALL=(ALL) NOPASSWD: ALL
```

### 1.5 压缩和解压

```bash
# tar 包
tar -cvf archive.tar files/          # 创建 tar 包
tar -xvf archive.tar                 # 解压 tar 包
tar -czvf archive.tar.gz files/      # 创建 gzip 压缩的 tar 包
tar -xzvf archive.tar.gz             # 解压 gzip 压缩的 tar 包
tar -cjvf archive.tar.bz2 files/     # 创建 bzip2 压缩的 tar 包
tar -xjvf archive.tar.bz2            # 解压 bzip2 压缩的 tar 包
tar -tvf archive.tar                 # 查看 tar 包内容（不解压）

# zip/unzip
zip -r archive.zip files/            # 创建 zip 压缩包
unzip archive.zip                    # 解压 zip
unzip archive.zip -d /path/to/dir    # 解压到指定目录
unzip -l archive.zip                 # 查看 zip 内容

# gzip/gunzip（单个文件）
gzip file.txt                        # 压缩为 file.txt.gz
gunzip file.txt.gz                   # 解压
gzip -c file.txt > file.txt.gz       # 保留原文件压缩
```

### 1.6 常用快捷键

```bash
# Bash 快捷键
Ctrl + A                             # 光标移到行首
Ctrl + E                             # 光标移到行尾
Ctrl + U                             # 删除光标前所有内容
Ctrl + K                             # 删除光标后所有内容
Ctrl + W                             # 删除前一个单词
Ctrl + L                             # 清屏（同 clear 命令）
Ctrl + C                             # 终止当前命令
Ctrl + Z                             # 暂停当前命令
Ctrl + D                             # 退出终端（EOF）
Ctrl + R                             # 搜索历史命令
Ctrl + G                             # 取消搜索
!!                                   # 执行上一条命令
!n                                   # 执行历史第 n 条命令
!$                                   # 上一条命令的最后一个参数

# 历史命令
history                              # 显示命令历史
history | grep docker                # 搜索历史命令
!docker                              # 执行最近的以 docker 开头的命令
```

---