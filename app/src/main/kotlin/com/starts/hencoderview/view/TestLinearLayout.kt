package com.starts.hencoderview.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import timber.log.Timber

class TestLinearLayout :LinearLayout {
    var measureCount  = 0
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Timber.tag("onMeasure").d("measureCount  = ${measureCount++} hashcode = ${this.hashCode()}")
    }

}