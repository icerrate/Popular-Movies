package com.icerrate.popularmovies.view.common;

/**
 * @author Ivan Cerrate.
 */

public interface BaseView {

    void showProgressBar(boolean show);

    void showRefreshLayout(boolean show);

    void showError(String errorMessage);

    void showSnackbarMessage(int resId);
}
