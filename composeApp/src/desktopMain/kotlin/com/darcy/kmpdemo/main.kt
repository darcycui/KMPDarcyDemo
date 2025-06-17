package com.darcy.kmpdemo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.darcy.kmpdemo.log.initLoger

fun main() = application {
    initLoger()
    Window(
        onCloseRequest = ::exitApplication,
        title = "解密工具",
    ) {
        App()
    }
}