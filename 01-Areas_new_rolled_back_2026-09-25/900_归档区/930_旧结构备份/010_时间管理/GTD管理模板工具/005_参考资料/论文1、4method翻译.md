### 第一篇论文 - 3. Method

#### 原文

**3 Method**

Figure 1 shows the overview of our Chain-of-Agents (CoA) framework, containing two stages. In stage 1, long context is split into chunks where each chunk can be processed by a worker agent. Then, the worker agents communicate sequentially to produce evidence over the entire context. In stage 2, a manager agent consumes the knowledge from the chain of workers to generate the final answer.

To formulate the task, we denote a long-context sample as (x, y, q), where x is the input of n tokens, y is the output of n tokens, q is an optional query. The long context x is segmented into chunks {c_1, c_2, ..., c_l}, where each chunk c_i is processed by a worker agent W_i. Each worker agent W_i receives the previous communication unit CU_{i-1} (if i > 1) and the current chunk c_i, and generates a new communication unit CU_i that summarizes the information from c_i and updates CU_{i-1}. The first worker agent W_1 starts with an empty or initial CU_0. The sequence of communication units {CU_1, CU_2, ..., CU_l} forms a chain of evidence that aggregates information across the entire context.

The manager agent M, in stage 2, receives the final communication unit CU_l from the last worker agent W_l and uses it to generate the final output y, which could be an answer to q (for query-based tasks) or a summary/completion (for non-query tasks). The process can be described as follows:

1. **Chunking**: Split the input x into l chunks {c_1, c_2, ..., c_l} such that each chunk fits within the context window limit k of the LLM, i.e., |c_i| ≤ k for all i.
2. **Sequential Processing by Worker Agents**: For i = 1 to l:
    - If i = 1, W_1 processes c_1 and generates CU_1.
    - For i > 1, W_i receives CU_{i-1} and c_i, and generates CU_i by updating CU_{i-1} with new information from c_i.
3. **Final Synthesis by Manager Agent**: M receives CU_l and generates the final output y based on CU_l and q (if applicable).

The communication between agents is facilitated through natural language, where each CU_i is a summary or evidence snippet that is concise enough to fit within the context window k. This ensures that each agent focuses on a short context, mitigating the "lost in the middle" problem of long context processing.

The time complexity of this process is analyzed in Section 3.3, showing that CoA reduces the computational burden compared to full-context processing by leveraging the chain structure. Specifically, the encoding time for CoA is O(nk) instead of O(n^2) for full-context, where n is the total number of input tokens and k is the context window limit.

#### 中文翻译

**3 方法**

图 1 展示了我们的 Agent 链 (CoA) 框架的概览，该框架包含两个阶段。在第一阶段，长上下文被分割成块，每个块都可以由一个工作代理处理。然后，工作代理按顺序通信，以产生整个上下文的证据。在第二阶段，管理代理消耗来自工作代理链的知识来生成最终答案。

为了表述任务，我们将一个长上下文样本表示为 (x, y, q)，其中 x 是 n 个 token 的输入，y 是 n 个 token 的输出，q 是可选的查询。长上下文 x 被分割成块 {c_1, c_2, ..., c_l}，每个块 c_i 由一个工作代理 W_i 处理。每个工作代理 W_i 接收前一个通信单元 CU_{i-1}（如果 i > 1）和当前块 c_i，并生成一个新的通信单元 CU_i，该单元总结了来自 c_i 的信息并更新了 CU_{i-1}。第一个工作代理 W_1 从一个空的或初始的 CU_0 开始。通信单元序列 {CU_1, CU_2, ..., CU_l} 形成一个证据链，聚合了整个上下文的信息。

在第二阶段，管理代理 M 接收来自最后一个工作代理 W_l 的最终通信单元 CU_l，并使用它生成最终输出 y，该输出可以是 q 的答案（对于基于查询的任务）或摘要/完成（对于非查询任务）。该过程可以描述如下：

