package com.starts.hencoderview.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.starts.hencoderview.R
import kotlin.math.max

/**

 *文件描述：.
 *作者：Created by zhaosibo on 2021/5/14.
 *版本号：1.0

 */
class PodcastRankItemView : BaseViewGroup {

    val tvRankNum = TextView(context).apply {
        layoutParams =
            MarginLayoutParams(44.dp, LayoutParams.WRAP_CONTENT)
        addView(this)
    }

    val rankStateImg = ImageView(context).apply {
        layoutParams = MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }
    val tvRankChange = TextView(context).apply {
        layoutParams =
            MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        addView(this)
    }


    val podcastImg = ImageView(context).apply {
        layoutParams = MarginLayoutParams(58.dp, 58.dp)
        addView(this)
    }

    val podcastImgBg = ImageView(context).apply {
        layoutParams = MarginLayoutParams(48.dp, 48.dp)
        addView(this)
    }

    val moreImg = ImageView(context).apply {
        layoutParams = MarginLayoutParams(19.dp, 19.dp)
        setImageResource(R.mipmap.fuliimage)
        addView(this)
    }
    val tvPodcastTitle = TextView(context).apply {
        layoutParams = MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textSize = 15f
        setTextColor(ContextCompat.getColor(context, R.color.c_4d97ff))
        maxLines = 2
        ellipsize = TextUtils.TruncateAt.END
        addView(this)
    }
    val tvPodcastAuthor = TextView(context).apply {
        layoutParams = MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textSize = 11f
        setTextColor(ContextCompat.getColor(context, R.color.c_4d97ff))
        maxLines = 1
        setLines(1)
        ellipsize = TextUtils.TruncateAt.END
        addView(this)
    }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        tvRankNum.autoMeasure()
        rankStateImg.autoMeasure()
        tvRankChange.autoMeasure()
        podcastImg.autoMeasure()
        podcastImgBg.autoMeasure()
        moreImg.autoMeasure()
        tvPodcastTitle.autoMeasure()
        tvPodcastAuthor.autoMeasure()
        setMeasuredDimension(widthMeasureSpec, 69.dp)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val centerY = ((bottom - top) * 0.5f + top).toInt()
        layout(
            left,
            centerY - (tvRankNum.measuredHeight + max(
                rankStateImg.measuredHeight,
                tvRankChange.measuredHeight
            )) / 2
        )

        layout(
            tvRankNum.measuredWidth / 2 - (rankStateImg.measuredWidth + 4.dp + tvRankChange.measuredWidth) / 2,
            tvRankNum.bottom
        )

        tvRankChange.let {
            layout(rankStateImg.right + 4.dp, rankStateImg.top)
        }


        podcastImg.let {
            layout(tvRankNum.left, top + 6.dp)
        }

        podcastImgBg.let {
            layout(podcastImg.left + 14.dp, centerY - podcastImgBg.measuredHeight / 2)
        }
        moreImg.let {
            layout(it.measuredWidth + 14.dp, centerY - it.measuredHeight / 2, true)
        }
        tvPodcastTitle.let {
            layout(
                podcastImgBg.left + 10.dp,
                centerY - (tvPodcastTitle.measuredHeight + tvPodcastAuthor.measuredHeight + 5.dp) / 2,
                moreImg.left - 10.dp,
                centerY - (tvPodcastTitle.measuredHeight + tvPodcastAuthor.measuredHeight + 5.dp) / 2 + tvPodcastTitle.measuredHeight
            )
        }
        tvPodcastAuthor.let {
            layout(podcastImgBg.left + 10.dp, tvPodcastTitle.bottom + 5.dp)
        }

    }

}