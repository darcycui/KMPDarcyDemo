package com.darcy.kmpdemo.platform

import androidx.compose.ui.graphics.ImageBitmap


expect fun loadImageAsBitmap(filePath: String): ImageBitmap?

