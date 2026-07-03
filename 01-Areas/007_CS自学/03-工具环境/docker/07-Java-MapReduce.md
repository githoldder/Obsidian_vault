# Java MapReduce

## 安装 Maven

### Maven 下载地址

```
https://maven.apache.org/download.cgi
```

### 启动 Docker 镜像

```batch
docker run `
--name=myclient `
--hostname=myclient `
--ip=172.18.11.5 `
--network=mynet `
--add-host=mymaster:172.18.11.4 `
--add-host=myworker1:172.18.11.1 `
--add-host=myworker2:172.18.11.2 `
--add-host=myworker3:172.18.11.3 `
-v d:\source:/root/source `
-p 8888:8888 `
-itd myubuntu run.sh
```

### 拷贝 Maven 压缩包到镜像

```
docker cp apache-maven-3.8.6-bin.tar.gz myclient:/root/
```

### 进入容器

```
docker exec -it myclient bash
```

### 解压缩

```
cd
tar -zxvf apache-maven-3.8.6-bin.tar.gz
mv apache-maven-3.8.6 maven
rm -f apache-maven-3.8.6-bin.tar.gz
```

### 配置环境变量

编辑 .bashrc 文件
```
vi .bash_aliases
```
添加内容
```
# MAVEN
export MAVEN_HOME=/root/maven
export PATH=$MAVEN_HOME/bin:${PATH}
```
使配置文件生效
```
source .bash_aliases
mvn -v
```

### 配置阿里仓库

```
cd ~/maven/conf/
mv settings.xml settings.xml.bak
vi settings.xml
```

添加配置信息

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings>
	<mirrors>
		<mirror>
			<id>nexus-aliyun</id>
			<name>Nexus aliyun</name>
			<url>https://maven.aliyun.com/nexus/content/groups/public/</url>
			<mirrorOf>central</mirrorOf>
		</mirror>
	</mirrors>
</settings>  
```

## Maven Hello World

### 新建工程

```
mvn archetype:generate -DgroupId=com.yulecode.hello -DartifactId=hello -DinteractiveMode=false -Dproject.build.sourceEncoding=UTF-8
```

### 安装依赖包

```
cd hello
mvn package
```

### 运行

```
java -cp target/hello-1.0-SNAPSHOT.jar com.yulecode.hello.App
```


## WordCount MapReduce

### pom.xml

```xml
<!-- Hadoop Common 核心依赖 -->
<dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-common</artifactId>
    <version>3.3.4</version>
    <scope>provided</scope>
</dependency>
<!-- Hadoop MapReduce 核心依赖 -->
<dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-mapreduce-client-core</artifactId>
    <version>3.3.4</version>
    <scope>provided</scope>
</dependency>
```


UTF-8 编码错误

```xml
<build>
<plugins>
    <!-- 配置 maven-compiler-plugin 编码 -->
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.1</version>
        <configuration>
            <!-- 指定源码和目标 JDK 版本（根据你的环境调整） -->
            <source>8</source>
            <target>8</target>
            <!-- 核心：指定编码为 UTF-8 -->
            <encoding>UTF-8</encoding>
        </configuration>
    </plugin>
</plugins>
</build>
```


### WordCount.java

```java
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable; 
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Hadoop MapReduce 单词计数案例
 */
public class WordCount {

    // 1. Map 阶段：继承 Mapper 类
    /**
     * KEYIN: 输入数据的键类型（行偏移量，LongWritable）
     * VALUEIN: 输入数据的值类型（一行文本，Text）
     * KEYOUT: 输出数据的键类型（单词，Text）
     * VALUEOUT: 输出数据的值类型（计数 1，IntWritable）
     */
    public static class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        // 定义输出的 value 固定为 1
        private final static IntWritable one = new IntWritable(1);
        // 定义输出的 key（单词）
        private Text word = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // 1. 将一行文本转为字符串
            String line = value.toString();
            // 2. 按空格/制表符/换行符切分单词（支持多种分隔符）
            String[] words = line.split("\\s+");
            // 3. 遍历单词，输出 <单词, 1>
            for (String w : words) {
                word.set(w);
                context.write(word, one);
            }
        }
    }

    // 2. Reduce 阶段：继承 Reducer 类
    /**
     * KEYIN: Map 输出的键类型（单词，Text）
     * VALUEIN: Map 输出的值类型（1，IntWritable）
     * KEYOUT: 最终输出的键类型（单词，Text）
     * VALUEOUT: 最终输出的值类型（总次数，IntWritable）
     */
    public static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            // 1. 遍历相同单词的所有 1，求和
            for (IntWritable val : values) {
                sum += val.get();
            }
            // 2. 输出 <单词, 总次数>
            context.write(key, new IntWritable(sum));
        }
    }

    // 3. Driver 阶段：作业配置（主函数）
    public static void main(String[] args) throws Exception {
        // 1. 加载 Hadoop 配置
        Configuration conf = new Configuration();
        // 2. 创建 Job 实例
        Job job = Job.getInstance(conf, "wordcount");
        // 3. 设置驱动类
        job.setJarByClass(WordCount.class);
        // 4. 设置 Map/Reduce 类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        // 5. 设置 Map 输出键值类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        // 6. 设置最终输出键值类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        // 7. 设置输入/输出路径（从命令行传入）
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        // 8. 提交作业并等待完成
        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }
}

```

