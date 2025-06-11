import dev.icerock.gradle.MRVisibility
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("dev.icerock.mobile.multiplatform-resources")
    alias(libs.plugins.kotlinxSerialization)
}
// config moko resources plugin
multiplatformResources {
    resourcesPackage.set("org.example.library") // required
    resourcesClassName.set("SharedRes") // optional, default MR
    resourcesVisibility.set(MRVisibility.Internal) // optional, default Public
    iosBaseLocalizationRegion.set("en") // optional, default "en"
    iosMinimalDeploymentTarget.set("11.0") // optional, default "9.0"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")
//    moko resources 不支持wasm

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            // ktor network
            // kotlin coroutine
            implementation(libs.kotlinx.coroutines.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
//            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            // moko resources
            api(libs.moko.resources)
            // moko resources for compose multiplatform
            api(libs.moko.resources.compose)
            // moko resources test
//            api(libs.moko.resources.test)
            // ktor network
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio) // CIO 引擎
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.encoding)
            // kotlinx serialization
            api(libs.kotlinx.serialization.core)
            api(libs.kotlinx.serialization.json)
            // kotlin coroutine
            implementation(libs.kotlinx.coroutines.core)
            // navigation3
            implementation(libs.org.androidx.navigation.compose)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            // ktor network
        }
    }
}

android {
    namespace = "com.darcy.kmpdemo"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.darcy.kmpdemo"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.darcy.kmpdemo.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.darcy.kmpdemo"
            packageVersion = "1.0.0"
            windows {
                // 应用名称
                packageName = "PCUtil"
                // 图标配置
                iconFile.set(project.file("src/desktopMain/resources/distribute/house_128.ico"))  // Windows
                shortcut = true
            }
        }
    }
}
