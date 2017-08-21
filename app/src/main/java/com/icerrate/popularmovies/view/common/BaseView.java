package com.icerrate.popularmovies.view.common;

import android.content.Context;

/**
 * Created by Ivan Cerrate.
 */

public interface BaseView {

    void showProgressBar();
    void hideProgressBar();
    void showRefreshLayout();
    void hideRefreshLayout();
    void showError(String errorMessage);
    Context getContext();
}
