package com.darcy.kmpdemo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "解密工具",
    ) {
        App()
    }
}