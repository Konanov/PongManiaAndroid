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
import com.pongmania.konanov.api.PongManiaApi
import com.pongmania.konanov.fragments.PickDateFragment
import com.pongmania.konanov.model.Player
import com.pongmania.konanov.util.DataHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

class PlanGameActivity : AppCompatActivity() {

    @Inject lateinit var retrofit: Retrofit
    private lateinit var api: PongManiaApi

    private lateinit var hostEmail: String
    private lateinit var guestEmail: String

    private lateinit var gameDate: String
    private lateinit var gameTime: String

    @BindView(R.id.activityViewPart)
    lateinit var mainLayout: ConstraintLayout

    @OnClick(R.id.pickDate)
    fun pickDate() {
        loadFragment(PickDateFragment())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_game)
        ButterKnife.bind(this)
        (application as PongMania).webComponent.inject(this)
        api = retrofit.create(PongManiaApi::class.java)

        val player = intent.getSerializableExtra("rival") as Player

        hostEmail = DataHolder.getEmail(this.application)
        guestEmail = player.credentials.email

        val hostPlayer = api.getPlayerByEmail(hostEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val guestPlayer = api.getPlayerByEmail(guestEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        hostPlayer.concatWith(guestPlayer)
                .map { contestant -> contestant.credentials.lastName }
                .reduce { host: String, guest: String -> "$host VS $guest" }
                .subscribe({ result -> title = result }, { error -> errorOnUserLoading(error) })
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