package com.pongmania.konanov.model

import java.time.LocalDate
import java.time.LocalTime


data class Game(private val approved: Boolean? = false,
                private val gameDate: LocalDate,
                private val gameTime: LocalTime,
                private val hostMail: String,
                private val guestMail: String) {
    //private val matches: Collection<Match>? = null
    //private val hostId: ObjectId? = null
    //private val guestId: ObjectId? = null
}