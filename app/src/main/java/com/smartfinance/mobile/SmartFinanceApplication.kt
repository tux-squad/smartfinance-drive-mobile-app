package com.smartfinance.mobile

import android.app.Application
import com.smartfinance.mobile.di.AppContainer

class SmartFinanceApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
