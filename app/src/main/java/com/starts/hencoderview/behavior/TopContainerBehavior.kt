package com.starts.hencoderview.behavior

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.starts.hencoderview.R

/**

 *文件描述：.
 *作者：Created by lorizhao on 2022/3/10.
 *版本号：1.0

 */
class TopContainerBehavior<V : View> : HeaderBehavior<V> {
    companion object {
        val TAG = "TopContainerBehavior"
    }

    constructor() : super()
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        // Return true if we're nested scrolling vertically, and we either have lift on scroll enabled
        // or we can scroll the children.
        val result = (axes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
        Log.d(TAG, "onStartNestedScroll = $result")
        return result
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
//        Log.d(TAG, "onNestedPreScroll ，child = ${child::class.java.simpleName} , target = ${target::class.java.simpleName},dy = $dy, consumed = $consumed")
//        Log.d(TAG, "onNestedPreScroll ，consumed 0= ${consumed[0]} ,consumed 1 = ${consumed[1]} }")
    }


    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
        scroll(coordinatorLayout, child, dyUnconsumed, getMaxDragOffset(child), 0)
    }

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: V,
        ev: MotionEvent
    ): Boolean {
        val result = super.onInterceptTouchEvent(parent, child, ev)
        Log.d(TAG, "onInterceptTouchEvent = $result")
        return result
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, ev: MotionEvent): Boolean {
        val result = super.onTouchEvent(parent, child, ev)
        Log.d(TAG, "onTouchEvent = $result")
        return result
    }


    override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
        return super.onLayoutChild(parent, child, layoutDirection)
    }
}