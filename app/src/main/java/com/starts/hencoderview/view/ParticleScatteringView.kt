package com.starts.hencoderview.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.starts.hencoderview.R
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

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
    val particleCount = 100
    val particlePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    //粒子集合
    val particleList = ArrayList<Particle>()


    private var particleAnimator = ValueAnimator.ofFloat(0f, 1f)
    init {
        particleAnimator.duration = 2000
        particleAnimator.repeatCount = -1
        particleAnimator.interpolator = LinearInterpolator()
        particleAnimator.addUpdateListener {
            updateParticle(it.animatedValue as Float)
            invalidate()//重绘界面
        }
    }

    private fun updateParticle(fl: Float) {
        particleList.forEach {
            it.x = x + it.speed
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h

        val imageBitmap: Bitmap by lazy {
            val option = BitmapFactory.Options()
            option.outWidth = (viewWidth * 1f / 2).toInt()
            option.outHeight = (viewHeight * 1f / 2).toInt()
            BitmapFactory.decodeResource(context.resources, R.mipmap.ui_scale_img, option)
        }

        imageShader = BitmapShader(imageBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        imagePaint.shader = imageShader

        val singleAngle = 360 * 1f / particleCount

        particlePaint.color = Color.WHITE
        particlePaint.style = Paint.Style.FILL
        for (i in 0..particleCount) {
            val particle = Particle()
            particle.x = ((viewWidth * 1 / 2) + min(
                viewHeight * 1f,
                viewWidth * 1f
            ) / 2.7 * cos(singleAngle * i * Math.PI / 180)).toFloat()
            particle.y = ((viewHeight * 1f / 2) + min(viewHeight * 1f, viewWidth * 1f) / 2.7 * sin(
                singleAngle * i * Math.PI / 180
            )).toFloat()
            particle.speed = (Random.nextInt(10) + 5).toFloat()

            particleList.add(particle)
        }
        particleAnimator.start()

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(
            viewWidth * 1f / 2,
            viewHeight * 1f / 2,
            min(viewHeight * 1f, viewWidth * 1f) / 3,
            imagePaint
        )
        particleList.forEach {
            canvas.drawCircle(it.x ,it.y , it.radius ,particlePaint)
        }
    }

}

class Particle {
    var x: Float = 0f//X坐标
    var y: Float = 0f//Y坐标
    var radius: Float = 3f//半径
    var speed: Float = 0f//速度
    var alpha: Int = 1//透明度
    val maxDistance = 20f
}