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
import com.darcy.kmpdemo.platform.ImagePicker
import com.darcy.kmpdemo.platform.loadImageAsBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//@Composable
//fun ShowUploadImage() {
//    val scope: CoroutineScope = rememberCoroutineScope()
//    val filePath: MutableState<String> = remember { mutableStateOf("unknown") }
//    val imageBitmap: MutableState<ImageBitmap?> = remember { mutableStateOf(null) }
//    val scrollState: ScrollState = rememberScrollState()
//    val imagePicker: ImagePicker = remember { ImagePicker() }
//
//    Column(
//        modifier = Modifier.verticalScroll(scrollState).fillMaxSize(),
//        // 垂直间距
//        verticalArrangement = Arrangement.spacedBy(5.dp)
//    ) {
//        Button(onClick = { uploadFile(scope, imagePicker, filePath, imageBitmap) }) {
//            Text(text = "选择并上传图片")
//        }
//        Text(text = filePath.value)
//        imageBitmap.value?.let {
//            Image(
//                bitmap = it,
//                contentDescription = "本地图片",
//                modifier = Modifier.fillMaxSize()
//            )
//        } ?: Text("无法加载图片")
//
//    }
//}

