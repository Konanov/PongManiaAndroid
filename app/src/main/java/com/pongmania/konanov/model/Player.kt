package com.pongmania.konanov.model

data class Player(val id: String,
                  val credentials: Credentials,
                  val latestRating: Rating) {
    private val status: Status? = null
    private val points: Double = 0.toDouble()

    data class Credentials(val email: String,
                           val firstName: String? = null,
                           val lastName: String? = null,
                           val password: String? = null)

    data class Rating(val rating: String,
                 val ratingDeviation: String,
                 val volatility: String)

    private enum class Status {
        ACTIVE, PASSIVE
    }
}
