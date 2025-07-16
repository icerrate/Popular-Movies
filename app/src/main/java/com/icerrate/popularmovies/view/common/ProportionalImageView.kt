package com.icerrate.popularmovies.view.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.roundToInt

/**
 * @author Ivan Cerrate.
 */
class ProportionalImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        
        val finalHeight = when {
            width > height -> (width * HORIZONTAL_ASPECT_RATIO).roundToInt()
            height > width -> (width * VERTICAL_ASPECT_RATIO).roundToInt()
            else -> height
        }
        
        setMeasuredDimension(width, finalHeight)
    }

    companion object {
        private const val HORIZONTAL_ASPECT_RATIO = 0.6f
        private const val VERTICAL_ASPECT_RATIO = 1.5f
    }
}