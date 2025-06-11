package com.darcy.kmpdemo.network.http

import com.darcy.kmpdemo.bean.base.BaseResult
import com.darcy.kmpdemo.network.http.impl.KtorHttpClient
import kotlinx.serialization.KSerializer

object HttpManager : IHttp {
    private val iHttp: IHttp = KtorHttpClient()

    override suspend fun <T> doGetRequest(
        serializer: KSerializer<T>,
        url: String,
        params: Map<String, String>,
        success: ((BaseResult<T>?) -> Unit)?,
        successList: ((BaseResult<List<T>>?) -> Unit)?,
        error: ((String) -> Unit)?
    ) {
        iHttp.doGetRequest(serializer, url, params, success, successList, error)
    }

    override suspend fun <T> doPostRequest(
        serializer: KSerializer<T>,
        url: String,
        params: Map<String, String>,
        success: ((BaseResult<T>?) -> Unit)?,
        successList: ((BaseResult<List<T>>?) -> Unit)?,
        error: ((String) -> Unit)?
    ) {
        iHttp.doPostRequest(serializer, url, params, success, successList, error)
    }
}