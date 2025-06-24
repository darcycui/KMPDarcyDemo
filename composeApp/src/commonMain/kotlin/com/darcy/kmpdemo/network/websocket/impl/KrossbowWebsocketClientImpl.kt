package com.darcy.kmpdemo.network.websocket.impl

import com.darcy.kmpdemo.bean.websocket.STOMPMessageEntity
import com.darcy.kmpdemo.network.parser.impl.kotlinxJson
import com.darcy.kmpdemo.network.websocket.IWebSocketClient
import com.darcy.kmpdemo.network.websocket.heartbeat.PING
import com.darcy.kmpdemo.network.websocket.heartbeat.PONG
import com.darcy.kmpdemo.network.websocket.listener.IOuterListener
import com.darcy.kmpdemo.platform.ktorClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.newSingleThreadContext
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.config.HeartBeat
import org.hildan.krossbow.stomp.config.HeartBeatTolerance
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.WebSocketClient
import org.hildan.krossbow.websocket.builtin.builtIn
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient
import java.time.LocalDateTime
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class KrossbowWebsocketClientImpl : IWebSocketClient, IOuterListener {
    companion object {
        private val TAG = KrossbowWebsocketClientImpl::class.java.simpleName
    }

    private var url: String = ""
    private var fromUser: String = ""
    private var outListener: IOuterListener? = null
    private var session: StompSession? = null

    private val dispatcher: CoroutineDispatcher = newSingleThreadContext("websocket-stomp")
    private val exceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            println("KrossbowWebsocketClientImpl exceptionHandler: ${throwable.message}")
            throwable.printStackTrace()
        }
    private val scope = CoroutineScope(dispatcher + SupervisorJob() + exceptionHandler)

        val kcrossbowWebsocketClient: KtorWebSocketClient = KtorWebSocketClient(ktorClient)
//    val kcrossbowWebsocketClient: WebSocketClient = WebSocketClient.builtIn()
    val stompClient = StompClient(kcrossbowWebsocketClient) {
        connectionTimeout = 10.toDuration(DurationUnit.SECONDS)
        disconnectTimeout = 10.toDuration(DurationUnit.SECONDS)
//        receiptTimeout = 10.toDuration(DurationUnit.SECONDS)
//        subscriptionCompletionTimeout = 10.toDuration(DurationUnit.SECONDS)
//        autoReceipt = true
//        gracefulDisconnect = false
//        connectWithStompCommand = true
//        heartBeat = HeartBeat()
//        heartBeatTolerance = HeartBeatTolerance()
//        defaultSessionCoroutineContext = dispatcher + SupervisorJob() + exceptionHandler
    }

    override fun init(url: String, fromUser: String) {
        this.url = url
        this.fromUser = fromUser
    }

    override suspend fun connect() {
        if (outListener == null) {
            throw NullPointerException("outListener is null. please call setOutListener() first.")
        }
        if (session != null) {
            println("$TAG already connected!")
        }
        runCatching {
            println("$TAG connect...")
            session = stompClient.connect(
                this.url,
                customStompConnectHeaders = mapOf(
                    "Authorization" to fromUser,
//                    "accept-version" to "1.2,1.1,1.0"
                )
            )
            println("$TAG onOpen...")
            onOpen()

            session?.let {
//            scope.launch {
                it.subscribeText("/user/queue/msg").collect { message ->
                    println("$TAG onReceive message <-- $message")
                    onMessage(message)
                }
//            }
            } ?: run {
                println("$TAG session is null.")
                onFailure("session is null")
            }
        }.onFailure {
            println("$TAG connect error: ${it.message}")
            onFailure(it.message ?: "")
        }
    }

    override suspend fun disconnect() {
        println("$TAG disconnect")
        session?.let {
            it.disconnect()
            onClosed()
            session = null
        } ?: run {
            println("$TAG already disconnected!")
            onFailure("already disconnected!")
        }
    }

    override suspend fun send(message: String, toUser: String) {
        if (PING == message) {
            println("$TAG send ping...")
            session?.sendText("/app/chat", message)
            onSend(message)
            return
        }
        val messageBean = STOMPMessageEntity(
            from = fromUser,
            recipient = toUser,
            text = message,
            time = LocalDateTime.now().toString()
        )
        val jsonMessage = kotlinxJson.encodeToString(messageBean)
        println("$TAG send message --> $jsonMessage")
        session?.sendText("/app/chat", jsonMessage)
        onSend(jsonMessage)
    }

    override suspend fun send(bytes: ByteArray) {
        TODO("Not yet implemented")
    }

    override suspend fun reconnect() {
        disconnect()
        delay(1_000)
        connect()
    }

    override fun setOuterListener(listener: IOuterListener) {
        this.outListener = listener
    }

    override fun onOpen() {
        outListener?.onOpen()
    }

    override fun onSend(message: String) {
        outListener?.onSend(message)
    }

    override fun onSend(bytes: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun onMessage(message: String) {
        if (PONG == message) {
            println("$TAG receive pong...")
        }
        outListener?.onMessage(message)
    }

    override fun onMessage(bytes: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun onFailure(errorMessage: String) {
        outListener?.onFailure("Error: $errorMessage.")
    }

    override fun onClosed() {
        outListener?.onClosed()
    }
}