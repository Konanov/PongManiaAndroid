package com.pongmania.konanov

import io.reactivex.Flowable

import retrofit2.http.GET
import retrofit2.http.Header

interface PongManiaApi {

    @GET("player/all")
    fun getPlayers(@Header("Authorization") auth: String): Flowable<List<Player>>
}
