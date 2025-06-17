package com.darcy.kmpdemo.network.http

import com.darcy.kmpdemo.bean.http.base.BaseResult
import com.darcy.kmpdemo.network.http.impl.KtorHttpClient
import kotlinx.serialization.KSerializer

object HttpManager : IHttp {
    private val iHttp: IHttp = KtorHttpClient()

    override fun <T> doGetRequest(
        serializer: KSerializer<T>,
        url: String,
        params: Map<String, String>,
        needRetry: Boolean,
        needCache: Boolean,
        success: ((BaseResult<T>?) -> Unit)?,
        successList: ((BaseResult<List<T>>?) -> Unit)?,
        errors: ((String) -> Unit)?
    ) {
        iHttp.doGetRequest(serializer, url, params, needRetry, needCache, success, successList,
            errors
        )
    }

    override fun <T> doPostRequest(
        serializer: KSerializer<T>,
        url: String,
        params: Map<String, String>,
        needRetry: Boolean,
        needCache: Boolean,
        success: ((BaseResult<T>?) -> Unit)?,
        successList: ((BaseResult<List<T>>?) -> Unit)?,
        errors: ((String) -> Unit)?
    ) {
        iHttp.doPostRequest(serializer, url, params, needRetry, needCache, success, successList,
            errors
        )
    }
}