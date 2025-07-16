package com.icerrate.popularmovies.view.movies.detail

import android.view.View

/**
 * @author Ivan Cerrate.
 */
interface MovieDetailFragmentListener {

    fun setCollapsingTitle(title: String)

    fun setBackdropImage(backdropUrl: String)

    fun setFavoriteOnClickListener(onClickListener: View.OnClickListener)

    fun setFavoriteState(isFavorite: Boolean)

    fun updateFavoriteState(isFavorite: Boolean)
}