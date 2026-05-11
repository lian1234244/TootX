# ROOTX - Root工具箱

一款极客风格的 Android Root 玩机工具箱应用。

## 特性

- 🎨 极客风开屏动画（终端风格光标 + 故障风Logo展开）
- 🔧 Root权限管理（基于 Libsu）
- 📱 纯 Jetpack Compose UI

## 技术栈

| 技术 | 版本 |
|------|------|
| Kotlin | 1.9.22 |
| Jetpack Compose | BOM 2024.01.00 |
| Libsu | 5.2.2 |
| minSdk | 24 |
| targetSdk | 34 |

## 构建

```bash
# 克隆项目
git clone https://github.com/lian1234244/rootX.git
cd rootX

# 构建 Debug APK
./gradlew assembleDebug

# 构建 Release APK
./gradlew assembleRelease
```

## 项目结构

```
app/src/main/java/com/root/toolbox/
├── MainActivity.kt        # 主活动 + 开屏动画
└── MainActivityTheme.kt   # Compose主题配置
```

## License

MIT License
