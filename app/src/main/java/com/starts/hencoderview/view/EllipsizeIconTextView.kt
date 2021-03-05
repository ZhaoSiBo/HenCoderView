package com.starts.hencoderview.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.starts.hencoderview.R

/**
 *  文件描述：当 Ellipsize = end 并且 多行 。在超出情况下，可以显示包括尾部图片， 追加文字
 *  实现 文字...image  文字...文字的样式
 *  作者：Created by Lorizhao on 2021/3/4.
 *  版本号：1.0
 *
 *  如果要显示图片，请务必设置 自定义属性 isDisplayIcon = true,并设置图片的宽高
 *  可以单独设置保留文字的 大小，颜色，如果需要修改对齐方式，那就重写在OnDraw（）方法中的绘制方法
 *
 *  在onMeasure()方法中判断文字是否会超出，并设置needOverDraw，触发在onDraw()方法中追加绘制图片和文字
 *  避免在onDraw()方法中调用setText()方法，StaticLayout()在生成出来后，会重新调用OnDraw()方法
 */
class EllipsizeIconTextView : AppCompatTextView {

    companion object {
        const val PADDING_TEXT_SIZE = 10
        const val PADDING_ICON_SIZE = 10
    }

    private var needOverDraw = false
    private var viewWidth = 0
    private var viewHeight = 0
    private var hasEllipsis = false

    var iconWidth = 0
        set(value) {
            field = value
            invalidate()
        }

    var iconHeight = 0
        set(value) {
            field = value
            invalidate()
        }

    var keepText = ""
        set(value) {
            field = value
            invalidate()
        }
    private val keepTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var moreIcon: Drawable = BitmapDrawable(
            resources,
            BitmapFactory.decodeResource(resources, R.drawable.playing_com_into)
        )
        set(value) {
            field = value
            invalidate()
        }

    var isDisplayIcon = false
        set(value) {
            field = value
            invalidate()
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EllipsizeIconTextView)
        iconHeight = typedArray.getDimension(
            R.styleable.EllipsizeIconTextView_iconHeight,
            moreIcon.intrinsicHeight * 1f
        ).toInt()
        iconWidth = typedArray.getDimension(
            R.styleable.EllipsizeIconTextView_iconWidth,
            moreIcon.intrinsicWidth * 1f
        ).toInt()
        moreIcon = typedArray.getDrawable(R.styleable.EllipsizeIconTextView_moreIcon)
            ?: ContextCompat.getDrawable(context, R.drawable.playing_com_into)!!

        keepTextPaint.color =
            typedArray.getColor(R.styleable.EllipsizeIconTextView_keepTextColor, currentTextColor)

        keepTextPaint.textSize =
            typedArray.getDimension(R.styleable.EllipsizeIconTextView_keepTextSize, textSize)

        keepText = typedArray.getString(R.styleable.EllipsizeIconTextView_keepText).toString()

        isDisplayIcon =
            typedArray.getBoolean(R.styleable.EllipsizeIconTextView_isDisPlayIcon, false)

        typedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewHeight = h
        viewWidth = w
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (lineCount < maxLines) {
            return
        }

        hasEllipsis = layout.getEllipsisStart(maxLines - 1) != 0
        if (!hasEllipsis) {
            return
        }

        needOverDraw = true

        val keepTextWidth = keepTextPaint.measureText(keepText)

        val space: Float = if (isDisplayIcon) {
            if (TextUtils.isEmpty(keepText)) {
                layout.getLineWidth(0) * maxLines - iconWidth * 1f - PADDING_TEXT_SIZE
            } else {
                layout.getLineWidth(0) * maxLines - iconWidth - keepTextWidth - PADDING_TEXT_SIZE - PADDING_ICON_SIZE
            }
        } else {
            if (TextUtils.isEmpty(keepText)) {
                layout.getLineWidth(0) * maxLines * 1f
            } else {
                layout.getLineWidth(0) * maxLines * 1f - keepTextWidth - PADDING_TEXT_SIZE
            }
        }

        val subText = TextUtils.ellipsize(text, paint, space, TextUtils.TruncateAt.END)

        text = subText
    }

    fun setKeepTextColor(@ColorInt color: Int) {
        keepTextPaint.color = color
        invalidate()
    }

    fun setKeepTextSize(textSize: Float) {
        keepTextPaint.textSize = textSize
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!needOverDraw) {
            return
        }
        val keepTextWidth = keepTextPaint.measureText(keepText)

        val middle =
            layout.getLineTop(maxLines - 1) + ((layout.getLineBottom(maxLines - 1) - layout.getLineTop(
                maxLines - 1
            )) / 2)
        if (!TextUtils.isEmpty(keepText)) {
            val textLeft = layout.getLineWidth(maxLines - 1) + PADDING_TEXT_SIZE
            val textTop = layout.getLineBaseline(maxLines - 1) * 1f
            canvas.drawText(keepText, textLeft, textTop, keepTextPaint)
            if (isDisplayIcon) {
                val left = (textLeft + keepTextWidth + PADDING_ICON_SIZE).toInt()
                val bottom = middle + iconHeight / 2
                val top = middle - iconHeight / 2
                val right = left + iconWidth
                moreIcon.setBounds(left, top, right, bottom)
                moreIcon.draw(canvas)
            }
        } else {
            if (isDisplayIcon) {
                val left = (layout.getLineWidth(maxLines - 1) + PADDING_ICON_SIZE).toInt()
                val bottom = middle + iconHeight / 2
                val top = middle - iconHeight / 2
                val right = left + iconWidth
                moreIcon.setBounds(left, top, right, bottom)
                moreIcon.draw(canvas)
            }
        }


    }

}