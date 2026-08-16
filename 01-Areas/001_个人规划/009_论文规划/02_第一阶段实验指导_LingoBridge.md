---
tags:
  - 论文实验
  - Knowledge Tracing
  - 数据工程
  - 特征工程
  - 消融实验
  - 实验指导
created: 2026-07-11
status: active
---

# LingoBridge 第一阶段实验指导手册 🔬

> 从零到完整实验报告：数据集 → 清洗 → 特征 → 基线 → 主实验 → 消融 → 图表
> 每条指令 ≤ 2 分钟可执行 | 数据科学工程学视角 | 防坑注解贯穿全文

---

## 前言：这本手册怎么用

**定位**：这是你的实验操作台，不是论文提纲。每一个步骤都写成「打开终端 / 输入命令 / 确认输出」的形式。

**覆盖范围**：

```
Phase 1 数据工程    ████████  W1-W2  数据选型 → 清洗 → Schema 冻结 → 基线复现
Phase 2 特征工程    ████████  W3      编辑距离特征族提取与验证
Phase 3 实验执行    ████████  W4-W5  主实验 → 消融 → 诊断实验 → 泛化实验
Phase 4 图表产出    ████████  W6      全部图表生成 + 案例分析
```

**阅读约定**：
- `[终端]` = 在你的 shell 执行
- `[Python]` = 写 Python 脚本或 Jupyter cell
- `[SQL]` = 数据库查询
- `⚠️ 常见坑` = 我过去见过别人跌倒的地方
- `✅ 检查点` = 做完后必须验证的事项

---

## 一、数据集选型与获取

### 1.1 数据集决策树

在下载任何数据之前，先确认你的项目当前状态：

```
                    ┌─ LingoBridge 内部数据是否可导出？ ─┐
                    │                                     │
                   是                                     否
                    │                                     │
           ┌─ 有多少题？─┐                        直接走外部数据集路线
           │              │                        FoundationalASSIST 为主
        ≥ 500 题      < 500 题
           │              │
    回答文本是否有效？   降级为 case study
           │           主实验用外部数据
     是          否
      │           │
  内外双轨并行   降级为 case study
```

**最终数据集组合（推荐）**：

| 数据集                    |    角色    |  题目量级   | 题型       |  回答文本？   | 获取方式           |
| ---------------------- | :------: | :-----: | -------- | :------: | -------------- |
| **LingoBridge 内部**     |  主实验主战场  |   待审计   | 拼写/填空/短答 |  取决于埋点   | 数据库导出          |
| **FoundationalASSIST** | 外部对照+泛化  | ~50k 交互 | 数学选择题    | 有题面+答案文本 | HuggingFace 公开 |
| **SpeechOcean762**     | 发音子线（可选） | 2500 句  | 跟读/发音    |  有音素级标注  | OpenSLR.org    |

> ⚙️ 决策原则：**至少一个数据集的回答必须是自由文本**（拼写/填空/短答），否则 Levenshtein 特征毫无意义。FoundationalASSIST 虽有真实学生回答文本，但其题面文本和多选题特性使编辑距离的应用场景受限；真正让 edit-aware KT 成立的数据，来自你的 LingoBridge 内部交互日志。

---

### 1.2 LingoBridge 内部数据导出 — 原子步骤

#### 步骤组 A：调查现有数据结构

**A-1** | 2 min | `[终端]`
```bash
# 定位数据库文件（常见位置）
find /path/to/lingobridge -name "*.db" -o -name "*.sqlite" -o -name "*.sqlite3" 2>/dev/null
find /path/to/lingobridge -name "models.py" -o -name "schema*" 2>/dev/null
ls -la /path/to/lingobridge/migrations/ 2>/dev/null
```
✅ 确认：至少找到一个数据库文件或 schema 定义文件。

**A-2** | 2 min | `[终端]`
```bash
# 如果是 SQLite，快速查看表结构
sqlite3 /path/to/lingobridge.db ".tables"
sqlite3 /path/to/lingobridge.db ".schema user_responses" 2>/dev/null
sqlite3 /path/to/lingobridge.db ".schema interactions" 2>/dev/null
```
✅ 确认：看到至少一个与用户答题相关的表。

**A-3** | 2 min | `[SQL]`
```sql
-- 统计每个表的行数
SELECT 'user_responses' AS table_name, COUNT(*) AS n FROM user_responses
UNION ALL
SELECT 'interactions', COUNT(*) FROM interactions
UNION ALL
SELECT 'exercises', COUNT(*) FROM exercises;
```
⚠️ 常见坑：表名可能不同，先用 `.tables` 确认实际表名再写查询。

**A-4** | 2 min | `[SQL]`
```sql
-- 查看关键字段的值分布
SELECT 
    COUNT(DISTINCT user_id) AS n_users,
    COUNT(DISTINCT exercise_id) AS n_exercises,
    COUNT(*) AS n_interactions,
    AVG(LENGTH(answer_text)) AS avg_answer_len
FROM interactions;
```
✅ 检查点：如果 `n_interactions < 1000`，内部数据仅做 case study。

**A-5** | 2 min | `[SQL]`
```sql
-- 抽样 100 条实际回答，人工判断数据质量
SELECT user_id, exercise_id, answer_text, correct, created_at
FROM interactions
ORDER BY RANDOM()
LIMIT 100;
```
⚠️ 关键决策点：如果这 100 条中 `answer_text` 超过 50% 为 NULL / 空字符串 / 乱码，内部数据不可用于主实验。

**A-6** | 2 min | `[SQL]`
```sql
-- 统计题型分布
SELECT exercise_type, COUNT(*) AS n, COUNT(DISTINCT exercise_id) AS n_items
FROM interactions
GROUP BY exercise_type
ORDER BY n DESC;
```
✅ 确认：自由文本题型（拼写、填空、短答、造句）的数量。如果这些 < 200 题，考虑加入外部数据。

#### 步骤组 B：导出为实验可用格式

**B-1** | 2 min | `[Python]`
```python
# scripts/export_interactions.py
import sqlite3
import pandas as pd

conn = sqlite3.connect('/path/to/lingobridge.db')
# 调整字段名为你的实际 schema
query = """
SELECT 
    user_id,
    exercise_id,
    exercise_type,
    concept_id,
    prompt_text,
    reference_answer,
    learner_answer,
    correct   AS correctness,
    created_at AS timestamp,
    COALESCE(latency_ms, 0) AS latency_ms,
    COALESCE(hint_used, 0) AS hint_used,
    COALESCE(attempt_index, 1) AS attempt_index
FROM interactions
WHERE learner_answer IS NOT NULL 
  AND learner_answer != ''
  AND reference_answer IS NOT NULL
"""
df = pd.read_sql(query, conn)
df.to_csv('data/raw/lingobridge_interactions.csv', index=False)
print(f"Exported {len(df)} rows, {df['user_id'].nunique()} users")
```
✅ 检查点：确认导出行数 > 0，打印前 5 行检查字段完整性。

**B-2** | 2 min | `[终端]`
```bash
# 将导出文件纳入版本管理的 data/ 目录
mkdir -p data/raw data/processed data/external
mv lingobridge_interactions.csv data/raw/
wc -l data/raw/lingobridge_interactions.csv
```

---

### 1.3 FoundationalASSIST 数据获取

**C-1** | 2 min | `[终端]`
```bash
# 安装 HuggingFace datasets（如果没有）
pip install datasets huggingface_hub

# 搜索可用的 ASSIST 数据集
python -c "
from datasets import list_datasets
assist_datasets = [d for d in list_datasets() if 'assist' in d.lower() or 'foundational' in d.lower()]
for d in assist_datasets:
    print(d)
"
```

**C-2** | 2 min | `[Python]`
```python
# 加载 FoundationalASSIST（如果有；否则用 ASSISTments 2017 作为替代）
from datasets import load_dataset

try:
    # 首先尝试加载 FoundationalASSIST
    dataset = load_dataset("assistments/assistments2017")
    print("Using ASSISTments 2017")
except:
    # 回退到 ASSISTments 2009-2010
    dataset = load_dataset("assistments/assistments2009")
    print("Using ASSISTments 2009")

print(dataset)
# 查看特征
print(dataset['train'].features)
```

⚠️ 常见坑：FoundationalASSIST 可能尚未完全公开发布到 HuggingFace。如果找不到该数据集，**用 ASSISTments 2017 替代**仍然是一个被社区接受的 KT 外部数据集。论文中诚实说明即可。

**C-3** | 2 min | `[Python]`
```python
# 探索数据
train = dataset['train']
df = train.to_pandas() if hasattr(train, 'to_pandas') else pd.DataFrame(train[:5000])
print(f"Columns: {df.columns.tolist()}")
print(f"Shape: {df.shape}")
print(f"Unique users: {df['user_id'].nunique()}")
print(f"Unique items: {df['item_id'].nunique() if 'item_id' in df.columns else 'N/A'}")
print(df.head())
```

**C-4** | 2 min | `[Python]`
```python
# ASSISTments 数据标准化
# 不同版本的字段名不一样，这里统一映射
# 实际字段名可能为 user_id/skill_id/problem_id/answer_text/correct 等
# 先打印所有字段名确认
print(df.columns.tolist())
print(df.dtypes)
# 抽样看问题文本
if 'problem_text' in df.columns:
    print(df['problem_text'].dropna().head(10))
```

✅ 检查点：确认 ASSISTments 数据的题型（绝大多数是选择题，`learner_answer` 是选项字母而非自由文本）。这是正常的——这个数据集的角色是「KT 基线对标」，不是「编辑距离主战场」。编辑距离的价值在 LingoBridge 内部数据上体现。

---

### 1.4 SpeechOcean762 数据获取（可选子线）

**D-1** | 2 min | `[终端]`
```bash
# SpeechOcean762 在 OpenSLR
# 直接 curl 下载（约 2GB）
wget https://openslr.magicdatatech.com/resources/101/speechocean762.tar.gz -P data/external/
# 或
curl -L https://openslr.elda.org/resources/101/speechocean762.tar.gz -o data/external/speechocean762.tar.gz
```

**D-2** | 2 min | `[终端]`
```bash
tar -xzf data/external/speechocean762.tar.gz -C data/external/
ls data/external/speechocean762/
# 查看标注文件结构
head -20 data/external/speechocean762/scores/*.txt 2>/dev/null || echo "Check directory structure"
```

> ⚙️ 决策：LingoBridge 如果没有语音功能（无 audio_uri 字段），SpeechOcean762 可以整个跳过。这不是短路线论文的必需数据集。标注为「中期扩展子线」。

---

## 二、数据清洗流水线

### 2.1 数据质量基线审计

> 目标：在写任何清洗代码之前，先量化数据的脏度。在 Jupyter Notebook 中执行以下所有操作。

