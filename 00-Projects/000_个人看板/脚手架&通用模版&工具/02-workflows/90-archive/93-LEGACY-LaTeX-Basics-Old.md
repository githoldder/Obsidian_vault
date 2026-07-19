# LaTeX 工作流技能

## 方案 1：本地工作流 (TeX Live + VS Code)
**环境**：
Mac: `brew install --cask mactex-no-gui`
Win: 官网下载 `install-tl-windows.exe`，完整安装。
Ubuntu: `sudo apt install texlive-full`

**VS Code 配置** (.vscode/settings.json):
配置 recipes 为 `xelatex -> bibtex -> xelatex*2`。关闭自动编译 `"latex-workshop.latex.autoBuild.run": "never"`。

**快捷键配置** (keybindings.json):
绑 `Cmd+S` 为 `latex-workshop.build`。

**编译链**：`xelatex main.tex`

## 方案 2：云端工作流 (Overleaf)
注册 -> New Project -> 编译 (Cmd+Enter)。
支持中文：`\usepackage[UTF8]{ctex}`。

## 语法速查
- 粗体 `\textbf{}`，斜体 `\textit{}`
- 章节：`\chapter{}`, `\section{}`, `\subsection{}`
- 公式：行内 `$ $`，块级 `\begin{equation} \end{equation}`
- 图片：`\includegraphics[width=0.8\textwidth]{file.png}`
- 引用：`\cite{}` 配合 `\printbibliography`
