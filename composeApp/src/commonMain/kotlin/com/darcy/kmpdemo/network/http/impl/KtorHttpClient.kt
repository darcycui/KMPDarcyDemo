package com.darcy.kmpdemo.network.http.impl

import com.darcy.kmpdemo.bean.http.base.BaseResult
import com.darcy.kmpdemo.network.http.IHttp
import com.darcy.kmpdemo.network.parser.impl.JsonParserImpl
import com.darcy.kmpdemo.platform.ktorClient
import com.darcy.kmpdemo.utils.toFormDataContent
import com.darcy.kmpdemo.utils.toUrlEncodedString
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import jdk.javadoc.internal.tool.Main.execute
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer

class KtorHttpClient : IHttp {
    companion object {
        private val TAG = KtorHttpClient::class.java.simpleName
    }

    private val exceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            println("$TAG exceptionHandler: ${throwable.message}")
            throwable.printStackTrace()
        }
    private val scope: CoroutineScope =
        CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)
    private val jsonParser by lazy {
        JsonParserImpl()
    }

    override fun <T> doGetRequest(
        serializer: KSerializer<T>,
        url: String,
        params: Map<String, String>,
        needRetry: Boolean,
        needCache: Boolean,
        success: ((BaseResult<T>?) -> Unit)?,
        successList: ((BaseResult<List<T>>?) -> Unit)?,
        error: ((String) -> Unit)?
    ) {
        scope.launch {
            // dealRetry(needRetry)
            // dealCache(needCache)
            val realUrl = url + "?" + params.toUrlEncodedString()
            val json = ktorClient.get(realUrl).bodyAsText()
            jsonParser.toBean(json, serializer, success, successList, error)
        }
    }

    override fun <T> doPostRequest(
        serializer: KSerializer<T>,
        url: String,
        params: Map<String, String>,
        needRetry: Boolean,
        needCache: Boolean,
        success: ((BaseResult<T>?) -> Unit)?,
        successList: ((BaseResult<List<T>>?) -> Unit)?,
        error: ((String) -> Unit)?
    ) {
        scope.launch {
            val formDataContent = params.toFormDataContent()
            val json = ktorClient.post(url) {
                this.header("User-Agent", "KMP Client by Ktor")
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(formDataContent)
            }.bodyAsText()
            jsonParser.toBean(json, serializer, success, successList, error)
        }
    }
}