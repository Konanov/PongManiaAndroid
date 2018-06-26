package com.pongmania.konanov.fragments

import android.app.Application
import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.pongmania.konanov.PongMania
import com.pongmania.konanov.R
import com.pongmania.konanov.activity.ItemDivider
import com.pongmania.konanov.activity.app
import com.pongmania.konanov.adapter.PlayerMainAdapter
import com.pongmania.konanov.api.PlayerApi
import com.pongmania.konanov.model.Player
import com.pongmania.konanov.model.PublicLeague
import com.pongmania.konanov.util.DataHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

class ScoreBoardFragment : Fragment() {

    private val TAG = "ScoreBoardFragment"

    @Inject lateinit var retrofit: Retrofit
    @BindView(R.id.players_list) lateinit var playersList: RecyclerView
    private lateinit var api: PlayerApi
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var playersOfLeague: List<Player>

    private lateinit var app: Application

    @OnClick(R.id.me_button)
    fun findMe() {
        val mail = DataHolder.getEmail(app)
        val player = playersOfLeague.find { it -> it.credentials.email == mail }
        val scrollTo = playersOfLeague.indexOf(player)
        playersList.smoothScrollToPosition(scrollTo)
        Log.d(TAG, "Scroll to player position: $scrollTo")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        app = app(this)
        val view = inflater.inflate(R.layout.fragment_score_board, container, false)
        ButterKnife.bind(this, view)
        (app(this) as PongMania).webComponent.inject(this)
        api = retrofit.create(PlayerApi::class.java)

        initialise()
        return view
    }

    private fun initialise() {
        val email = DataHolder.getEmail(app)
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

    private fun ScoreBoardFragment.findPlayersOfLeague(email: String,
                                                       result: PublicLeague): Disposable? {
        Log.d(TAG, "User with email $email has public league ${result.type}")
        return api.playersOfLeague(result.type.value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ players ->
                    run {
                        playersOfLeague = players
                        val id = userId(players, email)
                        DataHolder.setId(app, id)
                        transformPlayerToViewItem(players)
                    }
                }, { error ->
                    run {
                        playersOfLeagueLoadFailure(error)
                    }
                })
    }

    private fun ScoreBoardFragment.invalidLeagueType(email: String, error: Throwable) {
        Log.d(TAG, "User with email $email has no public league\n"
                + "${error.printStackTrace()}")
        Toast.makeText(app, "Не найдена лига с указанным названием",
                Toast.LENGTH_SHORT).show()
    }

    private fun ScoreBoardFragment.playersOfLeagueLoadFailure(error: Throwable) {
        Log.d(TAG, "Request resulted in error\n${error.printStackTrace()}")
        Toast.makeText(app, "Ошибка при загрузке игроков лиги",
                Toast.LENGTH_SHORT).show()
    }

    private fun ScoreBoardFragment.transformPlayerToViewItem(players: List<Player>) {
        Log.d(TAG, "Users of league received. Total count: ${players.size}")

        viewManager = LinearLayoutManager(app)
        viewAdapter = PlayerMainAdapter(ArrayList(players))

        playersList.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        playersList.addItemDecoration(ItemDivider(app.applicationContext))
    }

    private fun userId(players: List<Player>, email: String) =
            players.filter { it.credentials.email == email }.map { it.id }.first()
}
