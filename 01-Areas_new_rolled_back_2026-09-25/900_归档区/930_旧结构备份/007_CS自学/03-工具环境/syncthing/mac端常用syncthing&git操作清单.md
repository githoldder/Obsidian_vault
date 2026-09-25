# Mac 端常用 Syncthing & Git 操作清单

> 日常维护速查 | 按使用频率从高到低

---

## 一、每日必查（打开电脑就扫一眼）

### 1. 同步状态一眼看完

```bash
# 一条命令看全局状态
syncthing cli show connections | python3 -c "
import sys,json; d=json.load(sys.stdin)['connections']
for dev,info in d.items():
    s='✅ 已连接' if info['connected'] else '❌ 离线'
    t=info.get('type','') or '—'
    a=info.get('address','') or ''
    print(f'{s}  {dev[:8]}...  {t:8s}  {a}')
"
```

### 2. 文件夹同步进度

```bash
curl -s -H 'X-API-Key: jH5R9wMxpwtwS6mNPCMRnf59qytsHrWT' \
  'http://localhost:8384/rest/db/status?folder=obsidian-vault' | python3 -c "
import sys,json; s=json.load(sys.stdin)
print(f'状态: {s[\"state\"]:12s} 文件: {s[\"localFiles\"]}  需同步: {s[\"needFiles\"]}  {s.get(\"error\",\"\")}')
"
```

### 3. Git 仓库是否干净

```bash
cd ~/Desktop/Obsidian_root && git status --short
# 有输出 = 有未提交的改动
# 无输出 = 干净
```

---

## 二、高频操作（每天用到）

### 4. 强制同步（改了大量笔记后）

```bash
syncthing cli operations rescan --folder=obsidian-vault
```

### 5. 临时暂停 / 恢复同步

```bash
# 暂停（比如要做大量文件重命名）
curl -X POST -H 'X-API-Key: jH5R9wMxpwtwS6mNPCMRnf59qytsHrWT' \
  http://localhost:8384/rest/system/pause

# 恢复
curl -X POST -H 'X-API-Key: jH5R9wMxpwtwS6mNPCMRnf59qytsHrWT' \
  http://localhost:8384/rest/system/resume
```

### 6. 手动 Git commit + push

```bash
cd ~/Desktop/Obsidian_root
git add -A
git commit -m "$(date '+%Y-%m-%d %H:%M') - 日常同步"
git push
```

### 7. 看谁在连我的设备（安全审计）

```bash
syncthing cli show system | python3 -c "
import sys,json
s=json.load(sys.stdin)
print(f'运行时间: {s[\"uptime\"]}s  内存: {s[\"alloc\"]/1024/1024:.1f}MB  协程: {s[\"goroutines\"]}')
"
```

---

## 三、中频操作（每周/出问题时）

### 8. 检查是否有冲突文件

```bash
cd ~/Desktop/Obsidian_root && find . -name "*sync-conflict*" 2>/dev/null
# 有输出 → 参考 SOP 步骤 10 处理
# 无输出 → 干净
```

### 9. 查看最近 5 次 Git 提交

```bash
cd ~/Desktop/Obsidian_root && git log --oneline -5
```

### 10. 查看某文件的版本历史

```bash
cd ~/Desktop/Obsidian_root
git log --oneline -- 文件路径.md

# 对比两个版本
git diff HEAD~1..HEAD -- 文件路径.md
```

### 11. 查看同步传输了多少数据

```bash
syncthing cli show connections | python3 -c "
import sys,json
t=json.load(sys.stdin)['total']
print(f'总接收: {t[\"inBytesTotal\"]/1024/1024:.1f}MB  总发送: {t[\"outBytesTotal\"]/1024/1024:.1f}MB')
"
```

### 12. 检查 .stignore 是否生效

```bash
# 列出一个文件夹看看有没有不该出现的东西
cd ~/Desktop/Obsidian_root
ls -la .git/ .obsidian/plugins/obsidian-git/ 2>&1
# 如果这些目录不存在 → .stignore 已生效（它们被排除但 Mac 端保留原始文件）
```

---

## 四、低频操作（配置变更/故障恢复）

### 13. 修改 .stignore 后强制生效

```bash
# 编辑 .stignore 后重扫
syncthing cli operations rescan --folder=obsidian-vault
```

### 14. 重启 Syncthing

```bash
syncthing cli operations restart
# 或
brew services restart syncthing
```

### 15. 查看 Mac Device ID（分享给新设备）

```bash
syncthing cli show system | python3 -c "
import sys,json; print(json.load(sys.stdin)['myID'])
"
```

### 16. 导出当前配置备份

```bash
curl -s -H 'X-API-Key: jH5R9wMxpwtwS6mNPCMRnf59qytsHrWT' \
  http://localhost:8384/rest/config | python3 -m json.tool \
  > ~/Desktop/syncthing-config-backup-$(date +%Y%m%d).json
```

---

## 五、一键综合检查（建议设为 alias）

```bash
# 加到 ~/.zshrc
alias obs-check='echo "=== Syncthing ===" && \
curl -s -H "X-API-Key: jH5R9wMxpwtwS6mNPCMRnf59qytsHrWT" "http://localhost:8384/rest/db/status?folder=obsidian-vault" | python3 -c "import sys,json; s=json.load(sys.stdin); print(f\"状态:{s[\"state\"]:10s} 文件:{s[\"localFiles\"]} 需同步:{s[\"needFiles\"]}\")" && \
echo "=== Git ===" && \
cd ~/Desktop/Obsidian_root && git status --short && \
echo "=== 冲突检查 ===" && \
find ~/Desktop/Obsidian_root -name "*sync-conflict*" 2>/dev/null | wc -l | xargs -I{} echo "冲突文件: {} 个"
'

alias obs-push='cd ~/Desktop/Obsidian_root && git add -A && git commit -m "$(date "+%Y-%m-%d %H:%M")" && git push && echo "✅ 已推送"'
alias obs-scan='syncthing cli operations rescan --folder=obsidian-vault && echo "✅ 已重扫"'
```

---

## 六、频率速查表

| 频率 | 操作 | 命令 |
|------|------|------|
| 🔴 每天 | 看同步状态 | `syncthing cli show connections` |
| 🔴 每天 | 看 Git 是否干净 | `git status --short` |
| 🔴 每天 | 改大量笔记后强制同步 | `syncthing cli operations rescan` |
| 🟡 每周 | 检查冲突文件 | `find . -name "*sync-conflict*"` |
| 🟡 每周 | 看最近提交 | `git log --oneline -5` |
| 🟢 每月 | 导出配置备份 | 操作 16 |
| 🟢 出问题时 | 重启 Syncthing | 操作 14 |
| 🟢 新增设备时 | 查看 Device ID | 操作 15 |
