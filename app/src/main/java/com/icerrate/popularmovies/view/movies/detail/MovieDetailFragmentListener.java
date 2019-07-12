package com.icerrate.popularmovies.view.movies.detail;

import android.view.View;

/**
 * @author Ivan Cerrate.
 */

public interface MovieDetailFragmentListener {

    void setCollapsingTitle(String title);

    void setBackdropImage(String backdropUrl);

    void setFavoriteOnClickListener(View.OnClickListener onClickListener);

    void setFavoriteState(boolean isFavorite);

    void updateFavoriteState(boolean isFavorite);
}
