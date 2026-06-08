---
tags:
  - 提示词
  - 物业端
---
### 题目30：社区物业系统

社区物业系统是智慧社区建设的核心平台。物业端支持公告发布和报修处理，提高服务响应速度。
业主端提供一站式社区服务，增强居住体验。系统特别整合了智能门禁、停车管理等IoT设备，实现社区服务数字化。邻里社交功能促进社区文化建设。这套系统将传统物业服务升级为智慧社区生态，既提高了管理效率，又增进了居民互动，是新型城镇化建设的重要支撑。
##### 物业端
```
1. 登录/注册：物业员工工号登录，管理小区事务。

2. 发布公告（Create）：推送停水停电等小区通知。

3. 更新公告（Update）：修改公告内容，保持信息准确。

4. 删除公告（Delete）：移除过期公告。

5. 查看报修记录（Read）：处理业主报修申请。
```
##### 业主端
```
1. 登录/注册：业主房产认证后登录。

2. 查看公告（Read）：获取小区最新动态。

3. 提交报修（Create）：上传故障描述和照片。

4. 取消报修（Delete）：问题解决后关闭报修。

5. 查看报修记录（Read）：追踪处理进度。
```

#### 1.选题\分组\分工
社区物业系统\2人小组\前端、后端
**功能分工**
- 组长:
> 物业/业主的登录/注册
> 发布公告
> 更新公告
> 删除公告
> 查看公告
- 组员：
> 提交报修
> 查看报修记录
> 取消报修

**ER图设计**
**组长**
- manager
- announcement
**组员**
- repair_request
- user

**数据库表设计、sql代码、Excel可视化呈现**

**数据库表设计**
1.**设计实体**

| 实体               | 说明               |
| ---------------- | ---------------- |
| `user`           | 用户表（物业员工 + 业主）   |
| `announcement`   | 公告表（由物业发布）       |
| `repair_request` | 报修申请表（业主提交）      |
| `property_info`  | 业主房产认证信息（业主登录前置） |
2.*设计ER图*
```

+---------------+        +---------------+
|   PropertyInfo|<------ |    User       |
|---------------|        |---------------|
| id (PK)       |        | id (PK)       |
| name          |        | username      |
| address       |        | password      |
| contact_phone |        | role          |
+---------------+        | phone         |
                         | community_id FK
                         +---------------+
                               ^
                               |
               +---------------+---------------+
               |                               |
        +---------------+              +---------------+
        | Announcement   |              | RepairRequest  |
        |--------------- |              |--------------- |
        | id (PK)        |              | id (PK)        |
        | title          |              | description    |
        | content        |              | photo_url      |
        | created_at     |              | created_at     |
        | updated_at     |              | status         |
        | staff_id (FK)  |              | owner_id (FK)  |
        +---------------+              | staff_id (FK)  |
                                       +---------------+

```
***
**表设计**
1. `User`用户表

| 字段       | 类型                      | 描述        |
| -------- | ----------------------- | --------- |
| id       | INT, PK, AUTO_INCREMENT | 主键        |
| username | VARCHAR(50)             | 用户名       |
| password | VARCHAR(100)            | 密码        |
| role     | ENUM('owner','staff')   | 身份（业主/物业） |
2. `announcement`公告表

|字段|类型|描述|
|---|---|---|
|id|INT, PK|主键|
|title|VARCHAR(100)|公告标题|
|content|TEXT|公告内容|
|created_at|DATETIME|发布时间|
|user_id|INT, FK → user(id)|发布人（物业）|
3. `repair_request`报修表

|字段|类型|描述|
|---|---|---|
|id|INT, PK|主键|
|user_id|INT, FK|报修人|
|description|TEXT|报修描述|
|photo_path|VARCHAR(255)|图片路径|
|status|ENUM('未处理', '处理中', '已解决', '已取消')|状态|
|created_at|DATETIME|提交时间|
4. `property_info`房产认证表

| 字段          | 类型               | 描述      |
| ----------- | ---------------- | ------- |
| user_id     | INT, PK, FK      | 业主用户 ID |
| building    | VARCHAR(50)      | 楼栋      |
| unit        | VARCHAR(50)      | 单元      |
| room_number | VARCHAR(50)      | 房号      |
| verified    | ENUM('yes','no') | 是否已认证   |

