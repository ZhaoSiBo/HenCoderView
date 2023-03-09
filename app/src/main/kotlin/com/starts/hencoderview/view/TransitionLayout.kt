package com.starts.hencoderview.view

import android.animation.LayoutTransition
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import com.starts.hencoderview.R
import com.starts.hencoderview.container.CustomLayout
import com.starts.hencoderview.container.matchParent
import com.starts.hencoderview.container.wrapContent
import com.starts.hencoderview.util.dp2px
import com.starts.hencoderview.util.getScreenWidth

class TransitionLayout(context: Context) : CustomLayout(context) {
    val button = Button(context).apply {
        layoutParams = LayoutParams(dp2px(120), dp2px(40))
        text ="增加"
        gravity = Gravity.CENTER
    }

    val tv1 = TextView(context).apply {
        layoutParams = LayoutParams(getScreenWidth()/2 , getScreenWidth()/2)
        setBackgroundColor(ContextCompat.getColor(context, R.color.blue_300))
        text = "tv1"
    }

    val tv2 = TextView(context).apply {
        layoutParams = CustomLayout.LayoutParams(getScreenWidth() / 2, getScreenWidth() / 2)
        setBackgroundColor(ContextCompat.getColor(context, R.color.red_300))
        text = "tv2"
    }

    init {
        this@TransitionLayout.addView(button)
        this@TransitionLayout.addView(tv1)

    }

    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int): Dimension {
        forEachAutoMeasure()
        return Dimension(matchParent, matchParent)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        tv1.layout(0,0)
        tv2.layout(0, tv1.right)
    }

}