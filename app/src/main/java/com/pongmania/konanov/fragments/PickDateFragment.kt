package com.pongmania.konanov.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pongmania.konanov.R

class PickDateFragment : Fragment() {

//    @OnClick(R.id.chooseTime)
//    fun chooseTime() {
//        val game = Game(false, gameDate, hostMail = hostEmail, guestMail = guestEmail)
//
//    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_plan_date, container, false)
    }
}