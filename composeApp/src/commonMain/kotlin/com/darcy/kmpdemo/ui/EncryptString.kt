package com.darcy.kmpdemo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.darcy.kmpdemo.decryptString
import com.darcy.kmpdemo.encryptString
import com.darcy.kmpdemo.getPlatform
import dev.icerock.moko.resources.compose.readTextAsState
import kmpdarcydemo.composeapp.generated.resources.Res
import kmpdarcydemo.composeapp.generated.resources.common_app_name
import kmpdarcydemo.composeapp.generated.resources.icon_star
import org.example.library.SharedRes
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import dev.icerock.moko.resources.compose.stringResource as mokoStringResource
import dev.icerock.moko.resources.compose.colorResource as mokoColorResource
import dev.icerock.moko.resources.compose.painterResource as mokoPainterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun EncryptString() {
    var content by remember { mutableStateOf(getPlatform().name + " " + getPlatform().version) }
    var bytes by remember {
        mutableStateOf(ByteArray(0))
    }
    val mokoFileContent: String? by SharedRes.files.txt.moko_message_txt.readTextAsState()
    LaunchedEffect(Unit) {
        bytes = Res.readBytes("files/txt/message.txt")
    }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            Image(painter = painterResource(Res.drawable.icon_star), contentDescription = null,
                modifier = Modifier.width(30.dp).height(30.dp))
            Text(text = stringResource(Res.string.common_app_name))
        }
        Row {
            Image(painter = mokoPainterResource(SharedRes.images.icon_house), contentDescription = null,
                modifier = Modifier.width(30.dp).height(30.dp))
        Text(
            text = mokoStringResource(SharedRes.strings.my_string),
            color = mokoColorResource(SharedRes.colors.darcy_red)
        )
        }
        Text(text = content, color = mokoColorResource(SharedRes.colors.darcy_purple_200))
        Text(bytes.decodeToString())
        Text(mokoFileContent ?: "moko file default")
        Button(onClick = { content = encryptString(content) }) {
            Text("Encrypt")
        }
        Button(onClick = { content = decryptString(content) }) {
            Text("Decrypt")
        }
        TextField(value = content, onValueChange = { content = it })

    }
}