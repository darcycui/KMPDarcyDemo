package com.darcy.kmpdemo.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import java.io.File

actual fun loadImageAsBitmap(filePath: String): ImageBitmap {
    return Image.makeFromEncoded(File(filePath).readBytes()).toComposeImageBitmap()
}