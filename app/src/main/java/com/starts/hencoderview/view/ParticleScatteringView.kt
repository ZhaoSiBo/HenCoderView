package com.starts.hencoderview.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.starts.hencoderview.R
import kotlin.math.*
import kotlin.random.Random

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/10/23.
 *版本号：1.0

 */
class ParticleScatteringView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var viewWidth = 0
    var viewHeight = 0


    var centerX = 0f
    var centerY = 0f

    val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    lateinit var imageShader: BitmapShader

    //粒子的数量
    val particleCount = 300
    val particlePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    //粒子集合
    val particleList = ArrayList<Particle>()

    private var particleAnimator = ValueAnimator.ofFloat(0f, 1f)

    private var innerCirclePath = Path()
    private var pathMeasure = PathMeasure()

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


        }
    }

    private fun fixXY(it: Particle) {

        if (it.x.absoluteValue >= (cos(it.angle)) * it.maxOffset) {
            it.x = ((viewWidth * 1 / 2) + min(
                viewHeight * 1f,
                viewWidth * 1f
            ) / 2.7 * cos(it.angle * Math.PI / 180)).toFloat() + (10 - Random.nextInt(20))
        }
        if (it.y.absoluteValue >= (sin(it.angle)) * it.maxOffset) {
            it.y = ((viewHeight * 1f / 2) + min(viewHeight * 1f, viewWidth * 1f) / 2.7 * sin(
                it.angle * Math.PI / 180
            )).toFloat() + (10 - Random.nextInt(20))
        }

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h

        centerX = viewWidth * 1f / 2
        centerY = viewHeight * 1f / 2

        innerCirclePath.addCircle(
            viewWidth * 1f / 2,
            viewHeight * 1f / 2,
            min(viewHeight * 1f, viewWidth * 1f) / 3, Path.Direction.CCW
        )

        pathMeasure.setPath(innerCirclePath, false)

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

            particle.angle = i * singleAngle

            particle.x = ((viewWidth * 1 / 2) + min(
                viewHeight * 1f,
                viewWidth * 1f
            ) / 2.7 * cos(particle.angle * Math.PI / 180)).toFloat() + (10 - Random.nextInt(20))

            particle.y = ((viewHeight * 1f / 2) + min(viewHeight * 1f, viewWidth * 1f) / 2.7 * sin(
                particle.angle * Math.PI / 180
            )).toFloat() + (10 - Random.nextInt(20))

            particle.speed = (Random.nextInt(10) + 5).toFloat()

            particle.offset = cos(particle.angle) * particle.x

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
            canvas.drawCircle(it.x, it.y, it.radius, particlePaint)
        }

    }

    class Particle {
        var x: Float = 0f//X坐标
        var y: Float = 0f//Y坐标
        var radius: Float = 3f//半径
        var speed: Float = 0f//速度
        var alpha: Int = 1//透明度
        var angle = 0f
        val maxOffset = 150f
        var offset = 0f
    }
}