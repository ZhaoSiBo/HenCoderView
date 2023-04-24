package com.starts.hencoderview.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.starts.hencoderview.R
import timber.log.Timber

class CustomUITest(context: Context) : CustomLayout(context) {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.pink_300)
        textSize = 32f
    }
    private var measureCount  = 0
    init {
        setWillNotDraw(false)
    }
    private val commonMargin = 12.dp
    private val topImg = TestImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        layoutParams = LayoutParams(matchParent, 280.dp)
        setImageResource(R.mipmap.personal_bg)
        this@CustomUITest.addView(this)
    }
    private val avatar = TestImageView(context).apply {
        layoutParams = LayoutParams(60.dp, 60.dp)
        setImageResource(R.mipmap.avatar)
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, 60f); }
        }
        this@CustomUITest.addView(this)
    }
    private val floatingActionButton = TestFloatActionButton(context).apply {
        layoutParams = LayoutParams(wrapContent, wrapContent)
        this@CustomUITest.addView(this)
    }
    private val title = TestTextView(context).apply {
        layoutParams = LayoutParams(wrapContent, wrapContent)
        text = "UIShare"
        textSize = 18f
        this@CustomUITest.addView(this)
    }
    private val introduce = TestTextView(context).apply {
        layoutParams = LayoutParams(wrapContent, wrapContent)
        text = "在Android界面的展示中，我们常见工作流程是在xml写好布局文件，之后在Activity或者fragment中展示一个UI界面， 画面需要经" +
                "过xml解析，之后window要通过setView方法实际构造各个View对象，并且按照层级排列好，之后Android系统再找到合适" +
                "的时机，触发measure，layout，draw这一流程，但是随着布局嵌套的加深，xml的解析越来越复杂，测绘的次数会逐步提高，逐渐到" +
                "达失控的地步。那么，需要一种方式可以一次性解决这个问题，让我们的布局不再需要多次测量来确定自己的宽高，答案就是完全自己" +
                "接管ViewGroup的onMeasure，onLayout过"
        this@CustomUITest.addView(this)
    }
    private val share = TestImageView(context).apply {
        layoutParams = LayoutParams(24.dp, 24.dp)
        setImageResource(R.mipmap.share)
        this@CustomUITest.addView(this)
    }

    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int): Dimension {
        Timber.tag("onMeasure").d("measureCount  = ${measureCount++} hashcode = ${this.hashCode()}")
        topImg.autoMeasure()
        avatar.autoMeasure()
        floatingActionButton.autoMeasure()
        title.autoMeasure()
        share.autoMeasure()
        val spaceHeight = (MeasureSpec.getSize(heightMeasureSpec) -
                title.measuredHeight -
                topImg.measuredHeight -
                commonMargin * 2).toExactlyMeasureSpec()
        val spaceWidth = (MeasureSpec.getSize(widthMeasureSpec) - avatar.measuredWidth - share.measuredWidth - 4 * commonMargin).toExactlyMeasureSpec()
        introduce.measure(spaceWidth, spaceHeight)
        return Dimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        topImg.layout(l, t)
        avatar.layout(commonMargin, topImg.bottom + commonMargin)
        floatingActionButton.layout(
            commonMargin,
            topImg.bottom - avatar.measuredHeight / 2,
            true
        )
        title.layout(avatar.right + commonMargin, topImg.bottom + commonMargin)
        introduce.layout(avatar.right + commonMargin, title.bottom + commonMargin)
        share.layout(commonMargin, introduce.top, true)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawText("c=$measureCount" , 40f,40f,paint)
    }
}