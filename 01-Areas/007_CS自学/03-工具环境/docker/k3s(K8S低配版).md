# Docker、Linux、K8s/K3s 全能 Cheatsheet

> 从新手到生产环境的实用命令指南
> 适用于：大数据、分布式系统开发，个人多项目管理

---

## 一、Docker 快速上手

### 1.1 基础概念（新手必看）

| 概念                | 解释                | 类比                 |
| ----------------- | ----------------- | ------------------ |
| **Image（镜像）**     | 只读的模板，包含运行应用所需的一切 | 类固醇强化版的安装光盘        |
| **Container（容器）** | 镜像的运行实例           | 从光盘安装好的运行中的软件      |
| **Dockerfile**    | 构建镜像的"食谱"         | 做菜时的菜谱             |
| **Volume（卷）**     | 持久化数据存储           | 容器外的硬盘，容器删了数据还在    |
| **Network（网络）**   | 容器间通信方式           | 容器之间的局域网           |
| **Registry（仓库）**  | 存放镜像的地方           | Docker Hub 是"应用商店" |

### 1.2 核心命令速查

#### 镜像管理

```bash
# 搜索镜像（从 Docker Hub）
docker search nginx

# 拉取镜像
docker pull nginx                    # 拉取最新版
docker pull nginx:1.21               # 拉取指定版本
docker pull registry.cn-hangzhou.aliyuncs.com/acs/nginx  # 从阿里云拉取

# 查看本地镜像
docker images
docker image ls

# 删除镜像
docker rmi nginx                     # 删除单个镜像
docker rmi nginx redis mysql         # 删除多个镜像
docker image prune                   # 删除所有悬空镜像（<none>）
docker image prune -a                # 删除所有未使用的镜像

# 构建镜像
docker build -t myapp:1.0 .          # 在当前目录用 Dockerfile 构建
docker build -t myapp:1.0 -f Dockerfile.prod .  # 指定 Dockerfile 文件

# 保存和加载镜像（离线传输）
docker save -o myapp.tar myapp:1.0   # 导出为 tar 包
docker load -i myapp.tar             # 从 tar 包导入

# 给镜像打标签
docker tag myapp:1.0 registry.example.com/myapp:1.0
```

#### 容器生命周期

```bash
# 运行容器（最常用！）
docker run -d --name mynginx -p 80:80 nginx
docker run -it --name myubuntu ubuntu bash

# run 参数详解：
# -d, --detach          后台运行（守护进程模式）
# -it                   交互模式 + 伪终端（用于进入容器操作）
# --name                给容器起名字（方便管理）
# -p, --publish         端口映射 主机端口:容器端口
# -v, --volume          挂载卷 主机路径:容器路径
# -e, --env             设置环境变量
# --network             指定网络
# --restart=always      自动重启策略
# --rm                  停止后自动删除容器（一次性任务）

# 实际生产例子
# 1. 运行 MySQL（数据持久化）
docker run -d \
  --name mysql \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=mysecret \
  -v /mydata/mysql:/var/lib/mysql \
  --restart=always \
  mysql:8.0

# 2. 运行 Redis（带密码）
docker run -d \
  --name redis \
  -p 6379:6379 \
  -v /mydata/redis:/data \
  --restart=always \
  redis:7 redis-server --requirepass mypassword

# 3. 运行 Nginx（挂载配置和网站文件）
docker run -d \
  --name nginx \
  -p 80:80 -p 443:443 \
  -v /mydata/nginx/html:/usr/share/nginx/html \
  -v /mydata/nginx/conf/nginx.conf:/etc/nginx/nginx.conf \
  -v /mydata/nginx/logs:/var/log/nginx \
  --restart=always \
  nginx:latest

# 启动/停止/重启容器
docker start mynginx
docker stop mynginx                  # 优雅停止（发送 SIGTERM）
docker stop -t 30 mynginx            # 30秒后强制停止
docker kill mynginx                  # 强制停止（SIGKILL）
docker restart mynginx

# 查看容器
docker ps                            # 查看运行中的容器
docker ps -a                         # 查看所有容器（包括停止的）
docker ps -q                         # 只显示容器 ID（用于批量操作）
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

# 进入容器
docker exec -it mynginx bash         # 进入运行中的容器（推荐）
docker exec -it mynginx sh           # 如果容器没有 bash

# 退出容器（在容器内执行）
exit                                 # 退出并关闭交互
Ctrl + P + Q                         # 退出但不停止容器（仅 -it 模式）

# 删除容器
docker rm mynginx                    # 删除已停止的容器
docker rm -f mynginx                 # 强制删除运行中的容器
docker rm $(docker ps -aq)           # 删除所有容器（慎用！）
docker container prune               # 删除所有停止的容器

# 复制文件
docker cp mynginx:/etc/nginx/nginx.conf ./nginx.conf    # 容器 → 主机
docker cp ./myfile.txt mynginx:/tmp/myfile.txt          # 主机 → 容器

# 查看容器信息
docker logs mynginx                  # 查看日志
docker logs -f mynginx               # 实时跟踪日志（类似 tail -f）
docker logs --tail 100 mynginx       # 查看最后 100 行
docker inspect mynginx               # 查看容器详细信息（JSON）
docker stats mynginx                 # 查看实时资源使用
docker top mynginx                   # 查看容器内进程
```

