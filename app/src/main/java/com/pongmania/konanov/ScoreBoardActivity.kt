package com.pongmania.konanov

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ScoreBoardActivity : AppCompatActivity() {

    private val TAG = "ScoreBoardActivity"

    val retrofit = Retrofit.Builder().baseUrl("http://localhost:8080/")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initialise()
    }

    private fun initialise() {
        val api = retrofit.create(PongManiaApi::class.java)
        val playersStream = api.getPlayers("Basic dmFuamFAZ21haWwuY29tOmtla3Bhc3N3b3Jkk")
    }

}