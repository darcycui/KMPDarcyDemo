package com.darcy.kmpdemo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.colorResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.readTextAsState
import dev.icerock.moko.resources.compose.stringResource
import org.example.library.SharedRes

@Composable
fun ShowLoadMokoResource() {
    val mokoFileContentState: String? by SharedRes.files.txt.moko_message_txt.readTextAsState()
// FIXME: No value passed for parameter 'context'
//    val mokoFileContent: String = SharedRes.files.txt.moko_message_txt.readText()

    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = "加载字符串资源")
        Text(text = stringResource(SharedRes.strings.my_string))
        Text(text = "加载颜色资源")
        Text(
            text = stringResource(SharedRes.strings.my_string),
            color = colorResource(SharedRes.colors.darcy_purple_200)
        )
        Text(text = "加载文件资源")
        Text(text = mokoFileContentState ?: "moko file error")
        Text(text = "加载图片资源")
        Image(
            painter = painterResource(SharedRes.images.icon_house),
            contentDescription = null,
            modifier = Modifier.width(100.dp).height(100.dp)
        )
    }

}