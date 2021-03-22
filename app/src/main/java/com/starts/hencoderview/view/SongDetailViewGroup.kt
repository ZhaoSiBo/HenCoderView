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
import com.starts.hencoderview.R
import com.starts.hencoderview.dp
import com.starts.hencoderview.sp
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
        const val PADDING_SIZE = 15;
        const val margin_top_song_publish_time = 6;
        const val margin_top_song_detail = 15;
    }

    val songName: TextView = TextView(context).apply {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        text =
            "Vibration/ep.9五月天的青春你需要了解下,Vibration/ep.9五月天的青春你需要了解下,Vibration/ep.9五月天的青春你需要了解下"
        maxLines = 2
        ellipsize = TextUtils.TruncateAt.END
        setTextColor(Color.WHITE)
        paint.isFakeBoldText = true
        textSize = sp(23)
        addView(this)
    }


    val songPublishTime = TextView(context).apply {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
        text = "2018年10月17日更新"
        maxLines = 1
        setTextColor(Color.WHITE)
        paint.isFakeBoldText = true
        textSize = sp(23)
        visibility = if (!TextUtils.isEmpty(text)) {
            View.VISIBLE
        } else {
            View.GONE
        }
        addView(this)
    }

    val songDetails: EllipsizeIconTextView = EllipsizeIconTextView(context).apply {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT)
        ellipsize = TextUtils.TruncateAt.END
        maxLines = 2
        setTextColor(Color.WHITE)
        setTextWithKeepText(
            text = "五月来了，据说这是一个非常多人都喜欢的月份，在盛夏来临之前不仅天气舒服，而且充满各种未知的幻想，五月来了，据说这是一个非常多人都喜欢的月份，在盛夏来临之前不仅天气舒服，而且充满各种未知的幻想，",
            keepTextString = "",
            isDisplayIcon = true,
            drawableRes = R.drawable.playing_com_into
        )
        addView(this)
    }


    val songMore: ImageView = ImageView(context).apply {
        layoutParams = LayoutParams(10.dp , 10.dp)
        setImageResource(R.drawable.playing_com_into)
        visibility = if (songDetails.text == "歌曲详情") {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
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
        songName.measure(widthMeasureSpec , heightMeasureSpec)


//        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }


}