1. **分块**：将输入 x 分割成 l 个块 {c_1, c_2, ..., c_l}，使每个块都适合 LLM 的上下文窗口限制 k，即 |c_i| ≤ k 对所有 i 成立。
2. **工作代理的顺序处理**：对于 i = 1 到 l：
    - 如果 i = 1，W_1 处理 c_1 并生成 CU_1。
    - 对于 i > 1，W_i 接收 CU_{i-1} 和 c_i，并通过用来自 c_i 的新信息更新 CU_{i-1} 来生成 CU_i。
3. **管理代理的最终综合**：M 接收 CU_l 并基于 CU_l 和 q（如果适用）生成最终输出 y。

代理之间的通信是通过自然语言实现的，每个 CU_i 是一个摘要或证据片段，足够简洁以适合上下文窗口 k。这确保每个代理都关注一个短的上下文，从而缓解了长上下文处理中的“迷失在中间”问题。

该过程的时间复杂度在第 3.3 节中分析，表明 CoA 通过利用链结构减少了与完整上下文处理相比的计算负担。具体而言，CoA 的编码时间为 O(nk)，而完整上下文的编码时间为 O(n^2)，其中 n 是总输入 token 数，k 是上下文窗口限制。

---

### 第二篇论文 - III. MACCPP-MPC Algorithm

#### 原文

**III. MACCPP-MPC Algorithm**

A two-stage algorithm, called multi-agent coverage path planning based on an MPC technique (MACCPP-MPC), is proposed to guide agents in achieving complete coverage in unknown complex environments with the minimum makespan. The first stage is the dispersion stage focusing on the reasonable dispersion of agents. And the second one is the dispersion stage focused to achieve an effective coverage with the environment via the interaction and cooperation among agents. In MACCPP-MPC, the behavior-guide-point is introduced and a new motion mode is presented.

**A. The Dispersion Stage of the MACCP-MPC**

In this stage, each agent iteratively optimizes its decision according to the observation, the behavior-guide-point and the reward function to achieve a reasonable dispersion. Algorithm 1 illustrates the pseudocode of the dispersion stage.

In line 2 of Algorithm 1, the grid value matrix corresponding to each agent is calculated. Lines 4 to 9 describe the decision making process of each agent. The function URPC(i, O^i) calculates the set of uncovered reachable points of each agent. The function DG(i, O^i, N^i) determines the next goal point based on the current observation O^i and neighborhood information N^i.

**C. Cooperation Mechanism Based on the Model Predictive Control**

The concept of associated individuals is introduced to decouple the problem based on the positions of agents, which can effectively reduce the dimension of the decision space. In each neighborhood, the model predictive control (MPC) technology is adopted to predict the paths. Through proximity interaction, the prediction results are fused, evaluated, and sorted, based on which decisions are gained.

#### 中文翻译

**III. MACCPP-MPC 算法**

提出了一种两阶段算法，称为基于 MPC 技术的多代理覆盖路径规划（MACCPP-MPC），用于指导代理在未知复杂环境中以最小工期实现完全覆盖。第一阶段是分散阶段，关注代理的合理分散。第二阶段是覆盖阶段，通过代理之间的互动和合作实现与环境的有效覆盖。在 MACCPP-MPC 中，引入了行为引导点并提出了新的运动模式。

**A. MACCP-MPC 的分散阶段**

在这一阶段，每个代理根据观察、行为引导点和奖励函数迭代优化其决策，以实现合理的分散。算法 1 说明了分散阶段的伪代码。

在算法 1 的第 2 行，计算了每个代理对应的网格值矩阵。第 4 到 9 行描述了每个代理的决策过程。函数 URPC(i, O^i) 计算每个代理的未覆盖可达点集。函数 DG(i, O^i, N^i) 根据当前观察 O^i 和邻域信息 N^i 确定下一个目标点。

**C. 基于模型预测控制的合作机制**

引入了关联个体的概念，基于代理的位置来解耦问题，从而有效减少决策空间的维度。在每个邻域中，采用模型预测控制（MPC）技术预测路径。通过邻近交互，融合、评估和排序预测结果，并基于此获得决策。


> [!NOTE] 注明
> 以上为Grok使用OCR识别翻译效果，下面是纯文本粘贴的翻译效果，内容更完整

---

