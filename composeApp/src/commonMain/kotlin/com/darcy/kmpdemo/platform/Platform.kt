package com.darcy.kmpdemo.platform

interface Platform {
    val version: String
    val name: String
}

expect fun getPlatform(): Platform

expect fun isAndroid(): Boolean

expect fun isDesktop(): Boolean

