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

==//目前项目进行到了这里==

#### (拖拉拽)设计原型图
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