### **3. 方法3**  
**框架总览**（图1所示）：  
CoA框架包含两个阶段：  
1. **阶段1**：  
   - 将长上下文分割为多个**文本块**（chunks）  
   - 每个文本块由**工作智能体**（worker agent）处理  
   - 工作智能体**顺序通信**，在整个上下文中生成证据  
2. **阶段2**：  
   - **管理智能体**（manager agent）整合工作智能体链的知识  
   - 生成最终答案  

**算法1 智能体链（CoA）**  
```python
输入: 源输入 x, 查询 q, 智能体窗口大小 k, 大语言模型 LLM(∗)
输出: 查询的答案

1. 将 x 分割为 l 个文本块 {c₁, c₂, ···, cₗ}（满足 |cᵢ| < k）
2. 初始化 CU₀ ← 空字符串
3. for i in 1,2,...,l:
4.     CUᵢ ← LLMᵂⁱ(Iᵂ, CUᵢ₋₁, cᵢ, q)  # 工作智能体处理
5. end for
6. return LLMᴹ(Iᴹ, CUₗ, q)          # 管理智能体生成答案
```

**任务形式化定义**：  
- 长上下文样本表示为三元组 $(x, y, q)$：  
  - $x$：输入（含 $n$ 个token）  
  - $y$：输出（含 $m$ 个token）  
  - $q$：可选查询  
- 给定LLM的**上下文窗口限制**为 $k$ 个token（通常 $k \ll n$）  
- 目标：在有限上下文窗口下生成 $y$  
- 解决方案：  
  将源文本 $x$ 分割为块序列 $x = \{c_1, c_2, \cdots, c_l\}$  
  确保每个块 $c_i$ 可完整输入LLM骨干模型  

---

#### **3.1 阶段1：工作智能体 - 分段理解与链式通信**  
在阶段1中，CoA框架包含 $l$ 个**顺序工作智能体**。每个工作智能体 $W_i$ 的输入为：  
1. 源文本 $x$ 的分块 $c_i$  
2. 查询 $q$  
3. 工作智能体任务指令 $I^W$  
4. 前序智能体传递的**通信单元** $CU_{i-1}$  

通信为**单向传递**（$W_{i-1} \rightarrow W_i$）。工作智能体处理这些输入并输出新的通信单元：  
$$  
CU_i = LLM^{W_i}(I^W, CU_{i-1}, c_i, q) \tag{1}  
$$  

**通信单元特性**：  

| 任务类型 | $CU$ 内容 |  
|----------|-----------|  
| **问答任务** | 供管理智能体使用的证据 |  
| **摘要任务** | 前序文本的摘要 |  
| **代码补全** | 含函数/类名解释的代码摘要 |  
（多任务有效性证明CoA的灵活性，详见附录C）  

**技术优势**：  
1. **全上下文感知**：  
   - 通过链式通信，末位工作智能体可感知**完整输入**  
   - 无论输入长度如何，最终覆盖整个接收域  
2. **动态扩展性**：  
   - 通过调整工作智能体数量 $l$，适配任意长度输入  

**协作必要性验证**（图1左侧示意）：  
通过三工作智能体案例证明：  
1. $W_1$：当 $c_1$ 无法回答问题时，生成**相关证据**  
2. $W_2$：基于 $W_1$ 的部分答案，结合当前文本完成**跨智能体推理链**  
3. $W_3$：当 $c_3$ 无相关信息时，直接继承 $CU_2$ 并将正确答案置于 $CU_3$ 首部（附录F.4）  

**关键结论**：  
若采用独立工作模式（如树状通信），当问题需跨段推理时（例如第一段答案由其他智能体持有），则无法完成回答（附录G）。  

---
#### **3.2 阶段2：管理智能体 - 信息整合与响应生成**  
在阶段2中，经过工作智能体的多步信息提取与理解后，**管理智能体**生成最终解决方案。工作智能体负责长上下文中的相关信息提取，而管理智能体则整合工作链末端的**累积知识**生成最终答案。具体流程：  
$$  
\text{Response} = LLM^M(I^M, CU_l, q) \tag{2}  
$$  
- **输入**：  
  - 管理智能体指令 $I^M$  
  - 查询 $q$  
  - 末位工作智能体的通信单元 $CU_l$  

