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
        print(f'{'✅' if flag else '❌'} {info.filename}')
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

可以直接做成一个命令，放在 `~/.zshrc` 里：

```zsh
zipwin () {
    python3 - "$1" <<'PY'
import os, sys, zipfile
src = sys.argv[1]
src = os.path.abspath(src).rstrip("/\\")
dst = src + ".zip"
# 系统痕迹黑名单：macOS / Windows 各自的隐藏元数据
SKIP_NAMES = {".DS_Store", "Thumbs.db", "desktop.ini"}
SKIP_DIRS  = {"__MACOSX", ".fseventsd", ".Spotlight-V100", ".Trashes"}
base = os.path.basename(src)
with zipfile.ZipFile(dst, "w", zipfile.ZIP_DEFLATED) as z:
    for root, dirs, files in os.walk(src):
        # 就地剪枝：跳过整个黑名单目录
        dirs[:] = [d for d in dirs if d not in SKIP_DIRS]
        rel = os.path.relpath(root, src)
        if rel != "." and not dirs and not files:
            z.writestr(os.path.join(base, rel) + "/", "")
        for f in files:
            if f in SKIP_NAMES:
                continue
            p = os.path.join(root, f)
            arcname = os.path.join(base, os.path.relpath(p, src))
            z.write(p, arcname)
print(dst)
PY
}
```

然后执行：

```zsh
source ~/.zshrc
zipwin "/Users/caolei/Desktop/简历材料/25-26年综测/23030301曹磊"
```

就会直接生成 Windows 兼容的 ZIP。

## 踩坑记录：下次遇到类似问题该怎么干

### 1. 解压后中文文件名乱码

**原因**：macOS 原生 `zip` 用了 UTF-8 NFD，Windows 默认按 ANSI/GBK 解码。  
**怎么办**：用 Python `zipfile` 或 `7z` 重新打包，它们会设置 Bit 11 UTF-8 标志。  
**不要做的事**：让对方"安装 Bandizip 自动修复"——那是绕过问题，不是解决问题。你的交付物应该是标准的。

### 2. 解压后根目录是 `/Users/xxx/.../`，而不是原目录名

**原因**：打包时用了 `z.write(p, p)`，把文件在硬盘上的**绝对路径**直接写进了 ZIP 里。  
**怎么办**：打包时显式指定 `arcname`（ZIP 内路径），用 `os.path.basename(src)` 作为根目录，子文件用 `os.path.relpath(p, src)` 作为相对路径。  
**检查命令**：

```bash
python3 -c "
import zipfile
with zipfile.ZipFile('output.zip') as z:
    print(z.namelist()[0])  # 应该是 '目录名/文件'，而不是 '/Users/...'
"
```

### 3. Windows 端看到 `.DS_Store` 或 `__MACOSX`

**原因**：macOS 系统文件/资源分叉被一起打包了。  
**怎么办**：在 `zipwin` 里加黑名单过滤：

- 文件级跳过：`.DS_Store`、`Thumbs.db`、`desktop.ini`
- 目录级剪枝：`__MACOSX`、`.fseventsd`、`.Spotlight-V100`、`.Trashes`

```python
for root, dirs, files in os.walk(src):
    dirs[:] = [d for d in dirs if d not in SKIP_DIRS]  # 就地剪枝，整目录不进入
```

### 4. macOS 自带工具无法解压 `.rar`

**原因**：macOS 的 `Archive Utility`（Finder 双击）只支持 `.zip`，不支持 `.rar`、`.7z` 等格式。  
**推荐方案**：安装 `unar`（The Unarchiver 的命令行版，免费、开源、支持 rar/7z/zip）。

```bash
# 安装
brew install unar

# 解压到当前目录（自动识别编码，中文不乱码）
unar "/path/to/file.rar"

# 解压到指定目录
unar -o /path/to/output "/path/to/file.rar"

# 你的例子
unar -o ./extracted "/Users/caolei/Desktop/springboot-lgg/docs/01-resource/23000001张三.rar"
```

**备选方案**：
- GUI 用户：装 [Keka](https://www.keka.io/) 或 App Store 里的 [The Unarchiver](https://apps.apple.com/cn/app/the-unarchiver/id425424353)
- 需要命令行创建 rar：装官方 `rar`（收费），但一般只需要解压的话 `unar` 就够了

### 5. 打包前快速自检清单

```bash
# 1. 检查 zip 内路径是否正确
python3 -c "import zipfile; print(zipfile.ZipFile('output.zip').namelist()[:5])"

# 2. 检查 UTF-8 标志
python3 -c "
import zipfile
with zipfile.ZipFile('output.zip') as z:
    bad = [i for i in z.infolist() if not (i.flag_bits & 0x800)]
    print('UTF-8 未设置:', bad if bad else '全部 OK')
"

# 3. 检查 macOS 污染
python3 -c "
import zipfile
with zipfile.ZipFile('output.zip') as z:
    bad = [n for n in z.namelist() if '__MACOSX' in n or '.DS_Store' in n]
    print('污染:', bad if bad else '无')
"
```

### 6. 如果对方还是打不开

- 让对方装 [7-Zip](https://www.7-zip.org/)
- 你的交付物只要满足上面三条（UTF-8 标志、根目录正确、无 macOS 污染），99% 的 Windows 环境都能正常解压

## 直接可用的命令

### 压缩：zipwin

```zsh
zipwin "/path/to/your/folder"
```

已经写进 `~/.zshrc`，新终端自动生效。当前终端先执行一次：

```zsh
source ~/.zshrc
```

你的例子：

```zsh
zipwin "/Users/caolei/Desktop/简历材料/25-26年综测/23030301曹磊"
```

输出就是 `/Users/caolei/Desktop/简历材料/25-26年综测/23030301曹磊.zip`，解压根目录是 `23030301曹磊/`，无乱码、无 `.DS_Store`、无 `__MACOSX`。

### 解压：rar

```zsh
unar -o ./extracted "/path/to/file.rar"
```

你的例子：

```zsh
unar -o ./extracted "/Users/caolei/Desktop/springboot-lgg/docs/01-resource/23000001张三.rar"
```

## 参考资料

- [Zip Specification: Appendix D - UTF-8](https://pkware.cachefly.net/webdocs/casestudies/APPNOTE.TXT)
- [Python zipfile - ZipInfo.flag_bits](https://docs.python.org/3/library/zipfile.html#zipfile.ZipInfo.flag_bits)

---

> **一句话总结**：macOS 给 Windows 发 zip，别用右键"压缩"或 `zip` 命令，用 Python `zipfile` 或 `7z`；注意 arcname 不要写绝对路径，默认过滤掉 macOS 系统痕迹。macOS 解压 rar/7z 等格式用 `unar`。
