# Docker

Docker 是一个开源的应用容器引擎，基于 Go 语言 并遵从 Apache2.0 协议开源。

## 虚拟机与容器

虚拟机技术是通过在物理服务器上安装虚拟化软件（如VMware、KVM等）来创建和管理虚拟机。每个虚拟机都运行着一个完整的操作系统，它们彼此之间是相互隔离的。虚拟机的创建和启动需要较长的时间，并占用较多的系统资源。

而Docker则采用了一种不同的虚拟化技术，称为容器化。容器是一种轻量级的虚拟化技术，相对于虚拟机来说，容器只包含应用程序运行所需的最低限度的操作系统和库文件。这使得容器具备了更快的启动速度和更高的资源利用率。



## 容器和镜像

* 镜像

是一个只读模板，用于指示创建容器。镜像分层(layers)构建的，而定义这些层次的文件叫Dockerfile。

* 容器

是镜像的可运行的实例。容器可通过API或CLI（命令行）进行操控。

## 仓库镜像

https://github.com/DaoCloud/public-image-mirror

Settings -> Docker Engine 中设置

```json
{
  ...,
  "registry-mirrors": [
	...
  ]
}
```

## 基础

### 查看版本

```
docker --version
```

## 镜像(Image)

Docker Hub 

### 查看本地镜像

```
docker images
```

### 查找镜像

```
docker search ubuntu
```

### 获取镜像

```
docker pull ubuntu
```

### 删除镜像

没有 VPN 加 m.daocloud.io/docker.io/library/ 前缀

```
docker pull m.daocloud.io/docker.io/library/ubuntu
```

## 容器(Container)

### 创建容器

```
docker run -itd --name ubuntu ubuntu bash
docker run -itd --name ubuntu m.daocloud.io/docker.io/library/ubuntu bash


-itd 	表示可以交互
--name 	给容器起的名称
bash 	运行的程序
```

### 创建后台容器

```
docker run -itd --name centos centos bash
docker run -itd --name ubuntu ubuntu bash
```

### 查看当前运行的容器
```
docker ps
```

### 查看所有容器

包括运行中的和停止的容器

```
docker ps -a
```

### 进入容器

```
docker exec -it centos bash
```

### 退出容器
```
exit 或 Ctrl + D
```

### 停止容器
```
docker stop centos
```

### 启动容器
```
docker start centos
```

### 删除容器

只有停止的容器，才可以被删除

只有删除容器，对应的镜像才可以被删除

```
docker rm centos
```

## 与 Host 文件互传

### Host -> 容器

```
docker cp hello.txt centos:/root
```

### 容器 -> Host

```
docker cp centos:/root/hello.txt .
```

## 网络

https://developer.aliyun.com/article/1582137

### 网络类型

* **桥接模式**（Bridge Network）容器使用的默认类型，每个容器连接到一个内部网路的私有网络。
* **主机网络** (Host Network) 容器共享主机的网络命名空间。
* **覆盖网络**（Overlay Network）用户 Docker Swarm 集群中，支持不同 Docker 守护进程上的容器之间的网络连接。
* **Mavvlan网络** 容器可以直接映射到物理网络，拥有独立的 MAC 地址。
* **None网络** 容器有自己的网络命名空间，但不配置任何网络接口，需要手动配置网络。

### 桥接模式

创建桥接网络

```
# 查看所有网络
docker network ls

# 查看网络详情（看容器、IP、网关）
docker network inspect 网络名

# 创建自定义桥接网络
docker network create --driver bridge my-net

# 连接容器到网络
docker network connect my-net 容器名

# 断开容器网络
docker network disconnect my-net 容器名

# 删除无用网络
docker network prune
```

创建容器

```sh
docker run -itd `
  --network=mynet `
  --name=ubuntu1 `
  --hostname=ubuntu1 `
  --add-host=ubuntu2:172.18.11.2 `
  --ip=172.18.11.1 `
  --privileged `
  ubuntu bash
  
docker run -itd `
  --network=mynet `
  --name=ubuntu2 `
  --hostname=ubuntu2 `
  --add-host=ubuntu1:172.18.11.1 `
  --ip=172.18.11.2 `
  --privileged `
  ubuntu bash
```

安装 ping 

```sh
apt update
apt upgrade
apt install iputils-ping
```
