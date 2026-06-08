## 环境准备

### 安装 Node.js


```
https://nodejs.org/zh-cn/download
```

### 确认版本

```sh
node -v
npm -v
```

### 初始化工程

```sh
npm init -y
```
在目录下创建一个 package.json 文件

### 安装依赖

```sh
npm i express multer webhdfs cors
```

需要的第三方组件将安装到 node_modules 目录下

## Web 服务

### Hello Express

```js
const express = require('express');
const app = express();

app.get('/', (req, res) => {
    res.send('Hello Express');
})

app.listen(8080, () => {
  console.log(`Web服务已启动，地址: http://localhost:8080`);
});
```



### 测试

浏览器访问地址 http://localhost:8080
```
http://localhost:8080
```

## JSON 数据

### 解析 JSON 数据

```js
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
```

### 发送接收 JSON 数据

JSON 数据将被解析到 req.body 上


```js
app.post('/json', (req, res) => {
    // 接收 JSON 数据
    const json = req.body;
    // 发送 JSON 数据
    res.json(json);
})
```

### REST Client 测试

VS Code 安装插件 REST Client

```rest
@BASE_URL = http://localhost:8080

### GET /
GET {{BASE_URL}}


### POST /JSON
POST {{BASE_URL}}/json
Content-Type: application/json

{
    "username": "admin",
    "password": "123456"
}
```

需要权限验证的模式

```rest
# 登录获取 token
POST {{BASE_URL}}/api/login
Content-Type: application/json

{
  "username":"admin",
  "password":"123456"
}

> {%
client.global.set("token", response.body.token);
%}

### 用 token 请求
GET {{BASE_URL}}/api/user/info
Authorization: Bearer {{token}}
```

## 文件上传

### 服务器接收文件

```js
const multer = require("multer");
const path = require("path");

const UPLOAD_TMP_DIR = path.join(__dirname, "tmp");

const upload = multer({
  // storage: multer.memoryStorage(),
  dest: UPLOAD_TMP_DIR,
  limits: {
    fileSize: 100 * 1024 * 1024, // 100MB限制
  },
});

app.post("/upload, upload.single("file"), (req, res) => {
  // 检查文件是否存在
  if (!req.file) {
    return res.status(400).json({ error: "请选择要上传的文件" });
  } else {
  	res.json(req.file);
  }
}
```

上传的文件保存到 tmp 目录下 

### 客户端上传文件
```
POST {{BASE_URL}}/upload
Content-Type: multipart/form-data; boundary=WebAppBoundary

--WebAppBoundary
Content-Disposition: form-data; name="file"; filename="drone.png"
Content-Type: image/jpeg

< ./drone.png
```

### HTML/js 

```html
<input type="file" id="fileInput" />
<button onclick="uploadWithProgress()">上传</button>
<div>进度：<span id="progress">0%</span></div>

<script>
function uploadWithProgress() {
  const file = document.getElementById('fileInput').files[0];
  if (!file) return;

  const formData = new FormData();
  formData.append('file', file);

  const xhr = new XMLHttpRequest();
  xhr.open('POST', 'http://你的后端上传接口');

  // 监听上传进度
  xhr.upload.onprogress = function(e) {
    if (e.lengthComputable) {
      const percent = (e.loaded / e.total) * 100;
      document.getElementById('progress').textContent = percent.toFixed(0) + '%';
    }
  };

  xhr.onload = function() {
    if (xhr.status === 200) {
      console.log('成功', JSON.parse(xhr.responseText));
    }
  };

  xhr.send(formData);
}
</script>
```

## RESTful 框架

```js
// 浏览文件夹
app.get(/^\/api\/list\/(.*)$/, (req, res) => { });

// 创建文件夹
app.post(/^\/api\/mkdir\/(.*)$/, (req, res) => { });

// 删除文件夹或文件
app.delete(/^\/api\/delete\/(.*)$/, (req, res) => { });

// 重命名/移动文件夹或文件
app.put(/^\/api\/rename\/(.*)$/, (req, res) => { });

