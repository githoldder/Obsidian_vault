# 部署运维SOP：常工鲜生系统部署与运维

> **文档版本**：v1.0 | **创建日期**：2026-03-14 | **项目**：常工鲜生小程序

---

## 一、部署架构

### 1.1 系统架构图

```
┌─────────────────────────────────────────────────────────────────────────┐
│                              用户层                                       │
│                  微信小程序 ←→ 用户微信                                   │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                              服务层                                       │
│  ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐     │
│  │   API Gateway   │    │   云函数        │    │   定时任务      │     │
│  │   (入口路由)     │    │   (业务逻辑)    │    │   (自动化)      │     │
│  └────────┬────────┘    └────────┬────────┘    └────────┬────────┘     │
└───────────┼──────────────────────┼───────────────────────┼──────────────┘
            │                      │                        │
            ▼                      ▼                        ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                              数据层                                       │
│  ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐     │
│  │   云数据库       │    │   云存储        │    │   CDN加速       │     │
│  │   (核心数据)     │    │   (图片/文件)   │    │   (静态资源)    │     │
│  └─────────────────┘    └─────────────────┘    └─────────────────┘     │
└─────────────────────────────────────────────────────────────────────────┘
```

### 1.2 部署环境

| 环境 | 说明 | 用途 |
|------|------|------|
| **开发环境** | 本地开发 | 开发调试 |
| **测试环境** | 微信测试号 | QA测试 |
| **生产环境** | 正式小程序 | 正式运营 |

---

## 二、部署流程

### 2.1 小程序端部署

#### 2.1.1 开发环境搭建

```bash
# 1. 安装微信开发者工具
下载安装包：https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html

# 2. 注册小程序账号
访问：https://mp.weixin.qq.com/
- 注册账号
- 完成认证
- 获取 AppID

# 3. 下载代码模板
git clone https://github.com/your-repo/cg-fresh-miniprogram.git
cd cg-fresh-miniprogram

# 4. 安装依赖
npm install

# 5. 配置 AppID
# 修改 project.config.json 中的 appid
```

#### 2.1.2 本地开发

```bash
# 1. 启动开发服务器
npm run dev

# 2. 打开微信开发者工具
# 导入项目目录

# 3. 开启自动编译
# 开发者工具 → 详情 → 本地设置 → 开启自动编译
```

#### 2.1.3 上传发布

```bash
# 1. 版本控制
git add .
git commit -m "v1.0.0: 完成基础功能"

# 2. 上传代码
# 微信开发者工具 → 上传
# 填写版本号：1.0.0
# 填写备注：初始版本

# 3. 登录小程序后台
https://mp.weixin.qq.com/

# 4. 提交审核
# 管理 → 版本管理 → 提交审核

# 5. 发布
# 审核通过后 → 发布
```

---

### 2.2 云开发部署

#### 2.2.1 环境配置

```
1. 登录微信小程序云开发控制台
   https://cloud.weixin.qq.com/

2. 创建环境
   - 环境名称：production
   - 环境ID：env-xxx

3. 创建数据库集合
   - users（用户表）
   - products（商品表）
   - orders（订单表）
   - delivery（配送表）
   - cart（购物车表）
   - addresses（地址表）
```

#### 2.2.2 安全规则配置

```javascript
// database/security.rules
{
    "users": {
        "read": "openid == auth.openid",
        "write": "openid == auth.openid"
    },
    "orders": {
        "read": "openid == auth.openid",
        "write": "openid == auth.openid"
    },
    "products": {
        "read": true,
        "write": false  // 仅管理员可写
    }
}
```

#### 2.2.3 云函数部署

```bash
# 1. 安装 CLI（可选）
npm install -g @cloudbase/cli

# 2. 登录
tcb login

# 3. 初始化云开发
tcb init

# 4. 上传云函数
tcb fn deploy functionName

# 5. 或在开发者工具中
# 云开发 → 云函数 → 上传并部署
```

---

## 三、运维监控

### 3.1 监控指标

#### 3.1.1 业务指标

