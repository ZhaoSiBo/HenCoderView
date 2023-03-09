package com.starts.hencoderview.container

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.*
import android.widget.Scroller
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.starts.hencoderview.R
import com.starts.hencoderview.util.findFirst
import com.starts.hencoderview.util.isUnder
import java.lang.ref.WeakReference
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.max

/**
 * 顶部 TopRecyclerView 顶部内容
 * 底部 BottomSheetLayout 底部浮窗布局（已经扩展使用ViewPager2支持新增Fragment）
 * MovieDetailContainer 通过 NestedScrollingParent3 处理滑动分发，自己滑动，还是整体滑动
 * 在非悬浮状态下的手动滚动分发，全部在PreNestScroll中处理，预处理逻辑统一，好写好理解，尽量不要分开，不要和OnNestScrolled混合处理
 * 在非悬浮状态下的fling滚动分发，全部在PreNestFling中处理，在PreNestFling中返回true，全部拦截，然后通过scroller，
 * 计算fling，再重写computeScroll来调用dispatchFling来分发fling
 * 底部面板拖拽通过 ViewDragHelper 处理，借鉴 iosched 项目中的升级版 BottomSheetBehavior 代码
 * iosched 中的 BottomSheetBehavior 解决了一些原生 BottomSheetBehavior 的Bug，而且是 Kotlin 代码
 * @see https://github.com/google/iosched/blob/main/mobile/src/main/java/com/google/samples/apps/iosched/widget/BottomSheetBehavior.kt
 * @since 2022/3/22
 */
