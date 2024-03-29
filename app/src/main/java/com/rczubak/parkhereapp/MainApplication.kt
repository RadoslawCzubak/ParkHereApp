package com.rczubak.parkhereapp

import android.app.Application
import com.rczubak.parkhereapp.di.koinInjector
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(koinInjector)
        }
    }
}