package com.icerrate.popularmovies.utils

import android.app.Activity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import android.view.Display
import android.view.Surface
import android.view.View
import android.view.WindowManager
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowCompat
import android.os.Build
import com.icerrate.popularmovies.R

/**
 * @author Ivan Cerrate.
 */
object ViewUtils {

    private const val DEFAULT_COLUMNS = 2
    private const val HORIZONTAL_COLUMNS = 4

    fun createSnackbar(parent: View, message: String, duration: Int): Snackbar {
        return Snackbar.make(parent, message, duration)
            .setActionTextColor(ContextCompat.getColor(parent.context, R.color.colorPrimary))
    }

    fun createSnackbar(parent: View, resId: Int, duration: Int): Snackbar {
        return Snackbar.make(parent, resId, duration)
            .setActionTextColor(ContextCompat.getColor(parent.context, R.color.colorPrimary))
    }

    fun getGridColumns(activity: Activity?): Int {
        return if (activity?.windowManager != null) {
            val rotation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                activity.display?.rotation ?: Surface.ROTATION_0
            } else {
                @Suppress("DEPRECATION")
                activity.windowManager.defaultDisplay.rotation
            }
            if (rotation == Surface.ROTATION_0) {
                DEFAULT_COLUMNS
            } else {
                HORIZONTAL_COLUMNS
            }
        } else {
            DEFAULT_COLUMNS
        }
    }
}