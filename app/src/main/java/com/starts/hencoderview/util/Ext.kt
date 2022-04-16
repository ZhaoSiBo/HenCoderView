package com.starts.hencoderview.util

import android.content.res.Resources
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.starts.hencoderview.R

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/4/29.
 *版本号：1.0

 */
fun dp2px(dp: Int): Int {
    val density = Resources.getSystem().displayMetrics.density
    return (dp * density).toInt()
}

fun sp(sp: Int): Float {
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return sp * scaledDensity
}

fun getMaterialColor(resources: Resources, index: Int): Int {
    val colors = resources.obtainTypedArray(R.array.mdcolor_300);
    val returnColor = colors.getColor(index % colors.length(), Color.BLACK);
    colors.recycle();
    return returnColor
}

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

fun View.isUnder(rawX: Float, rawY: Float): Boolean {
    val xy = IntArray(2)
    getLocationOnScreen(xy)
    return rawX.toInt() in xy[0]..(xy[0] + width) && rawY.toInt() in xy[1]..(xy[1] + height)
}

/**
 * 寻找 ViewGroup 中某个符合条件的子 View，支持递归遍历其子 View
 */
fun ViewGroup.findFirst(recursively: Boolean, predict: (View) -> Boolean): View? {
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
