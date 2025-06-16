package com.darcy.kmpdemo.platform

import java.io.File
object DarcyFolder {
    const val DIR_CACHE = "Cache"
    const val DIR_DOCUMENT = "Documents"
    const val DIR_DOWNLOAD = "Download"
}

// 期望函数：创建文件夹
expect fun createADirectory(path: String): Boolean

// 期望函数：获取缓存目录
expect fun getCacheDir(): File

// 期望函数：获取文档目录
expect fun getDocumentsDir(): File

// 期望函数：获取下载目录
expect fun getDownloadDir(): File
