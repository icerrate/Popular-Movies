package com.icerrate.popularmovies.view.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Ivan Cerrate.
 */
class BaseItemDecoration : RecyclerView.ItemDecoration {

    private val mDivider: Drawable?

    constructor(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.listDivider))
        mDivider = a.getDrawable(0)
        a.recycle()
    }

    constructor(divider: Drawable?) {
        mDivider = divider
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (mDivider == null) return
        if (parent.getChildAdapterPosition(view) < 1) return

        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            outRect.top = mDivider.intrinsicHeight
        } else {
            outRect.left = mDivider.intrinsicWidth
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mDivider == null) {
            super.onDrawOver(c, parent, state)
            return
        }

        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            val childCount = parent.childCount

            for (i in 1 until childCount) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val size = mDivider.intrinsicHeight
                val top = child.top - params.topMargin
                val bottom = top + size
                mDivider.setBounds(left, top, right, bottom)
                mDivider.draw(c)
            }
        } else { // horizontal
            val top = parent.paddingTop
            val bottom = parent.height - parent.paddingBottom
            val childCount = parent.childCount

            for (i in 1 until childCount) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val size = mDivider.intrinsicWidth
                val left = child.left - params.leftMargin
                val right = left + size
                mDivider.setBounds(left, top, right, bottom)
                mDivider.draw(c)
            }
        }
    }

    private fun getOrientation(parent: RecyclerView): Int {
        return if (parent.layoutManager is LinearLayoutManager) {
            (parent.layoutManager as LinearLayoutManager).orientation
        } else {
            throw IllegalStateException("DividerItemDecoration can only be used with a LinearLayoutManager.")
        }
    }
}