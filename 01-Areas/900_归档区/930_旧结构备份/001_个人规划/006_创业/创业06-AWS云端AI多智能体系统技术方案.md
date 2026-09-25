# AWS云端AI多智能体系统技术方案

> 文档版本：v1.0 | 创建日期：2026-03-09 | 适用阶段：v0.3→v0.5

## 一、项目概述

### 1.1 核心目标

利用AWS $200免费额度，搭建一套**云端AI多智能体自动化系统**，为校园鲜果零售业务提供：

1. **多智能体调度系统**（小龙虾机制）- 4大职能AI智能体
2. **正规微信生态接入** - 企业微信API + 小程序
3. **生鲜行业数据集成** - 价格/产地/技术顾问数据源
4. **业务自动化流水线** - 从客服到财务的全链路自动化

### 1.2 架构总览

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              用户交互层                                        │
├────────────────────────┬────────────────────────┬───────────────────────────┤
│      企业微信           │       微信小程序        │        后台管理系统         │
│   (客服/群管/私域)       │   (下单/支付/会员)      │    (数据看板/人工介入)       │
└──────────┬─────────────┴──────────┬─────────────┴───────────┬───────────────┘
           │                        │                         │
           ▼                        ▼                         ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           API网关层 (AWS API Gateway)                        │
│                    统一入口 / 限流 / 鉴权 / 路由                              │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                          多智能体调度核心 (Python/FastAPI)                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐ │
│  │   客服AI      │  │   财务AI      │  │   销售AI      │  │    自媒体AI       │ │
│  │   智能体      │  │   智能体      │  │   智能体      │  │    智能体         │ │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  └────────┬─────────┘ │
│         │                 │                 │                   │           │
│         ▼                 ▼                 ▼                   ▼           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐ │
│  │ 微信消息处理  │  │ 账单数据分析  │  │ 用户画像/触达 │  │ 内容生成/发布    │ │
│  │ 群聊管理      │  │ 库存预警      │  │ 促销活动      │  │ 多平台运营       │ │
│  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                             数据与工具层                                      │
├──────────────┬──────────────┬──────────────┬──────────────┬─────────────────┤
│  业务数据库   │   向量数据库  │  LLM模型服务  │  外部数据源   │   业务系统集成   │
│  (PostgreSQL) │  (Pinecone)  │  (OpenAI API) │  (价格/产地)  │  (小程序/支付)   │
└──────────────┴──────────────┴──────────────┴──────────────┴─────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                              AWS基础设施层                                    │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐ │
│  │   EC2/ECS    │  │     RDS      │  │     S3       │  │  Lambda/StepFn   │ │
│  │  (计算服务)   │  │  (托管数据库) │  │  (对象存储)   │  │  (定时任务/工作流)│ │
│  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 二、AWS基础设施规划（$200额度优化配置）

### 2.1 免费套餐核心服务

| 服务 | 免费额度 | 用途 | 月成本预估 |
|------|---------|------|-----------|
| **EC2** | 750小时/月 t2.micro | 主应用服务器 | $0 |
| **RDS** | 750小时/月 db.t2.micro | PostgreSQL数据库 | $0 |
| **S3** | 5GB标准存储 | 文件/图片存储 | $0 |
| **Lambda** | 100万次请求/月 | 定时任务/触发器 | $0 |
| **API Gateway** | 100万次调用/月 | API统一入口 | $0 |
| **CloudWatch** | 10个指标/月 | 监控告警 | $0 |

### 2.2 推荐架构配置

```
【生产环境架构】

                    ┌──────────────┐
                    │   Route53    │  ← 域名解析 (可选，约$0.5/月)
                    └──────┬───────┘
                           │
                    ┌──────▼───────┐
                    │  CloudFront  │  ← CDN加速 (免费额度内)
                    └──────┬───────┘
                           │
                    ┌──────▼───────┐
                    │ API Gateway  │  ← REST/WebSocket API入口
                    └──────┬───────┘
                           │
            ┌──────────────┼──────────────┐
            │              │              │
     ┌──────▼──────┐ ┌────▼─────┐ ┌──────▼──────┐
     │   EC2       │ │  Lambda  │ │  Step       │
     │ (Docker部署) │ │ (定时任务)│ │  Functions  │
     │  主应用服务   │ │           │ │  (工作流)    │
     └──────┬──────┘ └──────────┘ └─────────────┘
            │
     ┌──────┴──────┐
     │             │
┌────▼─────┐ ┌────▼─────┐ ┌─────────────┐
│   RDS    │ │   S3     │ │  ElastiCache │  ← Redis缓存 (可选)
│PostgreSQL│ │  存储    │ │  (session)   │
└──────────┘ └──────────┘ └─────────────┘
```

### 2.3 详细配置步骤

#### Step 1: 创建VPC网络

```bash
# 使用AWS CLI或控制台创建
aws ec2 create-vpc --cidr-block 10.0.0.0/16

# 创建子网 (公有子网 + 私有子网)
aws ec2 create-subnet --vpc-id vpc-xxxx --cidr-block 10.0.1.0/24 --availability-zone cn-north-1a
aws ec2 create-subnet --vpc-id vpc-xxxx --cidr-block 10.0.2.0/24 --availability-zone cn-north-1b
```

#### Step 2: 启动EC2实例

```bash
# 启动t2.micro实例 (免费额度)
aws ec2 run-instances \
    --image-id ami-xxxxx \
    --count 1 \
    --instance-type t2.micro \
    --key-name MyKeyPair \
    --security-group-ids sg-xxxxx \
    --subnet-id subnet-xxxxx \
    --tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=ai-agent-server}]'
```

