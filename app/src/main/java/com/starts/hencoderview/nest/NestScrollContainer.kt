package com.starts.hencoderview.nest

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import android.widget.Toolbar
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.customview.widget.ViewDragHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.starts.hencoderview.R
import com.starts.hencoderview.behavior.BottomSheetBehavior
import com.starts.hencoderview.behavior.BottomSheetBehavior.Companion.STATE_COLLAPSED
import com.starts.hencoderview.behavior.BottomSheetBehavior.Companion.STATE_DRAGGING
import com.starts.hencoderview.behavior.BottomSheetBehavior.Companion.STATE_EXPANDED
import com.starts.hencoderview.behavior.BottomSheetBehavior.Companion.STATE_HALF_EXPANDED
import com.starts.hencoderview.behavior.BottomSheetBehavior.Companion.STATE_HIDDEN
import com.starts.hencoderview.behavior.BottomSheetBehavior.Companion.STATE_SETTLING
import com.starts.hencoderview.dp2px
import com.starts.hencoderview.view.CustomLayout
import com.starts.hencoderview.view.matchParent
import com.starts.hencoderview.view.parentView
import com.starts.hencoderview.view.wrapContent
import java.lang.ref.WeakReference
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

/**

 *文件描述：.
 *作者：Created by lorizhao on 2022/3/24.
 *版本号：1.0
 */

class NestScrollContainer : CustomLayout, NestedScrollingParent3 {
    companion object {
        const val TAG = "NestScrollContainer"

        /** 拖拽 */
        const val STATE_DRAGGING = 1

        /** 沉淀？ */
        const val STATE_SETTLING = 2

        /** 展开. */
        const val STATE_EXPANDED = 3

        /** 折叠. */
        const val STATE_COLLAPSED = 4

        /** 半折叠. */
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

    interface BottomSheetCallback {
        /**
         * Called when the bottom sheet changes its state.
         *
         * @param bottomSheet The bottom sheet view.
         * @param newState The new state. This will be one of link [STATE_DRAGGING],
         * [STATE_SETTLING], [STATE_EXPANDED], [STATE_COLLAPSED], [STATE_HIDDEN], or
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


    /** The current state of the bottom sheet, backing property */
    private var _state = BottomSheetBehavior.STATE_COLLAPSED

    /** The current state of the bottom sheet */
    @State
    var state
        get() = _state
        private set(@State value) {
            if (_state == value) {
                return
            }
//            viewRef?.get()?.apply {
//                // Start the animation; wait until a pending layout if there is one.
//                if (parent != null && parent.isLayoutRequested && isAttachedToWindow) {
//                    post {
//                        startSettlingAnimation(this, value)
//                    }
//                } else {
//                    startSettlingAnimation(this, value)
//                }
//            }
        }

    /** Whether the bottom sheet can be dragged or not. */
    var isDraggable = true

    /** Keeps reference to the bottom sheet outside of Behavior callbacks */
    private var viewRef: WeakReference<View>? = null

    private lateinit var dragHelper: ViewDragHelper

    private var _peekHeight = 120.dp

    private var halfExpandedOffset = 400.dp

    private var lastTouchX = 0
    private var lastTouchY = 0
    private var initialTouchY = 0

    private var minimumVelocity = 0
    private var maximumVelocity = 0
    private var velocityTracker: VelocityTracker? = null


    /** Bottom sheet's top offset in [STATE_COLLAPSED] state. */
    private var collapsedOffset = 0

    /** Bottom sheet's top offset in [STATE_EXPANDED] state. */
    private var fitToContentsOffset = 0


    private val callbacks: MutableSet<BottomSheetCallback> = mutableSetOf()

    private val dragCallback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            when {
                // Sanity check
                state == STATE_DRAGGING -> return false
                // recapture a settling sheet
                dragHelper.viewDragState == ViewDragHelper.STATE_SETTLING -> return true
                // let nested scroll handle this
//                nestedScrollingChildRef?.get() != null -> return false
            }

            val dy = lastTouchY - initialTouchY
            if (dy == 0) {
                // ViewDragHelper tries to capture in onTouch for the ACTION_DOWN event, but there's
                // really no way to check for a scrolling child without a direction, so wait.
                return false
            }

            if (state == STATE_COLLAPSED) {
                if (dy < 0) {
                    // Expand on upward movement, even if there's scrolling content underneath
                    return true
                }
            }

            // Check for scrolling content underneath the touch point that can scroll in the
            // appropriate direction.
            val scrollingChild = findScrollingChildUnder(child, lastTouchX, lastTouchY, -dy)
            return scrollingChild == null
        }

        private fun findScrollingChildUnder(view: View, x: Int, y: Int, direction: Int): View? {
            if (view.visibility == View.VISIBLE && dragHelper.isViewUnder(view, x, y)) {
                if (view.canScrollVertically(direction)) {
                    return view
                }
                if (view is ViewGroup) {
                    // TODO this doesn't account for elevation or child drawing order.
                    for (i in (view.childCount - 1) downTo 0) {
                        val child = view.getChildAt(i)
                        val found =
                            findScrollingChildUnder(child, x - child.left, y - child.top, direction)
                        if (found != null) {
                            return found
                        }
                    }
                }
            }
            return null
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return collapsedOffset
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return top.coerceIn(getExpandedOffset(), collapsedOffset)
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int) = child.left

        override fun onViewDragStateChanged(state: Int) {
//            if (state == ViewDragHelper.STATE_DRAGGING) {
//                setStateInternal(BottomSheetBehavior.STATE_DRAGGING)
//            }
        }

        override fun onViewPositionChanged(child: View, left: Int, top: Int, dx: Int, dy: Int) {
//            dispatchOnSlide(top)
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            settleBottomSheet(releasedChild, yvel, false)
        }
    }

