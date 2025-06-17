package com.darcy.kmpdemo.platform

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.darcy.kmpdemo.log.logD
import com.darcy.kmpdemo.log.logE
import kotlinx.coroutines.CoroutineScope
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class ImagePicker() {
    actual suspend fun pickImage(): File {
        // 获取当前协程并挂起
        return suspendCoroutine { cont ->
            val dialog = FileDialog(null as? Frame, "选择文件").apply {
                mode = FileDialog.LOAD // 读文件
                isVisible = true // 可见
                isMultipleMode = false // 单选
            }
            dialog.file?.let { fileName ->
                val sourceFile = File(dialog.directory, fileName)
                val documentDir = getDocumentsDir().resolve("image")
                if (!documentDir.exists()) {
                    createADirectory(documentDir.path)
                }
                val cacheFile = File(documentDir, "${System.currentTimeMillis()}_$fileName")
                sourceFile.copyTo(cacheFile, true)
                logD("选择文件成功：${cacheFile.absolutePath}")
                cont.resume(cacheFile)
            } ?: cont.resume(File("").also { logE("选择文件失败") })
        }
    }
}

@Composable
actual fun ShowUploadImage() {
    val scope: CoroutineScope = rememberCoroutineScope()
    val filePath: MutableState<String> = remember { mutableStateOf("unknown") }
    val imageBitmap: MutableState<ImageBitmap?> = remember { mutableStateOf(null) }
    val scrollState: ScrollState = rememberScrollState()
    val imagePicker: ImagePicker = remember { ImagePicker() }
    val uploadResult: MutableState<String> = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.verticalScroll(scrollState).fillMaxSize(),
        // 垂直间距
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Button(onClick = {
            uploadFile(scope, imagePicker, filePath, imageBitmap, uploadResult) }) {
            Text(text = "选择并上传图片")
        }
        Text(text = filePath.value)
        Text(text = uploadResult.value)
        imageBitmap.value?.let {
            Image(
                bitmap = it,
                contentDescription = "本地图片",
                modifier = Modifier.fillMaxSize()
            )
        } ?: Text("无法加载图片")

    }
}