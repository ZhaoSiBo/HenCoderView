package com.starts.hencoderview.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.starts.hencoderview.R
import kotlin.math.sin

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/4/30.
 *版本号：1.0

 */
class FlipBoardView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private lateinit var bitmap: Bitmap
    lateinit var srcRect: Rect
    private lateinit var center: Point
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val camera = Camera()

    private var radius: Double = 0.00

    var angle: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    var cameraAngle: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    init {

        camera.setLocation(0f, 0f, -6f * context.resources.displayMetrics.density)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        center = Point(w / 2, h / 2)

        bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.fuliimage)
        srcRect = Rect(0, 0, bitmap.width, bitmap.height)
        radius = (bitmap.width) * sin(Math.toRadians(45.0))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        canvas.drawColor(ContextCompat.getColor(context, R.color.colorAccent))
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        canvas.save()
        canvas.translate(center.x.toFloat(), center.y.toFloat())
        canvas.rotate(angle)
        canvas.clipRect(-radius.toFloat(), -radius.toFloat(), radius.toFloat(), 0f)
        canvas.rotate(-angle)
        canvas.translate(-center.x.toFloat(), -center.y.toFloat())
        canvas.drawBitmap(
            bitmap,
            center.x - bitmap.width / 2f,
            center.y - bitmap.height / 2f,
            paint
        )
        canvas.restore()

        canvas.save()
        canvas.translate(center.x.toFloat(), center.y.toFloat())
        canvas.rotate(angle)
        camera.save()
        camera.rotateX(cameraAngle)
        camera.applyToCanvas(canvas)
        camera.restore()
        canvas.clipRect(-radius.toFloat(), 0f, radius.toFloat(), radius.toFloat())
        canvas.rotate(-angle)
        canvas.translate(-center.x.toFloat(), -center.y.toFloat())
        canvas.drawBitmap(
            bitmap,
            center.x - bitmap.width / 2f,
            center.y - bitmap.height / 2f,
            paint
        )
        canvas.restore()
    }

}