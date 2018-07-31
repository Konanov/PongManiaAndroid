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
import com.pongmania.konanov.util.DataHolder

class PickDateFragment : Fragment() {

    @BindView(R.id.plannedGameDate)
    lateinit var calendar: CalendarView

    @OnClick(R.id.back)
    fun backToActivity() {
        hideFragment()
    }

    @OnClick(R.id.confirmDate)
    fun confirmDate() {
        hideFragment()
    }

    private fun hideFragment() {
        fragmentManager.beginTransaction().hide(this).commit()
        this.activity.findViewById<ConstraintLayout>(R.id.activityViewPart).visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_plan_date, container, false)
        ButterKnife.bind(this, view)

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val properMonth: String = if (month < 10) {
                "0${month + 1}"
            } else {
                (month + 1).toString()
            }
            val properDay: String = if (dayOfMonth < 10) {
                "0$dayOfMonth"
            } else {
                (dayOfMonth).toString()
            }
            DataHolder.setGameDate(this.activity.application, "$properDay-$properMonth-$year")
            Toast.makeText(view.context, "Выбрана дата $properDay-$properMonth-$year",
                    Toast.LENGTH_SHORT).show()
        }

        return view
    }
}