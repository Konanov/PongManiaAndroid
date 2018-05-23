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
import com.pongmania.konanov.PongMania
import com.pongmania.konanov.R
import com.pongmania.konanov.activity.ItemDivider
import com.pongmania.konanov.activity.app
import com.pongmania.konanov.adapter.GamesMainAdapter
import com.pongmania.konanov.api.GameApi
import com.pongmania.konanov.api.PlayerApi
import com.pongmania.konanov.model.Game
import com.pongmania.konanov.util.DataHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

class PlannedGamesFragment : Fragment() {

    private val TAG = "PlannedGamesFragment"

    @Inject lateinit var retrofit: Retrofit
    @BindView(R.id.games_list) lateinit var gamesList: RecyclerView
    private lateinit var gameApi: GameApi
    private lateinit var playerApi: PlayerApi
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var plannedGames: List<Game>

    private lateinit var app: Application

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        app = app(this)
        //val view = inflater.inflate(R.layout.fragment_planned_games, container, false) todo real fragment
        ButterKnife.bind(this, view)
        (app(this) as PongMania).webComponent.inject(this)
        gameApi = retrofit.create(GameApi::class.java)
        playerApi = retrofit.create(PlayerApi::class.java)

        initialise()
        return view
    }

    private fun initialise() {
        val id = DataHolder.getId(app)
        gameApi.userPlannedGames(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ games ->
                    run {
                        transformGamesToViewItems(games)
                    }
                }, { error ->
                    run {
                        invalidGamesList(error)
                    }
                })
    }

    private fun PlannedGamesFragment.transformGamesToViewItems(plannedGames: List<Game>) {
        Log.d(TAG, "Users of league received. Total count: ${plannedGames.size}")

        viewManager = LinearLayoutManager(app)
        viewAdapter = GamesMainAdapter(ArrayList(plannedGames))

        gamesList.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        gamesList.addItemDecoration(ItemDivider(app.applicationContext))
    }

    private fun PlannedGamesFragment.invalidGamesList(error: Throwable) {
        Log.d(TAG, "Request resulted in error\n${error.printStackTrace()}")
        Toast.makeText(app, "Ошибка при загрузке игр пользователя",
                Toast.LENGTH_SHORT).show()
    }
}