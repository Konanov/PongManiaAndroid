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
import com.pongmania.konanov.model.Player
import com.squareup.picasso.Picasso


class PlayerMainAdapter(private val players: ArrayList<Player>) :
        RecyclerView.Adapter<PlayerMainAdapter.PlayerHolder>() {

    @BindView(R.id.profile_image)
    lateinit var avatar: ImageView
    @BindView(R.id.userName_subtitle)
    lateinit var userName: TextView
    @BindView(R.id.rating_title)
    lateinit var ratingTitle: TextView
    @BindView(R.id.rating)
    lateinit var rating: TextView
    @BindView(R.id.player_position)
    lateinit var playerPosition: TextView
    @BindView(R.id.gamesPlayed)
    lateinit var playerGamesPlayed: TextView
    @BindView(R.id.matchWinRatio)
    lateinit var matchWinRatio: TextView

    private lateinit var playerLayout: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        playerLayout = layoutInflater.inflate(R.layout.player_row, parent, false)
        ButterKnife.bind(this, playerLayout)

        return PlayerHolder(playerLayout, players)
    }

    override fun onBindViewHolder(holder: PlayerHolder, position: Int) {
        setPlayerProperties(position)
    }

    private fun setPlayerProperties(position: Int) {
        val player = players[position]
        val playerName = "${player.credentials.firstName} ${player.credentials.lastName}"
        val winRatio = "${player.matchWinRatio.toPlainString()}%"
        val picasso = Picasso.get()
        val leaguePosition = "${position + 1}."
        picasso.isLoggingEnabled = true
        picasso.load(R.drawable.placeholder).into(avatar)
        playerGamesPlayed.text = player.playedGamesCount.toString()
        userName.text = playerName
        ratingTitle.text = playerLayout.context.getString(R.string.rating)
        matchWinRatio.text = winRatio
        rating.text = player.latestRating.rating
        playerPosition.text = leaguePosition
    }

    override fun getItemCount() = players.size

    class PlayerHolder internal constructor(view: View, players: ArrayList<Player>) :
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

    override fun getItemId(position: Int): Long {
        return players[position].credentials.hashCode().toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return players[position].credentials.hashCode()
    }
}