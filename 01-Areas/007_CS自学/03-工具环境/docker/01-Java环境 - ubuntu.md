# 在 Docker Ubuntu 容器中搭建 Java 开发环境
## 1. 准备并运行容器
```sh
docker run -itd --name ubuntu ubuntu bash
```

## 2. 安装 Java 环境
### 2.1 进入容器并准备系统
```sh
docker exec -it ubuntu bash
```

### 2.2 设置阿里云镜像源
```sh

cp /etc/apt/sources.list /etc/apt/sources.list.bak
sed -i 's/archive.ubuntu.com/mirrors.aliyun.com/g' /etc/apt/sources.list
sed -i 's/security.ubuntu.com/mirrors.aliyun.com/g' /etc/apt/sources.list
```

### 2.3 安装必要工具
```sh
apt update
apt upgrade
apt install vim
```

### 2.4 安装 JDK 8
1. 将 JDK 压缩包拷贝到容器：
```sh
docker cp jdk_aarch64_linux_hotspot_8u482b08.tar.gz ubuntu:/root/
```
2. 在容器内解压并修改文件夹名，删除压缩文件：
```sh
cd
tar -zxvf jdk_aarch64_linux_hotspot_8u482b08.tar.gz
mv jdk8u482-b08 java
rm -f jdk_aarch64_linux_hotspot_8u482b08.tar.gz
```

### 2.5 设置 Java 环境变量
编辑 `.bash_aliases`：
```sh
vi .bash_aliases
```
添加配置：
```sh
# JAVA
export JAVA_HOME=/root/java
export CLASSPATH=.:$JAVA_HOME/jre/lib/rt.jar:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export PATH=$JAVA_HOME/bin:${PATH}
```
生效配置：
```sh
source .bash_aliases
```
确认 `JAVA_HOME`：
```sh
echo $JAVA_HOME
```

### 2.6 测试 Java 环境
```sh
java -version
mkdir test
cd test
vi HelloWorld.java
```
`HelloWorld.java` 内容：
```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello World !!!");
    }
}
```
编译运行：
```sh
javac HelloWorld.java
java HelloWorld
```
删除测试文件夹：
```sh
cd ..
rm -rf test
```

## 3. 安装 Maven
### 3.1 下载并安装 Maven
将 Maven 压缩包拷贝到容器：
```sh
docker cp apache-maven-3.8.6-bin.tar.gz ubuntu:/root/
```
在容器内解压并修改文件夹名，删除压缩文件：
```sh
cd
tar -zxvf apache-maven-3.8.6-bin.tar.gz
mv apache-maven-3.8.6 maven
rm -f apache-maven-3.8.6-bin.tar.gz
```

### 3.2 设置 Maven 环境变量
编辑 `.bash_aliases`：
```sh
vi .bash_aliases
```
添加配置：
```sh
# MAVEN
export MAVEN_HOME=/root/maven
export PATH=$MAVEN_HOME/bin:${PATH}
```
生效配置：
```sh
source .bash_aliases
```
确认版本：
```sh
mvn -v
```

### 3.3 配置 Maven 阿里云仓库镜像
编辑 `maven/conf/setting.xml`，添加以下内容：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings>
	<mirrors>
		<mirror>
		    <id>nexus-aliyun</id>
		    <mirrorOf>central</mirrorOf>
		    <name>Nexus aliyun</name>
		    <url>http://maven.aliyun.com/nexus/content/groups/public</url>
		</mirror>
	</mirrors>
</settings>
```

### 3.4 创建并运行 Maven 工程
新建工程：
```sh
mvn archetype:generate -DgroupId=com.yulecode.app -DartifactId=my-app  -DinteractiveMode=false
```
打包：
```sh
mvn package
```
运行：
```sh
java -cp target/my-app-1.0-SNAPSHOT.jar com.yulecode.app.App
```