package com.pongmania.konanov.dagger.components

import com.pongmania.konanov.activity.ScoreBoardActivity
import com.pongmania.konanov.dagger.modules.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(target: ScoreBoardActivity)
}
