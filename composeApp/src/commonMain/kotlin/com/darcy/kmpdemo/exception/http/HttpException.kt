package com.darcy.kmpdemo.exception.http

import com.darcy.kmpdemo.exception.BaseException
import io.ktor.http.HttpStatusCode

class HttpException(
    exceptionCode: Int,
    exceptionMessage: String
) : BaseException(exceptionCode, exceptionMessage) {
    constructor(httpStatusCode: HttpStatusCode) : this(
        httpStatusCode.value,
        httpStatusCode.description
    )

    companion object {
        val EXCEPTION_400 = HttpException(HttpStatusCode.BadRequest)
        val EXCEPTION_404 = HttpException(HttpStatusCode.NotFound)
        val EXCEPTION_500 = HttpException(HttpStatusCode.InternalServerError)
    }
}