package com.icerrate.popularmovies.view.movies.detail;

import com.icerrate.popularmovies.view.common.BaseView;

/**
 * Created by Ivan Cerrate.
 */

public interface MovieDetailView extends BaseView {

    void showMovieDetail(String title, String releaseDate, String posterUrl, String rating, String synopsis);
}