**分离设计的优势**：  
1. **职责解耦**：  
   - 工作智能体：专注**分析长上下文分块**  
   - 管理智能体：专注**生成最终答案**  
2. **性能优化**（实验验证）：  
   - 方案1：末位工作智能体 $W_l$ 直接生成答案 → **性能下降**  
   - 方案2：管理智能体接收所有 $CU_i$ → 因冲突导致**混淆**  
   - 方案3：管理智能体接收筛选后的 $CU$ → 仍**劣于**本文设计  

**数据集统计**（表3）：  

| 任务类型 | 数据集 | 平均输入长度 | 平均智能体数 |  
|----------|--------|--------------|--------------|  
| **问答** | HotpotQA | 10,603 | 2.35 |  
|  | MuSiQue | 12,975 | 2.88 |  
|  | NarrativeQA | 71,787 | 12.45 |  
|  | Qasper | 4,236 | 1.12 |  
|  | QuALITY | 4,936 | 1.31 |  
| **摘要** | QMSum | 12,524 | 2.57 |  
|  | GovReport | 9,239 | 2.03 |  
|  | BookSum | 108,478 | 18.63 |  
| **代码** | RepoBench-P | 7,105 | 1.69 |  

**注**：  
- 平均输入长度单位：词数（words）  
- 平均智能体数：处理该数据集所需工作智能体平均数  
- "✓"表示基于查询的任务  
---

#### **3.3 时间复杂度分析**  
**表2：时间复杂度对比**  
| 方法 | 编码时间 | 解码时间 |  
|------|----------|----------|  
| **全上下文方法** | $O(n^2)$ | $O(nr)$ |  
| **CoA框架** | $O(nk)$ | $O(nr)$ |  
| **RAG方法** | $O(nk') + O(k^2)$ | $O(n/k') + O(kr)$ |  

**理论对比**（基于纯解码器设定）：  
- **变量定义**：  
  - $n$：输入token总数  
  - $r$：LLM生成响应的平均token数  
  - $k$：LLM上下文窗口限制  
  - $k'$：RAG中每个文本块长度  
- **复杂度结论**：  
  1. **编码优势**：CoA的 $O(nk)$ 显著优于全上下文方法的 $O(n^2)$（因长上下文任务中 $k \ll n$）  
  2. **解码等效**：CoA与全上下文方法均为 $O(nr)$  
  3. **RAG局限**：额外引入 $O(k^2)$ 和 $O(kr)$ 开销  

**效率验证**：  
CoA框架在**编码阶段**实现线性复杂度（$n$为变量，$k$为常量），而全上下文方法呈二次方增长（详见附录A）。此特性使CoA在长上下文处理中具备显著速度优势。  


---
### 第二篇论文（完整部分） - III. MACCPP-MPC Algorithm

“MACPP（多智能体覆盖路径规划）问题可以简化为**无需返回起始位置**的旅行商问题（TSP）。由于TSP是NP难问题，因此在复杂环境中的MACPP问题也是NP难的[26]。因此，我们设计了一种启发式算法来有效解决该问题。所提出算法的框架如图3所示。

根据智能体的分布情况，该算法主要由两个阶段组成，包括**分散阶段**和**搜索阶段**：

1. **分散阶段**：此阶段主要通过分散智能体的位置来避免它们之间潜在的冲突。在此阶段，每个智能体根据其自身观察、**行为引导点**（behavior-guide-point）和奖励函数做出决策，其流程图如图3的上半部分所示。分散完成后，智能体的最终位置被作为下一阶段的输入。
2. **搜索阶段**：此阶段主要侧重于通过邻居智能体之间的交互与合作来**减少冗余路径**并**缩短完工时间**（makespan）。在此阶段，不同类型的智能体根据不同的规则做出决策，如图3的下半部分所示。”
---
#### **A. MACPP-MPC的分散阶段**  
在此阶段，每个智能体根据**观察结果**、**行为引导点**和**奖励函数**迭代优化决策，以实现合理分散。算法1展示了该阶段的伪代码：