#### 数据卷管理

```bash
# 创建卷（Docker 管理，推荐）
docker volume create mydata
docker volume ls
docker volume inspect mydata

# 使用卷
docker run -d -v mydata:/data nginx

# 删除卷
docker volume rm mydata
docker volume prune                  # 删除未使用的卷（慎用！）

# 绑定挂载（主机路径直接挂载）
docker run -d -v /host/path:/container/path nginx
docker run -d -v $(pwd)/data:/data nginx  # 挂载当前目录下的 data
```

#### 网络管理

```bash
# 查看网络
docker network ls

# 创建网络
docker network create mynet
docker network create --driver bridge mynet
docker network create --subnet=172.18.0.0/16 mynet

# 容器使用自定义网络（容器间可以通过名字互访！）
docker run -d --name web --network mynet nginx
docker run -d --name api --network mynet myapi
# 在 web 容器内可以 ping api，反之亦然

# 将容器连接到网络
docker network connect mynet mycontainer
docker network disconnect mynet mycontainer

# 查看网络详情
docker network inspect mynet

# 删除网络
docker network rm mynet
docker network prune
```

### 1.3 Docker Compose（多容器编排）

#### docker-compose.yml 示例

```yaml
version: '3.8'

services:
  # Web 服务
  web:
    image: nginx:latest
    container_name: my-web
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./html:/usr/share/nginx/html
      - ./nginx.conf:/etc/nginx/nginx.conf
      - ./logs:/var/log/nginx
    depends_on:
      - api
    networks:
      - mynet
    restart: always

  # API 服务
  api:
    build:
      context: ./api
      dockerfile: Dockerfile
    container_name: my-api
    ports:
      - "8080:8080"
    environment:
      - NODE_ENV=production
      - DB_HOST=mysql
      - DB_PASSWORD=secret
    volumes:
      - ./api:/app
      - /app/node_modules  # 匿名卷，不覆盖容器内已安装的依赖
    depends_on:
      - mysql
      - redis
    networks:
      - mynet
    restart: always

  # 数据库
  mysql:
    image: mysql:8.0
    container_name: my-mysql
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: secret
      MYSQL_DATABASE: myapp
    volumes:
      - mysql_data:/var/lib/mysql
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql
    networks:
      - mynet
    restart: always

  # 缓存
  redis:
    image: redis:7-alpine
    container_name: my-redis
    ports:
      - "6379:6379"
    command: redis-server --requirepass secret
    volumes:
      - redis_data:/data
    networks:
      - mynet
    restart: always

  # 大数据：Kafka
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181

  kafka:
    image: confluentinc/cp-kafka:latest
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
    depends_on:
      - zookeeper

volumes:
  mysql_data:
  redis_data:

networks:
  mynet:
    driver: bridge
```

#### Compose 常用命令