#### Step 3: 安装Docker和部署环境

```bash
#!/bin/bash
# user-data.sh - 实例启动脚本

# 更新系统
yum update -y

# 安装Docker
yum install -y docker
service docker start
usermod -a -G docker ec2-user

# 安装Docker Compose
curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# 安装Python和依赖
yum install -y python3 python3-pip git

# 创建应用目录
mkdir -p /opt/ai-agent-system
cd /opt/ai-agent-system

# 克隆代码仓库
git clone https://github.com/your-repo/ai-agent-system.git .

# 启动服务
docker-compose up -d
```

#### Step 4: 创建RDS数据库

```bash
aws rds create-db-instance \
    --db-instance-identifier ai-agent-db \
    --db-instance-class db.t2.micro \
    --engine postgres \
    --master-username admin \
    --master-user-password YourSecurePassword \
    --allocated-storage 20 \
    --vpc-security-group-ids sg-xxxxx \
    --db-subnet-group-name my-subnet-group
```

---

## 三、多智能体系统核心实现

### 3.1 小龙虾架构设计（Multi-Agent Orchestration）

参考当前主流多智能体框架（如OpenManus、AutoGen、CrewAI），设计以下架构：

```python
# core/orchestrator.py
"""
多智能体调度核心
实现"小龙虾"机制：
- 中央调度器 (Head)
- 多个职能智能体 (Claws)
- 共享记忆系统 (Memory)
- 工具调用系统 (Tools)
"""

from typing import Dict, List, Optional
from dataclasses import dataclass
from enum import Enum
import asyncio

class AgentRole(Enum):
    CUSTOMER_SERVICE = "客服AI"      # 处理咨询、售后、群管
    FINANCE = "财务AI"              # 记账、报表、库存预警
    SALES = "销售AI"                # 私域运营、促活、复购
    CONTENT = "自媒体AI"            # 内容生成、多平台发布

@dataclass
class Task:
    id: str
    role: AgentRole
    content: str
    context: Dict
    priority: int = 1
    source: str = ""  # wechat/mp/admin

class AgentOrchestrator:
    """智能体中央调度器"""

    def __init__(self):
        self.agents: Dict[AgentRole, BaseAgent] = {}
        self.memory = SharedMemory()
        self.task_queue = asyncio.PriorityQueue()

    def register_agent(self, role: AgentRole, agent: BaseAgent):
        """注册职能智能体"""
        self.agents[role] = agent

    async def dispatch(self, task: Task) -> str:
        """任务分发到对应智能体"""
        agent = self.agents.get(task.role)
        if not agent:
            return f"未找到 {task.role.value} 智能体"

        # 获取上下文记忆
        context = await self.memory.get_context(task.role, task.context.get("user_id"))

        # 执行任务
        result = await agent.execute(task, context)

        # 存储记忆
        await self.memory.store(task, result)

        return result

    async def route_intent(self, message: str, source: str, user_id: str) -> Task:
        """意图识别，自动路由到对应智能体"""
        # 使用LLM进行意图分类
        intent = await self.classify_intent(message)

        role_mapping = {
            "order_query": AgentRole.CUSTOMER_SERVICE,
            "refund": AgentRole.CUSTOMER_SERVICE,
            "price_inquiry": AgentRole.SALES,
            "financial_report": AgentRole.FINANCE,
            "content_creation": AgentRole.CONTENT,
        }

        role = role_mapping.get(intent, AgentRole.CUSTOMER_SERVICE)

        return Task(
            id=generate_id(),
            role=role,
            content=message,
            context={"user_id": user_id, "source": source, "intent": intent}
        )
```

### 3.2 四大职能智能体详细设计

#### 3.2.1 客服AI智能体

```python
# agents/customer_service_agent.py

class CustomerServiceAgent(BaseAgent):
    """客服AI智能体 - 处理微信消息、售后、群管理"""

    def __init__(self):
        super().__init__(AgentRole.CUSTOMER_SERVICE)
        self.tools = [
            OrderQueryTool(),      # 查询订单
            RefundProcessTool(),   # 处理退款
            FAQTool(),             # 常见问题
            HumanHandoffTool(),    # 转人工
        ]

    async def execute(self, task: Task, context: Dict) -> str:
        """执行客服任务"""

        # 常见问题自动回复
        if self.is_faq(task.content):
            return await self.handle_faq(task.content)

        # 订单查询
        if self.is_order_query(task.content):
            order_id = self.extract_order_id(task.content)
            return await self.query_order(order_id)

        # 售后处理
        if self.is_after_sales(task.content):
            return await self.process_after_sales(task)

        # 复杂问题转人工
        return await self.handoff_to_human(task)

    async def handle_faq(self, question: str) -> str:
        """处理常见问题"""
        faq_responses = {
            "配送范围": "目前支持XX校区所有宿舍楼配送，免配送费哦～",
            "坏果处理": "亲，坏果包赔！收到货24小时内拍照，立即退款或补发～",
            "优惠": "新客首单减3元，满20减2，满30减5，邀请好友得红包！",
            "营业时间": "周一至周日 9:00-22:00，下单后1小时内送达～",
        }

        # 使用向量检索匹配最相似问题
        return await self.rag_query(question, faq_responses)

    async def handle_group_message(self, group_id: str, message: str) -> Optional[str]:
        """处理群聊消息 - 群管功能"""

        # 自动欢迎新成员
        if "加入了群聊" in message:
            return self.get_welcome_message()

        # 定时促销推送
        if self.is_promotion_time():
            return self.get_daily_promotion()

        # 关键词回复
        if "今天有什么水果" in message:
            return await self.get_today_fruits()

        return None
```

