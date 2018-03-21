package com.pongmania.konanov

import android.app.Application
import com.pongmania.konanov.dagger.components.AppComponent
import com.pongmania.konanov.dagger.components.DaggerAppComponent
import com.pongmania.konanov.dagger.components.WebComponent
import com.pongmania.konanov.dagger.modules.AppModule

class PongMania : Application() {

    lateinit var appComponent: AppComponent
    lateinit var webComponent: WebComponent

    private fun initDagger(app: PongMania): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()

    private fun initWebDagger(app: PongMania): WebComponent =
            DaggerWebComponent.builder()
                    .build()


    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)
        webComponent = initWebDagger(this)
    }
}