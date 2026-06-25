# CulCloud 代码架构与现场说明认知树

更新时间：2026-06-24

本文用于后续接手、现场说明和正文第 4--6 章写作。内容基于当前仓库真实代码结构，不把未实现能力写成已实现能力。

## 1. 顶层架构

```text
CulCloud 云端资料处理平台
├── 用户端 React 应用
│   ├── 文件上传、目录整理、文件预览
│   ├── 格式转换任务提交与结果下载
│   └── PDF 页面整理、批注与导出
├── 管理端 React 应用
│   ├── 第一屏：FastAPI 实时运行口径
│   ├── 第二屏：Spark 历史文件样本与历史转换任务
│   └── 第三屏：历史遥测质量、存储水位、错误热力图
├── FastAPI 业务服务
│   ├── /api/v1/files       文件对象接口
│   ├── /api/v1/folders     目录模拟接口
│   ├── /api/v1/preview     预览接口
│   ├── /api/v1/convert     转换与 PDF 编辑接口
│   ├── /api/v1/tasks       任务状态接口
│   ├── /api/v1/logs        事件日志接口
│   ├── /api/v1/system      服务健康接口
│   └── /api/v1/auth        用户端/管理端角色接口
├── 基础运行依赖
│   ├── MinIO：文件对象、预览缓存、转换产物
│   ├── Redis：任务状态、任务索引、事件日志、健康快照
│   └── Gotenberg：Office/PDF 转换渲染能力
└── 历史分析链路
    ├── Spark 脚本读取遥测样本并输出 JSON 聚合结果
    ├── Flask Analytics 暴露 /api/analytics/* 接口
    └── 管理端 ECharts 读取历史分析样本
```

## 2. 技术选型与作用

| 技术 | 代码位置 | 当前作用 |
| --- | --- | --- |
| React + Vite + TypeScript | `file-cloud-frontend/src/` | 用户端与管理端单页应用，视图状态和 API 调用集中在前端工程。 |
| FastAPI | `backend/app/main.py`、`backend/app/routers/` | 主业务 API，负责文件、目录、预览、转换、任务、日志和健康检查。 |
| MinIO | `backend/app/services/minio_client.py` | S3 风格对象存储，保存用户文件、预览缓存和转换结果。 |
| Redis | `backend/app/services/task_queue.py`、`task_tracker.py`、`log_collector.py` | 保存任务短状态、任务索引、时间线、事件日志和健康快照。 |
| Gotenberg | `backend/app/services/converter.py` | 处理 Office 到 PDF 等高保真转换，失败时部分转换使用本地库兜底。 |
| PyMuPDF | `backend/app/services/converter.py` | PDF 页面图像提取、PDF 页面处理、批注导出和部分兜底转换。 |
| Flask | `flask-analytics/app.py` | 对 Spark 输出的历史分析 JSON 提供 HTTP 接口。 |
| Spark | `scripts/spark/culcloud_analytics.py` | 对遥测样本做格式分布、转换统计、趋势、质量、错误热力图等聚合。 |
| ECharts | `file-cloud-frontend/src/components/EChartsWrapper.tsx` | 管理驾驶舱图表渲染。 |

## 3. 前端代码树

```text
file-cloud-frontend/src
├── App.tsx
│   └── 负责用户端/管理端路由与视图装配
├── context/useDashboard.tsx
│   ├── DashboardProvider()
│   ├── useDashboard()
│   └── 管理 role/view，本地缓存用户端或管理端当前视图
├── services/api.ts
│   ├── listFiles() / uploadFile() / deleteFile()
│   ├── createFolder() / browseFolder() / moveToFolder()
│   ├── getPreviewMetadata() / getPreviewContentUrl()
│   ├── getConversionCapabilities()
│   ├── convertFile() / convertExistingFile()
│   ├── extractPdfPages() / processPdf()
│   ├── getTaskStatus() / listTasks() / getTaskStats()
│   ├── getRecentLogs() / getLogTimeline() / getLogStats()
│   └── getSystemHealth() / fetchRole() / setServerRole()
├── views/Dashboard.tsx
│   └── 用户首页，展示用户侧概览、近期任务、存储等真实指标
├── views/MyFiles.tsx
│   ├── MyFiles()
│   ├── PreviewFrame()
│   ├── extensionOf() / filterFor() / fileIcon()
│   └── 文件列表、上传、目录切换、搜索、类型筛选、移动和预览入口
├── views/ConvertCenter.tsx
│   ├── P0_WHITELIST
│   └── ConvertCenter()
│       ├── 本地文件或云端文件选择
│       ├── 自动识别可转换目标
│       ├── 提交 convertFile()/convertExistingFile()
│       └── 轮询 getTaskStatus() 展示完成或失败
├── views/PDFStudio.tsx
│   ├── WorkspacePage
│   ├── Annotation
│   ├── PDFStudio()
│   ├── pointsToPath()
│   └── annotationBounds()
│       ├── 选择 PDF
│       ├── extractPdfPages() 获取页面图像
│       ├── 页面排序、旋转、删除和批注
│       └── processPdf() 导出新 PDF
├── views/TaskMonitor.tsx
│   └── 轮询任务列表、队列长度、失败任务和状态分布
├── views/SystemStatus.tsx
│   └── 服务健康、事件日志与相对时间展示
├── views/Analytics.tsx
│   ├── useBizData()
│   └── Analytics()
│       ├── 第一屏读取 FastAPI files/tasks/logs/system 实时数据
│       └── 第二/三屏读取 Flask/Spark 历史分析样本
└── views/analytics/
    ├── AnalyticsSlides.tsx
    │   ├── SlideOverview()
    │   ├── SlideProcessing()
    │   ├── SlideQuality()
    │   └── buildTopologyGraphic()
    ├── AnalyticsTopbar.tsx
    └── AnalyticsModals.tsx
```

