package com.icerrate.popularmovies.view.movies;

import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.source.MovieRepository;
import com.icerrate.popularmovies.view.common.BaseCallback;
import com.icerrate.popularmovies.view.common.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Ivan Cerrate.
 */

public class MoviesCatalogPresenter extends BasePresenter<MoviesCatalogView> {

    public enum SortType {
        MOST_POPULAR,
        HIGHEST_RATED,
        FAVORITE
    }

    private SortType sortType;

    private PaginatedResponse<Movie> moviesPaginatedResponse = new PaginatedResponse<>();

    private MovieRepository movieRepository;

    public MoviesCatalogPresenter(MoviesCatalogView view, MovieRepository movieRepository) {
        super(view);
        this.movieRepository = movieRepository;
    }

    public void loadNextMoviesPage() {
        view.showFooterProgress(true);
        boolean isLastPage = moviesPaginatedResponse.isLastPage();
        if (!isLastPage) {
            getMovies(false);
        } else {
            view.showFooterProgress(false);
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
            case FAVORITE:
                getInternalFavoriteMovies(force);
                break;
        }
    }

    private void getInternalPopularMovies(final boolean force) {
        Integer currentPage = moviesPaginatedResponse.getPage() != null ? moviesPaginatedResponse.getPage()+1 : 1;
        movieRepository.getPopularMovies(currentPage, new BaseCallback<PaginatedResponse<Movie>>() {
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
                showMovies(response.getResults(), getStringRes(R.string.movies_no_data));
                finishLoading();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                if (moviesPaginatedResponse.getResults().isEmpty()) {
                    view.showNoDataView(true, getStringRes(R.string.movies_no_data));
                }
                finishLoading();
            }
        });
    }

    private void getInternalTopRatedMovies(final boolean force) {
        Integer currentPage = moviesPaginatedResponse.getPage() != null ? moviesPaginatedResponse.getPage()+1 : 1;
        movieRepository.getTopRatedMovies(currentPage, new BaseCallback<PaginatedResponse<Movie>>() {
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
                showMovies(response.getResults(), getStringRes(R.string.movies_no_data));
                finishLoading();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                if (moviesPaginatedResponse.getResults().isEmpty()) {
                    view.showNoDataView(true, getStringRes(R.string.movies_no_data));
                }
                finishLoading();
            }
        });
    }

    private void getInternalFavoriteMovies(final boolean force) {
        movieRepository.getFavoriteMovies(new BaseCallback<ArrayList<Movie>>() {
            @Override
            public void onSuccess(ArrayList<Movie> response) {
                if (force) {
                    resetMovies();
                }
                moviesPaginatedResponse.setMeta(1, response.size(), 1);
                moviesPaginatedResponse.getResults().addAll(response);
                showMovies(response, getStringRes(R.string.movies_no_data));
                finishLoading();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                if (moviesPaginatedResponse.getResults().isEmpty()) {
                    view.showNoDataView(true, getStringRes(R.string.movies_no_data));
                }
                finishLoading();
            }
        });
    }

    public void showMovies(ArrayList<Movie> movies, String noDataText) {
        if (movies != null && !movies.isEmpty()) {
            view.showNoDataView(false, null);
            view.showMovies(movies);
        } else {
            if (moviesPaginatedResponse.getResults().isEmpty()) {
                view.showNoDataView(true, noDataText);
            }
        }
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

    public void onMovieItemClick(final Movie movie) {
        movieRepository.isFavoriteMovie(movie.getId(), new BaseCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean isFavorite) {
                movie.setFavorite(isFavorite);
                view.goToMovieDetail(movie);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
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
