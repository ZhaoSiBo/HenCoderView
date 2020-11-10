package com.starts.hencoderview

import android.content.res.Resources
import android.graphics.Color

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/4/29.
 *版本号：1.0

 */
fun dp2px(dp: Int): Int {
    val density = Resources.getSystem().displayMetrics.density
    return (dp * density).toInt()
}

fun px2dp(px: Int): Int {
    val density = Resources.getSystem().displayMetrics.density
    return (px / density).toInt()
}

fun sp(sp: Int): Float {
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return sp * scaledDensity
}

fun getMaterialColor(resources: Resources , index:Int):Int {
    val colors = resources.obtainTypedArray(R.array.mdcolor_300);

    val  returnColor = colors.getColor(index % colors.length(), Color.BLACK);

    colors.recycle();
    return returnColor
}