package com.icerrate.popularmovies.view.movies.catalog;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.view.common.BaseView;
import com.icerrate.popularmovies.view.movies.catalog.MoviesCatalogPresenter.SortType;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public interface MoviesCatalogContract {

    interface View extends BaseView {

        void resetMovies();

        void showMovies(ArrayList<Movie> movies);

        void showNoDataView(boolean show);

        void showFooterProgress(boolean show);

        void goToMovieDetail(Movie movie);
    }

    interface Presenter {

        void loadMovies();

        void loadMoviesBySortType(SortType sortType);

        void loadNextMoviesPage();

        void refreshMovies();

        void refreshMoviesBySortType(SortType sortType);

        void onBackFromDetail();

        void onMovieItemClick(Movie movie);

        SortType getSortType();

        PaginatedResponse<Movie> getMoviesPaginatedResponse();

        void loadPresenterState(SortType sortType, PaginatedResponse<Movie> response);
    }
}
