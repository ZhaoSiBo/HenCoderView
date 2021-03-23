package com.starts.hencoderview.view

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import com.starts.hencoderview.*
import java.time.format.TextStyle

/**
 *  文件描述：.业务中，经常遇到，根据业务的数据值，或有或无，来控制UI的显示隐藏，位置上下，测试使用自定义ViewGroup来实现这种效果，
 *  也为了更好的空安全
 *  作者：Created by lorizhao on 2021/3/22.
 *  版本号：1.0

 */
class SongDetailViewGroup : ViewGroup {

    companion object {
        //内部padding 15dp
        const val PADDING_SIZE = 30;
        const val margin_top_song_publish_time = 6;
        const val margin_top_song_detail = 15;
    }

    val songName: TextView = TextView(context).apply {
        layoutParams = MarginLayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setLayoutMarginTop(if (TextUtils.isEmpty(text)) {
            0.dp
        } else {
            4.dp
        })
        text = "Vibration/ep.9五月天的青春你需要了解下,Vibration/ep.9五月天的青春你需要了解下,Vibration/ep.9五月天的青春你需要了解下"
        maxLines = 2
        ellipsize = TextUtils.TruncateAt.END
        setTextColor(Color.WHITE)
        paint.isFakeBoldText = true
        textSize = sp(23)
        setVisibility(!TextUtils.isEmpty(text))
        addView(this)
    }


    val songPublishTime = TextView(context).apply {
        layoutParams = MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        text = "2018年10月17日更新"
        setLayoutMarginTop(if (TextUtils.isEmpty(text)) {
            0.dp
        } else {
            6.dp
        })
        maxLines = 1
        setTextColor(Color.WHITE)
        paint.isFakeBoldText = true
        textSize = sp(23)
        setVisibility(!TextUtils.isEmpty(text))
        addView(this)
    }

    val songDetails: EllipsizeIconTextView = EllipsizeIconTextView(context).apply {
        layoutParams = MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        setLayoutMarginTop(if (TextUtils.isEmpty(text)) {
            0.dp
        } else {
            6.dp
        })
        setTextWithKeepText(
                text = "五月来了，据说这是一个非常多人都喜欢的月份，在盛夏来临之前不仅天气舒服，而且充满各种未知的幻想，五月来了，据说这是一个非常多人都喜欢的月份，在盛夏来临之前不仅天气舒服，而且充满各种未知的幻想，",
                keepTextString = "",
                isDisplayIcon = true,
                drawableRes = R.drawable.playing_com_into
        )
        ellipsize = TextUtils.TruncateAt.END
        maxLines = 2
        setVisibility(!TextUtils.isEmpty(text))
        setTextColor(Color.WHITE)
        addView(this)
    }


    val songMore: ImageView = ImageView(context).apply {
        layoutParams = LayoutParams(10.dp, 10.dp)
        setImageResource(R.drawable.playing_com_into)
        setVisibility(songDetails.text == "歌曲详情")
        addView(this)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        setBackgroundColor(Color.GREEN)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        songName.let {
            it.defaultWithMeasureSpace(this)
            it.defaultHeightMeasureSpace(this)
        }

        songPublishTime.let {
            it.defaultWithMeasureSpace(this)
            it.defaultHeightMeasureSpace(this)
        }

        songDetails.let {
            it.defaultWithMeasureSpace(this)
            it.defaultHeightMeasureSpace(this)
        }
        songMore.let {
            it.defaultWithMeasureSpace(this)
            it.defaultHeightMeasureSpace(this)
        }
        val returnHeight = songName.measuredHeight
        + songPublishTime.measuredHeight
        + songDetails.measuredHeight
        + songMore.measuredHeight
        + songName.marginTop
        + songPublishTime.marginTop
        + songDetails.marginTop
        + songMore.marginTop
        + PADDING_SIZE

        setMeasuredDimension((getScreenWidth() - 40.dp).toExactlyMeasureSpace(),returnHeight.toExactlyMeasureSpace())
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

//        songName.layout(PADDING_SIZE + )

    }


}
