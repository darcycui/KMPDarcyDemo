package com.darcy.kmpdemo.ui

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
import com.darcy.kmpdemo.platform.ktorClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.prepareGet
import io.ktor.http.ContentType
import io.ktor.http.contentLength
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.remaining
import io.ktor.utils.io.exhausted
import io.ktor.utils.io.readRemaining
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.io.asSink
import java.io.File
import com.darcy.kmpdemo.platform.loadImageAsBitmap
import com.darcy.kmpdemo.platform.createADirectory
import com.darcy.kmpdemo.platform.getDownloadDir
import com.darcy.kmpdemo.utils.suffix
import sun.nio.ch.Net.accept

@Composable
fun ShowDownloadImage() {
    val scope: CoroutineScope = rememberCoroutineScope()
    val filePath: MutableState<String> = remember { mutableStateOf("unknown") }
    val imageBitmap: MutableState<ImageBitmap?> = remember { mutableStateOf(null) }
    val scrollState: ScrollState = rememberScrollState()

    Column(
        modifier = Modifier.verticalScroll(scrollState).fillMaxSize(),
        // 垂直间距
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Button(onClick = { downloadFile(scope, filePath, imageBitmap) }) {
            Text(text = "下载图片")
        }
        Text(text = filePath.value)
        imageBitmap.value?.let {
            Image(
                bitmap = it,
                contentDescription = "本地图片",
                modifier = Modifier.fillMaxSize()
            )
        } ?: Text("无法加载图片")

    }
}

//private const val downloadImageUrl = "https://10.0.0.241:7443/api/download/image/a1.png"
//private const val downloadImageUrl = "https://10.0.0.241:7443/api/download/image/android.exe"
//private const val downloadImageUrl = "https://10.0.0.241:7443/api/download/image/jar.jar"
private const val downloadImageUrl = "https://10.0.0.241:7443/api/download/image/yaml.yaml"

private fun downloadFile(
    scope: CoroutineScope,
    filePath: MutableState<String>,
    imageBitmap: MutableState<ImageBitmap?>
) {
    scope.launch(Dispatchers.IO) {
        // 创建下载目录
        val downloadDir = getDownloadDir().resolve("image")
        if (!downloadDir.exists()) {
            createADirectory(downloadDir.path)
        }
        val file = File.createTempFile("image_", downloadImageUrl.suffix(), downloadDir)
        val stream = file.outputStream().asSink()
        // download file in stream
        ktorClient.prepareGet(downloadImageUrl) {
            accept(ContentType.Image.Any)
        }.execute() { httpResponse ->
            val channel: ByteReadChannel = httpResponse.body()
            var count = 0L
            stream.use {
                while (!channel.exhausted()) {
                    val chunk = channel.readRemaining()
                    count += chunk.remaining
                    chunk.transferTo(stream)
                    println("Received $count bytes from ${httpResponse.contentLength()}")
                }
            }

        }
        scope.launch(Dispatchers.Main) {
            filePath.value = file.absolutePath
            imageBitmap.value = loadImageAsBitmap(filePath.value)
        }
    }
}
