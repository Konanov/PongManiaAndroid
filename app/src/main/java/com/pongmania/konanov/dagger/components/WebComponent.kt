package com.pongmania.konanov.dagger.components

import com.pongmania.konanov.activity.*
import com.pongmania.konanov.adapter.GamesMainAdapter
import com.pongmania.konanov.dagger.modules.WebModule
import com.pongmania.konanov.fragments.PlannedGamesFragment
import com.pongmania.konanov.fragments.ScoreBoardFragment
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [WebModule::class])
interface WebComponent {
    fun inject(fragment: ScoreBoardFragment)
    fun inject(activity: LoginActivity)
    fun inject(activity: AssignLeagueActivity)
    fun inject(activity: CreateAccountActivity)
    fun inject(activity: PlanGameActivity)
    fun inject(fragment: PlannedGamesFragment)
    fun inject(adapter: GamesMainAdapter)
}