package com.darcy.kmpdemo.bean.websocket

import com.darcy.kmpdemo.bean.IEntity
import kotlinx.serialization.Serializable

@Serializable
data class MessageEntity(
    var from: String = "",
    var to: String = "",
    var createTime: String? = "",
    var message: String = ""
): IEntity
