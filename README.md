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
