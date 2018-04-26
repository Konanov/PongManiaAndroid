package com.pongmania.konanov.util

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager


object DataHolder {

    private const val EMAIL = "com.pongmania.konanov.Email"
    private const val PASSWORD = "com.pongmania.konanov.Password"

    private fun getSharedPreferences(app: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(app)
    }

    fun setCredentials(app: Application, email: String, password: String) {
        val editor = getSharedPreferences(app).edit()
        editor.putString(EMAIL, email)
        editor.putString(PASSWORD, password)
        editor.apply()
    }

    fun getEmail(app: Application): String {
        return getSharedPreferences(app).getString(EMAIL, "")
    }

    fun getPassword(app: Application): String {
        return getSharedPreferences(app).getString(PASSWORD, "")
    }
}