**E-1** | 2 min | `[Python]`
```python
# data_audit.ipynb - Cell 1
import pandas as pd
import numpy as np

raw = pd.read_csv('data/raw/lingobridge_interactions.csv', parse_dates=['timestamp'])
print(f"Shape: {raw.shape}")
print(f"Memory: {raw.memory_usage(deep=True).sum() / 1024**2:.2f} MB")
print("\n--- Dtypes ---")
print(raw.dtypes)
```

**E-2** | 2 min | `[Python]`
```python
# Cell 2: 缺失值全景
missing = raw.isnull().sum()
missing_pct = (missing / len(raw) * 100).round(2)
missing_df = pd.DataFrame({'n_missing': missing, 'pct_missing': missing_pct})
print(missing_df[missing_df['n_missing'] > 0].sort_values('n_missing', ascending=False))
```

**E-3** | 2 min | `[Python]`
```python
# Cell 3: 关键字段质量检查
print("--- correctness 分布 ---")
print(raw['correctness'].value_counts(dropna=False))
print(raw['correctness'].value_counts(normalize=True).round(3))

print("\n--- learner_answer 长度分布 ---")
answer_lens = raw['learner_answer'].str.len()
print(answer_lens.describe())
print(f"len=0 count: {(answer_lens == 0).sum()}")

print("\n--- reference_answer 长度分布 ---")
ref_lens = raw['reference_answer'].str.len()
print(ref_lens.describe())
print(f"len=0 count: {(ref_lens == 0).sum()}")
```

**E-4** | 2 min | `[Python]`
```python
# Cell 4: 异常检测
# learner_answer == reference_answer 但 correctness=0 的情况
paradox = raw[(raw['learner_answer'].str.strip().str.lower() == 
               raw['reference_answer'].str.strip().str.lower()) & 
              (raw['correctness'] == 0)]
print(f"Paradox (same answer but marked wrong): {len(paradox)}")
if len(paradox) > 0:
    print(paradox[['user_id', 'learner_answer', 'reference_answer', 'correctness']].head(10))

# correctness=1 但 learner_answer 明显不等于 reference_answer 的情况
potential_false_pos = raw[
    (raw['correctness'] == 1) & 
    (raw['learner_answer'].str.strip().str.lower() != raw['reference_answer'].str.strip().str.lower())
]
print(f"Potential false positives (different answer but marked right): {len(potential_false_pos)}")
```

⚠️ 常见坑：很多教育平台用「模糊匹配」或「教师人工判定」而非精确字符串比对来判断对错。如果 `correctness=1` 但 `learner_answer != reference_answer`，这不是 bug，而是现实——但这恰恰是你的 edit-aware KT 存在的理由。不要把这些样本洗掉！

**E-5** | 2 min | `[Python]`
```python
# Cell 5: 用户级质量审计
user_stats = raw.groupby('user_id').agg(
    n_interactions=('correctness', 'count'),
    mean_correctness=('correctness', 'mean'),
    n_exercises=('exercise_id', 'nunique')
).sort_values('n_interactions')

print("--- 交互次数最少的用户（潜在僵尸用户）---")
print(user_stats.head(10))

print("\n--- 交互次数分布 ---")
print(user_stats['n_interactions'].describe())

# 标记交互次数 < 5 的用户（在 KT 中几乎没信息量）
low_activity_users = user_stats[user_stats['n_interactions'] < 5].index.tolist()
print(f"Users with < 5 interactions: {len(low_activity_users)}")
```

### 2.2 数据清洗规则集

**F-1** | 2 min | `[Python]`
```python
# data_clean.py - 定义清洗规则
import pandas as pd

def build_cleaning_report(raw):
    """生成清洗前的质量报告，不做修改"""
    report = {
        'n_raw': len(raw),
        'n_users_raw': raw['user_id'].nunique(),
        'n_items_raw': raw['exercise_id'].nunique(),
        'missing_learner': raw['learner_answer'].isna().sum(),
        'missing_correctness': raw['correctness'].isna().sum(),
        'empty_learner': (raw['learner_answer'].str.strip() == '').sum(),
        'paradox_count': len(raw[(raw['learner_answer'].str.strip().str.lower() == 
                                   raw['reference_answer'].str.strip().str.lower()) & 
                                  (raw['correctness'] == 0)]),
    }
    return report
report_before = build_cleaning_report(raw)
print(report_before)
```

**F-2** | 2 min | `[Python]`
```python
def clean_dataset(raw):
    """清洗流水线：每一步只做一件事，清清楚楚"""
    df = raw.copy()
    logs = []
    
    # 规则 1: 删除 learner_answer 为空的记录
    n_before = len(df)
    df = df[df['learner_answer'].notna() & (df['learner_answer'].str.strip() != '')]
    logs.append(f"规则1: 删除空回答 → {n_before - len(df)} rows removed, {len(df)} remain")
    
    # 规则 2: 删除 correctness 不是 0/1 的记录
    df = df[df['correctness'].isin([0, 1])]
    logs.append(f"规则2: 删除无效 correctness → {len(df)} remain")
    
    # 规则 3: 用户级过滤（< 3 次交互的用户）
    n_before = len(df)
    user_counts = df['user_id'].value_counts()
    valid_users = user_counts[user_counts >= 3].index
    df = df[df['user_id'].isin(valid_users)]
    logs.append(f"规则3: 删除 < 3 次交互用户 → {(n_before - len(df))} rows, {len(valid_users)} users remain")
    
    # 规则 4: 裁剪异常长的回答（超过 500 字符视为异常）
    n_before = len(df)
    df = df[df['learner_answer'].str.len() < 500]
    logs.append(f"规则4: 删除 > 500 字符回答 → {(n_before - len(df))} rows removed")
    
    # 规则 5: 标准化布尔标签
    df['correctness'] = df['correctness'].astype(int)
    
    for log in logs:
        print(log)
    
    print(f"\n清洗完毕: {len(df)} rows, {df['user_id'].nunique()} users, {df['exercise_id'].nunique()} exercises")
    return df

df_clean = clean_dataset(raw)
```

**F-3** | 2 min | `[Python]`
```python
# 清洗前后对比
report_after = build_cleaning_report(df_clean)
print("=== 清洗前 ===")
for k, v in report_before.items():
    print(f"  {k}: {v}")
print("\n=== 清洗后 ===")
for k, v in report_after.items():
    print(f"  {k}: {v}")
print(f"\n保留率: {report_after['n_raw'] / report_before['n_raw']:.1%}")
```

**F-4** | 2 min | `[Python]`
```python
# 保存清洗后数据
df_clean.to_csv('data/processed/lingobridge_clean.csv', index=False)
print(f"Saved: data/processed/lingobridge_clean.csv ({len(df_clean)} rows)")

# 同时保存清洗报告
import json
with open('data/processed/cleaning_report.json', 'w') as f:\n    json.dump({'before': report_before, 'after': report_after}, f, indent=2)
```

### 2.3 Schema 冻结

**G-1** | 2 min | `[终端]`
```bash
# 创建 schema 定义文件
cat > data/processed/schema.json << 'SCHEMA'
{
  "schema_version": "1.0.0",
  "frozen_at": "2026-07-11",
  "description": "LingoBridge unified interaction schema",
  "fields": {
    "user_id": {"type": "string", "required": true, "description": "De-identified user identifier"},
    "exercise_id": {"type": "string", "required": true, "description": "Unique exercise identifier"},
    "exercise_type": {"type": "string", "required": false, "description": "Exercise type: spelling/fill_blank/short_answer/dictation"},
    "concept_id": {"type": "string", "required": false, "description": "Associated knowledge concept"},
    "prompt_text": {"type": "string", "required": true, "description": "Exercise prompt/question text"},
    "reference_answer": {"type": "string", "required": true, "description": "Correct/reference answer"},
    "learner_answer": {"type": "string", "required": true, "description": "Student's actual answer"},
    "correctness": {"type": "int", "required": true, "description": "0=incorrect, 1=correct"},
    "timestamp": {"type": "datetime", "required": true, "description": "Interaction timestamp"},
    "latency_ms": {"type": "int", "required": false, "description": "Response time in milliseconds"},
    "hint_used": {"type": "int", "required": false, "default": 0, "description": "Number of hints used"},
    "attempt_index": {"type": "int", "required": false, "default": 1, "description": "Attempt number for this exercise"}
  },
  "checks": {
    "min_interactions_per_user": 3,
    "max_answer_length": 500,
    "correctness_values": [0, 1]
  }
}
SCHEMA
```

**G-2** | 2 min | `[Python]`
```python
# 验证清洗后数据是否符合 schema
import json
with open('data/processed/schema.json') as f:\n    schema = json.load(f)\n\n# 自动化检查\ndf = pd.read_csv('data/processed/lingobridge_clean.csv')\nchecks = {\n    'all_required_fields_present': all(
        field in df.columns for field, props in schema['fields'].items() if props.get('required')
    ),
    'correctness_valid': df['correctness'].isin([0, 1]).all(),
    'no_empty_answers': (df['learner_answer'].str.strip() != '').all(),
    'min_interactions_per_user': (df.groupby('user_id').size() >= 3).all(),
    'min_interactions_overall': len(df) >= 100,
}
for check, passed in checks.items():
    status = '✅' if passed else '❌'
    print(f"{status} {check}: {passed}")

assert all(checks.values()), "Schema validation failed!"
print("\n✅ Schema validation passed!")
```

✅ 检查点：验证全部通过后才算「Schema 冻结」。Schema 冻结后，**不要再修改字段定义**，后续所有代码都应以此 schema 为准。

---

## 三、基线模型复现

> 目标：在 pyKT 框架内跑通 DKT / simpleKT / sparseKT，并记录每个基线的实现细节。
> pyKT 是统一实验平台——它的价值不在于代码本身，而在于**统一的数据预处理协议**。

### 3.1 pyKT 环境搭建

**H-1** | 2 min | `[终端]`
```bash
# 克隆 pyKT
git clone https://github.com/pykt-team/pykt-toolkit.git
cd pykt-toolkit
python --version  # 确认 Python ≥ 3.8
```

**H-2** | 2 min | `[终端]`
```bash
# 安装依赖
pip install -r requirements.txt
# 如果安装失败，逐条手动安装
pip install torch torchvision torchaudio
pip install pandas numpy scikit-learn tqdm
pip install wandb  # 实验追踪（推荐）
```

