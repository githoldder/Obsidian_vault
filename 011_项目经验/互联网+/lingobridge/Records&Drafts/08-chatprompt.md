**剩余里程碑**

1. **M1 本机单机演示闭环**  
    核心目标：你真人能从 admin/teacher/student 三端自由切换，完整跑一遍。  
    关键任务：
    
    - admin 能退出
    - teacher 创建/进入课程
    - teacher 创建课时、上传课件、导入作业
    - teacher 开 live
    - student 只看到自己课程
    - student 从 schedule 进 live，跟随教师翻页
    - student 做作业/录音
    - admin 能查看 live、录音、导入、进度数据
2. **M2 演示数据和音频资产收口**  
    关键任务：
    
    - 生成 14 个 demo MP3，generate-tts-assets.ts --check 必须全绿
    - 准备一套固定演示课程、课件、Excel 作业模板
    - 固定演示脚本，避免现场临时造数据
3. **M3 腾讯云部署**  
    关键任务：
    
    - 确定部署形态：单机 Node + Vite build + JSON/Postgres
    - 配置域名/端口/Nginx/PM2
    - 配置 .env
    - 验证公网 teacher/student/admin 三端流程
    - 做一次“断网/刷新/重新登录”容错测试
4. **M4 文档交付**  
    你列的 4 个 Obsidian 文档要更新：
    
    - 07-01 操作手册：按老师/学生/admin 实际流程重写
    - 07-02 测试数据采集指南：明确采集表、任务、录音、反馈方式
    - 07-04 当前进度、Sprint规划与部署扫盲：同步最新架构和部署路线
    - 07-03 预算总表：补 TTS、服务器、域名、备用 API 成本

注意：这些 Obsidian 文件在 /Users/caolei/Desktop/Obsidian_root/...，不在当前 LingoBridge 可写工作区里。下一步如果你要我直接修改它们，我需要走一次越出工作区的写权限审批。

我的建议顺序是：**你先刷新 3000 实测 admin 退出按钮**。如果这一步没问题，我们下一步就做一份“本机演示验收清单”，按 teacher/student/admin 三条线逐项打勾。