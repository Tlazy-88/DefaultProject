package com.greatappmobile.myapplication.defaults

import android.app.Application

class DefaultApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //✨Áp dụng theme
        applyTheme()
    }
}

