package com.starts.hencoderview.view

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

fun Int.toExactlyMeasureSpace(): Int {
    return View.MeasureSpec.makeMeasureSpec(this, View.MeasureSpec.EXACTLY)
}

/**
 *  文件描述：.业务中，经常遇到，根据业务的数据值，或有或无，来控制UI的显示隐藏，位置上下，使用自定义ViewGroup来实现这种效果
 *  也为了更好的空安全
 *  作者：Created by lorizhao on 2021/5/14.
 *  版本号：1.0

 */
abstract class BaseViewGroup : ViewGroup {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    protected fun View.autoMeasure() {
        measure(
            this.defaultWithMeasureSpace(this@BaseViewGroup),
            this.defaultHeightMeasureSpace(this@BaseViewGroup)
        )
    }

    protected fun View.layout(x: Int, y: Int, fromRight: Boolean = false) {
        if (!fromRight) {
            layout(x, y, x + this.measuredWidth, y + this.measuredHeight)
        } else {
            layout(this@BaseViewGroup.measuredWidth - x - measuredWidth, y)
        }
    }

}

fun Int.toAtMostMeasureSpace(): Int {
    return View.MeasureSpec.makeMeasureSpec(this, View.MeasureSpec.AT_MOST)
}

fun View.defaultWithMeasureSpace(parentView: ViewGroup): Int {
    return when (layoutParams.width) {
        ViewGroup.LayoutParams.MATCH_PARENT -> (parentView.measuredWidth - parentView.paddingLeft - parentView.paddingRight).toExactlyMeasureSpace()
        ViewGroup.LayoutParams.WRAP_CONTENT -> (parentView.measuredWidth - parentView.paddingLeft - parentView.paddingRight).toAtMostMeasureSpace()
        0 -> throw IllegalAccessException("Need special treatment for $this")
        else -> layoutParams.width.toExactlyMeasureSpace()
    }
}

fun View.defaultHeightMeasureSpace(parentView: ViewGroup): Int {
    return when (layoutParams.height) {
        ViewGroup.LayoutParams.MATCH_PARENT -> (parentView.measuredHeight - parentView.paddingTop - parentView.paddingBottom).toExactlyMeasureSpace()
        ViewGroup.LayoutParams.WRAP_CONTENT -> (parentView.measuredHeight - parentView.paddingTop - parentView.paddingBottom).toAtMostMeasureSpace()
        0 -> throw IllegalAccessException("Need special treatment for $this")
        else -> layoutParams.height.toExactlyMeasureSpace()
    }
}

fun View.setLayoutMarginTop(marginTop: Int) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val lp = layoutParams as ViewGroup.MarginLayoutParams
        lp.topMargin = marginTop
        this.layoutParams = lp
    }
}

fun View.setLayoutMarginLeft(marginLeft: Int) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val lp = layoutParams as ViewGroup.MarginLayoutParams
        lp.leftMargin = marginLeft
        this.layoutParams = lp
    }
}

fun View.setLayoutMarginRight(marginRight: Int) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val lp = layoutParams as ViewGroup.MarginLayoutParams
        lp.rightMargin = marginRight
        this.layoutParams = lp
    }
}

fun View.setLayoutMarginBottom(marginBottom: Int) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val lp = layoutParams as ViewGroup.MarginLayoutParams
        lp.bottomMargin = marginBottom
        this.layoutParams = lp
    }
}

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()