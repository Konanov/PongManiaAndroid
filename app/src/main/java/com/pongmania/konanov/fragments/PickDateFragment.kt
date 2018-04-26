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
import com.pongmania.konanov.util.DataHolder

class PickDateFragment : Fragment() {

    @BindView(R.id.plannedGameDate)
    lateinit var calendar: CalendarView

    private lateinit var gameDate: String

    @OnClick(R.id.back)
    fun backToActivity() {
        hideFragment()
    }

    @OnClick(R.id.confirmDate)
    fun confirmDate() {
        val gameDate = DataHolder.getGameDate(this.activity.application)
        if (gameDate.isEmpty()) {
            DataHolder.setGameDate(this.activity.application, gameDate)
            hideFragment()
        }
    }

    private fun hideFragment() {
        fragmentManager.beginTransaction().hide(this).commit()
        this.activity.findViewById<ConstraintLayout>(R.id.activityViewPart).visibility = View.VISIBLE
    }

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