- **第2行**：计算每个智能体对应的**网格值矩阵**。
- **第4-9行**：描述智能体决策过程：
    - `URPC(o_t^i, O_u^i)` 函数计算每个智能体的**未覆盖可达点集合**。
    - `DG(o_{t-1}^i, o_t^i, N_t^{urt}(i), M_{vm}^i, ω_d, ω_s, ω_b)` 函数基于行为引导点和奖励函数（数学表达式见第III-D节）决定智能体的**下一步动作**。
- **行为规则**：
    - 本阶段为所有智能体设置相同的行为引导点（如**仓库/depot**）。
    - **奇数编号智能体**目标：远离仓库。
    - **偶数编号智能体**目标：靠近仓库。
- **第10-13行**：执行数据更新流程。
- **迭代要求**：考虑到运动的离散特性，此阶段需进行 **(n-1)次迭代** 以实现智能体分散。


#### **B. MACPP-MPC的搜索阶段**  
本阶段采用**模型预测控制（MPC）技术**，预测智能体及其**直接交互集**内其他智能体的路径。算法2展示了该阶段的伪代码：

**1. 智能体决策规则（算法2第3-15行）**：  
根据邻居状态分为三类：  

| 类型 | 条件 | 决策机制 | 对应行号 |  
|------|------|----------|----------|  
| **Type (a)** |  &  | 直接结合观察、行为引导点和奖励函数决策 | 第9行 |  
| **Type (b)** |  &  | 应用**A*算法**修复路径 | 第11行 |  
| **Type (c)** |  &  | 将智能体索引存入矩阵 | 第13行 |

**2. 关键函数**：

- `DII(o_t^i, o_t^j (j=1,\cdots,n \ & \ j\neq i), r_c)` → 获取每个智能体的直接交互集
- `PP(ξ_{t-1}^{ψ_i}, ξ_t^{ψ_i}, O_u^{ψ_i}, O_o^{ψ_i}, M_{vm}^{ψ_i}, ω_d, ω_s, ω_b, psn_t)` → 返回各智能体的预测结果
- `PRS(tp_t^{ψ_i}, ξ_{t-1}^{ψ_i}, ξ_t^{ψ_i}, O_u^{ψ_i}, M_{vm}^{ψ_i}, ω_d, ω_s, ω_b)` → 评估融合结果的奖励值

**3. 流程说明**：

- Type (c)智能体的决策流程详见第III-E节（算法2第16-22行）
- 数据更新过程见第23-26行


---

#### **C. 基于模型预测控制的协作机制**  
在实际应用中，传播介质会影响智能体间的通信距离。假设每个智能体可与中心节点（如无人机）通信，但地面智能体仅能在有限范围内交互。受生物社会行为启发，我们引入协作机制以提升系统适应性。问题建模前给出以下命题：  

**命题1**：若智能体 $j \in \phi_i$，则 $i \in \phi_j$（关联关系对称性）。  
**证明**：  
- 若 $j \in \phi_i$，则存在两种情况：  
  (a) $j \in N_{di}^t(i)$：由 $||o_t^i - o_t^j|| \leq R_c$ 易得 $i \in N_{di}^t(j)$，故 $i \in \phi_j$  
  (b) $j \in N_{ii}^t(i)$：据**定义4**，存在序列 $\tau_1,\tau_2,\cdots,\tau_n \in A$ 满足  
  $$||o_t^i - o_t^{\tau_n}|| \leq R_c,\ ||o_t^{\tau_n} - o_t^{\tau_{n-1}}|| \leq R_c,\ \cdots,\ ||o_t^{\tau_1} - o_t^j|| \leq R_c$$  
  故 $i \in N_{ii}^t(j)$，即 $i \in \phi_j$  

**命题2**：若 $j \in \phi_k$ 且 $k \in \phi_i$，则 $j \in \phi_i$（关联关系传递性）。  
**证明**：  
- 由 $j \in \phi_k$，存在序列 $p_1,p_2,\cdots,p_{g1} \in A$ 满足：  
  $$||o_t^j - o_t^{p1}|| \leq R_c,\ ||o_t^{p1} - o_t^{p2}|| \leq R_c,\ \cdots,\ ||o_t^{p_{g1}} - o_t^k|| \leq R_c$$  
