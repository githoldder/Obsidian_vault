
```mermaid
flowchart TD
    %% 定义节点
    D[数据 Data<br>原始、离散的事实与符号<br>例：25, “北京”, “℃”]
    I[信息 Information<br>经过处理、有关联的数据<br>例：北京气温 25℃]
    K[知识 Knowledge<br>结构化、可应用的信息体系<br>例：25℃是舒适温度，适合户外活动]
    W[智慧 Wisdom<br>运用知识做出判断与决策<br>例：根据温度知识，决定今天穿衬衫出门]

    %% 连接节点，方向从上到下
    D --> I
    I --> K
    K --> W

    %% 样式设置，模拟金字塔形状和颜色渐变
    style D fill:#e8f5e9,stroke:#2e7d32,stroke-width:2px,width:260px,height:100px
    style I fill:#c8e6c9,stroke:#2e7d32,stroke-width:2px,width:260px,height:100px
    style K fill:#a5d6a7,stroke:#2e7d32,stroke-width:2px,width:260px,height:120px
    style W fill:#81c784,stroke:#2e7d32,stroke-width:2px,width:260px,height:120px

    %% 添加说明文字
    T1[“理解与关联”] -.-> D
    T2[“模式与原理”] -.-> I
    T3[“判断与行动”] -.-> K

    %% 隐藏说明文字的边框
    style T1 fill:none,stroke:none
    style T2 fill:none,stroke:none
    style T3 fill:none,stroke:none
```

## 模型核心要点

| 层级                   | 核心问题        | 特征            | 示例                                           |
| :------------------- | :---------- | :------------ | :------------------------------------------- |
| **数据 (Data)**        | 是什么？        | 原始、未处理、无上下文   | `42`, `“错误”`, `2023-10-27`                   |
| **信息 (Information)** | 谁？什么？何时？何地？ | 经过处理、有关联、有意义  | `用户ID 42 于 2023-10-27 登录失败`                  |
| **知识 (Knowledge)**   | 如何做？        | 结构化、体系化、可应用   | `登录失败通常由密码错误或账户锁定引起，可检查密码或联系管理员`             |
| **智慧 (Wisdom)**      | 为什么做？       | 洞察、判断、决策、价值导向 | `鉴于该用户多次失败登录且非工作时间，可能为攻击尝试，建议暂时封锁其IP并通知安全团队` |

**演进关系**：数据 →（**上下文与关联**）→ 信息 →（**模式归纳与理解**）→ 知识 →（**经验与伦理应用**）→ 智慧