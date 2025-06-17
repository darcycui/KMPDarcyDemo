package com.darcy.kmpdemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.darcy.kmpdemo.log.logE
import com.darcy.kmpdemo.log.logV
import com.darcy.kmpdemo.navigation.AppNavigation
import com.darcy.kmpdemo.network.ssl.SslSettings
import kmpdarcydemo.composeapp.generated.resources.Res
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    SslCertsConfig()
    AppNavigation()
}

@Composable
fun SslCertsConfig() {
    var bytesServer by remember {
        mutableStateOf(ByteArray(0))
    }
    var bytesIP by remember {
        mutableStateOf(ByteArray(0))
    }
    LaunchedEffect(Unit) {
        logV("SslCertsConfig: init ssl certs")
        bytesServer = Res.readBytes(SslSettings.KEYSTORE_PATH_SERVER)
        bytesIP = Res.readBytes(SslSettings.KEYSTORE_PATH_IP)
        SslSettings.initCertBytes(listOf(bytesServer, bytesIP))
    }
}