```bash
# 启动所有服务（后台运行）
docker-compose up -d

# 启动特定服务
docker-compose up -d mysql redis

# 构建并启动（Dockerfile 有变更时）
docker-compose up -d --build

# 停止服务
docker-compose stop

# 停止并删除容器
docker-compose down

# 停止并删除容器 + 卷（彻底清理）
docker-compose down -v

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs
docker-compose logs -f api           # 实时跟踪 api 服务日志
docker-compose logs --tail 100 mysql

# 重启服务
docker-compose restart api

# 进入容器
docker-compose exec api bash

# 扩容服务（需要配合负载均衡）
docker-compose up -d --scale api=3

# 拉取最新镜像
docker-compose pull

# 验证配置文件
docker-compose config
```

### 1.4 生产环境技巧

```bash
# 1. 限制容器资源
docker run -d \
  --memory=512m \
  --memory-swap=512m \
  --cpus=1.0 \
  --name limited-app \
  myapp

# 2. 健康检查
docker run -d \
  --health-cmd="curl -f http://localhost/health || exit 1" \
  --health-interval=30s \
  --health-timeout=10s \
  --health-retries=3 \
  --name healthy-app \
  myapp

# 3. 查看健康状态
docker inspect --format='{{.State.Health.Status}}' healthy-app

# 4. 容器自动清理（一次性任务）
docker run --rm alpine echo "Hello World"

# 5. 批量操作
# 停止所有容器
docker stop $(docker ps -q)

# 删除所有停止的容器
docker container prune -f

# 删除所有未使用的镜像、卷、网络
docker system prune -a --volumes

# 6. 导出/导入容器（不是镜像！）
docker export mycontainer -o mycontainer.tar
docker import mycontainer.tar myimage:tag

# 7. 提交容器为新镜像
docker commit mycontainer mynewimage:tag

# 8. 查看容器 IP
docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mycontainer
```

---


## 二、K3s - 轻量级 Kubernetes

### 2.1 什么是 K3s？

**K3s 是 Rancher 开发的轻量级 Kubernetes 发行版**，专为以下场景设计：

- **边缘计算**：IoT 设备、边缘节点
- **开发测试**：个人开发者、CI/CD 流水线
- **资源受限环境**：小于 1GB 内存的机器
- **ARM 设备**：树莓派等嵌入式设备

**为什么个人开发者需要 K3s？**

| 痛点 | 传统方案 | K3s 方案 |
|------|---------|---------|
| 端口冲突 | 手动管理端口 | 每个项目独立命名空间，自动分配 |
| 环境隔离 | Docker Compose | 完整的容器编排 + 服务发现 |
| 资源管理 | 所有项目争抢资源 | 可设置资源限制（CPU/内存） |
| 项目切换 | 启停不同容器组 | 一键切换 namespace 或上下文 |
| 服务发现 | 硬编码 IP 和端口 | 内置 DNS 服务发现 |
| 自动恢复 | 容器挂了需要手动重启 | 自动重启失败容器 |
| 负载均衡 | Nginx 手动配置 | 内置 Ingress Controller |

**K3s vs K8s 对比：**

| 特性 | Kubernetes | K3s |
|------|-----------|-----|
| 二进制大小 | ~100MB+ | ~60MB |
| 内存需求 | 2GB+ | 512MB |
| 启动时间 | 分钟级 | 秒级 |
| 安装复杂度 | 高 | 一条命令 |
| 内置组件 | 需单独安装 | 内置 Traefik、CoreDNS、SQLite |
| 生产就绪 | 是 | 是（经过 CNCF 认证） |

### 2.2 安装 K3s

#### 快速安装（单节点）

```bash
# 1. 一键安装（需要 root 或 sudo）
curl -sfL https://get.k3s.io | sh -

# 2. 验证安装
sudo k3s kubectl get nodes

# 3. 查看版本
sudo k3s --version

# 4. 查看服务状态
sudo systemctl status k3s
```

#### 非 root 用户使用 kubectl

```bash
# 方法1：使用 k3s 的 kubeconfig
sudo chmod 644 /etc/rancher/k3s/k3s.yaml
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml

# 方法2：复制到用户目录（推荐）
mkdir -p ~/.kube
sudo cp /etc/rancher/k3s/k3s.yaml ~/.kube/config
sudo chown $(id -u):$(id -g) ~/.kube/config
chmod 600 ~/.kube/config

# 添加到 ~/.bashrc 或 ~/.zshrc
export KUBECONFIG=~/.kube/config

# 测试
kubectl get nodes
kubectl get pods -A
```