**H-3** | 2 min | `[终端]`
```bash
# 验证安装
python -c "
import torch
print(f'PyTorch: {torch.__version__}')
print(f'CUDA available: {torch.cuda.is_available()}')
if torch.cuda.is_available():
    print(f'GPU: {torch.cuda.get_device_name(0)}')
    print(f'Memory: {torch.cuda.get_device_properties(0).total_mem / 1024**3:.1f} GB')
"
```
⚠️ 常见坑：pyKT 可能需要在具体模型目录下另有依赖。确保 `pip install -r pykt-toolkit/pykt/models/simplekt/requirements.txt` 也执行。

**H-4** | 2 min | `[终端]`
```bash
# 确认 pyKT 支持的数据集列表
ls pykt-toolkit/pykt/datasets/
# 应看到 assist2009, assist2015, assist2017, statics2011 等
# 这些是 pyKT 内置预处理的数据集

# 确认支持的模型列表
ls pykt-toolkit/pykt/models/
# 应看到 dkt, akt, simplekt, sparsekt, dkvmn 等
```

### 3.2 将 LingoBridge 数据适配为 pyKT 格式

> pyKT 的输入格式是：每行一个交互，按 user_id 分组，按时间排序的序列。

**I-1** | 2 min | `[Python]`
```python
# scripts/convert_to_pykt.py - 将清洗后数据转为 pyKT 格式
import pandas as pd

df = pd.read_csv('data/processed/lingobridge_clean.csv', parse_dates=['timestamp'])

# pyKT 需要的最小字段集
# 核心是 user_id, item_id, timestamp, correct
pykt_df = df[['user_id', 'exercise_id', 'timestamp', 'correctness']].copy()
pykt_df.columns = ['user_id', 'item_id', 'timestamp', 'correct']

# 按 user_id 和时间排序（pyKT 要求）
pykt_df = pykt_df.sort_values(['user_id', 'timestamp'])

# 过滤掉交互次数不足的用户
user_counts = pykt_df['user_id'].value_counts()
valid_users = user_counts[user_counts >= 3].index  # pyKT 默认最小序列长度
pykt_df = pykt_df[pykt_df['user_id'].isin(valid_users)]

print(f"pyKT format: {len(pykt_df)} rows, {pykt_df['user_id'].nunique()} users, {pykt_df['item_id'].nunique()} items")
pykt_df.to_csv('data/processed/lingobridge_pykt.csv', index=False)
print("Saved: data/processed/lingobridge_pykt.csv")
```

⚠️ 常见坑：pyKT 在你没有提供 `skill_id` 的情况下可能报错。如果报错，用一个 dummy `skill_id` 填充（如直接复制 `item_id`）。

**I-2** | 2 min | `[Python]`
```python
# 如果需要 skill_id 列
pykt_df['skill_id'] = pykt_df['item_id']  # dummy: 每个 item 映射到同名 skill
# 如果需要 concept_id
if 'concept_id' in df.columns:
    # 对 pyKT，通常 skill_id 就是 concept_id
    pykt_df['skill_id'] = df['concept_id'].fillna(df['exercise_id'])
pykt_df.to_csv('data/processed/lingobridge_pykt.csv', index=False)
```

### 3.3 DKT 基线复现

**J-1** | 2 min | `[终端]`
```bash
# 首先对 pyKT 内置数据跑一次 DKT，确保环境完全正确
cd pykt-toolkit
python examples/run_dkt.py \
    --dataset assist2009 \
    --num_epochs 10 \
    --batch_size 32 \
    --learning_rate 0.001 \
    --seed 42

# 观察输出中是否正常打印 loss 和 AUC
```

**J-2** | 2 min | `[终端]`
```bash
# 在 ASSISTments 2017 上跑 DKT
python examples/run_dkt.py \
    --dataset assist2017 \
    --num_epochs 10 \
    --batch_size 32 \
    --learning_rate 0.001 \
    --seed 42 \
    --use_wandb  # 如果配置了 wandb
```

**J-3** | 2 min | `[Python]`
```python
# 记录 ASSISTments 复现指标
import json
dkt_assist_results = {
    'model': 'DKT',
    'dataset': 'assist2017',
    'auc': 0.XXX,  # 从日志中填入
    'acc': 0.XXX,
    'rmse': 0.XXX,
    'seed': 42,
    'epochs': 10,
    'batch_size': 32,
    'lr': 0.001,
    'note': 'pyKT baseline reproduction'
}
with open('results/baselines/dkt_assist2017.json', 'w') as f:\n    json.dump(dkt_assist_results, f, indent=2)\n```\n\n⚠️ 常见坑：`examples/run_dkt.py` 这个路径可能不存在——pyKT 的实际运行入口可能在别处。如果找不到，通常的做法是：\n```bash\n# 在 pyKT 仓库根目录下\npython -m pykt.main --model dkt --dataset assist2009
# 或查看 README 中的实际运行命令
cat pykt-toolkit/README.md | grep -A5 "run"
```

如果 pyKT 的具体运行方式与上述假设不同，**以 pyKT 的 README 为准**。下面三节的命令同理，需要根据实际 pyKT 的 CLI 进行调整。

**J-4** | 2 min | `[终端]`
```bash
# 在 LingoBridge 数据上跑 DKT
python examples/run_dkt.py \
    --dataset lingobridge_pykt \
    --data_path ../../data/processed/lingobridge_pykt.csv \
    --num_epochs 20 \
    --batch_size 32 \
    --learning_rate 0.001 \
    --seed 42
```

**J-5** | 2 min | `[Python]`
```python
# 记录 LingoBridge 上的 DKT 指标
dkt_lingobridge_results = {
    'model': 'DKT',
    'dataset': 'LingoBridge',
    'auc': 0.XXX,
    'acc': 0.XXX,
    'rmse': 0.XXX,
    'seed': 42,
    'epochs': 20,
    'n_users': XXX,
    'n_items': XXX,
    'n_interactions': XXX,
}
with open('results/baselines/dkt_lingobridge.json', 'w') as f:\n    json.dump(dkt_lingobridge_results, f, indent=2)\n```\n\n### 3.4 simpleKT 基线复现（3 种子）\n\n**K-1** | 2 min | `[终端]`
```bash
# simpleKT 在 ASSISTments 2017 上 — 种子 42
python examples/run_simplekt.py \
    --dataset assist2017 \
    --num_epochs 30 \
    --batch_size 32 \
    --learning_rate 0.001 \
    --seed 42 \
    --run_name "simplekt_assist2017_seed42"
```

**K-2** | 2 min | `[终端]`
```bash
# 种子 123
python examples/run_simplekt.py \
    --dataset assist2017 \
    --num_epochs 30 \
    --batch_size 32 \
    --learning_rate 0.001 \
    --seed 123 \
    --run_name "simplekt_assist2017_seed123"
```

**K-3** | 2 min | `[终端]`
```bash
# 种子 2024
python examples/run_simplekt.py \
    --dataset assist2017 \
    --num_epochs 30 \
    --batch_size 32 \
    --learning_rate 0.001 \
    --seed 2024 \
    --run_name "simplekt_assist2017_seed2024"
```

**K-4** | 2 min | `[Python]`
```python
# 汇总 simpleKT 三种子结果
simplekt_aucs = [0.XXX, 0.XXX, 0.XXX]  # 从三次运行的日志中提取
import numpy as np
print(f"simpleKT ASSIST2017 AUC: {np.mean(simplekt_aucs):.4f} ± {np.std(simplekt_aucs):.4f}")

simplekt_assist_results = {
    'model': 'simpleKT',
    'dataset': 'assist2017',
    'auc_mean': float(np.mean(simplekt_aucs)),
    'auc_std': float(np.std(simplekt_aucs)),
    'aucs_per_seed': simplekt_aucs,
    'seeds': [42, 123, 2024],
    'epochs': 30,
}
with open('results/baselines/simplekt_assist2017.json', 'w') as f:\n    json.dump(simplekt_assist_results, f, indent=2)\n```\n\n⚠️ 常见坑：如果 pyKT 中的 simpleKT 入口不存在，解决方法：\n```bash\n# 查找实际可用的模型入口\ngrep -r "simplekt" pykt-toolkit/ --include="*.py" | head -20
# 或检查 pyKT 是否有统一的 run 入口
python -m pykt.main --help 2>/dev/null
```

如果 pyKT 没有统一的 CLI 入口，你需要按照以下模式手动写训练脚本（这是更常见的情况）：

```python
# manual_train_simplekt.py - 手动复现模板
import torch
from pykt.models import simpleKT  # 或 from pykt.models.simplekt import SimpleKT
from pykt.datasets import get_dataset  # 数据加载器
from pykt.trainer import Trainer

for seed in [42, 123, 2024]:
    torch.manual_seed(seed)
    # 加载数据 → 初始化模型 → 训练 → 评估 → 记录
    pass
```

### 3.5 sparseKT 基线复现

**L-1** ~ **L-4** | 各 2 min | `[终端]`
```bash
# 与 simpleKT 完全相同的流程，三个种子
for seed in 42 123 2024; do
    python examples/run_sparsekt.py \
        --dataset assist2017 \
        --num_epochs 30 \
        --batch_size 32 \
        --learning_rate 0.001 \
        --seed $seed \
        --run_name "sparsekt_assist2017_seed$seed"
done
```

### 3.6 基线对比矩阵生成

**M-1** | 2 min | `[Python]`
```python
# scripts/generate_baseline_matrix.py
import pandas as pd
import numpy as np

# 从各自的 JSON 中加载
results = {
    ('DKT', 'assist2017'): {'auc': 0.XXX, 'acc': 0.XXX},
    ('DKT', 'LingoBridge'): {'auc': 0.XXX, 'acc': 0.XXX},
    ('simpleKT', 'assist2017'): {'auc': 0.XXX, 'acc': 0.XXX},
    ('simpleKT', 'LingoBridge'): {'auc': 0.XXX, 'acc': 0.XXX},
    ('sparseKT', 'assist2017'): {'auc': 0.XXX, 'acc': 0.XXX},
    ('sparseKT', 'LingoBridge'): {'auc': 0.XXX, 'acc': 0.XXX},
}

rows = []
for (model, dataset), metrics in results.items():
    rows.append({
        'model': model,
        'dataset': dataset,
        'AUC': metrics['auc'],
        'ACC': metrics['acc'],
    })
baseline_df = pd.DataFrame(rows)
print(baseline_df.pivot(index='model', columns='dataset', values='AUC').round(4))
baseline_df.to_csv('results/baselines/table_baseline_all.csv', index=False)
```

**M-2** | 2 min | `[Python]`
```python
# 检查点：基线指标是否合理
for model in ['DKT', 'simpleKT', 'sparseKT']:
    dkt_auc = baseline_df[(baseline_df['model']=='DKT') & (baseline_df['dataset']=='assist2017')]['AUC'].values[0]
    model_auc = baseline_df[(baseline_df['model']==model) & (baseline_df['dataset']=='assist2017')]['AUC'].values[0]
    print(f"{model} vs DKT on ASSIST2017: AUC delta = {model_auc - dkt_auc:.4f}")

# 合理性检查
# simpleKT 应该 ≥ DKT（这是 simpleKT 论文的结论）
# sparseKT 应该与 simpleKT 接近或略高
```

