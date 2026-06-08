---
description: 管理项目阶段，切换、完成或查看阶段详情
---

# /context-manager:phase

管理项目阶段：查看阶段列表、切换阶段、标记阶段完成。

## 用法

```
/context-manager:phase list                    # 列出所有阶段
/context-manager:phase current                 # 显示当前阶段详情
/context-manager:phase next                    # 进入下一阶段
/context-manager:phase switch [阶段ID]         # 切换到指定阶段
/context-manager:phase complete [阶段ID]       # 标记阶段完成
/context-manager:phase review [阶段ID]         # 回顾阶段成果
```

## 子命令

### list

显示所有阶段及其状态：

```
01-intake          [██████████] 100% ✓  2小时前
02a-architecture   [██████████] 100% ✓  1小时前
02b-data-model     [████░░░░░░] 40%  →  进行中
03-api-design      [░░░░░░░░░░] 0%      待开始
...
```

### current

显示当前阶段的详细信息：
- 阶段目标
- 已完成任务
- 待办事项
- 相关文件
- 交接文档

### next

完成当前阶段，进入下一阶段：

```
1. 检查当前阶段是否满足完成条件
2. 生成阶段完成报告
3. 创建 handoff-out.json
4. 更新 project-manifest.json
5. 初始化下一阶段
6. 显示新阶段的交接信息
```

### switch

切换到指定阶段（用于回顾或修改）：

```
/context-manager:phase switch 01
→ 显示阶段01的成果
→ "您想回顾还是修改？"
```

### complete

手动标记阶段完成（通常自动完成，但可手动触发）：

```
/context-manager:phase complete 02a
→ 验证阶段输出完整性
→ 生成完成报告
→ 更新状态
```

### review

回顾阶段的完整成果：

```
/context-manager:phase review 02a
→ 显示阶段摘要
→ 列出所有产出文件
→ 显示关键决策
→ 显示遇到的问题和解决方案
```
