---
media: https://www.bilibili.com/video/BV1Hu4y1w7tL/
---
## Dataview

![[可能是B站最简单易懂的Obsidian Dataview插件使用教程！帮你快速实现自动化筛选整理笔记！PT6M22.656S.webp]]
-  [06:22](https://www.bilibili.com/video/BV1Hu4y1w7tL/?t=382.656462#t=06:22.66) dataview插件分三个部分
### Dataview-list用法

![[可能是B站最简单易懂的Obsidian Dataview插件使用教程！帮你快速实现自动化筛选整理笔记！PT10M47.832S.webp]]
-  [10:47](https://www.bilibili.com/video/BV1Hu4y1w7tL/?t=647.831694#t=10:47.83) 

lisit使用示例
可以看到，将所有的列表都列出来了
#### Dataview-Table用法
![[可能是B站最简单易懂的Obsidian Dataview插件使用教程！帮你快速实现自动化筛选整理笔记！PT13M1.496S.webp]]
-  [13:01](https://www.bilibili.com/video/BV1Hu4y1w7tL/?t=781.495984#t=13:01.50) 
项目信息**筛选**获取
![[可能是B站最简单易懂的Obsidian Dataview插件使用教程！帮你快速实现自动化筛选整理笔记！PT16M20.294S.webp]]
-  [16:20](https://www.bilibili.com/video/BV1Hu4y1w7tL/?t=980.294292#t=16:20.29) 

![[可能是B站最简单易懂的Obsidian Dataview插件使用教程！帮你快速实现自动化筛选整理笔记！PT17M56.345S.webp]]
-  [17:56](https://www.bilibili.com/video/BV1Hu4y1w7tL/?t=1076.344792#t=17:56.34) 将列表英文改成中文
- `file.name`: 笔记文件名
- `file.path`: 笔记存储路径
- `file.size`: 笔记大小(KB)
- `file.ctime`: 创建时间
- `file.mtime`: 修改时间

### Dataview-Table用法
- `status`: 笔记状态(draft/published/archived)
- `priority`: 优先级(1-5)
- `tags`: 笔记标签
- `author`: 作者
- `rating`: 评分(1-10)

### Dataview-Tasks用法

![[可能是B站最简单易懂的Obsidian Dataview插件使用教程！帮你快速实现自动化筛选整理笔记！PT18M54.766S.webp]]
-  [18:54](https://www.bilibili.com/video/BV1Hu4y1w7tL/?t=1134.765702#t=18:54.77) Tasks追踪任务
- `due`: 截止日期
- `completed`: 完成状态
- `project`: 所属项目
- `estimated`: 预计耗时(小时)
- `actual`: 实际耗时(小时)

### Dataview-Calendar用法

![[可能是B站最简单易懂的Obsidian Dataview插件使用教程！帮你快速实现自动化筛选整理笔记！PT21M58.278S.webp]]
-  [21:58](https://www.bilibili.com/video/BV1Hu4y1w7tL/?t=1318.278412#t=21:58.28) 
- `event-date`: 事件日期
- `event-type`: 事件类型
- `location`: 地点
- `participants`: 参与人
- `reminder`: 提醒时间

## Dataview更多使用场景
![[可能是B站最简单易懂的Obsidian Dataview插件使用教程！帮你快速实现自动化筛选整理笔记！PT23M39.232S.webp]]
-  [23:39](https://www.bilibili.com/video/BV1Hu4y1w7tL/?t=1419.232494#t=23:39.23) 

## 总结

Dataview的使用需要注意的事项:
1.dataview的查询逻辑是以笔记为最小单位的
2.想要实现在笔记库的任何一个角落都能通过dataview语句查询到各种信息
只需要满足两个要求:有笔记内容,为笔记添加了元数据(或内置属性、inline字段等)，就可以使用dataview基于这些属性进行筛选
3.学了dataview之后，才会发现，原来运用datavierw最重要的是具体的使用场景
[[Areas/002_知识体系构建/元数据]]需手动添加的
[[内置属性]]笔记一旦创建系统自带的属性
[[inline字段]]可以在笔记的任意位置添加的属性,可以在写笔记时顺手添加,在后期整理时dataview会根据键值对扫描到,用于形成table列表的表头信息