⚠️ 常见坑：复现指标与论文报告值有 1-3% 的差异是**完全正常的**（PyTorch 版本差异、CUDA 非确定性、数据预处理细节差异）。在 baseline_reproduction_log.md 中诚实记录差异并简要讨论原因即可。审稿人接受这种诚实做法。

---

## 四、特征工程：编辑距离特征族

> 这是你论文区别于已有 KT 工作的核心。不是你实现得有多精巧，而是你**想得有多细**。
> 以下所有特征必须在交互级别提取——每条 learner_answer 对应一组特征向量。

### 4.1 为什么选这些特征：设计原则

在开始写代码之前，先理解每一组特征的论文动机：

| 特征组 | 论文意义 | 审稿人会问 |
|------|------|------|
| Levenshtein 标量 | 最直观的「错得有多远」 | 为什么不用 WER/CER 直接表达？ |
| Damerau-Levenshtein | 捕获拼写中常见的 **字母交换** 错误（如 "recieve"→"receive"） | D-L 和 L 之间的 Δ 说明了什么？ |
| 编辑操作分布 | 把错误**分类**：多写了？少写了？写错了？位置颠倒了？ | 不同错误类型的分布如何影响 KT 预测？ |
| 错误位置分布 | 区分「开头就错」和「收尾出错」——对语言学习有诊断意义 | 位置特征对哪些错误类型最有识别力？ |
| Token 级编辑 | 上升到词层面——词序错误 vs 字母拼写错误 | 字符级和词级特征的相关性有多高？ |
| 长度归一化 | 避免长答案天然有更大的编辑距离 | 归一化方式是否公平？ |

### 4.2 环境准备

**N-1** | 2 min | `[终端]`
```bash
pip install python-Levenshtein  # C 实现的快速 Levenshtein
pip install editdistance        # 备选
pip install nltk                # tokenization
python -c "import nltk; nltk.download('punkt')"
```

**N-2** | 2 min | `[Python]`
```python
# 验证编辑距离库可用
import Levenshtein as lev
print(lev.distance("hello", "hallo"))        # → 1
print(lev.editops("hello", "hallo"))         # → [('replace', 1, 1)]
print(lev.damerau_levenshtein("ab", "ba"))   # → 1 (转置)
print(lev.damerau_levenshtein("ab", "abc"))  # → 1 (插入)
```

### 4.3 核心特征提取器（完整实现）

**O-1** | 2 min | `[Python]`
```python
# features/extract_edit_features.py
# ============================================================
# 编辑距离特征族 — 从一对 (learner_answer, reference_answer) 
# 提取全部 14 维编辑特征
# ============================================================
import Levenshtein as lev
import numpy as np
from nltk.tokenize import word_tokenize

def extract_edit_features(learner: str, reference: str) -> dict:
    """
    输入: 两个字符串
    输出: 14 维特征字典
    每对字符串的处理时间 < 2ms（python-Levenshtein C 扩展）
    """
    if not isinstance(learner, str) or not isinstance(reference, str):
        return _default_features()
    
    learner = learner.strip().lower()
    reference = reference.strip().lower()
    
    if len(learner) == 0 or len(reference) == 0:
        return _default_features()
    
    features = {}
    
    # --- 组1: 字符级单值指标 (3维) ---
    lev_dist = lev.distance(learner, reference)
    ref_len = len(reference)
    
    features['levenshtein_dist'] = lev_dist / ref_len if ref_len > 0 else 0.0
    features['damerau_levenshtein'] = lev.damerau_levenshtein(learner, reference) / ref_len if ref_len > 0 else 0.0
    features['length_norm_residual'] = (lev_dist / ref_len) if ref_len > 0 else 0.0
    
    # --- 组2: 编辑操作分布 (4维) ---
    ops = lev.editops(learner, reference)
    n_ops = len(ops)
    if n_ops > 0:
        ins = sum(1 for op in ops if op[0] == 'insert')
        dlt = sum(1 for op in ops if op[0] == 'delete')
        sub = sum(1 for op in ops if op[0] == 'replace')
        # Python-Levenshtein 中 'replace' 包含转置，但 Damerau-Levenshtein 独立处理转置
        # 我们在这里额外计算真正的 transpositions
        # 简化方案：用 damerau_levenshtein 和 levenshtein 的差来估计转置
        trans = max(0, lev_dist - lev.damerau_levenshtein(learner, reference))
        
        features['ins_ratio'] = ins / n_ops
        features['del_ratio'] = dlt / n_ops
        features['sub_ratio'] = sub / n_ops
        features['trans_ratio'] = trans / n_ops if n_ops > 0 else 0.0
    else:
        features['ins_ratio'] = features['del_ratio'] = features['sub_ratio'] = features['trans_ratio'] = 0.0
    
    # --- 组3: 错误位置分布 (2维) ---
    # 思路：沿着 learner_answer 逐字符比对，记录每次「不匹配」的位置比例
    errors_before = []
    i, j = 0, 0
    while i < len(learner) and j < len(reference):
        if learner[i] == reference[j]:
            i += 1; j += 1
        else:
            errors_before.append(i / max(1, len(learner)))
            # 从操作列表中推断是 learner 还是 reference 的问题
            # 简化：两个指针都前进（可能不精确，但对分布统计足够）
            i += 1; j += 1
    
    if errors_before:
        mid = 0.5
        features['error_head_ratio'] = sum(1 for e in errors_before if e < mid) / len(errors_before)
        features['error_tail_ratio'] = sum(1 for e in errors_before if e >= mid) / len(errors_before)
    else:
        features['error_head_ratio'] = features['error_tail_ratio'] = 0.0
    
    # --- 组4: Token 级编辑特征 (5维) ---
    try:
        learner_tokens = word_tokenize(learner)
        ref_tokens = word_tokenize(reference)
    except:
        learner_tokens = learner.split()
        ref_tokens = reference.split()
    
    if len(learner_tokens) > 0 and len(ref_tokens) > 0:
        # Token 级 Levenshtein
        token_lev = lev.distance(' '.join(learner_tokens), ' '.join(ref_tokens))
        features['token_levenshtein'] = token_lev / len(' '.join(ref_tokens)) if len(' '.join(ref_tokens)) > 0 else 0.0
        
        # Token 级操作分布
        token_ops = lev.editops(learner_tokens, ref_tokens)
        n_tops = len(token_ops)
        if n_tops > 0:
            features['token_ins_ratio'] = sum(1 for op in token_ops if op[0] == 'insert') / n_tops
            features['token_del_ratio'] = sum(1 for op in token_ops if op[0] == 'delete') / n_tops
            features['token_sub_ratio'] = sum(1 for op in token_ops if op[0] == 'replace') / n_tops
            features['token_trans_ratio'] = 0.0  # Token 级转置较少见
        else:
            features['token_ins_ratio'] = features['token_del_ratio'] = features['token_sub_ratio'] = features['token_trans_ratio'] = 0.0
    else:
        features['token_levenshtein'] = 0.0
        features['token_ins_ratio'] = features['token_del_ratio'] = features['token_sub_ratio'] = features['token_trans_ratio'] = 0.0
    
    return features


def _default_features():
    """当输入无效时返回全零特征"""
    return {
        'levenshtein_dist': 0.0, 'damerau_levenshtein': 0.0, 'length_norm_residual': 0.0,
        'ins_ratio': 0.0, 'del_ratio': 0.0, 'sub_ratio': 0.0, 'trans_ratio': 0.0,
        'error_head_ratio': 0.0, 'error_tail_ratio': 0.0,
        'token_levenshtein': 0.0,
        'token_ins_ratio': 0.0, 'token_del_ratio': 0.0, 'token_sub_ratio': 0.0, 'token_trans_ratio': 0.0,
    }


# 单元测试
if __name__ == '__main__':
    # 测试 1: 完全相同
    f = extract_edit_features("hello", "hello")
    assert f['levenshtein_dist'] == 0.0, f"Expected 0, got {f['levenshtein_dist']}"
    print("✅ Test 1 passed: identical strings")
    
    # 测试 2: 单字符替换
    f = extract_edit_features("hallo", "hello")
    assert f['levenshtein_dist'] > 0
    assert f['sub_ratio'] > 0
    print(f"✅ Test 2 passed: single substitution: {f['sub_ratio']:.3f}")
    
    # 测试 3: 空字符串
    f = extract_edit_features("", "hello")
    assert all(v == 0.0 for v in f.values())
    print("✅ Test 3 passed: empty input → all zeros")
    
    # 测试 4: 转置
    f = extract_edit_features("ab", "ba")
    assert f['damerau_levenshtein'] < f['levenshtein_dist']
    print(f"✅ Test 4 passed: transposition detection")
    
    # 测试 5: 批量性能
    import time
    pairs = [("hello", "hallo") for _ in range(1000)]
    start = time.time()
    for a, b in pairs:
        extract_edit_features(a, b)
    elapsed = time.time() - start
    print(f"✅ Test 5 passed: 1000 pairs in {elapsed:.3f}s ({elapsed*1000/1000:.3f}ms per pair)")
```

✅ 检查点：运行 `python features/extract_edit_features.py`，全部 5 个测试通过。

### 4.4 批量提取 + 质量验证

**P-1** | 2 min | `[Python]`
```python
# features/batch_extract.py
import pandas as pd
from extract_edit_features import extract_edit_features
import json

df = pd.read_csv('data/processed/lingobridge_clean.csv')
print(f"Processing {len(df)} rows...")

# 批量提取
feature_rows = []
for idx, row in df.iterrows():
    feats = extract_edit_features(str(row['learner_answer']), str(row['reference_answer']))
    feats['row_id'] = idx
    feature_rows.append(feats)
    
    if (idx + 1) % 500 == 0:
        print(f"  Progress: {idx + 1}/{len(df)}")

# 转为 DataFrame
features_df = pd.DataFrame(feature_rows)

# 合并到原始数据
df_with_features = pd.concat([df.reset_index(drop=True), features_df.drop(columns='row_id')], axis=1)
df_with_features.to_csv('data/processed/lingobridge_with_features.csv', index=False)
print(f"Saved: {len(df_with_features)} rows × {len(df_with_features.columns)} columns")
```

