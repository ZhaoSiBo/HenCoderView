package com.starts.hencoderview.link

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.starts.hencoderview.util.isUnder
import com.starts.hencoderview.view.BehavioralScrollView

/**

 *文件描述：.
 *作者：Created by lorizhao on 2022/3/13.
 *版本号：1.0

 */
class BottomBehaviorLayout : BehavioralScrollView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    private val tabLayoutHeight = 45.dp

    val floatTabLayout = TabLayout(context).apply {
        this@BottomBehaviorLayout
            .addView(this,ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , tabLayoutHeight))
    }
    val floatViewPager = ViewPager2(context).apply {
        this@BottomBehaviorLayout
            .addView(this,ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT))
    }

    init {
        setBackgroundColor(Color.GREEN)
    }
    companion object {
        const val POSITION_MIN = 1
        const val POSITION_MID = 2
        const val POSITION_MAX = 3
    }

    private var firstLayout = true

    /**
     * 中间高度 [POSITION_MID] 时 scrollY 的值
     */
    private var midScroll = 0

    /**
     * 初始位置，最低高度 [POSITION_MIN]、中间高度 [POSITION_MID] 或最大高度 [POSITION_MAX]
     */
    private var initPosition: Int = POSITION_MAX

    /**
     * 内容视图的最低显示高度
     */
    private var minContentHeight: Int = 0

    /**
     * 内容视图中间停留的显示高度，默认等于最低高度
     */
    private var midHeight: Int = 0


    fun setup(initPosition: Int = POSITION_MAX, minHeight: Int = 0, midHeight: Int = minHeight) {
        this.initPosition = initPosition
        this.minContentHeight = minHeight
        this.midHeight = midHeight
        firstLayout = true
        requestLayout()
    }

    override fun adjustScrollBounds() {
        minScroll = minContentHeight - height
        // 计算中间高度时的 scrollY
        midScroll = minScroll + midHeight - minContentHeight
        // 第一次 layout 滚动到初始位置
        if (firstLayout) {
            firstLayout = false
            scrollTo(
                scrollX,
                when (initPosition) {
                    POSITION_MIN -> minScroll
                    POSITION_MAX -> maxScroll
                    else -> midScroll
                }
            )
        }
    }

    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        floatTabLayout.autoMeasure()
        floatViewPager.autoMeasure()
        // 没有给下层布局设置可用宽高
        // setMeasuredDimension()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        floatTabLayout.layout(left,0)
        floatViewPager.layout(left,0 + floatTabLayout.measuredHeight)
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun handleDispatchTouchEvent(e: MotionEvent): Boolean? {
        if ((e.action == MotionEvent.ACTION_CANCEL || e.action == MotionEvent.ACTION_UP)
            && lastScrollDir != 0) {
            // 在 up 或 cancel 时，根据当前滚动位置和上次滚动的方向，决定动画的目标位置
            smoothScrollTo(
                if (scrollY > midScroll) {
                    if (lastScrollDir > 0) {
                        maxScroll
                    } else {
                        midScroll
                    }
                } else {
                    if (lastScrollDir > 0) {
                        midScroll
                    } else {
                        minScroll
                    }
                }
            )
            return true
        }
        return super.handleDispatchTouchEvent(e)
    }

    override fun handleTouchEvent(e: MotionEvent): Boolean? {
        // down 事件触点不在 midView 上时不做处理
        return if (e.action == MotionEvent.ACTION_DOWN && getChildAt(0)?.isUnder(e.rawX, e.rawY) != true) {
            false
        } else {
            null
        }
    }

    override fun handleNestedPreScrollFirst(
        scroll: Int,
        @ViewCompat.NestedScrollType type: Int
    ): Boolean? {
        // 只要 contentView 没有完全展开，就在子 View 滚动前处理
        return if (scrollY != 0) {
            true
        } else {
            null
        }
    }

    override fun handleNestedScrollFirst(
        scroll: Int,
        type: Int
    ): Boolean? {
        return true
    }

    override fun handleScrollSelf(
        scroll: Int,
        @ViewCompat.NestedScrollType type: Int
    ): Boolean? {
        // 只允许 touch 类型用于自身的滚动
        return if (type == ViewCompat.TYPE_NON_TOUCH) {
            true
        } else {
            null
        }
    }

}