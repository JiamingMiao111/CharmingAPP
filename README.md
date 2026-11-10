# CharmingAPP

> 把各大厂商的内置应用，移植到任何一台 Android 手机上

**Charming × DeepSeek Harness**

---

## 这是什么

一个可以下载**已做好兼容补丁**的厂商内置应用的网站。

传统上，小米的相册、三星的健康这些应用只能在原厂手机上用，因为它们依赖自家系统框架。本项目通过一系列技术改造，让它们能在**任意 Android 设备**上运行。

---

## 当前支持

### HyperOS · 小米

| 应用 | 包名 | 状态 | 大小 |
|---|---|---|---|
| 小米相册 | `com.miui.gallery` | ✅ 完美运行 | 96 MB |
| 小米笔记 | `com.miui.notes` | ✅ 完美运行 | 83 MB |
| 小米天气 | `com.miui.weather2` | ✅ 完美运行 | 25 MB |
| 小米指南针 | `com.miui.compass` | ✅ 可用 | 4.3 MB |
| 小米日历 | `com.android.calendar` | ✅ 可用 | 16 MB |
| 小米音乐 | `com.miui.player` | ✅ 可用 | 146 MB |
| 小米互传 | `com.xiaomi.midrop` | ✅ 可用 | 28 MB |

### One UI · 三星

| 应用 | 包名 | 状态 | 大小 |
|---|---|---|---|
| 三星健康 | `com.sec.android.app.shealth` | ✅ 完美运行 | 333 MB |
| 三星浏览器 | `com.sec.android.app.sbrowser` | ✅ 完美运行 | 318 MB |
| 三星日历 | `com.samsung.android.calendar` | ✅ 完美运行 | 9 MB |
| 三星笔记 | `com.samsung.android.app.notes` | ✅ 可用 | 106 MB |

---

## 部署到 Cloudflare Pages

### 方式一：直接上传（推荐）

