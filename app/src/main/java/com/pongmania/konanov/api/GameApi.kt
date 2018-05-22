package com.pongmania.konanov.api

import com.pongmania.konanov.model.Game
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GameApi {

    @POST("game/offer")
    fun offerGame(@Body game: Game): Observable<Game>

    @GET("/game/{id}/planned")
    fun userPlannedGames(@Path("id") uuid: String): Observable<List<Game>>
}