#### 自定义安装选项

```bash
# 指定数据目录（避免占满根分区）
curl -sfL https://get.k3s.io | sh -s - --data-dir /data/k3s

# 禁用某些组件（节省资源）
curl -sfL https://get.k3s.io | sh -s - \
  --disable traefik \
  --disable servicelb \
  --disable metrics-server

# 使用 Docker 作为容器运行时（而不是 containerd）
curl -sfL https://get.k3s.io | sh -s - --docker

# 指定节点名称和标签
curl -sfL https://get.k3s.io | sh -s - \
  --node-name mynode \
  --node-label environment=dev \
  --node-taint dedicated=dev:NoSchedule
```

#### 卸载 K3s

```bash
# 卸载 K3s
/usr/local/bin/k3s-uninstall.sh

# 如果数据目录被修改过，需要手动删除
sudo rm -rf /data/k3s
```

### 2.3 K3s 核心命令

#### 服务管理

```bash
# 启动/停止/重启 K3s 服务
sudo systemctl start k3s
sudo systemctl stop k3s
sudo systemctl restart k3s

# 启用/禁用开机启动
sudo systemctl enable k3s
sudo systemctl disable k3s

# 查看日志
sudo journalctl -u k3s -f
sudo journalctl -u k3s --since "1 hour ago"
```

#### kubectl 基础命令

```bash
# ========== 信息查看 ==========
kubectl version                      # 查看版本信息
kubectl cluster-info                 # 查看集群信息
kubectl get nodes                    # 查看所有节点
kubectl get nodes -o wide            # 查看详细信息

# ========== Namespace 管理 ==========
kubectl get namespaces               # 列出所有命名空间
kubectl create ns project-a          # 创建命名空间
kubectl delete ns project-a          # 删除命名空间（及其所有资源）
kubectl config set-context --current --namespace=project-a  # 切换默认命名空间

# ========== Pod 管理 ==========
kubectl get pods                     # 查看当前命名空间的 Pod
kubectl get pods -A                  # 查看所有命名空间的 Pod
kubectl get pods -n project-a        # 查看指定命名空间
kubectl get pods -o wide             # 显示更多信息（IP、节点等）
kubectl get pods --show-labels       # 显示标签
kubectl describe pod pod-name        # 查看 Pod 详细信息（排错必备）
kubectl logs pod-name                # 查看 Pod 日志
kubectl logs pod-name -f             # 实时跟踪日志
kubectl logs pod-name --previous     # 查看上一次运行的日志（重启后）
kubectl exec -it pod-name -- bash    # 进入 Pod 容器
kubectl exec -it pod-name -c container-name -- bash  # 多容器 Pod 指定容器
kubectl delete pod pod-name          # 删除 Pod
kubectl top pod                      # 查看 Pod 资源使用（需要 metrics-server）

# ========== Deployment 管理 ==========
kubectl get deployments              # 查看部署
kubectl get deploy -o wide
kubectl describe deploy deploy-name
kubectl scale deploy deploy-name --replicas=3    # 扩容到 3 个副本
kubectl rollout status deploy/deploy-name        # 查看滚动更新状态
kubectl rollout history deploy/deploy-name       # 查看更新历史
kubectl rollout undo deploy/deploy-name          # 回滚到上一版本
kubectl rollout undo deploy/deploy-name --to-revision=2  # 回滚到指定版本
kubectl set image deploy/deploy-name container=new-image:tag  # 更新镜像
kubectl delete deploy deploy-name    # 删除部署

# ========== Service 管理 ==========
kubectl get services                 # 查看服务
kubectl get svc                      # 简写形式
kubectl describe svc svc-name
kubectl delete svc svc-name

# ========== ConfigMap 和 Secret ==========
kubectl get configmap
kubectl get cm
kubectl describe cm cm-name
kubectl create cm myconfig --from-file=config.json
kubectl create cm myconfig --from-literal=key1=value1 --from-literal=key2=value2

kubectl get secrets
kubectl describe secret secret-name
kubectl create secret generic mysecret --from-literal=password=mypass
kubectl create secret tls mytls --cert=cert.pem --key=key.pem
kubectl create secret docker-registry regcred \
  --docker-server=registry.example.com \
  --docker-username=user \
  --docker-password=pass

# ========== 应用部署 ==========
# 从 YAML 文件部署
kubectl apply -f deployment.yaml
kubectl apply -f ./manifests/        # 部署整个目录
kubectl apply -f https://example.com/manifest.yaml  # 从 URL 部署

# 删除资源
kubectl delete -f deployment.yaml
kubectl delete -f ./manifests/

# 导出 YAML（用于备份或修改）
kubectl get deploy myapp -o yaml > myapp-backup.yaml

# ========== 资源管理 ==========
kubectl get all                      # 查看所有资源
kubectl get all -n project-a
kubectl get ingress                  # 查看 Ingress
kubectl get pv                       # 查看持久卷
kubectl get pvc                      # 查看持久卷声明
kubectl get events                   # 查看事件（排错）
kubectl get events --sort-by=.metadata.creationTimestamp
kubectl get events --field-selector type=Warning  # 只看警告

# ========== 标签和选择器 ==========
kubectl label pod mypod env=prod     # 添加标签
kubectl label pod mypod env-         # 删除标签
kubectl get pods -l env=prod         # 按标签筛选
kubectl get pods -l 'env in (dev, test)'

# ========== 端口转发（本地调试）==========
kubectl port-forward pod/myapp 8080:80        # 转发 Pod 端口
kubectl port-forward svc/myapp 8080:80        # 转发 Service 端口
kubectl port-forward deploy/myapp 8080:80     # 转发 Deployment

# 现在本地访问 localhost:8080 就能访问到 K8s 中的服务
```

