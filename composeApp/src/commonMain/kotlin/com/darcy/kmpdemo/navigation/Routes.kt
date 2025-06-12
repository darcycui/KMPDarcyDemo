package com.darcy.kmpdemo.navigation

import dev.icerock.moko.resources.StringResource
import kotlinx.serialization.Serializable
import org.example.library.SharedRes

@Serializable
enum class Pages(val title: StringResource) {
    Unknown(title = SharedRes.strings.unknown),
    HomePage(title = SharedRes.strings.home),
    EncryptTextPage(title = SharedRes.strings.encrypt_text),
    EncryptFilePage(title = SharedRes.strings.encrypt_file),
    LoadResourcePage(title = SharedRes.strings.load_resource),
    LoadMokoResourcePage(title = SharedRes.strings.load_moko_resource),
    KtorHttpPage(title = SharedRes.strings.ktor_http),
    KtorWebsocketPage(title = SharedRes.strings.ktor_websocket),
}