- 由 $k \in \phi_i$，存在序列 $q_1,q_2,\cdots,q_{g2} \in A$ 满足：  
  $$||o_t^k - o_t^{q1}|| \leq R_c,\ ||o_t^{q1} - o_t^{q2}|| \leq R_c,\ \cdots,\ ||o_t^{q_{g2}} - o_t^i|| \leq R_c$$  
- 连接路径 $\{p_1,\cdots,p_{g1},q_1,\cdots,q_{g2}\}$ 可证 $j \in \phi_i$  

**命题3**：若 $j \in \phi_i$，定义 $\psi_i = \{i, \phi_i\},\ \psi_j = \{j, \phi_j\}$，则 $\psi_i = \psi_j$。  
**证明**（反证法）：  
- 假设 $\exists k \in \psi_i$ 但 $k \notin \psi_j$，分两种情况：  
  (a) $k=i$：由命题1知 $k \in \phi_j \subseteq \psi_j$，矛盾  
  (b) $k \in \phi_i$：由 $k \in \phi_i$ 且 $i \in \phi_j$，据命题2得 $k \in \phi_j \subseteq \psi_j$，矛盾  

**问题分解**：  
由命题3，MACPP问题 $SP$ 可划分为 $g$ 个互斥子问题：  
$$SP = \{SP_1, SP_2, \cdots, SP_p, \cdots, SP_g\},\ p=1,2,\cdots,g$$  
其中 $|\vartheta_p|$ 表示子问题 $SP_p$ 的智能体数量，且 $\sum_{p=1}^g |\vartheta_p| = n$。  

**子问题求解策略**：  
1. $|\vartheta_p|=1$：智能体基于**自身观察**与**奖励函数**独立决策  
2. $|\vartheta_p|>1$：采用MPC技术预测未来 $psn_t$ 步路径，通过**融合-评估-排序**确定下一步动作  
   **数学模型**（公式8）：  
   $$
   \begin{aligned}
   SP_p: & \max_{q \in [1,\xi_p]} J_p(tp_q^{fusion}), & p=1,2,\cdots,g \\
   \text{s.t.} & \eta_i^q[t+1] = f(\eta_i^q[t], u_i^q[t]) \\
   & \eta_i^q[t] \in \psi_a, & t \in [st_{cur}+1, st_{cur}+psn_t] \\
   & i \in \vartheta_p
   \end{aligned}
   $$  
   - $tp_q^{fusion}$：子问题 $SP_p$ 的第 $q$ 个融合结果（共 $psn_t \times |\vartheta_p|$ 个元素，见图6）  
   - $\xi_p$：融合结果总数  
   - $\eta_i^q[t]$：智能体 $i$ 在 $t$ 时刻的状态  
   - $u_i^q[t]$：智能体 $i$ 在 $t$ 时刻的决策变量  
   - $st_{cur}$：当前时刻标签  

**优化目标**：最大化融合结果中**非重复未覆盖点**的数量，且路径点必须在可达区域内。  

**系统架构**：  
如**图4**所示，算法采用**多组分布式架构**：  
- 问题被解耦为若干子问题，每个子问题对应一个独立决策组（无中心干预）  
- 中心节点仅负责：  
  (a) 接收智能体更新的位置与观察数据  
  (b) 更新并反馈全局信息（不参与决策）  

---
#### **D. 奖励函数**  
奖励函数的数学表达式由三部分组成：**方向覆盖奖励**、**平滑奖励**和**边界奖励**。  

**1) 方向覆盖奖励**：  
为实现智能体合理分布并避免冲突，引入**行为引导点**（$\kappa_i$）指导决策。每轮迭代中，智能体通过选择未覆盖可达点最大化该奖励：  
$$  
R_d^i(o_k) = \frac{g(o_k) - g_{\min}(o_j)}{g_{\max}(o_j) - g_{\min}(o_j)}, \quad j,k \in N_t^{urt}(i) \tag{9}  
$$  
- $g(o_k)$：以$\kappa_i$为行为引导点时，点$k$在**网格值矩阵**中的取值  
- $g_{\max}(o_j)/g_{\min}(o_j)$：智能体$i$的未覆盖可达点集中最大/最小值  
- 特性：$(g_{\max}(o_j) - g_{\min}(o_j))$为常数，且$R_d^i(o_k) \in [0,1]$  

