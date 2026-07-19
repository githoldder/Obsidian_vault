# 知识管理与效率工具工作流

---

## 1. 适用场景

用于管理个人知识库、项目目录、CLI 工具链、Git 版本管理、Playwright/脚本自动化、Skill 蒸馏和 Agent 长任务运行。

---

## 2. 总链路

```text
信息/项目进入
-> PARA 分类
-> 就近归档
-> CLI 化执行
-> Git 留痕
-> 重复任务脚本化
-> Skill 蒸馏
-> Harness 长任务管理
```

---

## 3. Skill 串联

| 阶段 | 调用文件 |
|---|---|
| 文件夹与知识库 | [[51-PARA-Management]] |
| CLI 降 token | [[52-CLI-Token-Saving]] |
| Git 版本管理 | [[53-Git-Version-SOP]] |
| Skill 蒸馏 | [[54-Skill-Distillation]] |
| Agent 长任务 | [[55-Harness-Engineering]] |

---

## 4. 使用规则

1. 能脚本化的重复任务，不反复让 AI 自由推理。
2. 能用 CLI 输出的，不用 GUI 截图喂给 AI。
3. 能沉淀成 skill 的，不让经验留在聊天记录里。
4. 能用 Git checkpoint 的，不靠记忆回滚。
5. 长任务必须有 context、checkpoint、resume 机制。
