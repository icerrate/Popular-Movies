package com.icerrate.popularmovies.view.common

/**
 * @author Ivan Cerrate.
 */
interface BaseCallback<T> {
    fun onSuccess(result: T)
    fun onFailure(errorMessage: String)
}