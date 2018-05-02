package com.pongmania.konanov.model

import java.io.Serializable
import java.math.BigDecimal

data class Player(val id: String,
                  val credentials: Credentials,
                  val latestRating: Rating,
                  val publicLeague: PublicLeague): Serializable {
    private val status: Status? = null
    private val points: Double = 0.toDouble()
    val games: Collection<Game> = ArrayList()
    val playedGamesCount: Long = 0
    val plannedGamesCount: Long = 0
    val matchWinRatio: BigDecimal = BigDecimal(0)

    data class Credentials(val email: String,
                           val firstName: String? = null,
                           val lastName: String? = null,
                           val password: String? = null): Serializable

    data class Rating(val rating: String,
                 val ratingDeviation: String,
                 val volatility: String): Serializable

    private enum class Status {
        ACTIVE, PASSIVE
    }
}
