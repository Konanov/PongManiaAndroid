package com.pongmania.konanov.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.pongmania.konanov.R
import com.pongmania.konanov.model.Player
import com.squareup.picasso.Picasso

class PlayerProfileActivity : AppCompatActivity() {

    @BindView(R.id.playerRatingValue) lateinit var ratingValue: TextView
    @BindView(R.id.profile_image_main) lateinit var avatar: ImageView
    @BindView(R.id.gamesPlayedValue) lateinit var gamesPlayed: TextView
    @BindView(R.id.pingUser) lateinit var pingUser: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_profile)
        ButterKnife.bind(this)

        val player = intent.getSerializableExtra("currentPlayer") as Player
        val name = "${player.credentials.firstName} ${player.credentials.lastName}"
        title = name

        //gamesPlayed.text = player.
        ratingValue.text = player.latestRating.rating

        loadAvatar()

        pingUser.setOnClickListener({
            intent = Intent(this@PlayerProfileActivity, PlanGameActivity::class.java)
            intent.putExtra("rival", player)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        })
    }

    private fun loadAvatar() {
        val picasso = Picasso.get()
        picasso.isLoggingEnabled = true
        picasso.load(R.drawable.placeholder).into(avatar)
    }
}