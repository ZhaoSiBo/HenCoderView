package com.starts.hencoderview.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.starts.hencoderview.R

class TestViewLayout : CustomLayout {

    private val topBg = TestTextView(context).apply {
        this.layoutParams = LayoutParams(matchParent, matchParent)
        setBackgroundColor(ContextCompat.getColor(context, R.color.blue_300))
    }

    private val bottomBg = TestTextView(context).apply {
        this.layoutParams = LayoutParams(matchParent, matchParent)
        setBackgroundColor(ContextCompat.getColor(context, R.color.blue_grey_300))
    }

    val leftTv = TestTextView(context).apply {
        this.layoutParams = LayoutParams(matchParent, wrapContent)
        setBackgroundColor(ContextCompat.getColor(context, R.color.red_300))
        setPadding(20.dp)
    }

    private val rightTv = TestTextView(context).apply {
        this.layoutParams = LayoutParams(matchParent, wrapContent)
        setBackgroundColor(ContextCompat.getColor(context, R.color.orange_300))
        setPadding(20.dp)
    }

    init {
        this@TestViewLayout.addView(topBg)
        this@TestViewLayout.addView(bottomBg)
        this@TestViewLayout.addView(leftTv)
        this@TestViewLayout.addView(rightTv)
        setBackgroundColor(Color.WHITE)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int): Dimension {
        topBg.measure(
            widthMeasureSpec,
            ((MeasureSpec.getSize(heightMeasureSpec) - 80.dp) / 2).toExactlyMeasureSpec()
        )
        bottomBg.measure(
            widthMeasureSpec,
            ((MeasureSpec.getSize(heightMeasureSpec) - 80.dp) / 2).toExactlyMeasureSpec()
        )
        leftTv.measure(
            (MeasureSpec.getSize(widthMeasureSpec) / 2).toExactlyMeasureSpec(),
            MeasureSpec.getSize(heightMeasureSpec).toAtMostMeasureSpec()
        )
        rightTv.measure(
            (MeasureSpec.getSize(widthMeasureSpec) / 2).toExactlyMeasureSpec(),
            MeasureSpec.getSize(heightMeasureSpec).toAtMostMeasureSpec()
        )
        return Dimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        topBg.layout(0, 40.dp)
        bottomBg.layout(0, topBg.bottom)
        leftTv.layout(0, topBg.top + topBg.verticalCenterTop(leftTv))
        rightTv.layout(leftTv.right, topBg.top + topBg.verticalCenterTop(rightTv))
    }
}