#### 3.2.2 财务AI智能体

```python
# agents/finance_agent.py

class FinanceAgent(BaseAgent):
    """财务AI智能体 - 记账、报表、库存预警、采购建议"""

    def __init__(self):
        super().__init__(AgentRole.FINANCE)
        self.tools = [
            DataQueryTool(),       # 数据查询
            ReportGeneratorTool(), # 报表生成
            AlertTool(),           # 预警通知
            PriceAnalysisTool(),   # 价格分析
        ]

    async def execute(self, task: Task, context: Dict) -> str:
        """执行财务任务"""

        if "报表" in task.content or "报告" in task.content:
            return await self.generate_report(task.content)

        if "库存" in task.content:
            return await self.check_inventory()

        if "利润" in task.content or "收入" in task.content:
            return await self.calculate_profit()

        if "采购" in task.content or "进货" in task.content:
            return await self.generate_purchase_suggestion()

        return "财务助手已收到，正在处理..."

    async def daily_bookkeeping(self):
        """每日自动记账"""
        # 从小程序获取当日订单
        orders = await self.fetch_today_orders()

        # 分类统计
        stats = {
            "小程序订单": sum(o.amount for o in orders if o.channel == "mp"),
            "微信转账": sum(o.amount for o in orders if o.channel == "wechat"),
            "总订单数": len(orders),
            "客单价": sum(o.amount for o in orders) / len(orders) if orders else 0,
        }

        # 存入数据库
        await self.db.insert_daily_summary(stats)

        # 异常情况预警
        if stats["总订单数"] < 10:
            await self.alert("今日订单量异常偏低，请关注！")

    async def check_inventory(self) -> str:
        """库存检查与预警"""
        low_stock_items = await self.db.query_low_stock()

        if not low_stock_items:
            return "✅ 库存充足，暂无预警"

        alert_msg = "⚠️ 库存预警：\n"
        for item in low_stock_items:
            alert_msg += f"- {item.name}: 剩余{item.stock}件 (建议补货{item.suggest_restock}件)\n"

        return alert_msg

    async def generate_weekly_report(self) -> str:
        """生成周报"""
        # 查询本周数据
        data = await self.db.query_weekly_data()

        report = f"""
📊 【本周财务周报】({data.start_date} ~ {data.end_date})

【核心指标】
• 总销售额：¥{data.total_sales:,.2f}
• 订单总数：{data.total_orders}单
• 客单价：¥{data.avg_order_value:.2f}
• 毛利率：{data.gross_margin}%

【品类表现】
{self.format_category_performance(data.categories)}

【趋势分析】
{await self.ai_analyze_trend(data)}

【下周建议】
{await self.ai_suggest_next_week(data)}
        """
        return report
```

#### 3.2.3 销售AI智能体

```python
# agents/sales_agent.py

class SalesAgent(BaseAgent):
    """销售AI智能体 - 用户运营、促活、复购、促销"""

    def __init__(self):
        super().__init__(AgentRole.SALES)
        self.tools = [
            UserAnalysisTool(),    # 用户分析
            MessagePushTool(),     # 消息推送
            CouponTool(),          # 优惠券
            SegmentationTool(),    # 用户分层
        ]

    async def execute(self, task: Task, context: Dict) -> str:
        """执行销售任务"""

        if "促销" in task.content:
            return await self.create_promotion()

        if "复购" in task.content or "回访" in task.content:
            return await self.handle_repurchase_task()

        if "新客" in task.content:
            return await self.handle_new_customer(context.get("user_id"))

        return await self.generate_sales_suggestion()

    async def handle_new_customer(self, user_id: str) -> str:
        """新客欢迎与转化"""
        welcome_msg = """Hi～我是绿果果校园鲜果的老板小X！🍎

看到你对我们家水果感兴趣，太开心啦～
有任何问题随时问我，保证让你吃到最新鲜的水果！

🎁 新客专属福利：
• 首单立减3元
• 满20减2，满30减5
• 邀请好友下单，双方各得5元红包

点击这里下单：[小程序链接]
"""
        return welcome_msg

    async def repurchase_reminder(self):
        """复购提醒 - 定时任务"""
        # 查询3天前购买但未复购的用户
        users = await self.db.query_dormant_users(days=3)

        for user in users:
            msg = f"""Hi～上次买的{user.last_purchase_item}怎么样呀？甜吗？🥭

这周新到了一批海南芒果，超级甜！
要不要尝鲜呀？

🎁 老客户专属优惠：满30减5元哦～
[小程序链接]
"""
            await self.send_message(user.id, msg)

    async def price_inquiry_handler(self, fruit_name: str) -> str:
        """处理价格咨询"""
        # 查询当前价格
        price_info = await self.db.query_fruit_price(fruit_name)

        # 结合市场行情给出建议
        market_data = await self.fetch_market_price(fruit_name)

        return f"""🍎 {fruit_name}今日价格：

💰 我们的价格：¥{price_info.our_price}/斤
📊 市场行情：¥{market_data.avg_price}/斤

✅ 我们的优势：
• 早上新鲜采购，亲自挑选
• 宿舍直达，免配送费
• 坏果包赔，售后无忧

现在下单还有优惠哦！满20减2元～
"""
```