class MovieDetailContainer : CustomLayout, NestedScrollingParent3 {
    companion object {
        const val TAG = "MovieDetailContainer"

        /** Callback for monitoring events about bottom sheets. */
        interface BottomSheetCallback {
            /**
             * Called when the bottom sheet changes its state.
             *
             * @param bottomSheet The bottom sheet view.
             * @param newState The new state. This will be one of link [STATE_DRAGGING],
             * [STATE_SETTLING], [STATE_EXPANDED], [STATE_COLLAPSED], or
             * [STATE_HALF_EXPANDED].
             */
            fun onStateChanged(bottomSheet: View, newState: Int) {}

            /**
             * Called when the bottom sheet is being dragged.
             *
             * @param bottomSheet The bottom sheet view.
             * @param slideOffset The new offset of this bottom sheet within [-1,1] range. Offset
             * increases as this bottom sheet is moving upward. From 0 to 1 the sheet is between
             * collapsed and expanded states and from -1 to 0 it is between hidden and collapsed states.
             */
            fun onSlide(bottomSheet: View, slideOffset: Float) {}
        }

        /** 底部拖拽状态. （外界不要设置）*/
        const val STATE_DRAGGING = 1

        /** 正在沉淀状态. （外界不要设置）*/
        const val STATE_SETTLING = 2

        /** 展开状态. */
        const val STATE_EXPANDED = 3

        /** 折叠状态. */
        const val STATE_COLLAPSED = 4

        /** 半展开状态. */
        const val STATE_HALF_EXPANDED = 5

        @IntDef(
                value = [
                    STATE_DRAGGING,
                    STATE_SETTLING,
                    STATE_EXPANDED,
                    STATE_COLLAPSED,
                    STATE_HALF_EXPANDED
                ]
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class State

    }
    /** 顶部信息面板 */
    val topRecyclerView: RecyclerView = RecyclerView(context).apply {
        this.layoutParams = LayoutParams(matchParent, matchParent)
        overScrollMode = OVER_SCROLL_NEVER
        this@MovieDetailContainer.addView(this)
    }

    /** 可拖拽和悬浮的面板  内涵TagLayout和 Viewpager2 .*/
    val bottomSheetLayout: BottomSheetLayout = BottomSheetLayout(context).apply {
        this.layoutParams = LayoutParams(matchParent, matchParent)
        this.background = GradientDrawable().apply {
            this.cornerRadii = floatArrayOf(20.dp * 1f,20.dp * 1f,20.dp * 1f,20.dp * 1f,0f,0f,0f,0f)
            this.setColor(ContextCompat.getColor(context,R.color.white))
        }
        this@MovieDetailContainer.addView(this)
    }

    private var viewPage2: ViewPager2 = bottomSheetLayout.bottomViewPager
    private var tabLayout: TabLayout = bottomSheetLayout.bottomTabLayout

    private var initPeekHeight = 180.dp
    /* 窥视高度 */
    var peekHeight = initPeekHeight

    /** 底部拖拽帮助类 */
    private lateinit var dragHelper: ViewDragHelper

    /**
     * 容器 y 轴的最大滚动范围 = 顶部视图高度 + 底部视图高度 - 自身的高度
     */
    var maxScrollY = 0
        private set


    /** 移动事件分发. */
    private var lastTouchX = 0
    private var lastTouchY = 0
    private var initialTouchY = 0
    private var initialTouchX = 0
    private var activePointerId = MotionEvent.INVALID_POINTER_ID
    private var acceptTouches = true

    private var minimumVelocity = 0
    private var maximumVelocity = 0
    private var velocityTracker: VelocityTracker? = null

    /** 控制是否可拖拽. */
    var isDraggable = true
        private set

    /** STATE_COLLAPSED 状态下，距离顶部偏移量. */
    private var collapsedOffset = 0

    /** STATE_HALF_EXPANDED 状态下，距离顶部偏移量. */
    private var halfExpandedOffset = 0

    /** STATE_EXPANDED 状态下，距离顶部偏移量. */
    private var expandedOffset = 0

    //是否直接充满内容  true 表示没有中间态，false 表示有中间
    var isFitToContents = false
        set(value) {
            if (field != value) {
                field = value
                // Fix incorrect expanded settings.
//                setStateInternal(
//                        if (field && state == STATE_HALF_EXPANDED) STATE_EXPANDED else state
//                )
            }
        }

    // 悬浮面板的刷新 RefreshLayout 用来提供歌曲面板 是否下拉加载更多数据的
//    var refreshLayoutRef: WeakReference<RefreshLayout?>? = null

    // 悬浮面板的刷新 RecyclerView
    var bottomRecyclerRef: WeakReference<RecyclerView?>? = null

    /**
     * 处理 fling，通过 scroller 计算 fling，暂存 fling 的初值和需要 fling 的 view
     */
    private val scroller = Scroller(context)

    private var lastFlingY = 0

    private var flingTarget: View? = null

    private val callbacks: MutableSet<BottomSheetCallback> = mutableSetOf()

    private val dragCallback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        // 用来判断是否需要捕获BottomSheetLayout
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            when {
                // Sanity check
                state == STATE_DRAGGING -> return false
                // recapture a settling sheet
                dragHelper.viewDragState == ViewDragHelper.STATE_SETTLING -> return true

                bottomRecyclerRef?.get() == null -> return false
            }

            val dy = lastTouchY - initialTouchY
            if (dy == 0 || scrollY != 0) {
                return false
            }
            //dy 大于0 是下拉面板 小于0 是提高面板
            bottomRecyclerRef?.get()?.let { safeRecycle ->
                if (dy < 0) {
                    //提高面板
                    return true
                } else {
                    //下拉面板 判断是下拉的容器，还是下拉的确实是内部的RecyclerView
                    if (safeRecycle.isUnder(initialTouchX.toFloat(), initialTouchY.toFloat())) {
                        //滑动方向和拖拽方向相反
                        return !safeRecycle.canScrollVertically(-dy)
                    } else {
                        return true
                    }
                }
            }
            return false
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return collapsedOffset - expandedOffset
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            val maxOffset = collapsedOffset
            return top.coerceIn(expandedOffset, maxOffset)
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int) = child.left

        override fun onViewDragStateChanged(state: Int) {
            if (state == ViewDragHelper.STATE_DRAGGING) {
                setStateInternal(STATE_DRAGGING)
            }
        }

