package com.icerrate.popularmovies.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.icerrate.popularmovies.R;

/**
 * @author Ivan Cerrate.
 */

public class ViewUtils {

    private final static int DEFAULT_COLUMNS = 2;

    private final static int HORIZONTAL_COLUMNS = 4;

    public static Snackbar createSnackbar(View parent, String message, int duration) {
        return Snackbar.make(parent, message, duration)
                .setActionTextColor(parent.getResources().getColor(R.color.colorPrimary));
    }

    public static Snackbar createSnackbar(View parent, int resId, int duration) {
        return Snackbar.make(parent, resId, duration)
                .setActionTextColor(parent.getResources().getColor(R.color.colorPrimary));
    }

    public static int getGridColumns(Activity activity) {
        if (activity != null && activity.getWindowManager() != null) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            if (display.getRotation() == Surface.ROTATION_0) {
                return DEFAULT_COLUMNS;
            } else {
                return HORIZONTAL_COLUMNS;
            }
        }
        return DEFAULT_COLUMNS;
    }
}
