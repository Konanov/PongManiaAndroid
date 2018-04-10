package com.pongmania.konanov.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.pongmania.konanov.R
import com.pongmania.konanov.model.Player
import com.squareup.picasso.Picasso


class PlayerMainAdapter(private val context: Context,
                        private val dataSource: ArrayList<Player>,
                        private val listener: ViewHolderClickListener):
        RecyclerView.Adapter<PlayerMainAdapter.PlayerHolder>() {

    @BindView(R.id.profile_image) lateinit var avatar: ImageView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowView = layoutInflater.inflate(R.layout.player_row, parent, false)
        ButterKnife.bind(this, rowView)

        return PlayerHolder(rowView, listener)
    }

    override fun onBindViewHolder(holder: PlayerHolder, position: Int) {
        val picasso = Picasso.get()
        picasso.isLoggingEnabled = true
        picasso.load(R.drawable.placeholder).into(avatar)

        val player = dataSource[position]
        val playerName = "${player.credentials.firstName} ${player.credentials.lastName}"

        setFieldValues(holder, playerName, player)
    }

    interface ViewHolderClickListener {
        fun onClick(view: View, position: Int)
    }

    class PlayerHolder internal constructor(v: View,
                                            private val mListener: ViewHolderClickListener):
            RecyclerView.ViewHolder(v), View.OnClickListener {
        @BindView(R.id.userName_title) internal lateinit var userNameTitle: TextView
        @BindView(R.id.userName_subtitle) lateinit var userName: TextView
        @BindView(R.id.rating_title) lateinit var ratingTitle: TextView
        @BindView(R.id.rating) lateinit var rating: TextView

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            mListener.onClick(view, adapterPosition)
        }
    }

    private fun setFieldValues(holder: PlayerHolder, playerName: String, player: Player) {
        holder.userNameTitle.text = context.getString(R.string.user_name_title)
        holder.userName.text = playerName
        holder.ratingTitle.text = context.getString(R.string.rating)
        holder.rating.text = player.latestRating.rating
    }

    override fun getItemCount() = dataSource.size
}