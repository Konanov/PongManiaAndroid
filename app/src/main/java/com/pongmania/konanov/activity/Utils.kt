package com.pongmania.konanov.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import java.io.Serializable

/**
 * Starts new activity finishing previous one.
 */
fun <T : AppCompatActivity> AppCompatActivity.goFor(context: Context, t: Class<T>) {
    val intent = Intent(context, t)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
    this.finish()
}

/**
 * Starts new activity finishing previous one and passing Extra object.
 */
fun <T : AppCompatActivity> AppCompatActivity.goFor(context: Context, t: Class<T>,
                                                    reference: String, extra: Serializable?) {
    val intent = Intent(context, t)
    intent.putExtra(reference, extra)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}