## 4. FastAPI 后端代码树

```text
backend/app
├── main.py
│   ├── lifespan()
│   │   ├── init_minio()
│   │   ├── init_redis()
│   │   └── start_health_checker()
│   ├── app = FastAPI(...)
│   └── include_router(...)
├── routers/auth.py
│   ├── get_me()
│   ├── set_role()
│   └── get_current_role()
├── routers/files.py
│   ├── upload_file()
│   ├── list_files()
│   ├── search_files()
│   ├── get_download_url()
│   ├── download_file_content()
│   ├── stat_file()
│   ├── rename_file()
│   ├── share_file()
│   ├── batch_delete_files()
│   └── delete_file()
├── routers/folders.py
│   ├── create_folder()
│   ├── browse_root()
│   ├── browse_folder()
│   ├── delete_folder()
│   ├── rename_folder()
│   └── move_to_folder()
├── routers/preview.py
│   ├── preview_metadata()
│   ├── preview_content()
│   └── clear_preview_cache()
├── routers/convert.py
│   ├── conversion_capabilities()
│   ├── convert_file()
│   ├── convert_existing_file()
│   ├── async_convert_task()
│   ├── extract_pdf_pages()
│   ├── process_pdf()
│   ├── async_pdf_process_task()
│   ├── conversion_preview()
│   └── export_preview()
├── routers/tasks.py
│   ├── queue_length()
│   ├── task_stats()
│   ├── cluster_overview()
│   ├── recent_failures()
│   ├── get_task_by_id()
│   └── get_task_list()
├── routers/logs.py
│   ├── logs_recent()
│   ├── logs_timeline()
│   └── logs_stats()
├── routers/system.py
│   ├── system_health()
│   └── service_health_history()
└── services/
    ├── minio_client.py
    ├── converter.py
    ├── preview.py
    ├── conversion_capabilities.py
    ├── task_queue.py
    ├── task_tracker.py
    ├── log_collector.py
    └── health_checker.py
```

## 5. 核心业务流

### 5.1 文件上传与预览

```text
MyFiles.tsx
└── uploadFile(file, prefix, relativePath)
    └── POST /api/v1/files/upload
        └── files.upload_file()
            ├── _sanitize_path()
            ├── MinIO put_object()
            └── log_event(FILE_UPLOADED)

MyFiles.tsx
└── getPreviewMetadata(objectName)
    └── GET /api/v1/preview/{object_name}
        └── preview.preview_metadata()
            └── services.preview.get_preview_metadata()
                ├── 判断 preview_type
                ├── Office 类文件生成 previews/{hash}.pdf
                └── 返回 content_url
```

### 5.2 格式转换状态流

```text
ConvertCenter.tsx
└── convertExistingFile(objectName, targetFormat)
    └── POST /api/v1/convert/existing
        └── convert.convert_existing_file()
            ├── _assert_p0_whitelist()
            ├── 从 MinIO 读取源文件
            ├── set_task_status(PENDING)
            └── BackgroundTasks.add_task(async_convert_task)
                ├── set_task_status(PROCESSING)
                ├── log_event(CONVERSION_STARTED)
                ├── DocumentConverter 执行转换
                ├── MinIO put_object(conversions/{task_id}_*)
                ├── set_task_status(SUCCESS, result_url)
                └── log_event(CONVERSION_COMPLETED)

ConvertCenter.tsx
└── getTaskStatus(taskId)
    └── GET /api/v1/tasks/{task_id}
        └── Redis task:{id} / tasks:index
```

任务状态主线：

```text
pending -> processing -> success
pending -> processing -> failed
```

### 5.3 PDF 页面整理与导出

