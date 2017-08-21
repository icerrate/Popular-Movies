package com.icerrate.popularmovies.data.source;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.provider.cloud.BaseService;
import com.icerrate.popularmovies.provider.cloud.ServerServiceRequest;
import com.icerrate.popularmovies.provider.cloud.ServiceRequest;
import com.icerrate.popularmovies.provider.cloud.api.MovieAPI;
import com.icerrate.popularmovies.view.common.BaseCallback;

import retrofit2.Call;

/**
 * Created by Ivan Cerrate
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
}