**2) 平滑奖励**：  
减少路径转向次数可缩短执行时间。该奖励使智能体保持原运动方向，仅在遇到边界、死点或邻居时转向：  
$$  
R_s^i(o_k) = \frac{|180^\circ - \theta_{ijk}^i|}{180^\circ} \tag{10}  
$$  
- $\theta_{ijk}^i$：当前运动方向与候选点方向的夹角  
- 特性：$R_s^i(o_k) \in \{0, 0.5, 1\}$（离散取值）  

**3) 边界奖励**：  
为降低边界点遗漏导致的冗余路径，智能体优先选择边界点：  
$$  
R_b^i(o_k) = \frac{n_b^{\max} - n_b^{o_k}}{n_b^{\max}} \tag{11}  
$$  
- $n_b^{o_k}$：点$k$的**相邻边界点数量**  
- $n_b^{\max}$：最大可能相邻边界点数（通常=8）  
- 特性：$R_b^i(o_k) \in [0,1]$  

**4) 总奖励合成**：  
智能体$i$移动到未覆盖可达点$k$的总奖励为：  
$$  
R^i(o_k) = \omega_d \cdot R_d^i(o_k) + \omega_s \cdot R_s^i(o_k) + \omega_b \cdot R_b^i(o_k) \tag{12}  
$$  
- **权重调整**：$\omega_d, \omega_s, \omega_b$ 值通过**田口方法**[27]优化（见第IV-A节）  

**决策规则**：  
对无邻居智能体（$N_t^{di}(i) = \emptyset \ \& \ N_t^{urt}(i) \neq \emptyset$）及Type (c)智能体，通过**最大化奖励**选择下一步动作：  
$$  
k^* = \underset{k \in N_t^{rut}(i)}{\arg\max} \left( R^i(o_k) \right) \tag{13}  
$$  


---

#### **E. 决策生成**  
决策规则按智能体类型详细说明（对应算法2）：  
| 类型 | 条件 | 决策机制 |  
|------|------|----------|  
| **Type (A)** | $N_t^{di}(i) = \emptyset \ \& \ N_t^{urt}(i) \neq \emptyset$ | 无邻居干扰，仅受**观察**、**行为引导点**和**奖励函数**影响 |  
| **Type (B)** | $N_t^{di}(i) = \emptyset \ \& \ N_t^{urt}(i) = \emptyset$ | 处于**死点状态**，采用A*算法修复路径 |  
| **Type (C)** | $N_t^{di}(i) \neq \emptyset \ \& \ N_t^{urt}(i) \neq \emptyset$ | 需考虑智能体间相互影响 |  

**1) Type (A) 决策机制**：  
无邻居智能体直接通过最大化奖励函数决策（见公式13），无需交互。  

**2) Type (B) 路径修复**：  
- 采用**单步修复法**（one-step repair）避免碰撞  
- 修复方案受**实时环境**影响（见图5示意图）  
- 注：死点状态指无未覆盖可达点的情形  

**3) Type (C) 协同预测**：  
以同一$R_c$**直接交互集**内的智能体$\{i,j,k\}$为例（交互关系见图2a）：  
**预测流程**（见图5）：  
- **基于个体的预测**（图5上半部）：  
  - 顺序1：$i \rightarrow j \rightarrow k$ → 结果存于$sp1_i$  
  - 顺序2：$i \rightarrow k \rightarrow j$ → 结果存于$sp2_i$  
- **基于步长的预测**（图5下半部）：  
  - 顺序1：$i \rightarrow j \rightarrow k \rightarrow i \rightarrow j \rightarrow k \cdots$ → 结果存于$sp3_i$  
  - 顺序2：$i \rightarrow k \rightarrow j \rightarrow i \rightarrow k \rightarrow j \cdots$ → 结果存于$sp4_i$  
（$psn$=预测步数）  

**结果融合与决策**（见图6）：  
1. 智能体在邻域内**广播预测结果**  
2. 对子问题$SP_p$，生成融合结果$tp_q^{fusion}$（图6示意）  
3. 通过评估排序所有融合结果的**累积奖励**，选择奖励最大者  
4. 相关智能体据此确定**下一步动作**  
