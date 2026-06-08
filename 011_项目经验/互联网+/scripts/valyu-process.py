#!/usr/bin/env python3
# valyu数据处理脚本
# 接收valyu输出的JSON，进行清洗、筛选、存储

import json
import sys
import csv
from datetime import datetime

def process_search_results(json_str, output_csv=None):
    """处理valyu search结果"""
    try:
        data = json.loads(json_str)
        
        # 提取关键字段
        results = []
        if 'results' in data:
            for item in data['results']:
                results.append({
                    'title': item.get('title', ''),
                    'url': item.get('url', ''),
                    'snippet': item.get('snippet', '')[:200],
                    'source': item.get('source', ''),
                    'date': item.get('date', '')
                })
        
        # 输出CSV
        if output_csv:
            with open(output_csv, 'w', newline='', encoding='utf-8') as f:
                writer = csv.DictWriter(f, fieldnames=['title', 'url', 'snippet', 'source', 'date'])
                writer.writeheader()
                writer.writerows(results)
            print(f"✅ 已保存到 {output_csv}")
        
        return results
    
    except json.JSONDecodeError:
        print("❌ JSON解析失败")
        return []

def process_answer_results(json_str):
    """处理valyu answer结果"""
    try:
        data = json.loads(json_str)
        if 'answer' in data:
            print("\n📝 核心结论:")
            print(data['answer'][:1000])
        if 'sources' in data:
            print("\n📚 参考来源:")
            for src in data['sources'][:5]:
                print(f"  - {src.get('title', 'N/A')}")
    except:
        print("❌ 解析失败")

if __name__ == "__main__":
    # 读取stdin
    input_data = sys.stdin.read()
    
    if '--csv' in sys.argv:
        output_file = sys.argv[sys.argv.index('--csv') + 1] if len(sys.argv) > 2 else 'output.csv'
        process_search_results(input_data, output_file)
    else:
        process_answer_results(input_data)