| 指标 | 说明 | 告警阈值 |
|------|------|----------|
| 日订单数 | 每日订单总量 | <10单/天 |
| 支付成功率 | 支付成功/支付请求 | <95% |
| 下单转化率 | 下单用户/访问用户 | <30% |
| 客单价 | 订单总额/订单数 | <15元 |

#### 3.1.2 系统指标

| 指标 | 说明 | 告警阈值 |
|------|------|----------|
| 云函数调用量 | 每日调用次数 | 接近配额 |
| 数据库读次数 | 每日读操作 | 接近配额 |
| 数据库写次数 | 每日写操作 | 接近配额 |
| 存储容量 | 已用/总容量 | >80% |

#### 3.1.3 用户体验指标

| 指标 | 说明 | 目标值 |
|------|------|--------|
| 首屏加载时间 | 首次渲染完成 | <2秒 |
| 接口响应时间 | API平均响应 | <500ms |
| 错误率 | 请求失败比例 | <1% |

### 3.2 监控配置

#### 3.2.1 云开发控制台监控

```
访问：https://cloud.weixin.qq.com/cloudrun/monitor

查看内容：
- 调用量统计
- 流量统计
- 错误日志
- 性能分析
```

#### 3.2.2 自定义告警

```javascript
// 云函数：定时检查告警
// functions/monitor/index.js

const cloud = require('wx-server-sdk')
cloud.init()

exports.main = async (event, context) => {
    const db = cloud.database()
    const _ = db.command

    // 检查今日订单数
    const today = new Date()
    today.setHours(0, 0, 0, 0)

    const orderCount = await db.collection('orders')
        .where({
            created_at: _.gte(today)
        })
        .count()

    // 如果订单数低于阈值，发送通知
    if (orderCount.total < 10) {
        await cloud.openapi.subscribeMessage.send({
            touser: '管理员OpenID',
            templateId: 'xxx',
            data: {
                thing1: { value: '订单预警' },
                number2: { value: orderCount.total },
                thing3: { value: '今日订单低于10单' }
            }
        })
    }

    return { orderCount: orderCount.total }
}
```

---

## 四、备份与恢复

### 4.1 数据备份

#### 4.1.1 自动备份

```
微信云开发自动提供：
- 每日自动备份
- 保留7天数据
- 免费额度内无限次恢复
```

#### 4.1.2 手动备份

```javascript
// 云函数：导出数据
// functions/backup/index.js

exports.main = async (event, context) => {
    const cloud = require('wx-server-sdk')
    const db = cloud.database()

    // 导出订单数据
    const orders = await db.collection('orders')
        .where({
            created_at: _.gte(new Date(Date.now() - 30 * 24 * 60 * 60 * 1000))
        })
        .get()

    // 生成Excel
    // 或上传到云存储
    const fileID = await cloud.uploadFile({
        cloudPath: `backup/orders_${Date.now()}.json`,
        fileContent: Buffer.from(JSON.stringify(orders.data))
    })

    return { fileID }
}
```

### 4.2 数据恢复

```
恢复步骤：

1. 登录云开发控制台
   https://cloud.weixin.qq.com/

2. 进入数据库
   选择对应的集合

3. 导入数据
   导入 → 选择JSON文件 → 确认导入

4. 验证数据
   检查关键数据是否恢复
```

---

## 五、日志管理

### 5.1 日志类型

| 类型 | 说明 | 查看位置 |
|------|------|----------|
| **云函数日志** | 函数执行日志 | 云开发控制台 → 云函数 → 日志 |
| **前端日志** | 小程序端日志 | 开发者工具 → 调试器 → Console |
| **API日志** | 请求响应日志 | 云开发控制台 → 分析 → API分析 |

### 5.2 日志分析

```javascript
// 云函数：记录关键日志
// functions/logger/index.js

exports.main = async (event, context) => {
    const cloud = require('wx-server-sdk')
    const db = cloud.database()

    // 记录订单创建日志
    await db.collection('logs').add({
        data: {
            type: 'order_created',
            order_id: event.orderId,
            user_id: event.userId,
            amount: event.amount,
            created_at: db.serverDate()
        }
    })
}
```

---

## 六、安全运维

### 6.1 权限管理