### 2.4 个人开发者实战：多项目管理

#### 场景设置

假设你同时开发以下项目：
- **项目 A**：Web 应用（前端 + API + MySQL）
- **项目 B**：数据处理服务（Python + Redis + Kafka）
- **项目 C**：个人博客（Ghost + SQLite）

每个项目使用独立的 Namespace，完全隔离。

#### 项目 A：Web 应用示例

```yaml
# project-a/namespace.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: project-a
  labels:
    environment: development
    owner: myname

---
# project-a/mysql.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
  namespace: project-a
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - name: mysql
        image: mysql:8.0
        env:
        - name: MYSQL_ROOT_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysql-secret
              key: root-password
        - name: MYSQL_DATABASE
          value: myapp
        ports:
        - containerPort: 3306
        volumeMounts:
        - name: mysql-storage
          mountPath: /var/lib/mysql
      volumes:
      - name: mysql-storage
        persistentVolumeClaim:
          claimName: mysql-pvc

---
apiVersion: v1
kind: Service
metadata:
  name: mysql
  namespace: project-a
spec:
  selector:
    app: mysql
  ports:
  - port: 3306
    targetPort: 3306
  type: ClusterIP

---
# project-a/api.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: api
  namespace: project-a
spec:
  replicas: 2
  selector:
    matchLabels:
      app: api
  template:
    metadata:
      labels:
        app: api
    spec:
      containers:
      - name: api
        image: myapi:latest
        imagePullPolicy: IfNotPresent
        ports:
        - containerPort: 8080
        env:
        - name: DB_HOST
          value: mysql  # 直接使用 Service 名字，K3s 自动解析
        - name: DB_PORT
          value: "3306"
        resources:
          requests:
            memory: "128Mi"
            cpu: "100m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /ready
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5

---
apiVersion: v1
kind: Service
metadata:
  name: api
  namespace: project-a
spec:
  selector:
    app: api
  ports:
  - port: 80
    targetPort: 8080
  type: ClusterIP

---
# project-a/ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: project-a-ingress
  namespace: project-a
  annotations:
    traefik.ingress.kubernetes.io/router.entrypoints: web
spec:
  rules:
  - host: project-a.local
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: api
            port:
              number: 80

---
# project-a/storage.yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mysql-pvc
  namespace: project-a
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 10Gi
```

#### 部署脚本

