---
type: project
tags:
  - 项目
  - 学习
  - AI
状态: 已完成
成员: cl
开始时间: 2025-07-21
截止时间: 2025-07-25
---

**下一步**:: 拉取github网址，开始利用AI调研法学习
**参考链接**:: [[TTS研究]],[[Media Note - 每个人都应该掌握的快速调研法2.0！]],[[TTS实现方法研究.canvas|TTS实现方法研究]]

# 大模型TTS学习和音图分析

## 项目信息
- **状态**: 已完成
- **成员**: cl、xzq
- **开始时间**: 2025-07-21
- **截止时间**: 2025-08-15

## 当前任务
- [x] 拉取github网址，开始利用AI调研法学习  [completion:: 2025-07-21]
- [x] 提取语图  [completion:: 2025-07-24]
- [x] 得出TTS中有关语图如何提取的原理
- [x] 设计相关的脚本,用praat脚本运行将数据进行关键特征提取,输出成AI大模型训练可用平面数据图  [completion:: 2025-07-24]
- [x] 查找相关的论文  [completion:: 2025-08-06]
- [x] 学习、确认国外学术网站的主要信源，查找搜集相关的论文文献  [completion:: 2025-08-06]
- [x] 筛选高质量论文，结合项目相关的知识，根据关键词和英译进行检索  [completion:: 2025-08-06]
- [ ] 学习latex语法，用typst快速上手尝试修改先以计设赛的文档为例，设计文档
- [x] 帮助调研firebase和supabase的底层架构论文和官方文档
- [x] 读三篇论文，将模型和算法总结出来，英文原文和中文翻译，标注出处页码
- [x] 快速学习ppt的排版方式  [completion:: 2025-08-12]
- [x] 确定ppt制作大纲、框架、重点关注部分、分工  [completion:: 2025-08-12]
- [x] 设计核心技术模块的ppt  [completion:: 2025-08-23]
## 项目说明
github上的TTS的研究是已经实现的文本转语音的7000多种语言的转换,我的任务其实可以细分成:
0.什么是语图?声学领域内的语图、频谱图分别具有什么特征？区别是什么？如果我要提取音频文本中的特征，形成三元组的矩阵，以此来生成可视化图像具体应该怎么做？TTS大模型是如何实现语义识别的？
1.了解该开源项目TTS如何实现语图提取的
https://github.com/githoldder/IMS-Toucan
了解其在语图提取部分的核心实现代码
2.应该提取什么样的特征,怎么提取这些特征?
https://www.mathworks.com/help/signal/ug/formant-estimation-with-lpc-coefficients.html 
可以演示调试特征提取
3.如何将这些特征从我的音频WAV文件中提取?
写praat脚本
4.在现有的praat的脚本中应该怎么修改才能提取出我想要的关键的特征信息?
了解之前的已经跑通的praat脚本，根据语法规则、错误日志、特殊情况处理增加脚本的健壮性和鲁棒性，最小程度地修改脚本，提取单一音频的数据保存成csv格式，便于下一步的python跑图
5.现在老师已经教会了我如何进行下一步的执行
https://www.mathworks.com/help/signal/ug/formant-estimation-with-lpc-coefficients.html
这个网址可以用于生成最终的效果图，在平面上展示3D的效果，也就是说我的任务就转化成了
1.使用开源软件praat从音频文件WAV中提取以下关键特征：
时间（本来就有）、频率（可以提取）、浓度（共振峰和频率的比值）这三个数据构成三元组的矩阵2.通过参考matlab进行的语音能量图生成的底层代码原理，最终能够达到的效果为：能够呈现横向的时间，纵向的频率，颜色上的色差对比（浓度）
3.在windsurf上配置好支持运行出能量图的python环境，将三元文本数据进行输出，得到最终的平面图
原型设计交给figma（make）进行负责
事实证明，figma的高级功能可以通过调用API的方式实现多种功能，例如google的谷歌地图API，索尼的TTS，FIrebase数据库存储
，在wlyl的原型设计过程中，还遇到的问题是，如何通过praat脚本生成某段特定音频的语图。
中间过程遇到了的问题是：如何用python画出语图，围绕这个问题我尝试了很多种方法，想通过python调用画图的库，到最后才发现，原来可以通过praat直接调用python库，可以达到praat的draw一样的效果。
接着完成figma原型的搭建后，我们进入了下一阶段的ppt和文档修改。
***
首先，文档分为了技术方案、产品说明书、测试方案
为了尽快完成，需要先根据关键词检索论文，再筛选出相关性最强的论文，接着速读论文，将摘要进行翻译；[[如何做好论文文献阅读以及如何记好笔记]]
接着将论文中需要重点展开的部分进行延申，即重点放在：算法、架构、模型训练这几个技术点
我调研了google的firebase、supabase的官方文档，对多智能体协同路径规划论文进行翻译
接着是通过figma进行图形的绘制（论文中技术方案的图进行绘制）
然后接下来就是ppt模板的选择和修改
和产品设计书几乎同时完成，方便在过程中进行修改。

