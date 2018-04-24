package com.pongmania.konanov.activity

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.CalendarView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.pongmania.konanov.PongMania
import com.pongmania.konanov.R
import com.pongmania.konanov.api.PongManiaApi
import com.pongmania.konanov.fragments.PickDateFragment
import com.pongmania.konanov.model.Player
import com.pongmania.konanov.util.CredentialsPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

class PlanGameActivity : AppCompatActivity() {

    @BindView(R.id.plannedGameDate)
    lateinit var calendarView: CalendarView

    @Inject lateinit var retrofit: Retrofit
    private lateinit var api: PongManiaApi

    private lateinit var gameDate: String
    private lateinit var hostEmail: String
    private lateinit var guestEmail: String

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

        hostEmail = CredentialsPreference.getEmail(this.application)
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

        calendarView.setOnDateChangeListener({ _, year, month, dayOfMonth ->
            //refactor this deprecation
            gameDate = "$dayOfMonth/$month/$year"
            Toast.makeText(this, "Выбрана дата $dayOfMonth/$month/$year",
                    Toast.LENGTH_SHORT).show()
        })

    }

    private fun errorOnUserLoading(error: Throwable) {
        Toast.makeText(this, "Ошибка при попытке предложить игру \n ${error.message}",
                Toast.LENGTH_SHORT).show()
    }

    private fun loadFragment(fragment: Fragment) {
        fragmentManager.beginTransaction().replace(R.id.planGameFrame, fragment).commit()
    }
}