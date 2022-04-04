package com.starts.hencoderview.nest

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import androidx.core.content.ContextCompat
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.customview.widget.ViewDragHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.starts.hencoderview.R
import com.starts.hencoderview.dp2px
import com.starts.hencoderview.view.CustomLayout
import com.starts.hencoderview.view.parentView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**

 *文件描述：.
 *作者：Created by lorizhao on 2022/3/24.
 *版本号：1.0

 */
class NestScrollContainer : CustomLayout , NestedScrollingParent3{
    companion object{
        const val TAG = "NestScrollContainer"
    }
    var peekHeight = dp2px(120)
    //是否是悬浮状态，false 表示跟随状态
    var isFloat = true
        private set


    var currentPeekHeight = 0

    var minScroll = 0
    var maxScroll = 0

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
        topRecyclerView.layout(0,0 )
        floatView.layout(l,b - peekHeight)
        maxScroll = topRecyclerView.measuredHeight + floatView.measuredHeight - height
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {

    }

    override fun onStopNestedScroll(target: View, type: Int) {

    }
    //外面不嵌套更复杂的父布局了，不需要用这个 consumed: IntArray 参数，所以写在下面方法里
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
        Log.d(TAG , "onNestedScroll dyConsumed = ${dyConsumed} ,dyUnconsumed = $dyUnconsumed" )

    }

    val scroller = Scroller(context)

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        Log.d(TAG , "onNestedPreScroll dy = ${dy} ,consumed = ${consumed[1]},scrollY = ${scrollY},bottom = ${topRecyclerView.bottom}" )

        if (target == topRecyclerView){

        } else{

        }
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return if (direction > 0) {
            scrollY < maxScroll
        } else {
            scrollY > minScroll
        }
    }


}

/**
 * 折叠状态，此时只露出最小显示高度
 */
const val BOTTOM_SHEET_STATE_COLLAPSED = 1

/**
 * 正在滚动的状态
 */
const val BOTTOM_SHEET_STATE_SCROLLING = 2

/**
 * 展开状态，此时露出全部内容
 */
const val BOTTOM_SHEET_STATE_EXTENDED = 3

const val BOTTOM_SHEET_STATE_HALF_EXTENDED = 4


class NestChildContainer:CustomLayout,NestedScrollingParent3{
    companion object{
        const val  TAG = "NestChildContainer"
    }

    /**
     * y 轴最小的滚动值，此时 [contentView] 在底部露出 [minShowingHeight]
     */
    private var minScrollY = 0

    /**
     * y 轴最大的滚动值，此时 [contentView] 全部露出
     */
    private var maxScrollY = 0

    /**
     * y 轴的中间值，此时 [contentView] 半露出
     */

    private var midScrollY = 0

    /**
     * 内容视图的状态
     */
    var state = 0
        get() = when (scrollY) {
            minScrollY -> BOTTOM_SHEET_STATE_COLLAPSED
            maxScrollY -> BOTTOM_SHEET_STATE_EXTENDED
            midScrollY -> BOTTOM_SHEET_STATE_HALF_EXTENDED
            else -> BOTTOM_SHEET_STATE_SCROLLING
        }
        private set


    /**
     * 用来处理平滑滚动
     */
    private val scroller = Scroller(context)

    /**
     * 用于计算自身时的 y 轴速度，处理自身的 fling
     */
    private val velocityTracker = VelocityTracker.obtain()


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
        minScrollY = top + dp2px(120) - height
        maxScrollY = bottom - height
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0 && target is RecyclerView
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
        if (canScrollVertically(dy)){
            scrollBy(0 , dy)
            consumed[1] = dy
        }
    }


    /**
     * 滚动范围是[[minScrollY], [maxScrollY]]，根据方向判断垂直方向是否可以滚动
     */
    override fun canScrollVertically(direction: Int): Boolean {
        return if (direction > 0) {
            scrollY < maxScrollY
        } else {
            scrollY >= minScrollY
        }
    }



}