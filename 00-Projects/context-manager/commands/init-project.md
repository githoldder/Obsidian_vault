---
description: 初始化一个新的上下文管理项目，创建标准目录结构
---

# /context-manager:init-project

初始化一个新的 Context Manager 项目，创建标准目录结构和元数据文件。

## 用法

```
/context-manager:init-project [项目名] [描述]
```

## 参数

- `项目名` (可选): 项目名称，将用作目录名。如不提供，将提示输入。
- `描述` (可选): 项目简要描述。

## 功能

1. 创建 `.[项目名]/` 目录结构
2. 生成 `project-manifest.json`
3. 初始化 `context-history.json`
4. 创建所有阶段目录
5. 生成 README.md 模板

## 示例

```
/context-manager:init-project 电商平台 "一个B2C电商平台，包含用户、商品、订单模块"
```

## 输出

```
✓ 创建目录结构: .ecommerce-platform/
✓ 生成: project-manifest.json
✓ 生成: context-history.json
✓ 创建阶段目录: 01-intake/, 02-design/, ...
✓ 生成: README.md

项目已初始化。使用 /context-manager:status 查看状态。
```

## 执行逻辑

1. 验证项目名合法（只允许字母、数字、连字符、下划线）
2. 检查目录是否已存在，避免覆盖
3. 创建所有标准目录
4. 生成初始元数据文件
5. 记录初始化事件到 context-history
