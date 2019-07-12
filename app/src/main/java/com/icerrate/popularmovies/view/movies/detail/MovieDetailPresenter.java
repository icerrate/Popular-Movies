package com.icerrate.popularmovies.view.movies.detail;

import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.model.Review;
import com.icerrate.popularmovies.data.model.Trailer;
import com.icerrate.popularmovies.data.model.TrailersResponse;
import com.icerrate.popularmovies.data.source.MovieRepository;
import com.icerrate.popularmovies.utils.FormatUtils;
import com.icerrate.popularmovies.view.common.BaseCallback;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    private final static String POSTER_CODE = "w342";

    private final static String BACKDROP_CODE = "w780";

    private MovieDetailContract.View view;

    private Movie movie;

    private ArrayList<Trailer> trailers;

    private ArrayList<Review> reviews;

    private MovieRepository movieRepository;

    public MovieDetailPresenter(MovieDetailContract.View view, MovieRepository movieRepository) {
        this.view = view;
        this.movieRepository = movieRepository;
    }

    @Override
    public void setMovieDetail(Movie movie) {
        this.movie = movie;
    }

    @Override
    public void loadMovieDetails() {
        loadHeader();
        loadTrailers();
        loadReviews();
    }

    private void loadHeader() {
        String title = movie.getTitle();
        String releaseDate = FormatUtils.formatDate(movie.getReleaseDate(), FormatUtils.FORMAT_yyyy_MM_dd, FormatUtils.FORMAT_MMM_dd_yyyy);
        String posterUrl = movie.getPosterUrl(POSTER_CODE);
        String backdropUrl = movie.getBackdropUrl(BACKDROP_CODE);
        String rating = String.valueOf(movie.getVoteAverage());
        String synopsis = movie.getOverview();
        boolean isFavorite = movie.isFavorite();
        view.showHeader(title, releaseDate, posterUrl, backdropUrl, rating, synopsis);
        view.showFavoriteState(isFavorite);
    }

    private void loadTrailers() {
        if (trailers == null) {
            getInternalTrailers();
        } else {
            showTrailers(trailers);
        }
    }

    private void getInternalTrailers() {
        movieRepository.getMovieTrailers(movie.getId(), new BaseCallback<TrailersResponse<Trailer>>() {
            @Override
            public void onSuccess(TrailersResponse<Trailer> response) {
                trailers = response.getResults();
                showTrailers(trailers);
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                view.showShareMenu(false);
                view.showTrailersNoData(true);
            }
        });
    }

    private void showTrailers(ArrayList<Trailer> trailers) {
        if (trailers != null && !trailers.isEmpty()) {
            view.showTrailersNoData(false);
            view.showTrailers(trailers);
            view.showShareMenu(true);
        } else {
            view.showTrailersNoData(true);
            view.showShareMenu(false);
        }
    }

    private void loadReviews() {
        if (reviews == null) {
            getInternalReviews();
        } else {
            showReviews(reviews);
        }
    }

    private void getInternalReviews() {
        movieRepository.getMovieReviews(movie.getId(), new BaseCallback<PaginatedResponse<Review>>() {
            @Override
            public void onSuccess(PaginatedResponse<Review> response) {
                reviews = response.getResults();
                showReviews(reviews);
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                view.showReviewsNoData(true);
            }
        });
    }

    private void showReviews(ArrayList<Review> reviews) {
        if (reviews != null && !reviews.isEmpty()) {
            view.showReviewsNoData(false);
            view.showReviews(reviews);
        } else {
            view.showReviewsNoData(true);
        }
    }

    @Override
    public void onFavoriteFabClicked() {
        boolean isFavorite = movie.isFavorite();
        if (isFavorite) {
            movieRepository.removeFavoriteMovie(movie.getId(), new BaseCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    movie.setFavorite(false);
                    view.updateFavoriteState(false);
                    view.showSnackbarMessage(R.string.favorite_removed);
                }

                @Override
                public void onFailure(String errorMessage) {
                    view.showError(errorMessage);
                }
            });
        } else {
            movieRepository.addFavoriteMovie(movie, new BaseCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    movie.setFavorite(true);
                    view.updateFavoriteState(true);
                    view.showSnackbarMessage(R.string.favorite_added);
                }

                @Override
                public void onFailure(String errorMessage) {
                    view.showError(errorMessage);
                }
            });
        }
    }

    @Override
    public void onShareClick() {
        boolean hasTrailers = trailers != null && !trailers.isEmpty();
        if (hasTrailers) {
            view.prepareTrailerShare(trailers.get(0).getVideoUrl());
        }
    }

    @Override
    public void validateMenu() {
        boolean hasTrailers = trailers != null && !trailers.isEmpty();
        view.showShareMenu(hasTrailers);
    }

    // Presenter State

    @Override
    public Movie getMovie() {
        return movie;
    }

    @Override
    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }

    @Override
    public ArrayList<Review> getReviews() {
        return reviews;
    }

    @Override
    public void loadPresenterState(Movie movie, ArrayList<Trailer> trailers, ArrayList<Review> reviews) {
        this.movie = movie;
        this.trailers = trailers;
        this.reviews = reviews;
    }
}
