package com.icerrate.popularmovies.view.common;

import android.content.Context;

/**
 * Created by Ivan Cerrate.
 */

public interface BaseView {

    void setToolbar(String title);
    void showProgressBar();
    void hideProgressBar();
    void hideRefreshLayout();
    void showProgressDialog();
    void showProgressDialog(String message);
    void dismissProgressDialog();
    void showError(String errorMessage);
    Context getContext();
}
