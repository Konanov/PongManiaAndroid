package com.pongmania.konanov.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.pongmania.konanov.R
import com.pongmania.konanov.api.PlayerApi
import com.pongmania.konanov.model.Game
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject

class GamesMainAdapter(private val games: ArrayList<Game>) : RecyclerView.Adapter<GamesMainAdapter.GameHolder>() {

    @Inject lateinit var retrofit: Retrofit
    private lateinit var playerApi: PlayerApi

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

        playerApi = retrofit.create(PlayerApi::class.java)

        return GamesMainAdapter.GameHolder(gameLayout, games)
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: GamesMainAdapter.GameHolder, position: Int) {
        setGameProperties(position)
    }

    private fun setGameProperties(position: Int) {
        val game = games[position]
        val host = playerApi.getPlayerByEmail(game.hostEmail).subscribe { hostName.text = "${it.credentials.firstName} ${it.credentials.lastName}" }
        val guest = playerApi.getPlayerByEmail(game.guestEmail).subscribe { guestName.text = "${it.credentials.firstName} ${it.credentials.lastName}" }
        //val winRatio = "${player.matchWinRatio.toPlainString()}%"
        //val picasso = Picasso.get()
        //val leaguePosition = "${position + 1}."
        //playerGamesPlayed.text = player.playedGamesCount.toString()
        //userName.text = playerName
        //ratingTitle.text = playerLayout.context.getString(R.string.rating)
        //matchWinRatio.text = winRatio
        //rating.text = player.latestRating.rating
        //playerPosition.text = leaguePosition
    }

    class GameHolder internal constructor(view: View, games: ArrayList<Game>) :
            RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                //val game = games[adapterPosition]
                //val intent = Intent(view.context, PlayerProfileActivity::class.java)
                //intent.putExtra("currentPlayer", player)
                //view.context.startActivity(intent)
            }
        }
    }
}