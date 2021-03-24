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

val Int.dp:Int get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

fun getScreenWidth():Int{
    return Resources.getSystem().displayMetrics.widthPixels
}


fun Int.toExactlyMeasureSpace():Int{
    return View.MeasureSpec.makeMeasureSpec(this,View.MeasureSpec.EXACTLY)
}
fun Int.toAtMostMeasureSpace():Int{
    return View.MeasureSpec.makeMeasureSpec(this,View.MeasureSpec.AT_MOST)
}

fun View.defaultWithMeasureSpace(parentView:ViewGroup):Int{
    return when(layoutParams.width){
        ViewGroup.LayoutParams.MATCH_PARENT -> (parentView.measuredWidth - parentView.paddingLeft - parentView.paddingRight).toExactlyMeasureSpace()
        ViewGroup.LayoutParams.WRAP_CONTENT -> (parentView.measuredWidth - parentView.paddingLeft - parentView.paddingRight).toAtMostMeasureSpace()
        0->throw IllegalAccessException("Need special treatment for $this")
        else-> layoutParams.width.toExactlyMeasureSpace()
    }
}

fun View.defaultHeightMeasureSpace(parentView:ViewGroup):Int{
    return when(layoutParams.height){
        ViewGroup.LayoutParams.MATCH_PARENT ->(parentView.measuredHeight - parentView.paddingTop - parentView.paddingBottom).toExactlyMeasureSpace()
        ViewGroup.LayoutParams.WRAP_CONTENT -> (parentView.measuredHeight - parentView.paddingTop - parentView.paddingBottom).toAtMostMeasureSpace()
        0->throw IllegalAccessException("Need special treatment for $this")
        else-> layoutParams.height.toExactlyMeasureSpace()
    }
}

fun View.setVisibility(isVisibility:Boolean){
    if(isVisibility){
        this.visibility  = View.VISIBLE
    }else{
        this.visibility = View.GONE
    }
}
fun View.setLayoutMarginTop(marginTop:Int){
    if (this.layoutParams is ViewGroup.MarginLayoutParams){
        val lp = layoutParams as ViewGroup.MarginLayoutParams
        lp.topMargin = marginTop
        this.layoutParams = lp
    }
}
fun View.setLayoutMarginLeft(marginLeft:Int){
    if (this.layoutParams is ViewGroup.MarginLayoutParams){
        val lp = layoutParams as ViewGroup.MarginLayoutParams
        lp.leftMargin = marginLeft
        this.layoutParams = lp
    }
}
fun View.setLayoutMarginRight(marginRight:Int){
    if (this.layoutParams is ViewGroup.MarginLayoutParams){
        val lp = layoutParams as ViewGroup.MarginLayoutParams
        lp.rightMargin = marginRight
        this.layoutParams = lp
    }
}
fun View.setLayoutMarginBottom(marginBottom:Int){
    if (this.layoutParams is ViewGroup.MarginLayoutParams){
        val lp = layoutParams as ViewGroup.MarginLayoutParams
        lp.bottomMargin = marginBottom
        this.layoutParams = lp
    }
}