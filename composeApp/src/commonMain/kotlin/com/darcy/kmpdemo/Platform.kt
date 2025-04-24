package com.darcy.kmpdemo

interface Platform {
    val version: String
    val name: String
}

expect fun getPlatform(): Platform

expect fun encryptString(str: String?): String

expect fun decryptString(str: String?): String