// 上传文件
app.post(/^\/api\/upload\/(.*)$/, upload.single("file"), (req, res) => { });

// 下载文件
app.get(/^\/api\/download\/(.*)$/, (req, res) => { });

// 获取文件信息
app.get(/^\/api\/info\/(.*)$/, (req, res) => { });

// 复制文件
app.post(/^\/api\/copy\/(.*)$/, (req, res) => { });
```

## HDFS 操作

### API 准备

```js
const WebHDFS = require("webhdfs");

// HDFS配置
const hdfsConfig = {
  host: "172.18.11.1", // HDFS namenode host
  port: 9870, // WebHDFS端口
  user: "root", // HDFS用户
  path: "/webhdfs/v1",
};

// 创建HDFS客户端实例
const hdfs = WebHDFS.createClient(hdfsConfig);
```

### 辅助函数

```js
// ==================== 辅助函数 ====================
/**
 * 标准化路径，防止路径遍历攻击
 * @param {string} filePath - 原始路径
 * @returns {string} 标准化后的路径
 */
function normalizePath(filePath) {
  if (!filePath || filePath === "/") return "/";

  // 确保以/开头
  let normalized = filePath.startsWith("/") ? filePath : `/${filePath}`;

  // 移除重复的斜杠
  normalized = normalized.replace(/\/+/g, "/");

  // 移除路径遍历攻击（..）
  const parts = normalized.split("/");
  const safeParts = [];
  for (const part of parts) {
    if (part === "..") {
      safeParts.pop();
    } else if (part !== "." && part !== "") {
      safeParts.push(part);
    }
  }

  normalized = "/" + safeParts.join("/");

  // 如果原路径以/结尾且不是根路径，保持以/结尾（表示目录）
  if (filePath.endsWith("/") && normalized !== "/") {
    normalized += "/";
  }

  return normalized;
}

/**
 * 验证路径安全性
 * @param {string} filePath - 要验证的路径
 * @returns {boolean} 是否安全
 */
function isPathSafe(filePath) {
  const normalized = normalizePath(filePath);
  // 防止路径遍历攻击
  return !normalized.includes("..");
}
```

### 浏览文件夹

```js
/**
 * 浏览目录
 * GET /api/list/*?recursive=false
 */
app.get(/^\/api\/list\/(.*)$/, (req, res) => {
  let dirPath = req.params[0] || "";
  dirPath = normalizePath(dirPath || "/");

  if (!isPathSafe(dirPath)) {
    return res.status(400).json({ error: "无效的路径" });
  }

  hdfs.readdir(dirPath, (err, files) => {
    if (err) {
      console.error(`读取目录失败 ${dirPath}:`, err);
      return res.status(500).json({
        error: "读取目录失败",
        details: err.message,
        path: dirPath,
      });
    }
    res.json({
      success: true,
      path: dirPath,
      items: files,
    });
  });
});
```

### 创建文件夹

```js
/**
 * 创建文件夹
 * POST /api/mkdir/*
 */
app.post(/^\/api\/mkdir\/(.*)$/, (req, res) => {
  let dirPath = req.params[0] || "";
  dirPath = normalizePath(dirPath);

  if (!dirPath || dirPath === "/") {
    return res.status(400).json({ error: "请指定要创建的文件夹路径" });
  }

  if (!isPathSafe(dirPath)) {
    return res.status(400).json({ error: "无效的路径" });
  }

  const permission = (req.body && req.body.permission) || "0755";

  hdfs.mkdir(dirPath, { permission }, (err) => {
    if (err) {
      console.error(`创建文件夹失败 ${dirPath}:`, err);
      return res.status(500).json({
        error: "创建文件夹失败",
        details: err.message,
        path: dirPath,
      });
    }
    res.json({
      success: true,
      message: "文件夹创建成功",
      path: dirPath,
    });
  });
});
```

### 删除文件/文件夹

```js
/**
 * 删除文件/文件夹
 * DELETE /api/delete/*?recursive=false
 */
