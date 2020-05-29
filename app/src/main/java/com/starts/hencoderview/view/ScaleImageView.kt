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
class ScaleImageView(context: Context, attrs: AttributeSet) : View(context, attrs),GestureDetector.OnDoubleTapListener,GestureDetector.OnGestureListener {

    private var originalOffsetX = 0f
    private var originalOffsetY = 0f

    private var currentScale = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var bigScale = 0f
    private var smallScale = 0f

    private val OVER_SCALE_FACTOR = 1.2f

    private var isBig = false

    private var offsetX = 0f
    private var offsetY = 0f

    private val gestureDetector:GestureDetectorCompat by  lazy {
        GestureDetectorCompat(context,this)
    }

    private val scroll = OverScroller(context)

    private val scaleAnimation: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(this, "currentScale", smallScale, bigScale)
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap: Bitmap by lazy {
        BitmapFactory.decodeResource(context.resources, R.mipmap.ui_scale_img_2)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        originalOffsetX = (width * 1f - bitmap.width) / 2f
        originalOffsetY = (height * 1f - bitmap.height) / 2f

        if (bitmap.width * 1f / bitmap.height > width * 1f / height) {
            smallScale = width * 1f / bitmap.width
            bigScale = height * 1f / bitmap.height * OVER_SCALE_FACTOR
        } else {
            smallScale = height * 1f / bitmap.height
            bigScale = width * 1f / bitmap.width * OVER_SCALE_FACTOR
        }
        currentScale = smallScale
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(offsetX * currentScale , offsetY * currentScale)
        canvas.scale(currentScale, currentScale, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onDoubleTap(e: MotionEvent?):Boolean{
        isBig = !isBig
        if(isBig){
            scaleAnimation.start()
        }else{
            scaleAnimation.reverse()
        }
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?) = false
    override fun onSingleTapConfirmed(e: MotionEvent?) = false
    override fun onShowPress(e: MotionEvent?){

    }
    override fun onSingleTapUp(e: MotionEvent?): Boolean = false

    override fun onDown(e: MotionEvent?) = true

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if(isBig){
            offsetX-=distanceX
            offsetY-=distanceY
            fixOffsets()
            Log.d("onScroll","distanceX:${distanceX} , distanceY:${distanceY}")
            invalidate()
        }
       return false
    }

    override fun onLongPress(e: MotionEvent?) {

    }


    private fun fixOffsets(){
        offsetX = max(offsetX , (width*1f - bitmap.width*1f) / 2)
        offsetX = min(offsetX, -(width * 1f - bitmap.width * 1f) / 2)

        offsetY = max(offsetY , (height*1f - bitmap.height*1f) / 2)
        offsetY = min(offsetY, -(height * 1f - bitmap.height * 1f) / 2)

    }


}