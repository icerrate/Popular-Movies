package com.icerrate.popularmovies.view.common;

import android.content.Context;

/**
 * Created by Ivan Cerrate.
 */

public interface BaseView {

    void showProgressBar(boolean show);

    void showRefreshLayout(boolean show);

    void showError(String errorMessage);

    Context getContext();
}
