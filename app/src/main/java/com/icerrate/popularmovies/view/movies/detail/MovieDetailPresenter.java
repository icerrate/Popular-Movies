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
import com.icerrate.popularmovies.view.common.BasePresenter;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public class MovieDetailPresenter extends BasePresenter<MovieDetailView> {

    private Movie movie;

    private ArrayList<Trailer> trailers;

    private ArrayList<Review> reviews;

    private MovieRepository movieRepository;

    public MovieDetailPresenter(MovieDetailView view, MovieRepository movieRepository) {
        super(view);
        this.movieRepository = movieRepository;
    }

    public void setMovieDetail(Movie movie) {
        this.movie = movie;
    }

    public void loadView() {
        //Start process
        loadMovie();
    }

    public void loadMovie() {
        String title = movie.getTitle();
        String releaseDate = FormatUtils.formatDate(movie.getReleaseDate(), FormatUtils.FORMAT_yyyy_MM_dd, FormatUtils.FORMAT_MMM_dd_yyyy);
        String posterUrl = movie.getPosterUrl("w342");
        String backdropUrl = movie.getBackdropUrl("w780");
        String rating = movie.getVoteAverage() + getStringRes(R.string.rating);
        String synopsis = movie.getOverview();
        boolean isFavorite = movie.isFavorite();
        view.showMovieDetail(title, releaseDate, posterUrl, backdropUrl, rating, synopsis);
        view.updateFavoriteIcon(isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        //Next steps
        loadTrailers();
        loadReviews();
    }

    public void loadTrailers() {
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
                view.showTrailersNoData(true);
            }
        });
    }

    private void showTrailers(ArrayList<Trailer> trailers) {
        if (trailers != null && !trailers.isEmpty()) {
            view.showTrailersNoData(false);
            view.showTrailers(trailers);
        } else {
            view.showTrailersNoData(true);
        }
    }

    public void loadReviews() {
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
                if (reviews != null && !reviews.isEmpty()) {
                    view.showReviewsNoData(false);
                    view.showReviews(response.getResults());
                } else {
                    view.showReviewsNoData(true);
                }
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

    public void onFavoriteFabClicked() {
        boolean isFavorite = movie.isFavorite();
        if (isFavorite) {
            movieRepository.removeFavoriteMovie(movie.getId(), new BaseCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    movie.setFavorite(false);
                    view.notifyUpdate();
                    view.updateFavoriteIcon(R.drawable.ic_favorite_border);
                    view.showSnackbarMessage(getStringRes(R.string.favorite_removed));
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });
        } else {
            movieRepository.addFavoriteMovie(movie, new BaseCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    movie.setFavorite(true);
                    view.notifyUpdate();
                    view.updateFavoriteIcon(R.drawable.ic_favorite);
                    view.showSnackbarMessage(getStringRes(R.string.favorite_added));
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });
        }
    }

    // State

    public Movie getMovie() {
        return movie;
    }

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void loadPresenterState(Movie movie, ArrayList<Trailer> trailers, ArrayList<Review> reviews) {
        this.movie = movie;
        this.trailers = trailers;
        this.reviews = reviews;
    }
}
