package com.darcy.kmpdemo.bean

import com.darcy.kmpdemo.bean.base.IEntity
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

// @OptIn 提示当前非稳定API
@OptIn(InternalSerializationApi::class)
@Serializable
data class UserEntity(
    val id: Long = -1L,
    val name: String = "",
) : IEntity {
    override fun toString(): String {
        return "UserEntity(id=$id, name='$name')"
    }
}