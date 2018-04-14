package com.pongmania.konanov.api

import com.pongmania.konanov.model.Player
import com.pongmania.konanov.model.PublicLeague
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PongManiaApi {

    @GET("player/all")
    fun getPlayers(): Observable<List<Player>>

    @POST("registration")
    fun createUser(@Body credentials: Player.Credentials): Observable<ResponseBody>

    @GET("player/{email}/has/league")
    fun playerHasLeague(@Path("email") email: String): Observable<Boolean>

    @GET("players/of/{type}/league")
    fun playersOfLeague(@Path("type") type: String): Observable<List<Player>>

    @POST("league/{type}/assign")
    fun assignLeague(@Path("type") type: String,
                     @Body credentials: Player.Credentials): Observable<PublicLeague>

    @GET("player/{email}/public/league")
    fun playersPublicLeague(@Path("email") email: String): Observable<PublicLeague>

    @GET("players/{email}")
    fun getPlayerByEmail(@Path("email") email: String): Observable<Player>
}
