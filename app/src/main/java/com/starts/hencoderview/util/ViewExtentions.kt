package com.starts.hencoderview.util

import android.graphics.Outline
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.core.view.NestedScrollingChild
import androidx.core.view.ViewCompat
import com.starts.hencoderview.view.BehavioralScrollView

/**
 * [isUnder] 判断 View 是否在某个点下
 * [findFirst] 寻找 ViewGroup 中某个符合条件的子 View，支持递归遍历其子 View
 * [findChildUnder] 寻找 ViewGroup 在某个点下的子 View
 * [findScrollableTarget] 寻找在某个点下，可以处理滚动量的子 View
 * [canNestedScrollVertically] 是否支持垂直方向的嵌套滚动
 * [canNestedScrollHorizontally] 是否支持水平方向的嵌套滚动
 * [findHorizontalNestedScrollingTarget] 寻找 ViewGroup 在某个点下，且支持水平嵌套滚动的子 View
 * [findVerticalNestedScrollingTarget] 寻找 ViewGroup 在某个点下，且支持垂直嵌套滚动的子 View
 * [setRoundRect] 设置圆角矩形进行裁切
 * [setLayoutSize] 设置布局尺寸
 * [stopScroll] 停止滚动
 * [constrains] 限定数字的范围
 *
 * @author https://github.com/funnywolfdadada
 * @since 2020/3/31
 */
fun View.isUnder(rawX: Float, rawY: Float): Boolean {
    val xy = IntArray(2)
    getLocationOnScreen(xy)
    return rawX.toInt() in xy[0]..(xy[0] + width) && rawY.toInt() in xy[1]..(xy[1] + height)
}

/**
 * 寻找 ViewGroup 中某个符合条件的子 View，支持递归遍历其子 View
 */
fun ViewGroup.findFirst(recursively: Boolean, predict: (View)->Boolean): View? {
    for (i in 0 until childCount) {
        val v = getChildAt(i)
        if (predict(v)) {
            return v
        }
        if (recursively) {
            return (v as? ViewGroup)?.findFirst(recursively, predict) ?: continue
        }
    }
    return null
}

/**
 * 寻找 ViewGroup 在某个点下的子 View
 */
fun ViewGroup.findChildUnder(rawX: Float, rawY: Float): View? {
    return findFirst(false) { it.isUnder(rawX, rawY) }
}

/**
 * 寻找在某个点下，可以处理滚动量的子 View
 */
fun View.findScrollableTarget(rawX: Float, rawY: Float, dScrollY: Int): View? {
    return when {
        !isUnder(rawX, rawY) -> null
        canScrollVertically(dScrollY) -> this
        this !is ViewGroup -> null
        else -> {
            var t: View? = null
            for (i in 0 until childCount) {
                t = getChildAt(i).findScrollableTarget(rawX, rawY, dScrollY)
                if (t != null) {
                    break
                }
            }
            t
        }
    }
}

/**
 * 是否支持垂直方向的嵌套滚动
 */
fun View.canNestedScrollVertically(): Boolean = this is NestedScrollingChild
        && (this.canScrollVertically(1) || this.canScrollVertically(-1))

/**
 * 是否支持水平方向的嵌套滚动
 */
fun View.canNestedScrollHorizontally(): Boolean = this is NestedScrollingChild
        && (this.canScrollHorizontally(1) || this.canScrollHorizontally(-1))

/**
 * 寻找 ViewGroup 在某个点下，且支持水平嵌套滚动的子 View
 */
fun ViewGroup.findHorizontalNestedScrollingTarget(rawX: Float, rawY: Float): View? {
    for (i in 0 until childCount) {
        val v = getChildAt(i)
        if (!v.isUnder(rawX, rawY)) {
            continue
        }
        if (v.canNestedScrollHorizontally()) {
            return v
        }
        if (v !is ViewGroup) {
            continue
        }
        val t = v.findHorizontalNestedScrollingTarget(rawX, rawY)
        if (t != null) {
            return t
        }
    }
    return null
}