```bash
#!/bin/bash
# deploy-project-a.sh

set -e

echo "=== 部署项目 A ==="

# 1. 创建命名空间
kubectl apply -f project-a/namespace.yaml

# 2. 创建 Secret（如果不存在）
kubectl create secret generic mysql-secret \
  --from-literal=root-password=mysecret123 \
  -n project-a \
  --dry-run=client -o yaml | kubectl apply -f -

# 3. 部署存储
kubectl apply -f project-a/storage.yaml

# 4. 部署 MySQL
kubectl apply -f project-a/mysql.yaml

# 5. 部署 API
kubectl apply -f project-a/api.yaml

# 6. 部署 Ingress
kubectl apply -f project-a/ingress.yaml

# 7. 等待部署完成
echo "等待 MySQL 启动..."
kubectl wait --for=condition=ready pod -l app=mysql -n project-a --timeout=120s

echo "等待 API 启动..."
kubectl wait --for=condition=ready pod -l app=api -n project-a --timeout=120s

echo "=== 部署完成 ==="
echo "访问地址: http://project-a.local"
echo "查看状态: kubectl get all -n project-a"
```

#### 快速切换项目

```bash
# 创建快捷命令别名
alias k='kubectl'
alias ka='kubectl apply -f'
alias kd='kubectl delete -f'
alias kg='kubectl get'
alias kgp='kubectl get pods'
alias kgs='kubectl get svc'
alias kgn='kubectl get nodes'
alias kdesc='kubectl describe'
alias klogs='kubectl logs'
alias kexec='kubectl exec -it'
alias kctx='kubectl config current-context'
alias kns='kubectl config set-context --current --namespace'

# 项目切换函数
project-a() {
    kubectl config set-context --current --namespace=project-a
    echo "已切换到项目 A"
    kubectl get pods
}

project-b() {
    kubectl config set-context --current --namespace=project-b
    echo "已切换到项目 B"
    kubectl get pods
}

project-c() {
    kubectl config set-context --current --namespace=project-c
    echo "已切换到项目 C"
    kubectl get pods
}

# 添加到 ~/.bashrc 或 ~/.zshrc
```

### 2.5 常用 K3s 配置

#### 修改 Traefik 配置（默认 Ingress Controller）

```bash
# K3s 使用 Traefik 作为默认 Ingress Controller
# 编辑 ConfigMap 来修改配置

kubectl edit cm traefik -n kube-system

# 或者创建自定义配置
kubectl apply -f - <<EOF
apiVersion: helm.cattle.io/v1
kind: HelmChartConfig
metadata:
  name: traefik
  namespace: kube-system
spec:
  valuesContent: |-
    dashboard:
      enabled: true
    logs:
      general:
        level: INFO
      access:
        enabled: true
    additionalArguments:
      - --serversTransport.insecureSkipVerify=true
EOF

# 重启 K3s 使配置生效
sudo systemctl restart k3s
```

#### 使用本地 Docker 镜像

```bash
# 1. 在 K3s 节点上导入本地镜像
sudo k3s ctr images import myimage.tar

# 2. 或者使用 containerd 命名空间
sudo k3s ctr -n k8s.io images import myimage.tar

# 3. 部署时指定 imagePullPolicy: Never 或 IfNotPresent
```

#### 使用私有镜像仓库

```bash
# 1. 创建 Secret
kubectl create secret docker-registry regcred \
  --docker-server=registry.example.com \
  --docker-username=username \
  --docker-password=password \
  --docker-email=email@example.com

# 2. 在 Deployment 中引用
```

```yaml
apiVersion: apps/v1
kind: Deployment
spec:
  template:
    spec:
      imagePullSecrets:
      - name: regcred
      containers:
      - name: app
        image: registry.example.com/myapp:latest
```

### 2.6 监控和日志

```bash
# 安装 Kubernetes Dashboard（可选）
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml

# 创建管理员用户
kubectl create serviceaccount dashboard-admin -n kubernetes-dashboard
kubectl create clusterrolebinding dashboard-admin \
  --clusterrole=cluster-admin \
  --serviceaccount=kubernetes-dashboard:dashboard-admin

# 获取登录 Token
kubectl -n kubernetes-dashboard create token dashboard-admin

# 启动代理访问 Dashboard
kubectl proxy
# 访问：http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/
```

```bash
# 安装 Lens 或 k9s（更友好的命令行界面）
# k9s - 终端 UI
wget https://github.com/derailed/k9s/releases/download/v0.27.4/k9s_Linux_amd64.tar.gz
tar -xzf k9s_Linux_amd64.tar.gz
sudo mv k9s /usr/local/bin/
k9s
```

