package com.pongmania.konanov.model

data class Player(val id: String,
                  val credentials: Credentials1,
                  val latestRating: Rating) {
    private val status: Status? = null
    private val points: Double = 0.toDouble()

    data class Credentials1(val email: String,
                      val firstName: String,
                      val lastName: String,
                      val password: String)

    data class Rating(val rating: String,
                 val ratingDeviation: String,
                 val volatility: String)

    private enum class Status {
        ACTIVE, PASSIVE
    }
}
