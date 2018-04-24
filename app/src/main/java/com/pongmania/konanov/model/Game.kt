package com.pongmania.konanov.model

data class Game(private val approved: Boolean? = false,
                private val gameDate: String,
                private val gameTime: String = "18:00",
                private val hostMail: String,
                private val guestMail: String) {
    //private val matches: Collection<Match>? = null
    //private val hostId: ObjectId? = null
    //private val guestId: ObjectId? = null
}