# Vue2 项目 QuickStart（苍穹外卖前端）

## 1️⃣ 推荐环境（最重要）

|工具|推荐版本|说明|
|---|---|---|
|Node|**16.20.x**|Vue CLI 3 / Webpack4 最稳定|
|npm|**8.x**|避免 npm10 的依赖冲突|
|Vue|2.6.10|项目原始版本|
|OS|macOS / Linux|都可以|

检查环境：

```bash
node -v
npm -v
```

---

# 2️⃣ 第一次启动流程

进入项目目录：

```bash
cd project-sky-admin-vue-ts
```

删除旧依赖：

```bash
rm -rf node_modules package-lock.json
```

安装依赖（Vue2 项目必须）：

```bash
npm install --legacy-peer-deps
```

启动项目：

```bash
npm run serve
```

访问：

```
http://localhost:8080
```

---

# 3️⃣ 必须避免的坑

|问题|原因|解决|
|---|---|---|
|`ERESOLVE dependency tree`|npm7+严格依赖检查|`npm install --legacy-peer-deps`|
|`coroutine.cc thread_id_key`|fibers 与 Node16+不兼容|删除 `fibers`|
|项目直接 abort|node-sass / fibers|使用 `sass`|
|`eslint dependency conflict`|eslint 版本旧|legacy-peer-deps|

---

# 4️⃣ 项目必须修改的依赖（已修复）

删除：

```json
"fibers": "^4.0.2"
```

原因：

```
fibers 在 Node16+ 已停止维护
```

否则会报：

```
Assertion failed: coroutine.cc
```

---

# 5️⃣ 推荐 Node 管理方式

安装 nvm：

```bash
brew install nvm
```

使用 Node16：

```bash
nvm install 16
nvm use 16
```

以后进入项目可以：

```bash
nvm use 16
```

---

# 6️⃣ 日常开发命令

启动：

```bash
npm run serve
```

打包：

```bash
npm run build
```

代码检查：

```bash
npm run lint
```

---

# 7️⃣ 项目结构快速理解

```
src
 ├─ api          接口请求
 ├─ views        页面
 ├─ components   公共组件
 ├─ router       路由
 ├─ store        vuex
 ├─ utils        工具函数
```

技术栈：

```
Vue2
TypeScript
ElementUI
Vuex
Vue Router
Axios
ECharts
```

---

# 8️⃣ 如果项目启动失败（排查顺序）

① 检查 Node：

```bash
node -v
```

必须：

```
16.x
```

② 删除依赖：

```bash
rm -rf node_modules package-lock.json
```

③ 重新安装：

```bash
npm install --legacy-peer-deps
```

④ 启动：

```bash
npm run serve
```

---

✅ **一句话经验总结**

> Vue2 老项目启动三件套：

```
Node16
npm install --legacy-peer-deps
删除 fibers
```

基本 **100%能跑起来**。

---

如果你愿意，我还可以给你做一个 **《Vue2 老项目通用启动模板》（很多公司旧项目都适用）**，以后遇到类似项目 **3分钟就能跑起来**。