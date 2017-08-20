package com.icerrate.popularmovies.provider.cloud.api;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Ivan Cerrate
 */
public interface MovieAPI {

    @GET(ServerPaths.POPULAR_MOVIES)
    Call<PaginatedResponse<Movie>> getPopularMovies();

    @GET(ServerPaths.TOP_RATED_MOVIES)
    Call<PaginatedResponse<Movie>> getTopRatedMovies();
}
