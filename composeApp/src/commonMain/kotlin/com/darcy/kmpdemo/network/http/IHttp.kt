package com.darcy.kmpdemo.network.http

import com.darcy.kmpdemo.bean.http.base.BaseResult
import kotlinx.serialization.KSerializer

interface IHttp {
    fun <T> doGetRequest(
        serializer: KSerializer<T>,
        url: String,
        params: Map<String, String> = mapOf(),
        needRetry: Boolean,
        needCache: Boolean,
        success: ((BaseResult<T>?) -> Unit)?,
        successList: ((BaseResult<List<T>>?) -> Unit)?,
        error: ((String) -> Unit)?
    )

    fun <T> doPostRequest(
        serializer: KSerializer<T>,
        url: String,
        params: Map<String, String> = mapOf(),
        needRetry: Boolean,
        needCache: Boolean,
        success: ((BaseResult<T>?) -> Unit)?,
        successList: ((BaseResult<List<T>>?) -> Unit)?,
        error: ((String) -> Unit)?
    )
}