package com.darcy.kmpdemo.platform

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File

actual fun loadImageAsBitmap(filePath: String): ImageBitmap {
    return try {
        // 1. 创建文件对象
        val file = File(filePath)

        // 2. 检查文件是否存在
        if (!file.exists()) {
            throw IllegalArgumentException("文件不存在: $filePath")
        }

        // 3. 使用 BitmapFactory 解码文件
        val bitmap = BitmapFactory.decodeFile(filePath)
            ?: throw IllegalStateException("无法解码图片: $filePath")

        // 4. 转换为 Compose 的 ImageBitmap
        bitmap.asImageBitmap()
    } catch (e: Exception) {
        // 5. 错误处理
        throw RuntimeException("加载图片失败: ${e.message}", e)
    }
}