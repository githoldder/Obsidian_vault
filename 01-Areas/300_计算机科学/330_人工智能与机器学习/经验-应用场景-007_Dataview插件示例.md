---
type: case
status: seed
layer: E
subject: 330_人工智能与机器学习
created: 2026-09-25
updated: 2026-09-25
---

````tabs
--- 最近编辑
```dataview
TABLE file.mtime as "最后修改"
FROM ""
SORT file.mtime DESC
LIMIT 15
```

--- 任务管理

```dataview
TASK
FROM ""
GROUP BY completed
```

--- 每月新建文档数

```dataview
TABLE length(rows) as "笔记数量"

FROM ""

GROUP BY date(file.ctime).year + "-" + date(file.ctime).month

SORT rows.file.ctime DESC

```

--- 即将到期

```dataview
TABLE 
    title AS "标题",
    priority AS "优先级",
    dueDate AS "截止日期",
    (date(dueDate) - date(today)) AS "剩余天数",
    tags AS "标签"
FROM "/"
WHERE status = "进行中" AND date(dueDate) - date(today) <= 30
SORT dueDate ASC

```

````

