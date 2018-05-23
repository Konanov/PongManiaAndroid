package com.pongmania.konanov.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.pongmania.konanov.R
import com.pongmania.konanov.activity.PlayerProfileActivity
import com.pongmania.konanov.model.Game
import com.squareup.picasso.Picasso

class GamesMainAdapter(private val games: ArrayList<Game>) : RecyclerView.Adapter<GamesMainAdapter.GameHolder>() {

    @BindView(R.id.profile_image)
    lateinit var avatar: ImageView
    @BindView(R.id.guestName)
    lateinit var guestName: TextView
    @BindView(R.id.hostName)
    lateinit var hostName: TextView
    @BindView(R.id.guestRating)
    lateinit var guestRating: TextView
    @BindView(R.id.hostRating)
    lateinit var hostRating: TextView
    @BindView(R.id.guestWinRatio)
    lateinit var guestWinRatio: TextView
    @BindView(R.id.hostWinRatio)
    lateinit var hostWinRatio: TextView
    @BindView(R.id.date)
    lateinit var date: TextView
    @BindView(R.id.time)
    lateinit var time: TextView

    private lateinit var gameLayout: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesMainAdapter.GameHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        gameLayout = layoutInflater.inflate(R.layout.game_row, parent, false)
        ButterKnife.bind(this, gameLayout)

        return GamesMainAdapter.GameHolder(gameLayout, players)
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: GamesMainAdapter.GameHolder, position: Int) {
        setGameProperties(position)
    }

    private fun setGameProperties(position: Int) {
        val game = games[position]
        val hostName = game.hostName
        val guestName = game.hostName
        val winRatio = "${player.matchWinRatio.toPlainString()}%"
        val picasso = Picasso.get()
        val leaguePosition = "${position + 1}."
        playerGamesPlayed.text = player.playedGamesCount.toString()
        userName.text = playerName
        ratingTitle.text = playerLayout.context.getString(R.string.rating)
        matchWinRatio.text = winRatio
        rating.text = player.latestRating.rating
        playerPosition.text = leaguePosition
    }

    class GameHolder internal constructor(view: View, players: ArrayList<Game>) :
            RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener({
                val player = players[adapterPosition]
                val intent = Intent(view.context, PlayerProfileActivity::class.java)
                intent.putExtra("currentPlayer", player)
                view.context.startActivity(intent)
            })
        }
    }
}