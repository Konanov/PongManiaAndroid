package com.pongmania.konanov;

import com.konanov.model.person.Player;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PongManiaApi {

    @GET("player/all")
    Flowable<Player> getPlayers(@Header("Authorization") String value);

}
