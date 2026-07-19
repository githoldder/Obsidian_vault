# 软件工程文档交付工作流

---

## 1. 适用场景

当项目原型已经跑通，需要按照软件工程生命周期输出正式文档：SRS、概要设计、详细设计、数据库设计、接口文档、测试文档、用户手册、部署文档、Sprint 记录和最终归档。

---

## 2. 总链路

```text
PRD / 原型
-> SRS
-> 架构设计 HLD
-> 详细设计 LLD
-> 数据库设计 DBD
-> 接口文档 API
-> UI 设计说明
-> 测试计划/测试报告
-> 用户手册/部署文档
-> Sprint checkpoint
-> 质量审查
-> 归档
```

---

## 3. 调用顺序

| 阶段     | 调用文件                                                             |
| ------ | ---------------------------------------------------------------- |
| 文档总规范  | [[21-Doc-Engineering-Standard]]                                  |
| 生命周期路线 | [[42-BigTech-Agile-Spec]]                                        |
| 需求来源   | [[11-Req-Interview-SOP]]                                         |
| 目标拆解   | [[44-OKRTS-5W-Framework]]                                        |
| 项目结构   | [[41-Project-Three-Layers]]                                      |
| 质量审查   | `skills_library/04_国家标准文档工程化写作规范/doc-quality-audit-checklist.md` |

---

## 4. 最小文档包

课程项目或 MVP 原型建议：

1. `01-PRD.md`
2. `02-SRS.md`
3. `03-HLD.md`
4. `04-LLD.md`
5. `05-DBD.md`
6. `06-API.md`
7. `07-TestPlan.md`
8. `08-TestReport.md`
9. `09-UserManual.md`
10. `10-Deploy.md`

---

## 5. Sprint 结束记录模板

```md
[Issue] | None | [Status] Completed Sprint XX successfully

## 1. 学术/工程规范修复
- 修复：
- 对齐：

## 2. 文档工件产出
- 新增：
- 修改：
- 解耦：

## 3. 验证结果
- 编译/测试：
- 页数/格式：
- 风险：

## 4. 环境与版本归档
- task.md：
- walkthrough.md：
- context/context.txt：
- commit：
```
