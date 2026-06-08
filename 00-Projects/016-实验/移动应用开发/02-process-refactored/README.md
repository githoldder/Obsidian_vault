# StudentApp 运行说明

## 开发环境

| 项目 | 版本 |
|------|------|
| Android Studio | Hedgehog 2023.1.1 或更高版本 |
| JDK | 17 |
| Gradle | 8.2（由 Gradle Wrapper 自动管理） |
| Android Gradle Plugin | 8.2.2 |
| compileSdk | 34 |
| targetSdk | 34 |
| minSdk | 24 |

## 首次运行步骤

1. 解压项目压缩包
2. 打开 Android Studio，选择 **File → Open**，定位到项目根目录并打开
3. 等待 Gradle Sync 自动完成（首次可能需要几分钟下载依赖）
4. Sync 完成后，点击 **Run** 或 **Build → Rebuild Project**

## 注意事项

- 项目已配置阿里云镜像源，无需科学上网即可下载依赖
- `local.properties` 文件会由 Android Studio 自动生成，无需手动创建
- 如遇 SDK 版本缺失提示，请在 SDK Manager 中安装 API 34
