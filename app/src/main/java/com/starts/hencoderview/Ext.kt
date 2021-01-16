package com.starts.hencoderview

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

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

fun <T>fill(array:Array<in T>, ob:T ) {
    array[0] = ob
}

fun <T>copy(from:Array<out T>,to:Array<in T>){
    check(from.size == to.size)
    for (i in from.indices){
        to[i] == from[i]
    }
}

//fun main(args: Array<String>) {
//    val a = "str"
//    val b = String(StringBuffer("str"))
//    println(a == b )
//    val a = ThreadLocal<Looper>
//
//    val h = Handler()
//}