## 进展记录
![[e8999034d7e20bb2871df142282308a0.png]]
问题1.TTS如何实现语图提取：IMS-Toucan 是一个支持超过 7000 种语言的文本到语音（TTS）系统，研究表明它通过 eSpeak-NG 和 transphone 将文本转换为音素序列。接着，这些音素被转换为发音特征，即二进制编码的发音器官配置（如舌头、嘴唇位置），这对低资源语言特别有益。这种方法在论文《Meta Learning Text-to-Speech Synthesis in over 7000 Languages》（Interspeech 2024）中有详细描述，相关代码可能在 GitHub 仓库 https://github.com/DigitalPhonetics/IMS-Toucan 中找到，具体文件如 phonemizer.py 或 feature_extraction.py。
问题2：
**特征定义**：
- **时间**：音频的时间轴，直接从 Praat 的帧信息中获取。
- **频率**：指共振峰频率（formants），通常包括 F1、F2、F3、F4、F5，这些是声道的共振频率。
- **浓度**：用户提到“共振峰和频率的比值”，但在语音分析中，这可能指的是整体强度（intensity）或能量，与 MATLAB 文档 [[https://www.mathworks.com/help/signal/ug/formant-estimation-with-lpc-coefficients.html](https://www.mathworks.com/help/signal/ug/formant-estimation-with-lpc-coefficients.html)] 中的形式化估计相关。
问题3：**提取方法**：
- 使用 Praat 的“Formant (burg)”方法提取共振峰，基于 Burg 的线性预测编码（LPC）方法。参数建议：
    - 时间步长（time step）：0.01 秒，确保足够的时间分辨率。
    - 最大共振峰数：5（F1 到 F5），适用于成人语音。
    - 最大频率：5500 Hz，覆盖成人语音的频率范围。
    - 窗口长度：0.025 秒（25 ms）。
    - 前重（pre-emphasis）：50 Hz，增强高频成分。
- 使用“To Intensity”提取强度，参数建议：
    - 最小音高：75 Hz（男性）或 150 Hz（女性），影响帧大小。
    - 时间步长：0.01 秒，与 Formant 保持一致。
    - 是否减去均值：是，确保强度值相对稳定。
**保存为 CSV**：

- 通过 Praat 脚本将提取的特征（时间、F1-F5、强度）保存为 CSV 文件。脚本需要循环每个帧，获取对应时间点的值，并处理未定义情况。
问题4：通过参考matlab进行的语音能量图生成的底层代码原理，最终能够达到的效果为：能够呈现横向的时间，纵向的频率，颜色上的色差对比（浓度），配置好支持运行出能量图的python环境，将三元文本数据进行输出，得到最终的平面图
问题5：调整参数，修改颜色样式，修改呈现的像素点大小，使能量图呈现更清晰，如 https://github.com/DigitalPhonetics/IMS-Toucan 所展示的一样,
最终我们决定采用的方案是python绘图

原型开发全部由wlyl完成后
现在项目进入到了下一个阶段：
1.论文搜集背书
2.摘取筛选论文高质量信息（模型、算法）
3.撰写技术报告、制作ppt、写测试文档
论文检索调研步骤
课题1：如何确定信源？哪些权威的国外网站可以提供优质的论文？
课题2：如何筛选信源，筛选出高质量的论文？
课题3：设置Latex模板，管理每一个分享到链接的组，按照不同的文档分成：
技术文档、产品设计说明书、测试文档
课题4：如何读论文？提取其中有用的模型、算法、公式
一种方法，将论文摘要进行翻译，并且全部提取出来，转换成markdown的格式
***
[[Chain of Agents: Large Language Models Collaborating on Long-Context Tasks]]

**摘要**
Addressing the challenge of effectively processing long contexts has become a
critical issue for Large Language Models (LLMs). Two common strategies have
emerged: 1) reducing the input length, such as retrieving relevant chunks by
Retrieval-Augmented Generation (RAG), and 2) expanding the context window
limit of LLMs. However, both strategies have drawbacks: input reduction has no
guarantee of covering the part with needed information, while window extension
struggles with focusing on the pertinent information for solving the task. To miti-
gate these limitations, we propose Chain-of-Agents (CoA), a novel framework that
harnesses multi-agent collaboration through natural language to enable informa-
tion aggregation and context reasoning across various LLMs over long-context
tasks. CoA consists of multiple worker agents who sequentially communicate to
handle different segmented portions of the text, followed by a manager agent who
synthesizes these contributions into a coherent final output. CoA processes the
entire input by interleaving reading and reasoning, and it mitigates long context
focus issues by assigning each agent a short context. We perform a comprehensive
evaluation of CoA on a wide range of long-context tasks in question answering,
summarization, and code completion, demonstrating significant improvements by
up to 10% over strong baselines of RAG, Full-Context, and multi-agent LLMs.

