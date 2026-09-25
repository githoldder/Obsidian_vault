# Node.js 访问 Hadoop

## 准备 Yarn 本地环境

准备好已经安装了 hadoop 的镜像 myubuntu

### 启动容器

```sh
docker run -itd `
--name myhadoop `
--network=mynet `
--ip=172.18.11.1 `
--privileged `
-p 8088:8088 `
-p 9870:9870 `
-p 19888:19888 `
-p 9000:9000 `
myhadoop run.sh
```

### 进入容器

```sh
docker exec -it mynode bash
```

### core-site.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://myhadoop:9000</value>
    </property>
</configuration>
```

### hdfs-site.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
    <property>
    	<name>dfs.persissions.enabled</name>
    	<value>false</value>
	</property>
</configuration>
```

### mapred-site.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
    <property>
        <name>mapreduce.application.classpath</name>
        <value>$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/*:$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/lib/*</value>
    </property>
</configuration>
```

### yarn-site.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <property>
        <name>yarn.nodemanager.env-whitelist</name>
        <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
    </property>
</configuration>
```

### start-dfs.sh 和 stop-dfs.sh

```sh
#!/usr/bin/env bash
HDFS_DATANODE_USER=root
HDFS_NAMENODE_USER=root
HDFS_SECONDARYNAMENODE_USER=root
HADOOP_SECURE_DN_USER=hdfs
```

### start-yarn.sh 和 stop-yarn.sh

```sh
#!/usr/bin/env bash
YARN_RESOURCEMANAGER_USER=root
HADOOP_SECURE_DN_USER=yarn
YARN_NODEMANAGER_USER=root
```

### JAVA_HOME 错误

hadoop/etc/hadoop/hadoop-env.sh 中添加

```sh
JAVA_HOME=/root/java
```

### 添加 java 软连接

```sh
ln -s /root/java/bin/java /bin/java
```

### 格式化 namenode

```sh
hdfs namenode -format
```

## Node.js 访问

### 创建 node 容器

docker pull m.daocloud.io/docker.io/library/node

```sh
docker run -itd `
--name mynode `
--network=mynet `
--ip=172.18.11.5 `
-p 8080:8080 `
-v D:/hello-dfs:/root/hello-dfs `
node bash
```
将本地路径映射到容器路径

D:/hello-dfs 为本地路径

/root/hello-dfs 为容器路径

进入容器

```sh
docker exec -it myhadoop bash
```

### 初始化目录

```sh
npm init -y
```

### 安装 webhdfs

https://hadoop.apache.org/docs/r3.3.4/hadoop-project-dist/hadoop-hdfs/WebHDFS.html

https://github.com/harrisiirak/webhdfs

```sh
npm i webhdfs
```

### 显示目录内容

```js
var WebHDFS = require('webhdfs');

var hdfs = WebHDFS.createClient({
    user: 'root',
    host: '172.18.11.1',
    port: 9870,
    path: '/webhdfs/v1'
});

// 列出HDFS根目录下的文件和文件夹
hdfs.readdir('/user/root', function (err, data) {
    if (err) throw err;
    console.log(JSON.stringify(data));
});

```

### 创建目录

```js
hdfs.mkdir('/user/root/test', callback = function (err) {
    if (err) throw err;
    console.log('Directory created successfully');
});
```

### 写文件

```js
const content = 'Hello, HDFS!';
hdfs.writeFile('/user/root/hello.txt', content, callback = function (err) {
    if (err) throw err;
    console.log('File written successfully');
});
```

### 读文件

```js
hdfs.readFile('/user/root/hello.txt', function (err, data) {
    if (err) throw err;
    const decoder = new TextDecoder();
    console.log(decoder.decode(data));
});
```

## Server

### express

```js
import express from 'express';

const app = express();

const PORT = 8080;

// 健康检查接口
app.get('/health', (req, res) => {
    res.status(200).json({ status: 'healthy', timestamp: new Date() });
});

// 启动服务器
app.listen(PORT, () => {
    console.log(`HDFS RESTful 服务已启动，端口: ${PORT}`);
});

```

### dotenv

```js
require('dotenv').config();

const PORT = process.env.PORT || 8080;
```

.env 

```
PORT=8080
```

### axios

HTTP 请求

```js
const axios = require('axios');

const response = await axios.put(url);
        
res.status(200).json({
    message: `文件夹创建成功: ${path}`,
    response: response.data
});

```

### bodyParser

body 解析

```js
const bodyParser = require('body-parser');

app.use(bodyParser.json());
app.use(bodyParser.text()); // 用于接收文件内容
app.use(bodyParser.urlencoded({ extended: true }));
```






