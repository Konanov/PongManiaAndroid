package com.pongmania.konanov.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.pongmania.konanov.R
import com.pongmania.konanov.model.Player

class PlayerProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_profile)

        val player = intent.getSerializableExtra("currentPlayer") as Player
        val name = "${player.credentials.firstName} ${player.credentials.lastName}"
        title = name
    }
}