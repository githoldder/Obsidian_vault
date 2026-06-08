这是一份为你整理的 **2026年版前端工程师 UI/UX 权威审美参照清单**。

这份清单舍弃了泛泛而谈的素材站，直接聚焦于**前端实现效果最好、设计逻辑最严密**的标杆级项目。

> 解耦说明：本文只负责“去哪里看、参考什么”。如果目标是让 AI 生成不模板化的界面，请使用 [[03_AI前端UI-Taste约束生成Skill]]；如果目标是审查和删改已经生成的界面，请使用 [[05_AI前端UI减法审查与反模板巡检Skill]]。

---

## 🟦 B端：逻辑、效率与系统感（Business & Enterprise）

B端的审美核心是：**克制、一致性、高信息密度、逻辑自洽**。

### 1. 行业设计系统（Design Systems）

这些是前端组件化开发的“标准答案”，不仅有 UI，还有极高质量的代码实现参考。
- **[Linear](https://linear.app/) (全球 B端审美天花板)**：目前的绝对标杆。其磨砂玻璃效果、极其细腻的边框高光（Border Glow）、响应式的快捷键系统，是所有前端模仿的对象。
- **[Vercel / Geist](https://vercel.com/design)**：极简主义的巅峰。黑白灰的极致运用，教科书级的排版（Typography）和间距（Spacing）控制。
- **[Stripe](https://stripe.com/)**：虽然是支付工具，但其 Dashboard 的色彩系统（多色阶蓝色）和复杂的流式布局是行业神话。
- **[Ant Design](https://ant.design/)**：国内 B端复杂业务逻辑处理的最佳实践，特别是在处理超大数据量表格和多层嵌套表单时。
### 2. 垂直领域参考
- **[SaaSFrame](https://www.saasframe.io/)**：专门收集 SaaS 产品的界面，涵盖了侧边栏设计、计费页面、设置面板等真实业务场景。
- **[Refero.design](https://refero.design/)**：强大的 B端组件搜索工具。你可以直接搜“Table（表格）”或“Filter（过滤器）”，看顶级产品是如何设计的。
### 3. 数据可视化（Data Viz）
- **[AntV / G2Plot](https://antv.vision/)**：不仅是工具，其配套的“设计原则”文档会告诉你为什么要用某种颜色表示告警。
- **[Observable](https://observablehq.com/)**：D3.js 创始人的作品集散地，代表了 Web 端数据可视化的技术极限。
---

## 🟧 C端：情绪、交互与视觉冲击（Consumer & Creative）

C端的审美核心是：**品牌感、丝滑动效、沉浸式体验、创新交互**。
### 1. 交互与动效标杆

这些网站代表了浏览器能实现的视觉极限，通常涉及 WebGL、WebGPU 和复杂的 CSS 动画。
- **[Awwwards](https://www.awwwards.com/)**：前端界的奥斯卡，适合寻找年度趋势（如目前流行的 Bento Grid 布局或 3D 沉浸式叙事）。
- **[Godly](https://godly.website/)**：只收录交互最丝滑、技术实现最高级的网站，非常适合寻找动画衔接（Transition）的灵感。
- **[Cosmos](https://www.cosmos.so/)**：新一代审美聚合平台，相比 Dribbble，这里的作品更具现代感和艺术气息。
### 2. 移动端与产品流
- **[Mobbin](https://mobbin.com/)**：全球最全的 App 截图库。前端可以学习其移动端组件的交互细节（如抽屉组件 Drawer 的弹出高度）。
- **[Lapa Ninja](https://www.lapa.ninja/)**：落地页（Landing Page）专家。如果你需要写一个让人惊叹的产品官网，这里有最好的排版和配色参考。

---

## 🛠️ 工程师跨界审美工具包

为了让你更高效地“对齐”这些审美，建议收藏以下工具：

|**工具名称**|**用途**|
|---|---|
|**[WhatFont](https://www.google.com/search?q=https://chrome.google.com/webstore/detail/whatfont/jojjocappaigpkpnieokejdcjkebnacf)**|插件。一键查看大厂网页用了什么字体、行高、颜色。|
|**[Eagle](https://www.eagle.cool/)**|软件。本地管理你抓取的 UI 截图，支持按颜色和标签筛选。|
|**[Tailwind UI](https://tailwindui.com/)**|库。如果你用 Tailwind，这是将顶级审美转化为代码的最短路径。|
|**[Shadcn UI](https://ui.shadcn.com/)**|库。目前最火的组件库设计风格，完美平衡了 B端的规范和 C端的精致。|

---

### 💡 总结建议

- **做 B端时**：多看 **Linear** 和 **Stripe**。学习他们如何处理“灰色”和“边框”，不要乱用阴影，要用边框叠加来产生层次感。
    
- **做 C端时**：多看 **Godly** 和 **Awwwards**。关注滚动驱动动画（Scroll-driven Animation），这是提升网页高级感的最快手段。
    
