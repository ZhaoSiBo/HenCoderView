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
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

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

fun TextView.setEllipsizedSuffix(maxLines: Int, suffix: SpannableStringBuilder) {
    addOnLayoutChangeListener(object: View.OnLayoutChangeListener {
        override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom:     Int) {

            val allText = text.toString()
            var newText = allText
            val tvWidth = width
            val textSize = textSize

            if(!textHasEllipsized(newText, tvWidth, textSize, maxLines)) return

            while (textHasEllipsized(newText, tvWidth, textSize, maxLines)) {
                newText = newText.substring(0, newText.length - 1).trim()
            }

            //now replace the last few chars with the suffix if we can
            val endIndex = newText.length - suffix.length - 1 //minus 1 just to make sure we have enough room
            if(endIndex > 0) {
                newText = "${newText.substring(0, endIndex).trim()}$suffix"
            }

            text = newText

            removeOnLayoutChangeListener(this)
        }
    })
}

fun textHasEllipsized(text: String, tvWidth: Int, textSize: Float, maxLines: Int): Boolean {
    val paint = Paint()
    paint.textSize = textSize
    val size = paint.measureText(text).toInt()
    return size > tvWidth * maxLines
}

fun getPicAtEnd(
    context:Context,
    @DrawableRes picRes: Int,
    charSequence: CharSequence,
    picSize: Int
): SpannableString {
    val drawable: Drawable = ContextCompat.getDrawable(context, picRes)!!
    val picWidth = if (picSize < 0) drawable.intrinsicWidth else picSize
    val picHeight = if (picSize < 0) drawable.intrinsicHeight else picSize
    drawable.setBounds(0, 0, picWidth, picHeight)
    val ret = SpannableString(charSequence.toString() + "p")
    val imgSpan = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
    ret.setSpan(
        imgSpan,
        charSequence.length,
        charSequence.length + 1,
        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
    )
    return ret
}