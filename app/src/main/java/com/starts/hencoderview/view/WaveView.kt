package com.starts.hencoderview.view

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.LruCache
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.starts.hencoderview.HenApplication
import com.starts.hencoderview.R
import com.starts.hencoderview.getMaterialColor
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import kotlin.math.pow
import kotlin.math.sqrt

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/11/10.
 *版本号：1.0

 */
class WaveView:View{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mBitmapPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var bannerViewPager: BannerViewPager<String, ViewPager2LoopViewHolder>? = null
    private var currentPosition = 0
    private var nextPosition = 0
    //手指是否是从右向左
    private var fromRightToLeft = false
    private var scrollState = 0
    private var positionOffset = 0f

    val portMode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    /**
     * 波纹半径
     */
    private var mRadius = 0.0

    private lateinit var drawBitmap: Bitmap
    private lateinit var drawCanvas: Canvas

    init {
        mBitmapPaint.isAntiAlias = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (width > 0 && height > 0) {
            drawBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            drawCanvas = Canvas(drawBitmap)
            mRadius = sqrt((height / 2.0).pow(2.0) + width.toDouble().pow(2.0)) * 1.2
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (bannerViewPager != null && bannerViewPager?.data?.isNotEmpty() == true) {
            val currentItemSection = bannerViewPager!!.data[currentPosition]
//            Logger.d("currentSection = $currentItemSection")
            val nextItemSection = bannerViewPager!!.data[nextPosition]
//            Logger.d("nextItemSection = $nextItemSection")

            val curBitmap = BitmapCache.getBitmap(currentPosition,
                width, height , resources)
            val nextBitmap = BitmapCache.getBitmap(nextPosition, width, height ,resources)
            if (positionOffset == 0f) {
                canvas.drawBitmap(curBitmap, 0f, 0f, null)
            } else if (positionOffset < 1f) {

//                canvas.drawBitmap(nextBitmap, 0f, 0f, null)
//                drawCanvas.drawBitmap(curBitmap, 0f, 0f, mBitmapPaint)
//                mBitmapPaint.xfermode = portMode
//                if (fromRightToLeft) {
//                    drawCanvas.drawCircle(width.toFloat(), height / 2f, (positionOffset * mRadius).toFloat(), mBitmapPaint)
//                } else {
//                    drawCanvas.drawCircle(0f, height / 2f, ((1 - positionOffset) * mRadius).toFloat(), mBitmapPaint)
//                }
//                mBitmapPaint.xfermode = null
//                canvas.drawBitmap(drawBitmap, 0f, 0f, mBitmapPaint)

                canvas.drawBitmap(curBitmap , 0f , 0f , mBitmapPaint)
                drawCanvas.drawBitmap(nextBitmap,0f, 0f , mBitmapPaint)
                mBitmapPaint.xfermode = portMode
                if (fromRightToLeft) {
                    drawCanvas.drawCircle(width.toFloat(), height / 2f, (positionOffset * mRadius).toFloat(), mBitmapPaint)
                } else {
                    drawCanvas.drawCircle(0f, height / 2f, ((1 - positionOffset) * mRadius).toFloat(), mBitmapPaint)
                }
                canvas.drawBitmap(drawBitmap, 0f, 0f, mBitmapPaint)

//                mBitmapPaint.xfermode = null

            }
        }
    }

    fun bindViewPager(banner: BannerViewPager<String, ViewPager2LoopViewHolder>) {
        this.bannerViewPager = banner
//        banner.data.forEachIndexed { index, section ->
//            BitmapCache.putBitmap(index,
//                section.bg_img,
//                if (section.bg_color.isNotEmpty()) {
//                    Color.parseColor("#${section.bg_color}")
//                } else {
//                    ContextCompat.getColor(Fast4Android.CONTEXT, R.color.colorPrimary)
//                },
//                width, height)
//        }

        bannerViewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var sumPositionAndPositionOffset = 0f

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                scrollState = state
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                when (scrollState) {
                    ViewPager2.SCROLL_STATE_IDLE -> {
                        currentPosition = 0
                        nextPosition = 0
                    }
                    else -> {
                        //非最后一刻，当前位置和下一个位置有变化
                        if(positionOffset != 0f && positionOffsetPixels != 0){
                            if (position + positionOffset > sumPositionAndPositionOffset) {
                                //right to left
                                //                    Logger.d("right to left")
                                fromRightToLeft = true
                                currentPosition = fixPosition(position)
                                nextPosition = fixPosition(currentPosition + 1)

                                //                    calculateDirection()
                            } else {
                                //left to right
                                //                    Logger.d("left to right")
                                fromRightToLeft = false
                                currentPosition = fixPosition(position + 1)
                                nextPosition = fixPosition(position)

                            }
                            sumPositionAndPositionOffset = position + positionOffset
                        }else{
                            //最后一刻，当前位置和下一个位置 ，则根据position来确定
                            currentPosition = position
                            nextPosition = fixPosition(position + 1)
                        }
                    }
                }
                this@WaveView.positionOffset = positionOffset
                invalidate()
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPosition = position
            }
        })
    }


    fun fixPosition(position: Int): Int {
        return if (bannerViewPager != null) {
            return when {
                position > bannerViewPager!!.data.size - 1 -> {
                    0
                }
                position < 0 -> {
                    bannerViewPager!!.data.size - 1
                }
                else -> {
                    position
                }
            }
        } else {
            0
        }

    }

}

object BitmapCache{

    private val lruCache = LruCache<Int, Bitmap?>(10)

    fun getBitmap(position:Int , width:Int ,height:Int , resources: Resources): Bitmap {
        val bitmap = lruCache.get(position)
        return if(bitmap == null){
            val b =  Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            b.eraseColor(getMaterialColor(resources, position))
            lruCache.put(position , b)
            b
        }else{
            bitmap
        }
    }
    fun putBitmap(position:Int , image:Bitmap){
        lruCache.put(position, image)
    }

}

class ViewPager2LoopAdapter : BaseBannerAdapter<String, ViewPager2LoopViewHolder>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.view_pager
    }

    override fun createViewHolder(itemView: View, viewType: Int): ViewPager2LoopViewHolder {
        return ViewPager2LoopViewHolder(itemView)
    }

    override fun onBind(holder: ViewPager2LoopViewHolder, data: String, position: Int, pageSize: Int) {
        holder.bindData(data, position, pageSize)
    }

}

class ViewPager2LoopViewHolder(val itemView: View) : BaseViewHolder<String>(itemView) {

    override fun bindData(data: String, position: Int, pageSize: Int) {
        val imageView = findView<ImageView>(R.id.imageView)
        Glide.with(imageView).load(data).into(imageView)
    }
}



