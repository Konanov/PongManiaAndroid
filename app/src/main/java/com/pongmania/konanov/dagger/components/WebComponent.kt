package com.pongmania.konanov.dagger.components

import com.pongmania.konanov.activity.AssignLeagueActivity
import com.pongmania.konanov.activity.CreateAccountActivity
import com.pongmania.konanov.activity.LoginActivity
import com.pongmania.konanov.activity.ScoreBoardActivity
import com.pongmania.konanov.dagger.modules.WebModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [WebModule::class])
interface WebComponent {
    fun inject(activity: ScoreBoardActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: AssignLeagueActivity)
    fun inject(activity: CreateAccountActivity)
}