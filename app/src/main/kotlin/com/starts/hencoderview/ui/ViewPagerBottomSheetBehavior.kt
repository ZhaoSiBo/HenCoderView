//package com.starts.hencoderview.ui;
//
//import android.R.attr
//import android.R.attr.*
//import android.content.Context
//import android.util.AttributeSet
//import android.util.Log
//import android.view.*
//import androidx.annotation.IntDef
//import androidx.coordinatorlayout.widget.CoordinatorLayout
//import androidx.core.view.ViewCompat
//import androidx.customview.widget.ViewDragHelper
//import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
//import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HALF_EXPANDED
//import java.lang.ref.WeakReference
//import kotlin.math.absoluteValue
//import kotlin.math.max
//
//
//class ViewPagerBottomSheetBehavior<V : View> :
//    CoordinatorLayout.Behavior<V> {
//
//    constructor() : super()
//    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        val configuration = ViewConfiguration.get(context)
//        minimumVelocity = configuration.scaledMinimumFlingVelocity
//        maximumVelocity = configuration.scaledMaximumFlingVelocity
//    }
//
//
//    companion object {
//        /** The bottom sheet is dragging. */
//        const val STATE_DRAGGING = 1
//
//        /** The bottom sheet is settling. */
//        const val STATE_SETTLING = 2
//
//        /** The bottom sheet is expanded. */
//        const val STATE_EXPANDED = 3
//
//        /** The bottom sheet is collapsed. */
//        const val STATE_COLLAPSED = 4
//
//        /** The bottom sheet is hidden. */
//        const val STATE_HIDDEN = 5
//
//        /** The bottom sheet is half-expanded (used when behavior_fitToContents is false). */
//        const val STATE_HALF_EXPANDED = 6
//
//        /**
//         * Peek at the 16:9 ratio keyline of its parent. This can be used as a parameter for
//         * [setPeekHeight(Int)]. [getPeekHeight()] will return this when the value is set.
//         */
//        const val PEEK_HEIGHT_AUTO = -1
//
//        private const val HIDE_THRESHOLD = 0.5f
//        private const val HIDE_FRICTION = 0.1f
//
//        @IntDef(
//            value = [
//                STATE_DRAGGING,
//                STATE_SETTLING,
//                STATE_EXPANDED,
//                STATE_COLLAPSED,
//                STATE_HIDDEN,
//                STATE_HALF_EXPANDED
//            ]
//        )
//        @Retention(AnnotationRetention.SOURCE)
//        annotation class State
//
//        /** Utility to get the [BottomSheetBehavior] from a [view]. */
//        @JvmStatic
//        fun from(view: View): ViewPagerBottomSheetBehavior<*> {
//            val lp = view.layoutParams as? CoordinatorLayout.LayoutParams
//                ?: throw IllegalArgumentException("view is not a child of CoordinatorLayout")
//            return lp.behavior as? ViewPagerBottomSheetBehavior
//                ?: throw IllegalArgumentException("view not associated with this behavior")
//        }
//    }
//
//    /** Callback for monitoring events about bottom sheets. */
//    interface BottomSheetCallback {
//        /**
//         * Called when the bottom sheet changes its state.
//         *
//         * @param bottomSheet The bottom sheet view.
//         * @param newState The new state. This will be one of link [STATE_DRAGGING],
//         * [STATE_SETTLING], [STATE_EXPANDED], [STATE_COLLAPSED], [STATE_HIDDEN], or
//         * [STATE_HALF_EXPANDED].
//         */
//        fun onStateChanged(bottomSheet: View, newState: Int) {}
//
//        /**
//         * Called when the bottom sheet is being dragged.
//         *
//         * @param bottomSheet The bottom sheet view.
//         * @param slideOffset The new offset of this bottom sheet within [-1,1] range. Offset
//         * increases as this bottom sheet is moving upward. From 0 to 1 the sheet is between
//         * collapsed and expanded states and from -1 to 0 it is between hidden and collapsed states.
//         */
//        fun onSlide(bottomSheet: View, slideOffset: Float) {}
//    }
//
//    private var nestedScrollingChildRef: WeakReference<View>? = null
//
//    /** Keeps reference to the bottom sheet outside of Behavior callbacks */
//    private var viewRef: WeakReference<View>? = null
//
//    // 子布局是否嵌套滑动
//    private var childNeedNestedScroll = false
//
//    /** Bottom sheet's top offset in [STATE_EXPANDED] state. */
//    private var fitToContentsOffset = 0
//
//    /** Bottom sheet's top offset in [STATE_HALF_EXPANDED] state. */
//    private var halfExpandedOffset = 0
//
//    /** Bottom sheet's top offset in [STATE_COLLAPSED] state. */
//    private var collapsedOffset = 0
//    private var parentHeight: Int = 0
//    private var lastPeekHeight: Int = 0
//
//    // Touch event handling, etc
//    private var lastTouchX = 0
//    private var lastTouchY = 0
//    private var initialTouchY = 0
//
//    private var minimumVelocity = 0
//    private var maximumVelocity = 0
//    private var velocityTracker: VelocityTracker? = null
//
//    /** Whether the bottom sheet should skip collapsed state after being expanded once. */
//    var skipCollapsed = false
//
//    /** Controls movement of the bottom sheet */
//    private lateinit var dragHelper: ViewDragHelper
//
//    private val callbacks: MutableSet<BottomSheetCallback> = mutableSetOf()
//
//    /** Whether the bottom sheet can be hidden. */
//    var isHideable = false
//        set(value) {
//            if (field != value) {
//                field = value
//                if (!value && state == STATE_HIDDEN) {
//                    // Fix invalid state by moving to collapsed
//                    state = STATE_COLLAPSED
//                }
//            }
//        }
//
//    /** Whether to fit to contents. If false, the behavior will include [STATE_HALF_EXPANDED]. */
//    var isFitToContents = true
//        set(value) {
//            if (field != value) {
//                field = value
//                // If sheet is already laid out, recalculate the collapsed offset.
//                // Otherwise onLayoutChild will handle this later.
//                if (viewRef != null) {
//                    collapsedOffset = calculateCollapsedOffset()
//                }
//                // Fix incorrect expanded settings.
//                setStateInternal(
//                    if (field && state == STATE_HALF_EXPANDED) STATE_EXPANDED else state
//                )
//            }
//        }
//
//    /** The current state of the bottom sheet, backing property */
//    private var _state = STATE_COLLAPSED
//
//    /** The current state of the bottom sheet */
//    @State
//    var state
//        get() = _state
//        set(@State value) {
//            if (_state == value) {
//                return
//            }
//            if (viewRef == null) {
//                // Child is not laid out yet. Set our state and let onLayoutChild() handle it later.
//                if (value == STATE_COLLAPSED ||
//                    value == STATE_EXPANDED ||
//                    value == STATE_HALF_EXPANDED ||
//                    (isHideable && value == STATE_HIDDEN)
//                ) {
//                    _state = value
//                }
//                return
//            }
//
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
//        }
//
//    override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
//        if (ViewCompat.getFitsSystemWindows(parent) && !ViewCompat.getFitsSystemWindows(child)
//        ) {
//            child.fitsSystemWindows = true
//        }
//        val savedTop = child.top
//        // First let the parent lay it out
//        // First let the parent lay it out
//        parent.onLayoutChild(child, attr.layoutDirection)
//        // Offset the bottom sheet
//        // Offset the bottom sheet
//        parentHeight = parent.height
//        lastPeekHeight = 80
//
//        fitToContentsOffset = max(0, parentHeight - child.height)
//        halfExpandedOffset = parentHeight / 2
//        calculateCollapsedOffset()
//
//        // Offset the bottom sheet
//        when (state) {
//            STATE_EXPANDED -> ViewCompat.offsetTopAndBottom(child, getExpandedOffset())
//            STATE_HALF_EXPANDED -> ViewCompat.offsetTopAndBottom(child, halfExpandedOffset)
//            STATE_HIDDEN -> ViewCompat.offsetTopAndBottom(child, parentHeight)
//            STATE_COLLAPSED -> ViewCompat.offsetTopAndBottom(child, collapsedOffset)
//            STATE_DRAGGING, STATE_SETTLING -> ViewCompat.offsetTopAndBottom(
//                child, savedTop - child.top
//            )
//        }
//
//        if (!::dragHelper.isInitialized) {
//            dragHelper = ViewDragHelper.create(parent, dragCallback)
//        }
//        viewRef = WeakReference(child)
//        return true
//    }
//
//    private fun getExpandedOffset() = if (isFitToContents) fitToContentsOffset else 0
//
//    override fun onStartNestedScroll(
//        coordinatorLayout: CoordinatorLayout,
//        child: V,
//        directTargetChild: View,
//        target: View,
//        axes: Int,
//        type: Int
//    ): Boolean {
//        nestedScrollingChildRef = WeakReference(target)
//        Log.i("test", "onStartNestedScroll : $childNeedNestedScroll")
//        if (!childNeedNestedScroll) {
//            return false
//        }
//        return super.onStartNestedScroll(
//            coordinatorLayout,
//            child,
//            directTargetChild,
//            target,
//            axes,
//            type
//        )
//    }
//
//    private fun startSettlingAnimation(child: View, state: Int) {
//    }
//
//    private fun calculateCollapsedOffset(): Int {
//        return if (isFitToContents) {
//            max(parentHeight - lastPeekHeight, fitToContentsOffset)
//        } else {
//            parentHeight - lastPeekHeight
//        }
//    }
//
//    override fun onInterceptTouchEvent(
//        parent: CoordinatorLayout,
//        child: V,
//        event: MotionEvent
//    ): Boolean {
//        childNeedNestedScroll = event.y > child.top
//        Log.i("test", "event.y < child.top : ${event.y < child.top}")
//
//        val onInterceptTouchEvent = super.onInterceptTouchEvent(parent, child, event)
//        Log.i(
//            "test", "onInterceptTouchEvent : $onInterceptTouchEvent event : ${event.action} " +
//                    "x: ${event.x} y: ${event.y}"
//        )
//        Log.i("test", "childTop : ${child.top}")
//        return onInterceptTouchEvent
//    }
//
//    override fun onTouchEvent(parent: CoordinatorLayout, child: V, event: MotionEvent): Boolean {
//        val onTouchEvent = super.onTouchEvent(parent, child, event)
//        Log.i("test", "onTouchEvent : $onTouchEvent event : ${event.action}")
//        Log.i("test", "childTop : ${child.top}")
//        return onTouchEvent
//    }
//
//    fun addBottomSheetCallback(callback: BottomSheetCallback) {
//        callbacks.add(callback)
//    }
//
//    fun removeBottomSheetCallback(callback: BottomSheetCallback) {
//        callbacks.remove(callback)
//    }
//
//    private fun setStateInternal(@State state: Int) {
//        if (_state != state) {
//            _state = state
//            viewRef?.get()?.let { view ->
//                callbacks.forEach { callback ->
//                    callback.onStateChanged(view, state)
//                }
//            }
//        }
//    }
//
//    private fun dispatchOnSlide(top: Int) {
//        viewRef?.get()?.let { sheet ->
//            val denom = if (top > collapsedOffset) {
//                parentHeight - collapsedOffset
//            } else {
//                collapsedOffset - getExpandedOffset()
//            }
//            callbacks.forEach { callback ->
//                callback.onSlide(sheet, (collapsedOffset - top).toFloat() / denom)
//            }
//        }
//    }
//
//    private fun shouldHide(child: View, yVelocity: Float): Boolean {
//        if (skipCollapsed) {
//            return true
//        }
//        if (child.top < collapsedOffset) {
//            return false // it should not hide, but collapse.
//        }
//        val newTop = child.top + yVelocity * HIDE_FRICTION
//        return Math.abs(newTop - collapsedOffset) / _peekHeight.toFloat() > HIDE_THRESHOLD
//    }
//
//    private fun settleBottomSheet(sheet: View, yVelocity: Float, isNestedScroll: Boolean) {
//        val top: Int
//        @State val targetState: Int
//
//        val flinging = yVelocity.absoluteValue > minimumVelocity
//        if (flinging && yVelocity < 0) { // Moving up
//            if (isFitToContents) {
//                top = fitToContentsOffset
//                targetState = STATE_EXPANDED
//            } else {
//                if (sheet.top > halfExpandedOffset) {
//                    top = halfExpandedOffset
//                    targetState = STATE_HALF_EXPANDED
//                } else {
//                    top = 0
//                    targetState = STATE_EXPANDED
//                }
//            }
//        } else if (isHideable && shouldHide(sheet, yVelocity)) {
//            top = parentHeight
//            targetState = STATE_HIDDEN
//        } else if (flinging && yVelocity > 0) { // Moving down
//            top = collapsedOffset
//            targetState = STATE_COLLAPSED
//        } else {
//            val currentTop = sheet.top
//            if (isFitToContents) {
//                if (Math.abs(currentTop - fitToContentsOffset)
//                    < Math.abs(currentTop - collapsedOffset)
//                ) {
//                    top = fitToContentsOffset
//                    targetState = STATE_EXPANDED
//                } else {
//                    top = collapsedOffset
//                    targetState = STATE_COLLAPSED
//                }
//            } else {
//                if (currentTop < halfExpandedOffset) {
//                    if (currentTop < Math.abs(currentTop - collapsedOffset)) {
//                        top = 0
//                        targetState = STATE_EXPANDED
//                    } else {
//                        top = halfExpandedOffset
//                        targetState = STATE_HALF_EXPANDED
//                    }
//                } else {
//                    if (Math.abs(currentTop - halfExpandedOffset)
//                        < Math.abs(currentTop - collapsedOffset)
//                    ) {
//                        top = halfExpandedOffset
//                        targetState = STATE_HALF_EXPANDED
//                    } else {
//                        top = collapsedOffset
//                        targetState = STATE_COLLAPSED
//                    }
//                }
//            }
//        }
//
//        val startedSettling = if (isNestedScroll) {
//            dragHelper.smoothSlideViewTo(sheet, sheet.left, top)
//        } else {
//            dragHelper.settleCapturedViewAt(sheet.left, top)
//        }
//
//        if (startedSettling) {
//            setStateInternal(STATE_SETTLING)
//            ViewCompat.postOnAnimation(sheet, SettleRunnable(sheet, targetState))
//        } else {
//            setStateInternal(targetState)
//        }
//    }
//
//    private inner class SettleRunnable(
//        private val view: View,
//        @State private val state: Int
//    ) : Runnable {
//        override fun run() {
//            if (dragHelper.continueSettling(true)) {
//                view.postOnAnimation(this)
//            } else {
//                setStateInternal(state)
//            }
//        }
//    }
//
//    private val dragCallback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
//
//        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
//            when {
//                // Sanity check
//                state == STATE_DRAGGING -> return false
//                // recapture a settling sheet
//                dragHelper.viewDragState == ViewDragHelper.STATE_SETTLING -> return true
//                // let nested scroll handle this
//                nestedScrollingChildRef?.get() != null -> return false
//            }
//
//            val dy = lastTouchY - initialTouchY
//            if (dy == 0) {
//                // ViewDragHelper tries to capture in onTouch for the ACTION_DOWN event, but there's
//                // really no way to check for a scrolling child without a direction, so wait.
//                return false
//            }
//
//            if (state == STATE_COLLAPSED) {
//                if (isHideable) {
//                    // Any drag should capture in order to expand or hide the sheet
//                    return true
//                }
//                if (dy < 0) {
//                    // Expand on upward movement, even if there's scrolling content underneath
//                    return true
//                }
//            }
//
//            // Check for scrolling content underneath the touch point that can scroll in the
//            // appropriate direction.
//            val scrollingChild = findScrollingChildUnder(child, lastTouchX, lastTouchY, -dy)
//            return scrollingChild == null
//        }
//
//        private fun findScrollingChildUnder(view: View, x: Int, y: Int, direction: Int): View? {
//            if (view.visibility == View.VISIBLE && dragHelper.isViewUnder(view, x, y)) {
//                if (view.canScrollVertically(direction)) {
//                    return view
//                }
//                if (view is ViewGroup) {
//                    // TODO this doesn't account for elevation or child drawing order.
//                    for (i in (view.childCount - 1) downTo 0) {
//                        val child = view.getChildAt(i)
//                        val found =
//                            findScrollingChildUnder(child, x - child.left, y - child.top, direction)
//                        if (found != null) {
//                            return found
//                        }
//                    }
//                }
//            }
//            return null
//        }
//
//        override fun getViewVerticalDragRange(child: View): Int {
//            return if (isHideable) parentHeight else collapsedOffset
//        }
//
//        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
//            val maxOffset = if (isHideable) parentHeight else collapsedOffset
//            return top.coerceIn(getExpandedOffset(), maxOffset)
//        }
//
//        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int) = child.left
//
//        override fun onViewDragStateChanged(state: Int) {
//            if (state == ViewDragHelper.STATE_DRAGGING) {
//                setStateInternal(STATE_DRAGGING)
//            }
//        }
//
//        override fun onViewPositionChanged(child: View, left: Int, top: Int, dx: Int, dy: Int) {
//            dispatchOnSlide(top)
//        }
//
//        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
//            settleBottomSheet(releasedChild, yvel, false)
//        }
//    }
//}