#### 3.2.4 自媒体AI智能体

```python
# agents/content_agent.py

class ContentAgent(BaseAgent):
    """自媒体AI智能体 - 内容生成、多平台发布、数据分析"""

    def __init__(self):
        super().__init__(AgentRole.CONTENT)
        self.tools = [
            ContentGenTool(),      # 内容生成
            ImageGenTool(),        # 图片生成
            PublishTool(),         # 多平台发布
            AnalyticsTool(),       # 数据分析
        ]

    async def execute(self, task: Task, context: Dict) -> str:
        """执行内容任务"""

        if "文案" in task.content or "笔记" in task.content:
            return await self.generate_content(task.content)

        if "发布" in task.content:
            return await self.publish_content(task)

        if "分析" in task.content or "数据" in task.content:
            return await self.analyze_performance()

        return await self.generate_daily_content()

    async def generate_xiaohongshu_post(self, topic: str) -> Dict:
        """生成小红书爆款笔记"""

        # 使用LLM生成文案
        prompt = f"""为校园鲜果店生成一篇小红书爆款笔记。
主题：{topic}
风格：真实、亲和、学生视角
要求：
1. 标题使用爆款公式（痛点+解决方案/数字+对比/场景+情绪）
2. 正文包含emoji、真实体验、价格优势
3. 结尾引导互动
4. 添加相关话题标签
"""

        content = await self.llm.generate(prompt)

        # 生成配图提示词
        image_prompt = f"Fresh fruits on campus, {topic}, natural lighting, aesthetic style, mobile photography"

        return {
            "title": content.title,
            "body": content.body,
            "image_prompt": image_prompt,
            "hashtags": ["#校园水果", "#宿舍必备", "#大学生日常", "#水果自由"]
        }

    async def weekly_content_calendar(self) -> List[Dict]:
        """生成周内容日历"""
        calendar = [
            {"day": "周一", "type": "种草", "theme": "宿舍党必囤水果清单"},
            {"day": "周二", "type": "科普", "theme": "这5种水果空腹不能吃"},
            {"day": "周三", "type": "互动", "theme": "你最爱吃什么水果？抽奖"},
            {"day": "周四", "type": "促销", "theme": "周三限时秒杀预告"},
            {"day": "周五", "type": "测评", "theme": "本周新品真实测评"},
            {"day": "周六", "type": "直播", "theme": "水果开箱+配送到寝"},
            {"day": "周日", "type": "反馈", "theme": "客户好评合集"},
        ]

        # 为每一天生成具体内容
        for item in calendar:
            item["content"] = await self.generate_xiaohongshu_post(item["theme"])

        return calendar
```

### 3.3 共享记忆系统

```python
# core/memory.py

from typing import List, Dict, Optional
import redis
import json
from datetime import datetime, timedelta

class SharedMemory:
    """智能体共享记忆系统"""

    def __init__(self):
        self.redis = redis.Redis(host='localhost', port=6379, db=0)
        self.db = PostgresMemoryStore()

    async def store(self, task: Task, result: str):
        """存储交互记忆"""
        memory = {
            "timestamp": datetime.now().isoformat(),
            "role": task.role.value,
            "content": task.content,
            "result": result,
            "user_id": task.context.get("user_id"),
            "source": task.source,
        }

        # 短期记忆 - Redis (24小时)
        key = f"memory:{task.context.get('user_id')}:{task.role.value}"
        self.redis.lpush(key, json.dumps(memory))
        self.redis.ltrim(key, 0, 99)  # 保留最近100条
        self.redis.expire(key, 86400)

        # 长期记忆 - PostgreSQL
        await self.db.insert_memory(memory)

    async def get_context(self, role: AgentRole, user_id: str) -> Dict:
        """获取上下文记忆"""
        # 获取最近对话历史
        key = f"memory:{user_id}:{role.value}"
        recent = self.redis.lrange(key, 0, 9)

        # 获取用户画像
        profile = await self.db.get_user_profile(user_id)

        # 获取关键记忆摘要
        summary = await self.get_memory_summary(user_id)

        return {
            "recent_history": [json.loads(m) for m in recent],
            "user_profile": profile,
            "memory_summary": summary,
        }

    async def get_memory_summary(self, user_id: str) -> str:
        """使用LLM生成记忆摘要"""
        # 获取该用户的所有历史记录
        history = await self.db.get_user_history(user_id, limit=50)

        # 使用LLM提取关键信息
        prompt = f"""总结以下客户交互历史，提取关键信息：
- 客户偏好
- 购买历史
- 特殊需求
- 之前的问题

历史记录：{history}
"""
        return await self.llm.generate(prompt)
```

---

## 四、微信生态正规接入方案

### 4.1 架构选择对比

| 方案 | 优点 | 缺点 | 适用场景 | 风险等级 |
|------|------|------|---------|---------|
| **企业微信自建应用** ✅ | 官方API，功能完整 | 需要企业资质认证 | 正规业务运营 | ⭐ 极低 |
| **微信小程序** ✅ | 用户触达，交易闭环 | 开发成本较高 | 下单/会员/支付 | ⭐ 极低 |
| **微信公众号** ✅ | 内容推送，自动回复 | 交互能力有限 | 信息发布/客服 | ⭐ 低 |
| ~~个人微信Hook~~ | 功能强大 | **违反协议，封号风险** | ❌ 不推荐 | ⭐⭐⭐⭐⭐ 极高 |
| ~~微信网页版协议~~ | 无需认证 | 不稳定，易封号 | ❌ 不推荐 | ⭐⭐⭐⭐ 高 |

