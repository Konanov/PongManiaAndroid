package com.pongmania.konanov.fragments

import android.app.Fragment
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.Gson
import com.pongmania.konanov.R
import com.pongmania.konanov.model.Game

class PickDateFragment : Fragment() {

    @BindView(R.id.plannedGameDate)
    lateinit var calendar: CalendarView

    private lateinit var gameDate: String

    @OnClick(R.id.back)
    fun backToActivity() {
        fragmentManager.beginTransaction().hide(this).commit()
        this.activity.findViewById<ConstraintLayout>(R.id.activityViewPart).visibility = View.VISIBLE
    }

    @OnClick(R.id.confirmDate)
    fun confirmDate() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this.activity)
        if (gameIsPresentIn(preferences)) {
            val game = getPlannedGame(preferences)
        }
    }

    private fun getPlannedGame(preferences: SharedPreferences) =
            Gson().fromJson<Game>(preferences.getString("PlannedGame", ""),
                    Game::class.java)

    private fun gameIsPresentIn(preferences: SharedPreferences) = preferences.contains("PlannedGame")

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater!!.inflate(R.layout.fragment_plan_date, container, false)
        ButterKnife.bind(this, view)

        calendar.setOnDateChangeListener({ _, year, month, dayOfMonth ->
            //refactor this deprecation
            gameDate = "$dayOfMonth/$month/$year"
            Toast.makeText(view.context, "Выбрана дата $dayOfMonth/$month/$year",
                    Toast.LENGTH_SHORT).show()
        })

        return view
    }
}