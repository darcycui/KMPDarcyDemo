package com.darcy.kmpdemo.platform

import android.content.Context
import androidx.core.content.ContextCompat
import com.darcy.kmpdemo.app.AppContextProvider
import java.io.File

actual fun createADirectory(path: String): Boolean {
    val dir = File(path)
    return if (!dir.exists()) {
        dir.mkdirs()
    } else {
        true
    }
}

actual fun getCacheDir(): File {
    val context = getAndroidContext()
    return ContextCompat.getExternalCacheDirs(context).firstOrNull()
        ?: context.externalCacheDir
        ?: throw IllegalStateException("cacheDir is null")
}

actual fun getDocumentsDir(): File {
    val context = getAndroidContext()
    return ContextCompat.getExternalFilesDirs(context, DarcyFolder.DIR_DOCUMENT).firstOrNull()
        ?: context.getExternalFilesDir(DarcyFolder.DIR_DOCUMENT)
        ?: throw IllegalStateException("documentsDir null")
}

actual fun getDownloadDir(): File {
    val context = getAndroidContext()
    return ContextCompat.getExternalFilesDirs(context, DarcyFolder.DIR_DOWNLOAD).firstOrNull()
        ?: context.getExternalFilesDir(DarcyFolder.DIR_DOCUMENT)
        ?: throw IllegalStateException("cacheDir is null")
}

// 获取 Android 上下文
private fun getAndroidContext(): Context {
    return AppContextProvider.getAppContext()
}