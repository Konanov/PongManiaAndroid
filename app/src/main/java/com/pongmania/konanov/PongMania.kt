package com.pongmania.konanov

import android.app.Application
import com.pongmania.konanov.components.AppComponent
import com.pongmania.konanov.components.DaggerAppComponent
import com.pongmania.konanov.modules.AppModule
import com.pongmania.konanov.modules.WebModule

class PongMania : Application() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    private val webComponent: AppComponent by lazy {
        DaggerWebComponent.builder()
                .webModule(WebModule("http://10.0.2.2:8080/"))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        webComponent.inject(this)
    }
}