package com.icerrate.popularmovies.data.source;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.model.Review;
import com.icerrate.popularmovies.data.model.Trailer;
import com.icerrate.popularmovies.data.model.TrailersResponse;
import com.icerrate.popularmovies.view.common.BaseCallback;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate.
 */

public interface MovieDataSource {

    void getPopularMovies(Integer page, BaseCallback<PaginatedResponse<Movie>> callback);

    void getTopRatedMovies(Integer page, BaseCallback<PaginatedResponse<Movie>> callback);

    void searchMovies(String quey, Integer page, BaseCallback<PaginatedResponse<Movie>> callback);

    void getMovieTrailers(Integer movieId, BaseCallback<TrailersResponse<Trailer>> callback);

    void getMovieReviews(Integer movieId, BaseCallback<PaginatedResponse<Review>> callback);

    void getFavoriteMovies(BaseCallback<ArrayList<Movie>> callback);

    void isFavoriteMovie(int movieId, BaseCallback<Boolean> callback);

    void addFavoriteMovie(Movie movie, BaseCallback<Void> callback);

    void removeFavoriteMovie(int movieId, BaseCallback<Void> callback);
}
