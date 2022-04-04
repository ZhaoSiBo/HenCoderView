package com.starts.hencoderview.behavior

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.starts.hencoderview.dp2px
import kotlin.math.abs

class ScrollCoordinatorLayout : CoordinatorLayout {
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
    // 滚动工具类
    val scroller = Scroller(context)
    // 速度计算工具
    val velocityTracker = VelocityTracker.obtain()

    val viewConfiguration = ViewConfiguration.get(context)

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
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when(ev.action){
            MotionEvent.ACTION_DOWN->{
                lastX = ev.x
                lastY = ev.y
                velocityTracker.clear()
                velocityTracker.addMovement(ev)
            }
            MotionEvent.ACTION_MOVE->{
                Log.d(TAG,"isShouldLindScroll() = ${isShouldLindScroll()} , " +
                        "${ abs(ev.y - lastY) > abs(ev.x - lastX)},${abs(ev.y - lastY) > viewConfiguration.scaledTouchSlop}")
                if(isShouldLindScroll() && abs(ev.y - lastY) > abs(ev.x - lastX) && abs(ev.y - lastY) > viewConfiguration.scaledTouchSlop){
                    return true
                }else{
                    lastX = ev.x
                    lastY = ev.y
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when(ev.action){
            MotionEvent.ACTION_DOWN -> {
                // 手指按下时记录 y 轴初始位置
                lastY = ev.y
                velocityTracker.clear()
                velocityTracker.addMovement(ev)

            }
            MotionEvent.ACTION_MOVE->{
                val scrollY = ev.y - lastY
                scrollBy(0 ,scrollY.toInt())
                lastY = ev.y
                lastX = ev.x
                velocityTracker.addMovement(ev)
            }
            MotionEvent.ACTION_UP->{
                velocityTracker.addMovement(ev)
                velocityTracker.computeCurrentVelocity(1000)
                val yv = -velocityTracker.yVelocity.toInt()
                scroller.fling(0, ev.y.toInt(), 0, yv, 0, 0, Int.MIN_VALUE, Int.MAX_VALUE)
            }

        }
        return super.onTouchEvent(ev)
    }

    private fun isShouldLindScroll():Boolean{
        val topRecyclerView = getChildAt(0)
        val bottomSheetLayout = getChildAt(1)
        return  (topRecyclerView.bottom >= bottomSheetLayout.top)
    }

}