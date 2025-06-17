package com.darcy.kmpdemo.log

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import com.darcy.kmpdemo.platform.getPlatform
import java.util.logging.Logger

const val DARCY_TAG = "DarcyLog"
fun initLoger() {
    Napier.base(DebugAntilog())
    val platform = getPlatform()
    logD("initLoger $platform")
    logV("initLoger $platform")
    logI("initLoger $platform")
    logW("initLoger $platform")
    logE("initLoger $platform")
}

fun logD(msg: String, tag: String = DARCY_TAG, throwable: Throwable? = null) {
    Napier.d(message = msg, tag = tag, throwable = throwable)
}

fun logI(msg: String, tag: String = DARCY_TAG, throwable: Throwable? = null) {
    Napier.i(message = msg, tag = tag, throwable = throwable)
}

fun logV(msg: String, tag: String = DARCY_TAG, throwable: Throwable? = null) {
    Napier.v(message = msg, tag = tag, throwable = throwable)
}

fun logW(msg: String, tag: String = DARCY_TAG, throwable: Throwable? = null) {
    Napier.w(message = msg, tag = tag, throwable = throwable)
}

fun logE(msg: String, tag: String = DARCY_TAG, throwable: Throwable? = null) {
    Napier.e(message = msg, tag = tag, throwable = throwable)
}