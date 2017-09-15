package com.icerrate.popularmovies.view.movies;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.view.common.BaseView;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public interface MoviesCatalogView extends BaseView {

    void resetMovies();

    void showMovies(ArrayList<Movie> movies);

    void showNoDataView(boolean show, String noDataText);

    void showFooterProgress(boolean show);

    void goToMovieDetail(Movie movie);
}
