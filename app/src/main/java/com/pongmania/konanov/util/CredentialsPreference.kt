package com.pongmania.konanov.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Base64


object CredentialsPreference {

    internal val CREDENTIALS = "com.pongmania.konanov.Credentials"

    internal fun getSharedPreferences(ctx: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun setUserName(ctx: Context, email: String, password: String) {
        var toEncode = "$email:$password"
        val encoded = Base64.encodeToString(toEncode.toByteArray(), Base64.DEFAULT)
        val editor = getSharedPreferences(ctx).edit()
        editor.putString(CREDENTIALS, "Basic $encoded")
        editor.apply()
    }

    fun getUserName(ctx: Context): String {
        return getSharedPreferences(ctx).getString(CREDENTIALS, "")
    }
}