app.delete(/^\/api\/delete\/(.*)$/, (req, res) => {
  const targetPath = normalizePath(req.params[0] || "");

  if (!targetPath || targetPath === "/") {
    return res.status(400).json({ error: "无法删除根目录" });
  }

  if (!isPathSafe(targetPath)) {
    return res.status(400).json({ error: "无效的路径" });
  }

  const recursive = req.query.recursive === "true";

  // 先检查是文件还是目录
  hdfs.stat(targetPath, (err, stats) => {
    if (err) {
      return res
        .status(500)
        .json({ error: "获取文件信息失败", details: err.message });
    }

    if (stats.type === "DIRECTORY") {
      // 删除目录
      hdfs.rmdir(targetPath, { recursive }, (err) => {
        if (err) {
          console.error(`删除目录失败 ${targetPath}:`, err);
          return res.status(500).json({
            error: "删除目录失败",
            details: err.message,
            path: targetPath,
          });
        }
        res.json({
          success: true,
          message: "目录删除成功",
          path: targetPath,
        });
      });
    } else {
      // 删除文件
      hdfs.unlink(targetPath, (err) => {
        if (err) {
          console.error(`删除文件失败 ${targetPath}:`, err);
          return res.status(500).json({
            error: "删除文件失败",
            details: err.message,
            path: targetPath,
          });
        }
        res.json({
          success: true,
          message: "文件删除成功",
          path: targetPath,
        });
      });
    }
  });
});

```

### 重命名/移动

```js
/**
 * 重命名/移动文件或文件夹
 * PUT /api/rename/*
 * Body: { "destination": "/新路径" }
 */
app.put(/^\/api\/rename\/(.*)$/, (req, res) => {
  const srcPath = normalizePath(req.params[0] || "");
  const { destination } = req.body;

  if (!srcPath || srcPath === "/") {
    return res.status(400).json({ error: "无效的源路径" });
  }

  if (!destination) {
    return res.status(400).json({ error: "缺少目标路径" });
  }

  const destPath = normalizePath(destination);

  if (!isPathSafe(srcPath) || !isPathSafe(destPath)) {
    return res.status(400).json({ error: "无效的路径" });
  }

  hdfs.rename(srcPath, destPath, (err) => {
    if (err) {
      console.error(`重命名失败 ${srcPath} -> ${destPath}:`, err);
      return res.status(500).json({
        error: "重命名失败",
        details: err.message,
        from: srcPath,
        to: destPath,
      });
    }
    res.json({
      success: true,
      message: "重命名成功",
      from: srcPath,
      to: destPath,
    });
  });
});
```

### 上传文件

```js
/**
 * 上传文件
 * POST /api/upload/*
 * FormData: { file: 文件 }
 */
app.post(/^\/api\/upload\/(.*)$/, upload.single("file"), (req, res) => {
  // 检查文件是否存在
  if (!req.file) {
    return res.status(400).json({ error: "请选择要上传的文件" });
  }

  let targetDir = req.params[0] || "";
  targetDir = normalizePath(targetDir || "/");

  if (!isPathSafe(targetDir)) {
    return res.status(400).json({ error: "无效的路径" });
  }

  const fileName = req.file.originalname;
  const targetPath = path.posix.join(targetDir, fileName);
  // 确保目标目录存在，如果不存在则创建
  const localStream = fs.createReadStream(req.file.path);
  const remoteStream = hdfs.createWriteStream(targetPath);
  localStream.pipe(remoteStream);

  remoteStream.on("error", (err) => {
    console.error(`上传失败 ${targetPath}:`, err);
    res.status(500).json({
      error: "上传失败",
      details: err.message,
      path: targetPath,
    });
  });

  remoteStream.on("finish", () => {
    fs.unlinkSync(req.file.path);
    res.json({
      success: true,
      message: "上传成功",
      path: targetPath,
      size: req.file.size,
      fileName: fileName,
    });
  });
});
```

### 下载文件

```js
/**
 * 下载文件
 * GET /api/download/*
 */