### 4.2 企业微信自建应用详细配置

#### 步骤1: 注册与认证

```
1. 访问 https://work.weixin.qq.com/
2. 注册企业微信账号（可用个体工商户资质）
3. 完成企业认证（提升API调用频次）
4. 创建自建应用
```

#### 步骤2: 获取API凭证

```python
# config/wechat_work.py

WECHAT_WORK_CONFIG = {
    "corp_id": "wwxxxxxxxxxxxxxxxx",  # 企业ID
    "agent_id": "1000002",             # 应用ID
    "secret": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",  # 应用Secret
    "token": "xxxxxxxxxxxx",           # 消息加密Token
    "encoding_aes_key": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",  # 加密Key
}
```

#### 步骤3: 消息接收与响应

```python
# integrations/wechat_work.py

from flask import Flask, request, jsonify
import xml.etree.ElementTree as ET
from wechatpy import parse_message
from wechatpy.crypto import WeChatCrypto

app = Flask(__name__)

crypto = WeChatCrypto(
    token=WECHAT_WORK_CONFIG["token"],
    encoding_aes_key=WECHAT_WORK_CONFIG["encoding_aes_key"],
    corp_id=WECHAT_WORK_CONFIG["corp_id"]
)

@app.route("/wechat/callback", methods=["POST"])
async def wechat_callback():
    """接收企业微信消息"""

    # 验证消息签名
    signature = request.args.get("msg_signature")
    timestamp = request.args.get("timestamp")
    nonce = request.args.get("nonce")

    # 解密消息
    encrypted_msg = request.data
    decrypted_msg = crypto.decrypt_message(
        encrypted_msg, signature, timestamp, nonce
    )

    # 解析消息
    msg = parse_message(decrypted_msg)

    # 构建任务并分发到智能体
    task = await orchestrator.route_intent(
        message=msg.content,
        source="wechat_work",
        user_id=msg.source
    )

    # 执行并获取回复
    response = await orchestrator.dispatch(task)

    # 加密响应
    encrypted_response = crypto.encrypt_message(
        create_reply(response, msg).render(),
        nonce,
        timestamp
    )

    return encrypted_response

# 主动推送消息API
async def send_wechat_message(user_id: str, message: str):
    """主动给用户发消息"""
    access_token = await get_access_token()

    url = f"https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token={access_token}"

    data = {
        "touser": user_id,
        "msgtype": "text",
        "agentid": WECHAT_WORK_CONFIG["agent_id"],
        "text": {"content": message},
        "safe": 0
    }

    async with aiohttp.ClientSession() as session:
        async with session.post(url, json=data) as resp:
            return await resp.json()
```

#### 步骤4: 群机器人集成

```python
# integrations/wechat_group_bot.py

class WeChatGroupBot:
    """企业微信群机器人"""

    def __init__(self, webhook_url: str):
        self.webhook_url = webhook_url

    async def send_text(self, content: str, mentioned_list: List[str] = None):
        """发送文本消息"""
        data = {
            "msgtype": "text",
            "text": {
                "content": content,
                "mentioned_list": mentioned_list or []
            }
        }

        async with aiohttp.ClientSession() as session:
            async with session.post(self.webhook_url, json=data) as resp:
                return await resp.json()

    async def send_markdown(self, content: str):
        """发送Markdown消息"""
        data = {
            "msgtype": "markdown",
            "markdown": {"content": content}
        }

        async with aiohttp.ClientSession() as session:
            async with session.post(self.webhook_url, json=data) as resp:
                return await resp.json()

    async def send_news(self, title: str, description: str, url: str, pic_url: str):
        """发送图文消息"""
        data = {
            "msgtype": "news",
            "news": {
                "articles": [{
                    "title": title,
                    "description": description,
                    "url": url,
                    "picurl": pic_url
                }]
            }
        }

        async with aiohttp.ClientSession() as session:
            async with session.post(self.webhook_url, json=data) as resp:
                return await resp.json()

# 使用示例：财务机器人推送日报
async def send_daily_report_to_group():
    bot = WeChatGroupBot("https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=xxxx")

    report = await finance_agent.generate_daily_report()

    markdown = f"""## 📊 今日财务日报

{report}

> 由财务AI智能体自动生成
"""
    await bot.send_markdown(markdown)
```

### 4.3 微信小程序集成

```python
# integrations/wechat_mp.py

class WeChatMiniProgram:
    """微信小程序后端集成"""

    def __init__(self):
        self.app_id = "wx"
        self.app_secret = ""

    async def code_to_session(self, code: str) -> Dict:
        """登录凭证校验"""
        url = "https://api.weixin.qq.com/sns/jscode2session"
        params = {
            "appid": self.app_id,
            "secret": self.app_secret,
            "js_code": code,
            "grant_type": "authorization_code"
        }

        async with aiohttp.ClientSession() as session:
            async with session.get(url, params=params) as resp:
                return await resp.json()

    async def get_access_token(self) -> str:
        """获取小程序access_token"""
        url = "https://api.weixin.qq.com/cgi-bin/token"
        params = {
            "grant_type": "client_credential",
            "appid": self.app_id,
            "secret": self.app_secret
        }

        async with aiohttp.ClientSession() as session:
            async with session.get(url, params=params) as resp:
                data = await resp.json()
                return data.get("access_token")

    async def send_subscribe_message(self, openid: str, template_id: str, data: Dict):
        """发送订阅消息（订单状态通知）"""
        access_token = await self.get_access_token()
        url = f"https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token={access_token}"

        payload = {
            "touser": openid,
            "template_id": template_id,
            "page": "pages/order/detail",
            "data": data
        }

        async with aiohttp.ClientSession() as session:
            async with session.post(url, json=payload) as resp:
                return await resp.json()

# 订单状态变更通知
async def notify_order_status(openid: str, order_no: str, status: str):
    """通知用户订单状态"""
    mp = WeChatMiniProgram()

    template_data = {
        "character_string1": {"value": order_no},  # 订单编号
        "phrase2": {"value": status},              # 订单状态
        "time3": {"value": datetime.now().strftime("%Y-%m-%d %H:%M")},  # 更新时间
        "thing4": {"value": "点击查看详情"}         # 备注
    }

    await mp.send_subscribe_message(
        openid=openid,
        template_id="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
        data=template_data
    )
```