        override fun onViewPositionChanged(child: View, left: Int, top: Int, dx: Int, dy: Int) {
            dispatchOnSlide(top)
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            settleBottomSheet(releasedChild, yvel, false)
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val configuration = ViewConfiguration.get(context)
        minimumVelocity = configuration.scaledMinimumFlingVelocity
        maximumVelocity = configuration.scaledMaximumFlingVelocity
    }

    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int):Dimension {
        val topRecyclerViewMinHeight = heightMeasureSpec - initPeekHeight
        topRecyclerView.measure(widthMeasureSpec , topRecyclerViewMinHeight)
        bottomSheetLayout.measure(widthMeasureSpec, heightMeasureSpec)
        return Dimension(widthMeasureSpec,heightMeasureSpec)
    }

    /**
     * 布局时，topContainer 在顶部，bottomContainer 紧挨着 topContainer 底部
     * 布局完还要计算下最大的滚动距离
     * 容器 y 轴的最大滚动范围 = 顶部视图高度 + 底部视图高度 - 自身的高度
     */

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        topRecyclerView.layout(0, 0)
        bottomSheetLayout.layout(0, bottom - height)
        //因为顶部的RecyclerView可能会多次测量才能拿到正确结果，这个时候maxScroll如果不加Max可能会小于0
        maxScrollY = max(0,topRecyclerView.measuredHeight + bottomSheetLayout.measuredHeight - (bottom - top))
        //这里有顺序，半展开的高度要最后算
        expandedOffset = getExpandedOffset()
        collapsedOffset = calculateCollapsedOffset()
        halfExpandedOffset = getHalfExpandedOffset()
        when (state) {
            STATE_EXPANDED -> ViewCompat.offsetTopAndBottom(bottomSheetLayout, expandedOffset)
            STATE_HALF_EXPANDED -> ViewCompat.offsetTopAndBottom(bottomSheetLayout, halfExpandedOffset)
            STATE_COLLAPSED -> ViewCompat.offsetTopAndBottom(bottomSheetLayout, collapsedOffset)
            STATE_DRAGGING, STATE_SETTLING -> ViewCompat.offsetTopAndBottom(
                    bottomSheetLayout, collapsedOffset
            )
        }

        if (!::dragHelper.isInitialized) {
            dragHelper = ViewDragHelper.create(this, dragCallback)
        }
        bottomRecyclerRef = WeakReference(findBottomRecycler())
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            // 手指按下就中止 fling 等滑动行为
            scroller.forceFinished(true)
        }
        return super.dispatchTouchEvent(ev)
    }


    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (!isDraggable || !bottomSheetLayout.isShown) {
            return false
        }
        val action = event.actionMasked
        lastTouchX = event.x.toInt()
        lastTouchY = event.y.toInt()

        // Record velocity
        if (action == MotionEvent.ACTION_DOWN) {
            resetVelocityTracker()
        }
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }
        velocityTracker?.addMovement(event)
        when (action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                activePointerId = MotionEvent.INVALID_POINTER_ID
                return false
            }

            MotionEvent.ACTION_DOWN -> {
                activePointerId = event.getPointerId(event.actionIndex)
                initialTouchY = event.y.toInt()
                initialTouchX = event.x.toInt()
                // is touching the sheet
                acceptTouches = bottomSheetLayout.isUnder(event.rawX, event.rawY)
//                直接收起悬浮面板
                if (!acceptTouches && state != STATE_COLLAPSED) {
                    state = STATE_COLLAPSED
                }
            }
        }
        // 点在可拖拽范围内，并且dragHelper初始化了，tryCaptureView 允许拖拽
        return acceptTouches &&
                ::dragHelper.isInitialized &&
                dragHelper.shouldInterceptTouchEvent(event)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isDraggable || !bottomSheetLayout.isShown) {
            return false
        }

        val action = event.actionMasked
        if (action == MotionEvent.ACTION_DOWN && state == STATE_DRAGGING) {
            return true
        }

        lastTouchX = event.x.toInt()
        lastTouchY = event.y.toInt()

        // Record velocity
        if (action == MotionEvent.ACTION_DOWN) {
            resetVelocityTracker()
        }
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }
        velocityTracker?.addMovement(event)

        if (::dragHelper.isInitialized) {
            dragHelper.processTouchEvent(event)
        }

        if (acceptTouches &&
                action == MotionEvent.ACTION_MOVE &&
                exceedsTouchSlop(initialTouchY, lastTouchY)
        ) {
            dragHelper.captureChildView(bottomSheetLayout, event.getPointerId(event.actionIndex))
        }
        return acceptTouches
    }

    private fun exceedsTouchSlop(p1: Int, p2: Int) = abs(p1 - p2) >= dragHelper.touchSlop

    private fun resetVelocityTracker() {
        activePointerId = MotionEvent.INVALID_POINTER_ID
        velocityTracker?.recycle()
        velocityTracker = null
    }

    /** 内部当前状态 */
    private var _state = STATE_COLLAPSED

    /**  状态用来控制底部浮窗的折叠状态的 ，外部控制，就用这个来处理就行 */
    @State
    var state
        get() = _state
        set(@State value) {
            if (_state == value) {
                return
            }
            //支持中间态的时候不要设置中间态状态
            if(isFitToContents && value == STATE_HALF_EXPANDED){
                return
            }
            _state = value
            bottomSheetLayout.apply {
                // Start the animation; wait until a pending layout if there is one.
                if (parent != null && parent.isLayoutRequested && isAttachedToWindow) {
                    post {
                        startSettlingAnimation(this, value)
                    }
                } else {
                    startSettlingAnimation(this, value)
                }
            }
        }

    /**
     * 滚动范围是[0, [maxScrollY]]，根据方向判断垂直方向是否可以滚动
     */
    override fun canScrollVertically(direction: Int): Boolean {
        return when {
            direction > 0 -> {
                scrollY < maxScrollY
            }
            direction < 0 -> {
                scrollY > 0
            }
            else -> {
                true
            }
        }
    }

    private fun calculateCollapsedOffset(): Int {
        return height - peekHeight
    }

    private fun getExpandedOffset() = 0

    private fun getHalfExpandedOffset() = if (isFitToContents){
        //没有中间态
        0
    }else{
        //有中间态
        (height * 0.5).toInt()
    }

    /**
     * 修改这个比值，不一定会成功，因为要改偏移量，所以要整体考虑当前状态,如果不支持中间态，修改是失效的
     * @see isFitToContents
     */

