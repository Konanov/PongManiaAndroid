package com.pongmania.konanov.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.CalendarView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.pongmania.konanov.R
import java.util.*

class PlanGameActivity : AppCompatActivity() {

    @BindView(R.id.plannedGameDate) lateinit var calendarView: CalendarView
    lateinit var gameDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_game)
        ButterKnife.bind(this)

        calendarView.setOnDateChangeListener({ _, year, month, dayOfMonth ->
            //refactor this deprecation
            gameDate = Date("$dayOfMonth/$month/$year")
            Toast.makeText(this, "Выбрана дата $dayOfMonth/$month/$year", Toast.LENGTH_SHORT).show()
        })

    }
}