### 安装依赖包

```
cd hello
mvn clean package
```
复制到 mymaster

```
scp target/hello-1.0-SNAPSHOT.jar mymaster:/root/wordcount.jar
```


### 准备测试数据

test.txt

```
hello hadoop
hello mapreduce
hadoop mapreduce
```

### 复制到HDFS

```sh
# 创建输入目录
hdfs dfs -mkdir -p /wordcount/input
# 上传本地文件到 HDFS
hdfs dfs -put test.txt /wordcount/input
```

### 运行 MapReduce

```sh
hadoop jar wordcount.jar WordCount /wordcount/input /wordcount/output
```

第一个参数：Jar 包路径
第二个参数：主类全类名
第三个参数：HDFS 输入路径
第四个参数：HDFS 输出路径 **（必须不存在，否则报错）**

删除 output 目录

```sh
hdfs dfs -rm -r /wordcount/output
```

出现类似以下错误信息

```
/bin/bash: /bin/java: No such file or directory

```

添加 java 软连接

```
ln -s /root/java/bin/java /bin/java

```

### 查看结果

```
hdfs dfs -cat /wordcount/output/part-r-00000
```

## 销售统计

### sales.txt

```
1001,apple,5,3.5,2025-01-01
1002,banana,10,2.0,2025-01-01
1003,apple,3,3.5,2025-01-01
1004,orange,8,4.0,2025-01-01
1005,banana,5,2.0,2025-01-01
```

### 上传到 HDFS

```sh
# 创建输入目录
hdfs dfs -mkdir -p /sales/input
# 上传文件
hdfs dfs -put sales.txt /sales/input
```

### SalesCount.java

```java
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 销售数据统计：按商品统计总销售额
 */
public class SalesCount {

    // 1. Map 阶段：解析数据，输出 <商品ID, 单条销售额>
    public static class SalesMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // 1. 将一行数据转为字符串
            String line = value.toString();
            // 2. 按逗号切割字段
            String[] fields = line.split(",");
            
            // 简单数据校验（防止脏数据报错）
            if (fields.length != 5) {
                return;
            }

            try {
                // 3. 提取需要的字段
                String goodsId = fields[1];       // 商品ID
                int num = Integer.parseInt(fields[2]);  // 销售数量
                double price = Double.parseDouble(fields[3]); // 销售单价
                
                // 4. 计算单条销售额
                double sales = num * price;
                
                // 5. 输出：key=商品ID，value=单条销售额
                context.write(new Text(goodsId), new DoubleWritable(sales));
            } catch (Exception e) {
                // 脏数据跳过
                return;
            }
        }
    }

    // 2. Reduce 阶段：按商品聚合，计算总销售额
    public static class SalesReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
        @Override
        protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            // 1. 初始化总销售额
            double totalSales = 0.0;
            
            // 2. 遍历累加所有销售额
            for (DoubleWritable val : values) {
                totalSales += val.get();
            }
            
            // 3. 输出最终结果：<商品ID, 总销售额>
            context.write(key, new DoubleWritable(totalSales));
        }
    }

    // 3. Driver 阶段：配置任务
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "sales_count");
        
        job.setJarByClass(SalesCount.class);
        job.setMapperClass(SalesMapper.class);
        job.setReducerClass(SalesReducer.class);
        
        // Map 输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);
        
        // 最终输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
        
        // 设置输入输出路径
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        // 提交任务
        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }
}
```

### 运行 

```sh
hadoop jar wordcount.jar SalesCount /sales/input /sales/output
```

### 查看结果

```sh
hdfs dfs -cat /sales/output/part-r-00000
```