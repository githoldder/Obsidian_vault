## 创建 docker 网络
```
docker network create --subnet=172.18.0.0/16 mynet
```
## 创建主机集群

host|ip|hosts|net
----|----|----|----
myubuntu1|172.18.11.1|myubuntu2, myubuntu3|mynet
myubuntu2|172.18.11.2|myubuntu1, myubuntu3|mynet
myubuntu3|172.18.11.3|myubuntu1, myubuntu2|mynet

因为 docker 容器会将自己加入 hosts 列表，所以只需加入其他容器加入到 hosts 列表

shell 

```shell
IP_PREFIX="172.18.11"
HOST_COUNT=3
for i in `seq $NODE_COUNT`
do
    # 准备参数
	add_host=""
	# 1->2,3 2->1,3 3->1,2
	for j in `seq $[HOST_COUNT - 1]`
	do
		k=$[(i + j - 1) % HOST_COUNT + 1]
		add_host="${add_host} --add-host=myubuntu${k}:${IP_PREFIX}.${k}"
	done

	# 运行容器
	docker run -itd \
	  --network=mynet \
	  --name=myubuntu${i} \
      --hostname=myubuntu${i} \
	  --ip=${IP_PREFIX}.${i} \
	  ${add_host} \
	  --privileged \
      myubuntu run.sh
done
```

PowerShell

```batch
@echo off
setlocal enabledelayedexpansion
set IP_PREFIX=172.18.11
set /a HOST_COUNT=4
set /a SUB_HOST_COUNT=%HOST_COUNT% - 1
for /l %%i in (1,1, %HOST_COUNT%) do (
	set add_host=
	for /l %%j in (1,1, %SUB_HOST_COUNT%) do (
		set /a K=%%i+%%j-1
		set /a K=!k!%%%HOST_COUNT%+1
		set add_host=!add_host! --add-host=myubuntu!k!:%IP_PREFIX%.!k!
	)
	docker run -itd ^
      --network=mynet ^
      --name=myubuntu%%i ^
      --hostname=myubuntu%%i ^
      --ip=%IP_PREFIX%.%%i ^
      !add_host! ^
      --privileged ^
      myubuntu run.sh
)
@echo on
```

setlocal enabledelayedexpansion 
开启变量延迟，在循环中可以修改变量，通过 !变量名! 替换 %变量名%
## 测试相互间免密登录
```
ssh myubuntu1
ssh myubuntu2
ssh myubuntu3
```
## 停止所有容器

停止所有容器名 myubuntu 开头的容器

```
docker stop $(docker ps -aq --filter="name=myubuntu*")
```

## 删除所有容器

 删除所有容器名 myubuntu 开头的容器
```
docker rm $(docker ps -aq --filter="name=myubuntu*")
```

## 启动所有容器

启动所有容器名 myubuntu 开头的容器
```
docker start $(docker ps -aq --filter="name=myubuntu*")
```
