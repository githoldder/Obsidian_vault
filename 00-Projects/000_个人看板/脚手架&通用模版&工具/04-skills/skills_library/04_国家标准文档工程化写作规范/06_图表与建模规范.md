# 图表与建模规范

> **版本：** v2.0（全面升级版）
> **维护者：** 文档管理 Skill
> **适用范围：** 软件工程文档、毕业设计论文、项目技术文档、工程管理文档
> **UML 版本：** UML 2.5 / Mermaid 10+
> **最后更新：** 2026-05-12

---

## 摘要速查

| 图表类型 | 推荐工具 | 输出格式 | 编号前缀 |
|---|---|---|---|
| 流程图 | Mermaid / draw.io | SVG | 图 |
| 活动图 | Mermaid / PlantUML | SVG | 图 |
| 状态图 | Mermaid / PlantUML | SVG | 图 |
| 时序图 | Mermaid / PlantUML | SVG | 图 |
| 用例图 | Mermaid / PlantUML | SVG | 图 |
| 类图 | PlantUML / draw.io | SVG | 图 |
| 组件图 | PlantUML / draw.io | SVG | 图 |
| 部署图 | PlantUML / draw.io | SVG | 图 |
| E-R 图 | Mermaid / draw.io | SVG | 图 |
| 数据流图 | draw.io / Visio | SVG/PNG | 图 |
| 架构图 | draw.io / Excalidraw | SVG | 图 |
| 甘特图 | Mermaid | SVG | 图 |
| 思维导图 | XMind / Obsidian | PNG | 图 |
| UI 流程图 | Figma / Axure | PNG | 图 |
| 原型图 | Figma / Axure | PNG/PDF | 图 |
| 数据可视化 | Python / ECharts | PNG≥300dpi | 图 |

---

## 一、图表类型分类与适用场景

### 1.1 流程图（Flowchart）

| 项目 | 说明 |
|---|---|
| **适用场景** | 业务流程、算法步骤、数据处理步骤、决策树；仅描述"做什么"的线性或分支逻辑 |
| **不适用场景** | 并发行为、时序依赖（用活动图）、状态对象（用状态图）、多对象交互（用时序图） |
| **推荐工具** | Mermaid（代码优先）、draw.io（可视化编辑）、Visio（复杂流程） |
| **输出格式** | SVG（优先） > PNG ≥ 300 DPI |

**Mermaid 示例：**
````mermaid
flowchart TD
    A([开始]) --> B[输入参数]
    B --> C{参数校验}
    C -->|校验通过| D[查询数据库]
    C -->|校验失败| E[记录错误日志]
    E --> F([异常结束])
    D --> G{结果为空?}
    G -->|是| H[返回空列表]
    G -->|否| I[遍历处理]
    I --> J[汇总统计]
    J --> K([正常结束])
    H --> K
````

> **绘图要点：** 圆角矩形 = 开始/结束，矩形 = 处理步骤，菱形 = 条件判断，平行四边形 = 输入/输出；箭头必须标注条件。

---

### 1.2 活动图（Activity Diagram）

| 项目 | 说明 |
|---|---|
| **适用场景** | 复杂业务流程（含分支/循环/并发泳道）、用例行为细化、跨角色协作流程 |
| **不适用场景** | 简单线性顺序流程（直接用流程图）；精确时序关系（用时序图）；静态结构描述（用类图） |
| **推荐工具** | Mermaid（泳道语法强）、PlantUML（UML 语义完整） |
| **输出格式** | SVG（优先） |

**Mermaid 示例（泳道活动图）：**
````mermaid
activityDiagram
    partition 客户端 {
        :用户填写订单信息;
        :点击提交按钮;
    }
    partition 服务端 {
        :接收订单请求;
        :参数校验;
        if (校验通过?) then (是)
            :锁定库存;
            :创建订单记录;
            :调用支付接口;
            if (支付成功?) then (是)
                :更新订单状态;
                :发送通知;
            else (否)
                :回滚库存;
                :记录失败原因;
            endif
        else (否)
            :返回错误信息;
        endif
    }
    :展示处理结果;
````

> **绘图要点：** 活动图表达的是"活动"（动作），泳道（partition/swimlane）用于区分不同角色的职责；支持 `fork`/`join` 表示并发行为。

---

### 1.3 状态图（State Diagram）

| 项目 | 说明 |
|---|---|
| **适用场景** | **单一对象**的完整生命周期状态变迁（如订单、任务、审批、工单、用户会话）；必须覆盖所有合法状态和转换 |
| **不适用场景** | 多对象交互（用时序图）、并行行为（用活动图）、业务规则本身（用流程图或决策表） |
| **推荐工具** | Mermaid `stateDiagram-v2`、PlantUML `state` |
| **输出格式** | SVG（优先） |

**Mermaid 示例（状态机）：**
````mermaid
stateDiagram-v2
    [*] --> 待支付: 创建订单
    待支付 --> 待发货: 支付成功
    待支付 --> 已取消: 超时取消
    待支付 --> 已取消: 用户主动取消
    待发货 --> 运输中: 商家发货
    待发货 --> 已取消: 退款并取消
    运输中 --> 已签收: 确认收货
    已签收 --> 已完成: 超过售后期
    已完成 --> [*]
    已取消 --> [*]
    已签收 --> 退货中: 申请退货
    退货中 --> 已退款: 审核通过
    退货中 --> 待收货: 重新发货
````

> **绘图要点：** 状态图画的是**一个对象**的状态变化，每条边上必须标注**触发事件**（不是条件判断）；初始状态 `[*]` 和终态 `[*]` 必须出现；复合状态用 `state 复合状态名 { ... }` 表达。

---

### 1.4 时序图（Sequence Diagram）

| 项目 | 说明 |
|---|---|
| **适用场景** | 系统模块间交互、API 调用链、微服务调用、分布式通信、HTTP 请求/响应全流程 |
| **不适用场景** | 静态结构（用类图）、业务逻辑分支（用活动图）、并行进程（用活动图 fork） |
| **推荐工具** | Mermaid（代码优先）、PlantUML（完整语义）、SeqViz（在线） |
| **输出格式** | SVG（优先） |

**Mermaid 示例（REST API 调用）：**
````mermaid
sequenceDiagram
    autonumber
    participant C as 客户端
    participant G as API Gateway
    participant AU as 认证服务
    participant OR as 订单服务
    participant PS as 支付服务
    participant DB as 数据库

    C->>G: POST /api/orders<br/>Header: Authorization
    G->>AU: 验证 Token
    AU-->>G: Token 有效<br/>user_id: 1001
    G->>OR: 创建订单请求<br/>user_id, items, amount
    OR->>DB: BEGIN TRANSACTION
    OR->>DB: SELECT stock WHERE item_id IN (...)
    DB-->>OR: stock 充足
    OR->>DB: INSERT INTO orders (...)
    OR->>DB: INSERT INTO order_items (...)
    OR->>DB: UPDATE stock (decrement)
    OR->>DB: COMMIT
    OR->>PS: 调用支付接口<br/>order_id, amount
    PS-->>OR: 支付链接 URL
    OR-->>G: 订单已创建<br/>payment_url
    G-->>C: 201 Created<br/>{order_id, payment_url}

    PS->>PS: 用户支付中 (异步)
    PS->>OR: 支付回调通知<br/>POST /webhook/payment
    OR->>DB: UPDATE orders<br/>SET status='PAID'
    OR-->>PS: 200 OK
````

> **绘图要点：** 参与方按调用顺序从左到右排列；异步消息用虚线箭头 `-->>`；`activate`/`deactivate` 可标生命线；消息较多时分栏（alt/loop/rect）；参与者命名应与其类名一致。

---

### 1.5 用例图（Use Case Diagram）

