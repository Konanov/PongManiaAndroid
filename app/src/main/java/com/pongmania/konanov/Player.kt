package com.pongmania.konanov

data class Player(val id: String,
                  val credentials: Credentials,
                  val latestRating: Rating) {
    private val status: Status? = null
    private val points: Double = 0.toDouble()

    class Credentials {
        val email: String? = null
        val userName: String? = null
        val password: String? = null
    }

    class Rating {
        val rating: String? = null
        val ratingDeviation: String? = null
        val volatility: String? = null
    }

    private enum class Status {
        ACTIVE, PASSIVE
    }
}
