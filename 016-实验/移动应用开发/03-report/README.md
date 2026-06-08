# StudentApp — 学生管理系统

## 一、环境要求

| 项目 | 最低版本 |
|---|---|
| Android Studio | Hedgehog 2023.1.1 或更高 |
| JDK | 17（AS 自带即可） |
| Gradle | 8.2（项目自动下载） |
| Android SDK | compileSdk 34 / minSdk 24 |
| 模拟器或真机 | Android 7.0 (API 24) 及以上 |

## 二、导入与运行步骤

### 第 1 步：解压

将 `StudentApp.zip` 解压到任意位置，得到 `02-process` 文件夹。

### 第 2 步：打开项目

1. 启动 Android Studio
2. 选择 **File → Open**（或欢迎页的 **Open**）
3. 在弹出的文件选择器中，定位到刚才解压出的 **`02-process`** 文件夹，**选中它**，点击 **OK**

> ⚠️ 注意：要选择 `02-process` 这一层文件夹（即包含 `build.gradle` 和 `settings.gradle` 的那一层），**不要**选到里面的 `app` 子目录。

### 第 3 步：等待 Gradle 同步

Android Studio 会自动开始 Gradle 同步，右下角会出现进度条。
- 首次打开需要下载 Gradle 分发包和依赖库，**可能需要 3～10 分钟**，取决于网络。
- 如果遇到下载超时，请检查网络代理设置（**File → Settings → Appearance & Behavior → System Settings → HTTP Proxy**）。

同步成功后，左侧项目视图会正常展开模块结构。

### 第 4 步：配置模拟器（如无真机）

1. 点击顶部工具栏的 **Device Manager**（手机+扳手图标）
2. 点击 **Create Virtual Device**
3. 选择一款手机型号（推荐 **Pixel 5**），点击 **Next**
4. 选择系统镜像 **API 34**（或任何 ≥ 24 的版本），点击下载后 **Next → Finish**

### 第 5 步：运行

1. 顶部工具栏确认模块为 **app**，目标设备为刚创建的模拟器或已连接的真机
2. 点击绿色 **▶ Run** 按钮（或快捷键 `Shift + F10`）
3. 等待编译完成后，应用会自动安装并启动

## 三、功能说明

| 界面 | 功能 |
|---|---|
| **学生一览表** | 以 ListView 展示学生列表，每行包含头像、学号、姓名、班级、手机号和"详细"按钮 |
| **学生信息添加** | 表单包含学号、姓名、性别、年龄、班级、手机号、兴趣爱好（复选框）、家庭住址及图片选择 |
| **图片选择对话框** | 点击"选择图片"弹出 AlertDialog，可选择 3 张预置头像，选中后右上角预览即时更新 |
| **数据校验** | 学号 8 位数字且不重复、手机号 11 位数字、年龄 16~30、所有项不为空，错误信息以 Toast 提示 |

## 四、项目目录树

```
02-process/
├── build.gradle                    # 根级构建脚本 (AGP 8.2.2)
├── settings.gradle                 # 项目名 StudentApp
├── img/                            # 原始头像图片素材
│   ├── 026486c...720.png
│   ├── mmexport...723.jpg
│   └── mmexport...133.jpg
└── app/
    ├── build.gradle                # 模块构建脚本 (compileSdk 34)
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/example/studentapp/
        │   ├── MainActivity.java           # 主界面 (ListView + 添加/退出)
        │   ├── AddStudentActivity.java     # 添加界面 (表单 + 图片选择 AlertDialog)
        │   ├── Student.java                # 数据模型 (静态 List<Map>)
        │   └── StudentListAdapter.java     # 列表适配器 (自定义 BaseAdapter)
        └── res/
            ├── drawable/
            │   ├── avatar_1.png            # 头像1
            │   ├── avatar_2.jpg            # 头像2
            │   └── avatar_3.jpg            # 头像3
            ├── layout/
            │   ├── activity_main.xml       # 主界面布局
            │   ├── activity_add_student.xml# 添加界面布局
            │   └── item_student.xml        # 列表项布局
            └── values/
                └── strings.xml             # 字符串资源
```

## 五、常见问题

### Q1: Gradle 同步失败，提示找不到依赖

确保可以访问 `maven.google.com` 和 `repo1.maven.org`。如果校园网受限，可在 `settings.gradle` 的 `repositories` 块中添加阿里云镜像：

```groovy
maven { url 'https://maven.aliyun.com/repository/google' }
maven { url 'https://maven.aliyun.com/repository/central' }
```

### Q2: 提示 "SDK location not found"

点击 **File → Project Structure → SDK Location**，确认 Android SDK 路径已正确设置。

### Q3: 模拟器黑屏或启动极慢

- 勾选 **Enable hardware acceleration (HAXM)**
- 分配至少 2GB RAM 给模拟器
- 优先选择 x86_64 系统镜像
