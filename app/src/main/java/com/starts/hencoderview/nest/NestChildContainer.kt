package com.starts.hencoderview.nest

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import androidx.core.content.ContextCompat
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.starts.hencoderview.R
import com.starts.hencoderview.dp2px
import com.starts.hencoderview.view.CustomLayout

/**
 * 底部可以悬浮的 ViewGroup
 */
class NestChildContainer : CustomLayout {
    
    companion object{
        const val TAG = "NestChildContainer"
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    val tabLayout = TabLayout(context).apply {
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.red_300))
        this.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(45))
        this@NestChildContainer.addView(this)
    }
    val viewPager2 = ViewPager2(context).apply {
        this.layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        this@NestChildContainer.addView(this)
    }

    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        tabLayout.autoMeasure()
        viewPager2.measure(
            widthMeasureSpec,
            (MeasureSpec.getSize(heightMeasureSpec) - tabLayout.measuredHeight).toExactlyMeasureSpec()
        )
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        tabLayout.layout(0, 0)
        viewPager2.layout(0, tabLayout.bottom)
    }
}