**P-2** | 2 min | `[Python]`
```python
# 特征质量验证
import matplotlib.pyplot as plt
import seaborn as sns

# 1. 缺失值检查
print("=== 缺失值 ===")
print(df_with_features[feature_cols].isnull().sum())

# 2. 非零率（如果某个特征 90%+ 是 0，说明区分度不够）
feature_cols = [c for c in df_with_features.columns if c not in df.columns]
nonzero_rates = (df_with_features[feature_cols] > 0.001).mean().sort_values(ascending=False)
print("\n=== 特征非零率 ===")
print(nonzero_rates)

# 3. 正确/错误样本的特征差异
correct = df_with_features[df_with_features['correctness'] == 1]
incorrect = df_with_features[df_with_features['correctness'] == 0]

print("\n=== 正确 vs 错误 特征均值 ===")
for col in feature_cols:
    c_mean = correct[col].mean()
    i_mean = incorrect[col].mean()
    delta = i_mean - c_mean
    print(f"  {col:25s}: correct={c_mean:.4f}  incorrect={i_mean:.4f}  Δ={delta:+.4f}")
```

**P-3** | 2 min | `[Python]`
```python
# 关键验证：编辑距离是否正确反映了 correctness
# 正确回答的 Levenshtein 应该接近 0
import numpy as np

for col in ['levenshtein_dist', 'damerau_levenshtein', 'token_levenshtein']:
    correct_median = correct[col].median()
    incorrect_median = incorrect[col].median()
    ratio = incorrect_median / max(0.001, correct_median)
    print(f"{col}: correct_median={correct_median:.4f}, incorrect_median={incorrect_median:.4f}, ratio={ratio:.1f}x")

# ⚠️ 关键检查点:
# 如果 incorrect/correct ratio < 2，说明编辑距离对 label 的区分力很弱
# → 需要检查 correctness 的判定方式（是否是模糊匹配？）
# → 需要检查 learner_answer 是否有系统性的格式化/归一化（去空格、去标点等）
```

✅ 检查点：`levenshtein_dist` 在 correct 组的中位数 < 0.1 且在 incorrect 组 > 0.2。如果不是，回到数据清洗步骤重新审视数据质量。

---

## 五、主实验执行

### 5.1 实验矩阵与执行顺序

```
实验编号          数据集              模型                      指标              依赖
────────────────────────────────────────────────────────────────────────────────
E1              ASSIST2017         DKT / simpleKT / sparseKT   AUC, ACC, RMSE   基线已完成
E2              LingoBridge        DKT / simpleKT / sparseKT   AUC, ACC, RMSE   基线已完成
                                                                                
E3              ASSIST2017         Edit-simpleKT                AUC, ACC, RMSE   E1 + 特征完成
E4              LingoBridge        Edit-simpleKT                AUC, ACC, RMSE   E2 + 特征完成
E5              LingoBridge        Edit-sparseKT                AUC, ACC, RMSE   E4
                                                                                
E6 (消融)       LingoBridge        Edit-simpleKT 去全部edit      AUC               E4
E7 (消融)       LingoBridge        Edit-simpleKT 仅Levenshtein   AUC               E4
E8 (消融)       LingoBridge        Edit-simpleKT 去操作分布      AUC               E4
E9 (消融)       LingoBridge        Edit-simpleKT 去Token特征     AUC               E4
E10 (消融)      LingoBridge        Edit-simpleKT 换DKT backbone  AUC               E4
                                                                                
E11 (诊断)      LingoBridge        错误诊断实验                  CER, WER, F1     E4
E12 (泛化)      LingoBridge→ASSIST 跨数据集迁移                  AUC drop         E4
E13 (公平性)    LingoBridge        子群体分析                    subgroup AUC     E4
```

> ⚙️ 执行原则：E1→E5 是主线，必须 100% 完成。E6→E13 根据实验结果选择性裁剪。

### 5.2 实验运行协议

**每个实验的标准配置**（写在配置文件中，不要每次改命令行）：

**Q-1** | 2 min | `[YAML]`
```yaml
# configs/experiment_base.yaml
training:
  batch_size: 32
  learning_rate: 0.001
  max_epochs: 100
  early_stop_patience: 10
  optimizer: adam
  weight_decay: 1e-5
  
evaluation:
  metrics: [auc, acc, rmse, bce, brier, ece]
  kfold: false
  split: user_level  # 关键！按用户拆分
  train_ratio: 0.8
  val_ratio: 0.1
  test_ratio: 0.1
  
reproducibility:
  seeds: [42, 123, 2024]
  deterministic: true
  cudnn_benchmark: false  # 关闭以确保可复现
  
hardware:
  gpu: auto
  mixed_precision: true  # 如果 GPU 支持
  
tracking:
  wandb_project: lingobridge-kt
  log_interval: 10
  save_checkpoints: true
```

**Q-2** | 2 min | `[Python]`
```python
# scripts/run_experiment.py - 统一实验运行脚本模板
import torch
import numpy as np
from pathlib import Path
import json
import wandb

def run_experiment(config):
    """运行一次完整的实验并记录所有指标"""
    
    results = {}
    for seed in config['seeds']:
        # 设置随机种子
        torch.manual_seed(seed)
        np.random.seed(seed)
        
        # 加载数据（user-level split）
        train_loader, val_loader, test_loader = load_data(
            config['dataset'],
            split='user_level',
            seed=seed
        )
        
        # 初始化模型（根据 config 选择 DKT/simpleKT/sparseKT/edit-aware 等）
        model = init_model(config['model'], config['model_params'])
        
        # 训练 + 早停
        best_metric = train_with_early_stop(
            model, train_loader, val_loader, 
            max_epochs=config['max_epochs'],
            patience=config['early_stop_patience']
        )
        
        # 评估
        metrics = evaluate(model, test_loader)
        metrics['seed'] = seed
        results[f'seed_{seed}'] = metrics
    
    # 汇总
    summary = aggregate_results(results, config['metrics'])
    
    # 保存
    exp_name = f"{config['model']}_{config['dataset']}"
    save_path = Path('results') / f"{exp_name}.json"
    save_path.parent.mkdir(parents=True, exist_ok=True)
    with open(save_path, 'w') as f:\n        json.dump({'config': config, 'results': summary, 'per_seed': results}, f, indent=2, default=str)
    
    return summary
```

### 5.3 主实验执行 — E3/E4（Edit-aware simpleKT）

> 这是整个论文的核心实验。请在确认所有基线都跑通，且编辑特征质量验证通过后，再进行这一步。

**R-1** | 2 min | `[终端]`
```bash
# Edit-aware simpleKT on ASSISTments 2017 (E3)
# 注意：ASSISTments 2017 的数据中是否有 learner_answer 文本？
# 如果没有！（大概率没有，因为 ASSISTments 只存了选择题答案标签）
# 那么 E3 的做法需要调整：
# - 对 ASSISTments，编辑特征补充仅在你有真实答案文本时适用
# - 如果 ASSISTments 没有开放回答文本，E3 的值在于确认「纯 KT 部分没有退化」
python scripts/run_experiment.py \
    --config configs/edit_aware_simplekt.yaml \
    --dataset assist2017 \
    --seed 42,123,2024
```

⚠️ 关键认知：**ASSISTments 数据集的编辑距离实验很可能是你论文的负对照**（negative control）。因为 ASSISTments 没有自由文本回答，编辑距离的特征可能在上面「没有增益」。这是好事——它说明你的方法只有在真正存在自由文本的学习场景（LingoBridge）中才起作用。在论文中把这点写清楚，比硬说「所有数据集都提升」更有说服力。

**R-2** | 2 min | `[终端]`
```bash
# Edit-aware simpleKT on LingoBridge (E4) — 论文主实验
python scripts/run_experiment.py \
    --config configs/edit_aware_simplekt.yaml \
    --dataset lingobridge \
    --seed 42,123,2024
```

**R-3** | 2 min | `[Python]`
```python
# 主实验结果对比
import json
from pathlib import Path

# 加载 baseline 和 edit-aware 结果
simplekt_baseline = json.load(open('results/baselines/simplekt_lingobridge.json'))
edit_simplekt = json.load(open('results/edit_aware_simplekt_lingobridge.json'))

for metric in ['auc', 'acc', 'rmse', 'bce']:
    base_val = simplekt_baseline.get(f'{metric}_mean', simplekt_baseline.get(metric, 0))
    edit_val = edit_simplekt['results'].get(f'{metric}_mean', edit_simplekt['results'].get(metric, 0))
    delta = edit_val - base_val
    direction = '↑' if delta > 0 else '↓'
    print(f"{metric.upper():8s}: baseline={base_val:.4f}  edit={edit_val:.4f}  Δ={delta:+.4f} {direction}")

# 核心检查点
auc_delta = edit_simplekt['results']['auc_mean'] - simplekt_baseline['auc_mean']
if auc_delta >= 0.015:
    print(f"✅ AUC 提升 {auc_delta:.3f} ≥ 1.5%，达到 KR-1 标准！")
elif auc_delta > 0:
    print(f"⚠️ AUC 提升 {auc_delta:.3f}，未达 1.5% 阈值，考虑特征改进")
else:
    print(f"❌ AUC 下降 {auc_delta:.3f}，需要回头检查特征工程或数据质量")
```

---

## 六、消融实验设计

> 消融不是「去掉一个东西跑一下看看」。每一刀都必须对应一个**可解释的论文论点**。

### 6.1 消融设计矩阵

| 消融编号 | 砍掉的组件 | 保留的组件 | 对应论文论点 | 预期结果 |
|:--:|------|------|------|------|
| A1 | 全部 edit 特征 | Backbone only | 验证「edit 特征本身有价值」 | AUC 降幅最大 |
| A2 | 操作分布（ins/del/sub/trans 占比） | 仅 Levenshtein 标量 + Token 级 | 验证「操作类型比单一距离数值更有信息量」 | AUC 中等下降 |
| A3 | Token 级特征 | 仅字符级特征 | 验证「跨粒度（词级+字符级）的双重视角」 | AUC 小幅下降 |
| A4 | Backbone 换成 DKT | Edit 全量特征 | 验证「edit 特征的增益不依赖于 backbone」 | 绝对 AUC 可能下降但相对提升保留 |
| A5 | 位置分布特征 | 其余全量 | 验证「错误位置是否有独立预测价值」 | 很小但正面的 Δ |

**消融的解释链条**（论文中最有力的论据结构）：

```
全量 Edit-aware simpleKT          AUC = 0.XXXX (最高)
    │
    ├─ 去全部 edit (A1)           AUC = 0.YYYY (最低)
    │   → edit 特征族贡献 = Δ₁ = 0.XXXX - 0.YYYY
    │
    ├─ 仅 Levenshtein (A2)        AUC = 0.ZZZZ
    │   → 操作分布贡献 = Δ₂ = 0.XXXX - 0.ZZZZ
    │
    ├─ 去 Token (A3)              AUC = 0.WWWW
    │   → 跨粒度贡献 = Δ₃ = 0.XXXX - 0.WWWW
    │
    └─ 去位置分布 (A5)            AUC = 0.VVVV
        → 位置贡献 = Δ₅ = 0.XXXX - 0.VVVV
```

