package com.icerrate.popularmovies.view.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author Ivan Cerrate.
 */

public class ProportionalImageView extends ImageView {

    private static final float ASPECT_RATIO = 0.6f;

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
        int height = Math.round(width * ASPECT_RATIO);
        setMeasuredDimension(width, height);
    }
}