```text
PDFStudio.tsx
└── extractPdfPages(objectName)
    └── POST /api/v1/convert/pdf/extract-pages
        └── convert.extract_pdf_pages()
            ├── MinIO 读取源 PDF
            ├── DocumentConverter.pdf_to_images(dpi=120)
            ├── MinIO 写入 previews/{preview_id}/page_N.png
            └── 返回 pages[{page_num,url,width,height}]

PDFStudio.tsx
└── processPdf(pages, outputFilename)
    └── POST /api/v1/convert/pdf/process
        └── convert.process_pdf()
            └── BackgroundTasks.add_task(async_pdf_process_task)
                ├── set_task_status(PROCESSING)
                ├── DocumentConverter.process_pdf_pages()
                ├── MinIO 写入 conversions/{task_id}_{filename}.pdf
                ├── set_task_status(SUCCESS, result_url)
                └── log_event(PDF_REORDER_COMPLETED)
```

### 5.4 管理驾驶舱数据流

第一屏实时运行口径：

```text
Analytics.tsx useBizData()
├── GET /api/v1/files?recursive=true&limit=200
├── GET /api/v1/tasks/stats
├── GET /api/v1/tasks/queue-length
├── GET /api/v1/logs/timeline?hours=24
├── GET /api/v1/logs/stats?hours=24
└── GET /api/v1/system/health
```

第二/三屏历史分析口径：

```text
scripts/spark/culcloud_analytics.py
├── read_telemetry()
├── analyze_overview()
├── analyze_format_distribution()
├── analyze_conversion_stats()
├── analyze_traffic_trend()
├── analyze_quality_report()
└── analyze_error_heatmap()
    └── data/spark-output/*.json
        └── flask-analytics/app.py /api/analytics/*
            └── AnalyticsSlides.tsx 第二/三屏 ECharts
```

## 6. 关键数据结构

| 数据对象 | 存放位置 | 说明 |
| --- | --- | --- |
| 用户原始文件 | MinIO `culcloud-bucket` | 上传文件以对象名保存，目录通过前缀模拟。 |
| 目录标记 | MinIO `path/.keep` | 空对象表达空目录或目录存在性。 |
| 预览缓存 | MinIO `previews/*` | Office 预览 PDF、PDF 页面图像等缓存。 |
| 转换产物 | MinIO `conversions/*` | 转换完成或 PDF 导出的结果文件。 |
| 任务短状态 | Redis `task:{task_id}` | 供轮询接口快速读取当前状态。 |
| 任务索引 | Redis `tasks:index`、`tasks:*` | 供管理端列表、统计和队列长度使用。 |
| 事件日志 | Redis `logs:recent`、`logs:timeline` | 供运行面板和管理端事件流使用。 |
| 健康快照 | Redis `health:*` | 由健康检查循环写入。 |
| 历史分析结果 | `data/spark-output/*.json` | Spark 离线聚合结果。 |

## 7. 已实现、弱覆盖与未实现边界

已实现：

- 用户端与管理端角色切换和视图隔离。
- 文件上传、列表、搜索、下载、删除、重命名、分享、批量删除。
- 目录创建、浏览、删除、重命名、文件移动。
- 图片、PDF、音频、视频、文本、Markdown、CSV、Office 预览。
- P0 白名单内的文档/图片/PDF 相关格式转换。
- 转换任务状态轮询、任务列表、队列长度、近期失败。
- PDF 页面提取、排序、旋转、删除、批注、导出。
- 操作日志、任务统计、系统健康检查。
- 管理端第一屏实时运行口径，以及第二/三屏 Spark 历史分析样本。

弱覆盖：

- 鉴权目前偏演练/角色切换性质，未形成生产级用户体系。
- 大文件上传没有分片续传。
- 转换任务使用 FastAPI BackgroundTasks 和 Redis 状态，未完全切换到生产级分布式任务调度。
- PDF 批注支持轻量工具，未达到专业 PDF 编辑器的全部对象编辑能力。
- Spark 分析以样本和 Redis 增量桥接为主，未形成持续生产级数据湖。

未实现：

- 多租户权限隔离和细粒度 ACL。
- 文件全文检索、版本管理和协同编辑。
- 在线支付、商业化账户和配额计费。
- 灾备、多节点对象存储和生产级监控告警闭环。

## 8. 第 4--6 章正文映射建议

| 报告章节 | 应引用代码/图 | 说明重点 |
| --- | --- | --- |
| 第 4 章系统总体设计 | `fig08`--`fig14` UML PDF、`main.py`、routers/services 总览 | 讲清系统分层、流程、数据结构和部署关系。 |
| 第 5 章详细设计及实现 | `fig15`、前端 views、`services/api.ts`、后端 routers/services、界面结果图 | 讲清每个模块的 API、函数和状态流。 |
| 第 6 章系统测试 | `figures-png/tests/*`、测试命令、任务统计 API、运行结果图 | 讲清可复现测试、业务闭环和修复问题。 |

