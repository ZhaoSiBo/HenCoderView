package com.starts.hencoderview.behavior

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.forEachIndexed
import com.starts.hencoderview.R
import java.lang.ref.WeakReference
import kotlin.math.abs
import kotlin.math.min

/**

 *文件描述：.
 *作者：Created by lorizhao on 2022/3/10.
 *版本号：1.0

 */
class TopContainerBehavior<V : View> : CoordinatorLayout.Behavior<V> {
    companion object {
        val TAG = "TopContainerBehavior"
    }

    var bottomTop = 0
    var offsetRange = 0

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
        val result =  viewRef?.get() == directTargetChild &&
                (axes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
        return result
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
        val overage = overageOffsetValue(dyUnconsumed)
        if (overage > abs(dyUnconsumed)){
            Log.d(TAG ,"overage > abs(dyUnconsumed)" )
            ViewCompat.offsetTopAndBottom(target,-dyUnconsumed)
        }else if(overage < abs(dyUnconsumed)){
            Log.d(TAG ,"overage < abs(dyUnconsumed)" )
            ViewCompat.offsetTopAndBottom(target, -overage)
        }
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
        val result = super.onLayoutChild(parent, child, layoutDirection)
        viewRef = WeakReference(child)
        bottomSheetBehaviorRef = WeakReference(getBottomSheetBehavior(parent))
        offsetRange = bottomSheetBehaviorRef?.get()?.peekHeight?:0
        parent.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            val bottom = parent.findViewById<ConstraintLayout>(R.id.layoutBottomSheet)
            bottomTop = bottom.top
        }
        return result
    }

    private var bottomSheetBehaviorRef: WeakReference<BottomSheetBehavior<*>?>? = null
    private var viewRef: WeakReference<View>? = null

    private fun getBottomSheetBehavior(parent: CoordinatorLayout):BottomSheetBehavior<*>? {
        parent.forEachIndexed { index, view ->
            val lp = view.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = lp.behavior
            if (behavior is BottomSheetBehavior)
                return behavior
        }
        return null
    }

    /**
     * @return 剩余偏移量 ,大于0表示还能偏移，小于0 不能偏移
     *
     */
    private fun overageOffsetValue(dyUnconsumed:Int):Int{
        val topBottom = viewRef?.get()?.bottom?:0
        val topTop = viewRef?.get()?.top?:0
        val result = if(dyUnconsumed > 0){
            topBottom - bottomTop
        }else{
            Log.d(TAG ,"topTop = ${topTop}" )
            0 - topTop
        }
        Log.d(TAG ,"dyUnconsumed = ${dyUnconsumed} overageOffsetValue = ${result}" )
        return result
    }

}