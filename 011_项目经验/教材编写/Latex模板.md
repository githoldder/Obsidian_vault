```LaTex
\documentclass[UTF8, fontset=ubuntu]{ctexart}
\usepackage{geometry} % 用于设置页面布局
\usepackage{graphicx} % 用于插入图片
\usepackage{caption} % 用于表格和图片的标题
\usepackage{amsmath} % 用于数学公式
\usepackage{fancyhdr} % 用于设置页眉页脚
\usepackage{tocloft} % 用于目录设置
\usepackage{titling} % 用于设置标题、作者、日期

\setCJKmainfont{Noto Serif CJK SC}%设置字体支持中文
% 调整页眉高度
\setlength{\headheight}{12.64723pt}

% 设置页眉页脚
\pagestyle{fancy}
\fancyhf{} % 清空默认页眉页脚
\fancyhead[L]{\textit{MY LaTeX Document}} % 左页眉
\fancyfoot[C]{\thepage} % 居中页脚显示页码

% 设置封面信息
\title{MY LaTeX Doucument}
\author{你的名字}
\date{\today}

\begin{document}

% 生成封面
\begin{titlepage}
    \centering
    \vspace*{2cm}
    {\Huge \textbf{\thetitle}\par}
    \vspace{1cm}
    {\Large \theauthor\par}
    \vspace{0.5cm}
    {\large \thedate\par}
    \vspace{2cm}
    \rule{\linewidth}{0.5pt} % 下划线作为装饰
\end{titlepage}

% 生成目录
\tableofcontents
\newpage

% 正文内容
\section{这是第一个章节}
\textbf{LaTeX} 是一种基于 \TeX{} 的排版系统，由美国计算机科学家莱斯利·兰伯特在 20 世纪 80 年代初期开发，\textit{利用这种格式系统的处理，即使用户没有排版和程序设计的知识也可以充分发挥由 \TeX{} 所提供的强大功能}，不必一一亲自去设计或校对，能在几天，甚至几小时内生成具有书籍质量的印刷品。

\underline{对于生成复杂表格和公式，这一点表现得尤为突出。} 因此它非常适用于生成高印刷质量的科技和数学、物理文档。

% 插入公式
\subsection{数学公式示例}
:
\begin{equation}
E = mc^2
\end{equation}
:
\begin{equation}
\int_{0}^{\infty} e^{-x^2} \, dx = \frac{\sqrt{\pi}}{2}
\end{equation}

% 插入图片
\subsection{示例图片}
\begin{figure}[h]
    \centering
    \includegraphics[width=0.5\textwidth]{screenshot_2025-07-11_090511.png} % 替换为你的图片文件名
    \caption{这是一个示例图片}
    \label{fig:example}
\end{figure}

% 插入表格
\subsection{表格示例}
\begin{table}[h]
    \centering
    \begin{tabular}{|c|c|c|}
        \hline
        \textbf{项目} & \textbf{描述} & \textbf{值} \\
        \hline
        1 & LaTeX & 排版系统 \\
        \hline
        2 & \TeX{} & 核心引擎 \\
        \hline
    \end{tabular}
    \caption{这是一个示例表格}
    \label{tab:example}
\end{table}

\end{document}
```
