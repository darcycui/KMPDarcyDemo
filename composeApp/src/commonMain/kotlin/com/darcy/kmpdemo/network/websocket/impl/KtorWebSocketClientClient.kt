package com.darcy.kmpdemo.network.websocket.impl

import com.darcy.kmpdemo.bean.websocket.MessageEntity
import com.darcy.kmpdemo.network.parser.impl.kotlinxJson
import com.darcy.kmpdemo.network.websocket.IWebSocketClient
import com.darcy.kmpdemo.network.websocket.heartbeat.HeartbeatHelper
import com.darcy.kmpdemo.network.websocket.heartbeat.PING
import com.darcy.kmpdemo.network.websocket.heartbeat.PONG
import com.darcy.kmpdemo.network.websocket.listener.IOuterListener
import com.darcy.kmpdemo.platform.ktorClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import java.time.LocalDateTime

class KtorWebSocketClientClient : IWebSocketClient, IOuterListener {
    companion object {
        private val TAG = KtorWebSocketClientClient::class.java.simpleName
    }

    private var url: String = ""
    private var fromUser: String = ""
    private var session: WebSocketSession? = null
    private var outListener: IOuterListener? = null
    private val heartbeatHelper = HeartbeatHelper.getInstance(this)

    override fun init(url: String, fromUser: String) {
        this.url = "$url/$fromUser"
        this.fromUser = fromUser
    }

    override suspend fun connect() {
        if (outListener == null) {
            throw NullPointerException("outListener is null. please call setOutListener() first.")
        }
        if (session != null && session?.isActive == true) {
            println("$TAG already connected!")
        }
        println("$TAG connect...")
        session = ktorClient.webSocketSession(url)
        println("$TAG onOpen...")
        onOpen()

        session?.let {
            it.incoming.receiveAsFlow().filterIsInstance<Frame.Text>().filterNotNull()
                .collect { frame ->
                    val message = frame.readText()
                    println("$TAG onReceive message <-- $message")
                    onMessage(message)
                }
        } ?: run {
            println("$TAG session is null.")
            onFailure("session is null")
        }
    }

    override suspend fun disconnect() {
        println("$TAG disconnect")
        session?.let {
            it.close()
            onClosed()
            session = null
        } ?: run {
            println("$TAG already disconnected!")
            onFailure("already disconnected!")
        }
    }

    override suspend fun send(message: String, toUser: String) {
        if (PING  == message) {
            println("$TAG send ping...")
            session?.send(Frame.Text(message))
            onSend(message)
            return
        }
        val messageBean = MessageEntity(
            from = fromUser,
            to = toUser,
            message = message,
            createTime = LocalDateTime.now().toString()
        )
        val jsonMessage = kotlinxJson.encodeToString(messageBean)
        println("$TAG send message --> $jsonMessage")
        session?.send(Frame.Text(jsonMessage))
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
        heartbeatHelper.start(10_000, 5_000)
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
            heartbeatHelper.clearPongTimeoutJob()
        }
        outListener?.onMessage(message)
    }

    override fun onMessage(bytes: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun onFailure(errorMessage: String) {
        heartbeatHelper.stop()
        outListener?.onFailure("Error: $errorMessage.")
    }

    override fun onClosed() {
        heartbeatHelper.stop()
        outListener?.onClosed()
    }
}