| 项目 | 说明 |
|---|---|
| **适用场景** | 系统需求全景、用户与系统边界、识别系统功能和角色、需求评审起点 |
| **不适用场景** | 实现细节（用类图/序列图）、界面设计（用原型图）、性能要求（非功能需求另立章节） |
| **推荐工具** | Mermaid（快速原型）、PlantUML（完整 UML 语义）、draw.io |
| **输出格式** | SVG（优先） |

**Mermaid 示例：**
````mermaid
graph LR
    classDef actor fill:#f9f,stroke:#333,stroke-width:2px
    classDef system fill:#bfe,stroke:#333,stroke-width:2px
    classDef external fill:#ffc,stroke:#333,stroke-width:1px

    A1((👤 系统管理员)):::actor
    A2((👤 普通用户)):::actor
    A3((👤 商家)):::actor

    Sys((📦 电商平台系统)):::system

    A1 -->|登录系统| Sys
    A1 -->|管理用户| Sys
    A1 -->|查看运营报表| Sys
    A2 -->|浏览商品| Sys
    A2 -->|搜索商品| Sys
    A2 -->|下单购买| Sys
    A2 -->|管理购物车| Sys
    A2 -->|查看个人订单| Sys
    A2 -->|申请售后| Sys
    A3 -->|发布商品| Sys
    A3 -->|管理库存| Sys
    A3 -->|处理订单| Sys
    Sys -->|发送短信通知| EXT1((📱 短信平台)):::external
    Sys -->|发起支付| EXT2((💳 第三方支付)):::external
    Sys -->|查询物流| EXT3((🚚 物流 API)):::external

    class A1,A2,A3 actor
    class Sys system
````

> **绘图要点：** 系统边界用 `((系统名))` 圆括号；角色用 `((👤 角色名))`；用例用 `(用例名)` 圆角矩形；include 用 `<<include>>` 标签；extend 用 `<<extend>>` 标签；include/extend 关系在 Mermaid 中用 `graph LR` + 箭头注释标注。

---

### 1.6 类图（Class Diagram）

| 项目 | 说明 |
|---|---|
| **适用场景** | 面向对象设计（类、接口、抽象类、泛化/聚合/组合/依赖关系）、数据库概念模型（映射到表结构）、模块接口定义 |
| **不适用场景** | 业务流程（用活动图）、对象间消息传递（用时序图）、一对一简单关系（直接文字描述） |
| **推荐工具** | PlantUML（代码优先，UML 2.0 完整）、draw.io（可视化） |
| **输出格式** | SVG（优先） |

**PlantUML 示例（完整类图）：**
````plantuml
@startuml
' === 可见性 ===
' - 私有   # 受保护   ~ 包内私有   + 公开

' === 关系线类型 ===
' <|.. 泛化（继承）    *-- 组合    o-- 聚合    --> 依赖    ..>  实现

skinparam classAttributeIconSize 0

class User {
    - id: Long
    - username: String
    - email: String
    - passwordHash: String
    - createdAt: LocalDateTime
    - updatedAt: LocalDateTime
    --
    + getId(): Long
    + getUsername(): String
    + changePassword(oldPwd: String, newPwd: String): Boolean
    + toString(): String
}

class UserProfile {
    - id: Long
    - userId: Long
    - nickname: String
    - avatarUrl: String
    - bio: String
    --
    + updateProfile(dto: ProfileDTO): Boolean
}

interface Identifiable {
    + getId(): Long
}

interface Auditable {
    + getCreatedAt(): LocalDateTime
    + getUpdatedAt(): LocalDateTime
}

class Order {
    - id: Long
    - userId: Long
    - orderNo: String
    - status: OrderStatus
    - totalAmount: BigDecimal
    - shippingAddress: String
    - createdAt: LocalDateTime
    --
    + calculateTotal(): BigDecimal
    + cancel(): Boolean
    + confirmReceipt(): Boolean
}

enum OrderStatus {
    PENDING_PAYMENT
    PAID
    SHIPPED
    DELIVERED
    COMPLETED
    CANCELLED
    REFUNDED
}

class OrderItem {
    - id: Long
    - orderId: Long
    - productId: Long
    - productName: String
    - quantity: Integer
    - unitPrice: BigDecimal
    - subtotal: BigDecimal
    --
    + getSubtotal(): BigDecimal
}

User "1" o-- "1" UserProfile : 拥有
User "1" *-- "*" Order : 下单
Order "1" *-- "*" OrderItem : 包含
OrderItem "0..*" --> "1" Product : 关联
User ..|> Identifiable
User ..|> Auditable
Order ..|> Auditable
@enduml
````

> **绘图要点：** 关系线箭头方向：子类指向父类（泛化）、整体指向部分（组合/聚合）；多重性标注在连线两端；接口用 `<<interface>>` 标签或 `className` 斜体；枚举用 `enum` 关键字；每行一个属性/方法；属性行用 `--` 分隔。

---

### 1.7 组件图（Component Diagram）

| 项目 | 说明 |
|---|---|
| **适用场景** | 系统模块划分、物理组件依赖关系、可部署单元规划、团队分工依据 |
| **不适用场景** | 运行时进程关系（用部署图）、逻辑类结构（用类图）、接口细节（用类图+时序图） |
| **推荐工具** | PlantUML（组件语法）、draw.io |
| **输出格式** | SVG（优先） |

**PlantUML 示例（微服务组件图）：**
````plantuml
@startuml
skinparam componentStyle uml2

skinparam node {
    BackgroundColor LightBlue
    BorderColor DarkBlue
    FontSize 12
}
skinparam component {
    BackgroundColor #Gold
    BorderColor #DarkOrange
    FontSize 11
}
skinparam interface {
    BackgroundColor #PaleGreen
    BorderColor #DarkGreen
    FontSize 10
}

package "前端层" {
    [Vue 3 SPA] as FE
    [移动端 H5] as MOBILE
}

package "网关层" as GATEWAY {
    [Spring Cloud Gateway] as GW
    [认证过滤器 AuthFilter] as AUTH
}

package "业务服务层" {
    [用户服务 UserService] as US
    [订单服务 OrderService] as OS
    [商品服务 ProductService] as PS
    [支付服务 PaymentService] as PAY
    [通知服务 NotificationService] as NS
}

package "基础支撑层" {
    database "MySQL 主库" as DB
    database "Redis 缓存" as REDIS
    database "MongoDB" as MONGO
    [Elasticsearch] as ES
    [RabbitMQ] as MQ
    [Nacos 注册中心] as NACOS
}

FE --> GW : HTTPS
MOBILE --> GW : HTTPS
GW --> AUTH
AUTH --> US : 验证 Token

US --> DB : JDBC
US --> REDIS : 缓存会话
US ..> NACOS : 注册/发现

OS --> PS : RPC
OS --> PAY : RPC/消息
OS --> DB : CRUD
OS --> MQ : 事件发布
PAY --> MQ : 消费事件
PS --> ES : 全文搜索
PS --> MONGO : 商品详情

NS --> MQ : 订阅事件
NS --> REDIS : 队列

@enduml
````

> **绘图要点：** 组件用 `[组件名]` 或 `component ComponentName {}`；用 `package` 分组；接口用 `(InterfaceName)` 圆括号表示；关系线上标注接口/协议（HTTP、RPC、MQ）；皮肤参数 `uml2` 风格更现代；组件图侧重于**物理部署单元**而非逻辑类。

---

### 1.8 部署图（Deployment Diagram）

| 项目 | 说明 |
|---|---|
| **适用场景** | 硬件节点拓扑、服务器/容器部署配置、网络拓扑、软件在节点上的artifact部署、软件运行架构 |
| **不适用场景** | 逻辑模块划分（用组件图）、代码结构（用类图）、运行时调用关系（用时序图） |
| **推荐工具** | PlantUML（节点语法）、draw.io |
| **输出格式** | SVG（优先） |

**PlantUML 示例（云原生部署图）：**
````plantuml
@startuml
skinparam node {
    BackgroundColor LightYellow
    BorderColor #DarkOrange
    RoundCorner 8
}
skinparam artifact {
    BackgroundColor #LightGreen
    BorderColor DarkGreen
}
skinparam database {
    BackgroundColor #LightCyan
    BorderColor DarkCyan
}

