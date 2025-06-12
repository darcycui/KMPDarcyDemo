package com.darcy.kmpdemo.bean.http

import com.darcy.kmpdemo.bean.IEntity
import com.darcy.kmpdemo.type.LocalDateTimeSerializer
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

// @OptIn 提示当前非稳定API
@OptIn(InternalSerializationApi::class)
@Serializable
data class IPEntity(
    @SerialName("City")
    val city: String = "",
    @SerialName("Country")
    val country: String = "",
    @SerialName("District")
    val district: String = "",
    @SerialName("Isp")
    val isp: String = "",
    @SerialName("Province")
    val province: String = "",

    @Serializable(with = LocalDateTimeSerializer::class)
    val time: LocalDateTime = LocalDateTime.now().also {
        println("time: $it")
    },
): IEntity {
    override fun toString(): String {
        return "IPEntity(city='$city', country='$country', district='$district', isp='$isp', province='$province', time=$time)"
    }
}