//    var halfRatio = 0.5f
//        set(value) {
//            if (value in 0f..1f && _state != STATE_HALF_EXPANDED && (height * halfRatio).toInt() > collapsedOffset && !isFitToContents){
//                halfExpandedOffset = (height * halfRatio).toInt()
//                field = value
//            }
//        }

    fun addBottomSheetCallback(callback: BottomSheetCallback) {
        callbacks.add(callback)
    }

    fun removeBottomSheetCallback(callback: BottomSheetCallback) {
        callbacks.remove(callback)
    }

    fun setStateInternal(@State state: Int) {
        if (_state != state) {
            _state = state
            bottomSheetLayout.let { view ->
                callbacks.forEach { callback ->
                    callback.onStateChanged(view, state)
                }
            }
        }
    }

    /**
     * 滚动前做范围限制
     */
    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, when {
            y < 0 -> 0
            y > maxScrollY -> maxScrollY
            else -> y
        })
    }


    private fun findBottomRecycler(): RecyclerView? {
        val layoutManagerFiled = viewPage2::class.java.getDeclaredField("mLayoutManager")
        layoutManagerFiled.isAccessible = true
        val pagerLayoutManager = layoutManagerFiled.get(viewPage2) as LinearLayoutManager
        val currentChild = pagerLayoutManager.findViewByPosition(viewPage2.currentItem)
        return ((currentChild as ViewGroup).findFirst(true) {
            it is RecyclerView
        }) as RecyclerView?
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return (axes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {

    }


    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        flingTarget = target
        triggerFling(velocityY.toInt())
        return true
    }

    override fun onStopNestedScroll(target: View, type: Int) {
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int, consumed: IntArray) {

    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
    }


    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        //滚动前，判断是否是要容器消费一部分
        if (target == topRecyclerView) {
//            //顶部触发
            if (dy > 0) {
                if (!target.canScrollVertically(dy)) {
                    scrollBy(0, dy)
                    consumed[1] = dy
                }
            } else {
                if (canScrollVertically(dy)) {
                    scrollBy(0, dy)
                    consumed[1] = dy
                }
            }
        } else {
            if (state == STATE_COLLAPSED) {
                //底部触发
                if (dy > 0) {
                    if (canScrollVertically(dy)) {
                        scrollBy(0, dy)
                        consumed[1] = dy
                    }
                } else {
                    //判断整体容器是否到底了
                    if (canScrollVertically(1)) {
                        //容器没到底
                        scrollBy(0, dy)
                        consumed[1] = dy
                    } else {
                        //容器到底了 交给自己处理,还是交给容器处理
                        if (target.canScrollVertically(dy)) {
                            //自己处理
                        } else {
                            scrollBy(0, dy)
                            consumed[1] = dy
                        }
                    }
                }
            } else {
                //不用处理

            }

        }
    }


    private fun startSettlingAnimation(child: View, state: Int) {
        val top: Int
        val finalState = state
        when {
            state == STATE_COLLAPSED -> top = collapsedOffset
            state == STATE_EXPANDED -> top = expandedOffset
            state == STATE_HALF_EXPANDED -> {
                top = halfExpandedOffset
            }
            else -> throw IllegalArgumentException("Invalid state: " + state)
        }

        if (dragHelper.smoothSlideViewTo(child, child.left, top)) {
            setStateInternal(STATE_SETTLING)
            ViewCompat.postOnAnimation(child, SettleRunnable(child, finalState))
        } else {
            setStateInternal(finalState)
        }
    }

    fun dispatchOnSlide(top: Int) {
        bottomSheetLayout.let { sheet ->
            val denom = if (top > collapsedOffset) {
                height - collapsedOffset
            } else {
                collapsedOffset - getExpandedOffset()
            }
            callbacks.forEach { callback ->
                callback.onSlide(sheet, (collapsedOffset - top).toFloat() / denom)
            }
        }
    }

    private inner class SettleRunnable(
            private val view: View,
            @State private val state: Int
    ) : Runnable {
        override fun run() {
            if (dragHelper.continueSettling(true)) {
                view.postOnAnimation(this)
            } else {
                setStateInternal(state)
            }
        }
    }

    private fun settleBottomSheet(sheet: View, yVelocity: Float, isNestedScroll: Boolean) {
        val top: Int
        @State val targetState: Int
        val flinging = yVelocity.absoluteValue > minimumVelocity
        if (flinging && yVelocity < 0) {
            // Moving up
            // 向上一个等级进位 ，比如 从 折叠状态进位到半折叠， 半折叠进位到折叠
            if (!isFitToContents) {
                if (sheet.top > halfExpandedOffset) {
                    top = halfExpandedOffset
                    targetState = STATE_HALF_EXPANDED
                } else {
                    top = expandedOffset
                    targetState = STATE_EXPANDED
                }
            } else {
                top = expandedOffset
                targetState = STATE_EXPANDED
            }

        } else if (flinging && yVelocity > 0) {
            // Moving down
            // 向下一个等级进位 ，从展开到半折叠，从半折叠，到折叠
            if (!isFitToContents) {
                if (sheet.top > halfExpandedOffset) {
                    top = collapsedOffset
                    targetState = STATE_COLLAPSED
                } else {
                    top = halfExpandedOffset
                    targetState = STATE_HALF_EXPANDED
                }
            } else {
                top = collapsedOffset
                targetState = STATE_COLLAPSED
            }
        } else {
            // 速度不够，不达到进位标准 ，比如，从半折叠，到展开，这时候速度不够要回到半折叠状态
            val currentTop = sheet.top
            if (!isFitToContents) {
                if (currentTop < halfExpandedOffset) {
                    top = expandedOffset
                    targetState = STATE_EXPANDED
                } else {
                    top = halfExpandedOffset
                    targetState = STATE_HALF_EXPANDED
                }
            } else {
                if (abs(currentTop - expandedOffset)
                        < abs(currentTop - collapsedOffset)
                ) {
                    top = expandedOffset
                    targetState = STATE_EXPANDED
                } else {
                    top = collapsedOffset
                    targetState = STATE_COLLAPSED
                }
            }

        }
        val startedSettling = if (isNestedScroll) {
            dragHelper.smoothSlideViewTo(sheet, sheet.left, top)
        } else {
            dragHelper.settleCapturedViewAt(sheet.left, top)
        }

        if (startedSettling) {
            setStateInternal(STATE_SETTLING)
            ViewCompat.postOnAnimation(sheet, SettleRunnable(sheet, targetState))
        } else {
            setStateInternal(targetState)
        }
    }

    private fun triggerFling(yv: Int) {
        lastFlingY = 0
        scroller.fling(0, lastFlingY, 0, yv, 0, 0, Int.MIN_VALUE, Int.MAX_VALUE)
        invalidate()
    }

    private fun dispatchFling(dScrollY: Int, target: View?) {
        if (dScrollY == 0) return
        if (target == null) return
        if (target == topRecyclerView) {
            if (dScrollY > 0) {
                if (target.canScrollVertically(dScrollY)) {
                    //自己fling
                    target.scrollBy(0, dScrollY)
                } else {
                    //给容器fling和给底部fling
                    if (canScrollVertically(dScrollY)) {
                        scrollBy(0, dScrollY)
                    } else {
                        bottomRecyclerRef?.get()?.scrollBy(0, dScrollY)
                    }
                }
            } else {
                //顶部自己内容fling
                if (canScrollVertically(dScrollY)) {
                    scrollBy(0, dScrollY)
                } else {
                    topRecyclerView.scrollBy(0, dScrollY)
                }
            }
        } else {
            //底部RecyclerView 触发fling 分发
            if (dScrollY > 0) {
                if (state != STATE_EXPANDED) {
                    if (canScrollVertically(dScrollY)) {
                        scrollBy(0, dScrollY)
                    } else {
                        target.scrollBy(0, dScrollY)
                    }
                } else {
                    target.scrollBy(0, dScrollY)
                }
            } else {
                if (!canScrollVertically(1)) {
                    // 到底
                    if (target.canScrollVertically(dScrollY)) {
                        target.scrollBy(0, dScrollY)
                    } else {
                        if (canScrollVertically(dScrollY)) {
                            scrollBy(0, dScrollY)
                        } else {
                            topRecyclerView.scrollBy(0, dScrollY)
                        }
                    }
                } else {
                    // 没到底
                    if (target.canScrollVertically(dScrollY)) {
                        //判断内部能滚动
                        target.scrollBy(0, dScrollY)
                    } else {
                        //判断内部不能滚动
                        if (canScrollVertically(dScrollY)) {
                            scrollBy(0, dScrollY)
                        } else {
                            topRecyclerView.scrollBy(0, dScrollY)
                        }
                    }
                }
            }
        }
    }

    /**
     * 计算 fling 的滚动量，并将其分发到真正需要处理的 view
     */
    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            val currentFlingY = scroller.currY
            val dScrollY = currentFlingY - lastFlingY
            dispatchFling(dScrollY, flingTarget)
            lastFlingY = currentFlingY
            invalidate()
        } else {
            flingTarget = null
        }
    }

}
