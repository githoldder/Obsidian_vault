**📋 当前状态总结：**

|项目|状态|
|---|---|
|Vault 路径|`~/Desktop/Obsidian_root`|
|HEAD|`9d0a351`（最新，含 4 个 subtree）|
|Remote|`origin → githoldder/Obsidian_vault`|
|你的修改|`requirements.md` 已保留|
|新文件|~20 个 `01-Areas/` 下新增文件已保留|
|同步脚本|`~/Desktop/sync-vault-subtrees.sh`|

**🔁 日常同步流程：**

1. **vault → GitHub**：Obsidian Git 插件自动 push（你已经配好了）
2. **vault → 子仓库**：终端跑 `~/Desktop/sync-vault-subtrees.sh`
3. **子仓库 → vault**（很少需要）：`git subtree pull --prefix=00-Projects obsidian-projects main --squash`

最后一步：清理残留 stash：

bash复制

```bash
cd ~/Desktop/Obsidian_root && git stash drop
```

本项目为个人obsidian知识库系统
分为四大板块：
详情可以参考[[51-PARA-Management]]
搭建个人知识库的方式千差万别，本项目旨在提供知识库的搭建方式以及构建的结构；有关知识体系如何搭建可以参考：[[如何快速学习一个陌生领域]][[KL01-从义务教育到高等教育，我们学习了多少知识]][[KL03-如何快速理解一门知识]]