---

## 五、生鲜行业数据集成

### 5.1 数据源调研与接入方案

| 数据源 | 数据类型 | 接入方式 | 更新频率 | 费用 |
|--------|---------|---------|---------|------|
| **全国农产品商务信息公共服务平台** | 批发市场价格 | 公开API/爬虫 | 每日 | 免费 |
| **一亩田** | 产地价格/供应 | 开放平台API | 实时 | 需申请 |
| **惠农网** | 农产品行情 | 开放平台API | 实时 | 需申请 |
| **农业农村部市场信息平台** | 官方统计数据 | 公开数据 | 每日/周 | 免费 |
| **本地批发市场** | 实地价格 | 人工录入/对接 | 每日 | 免费 |

### 5.2 数据采集服务实现

```python
# services/market_data.py

class MarketDataService:
    """市场行情数据服务"""

    def __init__(self):
        self.cache = redis.Redis()
        self.db = PostgresDataStore()

    async def fetch_nybj_price(self) -> List[Dict]:
        """获取农业农村部市场价格"""
        # 全国农产品商务信息公共服务平台
        url = "http://nc.mofcom.gov.cn/"

        # 使用爬虫或公开API获取
        async with aiohttp.ClientSession() as session:
            async with session.get(url) as resp:
                html = await resp.text()
                return self.parse_price_html(html)

    async def fetch_yimutian_price(self, fruit_name: str) -> Dict:
        """获取一亩田价格数据"""
        # 需要申请API Key
        api_key = config.YIMUTIAN_API_KEY
        url = f"https://api.yimutian.com/price/query"

        params = {
            "api_key": api_key,
            "product": fruit_name,
            "market": "",  # 可指定市场
        }

        async with aiohttp.ClientSession() as session:
            async with session.get(url, params=params) as resp:
                data = await resp.json()
                return self.normalize_price_data(data)

    async def get_price_trend(self, fruit_name: str, days: int = 7) -> Dict:
        """获取价格趋势"""
        # 查询本地数据库历史数据
        history = await self.db.query_price_history(fruit_name, days)

        # 计算趋势
        if len(history) < 2:
            return {"trend": "stable", "change": 0}

        latest = history[-1]["price"]
        previous = history[0]["price"]
        change = (latest - previous) / previous * 100

        trend = "up" if change > 5 else "down" if change < -5 else "stable"

        return {
            "trend": trend,
            "change": round(change, 2),
            "current": latest,
            "history": history
        }

    async def generate_purchase_advice(self) -> str:
        """生成采购建议"""
        # 获取关注的水果品类
        fruits = ["苹果", "香蕉", "橙子", "葡萄", "西瓜"]

        advice = []
        for fruit in fruits:
            trend = await self.get_price_trend(fruit, days=7)

            if trend["trend"] == "down":
                advice.append(f"✅ {fruit}：价格下降{trend['change']}%，建议适量囤货")
            elif trend["trend"] == "up":
                advice.append(f"⚠️ {fruit}：价格上涨{trend['change']}%，建议控制进货量")
            else:
                advice.append(f"📊 {fruit}：价格稳定，按需采购")

        return "\n".join(advice)

# 定时任务：每日更新市场价格
async def daily_price_update():
    """每日更新市场价格数据"""
    service = MarketDataService()

    # 获取各类价格数据
    official_data = await service.fetch_nybj_price()

    # 存入数据库
    for item in official_data:
        await service.db.insert_price_record({
            "source": "农业农村部",
            "fruit_name": item["name"],
            "market": item["market"],
            "price": item["price"],
            "unit": item["unit"],
            "date": datetime.now()
        })

    # 发送采购建议到财务智能体
    advice = await service.generate_purchase_advice()
    await finance_agent.receive_market_advice(advice)
```

### 5.3 智能采购助手

