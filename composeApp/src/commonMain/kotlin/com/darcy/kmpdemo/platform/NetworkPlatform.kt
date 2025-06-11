package com.darcy.kmpdemo.platform

import com.darcy.kmpdemo.network.ssl.SslSettings
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

val ktorClient: HttpClient
    // CIO 异步协程 支持 JVM Android Native(iOS) 支持 http1.x 和 websocket
    get() = HttpClient(CIO) {
        //Timeout plugin to set up timeout milliseconds for client
        install(HttpTimeout) {
            socketTimeoutMillis = 60_000
            requestTimeoutMillis = 60_000
        }
        //Logging plugin combined with kermit(KMP Logger library)
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    println("KtorClientDesktop: $message")
                }
            }
        }
        //We can configure the BASE_URL and also
        //the default headers by defaultRequest builder
        defaultRequest {
            header("Content-Type", "application/json")
            header("Authorization", "Bearer BuildConfig.OPENAI_API_KEY")
            url("https://api.openai.com/v1/")
        }
        //ContentNegotiation plugin for negotiation media types between the client and server
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
                println("engine https-1")
                println("engine https-2")
                trustManager = SslSettings.getTrustManager()
                println("engine https-3")
            }
        }
    }
