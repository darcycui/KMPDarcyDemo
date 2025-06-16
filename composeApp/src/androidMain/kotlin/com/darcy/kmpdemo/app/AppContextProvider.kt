package com.darcy.kmpdemo.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

// 在 Application 类中初始化
@SuppressLint("StaticFieldLeak")
object AppContextProvider {
    private lateinit var context: Context

    fun getAppContext(): Context {
        if (!::context.isInitialized) {
            throw IllegalStateException("Context is not initialized")
        }
        return context
    }

    fun init(application: Application) {
        context = application.applicationContext
    }
}