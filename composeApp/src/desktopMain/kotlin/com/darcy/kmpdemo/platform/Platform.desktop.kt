package com.darcy.kmpdemo.platform

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val version: String = "1.0.0"

    override fun toString(): String {
        return "JVMPlatform(name='$name', version='$version')"
    }

}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun isAndroid(): Boolean = false

actual fun isDesktop(): Boolean = true
