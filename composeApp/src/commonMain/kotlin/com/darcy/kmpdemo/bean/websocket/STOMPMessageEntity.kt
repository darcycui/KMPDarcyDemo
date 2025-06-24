package com.darcy.kmpdemo.bean.websocket

import com.darcy.kmpdemo.utils.TimeUtil
import kotlinx.serialization.Serializable

@Serializable
data class STOMPMessageEntity(
    var id: Long = -1L,

    val from: String = "",
    val text: String = "",
    val recipient: String = "",
    val time: String? = TimeUtil.getCurrentTimeStamp(),
) {
    override fun toString(): String {
        return "MessageEntity(id=$id, from='$from', text='$text', recipient='$recipient', time=$time)"
    }
}
