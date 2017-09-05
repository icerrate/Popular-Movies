package com.icerrate.popularmovies.view.movies.detail;

import com.icerrate.popularmovies.R;
import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.model.Review;
import com.icerrate.popularmovies.data.model.Trailer;
import com.icerrate.popularmovies.data.model.TrailersResponse;
import com.icerrate.popularmovies.data.source.MovieDataSource;
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

    private MovieDataSource movieDataSource;

    public MovieDetailPresenter(MovieDetailView view, MovieDataSource movieDataSource) {
        super(view);
        this.movieDataSource = movieDataSource;
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
        view.showMovieDetail(title, releaseDate, posterUrl, backdropUrl, rating, synopsis);
        //Next step
        loadTrailers();
    }

    public void loadTrailers() {
        resetTrailers();
        getInternalTrailers(true);
    }

    private void getInternalTrailers(final boolean force) {
        movieDataSource.getMovieTrailers(movie.getId(), new BaseCallback<TrailersResponse<Trailer>>() {
            @Override
            public void onSuccess(TrailersResponse<Trailer> response) {
                if (force) {
                    resetTrailers();
                }
                trailers = response.getResults();
                if (trailers != null && !trailers.isEmpty()) {
                    view.showTrailersNoData(false);
                    view.showTrailers(response.getResults());
                } else {
                    view.showTrailersNoData(true);
                }

                //Next step
                loadReviews();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
            }
        });
    }

    private void resetTrailers() {
        view.resetTrailers();
        trailers = new ArrayList<>();
    }

    public void loadReviews() {
        resetReviews();
        getInternalReviews(true);
    }

    private void getInternalReviews(final boolean force) {
        movieDataSource.getMovieReviews(movie.getId(), new BaseCallback<PaginatedResponse<Review>>() {
            @Override
            public void onSuccess(PaginatedResponse<Review> response) {
                if (force) {
                    resetReviews();
                }
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
            }
        });
    }

    private void resetReviews() {
        view.resetReviews();
        trailers = new ArrayList<>();
    }

    // State

    public Movie getMovie() {
        return movie;
    }

    public void loadPresenterState(Movie movie) {
        this.movie = movie;
    }
}
