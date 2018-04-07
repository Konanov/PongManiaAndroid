package com.pongmania.konanov.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.pongmania.konanov.PongMania
import com.pongmania.konanov.R
import com.pongmania.konanov.adapter.PlayerMainAdapter
import com.pongmania.konanov.api.PongManiaApi
import com.pongmania.konanov.model.Player
import com.pongmania.konanov.model.PublicLeague
import com.pongmania.konanov.util.CredentialsPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

class ScoreBoardActivity : AppCompatActivity() {

    private val TAG = "ScoreBoardActivity"

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var api: PongManiaApi

    private lateinit var playersList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_board)
        (application as PongMania).webComponent.inject(this)
        api = retrofit.create(PongManiaApi::class.java)

        initialise()
    }

    private fun initialise() {
        val email = CredentialsPreference.getEmail(this.application)
        api.playersPublicLeague(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({league ->
                    run {
                        findPlayersOfLeague(email, league)
                    }
                }, { error ->
                    run {
                        invalidLeagueType(email, error)
                    }
                })
    }

    private fun ScoreBoardActivity.findPlayersOfLeague(email: String,
                                                       result: PublicLeague): Disposable? {
        Log.d(TAG, "User with email $email has public league ${result.type}")
        return api.playersOfLeague(result.type.value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ players ->
                    run {
                        transformPlayerToViewItem(players)
                    }
                }, { error ->
                    run {
                        playersOfLeagueLoadFailure(error)
                    }
                })
    }

    private fun ScoreBoardActivity.invalidLeagueType(email: String, error: Throwable) {
        Log.d(TAG, "User with email $email has no public league\n"
                + "${error.printStackTrace()}")
        Toast.makeText(this, "Не найдена лига с указанным названием",
                Toast.LENGTH_SHORT).show()
    }

    private fun ScoreBoardActivity.playersOfLeagueLoadFailure(error: Throwable) {
        Log.d(TAG, "Request resulted in error\n${error.printStackTrace()}")
        Toast.makeText(this, "Ошибка при загрузке игроков лиги",
                Toast.LENGTH_SHORT).show()
    }

    private fun ScoreBoardActivity.transformPlayerToViewItem(players: List<Player>) {
        Log.d(TAG, "Users of league received. Total count: ${players.size}")
        playersList = findViewById(R.id.playersList)
        val adapter = PlayerMainAdapter(this, ArrayList(players))
        playersList.adapter = adapter
        playersList.setOnItemClickListener{parent, view, position, id ->
            run {
                val player = players[position]
                intent = Intent(this@ScoreBoardActivity, PlayerProfileActivity::class.java)
                intent.putExtra("currentPlayer", player)
                startActivity(intent)
                //Toast.makeText(this, "Current user: ${players[position].id}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}