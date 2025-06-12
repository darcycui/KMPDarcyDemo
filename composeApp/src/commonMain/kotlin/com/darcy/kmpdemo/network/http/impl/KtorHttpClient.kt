package com.darcy.kmpdemo.network.http.impl

import com.darcy.kmpdemo.bean.http.base.BaseResult
import com.darcy.kmpdemo.network.http.IHttp
import com.darcy.kmpdemo.network.parser.impl.JsonParserImpl
import com.darcy.kmpdemo.platform.ktorClient
import com.darcy.kmpdemo.utils.toFormDataContent
import com.darcy.kmpdemo.utils.toUrlEncodedString
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.KSerializer

class KtorHttpClient : IHttp {
    private val jsonParser by lazy {
        JsonParserImpl()
    }

    override suspend fun <T> doGetRequest(
        serializer: KSerializer<T>,
        url: String,
        params: Map<String, String>,
        success: ((BaseResult<T>?) -> Unit)?,
        successList: ((BaseResult<List<T>>?) -> Unit)?,
        error: ((String) -> Unit)?
    ) {
        val realUrl = url + "?" + params.toUrlEncodedString()
        val json = ktorClient.get(realUrl).bodyAsText()
        jsonParser.toBean(json, serializer, success, successList, error)
    }

    override suspend fun <T> doPostRequest(
        serializer: KSerializer<T>,
        url: String,
        params: Map<String, String>,
        success: ((BaseResult<T>?) -> Unit)?,
        successList: ((BaseResult<List<T>>?) -> Unit)?,
        error: ((String) -> Unit)?
    ) {
        val formDataContent = params.toFormDataContent()
        val json = ktorClient.post(url) {
            this.header("User-Agent", "KMP Client by Ktor")
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(formDataContent)
        }.bodyAsText()
        jsonParser.toBean(json, serializer, success, successList, error)
    }
}