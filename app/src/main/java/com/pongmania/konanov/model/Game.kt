package com.pongmania.konanov.model

data class Game(val approved: Boolean? = false,
                val gameDate: String,
                val gameTime: String = "18:00",
                val hostEmail: String,
                val guestEmail: String) {
    //private val matches: Collection<Match>? = null
    //private val hostId: ObjectId? = null
    //private val guestId: ObjectId? = null
}