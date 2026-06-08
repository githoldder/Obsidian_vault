# Skill: Valyu CLI 调研技能

## 使用场景
使用Valyu CLI进行产品市场调研、信息检索

## 前置条件
```bash
# 1. 安装
brew install valyu

# 2. 认证（必需）
valyu login
# 或设置环境变量
export VALYU_API_KEY="your-key"
```

## 检查认证
```bash
valyu doctor
```

## 调研命令

### 1. 基础搜索
```bash
# 网页搜索
valyu search web "关键词" -n 10

# 学术论文搜索
valyu search academic "关键词" -n 15

# 金融数据搜索
valyu search financial "关键词"
```

### 2. AI问答
```bash
valyu answer "具体问题"
```

### 3. 深度研究
```bash
valyu deepresearch create "研究主题" --watch
```

### 4. 内容提取
```bash
valyu contents https://example.com --summary
```

## 数据处理

```bash
# 搜索结果转CSV
valyu search web "关键词" --json > output.json
cat output.json | jq -r '.results[] | {title, url, snippet} | @csv' > output.csv
```

## 踩坑记录

### 2026-04-27 ✅ 已解决
- ❌ 初次使用需先认证API Key
- ❌ 没有Key时所有search返回认证错误
- ✅ `valyu doctor` 可检查认证状态
- ✅ 安装成功但需配置API Key才能使用

### 常用参数
- `-n 10`: 返回10条结果
- `--json`: JSON格式输出
- `-q, --quiet`: 静默模式

## 注意事项
- Valyu是Tier 2配置，可能存在兼容性问题
- 调研前建议先运行 `valyu doctor` 确认状态
- 深度研究任务耗时较长，建议使用 `--watch` 模式
- 免费版有速率限制，商业版可提升配额

## 成本参考
- 网页搜索：$0.0015/条
- 学术搜索：$0.0015/条
- AI问答：流式输出
- 深度研究：按复杂度收费

## 更新日志
- 2026-04-27: 初始化Skill，认证踩坑记录
- 2026-04-27: 首次大规模调研（俄语教育市场、竞品、一带一路、哈萨克斯坦）