#### 6.1.1 管理员权限

```
通过 OpenID 判断是否为管理员：

const ADMIN_OPENIDS = [
    'oxxxxxx1',  // 创业者本人
    'oxxxxxx2'  // 核心合伙人
]

exports.main = async (event, context) => {
    const wxContext = cloud.getWXContext()
    const isAdmin = ADMIN_OPENIDS.includes(wxContext.OPENID)

    if (!isAdmin && event.action === 'admin') {
        return { error: '无权限' }
    }
}
```

### 6.2 安全配置

| 配置项 | 说明 | 操作 |
|--------|------|------|
| **开放权限** | 设置安全规则 | 数据库 → 安全规则 |
| **API鉴权** | 开启token验证 | 云开发控制台 → 环境设置 |
| **请求限制** | 设置调用频率 | 云函数 → 并发数配置 |

### 6.3 数据安全

```
敏感数据处理：

1. 手机号加密存储
   - 使用 wx.login 获取 openid
   - 不存储明文手机号

2. 虚拟号保护
   - 用户查看他人信息时显示虚拟号
   - 例：138****8000

3. 支付安全
   - 使用微信支付官方API
   - 不自行处理支付流程
```

---

## 七、故障处理

### 7.1 常见问题

#### 7.1.1 支付失败

```
排查步骤：

1. 检查商户号配置
   - AppID 是否匹配
   - 商户号是否正确

2. 检查支付参数
   - 订单号是否唯一
   - 金额是否正确（单位：分）

3. 检查用户状态
   - 用户是否实名
   - 账户是否异常
```

#### 7.1.2 云函数超时

```
解决方案：

1. 增加超时时间
   云函数 → 配置 → 超时时间（默认60秒）

2. 优化代码逻辑
   - 减少数据库查询
   - 使用批量操作

3. 异步处理
   - 将非核心逻辑放入消息队列
```

#### 7.1.3 数据库读写慢

```
优化方案：

1. 添加索引
   - 频繁查询字段添加索引
   - 复合索引优化

2. 限制返回字段
   - 使用 field() 指定字段

3. 分页查询
   - 大数据量使用 skip/limit
```

---

## 八、版本管理

### 8.1 版本命名规范

```
格式：主版本.次版本.修订号

- 主版本(1.x.x)：重大功能变更
- 次版本(x.1.x)：新功能
- 修订号(x.x.1)：Bug修复
```

### 8.2 发布流程

```
发布检查清单：

[ ] 开发完成并自测
[ ] 测试环境验收通过
[ ] 提交代码到仓库
[ ] 微信开发者工具上传
[ ] 小程序后台提交审核
[ ] 审核通过后发布
[ ] 观察24小时数据
[ ] 如有异常及时回滚
```

### 8.3 回滚方案

```
回滚步骤：

1. 登录微信开发者工具

2. 下载历史版本
   管理 → 版本管理 → 下载历史版本

3. 重新上传旧版本

4. 提交审核发布
```

---

## 九、运维工具

### 9.1 常用命令

```bash
# 微信云开发CLI
tcb login                    # 登录
tcb fn list                 # 列出云函数
tcb fn deploy [name]        # 部署云函数
tcb fn delete [name]       # 删除云函数

# 数据库操作
tcb db export [collection]  # 导出数据
tcb db import [collection] # 导入数据
```

### 9.2 常用链接

| 资源 | 链接 |
|------|------|
| 小程序后台 | https://mp.weixin.qq.com/ |
| 云开发控制台 | https://cloud.weixin.qq.com/ |
| 微信开发者工具 | https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html |
| 小程序文档 | https://developers.weixin.qq.com/miniprogram/dev/ |

---

## 十、关联文档导航

| 序号 | 文档 | 说明 |
|------|------|------|
| → | [[05_技术架构/创业08-数据库设计]] | 数据库设计 |
| → | [[05_技术架构/创业09-接口文档设计]] | 接口设计 |
| → | [[Areas/001_个人规划/006_创业/08_前端原型/创业08-前端原型设计]] | 前端原型 |

---

> **运维原则**：监控先行、快速响应、数据优先、定期复盘
