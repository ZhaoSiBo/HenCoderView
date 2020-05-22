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

        val widthMeasureSize  = MeasureSpec.getSize(widthMeasureSpec)
        val widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec)

        var widthUsed = 0
        var heightUsed = 0

        var maxHeight = 0
        var lineWidthUsed = 0

        for(i in 0 until childCount ){
            val child = getChildAt(i)
            measureChildWithMargins(child,widthMeasureSpec, 0 , heightMeasureSpec ,heightUsed)
            if(lineWidthUsed + child.measuredWidth > widthMeasureSize){
                heightUsed += maxHeight
                maxHeight = 0
                lineWidthUsed = 0
                measureChildWithMargins(child,widthMeasureSpec, 0 , heightMeasureSpec,heightUsed)
            }


            if(i < childBounds.size){
                val bound = childBounds[i]
                bound.set(lineWidthUsed , heightUsed, lineWidthUsed + child.measuredWidth ,heightUsed + child.measuredHeight)
            }else{
                val bound = Rect()
                bound.set(lineWidthUsed , heightUsed, lineWidthUsed + child.measuredWidth ,heightUsed + child.measuredHeight)
                childBounds.add(bound)
            }

            maxHeight = max(maxHeight , child.measuredHeight)

            lineWidthUsed += child.measuredWidth

            widthUsed = max(lineWidthUsed , widthUsed)
        }
        setMeasuredDimension(widthUsed , heightUsed+ maxHeight)
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