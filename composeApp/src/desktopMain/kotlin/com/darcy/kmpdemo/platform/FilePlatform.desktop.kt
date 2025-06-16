package com.darcy.kmpdemo.platform

import java.io.File

actual fun createADirectory(path: String): Boolean {
    return File(path).mkdirs()
}

actual fun getCacheDir(): File {
    return File(System.getProperty("user.home"), ".cache/your_app_name")
}

actual fun getDocumentsDir(): File {
    return File(System.getProperty("user.home"), "Documents")
}

actual fun getDownloadDir(): File {
    return File(System.getProperty("user.home"), "Downloads")
}
