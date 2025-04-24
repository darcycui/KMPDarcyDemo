buildscript {
    repositories {
        gradlePluginPortal()
    }

    dependencies {
        // 添加moko资源库 支持color string drawable等
        classpath (libs.moko.resources.generator)
    }
}
plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}