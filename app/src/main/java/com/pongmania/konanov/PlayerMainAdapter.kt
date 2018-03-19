package com.pongmania.konanov

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class PlayerMainAdapter(private val context: Context,
                        private val dataSource: ArrayList<Player>) : BaseAdapter() {
    private val inflater: LayoutInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.player_row, parent, false)
        // Get title element
        val userName = rowView.findViewById<View>(R.id.userName_subtitle) as TextView
        // Get subtitle element
        val rating = rowView.findViewById<View>(R.id.rating) as TextView

        // Get detail element
        val ratingDeviation = rowView.findViewById<View>(R.id.rating_deviation) as TextView

        val player = getItem(position) as Player

        userName.text = player.credentials.userName
        rating.text = player.latestRating.rating
        ratingDeviation.text = player.latestRating.ratingDeviation

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