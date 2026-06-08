---
annotation-target：: "![[2025 APMCM Problem D.pdf]]"
---


>%%
>```annotation-json
>{"created":"2025-11-20T03:30:08.175Z","text":"机组组合（Unit Commitment, UC）是电力系统运行中的基础优化问题，其目标是在给定时间范围内，决定多台火电机组的开机/停机状态与发电出力，以满足负荷需求，同时将系统运行成本最小化。UC 属于==大规模混合整数规划==（MIP）问题，其**计算复杂度**随着**系统规模**呈*指数级增长*。","updated":"2025-11-20T03:30:08.175Z","document":{"title":"2025%20APMCM%20Problem%20D.pdf","link":[{"href":"urn:x-pdf:c7f0da4e01804ea7ab4a26b516424eb4"},{"href":"vault:/011_%E9%A1%B9%E7%9B%AE%E7%BB%8F%E9%AA%8C/%E4%BA%9A%E5%A4%AA%E6%9D%AF/2025%20APMCM%20Problem%20D.pdf"}],"documentFingerprint":"c7f0da4e01804ea7ab4a26b516424eb4"},"uri":"vault:/011_%E9%A1%B9%E7%9B%AE%E7%BB%8F%E9%AA%8C/%E4%BA%9A%E5%A4%AA%E6%9D%AF/2025%20APMCM%20Problem%20D.pdf","target":[{"source":"vault:/011_%E9%A1%B9%E7%9B%AE%E7%BB%8F%E9%AA%8C/%E4%BA%9A%E5%A4%AA%E6%9D%AF/2025%20APMCM%20Problem%20D.pdf","selector":[{"type":"TextPositionSelector","start":134,"end":553},{"type":"TextQuoteSelector","exact":"Unit commitment (UC) is a fundamental optimization problem in power systemoperation. Its goal is to determine the on/off status and generation levels of thermalunits over a given time horizon to minimize operating costs while satisfying demandand system constraints. The UC problem is known to be a large-scale mixed integerprogramming (MIP) challenge, whose computational complexity grows exponentiallywith system size","prefix":"mitment Problems inPower Systems","suffix":".With the emergence of quantum o"}]}]}
>```
>%%
>*%%PREFIX%%mitment Problems inPower Systems%%HIGHLIGHT%% ==Unit commitment (UC) is a fundamental optimization problem in power systemoperation. Its goal is to determine the on/off status and generation levels of thermalunits over a given time horizon to minimize operating costs while satisfying demandand system constraints. The UC problem is known to be a large-scale mixed integerprogramming (MIP) challenge, whose computational complexity grows exponentiallywith system size== %%POSTFIX%%.With the emergence of quantum o*
>%%LINK%%[[#^zm48ylszq7n|show annotation]]
>%%COMMENT%%
>机组组合（Unit Commitment, UC）是电力系统运行中的基础优化问题，其目标是在给定时间范围内，决定多台火电机组的开机/停机状态与发电出力，以满足负荷需求，同时将系统运行成本最小化。UC 属于==大规模混合整数规划==（MIP）问题，其**计算复杂度**随着**系统规模**呈*指数级增长*。
>%%TAGS%%
>
^zm48ylszq7n


>%%
>```annotation-json
>{"created":"2025-11-20T03:31:55.517Z","text":"随着量子优化技术（如**相干伊辛机 CIM**）的出现，UC 问题可以被重新表述为==二次无约束二元优化（QUBO）==模型。这种转化使得量子启发式求解器能够在量子硬件上进行快速并行寻优，为电力系统的实时调度提供新的可能。","updated":"2025-11-20T03:31:55.517Z","document":{"title":"2025%20APMCM%20Problem%20D.pdf","link":[{"href":"urn:x-pdf:c7f0da4e01804ea7ab4a26b516424eb4"},{"href":"vault:/011_%E9%A1%B9%E7%9B%AE%E7%BB%8F%E9%AA%8C/%E4%BA%9A%E5%A4%AA%E6%9D%AF/2025%20APMCM%20Problem%20D.pdf"}],"documentFingerprint":"c7f0da4e01804ea7ab4a26b516424eb4"},"uri":"vault:/011_%E9%A1%B9%E7%9B%AE%E7%BB%8F%E9%AA%8C/%E4%BA%9A%E5%A4%AA%E6%9D%AF/2025%20APMCM%20Problem%20D.pdf","target":[{"source":"vault:/011_%E9%A1%B9%E7%9B%AE%E7%BB%8F%E9%AA%8C/%E4%BA%9A%E5%A4%AA%E6%9D%AF/2025%20APMCM%20Problem%20D.pdf","selector":[{"type":"TextPositionSelector","start":554,"end":932},{"type":"TextQuoteSelector","exact":"With the emergence of quantum optimization technologies such as the coherentIsing Machine (CIM), the UC problem can be reformulated as a quadraticunconstrained binary optimization (QUBO) model. This transformation enables theuse of quantum-inspired solvers for fast and parallel search of optimal solutions,paving the way for real-time power system operation on quantum hardware","prefix":"s exponentiallywith system size.","suffix":".We invite participants to bridg"}]}]}
>```
>%%
>*%%PREFIX%%s exponentiallywith system size.%%HIGHLIGHT%% ==With the emergence of quantum optimization technologies such as the coherentIsing Machine (CIM), the UC problem can be reformulated as a quadraticunconstrained binary optimization (QUBO) model. This transformation enables theuse of quantum-inspired solvers for fast and parallel search of optimal solutions,paving the way for real-time power system operation on quantum hardware== %%POSTFIX%%.We invite participants to bridg*
>%%LINK%%[[#^4svvtjnd6co|show annotation]]
>%%COMMENT%%
>随着量子优化技术（如**相干伊辛机 CIM**）的出现，UC 问题可以被重新表述为==二次无约束二元优化（QUBO）==模型。这种转化使得量子启发式求解器能够在量子硬件上进行快速并行寻优，为电力系统的实时调度提供新的可能。
>%%TAGS%%
>
^4svvtjnd6co


>%%
>```annotation-json
>{"created":"2025-11-20T03:33:54.163Z","text":"**Problem 1（经典 UC 模型建立与求解）**\n要求构建包含以下内容的经典 UC 模型：\n目标函数：*燃料成本 + 启动/停机成本*\n约束：功率平衡、机组出力上下界、最短开机/停机时间、爬坡约束、启动/停机逻辑一致性\n必须使用：\n**Gurobi 或 CPLEX。**","updated":"2025-11-20T03:33:54.163Z","document":{"title":"2025%20APMCM%20Problem%20D.pdf","link":[{"href":"urn:x-pdf:c7f0da4e01804ea7ab4a26b516424eb4"},{"href":"vault:/011_%E9%A1%B9%E7%9B%AE%E7%BB%8F%E9%AA%8C/%E4%BA%9A%E5%A4%AA%E6%9D%AF/2025%20APMCM%20Problem%20D.pdf"}],"documentFingerprint":"c7f0da4e01804ea7ab4a26b516424eb4"},"uri":"vault:/011_%E9%A1%B9%E7%9B%AE%E7%BB%8F%E9%AA%8C/%E4%BA%9A%E5%A4%AA%E6%9D%AF/2025%20APMCM%20Problem%20D.pdf","target":[{"source":"vault:/011_%E9%A1%B9%E7%9B%AE%E7%BB%8F%E9%AA%8C/%E4%BA%9A%E5%A4%AA%E6%9D%AF/2025%20APMCM%20Problem%20D.pdf","selector":[{"type":"TextPositionSelector","start":2862,"end":2976},{"type":"TextQuoteSelector","exact":"formulate and solve aclassical Unit Commitment model using conventional optimization tools (e.g., Gurobior CPLEX).","prefix":"l UC Modeling and Optimization: ","suffix":"To ensure consistency and fairne"}]}]}
>```
>%%
>*%%PREFIX%%l UC Modeling and Optimization:%%HIGHLIGHT%% ==formulate and solve aclassical Unit Commitment model using conventional optimization tools (e.g., Gurobior CPLEX).== %%POSTFIX%%To ensure consistency and fairne*
>%%LINK%%[[#^t3h1xfewkz|show annotation]]
>%%COMMENT%%
>**Problem 1（经典 UC 模型建立与求解）**
>要求构建包含以下内容的经典 UC 模型：
>目标函数：*燃料成本 + 启动/停机成本*
>约束：功率平衡、机组出力上下界、最短开机/停机时间、爬坡约束、启动/停机逻辑一致性
>必须使用：
>**Gurobi 或 CPLEX。**
>%%TAGS%%
>
^t3h1xfewkz
