package com.pongmania.konanov.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.pongmania.konanov.PongMania
import com.pongmania.konanov.R
import com.pongmania.konanov.adapter.PlayerMainAdapter
import com.pongmania.konanov.api.PongManiaApi
import com.pongmania.konanov.model.Player
import com.pongmania.konanov.model.PublicLeague
import com.pongmania.konanov.util.DataHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

class ScoreBoardActivity : AppCompatActivity() {

    private val TAG = "ScoreBoardActivity"

    @Inject lateinit var retrofit: Retrofit
    @BindView(R.id.players_list) lateinit var playersList: RecyclerView
    private lateinit var api: PongManiaApi
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var playersOfLeague: List<Player>

    @OnClick(R.id.me_button)
    fun findMe() {
        val mail = DataHolder.getEmail(this.application)
        val player = playersOfLeague.find { it -> it.credentials.email == mail }
        val scrollTo = playersOfLeague.indexOf(player)
        playersList.smoothScrollToPosition(scrollTo)
        Log.d(TAG, "Scroll to player position: $scrollTo")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_board)
        (application as PongMania).webComponent.inject(this)
        api = retrofit.create(PongManiaApi::class.java)
        ButterKnife.bind(this)

        initialise()
    }

    private fun initialise() {
        val email = DataHolder.getEmail(this.application)
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
                        playersOfLeague = players
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

        viewManager = LinearLayoutManager(this)
        viewAdapter = PlayerMainAdapter(ArrayList(players))

        playersList.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        playersList.addItemDecoration(ItemDivider(this@ScoreBoardActivity))
    }
}