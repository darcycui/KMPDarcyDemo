package com.darcy.kmpdemo.platform

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.darcy.kmpdemo.log.logE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

actual class ImagePicker(
    private val context: Context,
) {
    private var pickedFile: File? = null

    fun setPickedImage(pickedFile: File?) {
        this.pickedFile = pickedFile
    }

    actual suspend fun pickImage(): File {
        return pickedFile ?: File("")
    }
}

// 复制图片到缓存目录
private fun copyImageToCache(context: Context, uri: Uri): File? {
    val cacheDir = File(getDocumentsDir(), "image").apply { mkdirs() }
    val cacheFile = File(cacheDir, "${System.currentTimeMillis()}.jpg")
    return context.contentResolver.openInputStream(uri)?.use { input ->
        cacheFile.outputStream().use { output ->
            input.copyTo(output)
            cacheFile // 返回缓存文件
        }
    }
}

@Composable
actual fun ShowUploadImage() {
    val scope: CoroutineScope = rememberCoroutineScope()
    val filePath: MutableState<String> = remember { mutableStateOf("unknown") }
    val imageBitmap: MutableState<ImageBitmap?> = remember { mutableStateOf(null) }
    val scrollState: ScrollState = rememberScrollState()
    val context = LocalContext.current
    var imagePicker: ImagePicker? = remember { ImagePicker(context) }
    val uploadResult: MutableState<String> = remember { mutableStateOf("") }
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri == null || imagePicker == null) {
                logE("uri or imagePicker is null")
                return@rememberLauncherForActivityResult
            }
            scope.launch(Dispatchers.IO) {
                val cacheFile = copyImageToCache(context, uri)
                imagePicker.setPickedImage(cacheFile)
                uploadFile(scope, imagePicker, filePath, imageBitmap, uploadResult)
            }
        }
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize(),
        // 垂直间距
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Button(onClick = {
            // 启动图片选择
            pickImageLauncher.launch("image/*")
        }) {
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