package com.darcy.kmpdemo.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.darcy.kmpdemo.log.logE
import org.jetbrains.skia.Image
import java.io.File

actual fun loadImageAsBitmap(filePath: String): ImageBitmap? {
    return runCatching {
        Image.makeFromEncoded(File(filePath).readBytes()).toComposeImageBitmap()
    }.onFailure {
        logE(msg = "加载图片失败: $filePath", throwable = it)
    }.getOrElse { null }
}