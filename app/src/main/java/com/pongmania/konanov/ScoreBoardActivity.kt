package com.pongmania.konanov

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ScoreBoardActivity : AppCompatActivity() {

    private val TAG = "ScoreBoardActivity"

    private lateinit var playersList: ListView

    val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.score_board)

        initialise()
    }

    private fun initialise() {
        val api = retrofit.create(PongManiaApi::class.java)
        api.getPlayers("Basic a29uYW5vdm1heGltQGdtYWlsLmNvbToxMjM0NTY=")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    result ->
                    Log.d("Result", "User with id ${result[0].id} received")
                    playersList = findViewById(R.id.playersList)
                    val adapter = PlayerMainAdapter(this, ArrayList(result))
                    playersList.adapter = adapter
                }, {
                    error ->
                    error.printStackTrace()
                    Log.d("ERROR", "Request resulted in error")
                })
    }
}