实验四  HDFS编程实现

**一、实验目的**

本实验旨在全面培养基于 Node.js 的 HDFS 编程技能，涵盖以下关键点：

1.  环境搭建：安装与配置 Node.js、VS Code 及 Hadoop 开发环境，为 Node.js 操作 HDFS 奠定基础。

2.  工具使用：学习运用 npm/yarn 管理项目依赖，配置日志系统，提升开发效率与代码质量。

3.  Node.js 编程：通过 Node.js 及 HDFS WebHDFS API，掌握文件上传、下载、删除、重命名等基本操作，深入了解文件属性查询与类型判断。

4.  I/O 操作：实践 Node.js 流（Stream）技术在 HDFS 文件传输中的应用，实现高效上传下载。

5.  深入理解：通过实战操作，加深对 HDFS 架构、组件及工作原理的认识，为大数据处理打下坚实基础。

**二、实验环境**

1. 操作系统：推荐使用 Linux 发行版，如 Ubuntu 20.04 LTS，便于 Hadoop 环境的部署。

2. Node.js：安装 Node.js 24.x 或更高版本（LTS 版本）。

3. Hadoop：Hadoop 3.x 版本，开启 WebHDFS 服务（核心要求）。

4. IDE：VS Code（轻量高效，适配 Node.js 开发）。

5. 包管理工具：npm（Node.js 自带，用于管理项目依赖）。

**三、实验内容**

本实验的内容主要包括以下几个方面：

1.  为HDFS的客户端准备环境，包括安装Node.js并配置环境变量、安装VS Code开发工具、配置hadoop 开发环境。配置hadoop开发环境,设置HADOOP_HOME环境变量，并 确保hadoop的二进制文件包含在PATH变量中。

2.  HDFS编程api入门，包括创建 npm工程、导入工程依赖、添加Hadoop等所需库的依赖。配置日 志属性,设置日志级别和输出格式，以便于调试和查看程序运行情况。

3.  程序 实现与测试，编写代码以实现对HDFS文件的操作，包括文件上传、下载、删除、重命名等， 并执行单元测试来验证功能。

4.  HDFS文件上传api，HDFS文件下载api，HDFS文件夹删除api，HDFS文件名更改api操作，HDFS 查看文件详情api操作，判断HDFS文件或文件夹，文件上传API，使用HDFS的Put接口，编写 方法上传文件到HDFS。文件下载API，使用HDFS的Get接口，编写方法从HDFS下载文件。文件 夹删除API，编写方法删除HDFS中的目录。文件名更改API，利用HDFS的Rename接口，编写方 法更改HDFS中文件的名称。查看文件详情API，使用stat接口，获取文件的状态 信息，如大小、修改时间等。判断文件或文件夹，编写逻辑来判断给定的HDFS路径是文件还 是文件夹。

5. 通过I/O流方式实现HDFS文件上传，通过I/O流方式实现HDFS文件下载，HDFS定位读取文件。 文件上传，使用输入输出流结合HDFS的FileSystem API来上传文件到HDFS。文件下载，同样 使用I/O流从HDFS读取文件并下载到本地文件系统。

**四、实验预习和准备**

为了进行 Node.js 版本 HDFS 编程实现实验，需要完成以下预习和准备工作：

1. 理解 Hadoop 生态系统和 HDFS 基本概念阅读 Hadoop 资料，理解架构、组件（NameNode、DataNode），学习 WebHDFS 工作原理。

2. Node.js 和 npm 基础,熟悉 Node.js 基础语法、异步编程、流（Stream）；掌握 npm 包管理、依赖安装。

3. 环境安装与配置,安装 Node.js，验证 node -v、npm -v。

4. 安装 VS Code 开发工具,安装配置 Hadoop，验证 hadoop version。

5. WebHDFS 配置,学习修改 core-site.xml、hdfs-site.xml 开启 WebHDFS 服务，掌握服务重启流程。

6. Node.js HDFS 客户端,学习 webhdfs 库的使用，掌握基于 API 的文件操作、流操作方式。

7. 日志和调试,掌握 VS Code 调试 Node.js 代码的方法。

8. Node.js 流操作,理解 ReadStream、WriteStream，掌握流在文件上传下载中的应用。

**五、实验过程与结果**

**六、实验结果分析与体会**