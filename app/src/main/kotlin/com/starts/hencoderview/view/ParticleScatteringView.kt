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
import kotlin.system.measureTimeMillis

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/10/23.
 *版本号：1.0

 */
class ParticleScatteringView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var viewWidth = 0
    var viewHeight = 0


    var imageAngle = 0f

    var centerX = 0f
    var centerY = 0f

    val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    lateinit var imageShader: BitmapShader

    //粒子的数量
    val particleCount = 500
    val particlePaint = Paint(Paint.ANTI_ALIAS_FLAG)


    val imageBitmap: Bitmap by lazy {
        val option = BitmapFactory.Options()
        option.outWidth = (viewWidth * 1f / 2).toInt()
        option.outHeight = (viewHeight * 1f / 2).toInt()
        BitmapFactory.decodeResource(context.resources, R.mipmap.ui_scale_img, option)
    }


    val bitmapCanvas = Canvas()
    //粒子集合
    val particleList = ArrayList<Particle>()

    private var particleAnimator = ValueAnimator.ofFloat(0f, 1f)

    private var innerCirclePath = Path()
    private var pathMeasure = PathMeasure()

    init {
        particleAnimator.duration = 4000
        particleAnimator.repeatCount = -1
        particleAnimator.interpolator = LinearInterpolator()
        particleAnimator.addUpdateListener {
            updateParticle()
            imageAngle = 360 * it.animatedValue as Float
            invalidate()//重绘界面
        }
    }

    private fun updateParticle() {
        particleList.forEach {
            if (it.offset > it.maxOffset) {
                it.offset = 0f
                it.speed = (Random.nextInt(2, 4)).toFloat()
            }
            it.alpha = ((1f - it.offset / it.maxOffset) * 225f).toInt()

            it.offset += it.speed
//            if(it.offset >=  it.maxOffset){
//                return
//            }else{
            it.x = centerX + cos(it.angle) * (310f + it.offset)

            if (it.y > centerY) {
                it.y = (sin(it.angle) * (310f + it.offset) + centerY)
            } else {
                it.y = (centerY - sin(it.angle) * (310f + it.offset))
            }

            it.offset += it.speed

        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h

        centerX = viewWidth * 1f / 2
        centerY = viewHeight * 1f / 2

        innerCirclePath.addCircle(
            centerX,
            centerY,
            310f, Path.Direction.CCW
        )

        pathMeasure.setPath(innerCirclePath, false)
        val singleLength = pathMeasure.length / particleCount




        imageShader = BitmapShader(imageBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        imagePaint.shader = imageShader


        particlePaint.color = Color.WHITE
        particlePaint.style = Paint.Style.FILL
        for (i in 0..particleCount) {
            val particle = Particle()
            val localArray = FloatArray(2)
            val tanArray = FloatArray(2)

            if (pathMeasure.getPosTan(i * singleLength, localArray, tanArray)) {
                particle.x = localArray[0] + (Random.nextInt(10) - 20)
                particle.y = localArray[1] + (Random.nextInt(10) - 20)
            }

            particle.speed = (Random.nextInt(2, 4)).toFloat()
            particle.angle = acos((localArray[0] - centerX) / 280f)
            particleList.add(particle)
        }
        particleAnimator.start()

    }

    val bitmap:Bitmap by lazy {
        Bitmap.createBitmap(viewWidth, viewHeight,
            Bitmap.Config.ARGB_8888)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()

        canvas.rotate(imageAngle , centerX , centerY)
        canvas.drawCircle(
            centerX,
            centerY,
            300f,
            imagePaint
        )
        canvas.restore()
        val time = measureTimeMillis {
            particleList.forEach {
                particlePaint.alpha = it.alpha
                canvas.drawCircle(it.x, it.y, it.radius, particlePaint)
            }
        }

        Log.d("ParticleScatteringView", "time = $time")

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