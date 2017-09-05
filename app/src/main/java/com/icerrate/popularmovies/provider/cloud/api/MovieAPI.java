package com.icerrate.popularmovies.provider.cloud.api;

import com.icerrate.popularmovies.data.model.Movie;
import com.icerrate.popularmovies.data.model.PaginatedResponse;
import com.icerrate.popularmovies.data.model.Review;
import com.icerrate.popularmovies.data.model.Trailer;
import com.icerrate.popularmovies.data.model.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Ivan Cerrate
 */

public interface MovieAPI {

    @GET(ServerPaths.POPULAR_MOVIES)
    Call<PaginatedResponse<Movie>> getPopularMovies(@Query(ServerPaths.QUERY_PAGE) Integer page);

    @GET(ServerPaths.TOP_RATED_MOVIES)
    Call<PaginatedResponse<Movie>> getTopRatedMovies(@Query(ServerPaths.QUERY_PAGE) Integer page);

    @GET(ServerPaths.TRAILERS_MOVIES)
    Call<TrailersResponse<Trailer>> getMovieTrailers(@Path(ServerPaths.MOVIE_ID) Integer movieId);

    @GET(ServerPaths.REVIEWS)
    Call<PaginatedResponse<Review>> getMovieReviews(@Path(ServerPaths.MOVIE_ID) Integer movieId);
}
