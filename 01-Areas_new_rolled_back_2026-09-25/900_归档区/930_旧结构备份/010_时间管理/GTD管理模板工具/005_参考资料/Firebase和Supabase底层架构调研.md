### Firebase 的架构分析

##### Firestore 的详细架构

Firestore 是 Firebase 提供的一种 NoSQL、文档导向数据库，设计用于现代 web 和移动应用开发。研究表明，其架构具有以下特点：

- **数据模型**：数据存储在文档中，组织在集合里。文档可以包含键值对、嵌套对象（映射）和子集合。集合是无模式的，文档最大限制为 1 MB，支持嵌套至 100 层。
- **实时性和扩展性**：优化用于存储大量小型文档，支持实时同步和离线功能，提供高级查询能力。
- **基础设施**：建立在 Google Cloud 基础设施上，是无服务器数据库，支持快速部署、可扩展性和按需付费计费。
- **集成性**：与 Firebase 生态系统（如认证、云函数）无缝集成，实时通知系统通过 Firebase 客户端库确保即使网络连接问题也能提供流畅的用户体验。

关键论文包括：

- **标题**：Firestore: The NoSQL Serverless Database for the Application Developer
- **作者**：Ram Kesavan, David Gay, Daniel Thevessen, Jimit Shah, C. Mohan
- **会议**：2023 IEEE 39th International Conference on Data Engineering (ICDE)
- **页码**：pp. 3367-3379
- **URL**：[Firestore: The NoSQL Serverless Database for the Application Developer](https://research.google/pubs/firestore-the-nosql-serverless-database-for-the-application-developer/)
- **相关性**：高度相关，直接讨论 Firestore 的架构设计及其与 Firebase 生态系统的集成。

官方文档进一步补充：

- [Cloud Firestore Data Model](https://firebase.google.com/docs/firestore/data-model) 详细说明了文档和集合的结构，支持隐式创建和多语言引用（如 Web、Swift、Kotlin 等）。

#### Realtime Database 的架构

Firebase Realtime Database 是一种 NoSQL 云数据库，数据以 JSON 格式存储，呈现树状结构。研究显示：

- **数据同步**：数据在所有连接的客户端间实时同步，更新在几毫秒内完成。
- **离线功能**：支持本地磁盘持久化，允许应用在无网络连接时保持响应，重新连接后与服务器同步。
- **安全性**：通过 Firebase Realtime Database 安全规则（基于表达式的读写执行）确保直接客户端访问的安全性。
- **扩展性**：Blaze 计划支持多个数据库实例，每个实例可定制安全规则，并与 Firebase 认证集成以控制用户访问。

官方文档：[Firebase Realtime Database](https://firebase.google.com/docs/database) 提供了结构数据的最佳实践，强调数据结构规划的重要性，适合数百万用户的快速操作。

---
### Supabase 的架构分析

Supabase 是一个基于 PostgreSQL 的开放源代码平台，设计为 Firebase 的替代方案。研究显示，其架构具有以下特点：

- **核心组件**：以 PostgreSQL 数据库为核心，支持所有数据存储和管理。
- **关键组件**（见下表）：
    - Studio：开放源代码仪表板，用于管理数据库和服务。
    - GoTrue：基于 JWT 的 API 处理认证，集成 PostgreSQL 的行级安全性和 API 服务器。
    - PostgREST：将 PostgreSQL 转换为 RESTful API，支持 GraphQL。
    - Realtime：WebSocket 引擎，用于用户存在管理、广播消息和流式传输数据库更改。
    - Storage API：与 S3 兼容的对象存储，元数据存储在 PostgreSQL 中。
    - Deno：用于 JavaScript 和 TypeScript 的现代运行时，支持边缘函数。
    - postgres-meta：RESTful API 用于数据库管理（如获取表、添加角色、运行查询）。
    - Supavisor：Postgres 连接池器。
    - Kong：基于 NGINX 的 API 网关。

|**组件**|**描述**|
|---|---|
|Postgres (数据库)|核心，访问权限全面，工具使其使用像 Firebase 一样简单|
|Studio (仪表板)|开放源代码，用于管理数据库和服务|
|GoTrue (认证)|JWT 基于 API 管理用户和颁发访问令牌，集成 PostgreSQL 行级安全性和 API 服务器|
|PostgREST (API)|将 Postgres 数据库直接转换为 RESTful API，支持 GraphQL|
|Realtime (API & 多玩家)|可扩展 WebSocket 引擎，管理用户存在、广播消息、流式传输数据库更改|
|Storage API (大文件存储)|S3 兼容对象存储服务，元数据存储在 Postgres 中|
|Deno (边缘函数)|现代 JavaScript 和 TypeScript 运行时|
|postgres-meta (数据库管理)|RESTful API 管理 Postgres，获取表、添加角色、运行查询|
|Supavisor|云原生、多租户 Postgres 连接池器|
|Kong (API 网关)|基于 NGINX 的云原生 API 网关|

- **原则**：强调隔离性（每个组件独立运行）、集成性（可组合，暴露 API 和 Webhooks）、可扩展性（优先扩展现有工具）、可移植性（避免锁定，支持云和自托管，使用标准如 pg_dump）和长期支持（社区协作，功能上游化）。
- **相关文档**：[Supabase Architecture](https://supabase.com/docs/guides/getting-started/architecture)
- **相关性**：高度相关，详细说明了 Supabase 的模块化架构及其设计原则。
---
### 参考资料
- Firebase Firestore 论文：[Firestore: The NoSQL Serverless Database for the Application Developer](https://research.google/pubs/firestore-the-nosql-serverless-database-for-the-application-developer/)
- Firestore 官方文档：[Cloud Firestore Data Model](https://firebase.google.com/docs/firestore/data-model)
- Realtime Database 官方文档：[Firebase Realtime Database](https://firebase.google.com/docs/database)
- Supabase 架构文档：[Supabase Architecture](https://supabase.com/docs/guides/getting-started/architecture)
- Google官方附加资源：[NoSQL Database - Google’s Firebase: A Review](https://www.semanticscholar.org/paper/NoSQL-Database-Google%25E2%2580%2599s-Firebase:-A-Review-Lahudkar-Sawale/e846d6ba2cd2338c9ec207a0699d9b6b39d3ebc0)