### 6.2 消融执行脚本

**S-1** | 2 min | `[Python]`
```python
# scripts/run_ablations.py
import subprocess
import json
from pathlib import Path

ablations = [
    {
        'name': 'no_edit_features',
        'description': '去掉全部 edit 特征，退回标准 simpleKT',
        'config_overrides': {'use_edit_features': False}
    },
    {
        'name': 'only_levenshtein',
        'description': '只保留 Levenshtein 标量距离',
        'config_overrides': {
            'use_edit_features': True,
            'use_operation_distribution': False,
            'use_token_features': False,
            'use_position_features': False,
        }
    },
    {
        'name': 'no_operation_dist',
        'description': '去掉编辑操作分布（ins/del/sub/trans）',
        'config_overrides': {
            'use_edit_features': True,
            'use_operation_distribution': False,
            'use_token_features': True,
            'use_position_features': True,
        }
    },
    {
        'name': 'no_token_features',
        'description': '去掉 Token 级特征，仅保留字符级',
        'config_overrides': {
            'use_edit_features': True,
            'use_operation_distribution': True,
            'use_token_features': False,
            'use_position_features': True,
        }
    },
    {
        'name': 'dkt_backbone',
        'description': '将 simpleKT backbone 替换为 DKT',
        'config_overrides': {'backbone': 'dkt'}
    },
    {
        'name': 'no_position_features',
        'description': '去掉错误位置分布',
        'config_overrides': {
            'use_edit_features': True,
            'use_operation_distribution': True,
            'use_token_features': True,
            'use_position_features': False,
        }
    },
]

for ablation in ablations:
    print(f"\n{'='*60}")
    print(f"Running ablation: {ablation['name']}")
    print(f"Purpose: {ablation['description']}")
    print(f"{'='*60}")
    
    # 运行实验（这里用 subprocess 调用你实际的训练脚本）
    cmd = [
        'python', 'scripts/run_experiment.py',
        '--config', f'configs/ablations/{ablation["name"]}.yaml',
        '--dataset', 'lingobridge',
        '--seed', '42,123,2024',
    ]
    # subprocess.run(cmd)  # 取消注释以实际运行
    print(f"  [DRY RUN] Command: {' '.join(cmd)}")
```

**S-2** | 2 min | `[Python]`
```python
# 汇总消融结果
import pandas as pd

ablation_results = {
    'Full Edit-aware simpleKT':     0.XXXX,
    'A1: No edit (pure simpleKT)':  0.YYYY,
    'A2: Only Levenshtein scalar':  0.ZZZZ,
    'A3: No token features':        0.WWWW,
    'A4: DKT backbone':             0.VVVV,
    'A5: No position features':     0.UUUU,
}

df_ablation = pd.DataFrame(list(ablation_results.items()), columns=['Configuration', 'AUC'])
df_ablation['AUC_drop'] = df_ablation['AUC'].max() - df_ablation['AUC']
df_ablation = df_ablation.sort_values('AUC', ascending=False)
print(df_ablation.to_string(index=False))
df_ablation.to_csv('results/ablations_summary.csv', index=False)
```

---

## 七、错误诊断实验（E11）

> 编辑距离不仅有预测价值，更有**诊断价值**——这是你论文区别于普通 KT 方法的第二根支柱。

### 7.1 诊断实验设计

**T-1** | 2 min | `[Python]`
```python
# 错误诊断实验：模型能否预测学生具体会错在哪些方面？
# 注意：这与 KT 预测任务不同。KT 预测的是「下一题会不会做对」
# 错误诊断预测的是「如果做错了，错在哪里」(回归/分类任务)

# 诊断目标：
# 目标1: 预测编辑操作分布（回归）→ 4 个输出（ins/del/sub/trans ratio）
# 目标2: 预测错误位置比例（回归）→ 2 个输出（head/tail ratio） 
# 目标3: 预测编辑操作分类（多标签分类）→ 学生犯了哪几类错误

import numpy as np

# 在 Edit-aware simpleKT 的最后一层 hidden state 上添加诊断头
# Ht = Edit-aware simpleKT(h1, h2, ..., ht)
# 诊断预测 = MLP_diagnosis(Ht)
# 
# 损失函数：
# L_total = L_KT + λ * L_diagnosis
# 其中 L_diagnosis = MSE(操作分布) + MSE(位置比例) + BCE(操作分类)

# 具体实现思路：
# 1. 在训练 Edit-aware simpleKT 时同时优化诊断损失
# 2. 在测试时，仅使用 Ht 预测下一题的对错（KT 主任务）
#    同时评估诊断头的表现
# 3. 报告中分别报告 KT AUC 和诊断 MAE/F1
```

**T-2** | 2 min | `[Python]`
```python
# 诊断实验评估指标
diagnostic_metrics = {
    'CER': None,           # Character Error Rate — 预测的编辑距离 vs 真实编辑距离的误差
    'WER': None,           # Word Error Rate — 词级同理
    'op_F1': None,         # 编辑操作分类 F1
    'position_MAE': None,  # 错误位置预测的 MAE（head/tail）
    'distance_MAE': None,  # 编辑距离数值预测的 MAE
}

# 与 DKT baseline 对比的诊断能力：
# DKT 只能看到一个 0/1 标签，无法做任何错误诊断
# Edit-aware simpleKT 能基于历史编辑特征预测下次错误的类型和位置
# 这个对比本身就是论文中强有力的论据
```

---

## 八、跨数据集泛化实验（E12）

**U-1** | 2 min | `[Python]`
```python
# 泛化实验设计
# 
# 场景 A: ASSISTments 2017 上训练 → LingoBridge 上测试
#   - 目的：验证模型在完全不同题库上的泛化能力
#   - 预期：AUC 会有明显下降，但 edit-aware 版本下降幅度应该 < 纯 KT 版本
#   - 原因：edit 特征（编辑距离、操作分布）是「答题质量」的信号，
#           具有一定的跨题库迁移性
#
# 场景 B: LingoBridge 上训练 → ASSISTments 上测试
#   - 目的：验证 edit-aware 模型是否学到了可迁移的模式
#   - 此场景的价值较低（ASSISTments 的 learner_answer 文本质量差）
#   - 如果 ASSISTments 没有开放的答案文本，此实验不可行
#
# 建议：
# - 如果只有 LingoBridge 一个有自由文本的数据集，跳过跨数据集实验
# - 在论文中声明此为局限性，并在 Discussion 中讨论未来的多数据集扩展
```

---

## 九、子群体公平性实验（E13）

**V-1** | 2 min | `[Python]`
```python
# 公平性分析
# 分组维度（选择至少 2 个）:
# 1. 按先验能力分组：top 25% / middle 50% / bottom 25%
#    依据：用户前 20% 交互的平均 correctness
# 2. 按活跃度分组：高频 / 中频 / 低频用户
# 3. 按答题时长分组：快答 / 慢答

import pandas as pd

df = pd.read_csv('data/processed/lingobridge_clean.csv')

# 计算每个用户的先验能力
user_ability = df.groupby('user_id').agg(
    total_correct=('correctness', 'sum'),
    total_interactions=('correctness', 'count')
)
user_ability['ability'] = user_ability['total_correct'] / user_ability['total_interactions']

# 分位分组
bottom25_threshold = user_ability['ability'].quantile(0.25)
top25_threshold = user_ability['ability'].quantile(0.75)

user_ability['group'] = 'middle'
user_ability.loc[user_ability['ability'] <= bottom25_threshold, 'group'] = 'low'
user_ability.loc[user_ability['ability'] >= top25_threshold, 'group'] = 'high'

print(f"Low ability (<{bottom25_threshold:.2f}): {(user_ability['group']=='low').sum()} users")
print(f"Middle ability: {(user_ability['group']=='middle').sum()} users")
print(f"High ability (>{top25_threshold:.2f}): {(user_ability['group']=='high').sum()} users")

user_ability['group'].to_csv('data/processed/user_groups.csv')
```

**V-2** | 2 min | `[Python]`
```python
# 按分组重新评估 Edit-aware simpleKT 的性能
# 关键指标：subgroup AUC gap
# 如果 low 组的 AUC 显著低于 high 组，说明模型对低能力学生不够公平

import json

# 假设已有分组评估结果
fairness_results = {
    'overall': {'auc': 0.XXXX},
    'low_ability': {'auc': 0.YYYY},
    'middle_ability': {'auc': 0.ZZZZ},
    'high_ability': {'auc': 0.WWWW},
}

max_gap = max(fairness_results[k]['auc'] for k in ['low_ability','middle_ability','high_ability']) - \
          min(fairness_results[k]['auc'] for k in ['low_ability','middle_ability','high_ability'])

print(f"Subgroup AUC gap: {max_gap:.4f}")
print(f"Calibration (ECE) per group: 需要额外实现")

# 还需要报告 calibration gap:
# ECE_low - ECE_high 应在可接受范围内
# 如果某个分组的模型概率校准特别差，需要在论文中讨论
```

---

## 十、图表清单与绘制规范

### 10.1 论文图表完整清单

> 以下列出了短路线论文需要的全部图表。每一张都有明确的论文目的和绘制要点。

#### 必做图表（论文主体，Figure 1-8）

| 编号 | 类型 | 内容 | 尺寸 | 必须向量 | 章节 |
|:--:|------|------|:--:|:--:|------|
| **Fig 1** | 示意图 | Edit-aware KT 系统架构概览 | 全宽 | ✅ | Introduction |
| **Fig 2** | 分布图 | 编辑距离 vs correctness 的分布对比（箱线图或小提琴图） | 半宽 | ✅ | Method |
| **Fig 3** | 热力图 | 编辑操作分布在不同题型上的差异 | 半宽 | ✅ | Method |
| **Fig 4** | 柱状图 | 主实验结果：所有模型在所有数据集上的 AUC 对比 | 全宽 | ✅ | Experiments |
| **Fig 5** | 表格+柱状 | 消融实验结果（瀑布图或分组柱状图） | 全宽 | ✅ | Experiments |
| **Fig 6** | 折线图 | 训练曲线：loss 下降 / AUC 上升（simpleKT vs Edit-simpleKT） | 半宽 | — | Experiments |
| **Fig 7** | 混淆矩阵+散点 | 错误诊断结果：预测 vs 真实的编辑操作分布 | 半宽 | ✅ | Experiments |
| **Fig 8** | 案例分析 | 3-5 个典型学生的编辑距离轨迹（随时间的变化） | 全宽 | ✅ | Discussion |