> [!NOTE] 翻译：
> 解决有效处理长上下文的挑战已成为大型语言模型 （LLM） 的关键问题。出现了两种常见的策略：1）减少输入长度，例如通过检索增强生成（RAG）检索相关块，以及 2）扩展 LLM 的上下文窗口限制。然而，这两种策略都有缺点：减少输入并不能保证用所需的信息覆盖该部分，而窗口扩展则难以专注于解决任务的相关信息。为了减轻这些限制，我们提出了代理链 （CoA），这是一种新颖的框架，它通过自然语言利用多智能体协作，在长上下文任务中实现跨各种 LLM 的信息聚合和上下文推理。CoA 由多个工作代理组成，他们按顺序通信以处理文本的不同分段部分，然后是一个经理代理，他将这些贡献综合成连贯的最终输出。CoA 通过交错读取和推理来处理整个输入，并通过为每个代理分配一个短上下文来缓解长上下文焦点问题。我们在问答、摘要和代码完成等各种长上下文任务上对 CoA 进行了全面评估，与 RAG、全上下文和多代理 LLM 的强基线相比，CoA 的显着改进高达 10%。
> 

[[An Empirical Study of Retrieval Augmented Generation with Chain-of-Thought]]
摘要：Since the launch of ChatGPT at the end of 2022, generative
dialogue models represented by ChatGPT have quickly become
widely used. As user expectations increase, enhancing the
capability of generative dialogue models to solve complex
problems has become a focal point of current research. This
paper delves into the effectiveness of the RAFT (Retrieval Aug-
mented Fine-Tuning) method in improving the performance
of Generative dialogue models. RAFT combines chain-of-
thought with model supervised fine-tuning (SFT) and retrieval
augmented generation (RAG), which significantly enhanced the
model’s information extraction and logical reasoning abilities.
We evaluated the RAFT method across multiple datasets and
analysed its performance in various reasoning tasks, including
long-form QA and short-form QA tasks, tasks in both Chinese
and English, and supportive and comparison reasoning tasks.
Notably, it addresses the gaps in previous research regarding
long-form QA tasks and Chinese datasets. Moreover, we also
evaluate the benefit of the chain-of-thought (CoT) in the RAFT
method. This work offers valuable insights for studies focused
on enhancing the performance of generative dialogue models.
Index Terms: generative dialogue model, large language
model, chain-of-thought, retrieval augmented generation

