package com.starts.hencoderview.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import kotlin.math.max


/**

 *文件描述：.
 *作者：Created by Administrator on 2020/5/9.
 *版本号：1.0
 */
class TagLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private val childBounds = ArrayList<Rect>()
    //1.调用每个子 View 的 measure() 来计算子 View 的尺寸
    //2.计算子 View 的位置并保存子 View 的位置和尺寸
    //3.计算自己的尺寸并用 setMeasuredDimension() 保存
    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec)

        var height = 0
        var width = 0

        var lineMaxHeight = 0
        var lineWidthUsed = 0
        var lineHeightUsed = 0




        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChildWithMargins(child, 0, lineWidthUsed, heightMeasureSpec, lineHeightUsed)

            if (child.measuredWidth + lineWidthUsed > widthMeasureSize) {
                lineHeightUsed += lineMaxHeight
                lineWidthUsed = 0
                lineMaxHeight = 0
                measureChildWithMargins(child, 0, lineWidthUsed, heightMeasureSpec, lineHeightUsed)
            }

            //保存过程
            if (i < childBounds.size) {
                childBounds[i].set(
                    lineWidthUsed,
                    lineHeightUsed,
                    lineWidthUsed + child.measuredWidth,
                    lineHeightUsed + child.measuredHeight
                )
            } else {
                val childBound = Rect()
                childBound.set(
                    lineWidthUsed,
                    lineHeightUsed,
                    lineWidthUsed + child.measuredWidth,
                    lineHeightUsed + child.measuredHeight
                )
                childBounds.add(childBound)
            }

            lineWidthUsed += child.measuredWidth
            lineMaxHeight = max(lineMaxHeight, child.measuredHeight)
            width = max(lineWidthUsed, width)

        }
        height = lineHeightUsed + lineMaxHeight
        setMeasuredDimension(width, height)

    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val bound = childBounds[i]
            val child = getChildAt(i)
            child.layout(bound.left, bound.top, bound.right, bound.bottom)
        }

    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

}