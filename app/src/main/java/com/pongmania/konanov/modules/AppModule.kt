package com.pongmania.konanov.modules

import android.app.Application
import dagger.Module
import javax.inject.Singleton
import dagger.Provides


@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideApplication() = app
}