package com.pongmania.konanov.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.pongmania.konanov.activity.PlayerProfileActivity
import com.pongmania.konanov.model.Game
import com.pongmania.konanov.model.Player

class GamesMainAdapter(private val players: ArrayList<Game>) : RecyclerView.Adapter<GamesMainAdapter.GameHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesMainAdapter.GameHolder{
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: PlayerMainAdapter.PlayerHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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