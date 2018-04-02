package com.pongmania.konanov.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.pongmania.konanov.R
import com.pongmania.konanov.model.Player
import com.squareup.picasso.Picasso

class PlayerMainAdapter(private val context: Context,
                        private val dataSource: ArrayList<Player>) : BaseAdapter() {
    private val inflater: LayoutInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.player_row, parent, false)
        // Get title element
        val avatar = rowView.findViewById(R.id.profile_image) as ImageView
        val picasso = Picasso.get()
        picasso.isLoggingEnabled = true
        picasso.load(R.drawable.placeholder).into(avatar)

        val userNameTitle = rowView.findViewById<View>(R.id.userName_title) as TextView
        val userName = rowView.findViewById<View>(R.id.userName_subtitle) as TextView
        // Get subtitle element
        val rating = rowView.findViewById<View>(R.id.rating) as TextView
        val ratingTitle = rowView.findViewById<View>(R.id.rating_title) as TextView

        val player = getItem(position) as Player
        val playerName = "${player.credentials.firstName} ${player.credentials.lastName}"

        userNameTitle.text = context.getString(R.string.user_name_title)
        userName.text = playerName
        ratingTitle.text = context.getString(R.string.rating)
        rating.text = player.latestRating.rating

        return rowView
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return  dataSource.size
    }
}