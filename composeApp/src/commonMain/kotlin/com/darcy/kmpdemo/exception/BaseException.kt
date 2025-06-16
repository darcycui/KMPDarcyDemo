package com.darcy.kmpdemo.exception

open class BaseException (
    exceptionCode: Int,
    exceptionMessage: String
) : IllegalStateException("exceptionCode=$exceptionCode exceptionMessage=$exceptionMessage") {
}