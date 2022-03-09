package com.google.android.material.bottomsheet

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import java.lang.ref.WeakReference

class ViewPagerBottomSheetBehavior<V : View>(context: Context, attrs: AttributeSet?) : BottomSheetBehavior<V>(context, attrs) {

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        nestedScrollingChildRef = WeakReference(target)
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
    }
}