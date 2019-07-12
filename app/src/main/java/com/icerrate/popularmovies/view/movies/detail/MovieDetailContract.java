package com.icerrate.popularmovies.view.movies.detail;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.Review;
import com.icerrate.popularmovies.data.model.Trailer;
import com.icerrate.popularmovies.view.common.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Cerrate.
 */

public interface MovieDetailContract {

    interface View extends BaseView {

        void showHeader(String title, String releaseDate, String posterUrl,
                        String backdropUrl, String rating, String synopsis);

        void showTrailers(ArrayList<Trailer> trailers);

        void showTrailersNoData(boolean show);

        void showReviews(ArrayList<Review> reviews);

        void showReviewsNoData(boolean show);

        void showFavoriteState(boolean isFavorite);

        void updateFavoriteState(boolean isFavorite);

        void prepareTrailerShare(String trailerUrl);

        void showShareMenu(boolean show);
    }

    interface Presenter {

        void setMovieDetail(Movie movieDetail);

        void loadMovieDetails();

        void onFavoriteFabClicked();

        void onShareClick();

        void validateMenu();

        Movie getMovie();

        ArrayList<Trailer> getTrailers();

        ArrayList<Review> getReviews();

        void loadPresenterState(Movie movie, ArrayList<Trailer> trailers, ArrayList<Review> reviews);
    }
}
