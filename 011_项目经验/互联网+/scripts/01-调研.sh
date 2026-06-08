#!/bin/bash
# lingobridge调研.sh
# 使用Valyu CLI进行LingoBridge产品调研
# Token消耗：0（已固化）

# 检查认证
valyu doctor --json > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "❌ Valyu未认证，请运行 'valyu login' 后再试"
    exit 1
fi

echo "🔍 开始调研..."

# 1. 俄语在线教育市场调研
echo "📊 调研1: 俄语在线教育市场规模"
valyu search web "russian online education market size 2025 2026" -n 10

echo ""
echo "📊 调研2: 一带一路俄语人才需求"
valyu search web "一带一路 俄语人才需求 中国" -n 10

echo ""
echo "📊 调研3: 俄语学习app竞品"
valyu search web "俄语学习app 竞品 多邻国 SpeakPal" -n 10

echo ""
echo "📊 调研4: 在线教育监管政策"
valyu search web "在线教育监管政策 vipkid 2025" -n 10

echo ""
echo "📊 调研5: 腾讯会议教育版功能"
valyu search web "腾讯会议教育版 功能 实时翻译" -n 10

echo ""
echo "✅ 调研完成"