node "🏷️ K8s Cluster" as K8S {
    node "🌐 Nginx Ingress" as INGRESS {
        artifact "ingress-controller"
    }

    node "📦 Namespace: production" {
        node "🐳 用户服务 Pod" as USER_POD {
            artifact "user-service:2.1.0"
        }
        node "🐳 订单服务 Pod" as ORDER_POD {
            artifact "order-service:1.8.3"
        }
        node "🐳 商品服务 Pod" as PROD_POD {
            artifact "product-service:3.0.1"
        }
        node "🐳 网关服务 Pod" as GW_POD {
            artifact "gateway:2.0.0"
        }
    }

    node "💾 数据层 Pods" {
        node "🗄️ MySQL 主从" as MYSQL {
            artifact "mysql-operator"
        }
        node "📊 Redis Cluster" as REDIS {
            artifact "redis-cluster"
        }
        node "📬 RocketMQ" as MQ {
            artifact "rocketmq-broker"
        }
    }
}

node "☁️ 云服务商负载均衡" as ELB {
    artifact "SLB (公网)"
}

node "🌍 CDN 加速层" as CDN {
    artifact "OSS 静态资源"
}

ELB --> INGRESS : 443/HTTPS
CDN --> INGRESS : 回源请求
USER_POD --> MYSQL : 读写分离
USER_POD --> REDIS : 读写分离
ORDER_POD --> MQ : 异步消息
GW_POD --> USER_POD : HTTP
GW_POD --> ORDER_POD : HTTP
GW_POD --> PROD_POD : HTTP

note right of K8S
  副本数：用户服务 3 / 订单服务 2 / 商品服务 3
  配置：CPU request 500m / limit 2000m
end note
@enduml
````

> **绘图要点：** 节点（node）表示硬件/容器运行环境；artifact 表示部署的软件单元；用 `artifact` 或 `database` 模具；节点可嵌套（Kubernetes 环境常用）；箭头表示通信路径，标注协议和端口；部署图反映**运行时物理拓扑**。

---

### 1.9 E-R 图（Entity Relationship Diagram）

| 项目 | 说明 |
|---|---|
| **适用场景** | 数据库概念设计、实体关系建模（项目早期）、需求分析阶段的数据建模 |
| **不适用场景** | 详细表结构/字段设计（用物理数据模型）、SQL 建表语句（用数据库文档） |
| **推荐工具** | Mermaid（快速原型）、draw.io、PowerDesigner（逆向工程） |
| **输出格式** | SVG（优先） |

