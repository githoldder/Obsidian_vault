# 配置免密登录

## 启动 ubuntu 容器 

```sh
docker run -itd --privileged --name ubuntu ubuntu bash

```

## 进入容器

```sh
docker exec -it ubuntu bash
```

## 更新 apt

### 设置阿里镜像
source.list 格式

```sh
cp /etc/apt/sources.list /etc/apt/sources.list.bak
sed -i 's/archive.ubuntu.com/mirrors.aliyun.com/g' /etc/apt/sources.list
sed -i 's/security.ubuntu.com/mirrors.aliyun.com/g' /etc/apt/sources.list
```

DEB822 格式

```sh
cp /etc/apt/sources.list.d/ubuntu.sources /etc/apt/sources.list.d/ubuntu.sources.bak
sed -i 's/archive.ubuntu.com/mirrors.aliyun.com/g' /etc/apt/sources.list.d/ubuntu.sources
sed -i 's/security.ubuntu.com/mirrors.aliyun.com/g' /etc/apt/sources.list.d/ubuntu.sources
```

### 更新升级

```sh
apt update
apt upgrade
```

## 安装 ssh server 

### 安装服务

```sh
apt install -y openssl openssh-server iputils-ping vim
```

### 启动 ssh 服务

```sh
service ssh start
```

### 查看状态

```sh
service ssh status
```

## 免密登录

### 生成私钥、公钥

```sh
cd
ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
```

### 配置 config
 
在目录 ~/.ssh/config 创建设置 config 文件

```shell
vim ~/.ssh/config
```

添加

```
Host localhost
  StrictHostKeyChecking no

Host 0.0.0.0
  StrictHostKeyChecking no

Host my*
   StrictHostKeyChecking no
   UserKnownHostsFile=/dev/null
```

### 确认免密登录

```
ssh localhost
```

## 创建镜像

### 配置为开机启动

创建 run.sh 文件

```sh
vim ~/run.sh
```

run.sh

```sh
#!/bin/bash
/usr/sbin/sshd -D
```

添加执行权限

```
chmod +x ~/run.sh
```

创建软连接

```sh
ln -s ~/run.sh /usr/bin/run.sh
```

### 停止容器

```sh
docker stop ubuntu
```

### 创建镜像

```sh
docker commit ubuntu myubuntu
```

创建容器

```sh
docker run -itd --name ubuntu myubuntu run.sh
```

进入容器

```sh
docker exec -it ubuntu bash
```

或者

```
docker run -itd --name ubuntu myubuntu /usr/sbin/sshd -D
```
OK，但是仅这些是不够的，在‘/Users/caolei/Desktop/Obsidian_root/007_CS自学’里有我的很多课程、自学笔记，都是专业相关的；‘/Users/caolei/Desktop/Obsidian_root/010_时间管理’里面是我的个人时间事务管理系统；‘/Users/caolei/Desktop/Obsidian_root/011_项目经验’里面有我所有的项目经验、包括一些关键的信息和取得的成就；‘/Users/caolei/Desktop/Obsidian_root/000_个人看板/脚手架&通用模版&工具’这里面是我的一些效率提升工具、工作流；这些信息希望你能一一对齐，填充进我的个人网站。