    val titleBar = Toolbar(context).apply {
        title = "容器"
        this.layoutParams = LayoutParams(matchParent, wrapContent)
        this.setPadding(0,7.dp,0,7.dp)
        this@NestScrollContainer.addView(this)
    }

    val topRecyclerView = RecyclerView(context).apply {
        this.layoutParams =
            LayoutParams(matchParent, matchParent)
        this@NestScrollContainer.addView(this)
    }
    val floatView = NestChildContainer(context).apply {
        this.layoutParams =
            LayoutParams(matchParent, matchParent)
        this@NestScrollContainer.addView(this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        titleBar.autoMeasure()
        topRecyclerView.autoMeasure()
        floatView.measure(widthMeasureSpec ,(MeasureSpec.getSize(heightMeasureSpec) - titleBar.measuredHeight).toExactlyMeasureSpec() )
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        titleBar.layout(0,0)
        topRecyclerView.layout(0, 0)
        floatView.layout(l, b - _peekHeight)
        if (!::dragHelper.isInitialized) {
            dragHelper = ViewDragHelper.create(this, dragCallback)
        }
        collapsedOffset = calculateCollapsedOffset()

    }

    private fun calculateCollapsedOffset(): Int {
        return height - _peekHeight
    }

    private fun getExpandedOffset() =  0


    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {

    }

    override fun onStopNestedScroll(target: View, type: Int) {

    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
    }


    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {

    }

    //dy 代表 target的意图滑动多少， > 0 表示看下面的内容， < 0 表示看上面的内容，consumed 表示消耗的
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        Log.d(TAG, "onNestedPreScroll dy = ${dy} ,consumed = ${consumed[1]}")
    }


    private fun settleBottomSheet(sheet: View, yVelocity: Float, isNestedScroll: Boolean) {
        val top: Int
        @State val targetState: Int

        val flinging = yVelocity.absoluteValue > minimumVelocity
        if (flinging && yVelocity < 0) { // Moving up
//            if (isFitToContents) {
//                top = fitToContentsOffset
//                targetState = BottomSheetBehavior.STATE_EXPANDED
//            } else {
                if (sheet.top > halfExpandedOffset) {
                    top = halfExpandedOffset
                    targetState = BottomSheetBehavior.STATE_HALF_EXPANDED
                } else {
                    top = 0
                    targetState = BottomSheetBehavior.STATE_EXPANDED
                }
//            }
        }
//        else if (isHideable && shouldHide(sheet, yVelocity)) {
//            top = parentHeight
//            targetState = STATE_HIDDEN
//        }
        else if (flinging && yVelocity > 0) { // Moving down
            top = collapsedOffset
            targetState = STATE_COLLAPSED
        } else {
            val currentTop = sheet.top
//            if (isFitToContents) {
//                if (Math.abs(currentTop - fitToContentsOffset)
//                    < Math.abs(currentTop - collapsedOffset)
//                ) {
//                    top = fitToContentsOffset
//                    targetState = BottomSheetBehavior.STATE_EXPANDED
//                } else {
//                    top = collapsedOffset
//                    targetState = BottomSheetBehavior.STATE_COLLAPSED
//                }
//            } else {
                if (currentTop < halfExpandedOffset) {
                    if (currentTop < Math.abs(currentTop - collapsedOffset)) {
                        top = 0
                        targetState = BottomSheetBehavior.STATE_EXPANDED
                    } else {
                        top = halfExpandedOffset
                        targetState = BottomSheetBehavior.STATE_HALF_EXPANDED
                    }
                } else {
                    if (Math.abs(currentTop - halfExpandedOffset)
                        < Math.abs(currentTop - collapsedOffset)
                    ) {
                        top = halfExpandedOffset
                        targetState = BottomSheetBehavior.STATE_HALF_EXPANDED
                    } else {
                        top = collapsedOffset
                        targetState = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
//            }
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


    fun setStateInternal(@State state: Int) {
        if (_state != state) {
            _state = state
//            viewRef?.get()?.let { view ->
//                callbacks.forEach { callback ->
//                    callback.onStateChanged(view, state)
//                }
//            }
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



//    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
//        if (!isDraggable || !child.isShown) {
//            acceptTouches = false
//            return false
//        }
//
//        val action = event.actionMasked
//        lastTouchX = event.x.toInt()
//        lastTouchY = event.y.toInt()
//
//        // Record velocity
//        if (action == MotionEvent.ACTION_DOWN) {
//            resetVelocityTracker()
//        }
//        if (velocityTracker == null) {
//            velocityTracker = VelocityTracker.obtain()
//        }
//        velocityTracker?.addMovement(event)
//
//        when (action) {
//            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//                activePointerId = MotionEvent.INVALID_POINTER_ID
//                if (!acceptTouches) {
//                    acceptTouches = true
//                    return false
//                }
//            }
//
//            MotionEvent.ACTION_DOWN -> {
//                activePointerId = event.getPointerId(event.actionIndex)
//                initialTouchY = event.y.toInt()
//
//                clearNestedScroll()
//
//                if (!parent.isPointInChildBounds(child, lastTouchX, initialTouchY)) {
//                    // Not touching the sheet
//                    acceptTouches = false
//                }
//            }
//        }
//        val result = acceptTouches &&
//                // CoordinatorLayout can call us before the view is laid out. >_<
//                ::dragHelper.isInitialized &&
//                dragHelper.shouldInterceptTouchEvent(event)
//        Log.d(BottomSheetBehavior.TAG, "onInterceptTouchEvent result = $result")
//        return result
//    }

}
