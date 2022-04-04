package com.starts.hencoderview.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.forEachIndexed
import com.starts.hencoderview.R
import java.lang.ref.WeakReference

/**

 *文件描述：.
 *作者：Created by lorizhao on 2022/3/10.
 *版本号：1.0

 */
class TopContainerBehavior<V : View> : CoordinatorLayout.Behavior<V> {
    companion object {
        val TAG = "TopContainerBehavior"
    }

    var bottomSheetLayoutTop = 0

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
        val result = viewRef?.get() == directTargetChild &&
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
        ViewCompat.offsetTopAndBottom(target, -overage)
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
        val result = super.onLayoutChild(parent, child, layoutDirection)
        viewRef = WeakReference(child)
        bottomSheetBehaviorRef = WeakReference(getBottomSheetBehavior(parent))
        parent.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            val bottom = parent.findViewById<ConstraintLayout>(R.id.layoutBottomSheet)
            bottomSheetLayoutTop = bottom.top
        }
        return result
    }

    private var bottomSheetBehaviorRef: WeakReference<BottomSheetBehavior<*>?>? = null
    private var viewRef: WeakReference<View>? = null

    private fun getBottomSheetBehavior(parent: CoordinatorLayout): BottomSheetBehavior<*>? {
        parent.forEachIndexed { index, view ->
            val lp = view.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = lp.behavior
            if (behavior is BottomSheetBehavior)
                return behavior
        }
        return null
    }


    /**
     * @return 合法的偏移量
     *
     */
    private fun overageOffsetValue(dyUnconsumed: Int): Int {
        val recyclerView = viewRef?.get() ?: return 0
        // dyUnconsumed > 0 手势向上,recyclerView到底了，无法消耗该值
        val isFillingToTop = dyUnconsumed > 0
        if (isFillingToTop) {
            // 如果recyclerView的底部内容没有完全显示，则为true
            val isRecyclerNeedOffsetByDy = recyclerView.bottom - dyUnconsumed > bottomSheetLayoutTop
            return if (isRecyclerNeedOffsetByDy) {
                dyUnconsumed
            } else {
                recyclerView.bottom - bottomSheetLayoutTop
            }
        } else {
            // 如果recyclerView的底部内容没有完全显示，则为true
            val isRecyclerNeedOffsetByDy = recyclerView.top - dyUnconsumed < 0
            return if (isRecyclerNeedOffsetByDy) {
                dyUnconsumed
            } else {
                recyclerView.top
            }
        }
    }

}