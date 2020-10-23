package com.starts.hencoderview.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.starts.hencoderview.R
import kotlin.math.min

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/10/23.
 *版本号：1.0

 */
class ParticleScatteringView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var viewWidth = 0
    var viewHeight = 0

    val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    lateinit var imageShader: BitmapShader

    //粒子的数量
    val particleCount = 500


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h

        val imageBitmap: Bitmap by lazy {
            val option = BitmapFactory.Options()
            option.outWidth = (viewWidth *1f/ 2).toInt()
            option.outHeight =( viewHeight*1f / 2).toInt()
            BitmapFactory.decodeResource(context.resources, R.mipmap.ui_scale_img,option)
        }

        imageShader = BitmapShader(imageBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        imagePaint.shader = imageShader




    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(
            viewWidth * 1f / 2,
            viewHeight * 1f / 2,
            min(viewHeight * 1f, viewWidth * 1f) / 3,
            imagePaint
        )
    }

}

class Particle {
    var x: Float = 0f//X坐标
    var y: Float = 0f//Y坐标
    var radius: Float = 0f//半径
    var speed: Float = 0f//速度
    var alpha: Int = 0//透明度
}