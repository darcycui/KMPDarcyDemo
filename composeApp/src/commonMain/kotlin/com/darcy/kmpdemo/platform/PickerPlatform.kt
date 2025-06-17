package com.darcy.kmpdemo.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.ImageBitmap
import com.darcy.kmpdemo.log.logE
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

expect class ImagePicker {
    suspend fun pickImage(): File
}

@Composable
expect fun ShowUploadImage()


private const val uploadImageUrl = "https://10.0.0.241:7443/api/upload/image"

fun uploadFile(
    scope: CoroutineScope,
    imagePicker: ImagePicker?,
    filePath: MutableState<String>,
    imageBitmap: MutableState<ImageBitmap?>,
    uploadResult: MutableState<String>
) {
    scope.launch(Dispatchers.IO) {
        if (imagePicker == null) {
            logE("imagePicker is null", "uploadFile")
            return@launch
        }
        val pickedFile = imagePicker.pickImage()
        scope.launch(Dispatchers.Main) {
            filePath.value = pickedFile.absolutePath
            imageBitmap.value = loadImageAsBitmap(filePath.value)
        }
        runCatching {
            val response = ktorClient.submitFormWithBinaryData(
                url = uploadImageUrl,
                formData = formData {
                    append("userId", 1)
                    append(
                        "file", pickedFile.readBytes(),
                        Headers.build {
                            append(HttpHeaders.ContentType, "image/*")
                            append(HttpHeaders.ContentDisposition, "filename=${pickedFile.name}")
                        })
                }
            )
            val json = response.bodyAsText()
            uploadResult.value = "上传成功:$json"
            println(json)
        }.onFailure {
            logE(msg = "uploadFile error:${it.message}", throwable = it)
            uploadResult.value = "上传失败:${it.message}"
        }
    }
}