sql代码
```
-- 创建数据库
CREATE DATABASE IF NOT EXISTS community_system;
USE community_system;

-- 物业信息表
CREATE TABLE PropertyInfo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    contact_phone VARCHAR(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户表
CREATE TABLE User (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('owner', 'staff') NOT NULL,
    phone VARCHAR(20),
    community_id INT,
    FOREIGN KEY (community_id) REFERENCES PropertyInfo(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 公告表
CREATE TABLE Announcement (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    staff_id INT,
    FOREIGN KEY (staff_id) REFERENCES User(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 报修单表
CREATE TABLE RepairRequest (
    id INT AUTO_INCREMENT PRIMARY KEY,
    description TEXT NOT NULL,
    photo_url VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'in_progress', 'completed') DEFAULT 'pending',
    owner_id INT,
    staff_id INT,
    FOREIGN KEY (owner_id) REFERENCES User(id),
    FOREIGN KEY (staff_id) REFERENCES User(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```


#### 5.(拖拉拽)设计原型图
**小组分工**：

#### 待完成任务

- **前端原型设计**：
    - 完成登录/注册、公告管理、报修管理的 FXML 界面。
    - 实现界面跳转（按钮点击弹出新窗口）。
    - 确保界面美观且功能完整。
- **后端业务逻辑**：
    - 实现登录/注册、公告 CRUD、报修 CRUD 的 Java 代码。
    - 连接数据库，处理前端与后端的交互逻辑。
    - 确保功能模块与数据库表一一对应。
#### 组员任务概述

- **前端（FXML 设计）**：
    - 设计报修提交界面（submitRepair.fxml）。
    - 设计报修记录查看界面（viewRepairs.fxml）。
    - 设计报修取消功能（集成在查看界面）。
- **后端（Java 业务逻辑）**：
    - 实现报修提交功能（插入 RepairRequest 表）。
    - 实现报修记录查看功能（查询 RepairRequest 表）。
    - 实现报修取消功能（更新 RepairRequest 表的 status）。
    - 连接数据库，确保数据交互正确。
    
```
windows-stage（空白脸） Scene场景（面具）
创建界面
写fxml
- 标签
- 按钮
- 输入框
- 超链接
- 菜单栏
与controller文件建立连接
点击button按钮可以跳转到对应的界面（即弹出窗口）
登录界面
可以打开超链接（新用户注册）
点击注册可以关闭当前窗口（hide（）；）
```


#### 6.编程实现
**用户注册**
密码设计：使用正则表达式，限制必须使用字母+数字+特殊字符
1.创建对应的实体类
2.对照数据库写完整属性
3.快捷键生成get/set方法
4.toString方法

编程逻辑先后顺序：
1. fxml
2. controller
3. 写实体类
4. DAO类 SQL
5. Services类 业务
6. 完善controller类

非常好，现在我们继续完成业主端，第一步就是创建业主端的页面，要求和物业员工端类似，在业主登录后跳转到业主登录界面，页面包括设置身份信息（即默认头像owner.png、用户名、身份）、报修管理、查看公告、注销等元素；同样点击报修管理之后能够触发事件，进入报修管理界面，而在报修管理界面里包括：”3.提交报修（Create）：上传故障描述和照片。

4. 取消报修（Delete）：问题解决后关闭报修，即删除数据库中这条报修记录信息。

5. 查看报修记录（Read）：追踪处理进度，在该功能界面还能实现搜索（可以在所有的报修记录数据内进行搜索查找（包括模糊查询和精确查询））。“等功能模块，其中，查找时基于数据库的特性，能够对一些基本的报修种类进行处理（即设计下拉框，可以让用户选择数据库里已经有的种类）；另外，还有查看公告的功能，查看公告（可以在所有的报修记录数据内进行搜索查找（包括模糊查询和精确查询））等；
在报修管理的界面实现点击某一条报修记录的信息可以选择编辑、修改的操作；在上方的搜索下拉框可以选择种类”已处理“对应‘completed’,"处理中"对应‘in_progress’,"申请中"对应‘pending’.

另外在staff端，查看报修记录的功能还未完全实现，点击查看报修记录的按钮未显示相应的窗口，希望实现的是像owner的查看公告的窗口类似的设计，只不过员工用户可以通过点击某一条记录进行操作（受理报修/报修已处理）

