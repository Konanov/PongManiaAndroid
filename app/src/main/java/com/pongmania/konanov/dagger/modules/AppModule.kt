package com.pongmania.konanov.dagger.modules

import android.app.Application
import android.content.Context
import dagger.Module
import javax.inject.Singleton
import dagger.Provides

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideContext() : Context = app
}