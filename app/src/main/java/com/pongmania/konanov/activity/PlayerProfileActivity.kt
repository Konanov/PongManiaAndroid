package com.pongmania.konanov.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.pongmania.konanov.R
import com.pongmania.konanov.model.Player
import com.squareup.picasso.Picasso

class PlayerProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_profile)

        val player = intent.getSerializableExtra("currentPlayer") as Player
        val name = "${player.credentials.firstName} ${player.credentials.lastName}"
        title = name

        val ratingValue = findViewById<TextView>(R.id.playerRatingValue)
        ratingValue.text = player.latestRating.rating

        val avatar = findViewById<ImageView>(R.id.profile_image_main)
        val picasso = Picasso.get()
        picasso.isLoggingEnabled = true
        picasso.load(R.drawable.placeholder).into(avatar)
    }
}