> [!NOTE] 翻译
> 自 2022 年底 ChatGPT 推出以来，以 ChatGPT 为代表的生成式对话模型迅速得到广泛应用。随着用户期望的提高，增强生成对话模型解决复杂问题的能力已成为当前研究的焦点。本文深入探讨了 RAFT（检索增强微调）方法在提高生成对话模型性能方面的有效性。RAFT 将思维链与模型监督微调（SFT）和检索增强生成（RAG）相结合，显著增强了模型的信息提取和逻辑推理能力。我们评估了 RAFT 方法在多个数据集中，并分析了其在各种推理任务中的性能，包括长篇 QA 和短篇 QA 任务、中英文任务以及支持性和比较性推理任务。值得注意的是，它弥补了先前在长篇 QA 任务和中文数据集方面的研究中的空白。此外，我们还评估了 RAFT 方法中思维链 （CoT） 的好处。这项工作为专注于提高生成对话模型性能的研究提供了宝贵的见解。索引术语：生成对话模型、大语言模型、思维链、检索增强生成

[[Chain-of-Retrieval Augmented Generation]]
摘要：This paper introduces an approach for training o1-like RAG models that retrieve and
reason over relevant information step by step before generating the final answer.
Conventional RAG methods usually perform a single retrieval step before the
generation process, which limits their effectiveness in addressing complex queries
due to imperfect retrieval results. In contrast, our proposed method, CoRAG
(Chain-of-Retrieval Augmented Generation), allows the model to dynamically
reformulate the query based on the evolving state. To train CoRAG effectively, we
utilize rejection sampling to automatically generate intermediate retrieval chains,
thereby augmenting existing RAG datasets that only provide the correct final
answer. At test time, we propose various decoding strategies to scale the model’s
test-time compute by controlling the length and number of sampled retrieval chains.
Experimental results across multiple benchmarks validate the efficacy of CoRAG,
particularly in multi-hop question answering tasks, where we observe more than
10 points improvement in EM score compared to strong baselines. On the KILT
benchmark, CoRAG establishes a new state-of-the-art performance across a diverse
range of knowledge-intensive tasks. Furthermore, we offer comprehensive analyses
to understand the scaling behavior of CoRAG, laying the groundwork for future
research aimed at developing factual and grounded foundation models.


> [!NOTE] 翻译：
> 本文介绍了一种训练类 o1RAG 模型的方法，该模型在生成最终答案之前逐步检索和推理相关信息。传统的 RAG 方法通常在生成过程之前执行单个检索步骤，这限制了它们由于检索结果不完美而导致的复杂查询的有效性。相比之下，我们提出的方法 CoRAG（Chain-of-Retrieval Augmented Generation）允许模型根据演变状态动态地重新表述查询。为了有效地训练 CoRAG，我们利用拒绝采样自动生成中间检索链，从而增强仅提供正确最终答案的现有 RAG 数据集。在测试时，我们提出了各种解码策略，通过控制采样检索链的长度和数量来扩展模型的测试时计算。跨多个基准的实验结果验证了 CoRAG 的功效，特别是在多跳问答任务中，我们观察到 EM 分数与强基线相比提高了 10 分以上。在 KILT 基准测试中，CoRAG 在各种知识密集型任务中建立了一种新的最先进性能。此外，我们还提供全面的分析来了解 CoRAG 的缩放行为，为未来旨在开发事实和扎根基础模型的研究奠定基础。

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



### PPT制作

