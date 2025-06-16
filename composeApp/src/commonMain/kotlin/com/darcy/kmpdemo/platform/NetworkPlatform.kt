package com.darcy.kmpdemo.platform

import com.darcy.kmpdemo.bean.http.base.BaseResult
import com.darcy.kmpdemo.exception.http.HttpException
import com.darcy.kmpdemo.network.ssl.SslSettings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.isSuccess
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val ktorClient: HttpClient
    // CIO 异步协程 支持 JVM Android Native(iOS) 支持 http1.x 和 websocket
    get() = HttpClient(CIO) {
        // timeout milliseconds
        install(HttpTimeout) {
            socketTimeoutMillis = 30_000
            requestTimeoutMillis = 30_000
        }
        //logging
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    println("KtorClientDesktop: $message")
                }
            }
        }
        // retry
        install(HttpRequestRetry) {
            maxRetries = 1
            exponentialDelay() // 指数增长延迟
            retryIf { request, response ->
                !response.status.isSuccess()
            }
            retryOnExceptionIf { request, cause ->
                cause is Exception
            }
            delayMillis { retry ->
                retry * 3000L
            } // retries in 3, 6, 9, etc. seconds
        }
        // intercept: monitor and retry request
        install(HttpSend) {
            maxSendCount = 20
        }

        // defaultRequest baseUrl headers
        defaultRequest {
            header("Content-Type", "application/json")
            header("Authorization", "Bearer BuildConfig.OPENAI_API_KEY")
            url {
                protocol = URLProtocol.HTTPS
                host = "darcycui.com.cn"
                path("api/")
            }
        }
        // contentNegotiation serialization
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
        // config ssl
        engine {
            https {
                trustManager = SslSettings.getTrustManager()
            }
        }
        // install websocket
        install(WebSockets) {
            pingIntervalMillis = 20_000
            maxFrameSize = 8 * 1024
            contentConverter = null
        }
        // response validation
        expectSuccess = true
        HttpResponseValidator {
            // 2xx codes
            validateResponse { response ->
                val error: BaseResult<String> = response.body()
                if (error.resultCode != 200) {
                    throw HttpException(error.resultCode, error.reason)
                }
            }
            // not 2xx codes
            handleResponseException { exception, request ->
                val clientException = exception as? ClientRequestException
                    ?: return@handleResponseException
                val exceptionResponse = clientException.response
                when (exceptionResponse.status) {
                    HttpStatusCode.BadRequest -> {
                        throw HttpException.EXCEPTION_400
                    }
                    HttpStatusCode.NotFound -> {
                        throw HttpException.EXCEPTION_404
                    }
                    HttpStatusCode.InternalServerError -> {
                        throw HttpException.EXCEPTION_500
                    }
                }
            }
        }

    }.also {
        // intercept: modify request before sending
        it.plugin(HttpSend).intercept { request ->
            println("ktorClient==> HttpSend interceptor run...")
            val newRequest = HttpRequestBuilder().takeFrom(request)
            // add headers
            newRequest.header("User-Agent", "KMP Client by Ktor")
            newRequest.header("token", "KMP Client token")
            execute(newRequest)
        }
    }
