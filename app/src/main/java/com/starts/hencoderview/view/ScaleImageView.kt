package com.starts.hencoderview.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import com.starts.hencoderview.R
import kotlin.math.max
import kotlin.math.min

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/5/20.
 *版本号：1.0

 */
class ScaleImageView(context: Context, attrs: AttributeSet) : View(context, attrs),
    GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener,Runnable {

    private var originOffsetX: Float = 0f
    private var originOffsetY: Float = 0f

    private var offsetX = 0f
    private var offsetY = 0f

    private var isBig: Boolean = false

    private var bigScale = 0f
    private var smallScale = 0f
    private var currentScale = 0f
        set(value) {
            field = value
            invalidate()
        }


    private val overScroller: OverScroller = OverScroller(context)

    private val gestureDetectorCompat = GestureDetectorCompat(context, this)

    private lateinit var scaleAnimator: ObjectAnimator

    private val bitmap: Bitmap by lazy {
        BitmapFactory.decodeResource(context.resources, R.mipmap.ui_scale_img_2)
    }

    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        originOffsetX = (w * 1f - bitmap.width) / 2
        originOffsetY = (h * 1f - bitmap.height) / 2

        if (bitmap.width * 1f / bitmap.height > w * 1f / h) {
            bigScale = height * 1f / bitmap.height * 1.5f
            smallScale = width * 1f / bitmap.width
        } else {
            bigScale = width * 1f / bitmap.width * 1.5f
            smallScale = height * 1f / bitmap.height
        }

        currentScale = smallScale
        scaleAnimator = ObjectAnimator.ofFloat(this, "currentScale", smallScale, bigScale)


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val scaleFraction = (bigScale - smallScale) * scaleAnimator.animatedFraction
        canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction)
        canvas.scale(currentScale, currentScale, width * 1f / 2, height * 1f / 2)
        canvas.drawBitmap(bitmap, originOffsetX, originOffsetY, bitmapPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetectorCompat.onTouchEvent(event)
    }

    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (isBig) {
            overScroller.fling(
                offsetX.toInt(),
                offsetY.toInt(),
                velocityX.toInt(),
                velocityY.toInt(),
                (-(bitmap.width * bigScale - width) / 2).toInt(),
                ((bitmap.width * bigScale - width) / 2).toInt(),
                (-(bitmap.height * bigScale - height) / 2).toInt(),
                ((bitmap.height * bigScale - height) / 2).toInt(),
                150,
                150
            )
            postOnAnimation(this)
        }

        return false
    }

    override fun onScroll(
        //down事件
        e1: MotionEvent,
        //移动事件
        e2: MotionEvent,
        //旧位置 - 新位置
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if (isBig) {
            offsetX -= distanceX
            offsetY -= distanceY
            fixOffsets()
            Log.d("onScroll", "distanceX = $distanceX ,distanceY = $distanceY ")
            invalidate()
        }
        return false
    }

    private fun fixOffsets() {
        offsetX = min(offsetX, (bitmap.width * bigScale - width) / 2);
        offsetX = max(offsetX, -(bitmap.width * bigScale - width) / 2);
        offsetY = min(offsetY, (bitmap.height * bigScale - height) / 2);
        offsetY = max(offsetY, -(bitmap.height * bigScale - height) / 2);
    }


    override fun onLongPress(e: MotionEvent?) {

    }


    override fun onDoubleTap(e: MotionEvent): Boolean {
        isBig = !isBig
        if (isBig) {
            scaleAnimator.start()
        } else {
            scaleAnimator.reverse()
        }

        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        return false
    }

    override fun run() {
        if(overScroller.computeScrollOffset()){
            offsetX = overScroller.currX * 1f
            offsetY = overScroller.currY * 1f
            invalidate()
            postOnAnimation(this)
        }
    }
}