```python
# agents/procurement_advisor.py

class ProcurementAdvisor:
    """智能采购顾问 - 结合市场数据给出采购建议"""

    def __init__(self):
        self.market_service = MarketDataService()
        self.inventory_service = InventoryService()

    async def generate_daily_procurement_plan(self) -> str:
        """生成每日采购计划"""

        # 1. 获取库存状态
        inventory = await self.inventory_service.get_current_inventory()

        # 2. 获取市场价格趋势
        market_data = {}
        for fruit in inventory:
            market_data[fruit.name] = await self.market_service.get_price_trend(fruit.name)

        # 3. 获取销售预测
        sales_forecast = await self.predict_sales(days=3)

        # 4. 生成采购建议
        plan = []
        for fruit in inventory:
            stock = fruit.current_stock
            forecast = sales_forecast.get(fruit.name, 0)
            market = market_data.get(fruit.name, {})

            if stock < forecast * 1.5:
                suggestion = "🔴 急需补货"
                quantity = forecast * 2 - stock
            elif stock < forecast * 2:
                suggestion = "🟡 建议补货"
                quantity = forecast * 1.5 - stock
            else:
                suggestion = "🟢 库存充足"
                quantity = 0

            if quantity > 0:
                plan.append({
                    "fruit": fruit.name,
                    "suggestion": suggestion,
                    "quantity": int(quantity),
                    "market_trend": market.get("trend", "unknown"),
                    "price_change": market.get("change", 0)
                })

        # 5. 格式化输出
        output = "## 📦 今日采购建议\n\n"
        for item in plan:
            output += f"**{item['fruit']}**\n"
            output += f"- {item['suggestion']}，建议采购 {item['quantity']} 斤\n"
            output += f"- 市场趋势：{item['market_trend']} ({item['price_change']}%)\n\n"

        return output
```

---

## 六、完整部署方案

### 6.1 Docker Compose配置

```yaml
# docker-compose.yml

version: '3.8'

services:
  # 主应用服务
  app:
    build: .
    container_name: ai-agent-app
    restart: always
    ports:
      - "8000:8000"
    environment:
      - DATABASE_URL=postgresql://user:pass@db:5432/ai_agent
      - REDIS_URL=redis://redis:6379/0
      - OPENAI_API_KEY=${OPENAI_API_KEY}
      - WECHAT_WORK_CORP_ID=${WECHAT_WORK_CORP_ID}
      - WECHAT_WORK_SECRET=${WECHAT_WORK_SECRET}
      - WECHAT_MP_APP_ID=${WECHAT_MP_APP_ID}
      - WECHAT_MP_SECRET=${WECHAT_MP_SECRET}
    depends_on:
      - db
      - redis
    volumes:
      - ./logs:/app/logs
      - ./data:/app/data

  # 定时任务服务
  scheduler:
    build: .
    container_name: ai-agent-scheduler
    restart: always
    command: python -m scheduler
    environment:
      - DATABASE_URL=postgresql://user:pass@db:5432/ai_agent
      - REDIS_URL=redis://redis:6379/0
    depends_on:
      - db
      - redis

  # PostgreSQL数据库
  db:
    image: postgres:15-alpine
    container_name: ai-agent-db
    restart: always
    environment:
      - POSTGRES_USER=user
      - POSTGRES_PASSWORD=pass
      - POSTGRES_DB=ai_agent
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "5432:5432"

  # Redis缓存
  redis:
    image: redis:7-alpine
    container_name: ai-agent-redis
    restart: always
    volumes:
      - redis_data:/data
    ports:
      - "6379:6379"

  # Nginx反向代理
  nginx:
    image: nginx:alpine
    container_name: ai-agent-nginx
    restart: always
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
      - ./ssl:/etc/nginx/ssl
    depends_on:
      - app

volumes:
  postgres_data:
  redis_data:
```

### 6.2 项目目录结构

```
ai-agent-system/
├── README.md
├── docker-compose.yml
├── Dockerfile
├── requirements.txt
├── .env.example
│
├── core/                          # 核心模块
│   ├── __init__.py
│   ├── orchestrator.py           # 多智能体调度器
│   ├── memory.py                 # 共享记忆系统
│   ├── config.py                 # 配置管理
│   └── exceptions.py             # 异常处理
│
├── agents/                        # 职能智能体
│   ├── __init__.py
│   ├── base.py                   # 智能体基类
│   ├── customer_service_agent.py # 客服AI
│   ├── finance_agent.py          # 财务AI
│   ├── sales_agent.py            # 销售AI
│   ├── content_agent.py          # 自媒体AI
│   └── procurement_advisor.py    # 采购顾问
│
├── integrations/                  # 第三方集成
│   ├── __init__.py
│   ├── wechat_work.py            # 企业微信
│   ├── wechat_mp.py              # 微信小程序
│   ├── wechat_group_bot.py       # 群机器人
│   └── market_data.py            # 市场数据
│
├── services/                      # 业务服务
│   ├── __init__.py
│   ├── order_service.py          # 订单服务
│   ├── inventory_service.py      # 库存服务
│   ├── user_service.py           # 用户服务
│   └── analytics_service.py      # 分析服务
│
├── models/                        # 数据模型
│   ├── __init__.py
│   ├── database.py               # 数据库模型
│   ├── schemas.py                # Pydantic模型
│   └── enums.py                  # 枚举定义
│
├── api/                           # API接口
│   ├── __init__.py
│   ├── routes/
│   │   ├── __init__.py
│   │   ├── webhook.py            # Webhook回调
│   │   ├── orders.py             # 订单接口
│   │   ├── users.py              # 用户接口
│   │   └── analytics.py          # 数据接口
│   └── middleware/
│       ├── auth.py               # 认证中间件
│       └── logging.py            # 日志中间件
│
├── scheduler/                     # 定时任务
│   ├── __init__.py
│   ├── tasks.py                  # 任务定义
│   └── jobs/                     # 具体任务
│       ├── daily_report.py
│       ├── price_update.py
│       ├── repurchase_reminder.py
│       └── content_publish.py
│
├── utils/                         # 工具函数
│   ├── __init__.py
│   ├── helpers.py
│   └── logger.py
│
├── tests/                         # 测试
│   └── ...
│
└── scripts/                       # 脚本
    ├── deploy.sh
    ├── backup.sh
    └── init_db.sh
```

