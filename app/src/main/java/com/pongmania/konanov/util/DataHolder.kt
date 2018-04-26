package com.pongmania.konanov.util

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager


object DataHolder {

    private const val EMAIL = "com.pongmania.konanov.Email"
    private const val PASSWORD = "com.pongmania.konanov.Password"
    private const val GAME_DATE = "com.pongmania.konanov.GameDate"
    private const val GAME_TIME = "com.pongmania.konanov.GameTime"

    private fun getSharedPreferences(app: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(app)
    }

    fun setCredentials(app: Application, email: String, password: String) {
        val editor = getSharedPreferences(app).edit()
        editor.putString(this.EMAIL, email)
        editor.putString(this.PASSWORD, password)
        editor.apply()
    }

    fun setGameDate(app: Application, date: String) {
        val editor = getSharedPreferences(app).edit()
        editor.putString(GAME_DATE, date)
        editor.apply()
    }
    
    fun setGameTime(app: Application, time: String) {
        val editor = getSharedPreferences(app).edit()
        editor.putString(GAME_TIME, time)
        editor.apply()
    }

    fun getEmail(app: Application): String {
        return getSharedPreferences(app).getString(EMAIL, "")
    }

    fun getPassword(app: Application): String {
        return getSharedPreferences(app).getString(PASSWORD, "")
    }

    fun getGameDate(app: Application): String {
        return getSharedPreferences(app).getString(GAME_DATE, "")
    }

    fun getGameTime(app: Application): String {
        return getSharedPreferences(app).getString(GAME_TIME, "")
    }
}