#### 推荐附加图（可能放 Appendix）

|   编号   | 内容                                            | 章节       |
| :----: | --------------------------------------------- | -------- |
| Fig S1 | 特征相关性矩阵（14 维 edit 特征之间的 Spearman 相关）          | Appendix |
| Fig S2 | 子群体 AUC 对比（low/middle/high ability）           | Appendix |
| Fig S3 | 模型校准曲线（reliability diagram，ECE 可视化）           | Appendix |
| Fig S4 | 不同 seed 的训练稳定性（箱线图）                           | Appendix |
| Fig S5 | t-SNE 可视化：simpleKT vs Edit-simpleKT 的用户状态向量聚类 | Appendix |

### 10.2 绘图原子步骤

#### 图 2：编辑距离 vs Correctness 分布

**W-1** | 2 min | `[Python]`
```python
# figures/fig2_edit_distance_vs_correctness.py
import matplotlib.pyplot as plt
import seaborn as sns
import pandas as pd

# 设置中英文混排（论文用英文即可）
plt.rcParams.update({
    'font.family': 'serif',
    'font.size': 11,
    'axes.labelsize': 12,
    'axes.titlesize': 13,
    'legend.fontsize': 10,
    'figure.dpi': 150,
    'savefig.dpi': 300,
    'savefig.bbox': 'tight',
    'savefig.format': 'pdf',  # 矢量！必须！
})

df = pd.read_csv('data/processed/lingobridge_with_features.csv')

fig, axes = plt.subplots(1, 2, figsize=(12, 5))

# 左图: Levenshtein 距离分布
for label, color, name in [(0, '#2ecc71', 'Correct'), (1, '#e74c3c', 'Incorrect')]:
    # 注意：correctness=1 是正确，但我们的图应该显示
    # 正确回答的编辑距离 → 小
    # 错误回答的编辑距离 → 大
    subset = df[df['correctness'] == label]
    if label == 0:  # 实际 label 是 0=错误
        subset_plot = df[df['correctness'] == 0]
        c, nm = '#e74c3c', 'Incorrect'
    else:
        subset_plot = df[df['correctness'] == 1]
        c, nm = '#2ecc71', 'Correct'
    
    axes[0].hist(subset_plot['levenshtein_dist'].clip(0, 2), 
                 bins=50, alpha=0.6, color=c, label=nm, density=True)

axes[0].set_xlabel('Normalized Levenshtein Distance')
axes[0].set_ylabel('Density')
axes[0].set_title('(a) Distribution of Edit Distances by Correctness')
axes[0].legend()
axes[0].grid(alpha=0.3)

# 右图: 箱线图（多特征对比）
features_to_show = ['levenshtein_dist', 'damerau_levenshtein', 'token_levenshtein']
df_melt = pd.melt(
    df, 
    id_vars=['correctness'], 
    value_vars=features_to_show,
    var_name='Feature', 
    value_name='Value'
)
df_melt['Correctness'] = df_melt['correctness'].map({0: 'Incorrect', 1: 'Correct'})
df_melt['Value'] = df_melt['Value'].clip(0, 1.5)  # clip outliers for visibility

sns.boxplot(data=df_melt, x='Feature', y='Value', hue='Correctness',
            palette={'Correct': '#2ecc71', 'Incorrect': '#e74c3c'}, ax=axes[1])
axes[1].set_title('(b) Feature Distributions by Correctness')
axes[1].set_ylabel('Normalized Distance')
axes[1].tick_params(axis='x', rotation=15)
axes[1].legend(loc='upper right')

plt.tight_layout()
plt.savefig('figures/fig2_edit_distribution.pdf')
plt.savefig('figures/fig2_edit_distribution.png')  # 备用 PNG
print("✅ Saved: figures/fig2_edit_distribution.pdf")
```

⚠️ 绘图规范：
- **总是同时保存 PDF（矢量）和 PNG（预览）两份**
- 使用 colorblind-friendly 配色：`#2ecc71`(绿) + `#e74c3c`(红) 或 `#3498db`(蓝) + `#e67e22`(橙)
- 标注 (a)(b)(c) 在标题里，方便论文中引用
- `clip()` 异常值时要标注在 figure caption 中（如 "Values above the 99th percentile are clipped for visualization"）

#### 图 4：主实验 AUC 对比柱状图

**W-2** | 2 min | `[Python]`
```python
# figures/fig4_main_results.py
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

# 模拟数据（替换为你的实际结果）
models = ['DKT', 'simpleKT', 'sparseKT', 'Edit-\nsimpleKT', 'Edit-\nsparseKT']
datasets = ['ASSIST17', 'LingoBridge']
results = {
    'ASSIST17': [0.72, 0.76, 0.76, 0.76, 0.76],  # ASSIST上很可能没有提升
    'LingoBridge': [0.68, 0.72, 0.73, 0.74, 0.74],  # LingoBridge上有提升
}

x = np.arange(len(models))
width = 0.35

fig, ax = plt.subplots(figsize=(10, 6))
bars1 = ax.bar(x - width/2, results['ASSIST17'], width, label='ASSISTments 2017', 
               color='#3498db', alpha=0.85, edgecolor='white', linewidth=0.5)
bars2 = ax.bar(x + width/2, results['LingoBridge'], width, label='LingoBridge', 
               color='#e67e22', alpha=0.85, edgecolor='white', linewidth=0.5)

# 标注数值
for bars in [bars1, bars2]:
    for bar in bars:
        height = bar.get_height()
        ax.annotate(f'{height:.3f}',
                    xy=(bar.get_x() + bar.get_width() / 2, height),
                    xytext=(0, 3), textcoords="offset points",
                    ha='center', va='bottom', fontsize=8)

ax.set_ylabel('AUC', fontsize=13)
ax.set_xlabel('Model', fontsize=13)
ax.set_title('Main Results: Next-Response Prediction', fontsize=14)
ax.set_xticks(x)
ax.set_xticklabels(models, fontsize=10)
ax.legend(loc='lower right', fontsize=11)
ax.set_ylim(0.60, 0.82)
ax.grid(axis='y', alpha=0.3)
ax.spines['top'].set_visible(False)
ax.spines['right'].set_visible(False)

plt.tight_layout()
plt.savefig('figures/fig4_main_results.pdf')
plt.savefig('figures/fig4_main_results.png')
print("✅ Saved: figures/fig4_main_results.pdf")
```

⚠️ 重要备注：如果 ASSISTments 上 edit-aware 没有提升（如上模拟数据所示），**不要隐藏这个事实**。在论文中写："As expected, edit-aware features did not improve performance on ASSISTments 2017, since the dataset lacks free-text student responses. This result serves as a negative control confirming that the edit-based features are only beneficial in language learning scenarios with open-ended answers."

#### 图 5：消融实验瀑布图

**W-3** | 2 min | `[Python]`
```python
# figures/fig5_ablation.py
import matplotlib.pyplot as plt
import numpy as np

# 消融结果（替换为你的实际数据）
configs = [
    'Full Edit-aware\nsimpleKT',
    '− Position\nFeatures',
    '− Token\nFeatures',
    '− Operation\nDistribution',
    'Only Levenshtein\nScalar',
    'No Edit\n(Pure simpleKT)',
]

auc_values = [0.7459, 0.7452, 0.7441, 0.7423, 0.7405, 0.7280]
deltas = [auc_values[0] - v for v in auc_values]

fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(14, 5), gridspec_kw={'width_ratios': [2, 1]})

# 左图：柱状图
colors = ['#2ecc71'] + ['#3498db'] * 4 + ['#95a5a6']
bars = ax1.barh(configs[::-1], auc_values[::-1], color=colors[::-1])
ax1.set_xlabel('AUC', fontsize=12)
ax1.set_title('(a) Ablation Study Results', fontsize=13)
ax1.set_xlim(0.72, 0.76)
ax1.grid(axis='x', alpha=0.3)

# 标注数值
for bar in bars:
    width = bar.get_width()
    ax1.text(width + 0.001, bar.get_y() + bar.get_height()/2, 
             f'{width:.4f}', va='center', fontsize=10)

# 右图：贡献度瀑布图（瀑布图写法：用堆叠 bar 模拟）
# 简化版：直接用降序柱状图表示每一步去掉后的 drop
ax2.bar(configs[1:], deltas[1:], color=['#f39c12']*4 + ['#e74c3c'], alpha=0.85)
ax2.set_ylabel('AUC Drop', fontsize=12)
ax2.set_title('(b) Contribution of Each Component', fontsize=13)
ax2.tick_params(axis='x', rotation=45)
ax2.grid(axis='y', alpha=0.3)
ax2.axhline(y=0, color='black', linewidth=0.5)

for i, (cfg, delta) in enumerate(zip(configs[1:], deltas[1:])):
    ax2.text(i, delta + 0.0002, f'{delta:.4f}', ha='center', fontsize=9)

plt.tight_layout()
plt.savefig('figures/fig5_ablation.pdf')
plt.savefig('figures/fig5_ablation.png')
print("✅ Saved: figures/fig5_ablation.pdf")
```

### 10.3 绘图规范速查卡

| 要素 | 标准 | 为什么 |
|------|------|------|
| **格式** | PDF（矢量）为主，PNG 为预览 | 放大不失真，期刊排版必须 |
| **分辨率** | 300 DPI（PNG），矢量不限 | 打印尺寸下清晰 |
| **字体** | Serif（Times New Roman 或 Computer Modern） | 与 LaTeX 正文一致 |
| **字号** | 正文 ≥ 8pt，标题 ≥ 10pt | 双栏排版下可读 |
| **配色** | Colorblind-friendly（Viridis / Set2 / 自定义对比色） | ~8% 男性色盲，审稿人会关注 |
| **边框** | 去掉 top/right spine（`ax.spines['top'].set_visible(False)`） | 现代学术图表惯例 |
| **网格** | 浅灰色（alpha=0.3）辅助线 | 不喧宾夺主 |
| **图例** | 在图形区域内或正下方，不要放外面导致裁切 | 排版友好 |
| **标注** | (a)(b)(c) 在标题中，不要在图上用大号字 | 论文中引用方便 |
| **裁剪** | `bbox_inches='tight'` | 避免白边过大 |

---

## 十一、案例分析 (Case Studies)

> 案例不是随便挑几个好看的样本。案例是证明「你的方法有实际理解价值」的最直接证据。

### 11.1 案例选择标准

**至少 5 个案例，覆盖以下类别：**