**Mermaid 示例（电商 E-R 图）：**
````mermaid
erDiagram
    USER ||--o{ ORDER : "1:N 下单"
    USER ||--o{ ADDRESS : "1:N 收货地址"
    USER ||--o{ COMMENT : "1:N 评论"
    USER {
        bigint id PK "用户ID"
        varchar50 username "用户名"
        varchar100 email "邮箱"
        varchar20 phone "手机号"
        varchar255 password_hash "密码哈希"
        timestamp created_at "创建时间"
        timestamp updated_at "更新时间"
        tinyint status "账号状态:1正常 0禁用"
    }

    ADDRESS ||..|| USER : "属于"
    ADDRESS {
        bigint id PK "地址ID"
        bigint user_id FK "用户ID"
        varchar20 receiver_name "收货人"
        varchar20 phone "联系电话"
        varchar200 province "省"
        varchar100 city "市"
        varchar100 district "区"
        varchar255 detail "详细地址"
        tinyint is_default "是否默认:1默认 0否"
    }

    ORDER ||--|{ ORDER_ITEM : "1:N 包含"
    ORDER ||--|| PAYMENT : "1:1 支付记录"
    ORDER ||--o| REFUND : "1:0..1 退款"
    ORDER {
        bigint id PK "订单ID"
        bigint user_id FK "用户ID"
        varchar32 order_no "订单编号(唯一)"
        tinyint status "订单状态:1待支付 2已支付..."
        decimal10_2 total_amount "订单总金额"
        bigint address_id FK "收货地址ID"
        text remark "订单备注"
        timestamp paid_at "支付时间"
        timestamp created_at "创建时间"
        timestamp updated_at "更新时间"
    }

    ORDER_ITEM ||..|| ORDER : "属于"
    ORDER_ITEM ||..|| PRODUCT : "关联"
    ORDER_ITEM {
        bigint id PK "订单项ID"
        bigint order_id FK "订单ID"
        bigint product_id FK "商品ID"
        varchar100 product_name "商品名称(快照)"
        varchar255 sku_snapshot "SKU规格(快照)"
        int quantity "购买数量"
        decimal10_2 unit_price "单价(快照)"
        decimal10_2 subtotal "小计金额"
    }

    PRODUCT ||--o{ ORDER_ITEM : "1:N 被下单"
    PRODUCT ||--o{ COMMENT : "1:N 被评价"
    PRODUCT {
        bigint id PK "商品ID"
        bigint category_id FK "分类ID"
        varchar200 name "商品名称"
        text description "商品描述"
        decimal10_2 price "售价"
        int stock "库存"
        varchar255 main_image "主图URL"
        tinyint status "上架状态:1上架 0下架"
        timestamp created_at "创建时间"
    }

    COMMENT ||--|| USER : "用户发表"
    COMMENT ||..|| PRODUCT : "评价"
    COMMENT {
        bigint id PK "评论ID"
        bigint user_id FK "用户ID"
        bigint product_id FK "商品ID"
        bigint order_id FK "订单ID"
        tinyint rating "评分:1-5星"
        text content "评论内容"
        varchar100 sku_ specs "购买规格(快照)"
        timestamp created_at "评论时间"
    }

    PAYMENT ||..|| ORDER : "支付"
    PAYMENT {
        bigint id PK "支付ID"
        bigint order_id FK "订单ID(唯一)"
        varchar32 payment_no "支付流水号"
        varchar20 payment_method "支付方式:wechat/alipay"
        decimal10_2 amount "支付金额"
        tinyint status "支付状态"
        timestamp paid_at "支付时间"
        varchar255 transaction_id "第三方交易号"
    }
````

> **绘图要点：** Mermaid E-R 语法：`||--o{` 表示 1:N，`}o--||` 表示 N:1，`||..||` 表示 1:1（通过外键）；属性行中 PK=主键、FK=外键；字段后跟类型+中文说明；枚举值字段在注释中说明；ER 图服务**概念模型**，不展示索引、约束等物理细节。

---

### 1.10 数据流图（DFD，Data Flow Diagram）

| 项目 | 说明 |
|---|---|
| **适用场景** | 信息系统需求分析（结构化方法）、功能建模（不含控制流）、与外部系统的数据交换 |
| **不适用场景** | 面向对象设计（用 UML）、实时/嵌入式系统控制流、并发行为描述 |
| **推荐工具** | draw.io（自定义形状）、Visio、PowerDesigner |
| **输出格式** | SVG（优先） |

**draw.io 结构示例（用 SVG 组装）：**
```
┌─────────────┐      ┌──────────────┐      ┌─────────────┐
│  外部实体    │      │  处理（加工）  │      │  数据存储    │
│  (Rectangle) │      │  (Circle/R)  │      │  (Open H-P) │
└──────┬──────┘      └──────┬───────┘      └──────┬──────┘
       │                    │                    │
       │ 数据流箭头          │ 数据流箭头          │ 数据流箭头
       ▼                    ▼                    ▼
```

> **绘图要点：** DFD 四要素：**外部实体**（方形）→ **处理**（圆/带编号的圆角矩形）→ **数据存储**（开口平行线）→ 数据流（箭头）；每个处理有编号（P1、P2...）；数据流标注数据内容；分层 DFD 可分解为 Level-0、Level-1。

---

### 1.11 架构图（Architecture Diagram）

| 项目 | 说明 |
|---|---|
| **适用场景** | 系统整体架构、技术栈全景图、微服务/模块架构、技术选型说明 |
| **不适用场景** | 精确接口定义（用接口协议文档）、部署细节（用部署图）、运行时调用（用时序图） |
| **推荐工具** | draw.io、C4 Model（Context/Container/Component/Code）、Excalidraw（手绘风格）、Mermaid |
| **输出格式** | SVG（优先） |

**C4 Model 示例（Mermaid 绘制）：**
````mermaid
C4Context
    title 系统上下文 - 电商平台

    Person(customer, "买家", "在平台购物的消费者")
    Person(seller, "商家", "在平台销售商品的商家")
    Person(admin, "管理员", "平台运营管理人员")

    System(ecc, "电商核心系统", "提供商品、订单、支付等核心服务", "Java/Spring Boot")
    System(cms, "内容管理系统", "管理商品图文、分类、营销活动", "Vue3/Nuxt")
    System(ms, "营销系统", "优惠券、满减、秒杀活动引擎", "Go")
    System(im, "即时通讯", "买家与商家实时聊天", "WebSocket/Nginx")
    System(pay, "支付网关", "聚合微信/支付宝/银行卡", "第三方接口")
    System(oss, "对象存储", "存储商品图片、视频等静态资源", "OSS/S3")
    System(sms, "短信服务", "发送验证码和通知短信", "第三方接口")
    System(logistics, "物流查询", "快递轨迹查询", "第三方接口")

    Rel(customer, ecc, "浏览商品、下单、支付")
    Rel(seller, ecc, "管理商品、处理订单")
    Rel(seller, ms, "回复买家消息")
    Rel(admin, cms, "管理内容和营销活动")
    Rel(ecc, pay, "发起支付", "HTTPS/REST")
    Rel(ecc, oss, "上传/读取文件", "SDK")
    Rel(ecc, sms, "发送短信通知")
    Rel(ecc, logistics, "查询物流状态")
    Rel(ms, im, "消息中转")
    Rel(customer, im, "咨询商品/订单")

    UpdateRelStyle(customer, ecc, $lineColor="#0066CC", $textColor="#0066CC")
    UpdateRelStyle(seller, ecc, $lineColor="#009933", $textColor="#009933")
````

> **绘图要点：** C4 Model 四层：Context（系统全景）→ Container（应用/服务）→ Component（组件）→ Code（代码）；架构图要有统一的图例（符号说明）；分层清晰、职责明确；标注技术栈和关键协议；可加 `UpdateRelStyle` 美化线条颜色。

---

### 1.12 甘特图（Gantt Chart）

| 项目 | 说明 |
|---|---|
| **适用场景** | 项目进度计划、任务分解（WBS）、里程碑管理、研发周期可视化、资源加载视图 |
| **不适用场景** | 精确资源调度/负载均衡（用专业项目管理软件）、实时进度跟踪（用燃尽图） |
| **推荐工具** | Mermaid（嵌入文档）、Excel/Google Sheets（快速原型）、Microsoft Project（复杂管理） |
| **输出格式** | PNG ≥ 300 DPI（优先嵌入）/ SVG |

**Mermaid 示例（带里程碑和依赖）：**
````mermaid
gantt
    title 毕业设计项目进度计划
    dateFormat YYYY-MM-DD
    axisFormat %m/%d

    section 前期准备
    选题与可行性分析      :a1, 2025-09-01, 14d
    开题报告撰写           :a2, after a1, 7d
    开题答辩               :crit, milestone, 2025-09-22, 0d
    文献调研               :a3, 2025-09-15, 21d

    section 需求与设计
    需求调研与分析         :b1, 2025-10-01, 14d
    系统功能建模           :b2, after b1, 10d
    用例图/类图/时序图     :b3, 2025-10-25, 7d
    概要设计               :b4, 2025-11-01, 7d
    详细设计               :b5, 2025-11-08, 14d
    需求/设计评审          :crit, milestone, 2025-11-22, 0d

    section 开发实现
    框架搭建与环境配置     :c1, 2025-11-23, 5d
    核心功能开发           :c2, 2025-11-28, 45d
    接口开发与联调         :c3, after c2, 14d
    前端页面开发           :c4, 2025-12-15, 21d
    功能模块集成           :c5, after c3, 7d
    系统集成测试           :crit, milestone, 2026-01-20, 0d

    section 测试与文档
    单元测试编写           :d1, 2026-01-06, 14d
    集成测试与系统测试      :d2, after c3, 14d
    性能测试               :d3, after d2, 7d
    论文撰写               :d4, 2025-12-01, 45d
    毕业设计说明书定稿      :crit, milestone, 2026-02-10, 0d

    section 答辩准备
    PPT 制作               :e1, 2026-02-11, 7d
    答辩预演               :e2, 2026-02-18, 3d
    最终答辩               :crit, milestone, 2026-02-25, 0d

    section 里程碑
    ✅ 开题完成            :milestone, 2025-09-22, 0d
    ✅ 需求设计完成         :milestone, 2025-11-22, 0d
    ✅ 系统集成完成         :milestone, 2026-01-20, 0d
    ✅ 论文定稿             :milestone, 2026-02-10, 0d
    ✅ 答辩完成             :milestone, 2026-02-25, 0d
````

> **绘图要点：** 里程碑用 `milestone` 或 `crit, milestone`；关键路径任务加 `crit` 标记；任务可设置依赖 `after taskId`；section 分组使图表更清晰；长期任务跨多个月时标注起止日期；每个项目阶段至少一个里程碑；时间轴用 `axisFormat` 格式化。

---

### 1.13 思维导图（Mind Map）

| 项目 | 说明 |
|---|---|
| **适用场景** | 头脑风暴、需求发散、知识整理、功能拆解、学习笔记、项目启动讨论 |
| **不适用场景** | 正式交付文档（正式文档用层级标题+表格）、精确流程描述（用流程图）、需要他人精确理解的文档 |
| **推荐工具** | XMind（桌面/移动端）、幕布（大纲+导图）、Obsidian（本地笔记）、Draw.io |
| **输出格式** | PNG ≥ 300 DPI |

**Mermaid 示例（Mind Map）：**
````mermaid
mindmap
    root((毕业设计))
        选题方向
            Web 系统开发
                Vue3 + Spring Boot
                微服务架构
            移动应用开发
                Flutter / RN
            数据分析与可视化
                Python + ECharts
            AI 应用
                大模型 API 调用
                本地模型部署
        研究方法
            文献综述
            需求调研
            竞品分析
            原型设计
        技术方案
            架构设计
            技术选型
            核心算法
        实现要点
            需求分析
            数据库设计
            接口设计
            前端实现
            后端实现
        测试验证
            功能测试
            性能测试
            用户测试
        论文结构
            摘要
            引言
            需求分析
            系统设计
            系统实现
            测试与评价
            总结展望
````

> **绘图要点：** 思维导图是发散工具，不是规范文档；导出后建议转为结构化文档使用；中心节点是主题，一级分支是大类；颜色不宜过多（≤5 色）；内容精简，每个节点不超过 15 字。

---

### 1.14 UI 流程图（Wireflow）

| 项目 | 说明 |
|---|---|
| **适用场景** | 页面跳转路径、用户操作路径、核心流程验证、PRD 配套流程图 |
| **不适用场景** | 精确界面设计（用原型图）、交互细节（用原型文档）、响应式适配（用流程图描述逻辑即可） |
| **推荐工具** | Figma（协作原型）、Axure（高保真原型）、draw.io（线框流程图） |
| **输出格式** | PNG ≥ 300 DPI / PDF |

**draw.io 命名规则（用 SVG 标注）：**
```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  首页         │────▶│  商品详情页   │────▶│  购物车页    │
│  Home Page   │     │  Product     │     │  Cart Page   │
└──────────────┘     └──────────────┘     └──────┬───────┘
       │                                          │
       │              ┌──────────────┐            │
       └─────────────▶│  登录/注册页  │◀───────────┘
                      │  Login/Reg   │   (未登录跳转)
                      └──────┬───────┘
                             │
                      ┌──────▼───────┐
                      │  结算确认页   │
                      │  Checkout    │
                      └──────┬───────┘
                             │
                      ┌──────▼───────┐
                      │  支付页面    │
                      │  Payment    │
                      └──────────────┘
```

> **绘图要点：** 每个节点标注页面名称（中英文）；箭头旁标注触发动作（如"点击商品卡片"）；登录验证分支用菱形决策点；用虚线框分组同一操作流程；页面编号可标注在节点内；图例说明所有箭头含义。

---

### 1.15 原型图（Prototype）

| 项目 | 说明 |
|---|---|
| **适用场景** | 界面设计验证、需求确认（甲方/产品经理）、 usability 测试、PRD 配套 UI |
| **不适用场景** | 实现细节（用详细设计文档）、响应式细节（用多套原型）、动效规格（用动效规范文档） |
| **推荐工具** | Figma（团队协作首选）、Sketch（Mac）、Axure RP（高保真+交互）、墨刀（国内） |
| **输出格式** | PNG ≥ 300 DPI（交付）/ PDF（含标注）/ Figma 链接（协作） |

**原型标注规范（图片标注）：**
| 标注项 | 说明 | 示例 |
|---|---|---|
| 页面名称 | 页面标题/编号 | `页面编号：P-001` |
| 版本 | 当前版本 | `版本：v2.3` |
| 操作说明 | 点击/滑动/输入 | `① 点击 → 商品详情页` |
| 数据来源 | 接口/页面参数 | `② 数据：GET /api/products` |
| 状态说明 | 空/加载/错误 | `③ 空状态：显示占位图` |

---

### 1.16 数据可视化图

| 项目 | 说明 |
|---|---|
| **适用场景** | 实验数据、统计结果、趋势分析、性能对比、算法效果展示 |
| **不适用场景** | 精确数值查阅（用表格）、占比分析（可用表格替代）、因果关系证明（需统计方法） |
| **推荐工具** | Python(Matplotlib/Seaborn/Plotly)、ECharts（Web）、Tableau（分析） |
| **输出格式** | PNG ≥ 300 DPI（论文）/ SVG（工程文档） |

**常用图表代码示例（Python + Matplotlib）：**

````python
import matplotlib.pyplot as plt
import matplotlib
matplotlib.rcParams['font.sans-serif'] = ['SimHei', 'Arial Unicode MS', 'DejaVu Sans']
matplotlib.rcParams['axes.unicode_minus'] = False

# === 折线图（性能趋势）===
fig, ax = plt.subplots(figsize=(8, 5))
x = list(range(1, 21))
ax.plot(x, [round(50 + 2*i + 3*(i**0.5), 2) for i in x], 'o-', label='响应时间 (ms)', linewidth=2)
ax.set_xlabel('并发用户数', fontsize=12)
ax.set_ylabel('响应时间 (ms)', fontsize=12)
ax.set_title('图 4-1 并发用户数与响应时间关系', fontsize=13, fontweight='bold')
ax.grid(True, alpha=0.3)
ax.legend()
plt.tight_layout()
plt.savefig('fig_4_1_concurrency_response.svg', dpi=300, bbox_inches='tight')
plt.close()

# === 柱状图（算法对比）===
fig, ax = plt.subplots(figsize=(7, 5))
algorithms = ['FCFS', 'SJF', 'RR(q=2)', 'Priority']
throughput = [68.3, 81.7, 74.2, 76.5]
colors = ['#4C78A8', '#F58518', '#E45756', '#72B7BA']
bars = ax.bar(algorithms, throughput, color=colors, edgecolor='black', linewidth=0.8)
ax.set_ylabel('系统吞吐量 (任务/秒)', fontsize=12)
ax.set_title('表 3-2 调度算法吞吐量对比', fontsize=13, fontweight='bold')
ax.set_ylim(0, 100)
for bar in bars:
    ax.text(bar.get_x() + bar.get_width()/2, bar.get_height() + 1,
            f'{bar.get_height():.1f}', ha='center', va='bottom', fontsize=10)
plt.tight_layout()
plt.savefig('fig_3_2_algorithm_comparison.svg', dpi=300, bbox_inches='tight')
plt.close()

# === 混淆矩阵热力图 ===
import numpy as np
fig, ax = plt.subplots(figsize=(6, 5))
cm = np.array([[85, 5], [3, 92]])  # 示例数据
im = ax.imshow(cm, cmap='Blues')
ax.set_xticks([0, 1]); ax.set_yticks([0, 1])
ax.set_xticklabels(['预测: Negative', '预测: Positive'])
ax.set_yticklabels(['实际: Negative', '实际: Positive'])
ax.set_title('图 5-3 混淆矩阵热力图', fontsize=13, fontweight='bold')
for i in range(2):
    for j in range(2):
        ax.text(j, i, str(cm[i, j]), ha='center', va='center', fontsize=16, fontweight='bold')
plt.colorbar(im, ax=ax)
plt.tight_layout()
plt.savefig('fig_5_3_confusion_matrix.svg', dpi=300, bbox_inches='tight')
plt.close()
````

> **绘图要点：** 坐标轴必须有中文标签和单位；图题用"图 X-Y 描述"格式；图例用英文时首字母大写（Sentence case）；颜色不超过 5 种（色盲友好配色：#4C78A8 蓝、#F58518 橙、#E45756 红、#72B7BA 青、#EECA3B 黄）；图片长宽比合适（一般 4:3 或 16:9）；导出时使用 `bbox_inches='tight'` 去除白边。

---

## 二、图表编号与引用规范

### 2.1 编号体系（全标准格式）

#### 2.1.1 编号格式总表

| 元素类型 | 编号格式 | 示例 | 题注位置 |
|---|---|---|---|
| **图** | `图 X-Y`（章-序号） | `图 3-1`、`图 2-15` | 图下方居中 |
| **表** | `表 X-Y`（章-序号） | `表 1-3`、`表 4-2` | 表上方居中 |
| **公式** | `式 (X-Y)` 或 `(X-Y)` | `式 (3-2)`、`(5-10)` | 公式右侧居中 |
| **代码块** | `代码 X-Y` | `代码 2-1` | 代码上方或下方均可 |
| **算法** | `算法 X-Y` | `算法 1-1` | 算法下方 |
| **流程图（内嵌正文）** | 作为正文段落，不用独立编号 | — | — |

#### 2.1.2 编号规则细则

```
规则 1：编号随章连续。第1章的图表编号为 1-1, 1-2, ...；第2章为 2-1, 2-2, ...
规则 2：图与表独立编号。同一章中可以有图 2-3 和表 2-3，互不干扰。
规则 3：附录图表编号在章号前加字母。如"图 A-1"、"表 B-2"。
规则 4：公式编号全文档连续（如用章节编号则同图表规则）。
规则 5：代码块编号与图表共享同一编号体系（代码 X-Y 也随章编号）。
规则 6：多卷论文可加卷号前缀。如"图 I-3-1"表示第 I 卷第 3 章图 1。
```

#### 2.1.3 题注撰写规范

**图的题注（CAPTION）：**
```
格式：[图 X-Y] + 描述性标题（说明图表传达的核心信息）
位置：图的正下方、居中
字体：中文宋体 10.5pt（小五）或五号；英文 Figure + 编号 + 描述，首字母大写

正确示例：
  图 3-1 用户下单时序图
  图 4-3 系统吞吐量随并发数变化趋势
  Figure 2-1 Overall System Architecture

错误示例：
  "图3-1"（数字间无连字符）
  "图 3-1 如下图所示"（图表已经在图里看到了，题注应说明内容而非让读者"看图"）
  "图 3-1 这是流程图"（标题过于泛化，未说明图表传达的信息）
```

**表的题注（TABLE CAPTION）：**
```
格式：[表 X-Y] + 描述性标题
位置：表的正上方、居左对齐
字体：中文黑体 10.5pt（五号）

正确示例：
  表 2-1 三种缓存方案对比
  表 5-3 实验参数配置
```

### 2.2 正文引用规范

#### 2.2.1 引用原则

| 原则 | 说明 | 权重 |
|---|---|---|
| **信息优先** | 引用图表时必须说明图表传达的核心信息 | 必须 |
| **编号必引** | 任何对图表的描述必须带编号 | 必须 |
| **首次完整** | 每个图表第一次在正文中提及时必须呈现（可直接展示或标注"见下图"但正文随后必须接图） | 必须 |
| **编号一致** | 正文中的编号必须与图表题注编号完全一致 | 必须 |

#### 2.2.2 正确/错误引用示例对比

**正确引用：**
```
由图 3-1 可知，当并发用户数从 10 增加到 100 时，系统平均响应时间
从 45ms 增长至 210ms，增长约 3.7 倍，基本呈线性关系。

由表 2-1 可见，在中小规模数据（<10万条）场景下，Redis 缓存的
读性能是 MySQL 查询的 12 倍，但随着数据量增大，两者的性能差距逐渐缩小。
```

**错误引用（禁止）：**
```
❌ 如图 3-1 所示。（未说明图表传达什么信息）
❌ 见下图。（没有编号）
❌ 从图中可以看出...（没有编号，没有说明信息）
❌ 图 3-1 和图 3-2 分别展示了...（两张图分开引用，不要合并描述）
```

#### 2.2.3 引用位置规范

```
引用位置原则：
1. 先见图表，后在正文中引用（自然科学类通常如此）
2. 工程技术类可先引用后展示（正文→"如图 X-Y 所示"→随后接图），但正文解读不可省略
3. 同一图表不宜在正文中反复引用（最多 2 次，引用信息应有增量）
4. 图表与对应正文段落不宜距离过远（以一页之内为佳）
```

---

## 三、图表进入文档的规范

### 3.1 论文中的图表规范（GB/T 7713.1 / 学术出版通用标准）

#### 3.1.1 格式要求

| 要求项 | 标准 | 说明 |
|---|---|---|
| **分辨率** | ≥ 300 DPI（位图） | PNG、TIFF、JPEG；SVG/EMF 矢量图无分辨率限制 |
| **配色** | 简洁 ≤ 5 色 | 色盲友好配色，背景白色或透明 |
| **字体** | 与正文一致 | 中文宋体/黑体；英文 Times New Roman / Arial；图内字体 ≥ 9pt |
| **线条粗细** | 0.5pt - 1.5pt | 图内线条不宜过细，避免打印模糊 |
| **宽度** | ≤ 版心宽度（一般 ≤ 16cm） | 超宽图可用通栏排版（跨两栏） |
| **嵌入方式** | 矢量优先 | LaTeX 用 `\includegraphics[width=...]{.pdf/.svg}` |
| **文件命名** | `fig_{chapter}_{seq}.{ext}` 或中文 | LaTeX：`fig_3_1.pdf`；Word：随文档一起管理 |
| **表格样式** | 三线表（顶线、表头底线、底线） | 不用竖线，表中数据对齐方式统一 |
| **表格字号** | 9pt - 10.5pt | 不小于正文字号的 80% |

#### 3.1.2 图表与章节的关系

```
1. 图表必须归属于某个章节（不能游离在章节外）
2. 章节标题命名包含图/表序号的暗示词：
   ✅ "3.2 系统功能建模（本章主要涉及图 3-1 至图 3-5）"
   ✅ "4.3 实验结果与分析（图 4-1 至图 4-7，表 4-1 至表 4-3）"
3. 图表按出现顺序连续编号，不跨章节重置
```

#### 3.1.3 特殊图表附加要求

```
数据可视化图：
  - 必须标注坐标轴名称和单位
  - 必须标注图例（有多条曲线时）
  - 必须标注数据来源（"数据来源：实验测试，2026-03"）
  - 趋势线应给出拟合公式或 R² 值

E-R 图 / 类图：
  - 须在图注中说明使用的 UML 版本或建模工具
  - 须在正文说明是否反映了最终实现（如有删减需注明）

时序图：
  - 须在图注中说明调用协议（如"基于 HTTPS/REST"）
  - 须在图注中说明异步消息的触发机制（如"WebSocket 推送"）
```

---

### 3.2 工程文档中的图表规范

#### 3.2.1 格式要求

| 要求项 | 说明 |
|---|---|
| **版本号** | 每个图表必须标注版本（如"图 3-1 v1.2"），与文档版本独立管理 |
| **作者/负责人** | 图内或图注标注制图人（如"制图：张三 2026-03-01"） |
| **审签状态** | 图注中标注"初稿 / 审查中 / 已批准" |
| **读者级别** | 图注标注目标读者（如"仅供开发人员" / "机密"） |
| **配色** | 区分草稿（灰色调）与正式图（正式配色） |
| **超宽图** | A3 横向页面或分页 |

#### 3.2.2 版本对照表（图表适用）

| 文档类型 | 图表管理方式 | 说明 |
|---|---|---|
| SRS（需求规格） | 图表单独编号（图表库） | 图表独立版本控制，与需求条目关联 |
| SDS（软件设计） | 图表内嵌文档 | 图表嵌入 Word/Markdown，章节管理 |
| 接口文档 | 图表内嵌或独立文件 | 独立时需在文档目录中注册路径 |
| 测试文档 | 测试报告中图表编号连续 | 同 SRS 图表库关联 |

#### 3.2.3 图例（Legend）规范

> 每个工程文档中的图表集，必须在文档开头或附录提供**统一图例**，说明所有符号含义。

```
图例示例（请根据实际项目定制）：

【UML 图例】
  ——→  实线箭头     ：泛化/实现关系
  ---→  虚线箭头     ：依赖关系
  ◆---  实线菱形箭头 ：组合（Composition）
  ◇---  实线空心箭头 ：聚合（Aggregation）
  ●    实心圆       ：初始状态
  ⬡    菱形         ：里程碑

【数据流图图例】
  ┌─┐  方形         ：外部实体
  (○)  圆形/编号圆  ：处理（加工）
  ══╗  开口平行线   ：数据存储
   →  箭头          ：数据流

【部署图标例】
  [  ]  矩形        ：容器/Pod
  ( )  圆角矩形     ：外部系统
  🗄️   数据库图标   ：数据存储
  ⬡    菱形         ：里程碑/检查点
```

---

## 四、图表命名规范

### 4.1 文件命名总规则

```
规则 1：文件名只含字母、数字、连字符（-）、下划线（_），禁止空格和中文
规则 2：文件名长度 ≤ 64 字符（跨平台兼容）
规则 3：编号必须与文档内编号一致
规则 4：版本号格式：v1.0 / V1.0（禁止 v1.2.3 多级版本混用）
规则 5：区分草稿（draft）和正式（final）版本
```

### 4.2 分类命名规范

#### 4.2.1 论文图表命名

```
# 格式
fig_{chapter}_{type}_{sequence}[_suffix].{ext}

# 示例
fig_3_1_sequence_ecommerce.svg       # 图 3-1：电商下单时序图
fig_3_2_er_main.svg                  # 图 3-2：主实体关系图
fig_4_1_perf_concurrent.svg          # 图 4-1：并发性能图
fig_4_2_confusion_matrix.svg         # 图 4-2：混淆矩阵图
tbl_2_1_comparison_cache.svg         # 表 2-1：缓存方案对比
tbl_4_1_experiment_params.svg        # 表 4-1：实验参数表

# 导出格式优先级
.svg > .pdf > .png ≥300dpi
```

#### 4.2.2 工程文档图表命名

```
# 格式
{DocType}_{Chapter}_{ChartType}_{ShortDesc}_{Version}.{ext}

{DocType} 文档类型代码：
  SRS - 需求规格说明书
  SDS - 软件设计说明书
  STD - 软件测试文档
  SPD - 软件实现文档（开发文档）
  URD - 用户需求文档

{ChartType} 图表类型代码：
  FC - flowchart（流程图）
  AC - activity（活动图）
  SD - state（状态图）
  SQ - sequence（时序图）
  UC - usecase（用例图）
  CD - class（类图）
  CM - component（组件图）
  DP - deployment（部署图）
  ER - entity（E-R 图）
  DF - dataflow（数据流图）
  AR - architecture（架构图）
  GT - gantt（甘特图）
  MM - mindmap（思维导图）
  WF - wireflow（UI 流程图）
  PV - prototype（原型图）
  CH - chart（数据可视化图）

# 示例
SRS_02_UC_user_management_v1.2.svg   # SRS 第2章 用户管理用例图 v1.2
SDS_04_CM_backend_services_v1.0.svg  # SDS 第4章 后端服务组件图 v1.0
SDS_05_DP_k8s_deployment_v1.0.svg    # SDS 第5章 K8s 部署图 v1.0
STD_03_SQ_order_create_v2.1.svg      # STD 第3章 订单创建时序图 v2.1
SPDs_06_CD_user_order_v1.0.svg       # 实现文档 第6章 用户-订单类图 v1.0
```

#### 4.2.3 图表源文件与导出文件管理

```
# 建议目录结构（以工程文档为例）
.
├── 06_图表与建模规范.md          # 规范主文档
├── assets/                        # 图表资产目录
│   ├── fig/                       # 论文图表（fig_）
│   │   ├── fig_3_1_sequence_ecommerce.svg
│   │   └── fig_4_2_confusion_matrix.svg
│   ├── engineering/              # 工程图表（按文档类型）
│   │   ├── SRS/
│   │   │   ├── SRS_02_UC_user_management_v1.2.svg
│   │   └── SRS_03_ER_product_v1.0.svg
│   │   ├── SDS/
│   │   │   ├── SDS_04_CM_backend_v1.0.svg
│   │   │   └── SDS_05_DP_k8s_v1.0.svg
│   │   └── STD/
│   └── raw/                       # 原始源文件（draw.io / .graphml / .pu）
│       ├── fig_3_1_sequence_ecommerce.drawio
│       └── SRS_04_CM_backend_v1.0.pu        # PlantUML 源文件
└── 06_图表与建模规范_assets.md    # 可选：图表清单索引
```

---

## 五、常见错误清单

### 5.1 编号与引用错误（高发率 ⚠️）

| # | 错误描述 | 后果 | 正确做法 |
|---|---|---|---|
| 1 | 图表无编号或编号重复 | 读者无法引用，正文描述混乱 | 每个图表唯一编号，纳入编号体系表 |
| 2 | 图题在图上方（论文规范） | 与 GB/T 7713 规范不符 | 图题在图**下方**；表题在表**上方** |
| 3 | 正文引用说"如下图/表所示"但不写编号 | 引用失效 | 引用格式：`由图 3-1 可知...`、`见表 2-3` |
| 4 | 正文引用图表但未说明图表传达的信息 | 图表孤立，读者无法理解目的 | 必须补充：`由图 X-Y 可知...`（说明信息） |
| 5 | 图号与图题不一致 | 混淆读者 | 严格对应 |
| 6 | 引用了图表但图中无该信息 | 误导读者 | 引用前核对图表内容 |
| 7 | 编号跨章节重置（如第2章第一张图写成"图 1-1"） | 编号体系混乱 | 按章节编号，第2章第一张图 = 图 2-1 |

### 5.2 格式与呈现错误（高发率 ⚠️）

| # | 错误描述 | 后果 | 正确做法 |
|---|---|---|---|
| 8 | 图表清晰度不足（< 300 DPI） | 打印/投影模糊 | 导出 SVG 或 ≥ 300 DPI PNG |
| 9 | 图表宽于页面版心 | 排版错乱 | 限制宽度；超宽图用通栏 |
| 10 | 图表内字体与正文不一致 | 视觉不统一 | 统一字体（中文宋体/黑体，英文 Arial） |
| 11 | 图表字号 < 9pt | 打印后无法辨认 | 字号 ≥ 9pt（latex 图表适当缩小但保证可读） |
| 12 | 表格使用垂直边框或过多网格线 | 不符合三线表规范 | 仅保留顶线、表头底线、底线 |
| 13 | 表格单元格对齐混乱（数字居左/中文居右混用） | 阅读障碍 | 数字居右/居中，文字居左 |
| 14 | 彩色图表未提供黑白打印版本 | 无法黑白打印/出版 | 附灰度替代版本或用线型/形状区分 |

### 5.3 UML 图绘制错误（软件工程类 ⚠️）

| # | 错误描述 | 后果 | 正确做法 |
|---|---|---|---|
| 15 | 时序图参与方过多（> 8 个） | 图不可读 | 拆分为多张子时序图，或抽取关键路径 |
| 16 | 时序图画成活动图（混淆执行顺序与并行） | 语义错误 | 时序图 = 时间轴（垂直），活动图 = 活动流（水平/垂直均可） |
| 17 | 状态图缺少初始状态 `[*]` 或终态 `[*]` | 不完整状态机 | 必须包含，可有多个终态 |
| 18 | 状态图把动作（action）当作转换条件（guard） | 状态机语义错误 | 边标签格式：`事件[条件]/动作` |
| 19 | E-R 图关系线没有标注基数（1:N/M:N） | 关系歧义 | 每条关系线两端标注基数 |
| 20 | 类图画了代码实现细节（如方法体） | 超出了类图的抽象层级 | 类图只描述接口签名，不写实现 |
| 21 | 类图关系线方向画反（聚合/组合方向） | 误导设计 | 组合：实心菱形在整体端；聚合：空心菱形在整体端 |
| 22 | 组件图/部署图混用 | 两种图服务不同目的 | 组件图 = 逻辑部署单元；部署图 = 物理/容器节点 |
| 23 | 架构图缺少图例 | 符号含义不明 | 必须提供统一图例 |
| 24 | 甘特图缺少里程碑标记 | 关键节点不突出 | 用菱形标注关键里程碑节点 |
| 25 | 用例图 Actor 指向用例画成依赖关系 | 语义错误 | Actor 到用例用普通箭头（association） |
| 26 | 用例图包含系统内部实现细节 | 违反用例图抽象原则 | 用例只描述 Actor 能看到的行为 |
| 27 | 数据流图 DFD 混入控制流（if/else） | DFD 不支持控制流 | DFD 只建模数据流；控制流用活动图 |

### 5.4 数据可视化错误（实验类 ⚠️）

| # | 错误描述 | 后果 | 正确做法 |
|---|---|---|---|
| 28 | 坐标轴无标签或无单位 | 数据无法解读 | 标注轴名称和物理单位 |
| 29 | 纵坐标从 0 开始但被截断（截断零点） | 夸大数据差异 | 明确说明为何截断，或同时提供完整范围图 |
| 30 | 饼图类别过多（> 7 类） | 不可读 | 合并为"其他"，或改用柱状图 |
| 31 | 3D 图表遮挡数据 | 误导读者 | 优先用 2D 图表 |
| 32 | 趋势线缺少 R² 值或置信区间 | 结论依据不明 | 补充统计指标 |
| 33 | 数据来源未标注 | 结论可信度低 | 标注数据来源和采样时间 |
| 34 | 图表颜色传达了非中立信息（如好评绿色/差评红色暗示） | 违背学术中立原则 | 使用中性配色；或明确说明色彩编码规则 |

---

## 六、图表审查清单

### 6.1 提交前逐项审查（必查 ⭐）

#### 6.1.1 编号与引用审查

- [ ] **图表都有编号**：每个图表都有且只有一个编号，无重复
- [ ] **编号格式正确**：图 X-Y / 表 X-Y / 式 (X-Y) 格式无误
- [ ] **编号随章连续**：第2章第一张图为图 2-1，无跨章重置
- [ ] **题注位置正确**：图题在图**下方**，表题在表**上方**
- [ ] **正文有引用**：每张图表在正文中都有引用（带编号）说明
- [ ] **引用有信息增量**：正文引用时说明了图表传达的核心信息，不是"如图所示"
- [ ] **引用编号一致**：正文中的图表编号与题注编号完全一致
- [ ] **引用顺序正确**：图表按正文中出现顺序排列，无乱序

#### 6.1.2 格式规范审查

- [ ] **清晰度达标**：位图 ≥ 300 DPI；矢量图 .svg/.emf 无清晰度问题
- [ ] **字体统一**：图内字体与正文字体一致（中文宋体/黑体，英文 Arial/Times）
- [ ] **字号可读**：图内最小字号 ≥ 9pt
- [ ] **宽度合适**：图表宽度 ≤ 版心宽度
- [ ] **配色简洁**：颜色不超过 5 种，色盲友好配色
- [ ] **黑白兼容**：彩色图可区分（线型/形状辅助区分）；提供了灰度版
- [ ] **线条粗细合适**：0.5pt - 1.5pt，打印清晰
- [ ] **表格三线表规范**：无竖线、无多余网格线；数字对齐正确
- [ ] **图表独立可读**：不看正文也能大致理解图表内容

#### 6.1.3 UML 图专项审查

- [ ] **时序图可读**：参与方数量 ≤ 7；消息标签清晰；异步消息用虚线
- [ ] **状态图完整**：包含初始状态、终态、所有合法状态和转换；每条边有事件标签
- [ ] **活动图有泳道**：跨角色活动图有泳道区分职责
- [ ] **用例图边界清晰**：系统边界框明确；Actor 在边界外；无实现细节
- [ ] **类图关系正确**：泛化/聚合/组合方向正确；基数标注完整
- [ ] **E-R 图基数完整**：每条关系线两端标注基数（1:N/M:N/1:1）
- [ ] **组件图/部署图不混用**：物理拓扑用部署图；模块划分用组件图
- [ ] **架构图有图例**：所有符号有统一说明
- [ ] **甘特图标注里程碑**：关键节点用菱形或特殊标记
- [ ] **数据流图无控制流**：DFD 不含 if/else 等控制逻辑

#### 6.1.4 数据可视化专项审查

- [ ] **坐标轴有标签和单位**
- [ ] **图例完整**（有多条数据时）
- [ ] **数据来源已标注**
- [ ] **无误导性截断**（截断零点须说明）
- [ ] **趋势图有统计指标**（R² / p 值等）
- [ ] **图表数量适中**：不宜过多（论文图表一般 5-15 张）

#### 6.1.5 工程文档专项审查

- [ ] **图表有版本号**（如"v1.2"）
- [ ] **图表有审签状态**（初稿/审查中/已批准）
- [ ] **图表有作者标注**（制图人 + 日期）
- [ ] **图例完整**：符号含义全部覆盖
- [ ] **图表放在对应章节**：不游离在附录
- [ ] **图注标注目标读者**（如"开发人员/评审组/甲方"）

### 6.2 多文档交叉审查

- [ ] **图表编号不冲突**：同一文档中编号唯一
- [ ] **图表正文引用一致**：正文引用、图表题注、图表文件命名三处编号统一
- [ ] **版本一致性**：同一图表在文档多处引用时版本一致
- [ ] **图表库有索引**：assets 目录有 `INDEX.md` 记录图表清单

### 6.3 提交前最终检查

| 检查项 | 通过标准 | 检查方法 |
|---|---|---|
| 打印预览 | 打印/导出 PDF 后图表清晰 | 实际打印一张测试 |
| 目录自动编号 | 图表目录（若有）编号自动更新 | Word 域代码 / LaTeX 自动生成 |
| 超链接测试 | 文档内图表超链接跳转正确 | 逐一点击验证 |
| 版本对比 | 相邻版本间图表变更有记录 | CHANGELOG.md 或变更记录表 |
| 备份源文件 | 所有 .drawio / .pu / .graphml 源文件已备份 | 检查 raw/ 目录 |

---

## 附录 A：PlantUML / Mermaid 完整代码资源

### A.1 Mermaid 与 PlantUML 对照速查

| 特性 | Mermaid | PlantUML |
|---|---|---|
| 语法风格 | 声明式（配置即图） | 命令式（语句绘制） |
| 支持图表 | 流程图、活动图、状态图、时序图、ER、类图、、甘特图、思维导图、C4 | 全部 UML 2.0 图表 + 非 UML 图形 |
| 在线编辑 | mermaid.live | plantuml.com/online |
| VSCode 插件 | Mermaid Preview | PlantUML Language Support |
| 嵌入文档 | 直接嵌入 .md | 用 `@startuml` / `@enduml` 标签 |
| 适用场景 | 快速原型、轻量文档（毕业设计/博客） | 正式工程文档、高保真 UML |

### A.2 常用 Mermaid 代码模板（复制即用）

**C4 Model 模板：**
````mermaid
C4Context
    title 系统上下文 - {系统名称}
    Person(user, "用户角色", "用户描述")
    System(system, "系统名称", "系统描述")
    System_Ext(ext, "外部系统", "外部系统描述")
    Rel(user, system, "交互描述", "协议")
    Rel(system, ext, "调用描述", "协议")
````

**饼图（Pie Chart）模板：**
````mermaid
pie title 市场份额分布
    "竞品 A" : 42
    "竞品 B" : 28
    "竞品 C" : 15
    "其他" : 15
````

**用户旅程图（User Journey）模板：**
````mermaid
journey
    title 用户购物流程
    section 选购阶段
      浏览商品: 5: 用户
      搜索筛选: 4: 用户
      加入购物车: 3: 用户
    section 支付阶段
      结算: 1: 用户
      选择支付: 2: 用户
      输入密码: 1: 用户
    section 完成阶段
      支付成功: 5: 用户
      收到通知: 4: 系统
````

---

## 附录 B：图表与建模规范检查单（打印版）

```
╔══════════════════════════════════════════════════════════╗
║           图表与建模规范 - 提交前检查单                      ║
╠══════════════════════════════════════════════════════════╣
║ 一、编号与引用（每图必查）                                  ║
║   □ 有编号（格式：图 X-Y / 表 X-Y）                        ║
║   □ 编号随章连续，无重置                                    ║
║   □ 图题在图下方；表题在表上方                              ║
║   □ 正文有引用（带编号）                                    ║
║   □ 正文引用说明了图表传达的信息                            ║
║                                                            ║
║ 二、格式规范（每图必查）                                    ║
║   □ 清晰度：位图 ≥ 300 DPI 或矢量图                        ║
║   □ 字体：与正文一致，字号 ≥ 9pt                           ║
║   □ 配色：≤ 5 色，色盲友好                                  ║
║   □ 宽度：不超过版心                                       ║
║                                                            ║
║ 三、UML 图专项                                              ║
║   □ 时序图参与方 ≤ 7，消息标签清晰                          ║
║   □ 状态图有初始/终态，边有事件标签                         ║
║   □ E-R 图基数标注完整                                     ║
║   □ 类图关系方向正确；组件/部署图不混用                     ║
║   □ 架构图有统一图例                                       ║
║                                                            ║
║ 四、数据可视化专项                                           ║
║   □ 坐标轴有标签和单位                                      ║
║   □ 图例完整                                                ║
║   □ 数据来源已标注                                          ║
║                                                            ║
║ 五、工程文档专项                                             ║
║   □ 有版本号（vX.Y）                                        ║
║   □ 有审签状态和作者标注                                    ║
║   □ 有完整图例                                              ║
║                                                            ║
║ 检查人：____________   日期：____________                  ║
╚══════════════════════════════════════════════════════════╝
```

---

*本规范基于 GB/T 7713.1-2006（学位论文编写规范）、UML 2.5 规范、IEEE 标准绘图规范编制。*
*如发现规范与实际项目冲突，以项目内部规范为准，但须在项目规范中明确说明差异。*
