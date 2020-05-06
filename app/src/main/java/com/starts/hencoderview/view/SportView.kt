package com.starts.hencoderview.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.starts.hencoderview.dp2px
import com.starts.hencoderview.sp

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/4/29.
 *版本号：1.0

 */
class SportView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val outCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val intCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    lateinit var pointCenter: Point

    val radius = dp2px(100)

    val lineWidth = dp2px(15).toFloat()

    lateinit var rectF: RectF

    var progress = 0
        get() {
            return field
        }
        set(value) {
            field = value
            invalidate()
        }

    private val maxProgress = 100f

    private var bounds: Rect


    init {
        outCirclePaint.color = Color.parseColor("#cccccc")
        intCirclePaint.color = Color.parseColor("#45A4FF")
        textPaint.color = Color.parseColor("#45A4FF")

        outCirclePaint.strokeWidth = lineWidth
        intCirclePaint.strokeWidth = lineWidth

        outCirclePaint.strokeCap = Paint.Cap.ROUND
        intCirclePaint.strokeCap = Paint.Cap.ROUND

        outCirclePaint.style = Paint.Style.STROKE
        intCirclePaint.style = Paint.Style.STROKE

        textPaint.textSize = sp(32)

        bounds = Rect()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        pointCenter = Point(w / 2, h / 2)
        rectF = RectF(
            pointCenter.x - radius.toFloat(),
            pointCenter.y - radius.toFloat(),
            pointCenter.x + radius.toFloat(),
            pointCenter.y + radius.toFloat()
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(
            pointCenter.x.toFloat(),
            pointCenter.y.toFloat(),
            radius.toFloat(),
            outCirclePaint
        )

        val angle = progress / maxProgress * 360
        canvas.drawArc(rectF, 270f, angle, false, intCirclePaint)

        textPaint.getTextBounds("${progress}%", 0, "${progress}%".length, bounds)
        canvas.drawText(
            "${progress}%",
            0,
            "${progress}%".length,
            pointCenter.x - bounds.width() / 2f,
            pointCenter.y + bounds.height() / 2f,
            textPaint
        )

    }


}