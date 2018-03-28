package com.pongmania.konanov.api

import com.pongmania.konanov.model.Player
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PongManiaApi {

    @GET("player/all")
    fun getPlayers(): Observable<List<Player>>

    @GET("allPlayers/league/{email}")
    fun getLeaguePlayers(@Path("email") email : String): Observable<List<Player>>

    @GET("league/players/count/{email}")
    fun countLeaguePlayers(@Path("email") email: String): Observable<Long>

    @POST("registration")
    fun createUser(@Body credentials: Player.Credentials): Observable<String>

    @POST("league/{type}/assign")
    fun assignLeague(@Path("type") type: String, @Body credentials: Player.Credentials): Observable<String>

}
