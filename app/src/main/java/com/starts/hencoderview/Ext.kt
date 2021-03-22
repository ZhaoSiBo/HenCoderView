package com.starts.hencoderview

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import java.lang.IllegalArgumentException

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

fun textHasEllipsized(text: String, tvWidth: Int, textSize: Float, maxLines: Int): Boolean {
    val paint = Paint()
    paint.textSize = textSize
    val size = paint.measureText(text).toInt()
    return size > tvWidth * maxLines
}

val Int.dp:Int get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

fun Int.toExactlyMeasureSpace():Int{
    return View.MeasureSpec.makeMeasureSpec(this,View.MeasureSpec.EXACTLY)
}
fun Int.toAtMostMeasureSpace():Int{
    return View.MeasureSpec.makeMeasureSpec(this,View.MeasureSpec.AT_MOST)
}

fun View.defaultWithMeasureSpace(parentView:ViewGroup):Int{
    return when(layoutParams.width){
        View.MeasureSpec.AT_MOST -> parentView.measuredWidth.toExactlyMeasureSpace()
        View.MeasureSpec.EXACTLY -> parentView.measuredWidth.toExactlyMeasureSpace()
        0->throw IllegalArgumentException("UNSPECIFIED is not support")
        else-> parentView.measuredWidth.toExactlyMeasureSpace()
    }
}
