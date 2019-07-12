package com.icerrate.popularmovies.view.movies.search;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.source.MovieRepository;
import com.icerrate.popularmovies.view.common.BaseCallback;

import java.util.ArrayList;

public class SearchMoviesPresenter implements SearchMoviesContract.Presenter {

    private final static int MIN_QUERY_LENGTH = 3;

    private SearchMoviesContract.View view;

    private MovieRepository movieRepository;

    private String query;

    private PaginatedResponse<Movie> moviesPaginatedResponse = new PaginatedResponse<>();

    SearchMoviesPresenter(SearchMoviesContract.View view, MovieRepository movieRepository) {
        this.view = view;
        this.movieRepository = movieRepository;
    }

    @Override
    public void searchMovies(String query) {
        boolean isValid = query.length() >= MIN_QUERY_LENGTH;
        if (isValid) {
            view.showProgressBar(true);
            this.query = query;
            searchInternalMovies(query, true);
        } else {
            resetMovies();
            view.showSearchHint(true);
        }
    }

    @Override
    public void loadNextMoviesPage() {
        view.showFooterProgress(true);
        boolean isLastPage = moviesPaginatedResponse.isLastPage();
        if (isLastPage) {
            view.showFooterProgress(false);
        } else {
            searchInternalMovies(query, false);
        }
    }

    private void searchInternalMovies(String query, final boolean force) {
        Integer page = moviesPaginatedResponse.getPage() != null ? moviesPaginatedResponse.getPage()+1 : 1;
        Integer currentPage = force ? 1 : page;
        movieRepository.searchMovies(query, currentPage, new BaseCallback<PaginatedResponse<Movie>>() {
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
                if (moviesPaginatedResponse.getResults().isEmpty()) {
                    view.showNoDataView(true);
                }
                finishLoading();
            }
        });
    }

    @Override
    public void loadMovies() {
        if (moviesPaginatedResponse.getPage() != 0) {
            view.resetMovies();
            view.showMovies(moviesPaginatedResponse.getResults());
        }
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
    }

    private void resetMovies() {
        moviesPaginatedResponse = new PaginatedResponse<>();
        view.resetMovies();
    }

    //Presenter State

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public PaginatedResponse<Movie> getMoviesPaginatedResponse() {
        return moviesPaginatedResponse;
    }

    @Override
    public void loadPresenterState(String query, PaginatedResponse<Movie> moviePaginatedResponse) {
        this.query = query;
        this.moviesPaginatedResponse = moviePaginatedResponse;
    }
}