| 案例类型 | 选择标准 | 展示点 |
|------|------|------|
| 案例 1 | 正确率一直很高 → 突变错误 → 恢复 | Edit-aware 的提前预警能力 |
| 案例 2 | 同一知识点反复犯错但错误类型不同（拼写 vs 语法） | 编辑操作分布的变化轨迹 |
| 案例 3 | Levenshtein 距离高但正确率 = 1（模糊匹配容忍的学生） | 编辑距离揭示 CRT 标签隐藏的细粒度信息 |
| 案例 4 | 错误位置从头部移到尾部的学生 | 位置特征的学习诊断价值 |
| 案例 5 | low ability 学生的长期学习轨迹 | 对低能力学生的公平性 |

**X-1** | 2 min | `[Python]`
```python
# case_studies/case_selection.py
import pandas as pd
import numpy as np

df = pd.read_csv('data/processed/lingobridge_with_features.csv')

# 案例 1: 找「突变」用户 — 用滑动窗口检测转折点
def find_turning_points(user_df, window=5, threshold=0.3):
    """检测用户的正确率转折点"""
    user_df = user_df.sort_values('timestamp')
    rolling = user_df['correctness'].rolling(window=window, min_periods=window).mean()
    
    for i in range(window, len(rolling)):
        before = rolling.iloc[i-window:i].mean()
        after = rolling.iloc[i:]
        if len(after) >= window:
            after_val = after.iloc[:window].mean()
            if abs(after_val - before) > threshold:
                return i
    return None

candidates = []
for uid, group in df.groupby('user_id'):
    if len(group) >= 20:
        tp = find_turning_points(group)
        if tp:
            candidates.append((uid, tp, len(group)))

candidates.sort(key=lambda x: x[1], reverse=True)
print("Top 5 turning-point candidates:")
for uid, tp, n in candidates[:5]:
    print(f"  User {uid}: turn at interaction {tp}/{n}")
```

**X-2** | 2 min | `[Python]`
```python
# case_studies/plot_case_trajectory.py
# 为选中的案例绘制编辑距离轨迹图
import matplotlib.pyplot as plt

def plot_case_trajectory(user_id, df, save_path):
    """绘制单个用户的知识状态和编辑距离随时间的变化"""
    user_df = df[df['user_id'] == user_id].sort_values('timestamp').reset_index(drop=True)
    
    fig, (ax1, ax2, ax3) = plt.subplots(3, 1, figsize=(10, 8), sharex=True)
    
    x = range(len(user_df))
    
    # 子图 1: 正确率（绿=对，红=错）
    colors = ['#2ecc71' if c == 1 else '#e74c3c' for c in user_df['correctness']]
    ax1.bar(x, [1]*len(x), color=colors, width=0.8, alpha=0.6)
    ax1.set_ylabel('Correctness', fontsize=11)
    ax1.set_ylim(0, 1)
    ax1.set_yticks([0, 1])
    ax1.set_yticklabels(['✗', '✓'])
    
    # 子图 2: Levenshtein 距离
    ax2.plot(x, user_df['levenshtein_dist'], 'o-', color='#3498db', markersize=4, linewidth=1.5,
             label='Levenshtein')
    ax2.fill_between(x, 0, user_df['levenshtein_dist'], alpha=0.1, color='#3498db')
    ax2.set_ylabel('Normalized Edit Distance', fontsize=11)
    ax2.legend(fontsize=9)
    ax2.grid(alpha=0.3)
    
    # 子图 3: 编辑操作分布（堆叠面积）
    ops = ['ins_ratio', 'del_ratio', 'sub_ratio']
    labels = ['Insertion', 'Deletion', 'Substitution']
    colors_ops = ['#e74c3c', '#f39c12', '#3498db']
    ax3.stackplot(x, 
                  [user_df[op] for op in ops],
                  labels=labels, colors=colors_ops, alpha=0.7)
    ax3.set_ylabel('Operation Ratio', fontsize=11)
    ax3.set_xlabel('Interaction Index', fontsize=11)
    ax3.legend(fontsize=9, loc='upper right')
    
    plt.suptitle(f'Case Study: User {user_id[:8]}... (n={len(x)} interactions)', fontsize=13)
    plt.tight_layout()
    plt.savefig(save_path)
    print(f"✅ Saved: {save_path}")
```

---

## 十二、特征相关性分析（Appendix Fig S1）

**Y-1** | 2 min | `[Python]`
```python
# figures/fig_s1_feature_correlation.py
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

df = pd.read_csv('data/processed/lingobridge_with_features.csv')
feature_cols = [
    'levenshtein_dist', 'damerau_levenshtein', 'length_norm_residual',
    'ins_ratio', 'del_ratio', 'sub_ratio', 'trans_ratio',
    'error_head_ratio', 'error_tail_ratio',
    'token_levenshtein',
    'token_ins_ratio', 'token_del_ratio', 'token_sub_ratio', 'token_trans_ratio',
]

corr = df[feature_cols].corr(method='spearman')

plt.figure(figsize=(12, 10))
mask = np.triu(np.ones_like(corr, dtype=bool), k=1)
sns.heatmap(corr, mask=mask, annot=True, fmt='.2f', cmap='RdBu_r',
            vmin=-1, vmax=1, center=0, square=True,
            linewidths=0.5, cbar_kws={'shrink': 0.8, 'label': "Spearman's ρ"})
plt.title('Feature Correlation Matrix (14 Edit-based Features)', fontsize=14)
plt.tight_layout()
plt.savefig('figures/fig_s1_feature_correlation.pdf')
print("✅ Saved: figures/fig_s1_feature_correlation.pdf")
```

> ⚠️ 分析要点：如果 `levenshtein_dist` 和 `damerau_levenshtein` 的相关系数 > 0.95（通常就是！），在论文讨论中诚实指出这一点。这不削弱你的论点——两者虽然高度相关，但 Damerau-Levenshtein 提供了转置这一关键操作的信息，而普通 Levenshtein 没有。

---

## 十三、实验日志与可复现性清单

### 13.1 实验运行日志模板

**Z-1** | 2 min | `[终端]`
```bash
# 每次实验必须有这个日志
cat > results/experiments/EXP_YYYYMMDD_MODEL_DATASET.log << 'LOG'
=== Experiment Log ===
Date: 2026-07-11
Experiment: Edit-aware simpleKT on LingoBridge
Seed: 42

--- Environment ---
Python: 3.10.x
PyTorch: 2.x.x
CUDA: 12.x
GPU: NVIDIA A100 40GB

--- Data ---
Train users: XXX
Val users: XXX
Test users: XXX
Total interactions: XXX

--- Hyperparameters ---
Batch size: 32
Learning rate: 0.001
Max epochs: 100
Early stop patience: 10
Hidden dim: 256
Num layers: 2

--- Results ---
AUC: 0.XXXX ± 0.00XX
ACC: 0.XXXX ± 0.00XX
RMSE: 0.XXXX ± 0.00XX
Brier: 0.XXXX ± 0.00XX
ECE: 0.XXXX ± 0.00XX

--- Notes ---
(任何特殊观察、异常、debug 记录)
LOG
```

### 13.2 完整重现检查清单

**Z-2** | 2 min | `[终端]`
```bash
# 最终检查：一个全新 clone 能否在 30 分钟内复现你的主实验
# 1. 创建干净的 conda 环境
conda create -n lingobridge_reproduce python=3.10 -y
conda activate lingobridge_reproduce

# 2. 安装依赖
pip install -r reproducibility_package/requirements.txt
cd reproducibility_package

# 3. 一键运行
bash run_experiment.sh

# 4. 比较结果
python compare_results.py --expected results/expected_main.json --actual results/main.json
```

---

## 十四、时间预算与优先级矩阵

### 14.1 每项任务的预估耗时

| 板块 | 任务 | 原子步骤数 | 总预估时间 | 优先级 |
|------|------|:--:|:--:|:--:|
| 数据选型 | A-1→D-2 | 12 | ~25 min | 🔴 必须 |
| 数据清洗 | E-1→G-2 | 12 | ~25 min | 🔴 必须 |
| pyKT 环境 | H-1→I-2 | 8 | ~15 min | 🔴 必须 |
| DKT 基线 | J-1→J-5 | 5 | ~10 min + 训练时间 | 🔴 必须 |
| simpleKT 基线 | K-1→K-4 | 4 | ~10 min + 训练时间 | 🔴 必须 |
| sparseKT 基线 | L-1→L-4 | 4 | ~10 min + 训练时间 | 🟡 重要 |
| 基线汇总 | M-1→M-2 | 2 | ~5 min | 🔴 必须 |
| 编辑特征提取器 | N-1→O-1 | 3 | ~6 min | 🔴 必须 |
| 批量提取+验证 | P-1→P-3 | 3 | ~6 min + 处理时间 | 🔴 必须 |
| 主实验 E3/E4 | R-1→R-3 | 3 | ~8 min + 训练时间 | 🔴 必须 |
| 消融实验 | S-1→S-2 | 2 | ~5 min + 训练时间 | 🔴 必须 |
| 错误诊断 | T-1→T-2 | 2 | ~5 min | 🟡 重要 |
| 泛化实验 | U-1 | 1 | ~2 min | 🟢 可选 |
| 公平性实验 | V-1→V-2 | 2 | ~5 min | 🟢 可选 |
| 图表生成 | W-1→W-3 + Y-1 | 4 | ~10 min | 🔴 必须 |
| 案例分析 | X-1→X-2 | 2 | ~5 min | 🟡 重要 |
| 日志+复现 | Z-1→Z-2 | 2 | ~5 min | 🔴 必须 |

> ⚙️ 人工操作总时间（不含模型训练等待）：约 **2-3 小时**。模型训练时间取决于数据量，通常每个种子 15-60 分钟。

### 14.2 如果时间不够，裁剪顺序

| 裁剪优先级 | 可以放弃的内容 | 论文影响 |
|:--:|------|------|
| 1 | SpeechOcean762 子线（整个 D 组） | 无 — 这是中期扩展 |
| 2 | 跨数据集泛化（U-1） | 小 — 在 Limitations 中说明 |
| 3 | 子群体公平性（V-1, V-2） | 中 — 如果有时间尽量保留 |
| 4 | 错误诊断（T-1, T-2） | 大 — 这是第二卖点，建议保留 |
| 5 | 案例分析超过 3 个（X 组裁剪） | 中 — 3 个精华案例足够 |
| **决不放弃** | 主实验（E3/E4）+ 消融（全部）+ 图表（Fig 1-8） | 致命 — 没有这些无法投稿 |

---

## 相关笔记
- [[01_全流程任务规划_LingoBridge]] — 22 周甘特图与关键路径
- [[00_选题与研究规划_LingoBridge]] — 选题重构与技术路线
- [[90_计算机视觉论文发表规划]] — OKRT 模板与流程参考

🏷️: [[LingoBridge]] [[Knowledge Tracing]] [[实验指导]] [[特征工程]] [[数据科学]] [[消融实验]] [[论文图表]]
