# PPP（点对点协议）

## 📖 定义-解释-示例

### 什么是 PPP？

**PPP**（Point-to-Point Protocol）是数据链路层上用于**点到点链路**的协议，设计目标是取代老旧的 SLIP（串行线路 IP 协议）。PPP 不是单一的协议，而是一个**协议族**——由三个核心组件构成：**成帧方法**（基于 HDLC）、**链路控制协议（LCP）** 和**网络控制协议（NCP）**。

PPP 在现代网络中最广为人知的应用是 **PPPoE**（PPP over Ethernet）——ADSL 宽带拨号上网的底层协议。"宽带连接"那个需要输入用户名和密码的界面，背后就是在跑 PPPoE。

### PPP 的三件套架构

PPP 的三个组件各司其职：

**① 成帧（Framing）——数据怎么包装**
PPP 的帧格式继承自 HDLC，但做了一些简化：

```
┌──────┬─────┬─────┬──────┬──────┬──────┬──────┐
│ Flag │Addr │Ctrl │Proto │ Data │ FCS  │ Flag │
│ 7E   │ FF  │ 03  │1-2B  │≤1500B│ 2-4B │  7E  │
└──────┴─────┴─────┴──────┴──────┴──────┴──────┘
```

与 HDLC 的主要区别：
- **协议字段（Protocol）**：区分承载的网络层协议（0x0021=IP、0x8021=IPCP、0xC021=LCP）
- 地址固定为 0xFF（广播地址，点到点链路无意义但保留），控制字段固定为 0x03（UI 帧）
- 默认 MTU 为 1500 字节

**② LCP（链路控制协议）——链路怎么建立和维护**
LCP 负责 PPP 链路的"生老病死"：
- **链路建立**：发送 Configure-Request，协商 MRU、认证方式、压缩等参数
- **链路终止**：发送 Terminate-Request，优雅关闭连接
- **链路维护**：Echo-Request/Echo-Reply，检测链路存活（类似 ICMP ping）

LCP 报文类型：Configure-Request/Ack/Nak/Reject、Terminate-Request/Ack、Echo-Request/Reply、Code-Reject（收到不认识的 LCP 选项）

**③ NCP（网络控制协议）——上层协议怎么跑在 PPP 上**
每种网络层协议都有对应的 NCP：
- **IPCP（IP Control Protocol）**：为 IP 协议配置 PPP 链路，协商 IP 地址、DNS 服务器（这正是拨号上网时自动获取 IP 的机制）
- **IPXCP**：为 IPX 协议服务（已过时）
- **BCP**：桥接控制协议

### PPP 的链路建立过程——状态机

PPP 连接经历严格的阶段转化：

```
Dead → Establish → Authenticate → Network → Terminate
  ↑                                     │        │
  └─────────────────────────────────────┘        │
  └──────────────────────────────────────────────┘
```

1. **Dead（死亡）**：物理层未就绪或无载波信号
2. **Establish（建立）**：LCP 协商链路参数，成功后进入下一阶段
3. **Authenticate（认证）**：可选。PAP（明文密码）或 CHAP（挑战-应答）验证身份。如果认证失败，直接进入 Terminate
4. **Network（网络）**：NCP 协商网络层配置（IPCP 分配 IP 地址等），此时链路可用
5. **Terminate（终止）**：LCP 发送 Terminate-Request，链路关闭

**关键设计**：每个阶段都是"不成功便成仁"。如果在 Establish 阶段 LCP 协商失败（双方参数不可调和），链路直接终止，不会进入后续阶段。这种严格的状态机设计保证了链路的确定性和可预测性。

### PPP 的认证机制

**PAP（密码认证协议）**
- 两次握手：客户端发送用户名+密码 → 服务器确认/拒绝
- 密码**明文传输**，不够安全
- 简单但已不推荐使用

**CHAP（挑战握手认证协议）**
- 三次握手：服务器发送挑战（随机数）→ 客户端用密码哈希挑战返回 → 服务器验证
- 密码**不在网络传输**，只传哈希值
- 支持**周期性再验证**（防止会话劫持）
- 单向 CHAP 和双向 CHAP 两种模式

### PPPoE——PPP over Ethernet

**PPPoE** 让 PPP 跑在以太网上，是家庭宽带的标准协议。工作流程：

1. **发现阶段（Discovery）**：客户端广播 PADI 包寻找接入服务器（BRAS），服务器回复 PADO，客户端选择服务器发送 PADR，服务器确认（PADS），分配 Session ID
2. **会话阶段（Session）**：Session ID 确定后，以太网帧中封装 PPP 帧，走完整的 LCP → 认证 → NCP 流程

PPPoE 的帧结构：以太网头部 → PPPoE 头部 → PPP 头部 → IP 数据包。MTU 因此从 1500 降低到 1492（PPP 头 2 字节 + PPPoE 头 6 字节）。

### PPP 的设计哲学

PPP 的精髓在于**模块化**："成帧方法 + LCP + NCP"的三层架构让 PPP 极其灵活。想跑新的网络层协议？写一个新的 NCP。想换一种认证方式？LCP 协商时指定即可。想在以太网上跑 PPP？套一层 PPPoE。这种**组合优于继承**的思想，与软件工程中"合成复用原则"异曲同工。

## 🔗 相关笔记
[[数据链路层-协议与标准-HDLC]]
[[PPPoE协议详解]]
[[CHAP与PAP认证]]
[[SLIP协议]]
[[MTU路径最大传输单元]]
[[IPCP协议]]
[[NCP网络控制协议]]
[[LCP链路控制协议]]
[[OSI七层模型-数据链路层]]
[[帧中继]]

🏷️: [[计算机网络]] [[PPP]] [[数据链路层]] [[PPPoE]] [[点到点协议]] [[LCP]]
