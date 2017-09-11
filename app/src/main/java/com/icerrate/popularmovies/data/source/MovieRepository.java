package com.icerrate.popularmovies.data.source;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.model.Review;
import com.icerrate.popularmovies.data.model.Trailer;
import com.icerrate.popularmovies.data.model.TrailersResponse;
import com.icerrate.popularmovies.view.common.BaseCallback;

import java.util.ArrayList;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Ivan Cerrate.
 */

public class MovieRepository implements MovieDataSource {

    private MovieDataSource localDataSource;

    private MovieDataSource remoteDataSource;

    private static MovieRepository INSTANCE = null;

    public MovieRepository(MovieDataSource localDataSource, MovieDataSource remoteDataSource) {
        this.localDataSource = checkNotNull(localDataSource);
        this.remoteDataSource = checkNotNull(remoteDataSource);
    }

    public static MovieRepository getInstance(MovieDataSource localDataSource, MovieDataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MovieRepository(localDataSource, remoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getPopularMovies(Integer page, BaseCallback<PaginatedResponse<Movie>> callback) {
        remoteDataSource.getPopularMovies(page, callback);
    }

    @Override
    public void getTopRatedMovies(Integer page, BaseCallback<PaginatedResponse<Movie>> callback) {
        remoteDataSource.getTopRatedMovies(page, callback);
    }

    @Override
    public void getMovieTrailers(Integer movieId, BaseCallback<TrailersResponse<Trailer>> callback) {
        remoteDataSource.getMovieTrailers(movieId, callback);
    }

    @Override
    public void getMovieReviews(Integer movieId, BaseCallback<PaginatedResponse<Review>> callback) {
        remoteDataSource.getMovieReviews(movieId, callback);
    }

    @Override
    public void getFavoriteMovies(BaseCallback<ArrayList<Movie>> callback) {
        localDataSource.getFavoriteMovies(callback);
    }

    @Override
    public void isFavoriteMovie(int movieId, BaseCallback<Boolean> callback) {
        localDataSource.isFavoriteMovie(movieId, callback);
    }

    @Override
    public void addFavoriteMovie(Movie movie, BaseCallback<Void> callback) {
        localDataSource.addFavoriteMovie(movie, callback);
    }

    @Override
    public void removeFavoriteMovie(int movieId, BaseCallback<Void> callback) {
        localDataSource.removeFavoriteMovie(movieId, callback);
    }
}
