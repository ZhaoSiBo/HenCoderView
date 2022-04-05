package com.starts.hencoderview.behavior

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.Scroller
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import com.starts.hencoderview.R
import com.starts.hencoderview.link.BottomSheetLayout
import com.starts.hencoderview.util.findChildUnder
import com.starts.hencoderview.util.findScrollableTarget
import kotlin.math.abs

class ScrollCoordinatorLayout : CoordinatorLayout, GestureDetector.OnGestureListener,Runnable {
    companion object{
        const val TAG = "ScrollCoordinatorLayout"
    }
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    val bottomSheetBehavior:BottomSheetBehavior<*> by lazy {
       (( (findViewById<ConstraintLayout>(R.id.layoutBottomSheet).layoutParams) as LayoutParams).behavior)as BottomSheetBehavior
    }
    val topRecyclerBehavior: TopContainerBehavior<*> by lazy {
        (( (findViewById<RecyclerView>(R.id.rvInfo).layoutParams) as LayoutParams).behavior)as TopContainerBehavior
    }

    val topRecyclerView:RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.rvInfo)
    }
    val bottomSheetLayout:ConstraintLayout by lazy {
        findViewById<ConstraintLayout>(R.id.layoutBottomSheet)
    }
    private val velocityTracker = VelocityTracker.obtain()
    val scroller = Scroller(context)
    private val gestureDetector: GestureDetectorCompat by lazy {
        GestureDetectorCompat(context, this)
    }
    var maxScrollY = 0


    override fun canScrollVertically(direction: Int): Boolean {
        return if (direction > 0) {
            scrollY < maxScrollY
        } else {
            scrollY > 0
        }
    }

    override fun onLayoutChild(child: View, layoutDirection: Int) {
        super.onLayoutChild(child, layoutDirection)
        maxScrollY  = height
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
    }


    var lastX = 0f
    var lastY = 0f

    override fun dispatchTouchEvent(e: MotionEvent): Boolean {
        if (e.action == MotionEvent.ACTION_DOWN) {
            // 手指按下就中止 fling 等滑动行为
            scroller.forceFinished(true)
        }
        return super.dispatchTouchEvent(e)
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.d(TAG , "[onInterceptTouchEvent] isIntersect() = ${isIntersect()}")
        return if(!isIntersect()){
            true
        }else{
            super.onInterceptTouchEvent(ev)
        }

    }


    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG, "[onTouchEvent]ev.action=${ev?.action}")
        return gestureDetector.onTouchEvent(ev)
    }

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
//        Log.d(TAG ,"[requestDisallowInterceptTouchEvent]  = $disallowIntercept" )
//        super.requestDisallowInterceptTouchEvent(disallowIntercept)
    }

    private val topRect = Rect()
    private val bottomRect = Rect()

    private fun getTopRect(){
        getChildAt(0).getGlobalVisibleRect(topRect)
    }

    private fun getBottomRect(){
        getChildAt(1).getGlobalVisibleRect(bottomRect)
    }

    private fun isIntersect():Boolean{
        getTopRect()
        getBottomRect()
        return Rect.intersects(topRect,bottomRect)
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return if(!isIntersect()){
            Log.d(TAG,"[onScroll] distanceY = ${distanceY},scrollY = ${scrollY}")
            dispatchScrollY(distanceY.toInt())
            true
        }else{
            false
        }

    }

    override fun onLongPress(e: MotionEvent?) {
    }

    var lastFlingY = 0

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
//        scroller.fling(0, lastFlingY, velocityX.toInt(), velocityY.toInt(), 0, 0, Int.MIN_VALUE, Int.MAX_VALUE)
//        postOnAnimation(this)
        return false
    }

    override fun run() {
        if(scroller.computeScrollOffset()){
            lastFlingY = scroller.currY
            invalidate()
            postOnAnimation(this)
        }
    }

    private fun dispatchScrollY(dScrollY: Int) {
        if (dScrollY == 0) {
            return
        }
        // 滚动所处的位置没有在子 view，或者子 view 没有完全显示出来
        // 或者子 view 中没有要处理滚动的 target，或者 target 不在能够滚动
        if ( !isIntersect()) {
            // 优先自己处理，处理不了再根据滚动方向交给顶部或底部的 view 处理
            when {
                canScrollVertically(dScrollY) -> {
                    bottomSheetBehavior.isDraggable = false
                    scrollBy(0, dScrollY)
                }
//                dScrollY > 0 -> bottomScrollableView?.invoke()?.scrollBy(0, dScrollY)
//                else -> topScrollableView?.invoke()?.scrollBy(0, dScrollY)
            }
        } else {
//            target.scrollBy(0, dScrollY)
        }
    }


}