### 6.3 部署脚本

```bash
#!/bin/bash
# scripts/deploy.sh

# 部署脚本

set -e

echo "🚀 开始部署AI多智能体系统..."

# 1. 更新代码
echo "📥 拉取最新代码..."
git pull origin main

# 2. 创建环境变量文件
if [ ! -f .env ]; then
    echo "⚠️ 请创建.env文件并配置环境变量"
    cp .env.example .env
    exit 1
fi

# 3. 构建并启动服务
echo "🐳 构建Docker镜像..."
docker-compose build

echo "▶️ 启动服务..."
docker-compose up -d

# 4. 数据库迁移
echo "🗄️ 执行数据库迁移..."
docker-compose exec app alembic upgrade head

# 5. 健康检查
echo "🏥 健康检查..."
sleep 5
if curl -f http://localhost:8000/health; then
    echo "✅ 部署成功！"
else
    echo "❌ 健康检查失败，请检查日志"
    docker-compose logs app
    exit 1
fi

echo "🎉 部署完成！"
echo "📊 监控面板: http://localhost:8000/admin"
echo "📚 API文档: http://localhost:8000/docs"
```

---

## 七、成本估算与优化

### 7.1 AWS成本明细（超出免费额度部分）

| 服务 | 免费额度 | 超出后费用 | 预估月用量 | 预估月费用 |
|------|---------|-----------|-----------|-----------|
| EC2 t2.micro | 750小时 | $0.0116/小时 | 720小时 | $0 |
| RDS db.t2.micro | 750小时 | $0.017/小时 | 720小时 | $0 |
| S3 Standard | 5GB | $0.023/GB | 10GB | $0.115 |
| Data Transfer | 15GB出网 | $0.09/GB | 50GB | $3.15 |
| CloudWatch | 10指标 | $0.30/指标 | 20指标 | $3.00 |
| **总计** | | | | **~$6.27/月** |

### 7.2 OpenAI API成本估算

| 场景 | 模型 | 调用量/日 | Token数/次 | 预估日费用 |
|------|------|----------|-----------|-----------|
| 客服自动回复 | gpt-4o-mini | 100次 | 2K | $0.30 |
| 内容生成 | gpt-4o | 5次 | 10K | $1.00 |
| 报表分析 | gpt-4o-mini | 10次 | 5K | $0.15 |
| 意图识别 | gpt-4o-mini | 200次 | 1K | $0.20 |
| **总计** | | | | **~$1.65/日 = $49.5/月** |

### 7.3 优化建议

```
1. 使用缓存减少LLM调用
   - 常见问题直接查FAQ库
   - 对话历史复用上下文

2. 模型选择优化
   - 简单任务用gpt-4o-mini
   - 复杂任务才用gpt-4o

3. 本地部署小模型
   - AWS额度可部署轻量级LLM
   - 处理高频简单查询

4. 批处理优化
   - 报表生成改为批量处理
   - 定时任务集中执行
```

---

## 八、实施路线图

### 第一阶段：基础搭建（Week 1-2）

- [ ] AWS账号配置与免费额度激活
- [ ] EC2 + RDS环境搭建
- [ ] 基础API框架部署
- [ ] 企业微信注册与认证
- [ ] 数据库设计

### 第二阶段：智能体开发（Week 3-4）

- [ ] 多智能体调度核心实现
- [ ] 客服AI智能体开发
- [ ] 企业微信消息接入
- [ ] 基础FAQ系统上线

### 第三阶段：业务集成（Week 5-6）

- [ ] 微信小程序后端集成
- [ ] 财务AI智能体开发
- [ ] 订单系统对接
- [ ] 库存管理功能

### 第四阶段：自动化完善（Week 7-8）

- [ ] 销售AI智能体开发
- [ ] 自媒体AI智能体开发
- [ ] 定时任务系统
- [ ] 数据报表功能

### 第五阶段：数据集成（Week 9-10）

- [ ] 市场数据采集
- [ ] 采购顾问功能
- [ ] 价格预警系统
- [ ] 优化与迭代

---

## 九、风险与应对

| 风险 | 影响 | 应对方案 |
|------|------|---------|
| AWS额度耗尽 | 服务中断 | 设置预算告警；优化资源使用；准备降级方案 |
| OpenAI API故障 | AI功能失效 | 设置fallback响应；本地缓存常见问题答案 |
| 企业微信API限制 | 消息收发受阻 | 实现消息队列；限流控制；降级到人工 |
| 数据安全问题 | 敏感信息泄露 | 加密存储；访问控制；定期备份 |
| 模型幻觉 | 回复不准确 | 人工审核关键场景；持续优化prompt |

---

## 十、附录

### 参考资料

- [AWS Free Tier](https://aws.amazon.com/cn/free/)
- [企业微信开发者文档](https://developer.work.weixin.qq.com/document)
- [微信小程序开发文档](https://developers.weixin.qq.com/miniprogram/dev/framework/)
- [OpenAI API文档](https://platform.openai.com/docs)
- [LangChain Agents](https://python.langchain.com/docs/use_cases/agent/)

### 相关文档

- [[01-Areas/001_个人规划/006_创业/创业05-大学生鲜果零售公司设计]]
- [[01-Areas/001_个人规划/006_创业/智能体职能skills]]

---

*文档版本：v1.0 | 最后更新：2026-03-09*
*状态：待实施 | 优先级：P0*
