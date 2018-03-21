package com.pongmania.konanov.components

import com.pongmania.konanov.activity.CreateAccountActivity
import com.pongmania.konanov.activity.ScoreBoardActivity
import com.pongmania.konanov.modules.WebModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [WebModule::class])
public interface WebComponent {
    fun inject(activity: CreateAccountActivity)
    fun inject(activity: ScoreBoardActivity)
}