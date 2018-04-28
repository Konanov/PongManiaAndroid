package com.pongmania.konanov.activity

import android.app.Fragment
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.pongmania.konanov.PongMania
import com.pongmania.konanov.R
import com.pongmania.konanov.api.GameApi
import com.pongmania.konanov.api.PlayerApi
import com.pongmania.konanov.fragments.PickDateFragment
import com.pongmania.konanov.model.Game
import com.pongmania.konanov.model.Player
import com.pongmania.konanov.util.DataHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

class PlanGameActivity : AppCompatActivity() {

    @Inject lateinit var retrofit: Retrofit
    private lateinit var playerApi: PlayerApi
    private lateinit var gameApi: GameApi

    private lateinit var hostEmail: String
    private lateinit var guestEmail: String

    @BindView(R.id.activityViewPart)
    lateinit var mainLayout: ConstraintLayout

    @OnClick(R.id.pickDate)
    fun pickDate() {
        loadFragment(PickDateFragment())
    }

    @OnClick(R.id.offerGame)
    fun offerGame() {
        gameApi = retrofit.create(GameApi::class.java)
        val gameDate = DataHolder.getGameDate(this.application)
        val gameTime = DataHolder.getGameTime(this.application)
        gameApi.offerGame(
                Game(gameDate = gameDate,
                     gameTime = gameTime,
                     hostEmail = hostEmail,
                     guestEmail = guestEmail))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    gameOffered()
                },  {
                    error -> errorOnUserLoading(error)
                })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_game)
        ButterKnife.bind(this)
        (application as PongMania).webComponent.inject(this)
        playerApi = retrofit.create(PlayerApi::class.java)

        val player = intent.getSerializableExtra("rival") as Player

        hostEmail = DataHolder.getEmail(this.application)
        guestEmail = player.credentials.email

        val hostPlayer = playerApi.getPlayerByEmail(hostEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val guestPlayer = playerApi.getPlayerByEmail(guestEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        hostPlayer.concatWith(guestPlayer)
                .map { contestant -> contestant.credentials.lastName }
                .reduce { host: String, guest: String -> "$host VS $guest" }
                .subscribe({ result -> title = result }, { error -> errorOnUserLoading(error) })
    }

    private fun gameOffered() {
        Toast.makeText(this, "Пользователь $hostEmail уведомлен о предложенной игре",
                Toast.LENGTH_SHORT).show()
    }

    private fun errorOnUserLoading(error: Throwable) {
        Toast.makeText(this, "Ошибка при попытке предложить игру \n ${error.message}",
                Toast.LENGTH_SHORT).show()
    }

    private fun loadFragment(fragment: Fragment) {
        mainLayout.visibility = View.GONE
        fragmentManager.beginTransaction().replace(R.id.planGameActivity, fragment)
                .show(fragment).commit()
    }
}