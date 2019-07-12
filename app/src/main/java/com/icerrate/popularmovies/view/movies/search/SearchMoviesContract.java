package com.icerrate.popularmovies.view.movies.search;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.model.Review;
import com.icerrate.popularmovies.data.model.Trailer;
import com.icerrate.popularmovies.view.common.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Cerrate.
 */

public interface SearchMoviesContract {

    interface View {

        void showProgressBar(boolean show);

        void showError(String errorMessage);

        void resetMovies();

        void showMovies(List<Movie> movies);

        void showSearchHint(boolean show);

        void showNoDataView(boolean show);

        void showFooterProgress(boolean show);

        void goToMovieDetail(Movie movie);
    }

    interface Presenter {

        void searchMovies(String query);

        void loadNextMoviesPage();

        void loadMovies();

        void onMovieItemClick(Movie movie);

        String getQuery();

        PaginatedResponse<Movie> getMoviesPaginatedResponse();

        void loadPresenterState(String query, PaginatedResponse<Movie> moviePaginatedResponse);
    }
}
