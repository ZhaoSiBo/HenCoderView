package com.starts.hencoderview.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.starts.hencoderview.util.dp2px
import com.starts.hencoderview.util.getMaterialColor
import kotlin.math.min

/**

 *文件描述：.
 *作者：Created by LostStars on 2020/11/26.
 *版本号：1.0

 */
class PathView : View {

    private var path = Path()
    private val darkPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val lightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var startPercentage = 20
        set(value) {
            field = value
            calculationPath()
        }

    var endPercentage = 80
        set(value) {
            if(value>startPercentage){
                field = value
                calculationPath()

            }
        }

    val lightPoints = ArrayList<Float>()
    val darkPoints = ArrayList<Float>()

    private val singleDistance = 1

    init {
        darkPaint.style = Paint.Style.STROKE
        darkPaint.strokeWidth = dp2px(4) * 1f
        darkPaint.color = getMaterialColor(resources,3)

        lightPaint.style = Paint.Style.STROKE
        lightPaint.strokeWidth = dp2px(4) * 1f
        lightPaint.color = getMaterialColor(resources,4)

    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN->{
                path.moveTo(event.x , event.y)
                return true
            }
            MotionEvent.ACTION_MOVE->{
                path.lineTo(event.x,event.y)
            }
            MotionEvent.ACTION_UP->{
                calculationPath()
            }
            MotionEvent.ACTION_CANCEL->{

                invalidate()
            }
        }

        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path,darkPaint)
        canvas.drawPoints(lightPoints.toFloatArray(),lightPaint)
    }


    private fun calculationPath(){
        val pathMeasure = PathMeasure(path ,false)
        val pathLength = pathMeasure.length
        val count = (pathLength/singleDistance).toInt()
        var currentDistance = 0f
        for (i in 0 until count){
            currentDistance+=singleDistance
            val point = FloatArray(2)
            pathMeasure.getPosTan(currentDistance,point,null)
            darkPoints.add(point[0])
            darkPoints.add(point[1])
        }
        val start = (darkPoints.size * startPercentage*1f /100).toInt()
        val end = min((darkPoints.size * endPercentage *1f/100).toInt(), darkPoints.size-1)
        lightPoints.clear()
        lightPoints.addAll(darkPoints.subList(start,end))
        invalidate()
    }

}