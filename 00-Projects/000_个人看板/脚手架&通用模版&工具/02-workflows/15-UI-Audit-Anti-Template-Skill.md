# AI 前端 UI 减法审查与反模板巡检 Skill

---

## 1. Skill 定位

本 Skill 用于审查已经生成或已经实现的 UI，识别 AI 味、模板味、装饰性堆叠和错误的信息层级，并给出可执行的删改方案。

它解决的问题不是“这个页面漂不漂亮”，而是：

1. 是否像真实产品，而不是 AI SaaS 模板。
2. 是否由信息结构支撑，而不是由图标和卡片撑场面。
3. 是否每个视觉元素都帮助用户理解、比较、决定或行动。
4. 是否能长期高频使用，而不是只适合截图展示。

相关 Skill：

- 生成前约束：[[13-UI-Taste-Constraint-Skill]]
- 审美参考：[[12-UI-UX-Reference-List]]

---

## 2. 触发场景

当出现以下情况时使用：

1. AI 已经生成页面，需要检查是否同质化。
2. 页面出现大圆角卡片、渐变背景、大图标、假数据、空洞模块。
3. 用户说“看起来像模板”“AI 味太重”“太花了”“不够产品化”。
4. 需要做 UI review、设计走查、截图批注、前端重构前审查。
5. 需要把页面从“展示功能”改成“支持工作流”。

---

## 3. 输入

| 输入项 | 说明 |
|---|---|
| 页面截图或代码 | 最好同时提供截图和组件代码 |
| 产品类型 | 开发者工具、后台、研究工作台、学习系统等 |
| 核心用户任务 | 用户到底要完成什么 |
| 目标 taste | 可引用 [[13-UI-Taste-Constraint-Skill]] 中的 profile |
| 约束 | 技术栈、组件库、品牌色、必须保留的信息 |

---

## 4. 输出格式

```md
# UI Taste Review

## 1. 总体判断
- 当前气质：
- 主要问题：
- 最应该保留：
- 最应该删除：

## 2. 反模板问题
| 问题 | 位置 | 为什么像模板 | 修改动作 |
|---|---|---|---|

## 3. 信息结构问题
| 问题 | 影响 | 修改动作 |
|---|---|---|

## 4. 视觉减法清单
- 删除：
- 弱化：
- 合并：
- 移到二级：
- 改成真实数据：

## 5. 布局改造建议
- 当前布局：
- 建议布局：
- 原因：

## 6. 可执行修改任务
- Task 01：
- Task 02：
- Task 03：

## 7. 通过标准
- [ ] 页面不依赖装饰图标撑内容
- [ ] 主任务路径清晰
- [ ] 颜色只表达状态、优先级或动作
- [ ] 卡片数量被压缩到必要范围
- [ ] 信息密度符合产品类型
```

---

## 5. 审查原则

### 5.1 核心判断句

```text
If a visual element does not help the user understand, compare, decide, or act, remove it.
```

中文执行：

> 如果一个视觉元素不能帮助用户理解、比较、决定或行动，就删除。

### 5.2 四类删减动作

| 动作 | 适用对象 | 示例 |
|---|---|---|
| 删除 | 纯装饰、假指标、重复卡片 | 删除彩色图标 tile |
| 弱化 | 次要信息、低频操作 | 把大按钮改成 inline action |
| 合并 | 重复模块、相似状态 | 合并多个统计卡为状态摘要行 |
| 下钻 | 低频详情、历史记录 | 移到 inspector、drawer、log panel |

---

## 6. 反模板巡检清单

### 6.1 高危模板信号

发现以下内容，默认标记为风险：

- 页面首屏是巨大 hero，但产品其实是工具。
- 三列或四列 feature cards 解释功能。
- 每张卡都有一个大号彩色图标。
- 每个模块都是圆角卡片 + 阴影。
- 渐变背景、光斑、玻璃拟态没有信息作用。
- 指标数字没有上下文，例如 `98%`、`24/7`、`10x`。
- 图标比文字更抢眼。
- 页面像组件库展示，而不是工作流。
- 所有内容都是静态展示，没有状态、时间、进度、错误、约束。
- 颜色只是为了好看，不表达含义。

