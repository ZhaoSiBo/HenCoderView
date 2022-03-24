package com.starts.hencoderview.view

import android.content.Context
import android.graphics.Insets
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.starts.hencoderview.R
import com.starts.hencoderview.dp2px
import com.starts.hencoderview.link.BOTTOM_SHEET_STATE_EXTENDED
import com.starts.hencoderview.link.BottomSheetLayout
import com.starts.hencoderview.link.LinkedScrollView
import kotlin.math.max

/**
 * 豆瓣详情页的框架视图
 *
 * @author https://github.com/funnywolfdadada
 * @since 2020/4/10
 */
class AlbumDetailContainer: FrameLayout {

    public val toolBar: ToolbarView
    public val linkedScrollView: LinkedScrollView
    public val bottomSheetLayout: BottomSheetLayout
    public val topRecyclerView: RecyclerView
    val bottomLayout: FrameLayout
    var bottomScrollViewProvider: (()->View?)? = null
        set(value) {
            linkedScrollView.setBottomView(bottomLayout, value)
            field = value
        }

    var toolbarHeight = context.resources.getDimension(R.dimen.toolbar_height).toInt()
    var minBottomShowingHeight = dp2px(120)
    var midBottomShowingHeight = dp2px(400)
    var expandedOffset = dp2px(56)
    var isBottomViewFloating = false
        private set

    private var topScrolledY = 0F

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        linkedScrollView = LinkedScrollView(context)
        addView(linkedScrollView)
        bottomSheetLayout = BottomSheetLayout(context)
        addView(bottomSheetLayout, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        toolBar = ToolbarView(context)
        addView(toolBar, LayoutParams(MATCH_PARENT, toolbarHeight))

        linkedScrollView.topContainer.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        linkedScrollView.bottomContainer.layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
            topMargin = toolbarHeight
        }

        linkedScrollView.viewTreeObserver.addOnScrollChangedListener {
            updateBottomView()
            updateToolbar()
        }
        bottomSheetLayout.onProcessChangedListener = { updateToolbar() }
        topRecyclerView = RecyclerView(context)
        topRecyclerView.clipToPadding = false
//        topRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                topScrolledY += dy
//                if (topScrolledY < 0) {
//                    topScrolledY = 0F
//                }
//                updateToolbar()
//            }
//        })
        linkedScrollView.setTopView(topRecyclerView) { topRecyclerView }

        bottomLayout = FrameLayout(context)
        linkedScrollView.setBottomView(bottomLayout, bottomScrollViewProvider)
    }

    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        Log.d("onApplyWindowInsets" , "$insets")
        return super.onApplyWindowInsets(insets)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        post { updateBottomView() }
    }

    private fun updateBottomView() {
        val bottomY = linkedScrollView.bottomContainer.y - linkedScrollView.scrollY
        val shouldBottomFloating = bottomY > height - minBottomShowingHeight
        if (shouldBottomFloating && !isBottomViewFloating) {
            isBottomViewFloating = true
            linkedScrollView.removeBottomView()
            bottomSheetLayout.setContentView(
                contentView =  bottomLayout,
                minShowingHeight = minBottomShowingHeight,
                midShowingHeight =  midBottomShowingHeight,
                expandedOffset = expandedOffset)
        } else if (!shouldBottomFloating && isBottomViewFloating) {
            isBottomViewFloating = false
            bottomSheetLayout.removeContentView()
            linkedScrollView.setBottomView(bottomLayout, bottomScrollViewProvider)
        }
    }

    private fun updateToolbar() {
        toolBar.process = if (bottomSheetLayout.state == BOTTOM_SHEET_STATE_EXTENDED) {
            1F
        } else {
            max(topScrolledY, linkedScrollView.scrollY.toFloat()) / toolbarHeight
        }
    }

}