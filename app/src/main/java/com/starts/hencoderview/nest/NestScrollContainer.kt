package com.starts.hencoderview.nest

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.starts.hencoderview.R
import com.starts.hencoderview.dp2px
import com.starts.hencoderview.view.CustomLayout

/**

 *文件描述：.
 *作者：Created by lorizhao on 2022/3/24.
 *版本号：1.0

 */
class NestScrollContainer : CustomLayout ,NestedScrollingParent3{
    companion object{
        const val TAG = "NestScrollContainer"
    }
    val nes = NestedScrollView(context)
    var peekHeight = dp2px(120)

    var isFloat = true

    val topRecyclerView = RecyclerView(context).apply {
        this.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        this@NestScrollContainer.addView(this)
    }
    val floatView =  NestChildContainer(context).apply {
        this.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        this@NestScrollContainer.addView(this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        topRecyclerView.autoMeasure()
        floatView.autoMeasure()
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        topRecyclerView.layout(0,0)
        floatView.layout(l,b - peekHeight)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        val result =  axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0 && target !is ViewPager2
        Log.d(TAG , "onStartNestedScroll result  = ${result}")
        return result
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {

    }

    override fun onStopNestedScroll(target: View, type: Int) {

    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {

    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {

    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {

    }
}

class NestChildContainer:CustomLayout{
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
        this.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        this@NestChildContainer.addView(this)
    }

    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        tabLayout.autoMeasure()
        viewPager2.measure(widthMeasureSpec , (MeasureSpec.getSize(heightMeasureSpec) - tabLayout.measuredHeight).toExactlyMeasureSpec())
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec)
    }
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        tabLayout.layout(0,0)
        viewPager2.layout(0,tabLayout.bottom)
    }
}