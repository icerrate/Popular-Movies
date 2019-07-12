package com.icerrate.popularmovies.view.movies.catalog;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.source.MovieRepository;
import com.icerrate.popularmovies.view.common.BaseCallback;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public class MoviesCatalogPresenter implements MoviesCatalogContract.Presenter {

    private final static int PAGINATION = 1;

    public enum SortType {
        MOST_POPULAR,
        HIGHEST_RATED,
        FAVORITE
    }

    private SortType sortType = SortType.MOST_POPULAR;

    private PaginatedResponse<Movie> moviesPaginatedResponse = new PaginatedResponse<>();

    private MoviesCatalogContract.View view;

    private MovieRepository movieRepository;

    public MoviesCatalogPresenter(MoviesCatalogContract.View view, MovieRepository movieRepository) {
        this.view = view;
        this.movieRepository = movieRepository;
    }

    @Override
    public void loadMovies() {
        if (moviesPaginatedResponse.getPage() == 0) {
            refreshMovies();
        } else {
            view.resetMovies();
            view.showMovies(moviesPaginatedResponse.getResults());
        }
    }

    @Override
    public void loadMoviesBySortType(SortType sortType) {
        this.sortType = sortType;
        view.showProgressBar(true);
        loadMovies();
    }

    @Override
    public void loadNextMoviesPage() {
        view.showFooterProgress(true);
        boolean isLastPage = moviesPaginatedResponse.isLastPage();
        if (isLastPage) {
            view.showFooterProgress(false);
        } else {
            getMovies(false);
        }
    }

    @Override
    public void refreshMovies() {
        moviesPaginatedResponse = new PaginatedResponse<>();
        getMovies(true);
    }

    @Override
    public void refreshMoviesBySortType(SortType sortType) {
        this.sortType = sortType;
        resetMovies();
        view.showProgressBar(true);
        getMovies(true);
    }

    @Override
    public void onBackFromDetail() {
        if (sortType == SortType.FAVORITE) {
            refreshMovies();
        }
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
        Integer page = moviesPaginatedResponse.getPage() != null ? moviesPaginatedResponse.getPage()+1 : 1;
        Integer currentPage = force ? 1 : page;
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
                showMovies(response.getResults());
                finishLoading();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                finishLoading();
            }
        });
    }

    private void getInternalTopRatedMovies(final boolean force) {
        Integer page = moviesPaginatedResponse.getPage() != null ? moviesPaginatedResponse.getPage()+1 : 1;
        Integer currentPage = force ? 1 : page;
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
                showMovies(response.getResults());
                finishLoading();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
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
                moviesPaginatedResponse.setMeta(PAGINATION, response.size(), PAGINATION);
                moviesPaginatedResponse.getResults().addAll(response);
                showMovies(response);
                finishLoading();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                finishLoading();
            }
        });
    }

    private void showMovies(ArrayList<Movie> movies) {
        if (movies != null && !movies.isEmpty()) {
            view.showNoDataView(false);
            view.showMovies(movies);
        } else {
            if (moviesPaginatedResponse.getResults().isEmpty()) {
                view.showNoDataView(true);
            }
        }
    }

    private void finishLoading() {
        view.showFooterProgress(false);
        view.showProgressBar(false);
        view.showRefreshLayout(false);
    }

    private void resetMovies() {
        moviesPaginatedResponse = new PaginatedResponse<>();
        view.resetMovies();
    }

    @Override
    public void onMovieItemClick(final Movie movie) {
        movieRepository.isFavoriteMovie(movie.getId(), new BaseCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean isFavorite) {
                movie.setFavorite(isFavorite);
                view.goToMovieDetail(movie);
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
            }
        });
    }

    @Override
    public SortType getSortType() {
        return sortType;
    }

    @Override
    public PaginatedResponse<Movie> getMoviesPaginatedResponse() {
        return moviesPaginatedResponse;
    }

    @Override
    public void loadPresenterState(SortType sortType,
                                   PaginatedResponse<Movie> moviePaginatedResponse) {
        this.sortType = sortType;
        this.moviesPaginatedResponse = moviePaginatedResponse;
    }
}
