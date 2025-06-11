package com.darcy.kmpdemo.utils

import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.Parameters
import kotlin.collections.forEach

fun Map<String, String>.toFormDataContent(): FormDataContent {
    val map = this
    // create parameters builder
    val parameters = Parameters.Companion.build {
        map.forEach {
            append(it.key, it.value)
        }
    }
    return FormDataContent(parameters)
}