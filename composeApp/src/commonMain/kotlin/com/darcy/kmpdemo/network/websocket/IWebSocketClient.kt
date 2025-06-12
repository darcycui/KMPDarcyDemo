package com.darcy.kmpdemo.network.websocket

import com.darcy.kmpdemo.network.websocket.listener.IOuterListener

interface IWebSocketClient {
    fun init(url: String, fromUser: String)
    suspend  fun connect()
    suspend fun disconnect()
    suspend fun send(message: String, toUser: String)
    suspend fun send(bytes: ByteArray)
    suspend fun reconnect()
    fun setOuterListener(listener: IOuterListener)
}