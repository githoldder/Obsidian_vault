# macOS 下打包 ZIP 使 Windows 解压无中文乱码

## 问题

macOS 自带的 `zip` 命令（Finder 右键"压缩"同理）使用 Apple 专有的 UTF-8 NFD（Normalization Form D）编码存储文件名。Windows 系统的内置解压工具按 ANSI/GBK 代码页解析，导致中文文件名 **100% 乱码**。

## 原理

| 平台 | 文件名编码 | 能否互相解压 |
|------|-----------|:--:|
| macOS `zip` | UTF-8 NFD | ❌ |
| Windows 资源管理器 | ANSI / GBK | ❌ |
| 7-Zip / WinRAR | UTF-8（标准） | ✅ |
| Python `zipfile` | UTF-8（标准） | ✅ |

### 为什么 Python zipfile 不乱码？

Python 的 `zipfile` 模块在写入时会设置 zip 条目的 **Bit 11（UTF-8 Language Encoding Flag）**，这是 zip 规范的官方扩展。任何遵循规范的解压工具（7-Zip、WinRAR、Bandizip、Windows 11 内置）都会识别此标志，按 UTF-8 解码文件名。

## 方案对比

| 方案 | 工具 | 安装成本 | 可靠性 | 推荐场景 |
|------|------|:--:|:--:|------|
| A | 7z（p7zip） | `brew install p7zip` | ⭐⭐⭐ | 需要频繁操作，习惯命令行 |
| B | Python `zipfile` | 系统自带，零安装 | ⭐⭐⭐ | 一次性打包、自动化脚本 |
| C | Keka | `brew install keka` | ⭐⭐⭐ | GUI 用户，从 App Store 也可安装 |
| D | `ditto -c -k` | 系统自带 | ⭐⭐ | 仅 Mac→Mac 可靠 |

## 实操：Python zipfile 一行脚本

```bash
# 压缩指定目录，输出到同目录上级或桌面
python3 -c "
import zipfile, os
src = '/path/to/source/folder'
out = '/path/to/output.zip'
with zipfile.ZipFile(out, 'w', zipfile.ZIP_DEFLATED) as zf:
    for root, dirs, files in os.walk(src):
        dirs[:] = [d for d in dirs if d != '.DS_Store']
        for f in files:
            if f == '.DS_Store': continue
            fpath = os.path.join(root, f)
            arcname = os.path.relpath(fpath, os.path.dirname(src))
            zf.write(fpath, arcname)
print(f'Done: {os.path.getsize(out)/1024/1024:.1f} MB')
"
```

### 关键点

- `arcname = os.path.relpath(fpath, os.path.dirname(src))` → 保留源目录名作为 zip 根目录（如 `handoff/...`）
- `dirs[:]` 和跳过 `.DS_Store` → 避免 Mac 垃圾文件污染
- `ZIP_DEFLATED` → 标准压缩算法，所有解压软件都支持

## 验证 ZIP 的 UTF-8 标志

```bash
# 检查 zip 条目的 Bit 11 是否已设置
python3 -c "
import zipfile
with zipfile.ZipFile('/path/to/output.zip', 'r') as zf:
    for info in zf.infolist():
        flag = info.flag_bits & 0x800  # Bit 11
        print(f'{\"✅\" if flag else \"❌\"} {info.filename}')
"
```

macOS 原生 `zip` 命令生成的 zip 包，此标志**未设置**（❌），而 Python `zipfile` 生成的**已设置**（✅）。

## 如果对方电脑是 Windows 7/XP

Windows 7/XP 自带解压不支持 UTF-8 标志，建议对方安装免费工具：
- [7-Zip](https://www.7-zip.org/) — 免费、轻量（<2MB）、开源

7-Zip 在 Windows 11 / 10 / 8 / 7 / XP 全版本通用，中文文件名无问题。

## 相关命令速查

```bash
# 安装 7z（如果要用方案 A）
brew install p7zip

# 7z 打包 zip（Windows 无乱码）
7z a -tzip output.zip /path/to/source/

# ditto 打包（仅 Mac→Mac 可靠，不推荐跨平台）
ditto -c -k --sequesterRsrc /path/src output.zip

# 用 unzip 查看 Mac zip 的真实编码
unzip -l output.zip | grep '^[0-9]' | head -5
```

## 如果经常需要打包

可以直接做成一个命令，例如：

```
zipwin () {
    python3 - "$1" <<'PY'
import os, sys, zipfile
src = sys.argv[1]
dst = src.rstrip("/\\") + ".zip"
with zipfile.ZipFile(dst, "w", zipfile.ZIP_DEFLATED) as z:
    for root, dirs, files in os.walk(src):
        rel = os.path.relpath(root, src)
        if rel != "." and not dirs and not files:
            z.writestr(os.path.join(src, rel) + "/", "")
        for f in files:
            p = os.path.join(root, f)
            z.write(p, p)
print(dst)
PY
}
```

以后只需要：

```
zipwin "/Users/caolei/Desktop/简历材料/25-26年综测/23030301曹磊"
```

就会直接生成 Windows 兼容的 ZIP。
## 参考资料

- [Zip Specification: Appendix D - UTF-8](https://pkware.cachefly.net/webdocs/casestudies/APPNOTE.TXT)
- [Python zipfile - ZipInfo.flag_bits](https://docs.python.org/3/library/zipfile.html#zipfile.ZipInfo.flag_bits)

---

> **一句话总结**：macOS 给 Windows 发 zip，别用右键"压缩"或 `zip` 命令，用 Python `zipfile` 或 `7z`。