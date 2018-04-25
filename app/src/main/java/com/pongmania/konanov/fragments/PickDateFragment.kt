package com.pongmania.konanov.fragments

import android.app.Fragment
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.pongmania.konanov.R

class PickDateFragment : Fragment() {

    @BindView(R.id.plannedGameDate)
    lateinit var calendar: CalendarView

    private lateinit var gameDate: String

    @OnClick(R.id.back)
    fun backToActivity() {
        //fragmentManager.beginTransaction()
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