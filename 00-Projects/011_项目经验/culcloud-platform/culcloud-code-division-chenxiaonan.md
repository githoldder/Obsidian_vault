# CulCloud 成员代码分工说明书 - 陈晓楠 (组员)

学号：23030303  
分工职责：PDF Studio 处理面板前端交互开发，包含 PDF 页面高清图像渲染、Canvas 批注手写与文本输入、页面拖拽重排 (Pointer Reorder 算法)、页面旋转与删除状态维护；协助组长完成前端 Canvas 坐标到后端物理 PDF 页面坐标的联调测试。

## 1. 负责的前端编辑室文件与核心状态 (`file-cloud-frontend/src/views/PDFStudio.tsx`)

### 1.1 编辑室组件与页面数组管理
- **`PDFStudio()`**：PDF Studio 主视图组件。管理的核心状态包括：
  - **`WorkspacePage` 状态数组**：
    - `id` (页面唯一标识)
    - `pageNum` (原始页码，1-based)
    - `rotation` (单页旋转角度，支持 0, 90, 180, 270 度旋转)
    - `deleted` (单页是否被删除的布尔标记)
    - `imageUrl` (后端切出的高清 PNG 预览图)
  - **`annotations` 状态列表**：保存批注对象数组。

### 1.2 PDF 页面拖拽重排交互 (`Pointer Reorder`)
- 编写原生 Pointer 事件处理，模拟拖拽效果：
  - **`handlePointerDown(e, pageId)`** / **`handlePointerMove(e)`** / **`handlePointerUp()`**：监听鼠标或触控拖拽，依据页面物理边界计算拖拽位移。
  - 通过比对当前位置与相邻页面的中心坐标，动态更新 `WorkspacePage` 数组元素顺序，提供无缝的卡片位置重拍反馈。

---

## 2. 负责的 HTML5 Canvas 批注绘制与文本添加

### 2.1 Canvas 手写笔画与矩形绘制
- 挂接在底层 PDF 图片之上的 Canvas 交互控制层：
  - **`startDrawing(e)`**：捕获 Canvas 的逻辑点击点，记录起始坐标。
  - **`drawFreehand(ctx, points)`**：在画笔激活时，随 `onMouseMove` 不断捕获鼠标轨迹，绘制连续的 B样条 或折线线条。
  - **`drawRect(ctx, rect)`**：当使用矩形工具时，根据拖拽起点与当前点在 Canvas 上绘制实虚线矩形包围框。
  - **`addText(e)`**：支持点击任意位置弹出输入框，录入文本批注。

### 2.2 逻辑坐标数据结构定义 (`Annotation`)
- 负责定义批注的结构化描述：
  - **`pointsToPath(points)`**：将前端 Canvas 物理坐标集合转为相对视口百分比的归一化数组。
  - **`annotationBounds(rect)`**：计算批注矩形占整个 Canvas 逻辑宽高的 `[x_ratio, y_ratio, width_ratio, height_ratio]` 占比，确保在高分屏 (Retina) 与不同缩放尺度下批注框能精准对齐。

---

## 3. 协助组长的跨层坐标换算联调
- **物理 PDF 坐标换算 (PyMuPDF 换算)**：
  - 前端 Canvas 的坐标系随着页面缩放而动态变化，需将其百分比逻辑坐标换算为底层物理 PDF (72 DPI点阵坐标系)。
  - 陈晓楠与组长曹磊合作进行了跨端联调，通过 `processPdf(pages, outputFilename)` 传参给后端 `backend/app/routers/convert.py`。
  - 协助排查了当前端 PDF 旋转 90/180/270 度时，物理坐标的极值转换（如 `x' = width - y`）以及 STHeiti (系统黑体) 中英文混合导出时的宽体字符占位问题。
