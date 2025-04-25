### KMP是什么
- Kotlin Multiplatform 是使用Kotlin编写的跨平台框架，支持Android、iOS、Web、桌面
- 支持共享逻辑层代码
- 支持共享UI层代码
- 生成目标平台代码，规避了跨平台框架与原生代码交互
- 性能与原生代码一致，没有运行时性能损失

### KMP 如何实现跨平台
- 逻辑层
  - 公共代码使用通用Kotlin编写，编译器将代码转换为中间表示(IR)，最终编译为对应平台的代码(JVM、LLVM IR、 JavaScript)
  - 平台差异通过expect(声明接口)/actual(平台实现)机制实现(文件操作、系统交互等) 
- UI层
  - 使用Compose Multiplatform实现
    - Compose Compiler将可组合函数声明的UI结构，生成平台无关的组件逻辑树
    - 通过渲染抽象层对接不同平台渲染引擎(Canvas/Skia)
    - 渲染引擎将组件逻辑树显示在屏幕上


### KMP项目结构
composeApp模块
- 是Kotlin模块，包含Android、iOS、Web、桌面的共享逻辑
- commonMain
  - 各平台共享逻辑
  - 使用通用的Kotlin代码编写
-  不同平台的源集在不同的文件夹中
  - androidMain
    - 支持Android平台api
    - 使用Kotlin/JVM
  - iosMain
    - 支持iOS平台api
    - 使用Kotlin/Native
  - wasmJsMain
    - 支持Web平台api
    - 使用Kotlin/Wasm
  - desktopMain
    - 支持桌面平台api
    - 使用Kotlin/JVM

iosApp模块
- 是XCode项目 用于构建iOS应用程序

### 多平台运行
运行Android
- 运行 conposeApp即可

运行Desktop
- Edit Configurations
- Add--Gradle
- 输入指令：composeApp:run
- run

运行Web
- Edit Configurations
- Add--Gradle
- 输入指令：wasmJsBrowserRun -t --quiet
- run

运行iOS
- 运行XCode
- run iosApp

### 版本对应关系
- KMP Plugin Version<-->AGP Version
https://kotlinlang.org/docs/multiplatform-compatibility-guide.html#version-compatibility

| KMP版本        | Gradle版本    | AGP版本       | Xcode |
|--------------|-------------|-------------|-------|
| 2.1.20       | 7.6.3–8.11  | 7.4.2–8.7.2 | 16.0  |
| 2.1.0–2.1.10 | 7.6.3-8.10* | 7.4.2–8.7.2 | 16.0  |
| 2.0.21       | 7.5-8.8*    | 7.4.2–8.5   | 16.0  |
| 2.0.20       | 7.5-8.8*    | 7.4.2–8.5   | 15.3  |
| 2.0.0        | 7.5-8.5     | 7.4.2–8.3   | 15.3  |
| 1.9.20       | 7.5-8.1.1   | 7.4.2–8.2   | 15.0  |

- AGP version<-->gradle version
https://developer.android.google.cn/build/releases/gradle-plugin?hl=zh-cn

| AGP版本         | 最低 Gradle 版本 |
|---------------|--------------|
| 8.9           | 8.11.1       |
| 8.8           | 8.10.2       |
| 8.7           | 8.9          |
| 8.6           | 8.7          |
| 8.5           | 8.7          |
| 8.4           | 8.6          |
| 8.3           | 8.4          |
| 8.2           | 8.2          |
| 8.1           | 8.0          |
| 8.0           | 8.0          |
| 7.4           | 7.5          |
| 7.3           | 7.4          |
| 7.2           | 7.3.3        |
| 7.1           | 7.2          |
| 7.0           | 7.0          |
| 4.2.0+        | 6.7.1        |
| 4.1.0+        | 6.5+         |
| 4.0.0+        | 6.1.1+       |
| 3.6.0 - 3.6.4 | 5.6.4+       |
| 3.5.0 - 3.5.4 | 5.4.1+       |
| 3.4.0 - 3.4.3 | 5.1.1+       |
| 3.3.0 - 3.3.3 | 4.10.1+      |
| 3.2.0 - 3.2.1 | 4.6+         |
| 3.1.0+        | 4.4+         |
| 3.0.0+        | 4.1+         |
| 2.3.0+        | 3.3+         |
| 2.1.3 - 2.2.3 | 2.14.1 - 3.5 |
| 2.0.0 - 2.1.2 | 2.10 - 2.13  |
| 1.5.0         | 2.2.1 - 2.13 |
| 1.2.0 - 1.3.1 | 2.2.1 - 2.9  |
| 1.0.0 - 1.1.3 | 2.2.1 - 2.3  |

- Android Studio Version<-->AGP Version
https://developer.android.google.cn/build/releases/gradle-plugin?hl=zh-cn

| Android Studio 版本        | AGP 版本  |
|--------------------------|---------|
| Meerkat \| 2024.3.1      | 3.2-8.9 |
| Ladybug 功能更新 \| 2024.2.2 | 3.2-8.8 |
| Ladybug \| 2024.2.1      | 3.2-8.7 |
| Koala 功能更新 \| 2024.1.2   | 3.2-8.6 |
| Koala \| 2024.1.1        | 3.2-8.5 |
| Jellyfish \| 2023.3.1    | 3.2-8.4 |
| Iguana \| 2023.2.1       | 3.2-8.3 |
| Hedgehog \| 2023.1.1     | 3.2-8.2 |
| Giraffe \| 2022.3.1      | 3.2-8.1 |
| Flamingo \| 2022.2.1     | 3.2-8.0 |
| Electric Eel \| 2022.1.1 | 3.2-7.4 |
| Dolphin \| 2021.3.1      | 3.2-7.3 |
| Chipmunk \| 2021.2.1     | 3.2-7.2 |
| Bumblebee \| 2021.1.1    | 3.2-7.1 |
| Arctic Fox \| 2020.3.1   | 3.1-7.0 |
