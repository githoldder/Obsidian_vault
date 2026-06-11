# Hadoop Docker 镜像恢复方法

本机大数据/云计算实验镜像已推送到 Docker Hub 后，本地镜像可以删除，需要时再拉取。

## 重要镜像

最近实验状态镜像：

```bash
docker pull <你的DockerHub用户名>/myubuntu:hadoop-mapreduce-lab-topn
docker tag <你的DockerHub用户名>/myubuntu:hadoop-mapreduce-lab-topn myubuntu:hadoop-mapreduce-lab-topn
```

MapReduce lab 基线镜像：

```bash
docker pull <你的DockerHub用户名>/myubuntu:hadoop-mapreduce-lab
docker tag <你的DockerHub用户名>/myubuntu:hadoop-mapreduce-lab myubuntu:hadoop-mapreduce-lab
```

如果 Docker Hub 上使用的仓库名不是 `<你的DockerHub用户名>/myubuntu`，把上面的仓库名前缀替换成实际名称即可。

## 运行最新实验镜像

```bash
docker run -it --name hadoop-mapreduce-lab myubuntu:hadoop-mapreduce-lab-topn bash
```

如果只想临时进入，不保留容器：

```bash
docker run --rm -it myubuntu:hadoop-mapreduce-lab-topn bash
```

## 当前本地清理记录

- 已固化最近容器状态为：`myubuntu:hadoop-mapreduce-lab-topn`
- 旧基线镜像：`myubuntu:hadoop-mapreduce-lab`
- 旧重复快照：`myubuntu:hadoop-mapreduce-lab-snapshot` 已删除
- 本地镜像删除后，可按本文从 Docker Hub 拉回
