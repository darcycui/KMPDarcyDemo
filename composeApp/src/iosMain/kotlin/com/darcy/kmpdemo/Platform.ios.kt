package com.darcy.kmpdemo

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val version: String = "1.0.0"
}

actual fun getPlatform(): Platform = IOSPlatform()