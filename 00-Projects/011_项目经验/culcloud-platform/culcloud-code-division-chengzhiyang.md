# CulCloud 成员代码分工说明书 - 程志阳 (组员)

学号：23030304  
分工职责：转换中心 (ConvertCenter) 交互开发与接口联调，负责目标格式自适应匹配、异步转换任务提交、基于定时器的任务状态轮询与生命周期控制；协助组长开发 Flask 离线分析接口，处理驾驶舱历史分析数据的读取。

## 1. 负责的前端转换中心文件与核心函数 (`file-cloud-frontend/src/views/PDFStudio.tsx` & `ConvertCenter.tsx`)

### 1.1 格式选择与能力白名单自适应 (`views/ConvertCenter.tsx`)
- **`ConvertCenter()`**：转换中心主组件。实现云端已有文件或本地文件的拖拽上传转换。
- **`getConversionCapabilities(ext: string)`**：能力矩阵筛选。通过输入文件扩展名（如 `.docx`, `.xlsx`, `.md`），调用 `api.ts` 的 `getConversionCapabilities()`，在前端界面动态限制可转目标白名单（例如限制 Docx 只能选择 PDF 或 TXT 等 P0 白名单目标），避免触发不支持的转换。

### 1.2 异步状态轮询与生命周期维护 (`views/ConvertCenter.tsx`)
- 编写任务提交与状态轮询控制逻辑：
  - **`submitConversionTask()`**：提交文件转换请求。调用 `api.ts:convertFile()`（本地上传文件转换）或 `api.ts:convertExistingFile()`（云端存储文件直接转换），获取新任务的唯一 `taskId`。
  - **`startPollingTask(taskId: string)`**：状态轮询控制。
    - 在组件内部创建 `setInterval` 轮询计时器，每隔 1000ms 调用 `api.ts:getTaskStatus(taskId)` 查询 Redis 缓存。
    - **终止条件逻辑**：当接口返回状态为 `success` 时，从响应体提取 `result_url` 展示最终下载链接，并自动清除轮询定时器；若状态返回 `failed` 且带有 error message，则在浮层中呈现红色失败警告，同时停止定时器，防止死循环请求压挂后端。

---

## 2. 协助编写的轻量后端分析服务 (`flask-analytics/`)

### 2.1 历史遥测分析接口 (`flask-analytics/app.py`)
- 协助组长在独立的 Flask 分析微服务中编写遥测读取路由（该服务读取 Spark 分布式离线计算生成在 `data/spark-output/` 的结果 JSON 文件）：
  - **`get_analytics_overview()`**：对应 `GET /api/analytics/overview` 接口。从本地文件或 HDFS 读取系统总体任务与存储量统计。
  - **`get_traffic_trend()`**：对应 `GET /api/analytics/traffic-trend` 接口。读取流量变动曲线，返回管理大屏绘制。
  - **`get_error_heatmap()`**：对应 `GET /api/analytics/error-heatmap` 接口。提供 24×7 错误分布热力图的数据支撑。
  - **`get_quality_report()`**：对应 `GET /api/analytics/quality-report` 接口。返回包括完整性、时效性在内的大数据清洗多维评估雷达图指标。
- Flask 接口均配置了 CORS，以允许前端管理端大屏（`views/Analytics.tsx`）进行异步跨域获取（管理驾驶舱的第二、三屏可视化直链分析接口）。