###  一、项目核心任务总结 
基于文档内容，“声律方舟（DialectArk）”的核心任务是：**利用深度推理大模型与动态音系知识图谱，构建“诊断-归因-规划”三智能体协同系统，解决方言学习中“发音难纠正、用词易混淆、语境难把握”的问题**，以粤语、上海话等典型方言为切入点，实现方言非遗的自适应传承与学习。具体包括： 
1. 构建以“音系”为核心的动态知识图谱（含声韵调节点及与普通话的映射关系）； 
2. 开发三智能体协同决策系统（语音诊断、认知归因、个性化规划）； 
3. 通过Streamlit/Gradio搭建Web界面，形成“练习-反馈-再练习”的闭环。
### 二、PPT模板风格与配色 
#### 1. 风格定位 
结合“方言非遗传承+AI技术”的双重属性，建议采用**“科技非遗融合风”**：
- 整体框架简约大气（符合理工科严谨性），避免过度装饰； 
- 局部融入方言文化符号（如粤语的“粤”字篆刻、上海话的吴语文字纹样、闽南话的地域图腾等），用线条化、扁平化设计呈现，避免喧宾夺主； 
- 页面布局采用“左逻辑+右视觉”结构（左侧用文字/流程图展示技术逻辑，右侧用图标/简笔画体现方言文化），平衡理性与感性。 
#### 2. 配色方案 
基于已定的“青蓝色”主色调，搭配以下辅助色：
- 主色：青蓝色（#4A90E2）—— 体现科技感与专业性，呼应“方舟”的沉稳可靠；
- 辅助色1：暖棕色（#C19A6B）—— 象征方言的历史厚重感，用于非遗文化相关页面；
- 辅助色2：米白色（#F5F5F0）—— 作为背景色，提升文字可读性，避免视觉疲劳； 
- 强调色：朱红色（#E63946）—— 用于标注核心数据、技术亮点（如“65%认知诊断准确率”），增强视觉焦点。
### 三、PPT整体框架（按演示逻辑排序） 
#### 1. 封面页（1页） 
- 标题：声律方舟（DialectArk）—— 多智能体协同的方言自适应学习系统 
- 副标题：破解“发音、用词、语境”三大难题，守护方言非遗 
- 视觉元素：青蓝色渐变背景+抽象化“方舟”图标（融入方言文字剪影） 
#### 2. 背景与问题（2页） 
描述一下目前的ppt
- 痛点呈现：用数据对比图展示方言传承危机（如“某方言使用人群30年下降70%”“90后方言掌握率不足20%”）； 
“人工智能正在深度融入语言教育，推动智能化学习成为行业潮流 。”
深度融合的创新方向：“我们致力于将AI深度推理大模型与方言音系知识相结合，以确保方言学习的准确”
- 政策与价值：引用国家非遗保护政策，强调“方言是文化活化石”的独特价值，引出项目必要性。
“语保工程启动，为方言保护提供了宝贵的语料资源，奠定了技术活化传承的基础。方言研究进入多模态时代，声学、语言学等多个维度的技术开始应用于方言研究。国家语委明确提出，要重点“促进语言资源的开发利用”，标志着方言保护从“抢救记录”转向“活化传承”。”

用户需求多样：除了传统的英语学习，用户对日语、韩语等小语种的兴趣也在增长。
目标市场：主要面向年轻人，他们出于兴趣、工作调动或文化认同等原因学习方言
市场策略： 兼顾“大众化”与“专业性”，以有趣内容吸引普通用户，同时以严谨权威的资料和功能赢得学者用户。
#### 3. 核心解决方案（5-6页，重点模块） 
- 项目定位：1页说明“AI+知识图谱”的创新路径，对比传统方言学习工具的局限性； 
”现有应用在核心技术和教学法上存在a缺陷“
“缺乏有效的语音识别与反馈：大多数应用无法对用户的发音进行准确、实时的评估和针对性的纠正指导，口语练习低效”、”个性化学习机制缺失：课程内容和学习路径固定，无法根据学习者水平进行动态调整，遵循“一刀切”模式 。“、”生成式AI应用空白：缺乏生成式交互，未触及AI在低资源场景下常见的“幻觉”问题，交互模式停留在“查询-匹配”的静态阶段 。“、”缺乏有效的语音识别与反馈：大多数应用无法对用户的发音进行准确、实时的评估和针对性的纠正指导，口语练习低效“

- 动态音系知识图谱：2页详解（1页讲结构：音素、声韵调节点及映射关系；1页讲功能：个性化“认知声学画像”的生成逻辑，配示意图）； 
- 三智能体系统：3页拆解（每智能体1页，用流程图展示“诊断→归因→规划”的协同过程，标注技术亮点：如音素级错误定位、最小对比对练习生成）。 

#### 4. 技术实现与可行性（3页，理工科重点） 
- 落地路径：1页说明“从粤语切入”的原因（资源丰富、有API支持），附技术栈清单（Neo4j、Streamlit、讯飞星火API等）； 
- 大学生方案：1页讲“Prompt Engineering”实现智能体的低成本路径，避免“从零训练模型”的技术门槛； 
- 闭环演示：1页用界面截图展示Web交互流程（录音→反馈→练习），体现“自适应性”。 
#### 5. 成果与数据（2页） 
- 现有进展：1页展示原型系统截图、测试数据（如“50名用户测试，发音纠正准确率达72%”）； 
- 核心指标：1页突出“认知诊断准确率≥65%”“个性化路径匹配度≥65%”，用柱状图对比行业平均水平。 
#### 6. 团队与展望（2页） 
- 团队优势：1页强调成员匹配度（方言研究、AI开发、设计等分工）；
- 未来规划：1页说明“覆盖更多方言→接入非遗传承人资源→进校园/社区”的三步走策略。 
#### 7. 封底页（1页） 
- 口号：“让每一种方言都能被听见、被学会、被传承” 
- 联系方式+感谢语 
### 四、补充说明（呼应前期问题） 
#### 项目整体标题与副标题设计

