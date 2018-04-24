package com.pongmania.konanov.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import butterknife.BindView
import com.pongmania.konanov.R

class PickDateFragment : Fragment() {

//    @OnClick(R.id.chooseTime)
//    fun chooseTime() {
//        val game = Game(false, gameDate, hostMail = hostEmail, guestMail = guestEmail)
//
//    }

    private lateinit var gameDate: String

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        /*view.findViewById<>(R.layout.plannedGameDate)

        calendarView.setOnDateChangeListener({ _, year, month, dayOfMonth ->
            //refactor this deprecation
            gameDate = "$dayOfMonth/$month/$year"
            //Toast.makeText(this, "Выбрана дата $dayOfMonth/$month/$year",
            //        Toast.LENGTH_SHORT).show()
        })*/

        val view = inflater!!.inflate(R.layout.fragment_plan_date, container, false)
        val calendarView = view.findViewById<CalendarView>(R.id.plannedGameDate)

        calendarView.setOnDateChangeListener({ _, year, month, dayOfMonth ->
            //refactor this deprecation
            gameDate = "$dayOfMonth/$month/$year"
            Toast.makeText(view.context, "Выбрана дата $dayOfMonth/$month/$year",
                    Toast.LENGTH_SHORT).show()
        })

        return view
    }


}