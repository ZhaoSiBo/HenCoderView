package com.starts.hencoderview.container

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.RECTANGLE
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.starts.hencoderview.R

/**
 * 底部弹出式的 view 容器
 * @author lorizhao
 * @since 2022/3/22
 */

class BottomSheetLayout : CustomLayout {
    companion object{
        const val TAG = "BottomSheetLayout"
    }

    /**
     * 拖拽的时候能展示的小白条
     * 不要用Gone ,引起重测，导致性能浪费，也会导致TabLayout位置升高
     */
    val dragTip = View(context).apply {
        layoutParams = LayoutParams(40.dp , 4.dp)
        this@BottomSheetLayout.addView(this)
        this.background = GradientDrawable().apply {
            setColor(ContextCompat.getColor(context, R.color.cardview_dark_background))
            shape = RECTANGLE
            val floatArray = floatArrayOf(20.dp * 1f,20.dp * 1f,20.dp * 1f,20.dp * 1f,20.dp * 1f,20.dp * 1f,20.dp * 1f,20.dp * 1f)
            cornerRadii = floatArray
        }
    }

    val bottomTabLayout  = TabLayout(context).apply {
        layoutParams = LayoutParams(matchParent , 45.dp)
        this@BottomSheetLayout.addView(this)
    }
    val bottomViewPager = ViewPager2(context).apply {
        layoutParams = LayoutParams(matchParent , matchParent)
        overScrollMode = OVER_SCROLL_NEVER
        this@BottomSheetLayout.addView(this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    )

    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int):Dimension {
        dragTip.autoMeasure()
        bottomTabLayout.autoMeasure()
        bottomViewPager.measure(widthMeasureSpec,heightMeasureSpec - dragTip.measuredHeight -  bottomTabLayout.measuredHeight)
        return Dimension(widthMeasureSpec,heightMeasureSpec)
    }

    /**
     * 布局TabLayout 和 ViewPager，还有拖拽的时候能展示的小白条
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        dragTip.layout(horizontalCenterX(dragTip) , 8.dp)
        bottomTabLayout.layout(0,dragTip.bottom + 20.dp)
        bottomViewPager.layout(0,dragTip.bottom + 20.dp)
    }

}