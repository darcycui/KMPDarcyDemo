package com.darcy.kmpdemo.network.parser

import com.darcy.kmpdemo.bean.http.base.BaseResult
import kotlinx.serialization.KSerializer

interface IJsonParser {
    fun <T> toBean(
        json: String,
        kSerializer: KSerializer<T>?,
        success: ((BaseResult<T>?) -> Unit)?,
        successList: ((BaseResult<List<T>>?) -> Unit)?,
        error: ((String) -> Unit)?
    )
}