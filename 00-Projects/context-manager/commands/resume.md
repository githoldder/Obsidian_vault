---
description: 从之前的会话恢复工作，显示交接文档并继续当前阶段
---

# /context-manager:resume

从之前的会话恢复工作，自动加载上下文，显示交接文档，让用户无缝继续。

## 用法

```
/context-manager:resume
/context-manager:resume --phase [阶段ID]
/context-manager:resume --point [检查点ID]
```

## 参数

- `--phase [阶段ID]`: 恢复到指定阶段（如 `02a`, `03`）
- `--point [检查点ID]`: 恢复到指定检查点
- `--list`: 列出所有可用的恢复点

## 功能

1. 扫描项目目录，读取元数据
2. 生成 Executive Summary
3. 显示当前阶段的 Handoff Document
4. 列出待办事项
5. 询问用户切入点

## 恢复流程

```
1. 读取 project-manifest.json
2. 读取 context-history.json (最近10条)
3. 生成状态摘要
4. 显示当前阶段的交接信息
5. 列出可执行的操作选项
6. 根据用户选择启动相应 Agent
```

## 示例

```
/context-manager:resume
→ 显示当前状态摘要
→ "您想继续哪个方面的工作？"

/context-manager:resume --phase 02a
→ 显示阶段02a的交接文档
→ "准备进入架构设计回顾"

/context-manager:resume --list
→ 显示所有检查点列表
→ 用户选择后恢复到该点
```

## 自动触发

当用户在没有上下文的情况下进入项目目录时，自动调用：

```
[检测到项目目录]
正在恢复上下文...
[显示 Executive Summary]
```
