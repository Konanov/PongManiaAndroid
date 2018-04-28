package com.pongmania.konanov.api

import com.pongmania.konanov.model.Game
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface GameApi {

    @POST("game/offer")
    fun offerGame(@Body game: Game): Observable<Game>
}