# HTTP（超文本传输协议）

## 📖 定义-解释-示例

### 什么是 HTTP？

**HTTP**（HyperText Transfer Protocol）是万维网（WWW）数据通信的基础协议。它运行在 **TCP/IP 协议栈的应用层**，默认使用 **80 端口**，采用 **请求-响应** 模型：客户端（通常是浏览器）发起请求，服务器返回响应。HTTP 本身是**无状态协议**——每个请求独立，服务器不保留前后请求之间的上下文信息。

### HTTP 的发展历程

- **HTTP/0.9（1991）**：最简版本，仅支持 `GET` 方法，只返回 HTML，无 header，响应即关闭连接。一个请求一个 TCP 连接。
- **HTTP/1.0（1996）**：引入 `POST`、`HEAD` 方法，增加 **Content-Type** 等 header 字段，支持状态码。每个请求仍需要独立的 TCP 连接（短连接），效率低下。
- **HTTP/1.1（1997）**：最重要的改进是**持久连接（Keep-Alive）**，默认不复开新 TCP 连接；引入**管线化（Pipelining）**允许在单个 TCP 连接上并行发送多个请求；增加 `PUT` `DELETE` `OPTIONS` 等方法；引入 **Host 头**使同一 IP 可托管多个域名（虚拟主机）。
- **HTTP/2（2015）**：基于 SPDY 协议，核心特性：**二进制分帧**（不再用纯文本）、**多路复用**（一个 TCP 连接上同时传输多个请求/响应，彻底解决 HTTP/1.1 的队头阻塞）、**头部压缩**（HPACK 算法）、**服务器推送**。
- **HTTP/3（2022）**：底层传输从 TCP 切换到 **QUIC**（基于 UDP），解决 TCP 层面的队头阻塞，连接迁移更优雅（IP 变化不断连）。

### HTTP 请求报文结构

一个典型的 HTTP 请求如下：

```
POST /api/login HTTP/1.1
Host: www.example.com
Content-Type: application/json
Content-Length: 42
Accept: application/json
Authorization: Bearer eyJhbGciOi...

{"username":"admin","password":"123456"}
```

报文包含四个部分：
1. **请求行**：`方法 URI 版本` —— `POST /api/login HTTP/1.1`
2. **请求头**：`Host`、`Content-Type`、`Authorization` 等元信息
3. **空行**（`\r\n`）：分隔头部和体部
4. **请求体**：承载数据（GET 请求通常无体）

### HTTP 响应报文结构

```
HTTP/1.1 200 OK
Content-Type: application/json; charset=utf-8
Content-Length: 85
Set-Cookie: session=abc123; HttpOnly; Secure

{"status":"success","token":"eyJ..."}
```

1. **状态行**：`版本 状态码 原因短语` —— `HTTP/1.1 200 OK`
2. **响应头**
3. **空行**
4. **响应体**

### HTTP 请求方法

| 方法 | 语义 | 幂等 | 安全 |
|------|------|------|------|
| GET | 获取资源 | ✅ | ✅ |
| HEAD | 获取资源头部（无体） | ✅ | ✅ |
| POST | 创建资源/提交数据 | ❌ | ❌ |
| PUT | 替换（更新）资源 | ✅ | ❌ |
| PATCH | 部分更新资源 | ❌ | ❌ |
| DELETE | 删除资源 | ✅ | ❌ |
| OPTIONS | 查询支持的方法 | ✅ | ✅ |
| TRACE | 回显请求（调试） | ✅ | ✅ |
| CONNECT | 建立隧道（代理 HTTPS） | ❌ | ❌ |

### 常见状态码分类

- **1xx 信息**：`100 Continue`（客户端应继续发送请求体）、`101 Switching Protocols`
- **2xx 成功**：`200 OK`、`201 Created`（POST 创建成功）、`204 No Content`（删除成功）
- **3xx 重定向**：`301 Moved Permanently`（永久重定向，SEO 权重转移）、`302 Found`（临时重定向）、`304 Not Modified`（缓存命中，条件请求返回）
- **4xx 客户端错误**：`400 Bad Request`（请求语法错误）、`401 Unauthorized`（需认证）、`403 Forbidden`（已认证但无权限）、`404 Not Found`、`405 Method Not Allowed`
- **5xx 服务端错误**：`500 Internal Server Error`、`502 Bad Gateway`（上游服务器返回无效响应）、`503 Service Unavailable`（服务器过载/维护）

### HTTP 的核心特性

**无状态性**：服务器不记住客户端。解决方案——Cookie + Session（服务端存状态，客户端带 Session ID）、JWT Token（自包含令牌，客户端存储）、Token 基于 `Authorization` 请求头。

**内容协商**：客户端通过 `Accept` 头告知期望的响应格式，服务器通过 `Content-Type` 返回实际格式。常见 MIME 类型：`text/html`、`application/json`、`image/png`、`multipart/form-data`。

**条件请求**：利用 `If-Modified-Since`（时间）+ `If-None-Match`（ETag）实现缓存验证。服务器对比后若资源未变返回 `304 Not Modified`，客户端使用本地缓存，大幅减少带宽消耗。

**Range 请求**：`Range: bytes=0-1023` 实现断点续传。服务器返回 `206 Partial Content`，配合 `Content-Range` 头表示实际返回的字节范围。这对于大文件下载和视频流播放至关重要。

### HTTP 缓存机制

**强缓存**：由 `Cache-Control`（优先）和 `Expires` 控制。`Cache-Control: max-age=3600` 表示 3600 秒内直接使用缓存，不发请求。`no-cache` 表示每次都需验证，`no-store` 表示禁止缓存。

**协商缓存**：强缓存过期后，浏览器携带 `If-Modified-Since` 或 `If-None-Match` 请求服务器验证。服务器返回 `304` 表示可以继续用缓存，`200` 表示需要重新获取。

### HTTP 的局限性

HTTP 明文传输，所有数据（包括账号密码）在网络上**以明文形式传输**，任何中间节点（路由器、代理）都可以截获和篡改内容。这直接催生了 HTTPS 的出现。此外，HTTP/1.1 的队头阻塞（一个请求慢会阻塞后面所有请求）在 HTTP/2 的多路复用中得到缓解，但 TCP 层面的队头阻塞一直要到 HTTP/3（QUIC）才彻底解决。

## 🔗 相关笔记
[[应用层-万维网-HTTPS]]
[[TCP协议]]
[[DNS域名系统]]
[[Cookie与Session]]
[[RESTful API设计]]
[[WebSocket协议]]
[[OSI七层模型]]
[[TCP-IP四层模型]]
[[CDN内容分发网络]]
[[QUIC协议]]

🏷️: [[计算机网络]] [[HTTP]] [[应用层]] [[Web]] [[万维网]]
