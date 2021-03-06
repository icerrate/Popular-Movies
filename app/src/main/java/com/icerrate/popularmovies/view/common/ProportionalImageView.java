package com.icerrate.popularmovies.view.common;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author Ivan Cerrate.
 */

public class ProportionalImageView extends AppCompatImageView {

    private static final float HORIZONTAL_ASPECT_RATIO = 0.6f;

    private static final float VERTICAL_ASPECT_RATIO = 1.5f;

    public ProportionalImageView(Context context) {
        super(context);
    }

    public ProportionalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProportionalImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (width > height) {
            height = Math.round(width * HORIZONTAL_ASPECT_RATIO);
        } else if (height > width) {
            height = Math.round(width * VERTICAL_ASPECT_RATIO);
        }
        setMeasuredDimension(width, height);
    }
}