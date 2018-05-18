package com.pongmania.konanov.activity

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.pongmania.konanov.R
import com.pongmania.konanov.adapter.SliderAdapter

public class MainActivity : AppCompatActivity() {


    private lateinit var viewPager: ViewPager
    private val slideAdapter: SliderAdapter = SliderAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.slide_layout)

        viewPager = findViewById(R.id.slideViewPager)
        viewPager.adapter = slideAdapter
    }
}