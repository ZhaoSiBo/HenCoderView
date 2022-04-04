package com.starts.hencoderview.behavior

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.Scroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GestureDetectorCompat
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
        return when(ev.action){
            MotionEvent.ACTION_DOWN->{
                lastX = ev.x
                lastY = ev.y
                Log.d(TAG,"scrollY = ${scrollY}")
                false
            }
            MotionEvent.ACTION_MOVE -> {
                if (abs(lastX - ev.x) < abs(lastY - ev.y) && !isIntersect() && canScrollVertically((lastY - ev.y).toInt())) {
                    Log.d(TAG,"[ onInterceptTouchEvent ] true")
                    true
                } else {
                    lastX = ev.x
                    lastY = ev.y
                    false
                }
            }
            else->{
                return super.onInterceptTouchEvent(ev)
            }
        }

    }



    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return when(ev.action){
            MotionEvent.ACTION_DOWN->{
                lastY = ev.y
                velocityTracker.clear()
                velocityTracker.addMovement(ev)
                true
            }
            MotionEvent.ACTION_MOVE->{
                // 移动时分发滚动量
                val dScrollY = (lastY - ev.y).toInt()
//                val child = findChildUnder(e.rawX, e.rawY)
//                dispatchScrollY(dScrollY, child, child?.findScrollableTarget(e.rawX, e.rawY, dScrollY))
//                lastY = e.y
                if (canScrollVertically(dScrollY)){
                    scrollBy(0, dScrollY)
                }
                velocityTracker.addMovement(ev)
                true
            }
            MotionEvent.ACTION_UP->{
                lastX = 0f
                lastY = 0f
                velocityTracker.addMovement(ev)
                velocityTracker.computeCurrentVelocity(1000)
                val yv = -velocityTracker.yVelocity.toInt()
//                scroller.fling(0, 0, 0, yv, 0, 0, Int.MIN_VALUE, Int.MAX_VALUE)
                true
            }
            else ->{
                super.onTouchEvent(ev)
            }
        }
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
        return false
    }

    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Log.d(TAG,"[onScroll] distanceY = ${distanceY},scrollY = ${scrollY}")
        if (canScrollVertically(distanceY.toInt())){
            scrollBy(0,distanceY.toInt())

        }
        return false
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


}