/**
 * 寻找 ViewGroup 在某个点下，且支持垂直嵌套滚动的子 View
 */
fun ViewGroup.findVerticalNestedScrollingTarget(rawX: Float, rawY: Float): View? {
    for (i in 0 until childCount) {
        val v = getChildAt(i)
        if (!v.isUnder(rawX, rawY)) {
            continue
        }
        if (v.canNestedScrollVertically()) {
            return v
        }
        if (v !is ViewGroup) {
            continue
        }
        val t = v.findVerticalNestedScrollingTarget(rawX, rawY)
        if (t != null) {
            return t
        }
    }
    return null
}

/**
 * 设置圆角矩形进行裁切
 */
fun View.setRoundRect(radius: Float) {
    clipToOutline = true
    outlineProvider = object: ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius)
        }
    }
}

/**
 * 设置布局尺寸
 */
fun View.setLayoutSize(layoutWidth: Int, layoutHeight: Int) {
    layoutParams?.width = layoutWidth
    layoutParams?.height = layoutHeight
    requestLayout()
}

/**
 * 停止滚动
 */
fun View.stopScroll() {
    val e = MotionEvent.obtain(
            SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
            MotionEvent.ACTION_DOWN, (left + right) / 2F, (top + bottom) / 2F, 0
    )
    dispatchTouchEvent(e)
    e.action = MotionEvent.ACTION_CANCEL
    dispatchTouchEvent(e)
    e.recycle()
}

/**
 * 限定数字的范围
 */
fun <T: Number> T.constrains(min: T, max: T): T = when {
    this.toDouble() < min.toDouble() -> min
    this.toDouble() > max.toDouble() -> max
    else -> this
}

/**
 * 这里放一些只访问公共属性/方法的扩展方法
 *
 * @author https://github.com/funnywolfdadada
 * @since 2020/10/24
 */
/**
 * 当前滚动的百分比
 */
fun BehavioralScrollView.currProcess(): Float {
    return when(nestedScrollAxes) {
        ViewCompat.SCROLL_AXIS_HORIZONTAL -> if (scrollX > 0) {
            if (maxScroll != 0) { scrollX.toFloat() / maxScroll } else { 0F }
        } else {
            if (minScroll != 0) { scrollX.toFloat() / minScroll } else { 0F }
        }
        ViewCompat.SCROLL_AXIS_VERTICAL -> if (scrollY > 0) {
            if (maxScroll != 0) { scrollY.toFloat() / maxScroll } else { 0F }
        } else {
            if (minScroll != 0) { scrollY.toFloat() / minScroll } else { 0F }
        }
        else -> 0F
    }
}

/**
 * 是否发生了滚动
 */
fun BehavioralScrollView.isScrolled(): Boolean = when (nestedScrollAxes) {
    ViewCompat.SCROLL_AXIS_VERTICAL -> scrollY != 0
    ViewCompat.SCROLL_AXIS_HORIZONTAL -> scrollX != 0
    else -> false
}

/**
 * 当前滚动位置在 0 或者最大最小值，则认为是稳定位置
 */
fun BehavioralScrollView.inStablePosition(): Boolean {
    val scroll = when (nestedScrollAxes) {
        ViewCompat.SCROLL_AXIS_VERTICAL -> scrollY
        ViewCompat.SCROLL_AXIS_HORIZONTAL -> scrollX
        else -> return true
    }
    return scroll == 0 || scroll == minScroll || scroll == maxScroll
}

/**
 * 发生嵌套滚动的直接子 view 是否完全展示出来
 */
fun BehavioralScrollView.isScrollChildTotalShowing(): Boolean {
    val v = nestedScrollChild ?: return true
    return when (nestedScrollAxes) {
        ViewCompat.SCROLL_AXIS_VERTICAL -> v.y - scrollY >= 0 && v.y + v.height - scrollY <= height
        ViewCompat.SCROLL_AXIS_HORIZONTAL -> v.x - scrollX >= 0 && v.x + v.width - scrollX <= width
        else -> return true
    }
}

