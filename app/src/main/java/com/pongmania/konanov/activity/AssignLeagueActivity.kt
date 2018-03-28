package com.pongmania.konanov.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.pongmania.konanov.R
import android.widget.TabHost
import com.pongmania.konanov.PongMania
import com.pongmania.konanov.api.PongManiaApi
import com.pongmania.konanov.model.Player
import com.pongmania.konanov.util.CredentialsPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject


class AssignLeagueActivity : AppCompatActivity() {

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_league)
        (application as PongMania).webComponent.inject(this)

        initialise()
    }

    private fun initialise() {
        title = "TabHost"

        val tabHost = findViewById<TabHost>(R.id.tabHost)

        val juniorButton = findViewById<Button>(R.id.juniorLeagueButton)
        val middleButton = findViewById<Button>(R.id.middleLeagueButton)
        val proButton = findViewById<Button>(R.id.proLeagueButton)

        tabHost.setup()

        juniorButton.setOnClickListener {
            assignLeague("Junior")
        }

        middleButton.setOnClickListener {
            assignLeague("Middle")
        }

        proButton.setOnClickListener {
            assignLeague("Pro")
        }

        val tabSpec: TabHost.TabSpec = tabHost.newTabSpec("tag1")

        createTab(R.id.tab1, "Junior", tabSpec, tabHost)
        createTab(R.id.tab2, "Middle", tabSpec, tabHost)
        createTab(R.id.tab3, "Pro", tabSpec, tabHost)

        tabHost.currentTab = 0
    }

    private fun assignLeague(type: String) {
        retrofit.create(PongManiaApi::class.java).assignLeague(type,
                Player.Credentials(CredentialsPreference.getEmail(this.application)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    run {
                        if (result.isNotEmpty()) {
                            startActivity(Intent(this@AssignLeagueActivity,
                                    ScoreBoardActivity::class.java))
                        }
                    }
                })
    }

    private fun createTab(tabNumber: Int, indicator: String, tabSpec: TabHost.TabSpec, tabHost: TabHost) {
        tabSpec.setContent(tabNumber)
        tabSpec.setIndicator(indicator)
        tabHost.addTab(tabSpec)
    }

}