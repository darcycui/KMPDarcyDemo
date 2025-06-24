package com.darcy.kmpdemo.network.websocket

import com.darcy.kmpdemo.network.websocket.impl.KrossbowWebsocketClientImpl
import com.darcy.kmpdemo.network.websocket.impl.KtorWebSocketClientImpl
import com.darcy.kmpdemo.network.websocket.listener.IOuterListener

object WebSocketManager : IWebSocketClient {
//    private var iWebsocketClient: IWebSocketClient = KtorWebSocketClientImpl()
    private var iWebsocketClient: IWebSocketClient = KrossbowWebsocketClientImpl()

    fun setupWebSocketClient(iWebsocketClient: IWebSocketClient) {
        this.iWebsocketClient = iWebsocketClient
    }

    override fun init(url: String, fromUser: String) {
        iWebsocketClient.init(url, fromUser)
    }

    override suspend fun connect() {
        iWebsocketClient.connect()
    }

    override suspend fun disconnect() {
        iWebsocketClient.disconnect()
    }

    override suspend fun send(message: String, toUser: String) {
        iWebsocketClient.send(message, toUser)
    }

    override suspend fun send(bytes: ByteArray) {
        iWebsocketClient.send(bytes)
    }

    override suspend fun reconnect() {
        iWebsocketClient.reconnect()
    }

    override fun setOuterListener(listener: IOuterListener) {
        iWebsocketClient.setOuterListener(listener)
    }
}