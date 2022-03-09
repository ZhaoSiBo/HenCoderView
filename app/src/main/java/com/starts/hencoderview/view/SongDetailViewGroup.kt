package com.starts.hencoderview.view

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import com.starts.hencoderview.*

/**
 *  文件描述：.业务中，经常遇到，根据业务的数据值，或有或无，来控制UI的显示隐藏，位置上下，测试使用自定义ViewGroup来实现这种效果
 *  也为了更好的空安全
 *  作者：Created by lorizhao on 2021/3/22.
 *  版本号：1.0

 */
class SongDetailViewGroup : ViewGroup {

    companion object {
        //内部padding 15dp
        const val PADDING_SIZE = 30;
    }

    val songName: TextView = TextView(context).apply {
//        text = "Vibration/ep.9五月天的青春你需要了解下,Vibration/ep.9五月天的青春你需要了解下,Vibration/ep.9五月天的青春你需要了解下"
        layoutParams =
            MarginLayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        maxLines = 2
        ellipsize = TextUtils.TruncateAt.END
        setTextColor(Color.WHITE)
        paint.isFakeBoldText = true
        textSize = 16f
        addView(this)
    }


    val songPublishTime = TextView(context).apply {
        layoutParams = MarginLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        maxLines = 1
        setTextColor(Color.WHITE)
        paint.isFakeBoldText = true
        textSize = 14f
        addView(this)
    }

    val songDetails: EllipsizeIconTextView = EllipsizeIconTextView(context).apply {
        layoutParams = MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textSize = 14f
        ellipsize = TextUtils.TruncateAt.END
        maxLines = 2
        addView(this)
    }


    val songMore: ImageView = ImageView(context).apply {
        layoutParams = LayoutParams(15.dp, 15.dp)
        setImageResource(R.drawable.playing_com_into)
        addView(this)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue_300))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setPadding(PADDING_SIZE, PADDING_SIZE, PADDING_SIZE, PADDING_SIZE)

        songName.setVisibility(!TextUtils.isEmpty(songName.text))
        songPublishTime.setVisibility(!TextUtils.isEmpty(songPublishTime.text))
        songDetails.setVisibility(!TextUtils.isEmpty(songDetails.text))
        songMore.setVisibility(songDetails.text == "歌曲详情")

        songName.autoMeasure()
        songPublishTime.autoMeasure()
        songDetails.autoMeasure()
        songMore.autoMeasure()
        val wrapContentHeight =
            songName.measuredHeight +
                    songPublishTime.measuredHeight +
                    songDetails.measuredHeight +
                    if (songPublishTime.visibility == View.VISIBLE) {
                        14.dp
                    } else {
                        0
                    } +
                    if (songDetails.visibility == View.VISIBLE) {
                        14.dp
                    } else {
                        0
                    } +
                    PADDING_SIZE * 2
        setMeasuredDimension(
            widthMeasureSpec,
            wrapContentHeight
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        songName.let {
            it.layout(PADDING_SIZE, it.marginTop + PADDING_SIZE)
        }
        songPublishTime.let {
            it.layout(
                PADDING_SIZE,
                songName.bottom + if (songPublishTime.visibility == View.VISIBLE) {
                    14.dp
                } else {
                    0
                }
            )
        }
        songDetails.let {
            it.layout(
                PADDING_SIZE,
                songPublishTime.bottom + if (songDetails.visibility == View.VISIBLE) {
                    14.dp
                } else {
                    0
                }
            )
        }

        if (songDetails.text == "歌曲详情") {
            songMore.visibility = View.VISIBLE
            songMore.layout(
                songDetails.right + 2.dp,
                (songDetails.top + songDetails.measuredHeight / 2) - songMore.measuredHeight / 2
            )
        } else {
            songMore.layout(0, 0, 0, 0)
            songMore.visibility = View.INVISIBLE
        }
    }


    protected fun View.autoMeasure() {
        measure(
            this.defaultWithMeasureSpace(this@SongDetailViewGroup),
            this.defaultHeightMeasureSpace(this@SongDetailViewGroup)
        )
    }


    protected fun View.layout(x: Int, y: Int, fromRight: Boolean = false) {
        if (!fromRight) {
            layout(x, y, x + this.measuredWidth, y + this.measuredHeight)
        } else {
            layout(this@SongDetailViewGroup.measuredWidth - x - measuredWidth, y)
        }

    }

}