### 2.7 故障排查

```bash
# Pod 起不来
kubectl describe pod pod-name        # 看 Events 部分
kubectl logs pod-name                # 看日志
kubectl logs pod-name --previous     # 看上一次运行的日志

# 镜像拉取失败
kubectl describe pod pod-name | grep -A 5 Events
# 检查镜像名、镜像仓库认证、网络

# 服务无法访问
kubectl get endpoints svc-name       # 检查 Endpoint 是否存在
kubectl port-forward svc/svc-name 8080:80  # 本地测试
kubectl exec -it pod-name -- nslookup svc-name  # 测试 DNS

# 资源不足
kubectl describe node                # 看 Allocatable 和 Allocated
kubectl top nodes
kubectl top pods

# 网络问题
kubectl get pods -n kube-system      # 检查系统 Pod
sudo systemctl status k3s
sudo journalctl -u k3s -f            # 查看 K3s 日志

# 重置 K3s（全部清除，谨慎使用！）
/usr/local/bin/k3s-uninstall.sh
curl -sfL https://get.k3s.io | sh -
```

---

## 五、速查表汇总

### 5.1 Docker 速查

| 操作 | 命令 |
|------|------|
| 运行容器 | `docker run -d -p 80:80 --name web nginx` |
| 进入容器 | `docker exec -it web bash` |
| 查看日志 | `docker logs -f web` |
| 复制文件 | `docker cp web:/etc/nginx/nginx.conf ./` |
| 停止/删除 | `docker stop web && docker rm web` |
| 清理 | `docker system prune -a` |
| 构建镜像 | `docker build -t myapp:1.0 .` |
| Compose 启动 | `docker-compose up -d` |
| Compose 停止 | `docker-compose down` |

### 5.2 Linux 速查

| 操作 | 命令 |
|------|------|
| 查找文件 | `find . -name "*.log"` |
| 搜索文本 | `grep -r "error" /var/log` |
| 查看端口 | `lsof -i :8080` |
| 压缩目录 | `tar -czvf backup.tar.gz dir/` |
| 查看磁盘 | `df -h && du -sh *` |
| 查看进程 | `ps aux \| grep nginx` |
| 权限设置 | `chmod 755 script.sh` |

### 5.3 Vim 速查

| 操作 | 命令 |
|------|------|
| 编辑 | `i` 进入插入，`Esc` 退出 |
| 保存 | `:w` 保存，`:wq` 保存退出 |
| 退出 | `:q` 退出，`:q!` 强制退出 |
| 删除行 | `dd` |
| 复制行 | `yy`，`p` 粘贴 |
| 撤销 | `u`，重做 `Ctrl+r` |
| 搜索 | `/pattern`，`n` 下一个 |

### 5.4 K3s/kubectl 速查

| 操作 | 命令 |
|------|------|
| 查看 Pod | `kubectl get pods -A` |
| 查看日志 | `kubectl logs -f pod-name` |
| 进入容器 | `kubectl exec -it pod-name -- bash` |
| 应用配置 | `kubectl apply -f manifest.yaml` |
| 删除资源 | `kubectl delete -f manifest.yaml` |
| 端口转发 | `kubectl port-forward svc/web 8080:80` |
| 切换命名空间 | `kubectl config set-context --current --namespace=xxx` |

---

## 六、学习资源

### 官方文档
- Docker: https://docs.docker.com/
- Kubernetes: https://kubernetes.io/docs/
- K3s: https://docs.k3s.io/

### 推荐工具
- **Lens**: https://k8slens.dev/ - 最好的 K8s GUI
- **k9s**: https://k9scli.io/ - 终端 K8s UI
- **Portainer**: https://www.portainer.io/ - Docker/K8s 管理面板

### 实践建议
1. **从 Docker 开始**：先掌握容器基础
2. **用 Docker Compose 练习**：理解多容器编排
3. **过渡到 K3s**：体验完整的容器编排能力
4. **尝试 Helm**：K8s 包管理，简化复杂部署

---

> 最后更新：2026-03-09
> 建议收藏并定期复习，熟能生巧！