app.get(/^\/api\/download\/(.*)$/, (req, res) => {
  const filePath = normalizePath(req.params[0] || "");

  if (!filePath || filePath === "/") {
    return res.status(400).json({ error: "请指定要下载的文件" });
  }

  if (!isPathSafe(filePath)) {
    return res.status(400).json({ error: "无效的路径" });
  }

  // 检查文件是否存在
  hdfs.stat(filePath, (err, stats) => {
    if (err) {
      return res
        .status(404)
        .json({ error: "文件不存在", details: err.message });
    }

    if (stats.type === "DIRECTORY") {
      return res.status(400).json({ error: "无法下载目录，请指定文件" });
    }

    // 设置下载响应头
    const fileName = path.basename(filePath);
    res.setHeader(
      "Content-Disposition",
      `attachment; filename="${encodeURIComponent(fileName)}"`,
    );
    res.setHeader("Content-Type", "application/octet-stream");
    res.setHeader("Content-Length", stats.length);

    const remoteFile = hdfs.createReadStream(filePath);

    remoteFile.on("error", (err) => {
      console.error(`下载失败 ${filePath}:`, err);
      if (!res.headersSent) {
        res.status(500).json({ error: "下载失败", details: err.message });
      }
    });

    remoteFile.pipe(res);
  });
});
```

### 获取文件信息

```js
/**
 * 获取文件/文件夹信息
 * GET /api/info/*
 */
app.get(/^\/api\/info\/(.*)$/, (req, res) => {
  const filePath = normalizePath(req.params[0] || "/");

  if (!isPathSafe(filePath)) {
    return res.status(400).json({ error: "无效的路径" });
  }

  hdfs.stat(filePath, (err, stats) => {
    if (err) {
      console.error(`获取信息失败 ${filePath}:`, err);
      return res.status(500).json({
        error: "获取信息失败",
        details: err.message,
        path: filePath,
      });
    }
    res.json({
      success: true,
      path: filePath,
      info: stats,
    });
  });
});
```

### 复制文件

```js
/**
 * 复制文件
 * POST /api/copy/*
 * Body: { "destination": "/目标路径" }
 */
app.post(/^\/api\/copy\/(.*)$/, (req, res) => {
  const srcPath = normalizePath(req.params[0] || "");
  const { destination } = req.body;

  if (!srcPath || srcPath === "/") {
    return res.status(400).json({ error: "无效的源路径" });
  }

  if (!destination) {
    return res.status(400).json({ error: "缺少目标路径" });
  }

  const destPath = normalizePath(destination);

  if (!isPathSafe(srcPath) || !isPathSafe(destPath)) {
    return res.status(400).json({ error: "无效的路径" });
  }

  // 检查源文件是否存在
  hdfs.stat(srcPath, (err, stats) => {
    const readStream = hdfs.createReadStream(srcPath);
    const writeStream = hdfs.createWriteStream(destPath);

    // Node 内置内存流（零依赖，标准 Stream）
    const memoryStream = new PassThrough();

    // 1. webhdfs 读取 -> 内存流
    readStream.pipe(memoryStream);
    // 2. 内存流 -> webhdfs 写入（完美兼容）
    memoryStream.pipe(writeStream);

    // 错误监听
    readStream.on("error", (err) => {
      console.error("读取失败:", err);
      res.status(500).json({ error: "读取文件失败", details: err.message });
    });

    writeStream.on("error", (err) => {
      console.error("写入失败:", err);
      res.status(500).json({ error: "复制文件失败", details: err.message });
    });

    // 完成
    writeStream.on("finish", () => {
      res.json({
        success: true,
        message: "复制成功",
        from: srcPath,
        to: destPath,
        size: stats.length,
      });
    });
  });
});
```

## 其他接口

### 健康检查

```js
/**
 * 健康检查接口
 * GET /api/health
 */
app.get("/api/health", (req, res) => {
  res.json({
    status: "ok",
    timestamp: new Date().toISOString(),
    hdfs: {
      host: hdfsConfig.host,
      port: hdfsConfig.port,
      user: hdfsConfig.user,
    },
  });
});
```

### 404 NOT FOUND

```js
/**
 * 404处理中间件
 */
app.use((req, res) => {
  res.status(404).json({ error: "接口不存在", path: req.path });
});
````