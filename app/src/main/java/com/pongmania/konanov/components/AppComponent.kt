package com.pongmania.konanov.components

import android.app.Application
import com.pongmania.konanov.modules.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {
    fun inject(app: Application)
}