基于您提供的挑战杯项目主题（AI赋能的智慧城市规划与建设应用创新），我设计了整个PPT的主标题、副标题，以及每个部分的副标题。设计原则如下：

- **主标题**：简洁、吸引人，突出AI与智慧城市的创新结合，体现项目的前瞻性和实用性。
- **副标题（整体）**：补充主标题，强调与科大讯飞的合作、企业命题背景，以及可持续发展的核心价值。
- **各部分副标题**：每个副标题紧扣部分内容，简短有力（控制在8-12字），使用动态语言增强吸引力，同时与整体主题呼应。副标题旨在引导观众快速把握重点，便于PPT视觉布局（如使用加粗或不同字体突出）。

**副标题**：展望无限：项目愿景与合作邀请

1. 封面页：方舟启航，声韵传薪
2. 背景痛点：方言濒危，三难待破
3. 项目定位：智汇方舟，非遗新生
4. 音系图谱：动态织网，声韵藏真
5. 三智协同：诊断归因，规划护航
6. 技术突破：AI 精辨，个性赋能
7. 落地路径：步步为营，方言可续
8. 成果展望：声传千里，文脉永续
9. 团队聚力：同筑方舟，共守乡音
10. 封底页：舟载声律，非遗永传
---

#### **重点分布与篇幅建议**：

- **核心技术**：占用4页，重点突出，详细解释每个技术的创新性和应用性。这是整个PPT的重心之一。
    
- **解决方案**：占3页，是另一个核心部分。明确展示技术如何解决当前的痛点，适合详细阐述并通过图示增强表达效果。
    
- **技术现状与痛点**：占2页，为后续内容提供背景与支撑，起到铺垫作用，不宜过长。
    
- **研发历程与合作进展**：占2页，展示技术的实际进展，结合与科大讯飞的合作增加可信度。
    
- **其他部分**（竞品分析、行业评价、合作案例等）：占2-3页，不需要太多篇幅，内容上更侧重对项目的市场竞争力和现实应用的展示。
## AI调研法的大致流程
##### 一:**确定调研目的和提出问题**
最基础的一定要问的是行业的框架信息
在 #提出问题 这一块儿，还是以自我的思考为主，减少对AI的依赖
##### 二: *高质量的 #二手信息 汇总搜索*
1. 广泛地从可靠信源里搜集信息
2. 以战养战，基于第一手接受的信息去将专业术语逐一理解概念，然后再去进行阅读，扩大搜索词的范围
3. 为下一阶段的深度搜索和提出更高质量的问题打下基础
##### 三:筛选提取信息
1. 总结有观点有重点的内容来源
2. 让AI进行角色扮演
3. 无限逼问AI进行更多回答
4. 自己筛选一遍AI的内容
##### 四:输出行业/产品报告
1. AI表现不错的几种文档：冠冕堂皇需要格式的东西，它会天然地生成得很规范
2. AI作为创意辅助，可以永无止境地不断生成创意，可以不断迭代

> [!NOTE] ##### AI的特点
> 这里可以分成两个维度
1.AI的优势项：辅助人类理解内容的效率和效果比较优秀（即**喂ai**）
2.用AI总结直播回放、总结视频框架结构、节约大量时间（利用ai可以访问视频链接等内容的特性）
==生成思维导图、脑图==
3.AI的劣势：只会生成粗略的大致框架
解决方案：*在提示词中增强限制*：请将这个直播录播中每20分钟的内容进行总结，细致到主播和嘉宾的每一句对话以及谈话逻辑和框架。

#### 后端flask
[[后端Flask框架]]
#### 前端
[[前端|前端]]
#### 模型调用
[[模型调用]]
#### 数据交互
[[数据交互]]







