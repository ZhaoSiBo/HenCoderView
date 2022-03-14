package com.starts.hencoderview.link

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.starts.hencoderview.R
import com.starts.hencoderview.util.isScrollChildTotalShowing
import com.starts.hencoderview.view.BehavioralScrollView

/**

 *文件描述：.
 *作者：Created by lorizhao on 2022/3/13.
 *版本号：1.0

 */
class NestScrollContainerLayout : BehavioralScrollView {

    companion object {
        const val TAG = "NestScrollContainer"
    }

    //歌曲信息面板
    val rvTopInfo: RecyclerView = RecyclerView(context).apply {
        this@NestScrollContainerLayout.addView(
            this,
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )
    }

    //歌曲底部面板
    val bottomSheetLayout = BottomBehaviorLayout(context).apply {
        this@NestScrollContainerLayout.addView(
            this,
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )
    }


    //窥视高度
    var peekHeight = 120.dp

    // 半展开高度（屏幕底下是0 。顶部是最高）
    var halfExpandedHeight = 360.dp

    // 全展开距离顶部的偏移量，0是和顶部重合，越大离顶部越远
    var behaviorExpandedOffset = 45.dp

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.NestScrollContainerLayout)
        peekHeight = a.getDimensionPixelSize(
            R.styleable.NestScrollContainerLayout_peekHeight, 0
        )

        halfExpandedHeight = a.getDimensionPixelSize(
            R.styleable.NestScrollContainerLayout_halfExpandedHeight, 0
        )
        behaviorExpandedOffset =
            a.getDimensionPixelSize(
                R.styleable.NestScrollContainerLayout_behavior_expandedOffset, 0
            )
        a.recycle()

    }

    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        rvTopInfo.measure(widthMeasureSpec,(MeasureSpec.getSize(heightMeasureSpec) - peekHeight).toExactlyMeasureSpec())
        bottomSheetLayout.autoMeasure()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        rvTopInfo.layout(left, top)
        bottomSheetLayout.setup(peekHeight , halfExpandedHeight , behaviorExpandedOffset)
        bottomSheetLayout.layout(left, measuredHeight - peekHeight)
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun adjustScrollBounds() {
        minScroll = 0
        maxScroll = this.measuredHeight - peekHeight
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        // 为了能够将滚动传递下去，需要把 fling 拦截下来
        fling(velocityY)
        return true
    }
//

    override fun handleNestedPreScrollFirst(scroll: Int, type: Int): Boolean? {
        return if (isScrollChildTotalShowing()) {
            null
        } else {
            true
        }
    }

    override fun handleNestedScrollFirst(scroll: Int, type: Int): Boolean? {
        return true
    }
//
    override fun handleScrollSelf(scroll: Int, type: Int): Boolean? {
        bottomSheetLayout.isFloat = false
        return if (type == ViewCompat.TYPE_TOUCH) {
            handleDrag(scroll)
        } else {
            handleFling(scroll)
        }
    }
//
    private fun handleDrag(scroll: Int): Boolean? {
        scrollBy(0, if (scrollY > 0) { scroll } else { scroll / 2 })
        return true
    }
//
    private fun handleFling(scroll: Int): Boolean? {
        if (isScrollChildTotalShowing() && nestedScrollTarget?.canScrollVertically(scroll) == true) {
            nestedScrollTarget?.scrollBy(0, scroll)
            return true
        }
        // 自己可以滚动时，默认处理
        if (canScrollVertically(scroll)) {
            return null
        }
        // 自己无法滚动时根据方向确定滚动传递的目标
        val target:View = if (scroll < 0) {
            rvTopInfo
        } else {
            bottomSheetLayout
        }
        if (target.canScrollVertically(scroll)) {
            target.scrollBy(0, scroll)
            return true
        }
        return false
    }

}