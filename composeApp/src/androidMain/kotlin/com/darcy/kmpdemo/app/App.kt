package com.darcy.kmpdemo.app

import android.app.Application
import android.content.Context
import com.darcy.kmpdemo.log.initLoger
import com.darcy.kmpdemo.log.logD
import com.darcy.kmpdemo.log.logE
import com.darcy.kmpdemo.log.logI
import com.darcy.kmpdemo.log.logV
import com.darcy.kmpdemo.log.logW

class App : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        AppContextProvider.init(this)
        initLoger()
    }
}