1. 打开 [Cloudflare Dashboard](https://dash.cloudflare.com/)
2. 进入 **Workers & Pages** → **Create** → **Pages** → **Upload assets**
3. 项目名填 `charmingapp`
4. 把整个 `CharmingAPP/` 目录拖进去
5. 点 **Deploy site**

完成后你会得到：`https://charmingapp.pages.dev`

### 方式二：Wrangler CLI

```bash
npm install -g wrangler
wrangler login
cd CharmingAPP
wrangler pages deploy . --project-name charmingapp
```

### 方式三：Git 连接

1. 把整个目录推到 GitHub
2. Cloudflare Pages → Connect to Git
3. Build command 留空，Output directory 填 `/`

---

## 本地预览

```bash
cd CharmingAPP
python3 -m http.server 8080
# 打开 http://localhost:8080
```

---

## 目录结构

```
CharmingAPP/
├── index.html              # 主页面（单文件，含全部逻辑）
├── _headers                # Cloudflare 缓存/安全头
├── _redirects              # 路径重写
├── robots.txt
├── apks/
│   ├── samsung/            # 三星应用
│   │   ├── SamsungHealth.apk
│   │   ├── SamsungInternet.apk
│   │   ├── SamsungCalendar.apk
│   │   └── SamsungNotes.apk
│   ├── xiaomi/             # 小米应用
│   │   ├── MiGallery.apk
│   │   ├── MiNotes.apk
│   │   ├── MiWeather.apk
│   │   ├── MiCompass.apk
│   │   ├── MiCalendar.apk
│   │   ├── MiMusic.apk
│   │   └── MiDrop.apk
│   └── MiuiShim.dex        # MIUI shim 兼容层
└── tools/
    ├── fix_axml.py         # AXML 二进制补丁工具
    ├── inject_shim.py      # shim 注入工具
    └── shim_src/           # shim 源码（Java）
        └── miui/
            ├── os/Build.java
            ├── util/FeatureParser.java
            ├── util/HapticFeedbackUtil.java
            ├── accounts/ExtraAccountManager.java
            └── cloud/...
```

---

## 技术原理

改造一个厂商应用，需要四步。

### 第 1 步：修改 targetSdk

厂商应用通常 targetSdk=36/37（Android 16/17），在低版本系统上会因为框架 API 差异崩溃：

```
java.lang.NullPointerException
    at ClientTransactionListenerController.onContextConfigurationPreChanged
```

降到 35 即可。用 `apktool` 解包后改 `apktool.yml`：

```yaml
targetSdkVersion: 35
```

### 第 2 步：恢复 META-INF 文件

用 apktool 重新打包会**丢掉 META-INF 下的所有非签名文件**，导致 Kotlin 协程初始化失败：

```
java.lang.IllegalStateException: Module with the Main dispatcher is missing
    at iwj.<clinit>(Unknown Source:100)
```

必须从原版 APK 恢复：
- `META-INF/services/*` （SPI 服务注册）
- `META-INF/*.version` （库版本）
- `META-INF/*.kotlin_module` （Kotlin 模块元数据）

### 第 3 步：修补 AXML `uses-library`

厂商应用声明私有共享库：

```xml
<uses-library android:name="micloud-sdk" />
```

在非原厂设备上不存在，安装报错：

```
INSTALL_FAILED_MISSING_SHARED_LIBRARY
  requires unavailable shared library micloud
```

解决：直接改二进制 AndroidManifest.xml，加上 `required="false"`。

**关键：AXML StartElement 布局**

```
off+0   type(2) hsize(2) size(4)
off+8   lineNumber(4) comment(4)
off+16  ns(4) name(4)
off+24  attributeStart(2) attributeSize(2)
off+28  attributeCount(2)   ← 修改这里（不是 off+30！）
off+30  idIndex(2) classIndex(2) styleIndex(2)
```

工具：`tools/fix_axml.py`

```bash
python3 tools/fix_axml.py input.apk output.apk micloud-sdk
```

### 第 4 步：注入 shim 兼容类

厂商应用调用自家框架类：

```
java.lang.NoClassDefFoundError: miui.os.Build
java.lang.NoClassDefFoundError: miui.util.FeatureParser
```

因为 `miui.*` 不是 `android.*` 系统包，**可以在应用内定义同名类**。编译成 dex 后作为 `classesN.dex` 注入 APK 即可。

工具：`tools/inject_shim.py`

```bash
python3 tools/inject_shim.py input.apk output.apk shim.dex
```

---

## 已实现的 shim 类

### 小米 (miui.*)

| 类 | 说明 |
|---|---|
| `miui.os.Build` | 36 个字段（IS_GLOBAL_BUILD / IS_PRIVATE_BUILD / IS_DEBUGGABLE…） |
| `miui.util.FeatureParser` | 15 个方法（getBoolean / hasFeature / getIntArray…） |
| `miui.util.HapticFeedbackUtil` | 震动反馈 |
| `miui.accounts.ExtraAccountManager` | 账户（返回 null） |
| `miui.cloud.os.SystemProperties` | 系统属性（反射） |
| `miui.cloud.sync.MiCloudStatusInfo` | 云状态（返回未授权） |
| `miui.cloud.common.XLogger` | 日志（空实现） |
| `miui.cloud.util.SyncAutoSettingUtil` | 同步设置 |
| `miui.cloud.MiCloudCompat` | 云兼容层 |
| `miui.cloud.sync.FamilyInfo` | 家庭信息 |

### 三星 (Sem*)

三星的依赖通过**直接改 smali** 解决（不是 shim）：

| 类 | 处理方式 |
|---|---|
| `android.os.SemSystemProperties` | → 改用 `android.os.SystemProperties` |
| `com.samsung.android.feature.SemCscFeature` | → 方法体改成返回默认值 |
| `com.samsung.android.feature.SemFloatingFeature` | → 方法体改成返回 false |
| `com.samsung.android.emergencymode.SemEmergencyManager` | → 方法体改成返回常量 |

---

## 已知限制

以下功能**无法在非原厂设备实现**，原因是系统级限制：

| 功能 | 原因 |
|---|---|
| **小米云同步** | 需要 `com.xiaomi.account`（PLATFORM 签名） |
| **三星账户登录** | 需要 `saccount`（三星平台签名 + 系统权限） |
| **MIUI 专有框架方法** | 如 `Intent.getMiuiFlags()` 改在 framework.jar 内 |
| **私有共享库** | 需要原厂 system 分区 |

---

## 扩展新厂商

方案完全通用，四步照搬：

1. 改 `targetSdk` → 35
2. 从原版恢复 META-INF
3. 用 `fix_axml.py` 补 `uses-library`
4. 根据崩溃日志写 shim 类 → `inject_shim.py` 注入

**排查思路**：每次启动看 `logcat` 里的 `NoClassDefFoundError` / `NoSuchMethodError` / `NoSuchFieldError`，缺什么补什么。

---

## 免责声明

所有应用版权归各自厂商（小米、三星等）所有。

本项目仅提供**兼容性补丁和移植工具**，不修改应用的核心功能，仅供个人学习研究使用。

请支持正版，购买对应厂商的设备。

---

**Charming × DeepSeek Harness**

2026
