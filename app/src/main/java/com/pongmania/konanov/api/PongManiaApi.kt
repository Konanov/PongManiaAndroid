package com.pongmania.konanov.api

import com.pongmania.konanov.model.Player
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PongManiaApi {

    @GET("player/all")
    fun getPlayers(): Flowable<List<Player>>

    @POST("registration")
    fun createUser(@Body credentials: Player.Credentials): Observable<String>

}
