package com.starts.hencoderview.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.starts.hencoderview.dp2px

class ScrollCoordinatorLayout : CoordinatorLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

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

}