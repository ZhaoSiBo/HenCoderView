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
class TagLayout(context: Context?,attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private val childBounds = ArrayList<Rect>()
    //1.调用每个子 View 的 measure() 来计算子 View 的尺寸
    //2.计算子 View 的位置并保存子 View 的位置和尺寸
    //3.计算自己的尺寸并用 setMeasuredDimension() 保存
    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var widthUsed = 0

        var heightUsed = 0

        //单行被占用的宽度
        var lineWidthUsed = 0

        var lineMaxHeight = 0

        //获取当前Layout的Spec（当前Layout的父View给自己的测量规则和尺寸）
        val specMode  = MeasureSpec.getMode(widthMeasureSpec)
        val specWidth  = MeasureSpec.getSize(widthMeasureSpec)

        for (i in 0 until childCount) {
            val  childView = getChildAt(i)
            //测量子View的宽度和高度（withUsed 给0 是位置让空间自由测量，这样就可以得到空间的自由尺寸，用来指导是否需要折行）
            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            //判断折行
            if(lineWidthUsed  + childView.measuredWidth > specWidth){
                lineWidthUsed = 0
                heightUsed += lineMaxHeight
                lineMaxHeight = 0
                measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            }


            //存位置
            lateinit var childBound: Rect;
            if (childBounds.size <= i) {
                childBound = Rect()
                childBounds.add(childBound)
            } else {
                childBound = childBounds[i]
            }
            childBound.set(
                lineWidthUsed,
                heightUsed,
                lineWidthUsed + childView.measuredWidth,
                heightUsed + childView.measuredHeight
            )

            lineWidthUsed += childView.measuredWidth
            widthUsed  += max(widthUsed, lineWidthUsed)
            lineMaxHeight = max(childView.measuredHeight,lineMaxHeight)

        }

        //存自己的测量宽度
        val with = widthUsed
        val height = heightUsed + lineMaxHeight
        setMeasuredDimension(with, height)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childBounds = childBounds[i]
            child.layout(childBounds.left, childBounds.top, childBounds.right, childBounds.bottom)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

}