package com.starts.hencoderview.nest

import android.content.Context
import android.util.AttributeSet
import android.util.Log
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

/**

 *文件描述：.
 *作者：Created by lorizhao on 2022/3/24.
 *版本号：1.0

 */
class NestScrollContainer : CustomLayout ,NestedScrollingParent3{
    companion object{
        const val TAG = "NestScrollContainer"
    }
    var maxScroll = 0
    var minScroll = 0
    var peekHeight = dp2px(120)
    //是否是悬浮状态，false 表示跟随状态
    var isFloat = true
    var currentPeekHeight = 0

    lateinit var dragHelper : ViewDragHelper
    val scroller = Scroller(context)

    val topRecyclerView = RecyclerView(context).apply {
        this.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        this@NestScrollContainer.addView(this)
    }
    val floatView =  NestChildContainer(context).apply {
        this.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        this@NestScrollContainer.addView(this)
    }
    private val parentHelper by lazy { NestedScrollingParentHelper(this) }

//    val dragCallback = object : ViewDragHelper.Callback() {
//        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
//            return isFloat
//        }
//
//        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
//            return super.clampViewPositionHorizontal(child, left, dx)
//
//        }
//
//    }

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
//        dragHelper = ViewDragHelper.create(this,dragCallback)
        topRecyclerView.layout(0,0 )
        floatView.layout(l,b - peekHeight)
        currentPeekHeight = peekHeight
        maxScroll = topRecyclerView.measuredHeight
        minScroll = 0
//        isFloat = floatView.top > topRecyclerView.bottom
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

    //dy 代表 target的意图滑动多少， > 0 表示看下面的内容， < 0 表示看上面的内容，consumed 表示消耗的
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        Log.d(TAG , "onNestedPreScroll dy = ${dy} ,consumed = ${consumed[1]},scrollY = ${scrollY},bottom = ${topRecyclerView.bottom}" )
        if ( dy > 0){
            //不能看更多的下面内容
            if(!target.canScrollVertically(dy)){
                isFloat = abs(topRecyclerView.translationY) < abs(currentPeekHeight)
                if(isFloat){
                    topRecyclerView.translationY -=dy
                }else{
                    scrollBy(0,dy)
                }
                consumed[1] = dy
            }
        }else if (dy < 0){
            if(!target.canScrollVertically(dy)){

            }
        }else{
            return
        }
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
    //判断顶部RecyclerView到底，并且完全展示
    fun topViewIsAllShow(){

    }

}