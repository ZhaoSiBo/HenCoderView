package com.starts.hencoderview.view

import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.logging.Handler

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/10/28.
 *版本号：1.0

 */
class PathLayoutManager : RecyclerView.LayoutManager() {
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams( RecyclerView.LayoutParams.WRAP_CONTENT ,  RecyclerView.LayoutParams.WRAP_CONTENT)
    }

    override fun onMeasure(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        widthSpec: Int,
        heightSpec: Int
    ) {
        super.onMeasure(recycler, state, widthSpec, heightSpec)
    }


    override fun isAutoMeasureEnabled(): Boolean {
        return super.isAutoMeasureEnabled()
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
    }

    override fun canScrollHorizontally(): Boolean {
        return super.canScrollHorizontally()
    }

    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically()
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        return super.scrollHorizontallyBy(dx, recycler, state)
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        return super.scrollVerticallyBy(dy, recycler, state)
    }


    override fun scrollToPosition(position: Int) {
        super.scrollToPosition(position)
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?,
        position: Int
    ) {
        super.smoothScrollToPosition(recyclerView, state, position)
    }

}