### 6.2 信息真实性巡检

每个模块都要追问：

1. 这里是否有真实记录？
2. 是否有状态？
3. 是否有时间戳？
4. 是否有元数据？
5. 是否有下一步动作？
6. 是否有失败/异常/约束？
7. 是否能支持比较？
8. 是否能支持决策？

如果连续 3 个问题回答“没有”，该模块大概率是装饰性填充。

---

## 7. 产品类型审查重点

### 7.1 Developer Tool

应该出现：

- 队列
- 日志
- 文件树
- 任务状态
- 错误原因
- 元数据
- Diff / preview
- Command bar
- Inspector

不应该主导页面：

- 大图标入口
- 营销标语
- 随机渐变
- 没有上下文的成功率

### 7.2 Research Workspace

应该出现：

- 文档窗格
- 引文元数据
- 摘录
- 注释
- 阅读进度
- Backlinks
- Reference list

不应该主导页面：

- 游戏化徽章
- 彩色 dashboard
- 装饰性插画
- 大面积空白宣传文案

### 7.3 Enterprise System

应该出现：

- 表格
- 过滤器
- 表单
- 权限
- 审计日志
- 状态标签
- 批量操作
- 错误提示

不应该主导页面：

- 夸张动效
- 趣味插画
- 过大卡片
- 不可扫描的营销布局

### 7.4 Premium Consumer App

应该出现：

- 明确主任务
- 温和但克制的视觉反馈
- 真实进度
- 有帮助的微文案
- 减少焦虑的状态说明

不应该主导页面：

- 幼稚游戏化
- 成就图标泛滥
- 随机 badge
- 情绪过载

---

## 8. 审查 Prompt

### 8.1 通用减法审查

```text
Perform a strict UI taste review.

Your role:
You are a senior product designer who dislikes decorative AI-generated SaaS templates.
You prefer clarity, density, workflow support, and long-term usability.

Review the UI and identify:
1. Decorative elements that should be removed.
2. Cards or sections that only exist to fill space.
3. Fake metrics or fake content.
4. Icons that are too illustrative or too dominant.
5. Colors that do not communicate state, priority, or action.
6. Layout patterns that feel like a template rather than a workflow.
7. Missing realistic states, metadata, timestamps, logs, constraints, warnings, and actions.

Then produce a concrete refactor plan:
- delete
- weaken
- merge
- move to secondary panel
- replace with real information
- restructure layout
```

### 8.2 针对代码修改

```text
Audit this UI implementation for AI-template smell.

Do not rewrite everything.
Make surgical changes that improve product taste:
- reduce decorative cards
- improve information density
- replace fake content with realistic operational data
- make layout support the user's workflow
- use color only for state/action/risk
- keep icons small and utilitarian
- remove unnecessary shadows, gradients, blobs, and oversized empty areas

After changes, explain what was removed, what was weakened, and what was made more useful.
```

### 8.3 针对截图批注

```text
Review this screenshot as a product-grade UI.

Mark each issue as:
- P1: breaks workflow or comprehension
- P2: creates template/AI smell
- P3: visual polish issue
- P4: optional preference

For every issue, provide:
- location
- problem
- reason
- concrete fix
```

---

## 9. 最小改造策略

如果时间有限，优先做这 6 件事：

1. 删除首屏无意义 hero。
2. 把 feature card grid 改成任务队列、列表、表格或工作台。
3. 把大图标改成小型 utility icon。
4. 把假指标改成真实状态、进度、错误和时间戳。
5. 把彩色装饰改成语义色。
6. 增加 inspector/detail drawer/log panel，让页面像真实工具。

---

## 10. 完成标准

审查后页面必须满足：

1. 用户一眼知道当前要处理什么。
2. 用户能看到真实状态，而不是抽象功能宣传。
3. 主操作路径不被装饰干扰。
4. 视觉层级来自排版、间距、对齐和信息结构。
5. 图标小而有用，颜色少而有含义。
6. 页面可以被真实用户每天使用，而不是只适合发截图。
