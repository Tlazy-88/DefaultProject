package com.greatappmobile.myapplication.defaults

import android.app.Application

class DefaultApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //✨Áp dụng theme trước khi setContentView bất cứ activity nào - khi app bắt đầu
        applyTheme()
    }
}

