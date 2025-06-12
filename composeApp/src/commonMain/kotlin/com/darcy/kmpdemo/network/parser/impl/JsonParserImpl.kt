package com.darcy.kmpdemo.network.parser.impl

import com.darcy.kmpdemo.bean.http.base.BaseResult
import com.darcy.kmpdemo.network.parser.IJsonParser
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.modules.SerializersModule

val kotlinxJson = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
    coerceInputValues = true
    serializersModule = SerializersModule {
        // 注册BaseResult的上下文序列化器 KSerializer
    }
}

class JsonParserImpl : IJsonParser {
    override fun <T> toBean(
        json: String,
        kSerializer: KSerializer<T>?,
        success: ((BaseResult<T>?) -> Unit)?,
        successList: ((BaseResult<List<T>>?) -> Unit)?,
        error: ((String) -> Unit)?
    ) {
        // 解析时先转换为 JsonElement
        val jsonElement = kotlinxJson.parseToJsonElement(json)
        // 根据result字段判断是object还是array
        val resultElement = jsonElement.jsonObject["result"]
        when (resultElement) {
            is JsonObject -> {
                kSerializer?.let {
                    // 创建序列化器
                    // https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serializers.md#builtin-primitive-serializers
                    val realSerializer = BaseResult.createSerializer(it)
                    success?.invoke(
                        kotlinxJson.decodeFromString<BaseResult<T>>(realSerializer, json)
                    )
                } ?: run {
                    println("json is JsonObject but kSerializer is null")
                    error?.invoke("json is JsonObject but kSerializer is null")
                }
            }

            is JsonArray -> {
                kSerializer?.let {
                    val realSerializer = BaseResult.createSerializerList(it)
                    successList?.invoke(
                        kotlinxJson.decodeFromString<BaseResult<List<T>>>(realSerializer, json)
                    )
                } ?: run {
                    println("json is JsonArray but kSerializer is null")
                    error?.invoke("json is JsonArray but kSerializer is null")
                }
            }

            else -> {
                throw Exception("json result is not object or array")
            }
        }
    }
}