package com.pongmania.konanov.fragments

import android.app.Fragment
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.pongmania.konanov.R

class PickTimeFragment : Fragment() {

    @BindView(R.id.planGameTime)
    lateinit var timePicker: TimePickerDialog

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_plan_time, container, false)
        ButterKnife.bind(this, view)
        //timePicker.setOn


        return view
    }
}