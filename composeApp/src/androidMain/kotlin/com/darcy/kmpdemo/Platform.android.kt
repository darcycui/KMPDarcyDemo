package com.darcy.kmpdemo

import android.os.Build
import com.darcy.kmpdemo.utils.CommonEncryptUtil

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val version: String = "1.0.0"
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual fun encryptString(str: String?): String {
    return CommonEncryptUtil.encryptString(str)
}

actual fun decryptString(str: String?): String {
    return CommonEncryptUtil.decryptString(str)
}