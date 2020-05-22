package com.starts.hencoderview.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
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
    GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val OVER_SCALE = 1.5f

    private var originOffsetX = 0f
    private var originOffsetY = 0f

    private var offsetX = 0f
    private var offsetY = 0f

    private val bitmap: Bitmap by lazy {
        BitmapFactory.decodeResource(context.resources, R.mipmap.ui_scale_img_2)
    }
    private val gestureDetector = GestureDetectorCompat(context, this)
    private val centerPoint = Point()

    var scale = 0f
    var bigScale = 0f
    var smallScale = 0f

    var isBig = false

    private var scaleFraction = 0f
        set(value) {
            field = value
            invalidate()
        }


    private val objectAnimator:ObjectAnimator = ObjectAnimator.ofFloat(this,"scaleFraction",0f,1f)


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerPoint.set(w / 2, h / 2)

        if (bitmap.width * 1.0f / bitmap.height > width * 1.0f / height) {
            smallScale = width * 1f / bitmap.width
            bigScale = height * 1f / bitmap.height * OVER_SCALE
        } else {
            smallScale = height * 1f / bitmap.height
            bigScale = width * 1f / bitmap.width * OVER_SCALE
        }

        originOffsetX =  centerPoint.x - bitmap.width / 2f
        originOffsetY = centerPoint.y - bitmap.height / 2f

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(offsetX,offsetY)
        scale = smallScale + (bigScale - smallScale) * scaleFraction
        canvas.scale(scale, scale, width / 2f, height / 2f)
        canvas.drawBitmap(
            bitmap,
            originOffsetX,
            originOffsetY,
            bitmapPaint
        )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)

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
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    override fun onScroll(
        //按下事件
        e1: MotionEvent?,
        //当前事件
        e2: MotionEvent?,
        //两次位移之间位移的X的差值（旧位置-新位置）
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if(isBig){
            offsetX -= distanceX

            offsetY -= distanceY
            invalidate()
        }
        return false
    }

    override fun onLongPress(e: MotionEvent?) {

    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        isBig = !isBig
        if(isBig){
            objectAnimator.start()
        }else{
            objectAnimator.reverse()
        }
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?) = false

}