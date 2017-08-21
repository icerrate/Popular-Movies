package com.icerrate.popularmovies.provider.cloud.api;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ivan Cerrate
 */
public interface MovieAPI {

    @GET(ServerPaths.POPULAR_MOVIES)
    Call<PaginatedResponse<Movie>> getPopularMovies(@Query(ServerPaths.QUERY_PAGE) Integer page);

    @GET(ServerPaths.TOP_RATED_MOVIES)
    Call<PaginatedResponse<Movie>> getTopRatedMovies(@Query(ServerPaths.QUERY_PAGE) Integer page);
}
