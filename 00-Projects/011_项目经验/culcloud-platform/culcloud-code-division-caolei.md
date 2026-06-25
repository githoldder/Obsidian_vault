# CulCloud 成员代码分工说明书 - 曹磊 (组长)

学号：23030301  
分工职责：系统主架构、核心后端 API、领域服务、Redis/Celery 任务调度、Spark 离线分析管线、健康检查、自动化测试、管理驾驶舱核心数据联调。

## 1. 负责的后端文件与核心函数 (FastAPI)

### 1.1 系统启动与服务初始化 (`backend/app/main.py`)
- **`lifespan(app: FastAPI)`**：FastAPI 生命挂接函数，负责在服务启动时调用 `init_minio()` 初始化对象湖存储桶，调用 `init_redis()` 初始化缓存与队列，并拉起子线程 `start_health_checker()` 执行定时健康诊断。

### 1.2 文件对象接口与级联目录模拟 (`backend/app/routers/files.py` & `folders.py`)
- **`upload_file(file: UploadFile, prefix: str, relative_path: str)`**：文件上传业务。处理路径过滤 `_sanitize_path()`，调用 MinIO 客户端 API `put_object()` 将二进制文件流持久化。
- **`list_files(prefix: str, recursive: bool)`**：基于 MinIO 对象前缀检索文件列表。
- **`move_to_folder(file_id: str, target_folder_id: str)`**：目录与文件级联移动接口。在对象存储扁平架构下，通过前缀检索所有子对象，采用级联 `copy_object()` 再 `delete_object()` 的方法模拟文件夹物理移动。

### 1.3 转换服务与 PDF 处理路由 (`backend/app/routers/convert.py`)
- **`convert_existing_file(object_name: str, target_format: str)`**：云端文件格式转换。通过 Redis 写入 PENDING 状态，调用 `BackgroundTasks.add_task(async_convert_task)` 提交异步任务。
- **`extract_pdf_pages(object_name: str)`**：PDF 编辑室首屏页面提取。从 MinIO 中取出 PDF，调用 PyMuPDF 将页面切为高清 PNG，保存至预览缓存。
- **`process_pdf(pages: list, output_filename: str)`**：PDF 重组、旋转、批注合并任务路由。通过 `BackgroundTasks` 拉起异步进程完成 PDF 生成。

---

## 2. 负责的领域服务与辅助工具 (`backend/app/services/`)

### 2.1 任务队列与索引管理 (`services/task_queue.py` & `task_tracker.py`)
- **`set_task_status(task_id: str, status: str, result_url: str = None)`**：设置任务状态。将任务状态保存在 Redis 的 `task:{task_id}` 键中，设定 TTL。
- **`get_task_status(task_id: str)`**：轮询快速通道。直接读取 Redis 以保证极高的响应并发。
- **`track_task(task_id: str)`**：将任务元数据写入 Redis 的 `tasks:index` ZSET，实现高维度的任务监控数据检索。

### 2.2 Gotenberg 与本地兜底转换模块 (`services/converter.py`)
- **`DocumentConverter.convert_office_to_pdf(source_bytes: bytes, filename: str)`**：向 Gotenberg 容器服务发送 POST 转换请求。当 Gotenberg 服务降级时，拉起本地 Python 兜底模块解析 Markdown 与 CSV 格式，并输出 PDF 图像。
- **`DocumentConverter.process_pdf_pages(pages: list, source_bytes: bytes)`**：根据前端旋转角度（`rotation`）、删除页面（`deleted`）及 Canvas 批注数组，调用 PyMuPDF 生成最终 PDF 物理字节并回写 MinIO。

### 2.3 实时服务探测与监控 (`services/health_checker.py`)
- **`check_services()`**：定时探测 Redis、MinIO、Gotenberg 的 TCP 连通性并计算延迟，将健康状态快照写入 Redis 的 `health:history` ZSET 键。

---

## 3. 大数据分析与日志采集 (`scripts/spark/`)
- **`culcloud_analytics.py:read_telemetry()`**：Spark 大数据分析主程序入口。读取 HDFS/本地操作遥测日志 CSV 文件。
- **`analyze_overview()` / `analyze_format_distribution()`**：执行格式分布、操作趋势、时空错误热力图的 MapReduce 聚合计算，并将分析指标保存为本地 `spark-output/` 文件夹下的规范 JSON。
- **`redis_events_bridge.py`**：将 Redis 的实时运行日志流 `logs:timeline` 桥接写入遥测持久化文件，作为 Spark 离线计算的数据源。

---

## 4. 前端管理大屏与图表包装 (`file-cloud-frontend/src/`)
- **`views/Analytics.tsx`**：管理端大屏主组件。利用 `useBizData()` 执行实时指标轮询（`listFiles()`, `getTaskStats()`, `getSystemHealth()`）和历史 Spark 数据读取。
- **`components/EChartsWrapper.tsx`**：深度封装 ECharts。初始化及销毁 chart 实例，实现大屏暗黑模式的主题自适应及配置项刷新。

---

## 5. 端到端测试与质量控制 (`tests/e2e/specs/`)
- **`minio_integration.spec.ts`**：文件拖拽上传、文件夹创建与移动的端到端集成测试用例。
- **`pdf-page-organizer.spec.ts`**：PDF 旋转、重新排序、删除操作及导出的自动化回归用例。
- 曹磊编写了共 **11 项核心 spec 测试**，作为团队合并代码的前置门禁。
