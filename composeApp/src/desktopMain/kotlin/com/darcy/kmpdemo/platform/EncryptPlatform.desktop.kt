package com.darcy.kmpdemo.platform

import com.darcy.kmpdemo.utils.CommonEncryptUtil

actual fun encryptString(str: String?): String {
    return CommonEncryptUtil.encryptString(str)
}

actual fun decryptString(str: String?): String {
    return CommonEncryptUtil.decryptString(str)
}