package com.starts.hencoderview.view

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.text.TextUtilsCompat
import com.starts.hencoderview.BuildConfig
import com.starts.hencoderview.R
import com.starts.hencoderview.SpanUtils
import com.starts.hencoderview.dp2px

/**
 *  文件描述：当 Ellipsize = end 并且 多行 。在超出情况下，可以显示包括尾部图片， 追加文字  Tips：只能在缩略状态下才会生效！！！
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
        const val TAG = "EllipsizeIconTextView"
        const val PADDING_TEXT_SIZE = 10
        const val PADDING_ICON_SIZE = 10

        //用于缩略的符号
        var EXPAND_TEXT = "…"

        //箭头和缩略符之间的空格
        var SPACE_WITH_ICON = 1

    }

    private var needOverDraw = false

    private var iconWidth = -1
    private var iconHeight = -1
    private var keepText = ""
    private var content: String = ""
    private val keepTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var moreIcon: Drawable? = null

    private var isDisplayIcon = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!isEllipsized()) {
            return
        }

        needOverDraw = true
        if (TextUtils.isEmpty(keepText) && isDisplayIcon) {
            //在没有保留文字，只有保留图片功能的时候，通过缩略的字符串数量来操作，这样会更准确的显示图片的位置
            val lastLine = maxLines - 1
            val lineStartIndex = layout.getLineStart(lastLine)
            val lineEllipsisStart = lineStartIndex + layout.getEllipsisStart(lastLine)
            val expandTextLength: Int = EXPAND_TEXT.length

            val temp = text.subSequence(
                0,
                lineEllipsisStart - expandTextLength + 1 - SPACE_WITH_ICON
            ).toString() + EXPAND_TEXT
            text = temp
            return
        }

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
    }

    fun setKeepTextSize(textSize: Float) {
        keepTextPaint.textSize = textSize
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
                moreIcon?.setBounds(left, top, right, bottom)
                moreIcon?.draw(canvas)
            }
        } else {
            if (isDisplayIcon) {
                val left = (layout.getLineWidth(maxLines - 1) + PADDING_ICON_SIZE).toInt()
                val bottom = middle + iconHeight / 2
                val top = middle - iconHeight / 2
                val right = left + iconWidth
                moreIcon?.setBounds(left, top, right, bottom)
                moreIcon?.draw(canvas)
            }
        }
    }

    private fun isEllipsized(): Boolean {
        if (ellipsize == null || TextUtils.TruncateAt.MARQUEE == ellipsize) {
            return false;
        }
        if (layout == null) {
            return false
        }
        for (line in 0 until layout.lineCount) {
            if (layout.getEllipsisCount(line) > 0) {
                return true
            }
        }
        return false
    }

    fun setTextWithKeepText(
        text: String,
        keepTextString: String,
        isDisplayIcon: Boolean,
        @DrawableRes drawableRes: Int = 0,
        drawableHeight: Int = -1,
        drawableWidth: Int = -1
    ){
        val drawable = BitmapDrawable(resources , BitmapFactory.decodeResource(resources , R.drawable.playing_com_into))
        setTextWithKeepText(text , keepTextString , isDisplayIcon , drawable , drawableHeight , drawableWidth)
    }


    fun setTextWithKeepText(
        text: String,
        keepTextString: String,
        isDisplayIcon: Boolean,
        drawable: Drawable? = null,
        drawableHeight: Int = -1,
        drawableWidth: Int = -1
    ) {
        this.keepText = keepTextString
        this.isDisplayIcon = isDisplayIcon
        this.content = text
        if (isDisplayIcon) {
            if (BuildConfig.DEBUG) {
                checkNotNull(drawable) {
                    throw IllegalAccessException("${TAG}-fun setTextWithKeepText:drawable is null")
                }
            } else {
                drawable?.let {

                    iconWidth = if (drawableWidth == -1) {
                        it.intrinsicWidth
                    } else {
                        dp2px(drawableWidth)
                    }
                    iconHeight = if (drawableHeight == -1) {
                        it.intrinsicHeight
                    } else {
                        dp2px(drawableHeight)
                    }
                    it.setBounds(0, 0, iconWidth, iconHeight)
                    moreIcon = it
                    val sp = SpanUtils(context)
                        .append(text)
                        .setFontSize(this.textSize.toInt())
                        .setForegroundColor(this.currentTextColor)
                        .append(keepText)
                        .setFontSize(keepTextPaint.textSize.toInt())
                        .setForegroundColor(keepTextPaint.color)
                        .appendImage(moreIcon!!)
                        .create()
                    setText(sp)
                }
            }

        } else {
            val sp = SpanUtils(context)
                .append(text)
                .setFontSize(this.textSize.toInt())
                .setForegroundColor(this.currentTextColor)
                .append(keepText)
                .setFontSize(keepTextPaint.textSize.toInt())
                .setForegroundColor(keepTextPaint.color)
                .create()
            setText(sp)
        }


    }


}