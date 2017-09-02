package com.icerrate.popularmovies.view.movies;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.source.MovieDataSource;
import com.icerrate.popularmovies.view.common.BaseCallback;
import com.icerrate.popularmovies.view.common.BasePresenter;

/**
 * Created by Ivan Cerrate.
 */

public class MoviesCatalogPresenter extends BasePresenter<MoviesCatalogView> {

    public enum SortType {
        MOST_POPULAR,
        HIGHEST_RATED
    }

    private SortType sortType;

    private PaginatedResponse<Movie> moviesPaginatedResponse = new PaginatedResponse<>();

    private MovieDataSource movieDataSource;

    public MoviesCatalogPresenter(MoviesCatalogView view, MovieDataSource movieDataSource) {
        super(view);
        this.movieDataSource = movieDataSource;
    }

    public void loadNextMoviesPage() {
        view.showFooterProgress(true);
        boolean isLastPage = moviesPaginatedResponse.isLastPage();
        if (!isLastPage) {
            getMovies(false);
        }
    }

    public void loadMovies() {
        view.showMovies(moviesPaginatedResponse.getResults());
    }

    public void onSwipeMovies() {
        moviesPaginatedResponse = new PaginatedResponse<>();
        getMovies(true);
    }

    public void refreshMovies() {
        resetMovies();
        view.showProgressBar(true);
        moviesPaginatedResponse = new PaginatedResponse<>();
        getMovies(true);
    }

    private void getMovies(boolean force) {
        switch (sortType) {
            case MOST_POPULAR:
                getInternalPopularMovies(force);
                break;
            case HIGHEST_RATED:
                getInternalTopRatedMovies(force);
                break;
        }
    }

    private void getInternalPopularMovies(final boolean force) {
        Integer currentPage = moviesPaginatedResponse.getPage() != null ? moviesPaginatedResponse.getPage()+1 : 1;
        movieDataSource.getPopularMovie(currentPage, new BaseCallback<PaginatedResponse<Movie>>() {
            @Override
            public void onSuccess(PaginatedResponse<Movie> response) {
                if (force) {
                    resetMovies();
                }
                moviesPaginatedResponse.setMeta(
                        response.getPage(),
                        response.getTotalResults(),
                        response.getTotalPages());
                moviesPaginatedResponse.getResults().addAll(response.getResults());
                view.showMovies(response.getResults());
                view.showFooterProgress(false);
                view.showProgressBar(false);
                view.showRefreshLayout(false);
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                view.showFooterProgress(false);
                view.showProgressBar(false);
                view.showRefreshLayout(false);
            }
        });
    }

    private void getInternalTopRatedMovies(final boolean force) {
        Integer currentPage = moviesPaginatedResponse.getPage() != null ? moviesPaginatedResponse.getPage()+1 : 1;
        movieDataSource.getTopRatedMovie(currentPage, new BaseCallback<PaginatedResponse<Movie>>() {
            @Override
            public void onSuccess(PaginatedResponse<Movie> response) {
                if (force) {
                    resetMovies();
                }
                moviesPaginatedResponse.setMeta(
                        response.getPage(),
                        response.getTotalResults(),
                        response.getTotalPages());
                moviesPaginatedResponse.getResults().addAll(response.getResults());
                view.showMovies(response.getResults());
                finishLoading();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                finishLoading();
            }
        });
    }

    private void finishLoading() {
        view.showFooterProgress(false);
        view.showProgressBar(false);
        view.showRefreshLayout(false);
    }

    private void resetMovies() {
        view.resetMovies();
        moviesPaginatedResponse = new PaginatedResponse<>();
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public SortType getSortType() {
        return sortType;
    }

    public PaginatedResponse<Movie> getMoviesPaginatedResponse() {
        return moviesPaginatedResponse;
    }

    public void loadPresenterState(SortType sortType, PaginatedResponse<Movie> moviePaginatedResponse) {
        this.sortType = sortType;
        this.moviesPaginatedResponse = moviePaginatedResponse;
    }
}
