package com.pongmania.konanov;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface PongManiaApi {

    @GET("player/all")
    Flowable<Player> getPlayers();

}
