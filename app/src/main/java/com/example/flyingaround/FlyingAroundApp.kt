package com.example.flyingaround

import android.app.Application
import com.facebook.stetho.Stetho
import org.koin.android.ext.android.startKoin


class FlyingAroundApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule, dbModule, networkModule))
        Stetho.initializeWithDefaults(this)
    }
}