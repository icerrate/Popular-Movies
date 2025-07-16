package com.icerrate.popularmovies.view.common

/**
 * @author Ivan Cerrate.
 */
interface BaseView {
    fun showError(errorMessage: String)
    fun showSnackbarMessage(resId: Int)
}