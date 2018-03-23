package com.pongmania.konanov.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.pongmania.konanov.PongMania
import com.pongmania.konanov.R
import com.pongmania.konanov.adapter.PlayerMainAdapter
import com.pongmania.konanov.api.PongManiaApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

class ScoreBoardActivity : AppCompatActivity() {

    private val TAG = "ScoreBoardActivity"

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var playersList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.score_board)
        (application as PongMania).webComponent.inject(this)

        initialise()
    }

    private fun initialise() {
        retrofit.create(PongManiaApi::class.java).getPlayers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
            Log.d("Result", "User with id ${result[0].id} received")
            playersList = findViewById(R.id.playersList)
            val adapter = PlayerMainAdapter(this, ArrayList(result))
            playersList.adapter = adapter
        }, { error ->
            Log.d("ERROR", "Request resulted in error\n${error.printStackTrace()}")
            Toast.makeText(this, "Ошибка при загрузке рейтингов",
                    Toast.LENGTH_SHORT).show()
        })
    }
}