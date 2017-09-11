package com.icerrate.popularmovies.data.source.remote;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.model.Review;
import com.icerrate.popularmovies.data.model.Trailer;
import com.icerrate.popularmovies.data.model.TrailersResponse;
import com.icerrate.popularmovies.data.source.MovieDataSource;
import com.icerrate.popularmovies.provider.cloud.BaseService;
import com.icerrate.popularmovies.provider.cloud.ServerServiceRequest;
import com.icerrate.popularmovies.provider.cloud.ServiceRequest;
import com.icerrate.popularmovies.provider.cloud.api.MovieAPI;
import com.icerrate.popularmovies.view.common.BaseCallback;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * @author Ivan Cerrate
 */

public class MovieRemoteDataSource extends BaseService implements MovieDataSource {

    private MovieAPI movieAPI;

    public MovieRemoteDataSource(MovieAPI movieAPI) {
        this.movieAPI = movieAPI;
    }

    @Override
    public void getPopularMovies(Integer page, BaseCallback<PaginatedResponse<Movie>> callback) {
        final Call<PaginatedResponse<Movie>> call = movieAPI.getPopularMovies(page);
        ServiceRequest<PaginatedResponse<Movie>> serviceRequest = new ServerServiceRequest<>(call);
        enqueue(serviceRequest, callback);
    }

    @Override
    public void getTopRatedMovies(Integer page, BaseCallback<PaginatedResponse<Movie>> callback) {
        final Call<PaginatedResponse<Movie>> call = movieAPI.getTopRatedMovies(page);
        ServiceRequest<PaginatedResponse<Movie>> serviceRequest = new ServerServiceRequest<>(call);
        enqueue(serviceRequest, callback);
    }

    @Override
    public void getMovieTrailers(Integer movieId, BaseCallback<TrailersResponse<Trailer>> callback) {
        final Call<TrailersResponse<Trailer>> call = movieAPI.getMovieTrailers(movieId);
        ServiceRequest<TrailersResponse<Trailer>> serviceRequest = new ServerServiceRequest<>(call);
        enqueue(serviceRequest, callback);
    }

    @Override
    public void getMovieReviews(Integer movieId, BaseCallback<PaginatedResponse<Review>> callback) {
        final Call<PaginatedResponse<Review>> call = movieAPI.getMovieReviews(movieId);
        ServiceRequest<PaginatedResponse<Review>> serviceRequest = new ServerServiceRequest<>(call);
        enqueue(serviceRequest, callback);
    }

    @Override
    public void getFavoriteMovies(BaseCallback<ArrayList<Movie>> callback) {
        throw new UnsupportedOperationException("Operation is not available!");
    }

    @Override
    public void isFavoriteMovie(int movieId, BaseCallback<Boolean> callback) {
        throw new UnsupportedOperationException("Operation is not available!");
    }

    @Override
    public void addFavoriteMovie(Movie movie, BaseCallback<Void> callback) {
        throw new UnsupportedOperationException("Operation is not available!");
    }

    @Override
    public void removeFavoriteMovie(int movieId, BaseCallback<Void> callback) {
        throw new UnsupportedOperationException("Operation is not available!");
    }
}
