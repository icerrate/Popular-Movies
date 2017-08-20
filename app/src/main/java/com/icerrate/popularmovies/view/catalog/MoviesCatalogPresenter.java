package com.icerrate.popularmovies.view.catalog;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.source.MovieDataSource;
import com.icerrate.popularmovies.view.common.BaseCallback;
import com.icerrate.popularmovies.view.common.BasePresenter;

import static com.icerrate.popularmovies.view.catalog.MoviesCatalogPresenter.SortType.POPULARITY;

/**
 * Created by Ivan Cerrate.
 */

public class MoviesCatalogPresenter extends BasePresenter<MoviesCatalogView> {

    public enum SortType {
        POPULARITY,
        TOP_RATED
    }

    private SortType sortType;
    private PaginatedResponse<Movie> moviesPaginatedResponse = new PaginatedResponse<>();

    private MovieDataSource movieDataSource;

    public MoviesCatalogPresenter(MoviesCatalogView view, MovieDataSource movieDataSource) {
        super(view);
        this.movieDataSource = movieDataSource;
    }

    public void loadMovies() {
        view.resetMovies();
        if (sortType == null) {
            view.showProgressBar();
            sortType = POPULARITY;
        }
        getMovies(false);
    }

    public void loadMovies(SortType sortType) {
        view.resetMovies();
        view.showProgressBar();
        this.sortType = sortType;
        getMovies(false);
    }

    public void refreshMovies(SortType sortType) {
        this.sortType = sortType;
        getMovies(true);
    }

    private void getMovies(boolean force) {
        switch (sortType) {
            case POPULARITY:
                getInternalPopularMovies(force);
                break;
            case TOP_RATED:
                getInternalTopRatedMovies(force);
                break;
        }
    }

    private void getInternalPopularMovies(final boolean force) {
        movieDataSource.getPopularMovie(new BaseCallback<PaginatedResponse<Movie>>() {
            @Override
            public void onSuccess(PaginatedResponse<Movie> moviePaginatedResponse) {
                if (force) {
                    view.resetMovies();
                }
                view.showMovies(moviePaginatedResponse.getResults());
                view.hideProgressBar();
                view.hideRefreshLayout();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                view.hideProgressBar();
                view.hideRefreshLayout();
            }
        });
    }

    private void getInternalTopRatedMovies(final boolean force) {
        movieDataSource.getTopRatedMovie(new BaseCallback<PaginatedResponse<Movie>>() {
            @Override
            public void onSuccess(PaginatedResponse<Movie> moviePaginatedResponse) {
                if (force) {
                    view.resetMovies();
                }
                view.showMovies(moviePaginatedResponse.getResults());
                view.hideProgressBar();
                view.hideRefreshLayout();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                view.hideProgressBar();
                view.hideRefreshLayout();
            }
        });
    }

    public SortType getSortType() {
        return sortType;
    }

    public MovieDataSource getMovieDataSource() {
        return movieDataSource;
    }

    public void loadPresenterState(SortType sortType, PaginatedResponse<Movie> moviePaginatedResponse) {
        this.sortType = sortType;
        this.moviesPaginatedResponse = moviePaginatedResponse;
    }
}
