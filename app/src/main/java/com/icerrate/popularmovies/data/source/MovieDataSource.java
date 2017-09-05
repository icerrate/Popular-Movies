package com.icerrate.popularmovies.data.source;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.model.Review;
import com.icerrate.popularmovies.data.model.Trailer;
import com.icerrate.popularmovies.data.model.TrailersResponse;
import com.icerrate.popularmovies.provider.cloud.BaseService;
import com.icerrate.popularmovies.provider.cloud.ServerServiceRequest;
import com.icerrate.popularmovies.provider.cloud.ServiceRequest;
import com.icerrate.popularmovies.provider.cloud.api.MovieAPI;
import com.icerrate.popularmovies.view.common.BaseCallback;

import retrofit2.Call;

/**
 * @author Ivan Cerrate
 */

public class MovieDataSource extends BaseService {

    private MovieAPI movieAPI;

    public MovieDataSource(MovieAPI movieAPI) {
        this.movieAPI = movieAPI;
    }

    public void getPopularMovie(Integer page, BaseCallback<PaginatedResponse<Movie>> callback) {
        final Call<PaginatedResponse<Movie>> call = movieAPI.getPopularMovies(page);
        ServiceRequest<PaginatedResponse<Movie>> serviceRequest = new ServerServiceRequest<>(call);
        enqueue(serviceRequest, callback);
    }

    public void getTopRatedMovie(Integer page, BaseCallback<PaginatedResponse<Movie>> callback) {
        final Call<PaginatedResponse<Movie>> call = movieAPI.getTopRatedMovies(page);
        ServiceRequest<PaginatedResponse<Movie>> serviceRequest = new ServerServiceRequest<>(call);
        enqueue(serviceRequest, callback);
    }

    public void getMovieTrailers(Integer movieId, BaseCallback<TrailersResponse<Trailer>> callback) {
        final Call<TrailersResponse<Trailer>> call = movieAPI.getMovieTrailers(movieId);
        ServiceRequest<TrailersResponse<Trailer>> serviceRequest = new ServerServiceRequest<>(call);
        enqueue(serviceRequest, callback);
    }

    public void getMovieReviews(Integer movieId, BaseCallback<PaginatedResponse<Review>> callback) {
        final Call<PaginatedResponse<Review>> call = movieAPI.getMovieReviews(movieId);
        ServiceRequest<PaginatedResponse<Review>> serviceRequest = new ServerServiceRequest<>(call);
        enqueue(serviceRequest, callback);
    }
}
