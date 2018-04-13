package com.pongmania.konanov.activity

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.graphics.drawable.Drawable
import com.pongmania.konanov.R


class ItemDivider(context: Context) : RecyclerView.ItemDecoration() {
    private val mDivider: Drawable = context.resources.getDrawable(R.drawable.recycler_horizontal_divider)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val additionalPadding = 120
        val left = parent.paddingLeft + additionalPadding
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight

            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }
}