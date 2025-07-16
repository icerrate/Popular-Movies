package com.icerrate.popularmovies.view.common

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Ivan Cerrate.
 */
abstract class EndlessRecyclerOnScrollListener(
    private val mGridLayoutManager: GridLayoutManager
) : RecyclerView.OnScrollListener() {

    private var loading = false
    private val visibleThreshold = 0
    private var firstVisibleItem: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy < 0) return
        // check for scroll down
        visibleItemCount = recyclerView.childCount
        totalItemCount = mGridLayoutManager.itemCount
        firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition()

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached, Do something
            onLoadMore()
            loading = true
        }
    }

    fun setLoading(loading: Boolean) {
        this.loading = loading
    }

    abstract fun onLoadMore()
}