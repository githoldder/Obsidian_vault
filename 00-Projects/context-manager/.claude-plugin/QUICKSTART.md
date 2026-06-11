# Context Manager 快速入门

## 5分钟上手

### 1. 启动插件

```bash
claude --plugin-dir ./context-manager
```

### 2. 创建第一个项目

```
/context-manager:init-project my-first-project "我的第一个项目"
```

### 3. 查看状态

```
/context-manager:status
```

### 4. 开始工作

```
/context-manager:resume
```

## 常用命令速查

| 命令 | 用途 |
|------|------|
| `init-project <name>` | 初始化项目 |
| `status` | 查看状态 |
| `resume` | 恢复工作 |
| `phase list` | 列出阶段 |
| `phase next` | 下一阶段 |
| `decision add` | 记录决策 |
| `issue add` | 记录问题 |
| `handoff view` | 查看交接 |

## 工作流示例

```
1. init-project
2. [工作 - 需求澄清]
3. phase next
4. [工作 - 架构设计]
5. decision add (记录决策)
6. phase next
7. [工作 - 编码实现]
8. issue add (记录问题)
9. [隔天] resume (自动接手)
```
