package com.pongmania.konanov.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


object CredentialsPreference {

    private const val EMAIL = "com.pongmania.konanov.Email"
    private const val PASSWORD = "com.pongmania.konanov.Password"

    private fun getSharedPreferences(ctx: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun setCredentials(ctx: Context, email: String, password: String) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putString(EMAIL, email)
        editor.putString(PASSWORD, password)
        editor.apply()
    }

    fun getEmail(ctx: Context): String {
        return getSharedPreferences(ctx).getString(EMAIL, "")
    }

    fun getPassword(ctx: Context): String {
        return getSharedPreferences(ctx).getString(PASSWORD, "")
    }
}
