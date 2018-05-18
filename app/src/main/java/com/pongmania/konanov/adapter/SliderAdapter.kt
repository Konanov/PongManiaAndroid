package com.pongmania.konanov.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pongmania.konanov.R

class SliderAdapter(private val context: Context) : PagerAdapter() {

    val entries: Array<String> = arrayOf("Рейтинг","Приглашения")
    lateinit var layoutInflater: LayoutInflater
    lateinit var view: View

    override fun isViewFromObject(view: View, item: Any): Boolean {
        return view == item
    }

    override fun getCount(): Int {
        return entries.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = this.context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = layoutInflater.inflate(R.layout.slide_layout, container, false)

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        container.removeView(item as ConstraintLayout)
    }
}

