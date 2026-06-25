# CulCloud 成员代码分工说明书 - 陈佳明 (组员)

学号：23030302  
分工职责：用户端“个人首页”与“我的文件”模块前端交互开发，包含上传进度列表、多维度类型过滤器、路径面包屑导航、文件夹浏览及移动交互；配合组长进行 React 视图状态与 CSS 样式排版优化。

## 1. 负责的前端视图文件与核心组件 (`file-cloud-frontend/src/views/`)

### 1.1 个人首页模块 (`views/Dashboard.tsx`)
- **`Dashboard()`**：用户首页组件主入口。渲染当前用户的资料概览：
  - 调用 `api.ts:getTaskStats()` 获取成功率与转换数量卡片。
  - 调用 `api.ts:getRecentLogs()` 加载操作日志列表。
  - 通过 CSS 配套展示系统的可用存储水位。

### 1.2 云盘主界面与文件交互 (`views/MyFiles.tsx`)
- **`MyFiles()`**：云盘主文件视图组件。实现以下交互方法：
  - **`browseFolder(path: string)`**：浏览文件夹。通过传入面包屑导航点击的逻辑路径，动态触发视图渲染和对子文件的获取。
  - **`moveToFolder(fileId: str, targetFolderId: str)`**：文件移动交互。拉起树形目录树弹窗，选择目标后调用 `api.ts:moveToFolder` 提交级联请求，在成功后清除原文件缓存并刷新当前视图。
  - **`filterFor(type: string)`**：类型过滤控制。在工具栏挂接文档、图片、PDF 和音频等快捷按钮，通过过滤扩展名匹配 `FileItem` 的属性。
  - **`fileIcon(ext: string)`**：扩展名图标映射。根据文件后缀名，动态将对应的 SVG 图标绑定在文件行首。
  - **`PreviewFrame`**：文件预览辅助浮层，向用户展示不同扩展名（PDF、CSV、Markdown、图片、音视频等）预览结果框架。

---

## 2. 负责的前端事件监听与拖拽实现

### 2.1 拖拽上传触发器 (`views/MyFiles.tsx` 内置逻辑)
- 封装文件拖拽释放容器：
  - **`handleDragOver(e: React.DragEvent)`**：阻止浏览器默认打开行为，动态更新拖拽高亮状态。
  - **`handleDrop(e: React.DragEvent)`**：捕获 `e.dataTransfer.files` 对象，提取文件信息并迭代调用 `api.ts:uploadFile(file, currentPrefix)`，配合进度条渲染上传历史日志。

### 2.2 路径面包屑与层级树渲染 (`components/Breadcrumbs.tsx`)
- 负责开发动态面包屑组件：
  - 监听当前浏览前缀，通过 `split('/')` 拆分出路径级联链条，支持点击任意父级节点快速进行路径回溯。

---

## 3. 配合组长开发的协作文件

### 3.1 视图与角色状态同步 (`context/useDashboard.tsx` & `App.tsx`)
- 配合组长编写 `DashboardProvider`，绑定用户角色状态：
  - 前端路由根据本地 `localStorage` 的 `role` 状态，对 `demo_user` 与 `admin` 界面展示进行动态绑定。
  - 维护侧边栏 Aside 的收起与展开，保证在用户从云盘首页切换到大数据驾驶舱时，前端能够自适应地绑定 `.theme-admin` 类的 CSS 样式以启用暗黑 ToB 大屏风格。
