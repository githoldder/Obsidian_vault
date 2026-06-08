---
description: 生成或查看阶段交接文档
---

# /context-manager:handoff

生成阶段交接文档 (Handoff Document) 或查看现有交接信息。

## 用法

```
/context-manager:handoff create               # 为当前阶段创建交接文档
/context-manager:handoff view                 # 查看当前阶段的交接文档
/context-manager:handoff view --from [阶段]   # 查看指定阶段的交接文档
/context-manager:handoff history              # 查看所有交接历史
```

## 交接文档内容

交接文档包含：

```json
{
  "from-phase": "阶段ID",
  "to-phase": "下一阶段ID",
  "timestamp": "创建时间",
  "executive-summary": "执行摘要 (1-2句话)",
  "key-deliverables": ["关键产出列表"],
  "critical-decisions": ["关键决策列表"],
  "constraints-next-phase": ["下阶段约束"],
  "open-issues": ["待解决问题"],
  "reference-materials": ["参考资料路径"],
  "estimated-effort": "预计工作量",
  "confidence-level": "信心水平"
}
```

## 使用场景

### 场景1: 准备阶段交接

```
当前阶段工作完成
→ /context-manager:handoff create
→ 生成 handoff-out.json
→ 询问是否进入下一阶段
```

### 场景2: 接手新阶段

```
进入项目，准备开始工作
→ /context-manager:handoff view
→ 显示上一阶段的 handoff-out.json
→ 了解上下文和约束
```

### 场景3: 追溯历史

```
需要了解之前某个阶段的上下文
→ /context-manager:handoff view --from 02a
→ 显示阶段02a的交接信息
```

## 自动触发

- 阶段完成时自动创建 handoff-out.json
- 进入新阶段时自动显示 handoff-in.json
- 使用 resume 命令时自动加载相关交接文档
