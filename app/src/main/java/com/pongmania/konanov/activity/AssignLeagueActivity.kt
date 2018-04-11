package com.pongmania.konanov.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TabHost
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.pongmania.konanov.PongMania
import com.pongmania.konanov.R
import com.pongmania.konanov.api.PongManiaApi
import com.pongmania.konanov.model.Player
import com.pongmania.konanov.model.PublicLeague
import com.pongmania.konanov.model.PublicLeagueType
import com.pongmania.konanov.util.CredentialsPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject


class AssignLeagueActivity : AppCompatActivity() {

    @Inject
    lateinit var retrofit: Retrofit

    @BindView(R.id.tabHost)
    lateinit var tabView: TabHost

    @OnClick(R.id.juniorLeagueButton)
    fun handleJun() {
        assignLeague(PublicLeagueType.JUNIOR.value)
    }

    @OnClick(R.id.middleLeagueButton)
    fun handleMid() {
        assignLeague(PublicLeagueType.MIDDLE.value)
    }

    @OnClick(R.id.proLeagueButton)
    fun handlePro() {
        assignLeague(PublicLeagueType.PRO.value)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_league)
        (application as PongMania).webComponent.inject(this)
        ButterKnife.bind(this)

        initialise()
    }

    private fun initialise() {
        title = "Leagues"

        tabView.setup()

        val (tabSpec1: TabHost.TabSpec, tabSpec2: TabHost.TabSpec,
                tabSpec3: TabHost.TabSpec) = initTabSpecs(tabView)

        createTab(R.id.tab1, "Junior", tabSpec1, tabView)
        createTab(R.id.tab2, "Middle", tabSpec2, tabView)
        createTab(R.id.tab3, "Pro", tabSpec3, tabView)

        tabView.currentTab = 0
    }

    private fun assignLeague(type: String) {
        retrofit.create(PongManiaApi::class.java).assignLeague(type,
                Player.Credentials(CredentialsPreference.getEmail(this.application)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    startScoreBoardActivity(result)
                }, {error ->
                    leagueAssigningError(error)
                })
    }

    private fun leagueAssigningError(error: Throwable) {
        Toast.makeText(this, "Попытка вступить в лигу провалилась: "
                + error.message, Toast.LENGTH_SHORT).show()
    }

    private fun startScoreBoardActivity(result: PublicLeague) {
        Toast.makeText(this, result.type.value, Toast.LENGTH_LONG).show()
        intent = Intent(this@AssignLeagueActivity,
                ScoreBoardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        this.finish()
    }

    private fun createTab(tabNumber: Int, indicator: String,
                          tabSpec: TabHost.TabSpec, tabHost: TabHost) {
        tabSpec.setContent(tabNumber)
        tabSpec.setIndicator(indicator)
        tabHost.addTab(tabSpec)
    }

    private fun initTabSpecs(tabHost: TabHost):Triple<TabHost.TabSpec, TabHost.TabSpec, TabHost.TabSpec> {
        val tabSpec1: TabHost.TabSpec = tabHost.newTabSpec("tag1")
        val tabSpec2: TabHost.TabSpec = tabHost.newTabSpec("tag2")
        val tabSpec3: TabHost.TabSpec = tabHost.newTabSpec("tag3")
        return Triple(tabSpec1, tabSpec2, tabSpec3)
    }
}